/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import model.Usuario;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Controlador que maneja con la base de datos metodos sobre usuarios
 * @author oem
 */
public class UsuarioC {

    private Session session;

    /**
     * Constructor por omision
     */
    public UsuarioC() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    /**
     * Metodo que registra un nuevo usuario en la base de datos
     * (Las validaciones fueron hechas anteriormente en la vista y en el bean)
     * @param usuario Usuario nuevo a registrar en la base de datos
     */
    public void registrarBD(Usuario usuario) {
        Transaction tx = session.beginTransaction();
        session.save(usuario);
        tx.commit();
    }

    /**
     * Metodo que busca a un usuario en la base de datos dado un correo
     * @param correo Correo del usuario a buscar
     * @return Usuario cuyo correo es el mismo del parametro
     */
    public Usuario buscarPorCorreo(String correo) {
        Usuario resultado;
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.getNamedQuery("BuscarPorCorreo").setString("correo", correo);
            resultado = (Usuario) q.uniqueResult();
            session.close();
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metodo que borra a un usuario de la base de datos
     * @param usuario Usuario a borrar de la base de datos
     */
    public void borrarUsuarioBD(Usuario usuario) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(usuario);
        session.getTransaction().commit();
        //session.close? Aun no sabemos si va o no
    }

    /**
     * Metodo que actualiza la informacion de un usuario en la base de datos
     * @param usuario Usuario a modificar en la base de datos
     */
    public void actualizarUsuarioBD(Usuario usuario) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(usuario);
        session.getTransaction().commit();
        session.close();
    }
    
    public void habilitarUsuarioBD(Usuario usuario) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(usuario);
        session.getTransaction().commit();
        session.close();
    }
    
    public void deshabilitarUsuarioBD(Usuario usuario) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(usuario);
        session.getTransaction().commit();
        session.close();
    }
    
    public Session getSession() {
        return session;
    }

}
