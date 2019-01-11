package io.digitalstate.taxii.endpoints;

import io.digitalstate.taxii.common.Headers;
import io.digitalstate.taxii.mongo.exceptions.TenantDoesNotExistException;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * API Roots are implemented as a concept of a tenant.  A tenant does not have to be "within the taxii server itself".
 * A TenantDocument is just a linking/mapping to a tenant in this taxii server or another.
 */
@Controller
@RequestMapping("/taxii/tenant")
public class Tenant{

    @Autowired
    private TenantRepository tenantRepository;

    @GetMapping("/{tenantSlug}")
    public ResponseEntity<String> getTenant(@RequestHeader HttpHeaders headers,
                                             @PathVariable("tenantSlug") String tenantSlug) throws TenantDoesNotExistException {

        //@TODO process headers for validation

        TenantDocument tenant = tenantRepository.findTenantBySlug(tenantSlug)
                .orElseThrow(() -> new TenantDoesNotExistException(tenantSlug));

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(tenant.toJson());
    }

}
