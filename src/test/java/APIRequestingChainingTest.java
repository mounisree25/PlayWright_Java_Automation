import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class APIRequestingChainingTest {
    @Test
    public void test()
    {
        Playwright playwright=Playwright.create();
        APIRequestContext request=playwright.request().newContext();
        APIResponse response = request.get(
                "https://jsonplaceholder.typicode.com/users/1"
        );
        assertEquals(response.status(),200);
        JsonObject json= JsonParser.parseString(response.text()).getAsJsonObject();
        int id=json.get("id").getAsInt();
        String username=json.get("name").getAsString();
        System.out.println("User Id:"+id);
        System.out.println("User Name:"+username);
        APIResponse postsResponse = request.get(
                "https://jsonplaceholder.typicode.com/posts?userId="
                        + id
        );
        assertEquals(postsResponse.status(),200);
        JsonArray posts=JsonParser.parseString(postsResponse.text()).getAsJsonArray();
        System.out.println("Total Posts:"+posts.size());
        for(int i=0;i<posts.size();i++)
        {
            JsonObject post=posts.get(i).getAsJsonObject();
            int postUserId=post.get("userId").getAsInt();
            assertEquals(postUserId,id);
        }
        System.out.println("API Chaining Successful");
   request.dispose();
   playwright.close();

    }
}
