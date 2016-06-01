package correo;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Clase utilizada para enviar un correo electronico, utilizando una direccion
 * de gmail
 *
 * @author Kikinzco
 */
public class Correo {

    Email email;

    /**
     * Constructor por omision Configura el puerto y host del correo electronico
     * a enviar Asi como la direccion y contrasena del correo del remitente
     */
    public Correo() {
        email = new SimpleEmail();
        email.setSmtpPort(587);
        email.setHostName("smtp.gmail.com");
        email.setAuthentication("economiacompartida@gmail.com", "economiaCompartidaIngenieria");
        email.setStartTLSEnabled(true);
        try {
            email.setFrom("economiacompartida@gmail.com", "Sistema Quetzal");
        } catch (EmailException ex) { //Correo no existente (No deberia pasar, pues es fijo)
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo que envia un nuevo correo electronico, a partir de los parametros
     *
     * @param asunto Asunto que llevara el correo electronico
     * @param mensaje Mensaje (cuerpo) del correo electronico
     * @param destinatario Direccion electronica de la persona a la cual va
     * dirigida el correo
     * @throws EmailException Excepcion lanzada cuando no existe una conexion a
     * internet, por lo que el correo no es enviado
     */
    public void enviarCorreo(String asunto, String mensaje, String destinatario) throws EmailException {
        // Asunto
        email.setSubject(asunto);
        // Mensaje (Incluir informacion de facilitador (Que rechaza o acepta)
        email.setMsg(mensaje);
        // Destinatario
        email.addTo(destinatario); // Hacia prestatario            
        email.send();
        System.out.println("|-| Correo enviado correctamente a " + destinatario);
    }

}
