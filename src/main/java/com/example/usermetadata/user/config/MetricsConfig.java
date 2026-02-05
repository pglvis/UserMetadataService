package com.example.usermetadata.user.config;

import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    MeterBinder userMetrics() {
        return meterRegistry -> {};
    }
}
