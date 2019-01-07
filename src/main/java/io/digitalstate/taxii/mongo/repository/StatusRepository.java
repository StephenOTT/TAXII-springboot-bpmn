package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.document.StatusDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatusRepository extends MongoRepository<StatusDocument, String> {

}
