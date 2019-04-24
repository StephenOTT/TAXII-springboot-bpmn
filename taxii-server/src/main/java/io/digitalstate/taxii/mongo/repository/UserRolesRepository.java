package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.document.UserRolesDocument;
import io.digitalstate.taxii.mongo.repository.impl.user.UserRolesRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRolesRepository extends MongoRepository<UserRolesDocument, String>, UserRolesRepositoryCustom {

}
