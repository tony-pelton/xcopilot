package com.dsrts.xcopilot;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public class NavDataPoint extends GeoPoint {
    private Integer code;
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
