package com.dsrts.xcopilot.event;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class XcopilotEvent {
    private Map<String,Object> map;

    public XcopilotEvent(Map<String,Object> map) {
        this.map = map;
    }
    public XcopilotEvent() {
        this(new HashMap<>());
    }

    public Map<String,Object> getMap() { return ImmutableMap.copyOf(this.map); }

    public <V> V getValue(Object key) {
        return (V) map.get(key);
    }
    public void setValue(String key,Object value) {
        map.put(key,value);
    }
}
