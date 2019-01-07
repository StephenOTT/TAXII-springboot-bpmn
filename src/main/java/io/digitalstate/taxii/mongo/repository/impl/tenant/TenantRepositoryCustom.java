package io.digitalstate.taxii.mongo.repository.impl.tenant;

import io.digitalstate.taxii.mongo.model.document.TenantDocument;

import java.util.Optional;

public interface TenantRepositoryCustom {

    Optional<TenantDocument> findTenantBySlug(String slug);
}
