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
import logic.UsuarioC;
import model.Usuario;

/**
 * Bean de sesion utilizado para el manejo de la sesion de usuario activa dentro
 * del sitio
 *
 * @author Kikinzco
 */
@ManagedBean
@RequestScoped
public class Sesion {

    private Usuario usuario;    //Representa al usuario actual
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    private SesionC helper;

    /**
     * Constructor por omision
     */
    public Sesion() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if (usuario == null) {
            usuario = new Usuario();
        }
        helper = new SesionC();
    }

    /**
     * Metodo utilizado para iniciar sesion con el usuario obtenido desde la
     * vista
     *
     * @return String que representa mantenerse en la pagina principal de
     * acuerdo a lo ocurrido
     */
    public String login() {
        Usuario usuarioBD = helper.autentificar(usuario);
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
                    if (usuario.getHabilitado()) { //El usuario se encuentra habilitado y puede iniciar sesion
                        httpServletRequest.getSession().setAttribute("sessionUsuario", usuario); //Ponemos los datos de entrada en el servlet (sessionUsuario)
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso Correcto", null);
                        faceContext.addMessage(null, message);
                    } else { //En otro caso, se dio de baja anteriormente, por lo que no puede iniciar sesion
                        message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tu perfil se encuentra inhabilitado, contacta a un administrador para darte de alta", null);
                        faceContext.addMessage(null, message);
                    }
                } else { //Contrasena incorrecta
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contrasena introducida es incorrecta.", null);
                    faceContext.addMessage(null, message);
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { //El usuario no ha sido registrado
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo: " + usuario.getCorreo() + " no existe en la base de datos.", null);
            faceContext.addMessage(null, message);
        }
        return "index";
    }

    /**
     * Metodo que cierra la sesion del usuario, cambiando el contexto del
     * sistema Y quitando la informacion del usuario que se encontraba con la
     * sesion iniciada
     *
     * @return Cadena que representa irse a la pagina de inicio del sistema
     */
    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); // Asi lo tengo yo en mi practica
        httpServletRequest.getSession().removeAttribute("sessionUsuario");
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Session cerrada correctamente", null);
        faceContext.addMessage(null, message);
        System.out.println("|-| Sesion cerrada correctamente");
        return "index";
    }

    /**
     * Metodo que verifica si existe un usuario actualmente con sesion iniciada
     * dentro del sistema
     *
     * @return Booleano que indica si hay un usuario con sesion iniciada
     * actualmente
     */
    public boolean verificarSesion() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUsuario") != null;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
