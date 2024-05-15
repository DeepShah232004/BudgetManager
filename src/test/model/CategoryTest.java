package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {
    Category category;

    @BeforeEach
    public void runBefore() {
        category = new Category("food");
    }

    @Test
    public void testConstructor() {
        assertEquals("food", category.getName());
    }

    @Test
    public void testGetName() {
        assertEquals("food", category.getName());
    }

}
