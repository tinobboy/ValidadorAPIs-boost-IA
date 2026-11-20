package com.lucas.barabas.web.tests;

import com.lucas.barabas.web.pages.CheckoutPage;
import com.lucas.barabas.web.pages.InventoryPage;
import com.lucas.barabas.web.pages.LoginPage;
import com.lucas.barabas.web.pages.ProductsPage;
import com.lucas.barabas.web.utils.DatosLogin;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test E2E de SauceDemo migrado del proyecto ValidadorWeb (TypeScript)
 * a Playwright con Java.
 *
 * Ejecutar con: ./gradlew :web:runWebTests
 * Modo con interfaz: ./gradlew :web:runWebTests -Dplaywright.headless=false
 */
public class SauceDemoWebTest {

    private static final String URL_BASE = "https://www.saucedemo.com";
    private static final String DATOS_LOGIN = "JsonDatos/datoLogin.json";

    private static Playwright playwright;
    private static Browser browser;

    private BrowserContext context;
    private Page page;

    @BeforeAll
    static void iniciarNavegador() {
        playwright = Playwright.create();
        boolean headless = Boolean.parseBoolean(System.getProperty("playwright.headless", "true"));
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(headless));
    }

    @AfterAll
    static void cerrarNavegador() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void abrirContexto() {
        context = browser.newContext();
        page = context.newPage();
        page.setDefaultTimeout(30_000);
    }

    @AfterEach
    void cerrarContexto() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    @DisplayName("Ingresar a la web, agregar un producto y hacer checkout")
    void agregarProductoYHacerCheckout() {
        DatosLogin datosLogin = DatosLogin.desdeRecurso(DATOS_LOGIN);
        LoginPage loginPage = new LoginPage(page);
        InventoryPage inventoryPage = new InventoryPage(page);
        ProductsPage productsPage = new ProductsPage(page);
        CheckoutPage checkoutPage = new CheckoutPage(page);

        // Login (equivalente al beforeEach del proyecto original)
        page.navigate(URL_BASE);
        loginPage.validarTitulo();
        loginPage.completarUsuario(datosLogin.usuario());
        loginPage.completarPassword(datosLogin.password());
        loginPage.validarBotonLogin();
        loginPage.clickIngresar();

        // La validación de URL del inventario espera automáticamente la navegación.
        inventoryPage.validarUrl();
        inventoryPage.validarTitulo();

        productsPage.addProductToCart();
        productsPage.goToCart();

        checkoutPage.checkOut();
        checkoutPage.fillInformationForm();
        checkoutPage.finishCheckOut();
        checkoutPage.validateSuccessMessage();
    }
}
