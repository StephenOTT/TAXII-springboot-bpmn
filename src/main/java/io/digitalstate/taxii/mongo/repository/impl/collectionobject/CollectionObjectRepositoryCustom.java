package io.digitalstate.taxii.mongo.repository.impl.collectionobject;

import io.digitalstate.taxii.mongo.exception.CollectionObjectAlreadyExistsException;
import io.digitalstate.taxii.mongo.model.document.CollectionObjectDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Implemented with {@link CollectionObjectRepositoryImpl}
 */
public interface CollectionObjectRepositoryCustom {

    @Transactional
    <S extends CollectionObjectDocument> S save(S entity) throws CollectionObjectAlreadyExistsException;

    Page<CollectionObjectDocument> findAllObjectsByCollectionId(@NotNull String collectionId,
                                                                @Nullable String tenantId,
                                                                @NotNull Pageable pageable);

    List<CollectionObjectDocument> findObjectByObjectId(@NotNull String objectId,
                                                        @Nullable String collectionId,
                                                        @Nullable String tenantId);

    boolean objectExists(@NotNull String objectId,
                                            @Nullable Instant modified,
                                            @Nullable String collectionId,
                                            @Nullable String tenantId);

}
