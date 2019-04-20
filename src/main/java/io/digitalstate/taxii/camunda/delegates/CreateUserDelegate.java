package io.digitalstate.taxii.camunda.delegates;

import io.digitalstate.taxii.camunda.exception.MissingVariableConfigException;
import io.digitalstate.taxii.mongo.exceptions.TenantDoesNotExistException;
import io.digitalstate.taxii.mongo.model.document.ImmutableUserDocument;
import io.digitalstate.taxii.mongo.model.document.UserDocument;
import io.digitalstate.taxii.mongo.repository.TenantRepository;
import io.digitalstate.taxii.mongo.repository.UserRepository;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.Spin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("createUserDelegate")
public class CreateUserDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserDelegate.class);

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String RESULT_VARIABLE_NAME = "resultVariable";

    public void execute(DelegateExecution execution) throws Exception {

        String tenantId = Optional.ofNullable(execution.getVariableLocal("tenantId").toString())
                .orElseThrow(()-> new MissingVariableConfigException(null,"tenantId local variable cannot be found"));

        // Validate that the Tenant exists
        tenantRepository.findTenantByTenantId(tenantId)
                .orElseThrow(()-> new TenantDoesNotExistException(tenantId));

        String username = Optional.ofNullable(execution.getVariableLocal("username").toString())
                .orElseThrow(()-> new MissingVariableConfigException(null,"tenantId local variable cannot be found"));

        UserDocument userDocument = ImmutableUserDocument.builder()
                //@TODO Fix tenant assignment with new tenantMemberships
//                .tenantId(tenantId)
                .username(username)
                //@TODO Add password support
                .build();

        try {
            UserDocument createdUser = userRepository.createUser(userDocument, userDocument.tenantId());

            if (execution.hasVariableLocal(RESULT_VARIABLE_NAME)){
                String targetVarName = execution.getVariable(RESULT_VARIABLE_NAME).toString();
                execution.setVariableLocal(targetVarName, Spin.JSON(createdUser.toJson()));
            }

        } catch (DuplicateKeyException dk){
            logger.error("Duplicate Key was detected while trying to insert a User", dk);
            throw new BpmnError("existingUser", "Existing User was detected while trying to insert User");

        } catch (IllegalArgumentException e){
            throw new IllegalStateException("Unable to convert User Document into JSON", e);
        }
    }
}