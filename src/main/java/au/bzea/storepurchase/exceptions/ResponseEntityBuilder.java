package au.bzea.storepurchase.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {
    
    public static ResponseEntity<Object> build(RestError restError) {
        return new ResponseEntity<>(restError, restError.getStatus() == null? HttpStatus.INTERNAL_SERVER_ERROR : restError.getStatus());
    }
}
