package nl.novi.hulppost.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private String fieldName;
    private long fieldValue;

    public ResourceNotFoundException(String resourceName) {
        super(String.format("%s", resourceName));
        this.resourceName = resourceName;
    }

//    public ResourceNotFoundException(String resourceName, String fieldName) {
//        super(String.format("%s niet gevonden met de %s", resourceName, fieldName));
//        this.resourceName = resourceName;
//        this.fieldName = fieldName;
//    }

    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s niet gevonden met de %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public long getFieldValue() {
        return fieldValue;
    }
}
