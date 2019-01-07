package io.digitalstate.taxii.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.digitalstate.taxii.common.json.views.TaxiiSpecView;
import io.digitalstate.taxii.model.apiroot.TaxiiApiRoot;
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
public class ApiRoot {

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/{tenantId}")
    @ResponseBody
    public ResponseEntity<String> getTenant(@RequestHeader HttpHeaders headers,
                                             @PathVariable("tenantId") String tenantId) throws JsonProcessingException {

        TaxiiApiRoot root1 = TaxiiApiRoot.builder()
                .tenantId(tenantId)
                .tenantSlug("dog")
                .title("some root 1")
                .addVersions("taxii-2.0")
                .maxContentLength(1234567890)
                .build();

        HttpHeaders successHeaders = new HttpHeaders();
        successHeaders.add("content-type", "application/vnd.oasis.taxii+json");
        successHeaders.add("verison", "2.0");

        String successResponseString = objectMapper.writerWithView(TaxiiSpecView.class).writeValueAsString(root1);

        return ResponseEntity.ok()
                .headers(successHeaders)
                .body(successResponseString);
    }

}
