package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

@Controller
public class MainPushButtonPanel extends JPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainPushButtonPanel.class);
    private ApplicationEventPublisher publisher;
    private NavDataPoint navDataPoint;
    private final JButton sendNav1Button;
    private final JButton sendNav2Button;

    public MainPushButtonPanel(ApplicationEventPublisher publisher) {
        this.publisher = publisher;

        ((FlowLayout)getLayout()).setAlignment(FlowLayout.LEADING);

        JPanel sendButtonsPanel = new JPanel();
        sendButtonsPanel.setLayout(new BoxLayout(sendButtonsPanel,BoxLayout.PAGE_AXIS));

        sendNav1Button = new JButton("Send NAV1");
        sendNav1Button.addActionListener(this::actionListener);
        sendNav1Button.setEnabled(false);
        sendButtonsPanel.add(sendNav1Button,BorderLayout.LINE_START);

        sendButtonsPanel.add(Box.createRigidArea(new Dimension(0,5)));

        sendNav2Button = new JButton("Send NAV2");
        sendNav2Button.addActionListener(this::actionListener);
        sendNav2Button.setEnabled(false);
        sendButtonsPanel.add(sendNav2Button,BorderLayout.LINE_START);

        add(sendButtonsPanel);
        setVisible(true);
    }

    @EventListener
    public void setNavDataPoint(MainTable.NavDataPointSelectedEvent navDataPointSelectedEvent) {
        NavDataPoint inNavDataPoint = navDataPointSelectedEvent.getNavDataPoint();
        if(null != inNavDataPoint) {
            this.navDataPoint = inNavDataPoint;
            sendNav1Button.setEnabled(true);
            sendNav2Button.setEnabled(true);
        } else {
            this.navDataPoint = null;
            sendNav1Button.setEnabled(false);
            sendNav2Button.setEnabled(false);
        }
    }
    private void actionListener(ActionEvent actionEvent) {
        LOGGER.info(navDataPoint.toString());
        if(actionEvent.getSource() == sendNav1Button) {
            publisher.publishEvent(
                    new XPlaneConnectSendEvent(
                            XPlaneConnectSendEvent.DREF.SIM_COCKPIT_RADIOS_NAV1_FREQ_HZ,
                            navDataPoint.getFrequency().multiply(new BigDecimal("100")).setScale(0).floatValue()
                    )
            );
        }
        if(actionEvent.getSource() == sendNav2Button) {
            publisher.publishEvent(
                    new XPlaneConnectSendEvent(
                            XPlaneConnectSendEvent.DREF.SIM_COCKPIT_RADIOS_NAV2_FREQ_HZ,
                            navDataPoint.getFrequency().multiply(new BigDecimal("100")).setScale(0).floatValue()
                    )
            );
        }
    }
}
