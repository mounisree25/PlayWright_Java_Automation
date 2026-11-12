package com.practice;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class InventoryPage {

    private Page page;

    public InventoryPage(Page page) {
        this.page = page;
    }

    // Locators
    private String pageTitle = ".title";
    private String inventoryItems = ".inventory_item";
    String backpackbutton="#add-to-cart-sauce-labs-backpack";
    String bikelight="#add-to-cart-sauce-labs-bike-light";
    String CartIcon=".shopping_cart_link";

    // Actions / Methods
    public String getPageTitle() {
        return page.locator(pageTitle).textContent();
    }

    public int getProductCount() {
        return page.locator(inventoryItems).count();
    }
    public void addBackPackToCart()
    {
        Locator backpack=page.locator(".inventory_item")
                .filter(new Locator.FilterOptions().setHasText("Sauce Labs Backpack"));
        backpack.locator("button").click();
    }
    public void openCart()
    {
        page.locator(CartIcon).click();
    }
    public  void addBikeLight()
    {
        Locator bike=page.locator(".inventory_item")
                .filter(new Locator.FilterOptions().setHasText("Sauce Labs Bike Light "));
            bike.locator("button").click();
    }
}
