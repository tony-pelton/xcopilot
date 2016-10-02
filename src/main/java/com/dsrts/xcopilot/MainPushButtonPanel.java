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
    private RadioDataManager.NavData navData;
    private final JButton sendButton;

    public MainPushButtonPanel(XPlaneConnectService xPlaneConnectService) {
        this.xPlaneConnectService = xPlaneConnectService;
        ((FlowLayout)getLayout()).setAlignment(FlowLayout.LEADING);
        sendButton = new JButton("Send NAV1");
        sendButton.addActionListener(this::actionListener);
        sendButton.setEnabled(false);
        add(sendButton);
        setVisible(true);
    }

    public void setNavData(RadioDataManager.NavData navData) {
        this.navData = navData;
        sendButton.setEnabled(true);
    }
    private void actionListener(ActionEvent actionEvent) {
        LOGGER.info("actionListener()");
        LOGGER.info(navData.toString());
        xPlaneConnectService.sendDREF("sim/cockpit/radios/nav1_freq_hz",navData.getFrequency().multiply(new BigDecimal("100")).setScale(0).floatValue());
    }
}
