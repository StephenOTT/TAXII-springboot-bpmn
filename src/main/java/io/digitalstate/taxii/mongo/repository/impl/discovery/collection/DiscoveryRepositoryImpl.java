package io.digitalstate.taxii.mongo.repository.impl.discovery.collection;

import io.digitalstate.taxii.mongo.model.document.CollectionDocument;
import io.digitalstate.taxii.mongo.model.document.DiscoveryDocument;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DiscoveryRepositoryImpl implements DiscoveryRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    public DiscoveryRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public DiscoveryDocument getDiscoveryInfo() {

        //@TODO add aggregation projection that only returns the tenant_slug value
//        TenantDocument tenant = tenantRepository.findAll(slug)
//                .orElseThrow(()->new IllegalStateException("Cannot find tenant with slug: " + slug));

        Query query = new Query();
        return template.findOne(query, DiscoveryDocument.class);
    }
}
