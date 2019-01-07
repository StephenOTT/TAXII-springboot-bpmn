package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.document.CollectionDocument;
import io.digitalstate.taxii.mongo.repository.impl.collection.CollectionRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CollectionRepository extends MongoRepository<CollectionDocument, String>, CollectionRepositoryCustom {

}
