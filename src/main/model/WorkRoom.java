package model;

import org.json.JSONObject;

// Represents a workroom to add objects to json file
public class WorkRoom {

    // EFFECTS: returns this as JSON object
    public JSONObject toJson(TransactionExpense te, TransactionIncome ti, BudgetMap bm, CategoryMap cm) {
        JSONObject json = new JSONObject();

        json.put("ExpenseMap", te.toJson());
        json.put("IncomeMap", ti.toJson());
        json.put("BudgetMap", bm.toJson());
        json.put("CategoryMap", cm.toJson());
        json.put("CategoryList", cm.categoryListToJson());
        return json;
    }
}

