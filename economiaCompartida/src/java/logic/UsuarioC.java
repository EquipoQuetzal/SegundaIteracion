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
 *
 * @author oem
 */
public class UsuarioC {

    private Session session;

    public UsuarioC(){
    session = HibernateUtil.getSessionFactory().getCurrentSession();
    }
    
    public void registrarBD(Usuario usuario){        
            Transaction tx = session.beginTransaction();
            session.save(usuario);
            tx.commit();
    }
    
    public Session getSession(){
        return session;
    }
    
    public Usuario buscarPorCorreo(String correo){
        Usuario resultado;
        try{
            Transaction tx = session.beginTransaction();
            Query q = session.getNamedQuery("BuscarPorCorreo").setString("correo",correo);
            resultado = (Usuario) q.uniqueResult();
            //Si regresa null, significa que el usuario no esta registrado en la BD, no recuerdo donde afecta eso
            session.close();
            return resultado;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }    
    
    // Quizas regresar booleano que indique si se elimino correctamente    
    public void borrarUsuarioBD(Usuario usuario){
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.delete(usuario);
            session.getTransaction().commit();
    }
    
}
