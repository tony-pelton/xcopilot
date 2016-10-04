package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Controller
public class MainTable extends JTable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainTable.class);
    private RadioDataManager radioDataManager;
    private List<Consumer<NavDataPoint>> selectListenerConsumers = new ArrayList();
    @Autowired
    public MainTable(RadioDataManager radioDataManager) {
        this.radioDataManager = radioDataManager;
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getSelectionModel().addListSelectionListener(this::selectionListener);
        radioDataManager.addNavDataLoadListener(this::dataLoadListener);
        loadTable();
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
            selectListenerConsumers.forEach( c -> c.accept(navDataPointAt));
        }
    }
    public void addSelectListner(Consumer<NavDataPoint> navDataConsumer) {
        selectListenerConsumers.add(navDataConsumer);
    }
    private void dataLoadListener(ActionEvent actionEvent) {
        LOGGER.info("dataLoadListener()");
        SwingUtilities.invokeLater(this::loadTable);
    }
    private void loadTable() {
        clearSelection();
        this.setModel(new MainTableModel(radioDataManager.getDistanceFilteredNavDataList()));
        selectListenerConsumers.forEach(c -> c.accept(null));
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
