package com.lucas.barabas.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Page Object del proceso de checkout de SauceDemo.
 * Equivalente a tests/pageSteps/checkoutPage.ts del proyecto ValidadorWeb.
 */
public class CheckoutPage {

    private final Locator checkOutBtn;
    private final Locator firstName;
    private final Locator lastName;
    private final Locator zipCode;
    private final Locator continueBtn;
    private final Locator finishBtn;
    private final Locator completeOrderMessage;

    public CheckoutPage(Page page) {
        this.checkOutBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Checkout"));
        this.firstName = page.getByPlaceholder("First Name");
        this.lastName = page.getByPlaceholder("Last Name");
        this.zipCode = page.getByPlaceholder("Zip/Postal Code");
        this.continueBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue"));
        this.finishBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Finish"));
        this.completeOrderMessage = page.locator(".complete-header");
    }

    public void checkOut() {
        checkOutBtn.click();
    }

    public void fillInformationForm() {
        firstName.fill("asdad");
        lastName.fill("asdasd");
        zipCode.fill("123");
        continueBtn.click();
    }

    public void finishCheckOut() {
        finishBtn.click();
    }

    public void validateSuccessMessage() {
        assertThat(completeOrderMessage).isVisible();
    }
}
