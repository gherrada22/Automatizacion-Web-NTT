package com.ntt.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.es.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class StoreStepDefinitions {

    // --- CONSTANTES ---
    private static final String BASE_URL = "https://qalab.bensg.com/store/";
    private static final String LOGIN_URL = BASE_URL + "pe/iniciar-sesion?back=https%3A%2F%2Fqalab.bensg.com%2Fstore%2Flogin";
    private static final String USUARIO_VALIDO = "virginialittm.s.3.0.1.997@gmail.com";
    private static final String CLAVE_VALIDA = "#Cuenta2001";

    private WebDriver driver;
    private WebDriverWait wait;

    // --- ESTADO COMPARTIDO ENTRE PASOS ---
    private double precioUnitarioProducto;
    private int cantidadProductoAgregado;
    private boolean categoriaInexistenteEncontrada;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--no-sandbox", "--disable-dev-shm-usage");
        // options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // --- MÉTODOS AUXILIARES (Lógica de página) ---

    private void login(String user, String pass) {
        driver.get(LOGIN_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("field-email"))).sendKeys(user);
        driver.findElement(By.id("field-password")).sendKeys(pass);
        driver.findElement(By.id("submit-login")).click();
    }

    private void handleNewsletterPopup() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement closeButton = shortWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#myModal .close")));
            closeButton.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("myModal")));
        } catch (TimeoutException | NoSuchElementException e) {
        }
    }

    // --- DEFINICIÓN DE PASOS (Gherkin Steps) ---

    @Dado("estoy en la página de la tienda")
    public void estoyEnLaPaginaDeLaTienda() {
        driver.get(BASE_URL);
        handleNewsletterPopup();
    }

    @Cuando("me logueo con usuario {string} y clave {string}")
    public void meLogueoConUsuarioYClave(String usuario, String clave) {
        String userToLogin = usuario.equals("tu_usuario") ? USUARIO_VALIDO : usuario;
        String passToLogin = clave.equals("tu_contraseña") ? CLAVE_VALIDA : clave;
        login(userToLogin, passToLogin);
    }

    @Entonces("debería ver el resultado de login {string}")
    public void deberiaVerElResultadoDeLogin(String resultado) {
        if ("exitoso".equalsIgnoreCase(resultado)) {
            WebElement signOutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("logout")));
            Assert.assertTrue("El login no fue exitoso, no se encontró el botón de 'Sign out'.", signOutButton.isDisplayed());
        } else {
            WebElement alertError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-danger")));
            Assert.assertTrue("El login no falló como se esperaba.", alertError.isDisplayed());
        }
    }

    @Dado("estoy logueado en la tienda con un usuario valido")
    public void estoyLogueadoEnLaTienda() {
        login(USUARIO_VALIDO, CLAVE_VALIDA);
        driver.get(BASE_URL);
        handleNewsletterPopup();
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("logout"))).isDisplayed());
    }


    @Cuando("navego a la categoría {string} y subcategoría {string}")
    public void navegoACategoriaYSubcategoria(String categoria, String subcategoria) {
        WebElement categoryLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@id='top-menu']/li/a[contains(text(),'" + categoria + "')]")));
        categoryLink.click();
        WebElement subCategoryLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='" + subcategoria + "']")));
        subCategoryLink.click();
    }

    @Y("agrego {int} unidades del primer producto al carrito")
    public void agregoUnidadesDelPrimerProductoAlCarrito(Integer unidades) {
        this.cantidadProductoAgregado = unidades; // Guardamos el número de unidades
        WebElement primerProducto = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//article[@class='product-miniature'])[1]")));

        String precioTexto = primerProducto.findElement(By.className("price")).getText().replaceAll("[^0-9,.]", "").replace(',', '.');
        this.precioUnitarioProducto = Double.parseDouble(precioTexto);

        WebElement botonAgregar = primerProducto.findElement(By.className("add-to-cart"));

        for (int i = 0; i < this.cantidadProductoAgregado; i++) {
            wait.until(ExpectedConditions.elementToBeClickable(botonAgregar)).click();
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Continuar comprando']")));
            continueButton.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("blockcart-modal")));
        }
    }

    @Cuando("finalizo la compra")
    public void finalizoLaCompra() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#_desktop_cart a"))).click();
    }

    @Entonces("valido el título de la página del carrito")
    public void validoElTituloDeLaPaginaDelCarrito() {
        Boolean titleIsCorrect = wait.until(ExpectedConditions.titleIs("Carrito"));
        Assert.assertTrue("No se llegó a la página del carrito.", titleIsCorrect);
    }

    @Y("vuelvo a validar el calculo de precios en el carrito")
    public void vuelvoAValidarElCalculoDePreciosEnElCarrito() {

        WebElement precioTotalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-summary-line.cart-total .value")));

        String totalTexto = precioTotalElement.getText().replaceAll("[^0-9,.]", "").replace(',', '.');
        double totalMostrado = Double.parseDouble(totalTexto);


        double totalCalculado = this.precioUnitarioProducto * this.cantidadProductoAgregado;

        Assert.assertEquals("El total en la página del carrito no es correcto.", totalCalculado, totalMostrado, 0.01);
    }

    // --- PRUEBA NEGATIVA CORREGIDA ---

    @Cuando("intento navegar a la categoría inexistente {string}")
    public void intentoNavegarALaCategoriaInexistente(String categoria) {
        // ESTA es la forma correcta. Busca una LISTA de elementos.
        List<WebElement> elements = driver.findElements(By.xpath("//ul[@id='top-menu']/li/a[contains(text(),'" + categoria + "')]"));
        // Guardamos el resultado de la búsqueda para la aserción en el paso "Entonces".
        this.categoriaInexistenteEncontrada = !elements.isEmpty();
    }

    @Entonces("la prueba debe fallar de forma controlada")
    public void laPruebaDebeFallarDeFormaControlada() {
        Assert.assertFalse("Error: La categoría inexistente fue encontrada, pero no debería existir.", categoriaInexistenteEncontrada);
    }
}