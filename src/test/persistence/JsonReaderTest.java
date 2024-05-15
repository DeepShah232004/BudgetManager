package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            TransactionExpense te = reader.readExpenseMap();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWorkRoom.json");
        try {
            TransactionExpense te = reader.readExpenseMap();
            TransactionIncome ti = reader.readIncomeMap();
            BudgetMap bm = reader.readBudgetMap();
            CategoryMap cm = reader.readCategoryMap();

            assertTrue(ti.getTransactionMap().isEmpty());
            assertTrue(te.getTransactionMap().isEmpty());
            assertTrue(bm.getBudgetMap().isEmpty());
            assertTrue(cm.getCategoryMap().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralWorkRoom.json");
        try {
            TransactionExpense te = reader.readExpenseMap();
            TransactionIncome ti = reader.readIncomeMap();
            BudgetMap bm = reader.readBudgetMap();
            CategoryMap cm = reader.readCategoryMap();

            assertFalse(ti.getTransactionMap().isEmpty());
            assertFalse(te.getTransactionMap().isEmpty());
            assertFalse(bm.getBudgetMap().isEmpty());
            assertFalse(cm.getCategoryMap().isEmpty());

            assertEquals(29000, bm.getBudgetMap().get(2004).get(11).getRemainingBudget());
            assertEquals(10000, bm.getBudgetMap().get(2004).get(12).getBudget());
            assertEquals(9000, bm.getBudgetMap().get(2005).get(11).getRemainingBudget());

            assertEquals(1000, te.getTransactionMap().get(2004).get(11).get(0).getAmount());
            assertEquals("Travel", te.getTransactionMap().get(2004).get(11).get(1).getCategory().getName());
            assertEquals(2, te.getTransactionMap().get(2004).get(11).size());

            assertEquals("Grocery", te.getTransactionMap().get(2005).get(11).get(0).getCategory().getName());
            assertEquals(1, te.getTransactionMap().get(2005).get(11).size());

            assertEquals(1000, ti.getTransactionMap().get(2004).get(11).get(0).getAmount());
            assertEquals(1, ti.getTransactionMap().get(2004).get(11).size());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoomWrongDate() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralWorkRoomWrongDate.json");
        try {
            TransactionExpense te = reader.readExpenseMap();
            TransactionIncome ti = reader.readIncomeMap();
            CategoryMap cm = reader.readCategoryMap();
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
