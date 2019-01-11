package io.digitalstate.taxii.camunda.exception;

import io.digitalstate.taxii.exception.TaxiiException;
import org.springframework.http.HttpStatus;

public class CannotStartProcessInstanceException extends TaxiiException {

    private static String DEFAULT_TITLE = "Cannot Start Process Instance.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value());

    public CannotStartProcessInstanceException(Throwable cause) {
        super(cause, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public CannotStartProcessInstanceException(Throwable cause, String description) {
        super(cause, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
