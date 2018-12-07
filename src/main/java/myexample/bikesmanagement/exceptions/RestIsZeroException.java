package myexample.bikesmanagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class RestIsZeroException extends Exception {
    public RestIsZeroException(String message){
        super(message);
    }
}
