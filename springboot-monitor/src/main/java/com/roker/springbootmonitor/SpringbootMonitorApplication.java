package com.roker.springbootmonitor;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
public class SpringbootMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMonitorApplication.class, args);
    }

}
