package com.waterboard.waterqualityprediction.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.waterboard.waterqualityprediction.PaginationDto;
import com.waterboard.waterqualityprediction.PredictionModuleConfig;
import com.waterboard.waterqualityprediction.UIConfigs;
import com.waterboard.waterqualityprediction.dto.PredictionRequest;
import com.waterboard.waterqualityprediction.dto.PredictionResponse;
import com.waterboard.waterqualityprediction.dto.PredictionResult;
import com.waterboard.waterqualityprediction.dto.WeatherAndPredictionDTO;
import com.waterboard.waterqualityprediction.models.prediction.WaterQualityParameter;
import com.waterboard.waterqualityprediction.models.prediction.WeatherData;
import com.waterboard.waterqualityprediction.models.prediction.WeatherReading;
import com.waterboard.waterqualityprediction.modles.Prediction;
import com.waterboard.waterqualityprediction.repositories.PredictionRepository;
import com.waterboard.waterqualityprediction.repositories.WaterQualityParameterRepository;
import com.waterboard.waterqualityprediction.repositories.WeatherDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PredictionService {
    @Autowired
    private PredictionRepository predictionRepository;

    @Autowired
    private UIConfigs uiConfigs;

    @Autowired
    private DataService dataService;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private PredictionModuleConfig predictionModuleConfig;

    @Autowired
    private WaterQualityParameterRepository waterQualityParameterRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public void fetchAndStorePredictions(int days, String uuid) {
        log.info("Fetching predictions for {} days and storing, UUID = {}", days, uuid);
        String startDateStr = "2025-02-04T00:00:00";
        String endDateStr = "2025-02-05T16:00:00";

        Instant startDate = LocalDateTime.parse(startDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(ZoneOffset.UTC)
                .toInstant();

        Instant endDate = LocalDateTime.parse(endDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(ZoneOffset.UTC)
                .toInstant();

        List<Map<String, Object>> center1Data = dataService.getMergedData(startDate, endDate, "center_1").get("center_1");
        List<Map<String, Object>> center2Data = dataService.getMergedData(startDate, endDate, "center_2").get("center_2");
        List<Map<String, Object>> center3Data = dataService.getMergedData(startDate, endDate, "center_3").get("center_3");

        Function<List<Map<String, Object>>, List<Map<String, Object>>> formatData = data -> data.stream()
                .map(entry -> {
                    Map<String, Object> formattedEntry = new LinkedHashMap<>(entry);
                    if (entry.containsKey("datetime")) {
                        Instant instant = Instant.parse(entry.get("datetime").toString());
                        formattedEntry.put("datetime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                .withZone(ZoneOffset.UTC)
                                .format(instant));
                    }
                    return formattedEntry;
                })
                .toList();

        Map<String, List<Map<String, Object>>> mergedData = new HashMap<>();
        mergedData.put("center1", formatData.apply(center1Data));
        mergedData.put("center2", formatData.apply(center2Data));
        mergedData.put("center3", formatData.apply(center3Data));

        String apiUrl = uiConfigs.getPredictionUrl();
        String finalUrl = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("prediction_period", days)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, List<Map<String, Object>>>> requestEntity = new HttpEntity<>(mergedData, headers);
        ResponseEntity<PredictionResponse> responseEntity = restTemplate.exchange(
                finalUrl, HttpMethod.POST, requestEntity, PredictionResponse.class);

        PredictionResponse response = responseEntity.getBody();

        if (response != null && response.getPredictions() != null) {
            List<Prediction> predictions = response.getPredictions();

            for (Prediction prediction : predictions) {
                Instant predictionDate = LocalDate.parse(prediction.getDate(), DateTimeFormatter.ISO_LOCAL_DATE)
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant();

                if (!predictionRepository.existsByPredictedDate(predictionDate)) {
                    prediction.setPredictedDate(predictionDate);
                    predictionRepository.save(prediction);
                }
            }
        }
    }

    public PredictionResult getPredictions(PredictionRequest predictionRequest){
        log.info("Fetching predictions for the {} days for parameter: {} isPast {}", predictionRequest.getDays(), predictionRequest.getDays(), predictionRequest.getIsPast());

        List<Prediction> predictions = predictionRepository.findPredictionsByDays(predictionRequest.getDays(),predictionRequest.getIsPast());

        List<String> dates = predictions.stream()
                .map(prediction -> prediction.getPredictedDate().toString())
                .collect(Collectors.toList());

        List<Double> values = predictions.stream()
                .map(prediction -> getParameterValue(prediction, predictionRequest.getParameter()))
                .collect(Collectors.toList());

        return PredictionResult.initPredictionResult(dates,values);
    }

    private double getParameterValue(Prediction prediction, String parameter) {
        return switch (parameter.toLowerCase()) {
            case "ph" -> prediction.getAvgPh();
            case "conductivity" -> prediction.getAvgConductivity();
            case "turbidity" -> prediction.getAvgTurbidity();
            default -> throw new IllegalArgumentException("Invalid parameter: " + parameter);
        };
    }

    public void fetchAndStoreWeatherData(int days, String uuid) {
        log.info("Fetching weather predictions for {} days and storing, UUID = {}", days, uuid);

        for (int i = 0; i < days; i++) {
            Instant startOfDayUtc = LocalDate.now().plusDays(i)
                    .atStartOfDay(ZoneId.of("UTC"))
                    .toInstant();

            Optional<WeatherData> existingWeatherData = weatherDataRepository.findByDateAndLocation(startOfDayUtc, "center_1");

            if (existingWeatherData.isPresent()) {
                log.info("Data already exists for date: {}. Skipping.", startOfDayUtc);
                continue;
            }
            WeatherData weatherData = new WeatherData();
            weatherData.setDate(startOfDayUtc);
            weatherData.setLocation("center_1");

            WeatherReading dailyForecast = fetchWeatherForDay(startOfDayUtc);
            if (dailyForecast != null) {
                dailyForecast.setDateTime(startOfDayUtc);
                weatherData.setReadings(Collections.singletonList(dailyForecast));
            }

            weatherDataRepository.save(weatherData);
        }
    }

    private WeatherReading fetchWeatherForDay(Instant date) {
        String url = String.format("%s/onecall?lat=%f&lon=%f&exclude=hourly&appid=%s",
                predictionModuleConfig.getOpenWeatherUrl(),
                predictionModuleConfig.getOpenWeatherLatitude(),
                predictionModuleConfig.getOpenWeatherLongitude(),
                predictionModuleConfig.getOpenWeatherApiKey());

        try {
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
            log.info("Weather API Response: {}", response.getBody());
            JsonNode dailyNode = response.getBody().get("daily");

            if (dailyNode != null && dailyNode.isArray()) {
                for (JsonNode entry : dailyNode) {
                    long forecastTime = entry.get("dt").asLong();
                    Instant forecastInstant = Instant.ofEpochSecond(forecastTime);

                    LocalDate forecastLocalDate = forecastInstant.atZone(ZoneId.of("UTC")).toLocalDate();
                    LocalDate inputDate = date.atZone(ZoneId.of("UTC")).toLocalDate();

                    if (forecastLocalDate.isEqual(inputDate)) {
                        WeatherReading weatherReading = new WeatherReading();
                        Instant forecastDateTime = forecastLocalDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
                        weatherReading.setId(UUID.randomUUID().toString());
                        weatherReading.setDateTime(forecastDateTime);
                        weatherReading.setTemp(entry.get("temp").get("day").asDouble());
                        weatherReading.setFeelsLike(entry.get("feels_like").get("day").asDouble());
                        weatherReading.setPressure(entry.get("pressure").asInt());
                        weatherReading.setHumidity(entry.get("humidity").asInt());

                        JsonNode rainNode = entry.get("rain");
                        if (rainNode != null) {
                            double rainfall = rainNode.asDouble();
                            weatherReading.setRainfall(rainfall);
                        } else {
                            weatherReading.setRainfall(0);
                        }

                        return weatherReading;
                    }
                }
            }

            log.warn("No forecast found for the given date: {}", date);
            return null;
        } catch (Exception e) {
            log.error("Error fetching weather data", e);
            return null;
        }
    }

    public PaginationDto<WeatherAndPredictionDTO> getWeatherAndPredictions(
            String parameterName, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Instant today = Instant.now();
        Instant sevenDaysLater = today.plus(7, ChronoUnit.DAYS);

        Page<WeatherData> weatherPage = weatherDataRepository.findByDateBetween(today, sevenDaysLater, pageable);
        Page<Prediction> predictionPage = predictionRepository.findByPredictedDateBetween(today, sevenDaysLater, pageable);

        Optional<WaterQualityParameter> parameter = waterQualityParameterRepository.findByName(parameterName);
        double thresholdValue = parameter.map(WaterQualityParameter::getThreshold).orElse(0.0);

        List<WeatherAndPredictionDTO> responseList = new ArrayList<>();

        weatherPage.getContent().forEach(weatherData -> {
            predictionPage.getContent().stream()
                    .filter(prediction -> prediction.getPredictedDate().equals(weatherData.getDate()))
                    .findFirst()
                    .ifPresent(prediction -> responseList.add(WeatherAndPredictionDTO.init(weatherData, prediction, thresholdValue)));
        });

        return new PaginationDto<>(responseList, weatherPage);
    }

}
