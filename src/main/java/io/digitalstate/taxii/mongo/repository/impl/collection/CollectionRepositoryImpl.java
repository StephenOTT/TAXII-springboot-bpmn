package io.digitalstate.taxii.mongo.repository.impl.collection;

import io.digitalstate.taxii.mongo.model.document.CollectionDocument;
import io.digitalstate.taxii.endpoints.context.TenantWebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public class CollectionRepositoryImpl implements CollectionRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    private TenantWebContext tenantWebContext;

    @Autowired
    public CollectionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }

    @Override
    public List<CollectionDocument> findAllCollections(@NotNull String targetTenantId) {
        Query query = new Query();
        if (targetTenantId != null) {
            query.addCriteria(Criteria.where("tenant_id").is(targetTenantId));
        }
        return template.find(query, CollectionDocument.class);
    }

    @Override
    public Optional<CollectionDocument> findCollectionById(@NotNull String collectionId, @NotNull String targetTenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("collection.id").is(collectionId));
        if (targetTenantId != null) {
            query.addCriteria(Criteria.where("tenant_id").is(targetTenantId));
        }
        return Optional.ofNullable(template.findOne(query, CollectionDocument.class));
    }

//    @Override
//    public List<CollectionDocument> findAllCollectionsForUsername(@NotNull String username, @NotNull String tenantId) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("username").is(username));
//        query.addCriteria(Criteria.where("tenant_memberships")
//                .elemMatch(Criteria.where("tenant_id").is(tenantId)));
//        Optional<UserDocument> userDocument = Optional.ofNullable(template.findOne(query, UserDocument.class));
//
//        if (!userDocument.isPresent()) {
//            return Collections.emptyList();
//
//        } else {
//            Set<CollectionMembershipDefinition> collectionMemberships = userDocument.get()
//                    .tenantMemberships().parallelStream()
//                    .findFirst().orElseThrow(IllegalStateException::new)
//                    .getCollectionMemberships();
//            Set<String> collectionsIds = collectionMemberships.parallelStream()
//                    .map(CollectionMembershipDefinition::getCollectionId)
//                    .collect(Collectors.toSet());
//
//            Query batchQuery = new Query();
//            query.addCriteria(Criteria.where("collection.id").in(collectionsIds));
//            List<CollectionDocument> collectionDocumentsRaw = template.find(batchQuery, CollectionDocument.class);
//
//            return collectionDocumentsRaw.stream().map(d -> {
//                return ImmutableCollectionDocument.copyOf(d)
//                        .withCollection(
//                                TaxiiCollection.copyOf(d.collection())
//                                        .withCanRead(collectionMemberships.stream()
//                                                .filter(m -> m.getCollectionId().equals(d.collection().getId()))
//                                                .findFirst()
//                                                .orElseThrow(IllegalStateException::new).canRead())
//                                        .withCanWrite(collectionMemberships.stream()
//                                                .filter(m -> m.getCollectionId().equals(d.collection().getId()))
//                                                .findFirst()
//                                                .orElseThrow(IllegalStateException::new).canWrite()));
//            }).collect(Collectors.toList());
//
//        }
//    }
}
