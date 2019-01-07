package io.digitalstate.taxii.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.digitalstate.taxii.common.json.views.TaxiiSpecView;
import io.digitalstate.taxii.models.apiroot.TaxiiApiRoot;
import io.digitalstate.taxii.models.discovery.TaxiiDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/taxii")
public class Discovery {

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    @ResponseBody
    public ResponseEntity<String> getDiscovery(@RequestHeader HttpHeaders headers) throws JsonProcessingException {

        TaxiiApiRoot root1 = TaxiiApiRoot.builder()
                .tenantId("123")
                .tenantSlug("root1")
                .title("some root 1")
                .addVersions("taxii-2.0")
                .maxContentLength(1234567890)
                .build();

        TaxiiApiRoot root2 = TaxiiApiRoot.builder()
                .tenantId("1234")
                .tenantSlug("root2")
                .title("some root 2")
                .addVersions("taxii-2.0")
                .maxContentLength(1234567890)
                .build();

        TaxiiApiRoot root3 = TaxiiApiRoot.builder()
                .tenantId("12345")
                .tenantSlug("root3")
                .title("some root 3")
                .addVersions("taxii-2.0")
                .maxContentLength(1234567890)
                .build();

        Set<TaxiiApiRoot> roots = new HashSet<>();
        roots.add(root1);
        roots.add(root2);
        roots.add(root3);

        Set<String> rootStrings = roots.stream().map(TaxiiApiRoot::getTenantSlug).collect(Collectors.toSet());
        String defaultRoot = root1.getTenantSlug(); // Consider adding a "default" flag to a root to mark that it should be used as a default.  Could also be setup as a config in application.yaml

        TaxiiDiscovery discovery = TaxiiDiscovery.builder()
                .title("Some Taxi Server")
                .description("some description")
                .defaultApiRoot(defaultRoot)
                .addAllApiRoots(rootStrings)
                .build();

        String response = objectMapper.writerWithView(TaxiiSpecView.class).writeValueAsString(discovery);

        HttpHeaders successHeaders = new HttpHeaders();
        successHeaders.add("content-type", "application/vnd.oasis.taxii+json");
        successHeaders.add("version", "2.0");

        return ResponseEntity.ok()
                .headers(successHeaders)
                .body(response);
    }

}
