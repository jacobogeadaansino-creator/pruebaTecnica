package org.jacobo.pruebacapitole.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {
    @Bean(name = "appThreadPool")
    public ExecutorService threadPoolExecutor() {
        return Executors.newFixedThreadPool(20);
    }
}

