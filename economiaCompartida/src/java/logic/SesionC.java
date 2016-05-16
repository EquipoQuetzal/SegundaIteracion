/*
    Clase diseniada como controlador para las cuestiones de inicio de sesion
*/
package logic;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Usuario;

/**
 *
 * @author Kikinzco
 */
public class SesionC {
    
    private Session session;
    
    public SesionC(){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }
    
    public Usuario autentificar(Usuario usuario){
        Usuario resultado;
        try{
            Transaction tx = session.beginTransaction();
            Query q = session.getNamedQuery("BuscarPorCorreo").setString("correo",usuario.getCorreo());
            // INCLUIR EN EL .SETSTRING TAMBN LA CONTRASEÑA DEL USUARIO PERO LUEGO VEMOS CON EL MD5, IGUAL Y SE HACE EN EL BEAN
            resultado = (Usuario) q.uniqueResult();
            //Si regresa null, significa que el usuario no esta registrado en la BD, no recuerdo donde afecta eso
            session.close();
            return resultado;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}