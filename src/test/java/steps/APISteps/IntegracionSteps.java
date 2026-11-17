package steps.APISteps;

import com.fasterxml.jackson.databind.JsonNode;
import constants.RequestMethods;
import core.RequestSpecification;
import core.ResponseSpecification;
import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import steps.BaseSteps;
import steps.RequestSteps;
import utils.ValidadorPostcondicion;

/**
 * Steps de los escenarios de integracion entre los recursos de la API de Rick and Morty.
 * Se valida que las url que relacionan a los recursos (/character, /episode y /location) sean consistentes
 * en ambas direcciones.
 *
 * @author tinobboy
 * @version 0.1.0
 */
public class IntegracionSteps {

    private static final String URL_BASE = "https://rickandmortyapi.com/api";
    private static final BaseSteps step = new BaseSteps();

    private RequestSpecification requestPrincipal;
    private RequestSpecification requestRelacionado;
    private ResponseSpecification respuestaPrincipal;
    private ResponseSpecification respuestaRelacionada;
    private String campoRelacionado;

    @Before("@integracion")
    public void beforeIntegracion() {
        requestPrincipal = null;
        requestRelacionado = null;
        respuestaPrincipal = null;
        respuestaRelacionada = null;
        campoRelacionado = "";
    }

    @Dado("que se consulta el personaje con id {int} y el episodio de su primera aparicion")
    public void queSeConsultaElPersonajeYSuPrimerEpisodio(int id) {
        requestPrincipal = new RequestSpecification(URL_BASE, RequestMethods.GET);
        requestPrincipal.setPaths("/character/" + id);
        campoRelacionado = "episode";
    }

    @Dado("que se consulta la ubicacion con id {int} y el habitante de su primer registro")
    public void queSeConsultaLaUbicacionYSuPrimerHabitante(int id) {
        requestPrincipal = new RequestSpecification(URL_BASE, RequestMethods.GET);
        requestPrincipal.setPaths("/location/" + id);
        campoRelacionado = "residents";
    }

    @Cuando("se ejecutan las consultas de integracion")
    public void seEjecutanLasConsultasDeIntegracion() {
        respuestaPrincipal = new RequestSteps().executeRequest(requestPrincipal);
        step.validateResponseCodeyContentType(respuestaPrincipal, 200, ContentType.JSON);

        JsonNode recursosRelacionados = respuestaPrincipal.getBody().get(campoRelacionado);
        Assertions.assertNotNull(recursosRelacionados,
                "La respuesta principal no posee el campo " + campoRelacionado);
        Assertions.assertTrue(recursosRelacionados.isArray(),
                "El campo " + campoRelacionado + " no es un array");
        Assertions.assertFalse(recursosRelacionados.isEmpty(),
                "La respuesta principal no posee recursos relacionados en el campo " + campoRelacionado);

        String urlRelacionada = recursosRelacionados.get(0).asText();
        Assertions.assertTrue(urlRelacionada.startsWith(URL_BASE + "/"),
                "La url '" + urlRelacionada + "' no pertenece a la api de Rick and Morty");

        requestRelacionado = new RequestSpecification(URL_BASE, RequestMethods.GET);
        requestRelacionado.setPaths(urlRelacionada.replace(URL_BASE, ""));

        respuestaRelacionada = new RequestSteps().executeRequest(requestRelacionado);
        step.validateResponseCodeyContentType(respuestaRelacionada, 200, ContentType.JSON);
    }

    @Entonces("el episodio consultado incluye al personaje con id {int}")
    public void elEpisodioConsultadoIncluyeAlPersonajeConId(int id) {
        new ValidadorPostcondicion(respuestaRelacionada.getBody())
                .validarExistenciaCampoString("name")
                .validarExistenciaCampoString("episode")
                .validarExistenciaCampoArray("characters");

        String urlPersonaje = URL_BASE + "/character/" + id;
        JsonNode personajesDelEpisodio = respuestaRelacionada.getBody().get("characters");

        Assertions.assertTrue(contieneUrl(personajesDelEpisodio, urlPersonaje),
                "El episodio '" + respuestaRelacionada.getBody().at("/name").asText()
                        + "' no incluye al personaje con id " + id);
    }

    @Entonces("el habitante consultado pertenece a la ubicacion con id {int}")
    public void elHabitanteConsultadoPerteneceALaUbicacionConId(int id) {
        new ValidadorPostcondicion(respuestaRelacionada.getBody())
                .validarExistenciaCampoString("name")
                .validarExistenciaCampoObject("location");

        String urlUbicacion = URL_BASE + "/location/" + id;
        String urlObtenida = respuestaRelacionada.getBody().at("/location/url").asText();

        Assertions.assertEquals(urlUbicacion, urlObtenida,
                "La ubicacion del habitante consultado no coincide con la ubicacion esperada");
        Assertions.assertEquals(respuestaPrincipal.getBody().at("/name").asText(),
                respuestaRelacionada.getBody().at("/location/name").asText(),
                "El nombre de la ubicacion del habitante no coincide con el nombre de la ubicacion consultada");
    }

    private boolean contieneUrl(JsonNode urls, String urlEsperada) {
        for (JsonNode url : urls) {
            if (urlEsperada.equals(url.asText())) {
                return true;
            }
        }
        return false;
    }
}
