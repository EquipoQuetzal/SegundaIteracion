/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Date;

import model.Publicacion;
import model.Usuario;

/**
 * Controlador que realiza operaciones con la base de datos con cuestiones relacionadas a publicaciones
 * @author jorge
 */
public class PublicacionC {

    private Session session;

    /**
     * Constructor por omision
     */
    public PublicacionC() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }
    
    /**
     * Metodo que registra una nueva publicacion en la base de datos
     * @param publicacion Publicacion a insertar en la base de datos
     * @param usu Usuario asociado a la publicacion que sera insertada
     */
    public void registrarBD(Publicacion publicacion, Usuario usu) {
        Transaction tx = session.beginTransaction();
        java.util.Date fecha = new Date();
        publicacion.setUsuarioByIdusuario(usu);
        publicacion.setFechapublicacion(fecha);
        publicacion.setCalificacion(0);
        session.save(publicacion);
        session.getTransaction().commit();
    }
    
    /**
     * Metodo que busca una publicacion en especifico dado su ID
     * @param id ID de la publicacion a buscar
     * @return Publicacion obtenida con el ID solicitado
     */
    public Publicacion buscarPublicacion(Integer id) {
        Publicacion resultado;
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.getNamedQuery("BuscarPublicacion").setInteger("id", id);
            resultado = (Publicacion) q.uniqueResult();
            session.close();
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metodo que elimina una publicacion de la base de datos
     * @param publicacion Publicacion a eliminar
     */
    public void borrarPublicacionBD(Publicacion publicacion) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.delete(publicacion);
            session.getTransaction().commit();
        } catch (Exception e) { // Esto nunca deberia pasar porque ya sacamos anteriormente la publiccion de la base de datos
            e.printStackTrace();
        }
    }
    
    /**
     * Metodo que actualiza la informacion de la publicacion en la base de datos
     * @param publicacion Publicacion a modificar ne la base de datos
     */
    public void actualizarPublicacionBD(Publicacion publicacion) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(publicacion);
        session.getTransaction().commit();
        session.close();
    }

}
