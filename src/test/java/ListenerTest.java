
import base.BaseTest;
import listeners.ScreenshotListeners;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Listeners(ScreenshotListeners.class)
public class ListenerTest extends BaseTest {

    @Test
    public void successfulTest() {

        getPage().navigate("https://www.saucedemo.com/");

        assertTrue(getPage().title().contains("Swag Labs"));
    }

    @Test
    public void failedTest() {
        getPage().navigate("https://www.saucedemo.com/");
        getPage().locator("#user-name").fill("Mounisree");
        getPage().locator("#password").fill("Mouni@123");
        getPage().locator("#login-button").click();
        assertTrue(
                getPage().locator("[data-test='error']").isVisible() == false,
                "Login unexpectedly failed"
        );
    }
}