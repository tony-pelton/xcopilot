package com.dsrts.xcopilot;

import java.math.BigDecimal;

import static java.lang.String.format;

public class LOCNavigationGeoPoint extends NavigationGeoPoint {
    private BigDecimal bearing;
    private String id;
    private String codeICAO;
    private String runway;
    private String name;
    public LOCNavigationGeoPoint() {}
    public LOCNavigationGeoPoint(
            Integer code,
            BigDecimal latitude,
            BigDecimal longitude,
            Integer elevationMSL,
            BigDecimal frequency,
            Integer range,
            BigDecimal bearing,
            String id,
            String codeICAO,
            String runway,
            String name) {
        super(code,latitude,longitude,elevationMSL,frequency,range);
        setBearing(bearing);
        setId(id);
        setCodeICAO(codeICAO);
        setRunway(runway);
        setName(name);
    }

    public BigDecimal getBearing() {
        return bearing;
    }

    public void setBearing(BigDecimal bearing) {
        this.bearing = bearing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodeICAO() {
        return codeICAO;
    }

    public void setCodeICAO(String codeICAO) {
        this.codeICAO = codeICAO;
    }

    public String getRunway() {
        return runway;
    }

    public void setRunway(String runway) {
        this.runway = runway;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return format("%1$5s  /  %2$5s  /  %3$5s  /  %4$5s @ %5$s ",getCodeICAO(),getFrequency(),getId(),getRunway(),getBearing().toString());
    }
}
