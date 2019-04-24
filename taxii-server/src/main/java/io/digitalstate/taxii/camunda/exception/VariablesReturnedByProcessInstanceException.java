package io.digitalstate.taxii.camunda.exception;

import io.digitalstate.taxii.exception.TaxiiException;
import org.springframework.http.HttpStatus;

public class VariablesReturnedByProcessInstanceException extends TaxiiException {

    private static String DEFAULT_TITLE = "Variables returned by process instance were incorrect.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value());

    public VariablesReturnedByProcessInstanceException(Throwable cause) {
        super(cause, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public VariablesReturnedByProcessInstanceException(Throwable cause, String description) {
        super(cause, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
