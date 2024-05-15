package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonWriterTest {

    WorkRoom wr;
    TransactionExpense te;
    TransactionIncome ti;
    BudgetMap bm;
    CategoryMap cm;
    Expense expense1;
    Expense expense2;
    Expense expense3;
    Income income1;
    Income income2;
    Budget budget1;
    Budget budget2;


    @BeforeEach
    void runBefore() {
        wr = new WorkRoom();
        te = new TransactionExpense();
        ti = new TransactionIncome();
        bm = new BudgetMap();
        cm = new CategoryMap();
        expense1 = new Expense(new Date(104, 10, 23), 1000.0, new Category("Food"));
        expense2 = new Expense(new Date(104, 10, 24), 2000.0, new Category("School"));
        expense3 = new Expense(new Date(105, 10, 23), 2000.0, new Category("Food"));
        income1 = new Income(new Date(104, 10, 24), 1000.0);
        income2 = new Income(new Date(105, 10, 23), 4000.0);
        budget1 = new Budget();
        budget2 = new Budget();
    }

    @Test
    void testWriterInvalidFile() {
        try {
            te = new TransactionExpense();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            writer.open();
            writer.write(wr, te, ti, bm, cm);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
            te = reader.readExpenseMap();
            ti = reader.readIncomeMap();
            bm = reader.readBudgetMap();
            cm = reader.readCategoryMap();

            assertTrue(ti.getTransactionMap().isEmpty());
            assertTrue(te.getTransactionMap().isEmpty());
            assertTrue(bm.getBudgetMap().isEmpty());
            assertTrue(cm.getCategoryMap().isEmpty());


        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            te.addTransaction(expense1);
            te.addTransaction(expense2);
            te.addTransaction(expense3);
            ti.addTransaction(income1);
            ti.addTransaction(income2);
            bm.setBudget(2004, 11, budget1);
            bm.setBudget(2004, 11, budget2);
            bm.setBudget(2004, 12, budget2);
            bm.setBudget(2005, 11, budget2);
            cm.addExpenseToCategory(2004, 11, new Category("Food"),  expense1);
            cm.addExpenseToCategory(2004, 11, new Category("School"),  expense2);
            cm.addExpenseToCategory(2005, 11, new Category("Food"),  expense3);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(wr, te, ti, bm, cm);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            te = reader.readExpenseMap();
            ti = reader.readIncomeMap();
            bm = reader.readBudgetMap();
            cm = reader.readCategoryMap();

            assertFalse(ti.getTransactionMap().isEmpty());
            assertFalse(te.getTransactionMap().isEmpty());
            assertFalse(bm.getBudgetMap().isEmpty());
            assertFalse(cm.getCategoryMap().isEmpty());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
