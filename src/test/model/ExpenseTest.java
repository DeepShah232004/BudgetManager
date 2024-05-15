package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class ExpenseTest {
    Expense expense;
    Category category;
    Date date;

    @BeforeEach
    public void runBefore() {
        date = new Date(104, 10, 23);
        category = new Category("food");
        expense = new Expense(date, 1000.0, category);
    }

    @Test
    public void testConstructor() {
        assertEquals(category, expense.getCategory());
        assertEquals(date, expense.getDate());
        assertEquals(1000.0, expense.getAmount());
    }

    @Test
    public void testGetDate() {
        assertEquals(date, expense.getDate());
    }

    @Test
    public void testGetAmount() {
        assertEquals(1000.0, expense.getAmount());
    }

    @Test
    public void testGetCategory() {
        assertEquals(category, expense.getCategory());
    }
}
