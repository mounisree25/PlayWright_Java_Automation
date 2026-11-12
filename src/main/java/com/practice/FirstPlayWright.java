package com.practice;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FirstPlayWright {
    public static void main(String[] args) {
        Playwright playwright=Playwright.create();
        Browser browser=playwright.chromium().launch(
          new BrowserType.LaunchOptions().setHeadless(false)
        );
        BrowserContext context=browser.newContext();
        Page page=context.newPage();
        page.setDefaultTimeout(10000);// applicable for every element
         page.navigate("https://www.saucedemo.com/");
         assertThat(page).hasTitle("Swag Labs");
         page.locator("#user-name").fill("standard_user");
         page.locator("#password").fill("secret_sauce");
         page.locator("#login-button").click();
         page.locator(".inventory_list").waitFor(
                 new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
         );
        page.locator(".inventory_list").screenshot(
                new Locator.ScreenshotOptions()
                        .setPath(Paths.get("screenshots/products.png"))
        );
         assertThat(page.locator(".title")).hasText("Products");
         System.out.println("Login Successfully");

         browser.close();
         playwright.close();









    }
}