import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class APITest_post {
    public static void main(String[] args) {
        Playwright playwright = Playwright.create();

        APIRequestContext request = playwright.request().newContext();

        String requestBody = """
                {
                    "name": "Mounisree",
                    "username": "Mouni",
                    "email": "mouni@gmail.com"
                }
                """;

        APIResponse response = request.post(
                "https://jsonplaceholder.typicode.com/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(requestBody)
        );
        System.out.println("Status Code: " + response.status());
        System.out.println("Response Body: "+response.text());
        request.dispose();
        playwright.close();


    }
}