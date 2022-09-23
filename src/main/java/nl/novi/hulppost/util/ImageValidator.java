package nl.novi.hulppost.util;

import nl.novi.hulppost.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Base64;

public class ImageValidator implements ConstraintValidator<ValidImage, String> {

    @Autowired
    FileService attachmentService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }

        byte[] decodedBytes = Base64.getDecoder().decode(value);
        String fileType = attachmentService.detectType(decodedBytes);
        if(fileType.equalsIgnoreCase("image/png") || fileType.equalsIgnoreCase("image/jpeg")) {
            return true;
        }

        return false;
    }
}
