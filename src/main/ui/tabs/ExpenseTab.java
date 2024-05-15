package ui.tabs;

import model.*;
import ui.BudgetManagerUI;
import ui.DateLabelFormatter;


import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Properties;

// Represents adding expense functionality
public class ExpenseTab extends JPanel {
    private JTextField expense;
    private JLabel expenseLabel;
    private JLabel date;
    private JDatePickerImpl datePicker;
    private JComboBox<String> categoryDropdown;
    TransactionExpense te = new TransactionExpense();
    CategoryMap cm = new CategoryMap();
    BudgetMap bm = new BudgetMap();
    private JButton submitButton;


    // MODIFIES: this
    // EFFECTS: Initialises and lays out UI Components
    public ExpenseTab(TransactionExpense transactionExpense, CategoryMap categoryMap,
                      BudgetMap budgetMap) {


        initialiseConstructors(transactionExpense, categoryMap, budgetMap);

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initialiseComponents();

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleSubmitButtonClick());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 0, 0);

        add(submitButton, gbc);
    }


    // EFFECTS: lays out the UI components
    private void initialiseComponents() {
        expense = new JTextField(10);
        expenseLabel = new JLabel("Expense");
        date = new JLabel("Date");
        datePicker = createDatePicker();
        if (cm.getCategoryList() != null) {
            categoryDropdown = new JComboBox<>(cm.getCategoryNameList().toArray(new String[0]));
        }

        addLabelAndField(expenseLabel, expense, 0);
        addLabelAndField(date, datePicker, 1);
        addLabelAndField(new JLabel("Category"), categoryDropdown, 2);
    }

    // EFFECTS: initialise the constructors
    private void initialiseConstructors(TransactionExpense transactionExpense, CategoryMap categoryMap,
                                   BudgetMap budgetMap) {
        if (transactionExpense != null) {
            te = transactionExpense;
        }

        if (categoryMap != null) {
            cm = categoryMap;
        }

        if (budgetMap != null) {
            bm = budgetMap;
        }
    }

    // EFFECTS: add the label, component at the given row
    private void addLabelAndField(JLabel label, JComponent component, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rowPanel.add(label);
        rowPanel.add(component);

        add(rowPanel, gbc);
    }

    // EFFECTS: creates a date picker
    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        return datePicker;
    }

    // MODIFIES: this
    // EFFECTS: adds expense to the expense map
    private void handleSubmitButtonClick() {
        try {
            String enteredIncome = expense.getText();
            Double amount = Double.valueOf(enteredIncome);
            String categoryName = (String) categoryDropdown.getSelectedItem();

            Date selectedDate = (Date) datePicker.getModel().getValue();


            if (amount != null && selectedDate != null && categoryName != null) {
                Expense expense1 = new Expense(selectedDate, amount, new Category(categoryName));
                te.addTransaction(expense1);
                updateBudget(selectedDate.getYear() + 1900, selectedDate.getMonth() + 1,
                        new Category(categoryName), expense1);
                JOptionPane.showMessageDialog(this, "Expense added successfully!",
                        "Expense Added", JOptionPane.INFORMATION_MESSAGE);
            }

            updateCategoryDropdown();

            expense.setText("");
            datePicker.getModel().setValue(null);


        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter expense properly!",
                    "Invalid", JOptionPane.WARNING_MESSAGE);
        }

    }

    // MODIFIES: this
    // EFFECTS: updates the category dropdown when new category is added
    public void updateCategoryDropdown() {
        if (cm.getCategoryList() != null) {
            categoryDropdown.setModel(new DefaultComboBoxModel<>(cm.getCategoryNameList().toArray(new String[0])));
            categoryDropdown.revalidate();
            categoryDropdown.repaint();
        }
    }

    // MODIFIES: this
    // EFFECTS: updates remaining budget for month of the expense
    public void updateBudget(Integer year, Integer month, Category category, Expense e) {
        Double amount = e.getAmount();
        bm.addBudget(year, month);
        cm.addExpenseToCategory(year, month, category, e);
        Budget budget = bm.getBudgetMap().get(year).get(month);
        budget.applyCost(amount);
    }
}


