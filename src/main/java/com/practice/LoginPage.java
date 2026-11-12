
package com.practice;

import com.microsoft.playwright.Page;

public class LoginPage {

    private Page page;

    // Constructor
    public LoginPage(Page page) {
        this.page = page;
    }

    // Locators
    private String username = "#user-name";
    private String password = "#password";
    private String loginButton = "#login-button";

    // Actions
    public void enterUsername(String user) {
        page.locator(username).fill(user);
    }

    public void enterPassword(String pass) {
        page.locator(password).fill(pass);
    }

    public void clickLogin() {
        page.locator(loginButton).click();
    }

    // Complete login action
    public void login(String user, String pass) {
        enterUsername(user);
        enterPassword(pass);
        clickLogin();
    }
}
