import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.regex.*;
import java.util.stream.Stream;

public class PhoneLinkClipboardTest {

    // Phone Link stores its DB under one of these package folders
    private static final String[] ROOTS = {
            "Packages\\MicrosoftWindows.Client.CBS_cw5n1h2txyewy\\LocalCache\\Indexed",
            "Packages\\Microsoft.YourPhone_8wekyb3d8bbwe\\LocalCache\\Indexed"
    };

    private static Path findDb() throws IOException {
        Path local = Paths.get(System.getenv("LOCALAPPDATA"));
        Path newest = null;
        for (String r : ROOTS) {
            Path root = local.resolve(r);
            if (!Files.exists(root)) continue;
            try (Stream<Path> s = Files.walk(root)) {
                Path found = s.filter(p -> p.getFileName().toString().equals("phone.db"))
                        .max((a, b) -> Long.compare(a.toFile().lastModified(), b.toFile().lastModified()))
                        .orElse(null);
                if (found != null) newest = found;
            }
        }
        if (newest == null) throw new IOException("phone.db not found. Is Phone Link set up with Messages synced?");
        return newest;
    }

    /** Returns body of the newest SMS (used as a baseline before requesting OTP). */
    public static String latestMessage() throws Exception {
        Path db = findDb();
        Path tmpDir = Files.createTempDirectory("phonelink");

        // Copy db + WAL/SHM files so we can read a consistent snapshot while Phone Link has it locked
        for (String ext : new String[]{"", "-wal", "-shm"}) {
            Path src = Paths.get(db.toString() + ext);
            if (Files.exists(src)) {
                Files.copy(src, tmpDir.resolve("phone.db" + ext), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        String url = "jdbc:sqlite:" + tmpDir.resolve("phone.db");
        try (Connection c = DriverManager.getConnection(url);
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT body FROM message ORDER BY timestamp DESC LIMIT 1")) {
            return rs.next() ? rs.getString("body") : "";
        }
    }

    /** Waits until a NEW SMS arrives (different from baseline) and returns the 4-digit OTP. */
    public static String waitForOtp(String baseline, int timeoutSeconds) throws Exception {
        long end = System.currentTimeMillis() + timeoutSeconds * 1000L;
        Pattern keyed = Pattern.compile("(?i)(?:otp|code|password)\\D{0,40}(\\d{4})");
        Pattern any = Pattern.compile("\\b(\\d{4})\\b");

        while (System.currentTimeMillis() < end) {
            String latest = latestMessage();
            if (latest != null && !latest.equals(baseline)) {
                Matcher m = keyed.matcher(latest);
                if (m.find()) return m.group(1);
                m = any.matcher(latest);
                if (m.find()) return m.group(1);
            }
            Thread.sleep(2000);
        }
        throw new RuntimeException("No new OTP SMS arrived within " + timeoutSeconds + "s");
    }
}