package ui.tabs;

import model.*;
import model.Event;
import ui.BudgetManagerUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

// Represents the functionality for the Monthly Report
public class MonthlyReportTab extends JPanel {

    private JTable expenseTable;
    private JTable incomeTable;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JButton generateReportButton;
    private TransactionExpense te;
    private TransactionIncome ti;
    private BudgetMap bm;
    private JLabel remainingBudgetLabel;
    private JLabel budgetLabel;

    private final Map<String, Integer> monthNameToNumber;

    // MODIFIES: this
    // EFFECTS: initialises the components for Monthly Report
    public MonthlyReportTab(TransactionExpense transactionExpense,
                            TransactionIncome transactionIncome, BudgetMap budgetMap) {


        expenseTable = new JTable();
        incomeTable = new JTable();
        monthNameToNumber = new HashMap<>();

        if (transactionExpense != null) {
            te = transactionExpense;
        }

        if (transactionIncome != null) {
            ti = transactionIncome;
        }

        if (budgetMap != null) {
            bm = budgetMap;
        }

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initializeMonthMap();
        createComponents();
        generateReportButton.addActionListener(e -> generateMonthlyReport());
    }

    // MODIFIES: this
    // EFFECTS: adds the months with their number to the hashmap
    private void initializeMonthMap() {
        monthNameToNumber.put("January", 1);
        monthNameToNumber.put("February", 2);
        monthNameToNumber.put("March", 3);
        monthNameToNumber.put("April", 4);
        monthNameToNumber.put("May", 5);
        monthNameToNumber.put("June", 6);
        monthNameToNumber.put("July", 7);
        monthNameToNumber.put("August", 8);
        monthNameToNumber.put("September", 9);
        monthNameToNumber.put("October", 10);
        monthNameToNumber.put("November", 11);
        monthNameToNumber.put("December", 12);
    }

    // EFFECTS: create components of the GUI
    private void createComponents() {
        initialiseJComboBox();
        addMonthYearToGUI();
        addExpenseIncomePanel();
        initialiseBudgetLabel();

        remainingBudgetLabel = new JLabel();
        GridBagConstraints remainingBudgetLabelGbc = new GridBagConstraints();
        remainingBudgetLabelGbc.gridx = 0;
        remainingBudgetLabelGbc.gridy = 4;
        remainingBudgetLabelGbc.gridwidth = 2;
        remainingBudgetLabelGbc.anchor = GridBagConstraints.CENTER;
        remainingBudgetLabelGbc.insets = new Insets(10, 0, 0, 0);
        add(remainingBudgetLabel, remainingBudgetLabelGbc);

        generateReportButton = new JButton("Generate Monthly Report");
        GridBagConstraints generateReportButtonGbc = new GridBagConstraints();
        generateReportButtonGbc.gridx = 0;
        generateReportButtonGbc.gridy = 5;
        generateReportButtonGbc.gridwidth = 2;
        generateReportButtonGbc.anchor = GridBagConstraints.CENTER;
        generateReportButtonGbc.insets = new Insets(10, 0, 0, 0);

        add(generateReportButton, generateReportButtonGbc);
    }

    // EFFECTS: lays out the budgetLabel on the GUI
    private void initialiseBudgetLabel() {
        budgetLabel = new JLabel();
        GridBagConstraints budgetLabelGbc = new GridBagConstraints();
        budgetLabelGbc.gridx = 0;
        budgetLabelGbc.gridy = 3;
        budgetLabelGbc.gridwidth = 2;
        budgetLabelGbc.anchor = GridBagConstraints.CENTER;
        budgetLabelGbc.insets = new Insets(10, 0, 0, 0);
        add(budgetLabel, budgetLabelGbc);
    }

    // EFFECTS: add month and year labels with dropdown boxes to the GUI
    private void addMonthYearToGUI() {
        GridBagConstraints monthYearGbc = new GridBagConstraints();
        monthYearGbc.gridx = 0;
        monthYearGbc.gridy = 0;
        monthYearGbc.anchor = GridBagConstraints.WEST;
        monthYearGbc.insets = new Insets(5, 5, 5, 5);

        addLabelAndField(new JLabel("Month"), monthComboBox, monthYearGbc);

        monthYearGbc.gridx = 1;
        addLabelAndField(new JLabel("Year"), yearComboBox, monthYearGbc);
    }

    // EFFECTS: add the income and expense panel to the GUI
    private void addExpenseIncomePanel() {
        JPanel expensePanel = createTablePanel("Expense Table");
        JPanel incomePanel = createTablePanel("Income Table");

        GridBagConstraints expensePanelGbc = new GridBagConstraints();
        expensePanelGbc.gridx = 0;
        expensePanelGbc.gridy = 1;
        expensePanelGbc.gridwidth = 2;
        expensePanelGbc.weightx = 1.0;
        expensePanelGbc.weighty = 0.5;
        expensePanelGbc.fill = GridBagConstraints.BOTH;
        expensePanelGbc.anchor = GridBagConstraints.CENTER;
        expensePanelGbc.insets = new Insets(10, 0, 5, 0);

        GridBagConstraints incomePanelGbc = new GridBagConstraints();
        incomePanelGbc.gridx = 0;
        incomePanelGbc.gridy = 2;
        incomePanelGbc.gridwidth = 2;
        incomePanelGbc.weightx = 1.0;
        incomePanelGbc.weighty = 0.5;
        incomePanelGbc.fill = GridBagConstraints.BOTH;
        incomePanelGbc.anchor = GridBagConstraints.CENTER;
        incomePanelGbc.insets = new Insets(5, 0, 10, 0);

        add(expensePanel, expensePanelGbc);
        add(incomePanel, incomePanelGbc);
    }

