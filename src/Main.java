
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Listening on port 8080 ...");
            while (true) {
                try (Socket client = server.accept()) {
                    Date today = new Date();
                    String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
                    client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }
}
