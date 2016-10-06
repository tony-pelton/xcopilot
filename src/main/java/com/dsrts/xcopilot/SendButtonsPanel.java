package com.dsrts.xcopilot;

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

    private NavDataPoint navDataPoint;
    private final JButton sendNav1Button;
    private final JButton sendNav2Button;

    public SendButtonsPanel(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        sendNav1Button = new JButton("Send NAV1");
        sendNav1Button.addActionListener(this::actionListener);
        sendNav1Button.setEnabled(false);
        add(sendNav1Button);

        add(Box.createRigidArea(new Dimension(0,5)));

        sendNav2Button = new JButton("Send NAV2");
        sendNav2Button.addActionListener(this::actionListener);
        sendNav2Button.setEnabled(false);
        add(sendNav2Button);

    }
    @EventListener(condition = "#navDataPointSelectedEvent.dref == null")
    private void navDataPointSelectedEvent(NavDataPointSelectedEvent navDataPointSelectedEvent) {
        LOGGER.info(navDataPointSelectedEvent.toString());
        NavDataPoint inNavDataPoint = navDataPointSelectedEvent.getNavDataPoint();
        this.navDataPoint = inNavDataPoint;
        sendNav1Button.setEnabled(true);
        sendNav2Button.setEnabled(true);
    }
    private void actionListener(ActionEvent actionEvent) {
        LOGGER.info(navDataPoint.toString());
        if(actionEvent.getSource() == sendNav1Button) {
            publisher.publishEvent(
                    new XPlaneConnectSendEvent(
                            XPlaneConnectSendEvent.DREF.SIM_COCKPIT_RADIOS_NAV1_FREQ_HZ,
                            navDataPoint.getFrequencyForDREF()
                    )
            );
            publisher.publishEvent(
                    new NavDataPointSelectedEvent(
                            XPlaneConnectSendEvent.DREF.SIM_COCKPIT_RADIOS_NAV1_FREQ_HZ,
                            navDataPoint
                    )
            );
        }
        if(actionEvent.getSource() == sendNav2Button) {
            publisher.publishEvent(
                    new XPlaneConnectSendEvent(
                            XPlaneConnectSendEvent.DREF.SIM_COCKPIT_RADIOS_NAV2_FREQ_HZ,
                            navDataPoint.getFrequencyForDREF()
                    )
            );
            publisher.publishEvent(
                    new NavDataPointSelectedEvent(
                            XPlaneConnectSendEvent.DREF.SIM_COCKPIT_RADIOS_NAV2_FREQ_HZ,
                            navDataPoint
                    )
            );
        }
    }

}
