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
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN
    private ArrayList<Publicacion> pubPrestadas;
    private ArrayList<Usuario> usuariosPrestado;
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN
    
    



    public ConsultarBean() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        pubPrestadas = new ArrayList<Publicacion>();
    }

    /**
     * Método que busca en la base de datos todas la publicaciones que coinciden
     * con el termino de búsqueda del atributo clave.
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
    
    
    
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN
    
    public String resultadosCalificar(){
        publicacionesPrestadas();
        usuariosPrestado();
        return "CalificarIH.xhtml";
    }
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN
    
    
    public void publicacionesPrestadas() {
        termino = new ConsultarC();
        this.pubPrestadas = (ArrayList<Publicacion>) termino.buscarPublicacionesPrestadas();
        
        
    
    }
    
    
    
    
    /**
     * Metodo calculador, recupera los  usuarios a los que les ha prestado algún objeto es usuario actual. 
     * @return El nombre de la vista donde se mostrarán los resultados.
     */
    public void usuariosPrestado() {
        termino = new ConsultarC();
        this.usuariosPrestado = (ArrayList<Usuario>) termino.buscarUsuariosPrestado();
       
    }
    
  
    
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN


    
    
    
    
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
    
    
    
    
   //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN 
    public ArrayList<Publicacion> getPubPrestadas() {
        buscarPublicaciones();
        return this.pubPrestadas;
    }
    public ArrayList<Usuario> getUsuariosPrestado() {
        buscarPublicaciones();
        return this.usuariosPrestado;
    }
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN
}
