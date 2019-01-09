package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.exception.CollectionObjectAlreadyExistsException;
import io.digitalstate.taxii.mongo.model.document.CollectionObjectDocument;
import io.digitalstate.taxii.mongo.repository.impl.collectionobject.CollectionObjectRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CollectionObjectRepository extends MongoRepository<CollectionObjectDocument, String>, CollectionObjectRepositoryCustom {

    @Override
    <S extends CollectionObjectDocument> S save(S entity) throws CollectionObjectAlreadyExistsException;
}
