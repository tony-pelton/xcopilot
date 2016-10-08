package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RadioDataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RadioDataManager.class);

    private XPlaneConnectService xPlaneConnectService;
    private SettingsManager settingsManager;
    private ApplicationEventPublisher applicationEventPublisher;
    private RadioDataLoader radioDataLoader;

    private GeoPoint planePosition;

    public RadioDataManager(XPlaneConnectService xPlaneConnectService,
                            SettingsManager settingsManager,
                            ApplicationEventPublisher applicationEventPublisher) {
        this.xPlaneConnectService = xPlaneConnectService;
        this.settingsManager = settingsManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @EventListener(condition = "T(com.dsrts.xcopilot.SettingsManager).KEY_XPLANE_HOME.equals(#settingsManagerPropertyEvent.key)")
    protected void propertyListener(SettingsManagerPropertyEvent settingsManagerPropertyEvent) {
        radioDataLoader = new RadioDataLoader(new File(settingsManager.<String>getProperty(SettingsManager.KEY_XPLANE_HOME)));
        update();
    }

    @Scheduled(fixedRate = 10000)
    protected void update() {
        if (null != radioDataLoader) {
            if(null == planePosition || planePosition.distanceToNM(xPlaneConnectService.getPlanePosition()) > 10.0) {
                planePosition = xPlaneConnectService.getPlanePosition();
                loadFilteredRadioData();
            }
        }
    }

    private void loadFilteredRadioData() {
        List<NavDataPoint> distanceFilteredNavDataPointList = radioDataLoader.getRadioNavDataList().stream()
                .filter(n -> planePosition.distanceToNM(n) < 200.0)
                .collect(Collectors.toList());
        applicationEventPublisher.publishEvent(new DataLoadEvent(Collections.unmodifiableList(distanceFilteredNavDataPointList)));
    }

    public static class DataLoadEvent {
        private List<NavDataPoint> navDataPoints;
        public DataLoadEvent(List<NavDataPoint> navDataPoints) {
            this.navDataPoints = navDataPoints;
        }
        public List<NavDataPoint> getNavDataPoints() {
            return navDataPoints;
        }
    }
}
