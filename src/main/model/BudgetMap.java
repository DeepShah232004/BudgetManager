package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// Represents a budget map having budget for a month of the year
public class BudgetMap {
    Map<Integer, Map<Integer, Budget>> budgetMap;
    Map<Integer, String> monthMap;

    // EFFECTS: Instantiates a budgetMap (hash map)
    public BudgetMap() {
        budgetMap = new HashMap<>();
        monthMap = new HashMap<>();
        monthMap.put(0, "January");
        monthMap.put(1, "February");
        monthMap.put(2, "March");
        monthMap.put(3, "April");
        monthMap.put(4, "May");
        monthMap.put(5, "June");
        monthMap.put(6, "July");
        monthMap.put(7, "August");
        monthMap.put(8, "September");
        monthMap.put(9, "October");
        monthMap.put(10, "November");
        monthMap.put(11, "December");
    }

    // MODIFIES: this
    // EFFECTS: adds year, the month with an initial budget, if not already present
    public void addBudget(Integer year, Integer month) {
        if (!budgetMap.containsKey(year)) {
            this.budgetMap.put(year, new HashMap<>());
        }
        if (!budgetMap.get(year).containsKey(month)) {
            this.budgetMap.get(year).put(month, new Budget());
        }
//        EventLog.getInstance().logEvent(new Event("Budget with initial amount is added for " + month + " "
//                + year));
    }

    // MODIFIES: this
    // EFFECTS: sets given budget for given year and month
    public void setBudget(Integer year, Integer month, Budget budget) {
        if (!budgetMap.containsKey(year)) {
            this.budgetMap.put(year, new HashMap<>());
        }
        this.budgetMap.get(year).put(month, budget);

        EventLog.getInstance().logEvent(new Event("Budget for " + monthMap.get(month - 1) + " " + year
                + " is set to: " + budget.getBudget()));
    }

    // EFFECTS: returns the budgetMap
    public Map<Integer, Map<Integer, Budget>> getBudgetMap() {
        return budgetMap;
    }

    // EFFECTS: returns this as JSON array
    public JSONArray toJson() {
        JSONArray jsonArrayYear = new JSONArray();
        JSONArray jsonArrayMonth = new JSONArray();

        for (Map.Entry<Integer, Map<Integer, Budget>> entry: budgetMap.entrySet()) {
            JSONArray monthArray = new JSONArray();
            int year = entry.getKey();

            for (Map.Entry<Integer, Budget> monthlyEntry: budgetMap.get(year).entrySet()) {
                int month = monthlyEntry.getKey();
                jsonArrayMonth.put(month);
                Budget budget = monthlyEntry.getValue();

                JSONObject monthlyObject = new JSONObject();
                monthlyObject.put(Integer.toString(month), budget.toJson());
                monthArray.put(monthlyObject);
            }

            JSONObject yearlyObject = new JSONObject();
            yearlyObject.put(Integer.toString(year), monthArray);
            jsonArrayYear.put(yearlyObject);
        }
        return jsonArrayYear;
    }

}
