package io.digitalstate.taxii.mongo.initialization;

import io.digitalstate.stix.sdo.objects.AttackPattern;
import io.digitalstate.stix.sdo.objects.AttackPatternSdo;
import io.digitalstate.taxii.model.collection.TaxiiCollection;
import io.digitalstate.taxii.model.collection.TaxiiCollectionResource;
import io.digitalstate.taxii.mongo.model.document.*;
import io.digitalstate.taxii.mongo.repository.*;
import io.digitalstate.taxii.mongo.TenantDbContext;
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
    private ConfigUsers configUsers;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionObjectRepository collectionObjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionMembershipsRepository collectionMembershipsRepository;

    @Autowired
    private TenantDbContext tenantDbContext;

    @Bean("setupCollections")
    @DependsOn("setupUsers")
    public void setupCollectionsBean() {

        UserDocument userDocument = userRepository.findUserByUsername(configUsers.getDefaultUsername(), configTenants.getDefaultId())
                .orElseThrow(() -> new IllegalStateException("Cant find username: " + configUsers.getDefaultUsername()));

        TenantDocument tenantDocument = tenantRepository.findTenantByTenantId(configTenants.getDefaultId())
                .orElseThrow(() -> new IllegalStateException("Cant find tenant: " + configTenants.getDefaultId()));

        tenantDbContext.setDatabaseNameForCurrentThread(tenantDocument.tenant().getTenantId());

        TaxiiCollectionResource collection = TaxiiCollection.builder()
                .title("someTitle")
                .description("some description of a collection")
                .canRead(false)
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

        collectionObjectRepository.createCollectionObject(collectionObjectDocument, collectionObjectDocument.tenantId());

        CollectionObjectDocument collectionObjectDocumentWithDiffModified =
                ImmutableCollectionObjectDocument.copyOf(collectionObjectDocument)
                        .withId(UUID.randomUUID().toString())
                        .withObject(
                                AttackPattern.copyOf(attackPatternSdo)
                                        .withModified(Instant.now().plusSeconds(500000)));


        collectionObjectRepository.createCollectionObject(collectionObjectDocumentWithDiffModified, collectionObjectDocumentWithDiffModified.tenantId());

        // Setup Collection membership
        CollectionMembershipDocument collectionMembershipDocument =
                ImmutableCollectionMembershipDocument.builder()
                        .tenantId(tenantDocument.tenant().getTenantId())
                        .userId(userDocument.id())
                        .collectionId(collectionDocument.collection().getId())
                        .canRead(true)
                        .canWrite(true)
                        .build();
        collectionMembershipsRepository.createCollectionMembership(collectionMembershipDocument,
                collectionMembershipDocument.tenantId());

        tenantDbContext.setDatabaseNameToDefaultForCurrentThread();

    }
}
