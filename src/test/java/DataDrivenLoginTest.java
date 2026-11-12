
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.practice.ExcelReader;
import java.io.IOException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DataDrivenLoginTest {

    Playwright playwright;
    Browser browser;
    Page page;

    static int count = 0;

    @BeforeMethod
    public void setup() {

        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );

        page = browser.newPage();
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() throws IOException {

        String path = System.getProperty("user.dir")
                + "/test-data/LoginData.xlsx";

        System.out.println("Excel Path: " + path);
        System.out.println("File Exists: " + new java.io.File(path).exists());

        return ExcelReader.readExcel(path, "Sheet1");
    }

    @Test(dataProvider = "loginData")
    public void loginTest(String username, String password, boolean flag) {

        page.navigate("https://www.saucedemo.com/");

        page.locator("#user-name").fill(username);
        page.locator("#password").fill(password);
        page.locator("#login-button").click();

        count++;

        if (flag) {

            assertThat(page).hasURL(
                    "https://www.saucedemo.com/inventory.html"
            );

            System.out.println(count + " Login Successful");
        }
        else {

            assertThat(page.locator("[data-test='error']")).isVisible();

            System.out.println(count + " Login Failed");
        }
    }

    @AfterMethod
    public void tearDown() {

        browser.close();
        playwright.close();
    }
}
