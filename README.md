# PrimeraIteracion
Repositorio para el desarrollo de la Primera Iteración del proyecto de Software  

Equipo:  
  Quetzal  

Integrantes:  
  Bernal Cedillo Enrique Antonio  
  Chavez Maldonado Noe  
  García Flores Jorge Luis  
  Vergel Juárez Alan Luis  

Tips para implementar las cosas:  

-Ya hice una vista decente de inicio de sesion luego luego en la principal, hay k acomodarla como se debe

-Para k implementen lo suyo solo tienen k identificar el controlador a implementar (esos estan en el paquete "logic", se pueden guiar por los nombres del diagrama de controladores)  

-Los archivos .java de Model NO se mueven, esos son solo como contenedores para sacar cosas de la base de datos  

-Para usar las consultas con sql vean la k usé en SesionC.java esas consultas "namedQuery" deben estar registradas en los archivos .hbm.xml del paquete model, aun no se bien k significa todo pero pueden buscar las etiquetas y asi  

-Es en los beans donde se implementan varias cosas k interactuan con los modelos y controladores... ahi siento k falta acomodarlo o entenderle, no se bien k onda, si tienen duda pregunten bien si algun bean k vayan a usar lo esta usando alguien más del equipo, o hagan el suyo propio pero tampoco k sean tantos  

-Los metodos de los Beans deberian coincidir con los metodos de los modelos en el diagrama de Modelos  

Nombres de referencia, en caso de usar como guia otras fuentes:  

Java	| Ingenieria	|	Mi Prac2	|	Piña  
----------------------------------------------------  
Logic	| Controlador	|	DAO 		|	Helper  
Beans	| Modelo		|	Modelo		|	Modelo  
Model	| Tabla	 BD		|	Tabla BD	|	Tabla BD  
Web		| Vistas 		|	Vistas		|	Vistas  
