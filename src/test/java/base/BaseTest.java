package base;

import com.microsoft.playwright.*;
import org.testng.annotations.*;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected Page page;

    @BeforeMethod
    public void setup() {

        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
        );

        page = browser.newPage();
    }

    public Page getPage() {
        return page;
    }

    @AfterMethod
    public void tearDown() {

        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }
}