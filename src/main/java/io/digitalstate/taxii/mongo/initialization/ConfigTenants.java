package io.digitalstate.taxii.mongo.initialization;


import io.digitalstate.taxii.model.tenant.TaxiiTenant;
import io.digitalstate.taxii.mongo.model.document.*;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class ConfigTenants {

    @Autowired
    private TenantRepository tenantRepository;

    @Bean("setupTenants")
    public void setupTenantsBean() {

        TaxiiTenant tenant = TaxiiTenant.builder()
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
