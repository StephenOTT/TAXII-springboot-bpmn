package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.document.CollectionMembershipDocument;
import io.digitalstate.taxii.mongo.repository.impl.collection.CollectionMembershipsRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CollectionMembershipsRepository extends MongoRepository<CollectionMembershipDocument, String>, CollectionMembershipsRepositoryCustom {

}
