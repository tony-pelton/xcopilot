package com.dsrts.xcopilot.ui;

import com.dsrts.xcopilot.model.NavigationGeoPoint;
import com.dsrts.xcopilot.service.RadioDataManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;

@Controller
public class MainTree extends JScrollPane implements TreeSelectionListener {

    private final JPanel panel;
    private final JTree tree;
    private final DefaultMutableTreeNode root;
    private final DefaultMutableTreeNode nearby;

    public MainTree() {
        nearby = new DefaultMutableTreeNode("Nearby");
        root = new DefaultMutableTreeNode("Root");
        root.add(nearby);

        tree = new JTree(root);
        tree.setVisible(true);
        tree.addTreeSelectionListener(this);

        panel = new JPanel(new GridLayout(1,0));
        panel.add(tree);
        panel.setVisible(true);

//        add(panel);
//        getRootPane().add(panel);
        getViewport().add(panel);
        setVisible(true);
    }

    public void valueChanged(TreeSelectionEvent e) {
//        System.out.println(e.getPath());
//        System.out.println(e.getPath().getLastPathComponent());
    }

    @EventListener
    private void dataLoadListener(RadioDataManager.DataLoadEvent dataLoadEvent) {
        SwingUtilities.invokeLater(() -> loadTree(dataLoadEvent.getNavigationGeoPoints()));
    }

    private void loadTree(List<NavigationGeoPoint> points) {
        points.forEach(p -> {
                    nearby.add(new DefaultMutableTreeNode(p.getDescription()));
                }
        );
//        for(TreeNode treeNode : nearby.getPath()) {
//            tree.expandPath(new TreePath(treeNode));
//        }
    }
}
