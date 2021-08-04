package com.example.workflow.util;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.multitenancy.TenantIdProvider;
import org.camunda.bpm.engine.impl.cfg.multitenancy.TenantIdProviderCaseInstanceContext;
import org.camunda.bpm.engine.impl.cfg.multitenancy.TenantIdProviderHistoricDecisionInstanceContext;
import org.camunda.bpm.engine.impl.cfg.multitenancy.TenantIdProviderProcessInstanceContext;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Component
@Order(Ordering.DEFAULT_ORDER + 1)
public class MyCustomConfiguration implements ProcessEnginePlugin {
    private static final Logger LOG = LoggerFactory.getLogger(MyCustomConfiguration.class);

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        LOG.info("pre init");
        processEngineConfiguration.setTenantIdProvider(new TenantIdProvider() {
            @Override
            public String provideTenantIdForProcessInstance(TenantIdProviderProcessInstanceContext ctx) {
                LOG.info("tenant id fro process instance");
                return getTenantIdOfCurrentAuthentication();
            }
            @Override
            public String provideTenantIdForHistoricDecisionInstance(TenantIdProviderHistoricDecisionInstanceContext ctx) {
                LOG.info("Temant Id for historic decision");
                return getTenantIdOfCurrentAuthentication();
            }
            @Override
            public String provideTenantIdForCaseInstance(TenantIdProviderCaseInstanceContext ctx) {
                LOG.info("Tenant id for case instance");
                return getTenantIdOfCurrentAuthentication();
            }

            protected String getTenantIdOfCurrentAuthentication() {

                IdentityService identityService = Context.getProcessEngineConfiguration().getIdentityService();
                Authentication currentAuthentication = identityService.getCurrentAuthentication();

                if (currentAuthentication != null) {

                    List<String> tenantIds = currentAuthentication.getTenantIds();
                    if (tenantIds.size() == 1) {
                        return tenantIds.get(0);

                    //} else if (tenantIds.isEmpty()) {
                       // throw new IllegalStateException("no authenticated tenant");


                    } else {
                        throw new IllegalStateException("more than one authenticated tenant");
                    }

                } else {
                    throw new IllegalStateException("no authentication");
                }
            }
        });
    }
    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        LOG.info("post init");
    }
    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        LOG.info("process engine built");
    }
}
