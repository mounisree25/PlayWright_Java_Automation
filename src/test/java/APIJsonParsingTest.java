import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class APIJsonParsingTest {
    @Test
    public void test()
    {
        Playwright playwright=Playwright.create();
        APIRequestContext request=playwright.request().newContext();
        APIResponse response = request.get(
                "https://jsonplaceholder.typicode.com/users/1"
        );
        JsonObject json= JsonParser.parseString(response.text()).getAsJsonObject();
        int id=json.get("id").getAsInt();
        String name=json.get("name").getAsString();
        String email=json.get("email").getAsString();
        String username=json.get("username").getAsString();
        assertEquals(response.status(), 200);
        assertEquals(id, 1);
        assertEquals(name, "Leanne Graham");
        assertEquals(username, "Bret");
        assertEquals(email, "Sincere@april.biz");

        System.out.println("JSON Validation Successful");
        request.dispose();
        playwright.close();

    }
}
