/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import logic.SesionC;
import model.Usuario;

/**
 *
 * @author Kikinzco
 */
@ManagedBean
@RequestScoped
public class Sesion {
    
    private Usuario usuario = new Usuario();    //Representa al usuario actual
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    private SesionC helper;
    
    public Sesion(){
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if (usuario == null)
            usuario = new Usuario();
        helper = new SesionC();
    }
    
    public Usuario getUsuario(){
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String login() {
        model.Usuario usuarioBD = helper.autentificar(usuario);
        if (usuarioBD != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(usuario.getContrasena().getBytes());
                byte[] digest = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    sb.append(String.format("%02x", b & 0xff));
                }               
                if (sb.toString().equals(usuarioBD.getContrasena())) { //La contrasena introducida coincide con la encontrada en la base de datos
                    usuario = usuarioBD; // Guardamos los datos de la BD en la sesion para futuro uso
                    httpServletRequest.getSession().setAttribute("sessionUsuario", usuario); //Ponemos los datos de entrada en el servlet (sessionUsuario)
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso Correcto", null);
                    faceContext.addMessage(null, message);
                    return "index";
                }else{ //Contrasena incorrecta
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contrasena introducida es incorrecta.", null);
                    faceContext.addMessage(null, message);
                    return "index";
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { //El usuario no ha sido registrado
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"El correo: "+ usuario.getCorreo()+" no existe en la base de datos.", null);
            faceContext.addMessage(null, message);
            return "index";
        }
        return "index";
    }
    
    public String cerrarSesion() {
	FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); // Asi lo tengo yo en mi practica
        httpServletRequest.getSession().removeAttribute("sessionUsuario");
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Session cerrada correctamente", null);
        faceContext.addMessage(null, message);
        System.out.println("|-| Sesion cerrada correctamente");
	return "index";
    }
        
    public boolean verificarSesion() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUsuario") == null)
            return false;
        else
            return true;
    }
    
    public void prueba(){
        System.out.println("Prueba de acceso a metodos del Bean");
    }
        
}