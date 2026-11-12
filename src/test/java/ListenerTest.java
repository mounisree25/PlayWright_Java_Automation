
import base.BaseTest;
import listeners.ScreenshotListeners;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Listeners(ScreenshotListeners.class)
public class ListenerTest extends BaseTest {

    @Test
    public void successfulTest() {

        page.navigate("https://www.saucedemo.com/");

        assertTrue(page.title().contains("Swag Labs"));
    }

    @Test
    public void failedTest() {
        page.navigate("https://www.saucedemo.com/");
        page.locator("#user-name").fill("Mounisree");
        page.locator("#password").fill("Mouni@123");
        page.locator("#login-button").click();
        assertTrue(
                page.locator("[data-test='error']").isVisible() == false,
                "Login unexpectedly failed"
        );
    }
}