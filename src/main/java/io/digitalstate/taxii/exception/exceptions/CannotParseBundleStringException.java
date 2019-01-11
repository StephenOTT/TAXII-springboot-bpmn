package io.digitalstate.taxii.exception.exceptions;

import io.digitalstate.taxii.exception.TaxiiException;
import org.springframework.http.HttpStatus;

public class CannotParseBundleStringException extends TaxiiException {

    private static String DEFAULT_TITLE = "Cannot parse bundle.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value());

    public CannotParseBundleStringException(Throwable cause) {
        super(cause, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public CannotParseBundleStringException(Throwable cause, String description) {
        super(null, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
