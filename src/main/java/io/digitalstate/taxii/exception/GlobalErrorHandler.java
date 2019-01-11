package io.digitalstate.taxii.exception;

import io.digitalstate.taxii.camunda.exception.CannotStartProcessInstanceException;
import io.digitalstate.taxii.camunda.exception.VariablesReturnedByProcessInstanceException;
import io.digitalstate.taxii.exception.exceptions.CannotParseBundleStringException;
import io.digitalstate.taxii.exception.exceptions.CannotParseStatusUpdateParamsException;
import io.digitalstate.taxii.mongo.exceptions.*;
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
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public final ResponseEntity<String> processUserDoesNotExistException(UserDoesNotExistException ex) {
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(DiscoveryDoesNotExistException.class)
    public final ResponseEntity<String> processDiscoveryDoesNotExistException(DiscoveryDoesNotExistException ex) {
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(CollectionDoesNotExistException.class)
    public final ResponseEntity<String> processCollectionDoesNotExistException(CollectionDoesNotExistException ex) {
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(CollectionObjectDoesNotExistException.class)
    public final ResponseEntity<String> processCollectionObjectDoesNotExistException(CollectionObjectDoesNotExistException ex) {
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(CollectionObjectAlreadyExistsException.class)
    public final ResponseEntity<String> processCollectionObjectAlreadyExistsException(CollectionObjectAlreadyExistsException ex) {
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(CannotParseBundleStringException.class)
    public final ResponseEntity<String> processCannotParseBundleStringException(CannotParseBundleStringException ex) {
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(CannotCreateStatusDocumentException.class)
    public final ResponseEntity<String> processCannotCreateStatusDocumentException(CannotCreateStatusDocumentException ex) {
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(StatusDoesNotExistException.class)
    public final ResponseEntity<String> processStatusDoesNotExistException(StatusDoesNotExistException ex) {
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(CannotParseStatusUpdateParamsException.class)
    public final ResponseEntity<String> processCannotParseStatusUpdateParamsException(CannotParseStatusUpdateParamsException ex) {
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(CannotStartProcessInstanceException.class)
    public final ResponseEntity<String> processCannotStartProcessInstanceException(CannotStartProcessInstanceException ex) {
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }

    @ExceptionHandler(VariablesReturnedByProcessInstanceException.class)
    public final ResponseEntity<String> processVariablesReturnedByProcessInstanceException(VariablesReturnedByProcessInstanceException ex) {
        return ResponseEntity.status((int)Integer.valueOf(ex.getHttpStatus()))
                .headers(errorHeaders())
                .body(ex.toJson());
    }
}
