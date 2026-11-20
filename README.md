<div align="center">

# 🧪 API Testing Framework — Rick and Morty

### Automatización de pruebas de backend con Java, Cucumber y RestAssured

Framework de testing BDD diseñado para crear, ejecutar y mantener pruebas de API de forma legible, reutilizable y escalable.

[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Gradle](https://img.shields.io/badge/Gradle-7.6.4-02303A?logo=gradle&logoColor=white)](https://gradle.org/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.11-23D96C?logo=cucumber&logoColor=white)](https://cucumber.io/)
[![RestAssured](https://img.shields.io/badge/RestAssured-5.5-4CAF50)](https://rest-assured.io/)
[![JUnit](https://img.shields.io/badge/JUnit-5.7-E2B714)](https://junit.org/)
[![Allure](https://img.shields.io/badge/Allure-2.17-FF6A00)](https://allure.qatools.ru/)

[![Java CI with Gradle](https://github.com/tinobboy/ValidadorAPIs/actions/workflows/gradle.yml/badge.svg)](https://github.com/tinobboy/ValidadorAPIs/actions/workflows/gradle.yml)
![Escenarios](https://img.shields.io/badge/escenarios-37%20passing-brightgreen)
![BDD](https://img.shields.io/badge/BDD-Gherkin-6DB33F)

</div>

---

## 📌 Descripción

**ValidadorAPIs** es un framework de automatización de pruebas de backend escrito en **Java** que combina **Cucumber (BDD)**, **Gradle**, **RestAssured** y **JUnit** para validar servicios REST y SOAP.

Los escenarios se escriben en **Gherkin en español**, lo que permite que cualquier integrante del equipo —técnico o no— entienda qué se está probando, mientras que la lógica de ejecución y las validaciones quedan encapsuladas en capas reutilizables.

El proyecto se valida contra la API pública de [Rick and Morty](https://rickandmortyapi.com/documentation), cubriendo los recursos de **personajes**, **episodios** y **ubicaciones**.

> **¿Qué demuestra este proyecto?**
> Diseño de frameworks de testing, BDD, programación orientada a objetos aplicada a la automatización, validación de contratos de API, generación de reportes y automatización con CI/CD.

---

## ✨ Características principales

- 🥒 **BDD con Gherkin en español**: escenarios legibles y enfocados en el comportamiento del servicio.
- 🧱 **Framework propio sobre RestAssured**: abstracciones `RequestSpecification` / `ResponseSpecification` para construir requests y validar responses.
- 🔗 **Validadores encadenables**: `ValidadorPostcondicion` (campos y tipos) y `ValidadorListado` (paginación y colecciones).
- 📊 **Reportes Allure** con pasos, requests/responses adjuntos y tablas HTML generadas a partir de las respuestas.
- 🏷️ **Ejecución selectiva por etiquetas** (`@smoke`, `@regression`, `@personajes`, `@episodios`, `@ubicaciones`, `@integracion`).
- 🔁 **Escenarios data-driven** mediante `Esquema del escenario` + `Ejemplos`.
- 🌐 **Soporte REST y SOAP** dentro del mismo framework.
- 🛡️ **Reintentos automáticos ante `429 Too Many Requests`** para estabilizar ejecuciones contra APIs públicas.
- ⚙️ **Configuración por ambiente** mediante YAML (`settings.yml`) y propiedades del sistema.
- 🤖 **Integración continua** con GitHub Actions.

---

## 🧰 Stack tecnológico

| Tecnología | Versión | Uso |
| --- | --- | --- |
| Java | 17 | Lenguaje del framework |
| Gradle | 7.6.4 | Build, dependencias y tareas |
| Cucumber | 7.11.1 | BDD / Gherkin |
| RestAssured | 5.5.5 | Cliente HTTP y validaciones |
| JUnit | 5.7 / 4.13 | Runner y aserciones |
| Allure | 2.17 / plugin 2.11 | Reportes y trazabilidad |
| Jackson | 2.19 | Manejo de JSON / YAML |
| WireMock | 3.13.1 | Mocking de servicios |
| Playwright | 1.50 | Soporte de automatización web |

---

## 🏗️ Arquitectura

El framework separa claramente las responsabilidades:

```
ValidadorAPIs/
├─ src/main/java/
│  ├─ core/                # Request/Response Specifications (REST y SOAP)
│  ├─ steps/               # RequestSteps: ejecución y reintentos de requests
│  ├─ constants/           # HTTPCodes, métodos, headers, content types
│  ├─ helpers/             # JSON, Allure, logging, configuración
│  ├─ listeners/           # Event listeners de Allure
│  ├─ readers/             # Lectura de propiedades
│  └─ configuraciones/     # Proyecto, Servicio, Configuración
│
├─ src/test/
│  ├─ java/
│  │  ├─ steps/APISteps/   # Step definitions por recurso + validaciones
│  │  ├─ utils/            # Validadores, reportes HTML, utilidades
│  │  ├─ enums/            # Datos esperados (DatosPersonajes)
│  │  └─ test/java/tests/  # Runner TestsBDD
│  └─ resources/
│     ├─ features/         # Escenarios Gherkin
│     ├─ Styles/           # CSS de los adjuntos HTML
│     └─ defaults.yml
│
├─ web/                    # Módulo E2E con Playwright (Java)
│  ├─ src/test/java/       # Page Objects, tests y utilidades web
│  ├─ src/test/resources/  # Datos de prueba (login)
│  └─ README.md
├─ settings.yml            # Configuración por ambiente
├─ build.gradle            # Dependencias y tareas
└─ README.md
```

**Flujo de una prueba**

```
.feature → Step definitions → RequestSteps → RestAssured
                                   ↓
                         ResponseSpecification
                                   ↓
                  ValidadorPostcondicion / ValidadorListado
                                   ↓
                          Reporte + adjuntos Allure
```

---

## ✅ Cobertura de pruebas

La suite cuenta con **37 escenarios** distribuidos por recurso:

| Recurso | Endpoint | Escenarios | Validaciones clave |
| --- | --- | ---: | --- |
| Personajes | `GET /character`, `/character/{id}` | 10 | Esquema, status, especie, género, origen, episodios, paginación, búsqueda, filtros y 404 |
| Episodios | `GET /episode`, `/episode/{id}` | 10 | Esquema, código `SXXEXX`, fecha de emisión, personajes asociados, temporada, paginación y 404 |
| Ubicaciones | `GET /location`, `/location/{id}` | 8 | Esquema, tipo, dimensión, habitantes, paginación, filtro por dimensión y 404 |
| Integración | `/character` + `/episode`, `/location` | 2 | Consistencia bidireccional de las URLs entre recursos |
| Smoke / Regresión | `/episode/{1..7}` | 7 | Personajes por episodio comparados con la API de personajes |

| Archivo `.feature` | Etiqueta |
| --- | --- |
| `Main.feature` | `@smoke`, `@test` |
| `Personajes.feature` | `@personajes`, `@test` |
| `Episodios.feature` | `@episodios`, `@test` |
| `Ubicaciones.feature` | `@ubicaciones`, `@test` |
| `Integracion.feature` | `@integracion`, `@regression`, `@test` |

---

## 🥒 Ejemplo de escenario

```gherkin
#language: es
Característica: Personajes de Rick and Morty
  Como consumidor de la API de Rick and Morty
  Quiero consultar la informacion de los personajes
  Para validar que el servicio expone los datos correctamente

  @smoke @test @personajes
  Escenario: Se obtiene el detalle de un personaje por su id
    Dado que se consulta el personaje con id 1
    Cuando se ejecuta la consulta de personajes
    Entonces se valida el personaje "Rick Sanchez" con estado "Alive", especie "Human" y genero "Male"
    Y se valida que el personaje tenga los campos principales
```

## ☕ Ejemplo de step definition

```java
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
            .validarExistenciaCampoArray("episode");
}
```

---

## ⚙️ Requisitos previos

| Herramienta | Versión mínima |
| --- | --- |
| JDK | 17 |
| Gradle | 7.6.4 (o usar el wrapper incluido) |
| IntelliJ IDEA | Última versión recomendada |
| Plugin | [Cucumber for Java](https://plugins.jetbrains.com/plugin/7212-cucumber-for-java) |
| Allure | Se resuelve automáticamente con el plugin de Gradle |

Asegurate de tener `JAVA_HOME` configurado y `java` disponible en el `PATH`.

---

## 🚀 Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/tinobboy/ValidadorAPIs.git
cd ValidadorAPIs
```

### 2. Ejecutar la suite desde la terminal

```bash
# Windows
gradlew.bat runTestsBDD

# Linux / macOS
./gradlew runTestsBDD
```

La tarea `runTestsBDD` ejecuta los escenarios etiquetados con `@test`.

### 3. Ejecutar desde IntelliJ IDEA

1. `File > Open` y seleccionar el directorio del proyecto.
2. Verificar que sea reconocido como proyecto Gradle y sincronizar.
3. Abrir `src/test/java/test/java/tests/TestsBDD.java`.
4. Clic derecho → **Run 'TestsBDD'**.

### 4. Generar y ver el reporte Allure

```bash
./gradlew allureServe
```

También podés generarlo sin abrirlo automáticamente:

```bash
./gradlew allureReport
```

El reporte incluye pasos, requests/responses y adjuntos HTML con los listados obtenidos.

### 5. Ejecutar un subconjunto por etiqueta

Modificá el filtro en `build.gradle`:

```gradle
task runTestsBDD(type: Test) {
    useJUnit()
    systemProperty "cucumber.filter.tags", "@personajes"
}
```

Etiquetas disponibles: `@smoke`, `@regression`, `@personajes`, `@episodios`, `@ubicaciones`, `@integracion`.

### 6. Ejecutar los tests web (Playwright con Java)

El proyecto incluye el módulo [`web/`](web/README.md) con el flujo E2E de
SauceDemo migrado desde el proyecto TypeScript original a **Playwright Java**.

```bash
# Primera vez: descargar el navegador
gradlew.bat :web:installPlaywrightBrowsers

# Ejecutar el test
gradlew.bat :web:runWebTests

# Ver el navegador (modo no headless)
gradlew.bat :web:runWebTests -Dplaywright.headless=false
```

En Linux/macOS reemplazá `gradlew.bat` por `./gradlew`.

---

## 🧠 Decisiones de diseño

- **Escenarios declarativos**: el `.feature` describe *qué* se valida; el *cómo* vive en los steps.
- **Bajo acoplamiento**: los steps delegan en utilidades de validación reutilizables en lugar de repetir aserciones.
- **Datos esperados centralizados**: los valores de referencia de los personajes se mantienen en `enums/DatosPersonajes`.
- **Configuración externalizada**: URLs y servicios se resuelven desde `settings.yml` según el ambiente.
- **Trazabilidad**: cada request, response y validación queda registrada en Allure.
- **Resiliencia**: reintentos automáticos ante rate limiting (`429`) para evitar fallos intermitentes.

---

## 🤖 Integración continua

El workflow [`.github/workflows/gradle.yml`](.github/workflows/gradle.yml) compila y valida el proyecto en cada push y pull request a `main` usando JDK 17 y el Gradle Wrapper.

---

## 🗺️ Roadmap

- [ ] Publicar el reporte Allure en GitHub Pages desde el pipeline.
- [ ] Validación de contrato con JSON Schema / Pact.
- [ ] Ejecución paralela de escenarios.
- [ ] Escenarios negativos con WireMock (timeouts, errores 5xx).
- [ ] Datos de prueba externalizados en CSV/JSON.
- [ ] Soporte multi-entorno completo (dev / qa / prod).

---

## 👤 Autor

**Lucas Barabas**

- GitHub: [@tinobboy](https://github.com/tinobboy)
- Email: luksbreaking@gmail.com

---

<div align="center">

Si te resultó útil o te interesa el enfoque, ¡dejá una ⭐ en el repositorio!

</div>
