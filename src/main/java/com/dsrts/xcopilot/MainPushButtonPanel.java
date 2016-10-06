package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.awt.*;

@Controller
public class MainPushButtonPanel extends JPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainPushButtonPanel.class);
    private NavDataPoint navDataPoint;

    private JPanel nav1JPanel = new JPanel();
    private JLabel jLabelNav1 = new JLabel();

    private JPanel nav2JPanel = new JPanel();
    private JLabel jLabelNav2 = new JLabel();

    public MainPushButtonPanel(SendButtonsPanel sendButtonsPanel) {
        ((FlowLayout)getLayout()).setAlignment(FlowLayout.LEADING);
        add(sendButtonsPanel);

        JPanel sentPanel = new JPanel();
        sentPanel.setLayout(new BoxLayout(sentPanel,BoxLayout.PAGE_AXIS));

        jLabelNav1.setText("nav1");
        nav1JPanel.add(jLabelNav1);
        sentPanel.add(nav1JPanel);

        add(Box.createRigidArea(new Dimension(0,5)));

        jLabelNav2.setText("nav2");
        nav2JPanel.add(jLabelNav2);
        sentPanel.add(nav2JPanel);

        add(sentPanel);

        setVisible(true);
    }

    @EventListener
    private void setNavDataPoint(NavDataPointSelectedEvent navDataPointSelectedEvent) {
        NavDataPoint inNavDataPoint = navDataPointSelectedEvent.getNavDataPoint();
        if(null != inNavDataPoint) {
            this.navDataPoint = inNavDataPoint;
        } else {
            this.navDataPoint = null;
        }
    }
}
