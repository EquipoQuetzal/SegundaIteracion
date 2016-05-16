set client_encoding = 'utf-8';

-- El nombre de la base debe ser "economiaCompartida" (Creo que existe un "CREATE DATABASE" pero no estoy seguro)


CREATE TABLE usuario(
   idUsuario      SERIAL PRIMARY KEY,
   nombre         TEXT NOT NULL,
   correo         TEXT NOT NULL,
   contrasena     TEXT NOT NULL,
   calificacion   INT,
   esAdmin        BOOLEAN,
   constraint correoUnico unique(correo),
   constraint correoCorrecto check (correo ~ E'^[\\w!#$%&\'*+/=?`{|}~^-]+(\\.[\\w!#$%&\'*+/=?`{|}~^-]+)*@[\\w-]+(\\.[\\w-]+)*$')
);
comment on table usuario
is
'Relacion usuario guarda los datos de un usuario registrado en el sitio';

CREATE TABLE publicacion(
   idPublicacion     SERIAL PRIMARY KEY,
   idUsuario         INT NOT NULL REFERENCES usuario(idUsuario),   -- Este id es del usuario que publico
   idPrestatario     INT REFERENCES usuario(idUsuario),            -- Este id es del usuario que tiene prestado el objeto
                                                                   -- puede ser nulo para indicar que el objeto no ha sido prestado.
   precio            REAL,
   estado            TEXT NOT NULL,
   descripcion	  	   TEXT NOT NULL,
   fechaPublicacion  DATE NOT NULL,
   calificacion      INT                                           -- La calificación de la publicación.
);
comment on table publicacion
is
'Relacion publicacion guarda los datos de una publicacion hecha por un usuario';

CREATE TABLE comentario(
   idComentario   SERIAL PRIMARY KEY,
   idUsuario      INT NOT NULL REFERENCES usuario(idUsuario),
   idPublicacion  INT NOT NULL REFERENCES publicacion(idPublicacion),
   fecha          DATE NOT NULL,
   contenido      TEXT NOT NULL
  );
comment on table comentario
is
'Relacion comentario guarda los datos de un comentario que se ha publicado en una publicacion por un usuario';

CREATE TABLE galeria(
   idFoto	   	SERIAL PRIMARY KEY,
   idPublicacion  INT    NOT NULL REFERENCES publicacion(idPublicacion),
   foto           TEXT                                              --Una foto será la url de la fotografía.
);
comment on table galeria
is
'Relacion galeria guarda las fotografías de cada publicacion';