package com.tvspb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Не удалось найти файл config.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static long getTimeout() {
        return Long.parseLong(getProperty("timeout"));
    }

    public static long getImplicitWait() {
        return Long.parseLong(getProperty("implicit.wait"));
    }

    public static long getExplicitWait() {
        return Long.parseLong(getProperty("explicit.wait"));
    }
}