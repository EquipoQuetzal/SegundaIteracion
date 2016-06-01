/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;




/**
 *
 * @author oem
 */
@FacesValidator(value = "checkboxValidator")
public class Validacion implements Validator{
    
    @Override
    public void validate(FacesContext fc, UIComponent component, Object value)
    throws ValidatorException {
        if (value.equals(Boolean.FALSE)) {
        FacesMessage message = new FacesMessage();
        message.setSummary("Para continuar acepta los terminos de la politica de privacidad");
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        throw new ValidatorException(message);
        }
    }
}
