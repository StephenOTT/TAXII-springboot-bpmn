package io.digitalstate.taxii.camunda.delegates;

import io.digitalstate.stix.bundle.BundleableObject;
import io.digitalstate.taxii.model.status.TaxiiStatusFailure;
import io.digitalstate.taxii.model.status.TaxiiStatusFailureResource;
import io.digitalstate.taxii.mongo.model.document.CollectionObjectDocument;
import io.digitalstate.taxii.mongo.model.document.ImmutableCollectionObjectDocument;
import io.digitalstate.taxii.mongo.repository.CollectionObjectRepository;
import io.digitalstate.taxii.mongo.repository.StatusRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProcessFailure implements JavaDelegate {

    @Autowired
    private StatusRepository statusRepository;

    public void execute(DelegateExecution execution) throws Exception {

        BundleableObject bundleableObject = Optional.ofNullable((BundleableObject) execution.getVariable("object"))
                .orElseThrow(()-> new IllegalStateException("object variable returned null"));

        String collectionId = execution.getVariable("collection_id").toString();
        String tenantId = execution.getVariable("tenant_id").toString();
        String statusId = execution.getVariable("status_id").toString();

        String defaultMessage = "Unable to process the object.";
        String message = Optional.ofNullable(execution.getVariableLocal("message"))
                .orElse(defaultMessage).toString();

        TaxiiStatusFailureResource statusFailure = TaxiiStatusFailure.builder()
                .id(bundleableObject.getId())
                .message(message)
                .build();

        // @TODO add transactional error handling
        // @TODO look to transfer these into individual tasks:
        statusRepository.incrementFailureCountWithPendingCountDecrement(1, statusId, tenantId);
        statusRepository.addFailure(statusFailure, statusId, tenantId);

    }

}