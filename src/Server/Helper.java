package Server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Helper {
    public static String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter) + " | ";
    }
}
