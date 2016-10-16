package com.dsrts.xcopilot.ui;

import com.dsrts.xcopilot.event.XcopilotEvent;
import com.dsrts.xcopilot.model.LOCNavigationGeoPoint;
import com.dsrts.xcopilot.model.NavigationGeoPoint;
import com.dsrts.xcopilot.service.DREF;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Controller
public class SendButtonsPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendButtonsPanel.class);

    private ApplicationEventPublisher publisher;

    private NavigationGeoPoint navigationGeoPoint;
    private final JButton sendNav1Button;
    private final JButton sendNav2Button;

    public SendButtonsPanel(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        sendNav1Button = new JButton("Set NAV1");
        sendNav1Button.addActionListener(this::actionListener);
        sendNav1Button.setEnabled(false);
        add(sendNav1Button);

        add(Box.createRigidArea(new Dimension(0,5)));

        sendNav2Button = new JButton("Set NAV2");
        sendNav2Button.addActionListener(this::actionListener);
        sendNav2Button.setEnabled(false);
        add(sendNav2Button);

    }
    @EventListener(condition = "#navDataPointSelectedEvent.getValue('selectednavpoint') != null")
    private void navDataPointSelectedEvent(XcopilotEvent navDataPointSelectedEvent) {
        NavigationGeoPoint inNavigationGeoPoint = navDataPointSelectedEvent.getValue("selectednavpoint");
        LOGGER.debug(navDataPointSelectedEvent.toString());
        this.navigationGeoPoint = inNavigationGeoPoint;
        sendNav1Button.setEnabled(true);
        sendNav2Button.setEnabled(true);
    }
    private void actionListener(ActionEvent actionEvent) {
        LOGGER.info(navigationGeoPoint.toString());
        if(actionEvent.getSource() == sendNav1Button) {
            publisher.publishEvent(
                    new XcopilotEvent(
                            ImmutableMap.of(
                                    "senddref", DREF.SIM_COCKPIT_RADIOS_NAV1_FREQ_HZ.getDref(),
                                    "value", navigationGeoPoint.getFrequencyForDREF()
                            )
                    )
            );
            // ils
            if(navigationGeoPoint.getCode().equals(4)) {
                publisher.publishEvent(
                        new XcopilotEvent(
                                ImmutableMap.of(
                                        "senddref", DREF.SIM_COCKPIT_RADIOS_NAV1_OBS_DEGT.getDref(),
                                        "value", ((LOCNavigationGeoPoint) navigationGeoPoint).getBearing()
                                )
                        )
                );
            }
            publisher.publishEvent(
                    new XcopilotEvent(
                            ImmutableMap.of(
                                    "selectedradio", DREF.SIM_COCKPIT_RADIOS_NAV1_FREQ_HZ,
                                    "value", navigationGeoPoint
                            )
                    )

            );
        }
        if(actionEvent.getSource() == sendNav2Button) {
            publisher.publishEvent(
                    new XcopilotEvent(
                            ImmutableMap.of(
                                    "senddref", DREF.SIM_COCKPIT_RADIOS_NAV2_FREQ_HZ.getDref(),
                                    "value", navigationGeoPoint.getFrequencyForDREF()
                            )
                    )
            );
            // ils
            if (navigationGeoPoint.getCode().equals(4)) {
                publisher.publishEvent(
                        new XcopilotEvent(
                                ImmutableMap.of(
                                        "senddref", DREF.SIM_COCKPIT_RADIOS_NAV2_OBS_DEGT.getDref(),
                                        "value", ((LOCNavigationGeoPoint) navigationGeoPoint).getBearing()
                                )
                        )
                );
            }
            publisher.publishEvent(
                    new XcopilotEvent(
                            ImmutableMap.of(
                                    "selectedradio", DREF.SIM_COCKPIT_RADIOS_NAV2_FREQ_HZ,
                                    "value", navigationGeoPoint
                            )
                    )
            );
        }
    }

}
