import com.microsoft.playwright.*;
import org.testng.annotations.*;
import com.practice.LoginPage;
import com.practice.InventoryPage;
import com.practice.CartPage;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void setup() {

        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
        );

        page = browser.newPage();
        page.setDefaultTimeout(10000);
    }

    @Test
    public void loginTest() {

        page.navigate("https://www.saucedemo.com/");

        LoginPage loginPage = new LoginPage(page);

        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(page);

        assertThat(page).hasURL(
                "https://www.saucedemo.com/inventory.html"
        );

        assertThat(page.locator(".title")).hasText("Products");

        System.out.println("Page title: "
                + inventoryPage.getPageTitle());

        System.out.println("Product count: "
                + inventoryPage.getProductCount());
        inventoryPage.addBackPackToCart();
        inventoryPage.addBikeLight();
        inventoryPage.openCart();
        CartPage cart=new CartPage(page);
        System.out.println("Cart Item Count:"+cart.getCartItemCount());

       // assertThat(page.locator(".cart_item")).hasCount(2);
        cart.removeBackPack();
        System.out.println("Total Quantity :"+cart.getTotalCount());
        cart.setContinueshopping();
    }

    @AfterMethod
    public void tearDown() {

        browser.close();
        playwright.close();
    }
}
