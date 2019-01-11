package io.digitalstate.taxii.exception;

import org.springframework.http.HttpStatus;

public class CannotParseStatusUpdateParamsException extends TaxiiException {

    private static String DEFAULT_TITLE = "Cannot parse Status update parameters.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value());

    public CannotParseStatusUpdateParamsException(Throwable cause) {
        super(cause, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public CannotParseStatusUpdateParamsException(Throwable cause, String description) {
        super(null, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
