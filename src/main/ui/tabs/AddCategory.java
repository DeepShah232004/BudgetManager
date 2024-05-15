package ui.tabs;

import model.*;
import model.Event;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

// Represents adds category and set budget functionality
public class AddCategory extends JPanel {
    private JTextField category;
    private JLabel categoryLabel;
    private CategoryMap cm = new CategoryMap();
    private ExpenseTab et;
    private BudgetMap bm = new BudgetMap();
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JTextField budgetTextField;
    private JButton submitButton;
    private JButton setBudgetButton;

    // MODIFIES: this
    // EFFECTS: Initialises and lays out UI Components
    public AddCategory(CategoryMap categoryMap, JPanel expenseTab, BudgetMap budgetMap) {

        initialiseConstructors(categoryMap, expenseTab, budgetMap);
        setMonthComboBox();
        setYearComboBox();

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        category = new JTextField(10);
        categoryLabel = new JLabel("Category");

        JLabel categoryHeading = new JLabel("Add new category");
        categoryHeading.setFont(new Font("Arial", Font.BOLD, 14));

        addMainHeading(categoryHeading, 0);

        JLabel yearHeading = new JLabel("Set budget for the month");
        yearHeading.setFont(new Font("Arial", Font.BOLD, 14));

        addMainHeading(yearHeading, 4);

        addLabelAndField(categoryLabel, category, 1);

        submitButton = new JButton("Add Category");
        submitButton.addActionListener(e -> handleSubmitButtonClick());

        setBudgetButton = new JButton("Set Budget");
        setBudgetButton.addActionListener(e -> handleSetBudgetButtonClick());

        initialiseLabels();
        initialiseButton();
        initialiseJComboBox();
        initialiseBudget();
    }

    // MODIFIES: this
    // EFFECTS: initialises and configures the yearComboBox
    private void setYearComboBox() {
        int startYear = 2000;
        int endYear = Calendar.getInstance().get(Calendar.YEAR) + 1;

        Integer[] years = new Integer[endYear - startYear + 1];
        for (int i = 0; i < years.length; i++) {
            years[i] = (startYear + i);
        }

        yearComboBox = new JComboBox<>(years);
        yearComboBox.setSelectedItem(2023);
    }

    // MODIFIES: this
    // EFFECTS: initialises and configures the yearComboBox
    private void setMonthComboBox() {
        String[] months = new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        monthComboBox = new JComboBox<>(months);
    }

    // MODIFIES: this
    // EFFECTS: Initializes constructors based on provided inputs.
    private void initialiseConstructors(CategoryMap categoryMap, JPanel expenseTab, BudgetMap budgetMap) {
        if (expenseTab != null) {
            et = (ExpenseTab) expenseTab;
        }
        if (categoryMap != null) {
            cm = categoryMap;
        }
        if (budgetMap != null) {
            bm = budgetMap;
        }
    }

    // MODIFIES: this
    // EFFECTS: Initializes and adds labels for year, month, and budget to the UI.
    private void initialiseLabels() {
        GridBagConstraints gbcYearLabel = new GridBagConstraints();
        gbcYearLabel.gridx = 1;
        gbcYearLabel.gridy = 3;
        gbcYearLabel.anchor = GridBagConstraints.WEST;
        gbcYearLabel.insets = new Insets(5, 5, 5, 5);

        GridBagConstraints gbcMonthLabel = new GridBagConstraints();
        gbcMonthLabel.gridx = 1;
        gbcMonthLabel.gridy = 4;
        gbcMonthLabel.anchor = GridBagConstraints.WEST;
        gbcMonthLabel.insets = new Insets(5, 5, 5, 5);

        GridBagConstraints gbcBudgetLabel = new GridBagConstraints();
        gbcBudgetLabel.gridx = 1;
        gbcBudgetLabel.gridy = 5;
        gbcBudgetLabel.anchor = GridBagConstraints.WEST;
        gbcBudgetLabel.insets = new Insets(5, 5, 5, 5);

        add(new JLabel("Year"), gbcYearLabel);
        add(new JLabel("Month"), gbcMonthLabel);
        add(new JLabel("Budget"), gbcBudgetLabel);
    }

