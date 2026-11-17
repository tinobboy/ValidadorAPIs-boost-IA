package steps.APISteps;

import com.fasterxml.jackson.databind.JsonNode;
import constants.RequestMethods;
import core.RequestSpecification;
import core.ResponseSpecification;
import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import steps.BaseSteps;
import steps.RequestSteps;
import utils.ReportesHTML;
import utils.ValidadorListado;
import utils.ValidadorPostcondicion;

import java.util.ArrayList;
import java.util.List;

/**
 * Steps de los escenarios del recurso /episode de la API de Rick and Morty.
 * Documentacion: https://rickandmortyapi.com/documentation/#episode
 *
 * @author tinobboy
 * @version 0.1.0
 */
public class EpisodiosSteps {

    private static final String URL_BASE = "https://rickandmortyapi.com/api";
    private static final BaseSteps step = new BaseSteps();

    private RequestSpecification request;
    private ResponseSpecification respuestaActual;
    private int idEpisodio;

    @Before("@episodios")
    public void beforeEpisodios() {
        request = new RequestSpecification(URL_BASE, RequestMethods.GET);
        respuestaActual = null;
        idEpisodio = 0;
    }

    @Dado("que se consulta el episodio con id {int}")
    public void queSeConsultaElEpisodioConId(int id) {
        idEpisodio = id;
        request.setPaths("/episode/" + id);
    }

    @Dado("que se consultan los episodios de la pagina {int}")
    public void queSeConsultanLosEpisodiosDeLaPagina(int pagina) {
        request.setPaths("/episode");
        request.setParameters("page", String.valueOf(pagina));
    }

    @Dado("que se buscan episodios por el nombre {string}")
    public void queSeBuscanEpisodiosPorElNombre(String nombre) {
        request.setPaths("/episode");
        request.setParameters("name", nombre.replace(" ", "%20"));
    }

    @Dado("que se consultan los episodios de la temporada {string}")
    public void queSeConsultanLosEpisodiosDeLaTemporada(String temporada) {
        request.setPaths("/episode");
        request.setParameters("episode", temporada);
    }

    @Dado("que se consulta un episodio inexistente con id {int}")
    public void queSeConsultaUnEpisodioInexistenteConId(int id) {
        request.setPaths("/episode/" + id);
    }

    @Cuando("se ejecuta la consulta de episodios")
    public void seEjecutaLaConsultaDeEpisodios() {
        respuestaActual = new RequestSteps().executeRequest(request);
    }

    @Entonces("se valida el episodio {string} con codigo {string} emitido el {string}")
    public void seValidaElEpisodio(String nombre, String codigo, String fechaDeEmision) {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

        new ValidadorPostcondicion(respuestaActual.getBody())
                .validarExistenciaCampoNumber("id")
                .compararCampoNumber("id", idEpisodio)
                .compararCampoString("name", nombre)
                .compararCampoString("episode", codigo)
                .compararCampoString("air_date", fechaDeEmision)
                .validarExistenciaCampoArray("characters");
    }

    @Y("se valida que el episodio tenga los campos principales")
    public void seValidaQueElEpisodioTengaLosCamposPrincipales() {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);
        JsonNode body = respuestaActual.getBody();

        new ValidadorPostcondicion(body)
                .validarExistenciaCampoNumber("id")
                .validarExistenciaCampoString("name")
                .validarExistenciaCampoString("air_date")
                .validarExistenciaCampoString("episode")
                .validarExistenciaCampoArray("characters")
                .validarExistenciaCampoString("url")
                .validarExistenciaCampoString("created");

        Assertions.assertTrue(body.at("/url").asText().startsWith(URL_BASE + "/episode/"),
                "La url del episodio no pertenece a la api de episodios");
        Assertions.assertTrue(body.at("/episode").asText().matches("S\\d{2}E\\d{2}"),
                "El codigo del episodio no tiene el formato esperado SXXEXX");
    }

    @Y("se valida que el episodio tenga personajes asociados")
    public void seValidaQueElEpisodioTengaPersonajesAsociados() {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);
        JsonNode personajes = respuestaActual.getBody().get("characters");

        Assertions.assertNotNull(personajes, "El episodio no posee el campo characters");
        Assertions.assertTrue(personajes.isArray(), "El campo characters no es un array");
        Assertions.assertFalse(personajes.isEmpty(), "El episodio no tiene personajes asociados");

        List<String> idsPersonajes = new ArrayList<>();
        for (JsonNode personaje : personajes) {
            String urlPersonaje = personaje.asText();
            Assertions.assertTrue(urlPersonaje.startsWith(URL_BASE + "/character/"),
                    "La url '" + urlPersonaje + "' no pertenece a la api de personajes");
            idsPersonajes.add(urlPersonaje.substring(urlPersonaje.lastIndexOf("/") + 1));
        }

        new ReportesHTML().imprimirResultadoAllure(idsPersonajes);
    }

    @Y("se valida que la lista de episodios tenga informacion de paginacion")
    public void seValidaQueLaListaDeEpisodiosTengaInformacionDePaginacion() {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

        ValidadorListado listado = new ValidadorListado(respuestaActual.getBody()).validarPaginacion();
        new ReportesHTML().imprimirResultadoAllure(listado.obtenerValoresDeResultados("name"));
    }

    @Entonces("se valida que todos los episodios obtenidos sean de la temporada {string}")
    public void seValidaQueTodosLosEpisodiosObtenidosSeanDeLaTemporada(String temporada) {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);
        JsonNode resultados = respuestaActual.getBody().get("results");

        Assertions.assertNotNull(resultados, "La respuesta no posee el campo results");
        Assertions.assertTrue(resultados.isArray(), "El campo results no es un array");
        Assertions.assertFalse(resultados.isEmpty(), "El listado de episodios se encuentra vacio");

        for (JsonNode episodio : resultados) {
            String codigo = episodio.at("/episode").asText();
            Assertions.assertTrue(codigo.startsWith(temporada),
                    "El episodio '" + codigo + "' no pertenece a la temporada '" + temporada + "'");
        }
    }

    @Entonces("se valida que la busqueda de episodios devuelva resultados para {string}")
    public void seValidaQueLaBusquedaDeEpisodiosDevuelvaResultadosPara(String nombre) {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

        ValidadorListado listado = new ValidadorListado(respuestaActual.getBody())
                .validarPaginacion()
                .validarCampoContieneEnTodosLosResultados("name", nombre);

        new ReportesHTML().imprimirResultadoAllure(listado.obtenerValoresDeResultados("name"));
    }

    @Entonces("se valida que la respuesta de error de episodios sea {string}")
    public void seValidaQueLaRespuestaDeErrorDeEpisodiosSea(String mensaje) {
        step.validateResponseCodeyContentType(respuestaActual, 404, ContentType.JSON);

        new ValidadorPostcondicion(respuestaActual.getBody())
                .compararCampoString("error", mensaje);
    }
}
