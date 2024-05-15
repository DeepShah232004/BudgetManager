package model;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

// Represents an expense with date, amount and category
public class Expense {
    Date date;
    Double amount;
    Category category;

    // EFFECTS: creates an Expense object with date, amount and category.
    public Expense(Date date, Double amount, Category category) {
        this.date = date;
        this.amount = amount;
        this.category = category;
    }

    // EFFECTS: returns the date
    public Date getDate() {
        return date;
    }

    // EFFECTS: return the amount
    public double getAmount() {
        return amount;
    }

    /// EFFECTS: return the category
    public Category getCategory() {
        return category;
    }

    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        json.put("date", dateFormat.format(date));
        json.put("amount", amount);
        json.put("category", category.getName());

        return json;
    }
}
