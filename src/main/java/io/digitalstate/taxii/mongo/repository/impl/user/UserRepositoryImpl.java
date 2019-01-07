package io.digitalstate.taxii.mongo.repository.impl.user;

import io.digitalstate.taxii.mongo.model.document.UserDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }

    @Override
    public Optional<UserDocument> findUserByTenantSlug(String slug) {
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

        Optional<UserDocument> user = Optional.ofNullable(
                template.aggregate(aggregation,
                        template.getCollectionName(UserDocument.class),
                        UserDocument.class)
                        .getUniqueMappedResult());

        return user;
    }

    @Override
    public List<UserDocument> findAllUsersByTenantSlug(String slug) {
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
}
