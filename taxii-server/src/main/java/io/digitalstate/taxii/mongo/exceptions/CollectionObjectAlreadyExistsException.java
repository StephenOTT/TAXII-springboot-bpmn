package io.digitalstate.taxii.mongo.exceptions;

import io.digitalstate.taxii.exception.TaxiiException;
import org.springframework.http.HttpStatus;

public class CollectionObjectAlreadyExistsException extends TaxiiException {

    private static String DEFAULT_TITLE = "Duplicated detected: Collection Object already exists.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value());

    public CollectionObjectAlreadyExistsException(String collectionId, String objectId) {
        super(null, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public CollectionObjectAlreadyExistsException(String collectionId, String objectId, String description) {
        super(null, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
