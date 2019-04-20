package io.digitalstate.taxii.security.detailsservice;

import io.digitalstate.taxii.endpoints.context.TenantWebContext;
import io.digitalstate.taxii.mongo.initialization.ConfigTenants;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.model.document.UserDocument;
import io.digitalstate.taxii.mongo.model.document.UserRolesDocument;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import io.digitalstate.taxii.mongo.repository.UserRepository;
import io.digitalstate.taxii.mongo.repository.UserRolesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class TaxiiUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(TaxiiUserDetailsService.class);

    @Autowired
    private ConfigTenants configTenants;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantWebContext tenantWebContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String lowercaseUsername = username.toLowerCase();

        // Gets the TenantSlug from the TenantWeb Context
        // If there is no Tenant supplied then we fallback to the Default/base tenant
        String tenantSlug = Optional.ofNullable(tenantWebContext.getTenantSlug())
                .orElse(configTenants.getDefaultSlug());

        // Determines if the User exists in the default Tenant
        // If a different tenant setup is required then this would change to support that style of filtering.
        tenantWebContext.setDatabaseToDefaultTenantForCurrentThread();
        UserDocument user = userRepository.findUserByUsername(lowercaseUsername, configTenants.getDefaultId())
                .orElseThrow(() -> new UsernameNotFoundException("Unable to find username"));

        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();

        // We always check the Tenant info because even if there is no tenant then we load the User's Default Tenant info.
        // It could be possible that a user has a user account but is not part of any tenant, including the default tenant
        TenantDocument tenant = tenantRepository.findTenantBySlug(tenantSlug)
                .orElseThrow(() -> new UsernameNotFoundException("Tenant could not be found with the provided Slug"));
        String tenantId = tenant.tenant().getTenantId();

        UserRolesDocument tenantRoles = userRolesRepository.findTenantRoles(user.id(), tenantId)
                .orElseThrow(() -> new UsernameNotFoundException("User does not have access to tenant: " + tenantSlug));

        tenantRoles.getRoles().forEach(r -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(r));
        });

        tenantWebContext.setTenantId(tenantId, true);

        return new User(user.username(),
                user.passwordInfo().password(),
                grantedAuthorities);
    }
}
