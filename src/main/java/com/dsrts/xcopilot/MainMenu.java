package com.dsrts.xcopilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Controller
public class MainMenu extends JMenuBar {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenu.class);

    private final Kernel kernel;
    private final SettingsManager settingsManager;

    private final JMenu fileMenu;
    private final JMenuItem fileMenuOpen;
    private final JMenu settingsMenu;
    private final JMenuItem settingsMenuHome;

    @Autowired
    public MainMenu(Kernel kernel,SettingsManager settingsManager) {
        this.kernel = kernel;
        this.settingsManager = settingsManager;
        fileMenu = new JMenu("File");
        fileMenuOpen = new JMenuItem("Exit");
        fileMenuOpen.addActionListener(this::exitListener);
        fileMenu.add(fileMenuOpen);
        add(fileMenu);
        settingsMenu = new JMenu("Settings");
        settingsMenuHome = new JMenuItem("X-Plane Home");
        settingsMenuHome.addActionListener(this::settingsHomeListener);
        settingsMenu.add(settingsMenuHome);
        add(settingsMenu);
    }
    private void exitListener(ActionEvent actionEvent) {
        kernel.shutdown();
    }
    private void settingsHomeListener(ActionEvent actionEvent) {
        settingsManager.decideXPlaneHome(this);
    }
}
