package org.omer.logging.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Automatically sets the default logging configuration if not already set.
 * <p>
 * This processor runs early in the application startup and sets
 * {@code logging.config}
 * to {@code classpath:log4j2-base.yaml} if the property is missing.
 * </p>
 */
public class LoggingEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String LOGGING_CONFIG_PROPERTY = "logging.config";
    private static final String DEFAULT_LOGGING_CONFIG = "classpath:log4j2-base.yaml";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (!environment.containsProperty(LOGGING_CONFIG_PROPERTY)) {
            Map<String, Object> map = new HashMap<>();
            map.put(LOGGING_CONFIG_PROPERTY, DEFAULT_LOGGING_CONFIG);
            // Add as a low-priority property source so it can be overridden by other
            // sources if needed
            // though we checked containsProperty, adding it explicitly ensures it's
            // available.
            // Using addLast ensures command line or app properties (which are loaded) take
            // precedence if they existed but were null?
            // Actually, containsProperty checks all sources. If it's missing, we add it.
            // We add it to the end (lowest priority) or start?
            // Since we established it's missing, adding it anywhere works.
            // But to be "default", it implies lowest priority.
            environment.getPropertySources().addLast(new MapPropertySource("logging-lib-default", map));
        }
    }
}
