package io.digitalstate.taxii.mongo.repository.impl.collectionobject;

import io.digitalstate.taxii.mongo.model.document.CollectionObjectDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public interface CollectionObjectRepositoryCustom {

    Page<CollectionObjectDocument> findAllObjectsByCollectionId(@NotNull String collectionId, @Nullable String tenantId, @NotNull Pageable pageable);

}
