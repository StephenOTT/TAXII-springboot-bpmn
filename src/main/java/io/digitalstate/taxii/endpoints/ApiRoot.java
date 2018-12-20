package io.digitalstate.taxii.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.digitalstate.stix.bundle.Bundle;
import io.digitalstate.stix.sdo.objects.AttackPattern;
import io.digitalstate.taxii.models.apiroot.TaxiiApiRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/{apiRoot}")
public class ApiRoot {

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<String> getApiRoot(@RequestHeader HttpHeaders headers,
                                             @PathVariable("apiRoot") String apiRoot) throws JsonProcessingException {

        TaxiiApiRoot root1 = TaxiiApiRoot.builder()
                .url(apiRoot)
                .title("some root 1")
                .addVersions("taxii-2.0")
                .maxContentLength(1234567890)
                .build();

        HttpHeaders successHeaders = new HttpHeaders();
        successHeaders.add("content-type", "application/vnd.oasis.taxii+json");
        successHeaders.add("verison", "2.0");

        String successResponseString = objectMapper.writeValueAsString(root1);

        return ResponseEntity.ok()
                .headers(successHeaders)
                .body(successResponseString);
    }

}
