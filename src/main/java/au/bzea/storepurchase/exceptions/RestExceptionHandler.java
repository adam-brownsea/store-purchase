package au.bzea.storepurchase.exceptions;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static Logger logger = Logger.getLogger(RestExceptionHandler.class.getName());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception){
        logger.warning(exception.getMessage());

        RestError restError = new RestError();
        restError.setTimestamp(LocalDateTime.now());
        restError.setMessage("Bad request data");
        restError.setStatus(HttpStatus.BAD_REQUEST);
        return ResponseEntityBuilder.build(null);
    }
}
