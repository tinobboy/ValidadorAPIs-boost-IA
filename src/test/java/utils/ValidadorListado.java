package utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Validador de respuestas que devuelven listados paginados.
 * Se utiliza para los endpoints /character, /episode y /location de la API de Rick and Morty,
 * los cuales responden con la estructura {"info": {...}, "results": [...]}.
 *
 * @author tinobboy
 * @version 0.1.0
 */
public class ValidadorListado {

    private final JsonNode body;

    public ValidadorListado(JsonNode body) {
        this.body = body;
    }

    /**
     * Valida que la respuesta tenga la estructura de paginacion y que el listado de resultados no este vacio.
     */
    @Step("Se valida la estructura de paginacion del listado")
    public ValidadorListado validarPaginacion() {
        new ValidadorPostcondicion(body).validarExistenciaCampoObject("info");
        JsonNode info = body.get("info");
        new ValidadorPostcondicion(info)
                .validarExistenciaCampoNumber("count")
                .validarExistenciaCampoNumber("pages");
        Assertions.assertTrue(info.at("/count").asInt() > 0,
                "La cantidad de resultados informada por el servicio debe ser mayor a cero");
        Assertions.assertTrue(info.at("/pages").asInt() > 0,
                "La cantidad de paginas informada por el servicio debe ser mayor a cero");
        return validarListaResultados();
    }

    /**
     * Valida que el nodo 'results' exista y contenga al menos un elemento.
     */
    @Step("Se valida que el listado de resultados no este vacio")
    public ValidadorListado validarListaResultados() {
        new ValidadorPostcondicion(body).validarExistenciaCampoArray("results");
        Assertions.assertTrue(body.get("results").size() > 0,
                "El listado de resultados de la respuesta se encuentra vacio");
        return this;
    }

    /**
     * Valida que todos los elementos del listado tengan el mismo valor en un campo determinado.
     *
     * @param campo         nombre del campo a comparar
     * @param valorEsperado valor que deben tener todos los elementos
     */
    @Step("Se valida el campo {campo} con valor '{valorEsperado}' en todos los resultados del listado")
    public ValidadorListado validarCampoEnTodosLosResultados(String campo, String valorEsperado) {
        validarListaResultados();
        for (JsonNode resultado : body.get("results")) {
            Assertions.assertEquals(valorEsperado, resultado.at("/" + campo).asText(),
                    "El valor del campo '" + campo + "' del resultado con id " + resultado.at("/id").asText()
                            + " no coincide con el esperado");
        }
        return this;
    }

    /**
     * Valida que el valor del campo de cada elemento del listado contenga un fragmento de texto,
     * sin distinguir mayusculas de minusculas.
     *
     * @param campo     nombre del campo a comparar
     * @param fragmento texto que debe contener el campo
     */
    @Step("Se valida que el campo {campo} de todos los resultados contenga '{fragmento}'")
    public ValidadorListado validarCampoContieneEnTodosLosResultados(String campo, String fragmento) {
        validarListaResultados();
        for (JsonNode resultado : body.get("results")) {
            String valorObtenido = resultado.at("/" + campo).asText();
            Assertions.assertTrue(valorObtenido.toLowerCase().contains(fragmento.toLowerCase()),
                    "El valor obtenido '" + valorObtenido + "' del campo '" + campo + "' no contiene '" + fragmento + "'");
        }
        return this;
    }

    public List<String> obtenerValoresDeResultados(String campo) {
        List<String> valores = new ArrayList<>();
        for (JsonNode resultado : body.get("results")) {
            valores.add(resultado.at("/" + campo).asText());
        }
        return valores;
    }
}
