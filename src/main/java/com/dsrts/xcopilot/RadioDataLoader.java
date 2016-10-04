package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RadioDataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(RadioDataLoader.class);

    public List<NavDataPoint> loadRadioData(SettingsManager settingsManager) {
        List<NavDataPoint> list = new ArrayList<>();
        try {
            String home = settingsManager.getProperty(SettingsManager.KEY_XPLANE_HOME);
            if(null != home) {
                File navDataFile;
                navDataFile = new File(home, "Resources");
                navDataFile = new File(navDataFile, "default data");
                navDataFile = new File(navDataFile, "earth_nav.dat");
                BufferedReader bufferedReader = new BufferedReader(new FileReader(navDataFile));
                list = bufferedReader.lines()
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
                                    scanner.forEachRemaining(name -> {
                                        builder.append(name);
                                        builder.append(" ");
                                    });
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
            }
        } catch (IOException e) {
            LOGGER.warn("loadRadioData()", e);
        }
        return list;
    }
}