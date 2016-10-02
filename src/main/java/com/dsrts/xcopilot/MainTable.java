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
    private List<Consumer<RadioDataManager.NavData>> selectListenerConsumers = new ArrayList();
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
            RadioDataManager.NavData navDataAt = ((MainTableModel) getModel()).getNavDataAt(listSelectionEvent.getLastIndex());
            LOGGER.info( (navDataAt.toString()));
            selectListenerConsumers.forEach( c -> c.accept(navDataAt));
        }
    }
    public void addSelectListner(Consumer<RadioDataManager.NavData> navDataConsumer) {
        selectListenerConsumers.add(navDataConsumer);
    }
    private void dataLoadListener(ActionEvent actionEvent) {
        LOGGER.info("dataLoadListener()");
        SwingUtilities.invokeLater(this::loadTable);
    }
    private void loadTable() {
        this.setModel(new MainTableModel(radioDataManager.getNavDataList()));
    }
    public static class MainTableModel extends AbstractTableModel {
        private List<RadioDataManager.NavData> navDataList;
        public MainTableModel(List<RadioDataManager.NavData> navDataList) {
            this.navDataList = navDataList;
        }
        public RadioDataManager.NavData getNavDataAt(int index) {
            return navDataList.get(index);
        }
        @Override
        public int getRowCount() {
            return navDataList.size();
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            RadioDataManager.NavData navData = navDataList.get(rowIndex);
            Object o = "";
            switch (columnIndex) {
                case 0:
                    o = navData.getCode();
                    break;
                case 1:
                    o = navData.getLatitude();
                    break;
                case 2:
                    o = navData.getLongitude();
                    break;
                case 3:
                    o = navData.getElevationMSL();
                    break;
                case 4:
                    o = navData.getFrequency();
                    break;
                case 5:
                    o = navData.getDescription();
                    break;
            }
            return o;
        }
    }
}
