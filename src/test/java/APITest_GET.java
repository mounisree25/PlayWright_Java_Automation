import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class APITest_GET {
    @Test
    public void GetUserTest()
    {
        Playwright playwright=Playwright.create();
        APIRequestContext request=playwright.request().newContext();
        APIResponse response =
                request.get("https://jsonplaceholder.typicode.com/users/1");
        System.out.println("Response: "+response.text());
        assertEquals(response.status(),200);
        assertTrue(response.text().contains("Leanne Graham"));
        request.dispose();
        playwright.close();
    }

}
