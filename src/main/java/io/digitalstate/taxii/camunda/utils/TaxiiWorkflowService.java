package io.digitalstate.taxii.camunda.utils;

import com.fasterxml.jackson.annotation.JsonValue;
import io.digitalstate.stix.bundle.BundleObject;
import io.digitalstate.taxii.camunda.exception.CannotStartProcessInstanceException;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.spin.DataFormats;
import org.camunda.spin.Spin;
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.plugin.variable.SpinValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaxiiWorkflowService {

    //@TODO create a global config for this variable
    private String OBJECTS_SUBMISSION_PROCESS_DEFINITION_KEY = "ObjectsSubmissionProcessing";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    public ProcessInstanceWithVariables createObjectsSubmission(@NotNull String bundleString, @NotNull String tenantId, @NotNull String collectionId){
        Map<String, Object> processVariables = new HashMap<>();

        //@TODO Add try catch with custom error handling for bad JSON Format
        SpinJsonNode json = Spin.S(bundleString, DataFormats.json());
//        JsonValue json = SpinValues.jsonValue(bundleString).create();


        //@TODO Create a global config variable for the name of this variable
        processVariables.put("bundleJson", json);

        processVariables.put("tenant_id", tenantId);
        processVariables.put("collection_id", collectionId);

        try {
            ProcessInstanceWithVariables processInstance = runtimeService.createProcessInstanceByKey(OBJECTS_SUBMISSION_PROCESS_DEFINITION_KEY)
                    .setVariables(processVariables)
                    .executeWithVariablesInReturn();

            return processInstance;

        } catch (Exception e){
            throw new CannotStartProcessInstanceException(e, "Unable to Start Process Instance");
        }
    }
}
