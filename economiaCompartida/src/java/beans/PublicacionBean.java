/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import model.Publicacion;
import model.Usuario;
import logic.PublicacionC;

/**
 * Clase bean utilizada para administrar el manejo de publicaciones en el sistema
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
     * Metodo que registra una publicacion a la base de datos y notifica al usuario en la vista lo ocurrido
     * @return 
     */
    public String registrarPublicacion() {
        try {
            helper.registrarBD(publicacion, usuario);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Publicacion realizada con éxito", null);
            faceContext.addMessage(null, message);
            publicacion = new Publicacion(); // Para resetear los campos de texto en la vista
        } catch (org.hibernate.TransientPropertyValueException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al publicar", null);
            faceContext.addMessage(null, message);
        }
        return "PublicarOfertaIH";
    }

    /**
     * Metodo que regresa un booleano que indica si la publicacion fue solicitada ya por alguien
     * @return Valor booleano que indica si la solicitud ya esta solicitada
     */
    public Boolean solicitada() {
        if (publicacion.getUsuarioByIdprestatario() != null) {
            return true;
        }
        return false;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

}
