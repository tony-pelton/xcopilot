package com.dsrts.xcopilot.model;

import static java.lang.Math.*;

public class Geo {
    public Double distanceM(GeoPoint point1, GeoPoint point2) {
        Double theta = point1.getLongitudeDouble() - point2.getLongitudeDouble();
        Double distance = sin(deg2rad(point1.getLatitudeDouble())) * sin(deg2rad(point2.getLatitudeDouble())) + cos(deg2rad(point1.getLatitudeDouble())) * cos(deg2rad(point2.getLatitudeDouble())) * cos(deg2rad(theta));
        distance = acos(distance);
        distance = rad2deg(distance);
        distance = distance * 60 * 1.1515;
        return distance;
    }
    public Double distanceNM(GeoPoint point1, GeoPoint point2) {
        return 0.8684 * distanceM(point1,point2);
    }
    public Double distanceKM(GeoPoint point1, GeoPoint point2) {
        return 1.609344 * distanceM(point1,point2);
    }
    public static final Double DEG2RADHALFPI = PI / 180;
    public static Double deg2rad(Double deg) {
        return deg * DEG2RADHALFPI;
    }
    public static final Double RAD2DEGHALFPI = 180 / PI;
    public static Double rad2deg(Double rad) {
        return rad * RAD2DEGHALFPI;
    }
}
