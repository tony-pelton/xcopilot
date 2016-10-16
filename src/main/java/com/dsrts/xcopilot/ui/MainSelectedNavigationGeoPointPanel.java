package com.dsrts.xcopilot.ui;

import com.dsrts.xcopilot.event.XcopilotEvent;
import com.dsrts.xcopilot.model.NavigationGeoPoint;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import javax.swing.*;

import java.awt.*;

import static java.lang.String.format;

@Controller
public class MainSelectedNavigationGeoPointPanel extends JPanel {
    private static final String RADIO_LABEL = "Hz [%s] %s";
    private final JLabel label = new JLabel();
    private final JButton button = new JButton("Favorite");
    public MainSelectedNavigationGeoPointPanel() {
        setBorder(BorderFactory.createEtchedBorder());
        ((FlowLayout)getLayout()).setAlignment(FlowLayout.LEADING);

        button.setEnabled(false);
        add(button);

        label.setText("N/A");
        add(label);

        setVisible(true);
    }
    @EventListener(condition = "#navDataPointSelectedEvent.getValue('selectednavpoint') != null")
    private void navDataPointSelectedEvent(XcopilotEvent navDataPointSelectedEvent) {
        NavigationGeoPoint inNavigationGeoPoint = navDataPointSelectedEvent.getValue("selectednavpoint");
        SwingUtilities.invokeLater(() -> {
            label.setText(format(RADIO_LABEL, inNavigationGeoPoint.getFrequency().toString(), inNavigationGeoPoint.getDescription()));
            button.setEnabled(true);
        });
    }
}
