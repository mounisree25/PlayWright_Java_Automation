import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.Test;

import java.net.ContentHandler;
import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;


public class APIHeaderTest {
    @Test
            public void test() {
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response = request.get(
                "https://jsonplaceholder.typicode.com/users/1"
        );
        System.out.println(response.text());
        Map<String, String> headers = response.headers();
        System.out.println(" Response Headers:");
        headers.forEach((key, value) -> System.out.println(key+":-> "+value));
        String contentType=headers.get("content-type");
        assertNotNull(contentType,"Content-Type header is missing");
        assertTrue(contentType.contains("application/json"),
                "Response is not JSON");
        System.out.println("Status: " + response.status());
        System.out.println("Header Validation is Successful");
        request.dispose();
        playwright.close();


    }
}
