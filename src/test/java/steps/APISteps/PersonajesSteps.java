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

/**
 * Steps de los escenarios del recurso /character de la API de Rick and Morty.
 * Documentacion: https://rickandmortyapi.com/documentation/#character
 *
 * @author tinobboy
 * @version 0.1.0
 */
public class PersonajesSteps {

    private static final String URL_BASE = "https://rickandmortyapi.com/api";
    private static final BaseSteps step = new BaseSteps();

    private RequestSpecification request;
    private ResponseSpecification respuestaActual;
    private int idPersonaje;

    @Before("@personajes")
    public void beforePersonajes() {
        request = new RequestSpecification(URL_BASE, RequestMethods.GET);
        respuestaActual = null;
        idPersonaje = 0;
    }

    @Dado("que se consulta el personaje con id {int}")
    public void queSeConsultaElPersonajeConId(int id) {
        idPersonaje = id;
        request.setPaths("/character/" + id);
    }

    @Dado("que se consultan los personajes de la pagina {int}")
    public void queSeConsultanLosPersonajesDeLaPagina(int pagina) {
        request.setPaths("/character");
        request.setParameters("page", String.valueOf(pagina));
    }

    @Dado("que se buscan personajes por el nombre {string}")
    public void queSeBuscanPersonajesPorElNombre(String nombre) {
        request.setPaths("/character");
        request.setParameters("name", nombre.replace(" ", "%20"));
    }

    @Dado("que se filtran los personajes por estado {string} y especie {string}")
    public void queSeFiltranLosPersonajesPorEstadoYEspecie(String estado, String especie) {
        request.setPaths("/character");
        request.setParameters("status", estado.toLowerCase());
        request.setParameters("species", especie.toLowerCase());
    }

    @Dado("que se consulta un personaje inexistente con id {int}")
    public void queSeConsultaUnPersonajeInexistenteConId(int id) {
        request.setPaths("/character/" + id);
    }

    @Cuando("se ejecuta la consulta de personajes")
    public void seEjecutaLaConsultaDePersonajes() {
        respuestaActual = new RequestSteps().executeRequest(request);
    }

    @Entonces("se valida el personaje {string} con estado {string}, especie {string} y genero {string}")
    public void seValidaElPersonaje(String nombre, String estado, String especie, String genero) {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

        new ValidadorPostcondicion(respuestaActual.getBody())
                .validarExistenciaCampoNumber("id")
                .compararCampoNumber("id", idPersonaje)
                .compararCampoString("name", nombre)
                .compararCampoString("status", estado)
                .compararCampoString("species", especie)
                .compararCampoString("gender", genero)
                .validarExistenciaCampoObject("origin")
                .validarExistenciaCampoObject("location")
                .validarExistenciaCampoArray("episode");

        new ValidadorPostcondicion(respuestaActual.getBody().get("origin"))
                .validarExistenciaCampoString("name")
                .validarExistenciaCampoString("url");
    }

    @Y("se valida que el personaje tenga los campos principales")
    public void seValidaQueElPersonajeTengaLosCamposPrincipales() {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);
        JsonNode body = respuestaActual.getBody();

        new ValidadorPostcondicion(body)
                .validarExistenciaCampoNumber("id")
                .validarExistenciaCampoString("name")
                .validarExistenciaCampoString("status")
                .validarExistenciaCampoString("species")
                .validarExistenciaCampoString("type")
                .validarExistenciaCampoString("gender")
                .validarExistenciaCampoObject("origin")
                .validarExistenciaCampoObject("location")
                .validarExistenciaCampoString("image")
                .validarExistenciaCampoArray("episode")
                .validarExistenciaCampoString("url")
                .validarExistenciaCampoString("created");

        new ValidadorPostcondicion(body.get("location"))
                .validarExistenciaCampoString("name")
                .validarExistenciaCampoString("url");

        Assertions.assertFalse(body.get("episode").isEmpty(),
                "El personaje debe aparecer en al menos un episodio");
        Assertions.assertTrue(body.at("/url").asText().startsWith(URL_BASE + "/character/"),
                "La url del personaje no pertenece a la api de personajes");
        Assertions.assertTrue(body.at("/image").asText().startsWith("https://"),
                "La imagen del personaje no es una url valida");
    }

    @Y("se valida que la lista de personajes tenga informacion de paginacion")
    public void seValidaQueLaListaDePersonajesTengaInformacionDePaginacion() {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

        ValidadorListado listado = new ValidadorListado(respuestaActual.getBody()).validarPaginacion();
        new ReportesHTML().imprimirResultadoAllure(listado.obtenerValoresDeResultados("name"));
    }

    @Entonces("se valida que los personajes obtenidos tengan el estado {string} y la especie contenga {string}")
    public void seValidaQueLosPersonajesObtenidosTenganElEstadoYLaEspecie(String estado, String especie) {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

        new ValidadorListado(respuestaActual.getBody())
                .validarCampoEnTodosLosResultados("status", estado)
                .validarCampoContieneEnTodosLosResultados("species", especie);
    }

    @Entonces("se valida que la busqueda de personajes devuelva resultados para {string}")
    public void seValidaQueLaBusquedaDePersonajesDevuelvaResultadosPara(String nombre) {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

        ValidadorListado listado = new ValidadorListado(respuestaActual.getBody())
                .validarPaginacion()
                .validarCampoContieneEnTodosLosResultados("name", nombre);

        new ReportesHTML().imprimirResultadoAllure(listado.obtenerValoresDeResultados("name"));
    }

    @Entonces("se valida que la respuesta de error de personajes sea {string}")
    public void seValidaQueLaRespuestaDeErrorDePersonajesSea(String mensaje) {
        step.validateResponseCodeyContentType(respuestaActual, 404, ContentType.JSON);

        new ValidadorPostcondicion(respuestaActual.getBody())
                .compararCampoString("error", mensaje);
    }
}
