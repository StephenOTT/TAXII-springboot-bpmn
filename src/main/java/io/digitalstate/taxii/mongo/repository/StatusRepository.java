package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.document.StatusDocument;
import io.digitalstate.taxii.mongo.repository.impl.status.StatusRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatusRepository extends MongoRepository<StatusDocument, String>, StatusRepositoryCustom {

}
