package io.digitalstate.taxii.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    private HttpHeaders errorHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/vnd.oasis.taxii+json");
        headers.add("verison", "2.0");

        return headers;
    }

    @ExceptionHandler(TenantDoesNotExistException.class)
    public final ResponseEntity<String> processTenantDoesNotExistException(TenantDoesNotExistException ex) {
        return ResponseEntity.badRequest()
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public final ResponseEntity<String> processUserDoesNotExistException(UserDoesNotExistException ex) {
        return ResponseEntity.badRequest()
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(DiscoveryDoesNotExistException.class)
    public final ResponseEntity<String> processDiscoveryDoesNotExistException(DiscoveryDoesNotExistException ex) {
        return ResponseEntity.badRequest()
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(CollectionDoesNotExistException.class)
    public final ResponseEntity<String> processCollectionDoesNotExistException(CollectionDoesNotExistException ex) {
        return ResponseEntity.badRequest()
                .headers(errorHeaders())
                .body(ex.toJson());
    }
}
