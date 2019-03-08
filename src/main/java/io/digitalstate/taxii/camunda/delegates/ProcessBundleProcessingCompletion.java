package io.digitalstate.taxii.camunda.delegates;

import io.digitalstate.stix.bundle.BundleableObject;
import io.digitalstate.taxii.camunda.exception.MissingVariableConfigException;
import io.digitalstate.taxii.mongo.exceptions.CannotUpdateStatusException;
import io.digitalstate.taxii.mongo.repository.StatusRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("processBundleProcessingCompletion")
public class ProcessBundleProcessingCompletion implements JavaDelegate {

    @Autowired
    private StatusRepository statusRepository;

    public void execute(DelegateExecution execution) throws Exception {

        String collectionId = execution.getVariable("collection_id").toString();
        String tenantId = execution.getVariable("tenant_id").toString();
        String statusId = execution.getVariable("status_id").toString();

        String newStatus = Optional.ofNullable(execution.getVariableLocal("status").toString())
                .orElseThrow(() -> new MissingVariableConfigException(null, "Variable [status] was missing or return null"));

        boolean update = statusRepository.updateStatusValue(newStatus, statusId, tenantId);
        if (!update){
            throw new CannotUpdateStatusException(statusId,"Unable to update the Status' Status value with new Status: " + newStatus);
        }
    }

}