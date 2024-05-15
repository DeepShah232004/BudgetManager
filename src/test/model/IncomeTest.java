package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class IncomeTest {
    Income income;
    Date date;

    @BeforeEach
    public void runBefore() {
        date = new Date(104, 10, 23);
        income = new Income(date, 1000.0);
    }

    @Test
    public void testConstructor() {
        assertEquals(date, income.getDate());
        assertEquals(1000.0, income.getAmount());
    }

    @Test
    public void testGetDate() {
        assertEquals(date, income.getDate());
    }

    @Test
    public void testGetAmount() {
        assertEquals(1000.0, income.getAmount());
    }

}
