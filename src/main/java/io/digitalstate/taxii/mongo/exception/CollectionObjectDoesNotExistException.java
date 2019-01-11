package io.digitalstate.taxii.mongo.exception;

import io.digitalstate.taxii.exception.TaxiiException;
import org.springframework.http.HttpStatus;

public class CollectionObjectDoesNotExistException extends TaxiiException {

    private static String DEFAULT_TITLE = "Collection Object cannot be found or cannot be accessed.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.FORBIDDEN.value());

    public CollectionObjectDoesNotExistException(String collectionId, String objectId) {
        super(null, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public CollectionObjectDoesNotExistException(String collectionId, String objectId, String description) {
        super(null, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
