package io.digitalstate.taxii.mongo.initialization;

import io.digitalstate.taxii.mongo.model.document.*;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import io.digitalstate.taxii.mongo.repository.UserRepository;
import io.digitalstate.taxii.mongo.repository.UserRolesRepository;
import io.digitalstate.taxii.mongo.TenantDbContext;
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

    @Value("${taxii.user.username:admin}")
    private String defaultUsername;

    @Value("${taxii.user.password:admin}")
    private String defaultPassword;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private TenantDbContext tenantDbContext;

    @Bean("setupUsers")
    @DependsOn("setupTenants")
    public void setupUsersBean() {

        tenantDbContext.setDatabaseNameToDefaultForCurrentThread();

        TenantDocument tenantDocument = tenantRepository.findTenantByTenantId(configTenants.getDefaultId())
                .orElseThrow(() -> new IllegalStateException("Cant find tenant: " + configTenants.getDefaultId()));

        //@TODO Create method for adding role prefix
        //@TODO Review how role is used
        String rolePrefix = "ROLE_";

        UserDocument user1 = ImmutableUserDocument.builder()
                .username(defaultUsername)
                .tenantId(tenantDocument.tenant().getTenantId())
                .passwordInfo(ImmutablePassword.of(defaultPassword, false))
                .modifiedAt(Instant.now())
                .build();

        userRepository.createUser(user1, user1.tenantId());

        UserRolesDocument rolesDocument = ImmutableUserRolesDocument.builder()
                .userId(user1.id())
                .addRoles("ROLE_COLLECTIONS_VIEWER",
                        "ROLE_COLLECTION_VIEWER",
                        "ROLE_COLLECTION_OBJECTS_VIEWER",
                        "ROLE_COLLECTION_OBJECT_VIEWER",
                        "ROLE_COLLECTION_OBJECTS_CREATOR",
                        "ROLE_DISCOVERY_VIEWER",
                        "ROLE_TENANT_VIEWER",
                        "ROLE_USERS_VIEWER")
                .tenantId(tenantDocument.tenant().getTenantId())
                .build();

        userRolesRepository.createUserRole(rolesDocument);
    }

    public String getDefaultUsername() {
        return defaultUsername;
    }
}