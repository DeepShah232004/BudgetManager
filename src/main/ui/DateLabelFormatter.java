package ui;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Represents a Date formatter
public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);


    // EFFECTS: Parses the provided string text into a Date object
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    // EFFECTS: Converts the provided value (either Date or Calendar) into a string representation and returns it
    @Override
    public String valueToString(Object value) throws ParseException {
        if (value instanceof Date) {
            return dateFormatter.format((Date) value);
        } else if (value instanceof Calendar) {
            return dateFormatter.format(((Calendar) value).getTime());
        } else {
            return "";
        }
    }
}