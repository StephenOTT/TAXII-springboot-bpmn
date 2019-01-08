package io.digitalstate.taxii.mongo.repository.impl.collection;

import io.digitalstate.taxii.mongo.model.document.CollectionDocument;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.model.document.UserDocument;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

public class CollectionRepositoryImpl implements CollectionRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    public CollectionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }

    @Override
    public List<CollectionDocument> findAllCollectionsByTenantId(@NotNull String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        return template.find(query, CollectionDocument.class);
    }

    @Override
    public Optional<CollectionDocument> findCollectionById(@NotNull String collectionId, @Nullable String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("collection.id").is(collectionId));
        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }
        return Optional.ofNullable(template.findOne(query, CollectionDocument.class));
    }
}
