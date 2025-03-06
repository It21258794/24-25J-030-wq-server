package com.waterboard.waterqualityprediction.models.analytics;

import com.waterboard.waterqualityprediction.AuditModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "am_analytics_events",
        indexes = {
                @Index(name = "idx_am_ae_createdAt_asc", columnList = "createdAt ASC"),
                @Index(name = "idx_am_ae_createdAt_desc", columnList = "createdAt DESC"),
                @Index(name = "idx_am_ae_event_asc", columnList = "event ASC"),
                @Index(name = "idx_am_ae_event_desc", columnList = "event DESC"),
                @Index(name = "idx_am_ae_ip_asc", columnList = "ip ASC"),
                @Index(name = "idx_am_ae_ip_desc", columnList = "ip DESC")
        })
public class AMEvent extends AuditModel {
    public enum Level {
        INFO, TRACE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String app;
    private String env;
    private String platform;
    private String version;
    private String device;
    private String ip;
    private String hash;
    private String event;
    private String type;

    @Enumerated(EnumType.STRING)
    private Level level = Level.INFO;

    private long duration;

    @ElementCollection
    @CollectionTable(name = "am_event_data", joinColumns = @JoinColumn(name = "event_id"))
    @MapKeyColumn(name = "data_key")
    @Column(name = "data_value")
    private Map<String, String> data = new HashMap<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public void setDataVars(Object... data) {
        Map<String, String> eventData = new HashMap<>();
        for (int i = 0; i < data.length; i += 2) {
            eventData.put(data[i].toString(), data[i + 1].toString());
        }
        this.data = eventData;
    }
}