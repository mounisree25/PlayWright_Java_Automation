import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.annotations.Test;

public class APITest_Update {
      @Test
     public void setuptest()
      {
          Playwright playwright=Playwright.create();
          APIRequestContext request=playwright.request().newContext();

          String requestBody = """
                {
                    "name": "Mounisree Updated",
                    "username": "Mouni",
                    "email": "updated@gmail.com"
                }
                """;
          APIResponse response=request.put(
                  "https://jsonplaceholder.typicode.com/users/1",
                  RequestOptions.create().setHeader("Content-Type", "application/json")
                          .setData(requestBody)
          );
          System.out.println("Status Code: "+response.status());
          System.out.println("response Body: "+response.text());
          request.dispose();
          playwright.close();
      }
}
