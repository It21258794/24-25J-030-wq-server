package com.waterboard.waterqualityprediction.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.waterboard.waterqualityprediction.models.prediction.*;
import com.waterboard.waterqualityprediction.repositories.RawWaterQualityDataRepository;
import com.waterboard.waterqualityprediction.repositories.WaterQualityParameterRepository;
import com.waterboard.waterqualityprediction.repositories.WeatherDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataService {
    @Autowired
    private RawWaterQualityDataRepository waterQualityRepository;

    @Autowired
    private WeatherDataRepository weatherRepository;

    @Autowired
    private WaterQualityParameterRepository waterQualityParameterRepository;


    public List<WaterQualityParameter> saveWaterQualityParameters(List<WaterQualityParameter> parameters) {
        return waterQualityParameterRepository.saveAll(parameters);
    }

    public void saveWaterQualityData(String jsonData) throws IOException {
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        List<RawWaterQualityData> waterQualityDataList = mapper.readValue(jsonData, mapper.getTypeFactory().constructCollectionType(List.class, RawWaterQualityData.class));
        waterQualityRepository.saveAll(waterQualityDataList);
    }

    public void saveWeatherData(String jsonData) throws IOException {
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        List<WeatherData> weatherDataList = mapper.readValue(jsonData, mapper.getTypeFactory().constructCollectionType(List.class, WeatherData.class));

        weatherRepository.saveAll(weatherDataList);
    }

    public Map<String, List<Map<String, Object>>> getMergedData(Instant startDate, Instant endDate, String center) {
        log.info("get merge past water quality and weather data for the center {}",center);
        List<RawWaterQualityData> waterDataList = waterQualityRepository.findByDateBetween(startDate, endDate);
        List<WeatherData> weatherDataList = weatherRepository.findByDateBetween(startDate, endDate);

        waterDataList = waterDataList.stream()
                .filter(waterData -> waterData.getCenterId().equals(center))
                .collect(Collectors.toList());

        weatherDataList = weatherDataList.stream()
                .filter(weatherData -> weatherData.getLocation().equals(center))
                .collect(Collectors.toList());

        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        for (RawWaterQualityData waterData : waterDataList) {
            String centerId = waterData.getCenterId();
            List<WqDataReading> waterReadings = waterData.getWqDataReadings();

            List<Map<String, Object>> mergedList = new ArrayList<>();

            for (WqDataReading wqReading : waterReadings) {
                Instant dateTime = wqReading.getDateTime();

                WeatherReading matchingWeather = weatherDataList.stream()
                        .flatMap(weather -> weather.getReadings().stream())
                        .filter(weatherReading -> weatherReading.getDateTime().equals(dateTime))
                        .findFirst()
                        .orElse(null);

                Map<String, Object> dataEntry = new LinkedHashMap<>();
                dataEntry.put("datetime", dateTime.toString());
                dataEntry.put("PH", wqReading.getPh());
                dataEntry.put("Conductivity", wqReading.getConductivity());
                dataEntry.put("Turbidity", wqReading.getTurbidity());

                if (matchingWeather != null) {
                    dataEntry.put("temp", matchingWeather.getTemp());
                    dataEntry.put("humidity", matchingWeather.getHumidity());
                    dataEntry.put("wind_speed", matchingWeather.getWindSpeed());
                    dataEntry.put("clouds", matchingWeather.getClouds());
                    dataEntry.put("feels_like", matchingWeather.getFeelsLike());
                    dataEntry.put("pressure", matchingWeather.getPressure());
                    dataEntry.put("dew_point", matchingWeather.getDewPoint());
                    dataEntry.put("visibility", matchingWeather.getVisibility());
                    dataEntry.put("wind_deg", matchingWeather.getWindDeg());
                    dataEntry.put("rainfall", matchingWeather.getRainfall());
                } else {
                    dataEntry.put("temp", null);
                    dataEntry.put("humidity", null);
                    dataEntry.put("wind_speed", null);
                    dataEntry.put("clouds", null);
                    dataEntry.put("feels_like", null);
                    dataEntry.put("pressure", null);
                    dataEntry.put("dew_point", null);
                    dataEntry.put("visibility", null);
                    dataEntry.put("wind_deg", null);
                    dataEntry.put("rainfall", null);
                }

                mergedList.add(dataEntry);
            }
            result.put(centerId, mergedList);
        }

        return result;
    }

}