    // MODIFIES: this
    // EFFECTS: set up the year and month JComboBox
    private void initialiseJComboBox() {
        String[] months = new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        int startYear = 2000;
        int endYear = Calendar.getInstance().get(Calendar.YEAR) + 1;

        String[] years = new String[endYear - startYear + 1];
        for (int i = 0; i < years.length; i++) {
            years[i] = String.valueOf(startYear + i);
        }

        monthComboBox = new JComboBox<>(months);
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setSelectedItem("2023");
    }

    // MODIFIES: this
    // EFFECTS: create a panel for a table and returns it
    private JPanel createTablePanel(String tableLabel) {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints tableLabelGbc = new GridBagConstraints();
        tableLabelGbc.gridx = 0;
        tableLabelGbc.gridy = 0;
        tableLabelGbc.gridwidth = 2;
        tableLabelGbc.anchor = GridBagConstraints.CENTER;
        tableLabelGbc.insets = new Insets(5, 0, 5, 0);
        JLabel label = new JLabel(tableLabel);
        panel.add(label, tableLabelGbc);

        formatScrollPane(tableLabel, panel);


        return panel;
    }

    // MODIFIES: this
    // EFFECTS: creates and formats a JScroll pane
    private void formatScrollPane(String tableLabel, JPanel panel) {
        GridBagConstraints tableGbc = new GridBagConstraints();
        tableGbc.gridx = 0;
        tableGbc.gridy = 1;
        tableGbc.gridwidth = 2;
        tableGbc.weightx = 1.0;
        tableGbc.weighty = 1.0;
        tableGbc.fill = GridBagConstraints.BOTH;
        tableGbc.anchor = GridBagConstraints.CENTER;

        JScrollPane scrollPane = new JScrollPane();
        JTable table = new JTable();
        scrollPane.setViewportView(table);

        if (tableLabel.equals("Expense Table")) {
            expenseTable = table;
        } else {
            incomeTable = table;
        }

        panel.add(scrollPane, tableGbc);
    }

    // MODIFIES: this
    // EFFECTS: generates the monthly report
    private void generateMonthlyReport() {
        try {
            String selectedMonth = (String) monthComboBox.getSelectedItem();
            Integer selectedYear = Integer.parseInt((String) yearComboBox.getSelectedItem());
            int selectedMonthNumber = monthNameToNumber.get(selectedMonth);

            bm.addBudget(selectedYear, selectedMonthNumber);
            Budget budget = bm.getBudgetMap().get(selectedYear).get(selectedMonthNumber);

            double remainingBudget = budget.getRemainingBudget();
            double amount = budget.getBudget();
            remainingBudgetLabel.setText("Remaining Budget: $" + remainingBudget);
            budgetLabel.setText("Budget: $" + amount);

            updateTables(selectedYear, selectedMonthNumber);

            EventLog.getInstance().logEvent(new Event("Monthly report for "
                    + selectedMonth + " " + selectedYear + " generated"));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid month and year",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }


    // EFFECTS: updates the table based on income and expenses for given month and year
    private void updateTables(Integer year, Integer month) {
        DefaultTableModel expenseTableModel = expenseTableModel();

        if (!te.getMonthlyList(year, month).isEmpty()) {
            for (Expense e : te.getTransactionMap().get(year).get(month)) {
                Object[] rowData = {new SimpleDateFormat("dd-MM-yyyy").format(e.getDate()),
                        e.getAmount(), e.getCategory().getName()};
                expenseTableModel.addRow(rowData);
            }


        } else {
            expenseTableModel.addRow(new Object[]{"No data", "No data", "No data"});
        }
        expenseTable.setModel(expenseTableModel);

        DefaultTableModel incomeTableModel = incomeTableModel();

        if (!ti.getMonthlyList(year, month).isEmpty()) {
            for (Income i : ti.getTransactionMap().get(year).get(month)) {
                Object[] rowData = {new SimpleDateFormat("dd-MM-yyyy").format(i.getDate()), i.getAmount()};
                incomeTableModel.addRow(rowData);
            }


        } else {
            incomeTableModel.addRow(new Object[]{"No data", "No data", "No data"});
        }
        incomeTable.setModel(incomeTableModel);
    }

    // EFFECTS: initialises the expense table model and adds columns to the table
    private DefaultTableModel expenseTableModel() {
        DefaultTableModel expenseTableModel = new DefaultTableModel();
        expenseTableModel.addColumn("Date");
        expenseTableModel.addColumn("Amount");
        expenseTableModel.addColumn("Category");

        return expenseTableModel;
    }

    // EFFECTS: initialises the income table model and adds columns to the table
    private DefaultTableModel incomeTableModel() {
        DefaultTableModel incomeTableModel = new DefaultTableModel();
        incomeTableModel.addColumn("Date");
        incomeTableModel.addColumn("Amount");

        return incomeTableModel;
    }

    // EFFECTS: adds the label and component to the layout given
    private void addLabelAndField(JLabel label, JComponent component, GridBagConstraints gbc) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowPanel.add(label);
        rowPanel.add(component);

        add(rowPanel, gbc);
    }
}
