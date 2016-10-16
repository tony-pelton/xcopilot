package com.dsrts.xcopilot.event;

import static com.google.common.base.Preconditions.checkNotNull;

public class SettingsManagerPropertyEvent {

    private String key;

    public SettingsManagerPropertyEvent(String key) {
        this.key = checkNotNull(key);
    }

    public String getKey() {
        return key;
    }

}
