-- Inserts para tener usuarios en la base de datos
INSERT INTO usuario(nombre,correo,contrasena,calificacion, esadmin, habilitado, informacion, telefono)
VALUES ('darklink04x', 'darklink04x@hotmail.com', md5('darklink04x'), 0, FALSE, TRUE, 'Yo soy el primer usuario c:', '666');

INSERT INTO usuario (nombre, correo, contrasena, calificacion, esAdmin, habilitado, informacion, telefono)
VALUES ('enrique_bernal', 'enrique_bernal@ciencias.unam.mx', md5('enrique_bernal'), 0, TRUE, TRUE, 'Soy el unico administrador, muahaha', '56194403');