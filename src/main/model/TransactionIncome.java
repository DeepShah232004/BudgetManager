package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

// Represents an Income Map with list of incomes for a month in a given year
public class TransactionIncome {
    Map<Integer, Map<Integer, List<Income>>> transaction;
    Map<Integer, String> monthMap;

    // EFFECTS: create a transaction income hashmap
    public TransactionIncome() {
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
    // EFFECTS: add income to the list of the given year and given month
    public void addTransaction(Income income) {
        Date date = income.getDate();
        if (!transaction.containsKey(date.getYear() + 1900)) {
            transaction.put(date.getYear() + 1900, new HashMap<>());
        }
        if (!transaction.get(date.getYear() + 1900).containsKey(date.getMonth() + 1)) {
            transaction.get(date.getYear() + 1900).put(date.getMonth() + 1, new ArrayList<>());
        }
        transaction.get(date.getYear() + 1900).get(date.getMonth() + 1).add(income);

        EventLog.getInstance().logEvent(new Event("Income: " + income.getAmount() + " for "
                + monthMap.get(date.getMonth())
                + " " + (date.getYear() + 1900) + " added"));
    }

    // REQUIRES: income for that year and month is already added
    // EFFECTS: returns total earned in given year and month
    public double totalEarnedInMonth(Integer year, Integer month) {
        double total = 0;
        for (Income e: transaction.get(year).get(month)) {
            total += e.getAmount();
        }
        return total;
    }


    // EFFECTS: return the income list for given year and month
    public List<Income> getMonthlyList(Integer year, Integer month) {
        if (!transaction.containsKey(year)) {
            transaction.put(year, new HashMap<>());
        }
        if (!transaction.get(year).containsKey(month)) {
            transaction.get(year).put(month, new ArrayList<>());
        }
        return transaction.get(year).get(month);
    }

    // returns the income map
    public Map<Integer, Map<Integer, List<Income>>> getTransactionMap() {
        return transaction;
    }

    // EFFECTS: returns this as JSON array
    public JSONArray toJson() {
        JSONArray jsonArrayYear = new JSONArray();

        for (Map.Entry<Integer, Map<Integer, List<Income>>> entry: transaction.entrySet()) {
            JSONArray monthArray = new JSONArray();
            int year = entry.getKey();

            for (Map.Entry<Integer, List<Income>> monthlyEntry: transaction.get(year).entrySet()) {
                int month = monthlyEntry.getKey();
                List<Income> incomes = monthlyEntry.getValue();
                JSONObject monthlyObject = new JSONObject();
                monthlyObject.put(Integer.toString(month), incomesToJson(incomes));
                monthArray.put(monthlyObject);
            }

            JSONObject yearlyObject = new JSONObject();
            yearlyObject.put(Integer.toString(year), monthArray);
            jsonArrayYear.put(yearlyObject);
        }
        return jsonArrayYear;
    }

    // EFFECTS: returns incomes as JSON array
    public JSONArray incomesToJson(List<Income> incomes) {
        JSONArray jsonArray = new JSONArray();

        for (Income i: incomes) {
            jsonArray.put(i.toJson());
        }
        return jsonArray;
    }
}
