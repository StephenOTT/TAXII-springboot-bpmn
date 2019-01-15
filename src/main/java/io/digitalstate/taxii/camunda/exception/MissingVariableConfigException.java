package io.digitalstate.taxii.camunda.exception;

import io.digitalstate.taxii.exception.TaxiiException;
import org.springframework.http.HttpStatus;

public class MissingVariableConfigException extends TaxiiException {

    private static String DEFAULT_TITLE = "A expected variable was missing or returned null.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value());

    public MissingVariableConfigException(Throwable cause) {
        super(cause, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public MissingVariableConfigException(Throwable cause, String description) {
        super(cause, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
