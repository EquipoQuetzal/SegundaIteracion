/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import logic.ConsultarC;
import model.Publicacion;
import model.Usuario;

/**
 *
 * @author Alan
 */
@ManagedBean
@RequestScoped
public class ConsultarBean {

    private String clave;
    private ConsultarC termino;
    private ArrayList<Publicacion> resultados;
    private ArrayList<Usuario> resultadosUsuarios;
    private ArrayList<Publicacion> resultadosPublicaciones;
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message; // Permite el envio de mensajes entre el bean y la vista.  

    public ConsultarBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
    }

    /**
     * Método que busca en la base de datos todas la publicaciones que coinciden
     * con el termino de búsqueda del tributo clave.
     *
     * @return Una cadena que indica la vista donde se mostrarán los resultados.
     */
    public String buscar() {
        termino = new ConsultarC();
        this.resultados = new ArrayList<>();
        if (this.clave.length() <= 0) {
            return "ConsultarIH";
        }
        this.resultados = (ArrayList<Publicacion>) termino.buscar(clave);
        return "ConsultarIH";
    }

    public String buscarUsuarios() {
        termino = new ConsultarC();
        this.resultadosUsuarios = new ArrayList<>();
        this.resultadosUsuarios = (ArrayList<Usuario>) termino.buscarUsuarios();
        return "BorrarUsuarioIH.xhtml";
    }
    
    public String buscarPublicaciones() {
        termino = new ConsultarC();
        this.resultadosPublicaciones = new ArrayList<>();
        this.resultadosPublicaciones = (ArrayList<Publicacion>) termino.buscarPublicaciones();
        return "BorrarPublicacionIH.xhtml";
    }

    public ConsultarC getTermino() {
        return termino;
    }

    public void setTermino(ConsultarC t) {
        this.termino = t;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String c) {
        this.clave = c;
    }

    public ArrayList<Publicacion> getResultados() {
        return this.resultados;
    }

    public void setClave(ArrayList<Publicacion> r) {
        this.resultados = r;
    }

    public ArrayList<Usuario> getResultadosUsuarios() {
        buscarUsuarios();
        return this.resultadosUsuarios;
    }
    
    public ArrayList<Publicacion> getResultadosPublicaciones() {
        buscarPublicaciones();
        return this.resultadosPublicaciones;
    }

}
