package au.bzea.storepurchase.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {
    
    public static ResponseEntity<Object> build(RestError restError) {
        restError.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(restError, restError.getStatus());
    }
}
