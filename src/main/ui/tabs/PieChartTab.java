package ui.tabs;

import model.CategoryMap;
import model.Event;
import model.EventLog;
import model.Expense;
import model.TransactionExpense;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;
import ui.BudgetManagerUI;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Represents pie chart functionality
public class PieChartTab extends JPanel {

    private CategoryMap cm = new CategoryMap();
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JButton generateChartButton;
    private JPanel pieChartPanel;

    private TransactionExpense te = new TransactionExpense();

    private final Map<String, Integer> monthNameToNumber = new HashMap<>();

    // MODIFIES: this
    // EFFECTS: initialises and lays out the pie chat UI components
    public PieChartTab(CategoryMap categoryMap, TransactionExpense transactionExpense) {

        if (categoryMap != null) {
            cm = categoryMap;
        }

        if (transactionExpense != null) {
            te = transactionExpense;
        }

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initializeMonthMap();
        createComponents();
        generateChartButton.addActionListener(e -> generatePieChart());
    }

    // MODIFIES: this
    // EFFECTS: adds months to monthNameToNumber map
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

    // MODIFIES: this
    // EFFECTS: creates and lays out various components of UI
    private void createComponents() {

        initialiseMonthYear();

        generateChartButton = new JButton("Generate Pie Chart");
        pieChartPanel = new JPanel(new BorderLayout());

        GridBagConstraints generateChartButtonGbc = new GridBagConstraints();
        generateChartButtonGbc.gridx = 2;
        generateChartButtonGbc.gridy = 0;
        generateChartButtonGbc.gridwidth = 2;
        generateChartButtonGbc.anchor = GridBagConstraints.WEST;
        generateChartButtonGbc.insets = new Insets(5, 5, 5, 5);

        add(generateChartButton, generateChartButtonGbc);

        GridBagConstraints pieChartPanelGbc = new GridBagConstraints();
        pieChartPanelGbc.gridx = 0;
        pieChartPanelGbc.gridy = 1;
        pieChartPanelGbc.gridwidth = 3;
        pieChartPanelGbc.weightx = 1.0;
        pieChartPanelGbc.weighty = 1.0;
        pieChartPanelGbc.fill = GridBagConstraints.BOTH;
        pieChartPanelGbc.anchor = GridBagConstraints.CENTER;
        pieChartPanelGbc.insets = new Insets(10, 0, 0, 0);

        add(pieChartPanel, pieChartPanelGbc);
    }

    // MODIFIES: this
    // EFFECTS: assign values to monthComboBox, yearComboBox and lay them out
    private void initialiseMonthYear() {
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

        GridBagConstraints monthYearGbc = new GridBagConstraints();
        monthYearGbc.gridx = 0;
        monthYearGbc.gridy = 0;
        monthYearGbc.anchor = GridBagConstraints.WEST;
        monthYearGbc.insets = new Insets(5, 5, 5, 5);

        addLabelAndField(new JLabel("Month"), monthComboBox, monthYearGbc);

        monthYearGbc.gridx = 1;
        addLabelAndField(new JLabel("Year"), yearComboBox, monthYearGbc);
    }

    // EFFECTS: generate a pie chart
    private void generatePieChart() {
        try {
            String selectedMonth = (String) monthComboBox.getSelectedItem();
            Integer selectedYear = Integer.parseInt((String) yearComboBox.getSelectedItem());
            int selectedMonthNumber = monthNameToNumber.get(selectedMonth);

            Map<String, List<Expense>> categoryExpenses =
                    cm.getCategoryMap().get(selectedYear).get(selectedMonthNumber);


            CategoryDataset dataset = createDataset(categoryExpenses);


            JFreeChart chart = createPieChart(dataset);


            displayChart(chart);

            EventLog.getInstance().logEvent(new Event("Pie chart for " + selectedMonth + " "
                    + selectedYear + " generated"));


        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid month and year",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } catch (NullPointerException np) {
            JOptionPane.showMessageDialog(this, "No expense data added",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    // EFFECTS: create a dataset and return it
    private CategoryDataset createDataset(Map<String, List<Expense>> categoryExpenses) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, List<Expense>> entry : categoryExpenses.entrySet()) {
            String category = entry.getKey();
            double totalAmount = entry.getValue().stream().mapToDouble(Expense::getAmount).sum();
            dataset.addValue(totalAmount, "Categories", category);
        }

        return dataset;
    }

    // EFFECTS: creates the pie chart based on the dataset
    private JFreeChart createPieChart(CategoryDataset dataset) {
        PieDataset pieDataset = DatasetUtilities.createPieDatasetForRow(dataset, 0);
        JFreeChart chart =  ChartFactory.createPieChart(
                "Expense Distribution by Category",
                pieDataset,
                true,   // include legend
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400)); // Adjust the size as needed

        return chart;
    }

    // EFFECTS: display the pie chart
    private void displayChart(JFreeChart chart) {
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Categories", Color.BLUE);

        ChartPanel chartPanel = new ChartPanel(chart);
        pieChartPanel.removeAll();
        pieChartPanel.add(chartPanel, BorderLayout.CENTER);
        pieChartPanel.validate();
    }

    // MODIFIES: this
    // EFFECTS: add label, its component to the given grid bag constraints
    private void addLabelAndField(JLabel label, JComponent component, GridBagConstraints gbc) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowPanel.add(label);
        rowPanel.add(component);

        add(rowPanel, gbc);
    }
}

