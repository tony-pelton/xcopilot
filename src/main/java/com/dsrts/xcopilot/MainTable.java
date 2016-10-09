package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainTable extends JTable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainTable.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    private List<NavigationGeoPoint> navigationGeoPoints = new ArrayList<>();

    public MainTable(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setBorder(BorderFactory.createRaisedBevelBorder());
        setBackground(Color.LIGHT_GRAY);
        setCellSelectionEnabled(false);
        setGridColor(Color.BLACK);
        setRowSelectionAllowed(false);
        setShowVerticalLines(false);
        setFont(new Font(Font.MONOSPACED,Font.BOLD,14));
        setVisible(true);
    }
    private void selectionListener(ListSelectionEvent listSelectionEvent) {
        if(!listSelectionEvent.getValueIsAdjusting()) {
            NavigationGeoPoint navigationGeoPointAt = ((MainTableModel) getModel()).getNavDataAt(listSelectionEvent.getLastIndex());
            LOGGER.info( (navigationGeoPointAt.toString()));
            applicationEventPublisher.publishEvent(new NavDataPointSelectedEvent(navigationGeoPointAt));
        }
    }

    @EventListener
    private void dataLoadListener(RadioDataManager.DataLoadEvent dataLoadEvent) {
        LOGGER.info("dataLoadListener()");
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
