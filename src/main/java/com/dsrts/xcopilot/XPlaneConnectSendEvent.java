package com.dsrts.xcopilot;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class XPlaneConnectSendEvent {
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
