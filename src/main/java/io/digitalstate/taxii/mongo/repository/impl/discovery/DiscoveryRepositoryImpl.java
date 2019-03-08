package io.digitalstate.taxii.mongo.repository.impl.discovery;

import io.digitalstate.taxii.mongo.model.document.DiscoveryDocument;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.model.document.UserDocument;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class DiscoveryRepositoryImpl implements DiscoveryRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    public DiscoveryRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public Optional<DiscoveryDocument> findDiscovery() {
        Query query = new Query();
//        query.addCriteria(Criteria.where("_id").is(id));
//        if (tenantId != null){
//            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
//        }
        return Optional.ofNullable(template.findOne(query, DiscoveryDocument.class));
    }

    @Override
    public DiscoveryDocument createDiscovery(@NotNull DiscoveryDocument discoveryDoc) {
        return template.insert(discoveryDoc);
    }
}
