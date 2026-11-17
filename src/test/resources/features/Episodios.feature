#language: es
Característica: Episodios de Rick and Morty
  Como consumidor de la API de Rick and Morty
  Quiero consultar la informacion de los episodios
  Para validar que el servicio expone los datos correctamente

  @smoke @test @episodios
  Escenario: Se obtiene el detalle de un episodio por su id
    Dado que se consulta el episodio con id 1
    Cuando se ejecuta la consulta de episodios
    Entonces se valida el episodio "Pilot" con codigo "S01E01" emitido el "December 2, 2013"
    Y se valida que el episodio tenga los campos principales
    Y se valida que el episodio tenga personajes asociados

  @test @episodios
  Esquema del escenario: Se valida el detalle de los primeros episodios de la serie <id> <nombre> <codigo> <fechaEmision>
    Dado que se consulta el episodio con id <id>
    Cuando se ejecuta la consulta de episodios
    Entonces se valida el episodio "<nombre>" con codigo "<codigo>" emitido el "<fechaEmision>"
    Y se valida que el episodio tenga los campos principales
    Ejemplos:
      | id | nombre                  | codigo | fechaEmision       |
      | 1  | Pilot                   | S01E01 | December 2, 2013   |
      | 2  | Lawnmower Dog           | S01E02 | December 9, 2013   |
      | 3  | Anatomy Park            | S01E03 | December 16, 2013  |
      | 4  | M. Night Shaym-Aliens!  | S01E04 | January 13, 2014   |
      | 5  | Meeseeks and Destroy    | S01E05 | January 20, 2014   |

  @test @episodios
  Escenario: Se obtiene el listado de episodios de una temporada
    Dado que se consultan los episodios de la temporada "S01"
    Cuando se ejecuta la consulta de episodios
    Entonces se valida que todos los episodios obtenidos sean de la temporada "S01"

  @test @episodios
  Escenario: Se buscan episodios por su nombre
    Dado que se buscan episodios por el nombre "pilot"
    Cuando se ejecuta la consulta de episodios
    Entonces se valida que la busqueda de episodios devuelva resultados para "pilot"

  @test @episodios
  Escenario: Se obtiene el listado paginado de episodios
    Dado que se consultan los episodios de la pagina 2
    Cuando se ejecuta la consulta de episodios
    Entonces se valida que la lista de episodios tenga informacion de paginacion

  @test @episodios
  Escenario: Se obtiene un error al consultar un episodio inexistente
    Dado que se consulta un episodio inexistente con id 999
    Cuando se ejecuta la consulta de episodios
    Entonces se valida que la respuesta de error de episodios sea "Episode not found"
