package io.digitalstate.taxii.mongo.initialization;


import io.digitalstate.taxii.model.apiroot.TaxiiApiRoot;
import io.digitalstate.taxii.model.collection.TaxiiCollection;
import io.digitalstate.taxii.model.collection.TaxiiCollectionResource;
import io.digitalstate.taxii.mongo.model.document.*;
import io.digitalstate.taxii.mongo.repository.CollectionRepository;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import io.digitalstate.taxii.mongo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class SetupTenants  {

    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CollectionRepository collectionRepository;

    @PostConstruct
    public void init() {

        TaxiiApiRoot tenant1 = TaxiiApiRoot.builder()
                .tenantId("1234567890")
                .tenantSlug("tenant123")
                .title("Some Title")
                .description("some description").build();

        TenantDocument tenantDoc1 = ImmutableTenantDocument.builder()
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .tenant(tenant1).build();


//        tenantOperations.save(tenantDoc1);
        tenantRepository.save(tenantDoc1);
        Optional<TenantDocument> returnedDoc = tenantRepository.findById(tenantDoc1.id());
        System.out.println(returnedDoc.get().toString());


        List<TenantDocument> allTenants = tenantRepository.findAll();
        System.out.println(allTenants.toString());


        ImmutableUserDocument user1 = ImmutableUserDocument.builder()
                .username("steve")
                .modifiedAt(Instant.now())
                .tenantId(tenantDoc1.id())
                .build();

        userRepository.save(user1);

        Optional<UserDocument> userDoc1 = userRepository.findById(user1.id());
        System.out.println(userDoc1.get().toMongoJson());
        System.out.println(userDoc1.get().toJson());
        System.out.println(returnedDoc.get().toMongoJson());
        System.out.println(returnedDoc.get().toJson());

        Optional<TenantDocument> tDocBySlug =  tenantRepository.findTenantBySlug(returnedDoc.get().tenant().getTenantSlug());
        System.out.println("DOG--->");
        System.out.println(tDocBySlug);

        userRepository.findUserByTenantSlug(returnedDoc.get().tenant().getTenantSlug());
        List<UserDocument> users = userRepository.findAllUsersByTenantSlug("tenant123");
        System.out.println(users.size());
        System.out.println(users.toString());

        TaxiiCollectionResource taxiiCollectionResource = TaxiiCollection.builder()
                .title("someTitle")
                .description("some description of a collection")
                .canRead(true)
                .canWrite(false).build();
        CollectionDocument collectionDocument = ImmutableCollectionDocument.builder().
                tenantId(returnedDoc.get().id())
                .modifiedAt(Instant.now())
                .collection(taxiiCollectionResource).build();
        collectionRepository.save(collectionDocument);

        List<CollectionDocument> collectionDocuments = collectionRepository.findAllCollectionsByTenantSlug("tenant123");
        System.out.println(collectionDocuments.size());

    }
}
