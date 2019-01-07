package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.document.CollectionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CollectionRepository extends MongoRepository<CollectionDocument, String> {

}
