package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RadioDataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RadioDataManager.class);

    private XPlaneConnectService xPlaneConnectService;
    private SettingsManager settingsManager;

    private File navDataFile;

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
        String home = settingsManager.getProperty(SettingsManager.KEY_XPLANE_HOME);
        if(null != home) {
            navDataFile = new File(home,"Resources");
            navDataFile = new File(navDataFile,"default data");
            navDataFile = new File(navDataFile,"earth_nav.dat");
        }
        if(null != navDataFile) {
            synchronized (navDataPointList) {
                if(navDataPointList.size() < 1) {
                    loadRadioData();
                }
            }
        }
        if(null == planePosition || planePosition.distanceToNM(xPlaneConnectService.getPlanePosition()) > 10.0) {
            planePosition = xPlaneConnectService.getPlanePosition();
            loadFilteredRadioData();
        }
    }
    private void loadFilteredRadioData() {
        distanceFilteredNavDataPointList = navDataPointList.stream()
                .filter(n -> planePosition.distanceToNM(n) < 200.0)
                .collect(Collectors.toList());
        listeners.forEach(l -> l.actionPerformed(null));
    }
    private void loadRadioData() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(navDataFile));
            List<NavDataPoint> list = bufferedReader.lines()
                    .filter(s -> s.startsWith("3") || s.startsWith("4"))
                    .map(s -> {
                        Scanner scanner = new Scanner(s);
                        NavDataPoint navDataPoint = new NavDataPoint();
                        navDataPoint.setCode(scanner.nextInt());
                        navDataPoint.setLatitude(new BigDecimal(scanner.next()).setScale(8));
                        navDataPoint.setLongitude(new BigDecimal(scanner.next()).setScale(8));
                        navDataPoint.setElevationMSL(scanner.nextInt());
                        navDataPoint.setFrequency(new BigDecimal(scanner.next()).divide(new BigDecimal("100")).setScale(2));
                        // max range
                        scanner.next();
                        switch (navDataPoint.getCode()) {
                            case 3:
                                // slaved variation ?
                                scanner.next();
                                // ident
                                navDataPoint.setIdent(scanner.next());
                                // name
                                {
                                    StringBuilder builder = new StringBuilder();
                                    scanner.forEachRemaining(name -> {builder.append(name);builder.append(" ");} );
                                    navDataPoint.setDescription(builder.toString().trim());
                                }
                                break;
                            case 4:
                                // localiser bearing true degrees
                                scanner.next();
                                // ident
                                navDataPoint.setIdent(scanner.next());
                                // ICAO code
                                {
                                StringBuilder builder = new StringBuilder();
                                builder.append(scanner.next());
                                // runway number
                                builder.append(" RWY ");
                                builder.append(scanner.next());
                                // name
                                builder.append(" ");
                                builder.append(scanner.next());
                                navDataPoint.setDescription(builder.toString());
                                }
                                break;
                        }
                        return navDataPoint;
                    })
                    .collect(Collectors.toList());
            navDataPointList = Collections.unmodifiableList(list);

        } catch (IOException e) {
            LOGGER.warn("loadRadioData()",e);
        }
    }
}
