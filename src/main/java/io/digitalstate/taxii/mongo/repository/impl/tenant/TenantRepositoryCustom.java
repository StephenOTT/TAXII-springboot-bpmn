package io.digitalstate.taxii.mongo.repository.impl.tenant;

import io.digitalstate.taxii.mongo.model.document.TenantDocument;

import java.util.List;
import java.util.Optional;

/**
 * Implements with auto-detection with {@link TenantRepositoryImpl}
 */
public interface TenantRepositoryCustom {

    Optional<TenantDocument> findTenantBySlug(String slug);

    Optional<TenantDocument> findTenantByTenantId(String tenantId);

    List<TenantDocument> findAllTenantsByFilter(String futureFilterGoesHere);
}
