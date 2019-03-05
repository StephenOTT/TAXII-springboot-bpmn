package io.digitalstate.taxii.mongo.initialization;

import io.digitalstate.stix.sdo.objects.AttackPattern;
import io.digitalstate.stix.sdo.objects.AttackPatternSdo;
import io.digitalstate.taxii.model.collection.TaxiiCollection;
import io.digitalstate.taxii.model.collection.TaxiiCollectionResource;
import io.digitalstate.taxii.mongo.model.document.*;
import io.digitalstate.taxii.mongo.repository.CollectionObjectRepository;
import io.digitalstate.taxii.mongo.repository.CollectionRepository;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Instant;
import java.util.UUID;

@Configuration
public class ConfigCollections {

    @Autowired
    private ConfigTenants configTenants;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionObjectRepository collectionObjectRepository;

    @Bean("setupCollections")
    @DependsOn("setupTenants")
    public void setupCollectionsBean() {

        TenantDocument tenantDocument = tenantRepository.findTenantByTenantId(configTenants.getDefaultId())
                .orElseThrow(() -> new IllegalStateException("Cant find tenant: " + configTenants.getDefaultId()));

        TaxiiCollectionResource collection = TaxiiCollection.builder()
                .title("someTitle")
                .description("some description of a collection")
                .canRead(true)
                .canWrite(false)
                .build();

        CollectionDocument collectionDocument = ImmutableCollectionDocument.builder().
                tenantId(tenantDocument.tenant().getTenantId())
                .modifiedAt(Instant.now())
                .collection(collection)
                .build();

        collectionRepository.save(collectionDocument);

        // Setup Object Reference
        AttackPatternSdo attackPatternSdo = AttackPattern.builder()
                .name("some AttackPattern")
                .build();

        CollectionObjectDocument collectionObjectDocument = ImmutableCollectionObjectDocument.builder()
                .modifiedAt(Instant.now())
                .collectionId(collectionDocument.collection().getId())
                .tenantId(collectionDocument.tenantId())
                .object(attackPatternSdo)
                .build();

        collectionObjectRepository.save(collectionObjectDocument);

        CollectionObjectDocument collectionObjectDocumentWithDiffModified =
                ImmutableCollectionObjectDocument.copyOf(collectionObjectDocument)
                        .withId(UUID.randomUUID().toString())
                        .withObject(
                                AttackPattern.copyOf(attackPatternSdo)
                                .withModified(Instant.now().plusSeconds(500000)));


        collectionObjectRepository.insert(collectionObjectDocumentWithDiffModified);

    }
}
