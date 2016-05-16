/*
    Clase diseniada como controlador pararealizar busquedas.

 */
package logic;

import beans.PublicacionBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import model.Publicacion;
//ALAN ALAN ALAN ALAN ALAN 
import beans.Sesion;
//ALAN ALAN ALAN ALAN ALAN
import java.util.*;
import model.Usuario;

/**
 *
 * @author Alan
 */
public class ConsultarC {

    private ArrayList<Publicacion> resultados;
    private ArrayList<Usuario> resultadosUsuario;
    private ArrayList<Publicacion> resultadosPublicaciones;
    private Session session;

    public ConsultarC() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    /**
     * Método que obtiene las palabras de una cadena y crea unaexpresión regular
     * para buscar coincidencias con todas las palabras.
     *
     * @param cadena Cadena a separar por palabras.
     * @return La consulta p
     */
    public static String obtenerPalabras(String cadena) {
        if (cadena.length() <= 0) {
            return cadena;
        }
        cadena = cadena.toLowerCase(); //Transforma la cadena a minúsculas.
        String[] palabras = cadena.split(" ");
        String resultado = ".*(";
        for (String p : palabras) {
            resultado += p + "|";
        }
        if (resultado.length() >= 5) {
            resultado = resultado.substring(0, resultado.length() - 1);
        }
        resultado += ").*";
        return resultado;
    }

    public List<Publicacion> buscar(String clave) {
        clave = obtenerPalabras(clave);
        session= HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createSQLQuery("select * from publicacion where "
                    + "LOWER(publicacion.descripcion) ~ :clave ; ").addEntity(Publicacion.class).setString("clave", clave);
            resultados = (ArrayList<Publicacion>) q.list();
            //session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            //session.getTransaction().rollback();
        }
        return resultados;
    }
    
    
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN
    /**
     * Método para buscar aquellas publicaciones que han sido prestadas al usuario actiual.
     * @return Lista de publicaciones prestadas al usuario actual.
     */
    public List<Publicacion> buscarPublicacionesPrestadas() {
        //clave = obtenerPalabras(clave);
        Sesion usuarioActual = new Sesion();

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        int id;
        PublicacionBean  publicacion= new PublicacionBean();
        Usuario usuario = publicacion.getUsuario();
        id = usuario.getIdusuario();
        //id = usuarioActual.getUsuario().getIdusuario();
        ArrayList<Publicacion> pubPrestadas = new ArrayList<>();
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createSQLQuery("select * from publicacion where publicacion.idprestatario = :id").addEntity(Publicacion.class).setInteger("id", id);
            pubPrestadas = (ArrayList<Publicacion>) q.list();
            //session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            //session.getTransaction().rollback();
        }
        return pubPrestadas;
    }
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN
    /**
     * 
     * @return 
    */
    public List<Usuario> buscarUsuariosPrestado() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        ArrayList<Usuario> usuariosPrestado = new ArrayList<>();
        int id;
        PublicacionBean  publicacion= new PublicacionBean();
        Usuario usuario = publicacion.getUsuario();
        id = usuario.getIdusuario();
       
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createSQLQuery("select u.* from"
                    + " usuario as u   INNER JOIN publicacion as p  ON u.idusuario = p.idprestatario where p.idusuario = :id").addEntity(Usuario.class).setInteger("id", id);;
            usuariosPrestado = (ArrayList<Usuario>) q.list();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuariosPrestado;
    }
    //ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN ALAN
    
    
 
    public List<Usuario> buscarUsuarios() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createSQLQuery("select * from usuario").addEntity(Usuario.class);
            resultadosUsuario = (ArrayList<Usuario>) q.list();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultadosUsuario;
    }
    
    public List<Publicacion> buscarPublicaciones() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createSQLQuery("select * from publicacion").addEntity(Publicacion.class);
            resultadosPublicaciones = (ArrayList<Publicacion>) q.list();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultadosPublicaciones;
    }

    public ArrayList<Publicacion> getResultados() {
        return this.resultados;
    }

    public void setResultados(ArrayList<Publicacion> r) {
        this.resultados = r;
    }

}
