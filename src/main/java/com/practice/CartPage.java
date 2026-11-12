package com.practice;

import com.microsoft.playwright.Page;

public class CartPage {
    private Page page;
    public CartPage(Page page)
    {
        this.page=page;
    }
    //Locators
    private String CartItem=".cart_item";
    private String removebtn="#remove-sauce-labs-backpack";
    private  String continueshopping="#continue-shopping";
    private String total=".cart_quantity";
    public int getCartItemCount()
    {
        return page.locator(CartItem).count();
    }
    public void removeBackPack()
    {
        page.locator(removebtn).click();
    }
    public void setContinueshopping()
    {
        page.locator(continueshopping).click();
    }
    public  int getTotalCount()
    {
        return page.locator(total).count();
    }
}
