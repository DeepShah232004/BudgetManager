//package ui.tabs;
//
//import ui.BudgetManagerUI;
//
//import javax.swing.*;
//import java.awt.*;
//
//public abstract class Tab extends JPanel {
//
//    private final BudgetManagerUI controller;
//
//    public Tab(BudgetManagerUI controller) {
//        this.controller = controller;
//    }
//
//    public JPanel formatButtonRow(JButton b) {
//        JPanel p = new JPanel();
//        p.setLayout(new FlowLayout());
//        p.add(b);
//
//        return p;
//    }
//
//    //EFFECTS: returns the SmartHomeUI controller for this tab
//    public BudgetManagerUI getController() {
//        return controller;
//    }
//}
