package au.bzea.storepurchase.exceptions;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class RestError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
     private LocalDateTime timestamp;
     private HttpStatus status;
     private String message;
     private List<String> errors;    

     public RestError(LocalDateTime timestamp, 
        HttpStatus status,
        String message, 
        List<String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
     }
     public RestError(
        HttpStatus status,
        String message, 
        List<String> errors) {
        this.setTimestamp(LocalDateTime.now());
        this.status = status;
        this.message = message;
        this.errors = errors;
     }
     public RestError(
        HttpStatus status,
        String message) {
        this.setTimestamp(LocalDateTime.now());
        this.status = status;
        this.message = message;
     }
}
