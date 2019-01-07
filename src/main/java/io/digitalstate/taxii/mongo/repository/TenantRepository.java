package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.TenantDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TenantRepository extends MongoRepository<TenantDocument, String> {

}
