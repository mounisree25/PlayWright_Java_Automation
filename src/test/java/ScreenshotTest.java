import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class ScreenshotTest {
    Playwright playwright;
    Browser browser;
    Page page;
    @BeforeMethod
    public void setup() throws IOException
    {
        playwright=Playwright.create();
         browser=playwright.chromium().launch(
                 new BrowserType.LaunchOptions().setHeadless(false)
         );
         page=browser.newPage();

    }
    @Test
    public void takescreenshot()
    {
        page.navigate("https://www.saucedemo.com/");
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/login.png")).setFullPage(true));
    }
@AfterMethod
    public void quit()
{
    browser.close();
    playwright.close();
}


}
