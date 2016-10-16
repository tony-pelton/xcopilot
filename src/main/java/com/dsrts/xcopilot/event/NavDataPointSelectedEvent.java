package com.dsrts.xcopilot.event;

import com.dsrts.xcopilot.service.DREF;
import com.dsrts.xcopilot.model.NavigationGeoPoint;

import static com.google.common.base.Preconditions.checkNotNull;

public class NavDataPointSelectedEvent {
    private NavigationGeoPoint navigationGeoPoint;
    private DREF dref;
    public NavDataPointSelectedEvent(NavigationGeoPoint navigationGeoPoint) {
        this.navigationGeoPoint = checkNotNull(navigationGeoPoint);
    }
    public NavDataPointSelectedEvent(DREF dref, NavigationGeoPoint navigationGeoPoint) {
        this.navigationGeoPoint = checkNotNull(navigationGeoPoint);
        this.dref = checkNotNull(dref);
    }
    public NavigationGeoPoint getNavigationGeoPoint() {
        return navigationGeoPoint;
    }

    public DREF getDref() {
        return dref;
    }
}
