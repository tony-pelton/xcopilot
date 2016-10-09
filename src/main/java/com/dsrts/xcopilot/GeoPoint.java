package com.dsrts.xcopilot;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public class GeoPoint extends Geo {
    private BigDecimal latitude;
    private BigDecimal longitude;

    public GeoPoint() {}
    public GeoPoint(float latitude,float longitude) {
        this.latitude = new BigDecimal(latitude);
        this.longitude = new BigDecimal(longitude);
    }

    public GeoPoint(BigDecimal latitude,BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Double getLatitudeDouble() {
        return latitude.doubleValue();
    }

    public Double getLongitudeDouble() {
        return longitude.doubleValue();
    }

    public Double distanceToM(GeoPoint point) {
        return distanceM(this,point);
    }

    public Double distanceToNM(GeoPoint point) {
        return distanceNM(this,point);
    }

    public Double distanceToKM(GeoPoint point) {
        return distanceKM(this,point);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
