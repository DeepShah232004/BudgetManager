package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class TransactionExpenseTest {

    TransactionExpense transactionExpense;
    Map<Integer, Map<Integer, List<Expense>>> transaction;
    Expense expense1;
    Expense expense2;
    Category category;
    Date date1;
    Date date2;

    @BeforeEach
    public void runBefore() {
        transaction = new HashMap<>();
        transactionExpense = new TransactionExpense();
        date1 = new Date(104, 10, 23);
        date2 = new Date(104, 10, 24);
        category = new Category("food");
        expense1 = new Expense(date1, 1000.0, category);
        expense2 = new Expense(date2, 2000.0, category);
    }

    @Test
    public void testConstructor() {
        assertEquals(transaction, transactionExpense.getTransactionMap());
    }

    @Test
    public void testAddTransaction() {
        transactionExpense.addTransaction(expense1);
        transaction = transactionExpense.getTransactionMap();
        assertTrue(transaction.containsKey(2004));
        assertTrue(transaction.get(2004).containsKey(11));
        assertTrue(transaction.get(2004).get(11).contains(expense1));
    }

    @Test
    public void testTotalSpentInMonth() {
        transactionExpense.addTransaction(expense1);
        transactionExpense.addTransaction(expense2);
        assertEquals(3000.0, transactionExpense.totalSpentInMonth(2004, 11));
    }

    @Test
    public void testGetMonthlyListEmpty() {
        //transactionExpense.addTransaction(expense1);
        //transactionExpense.addTransaction(expense2);
        List<Expense> expenseList = new ArrayList<>();
        assertEquals(expenseList, transactionExpense.getMonthlyList(2004, 12));

        transaction.put(2004, new HashMap<>());
        transaction.get(2004).put(12, new ArrayList<>());
        assertEquals(transaction.get(2004), transactionExpense.getTransactionMap().get(2004));
    }

    @Test
    public void testGetMonthlyListAdded() {
        transactionExpense.addTransaction(expense1);
        transactionExpense.addTransaction(expense2);
        List<Expense> expenseList = new ArrayList<>(Arrays.asList(expense1, expense2));
        assertEquals(expenseList, transactionExpense.getMonthlyList(2004, 11));

    }

    @Test
    public void testGetTransactionMap() {
        assertEquals(transaction, transactionExpense.getTransactionMap());
    }
}
