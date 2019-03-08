package io.digitalstate.taxii.mongo.repository.impl.status;

import io.digitalstate.taxii.model.status.TaxiiStatusFailureResource;
import io.digitalstate.taxii.mongo.exceptions.CannotUpdateStatusException;
import io.digitalstate.taxii.mongo.model.document.StatusDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public class StatusRepositoryImpl implements StatusRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    public StatusRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }

    @Override
    @Transactional
    public StatusDocument createStatus(@NotNull StatusDocument statusDoc) {
        return template.insert(statusDoc);
    }

    @Override
    public Optional<StatusDocument> findStatusById(@NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        return Optional.ofNullable(template.findOne(query, StatusDocument.class));
    }

    @Override
    public Optional<StatusDocument> incrementSuccessCount(@NotNull @Min(0) long addAmt, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.inc("status_resource.success_count", addAmt);

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }

    }

    @Override
    public Optional<StatusDocument> incrementFailureCount(@NotNull @Min(0) long addAmt, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.inc("status_resource.failure_count", addAmt);

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public Optional<StatusDocument> decrementFailureCount(@NotNull @Min(0) long subtractAmt, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.inc("status_resource.failure_count", -(subtractAmt));

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public Optional<StatusDocument> incrementPendingCount(@NotNull @Min(0) long addAmt, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.inc("status_resource.pending_count", addAmt);

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public Optional<StatusDocument> decrementPendingCount(@NotNull @Min(0) long subtractAmt, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.inc("status_resource.pending_count", -(subtractAmt));

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public Optional<StatusDocument> addSuccess(@NotNull String objectId, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.push("status_resource.successes", objectId);

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public Optional<StatusDocument> addFailure(@NotNull TaxiiStatusFailureResource failureResource, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.push("status_resource.failures", failureResource);

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public Optional<StatusDocument> removeFailure(@NotNull String objectId, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.pull("status_resource.failures", objectId);

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public Optional<StatusDocument> addPending(@NotNull String objectId, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.push("status_resource.pendings", objectId);

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public Optional<StatusDocument> removePending(@NotNull String objectId, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.pull("status_resource.pendings", objectId);

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public Optional<StatusDocument> incrementSuccessCountWithPendingCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.inc("status_resource.success_count", addAmt);
        update.inc("status_resource.pending_count", -(addAmt));

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public Optional<StatusDocument> incrementSuccessCountWithFailureCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.inc("status_resource.success_count", addAmt);
        update.inc("status_resource.failure_count", -(addAmt));

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public Optional<StatusDocument> incrementFailureCountWithPendingCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.inc("status_resource.failure_count", addAmt);
        update.inc("status_resource.pending_count", -(addAmt));

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    /**
     * Used when processing a object that failed, and you want to send it back into the queue of processing after it hopefully "fixed".
     * @param addAmt
     * @param statusId
     * @param tenantId
     * @return Optional with StatusDocument if change occured, otherwise null
     *
     * Typically best to use something like
     * <br><br>
     * <p>
     * {@code
     * statusRepository.incrementFailureCountWithPendingCountDecrement(Long.valueOf(value), statusId, tenant.tenant().getTenantId()).orElseThrow(() -> new StatusDoesNotExistException(statusId));
     * }
     * </p>
     *
     */
    @Override
    public Optional<StatusDocument> incrementPendingCountWithFailureCountDecrement(@NotNull @Min(0) long addAmt, @NotNull String statusId, @Nullable String tenantId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.inc("status_resource.pending_count", addAmt);
        update.inc("status_resource.failure_count", -(addAmt));

        boolean updateSuccess = template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
        if (updateSuccess){
            return Optional.ofNullable(template.findOne(query, StatusDocument.class));
        } else {
            throw new CannotUpdateStatusException(statusId, "Status Update failed to persist.");
        }
    }

    @Override
    public boolean updateStatusValue(@NotNull String newStatus, @NotNull String statusId, String tenantId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("status_resource.id").is(statusId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        Update update = new Update();
        update.set("status_resource.status", newStatus);

        return template.updateFirst(query, update, StatusDocument.class).wasAcknowledged();
    }

//@TODO
//    public StatusDocument updateStatusById(@NotNull String statusId, @Nullable String tenantId){
//
//    }
}
