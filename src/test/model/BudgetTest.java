package model;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;


class BudgetTest {

    Budget budget;

    @BeforeEach
    public void runBefore() {
        budget = new Budget();
    }

    @Test
    public void testConstructor() {
        assertEquals(10000, budget.getBudget());
        assertEquals(10000, budget.getRemainingBudget());
    }

    @Test
    public void testSetBudgetMore() {
        budget.setBudget(20000);
        assertEquals(20000, budget.getBudget());
        assertEquals(20000, budget.getRemainingBudget());
    }

    @Test
    public void testSetBudgetLess() {
        budget.setBudget(5000);
        assertEquals(5000, budget.getBudget());
        assertEquals(5000, budget.getRemainingBudget());
    }

    @Test
    public void testApplyCost() {
        budget.applyCost(5000);
        assertEquals(10000, budget.getBudget());
        assertEquals(5000, budget.getRemainingBudget());
    }

    @Test
    public void testGetRemainingBudget() {
        assertEquals(10000, budget.getRemainingBudget());
    }

    @Test
    public void testGetBudget() {
        assertEquals(10000, budget.getRemainingBudget());
    }
}