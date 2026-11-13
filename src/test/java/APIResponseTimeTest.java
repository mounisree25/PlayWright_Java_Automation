import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class APIResponseTimeTest {
    @Test
    public void APIResponseTimeTest() {
        Playwright playwright=Playwright.create();
        APIRequestContext request=playwright.request().newContext();
        long startTime=System.currentTimeMillis();
        APIResponse response = request.get(
                "https://jsonplaceholder.typicode.com/users/1"
        );
        long endTime=System.currentTimeMillis();
        long responseTime=endTime-startTime;
        assertEquals(response.status(),200);
        assertTrue(responseTime<5000,"API response took longer than 5 seconds");
        System.out.println("Response Time Validation Successful");
        request.dispose();
        playwright.close();
    }
}
