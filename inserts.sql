-- Inserts para tener usuarios y publicaciones  en la base de datos
INSERT INTO usuario(idusuario,nombre,correo,contrasena,calificacion, esadmin) VALUES (1, 'usuario1', 'usuario1@gmail.com', md5('12345'), 0, TRUE);
INSERT INTO usuario(idusuario,nombre,correo,contrasena,calificacion, esadmin) VALUES (2, 'usuario2', 'usuario2@gmail.com', md5('12345'), 0, TRUE);

INSERT INTO publicacion (idPublicacion,idUsuario,idPrestatario,precio,estado,descripcion,fechaPublicacion,calificacion)
VALUES (1, 1,2, 600.0,'Nuevo', 'Memoria Ram Corsair 4gb DDR4 2133MHZ', '2016/2/25',0);

INSERT INTO publicacion (idPublicacion,idUsuario,idPrestatario,precio,estado,descripcion,fechaPublicacion,calificacion)
VALUES (2, 1,2, 1000.0,'Usado', 'Memoria Ram Kingnston 4gb DDR4 2X 1866MHZ', '2016/2/25',0);

INSERT INTO publicacion (idPublicacion,idUsuario,idPrestatario,precio,estado,descripcion,fechaPublicacion,calificacion)
VALUES (3, 1,2, 5800.0,'Nuevo', 'Intel Core i5-6500 - Procesador (Intel Core i5-6xxx, 3.2 GHz, LGA1151, 64 GB, DDR3L-SDRAM, DDR4-SDRAM, 2133,1333,1600,1866 MHz)', '2016/2/25',0);

INSERT INTO publicacion (idPublicacion,idUsuario,idPrestatario,precio,estado,descripcion,fechaPublicacion,calificacion)
VALUES (4, 2,1, 12000.0,'Nuevo', 'Tarjeta Nvidia GTX 1080 8GB DDR5X', '2016/5/27',5);
