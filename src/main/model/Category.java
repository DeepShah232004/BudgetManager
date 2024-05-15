package model;

import org.json.JSONObject;

import java.util.Objects;


// Represents a category with given name
public class Category {

    private String name;

    // EFFECTS: creates a category object with its name
    public Category(String name) {
        this.name = name;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    // EFFECTS: returns the name of the category
    public String getName() {
        return name;
    }

    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);

        return json;
    }
}
