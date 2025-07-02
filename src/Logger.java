import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "server.log";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static synchronized void log(String message) {
        String timestamp = LocalDateTime.now().format(dtf);
        String logEntry = timestamp + " - " + message;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Błąd zapisu loga: " + e.getMessage());
        }

    }
}
