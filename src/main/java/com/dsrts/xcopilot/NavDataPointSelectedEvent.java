package com.dsrts.xcopilot;

import static com.google.common.base.Preconditions.checkNotNull;

public class NavDataPointSelectedEvent {
    private NavigationGeoPoint navigationGeoPoint;
    private XPlaneConnectSendEvent.DREF dref;
    public NavDataPointSelectedEvent(NavigationGeoPoint navigationGeoPoint) {
        this.navigationGeoPoint = checkNotNull(navigationGeoPoint);
    }
    public NavDataPointSelectedEvent(XPlaneConnectSendEvent.DREF dref,NavigationGeoPoint navigationGeoPoint) {
        this.navigationGeoPoint = checkNotNull(navigationGeoPoint);
        this.dref = checkNotNull(dref);
    }
    public NavigationGeoPoint getNavigationGeoPoint() {
        return navigationGeoPoint;
    }

    public XPlaneConnectSendEvent.DREF getDref() {
        return dref;
    }
}
