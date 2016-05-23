/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import logic.ConsultarC;
import model.Publicacion;
import model.Usuario;

/**
 * Bean que maneja las consultas, generalmente con metodos relacionados a representar los elementos
 * en la base de datos de una tabla en particular
 * @author Alan
 */
@ManagedBean
@SessionScoped
public class ConsultarBean {

    private String clave;
    private ConsultarC termino;
    private ArrayList<Publicacion> resultados;
    private ArrayList<Usuario> resultadosUsuarios;
    private ArrayList<Publicacion> resultadosPublicaciones;
    private Usuario usuario;
    private HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.  

    public ConsultarBean() {
        termino = new ConsultarC();
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
     * @return
     */
    public ArrayList<Usuario> buscarUsuarios() {
        this.resultadosUsuarios = (ArrayList<Usuario>) termino.buscarUsuarios();
        return resultadosUsuarios;
    }

    /**
     * Metodo que busca el listado de publicaciones actuales para dar la opcion
     * de eliminarlas
     *
     * @return
     */
    public ArrayList<Publicacion> buscarPublicaciones() {
        this.resultadosPublicaciones = (ArrayList<Publicacion>) termino.buscarPublicaciones();
        return resultadosPublicaciones;
    }

    /**
     * Metodo que muestra el listado de publicaciones del usuario actual en la
     * sesion
     *
     * @return
     */
    public ArrayList<Publicacion> buscarPublicacionesUsuario() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if (usuario == null) {
            usuario = new Usuario();
        }
        this.resultadosPublicaciones = (ArrayList<Publicacion>) termino.buscarPublicacionesUsuario(usuario);
        return resultadosPublicaciones;
    }

    /**
     * Metodo que realiza la logica de pedir prestada una publicacion
     * De acuerdo a la publicacion seleccionada, la actualiza en la base de datos para que el usuario actual se postule
     * como candidato de dicha publicacion (Esto solo ocurre si no hay un candidato ya esperando respuesta)
     * @param publicacionSolicitada La publicacion solicitada para pedir prestado el objeto
     * @return 
     */
    public String pedir(Publicacion publicacionSolicitada) {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tu peticion de prestamo fue recibida correctamente", null);
        faceContext.addMessage(null, message);
        System.out.println("|-| PEDISTE UNA PUBLICACION, espero sea la ID: "+publicacionSolicitada.getIdpublicacion());
        //helper.prestarPublicacion(publi, usuario); 2NDA Iteracion, falta mucho
        return "ConsultarIH";
    }
    
    public String getClave(){
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
