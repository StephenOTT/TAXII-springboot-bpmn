package io.digitalstate.taxii.mongo.repository.impl.user;

import com.mongodb.DuplicateKeyException;
import io.digitalstate.taxii.mongo.model.document.ImmutableUserDocument;
import io.digitalstate.taxii.mongo.model.document.UserDocument;
import io.digitalstate.taxii.mongo.model.document.UserRolesDocument;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

public class UserRolesRepositoryImpl implements UserRolesRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    private UserRolesRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }

    @Override
    public Optional<UserRolesDocument> findTenantRoles(@NotNull String userId, @NotNull String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId).and("tenant_id").is(tenantId));

        return Optional.ofNullable(template.findOne(query, UserRolesDocument.class));
    }

    @Override
    public List<UserRolesDocument> findAllTenantRolesByUsername(@NotNull String username) {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("users")
                .localField("user_id")
                .foreignField("_id")
                .as("user");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupOperation,
                match(Criteria.where("user.username").is(username)),
                new ProjectionOperation().andExclude("user")
        );

        AggregationResults<UserRolesDocument> results = template.aggregate(aggregation,
                template.getCollectionName(UserRolesDocument.class),
                UserRolesDocument.class);

        List<UserRolesDocument> userRolesDocuments = results.getMappedResults();

        return userRolesDocuments;
    }

    @Override
    public UserRolesDocument createUserRole(@NotNull UserRolesDocument userRolesDocument) throws DuplicateKeyException {
        return template.insert(userRolesDocument);
    }

}
