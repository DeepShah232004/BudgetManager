package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryMapTest {
    CategoryMap categoryMap;
    Map<Integer, Map<Integer, Map<String, List<Expense>>>> map;
    Category category;
    Category category1;
    Expense expense;
    Expense expense2;
    Date date;
    List<Category> categoryList;

    @BeforeEach
    public void runBefore() {
        categoryMap = new CategoryMap();
        category = new Category("food");
        category1 = new Category("Electronics");
        map = new HashMap<>();
        date = new Date(104, 10, 23);
        expense = new Expense(date, 1000.0, category);
        expense2 = new Expense(new Date(104, 10, 14), 2000.0, category);
        categoryList = new ArrayList<>(Arrays.asList(new Category("Food"), new Category("Entertainment"),
                new Category("Travel"), new Category("Grocery"),
                new Category("Utilities"), new Category("Miscellaneous")));
    }

    @Test
    public void testConstructor() {
        assertEquals(map, categoryMap.getCategoryMap());
    }

    @Test
    public void testAddExpenseToCategory() {
        map = categoryMap.getCategoryMap();
        assertFalse(map.containsKey(2004));
        categoryMap.addExpenseToCategory(2004, 11, category, expense);
        assertTrue(map.containsKey(2004));
        assertTrue(map.get(2004).containsKey(11));
        assertTrue(map.get(2004).get(11).containsKey(category.getName()));
        assertTrue(map.get(2004).get(11).get(category.getName()).contains(expense));

        categoryMap.addExpenseToCategory(2004, 11, category, expense2);
        assertTrue(map.get(2004).get(11).get(category.getName()).contains(expense2));
    }

    @Test
    public void testGetCategoryMap() {
        assertEquals(map, categoryMap.getCategoryMap());
    }

    @Test
    public void testAddCategory() {
        List<String> categoryListName = new ArrayList<>();
        List<String> categoryMapListName = new ArrayList<>();
        categoryList.add(category1);
        categoryMap.addCategory(category1);

        for (Category c: categoryList) {
            categoryListName.add(c.getName());
        }

        for (Category c: categoryMap.getCategoryList()) {
            categoryMapListName.add(c.getName());
        }

        assertEquals(categoryListName, categoryMapListName);

        categoryMap.addCategory(category);
        categoryListName.add("food");

        assertEquals(categoryListName, categoryMap.getCategoryNameList());

        categoryMap.addCategory(category);
        assertEquals(categoryListName, categoryMap.getCategoryNameList());
    }

    @Test
    public void testGetCategoryNameList() {
        ArrayList<String> categoryList = new ArrayList<>();
        for (String c: categoryMap.getCategoryNameList()) {
            categoryList.add(c);
        }

        assertEquals(categoryList, categoryMap.getCategoryNameList());
    }

    @Test
    public void testGetCategory() {
        List<String> categoryListName = new ArrayList<>();
        List<String> categoryMapListName = new ArrayList<>();


        for (Category c: categoryList) {
            categoryListName.add(c.getName());
        }

        for (Category c: categoryMap.getCategoryList()) {
            categoryMapListName.add(c.getName());
        }

        assertEquals(categoryListName, categoryMapListName);
    }

    @Test
    public void testHashCodeDifferentName() {
        assertNotEquals(category1.hashCode(), category.hashCode());
    }

    @Test
    public void testHashCodeConsistency() {
        assertEquals(category.hashCode(), category.hashCode());
    }

    @Test
    public void testHashCodeSameNames() {
        Category category2 = new Category("food");
        assertNotEquals(category.hashCode(), category2.hashCode());
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(category.equals(category));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(category.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(category.equals("NotACategory"));
    }

    @Test
    public void testEqualsWithDifferentName() {
        assertFalse(category1.equals(category));
    }

    @Test
    public void testEqualsWithSameName() {
        Category category2 = new Category("food");
        assertTrue(category.equals(category2));
    }

}
