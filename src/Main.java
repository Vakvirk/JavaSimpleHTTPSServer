
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// TODO: parametry, formularze i POST, wielowątkowość, posprzątać

public class Main {
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Listening on port 8080 ...");
            while (true) {
                try (Socket client = server.accept()) {

                    // Input i output

                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    OutputStream output = client.getOutputStream();

                    // Zbieranie pierwszej linii żądania

                    String requestLine = reader.readLine();

                    // Zabezpieczenie przed pustymi żądaniami

                    if (requestLine == null || requestLine.isEmpty()) continue;

                    System.out.println("Źądanie: " + requestLine);

                    // logowanie pierwszej linii żądania

                    Logger.log("Żądanie: " + requestLine + " od " + client.getInetAddress());

                    // parsowanie pierwszej linii żądania

                    String[] parts = requestLine.split(" ");
                    if (parts.length < 2) {
                        sendResponse(output, 400, "Bad Request");
                    }

                    String method = parts[0];
                    String path = parts[1];
                    String html;

                    // routing

                    switch (path) {
                        case "/":
                            html = "<html><body><h1>Strona główna</h1></body></html>";
                            sendResponse(output, 200, html);
                            break;
                        case "/hello":
                            html = "<html><body><h1>Hello World!</h1></body></html>";
                            sendResponse(output, 200, html);
                            break;
                        case "/time":
                            DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm");
                            String time = LocalTime.now().format(dtfTime);
                            html = String.format("<html><body><h1> %s </h1></body></html>", time);
                            sendResponse(output, 200, html);
                            break;
                        case "/date":
                            DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault());
                            String date = LocalDate.now().format(dtfDate);
                            html = String.format("<html><body><h1> %s </h1></body></html>", date);
                            sendResponse(output, 200, html);
                        default:
                            sendResponse(output, 404, "Nie znaleziono ścieżki: " + path);
                            break;
                    }


                }
            }
        }
    }

    // Metoda składająca odpowiedź

    private static void sendResponse(OutputStream output, int statusCode, String body) throws IOException {
        String statusText = switch (statusCode) {
            case 200 -> "OK";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            default -> "Internal Server Error";
        };

        String response = "HTTP/1.1 " + statusCode + statusText + "\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" + "Content-Length: " +
                body.getBytes(StandardCharsets.UTF_8).length + "\r\n" + "\r\n" + body;

        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
