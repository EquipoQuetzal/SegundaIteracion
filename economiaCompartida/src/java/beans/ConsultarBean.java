/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import correo.Correo;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import logic.ConsultarC;
import logic.PublicacionC;
import model.Publicacion;
import model.Usuario;
import org.apache.commons.mail.EmailException;

/**
 * Bean que maneja las consultas, generalmente con metodos relacionados a
 * representar los elementos en la base de datos de una tabla en particular
 *
 * @author Alan
 */
@ManagedBean
@SessionScoped
public class ConsultarBean {

    private String clave;
    private ConsultarC termino;
    private PublicacionC helper;
    private ArrayList<Publicacion> resultados;
    private ArrayList<Usuario> resultadosUsuarios;
    private ArrayList<Publicacion> resultadosPublicaciones;
    private Usuario usuario;
    private HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.  

    public ConsultarBean() {
        termino = new ConsultarC();
        helper = new PublicacionC();
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if (usuario == null) {
            usuario = new Usuario();
        }
    }

    /**
     * Método que busca en la base de datos todas la publicaciones que coinciden
     * con el termino de búsqueda del tributo clave.
     *
     * @return Una cadena que indica la vista donde se mostrarán los resultados.
     */
    public String buscar() {
        this.resultados = new ArrayList<>();
        if (this.clave.length() <= 0) {
            return "ConsultarIH";
        }
        this.resultados = (ArrayList<Publicacion>) termino.buscar(clave);
        clave = ""; //Para resetear el campo de busqueda
        return "ConsultarIH";
    }

    /**
     * Metodo que busca el listado de usuarios actuales para dar la opcion de
     * eliminarlos
     *
     * @return Liata con todos los usuarios en el sistema
     */
    public ArrayList<Usuario> buscarUsuarios() {
        this.resultadosUsuarios = (ArrayList<Usuario>) termino.buscarUsuarios();
        return resultadosUsuarios;
    }

    /**
     * Metodo que busca el listado de publicaciones actuales para dar la opcion
     * de eliminarlas
     *
     * @return Lista con todas las publicaciones del sistema
     */
    public ArrayList<Publicacion> buscarPublicaciones() {
        this.resultadosPublicaciones = (ArrayList<Publicacion>) termino.buscarPublicaciones();
        return resultadosPublicaciones;
    }

    /**
     * Metodo que muestra el listado de publicaciones del usuario actual en la
     * sesion
     *
     * @return Lista con las publicaciones creadas por el usuario actual
     */
    public ArrayList<Publicacion> buscarPublicacionesUsuario() {
        update();
        this.resultadosPublicaciones = (ArrayList<Publicacion>) termino.buscarPublicacionesUsuario(usuario);
        return resultadosPublicaciones;
    }

    /**
     * Metodo que realiza la logica de pedir prestada una publicacion De acuerdo
     * a la publicacion seleccionada, la actualiza en la base de datos para que
     * el usuario actual se postule como candidato de dicha publicacion (Esto
     * solo ocurre si no hay un candidato ya esperando respuesta)
     *
     * @param publicacionSolicitada La publicacion solicitada para pedir
     * prestado el objeto
     * @return Cadena que representa la vista a la cual redireccionar en la
     * aplicacion
     */
    public String pedir(Publicacion publicacionSolicitada) {
        update();
        try {
            publicacionSolicitada.setUsuarioByIdprestatario(usuario);
            helper.actualizarPublicacionBD(publicacionSolicitada);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tu peticion de prestamo fue recibida correctamente", null);
            faceContext.addMessage(null, message);
        } catch (Exception e) { //Excepcion general (Acotar excepciones especificas, para saber si correo repetido o demas)
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
            faceContext.addMessage(null, message);
        }
        return "ConsultarIH";
    }

