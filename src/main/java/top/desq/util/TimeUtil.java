package top.desq.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

    /**
     * Gets current date in format dd.MM.yyyy HH:mm:ss
     *
     * @return formatted date
     */
    public static String getFormattedDate() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

    /**
     * Gets beginning of current day
     *
     * @return date
     */
    public static LocalDateTime getStartOfDay() {
        return LocalDate.now().atTime(LocalTime.MIN);
    }

    /**
     * Gets end of current day
     *
     * @return date
     */
    public static LocalDateTime getEndOfDay() {
        return LocalDate.now().atTime(LocalTime.MAX);
    }
}
