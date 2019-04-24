package io.digitalstate.taxii.endpoints;

import io.digitalstate.taxii.common.Headers;
import io.digitalstate.taxii.endpoints.context.TenantWebContext;
import io.digitalstate.taxii.exception.exceptions.CannotParseStatusUpdateParamsException;
import io.digitalstate.taxii.mongo.exceptions.StatusDoesNotExistException;
import io.digitalstate.taxii.mongo.exceptions.TenantDoesNotExistException;
import io.digitalstate.taxii.mongo.model.document.StatusDocument;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.repository.CollectionRepository;
import io.digitalstate.taxii.mongo.repository.StatusRepository;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;


@Controller
@RequestMapping("/taxii/tenant/{tenantSlug}/status")
public class Status {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private TenantWebContext tenantWebContext;

    @GetMapping("/{statusId}")
    @ResponseBody
    public ResponseEntity<String> getStatus(@RequestHeader HttpHeaders headers,
                                            @PathVariable("statusId") String statusId) {

        StatusDocument status = statusRepository.findStatusById(statusId, tenantWebContext.getTenantId())
                .orElseThrow(() -> new StatusDoesNotExistException(statusId));

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(status.toJson());
    }

    @PutMapping("/{statusId}/operations")
    @ResponseBody
    @Validated
    public ResponseEntity<String> updateStatus(@RequestHeader HttpHeaders headers,
                                               @PathVariable("tenantSlug") String tenantSlug,
                                               @PathVariable("statusId") String statusId,
                                               @RequestParam(value = "operator") @Size(max = 255) String operator,
                                               @RequestParam(value = "property") @Size(max = 255) String property,
                                               @RequestParam(value = "value") @Size(max = 255) String value) {

        //@TODO add operaiton vocab validation annotation
        // @TODO add validations for Values to be proper formats

        // @TODO review need for doing a collection id validation:
//        CollectionDocument collection = collectionRepository.findCollectionById(collectionId, tenant.tenant().getTenantId())
//                .orElseThrow(() -> new CollectionDoesNotExistException(collectionId));

        // @TODO Review for better handling: Likely create a unified operations method in the statusRepository so this switch case stuff is not needed.
        // @TODO Review for VASTELY better word cleanup.  This is messy.
        StatusDocument status;
        if (operator.equals("add")) {
            switch (property) {
                case "up_success_count_down_failure_count":
                    status = statusRepository.incrementSuccessCountWithFailureCountDecrement(Long.valueOf(value), statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                case "up_success_count_down_pending_count":
                    status = statusRepository.incrementSuccessCountWithPendingCountDecrement(Long.valueOf(value), statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                case "up_pending_count_down_failure_count":
                    status = statusRepository.incrementPendingCountWithFailureCountDecrement(Long.valueOf(value), statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                case "up_failure_count_down_pending_count":
                    status = statusRepository.incrementFailureCountWithPendingCountDecrement(Long.valueOf(value), statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                case "failure_count":
                    status = statusRepository.incrementFailureCount(Long.valueOf(value), statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                case "pending_count":
                    status = statusRepository.incrementPendingCount(Long.valueOf(value), statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                case "successes":
                    status = statusRepository.addSuccess(value, statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                // Removed because failures has a special sub-object
                // Further work is required if this endpoint is to remain
//                case "failures":
//                    status = statusRepository.addFailure(value, statusId, tenant.tenant().getTenantId())
//                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
//                    break;

                case "pendings":
                    status = statusRepository.addPending(value, statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                default:
                    throw new CannotParseStatusUpdateParamsException(null, "Unable to parse property value for add operation");
            }

        } else if (operator.equals("subtract")) {
            switch (property) {
                case "failure_count":
                    status = statusRepository.decrementFailureCount(Long.valueOf(value), statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                case "pending_count":
                    status = statusRepository.decrementPendingCount(Long.valueOf(value), statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                case "failures":
                    status = statusRepository.removeFailure(value, statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                case "pending":
                    status = statusRepository.removePending(value, statusId, tenantWebContext.getTenantId())
                            .orElseThrow(() -> new StatusDoesNotExistException(statusId));
                    break;

                default:
                    throw new CannotParseStatusUpdateParamsException(null, "Unable to parse property value for subtract operation");
            }

        } else {
            throw new CannotParseStatusUpdateParamsException(null, "Unable to parse operator");
        }

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(status.toJson());
    }
}
