package io.digitalstate.taxii.camunda.delegates;

import io.digitalstate.taxii.camunda.exception.MissingVariableConfigException;
import io.digitalstate.taxii.model.discovery.TaxiiDiscovery;
import io.digitalstate.taxii.mongo.model.document.DiscoveryDocument;
import io.digitalstate.taxii.mongo.model.document.ImmutableDiscoveryDocument;
import io.digitalstate.taxii.mongo.repository.DiscoveryRepository;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.Spin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component("createDiscoveryDelegate")
public class CreateDiscoveryDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(CreateDiscoveryDelegate.class);

    @Autowired
    private DiscoveryRepository discoveryRepository;

    private static final String RESULT_VARIABLE_NAME = "resultVariable";

    public void execute(DelegateExecution execution) throws Exception {

        String title = Optional.ofNullable(execution.getVariableLocal("discoveryTitle").toString())
                .orElseThrow(()-> new MissingVariableConfigException(null,"discoveryTitle local variable cannot be found"));

        Optional<String> description = Optional.ofNullable(execution.getVariableLocal("discoveryDescription").toString());

        Optional<String> contact = Optional.ofNullable(execution.getVariableLocal("discoveryContact").toString());

        Optional<String> defaultApiRoot = Optional.ofNullable(execution.getVariableLocal("discoveryDefaultApiRoot").toString());

        String apiRootsRaw = Optional.ofNullable(execution.getVariableLocal("discoveryApiRoots").toString()).orElse("");
        List<String> apiRoots = Arrays.asList(apiRootsRaw.split("\\s*,\\s*"));

        DiscoveryDocument discoveryDocument = ImmutableDiscoveryDocument.builder()
                .serverInfo(TaxiiDiscovery.builder()
                        .title(title)
                        .description(description)
                        .contact(contact)
                        .defaultApiRoot(defaultApiRoot)
                        .addAllApiRoots(apiRoots)
                        .build())
                .build();

        try {
            DiscoveryDocument createdDiscovery = discoveryRepository.createDiscovery(discoveryDocument);

            if (execution.hasVariableLocal(RESULT_VARIABLE_NAME)){
                String targetVarName = execution.getVariable(RESULT_VARIABLE_NAME).toString();
                execution.setVariableLocal(targetVarName, Spin.JSON(createdDiscovery.toJson()));
            }

        } catch (DuplicateKeyException dk){
            logger.error("Duplicate Key was detected while trying to insert a User", dk);
            throw new BpmnError("existingDiscovery", "Existing Discovery was detected while trying to insert Discovery");

        } catch (IllegalArgumentException e){
            throw new IllegalStateException("Unable to convert Discovery Document into JSON", e);
        }
    }
}