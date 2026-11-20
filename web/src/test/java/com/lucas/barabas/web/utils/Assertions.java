package com.lucas.barabas.web.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Utilidades de aserción reutilizables para los tests web.
 * Equivalente a Utils/assert.ts del proyecto ValidadorWeb.
 */
public class Assertions {

    private final Page page;

    public Assertions(Page page) {
        this.page = page;
    }

    public void assertTitulo(String titulo) {
        assertThat(page).hasTitle(titulo);
    }

    public void assertTituloContiene(String titulo) {
        assertThat(page).hasTitle(Pattern.compile(".*" + escaparRegex(titulo) + ".*"));
    }

    public void assertUrl(String url) {
        assertThat(page).hasURL(url);
    }

    public void assertUrlContiene(String fragmento) {
        assertThat(page).hasURL(Pattern.compile(".*" + escaparRegex(fragmento) + ".*"));
    }

    public void assertElementoExiste(Locator elemento) {
        assertThat(elemento).hasCount(1);
    }

    public void assertElementoEsONoVisible(Locator elemento, boolean esperado) {
        if (esperado) {
            assertThat(elemento).isVisible();
        } else {
            assertThat(elemento).not().isVisible();
        }
    }

    public void assertElementoEstaHabilitado(Locator elemento) {
        assertThat(elemento).isEnabled();
    }

    public void validarElementoVisibleYDisponible(Locator elemento) {
        assertThat(elemento).isVisible();
        if (!elemento.isEnabled()) {
            throw new AssertionError("El elemento no está operativo: " + elemento);
        }
    }

    /**
     * Escapa metacaracteres de regex sin usar \Q...\E, porque Playwright
     * serializa el patrón a JavaScript y esa sintaxis no es compatible.
     */
    private static String escaparRegex(String texto) {
        return texto.replaceAll("([\\\\.^$|?*+()\\[\\]{}])", "\\\\$1");
    }
}
