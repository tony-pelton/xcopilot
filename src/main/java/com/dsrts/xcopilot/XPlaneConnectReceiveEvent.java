package com.dsrts.xcopilot;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class XPlaneConnectReceiveEvent {
    enum Group {
        TELEMETRY
    }
    private Group group;
    private Map<DREF,float[]> data = new HashMap<>();

    public XPlaneConnectReceiveEvent(Group group,DREF[] drefs,float[][] values) {
        this.group = group;
        checkArgument(drefs.length == values.length,"<init> drefs.length != values.length");
        for(int y = 0;y < drefs.length;y++) {
            data.put(drefs[y],values[y]);
        }
    }

    public Group getGroup() {
        return group;
    }

    public float[] getData(DREF dref) {
        return data.get(dref);
    }
}
