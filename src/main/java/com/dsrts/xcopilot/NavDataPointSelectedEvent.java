package com.dsrts.xcopilot;

import static com.google.common.base.Preconditions.checkNotNull;

public class NavDataPointSelectedEvent {
    private NavDataPoint navDataPoint;
    private XPlaneConnectSendEvent.DREF dref;
    public NavDataPointSelectedEvent(NavDataPoint navDataPoint) {
        this.navDataPoint = checkNotNull(navDataPoint);
    }
    public NavDataPointSelectedEvent(XPlaneConnectSendEvent.DREF dref,NavDataPoint navDataPoint) {
        this.navDataPoint = checkNotNull(navDataPoint);
        this.dref = checkNotNull(dref);
    }
    public NavDataPoint getNavDataPoint() {
        return navDataPoint;
    }

    public XPlaneConnectSendEvent.DREF getDref() {
        return dref;
    }
}
