package com.dsrts.xcopilot.ui;

import com.dsrts.xcopilot.event.XcopilotEvent;
import com.dsrts.xcopilot.model.NavigationGeoPoint;
import com.dsrts.xcopilot.service.DREF;
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
        setBorder(BorderFactory.createEtchedBorder());
        ((FlowLayout)getLayout()).setAlignment(FlowLayout.LEADING);
        add(sendButtonsPanel);

        JPanel sentPanel = new JPanel();
        sentPanel.setLayout(new BoxLayout(sentPanel,BoxLayout.PAGE_AXIS));

        jLabelNav1.setText("nav1");
        nav1JPanel.add(jLabelNav1);
        ((FlowLayout)nav1JPanel.getLayout()).setAlignment(FlowLayout.LEADING);
        sentPanel.add(nav1JPanel);

        add(Box.createRigidArea(new Dimension(0,5)));

        jLabelNav2.setText("nav2");
        nav2JPanel.add(jLabelNav2);
        ((FlowLayout)nav2JPanel.getLayout()).setAlignment(FlowLayout.LEADING);
        sentPanel.add(nav2JPanel);

        add(sentPanel);

        setVisible(true);
    }

    private static final String RADIO_LABEL = "Hz [%s] %s";

    @EventListener(condition = "#navDataPointSelectedEvent.isEvent('selectedradio')")
    private void navDataPointSelectedEvent(XcopilotEvent navDataPointSelectedEvent) {
        LOGGER.info(navDataPointSelectedEvent.toString());
        NavigationGeoPoint inNavigationGeoPoint = navDataPointSelectedEvent.getValue();
        JLabel theLabel;
        switch (navDataPointSelectedEvent.<DREF>getKey()) {
            case SIM_COCKPIT_RADIOS_NAV1_FREQ_HZ:
                theLabel = jLabelNav1;
                break;
            case SIM_COCKPIT_RADIOS_NAV2_FREQ_HZ:
                theLabel = jLabelNav2;
                break;
            default:
                return;
        }
        SwingUtilities.invokeLater(() -> theLabel.setText(format(RADIO_LABEL, inNavigationGeoPoint.getFrequency().toString(), inNavigationGeoPoint.getDescription())));
    }
}
