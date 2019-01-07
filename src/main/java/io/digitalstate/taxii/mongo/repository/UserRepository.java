package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.document.UserDocument;
import io.digitalstate.taxii.mongo.repository.impl.user.UserRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDocument, String>, UserRepositoryCustom {


}
