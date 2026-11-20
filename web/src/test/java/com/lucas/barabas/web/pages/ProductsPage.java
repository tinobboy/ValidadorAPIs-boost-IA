package com.lucas.barabas.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Page Object de la grilla de productos de SauceDemo.
 * Equivalente a tests/pageSteps/productsPage.ts del proyecto ValidadorWeb.
 */
public class ProductsPage {

    private final Locator addToCartBtn;
    private final Locator shoppingCartIcon;

    public ProductsPage(Page page) {
        int indice = ThreadLocalRandom.current().nextInt(1, 7);
        this.addToCartBtn = page.locator(
                String.format("(//button[text() = 'Add to cart'])[%d]", indice));
        this.shoppingCartIcon = page.locator("a[class = \"shopping_cart_link\"]");
    }

    public void addProductToCart() {
        addToCartBtn.click();
    }

    public void goToCart() {
        shoppingCartIcon.click();
    }
}
