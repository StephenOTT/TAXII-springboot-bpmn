package io.digitalstate.taxii.mongo.initialization;


import io.digitalstate.taxii.mongo.model.document.ImmutableUserDocument;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.model.document.UserDocument;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import io.digitalstate.taxii.mongo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Instant;

@Configuration
public class ConfigUsers {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    @Bean("setupUsers")
    @DependsOn("setupTenants")
    public void setupUsersBean() {

        TenantDocument tenantDocument = tenantRepository.findTenantBySlug("tenant123")
                .orElseThrow(()-> new IllegalStateException("Cant find tenant123"));

        UserDocument user1 = ImmutableUserDocument.builder()
                .username("steve")
                .modifiedAt(Instant.now())
                .tenantId(tenantDocument.tenant().getTenantId())
                .build();

        userRepository.save(user1);

    }
}
