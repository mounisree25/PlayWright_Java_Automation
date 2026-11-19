
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPAutomation {

    private static final String POWERSHELL_SCRIPT =
            "C:\\Users\\Mounisree\\PlayWrightAutomation\\src\\test\\java\\Read-HclOtp.ps1";

    // Run PowerShell and read OTP messages from Phone Link
    private String readOtpFromPhoneLink() throws Exception {

        ProcessBuilder pb = new ProcessBuilder(
                "powershell.exe",
                "-NoProfile",
                "-ExecutionPolicy", "Bypass",
                "-File", POWERSHELL_SCRIPT
        );

        pb.redirectErrorStream(true);

        Process process = pb.start();

        if (!process.waitFor(15, java.util.concurrent.TimeUnit.SECONDS)) {
            process.destroyForcibly();
            throw new RuntimeException("PowerShell script timed out.");
        }

        StringBuilder output = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        if (process.exitValue() != 0) {
            throw new RuntimeException(
                    "PowerShell failed: " + output
            );
        }

        return output.toString();
    }

    // Extract all four-digit OTPs from PowerShell output
    private Set<String> extractOtps(String output) {

        Set<String> otps = new LinkedHashSet<>();

        Pattern pattern = Pattern.compile("OTP=(\\d{4})");
        Matcher matcher = pattern.matcher(output);

        while (matcher.find()) {
            otps.add(matcher.group(1));
        }

        return otps;
    }

    // Wait for a new OTP that was not present before requesting OTP
    private String waitForNewOtp(Set<String> oldOtps) throws Exception {

        long startTime = System.currentTimeMillis();
        long timeout = 90_000;

        System.out.println("Waiting for new HCLTech SMS...");

        while (System.currentTimeMillis() - startTime < timeout) {

            String output = readOtpFromPhoneLink();

            Set<String> currentOtps = extractOtps(output);

            for (String otp : currentOtps) {

                if (!oldOtps.contains(otp)) {

                    System.out.println("New HCLTech OTP detected.");
                    return otp;
                }
            }

            Thread.sleep(2000);
        }

        throw new RuntimeException(
                "New HCLTech OTP was not received within 90 seconds."
        );
    }

    @Test
    public void test() throws Exception {

        Playwright playwright = Playwright.create();

        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );

        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        try {

            // Step 1: Open HCLTech website
            page.navigate("https://freshers.hcltech.com/");

            page.waitForLoadState();

            System.out.println("Website opened.");

            // Step 2: Click Login tab
            Locator loginTab = page.getByRole(
                    AriaRole.LINK,
                    new Page.GetByRoleOptions().setName("Login")
            );

            loginTab.waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
            );

            loginTab.click();

            System.out.println("Login tab clicked.");

            // Step 3: Enter mobile number
            Locator mobileNumber = page.locator("#otpField");

            mobileNumber.waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
            );

            // Replace this placeholder with your own registered number.
            mobileNumber.fill("9392710360");

            System.out.println("Mobile number entered.");

            // Step 4: Read existing OTPs BEFORE requesting a new one
            System.out.println("Checking existing Phone Link messages...");

            Set<String> oldOtps = extractOtps(
                    readOtpFromPhoneLink()
            );

            System.out.println("Existing HCLTech OTP messages checked.");

            // Step 5: Click Get OTP
            Locator getOtp = page.locator("#getOtpBtn");

            getOtp.waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
            );

            getOtp.click();

            System.out.println("OTP requested.");

            // Step 6: Automatically wait for a new OTP
            String otp = waitForNewOtp(oldOtps);

            // Step 7: Enter OTP using keyboard
            Locator firstOtpBox = page.locator("#digit-1");

            firstOtpBox.waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
            );

            firstOtpBox.click();

// Type all four digits sequentially.
// The website should automatically move between OTP boxes.
            for (int i = 0; i < otp.length(); i++) {

                String digit = String.valueOf(otp.charAt(i));

                page.keyboard().type(digit);

                System.out.println("Entered OTP digit " + (i + 1));

                page.waitForTimeout(700);
            }

            System.out.println("OTP typed using keyboard.");

// Verify all four boxes
            StringBuilder enteredOtp = new StringBuilder();

            for (int i = 1; i <= 4; i++) {

                Locator otpBox = page.locator("#digit-" + i);

                enteredOtp.append(otpBox.inputValue());
            }

            if (!enteredOtp.toString().equals(otp)) {
                throw new RuntimeException(
                        "OTP entry verification failed."
                );
            }

            System.out.println("All four OTP digits verified.");
            // Step 8: Click Submit
            Locator submitButton = page.locator("#otpSubmitBtn");

            submitButton.waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
            );

            submitButton.click();

            System.out.println("OTP submitted.");
            page.waitForTimeout(5000);

            System.out.println("Current URL: " + page.url());
            System.out.println("Page title: " + page.title());

        } finally {

            browser.close();
            playwright.close();
        }
    }
}
