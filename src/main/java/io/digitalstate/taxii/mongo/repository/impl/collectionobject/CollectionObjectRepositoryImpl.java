package io.digitalstate.taxii.mongo.repository.impl.collectionobject;

import io.digitalstate.taxii.mongo.model.document.CollectionObjectDocument;
import io.digitalstate.taxii.mongo.repository.CollectionObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CollectionObjectRepositoryImpl implements CollectionObjectRepositoryCustom {

    private final MongoTemplate template;

    @Autowired
    public CollectionObjectRepositoryImpl(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate;
    }

    @Autowired
    private CollectionObjectRepository collectionObjectRepository;

    public Page<CollectionObjectDocument> findAllObjectsByCollectionId(@NotNull String collectionId, @Nullable String tenantId, @NotNull Pageable pageable){
        Query query = new Query();

        query.addCriteria(Criteria.where("collection_id").is(collectionId));

        if (tenantId != null){
            query.addCriteria(Criteria.where("tenant_id").is(tenantId));
        }

        query.with(pageable);

        List<CollectionObjectDocument> objects = template.find(query, CollectionObjectDocument.class);
        return PageableExecutionUtils.getPage(objects, pageable,
                () -> template.count(query, CollectionObjectDocument.class));
    }

}
