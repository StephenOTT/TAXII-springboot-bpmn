package io.digitalstate.taxii.exception;

import io.digitalstate.taxii.mongo.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {

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

    @ExceptionHandler(CollectionObjectDoesNotExistException.class)
    public final ResponseEntity<String> processCollectionObjectDoesNotExistException(CollectionObjectDoesNotExistException ex) {
        return ResponseEntity.badRequest()
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(CollectionObjectAlreadyExistsException.class)
    public final ResponseEntity<String> processCollectionObjectAlreadyExistsException(CollectionObjectAlreadyExistsException ex) {
        return ResponseEntity.badRequest()
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(CannotParseBundleStringException.class)
    public final ResponseEntity<String> processCannotParseBundleStringException(CannotParseBundleStringException ex) {
        return ResponseEntity.badRequest()
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(CannotCreateStatusDocumentException.class)
    public final ResponseEntity<String> processCannotCreateStatusDocumentException(CannotCreateStatusDocumentException ex) {
        return ResponseEntity.badRequest()
                .headers(errorHeaders())
                .body(ex.toJson());
    }
}
