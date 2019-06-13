package com.creditsuisse.orderbook.metrics;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.core.instrument.Meter;

/**
 * Metrics configuration class for providing custom metrics regarding processing time and percentiles.
 * @author afernandeza
 *
 */
@Configuration
public class OrderBookMetricsConfig {

    private static final Duration HISTOGRAM_EXPIRY = Duration.ofMinutes(10);

    private static final Duration STEP = Duration.ofSeconds(5);

    @Value("${order-book-app.metrics.host}")
    private String hostId;

    @Value("${order-book-app.metrics.service}")
    private String serviceId;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
            .commonTags("host", hostId, "service", serviceId)
            .meterFilter(MeterFilter.deny(id -> {
                String uri = id.getTag("uri");
                return (uri != null && uri.startsWith("/swagger") && uri.startsWith("/prometheus"));
            }))
            .meterFilter(new MeterFilter() {
                @Override
                public DistributionStatisticConfig configure(Meter.Id id,
                                                             DistributionStatisticConfig config) {
                    return config.merge(DistributionStatisticConfig.builder()
                            .percentilesHistogram(true)
                            .percentiles(0.5, 0.75, 0.95)
                            .expiry(HISTOGRAM_EXPIRY)
                            .bufferLength((int) (HISTOGRAM_EXPIRY.toMillis() / STEP.toMillis()))
                            .build());
                }
            });
    }

}