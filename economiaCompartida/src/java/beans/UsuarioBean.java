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

import logic.UsuarioC;
import model.Usuario;

/**
 * Clase utilizada para el manejo de usuarios en el sistema
 * @author oem
 */
@ManagedBean
@RequestScoped
public class UsuarioBean {

    private Usuario usuario; //Representa los datos obtenidos de la vista
    private Usuario usuarioSesion; //Representa al usuario actual
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    private UsuarioC helper;

    /**
     * Constructor por omision
     */
    public UsuarioBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = new Usuario();
        helper = new UsuarioC();
        usuarioSesion = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
    }

    /**
     * Metodo que registra un nuevo usuario en la base de datos
     * De acuerdo a los valores obtenidos en la vista, cifrando la contrasena e indicando cualquier error posible
     * @return 
     */
    public String registrar() {
        System.out.println("Intentando insertar al usuario: " + usuario.getNombre() + ", " + usuario.getCorreo());
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(usuario.getContrasena().getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            usuario.setContrasena(sb.toString());

            helper.registrarBD(usuario);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro finalizado correctamente", null);
            faceContext.addMessage(null, message);
        } catch (NoSuchAlgorithmException ex) { //Excepcion del algoritmo de cifrado
            System.out.println("|-| Algo raro paso con el algoritmo de cifrado");
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return "RegistroIH";
        } catch (org.hibernate.exception.ConstraintViolationException ex) { //Correo que no cuadra con la expresion regular, o ya existe en sistema
            helper.getSession().getTransaction().rollback(); // Posiblemente no (?)
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Correo Invalido o ya existente ", null);
            faceContext.addMessage(null, message);
            return "RegistroIH";
        } catch (Exception e) { //Excepcion general
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
            faceContext.addMessage(null, message);
            return "RegistroIH";
        }
        return "index"; //Se registro correctamente el usuario
    }

    /**
     * Metodo que modifica los datos del usuario actual de la sesion, en la base de datos
     * @return Cadena que representa el redireccionamiento en la pagina
     */
    public String editarDatos() {
        // Solo se actualizaran los datos si fue escrito algo nuevo
        if (!usuario.getNombre().equals(""))
            usuarioSesion.setNombre(usuario.getNombre());
        if(!usuario.getInformacion().equals(""))
            usuarioSesion.setInformacion(usuario.getInformacion());
        if(!usuario.getTelefono().equals(""))
            usuarioSesion.setTelefono(usuario.getTelefono());
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(usuario.getContrasena().getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            usuarioSesion.setContrasena(sb.toString());
            helper.actualizarUsuarioBD(usuarioSesion);
            httpServletRequest.getSession().setAttribute("sessionUsuario", usuarioSesion);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Edicion de perfil finalizada correctamente", null);
        } catch (NoSuchAlgorithmException ex) { //Excepcion del algoritmo de cifrado
            System.out.println("|-| Algo raro paso con el algoritmo de cifrado");
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return "EditarPerfilIH";
        } catch (Exception e) { //Excepcion general (Acotar excepciones especificas, para saber si correo repetido o demas)
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
            faceContext.addMessage(null, message);
            return "EditarPerfilIH";
        }
        return "index";
    }
    
    public String darDeBaja(){
        usuarioSesion.setHabilitado(false);
        try {
            helper.actualizarUsuarioBD(usuarioSesion);
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); // Asi lo tengo yo en mi practica
            httpServletRequest.getSession().removeAttribute("sessionUsuario");
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tu perfil ha sido inhabilitado, contacta un administrador para recuperarlo", null);
            faceContext.addMessage(null, message);
            System.out.println("|-| Usuario deshabilitado correctamente");
        }catch (Exception e) { //Excepcion general
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
            faceContext.addMessage(null, message);
            return "EditarPerfilIH";
        }
        return "index";
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Usuario getUsuarioSesion(){
        return usuarioSesion;
    }

}
