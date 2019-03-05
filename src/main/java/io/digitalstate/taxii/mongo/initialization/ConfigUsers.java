package io.digitalstate.taxii.mongo.initialization;


import io.digitalstate.taxii.mongo.model.document.ImmutableUserDocument;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.model.document.UserDocument;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import io.digitalstate.taxii.mongo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Instant;

@Configuration
public class ConfigUsers {

    @Autowired
    private ConfigTenants configTenants;

    @Value("${taxii.user.username : admin}")
    private String defaultUsername;

    @Value("${taxii.user.password : admin}")
    private String defaultPassword;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    @Bean("setupUsers")
    @DependsOn("setupTenants")
    public void setupUsersBean() {

        TenantDocument tenantDocument = tenantRepository.findTenantByTenantId(configTenants.getDefaultId())
                .orElseThrow(()-> new IllegalStateException("Cant find tenant" + configTenants.getDefaultId()));

        UserDocument user1 = ImmutableUserDocument.builder()
                .username(defaultUsername)
                .modifiedAt(Instant.now())
                .tenantId(configTenants.getDefaultId())
                .build();

        userRepository.insert(user1);

    }
}
