package com.dsrts.xcopilot;

import static com.google.common.base.Preconditions.checkNotNull;

public class NavDataPointSelectedEvent {
    private NavDataPoint navDataPoint;
    public NavDataPointSelectedEvent(NavDataPoint navDataPoint) {
        checkNotNull(navDataPoint);
        this.navDataPoint = navDataPoint;
    }
    public NavDataPoint getNavDataPoint() {
        return navDataPoint;
    }
}
