package com.dsrts.xcopilot.model;

import java.io.Serializable;

import static java.lang.Math.*;

public class Geo implements Serializable {
    public float distanceM(GeoPoint point1, GeoPoint point2) {
        float theta = point1.getLongitude() - point2.getLongitude();
        double distance = sin(deg2rad(point1.getLatitude())) * sin(deg2rad(point2.getLatitude())) + cos(deg2rad(point1.getLatitude())) * cos(deg2rad(point2.getLatitude())) * cos(deg2rad(theta));
        distance = acos(distance);
        distance = rad2deg(distance);
        distance = distance * 60 * 1.1515;
        return new Float(distance);
    }
    public float distanceNM(GeoPoint point1, GeoPoint point2) {
        return new Float(0.8684 * distanceM(point1,point2));
    }
    public float distanceKM(GeoPoint point1, GeoPoint point2) {
        return new Float(1.609344 * distanceM(point1,point2));
    }
    public static final double DEG2RADHALFPI = PI / 180;
    public static double deg2rad(double deg) {
        return deg * DEG2RADHALFPI;
    }
    public static final double RAD2DEGHALFPI = 180 / PI;
    public static double rad2deg(double rad) {
        return rad * RAD2DEGHALFPI;
    }
}
