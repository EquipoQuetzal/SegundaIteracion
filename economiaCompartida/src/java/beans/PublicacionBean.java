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
    
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN
    private Integer calificacion;
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN
    public PublicacionBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        helper = new PublicacionC();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
    }

    public String registrarPublicacion() {
        try {
            helper.registrarBD(publicacion, usuario);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Publicacion realizada con éxito", null);
            faceContext.addMessage(null, message);
            return "PublicarOfertaIH";
        } catch (org.hibernate.TransientPropertyValueException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al publicar", null);
            faceContext.addMessage(null, message);
            return "PublicarOfertaIH";
        }
        //return "PublicarOfertaIH"; // Por lo mientras regreso al perfil (Mostrar listado de publicaciones)
    }

    public Boolean prestado() {
        if (publicacion.getUsuarioByIdprestatario() != null) {
            return true;
        }
        return false;
    }

    public String pedir(){
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tu peticion de prestamo fue recibida", null);
        faceContext.addMessage(null, message);
        //helper.prestarPublicacion(publi, usuario); 2NDA Iteracion, falta mucho
        return "ConsultarIH";
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN
    
    
    public String calificarPublicacion(Publicacion publicacion) {
        
        this.helper.calificarPublicacion(publicacion, this.calificacion);
        return "CalificarIH";
    }
    public Usuario getUsuario(){
        return this.usuario;
    }
    public Integer getCalificacion(){
        return this.calificacion;
    }
    public void setCalificacion(Integer c){
        this.calificacion = c;
    }
   
    
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN

}