package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads each object from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads TransactionExpense from file and returns it;
    // throws IOException if an error occurs reading data from file
    public TransactionExpense readExpenseMap() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        TransactionExpense te = new TransactionExpense();
        parseExpenseMap(jsonObject, te);

        return te;
    }

    // EFFECTS: reads TransactionIncome from file and returns it;
    // throws IOException if an error occurs reading data from file
    public TransactionIncome readIncomeMap() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        TransactionIncome ti = new TransactionIncome();
        parseIncomeMap(jsonObject, ti);

        return ti;
    }

    // EFFECTS: reads BudgetMap from file and returns it;
    // throws IOException if an error occurs reading data from file
    public BudgetMap readBudgetMap() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        BudgetMap bm = new BudgetMap();
        parseBudgetMap(jsonObject, bm);

        return bm;
    }

    // EFFECTS: reads CategoryMap from file and returns it;
    // throws IOException if an error occurs reading data from file
    public CategoryMap readCategoryMap() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        CategoryMap cm = new CategoryMap();
        parseCategoryMap(jsonObject, cm);

        return cm;
    }


    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // MODIFIES: te
    // EFFECTS: parses ExpenseMap from JSON object adds it to the TransactionExpense
    private void parseExpenseMap(JSONObject jsonObject, TransactionExpense te) {
        JSONArray expenseMapArray = jsonObject.getJSONArray("ExpenseMap");

        for (Object yearObject : expenseMapArray) {
            JSONObject yearObj = (JSONObject) yearObject;
            for (String keyYear : yearObj.keySet()) {
                String key = keyYear;
                JSONArray monthArray = yearObj.getJSONArray(key);
                for (Object monthObject : monthArray) {
                    JSONObject monthObj = (JSONObject) monthObject;
                    for (String keyMonth : monthObj.keySet()) {
                        String keyM = keyMonth;
                        JSONArray expenseArray = monthObj.getJSONArray(keyM);
                        for (Object expenseObject : expenseArray) {
                            JSONObject expenseObj = (JSONObject) expenseObject;
                            addExpense(expenseObj, te);
                        }
                    }
                }
            }
        }
    }

    // MODIFIES: ti
    // EFFECTS: parses IncomeMap from JSON object adds it to the TransactionIncome
    private void parseIncomeMap(JSONObject jsonObject, TransactionIncome ti) {
        JSONArray incomeMapArray = jsonObject.getJSONArray("IncomeMap");

        for (Object yearObject : incomeMapArray) {
            JSONObject yearObj = (JSONObject) yearObject;
            for (String keyYear : yearObj.keySet()) {
                String key = keyYear;
                JSONArray monthArray = yearObj.getJSONArray(key);
                for (Object monthObject : monthArray) {
                    JSONObject monthObj = (JSONObject) monthObject;
                    for (String keyMonth : monthObj.keySet()) {
                        String keyM = keyMonth;
                        JSONArray incomeArray = monthObj.getJSONArray(keyM);
                        for (Object expenseObject : incomeArray) {
                            JSONObject expenseObj = (JSONObject) expenseObject;
                            addIncome(expenseObj, ti);
                        }
                    }
                }
            }
        }
    }

    // MODIFIES: cm
    // EFFECTS: parses CategoryMap from JSON object adds it to the CategoryMap
    private void parseCategoryMap(JSONObject jsonObject, CategoryMap cm) {
        JSONArray categoryMapArray = jsonObject.getJSONArray("CategoryMap");
        JSONArray categoryListArray = jsonObject.getJSONArray("CategoryList");

        addCategoryList(cm, categoryListArray);

        for (Object yearObject : categoryMapArray) {
            JSONObject yearObj = (JSONObject) yearObject;
            parseYear(yearObj, cm);
        }
    }

    // MODIFIES: cm
    // EFFECTS: gets the month object from year object
    private void parseYear(JSONObject yearObj, CategoryMap cm) {
        for (String keyYear : yearObj.keySet()) {
            String key = keyYear;
            JSONArray monthArray = yearObj.getJSONArray(key);
            for (Object monthObject : monthArray) {
                JSONObject monthObj = (JSONObject) monthObject;
                parseMonth(key, monthObj, cm);
            }
        }
    }

    // MODIFIES: cm
    // EFFECTS: gets the category object from month object
    private void parseMonth(String keyYear, JSONObject monthObj, CategoryMap cm) {
        for (String keyMonth : monthObj.keySet()) {
            String keyM = keyMonth;
            JSONArray categoryArray = monthObj.getJSONArray(keyM);
            for (Object categoryObject : categoryArray) {
                JSONObject categoryObj = (JSONObject) categoryObject;
                parseCategory(keyYear, keyM, categoryObj, cm);
            }
        }
    }

    // MODIFIES: cm
    // EFFECTS: adds the expense to the category to given month for a given year
    private void parseCategory(String keyYear, String keyMonth, JSONObject categoryObj, CategoryMap cm) {
        for (String keyC : categoryObj.keySet()) {
            String category = keyC;
            JSONArray expenseArray = categoryObj.getJSONArray(category);
            for (Object expenseObject : expenseArray) {
                JSONObject expenseObj = (JSONObject) expenseObject;
                addCategoryExpense(keyYear, keyMonth, category, expenseObj, cm);
            }
        }
    }

    // MODIFIES: cm
    // EFFECTS: replaces the categoryList in cm with a new list
    private void addCategoryList(CategoryMap cm, JSONArray jsonArray) {
        List<Category> categoryList = new ArrayList<>();
        for (Object categoryObject: jsonArray) {
            JSONObject categoryObj = (JSONObject) categoryObject;
            String name = categoryObj.getString("name");
            categoryList.add(new Category(name));
        }
        cm.setCategoryList(categoryList);

    }

    // MODIFIES: cm
    // EFFECTS: adds a category with expense to given month of given year
    private void addCategoryExpense(String yearString, String monthString, String category, JSONObject jsonObject,
                                    CategoryMap cm) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = jsonObject.getString("date");
        Double amount = jsonObject.getDouble("amount");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Invalid Date Format");
        }
        Integer year = Integer.valueOf(yearString);
        Integer month = Integer.valueOf(monthString);
        Category category1 = new Category(category);
        Expense expense = new Expense(date, amount, category1);
        cm.addExpenseToCategory(year, month, category1, expense);
    }

    // MODIFIES: cm
    // EFFECTS: parses BudgetMap from JSON object adds it to the BudgetMap
    private void parseBudgetMap(JSONObject jsonObject, BudgetMap bm) {
        JSONArray incomeMapArray = jsonObject.getJSONArray("BudgetMap");

        for (Object yearObject: incomeMapArray) {
            JSONObject yearObj = (JSONObject) yearObject;
            for (String keyYear: yearObj.keySet()) {
                String key = keyYear;
                JSONArray monthArray = yearObj.getJSONArray(key);
                for (Object monthObject: monthArray) {
                    JSONObject monthObj = (JSONObject) monthObject;
                    for (String keyMonth: monthObj.keySet()) {
                        String keyM = keyMonth;
                        JSONObject budgetObject = (JSONObject) monthObj.get(keyM);
                        addBudget(key, keyM, budgetObject, bm);
                    }
                }
            }
        }
    }

    // MODIFIES: bm
    // EFFECTS: adds a budget to given month of given year
    private void addBudget(String yearString, String monthString, JSONObject jsonObject, BudgetMap bm) {
        Integer year = Integer.valueOf(yearString);
        Integer month = Integer.valueOf(monthString);
        Double budgetAmount = jsonObject.getDouble("budget");
        Double remainingBudget = jsonObject.getDouble("remainingBudget");
        Budget budget = new Budget();
        budget.setBudget(budgetAmount);
        budget.setRemainingBudgetBudget(remainingBudget);
        bm.setBudget(year, month, budget);
    }

    // MODIFIES: te
    // EFFECTS: adds an expense to given month of given year
    private void addExpense(JSONObject jsonObject, TransactionExpense te) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = jsonObject.getString("date");
        Double amount = jsonObject.getDouble("amount");
        String category = jsonObject.getString("category");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Invalid Date Format");
        }
        Expense expense = new Expense(date, amount, new Category(category));
        te.addTransaction(expense);

    }

    // MODIFIES: ti
    // EFFECTS: adds an income to given month of given year
    private void addIncome(JSONObject jsonObject, TransactionIncome ti) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = jsonObject.getString("date");
        Double amount = jsonObject.getDouble("amount");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Invalid Date Format");
        }
        Income income = new Income(date, amount);
        ti.addTransaction(income);

    }
}

