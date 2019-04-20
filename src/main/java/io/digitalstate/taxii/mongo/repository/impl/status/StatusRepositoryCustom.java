package io.digitalstate.taxii.mongo.repository.impl.status;

import io.digitalstate.taxii.model.status.TaxiiStatusFailureResource;
import io.digitalstate.taxii.mongo.model.document.StatusDocument;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Implemented with {@link StatusRepositoryImpl}
 */
public interface StatusRepositoryCustom {

    Optional<StatusDocument> findStatusById(@NotNull String statusId, @NotNull String tenantId);

//    StatusDocument updateStatusById(@NotNull String statusId, @Nullable String tenantId);

    @Transactional
    StatusDocument createStatus(@NotNull StatusDocument statusDoc, @NotNull String targetTenantId);

    @Transactional
    Optional<StatusDocument> incrementSuccessCount(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> incrementFailureCount(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> decrementFailureCount(@NotNull @Min(0) long subtractAmt, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> incrementPendingCount(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> decrementPendingCount(@NotNull @Min(0) long subtractAmt, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> addSuccess(@NotNull String objectId, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> addFailure(@NotNull TaxiiStatusFailureResource failureResource, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> removeFailure(@NotNull String objectId, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> addPending(@NotNull String objectId, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> removePending(@NotNull String objectId, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> incrementSuccessCountWithPendingCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> incrementSuccessCountWithFailureCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> incrementFailureCountWithPendingCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    Optional<StatusDocument> incrementPendingCountWithFailureCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    @Transactional
    boolean updateStatusValue(@NotNull String newStatus, @NotNull String statusId, @NotNull String tenantId);

}
