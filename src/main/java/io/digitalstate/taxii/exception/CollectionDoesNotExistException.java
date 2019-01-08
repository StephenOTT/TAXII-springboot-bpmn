package io.digitalstate.taxii.exception;

import org.springframework.http.HttpStatus;

public class CollectionDoesNotExistException extends TaxiiException {

    private static String DEFAULT_TITLE = "Collection cannot be found or cannot be accessed.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.BAD_REQUEST.value());

    public CollectionDoesNotExistException(String collectionId) {
        super(DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public CollectionDoesNotExistException(String collectionId, String description) {
        super(DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
