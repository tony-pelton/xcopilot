package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainTable extends JTable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainTable.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    private List<NavDataPoint> navDataPoints = new ArrayList<>();

    public MainTable(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setVisible(true);
    }
    private void selectionListener(ListSelectionEvent listSelectionEvent) {
        LOGGER.info("selectionListener()");
        LOGGER.info(listSelectionEvent.getSource().toString());
        LOGGER.info(""+listSelectionEvent.getFirstIndex());
        LOGGER.info(""+listSelectionEvent.getLastIndex());
        LOGGER.info(""+listSelectionEvent.getValueIsAdjusting());
        if(!listSelectionEvent.getValueIsAdjusting()) {
            // pointer to row
            listSelectionEvent.getLastIndex();
            NavDataPoint navDataPointAt = ((MainTableModel) getModel()).getNavDataAt(listSelectionEvent.getLastIndex());
            LOGGER.info( (navDataPointAt.toString()));
            applicationEventPublisher.publishEvent(new NavDataPointSelectedEvent(navDataPointAt));
        }
    }

    @EventListener
    private void dataLoadListener(RadioDataManager.DataLoadEvent dataLoadEvent) {
        LOGGER.info("dataLoadListener()");
        navDataPoints = dataLoadEvent.getNavDataPoints();
        SwingUtilities.invokeLater(this::loadTable);
    }

    private void loadTable() {
        // not sure if this is right ? had trouble with selection listener
        // firing events when model was loading, getting index out of bounds exceptions
        setSelectionModel(new DefaultListSelectionModel());
        this.setModel(new MainTableModel(navDataPoints));
        getSelectionModel().addListSelectionListener(this::selectionListener);
        applicationEventPublisher.publishEvent(new NavDataPointSelectedEvent(null));
    }

    public static class NavDataPointSelectedEvent {
        private NavDataPoint navDataPoint;

        public NavDataPointSelectedEvent(NavDataPoint navDataPoint) {
            this.navDataPoint = navDataPoint;
        }

        public NavDataPoint getNavDataPoint() {
            return navDataPoint;
        }
    }

    public static class MainTableModel extends AbstractTableModel {
        private String[] columnNames = {"Code","Lat","Lon","MSL","Hz","Ident","Desc"};
        private List<NavDataPoint> navDataPointList;
        public MainTableModel(List<NavDataPoint> navDataPointList) {
            this.navDataPointList = navDataPointList;
        }
        public NavDataPoint getNavDataAt(int index) {
            return navDataPointList.get(index);
        }
        @Override
        public int getRowCount() {
            return navDataPointList.size();
        }

        @Override
        public int getColumnCount() {
            return 7;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            NavDataPoint navDataPoint = navDataPointList.get(rowIndex);
            Object o = "";
            switch (columnIndex) {
                case 0:
                    o = navDataPoint.getCode();
                    break;
                case 1:
                    o = navDataPoint.getLatitude();
                    break;
                case 2:
                    o = navDataPoint.getLongitude();
                    break;
                case 3:
                    o = navDataPoint.getElevationMSL();
                    break;
                case 4:
                    o = navDataPoint.getFrequency();
                    break;
                case 5:
                    o = navDataPoint.getIdent();
                    break;
                case 6:
                    o = navDataPoint.getDescription();
                    break;
            }
            return o;
        }
    }
}
