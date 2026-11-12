
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import static org.testng.Assert.assertEquals;

public class APITest_Patch {

    public static void main(String[] args) {

        Playwright playwright = Playwright.create();

        APIRequestContext request =
                playwright.request().newContext();

        String requestBody = """
                {
                    "email": "newemail@gmail.com"
                }
                """;

        APIResponse response = request.patch(
                "https://jsonplaceholder.typicode.com/users/1",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(requestBody)
        );

        System.out.println("Status Code: " + response.status());
        assertEquals(response.status(), 200);

        System.out.println("Response Body: " + response.text());

        request.dispose();
        playwright.close();
    }
}
