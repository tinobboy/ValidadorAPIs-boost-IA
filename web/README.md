# Tests Web — Playwright con Java

Módulo del proyecto **ValidadorAPIs** que integra los tests E2E del proyecto
**ValidadorWeb** (originalmente en TypeScript) reescritos en **Java** con
**Playwright** y **JUnit 5**.

El flujo automatizado es el mismo:

1. Ingresar a `https://www.saucedemo.com`.
2. Validar el título y completar las credenciales (`standard_user` / `secret_sauce`).
3. Agregar un producto aleatorio al carrito.
4. Ir al carrito y completar el checkout.
5. Validar el mensaje de compra exitosa.

## Estructura

```
web/
├─ build.gradle
├─ README.md
└─ src/test/
   ├─ java/com/lucas/barabas/web/
   │  ├─ pages/                 # Page Objects (login, inventario, productos, checkout)
   │  ├─ tests/                 # SauceDemoWebTest
   │  └─ utils/                 # Assertions, Waits, DatosLogin
   └─ resources/JsonDatos/
      └─ datoLogin.json         # Credenciales de prueba
```

## Requisitos

- JDK 17 (el proyecto ya lo requiere).
- No hace falta Node.js: Playwright descarga sus propios navegadores.

## Instalación de navegadores

La primera vez hay que descargar el navegador que usa Playwright:

```bash
# Windows
gradlew.bat :web:installPlaywrightBrowsers

# Linux / macOS
./gradlew :web:installPlaywrightBrowsers
```

En Linux, si faltan dependencias del sistema:

```bash
./gradlew :web:installPlaywrightDeps
```

## Ejecución

```bash
# Windows
gradlew.bat :web:runWebTests

# Linux / macOS
./gradlew :web:runWebTests
```

Por defecto corre en modo headless (sin interfaz). Para ver el navegador:

```bash
gradlew.bat :web:runWebTests -Dplaywright.headless=false
```

El test se ejecuta de forma aislada: la tarea `build` / `test` del proyecto
principal no lo corre, para no exigir los navegadores en cada compilación.

## Diferencia con el proyecto original

- El `sleep` fijo de 6 segundos se reemplazó por la aserción de URL del
  Page Object de inventario, que espera automáticamente la navegación. El test
  queda más rápido y estable.
- Las credenciales se leen de `datoLogin.json` (en el proyecto TypeScript el
  archivo existía pero el test usaba valores hardcodeados).
- Las aserciones y esperas se portaron a las utilidades de Playwright Java.
