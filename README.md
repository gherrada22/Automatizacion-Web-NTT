# Automatización de Pruebas de Regresión - Tienda Online

Este repositorio contiene un framework de automatización de pruebas de regresión para la tienda online de QALab. El objetivo es verificar la funcionalidad crítica del sitio, desde el inicio de sesión hasta el proceso de compra, para detectar defectos de forma temprana.

El proyecto está diseñado para ser ejecutado de forma periódica (ej. diariamente) en un entorno de Integración Continua.

---

## Stack Tecnológico

Este proyecto está construido con herramientas estándar de la industria, no con juguetes.

*   **Lenguaje:** [Java](https://www.java.com/es/) (JDK 11+)
*   **Framework de Automatización:** [Selenium WebDriver](https://www.selenium.dev/)
*   **Framework BDD:** [Cucumber](https://cucumber.io/)
*   **Gestor de Proyecto y Dependencias:** [Maven](https://maven.apache.org/)
*   **Test Runner:** [JUnit 4](https://junit.org/junit4/)
*   **Gestión de Drivers:** [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)

---

## Escenarios Cubiertos

La suite de pruebas cubre los siguientes flujos críticos de la aplicación:

*   **Autenticación de Usuarios:**
    *   Verificación de inicio de sesión con credenciales válidas.
    *   Verificación de inicio de sesión con credenciales inválidas.
*   **Flujo de Compra:**
    *   Navegación a una categoría y subcategoría de productos.
    *   Adición de múltiples unidades de un producto al carrito.
    *   Validación de precios en la página del carrito.
*   **Navegación:**
    *   Verificación de que no se puede navegar a categorías inexistentes.

---

## Requisitos Previos

Asegúrate de que tu sistema cumple con los siguientes requisitos antes de continuar:

*   **JDK 11** o superior.
*   **Apache Maven** instalado y configurado en el `PATH` del sistema.
*   **Git** instalado.
*   Un navegador web moderno (ej. **Google Chrome**).

## Configuración del Proyecto

### 1. Clonar el Repositorio

No hay magia aquí. Clona el repositorio en tu máquina local.

```bash
git clone https://github.com/gherrada22/Automatizacion-Web-NTT.git
cd Automatizacion-Web-NTT
```

### 2. Configurar Credenciales

**ESTE PASO ES OBLIGATORIO.** La automatización necesita una cuenta de usuario **real y válida** para funcionar.

1.  Crea una cuenta manualmente en la [tienda online](https://qalab.bensg.com/store/).
2.  Abre el siguiente archivo en el proyecto:
    `src/test/java/com/ntt/stepdefinitions/StoreStepDefinitions.java`
3.  Modifica las siguientes variables con el email y la contraseña que registraste:

    ```java
    private final String USUARIO_VALIDO = "tu-correo-registrado@ejemplo.com";
    private final String CLAVE_VALIDA = "tu-contraseña-real";
    ```

El proyecto está diseñado para que Maven descargue e instale todas las demás dependencias automáticamente.

---

## Ejecución de las Pruebas

Las pruebas se pueden ejecutar de dos maneras.

### Método 1: Desde la Terminal (Recomendado)

Esta es la forma correcta y la que se usaría en un sistema de Integración Continua. Abre una terminal en la raíz del proyecto y ejecuta:

```bash
mvn clean test
```

Este comando hará lo siguiente:
*   `clean`: Borra cualquier compilación anterior para asegurar una ejecución limpia.
*   `test`: Compila el código y ejecuta todas las pruebas definidas en el `TestRunner`.

### Método 2: Desde IntelliJ IDEA

Para depuración y desarrollo rápido:

1.  Abre el proyecto en IntelliJ.
2.  Espera a que Maven resuelva las dependencias.
3.  Navega hasta `src/test/java/com/ntt/runners/TestRunner.java`.
4.  Haz clic derecho en el archivo y selecciona **Run 'TestRunner'**.

---

## Visualización de Reportes

Al finalizar la ejecución, Cucumber genera un reporte HTML con el detalle de cada escenario y paso.

Para verlo, abre el siguiente archivo en tu navegador web:

`target/cucumber-reports.html`

---


Copyright (c) 2025 [gherrada22]
