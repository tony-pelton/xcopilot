package com.dsrts.xcopilot.service;

import com.dsrts.xcopilot.event.XcopilotEvent;
import com.dsrts.xcopilot.model.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

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
        synchronized (properties) {
            properties.put("xplane.location",new GeoPoint(43.6424f,-70.3044f));
            if (fileProperties.exists()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(fileProperties);
                    XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);

                    Map<String, Serializable> loadedproperties = (Map<String, Serializable>) xmlDecoder.readObject();
                    properties.putAll(loadedproperties);
                    properties.keySet().forEach(s -> publishEvent(s, properties.get(s),this));
                } catch (IOException e) {
                    LOGGER.warn("init<>", e);
                }
            }
        }
    }

    private void publishEvent(String key,Object value,Object source) {
        applicationEventPublisher.publishEvent(new XcopilotEvent("publishproperty",singletonMap(key,value),source));
    }

    @EventListener(condition = "#xcopilotEvent.isEvent('persistproperty')")
    private void consumeEvent(XcopilotEvent xcopilotEvent) {
        setProperty(xcopilotEvent.getKey(),xcopilotEvent.getValue(),xcopilotEvent.getSource());
    }

    public void setProperty(String key,Serializable value,Object source) {
        synchronized (properties) {
            properties.put(key,value);
            publishEvent(key,value,source);
        }
    }

    public <T extends Serializable> T getProperty(String key) {
        return (T)properties.get(key);
    }

    @PreDestroy
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
