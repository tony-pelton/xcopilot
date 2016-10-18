package com.dsrts.xcopilot.model;

import java.math.BigDecimal;

import static java.lang.String.format;

public class VORNavigationGeoPoint extends NavigationGeoPoint {
    private BigDecimal slavedVariation;
    private String id;
    private String name;
    public VORNavigationGeoPoint() {}
    public VORNavigationGeoPoint(
            Integer code,
            Float latitude,
            Float longitude,
            Integer elevationMSL,
            BigDecimal frequency,
            Integer range,
            BigDecimal slavedVariation,
            String id,
            String name) {
        super(code,latitude,longitude,elevationMSL,frequency,range);
        setSlavedVariation(slavedVariation);
        setId(id);
        setName(name);
    }

    public BigDecimal getSlavedVariation() {
        return slavedVariation;
    }

    public void setSlavedVariation(BigDecimal slavedVariation) {
        this.slavedVariation = slavedVariation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return format("%1$5s  /  %2$5s  /  %3$s",getId(),getFrequency(),getName());
    }
}
