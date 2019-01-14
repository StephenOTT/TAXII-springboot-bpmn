package io.digitalstate.taxii.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.digitalstate.taxii.common.Headers;
import io.digitalstate.taxii.model.discovery.TaxiiDiscovery;
import io.digitalstate.taxii.model.discovery.TaxiiDiscoveryResource;
import io.digitalstate.taxii.mongo.exceptions.DiscoveryDoesNotExistException;
import io.digitalstate.taxii.mongo.model.document.DiscoveryDocument;
import io.digitalstate.taxii.mongo.model.document.ImmutableDiscoveryDocument;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.repository.DiscoveryRepository;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class Discovery{

    @Autowired
    private DiscoveryRepository discoveryRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/taxii")
    @ResponseBody
    public ResponseEntity<String> getDiscovery(@RequestHeader HttpHeaders headers)
            throws JsonProcessingException {

        DiscoveryDocument discoveryDocument = discoveryRepository.findDiscovery()
                .orElseThrow(DiscoveryDoesNotExistException::new);

        // @TODO add configs for this value
        String tenantPath = servletContext.getContextPath() + "/taxii/tenant/";

        List<TenantDocument> tenants = tenantRepository.findAllTenantsByFilter(null);
        List<String> slugs = tenants.parallelStream()
                .map(t->t.tenant().getTenantSlug())
                .map(s-> s = tenantPath + s)
                .collect(Collectors.toList());

        TaxiiDiscoveryResource serverInfo = TaxiiDiscovery.copyOf(discoveryDocument.serverInfo()).withApiRoots(slugs);
        DiscoveryDocument newDoc = ImmutableDiscoveryDocument.copyOf(discoveryDocument).withServerInfo(serverInfo);

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(newDoc.toJson());
    }

}
