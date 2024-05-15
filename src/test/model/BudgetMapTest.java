package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class BudgetMapTest {

    BudgetMap budgetMap;
    Map<Integer, Map<Integer, Budget>> budgetMapList;
    Budget budget;

    @BeforeEach
    public void runBefore() {
        budgetMap = new BudgetMap();
        budgetMapList = new HashMap<>();
        budget = new Budget();
    }

    @Test
    public void testConstructor() {
        assertEquals(budgetMapList, budgetMap.getBudgetMap());
    }

    @Test
    public void testAddBudget() {
        budgetMap.addBudget(2004, 11);
        assertTrue(budgetMap.getBudgetMap().containsKey(2004));
        assertTrue(budgetMap.getBudgetMap().get(2004).containsKey(11));
        assertEquals(budgetMap.getBudgetMap().get(2004).get(11).getBudget(), budget.getBudget());

        budgetMap.getBudgetMap().get(2004).get(11).setBudget(20000);
        assertEquals(budgetMap.getBudgetMap().get(2004).get(11).getBudget(), 20000);

        budgetMap.addBudget(2004, 11);
        assertEquals(budgetMap.getBudgetMap().get(2004).get(11).getBudget(), 20000);

    }
    @Test
    public void testGetBudgetMap() {
        assertEquals(budgetMapList, budgetMap.getBudgetMap());
    }

}
