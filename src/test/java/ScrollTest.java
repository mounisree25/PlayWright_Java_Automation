import com.microsoft.playwright.*;
import org.testng.annotations.Test;

public class ScrollTest {

    @Test
    public void scrollPage() {

        Playwright playwright = Playwright.create();

        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );

        Page page = browser.newPage();

        // Open website
        page.navigate("https://www.selenium.dev/");

        // Scroll down by 500 pixels
        page.mouse().wheel(0, 500);

        page.waitForTimeout(2000);

        System.out.println("Scrolled down");

        // Scroll down to the bottom
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)");

        page.waitForTimeout(2000);

        System.out.println("Reached bottom");

        // Scroll back to the top
        page.evaluate("window.scrollTo(0, 0)");

        page.waitForTimeout(2000);

        System.out.println("Reached top");

        browser.close();
        playwright.close();
    }
}