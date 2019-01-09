package io.digitalstate.taxii.mongo.exception;

import io.digitalstate.taxii.exception.TaxiiException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public class CannotCreateStatusDocumentException extends TaxiiException {

    private static String DEFAULT_TITLE = "Cannot create Status Document.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.BAD_REQUEST.value());

    public CannotCreateStatusDocumentException(@Nullable Throwable cause) {
        super(cause, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public CannotCreateStatusDocumentException(@Nullable Throwable cause, @NotNull String description) {
        super(cause, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
