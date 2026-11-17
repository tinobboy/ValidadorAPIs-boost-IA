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

  @test @personajes
  Esquema del escenario: Se valida el detalle de los personajes principales <id> <nombre> <estado> <especie> <genero>
    Dado que se consulta el personaje con id <id>
    Cuando se ejecuta la consulta de personajes
    Entonces se valida el personaje "<nombre>" con estado "<estado>", especie "<especie>" y genero "<genero>"
    Y se valida que el personaje tenga los campos principales
    Ejemplos:
      | id | nombre       | estado | especie | genero |
      | 1  | Rick Sanchez | Alive  | Human   | Male   |
      | 2  | Morty Smith  | Alive  | Human   | Male   |
      | 3  | Summer Smith | Alive  | Human   | Female |
      | 4  | Beth Smith   | Alive  | Human   | Female |
      | 5  | Jerry Smith  | Alive  | Human   | Male   |

  @test @personajes
  Escenario: Se obtiene el listado paginado de personajes
    Dado que se consultan los personajes de la pagina 2
    Cuando se ejecuta la consulta de personajes
    Entonces se valida que la lista de personajes tenga informacion de paginacion

  @test @personajes
  Escenario: Se buscan personajes por su nombre
    Dado que se buscan personajes por el nombre "summer"
    Cuando se ejecuta la consulta de personajes
    Entonces se valida que la busqueda de personajes devuelva resultados para "summer"

  @test @personajes
  Escenario: Se filtran los personajes por estado y especie
    Dado que se filtran los personajes por estado "alive" y especie "human"
    Cuando se ejecuta la consulta de personajes
    Entonces se valida que los personajes obtenidos tengan el estado "Alive" y la especie contenga "Human"

  @test @personajes
  Escenario: Se obtiene un error al consultar un personaje inexistente
    Dado que se consulta un personaje inexistente con id 999
    Cuando se ejecuta la consulta de personajes
    Entonces se valida que la respuesta de error de personajes sea "Character not found"
