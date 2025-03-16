package com.waterboard.waterqualityprediction.repositories;

import com.waterboard.waterqualityprediction.models.analytics.AMEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface AMEventRepository extends JpaRepository<AMEvent, String> {
    @Query("SELECT COUNT(a) FROM AMEvent a WHERE a.createdAt BETWEEN :start AND :end " +
            "AND a.event = :event AND a.ip = :ip")
    long getCountForIP(@Param("start") Instant start,
                       @Param("end") Instant end,
                       @Param("event") String event,
                       @Param("ip") String ip);
}

