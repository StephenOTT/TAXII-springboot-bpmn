package io.digitalstate.taxii.mongo.repository;

import io.digitalstate.taxii.mongo.model.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDocument, String> {

}
