package io.digitalstate.taxii.mongo.repository.impl.user;

import io.digitalstate.taxii.mongo.model.document.UserDocument;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    private UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public Optional<UserDocument> findUserById(@NotNull String id, @Nullable String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }
        return Optional.ofNullable(template.findOne(query, UserDocument.class));
    }

    @Override
    public List<UserDocument> findAllUsersByTenantSlug(@NotNull String slug) {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("tenants")
                .localField("tenant_id")
                .foreignField("_id")
                .as("tenant");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupOperation,
                match(Criteria.where("tenant.tenant.tenant_slug").is(slug)),
                new ProjectionOperation().andExclude("tenant")
        );

        AggregationResults<UserDocument> results = template.aggregate(aggregation,
                template.getCollectionName(UserDocument.class),
                UserDocument.class);

        List<UserDocument> users = results.getMappedResults();

        return users;
    }

    public Page<UserDocument> findAllUsersByTenantId(@NotNull String tenantId, @NotNull Pageable pageable){
        Query query = new Query();
        query.addCriteria(Criteria.where("tenant_id").is(tenantId)).with(pageable);
        List<UserDocument> users = template.find(query, UserDocument.class);
        return PageableExecutionUtils.getPage(users, pageable,
                () -> template.count(query, UserDocument.class));
    }
}
