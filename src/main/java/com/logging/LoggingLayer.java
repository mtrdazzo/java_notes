package com.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class LoggingLayer {

    private static final String REPLACE_STR_KEY = "REPLACE_STR";
    private static final String LOGGER_CONFIG_KEY = "active";
    private static final String SUMMARY_APPENDER_KEY = "SUMMARY";
    private static final String ALL_TEST_APPENDER_KEY = "ALL";
    private static Configuration config;
    private static LoggerContext context;
    private static LoggerConfig logger_config;

    public void createSummaryLogFile(String name) {
        addAppender(createNewRollingFileAppender(name, SUMMARY_APPENDER_KEY));
    }

    public void createAllTestOutputLogfile(String name) {
        addAppender(createNewRollingFileAppender(name, ALL_TEST_APPENDER_KEY));
    }

    private RollingFileAppender createNewRollingFileAppender(String name, String appender) {
        RollingFileAppender def = getCurrentConfiguration().getAppender(appender);
        String file_name = def.getFileName().replace(REPLACE_STR_KEY, name);
        String file_pattern = def.getFilePattern().replace(REPLACE_STR_KEY, name);

        RollingFileAppender rolling = new RollingFileAppender.Builder<>()
                .setName(name)
                .withFileName(file_name)
                .setFilter(def.getFilter())
                .withFilePattern(file_pattern)
                .setLayout(def.getLayout())
                .withPolicy(def.getTriggeringPolicy())
                .setConfiguration(getCurrentConfiguration())
                .build();
        rolling.start();
        return rolling;
    }

    public void addAppender(AbstractOutputStreamAppender<?> appender) {
        LoggerConfig appLoggerConfig = getAppLoggerConfig();
        appLoggerConfig.addAppender(appender, appLoggerConfig.getLevel(), appender.getFilter());
        getCurrentContext().updateLoggers();
    }

    public void removeAppender(String name) {
        LoggerConfig appLoggerConfig = getAppLoggerConfig();
        appLoggerConfig.getAppenders().get(name).stop();
        appLoggerConfig.removeAppender(name);
    }

    public void addConsoleDebugOutput() {
        ConsoleAppender appender = getCurrentConfiguration().getAppender("console");
        addAppender(appender);
    }

    private LoggerContext getCurrentContext() {
        if (context == null) {
            context = (LoggerContext) LogManager.getContext(false);
        }
        return context;
    }

    private Configuration getCurrentConfiguration() {
        if (config == null) {
            config = getCurrentContext().getConfiguration();
        }
        return config;
    }

    private LoggerConfig getAppLoggerConfig() {
        if (logger_config == null) {
            logger_config = getCurrentConfiguration().getLoggers().get(LOGGER_CONFIG_KEY);
        }
        return logger_config;
    }
}