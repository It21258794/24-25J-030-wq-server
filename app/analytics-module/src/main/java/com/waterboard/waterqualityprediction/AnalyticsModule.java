package com.waterboard.waterqualityprediction;

import com.waterboard.waterqualityprediction.commonExceptions.http.TooManyRequestsException;
import com.waterboard.waterqualityprediction.repositories.AMEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class AnalyticsModule {

    @Autowired
    private RequestDataProvider requestDataProvider;

    @Autowired
    private AMEventRepository amEventRepository;

    public void throwIfLimitExceedForIP(String event, int count, int duration) {
        Instant start = Instant.now();
        Instant end = start.minusSeconds(duration * 60);
        long dbCount = this.amEventRepository.getCountForIP(start, end, event, requestDataProvider.getClientIP());
        if(dbCount > count) {
            throw new TooManyRequestsException("too many requests. please try again later");
        }
    }
}
