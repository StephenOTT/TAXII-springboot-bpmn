package io.digitalstate.taxii.camunda.delegates;

import io.digitalstate.stix.bundle.BundleableObject;
import io.digitalstate.stix.sdo.objects.AttackPattern;
import io.digitalstate.stix.sdo.objects.AttackPatternSdo;
import io.digitalstate.taxii.mongo.model.document.CollectionObjectDocument;
import io.digitalstate.taxii.mongo.model.document.ImmutableCollectionObjectDocument;
import io.digitalstate.taxii.mongo.repository.CollectionObjectRepository;
import io.digitalstate.taxii.mongo.repository.StatusRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component("processStixObject")
public class ProcessStixObject implements JavaDelegate {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CollectionObjectRepository collectionObjectRepository;

    public void execute(DelegateExecution execution) throws Exception {

        BundleableObject bundleableObject = Optional.ofNullable((BundleableObject) execution.getVariable("object"))
                .orElseThrow(()-> new IllegalStateException("object variable returned null"));


        //@TODO do some sort of processing action goes here.
        // On success of processing then we add the data into the DB and update
        // Add Transactional Rollback support

        String collectionId = execution.getVariable("collection_id").toString();
        String tenantId = execution.getVariable("tenant_id").toString();
        String statusId = execution.getVariable("status_id").toString();

        CollectionObjectDocument collectionObjectDocument = ImmutableCollectionObjectDocument.builder()
                .modifiedAt(Instant.now())
                .collectionId(collectionId)
                .tenantId(tenantId)
                .object(bundleableObject)
                .build();

        // @TODO add transactional error handling
        // @TODO look to transfer these into individual tasks:
        collectionObjectRepository.save(collectionObjectDocument);
        statusRepository.incrementSuccessCountWithPendingCountDecrement(1, statusId, tenantId);
        statusRepository.addSuccess(bundleableObject.getId(), statusId, tenantId);

    }

}