package io.digitalstate.taxii.mongo.initialization;


import io.digitalstate.taxii.model.tenant.TaxiiTenant;
import io.digitalstate.taxii.model.tenant.TaxiiTenantResource;
import io.digitalstate.taxii.mongo.model.document.*;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Configuration
public class ConfigTenants {

    @Value("${taxii.tenant.id : admin}")
    private String defaultId;

    @Value("${taxii.tenant.slug : admin}")
    private String defaultSlug;

    @Value("${taxii.tenant.title: Admin tenant}")
    private String defaultTitle;

    @Value("${taxii.tenant.description: The Admin tenant}")
    private String defaultDescription;

    @Autowired
    private TenantRepository tenantRepository;

    @Bean("setupTenants")
    public void setupTenantsBean() {
        TaxiiTenant tenant = TaxiiTenant.builder()
                .tenantId(defaultId)
                .tenantSlug(defaultSlug)
                .title(defaultTitle)
                .description(defaultDescription)
                .build();

        TenantDocument tenantDoc = ImmutableTenantDocument.builder()
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .tenant(tenant)
                .build();

        tenantRepository.save(tenantDoc);
    }

    public String getDefaultId() {
        return defaultId;
    }
}
