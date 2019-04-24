package io.digitalstate.taxii.mongo.exceptions;

import io.digitalstate.taxii.exception.TaxiiException;
import org.springframework.http.HttpStatus;

public class CannotUpdateTenantMembershipException extends TaxiiException {

    private static String DEFAULT_TITLE = "User's Tenant Membership cannot be updated.";
    private static String DEFAULT_HTTP_STATUS = String.valueOf(HttpStatus.NOT_FOUND.value());

    public CannotUpdateTenantMembershipException(String membershipData) {
        super(null, DEFAULT_TITLE,
                null, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }

    public CannotUpdateTenantMembershipException(String membershipData, String description) {
        super(null, DEFAULT_TITLE,
                description, null, null,
                DEFAULT_HTTP_STATUS,
                null, null);
    }
}
