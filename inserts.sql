-- Inserts para tener usuarios en la base de datos
INSERT INTO usuario(nombre,correo,contrasena,calificacion, esadmin)
VALUES ('usuario', 'usuario@gmail.com', md5('12345'), 0, FALSE);

INSERT INTO usuario (nombre, correo, contrasena, calificacion, esAdmin)
VALUES ('kike','kike@mail.com',md5('kike'),0,true);