    // MODIFIES: this
    // EFFECTS: Initializes and adds buttons for "Add Category" and "Set Budget" to the UI.
    private void initialiseButton() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        add(submitButton, gbc);

        GridBagConstraints gbcSetBudget = new GridBagConstraints();
        gbcSetBudget.gridx = 2;
        gbcSetBudget.gridy = 7;
        gbcSetBudget.gridwidth = 2;
        gbcSetBudget.anchor = GridBagConstraints.CENTER;
        gbcSetBudget.insets = new Insets(10, 0, 10, 0);

        add(setBudgetButton, gbcSetBudget);
    }

    // MODIFIES: this
    // EFFECTS: layouts the JComboBox components for selecting year and month to the UI.
    private void initialiseJComboBox() {
        GridBagConstraints gbcYearComboBox = new GridBagConstraints();
        gbcYearComboBox.gridx = 2;
        gbcYearComboBox.gridy = 3;
        gbcYearComboBox.anchor = GridBagConstraints.CENTER;
        gbcYearComboBox.insets = new Insets(10, 0, 10, 0);

        GridBagConstraints gbcMonthComboBox = new GridBagConstraints();
        gbcMonthComboBox.gridx = 2;
        gbcMonthComboBox.gridy = 4;
        gbcMonthComboBox.anchor = GridBagConstraints.CENTER;
        gbcMonthComboBox.insets = new Insets(10, 0, 10, 0);

        add(yearComboBox, gbcYearComboBox);
        add(monthComboBox, gbcMonthComboBox);
    }

    // MODIFIES: this
    // EFFECTS: lays out the budget text field to the UI.
    private void initialiseBudget() {
        budgetTextField = new JTextField(10);
        GridBagConstraints gbcBudget = new GridBagConstraints();
        gbcBudget.gridx = 2;
        gbcBudget.gridy = 5;
        gbcBudget.anchor = GridBagConstraints.CENTER;
        gbcBudget.insets = new Insets(10, 0, 10, 0);

        add(budgetTextField, gbcBudget);
    }

    // MODIFIES: this
    // EFFECTS: reads input value, set budget, and shows dialog on setting budget
    private void handleSetBudgetButtonClick() {
        try {
            Integer year = (Integer) yearComboBox.getSelectedItem();
            Integer month = monthComboBox.getSelectedIndex() + 1;
            Double amount = Double.parseDouble(budgetTextField.getText());


            bm.addBudget(year, month);



            Budget budget = bm.getBudgetMap().get(year).get(month);
            budget.setBudget(amount);
            bm.setBudget(year, month, budget);

            JOptionPane.showMessageDialog(this, "Budget Set",
                    "Budget", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid budget amount",
                    "Invalid Budget Amount", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds a label and its associated input component to the UI at the specified row
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

    // MODIFIES: this
    // EFFECTS: Reads input values, adds a new category, and shows a dialog when added
    private void handleSubmitButtonClick() {

        String enteredCategory = category.getText();

        if (enteredCategory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a category",
                    "Empty Category", JOptionPane.ERROR_MESSAGE);
        } else {

            if (enteredCategory != null) {
                Category category1 = new Category(enteredCategory);
                cm.addCategory(category1);
                JOptionPane.showMessageDialog(this, "New category added",
                        "Category", JOptionPane.INFORMATION_MESSAGE);
            }

            et.updateCategoryDropdown();
            EventLog.getInstance().logEvent(new Event("Added new category: " + enteredCategory));
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds a main heading label to the UI at the specified row
    private void addMainHeading(JLabel headingLabel, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 5, 5);

        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rowPanel.add(headingLabel);

        add(rowPanel, gbc);
    }
}

