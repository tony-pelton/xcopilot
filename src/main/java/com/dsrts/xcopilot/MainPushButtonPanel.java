package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

@Controller
public class MainPushButtonPanel extends JPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainPushButtonPanel.class);
    private XPlaneConnectService xPlaneConnectService;
    private NavDataPoint navDataPoint;
    private final JButton sendNav1Button;
    private final JButton sendNav2Button;
    private final JButton testButton;

    public MainPushButtonPanel(XPlaneConnectService xPlaneConnectService) {
        this.xPlaneConnectService = xPlaneConnectService;
        ((FlowLayout)getLayout()).setAlignment(FlowLayout.LEADING);

        sendNav1Button = new JButton("Send NAV1");
        sendNav1Button.addActionListener(this::actionListener);
        sendNav1Button.setEnabled(false);
        add(sendNav1Button);

        sendNav2Button = new JButton("Send NAV2");
        sendNav2Button.addActionListener(this::actionListener);
        sendNav2Button.setEnabled(false);
        add(sendNav2Button);

        testButton = new JButton("test");
        testButton.addActionListener(this::actionListener);
        testButton.setEnabled(false);
        add(testButton);

        setVisible(true);
    }

    public void setNavDataPoint(NavDataPoint inNavDataPoint) {
        if(null != inNavDataPoint) {
            this.navDataPoint = inNavDataPoint;
            sendNav1Button.setEnabled(true);
            sendNav2Button.setEnabled(true);
            testButton.setEnabled(true);
        } else {
            this.navDataPoint = null;
            sendNav1Button.setEnabled(false);
            sendNav2Button.setEnabled(false);
            testButton.setEnabled(false);
        }
    }
    private void actionListener(ActionEvent actionEvent) {
        LOGGER.info("actionListener()");
        LOGGER.info(navDataPoint.toString());
        if(actionEvent.getSource() == sendNav1Button) {
            LOGGER.info("actionListener() : nav 1");
            xPlaneConnectService.sendDREF("sim/cockpit/radios/nav1_freq_hz", navDataPoint.getFrequency().multiply(new BigDecimal("100")).setScale(0).floatValue());
        }
        if(actionEvent.getSource() == sendNav2Button) {
            LOGGER.info("actionListener() : nav 2");
            xPlaneConnectService.sendDREF("sim/cockpit/radios/nav2_freq_hz", navDataPoint.getFrequency().multiply(new BigDecimal("100")).setScale(0).floatValue());
        }
        if(actionEvent.getSource() == testButton) {
            LOGGER.info("actionListener() : test");
            xPlaneConnectService.test();
        }
    }
}
