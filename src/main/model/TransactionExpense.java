package model;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

// Represents an Expense Map with list of expenses for a month in a given year
public class TransactionExpense {
    Map<Integer, Map<Integer, List<Expense>>> transaction;
    Map<Integer, String> monthMap;

    // EFFECTS: create a transaction expense hashmap
    public TransactionExpense() {
        transaction = new HashMap<>();
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
    // EFFECTS: add expense to the list of the given year and given month
    public void addTransaction(Expense expense) {
        Date date = expense.getDate();
        if (!transaction.containsKey(date.getYear() + 1900)) {
            transaction.put(date.getYear() + 1900, new HashMap<>());
        }
        if (!transaction.get(date.getYear() + 1900).containsKey(date.getMonth() + 1)) {
            transaction.get(date.getYear() + 1900).put(date.getMonth() + 1, new ArrayList<>());
        }
        transaction.get(date.getYear() + 1900).get(date.getMonth() + 1).add(expense);

        EventLog.getInstance().logEvent(new Event("Expense: " + expense.getAmount() + " for "
                + monthMap.get(date.getMonth())
                + " " + (date.getYear() + 1900) + " added"));
    }

    // REQUIRES: expense for that year and month is already added
    // EFFECTS: returns total spent in given year and month
    public double totalSpentInMonth(Integer year, Integer month) {
        double total = 0;
        for (Expense e: transaction.get(year).get(month)) {
            total += e.getAmount();
        }
        return total;
    }


    // EFFECTS: return the expense list for given year and month
    public List<Expense> getMonthlyList(Integer year, Integer month) {
        if (!transaction.containsKey(year)) {
            transaction.put(year, new HashMap<>());
        }
        if (!transaction.get(year).containsKey(month)) {
            transaction.get(year).put(month, new ArrayList<>());
        }
        return transaction.get(year).get(month);
    }

    // EFFECTS: return the expense map
    public Map<Integer, Map<Integer, List<Expense>>> getTransactionMap() {
        return transaction;
    }

    // EFFECTS: returns this as JSON array
    public JSONArray toJson() {
        JSONArray jsonArrayYear = new JSONArray();
        

        for (Map.Entry<Integer, Map<Integer, List<Expense>>> entry: transaction.entrySet()) {
            JSONArray monthArray = new JSONArray();
            int year = entry.getKey();

            for (Map.Entry<Integer, List<Expense>> monthlyEntry: transaction.get(year).entrySet()) {
                int month = monthlyEntry.getKey();
                List<Expense> expenses = monthlyEntry.getValue();

                JSONObject monthlyObject = new JSONObject();
                monthlyObject.put(Integer.toString(month), expensesToJson(expenses));
                monthArray.put(monthlyObject);
            }

            JSONObject yearlyObject = new JSONObject();
            yearlyObject.put(Integer.toString(year), monthArray);
            jsonArrayYear.put(yearlyObject);
        }
        return jsonArrayYear;
    }

    // EFFECTS: returns the expenses as a json array
    public JSONArray expensesToJson(List<Expense> expenses) {
        JSONArray jsonArray = new JSONArray();

        for (Expense e: expenses) {
            jsonArray.put(e.toJson());
        }
        return jsonArray;
    }
}