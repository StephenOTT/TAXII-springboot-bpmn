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

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

public class CollectionRepositoryImpl implements CollectionRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    public CollectionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public List<CollectionDocument> findAllCollectionsByTenantSlug(String slug) {

        TenantDocument tenant = tenantRepository.findTenantBySlug(slug)
                .orElseThrow(()->new IllegalStateException("Cannot find tenant with slug: " + slug));

        Query query = new Query();
        query.addCriteria(Criteria.where("tenant_id").is(tenant.id()));
        return template.find(query, CollectionDocument.class);
    }
}
