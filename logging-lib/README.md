# Async Logging Library

A standardized, high-performance logging library built on top of **Log4j2** and **LMAX Disruptor**.

## Features

- **Async Logging**: Uses LMAX Disruptor for low-latency, garbage-free asynchronous logging.
- **Distributed Tracing**: Automatically extracts or generates a `X-Trace-Id` for every request and propagates it via SLF4J MDC.
- **Dynamic Configuration**: Log files are automatically named based on the service name (`spring.application.name`).
- **Standardized Format**: Consistent log pattern across all services, including coloring for console output.
- **YAML Configuration**: Uses modern YAML syntax for Log4j2 configuration.

## Installation

Add the library to your `build.gradle` dependencies:

```groovy
dependencies {
    implementation project(':logging-lib')
}
```

## Configuration

The library automatically sets the default logging configuration (`classpath:log4j2-base.yaml`) if you haven't defined your own `logging.config`.

### Basic Usage
**Zero configuration required.** Just run your application!

### Extending Configuration

If you need to add custom loggers or override settings without repeating the entire configuration, you can use Log4j2's **Composite Configuration**.

1.  Create your own `log4j2.yaml` (or `log4j2-app.yaml`) in your service's resources.
2.  Define only your additions/overrides (e.g., specific package log levels).
3.  Point `logging.config` to **both** files, separated by a comma:

```properties
logging.config=classpath:log4j2-base.yaml,classpath:log4j2-app.yaml
```

Log4j2 will merge them, with the later files overriding the earlier ones.

### Service Name
To ensure your log file is named correctly, define the application name:

```properties
spring.application.name=your-service-name
```

If not specified, the log file will default to `logs/service.log`.

### Log Levels
You can also override log levels via properties (standard Spring Boot behavior):

```properties
logging.level.com.example=DEBUG
logging.level.root=INFO
```

## Output

### Console
Colorized output for development ease. Format:
```
TIME [LEVEL] [PID] [TRACE_ID] [THREAD] LOGGER - MESSAGE
```

### File
Rolling file appender located in `logs/` directory.
- Relies on daily rotation or 100MB size limit.
- Retains logs for 30 days.
