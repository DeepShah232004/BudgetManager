package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    private Event e;
    private Date d;

    //NOTE: these tests might fail if time at which line (2) below is executed
    //is different from time that line (1) is executed.  Lines (1) and (2) must
    //run in same millisecond for this test to make sense and pass.

    @BeforeEach
    public void runBefore() {
        e = new Event("Expense added");   // (1)
        d = Calendar.getInstance().getTime();   // (2)
    }

    @Test
    public void testEvent() {
        assertEquals("Expense added", e.getDescription());
        assertEquals(d.toString(), e.getDate().toString());
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Expense added", e.toString());
    }

    @Test
    void testEqualsNull() {
        Event event = new Event("Test event");
        assertFalse(event.equals(null));
    }

    @Test
    void testEqualsDifferentClass() {
        Event event = new Event("Test event");
        assertFalse(event.equals("Not an event"));
    }

    @Test
    void testHashCodeEqualEvents() {
        Event event1 = new Event("Test event");
        Event event2 = new Event("Test event");
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void testHashCodeDifferentEvents() {
        Event event1 = new Event("Event 1");
        Event event2 = new Event("Event 2");
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

}
