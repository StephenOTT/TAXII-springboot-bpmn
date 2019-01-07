package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.document.CollectionObjectDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CollectionObjectRepository extends MongoRepository<CollectionObjectDocument, String> {

}
