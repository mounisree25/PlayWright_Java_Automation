import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DataDrivenLoginTest {
 Playwright playwright;
 Browser browser;
 Page page;
 static int count=0;
 @BeforeMethod
    public void setup()
 {
     playwright=Playwright.create();
     browser=playwright.chromium().launch(
             new BrowserType.LaunchOptions().setHeadless(false)
     );
     page=browser.newPage();

 }
 @DataProvider(name="loginData")
    public Object[][] getloginData()
 {
     return new Object[][]{
             {"standard_user","secret_sauce",true},
             {"locked_out_user","secret_sauce",false},
             {"invalid_user","wrong123",false}
     };
 }
 @Test(dataProvider = "loginData")
    public void loginTest(String username,String password,boolean flag)
 {
     page.navigate("https://www.saucedemo.com/");
     page.locator("#user-name").fill(username);
     page.locator("#password").fill(password);
     page.locator("#login-button").click();
     if(flag)
     {
         count++;
         assertThat(page).hasURL( "https://www.saucedemo.com/inventory.html");
         System.out.println(count+" Login Successful");
     }
     else {
         count++;
         assertThat(page.locator("[data-test='error']")).isVisible();
         System.out.println(count+" Login Failed");
     }
 }
 @AfterMethod
 public void tearDown()
 {
     browser.close();
     playwright.close();
 }


}
