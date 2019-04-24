package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.repository.impl.tenant.TenantRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TenantRepository extends MongoRepository<TenantDocument, String>, TenantRepositoryCustom {

}