    /**
     * Metodo que permite rechazar una solicitud de prestamo asociada a una
     * publicacion
     * (Lo que hace es regresar el id de usuario de la publicacion a
     * null) Y la publicacion queda en estado Disponible de nuevo
     * Asi como tambien envia un correo al prestatario, indicando que su solicitud fue rechazada
     * @param publicacion Publicacion a rechazar
     * @return Cadena que indica la pagina a la que se redireccionara despues de rechazar una publicacion
     */
    public String rechazar(Publicacion publicacion) {
        update();
        Usuario prestatario = publicacion.getUsuarioByIdprestatario();
        System.out.println("|-| El usuario actual rechazo la solicitud de prestamo de: " + prestatario);
        
        Correo email = new Correo();
        String asunto = "Solicitud de prestamo: Rechazada";
        String mensaje = "Que tal, "+prestatario.getNombre()+"!\n\n"
                + "El usuario "+usuario.getNombre()+" ha rechazado tu solicitud de prestamo.\n"
                + "Esto posiblemente se deba a una calificacion poco favorable de tu perfil.\n\n"
                + "Si crees que esto se trata de un error, ponte en contacto con "+usuario.getNombre()+ " ("+usuario.getCorreo()+")";
        String destinatario = prestatario.getCorreo();
        try{            
            email.enviarCorreo(asunto, mensaje, destinatario);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "La notificacion a "+prestatario.getCorreo()+" fue enviada exitosamente", null);
            faceContext.addMessage(null, message);
        } catch (EmailException ex) {
            Logger.getLogger(ConsultarBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo a "+prestatario.getCorreo()+" no fue enviado, por falta de internet", null);
            faceContext.addMessage(null, message);
        }
        
        try {
            publicacion.setUsuarioByIdprestatario(null);
            helper.actualizarPublicacionBD(publicacion);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "La solicitud de "+prestatario.getNombre()+" fue rechazada exitosamente en el sistema", null);
            faceContext.addMessage(null, message);
        } catch (Exception e) { //Excepcion general (Solo deberia pasar por problemas con la base de datos)
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio la excepcion: " + e, null);
            faceContext.addMessage(null, message);
        }
        
        return "PublicarOfertaIH";
    }

    /**
     * Regresa un booleano que indica si la publicacion del parametro es ajena
     * al usuario actual
     *
     * @param publicacion Publicacion de la cual comparar si es ajena o no
     * (creada por alguien mas)
     * @return Valor booleano verdadero si la publicacion es ajena, falso en
     * otro caso
     */
    public boolean esAjena(Publicacion publicacion) {
        update();
        Usuario usuarioPublicacion = publicacion.getUsuarioByIdusuario();
        return usuario.getIdusuario() != usuarioPublicacion.getIdusuario();
    }

    /**
     * Metodo que indica si una publicacion esta disponible para poder pedirse
     * prestada
     *
     * @param publicacion Publicacion a analizar
     * @return Valor booleano verdadero si la publicacion esta disponible para
     * ser solicitada
     */
    public boolean estaDisponible(Publicacion publicacion) {
        boolean noSolicitada = publicacion.getUsuarioByIdprestatario() == null;
        return noSolicitada && publicacion.getDisponible();
    }

    /**
     * Metodo que indica si una publicacion ha sido solicitada por un prestatario
     * 
     * @param publicacion Publicacion a analizar
     * @return Valor booleano verdadero si la publicacion ha sido solicitada por un usuario (prestatario)
     */
    public boolean estaSolicitada(Publicacion publicacion) {
        boolean solicitada = publicacion.getUsuarioByIdprestatario() != null;
        return solicitada && publicacion.getDisponible();
    }

    /**
     * Metodo que actualiza el contexto actual y el httpservlet Asi como la
     * variable que contiene al usuario de la sesion actual
     */
    public void update() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if (usuario == null) {
            usuario = new Usuario();
        }
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String c) {
        this.clave = c;
    }

    public ConsultarC getTermino() {
        return termino;
    }

    public ArrayList<Publicacion> getResultados() {
        return this.resultados;
    }

}
