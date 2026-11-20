package com.lucas.barabas.web.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Datos de login leídos desde el JSON compartido con el proyecto original.
 */
public record DatosLogin(String usuario, String password) {

    public static DatosLogin desdeRecurso(String ruta) {
        try (InputStream input = DatosLogin.class.getClassLoader().getResourceAsStream(ruta)) {
            if (input == null) {
                throw new IllegalStateException("No se encontró el recurso de datos: " + ruta);
            }
            JsonNode root = new ObjectMapper().readTree(input);
            JsonNode data = root.path("data");
            return new DatosLogin(data.path("Username").asText(), data.path("Password").asText());
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el recurso de datos: " + ruta, e);
        }
    }
}
