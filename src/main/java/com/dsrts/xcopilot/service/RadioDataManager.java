package com.dsrts.xcopilot.service;

import com.dsrts.xcopilot.event.XcopilotEvent;
import com.dsrts.xcopilot.model.GeoPoint;
import com.dsrts.xcopilot.model.NavigationGeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RadioDataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RadioDataManager.class);

    private SettingsManager settingsManager;
    private ApplicationEventPublisher applicationEventPublisher;
    private RadioDataLoader radioDataLoader;

    private GeoPoint planePosition;

    public RadioDataManager(SettingsManager settingsManager,
                            ApplicationEventPublisher applicationEventPublisher) {
        this.settingsManager = settingsManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @EventListener(condition = "#settingsManagerPropertyEvent.isEvent('publishproperty') && 'xplane.home'.equals(#settingsManagerPropertyEvent.getKey())")
    protected void propertyListener(XcopilotEvent settingsManagerPropertyEvent) {
        radioDataLoader = new RadioDataLoader(new File(settingsManagerPropertyEvent.<String>getValue()));
        update(new GeoPoint(38.51513f,-122.81252f));
    }

    @EventListener(condition = "#receiveEvent.isEvent('telemetry')")
    protected void telemetryListener(XcopilotEvent receiveEvent) {
        update(new GeoPoint(
                receiveEvent.<Map<DREF,float[]>>getValue().get(DREF.SIM_FLIGHTMODEL_POSITION_LATITUDE)[0],
                receiveEvent.<Map<DREF,float[]>>getValue().get(DREF.SIM_FLIGHTMODEL_POSITION_LONGITUDE)[0]));
    }

    private void update(GeoPoint point) {
        if (null != radioDataLoader) {
            if(null == planePosition || planePosition.distanceToNM(point) > 10.0) {
                planePosition = point;
                LOGGER.info("update() : "+planePosition);
                loadFilteredRadioData();
            }
        }
    }

    private void loadFilteredRadioData() {
        List<NavigationGeoPoint> distanceFilteredNavigationGeoPointList = radioDataLoader.getRadioNavDataList().stream()
                .filter(n -> planePosition.distanceToNM(n) < 200.0)
                .collect(Collectors.toList());
        applicationEventPublisher.publishEvent(new DataLoadEvent(Collections.unmodifiableList(distanceFilteredNavigationGeoPointList)));
    }

    public static class DataLoadEvent {
        private List<NavigationGeoPoint> navigationGeoPoints;
        public DataLoadEvent(List<NavigationGeoPoint> navigationGeoPoints) {
            this.navigationGeoPoints = navigationGeoPoints;
        }
        public List<NavigationGeoPoint> getNavigationGeoPoints() {
            return navigationGeoPoints;
        }
    }
}
