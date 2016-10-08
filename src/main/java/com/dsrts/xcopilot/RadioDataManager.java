package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class RadioDataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RadioDataManager.class);

    private XPlaneConnectService xPlaneConnectService;
    private SettingsManager settingsManager;
    private ApplicationEventPublisher applicationEventPublisher;

    List<NavDataPoint> navDataPointList = new ArrayList<>();
    List<NavDataPoint> distanceFilteredNavDataPointList = new ArrayList<>();

    private String xplaneHome;

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
        xplaneHome = settingsManager.getProperty(SettingsManager.KEY_XPLANE_HOME);
        synchronized (navDataPointList) {
            distanceFilteredNavDataPointList.clear();
            navDataPointList = new RadioDataLoader().loadRadioData(xplaneHome);
            update();
        }
    }

    @Scheduled(fixedRate = 10000)
    protected void update() {
        synchronized (navDataPointList) {
            if(null == planePosition) {
                planePosition = checkNotNull(xPlaneConnectService.getPlanePosition());
            }
            if (navDataPointList.size() > 0) {
                if(distanceFilteredNavDataPointList.size() < 1 || planePosition.distanceToNM(xPlaneConnectService.getPlanePosition()) > 10.0) {
                    loadFilteredRadioData();
                }
            }
        }
    }

    private void loadFilteredRadioData() {
        distanceFilteredNavDataPointList = navDataPointList.stream()
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
