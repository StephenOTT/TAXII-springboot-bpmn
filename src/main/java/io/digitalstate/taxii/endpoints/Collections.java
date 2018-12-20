package io.digitalstate.taxii.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.digitalstate.stix.bundle.Bundle;
import io.digitalstate.stix.sdo.objects.AttackPattern;
import io.digitalstate.taxii.models.collections.TaxiiCollection;
import io.digitalstate.taxii.models.status.TaxiiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("{apiRoot}/collections")
public class Collections {

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<String> getCollections( @RequestHeader HttpHeaders headers,
                                                  @PathVariable("apiRoot") String apiRoot) throws JsonProcessingException {

        TaxiiCollection collection1 = TaxiiCollection.builder()
                .id("123")
                .title("some Title of a collection")
                .canRead(true)
                .canWrite(false)
                .build();

        TaxiiCollection collection2 = TaxiiCollection.builder()
                .id("321")
                .title("some Title of some other collection")
                .canRead(true)
                .canWrite(true)
                .build();

        Set<TaxiiCollection> taxiiCollections = new HashSet<>();
        taxiiCollections.add(collection1);
        taxiiCollections.add(collection2);

        Map<String, Object> map = new HashMap<>();
        map.put("collections", taxiiCollections );

        String response = objectMapper.writeValueAsString(map);

        HttpHeaders successHeaders = new HttpHeaders();
        successHeaders.add("content-type", "application/vnd.oasis.taxii+json");
        successHeaders.add("verison", "2.0");

        return ResponseEntity.ok()
                .headers(successHeaders)
                .body(response);
    }


    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> getCollection( @RequestHeader HttpHeaders headers,
                                                 @PathVariable("id") String id,
                                                 @PathVariable("apiRoot") String apiRoot) throws JsonProcessingException {

        TaxiiCollection collection1 = TaxiiCollection.builder()
                .id(id)
                .title("some Title of a collection")
                .canRead(true)
                .canWrite(false)
                .build();
        String response = objectMapper.writeValueAsString(collection1);

        HttpHeaders successHeaders = new HttpHeaders();
        successHeaders.add("content-type", "application/vnd.oasis.taxii+json");
        successHeaders.add("verison", "2.0");

        return ResponseEntity.ok()
                .headers(successHeaders)
                .body(response);
    }

    @GetMapping("/{id}/objects")
    @ResponseBody
    public ResponseEntity<String> getCollectionObjects( @RequestHeader HttpHeaders headers,
                                                 @PathVariable("id") String id,
                                                 @PathVariable("apiRoot") String apiRoot) {

        AttackPattern attackPattern = AttackPattern.builder()
                .name("my attack pattern")
                .build();
        Bundle bundle = Bundle.builder()
                .addObjects(attackPattern)
                .build();

        HttpHeaders successHeaders = new HttpHeaders();
        successHeaders.add("content-type", "application/vnd.oasis.taxii+json");
        successHeaders.add("verison", "2.0");

        String successResponseString = bundle.toJsonString();

        return ResponseEntity.ok()
                .headers(successHeaders)
                .body(successResponseString);
    }

    @PostMapping("/{id}/objects")
    @ResponseBody
    public ResponseEntity<String> addCollectionObjects( @RequestHeader HttpHeaders headers,
                                                        @PathVariable("id") String id,
                                                        @PathVariable("apiRoot") String apiRoot,
                                                        @RequestBody Bundle requestBody) throws JsonProcessingException {
        // Create alternate handling option where Bundle is not pre-parsed and then sent into Camunda.
        // This alternate handling is interesting for use cases where manual adjustments are required.
        // Use a query param to use the proper end point

        //@TODO send bundle into BPMN instance for processing. Use Sync processing to push enough of the data
        // in order to perform the required secondary query.  Also consider having Camunda return all of the required variables? (likely not)

        //@TODO Run query on Camunda History DB to get Status
        // Return a taxii status based on Camunda DB info
        TaxiiStatus taxiiStatus = TaxiiStatus.builder()
                .id("some status id")
                .status("pending")
                .totalCount(2)
                .successCount(1)
                .failureCount(0)
                .pendingCount(1)
                .build();

        HttpHeaders successHeaders = new HttpHeaders();
        successHeaders.add("content-type", "application/vnd.oasis.taxii+json");
        successHeaders.add("verison", "2.0");

        String successResponseString = objectMapper.writeValueAsString(taxiiStatus);

        return ResponseEntity.ok()
                .headers(successHeaders)
                .body(successResponseString);
    }


}
