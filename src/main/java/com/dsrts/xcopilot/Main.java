package com.dsrts.xcopilot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PreDestroy;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

@Controller
public class Main extends JFrame implements WindowListener {
    private final Kernel kernel;
    @Autowired
    public Main(Kernel kernel,
                MainMenu mainMenu,
                MainTable mainTable,
                MainPushButtonPanel mainPushButtonPanel,
                MainSelectedNavigationGeoPointPanel mainSelectedNavigationGeoPointPanel) {
        this.kernel = kernel;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(this);
        setJMenuBar(mainMenu);

        JPanel headPanel = new JPanel();
        headPanel.setLayout(new BoxLayout(headPanel,BoxLayout.Y_AXIS));
        headPanel.add(mainPushButtonPanel);
        headPanel.add(mainSelectedNavigationGeoPointPanel);
        getContentPane().add(headPanel,BorderLayout.PAGE_START);

        getContentPane().add(new JScrollPane(mainTable), BorderLayout.CENTER);
    }

    @PreDestroy
    public void dispose() {
        super.dispose();
    }

    public void windowOpened(WindowEvent e) { }

    public void windowClosed(WindowEvent e) {
        kernel.shutdown();
    }

    public void windowIconified(WindowEvent e) { }

    public void windowDeiconified(WindowEvent e) { }

    public void windowActivated(WindowEvent e) { }

    public void windowDeactivated(WindowEvent e) { }

    public void windowClosing(WindowEvent e) { }
}
