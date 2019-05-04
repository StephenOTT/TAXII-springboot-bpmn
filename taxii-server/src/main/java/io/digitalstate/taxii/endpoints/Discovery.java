package io.digitalstate.taxii.endpoints;

import io.digitalstate.taxii.common.Headers;
import io.digitalstate.taxii.endpoints.context.TenantWebContext;
import io.digitalstate.taxii.model.discovery.TaxiiDiscovery;
import io.digitalstate.taxii.model.discovery.TaxiiDiscoveryResource;
import io.digitalstate.taxii.mongo.exceptions.DiscoveryDoesNotExistException;
import io.digitalstate.taxii.mongo.model.document.DiscoveryDocument;
import io.digitalstate.taxii.mongo.model.document.ImmutableDiscoveryDocument;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.model.document.UserRolesDocument;
import io.digitalstate.taxii.mongo.repository.DiscoveryRepository;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import io.digitalstate.taxii.mongo.repository.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class Discovery {

    @Autowired
    private DiscoveryRepository discoveryRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantWebContext tenantWebContext;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @GetMapping("/taxii")
    @PreAuthorize("hasRole('ROLE_DISCOVERY_VIEWER')")
    @ResponseBody
    public ResponseEntity<String> getDiscovery(@RequestHeader HttpHeaders headers,
                                               Principal principal) {

        tenantWebContext.setDatabaseToDefaultTenantForCurrentThread();
        DiscoveryDocument discoveryDocument = discoveryRepository.findDiscovery()
                .orElseThrow(DiscoveryDoesNotExistException::new);

        // Finds all UserRole Documents that the User has.  Each Document represents a Tenant Membership
        List<UserRolesDocument> allUsersTenantRoles = userRolesRepository.findAllTenantRolesByUsername(principal.getName());
        // Get the TenantIds of Each UserRoles Document; this represents the list of Tenants the User is a member of.
        // It is assumed that being a member of the Tenant is enough permission to be aware of its existence in teh Discovery list
        Set<String> usersTenantMemberships = allUsersTenantRoles.stream()
                .map(UserRolesDocument::tenantId)
                .collect(Collectors.toSet());

        // @TODO add configs for this value
        String tenantBasePath = "/taxii/tenant/";

        // Get all Tenants that the User is a member of
        List<TenantDocument> tenants = tenantRepository
                .findAllTenantsByTenantId(usersTenantMemberships.toArray(new String[0]));

        // For each Tenant, get the the Tenant Slug Value, and Append the TenantBase Path
        List<String> slugs = tenants.parallelStream()
                .map(t -> t.tenant().getTenantSlug())
                .map(s -> s = tenantBasePath + s)
                .collect(Collectors.toList());

        // Update the Discovery Resource data with the list of Tenant Roots that are relevant for the user
        TaxiiDiscoveryResource serverInfo = TaxiiDiscovery.copyOf(discoveryDocument.serverInfo()).withApiRoots(slugs);
        DiscoveryDocument newDoc = ImmutableDiscoveryDocument.copyOf(discoveryDocument).withServerInfo(serverInfo);
        tenantWebContext.setDatabaseToTenantIdForCurrentThread();

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(newDoc.serverInfo().toJson());
    }

}
