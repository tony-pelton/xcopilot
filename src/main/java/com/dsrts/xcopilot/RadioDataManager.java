package com.dsrts.xcopilot;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
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

    private File radioDataFile;
    List<NavData> navDataList = Arrays.asList(new NavData());

    List<ActionListener> listeners = new ArrayList<>();

    public RadioDataManager(XPlaneConnectService xPlaneConnectService) {
        this.xPlaneConnectService = xPlaneConnectService;
    }
    public void addNavDataLoadListener(ActionListener actionListener) {
        listeners.add(actionListener);
    }
    public List<NavData> getNavDataList() {
        return navDataList;
    }
    public void setRadioDataFile(File radioDataFile) {
        this.radioDataFile = radioDataFile;
    }
    @Async
    public void loadRadioData() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(radioDataFile));
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
                                scanner.next();
                                // name
                                navData.setDescription(scanner.next());
                                break;
                            case 4:
                                StringBuilder builder = new StringBuilder();
                                // localiser bearing true degrees
                                scanner.next();
                                // ident
                                scanner.next();
                                // ICAO code
                                builder.append(scanner.next());
                                // runway number
                                builder.append(" RWY ");
                                builder.append(scanner.next());
                                // name
                                builder.append(" ");
                                builder.append(scanner.next());
                                navData.setDescription(builder.toString());
                                break;
                        }
                        return navData;
                    })
                    .collect(Collectors.toList());
            navDataList = Collections.unmodifiableList(list);
            listeners.forEach(l -> l.actionPerformed(null));
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
