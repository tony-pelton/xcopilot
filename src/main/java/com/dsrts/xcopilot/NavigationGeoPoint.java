package com.dsrts.xcopilot;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

import static java.lang.String.format;

public class NavigationGeoPoint extends GeoPoint {
    private Integer code;
    private Integer elevationMSL;
    private BigDecimal frequency;
    private Integer range;

    public NavigationGeoPoint() {}
    public NavigationGeoPoint(
            Integer code,
            BigDecimal latitude,
            BigDecimal longitude,
            Integer elevationMSL,
            BigDecimal frequency,
            Integer range) {
        super(latitude,longitude);
        setCode(code);
        setElevationMSL(elevationMSL);
        setFrequency(frequency);
        setRange(range);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) { this.code = code; }

    public Integer getElevationMSL() {
        return elevationMSL;
    }

    public void setElevationMSL(Integer elevationMSL) {
        this.elevationMSL = elevationMSL;
    }

    public BigDecimal getFrequency() {
        return frequency;
    }

    public float getFrequencyForDREF() {
        return getFrequency().multiply(new BigDecimal("100")).setScale(0).floatValue();
    }

    public void setFrequency(BigDecimal frequency) {
        this.frequency = frequency;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public String getDescription() {
        return format("Hz [%s]",getFrequency().toString());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
