package io.digitalstate.taxii.mongo.repository.impl.user;

import io.digitalstate.taxii.mongo.model.document.UserDocument;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<UserDocument> findUserByTenantSlug(String slug);

    List<UserDocument> findAllUsersByTenantSlug(String slug);
}
