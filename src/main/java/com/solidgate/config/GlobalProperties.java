package com.solidgate.config;

import static java.util.Optional.ofNullable;

import java.util.List;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class GlobalProperties {

    private static GlobalProperties instance;

    public static CompositeConfiguration compositeConfiguration;

    private GlobalProperties() {
        try {
            compositeConfiguration = new CompositeConfiguration();
            compositeConfiguration.addConfiguration(new SystemConfiguration());
            compositeConfiguration.addConfiguration(new EnvironmentConfiguration());
            compositeConfiguration.addConfiguration(
                    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                            .configure(new Parameters().properties().setFileName(
                                    ofNullable(System.getProperty("property.file")).orElse(
                                            "testing.properties"))).getConfiguration());
        } catch (ConfigurationException ignored) {
            // ignored if it cannot load the testing.properties file
        }
    }

    public static synchronized GlobalProperties getInstance() {
        if (instance == null) {
            instance = new GlobalProperties();
        }
        return instance;
    }

    public String get(String key) {
        return compositeConfiguration.getString(key);
    }

    public String get(String key, String defaultValue) {
        return compositeConfiguration.getString(key, defaultValue);
    }

    public boolean getBoolean(String key, Boolean defaultValue) {
        return compositeConfiguration.getBoolean(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return compositeConfiguration.getLong(key, defaultValue);
    }

    public List<Object> getList(String key, char delimiter) {
        compositeConfiguration.setListDelimiterHandler(new DefaultListDelimiterHandler(delimiter));
        return compositeConfiguration.getList(key);
    }
}
