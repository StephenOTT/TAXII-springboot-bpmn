package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.document.DiscoveryDocument;
import io.digitalstate.taxii.mongo.repository.impl.discovery.DiscoveryRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiscoveryRepository extends MongoRepository<DiscoveryDocument, String>, DiscoveryRepositoryCustom {

}
