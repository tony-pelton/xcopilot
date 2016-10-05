package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class RadioDataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RadioDataManager.class);

    private XPlaneConnectService xPlaneConnectService;
    private SettingsManager settingsManager;

    List<NavDataPoint> navDataPointList = new ArrayList<>();
    List<NavDataPoint> distanceFilteredNavDataPointList = new ArrayList<>();

    private GeoPoint planePosition;

    List<ActionListener> listeners = new ArrayList<>();

    public RadioDataManager(XPlaneConnectService xPlaneConnectService,SettingsManager settingsManager) {
        this.xPlaneConnectService = xPlaneConnectService;
        this.settingsManager = settingsManager;
    }

    public void addNavDataLoadListener(ActionListener actionListener) {
        listeners.add(actionListener);
    }

    public List<NavDataPoint> getDistanceFilteredNavDataList() {
        return distanceFilteredNavDataPointList;
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
        distanceFilteredNavDataPointList = navDataPointList.stream()
                .filter(n -> planePosition.distanceToNM(n) < 200.0)
                .collect(Collectors.toList());
        listeners.forEach(l -> l.actionPerformed(null));
    }
}
