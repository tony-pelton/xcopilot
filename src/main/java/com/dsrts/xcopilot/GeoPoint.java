package com.dsrts.xcopilot;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public class GeoPoint extends Geo {
    private BigDecimal latitude;
    private BigDecimal longitude;

    public GeoPoint() {}
    public GeoPoint(float latitude,float longitude) {
        setLatitude(new BigDecimal(latitude));
        setLongitude(new BigDecimal(longitude));
    }

    public GeoPoint(BigDecimal latitude,BigDecimal longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude.setScale(8,BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude.setScale(8,BigDecimal.ROUND_HALF_EVEN);
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
