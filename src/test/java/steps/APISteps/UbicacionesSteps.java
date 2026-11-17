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
 * Steps de los escenarios del recurso /location de la API de Rick and Morty.
 * Documentacion: https://rickandmortyapi.com/documentation/#location
 *
 * @author tinobboy
 * @version 0.1.0
 */
public class UbicacionesSteps {

    private static final String URL_BASE = "https://rickandmortyapi.com/api";
    private static final BaseSteps step = new BaseSteps();

    private RequestSpecification request;
    private ResponseSpecification respuestaActual;
    private int idUbicacion;

    @Before("@ubicaciones")
    public void beforeUbicaciones() {
        request = new RequestSpecification(URL_BASE, RequestMethods.GET);
        respuestaActual = null;
        idUbicacion = 0;
    }

    @Dado("que se consulta la ubicacion con id {int}")
    public void queSeConsultaLaUbicacionConId(int id) {
        idUbicacion = id;
        request.setPaths("/location/" + id);
    }

    @Dado("que se consultan las ubicaciones de la pagina {int}")
    public void queSeConsultanLasUbicacionesDeLaPagina(int pagina) {
        request.setPaths("/location");
        request.setParameters("page", String.valueOf(pagina));
    }

    @Dado("que se buscan ubicaciones por la dimension {string}")
    public void queSeBuscanUbicacionesPorLaDimension(String dimension) {
        request.setPaths("/location");
        request.setParameters("dimension", dimension.replace(" ", "%20"));
    }

    @Dado("que se consulta una ubicacion inexistente con id {int}")
    public void queSeConsultaUnaUbicacionInexistenteConId(int id) {
        request.setPaths("/location/" + id);
    }

    @Cuando("se ejecuta la consulta de ubicaciones")
    public void seEjecutaLaConsultaDeUbicaciones() {
        respuestaActual = new RequestSteps().executeRequest(request);
    }

    @Entonces("se valida la ubicacion {string} de tipo {string} y dimension {string}")
    public void seValidaLaUbicacion(String nombre, String tipo, String dimension) {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

        new ValidadorPostcondicion(respuestaActual.getBody())
                .validarExistenciaCampoNumber("id")
                .compararCampoNumber("id", idUbicacion)
                .compararCampoString("name", nombre)
                .compararCampoString("type", tipo)
                .compararCampoString("dimension", dimension)
                .validarExistenciaCampoArray("residents");
    }

    @Y("se valida que la ubicacion tenga los campos principales")
    public void seValidaQueLaUbicacionTengaLosCamposPrincipales() {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);
        JsonNode body = respuestaActual.getBody();

        new ValidadorPostcondicion(body)
                .validarExistenciaCampoNumber("id")
                .validarExistenciaCampoString("name")
                .validarExistenciaCampoString("type")
                .validarExistenciaCampoString("dimension")
                .validarExistenciaCampoArray("residents")
                .validarExistenciaCampoString("url")
                .validarExistenciaCampoString("created");

        Assertions.assertTrue(body.at("/url").asText().startsWith(URL_BASE + "/location/"),
                "La url de la ubicacion no pertenece a la api de ubicaciones");
    }

    @Y("se valida que la lista de ubicaciones tenga informacion de paginacion")
    public void seValidaQueLaListaDeUbicacionesTengaInformacionDePaginacion() {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

        ValidadorListado listado = new ValidadorListado(respuestaActual.getBody()).validarPaginacion();
        new ReportesHTML().imprimirResultadoAllure(listado.obtenerValoresDeResultados("name"));
    }

    @Entonces("se valida que todas las ubicaciones obtenidas pertenezcan a la dimension {string}")
    public void seValidaQueTodasLasUbicacionesObtenidasPertenezcanALaDimension(String dimension) {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

        new ValidadorListado(respuestaActual.getBody())
                .validarPaginacion()
                .validarCampoEnTodosLosResultados("dimension", dimension);
    }

    @Y("se valida que los habitantes de la ubicacion referencien a la api de personajes")
    public void seValidaQueLosHabitantesDeLaUbicacionReferencienALaApiDePersonajes() {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);
        JsonNode habitantes = respuestaActual.getBody().get("residents");

        Assertions.assertNotNull(habitantes, "La ubicacion no posee el campo residents");
        Assertions.assertTrue(habitantes.isArray(), "El campo residents no es un array");

        List<String> idsHabitantes = new ArrayList<>();
        for (JsonNode habitante : habitantes) {
            String urlHabitante = habitante.asText();
            Assertions.assertTrue(urlHabitante.startsWith(URL_BASE + "/character/"),
                    "La url '" + urlHabitante + "' no pertenece a la api de personajes");
            idsHabitantes.add(urlHabitante.substring(urlHabitante.lastIndexOf("/") + 1));
        }

        if (!idsHabitantes.isEmpty()) {
            new ReportesHTML().imprimirResultadoAllure(idsHabitantes);
        }
    }

    @Entonces("se valida que la respuesta de error de ubicaciones sea {string}")
    public void seValidaQueLaRespuestaDeErrorDeUbicacionesSea(String mensaje) {
        step.validateResponseCodeyContentType(respuestaActual, 404, ContentType.JSON);

        new ValidadorPostcondicion(respuestaActual.getBody())
                .compararCampoString("error", mensaje);
    }
}
