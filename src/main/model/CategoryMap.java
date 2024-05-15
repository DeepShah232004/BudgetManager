package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

// Represents a category map with expenses under each category for a month of a year
public class CategoryMap {
    Map<Integer, Map<Integer, Map<String, List<Expense>>>> categoryMap;
    List<Category> categoryList;


    // EFFECTS: Instantiates a categoryMap (hash map)
    public CategoryMap() {
        categoryMap = new HashMap<>();

        categoryList = new ArrayList<>(Arrays.asList(new Category("Food"), new Category("Entertainment"),
                new Category("Travel"), new Category("Grocery"),
                new Category("Utilities"), new Category("Miscellaneous")));
    }

    // MODIFIES: this
    // EFFECTS: adds a category to the categoryList
    public void addCategory(Category c) {
        if (!categoryList.contains(c)) {
            categoryList.add(c);

        }
    }

    // MODIFIES: this
    // EFFECTS: re-defines the categoryList
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    // EFFECTS: returns the category list
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public List<String> getCategoryNameList() {
        List categoryName = new ArrayList<>();
        for (Category c: categoryList) {
            categoryName.add(c.getName());
        }
        return categoryName;
    }

    // MODIFIES: this
    // EFFECTS: creates a map for year->month->category-> and adds expense to its respective list
    public void addExpenseToCategory(Integer year, Integer month, Category c, Expense e) {
        if (!categoryMap.containsKey(year)) {
            this.categoryMap.put(year, new HashMap<>());
        }
        if (!categoryMap.get(year).containsKey(month)) {
            this.categoryMap.get(year).put(month, new HashMap<>());
        }
        if (!categoryMap.get(year).get(month).containsKey(c.getName())) {
            this.categoryMap.get(year).get(month).put(c.getName(), new ArrayList<>());
        }
        this.categoryMap.get(year).get(month).get(c.getName()).add(e);
    }

    // EFFECTS: returns category map
    public Map<Integer, Map<Integer, Map<String, List<Expense>>>> getCategoryMap() {
        return categoryMap;
    }

    // EFFECTS: returns this as JSON array
    public JSONArray toJson() {
        JSONArray jsonArrayYear = new JSONArray();

        for (Map.Entry<Integer, Map<Integer, Map<String, List<Expense>>>> entry: categoryMap.entrySet()) {
            int year = entry.getKey();
            JSONObject yearlyObject = new JSONObject();
            yearlyObject.put(Integer.toString(year), processYearlyData(year));
            jsonArrayYear.put(yearlyObject);
        }
        return jsonArrayYear;
    }

    // EFFECTS: returns the month data as an array
    private JSONArray processYearlyData(int year) {
        JSONArray monthArray = new JSONArray();

        for (Map.Entry<Integer, Map<String, List<Expense>>> monthlyEntry: categoryMap.get(year).entrySet()) {
            int month = monthlyEntry.getKey();
            JSONObject monthlyObject = new JSONObject();
            monthlyObject.put(Integer.toString(month), processMonthlyData(year, month));
            monthArray.put(monthlyObject);
        }
        return monthArray;
    }

    // EFFECTS: returns the category data as an array
    private JSONArray processMonthlyData(int year, int month) {
        JSONArray categoryArray = new JSONArray();
        Map<String, List<Expense>> categoryExpenses = categoryMap.get(year).get(month);

        for (Map.Entry<String, List<Expense>> categoryListEntry: categoryExpenses.entrySet()) {
            String category = categoryListEntry.getKey();
            List<Expense> expenses = categoryListEntry.getValue();
            JSONObject categoryObject = new JSONObject();
            categoryObject.put(category, expensesToJson(expenses));
            categoryArray.put(categoryObject);
        }
        return categoryArray;
    }



    // EFFECTS: returns categoryList as JSON array
    public JSONArray categoryListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Category c: categoryList) {
            jsonArray.put(c.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns expenses as JSON array
    public JSONArray expensesToJson(List<Expense> expenses) {
        JSONArray jsonArray = new JSONArray();

        for (Expense e : expenses) {
            jsonArray.put(e.toJson());
        }
        return jsonArray;
    }
}

