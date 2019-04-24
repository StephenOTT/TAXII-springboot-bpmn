package io.digitalstate.taxii.mongo.repository.impl.collection;

import io.digitalstate.taxii.mongo.model.document.CollectionDocument;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface CollectionRepositoryCustom {

    List<CollectionDocument> findAllCollections(@NotNull String targetTenantId);

    Optional<CollectionDocument> findCollectionById(@NotNull String collectionId, @NotNull String targetTenantId);

//    List<CollectionDocument> findAllCollectionsForUsername(@NotNull String username, @NotNull String tenantId);
}
