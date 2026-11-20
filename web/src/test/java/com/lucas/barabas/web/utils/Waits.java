package com.lucas.barabas.web.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Utilidades de espera reutilizables para los tests web.
 * Equivalente a Utils/esperas.ts del proyecto ValidadorWeb.
 */
public class Waits {

    private final Page page;

    public Waits(Page page) {
        this.page = page;
    }

    public void esperarSegundos(int segundos) {
        page.waitForTimeout(segundos * 1000.0);
    }

    public void esperarElementoPresente(Locator elemento) {
        elemento.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(10 * 60 * 1000));
    }

    public void esperarElementoVisibleYActivo(Locator elemento) {
        esperarElementoPresente(elemento);
        elemento.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(60 * 1000));
    }
}
