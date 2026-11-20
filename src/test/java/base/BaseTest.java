
package base;

import com.microsoft.playwright.*;
import org.testng.annotations.*;

public class BaseTest {

    private static final ThreadLocal<Playwright> playwright =
            new ThreadLocal<>();

    private static final ThreadLocal<Browser> browser =
            new ThreadLocal<>();

    private static final ThreadLocal<Page> page =
            new ThreadLocal<>();

    @BeforeMethod
    public void setup() {

        Playwright pw = Playwright.create();

        Browser br = pw.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
        );

        Page pg = br.newPage();

        playwright.set(pw);
        browser.set(br);
        page.set(pg);

        System.out.println(
                "Browser started on thread: "
                        + Thread.currentThread().getId()
        );
    }

    public Page getPage() {
        return page.get();
    }

    @AfterMethod
    public void tearDown() {

        try {
            if (page.get() != null) {
                page.get().close();
            }

            if (browser.get() != null) {
                browser.get().close();
            }

            if (playwright.get() != null) {
                playwright.get().close();
            }

        } finally {
            page.remove();
            browser.remove();
            playwright.remove();
        }
    }
}
