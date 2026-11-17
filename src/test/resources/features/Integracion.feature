#language: es
Característica: Integracion entre los recursos de Rick and Morty
  Como consumidor de la API de Rick and Morty
  Quiero validar la consistencia de las url que relacionan a los recursos
  Para asegurar que los datos de personajes, episodios y ubicaciones sean coherentes

  @regression @test @integracion
  Escenario: El primer episodio de un personaje lo incluye en su listado
    Dado que se consulta el personaje con id 1 y el episodio de su primera aparicion
    Cuando se ejecutan las consultas de integracion
    Entonces el episodio consultado incluye al personaje con id 1

  @regression @test @integracion
  Escenario: El primer habitante de una ubicacion pertenece a esa ubicacion
    Dado que se consulta la ubicacion con id 1 y el habitante de su primer registro
    Cuando se ejecutan las consultas de integracion
    Entonces el habitante consultado pertenece a la ubicacion con id 1
