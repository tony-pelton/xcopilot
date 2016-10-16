package com.dsrts.xcopilot.service;

import com.dsrts.xcopilot.XcopilotApplication;
import org.springframework.stereotype.Service;

@Service
public class Kernel {
    public Kernel() {
    }
    public void shutdown() {
        XcopilotApplication.shutdown();
    }
}
