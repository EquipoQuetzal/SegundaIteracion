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
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author oem
 */
@ManagedBean
@RequestScoped
public class UsuarioBean {

    private Usuario usuario = new Usuario();    //Representa al usuario actual
    private Usuario usuarioActual;
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.
    private UsuarioC helper;

    public UsuarioBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        helper = new UsuarioC();
    }

    public String registrar() {
        System.out.println("Intentando insertar al usuario: " + usuario.getNombre() + ", " + usuario.getCorreo() + ", " + usuario.getContrasena());
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
            //asignar a sessionUsuario (pork iniciara sesion automaticamente el usuario k se registro) O no....
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro finalizado correctamente", null);
            faceContext.addMessage(null, message);
        } catch (NoSuchAlgorithmException ex) { //Excepcion de hasheo
            System.out.println("|-| Algo raro paso con el algoritmo de cifrado");
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return "RegistroIH";
        } catch (org.hibernate.exception.ConstraintViolationException ex) {
            helper.getSession().getTransaction().rollback();
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Correo Invalido o ya existente ", null);
            faceContext.addMessage(null, message);
            return "RegistroIH";
        } catch (Exception e) { //Excepcion general (Acotar excepciones especificas, para saber si correo repetido o demas)
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
            faceContext.addMessage(null, message);
            return "RegistroIH";
        }
        return "index"; //Se registro correctamente el usuario
    }

    public String editarDatos() {
        System.out.println("|-| Datos a modificar: " + usuario.getNombre() + ", " + usuario.getContrasena());
        usuarioActual = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if(!usuario.getNombre().equals("")) // Solo se actualizara el nombre, si se escribio algo nuevo
            usuarioActual.setNombre(usuario.getNombre());

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(usuario.getContrasena().getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            usuarioActual.setContrasena(sb.toString());

            helper.actualizarUsuarioBD(usuarioActual);
            httpServletRequest.getSession().setAttribute("sessionUsuario", usuarioActual);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Edicion de perfil finalizada correctamente", null);

        } catch (NoSuchAlgorithmException ex) { //Excepcion de hasheo
            System.out.println("|-| Algo raro paso con el algoritmo de cifrado");
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return "EditarPerfilIH";
        } catch (Exception e) { //Excepcion general (Acotar excepciones especificas, para saber si correo repetido o demas)
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
            faceContext.addMessage(null, message);
            return "RegistroIH";
        }
        return "index";
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean verificarAdmin() {
        return usuario.getEsadmin();
    }
}
