package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.awt.*;

import static java.lang.String.format;

@Controller
public class MainPushButtonPanel extends JPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainPushButtonPanel.class);

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

    private static final String RADIO_LABEL = "Hz [%s] %s";

    @EventListener(condition = "#navDataPointSelectedEvent.dref != null")
    private void navDataPointSelectedEvent(NavDataPointSelectedEvent navDataPointSelectedEvent) {
        LOGGER.info(navDataPointSelectedEvent.toString());
        NavDataPoint inNavDataPoint = navDataPointSelectedEvent.getNavDataPoint();
        JLabel theLabel;
        switch (navDataPointSelectedEvent.getDref()) {
            case SIM_COCKPIT_RADIOS_NAV1_FREQ_HZ:
                theLabel = jLabelNav1;
                break;
            case SIM_COCKPIT_RADIOS_NAV2_FREQ_HZ:
                theLabel = jLabelNav2;
                break;
            default:
                return;
        }
        SwingUtilities.invokeLater(() -> theLabel.setText(format(RADIO_LABEL,inNavDataPoint.getFrequency().toString(),inNavDataPoint.getDescription())));
    }
}
