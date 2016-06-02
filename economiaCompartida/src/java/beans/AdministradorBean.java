/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import logic.UsuarioC;
import model.Usuario;
import model.Publicacion;
import logic.PublicacionC;

/**
 * Bean encargado de gestionar cuestiones relacionadas al manejo de administradores en el sitio
 * @author oem
 */
@ManagedBean
@RequestScoped
@ApplicationScoped
public class AdministradorBean {

    private Usuario usuario = new Usuario();    //Representa al usuario actual
    private Publicacion publicacion = new Publicacion();
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    private UsuarioC helper;
    private PublicacionC helper2;

    /**
     * Constructor por defecto
     */
    public AdministradorBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        helper = new UsuarioC();
        helper2 = new PublicacionC();
    }
    
    /**
     * Metodo que elimina a un usuario de la base de datos, de acuerdo a lo obtenido en la vista
     * @return BorrarUsuarioIH, cadena que representa que se quedara en la misma pantalla de borrado de usuarios
     */
    public String borrarUsuario() {
        model.Usuario usuarioBD = helper.buscarPorCorreo(usuario.getCorreo());
        if (usuarioBD != null) {
            try {
                usuario = usuarioBD;
                //Borrando al usuario
                helper.borrarUsuarioBD(usuario);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "El usuario del correo: " + usuario.getCorreo() + " ha sido borrado.", null);
                faceContext.addMessage(null, message);
            } catch (org.hibernate.exception.ConstraintViolationException ex) {
                //helper.getSession().getTransaction().rollback(); // Sujeto a cambio, aun no se sabe si es necesario
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario: " + usuario.getCorreo() + " aun tiene publicaciones en el sitio.", null);
                faceContext.addMessage(null, message);
            } catch (Exception ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { //El correo es incorrecto (Pues no se encontro ningun usuario con ese correo)
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo: " + usuario.getCorreo() + " no existe en la base de datos.", null);
            faceContext.addMessage(null, message);
        }
        return "BorrarUsuarioIH";
    }

    /**
     * Metodo que elimina una publicacion de la base de datos, de acuerdo a lo obtenido en la vista
     * @return BorrarUsuarioIH, cadena que representa que se quedara en la misma pantalla de borrado de usuarios
     */
    public String borrarPublicacion() {
        model.Publicacion publicacionBD = helper2.buscarPublicacion(publicacion.getIdpublicacion());
        if (publicacionBD != null) {
            try {
                publicacion = publicacionBD;
                //Borrando la publicacion
                helper2.borrarPublicacionBD(publicacion);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "La publicacion con el id: " + publicacion.getIdpublicacion() + " ha sido borrada.", null);
                faceContext.addMessage(null, message);
            } catch (Exception ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { //La publicacion solicitada no se encuentra en la base de datos
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La publicacion con el ID: " + publicacion.getIdpublicacion() + " no existe.", null);
            faceContext.addMessage(null, message);
        }
        return "BorrarPublicacionIH";
    }

    public String habilitarUsuario(){
        model.Usuario usuarioBD = helper.buscarPorCorreo(usuario.getCorreo());
        if (usuarioBD != null) {
            if(usuarioBD.getHabilitado()==true){
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "El usuario del correo: " + usuario.getCorreo() + " ya estaba habilitado.", null);
                faceContext.addMessage(null, message);
            }else{
                try {
                    usuario = usuarioBD;
                    usuario.setHabilitado(true);
                    helper.habilitarUsuarioBD(usuario);
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "El usuario del correo: " + usuario.getCorreo() + " ha sido habilitado.", null);
                    faceContext.addMessage(null, message);
                } catch (Exception ex) {
                    Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else { //El correo es incorrecto (Pues no se encontro ningun usuario con ese correo)
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo: " + usuario.getCorreo() + " no existe en la base de datos.", null);
            faceContext.addMessage(null, message);
        }
        return "HabilitarUsuarioIH";
    }
    
        public String deshabilitarUsuario(){
        model.Usuario usuarioBD = helper.buscarPorCorreo(usuario.getCorreo());
        if (usuarioBD != null) {
            if(usuarioBD.getHabilitado()==false){
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "El usuario del correo: " + usuario.getCorreo() + " ya habia sido deshabilitado.", null);
                faceContext.addMessage(null, message);
            }else{
                try {
                    usuario = usuarioBD;
                    usuario.setHabilitado(false);
                    helper.habilitarUsuarioBD(usuario);
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "El usuario del correo: " + usuario.getCorreo() + " ha sido deshabilitado.", null);
                    faceContext.addMessage(null, message);
                } catch (Exception ex) {
                    Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else { //El correo es incorrecto (Pues no se encontro ningun usuario con ese correo)
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo: " + usuario.getCorreo() + " no existe en la base de datos.", null);
            faceContext.addMessage(null, message);
        }
        return "DeshabilitarUsuarioIH";
        
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

}
