package io.digitalstate.taxii.mongo.repository.impl.user;

import com.mongodb.DuplicateKeyException;
import io.digitalstate.taxii.mongo.model.document.UserRolesDocument;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface UserRolesRepositoryCustom {

    Optional<UserRolesDocument> findTenantRoles(@NotNull String userId, @NotNull String tenantId);

    List<UserRolesDocument> findAllTenantRolesByUsername(@NotNull String username);

    UserRolesDocument insertUserRole(@NotNull UserRolesDocument userRolesDocument) throws DuplicateKeyException;
}
