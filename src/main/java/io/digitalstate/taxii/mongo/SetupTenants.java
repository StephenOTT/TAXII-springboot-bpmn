package io.digitalstate.taxii.mongo;


import io.digitalstate.taxii.models.apiroot.TaxiiApiRoot;
import io.digitalstate.taxii.mongo.model.*;
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

    }
}
