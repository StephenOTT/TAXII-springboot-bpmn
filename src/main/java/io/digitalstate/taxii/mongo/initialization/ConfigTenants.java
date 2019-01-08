package io.digitalstate.taxii.mongo.initialization;


import io.digitalstate.taxii.model.apiroot.TaxiiApiRoot;
import io.digitalstate.taxii.model.collection.TaxiiCollection;
import io.digitalstate.taxii.model.collection.TaxiiCollectionResource;
import io.digitalstate.taxii.mongo.model.document.*;
import io.digitalstate.taxii.mongo.repository.CollectionRepository;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import io.digitalstate.taxii.mongo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Configuration
public class ConfigTenants {

    @Autowired
    private TenantRepository tenantRepository;

    @Bean("setupTenants")
    public void setupTenantsBean() {

        TaxiiApiRoot tenant = TaxiiApiRoot.builder()
                .tenantId("1234567890")
                .tenantSlug("tenant123")
                .title("Some tenant title")
                .description("some description")
                .build();

        TenantDocument tenantDoc = ImmutableTenantDocument.builder()
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .tenant(tenant)
                .build();

        tenantRepository.save(tenantDoc);
    }
}
