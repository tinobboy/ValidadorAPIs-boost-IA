package com.lucas.barabas.web.pages;

import com.lucas.barabas.web.utils.Assertions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Page Object de la pantalla de login de SauceDemo.
 * Equivalente a tests/pageSteps/loginPage.ts del proyecto ValidadorWeb.
 */
public class LoginPage {

    private final Assertions assertions;
    private final Locator titulo;
    private final Locator usuarioInput;
    private final Locator passwordInput;
    private final Locator botonIngresar;

    public LoginPage(Page page) {
        this.assertions = new Assertions(page);
        this.titulo = page.getByText("Swag Labs");
        this.usuarioInput = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Username"));
        this.passwordInput = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Password"));
        this.botonIngresar = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Login"));
    }

    public void validarTitulo() {
        assertions.assertElementoEsONoVisible(titulo, true);
    }

    public void completarUsuario(String usuario) {
        assertions.assertElementoEsONoVisible(usuarioInput, true);
        usuarioInput.fill(usuario);
    }

    public void completarPassword(String password) {
        assertions.assertElementoEsONoVisible(passwordInput, true);
        passwordInput.fill(password);
    }

    public void validarBotonLogin() {
        assertions.assertElementoEsONoVisible(botonIngresar, true);
    }

    public void clickIngresar() {
        assertions.validarElementoVisibleYDisponible(botonIngresar);
        botonIngresar.click();
    }
}
