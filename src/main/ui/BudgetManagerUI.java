package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.tabs.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents the BudgetManagerUI
public class BudgetManagerUI extends JFrame {

    private static final String JSON_STORE = "./data/trial.json";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private JTabbedPane sidebar;

    private TransactionIncome transactionIncome;
    private TransactionExpense transactionExpense;
    private CategoryMap categoryMap;
    private BudgetMap budgetMap;
    private WorkRoom workRoom;

    public static void main(String[] args) {
        new BudgetManagerUI();
    }

    // MODIFIES: this
    // EFFECTS: configures the GUI
    public BudgetManagerUI() {
        super("BudgetManager Console");

        initialiseConstruction();


        setSize(WIDTH, HEIGHT);
//        setIconImage(getLogoIcon());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        sidebar = new JTabbedPane();
        sidebar.setTabPlacement(JTabbedPane.LEFT);

        add(sidebar);


        setVisible(true);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                handleWindowOpened();
                loadTabs();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }

    // EFFECTS: initialise the constructor
    public void initialiseConstruction() {
        transactionIncome = new TransactionIncome();
        transactionExpense = new TransactionExpense();
        categoryMap = new CategoryMap();
        budgetMap = new BudgetMap();
        workRoom = new WorkRoom();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: prompts the user to load data
    private void handleWindowOpened() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Do you want to load existing data?",
                "Load Data",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            loadData();
            EventLog.getInstance().logEvent(new Event("Data loaded"));
        }
    }

    // EFFECTS: prompt the user asking if user wants to save data
    private void handleWindowClosing() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Do you want to save the data?",
                "Save Data",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            saveData();
            EventLog.getInstance().logEvent(new Event("Data saved"));
        }
        printLog();
    }

    private void printLog() {
        System.out.println("Log: ");
        for (Event event: EventLog.getInstance()) {
            System.out.println(event.toString() + "\n");
        }

        EventLog.getInstance().clear();
    }

    // EFFECTS: save the data to a file
    public void saveData() {
        try {
            jsonWriter.open();
            jsonWriter.write(workRoom, transactionExpense, transactionIncome, budgetMap, categoryMap);
            jsonWriter.close();
            System.out.println("Saved data to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: load the data to the GUI
    public void loadData() {
        try {
            transactionExpense = jsonReader.readExpenseMap();
            transactionIncome = jsonReader.readIncomeMap();
            budgetMap = jsonReader.readBudgetMap();
            categoryMap = jsonReader.readCategoryMap();
            System.out.println("Loaded data from " + JSON_STORE);
            EventLog.getInstance().clear();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: loads the tabs to GUI
    private void loadTabs() {
        JPanel incomeTab = new IncomeTab(transactionIncome);
        JPanel expenseTab = new ExpenseTab(transactionExpense, categoryMap, budgetMap);
        JPanel addCategory = new AddCategory(categoryMap, (ExpenseTab) expenseTab, budgetMap);
        JPanel monthlyReport = new MonthlyReportTab(transactionExpense, transactionIncome, budgetMap);
        JPanel pieChart = new PieChartTab(categoryMap, transactionExpense);

        sidebar.add(expenseTab, 0);
        sidebar.setTitleAt(0, "Expense");
        sidebar.add(incomeTab, 1);
        sidebar.setTitleAt(1, "Income");
        sidebar.add(addCategory, 2);
        sidebar.setTitleAt(2, "Category/Budget");
        sidebar.add(monthlyReport, 3);
        sidebar.setTitleAt(3, "Monthly Report");
        sidebar.add(pieChart, 4);
        sidebar.setTitleAt(4, "Pie Chart");
    }

//    private Image getLogoIcon() {
//        ImageIcon icon = new ImageIcon("path/to/your/logo.png");
//        return icon.getImage();
//    }
}

