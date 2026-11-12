import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class APITest_Delete {
    @Test
    public void deleteuser()
    {
        Playwright playwright=Playwright.create();
        APIRequestContext request=playwright.request().newContext();
        APIResponse response=request.delete("https://jsonplaceholder.typicode.com/users/1");
        System.out.println("Status Code: "+response.status());
        assertEquals(response.status(), 200);
        System.out.println("Response Body: "+response.text());
        request.dispose();
        playwright.close();
    }
}
