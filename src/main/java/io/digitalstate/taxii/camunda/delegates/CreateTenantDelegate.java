package io.digitalstate.taxii.camunda.delegates;

import io.digitalstate.taxii.camunda.exception.MissingVariableConfigException;
import io.digitalstate.taxii.model.tenant.TaxiiTenant;
import io.digitalstate.taxii.mongo.model.document.ImmutableTenantDocument;
import io.digitalstate.taxii.mongo.model.document.TenantDocument;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
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

@Component("createTenantDelegate")
public class CreateTenantDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(CreateTenantDelegate.class);

    @Autowired
    private TenantRepository tenantRepository;

    private static final String RESULT_VARIABLE_NAME = "resultVariable";

    public void execute(DelegateExecution execution) throws Exception {

        String tenantId = Optional.ofNullable(execution.getVariableLocal("tenantId").toString())
                .orElseThrow(()-> new MissingVariableConfigException(null,"tenantId local variable cannot be found"));

        String tenantSlug = Optional.ofNullable(execution.getVariableLocal("tenantSlug").toString())
                .orElseThrow(()-> new MissingVariableConfigException(null,"tenantSlug local variable cannot be found"));

        String tenantTitle = Optional.ofNullable(execution.getVariableLocal("tenantTitle").toString())
                .orElseThrow(()-> new MissingVariableConfigException(null,"tenantTitle local variable cannot be found"));

        String tenantDescription = Optional.ofNullable(execution.getVariableLocal("tenantDescription").toString())
                .orElseThrow(()-> new MissingVariableConfigException(null,"tenantDescription local variable cannot be found"));

        String tenantSupportedVersionsRaw = Optional.ofNullable(execution.getVariableLocal("tenantSupportedVersions").toString())
                .orElseThrow(()-> new MissingVariableConfigException(null,"tenantSupportedVersions local variable cannot be found"));

        List<String> tenantSupportedVersions = Arrays.asList(tenantSupportedVersionsRaw.split("\\s*,\\s*"));

        long tenantMaxContentLength = Optional.ofNullable((Long)execution.getVariableLocal("tenantMaxContentLength"))
                .orElseThrow(()-> new MissingVariableConfigException(null,"tenantMaxContentLength local variable cannot be found"));

        TenantDocument tenantDocument = ImmutableTenantDocument.builder()
                .tenant(TaxiiTenant.builder()
                        .tenantId(tenantId)
                        .tenantSlug(tenantSlug)
                        .title(tenantTitle)
                        .description(tenantDescription)
                        .addAllVersions(tenantSupportedVersions)
                        .maxContentLength(tenantMaxContentLength)
                        .build())
                .build();

        try {
            TenantDocument createdTenant = tenantRepository.createTenant(tenantDocument);

            if (execution.hasVariableLocal(RESULT_VARIABLE_NAME)){
                String targetVarName = execution.getVariable(RESULT_VARIABLE_NAME).toString();
                execution.setVariableLocal(targetVarName, Spin.JSON(createdTenant.toJson()));
            }

        } catch (DuplicateKeyException dk){
            logger.error("Duplicate Key was detected while trying to insert a Tenant", dk);
            throw new BpmnError("existingTenant", "Existing Tenant was detected while trying to insert Tenant");

        } catch (IllegalArgumentException e){
            throw new IllegalStateException("Unable to convert Tenant Document into JSON", e);
        }
    }
}