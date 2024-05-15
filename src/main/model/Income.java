package model;

import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.Date;

// Represents an income with date and amount
public class Income {
    Date date;
    Double amount;

    // EFFECTS: creates an Expense object with date, amount and category.
    public Income(Date date, Double amount) {
        this.date = date;
        this.amount = amount;
    }

    // EFFECTS: returns the date
    public Date getDate() {
        return date;
    }

    // EFFECTS: returns the amount
    public double getAmount() {
        return amount;
    }

    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        json.put("date", dateFormat.format(date));
        json.put("amount", amount);

        return json;
    }
}

