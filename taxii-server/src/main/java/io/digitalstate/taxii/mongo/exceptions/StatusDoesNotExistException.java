package io.digitalstate.taxii.mongo.exceptions;

import io.digitalstate.taxii.exception.TaxiiException;
import org.springframework.http.HttpStatus;

public class StatusDoesNotExistException extends TaxiiException {

    private static String DEFAULT_TITLE = "Status cannot be found or cannot be accessed.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.NOT_FOUND.value());

    public StatusDoesNotExistException(String statusId) {
        super(null, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public StatusDoesNotExistException(String statusId, String description) {
        super(null, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
