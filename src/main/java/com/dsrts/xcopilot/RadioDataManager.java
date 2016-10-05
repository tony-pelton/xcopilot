package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class RadioDataManager implements ApplicationEventPublisherAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(RadioDataManager.class);

    private XPlaneConnectService xPlaneConnectService;
    private SettingsManager settingsManager;
    private ApplicationEventPublisher applicationEventPublisher;

    List<NavDataPoint> navDataPointList = new ArrayList<>();
    List<NavDataPoint> distanceFilteredNavDataPointList = new ArrayList<>();

    private GeoPoint planePosition;

    public RadioDataManager(XPlaneConnectService xPlaneConnectService,
                            SettingsManager settingsManager,
                            ApplicationEventPublisher applicationEventPublisher) {
        this.xPlaneConnectService = xPlaneConnectService;
        this.settingsManager = settingsManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(fixedRate = 10000)
    protected void update() {
        synchronized (navDataPointList) {
            if(navDataPointList.size() < 1) {
                // load data list
                navDataPointList = Collections.unmodifiableList(new RadioDataLoader().loadRadioData(settingsManager));
            }
        }
        if(null == planePosition || planePosition.distanceToNM(xPlaneConnectService.getPlanePosition()) > 10.0) {
            planePosition = checkNotNull(xPlaneConnectService.getPlanePosition());
            loadFilteredRadioData();
        }
    }

    private void loadFilteredRadioData() {
        distanceFilteredNavDataPointList = Collections.unmodifiableList(navDataPointList.stream()
                .filter(n -> planePosition.distanceToNM(n) < 200.0)
                .collect(Collectors.toList()));
        applicationEventPublisher.publishEvent(new DataLoadEvent(distanceFilteredNavDataPointList));
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
