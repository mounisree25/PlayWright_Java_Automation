package listeners;

import base.BaseTest;
import com.microsoft.playwright.Page;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class ScreenshotListeners implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test Started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {

        System.out.println("Test Failed: " + result.getName());

        try {
            BaseTest test = (BaseTest) result.getInstance();
            Page page = test.getPage();

            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/test_case_fail.png")));


        } catch (Exception e) {
            System.out.println("Could not capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getName());
    }
}