package io.digitalstate.taxii.exception;

import org.springframework.http.HttpStatus;

public class CannotParseBundleStringException extends TaxiiException {

    private static String DEFAULT_TITLE = "Cannot parse bundle.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.BAD_REQUEST.value());

    public CannotParseBundleStringException(Throwable cause) {
        super(cause, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public CannotParseBundleStringException(String description) {
        super(null, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
