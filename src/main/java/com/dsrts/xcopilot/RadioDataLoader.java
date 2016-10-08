package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RadioDataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(RadioDataLoader.class);

    private File navDataFile;
    private List<NavDataPoint> navDataPoints;

    public RadioDataLoader(File home) {
        checkNotNull(home);
        checkArgument(home.isDirectory(),"<init> : %s is not a directory",home.getAbsolutePath());
        navDataFile = new File(home, "Resources");
        navDataFile = new File(navDataFile, "default data");
        navDataFile = new File(navDataFile, "earth_nav.dat");
        try {
            checkArgument(navDataFile.canRead(),"<init> : %s is not a readable file",navDataFile.getCanonicalPath());
        } catch (IOException e) {
            LOGGER.error("<init>",e);
        }
    }
    public synchronized List<NavDataPoint> getRadioNavDataList() {
        if(null == navDataPoints) {
            loadRadioNavDataList();
        }
        return navDataPoints;
    }
    private void loadRadioNavDataList() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(navDataFile));
            navDataPoints = Collections.unmodifiableList(bufferedReader.lines()
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
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            LOGGER.warn("getRadioNavDataList()", e);
            navDataPoints = Collections.EMPTY_LIST;
        }
    }
}