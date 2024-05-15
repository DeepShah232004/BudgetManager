package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionIncomeTest {
    TransactionIncome transactionIncome;
    Map<Integer, Map<Integer, List<Income>>> transaction;
    Income income1;
    Income income2;
    Date date1;
    Date date2;
    Income income3;

    @BeforeEach
    public void runBefore() {
        transaction = new HashMap<>();
        transactionIncome = new TransactionIncome();
        date1 = new Date(104, 10, 23);
        date2 = new Date(104, 10, 24);
        income1 = new Income(date1, 1000.0);
        income2 = new Income(date2, 2000.0);
        income3 = new Income(new Date(104, 11, 23), 3000.0);
    }

    @Test
    public void testConstructor() {
        assertEquals(transaction, transactionIncome.getTransactionMap());
    }

    @Test
    public void testAddTransaction() {
        transactionIncome.addTransaction(income1);
        transaction = transactionIncome.getTransactionMap();
        assertTrue(transaction.containsKey(2004));
        assertTrue(transaction.get(2004).containsKey(11));
        assertTrue(transaction.get(2004).get(11).contains(income1));
    }

    @Test
    public void testTotalEarnedInMonth() {
        transactionIncome.addTransaction(income1);
        transactionIncome.addTransaction(income2);
        assertEquals(3000.0, transactionIncome.totalEarnedInMonth(2004, 11));
    }

    @Test
    public void testGetMonthlyListAdded() {
        transactionIncome.addTransaction(income1);
        transaction.put(2004, new HashMap<>());
        transaction.get(2004).put(11, new ArrayList<>(Arrays.asList(income1)));

        assertEquals(transaction.get(2004), transactionIncome.getTransactionMap().get(2004));

        List<Income> incomeList = new ArrayList<>(Arrays.asList(income1));
        assertEquals(incomeList, transactionIncome.getMonthlyList(2004, 11));

        transactionIncome.addTransaction(income2);
        transaction.get(2004).get(11).add(income2);

        assertEquals(transaction.get(2004), transactionIncome.getTransactionMap().get(2004));

        incomeList.add(income2);
        assertEquals(incomeList, transactionIncome.getMonthlyList(2004, 11));

        transactionIncome.addTransaction(income3);
        assertEquals(income3, transactionIncome.getTransactionMap().get(2004).get(12).get(0));
        assertEquals(1, transactionIncome.getTransactionMap().get(2004).get(12).size());
    }

    @Test
    public void testGetMonthlyListEmpty() {
        //transactionIncome.addTransaction(income1);
        //transactionIncome.addTransaction(income2);
        List<Income> incomeList = new ArrayList<>();
        assertEquals(incomeList, transactionIncome.getMonthlyList(2004, 12));
        transaction.put(2004, new HashMap<>());
        transaction.get(2004).put(12, new ArrayList<>());
        assertEquals(transaction.get(2004), transactionIncome.getTransactionMap().get(2004));
    }

    @Test
    public void testGetTransactionMap() {
        assertEquals(transaction, transactionIncome.getTransactionMap());
    }
}

