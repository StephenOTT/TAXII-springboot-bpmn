package io.digitalstate.taxii.mongo.repository.impl.user;

import com.mongodb.DuplicateKeyException;
import io.digitalstate.taxii.mongo.model.document.ImmutablePassword;
import io.digitalstate.taxii.mongo.model.document.Password;
import io.digitalstate.taxii.security.encoder.PasswordEncodingService;
import io.digitalstate.taxii.mongo.model.document.ImmutableUserDocument;
import io.digitalstate.taxii.mongo.model.document.UserDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;

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
    private PasswordEncodingService passwordEncodingService;

    @Override
    public Optional<UserDocument> findUserById(@NotNull String id, @NotNull String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        if (tenantId != null) {
            query.addCriteria(Criteria.where("tenant_memberships").elemMatch(Criteria.where("tenant_id").is(tenantId)));
        }
        return Optional.ofNullable(template.findOne(query, UserDocument.class));
    }

    /**
     * If tenantId is provided, it will check if user has membership in the tenant.
     * @param username
     * @return
     */
    @Override
    public Optional<UserDocument> findUserByUsername(@NotNull String username, @NotNull String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        query.addCriteria(Criteria.where("tenant_id").is(tenantId));
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

    @Override
    public Page<UserDocument> findAllUsersByTenantId(@NotNull String tenantId, @NotNull Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenant_id").is(tenantId)).with(pageable);
        List<UserDocument> users = template.find(query, UserDocument.class);
        return PageableExecutionUtils.getPage(users, pageable,
                () -> template.count(query, UserDocument.class));
    }

    @Override
    public UserDocument createUser(@NotNull UserDocument userDoc, @NotNull String targetTenantId) throws DuplicateKeyException, IllegalArgumentException {
        // If password is not encoded then encode using the built in encoder.
        if (!userDoc.tenantId().equals(targetTenantId)){
            throw new IllegalArgumentException("UserDocument's tenantId does not match the targetTenantId.  Document's tenantId and TargetTenantId constraint exists to ensure User's are not added into unexpected tenants");
        }

        if (!userDoc.passwordInfo().isEncoded()){
            //@TODO Review this section and abstract it out so the password encoder is not a dep (so the entire security system can be removed if chosen)
            Password password = ImmutablePassword.of(passwordEncodingService.encodeRawPassword(userDoc.passwordInfo().password()), true);
            UserDocument docWithEncodedPassword = ImmutableUserDocument.copyOf(userDoc).withPasswordInfo(password);
            return template.insert(docWithEncodedPassword);
        } else {
            return template.insert(userDoc);
        }
    }
}
