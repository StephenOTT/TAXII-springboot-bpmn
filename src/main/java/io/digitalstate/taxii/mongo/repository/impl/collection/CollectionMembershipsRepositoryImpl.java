package io.digitalstate.taxii.mongo.repository.impl.collection;

import io.digitalstate.taxii.mongo.model.document.CollectionMembershipDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.validation.constraints.NotNull;

public class CollectionMembershipsRepositoryImpl implements CollectionMembershipsRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    public CollectionMembershipsRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }


    @Override
    public CollectionMembershipDocument createCollectionMembership(@NotNull CollectionMembershipDocument collectionMembershipDocument, @NotNull String targetTenantId) {
        if (!collectionMembershipDocument.tenantId().equals(targetTenantId)){
            throw new IllegalArgumentException("CollectionMembershipDocument's tenantId does not match the targetTenantId.  Document's tenantId and TargetTenantId constraint exists to ensure documents are not added into unexpected tenants");
        }

        return template.insert(collectionMembershipDocument);
    }
}
