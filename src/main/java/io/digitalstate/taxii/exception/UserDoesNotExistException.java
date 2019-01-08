package io.digitalstate.taxii.exception;

import org.springframework.http.HttpStatus;

public class UserDoesNotExistException extends TaxiiException {

    private static String DEFAULT_TITLE = "User cannot be found or cannot be accessed.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.BAD_REQUEST.value());

    public UserDoesNotExistException(String userId) {
        super(DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public UserDoesNotExistException(String userId, String description) {
        super(DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
