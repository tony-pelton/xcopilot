package com.dsrts.xcopilot.ui;

import com.dsrts.xcopilot.event.XcopilotEvent;
import com.dsrts.xcopilot.model.NavigationGeoPoint;
import com.dsrts.xcopilot.service.RadioDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonMap;

@Controller
public class MainTable extends JTable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainTable.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    private List<NavigationGeoPoint> navigationGeoPoints = new ArrayList<>();

    public MainTable(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        setBorder(BorderFactory.createRaisedBevelBorder());
        setBackground(Color.LIGHT_GRAY);
        setGridColor(Color.BLACK);
        setShowVerticalLines(false);
        setFont(new Font(Font.MONOSPACED,Font.BOLD,14));

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellSelectionEnabled(false);
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(true);

        setVisible(true);
    }
    private void selectionListener(ListSelectionEvent listSelectionEvent) {
        if(!listSelectionEvent.getValueIsAdjusting()) {
            NavigationGeoPoint navigationGeoPointAt = ((MainTableModel) getModel()).getNavDataAt(getSelectedRow());
            LOGGER.debug(navigationGeoPointAt.toString());
            applicationEventPublisher.publishEvent(
                new XcopilotEvent("selectednavpoint",singletonMap("navpoint",navigationGeoPointAt),this)
            );
        }
    }

    @EventListener
    private void dataLoadListener(RadioDataManager.DataLoadEvent dataLoadEvent) {
        LOGGER.debug("dataLoadListener()");
        navigationGeoPoints = dataLoadEvent.getNavigationGeoPoints();
        SwingUtilities.invokeLater(this::loadTable);
    }

    private void loadTable() {
        // not sure if this is right ? had trouble with selection listener
        // firing events when model was loading, getting index out of bounds exceptions
        setSelectionModel(new DefaultListSelectionModel());
        this.setModel(new MainTableModel(navigationGeoPoints));
        getSelectionModel().addListSelectionListener(this::selectionListener);
    }

    public static class MainTableModel extends AbstractTableModel {
        private String[] columnNames = {"Description"};
        private List<NavigationGeoPoint> navigationGeoPointList;
        public MainTableModel(List<NavigationGeoPoint> navigationGeoPointList) {
            this.navigationGeoPointList = navigationGeoPointList;
        }
        public NavigationGeoPoint getNavDataAt(int index) {
            return navigationGeoPointList.get(index);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public int getRowCount() {
            return navigationGeoPointList.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            NavigationGeoPoint navigationGeoPoint = navigationGeoPointList.get(rowIndex);
            String o = "";
            switch (columnIndex) {
                case 0:
                    o = navigationGeoPoint.getDescription();
                    break;
            }
            return o;
        }
    }
}
