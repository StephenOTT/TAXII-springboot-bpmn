package io.digitalstate.taxii.camunda.delegates;

import io.digitalstate.stix.bundle.BundleObject;
import io.digitalstate.stix.json.StixParsers;
import io.digitalstate.taxii.model.status.TaxiiStatus;
import io.digitalstate.taxii.model.status.TaxiiStatusResource;
import io.digitalstate.taxii.mongo.model.document.ImmutableStatusDocument;
import io.digitalstate.taxii.mongo.model.document.StatusDocument;
import io.digitalstate.taxii.mongo.repository.StatusRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.json.SpinJsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component("prepBundle")
public class PrepBundle implements JavaDelegate {

    @Autowired
    private StatusRepository statusRepository;

    public void execute(DelegateExecution execution) throws Exception {

        String bundleJsonVarName = "bundleJson";
        SpinJsonNode json = SpinJsonNode.JSON(execution.getVariable(bundleJsonVarName));

        BundleObject bundle = StixParsers.parseBundle(json.toString());
//       ObjectValue bundleObjectValue =  Variables.objectValue(bundle).serializationDataFormat(Variables.SerializationDataFormats.JAVA).create();
        execution.setVariable("bundleObject", bundle);

        Long objectCount = (long) bundle.getObjects().size();

        execution.setVariable("originalObjectCount", objectCount);

        Instant createdTimestamp = Instant.now();

        //@TODO turn into optionals with error handling
        String collectionId = execution.getVariable("collection_id").toString();
        String tenantId = execution.getVariable("tenant_id").toString();

        // @TODO add try catch handling
        TaxiiStatusResource taxiiStatusResource = TaxiiStatus.builder()
                .status("pending")
                .requestTimestamp(createdTimestamp)
                .totalCount(objectCount)
                .successCount(0L)
                .failureCount(0L)
                .pendingCount(objectCount)
                .build();

        StatusDocument statusDocument = ImmutableStatusDocument.builder()
                .createdAt(createdTimestamp)
                .modifiedAt(createdTimestamp)
                .tenantId(tenantId)
                .collectionId(collectionId)
                .processInstanceId(execution.getProcessInstanceId())
                .lastReportedStatus("active")
                .statusResource(taxiiStatusResource)
                .build();

        statusRepository.createStatus(statusDocument, statusDocument.tenantId());

        execution.setVariable("status_id", taxiiStatusResource.getId());
        execution.setVariable("original_status_document", statusDocument);

    }

}