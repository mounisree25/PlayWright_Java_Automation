
import com.microsoft.playwright.*;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import java.util.Map;

public class API_AUTHENTICATION {

    @Test
    public void bearerTokenTest() {

        Playwright playwright = Playwright.create();

        String token = "my-test-token-123";

        APIRequestContext request =
                playwright.request().newContext(
                        new APIRequest.NewContextOptions()
                                .setExtraHTTPHeaders(
                                        Map.of(
                                                "Authorization",
                                                "Bearer " + token
                                        )
                                )
                );

        APIResponse response =
                request.get("https://httpbin.org/bearer");

        System.out.println("Status: " + response.status());
        System.out.println("Response: " + response.text());

        assertEquals(response.status(), 200);
        assertTrue(response.text().contains("authenticated"));

        request.dispose();
        playwright.close();
    }
}