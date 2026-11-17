#language: es
Característica: Ubicaciones de Rick and Morty
  Como consumidor de la API de Rick and Morty
  Quiero consultar la informacion de las ubicaciones
  Para validar que el servicio expone los datos correctamente

  @smoke @test @ubicaciones
  Escenario: Se obtiene el detalle de una ubicacion por su id
    Dado que se consulta la ubicacion con id 1
    Cuando se ejecuta la consulta de ubicaciones
    Entonces se valida la ubicacion "Earth (C-137)" de tipo "Planet" y dimension "Dimension C-137"
    Y se valida que la ubicacion tenga los campos principales
    Y se valida que los habitantes de la ubicacion referencien a la api de personajes

  @test @ubicaciones
  Esquema del escenario: Se valida el detalle de las primeras ubicaciones <id> <nombre> <tipo> <dimension>
    Dado que se consulta la ubicacion con id <id>
    Cuando se ejecuta la consulta de ubicaciones
    Entonces se valida la ubicacion "<nombre>" de tipo "<tipo>" y dimension "<dimension>"
    Y se valida que la ubicacion tenga los campos principales
    Ejemplos:
      | id | nombre            | tipo          | dimension       |
      | 1  | Earth (C-137)     | Planet        | Dimension C-137 |
      | 2  | Abadango          | Cluster       | unknown         |
      | 3  | Citadel of Ricks  | Space station | unknown         |
      | 5  | Anatomy Park      | Microverse    | Dimension C-137 |

  @test @ubicaciones
  Escenario: Se obtiene el listado paginado de ubicaciones
    Dado que se consultan las ubicaciones de la pagina 1
    Cuando se ejecuta la consulta de ubicaciones
    Entonces se valida que la lista de ubicaciones tenga informacion de paginacion

  @test @ubicaciones
  Escenario: Se buscan ubicaciones por su dimension
    Dado que se buscan ubicaciones por la dimension "Dimension C-137"
    Cuando se ejecuta la consulta de ubicaciones
    Entonces se valida que todas las ubicaciones obtenidas pertenezcan a la dimension "Dimension C-137"

  @test @ubicaciones
  Escenario: Se obtiene un error al consultar una ubicacion inexistente
    Dado que se consulta una ubicacion inexistente con id 999
    Cuando se ejecuta la consulta de ubicaciones
    Entonces se valida que la respuesta de error de ubicaciones sea "Location not found"
