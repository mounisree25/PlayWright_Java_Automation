import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class APIDataProviderTest {
    @DataProvider(name = "userData")
    public Object[][] getUserData() {

        return new Object[][] {
                {1, "Leanne Graham"},
                {2, "Ervin Howell"},
                {3, "Clementine Bauch"},
                {4, "Patricia Lebsack"},
                {5, "Chelsey Dietrich"}
        };
    }
    @Test(dataProvider = "userData")
    public void test(int userId,String expectedName)
    {
        Playwright playwright=Playwright.create();
        APIRequestContext request=playwright.request().newContext();
        APIResponse response = request.get(
                "https://jsonplaceholder.typicode.com/users/" + userId
        );
        JsonObject json= JsonParser.parseString(response.text()).getAsJsonObject();
        int actualId = json.get("id").getAsInt();

        String actualName = json.get("name").getAsString();

        // Validate response
        assertEquals(response.status(), 200);

        assertEquals(actualId, userId);

        assertEquals(actualName, expectedName);

        System.out.println(
                "User ID: " + actualId +
                        " | Name: " + actualName +
                        " | Validation Successful"
        );

        request.dispose();
        playwright.close();
    }
}
