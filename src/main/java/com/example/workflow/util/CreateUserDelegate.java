package com.example.workflow.util;
import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named("createUser")
public class CreateUserDelegate implements JavaDelegate {

    public void execute(final DelegateExecution execution) throws Exception {
        final String firstName = (String) execution.getVariable("firstName");
        final String secondName = (String) execution.getVariable("secondName");
        final String userName = (String) execution.getVariable("userName");
        final String password = (String) execution.getVariable("password");
        final String email = (String) execution.getVariable("emailAddress");

        final IdentityService identityService = execution.getProcessEngineServices().getIdentityService();
        final User user = identityService.newUser(userName);
        user.setFirstName(firstName);
        user.setLastName(secondName);
        user.setPassword(password);
        user.setEmail(email);
        identityService.saveUser(user);
        final Tenant tenant = identityService.newTenant(userName);
        identityService.saveTenant(tenant);
        identityService.createTenantUserMembership(userName, userName);
    }
}
