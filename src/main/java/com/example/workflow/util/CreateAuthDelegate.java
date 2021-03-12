package com.example.workflow.util;


import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named("createAuth")
public class CreateAuthDelegate  implements JavaDelegate {

    public void execute(final DelegateExecution execution) throws Exception {
        final String userName = (String)execution.getVariable("userName");
        final String password = (String)execution.getVariable("password");
        final AuthorizationService authorizationService = execution.getProcessEngineServices().getAuthorizationService();
        final IdentityService identityService = execution.getProcessEngineServices().getIdentityService();
        final AuthorizationEntity userTasklistAuth = new AuthorizationEntity(1);
        userTasklistAuth.setUserId(userName);
        userTasklistAuth.addPermission((Permission)Permissions.ACCESS);
        userTasklistAuth.setResourceId("tasklist");
        userTasklistAuth.setResource((Resource)Resources.APPLICATION);
        authorizationService.saveAuthorization((Authorization)userTasklistAuth);
        final AuthorizationEntity userCockpitAuth = new AuthorizationEntity(1);
        userCockpitAuth.setUserId(userName);
        userCockpitAuth.addPermission((Permission)Permissions.ACCESS);
        userCockpitAuth.setResourceId("cockpit");
        userCockpitAuth.setResource((Resource)Resources.APPLICATION);
        authorizationService.saveAuthorization((Authorization)userCockpitAuth);
        for (final Resource resource : Resources.values()) {
            if (resource != Resources.APPLICATION && resource != Resources.AUTHORIZATION && resource != Resources.TENANT && resource != Resources.TENANT_MEMBERSHIP && resource != Resources.USER && resource != Resources.GROUP && resource != Resources.GROUP_MEMBERSHIP) {
                final AuthorizationEntity userAdminAuth = new AuthorizationEntity(1);
                userAdminAuth.setUserId(userName);
                userAdminAuth.setResource(resource);
                userAdminAuth.setResourceId("*");
                userAdminAuth.addPermission((Permission)Permissions.ALL);
                authorizationService.saveAuthorization((Authorization)userAdminAuth);
            }
        }
        final AuthorizationEntity userCreateProcessAuth = new AuthorizationEntity(2);
        userCreateProcessAuth.setUserId(userName);
        userCreateProcessAuth.setResource((Resource)Resources.PROCESS_DEFINITION);
        userCreateProcessAuth.setResourceId("UserAndTenantCreator");
        userCreateProcessAuth.removePermission((Permission)Permissions.ALL);
        authorizationService.saveAuthorization((Authorization)userCreateProcessAuth);
    }
}
