package com.dsrts.xcopilot.service;

import com.dsrts.xcopilot.event.SettingsManagerPropertyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class SettingsManager implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsManager.class);
    public static final String KEY_XPLANE_HOME = "xplane.home";

    private final ApplicationEventPublisher applicationEventPublisher;

    private Map<String,Serializable> properties = new HashMap<>();
    private static File fileProperties = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"xcopilot.ser");

    public SettingsManager(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(fileProperties.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(fileProperties);
                XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);
                synchronized (properties) {
                    properties = (Map<String,Serializable>)xmlDecoder.readObject();
                }
                properties.keySet().forEach(s -> publishEvent(s));
            } catch (IOException e) {
                LOGGER.warn("init<>",e);
            }
        }
    }

    private void publishEvent(String key) {
        applicationEventPublisher.publishEvent(new SettingsManagerPropertyEvent(key));
    }

    public void setProperty(String key,Serializable value) {
        synchronized (properties) {
            properties.put(key,value);
            persistProperties();
            publishEvent(key);
        }
    }

    public <T extends Serializable> T getProperty(String key) {
        return (T)properties.get(key);
    }

    private void persistProperties() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileProperties);
            XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream);
            synchronized (properties) {
                xmlEncoder.writeObject(properties);
                xmlEncoder.close();
            }
        } catch (IOException e) {
            LOGGER.warn("close()",e);
        }
    }
}
