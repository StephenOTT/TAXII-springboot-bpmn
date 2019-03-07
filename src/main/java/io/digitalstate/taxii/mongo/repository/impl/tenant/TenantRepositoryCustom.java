package io.digitalstate.taxii.mongo.repository.impl.tenant;

import io.digitalstate.taxii.mongo.model.document.TenantDocument;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Implements with auto-detection with {@link TenantRepositoryImpl}
 */
public interface TenantRepositoryCustom {

    Optional<TenantDocument> findTenantBySlug(@NotNull String slug);

    Optional<TenantDocument> findTenantByTenantId(@NotNull String tenantId);

    List<TenantDocument> findAllTenantsByFilter(String futureFilterGoesHere);

    TenantDocument createTenant(@NotNull TenantDocument tenantDoc);
}
