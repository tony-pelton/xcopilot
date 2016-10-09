package com.dsrts.xcopilot;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import javax.swing.*;

import static java.lang.String.format;

@Controller
public class MainSelectedNavigationGeoPointPanel extends JLabel {
    private static final String RADIO_LABEL = "Hz [%s] %s";
    public MainSelectedNavigationGeoPointPanel() {
        setText("N/A");
        setVisible(true);
    }
    @EventListener
    private void navDataPointSelectedEvent(NavDataPointSelectedEvent navDataPointSelectedEvent) {
        NavigationGeoPoint inNavigationGeoPoint = navDataPointSelectedEvent.getNavigationGeoPoint();
        SwingUtilities.invokeLater(() -> setText(format(RADIO_LABEL, inNavigationGeoPoint.getFrequency().toString(), inNavigationGeoPoint.getDescription())));
    }
}
