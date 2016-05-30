-- Inserts para tener usuarios en la base de datos
INSERT INTO usuario(nombre,correo,contrasena,calificacion, esadmin, habilitado, informacion, telefono)
VALUES ('usuario', 'usuario@gmail.com', md5('12345'), 0, FALSE, TRUE, 'Yo soy el primer usuario c:', '666');

INSERT INTO usuario (nombre, correo, contrasena, calificacion, esAdmin, habilitado, informacion, telefono)
VALUES ('kike', 'kike@mail.com', md5('kike'), 0, TRUE, TRUE, 'Administrador', '56194403');