package io.digitalstate.taxii.mongo.repository.impl.tenant;

import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

public class TenantRepositoryImpl implements TenantRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    public TenantRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }

    @Override
    public Optional<TenantDocument> findTenantBySlug(String slug) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenant.tenant_slug").is(slug));
        return Optional.ofNullable(template.findOne(query, TenantDocument.class));
    }

    @Override
    public List<TenantDocument> findAllTenantsByFilter(String futureFilterGoesHere) {
        Query query = new Query();
        //@TODO add criteria based on the filter to be added
        return template.find(query, TenantDocument.class);
    }
}
