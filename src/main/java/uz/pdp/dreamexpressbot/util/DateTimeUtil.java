package uz.pdp.dreamexpressbot.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static String formatDate(LocalDateTime orderDate) {
        return orderDate.format(dateFormatter);
    }

    public static String formatTime(LocalDateTime orderTime) {
        return orderTime.format(timeFormatter);
    }
}
