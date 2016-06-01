/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import model.Publicacion;
import model.Usuario;
import logic.PublicacionC;

/**
 * Clase bean utilizada para administrar el manejo de publicaciones en el
 * sistema
 *
 * @author jorge
 */
@ManagedBean
@RequestScoped
public class PublicacionBean {

    private Usuario usuario = new Usuario();
    private Publicacion publicacion = new Publicacion(); //la nueva publicacion
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    private PublicacionC helper;
    private Part imagen;

    /**
     * Constructor por omision
     */
    public PublicacionBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        helper = new PublicacionC();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
    }

    /**
     * Metodo que registra una publicacion a la base de datos y notifica al
     * usuario en la vista lo ocurrido
     *
     * @return Cadena que representa la pagina a direccionar luego de realizar
     * el metodo
     */
    public String registrarPublicacion() {
        try { // Registramos la publicacion con el usuario asociado
            int identificador = helper.registrarBD(publicacion, usuario);
            upload(identificador);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Publicacion realizada con éxito", null);
            faceContext.addMessage(null, message);
            publicacion = new Publicacion(); // Para resetear los campos de texto en la vista
        } catch (org.hibernate.TransientPropertyValueException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al publicar", null);
            faceContext.addMessage(null, message);
            return "PublicarOfertaIH";
        } catch (IOException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al guardar la imagen", null);
            faceContext.addMessage(null, message);
            Logger.getLogger(PublicacionBean.class.getName()).log(Level.SEVERE, null, ex);
            return "PublicarOfertaIH";
        }
        return "index";
    }

    /**
     * Metodo que carga el objeto subido en la vista
     * Y lo guarda en un directorio absoluto
     * @throws IOException 
     */
    public void upload(int id) throws IOException {
        InputStream inputStream = imagen.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(getFilename(imagen));

        byte[] buffer = new byte[4096];
        int bytesRead;
        while (true) {
            bytesRead = inputStream.read(buffer);
            if (bytesRead > 0) {
                outputStream.write(buffer, 0, bytesRead);
            } else {
                break;
            }
        }
        outputStream.close();
        inputStream.close();
        System.out.println("Imagen cargada correctamente");
        
        File archivo = new File("//home//jorge//Escritorio//proyectodeingenieria //SegundaIteracion-develop//economiaCompartida//web//imagenesPublicaciones//"+id+".jpeg");
        Path ruta = archivo.toPath();
        try(InputStream input = imagen.getInputStream()){
            Files.copy(input, ruta, REPLACE_EXISTING);
        }
        System.out.println("Imagen disque guardada en: "+ruta);
    }

    /**
     * Metodo que obtiene el nombre de archivo a partir de un objeto de datos
     * @param part Objeto con datos del archivo subido
     * @return String con el nombre del archivo
     */
    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public Part getImagen() {
        return imagen;
    }

    public void setImagen(Part imagen) {
        this.imagen = imagen;
    }

}
