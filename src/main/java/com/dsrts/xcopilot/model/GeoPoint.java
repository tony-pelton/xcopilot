package com.dsrts.xcopilot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GeoPoint extends Geo {
    private float latitude;
    private float longitude;

    public GeoPoint() {}
    public GeoPoint(float latitude,float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float distanceToM(GeoPoint point) {
        return distanceM(this,point);
    }

    public float distanceToNM(GeoPoint point) {
        return distanceNM(this,point);
    }

    public float distanceToKM(GeoPoint point) {
        return distanceKM(this,point);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
