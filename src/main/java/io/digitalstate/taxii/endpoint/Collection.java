package io.digitalstate.taxii.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.digitalstate.taxii.common.Headers;
import io.digitalstate.taxii.mongo.exception.CollectionDoesNotExistException;
import io.digitalstate.taxii.mongo.exception.TenantDoesNotExistException;
import io.digitalstate.taxii.mongo.JsonUtils;
import io.digitalstate.taxii.mongo.model.document.CollectionDocument;
import io.digitalstate.taxii.mongo.model.document.CollectionObjectDocument;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.repository.CollectionObjectRepository;
import io.digitalstate.taxii.mongo.repository.CollectionRepository;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/taxii/tenant/{tenantSlug}")
public class Collection {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionObjectRepository collectionObjectRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @GetMapping("/collections")
    @ResponseBody
    public ResponseEntity<String> getAllCollections(@RequestHeader HttpHeaders headers,
                                                    @PathVariable("tenantSlug") String tenantSlug) throws JsonProcessingException {

        TenantDocument tenant = tenantRepository.findTenantBySlug(tenantSlug)
                .orElseThrow(() -> new TenantDoesNotExistException(tenantSlug));

        List<CollectionDocument> collections = collectionRepository.findAllCollectionsByTenantId(tenant.tenant().getTenantId());

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(JsonUtils.ListToJson(collections));
    }


    @GetMapping("/collections/{collectionId}")
    @ResponseBody
    public ResponseEntity<String> getCollection(@RequestHeader HttpHeaders headers,
                                                @PathVariable("collectionId") String collectionId,
                                                @PathVariable("tenantSlug") String tenantSlug) {

        TenantDocument tenant = tenantRepository.findTenantBySlug(tenantSlug)
                .orElseThrow(() -> new TenantDoesNotExistException(tenantSlug));

        CollectionDocument collection = collectionRepository.findCollectionById(collectionId, tenant.tenant().getTenantId())
                .orElseThrow(() -> new CollectionDoesNotExistException(collectionId));

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(collection.toJson());
    }

    @GetMapping("/collections/{collectionId}/objects")
    @ResponseBody
    public ResponseEntity<String> getCollectionObjects(@RequestHeader HttpHeaders headers,
                                                       @PathVariable("collectionId") String collectionId,
                                                       @PathVariable("tenantSlug") String tenantSlug,
                                                       @RequestParam(name = "page", defaultValue = "0") Integer page) {

        TenantDocument tenant = tenantRepository.findTenantBySlug(tenantSlug)
                .orElseThrow(() -> new TenantDoesNotExistException(tenantSlug));

        CollectionDocument collection = collectionRepository.findCollectionById(collectionId, tenant.tenant().getTenantId())
                .orElseThrow(() -> new CollectionDoesNotExistException(collectionId));

        List<CollectionObjectDocument> objects =
                collectionObjectRepository.findAllObjectsByCollectionId(
                        collection.collection().getId(),
                        tenant.tenant().getTenantId(),
                        PageRequest.of(page, 100)).getContent();

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(JsonUtils.ListToJson(objects));
    }
//
//    @PostMapping("/{id}/objects")
//    @ResponseBody
//    public ResponseEntity<String> addCollectionObjects( @RequestHeader HttpHeaders headers,
//                                                        @PathVariable("id") String id,
//                                                        @PathVariable("tenantId") String tenantId,
//                                                        @RequestBody Bundle requestBody) throws JsonProcessingException {
//        // Create alternate handling option where Bundle is not pre-parsed and then sent into Camunda.
//        // This alternate handling is interesting for use cases where manual adjustments are required.
//        // Use a query param to use the proper end point
//
//        //@TODO send bundle into BPMN instance for processing. Use Sync processing to push enough of the data
//        // in order to perform the required secondary query.  Also consider having Camunda return all of the required variables? (likely not)
//
//        //@TODO Run query on Camunda History DB to get Status
//        // Return a taxii status based on Camunda DB info
//        TaxiiStatus taxiiStatus = TaxiiStatus.builder()
//                .id("some status id")
//                .status("pending")
//                .totalCount(2)
//                .successCount(1)
//                .failureCount(0)
//                .pendingCount(1)
//                .build();
//
//        HttpHeaders successHeaders = new HttpHeaders();
//        successHeaders.add("content-type", "application/vnd.oasis.taxii+json");
//        successHeaders.add("verison", "2.0");
//
//        String successResponseString = objectMapper.writerWithView(TaxiiSpecView.class).writeValueAsString(taxiiStatus);
//
//        return ResponseEntity.ok()
//                .headers(successHeaders)
//                .body(successResponseString);
//    }


}
