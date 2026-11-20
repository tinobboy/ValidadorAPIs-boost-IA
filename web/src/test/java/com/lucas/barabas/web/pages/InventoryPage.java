package com.lucas.barabas.web.pages;

import com.lucas.barabas.web.utils.Assertions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Page Object de la pantalla de inventario de SauceDemo.
 * Equivalente a tests/pageSteps/inventoryPage.ts del proyecto ValidadorWeb.
 */
public class InventoryPage {

    private final Assertions assertions;
    private final Locator titulo;
    private final Locator botonOpenMenu;
    private final Locator botonAllItems;

    public InventoryPage(Page page) {
        this.assertions = new Assertions(page);
        this.titulo = page.getByText("Swag Labs");
        this.botonOpenMenu = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Open Menu"));
        this.botonAllItems = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("All Items"));
    }

    public void validarTitulo() {
        assertions.assertElementoEsONoVisible(titulo, true);
    }

    public void validarUrl() {
        assertions.assertUrlContiene("inventory.html");
    }

    public void clickEnBotonOpenMenu() {
        assertions.assertElementoEsONoVisible(botonOpenMenu, true);
        botonOpenMenu.click();
    }

    public void validarAllItems() {
        assertions.assertElementoEsONoVisible(botonAllItems, true);
        botonAllItems.click();
    }
}
