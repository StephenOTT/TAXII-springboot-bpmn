package io.digitalstate.taxii.mongo.initialization;


import io.digitalstate.taxii.model.tenant.TaxiiTenant;
import io.digitalstate.taxii.mongo.model.document.*;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import io.digitalstate.taxii.mongo.TenantDbContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class ConfigTenants {

    @Value("${taxii.tenant.id:adminFallBack}")
    private String defaultId;

    @Value("${taxii.tenant.slug:adminSlugFallBack}")
    private String defaultSlug;

    @Value("${taxii.tenant.title:Admin tenant}")
    private String defaultTitle;

    @Value("${taxii.tenant.description:The Admin tenant}")
    private String defaultDescription;

    @Value("${taxii.tenant.db_connection_string:1111}")
    private String defaultDbConnectionString;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantDbContext tenantDbContext;

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

        tenantDbContext.setDatabaseNameToDefaultForCurrentThread();
        tenantRepository.insert(tenantDoc);
    }

    public String getDefaultId() {
        return defaultId;
    }

    public String getDefaultSlug() {
        return defaultSlug;
    }

    public String getDefaultDbConnectionString() {
        return defaultDbConnectionString;
    }
}
