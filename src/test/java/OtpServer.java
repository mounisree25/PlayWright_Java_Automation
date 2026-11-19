import com.sun.net.httpserver.HttpServer;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.regex.*;

public class OtpServer {

    private final HttpServer server;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public OtpServer(int port) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/otp", exchange -> {
            byte[] bytes;
            try (InputStream is = exchange.getRequestBody()) {
                bytes = is.readAllBytes();
            }
            String body = new String(bytes, StandardCharsets.UTF_8);

            // MacroDroid may also send the text in the query string
            String query = exchange.getRequestURI().getQuery();
            if (query != null) body = body + " " + query;

            System.out.println("Received from phone: " + body);
            queue.offer(body);

            byte[] resp = "OK".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.close();
        });

        server.start();
        System.out.println("OTP server listening on port " + port);
    }

    /** Blocks until the phone sends a message, then returns the 4-digit OTP. */
    public String waitForOtp(int timeoutSeconds) throws Exception {
        String msg = queue.poll(timeoutSeconds, TimeUnit.SECONDS);
        if (msg == null) throw new RuntimeException("No OTP received within " + timeoutSeconds + "s");

        Matcher m = Pattern.compile("(?i)(?:otp|code|password)\\D{0,40}(\\d{4})").matcher(msg);
        if (m.find()) return m.group(1);

        m = Pattern.compile("(?<!\\d)(\\d{4})(?!\\d)").matcher(msg);
        if (m.find()) return m.group(1);

        throw new RuntimeException("No 4-digit OTP found in: " + msg);
    }

    public void stop() {
        server.stop(0);
    }
}