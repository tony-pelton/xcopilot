package com.dsrts.xcopilot.event;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class XcopilotEvent {
    private String event;
    private Map<String,Object> map;

    public XcopilotEvent(String event,Map<String,Object> map) {
        this.event = checkNotNull(event);
        this.map = map;
    }

    public XcopilotEvent(String event,Object key,Object value) {
        this(event, new HashMap());
        map.put("key", key);
        map.put("value",value);
    }

    public boolean isEvent(String event) {
        return this.event.equals(event);
    }

    public <V> V getKey() {
        return getValue("key");
    }

    public <V> V getValue() {
        return getValue("value");
    }

    public <V> V getValue(Object key) {
        return (V) map.get(key);
    }
    public String toString() {
        return new ToStringBuilder(this).append("event",event).append("map",map).toString();
    }
}
