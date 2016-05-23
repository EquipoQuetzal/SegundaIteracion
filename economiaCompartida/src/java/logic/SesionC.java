/*
    Clase diseniada como controlador para las cuestiones de inicio de sesion
 */
package logic;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Usuario;

/**
 * Controlador que maneja la conexion con la base de datos, sobre cuestiones de la sesion activa
 * @author Kikinzco
 */
public class SesionC {

    private Session session;

    /**
     * Constructor por omision
     */
    public SesionC() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    /**
     * Metodo que inicia la sesion en el sistema con el usuario dado
     * Para esto 
     * @param usuario
     * @return 
     */
    public Usuario autentificar(Usuario usuario) {
        Usuario resultado;
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.getNamedQuery("BuscarPorCorreo").setString("correo", usuario.getCorreo());
            resultado = (Usuario) q.uniqueResult(); //Si regresa null, significa que el usuario no esta registrado
            session.close();
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
