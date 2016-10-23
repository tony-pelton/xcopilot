package com.dsrts.xcopilot.event;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class XcopilotEvent {
    private Object source;
    private String event;
    private Map<Object,Object> map;

    public XcopilotEvent(String event,Map<Object,Object> map,Object source) {
        this.event = checkNotNull(event);
        this.map = checkNotNull(map);
        this.source = checkNotNull(source);
    }

    public boolean isEvent(String event) {
        return this.event.equals(event);
    }
    public <V> V getKey() {
        if(map.size() == 1) {
            return (V)map.keySet().stream().findFirst().get();
        } else {
            return null;
        }
    }
    public <V> V getValue() {
        if(map.size() == 1) {
            return (V)map.values().stream().findFirst().get();
        } else {
            return null;
        }
    }
    public <V> V getValue(Object key) {
        return (V) map.get(key);
    }
    public Object getSource() { return source; }

    public String toString() {
        return new ToStringBuilder(this).append("event",event).append("map",map).toString();
    }
}
