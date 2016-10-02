package com.dsrts.xcopilot;

import org.springframework.stereotype.Service;

@Service
public class Kernel {
    public Kernel() {
    }
    public void shutdown() {
        XcopilotApplication.shutdown();
    }
}
