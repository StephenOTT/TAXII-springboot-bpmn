package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.DiscoveryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiscoveryRepository extends MongoRepository<DiscoveryDocument, String> {

}
