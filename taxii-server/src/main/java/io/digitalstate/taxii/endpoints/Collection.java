package io.digitalstate.taxii.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.digitalstate.stix.bundle.BundleObject;
import io.digitalstate.stix.json.StixParsers;
import io.digitalstate.taxii.camunda.exception.VariablesReturnedByProcessInstanceException;
import io.digitalstate.taxii.camunda.utils.TaxiiWorkflowService;
import io.digitalstate.taxii.common.Headers;
import io.digitalstate.taxii.endpoints.context.TenantWebContext;
import io.digitalstate.taxii.exception.exceptions.CannotParseBundleStringException;
import io.digitalstate.taxii.mongo.JsonUtil;
import io.digitalstate.taxii.mongo.exceptions.CollectionDoesNotExistException;
import io.digitalstate.taxii.mongo.exceptions.CollectionObjectDoesNotExistException;
import io.digitalstate.taxii.mongo.model.document.CollectionDocument;
import io.digitalstate.taxii.mongo.model.document.CollectionObjectDocument;
import io.digitalstate.taxii.mongo.model.document.StatusDocument;
import io.digitalstate.taxii.mongo.repository.CollectionObjectRepository;
import io.digitalstate.taxii.mongo.repository.CollectionRepository;
import io.digitalstate.taxii.mongo.repository.StatusRepository;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/taxii/tenant/{tenantSlug}")
public class Collection {

    @Autowired
    private TenantWebContext tenantWebContext;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionObjectRepository collectionObjectRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TaxiiWorkflowService workflowService;

    @GetMapping("/collections")
    @PreAuthorize("hasRole('ROLE_COLLECTIONS_VIEWER')")
    @ResponseBody
    public ResponseEntity<String> getAllCollections(@RequestHeader HttpHeaders headers,
                                                    @PathVariable("tenantSlug") String tenantSlug) throws JsonProcessingException {

        List<CollectionDocument> collections = collectionRepository.findAllCollections(tenantWebContext.getTenantId());

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(JsonUtil.ListToJson(collections));
    }


    @GetMapping("/collections/{collectionId}")
    @PreAuthorize("hasRole('ROLE_COLLECTION_VIEWER')")
    @ResponseBody
    public ResponseEntity<String> getCollection(@RequestHeader HttpHeaders headers,
                                                @PathVariable("collectionId") String collectionId) {

        CollectionDocument collection = collectionRepository.findCollectionById(collectionId, tenantWebContext.getTenantId())
                .orElseThrow(() -> new CollectionDoesNotExistException(collectionId));

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(collection.toJson());
    }

    @GetMapping("/collections/{collectionId}/objects")
    @PreAuthorize("hasRole('ROLE_COLLECTION_OBJECTS_VIEWER')")
    @ResponseBody
    public ResponseEntity<String> getCollectionObjects(@RequestHeader HttpHeaders headers,
                                                       @PathVariable("collectionId") String collectionId,
                                                       @RequestParam(name = "page", defaultValue = "0") Integer page) {

        CollectionDocument collection = collectionRepository.findCollectionById(collectionId, tenantWebContext.getTenantId())
                .orElseThrow(() -> new CollectionDoesNotExistException(collectionId));

        List<CollectionObjectDocument> objects = collectionObjectRepository.findAllObjectsByCollectionId(
                collection.collection().getId(),
                tenantWebContext.getTenantId(),
                PageRequest.of(page, 100)).getContent();

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(JsonUtil.ListToJson(objects));
    }


    @GetMapping("/collections/{collectionId}/objects/{objectId}")
    @PreAuthorize("hasRole('ROLE_COLLECTION_OBJECT_VIEWER')")
    @ResponseBody
    public ResponseEntity<String> getCollectionObject(@RequestHeader HttpHeaders headers,
                                                      @PathVariable("collectionId") String collectionId,
                                                      @PathVariable("objectId") String objectId) {

        CollectionDocument collection = collectionRepository.findCollectionById(collectionId, tenantWebContext.getTenantId())
                .orElseThrow(() -> new CollectionDoesNotExistException(collectionId));

        //@TODO setup .map to only return the inner objects which is the spec.
        List<CollectionObjectDocument> objects = collectionObjectRepository.findObjectByObjectId(objectId,
                collection.collection().getId(),
                tenantWebContext.getTenantId());

        if (objects.size() == 0) {
            throw new CollectionObjectDoesNotExistException(collectionId, objectId);
        } else {
            return ResponseEntity.ok()
                    .headers(Headers.getSuccessHeaders())
                    .body(JsonUtil.ListToJson(objects));
        }
    }


    @PostMapping("/collections/{collectionId}/objects")
    @PreAuthorize("hasRole('ROLE_COLLECTION_OBJECTS_CREATOR')")
    @ResponseBody
    public ResponseEntity<String> addCollectionObjects(@RequestHeader HttpHeaders headers,
                                                       @PathVariable("collectionId") String collectionId,
                                                       @RequestBody String requestBody) {

        CollectionDocument collection = collectionRepository.findCollectionById(collectionId, tenantWebContext.getTenantId())
                .orElseThrow(() -> new CollectionDoesNotExistException(collectionId));

        //@TODO Review for using a stream and async as processing could be large sets of data.
        BundleObject bundle;
        try {
            bundle = StixParsers.parseBundle(requestBody);
        } catch (IOException e) {
            throw new CannotParseBundleStringException(e);
        }

        //@TODO Review for using a stream and async as processing could be large sets of data.
        ProcessInstanceWithVariables workflowSubmission = workflowService.createObjectsSubmission(bundle.toJsonString(),
                tenantWebContext.getTenantId(), collection.collection().getId());

        StatusDocument statusDocument = Optional.ofNullable(workflowSubmission.getVariables()
                .getValue("original_status_document", StatusDocument.class))
                .orElseThrow(() -> new VariablesReturnedByProcessInstanceException(null, "original_status_document variable was null or did not exist in the variables returned by process instance start."));

//        try {
        //@TODO update counts to become lazy set through lookup into Camunda
        //@TODO Add error handling that will remove the ProcessInstance if Save Document Fails
//            statusRepository.save(statusDocument);

        return ResponseEntity.accepted()
                .headers(Headers.getSuccessHeaders())
                .body(statusDocument.toJson());

//        }catch (Exception e){
//            throw new CannotCreateStatusDocumentException(e);
//        }
    }
}
