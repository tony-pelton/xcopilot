package com.dsrts.xcopilot;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.event.ActionEvent;
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
    private Geo geo = new Geo();

    private File navDataFile;

    List<NavData> navDataList = new ArrayList<>();
    List<NavData> distanceFilterednavDataList = new ArrayList<>();

    private GeoPoint planePosition;

    List<ActionListener> listeners = new ArrayList<>();

    public RadioDataManager(XPlaneConnectService xPlaneConnectService,SettingsManager settingsManager) {
        this.xPlaneConnectService = xPlaneConnectService;
        this.settingsManager = settingsManager;
    }
    public void addNavDataLoadListener(ActionListener actionListener) {
        listeners.add(actionListener);
    }
    public List<NavData> getDistanceFilteredNavDataList() {
        return distanceFilterednavDataList;
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
            synchronized (navDataList) {
                if(navDataList.size() < 1) {
                    loadRadioData();
                }
            }
        }
        if(null == planePosition || geo.distanceNM(planePosition,new GeoPoint(xPlaneConnectService.getPlanePosition())) > 10.0) {
            planePosition = new GeoPoint(xPlaneConnectService.getPlanePosition());
            loadFilteredRadioData();
        }
    }
    private void loadFilteredRadioData() {
        distanceFilterednavDataList = navDataList.stream()
                .filter(n -> geo.distanceNM(planePosition,new GeoPoint(n)) < 200.0)
                .collect(Collectors.toList());
        listeners.forEach(l -> l.actionPerformed(null));
    }
    private void loadRadioData() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(navDataFile));
            List<NavData> list = bufferedReader.lines()
                    .filter(s -> s.startsWith("3") || s.startsWith("4"))
                    .map(s -> {
                        Scanner scanner = new Scanner(s);
                        NavData navData = new NavData();
                        navData.setCode(scanner.nextInt());
                        navData.setLatitude(new BigDecimal(scanner.next()).setScale(8));
                        navData.setLongitude(new BigDecimal(scanner.next()).setScale(8));
                        navData.setElevationMSL(scanner.nextInt());
                        navData.setFrequency(new BigDecimal(scanner.next()).divide(new BigDecimal("100")).setScale(2));
                        // max range
                        scanner.next();
                        switch (navData.getCode()) {
                            case 3:
                                // slaved variation ?
                                scanner.next();
                                // ident
                                navData.setIdent(scanner.next());
                                // name
                                {
                                    StringBuilder builder = new StringBuilder();
                                    scanner.forEachRemaining(name -> {builder.append(name);builder.append(" ");} );
                                    navData.setDescription(builder.toString().trim());
                                }
                                break;
                            case 4:
                                // localiser bearing true degrees
                                scanner.next();
                                // ident
                                navData.setIdent(scanner.next());
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
                                navData.setDescription(builder.toString());
                                }
                                break;
                        }
                        return navData;
                    })
                    .collect(Collectors.toList());
            navDataList = Collections.unmodifiableList(list);

        } catch (IOException e) {
            LOGGER.warn("loadRadioData()",e);
        }
    }
    public static class NavData {
        private Integer code;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private Integer elevationMSL;
        private BigDecimal frequency;
        private String ident;
        private String description;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public BigDecimal getLatitude() {
            return latitude;
        }

        public void setLatitude(BigDecimal latitude) {
            this.latitude = latitude;
        }

        public BigDecimal getLongitude() {
            return longitude;
        }

        public void setLongitude(BigDecimal longitude) {
            this.longitude = longitude;
        }

        public Integer getElevationMSL() {
            return elevationMSL;
        }

        public void setElevationMSL(Integer elevationMSL) {
            this.elevationMSL = elevationMSL;
        }

        public BigDecimal getFrequency() {
            return frequency;
        }

        public void setFrequency(BigDecimal frequency) {
            this.frequency = frequency;
        }

        public String getIdent() {
            return ident;
        }

        public void setIdent(String ident) {
            this.ident = ident;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}
