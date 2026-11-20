
import base.BaseTest;
import org.testng.annotations.Test;

public class ParellelTest extends BaseTest {

    @Test
    public void testGoogle() {

        getPage().navigate("https://www.google.com");

        System.out.println(
                "Google Test: "
                        + Thread.currentThread().getId()
        );
    }

    @Test
    public void testAmazon() {

        getPage().navigate("https://www.amazon.in");

        System.out.println(
                "Amazon Test: "
                        + Thread.currentThread().getId()
        );
    }

    @Test
    public void testWikipedia() {

        getPage().navigate("https://www.wikipedia.org");

        System.out.println(
                "Wikipedia Test: "
                        + Thread.currentThread().getId()
        );
    }
}
