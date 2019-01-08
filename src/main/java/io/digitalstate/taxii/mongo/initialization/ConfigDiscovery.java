package io.digitalstate.taxii.mongo.initialization;

import io.digitalstate.taxii.model.apiroot.TaxiiApiRootResource;
import io.digitalstate.taxii.model.discovery.TaxiiDiscovery;
import io.digitalstate.taxii.model.discovery.TaxiiDiscoveryResource;
import io.digitalstate.taxii.mongo.model.document.DiscoveryDocument;
import io.digitalstate.taxii.mongo.model.document.ImmutableDiscoveryDocument;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.repository.DiscoveryRepository;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Initialize Mongo with basic Discovery info as defined by configuration
 */
@Configuration
public class ConfigDiscovery {

    @Autowired
    private DiscoveryRepository discoveryRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Bean("setupDiscovery")
    @DependsOn("setupTenants")
    public void setupDiscoveryBean() {

        TenantDocument tenantDocument = tenantRepository.findTenantBySlug("tenant123")
                .orElseThrow(()-> new IllegalStateException("Cant find tenant123"));

        Set<TaxiiApiRootResource> roots = new HashSet<>();
        roots.add(tenantDocument.tenant());

        Set<String> rootStrings = roots.stream().map(TaxiiApiRootResource::getTenantSlug).collect(Collectors.toSet());

        String defaultRoot = tenantDocument.tenant().getTenantSlug(); // Consider adding a "default" flag to a root to mark that it should be used as a default.  Could also be setup as a config in application.yaml

        TaxiiDiscoveryResource discovery = TaxiiDiscovery.builder()
                .title("Some Taxi Server")
                .description("some taxii server description")
                .defaultApiRoot(defaultRoot)
                .addAllApiRoots(rootStrings)
                .build();

        DiscoveryDocument discoveryDocument = ImmutableDiscoveryDocument.builder()
                .modifiedAt(Instant.now())
                .serverInfo(discovery)
                .build();

        discoveryRepository.save(discoveryDocument);
    }
}
