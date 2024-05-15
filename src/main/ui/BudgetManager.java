package ui;

import model.*;

import javax.swing.*; // will use later

import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BudgetManager  {
    private static final String JSON_STORE = "./data/budgetManager.json";
    private static final int TAB = 4;

    private Scanner input;
    private TransactionExpense transactionExpense;
    private TransactionIncome transactionIncome;
    private BudgetMap budgetMap;
    private CategoryMap categoryMap;
    private WorkRoom workRoom;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: creates a budgetManager object, instantiating necessary objects and calls the runBudgetManager function
    public BudgetManager() throws FileNotFoundException {
        transactionExpense = new TransactionExpense();
        transactionIncome = new TransactionIncome();
        categoryMap = new CategoryMap();
        budgetMap = new BudgetMap();
        workRoom = new WorkRoom();

        input = new Scanner(System.in);

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        runBudgetManager();

    }

    // EFFECTS: runs the application until user quits
    private void runBudgetManager() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayMenu();
            System.out.println("Enter choice: ");
            command = input.next().toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nGoodbye");
    }

    // EFFECTS: runs the code based on the users choice
    private void processCommand(String command) {
        if (command.equals("e")) {
            doAddExpense();
        } else if (command.equals("mr")) {
            doShowTransaction();
        } else if (command.equals("i")) {
            doAddIncome();
        } else if (command.equals("b")) {
            doSetBudget();
        } else if (command.equals("ac")) {
            doAddCategory();
        } else if (command.equals("ce")) {
            doCategoryExpense();
        } else if (command.equals("sd")) {
            saveWorkRoom();
        } else if (command.equals("ld")) {
            loadWorkRoom();
        }
    }

    // EFFECTS: displays the choices the user can choose from
    private void displayMenu() {
        System.out.println("\te -> Add expense");
        System.out.println("\ti -> Add income");
        System.out.println("\tmr -> Monthly report");
        System.out.println("\tb -> Set monthly budget");
        System.out.println("\tac -> Add Category");
        System.out.println("\tce -> Category-wise Expenses");
        System.out.println("\tsd -> Save Data");
        System.out.println("\tld -> Load Data");
        System.out.println("\tq -> Quit");
    }

    // MODIFIES: transactionExpense, budgetMap, categoryMap
    // EFFECTS: adds expense to the map transactionExpense (for given year and month) ,
    //          adds expense category wise to the map in categoryMap, assigns initial budget to the given year and
    //          month, also reduces the remaining budget for that month of the given year.
    public void doAddExpense() {
        Date date = manageDate();

        System.out.print("Enter amount: ");
        Double amount = input.nextDouble();

        getCategoryNameList();
        System.out.print("Enter category type: ");
        Integer index = input.nextInt();
        Category category = categoryMap.getCategoryList().get(index);

        Expense e = new Expense(date, amount, category);
        transactionExpense.addTransaction(e);

        budgetMap.addBudget(date.getYear() + 1900, date.getMonth() + 1);
        categoryMap.addExpenseToCategory(date.getYear() + 1900, date.getMonth() + 1, category, e);
        Budget budget = budgetMap.getBudgetMap().get(date.getYear() + 1900).get(date.getMonth() + 1);
        budget.applyCost(amount);
        if (budget.getRemainingBudget() > 0) {
            System.out.println("Remaining budget for the month: " + budget.getRemainingBudget());
        } else {
            System.out.println("Budget for the month crossed !!");
        }
    }

    // MODIFIES: transactionIncome
    // EFFECTS: adds income to the map transactionExpense (for given year and month)
    public void doAddIncome() {
        Date date = manageDate();

        System.out.print("Enter amount: ");
        Double amount = input.nextDouble();

        Income i = new Income(date, amount);
        transactionIncome.addTransaction(i);
    }

    // EFFECTS: Shows the income and expense transactions done in the month of the year provided.
    //          It shows remaining budget and total spent and earned also.
    public void doShowTransaction() {
        System.out.println("Enter month: ");
        Integer month = input.nextInt();
        System.out.println("Enter year: ");
        Integer year = input.nextInt();
        budgetMap.addBudget(year, month);
        Budget budget = budgetMap.getBudgetMap().get(year).get(month);
        if (transactionExpense.getMonthlyList(year, month).isEmpty()
                && transactionIncome.getMonthlyList(year, month).isEmpty()) {
            bothExpenseIncomeEmpty(budget);
        } else if (transactionIncome.getMonthlyList(year, month).isEmpty()) {
            onlyIncomeEmpty(year, month, budget);
        } else if (transactionExpense.getMonthlyList(year, month).isEmpty()) {
            onlyExpenseEmpty(year, month, budget);
        } else {
            bothExpenseIncomeNotEmpty(year, month, budget);
        }
    }

    // MODIFIES: budgetMap
    // EFFECTS: sets the budget by the amount provided by the user for the given month of the given year.
    public void doSetBudget() {
        System.out.print("Month: ");
        Integer month = input.nextInt();

        System.out.println("Year: ");
        Integer year = input.nextInt();

        System.out.print("Amount: ");
        Double amount = input.nextDouble();

        budgetMap.addBudget(year, month);

        Budget budget = budgetMap.getBudgetMap().get(year).get(month);

        budget.setBudget(amount);

    }

    // EFFECTS: shows the category wise expense for the given month of the given year
    public void doCategoryExpense() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        System.out.print("Month: ");
        Integer month = input.nextInt();

        System.out.println("Year: ");
        Integer year = input.nextInt();

        if (!categoryMap.getCategoryMap().containsKey(year)
                || !categoryMap.getCategoryMap().get(year).containsKey(month)) {
            System.out.println("No expenses added");
        } else {

            for (Map.Entry<String, List<Expense>> entry
                    : categoryMap.getCategoryMap().get(year).get(month).entrySet()) {
                String c = entry.getKey();
                List<Expense> expenseList = entry.getValue();
                System.out.println(c + ":");
                for (Expense e : expenseList) {
                    System.out.println(dateFormat.format(e.getDate()) + " - " + e.getAmount() + " - "
                            + e.getCategory().getName());
                }
            }
        }
    }

    // MODIFIES: categoryList
    // EFFECTS: it adds user given category to the list of categories.
    public void doAddCategory() {
        System.out.print("Name: ");
        String name = input.next();
        Category c = new Category(name);
        categoryMap.addCategory(c);
    }

    // EFFECTS: prints the name of all categories in the list.
    public void getCategoryNameList() {
        for (int i = 0; i < categoryMap.getCategoryList().size(); i++) {
            System.out.println(i + ". " + categoryMap.getCategoryList().get(i).getName());
        }
    }

    // EFFECTS: gives the monthly statement for expense/income
    public void getMonthlyStatement(Integer year, Integer month, String type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if (type.equals("income")) {
            for (Income i: transactionIncome.getTransactionMap().get(year).get(month)) {
                System.out.println(dateFormat.format(i.getDate()) +  " - " + i.getAmount());
            }
        } else if (type.equals("expense")) {
            for (Expense e: transactionExpense.getTransactionMap().get(year).get(month)) {
                System.out.println(dateFormat.format(e.getDate()) +  " - " + e.getAmount() + " - "
                        + e.getCategory().getName());
            }
        }
    }

    // REQUIRES: Date is the format ("dd-MM-yyyy")
    // EFFECTS: returns the date in the Date data type (formatted as "dd-MM-yyyy")
    public Date manageDate() {
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        boolean valid = false;
        while (!valid) {
            System.out.println("Date (dd-mm-yyyy): ");
            String dateString = input.next();

            try {
                date = dateFormat.parse(dateString);
                valid = true;
            } catch (ParseException e) {
                System.out.println("Invalid Date Format");
            }
        }
        return date;
    }

    // EFFECTS: prints the monthly statement when both income and expense are empty for the given month and year.
    public void bothExpenseIncomeEmpty(Budget budget) {
        System.out.println("Expense: ");
        System.out.println("Not added");
        System.out.println("Total Spent: 0");
        System.out.println("\n Income");
        System.out.println("Not added");
        System.out.println("Total Earned: 0");
        System.out.println("\nRemaining Budget: " + budget.getRemainingBudget());
        System.out.println("\nNet Flow: 0\n");
    }

    // EFFECTS: prints the monthly statement when only income is empty for the given month and year.
    public void onlyIncomeEmpty(Integer year, Integer month, Budget budget) {
        System.out.println("Expenses: ");
        getMonthlyStatement(year, month, "expense");
        System.out.println("Total Spent: " + transactionExpense.totalSpentInMonth(year, month));
        System.out.println("\n Income: ");
        System.out.println("Not added");
        System.out.println("Total Earned: 0");
        System.out.println("\nRemaining Budget: " + budget.getRemainingBudget());
        System.out.println("\nNet Flow: " + (0 - transactionExpense.totalSpentInMonth(year, month)) + "\n");
    }

    // EFFECTS: prints the monthly statement when only expense is empty for the given month and year.
    public void onlyExpenseEmpty(Integer year, Integer month, Budget budget) {
        System.out.println("Expenses: ");
        System.out.println("Not added");
        System.out.println("Total Spent = 0");
        System.out.println("\n Income: ");
        getMonthlyStatement(year, month, "income");
        System.out.println("Total Earned: " + transactionIncome.totalEarnedInMonth(year, month));
        System.out.println("\nRemaining Budget: " + budget.getRemainingBudget());
        System.out.println("\nNet Flow: " + transactionExpense.totalSpentInMonth(year, month) + "\n");
    }

    // EFFECTS: prints the monthly statement when both income and expense are not empty for the given month and year.
    public void bothExpenseIncomeNotEmpty(Integer year, Integer month, Budget budget) {
        System.out.println("Expenses: ");
        getMonthlyStatement(year, month, "expense");
        System.out.println("Total Spent: " + transactionExpense.totalSpentInMonth(year, month));
        System.out.println("\n Income: ");
        getMonthlyStatement(year, month, "income");
        System.out.println("Total Earned: " + transactionIncome.totalEarnedInMonth(year, month));
        System.out.println("\nRemaining Budget: " + budget.getRemainingBudget());
        System.out.println("\nNet Flow: " + (transactionIncome.totalEarnedInMonth(year, month)
                - transactionExpense.totalSpentInMonth(year, month)) + "\n");
    }

    // EFFECTS: saves the various classes to file
    public void saveWorkRoom() {
        try {
            jsonWriter.open();
            jsonWriter.write(workRoom, transactionExpense, transactionIncome, budgetMap, categoryMap);
            jsonWriter.close();
            System.out.println("Saved " + "data" + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads data from file
    public void loadWorkRoom() {
        try {
            transactionExpense = jsonReader.readExpenseMap();
            transactionIncome = jsonReader.readIncomeMap();
            budgetMap = jsonReader.readBudgetMap();
            categoryMap = jsonReader.readCategoryMap();
            System.out.println("Loaded " + "data" + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }


}
