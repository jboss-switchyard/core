/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.admin.base;

import java.util.EventObject;

import javax.xml.namespace.QName;

import org.switchyard.Exchange;
import org.switchyard.admin.SwitchYard;
import org.switchyard.admin.mbean.internal.LocalManagement;
import org.switchyard.admin.mbean.internal.MBeans;
import org.switchyard.deploy.ComponentNames;
import org.switchyard.deploy.ServiceDomainManager;
import org.switchyard.deploy.event.ApplicationDeployedEvent;
import org.switchyard.deploy.event.ApplicationUndeployedEvent;
import org.switchyard.deploy.internal.AbstractDeployment;
import org.switchyard.event.EventObserver;
import org.switchyard.runtime.event.ExchangeCompletionEvent;

/**
 * SwitchYardBuilder
 * 
 * {@link EventObserver} implementation which builds a
 * {@link org.switchyard.admin.SwitchYard} model using notifications from 
 * {@link AbstractDeployment} events.
 * 
 * @author Rob Cernich
 */
public class SwitchYardBuilder implements EventObserver {

    private BaseSwitchYard _switchYard;
    private ServiceDomainManager _domainManager;

    /**
     * Create a new SwitchYardBuilder.
     */
    public SwitchYardBuilder() {
        _switchYard = new BaseSwitchYard();
    }
    
    /**
     * Initializes the SwitchBuilder which includes registering the local management MBean
     * and registering as an EventObserver within SwitchYard.
     * @param domainManager the SY ServiceDomainManager
     */
    public void init(ServiceDomainManager domainManager) {
        _domainManager = domainManager;
        
        // Register local management MBeans
        LocalManagement lm = new LocalManagement(_domainManager);
        MBeans.registerLocalManagement(lm);
        
        // Register event hooks
        _domainManager.getEventManager()
            .addObserver(this, ExchangeCompletionEvent.class)
            .addObserver(this, ApplicationDeployedEvent.class)
            .addObserver(this, ApplicationUndeployedEvent.class);
    }
    
    /**
     * Tears down registered MBeans and event subscriptions.  Call this during system shutdown
     * to clean up.
     */
    public void destroy() {
        // Unregister event hooks
        _domainManager.getEventManager().removeObserver(this);
        // Unregister management mbeans
        MBeans.unregisterLocalManagement();
    }
    
    /**
     * Returns the SwitchYard admin object.
     * @return SwitchYard interface representing the SY runtime
     */
    public SwitchYard getSwitchYard() {
        return _switchYard;
    }
    
    /**
     * Returns the ServiceDomainManager instance in use for this builder.
     * @return ServiceDomainManager used by this builder instance.
     */
    public ServiceDomainManager getDomainManager() {
        return _domainManager;
    }
    
    @Override
    public void notify(EventObject event) {
        if (event instanceof ApplicationDeployedEvent) {
            applicationDeployed((ApplicationDeployedEvent)event);
        } else if (event instanceof ApplicationUndeployedEvent) {
            applicationUndeployed((ApplicationUndeployedEvent)event);
        } else if (event instanceof ExchangeCompletionEvent) {
            exchangeCompleted((ExchangeCompletionEvent)event);
        }
    }
    
    void applicationDeployed(ApplicationDeployedEvent event) {
        AbstractDeployment deployment = event.getDeployment();
        if (deployment.getName() != null) {
            BaseApplication app = new BaseApplication(deployment);
            _switchYard.addApplication(app);
            MBeans.registerApplication(app);
        }
    }

    void applicationUndeployed(ApplicationUndeployedEvent event) {
        AbstractDeployment deployment = event.getDeployment();
        if (deployment.getName() != null) {
            BaseApplication app = (BaseApplication) _switchYard.getApplication(deployment.getName());
            if (app != null) {
                MBeans.unregisterApplication(app);
                _switchYard.removeApplication(deployment.getName());
                app.dispose();
            }
        }
    }
    
    void exchangeCompleted(ExchangeCompletionEvent event) {
        final Exchange exchange = event.getExchange();
        final QName componentName = ComponentNames.comopnentName(exchange.getConsumer().getName());
        if (componentName == null) {
            // service gateway initiated exchange
            _switchYard.recordMetrics(exchange);
        }
        // else - don't include internally generated exchanges
    }
}
