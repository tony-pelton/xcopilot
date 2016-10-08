package com.dsrts.xcopilot;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class XPlaneConnectSendEvent {
    enum DREF {
        SIM_COCKPIT_RADIOS_NAV1_FREQ_HZ("sim/cockpit/radios/nav1_freq_hz"),
        SIM_COCKPIT_RADIOS_NAV2_FREQ_HZ("sim/cockpit/radios/nav2_freq_hz");
        private String dref;
        DREF(String dref) {
            this.dref = dref;
        }
        String getDref() {
            return dref;
        }
    }
    private DREF dref;
    private float fvalue;
    public XPlaneConnectSendEvent(DREF dref,float fvalue) {
        this.dref = dref;
        this.fvalue = fvalue;
    }
    public DREF getDREF() {
        return dref;
    }
    public String getDref() {
        return dref.getDref();
    }
    public float getFvalue() {
        return fvalue;
    }
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
