package io.digitalstate.taxii.mongo.exception;

import io.digitalstate.taxii.exception.TaxiiException;
import org.springframework.http.HttpStatus;

public class CannotUpdateStatusException extends TaxiiException {

    private static String DEFAULT_TITLE = "Status cannot be updated.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.NOT_FOUND.value());

    public CannotUpdateStatusException(String statusId) {
        super(null, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public CannotUpdateStatusException(String statusId, String description) {
        super(null, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
