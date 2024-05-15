package model;


import org.json.JSONObject;

// Represents a budget having budget amount and the remainingBudget
public class Budget {
    private static final double INITIAL_BUDGET = 10000;

    private double budget;
    private double remainingBudget;

    //EFFECTS: creates budget object with an initial value for budget and remaining budget
    public Budget() {
        this.budget = INITIAL_BUDGET;
        this.remainingBudget = INITIAL_BUDGET;
    }

    // MODIFIES: this
    // EFFECTS: set a new budget and update remainingBudget accordingly
    public void setBudget(double amount) {
        double extra = amount - budget;
        this.budget = amount;
        this.remainingBudget += extra;


    }

    public void setRemainingBudgetBudget(double amount) {
        this.remainingBudget = amount;

    }

    // MODIFIES: this
    // EFFECTS: decreases remainingBudget by given amount
    public void applyCost(double amount) {
        this.remainingBudget -= amount;
    }

    // EFFECTS: returns remainingBudget
    public double getRemainingBudget() {
        return remainingBudget;
    }

    // EFFECTS: returns budget amount
    public double getBudget() {
        return budget;
    }

    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("budget", budget);
        json.put("remainingBudget", remainingBudget);

        return json;
    }



}
