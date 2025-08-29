Feature: Pruebas de Regresión de la Tienda Online

  @login
  Scenario Outline: Verificación de inicio de sesión con diferentes credenciales
    Given estoy en la página de la tienda
    When me logueo con usuario "<usuario>" y clave "<clave>"
    Then debería ver el resultado de login "<resultado>"

    Examples:
      | usuario     | clave        | resultado |
      | tu_usuario  | tu_contraseña| exitoso   |
      | usuario_mal | clave_mal    | fallido   |

  @main_scenario
  Scenario: Validación completa del precio y flujo de compra de un producto
    Given estoy logueado en la tienda con un usuario valido
    When navego a la categoría "Clothes" y subcategoría "Men"
    And agrego 2 unidades del primer producto al carrito
    Then valido en el popup la confirmación del producto agregado
    And valido en el popup que el monto total sea calculado correctamente
    When finalizo la compra
    Then valido el título de la página del carrito
    And vuelvo a validar el calculo de precios en el carrito

  @non_existent_category
  Scenario: Intento de navegar a una categoría que no existe
    Given estoy logueado en la tienda con un usuario valido
    When intento navegar a la categoría inexistente "Autos"
    Then la prueba debe fallar de forma controlada