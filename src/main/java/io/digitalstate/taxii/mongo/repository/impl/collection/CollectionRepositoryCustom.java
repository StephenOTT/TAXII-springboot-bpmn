package io.digitalstate.taxii.mongo.repository.impl.collection;

import io.digitalstate.taxii.mongo.model.document.CollectionDocument;
import io.digitalstate.taxii.mongo.model.document.UserDocument;

import java.util.List;
import java.util.Optional;

public interface CollectionRepositoryCustom {

    List<CollectionDocument> findAllCollectionsByTenantSlug(String slug);
}
