package io.digitalstate.taxii.mongo.repository.impl.user;

import io.digitalstate.taxii.mongo.model.document.UserDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Impl with {@link UserRepositoryImpl}
 */
public interface UserRepositoryCustom {

    Optional<UserDocument> findUserById(@NotNull String id, @NotNull String tenantId);

    Optional<UserDocument> findUserByUsername(@NotNull String username, @NotNull String tenantId);

    List<UserDocument> findAllUsersByTenantSlug(@NotNull String slug);

    Page<UserDocument> findAllUsersByTenantId(@NotNull String tenantId, @NotNull Pageable pageable);

    UserDocument createUser(@NotNull UserDocument userDoc, @NotNull String tenantId);

}
