package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.Date;
import java.util.Properties;

@Service
public class SettingsManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsManager.class);
    public static final String KEY_XPLANE_HOME = "xplane.home";

    private Properties properties = new Properties();
    private static File fileProperties = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"xcopilot.properties");

    public void setProperty(String key,String value) {
        properties.setProperty(key,value);
        persistProperties();
    }
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    private void persistProperties() {
        try {
            properties.storeToXML(new FileOutputStream(fileProperties),new Date().toString());
        } catch (IOException e) {
            LOGGER.warn("close()",e);
        }
    }
    @PostConstruct
    protected void open() {
        if(fileProperties.exists()) {
            try {
                properties.loadFromXML(new FileInputStream(fileProperties));
            } catch (IOException e) {
                LOGGER.warn("init<>",e);
            }
        }
    }
    @PreDestroy
    protected void close() {
        persistProperties();
    }
}
