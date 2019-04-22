package io.digitalstate.taxii.mongo.repository.impl.status;

import io.digitalstate.taxii.model.status.TaxiiStatusFailureResource;
import io.digitalstate.taxii.mongo.model.document.StatusDocument;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Implemented with {@link StatusRepositoryImpl}
 */
public interface StatusRepositoryCustom {

    Optional<StatusDocument> findStatusById(@NotNull String statusId, @NotNull String tenantId);

//    StatusDocument updateStatusById(@NotNull String statusId, @Nullable String tenantId);

    StatusDocument createStatus(@NotNull StatusDocument statusDoc, @NotNull String targetTenantId);

    Optional<StatusDocument> incrementSuccessCount(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> incrementFailureCount(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> decrementFailureCount(@NotNull @Min(0) long subtractAmt, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> incrementPendingCount(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> decrementPendingCount(@NotNull @Min(0) long subtractAmt, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> addSuccess(@NotNull String objectId, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> addFailure(@NotNull TaxiiStatusFailureResource failureResource, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> removeFailure(@NotNull String objectId, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> addPending(@NotNull String objectId, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> removePending(@NotNull String objectId, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> incrementSuccessCountWithPendingCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> incrementSuccessCountWithFailureCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> incrementFailureCountWithPendingCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    Optional<StatusDocument> incrementPendingCountWithFailureCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @NotNull String tenantId);

    boolean updateStatusValue(@NotNull String newStatus, @NotNull String statusId, @NotNull String tenantId);

}
