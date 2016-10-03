package com.dsrts.xcopilot;

public class GeoPoint {
    private Double latitude;
    private Double longitude;

    public GeoPoint(RadioDataManager.NavData navData) {
        this.latitude = navData.getLatitude().doubleValue();
        this.longitude = navData.getLongitude().doubleValue();
    }

    public GeoPoint(XPlaneConnectService.PlanePosition planePosition) {
        this.latitude = new Double(planePosition.getLattitude());
        this.longitude = new Double(planePosition.getLongitude());
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
