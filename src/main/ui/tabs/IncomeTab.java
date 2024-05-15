package ui.tabs;

import model.Income;
import model.TransactionIncome;
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
public class IncomeTab extends JPanel {
    private JTextField income;
    private JLabel incomeLabel;
    private JLabel date;
    private JDatePickerImpl datePicker;
    TransactionIncome ti = new TransactionIncome();


    // MODIFIES: this
    // EFFECTS: Initialises and lays out UI Components
    public IncomeTab(TransactionIncome transactionIncome) {
        if (transactionIncome != null) {
            ti = transactionIncome;
        }
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        income = new JTextField(10);
        incomeLabel = new JLabel("Income");
        date = new JLabel("Date");
        datePicker = createDatePicker();

        addLabelAndField(incomeLabel, income, 0);
        addLabelAndField(date, datePicker, 1);

        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(e -> handleSubmitButtonClick());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 0, 0);

        add(submitButton, gbc);
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
    // EFFECTS: adds income to income map
    private void handleSubmitButtonClick() {
        try {

            String enteredIncome = income.getText();
            Double amount = Double.valueOf(enteredIncome);

            Date selectedDate = (Date) datePicker.getModel().getValue();


            if (amount != null && selectedDate != null) {
                Income income1 = new Income(selectedDate, amount);
                ti.addTransaction(income1);
                JOptionPane.showMessageDialog(this, "Income added successfully!",
                        "Expense Added", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter income properly!",
                    "Invalid", JOptionPane.ERROR_MESSAGE);
        }

        income.setText("");
        datePicker.getModel().setValue(null);
    }
}


