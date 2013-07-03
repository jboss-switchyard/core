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

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.switchyard.Exchange;
import org.switchyard.admin.Application;
import org.switchyard.admin.Component;
import org.switchyard.admin.ComponentReference;
import org.switchyard.admin.ComponentService;
import org.switchyard.admin.Reference;
import org.switchyard.admin.Service;
import org.switchyard.admin.Transformer;
import org.switchyard.admin.Validator;
import org.switchyard.config.model.composite.ComponentModel;
import org.switchyard.config.model.composite.ComponentReferenceModel;
import org.switchyard.config.model.composite.ComponentServiceModel;
import org.switchyard.config.model.composite.CompositeReferenceModel;
import org.switchyard.config.model.composite.CompositeServiceModel;
import org.switchyard.config.model.composite.InterfaceModel;
import org.switchyard.config.model.property.PropertyModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;
import org.switchyard.config.model.transform.TransformModel;
import org.switchyard.config.model.validate.ValidateModel;
import org.switchyard.deploy.ComponentNames;
import org.switchyard.deploy.internal.AbstractDeployment;
import org.switchyard.event.EventObserver;
import org.switchyard.runtime.event.ExchangeCompletionEvent;

/**
 * Base implementation of Application.
 */
public class BaseApplication implements Application, EventObserver {
    
    private final QName _name;
    private Map<QName, Service> _services;
    private Map<QName, Reference> _references;
    private Map<QName, Component> _components;
    private Map<QName, ComponentService> _componentServices;
    private List<Transformer> _transformers;
    private List<Validator> _validators;
    private AbstractDeployment _deployment;
    private Map<String, String> _properties;
    
    /**
     * Create a new BaseApplication.
     * @param deployment the deployment representing this application
     */
    public BaseApplication(AbstractDeployment deployment) {
        _name = deployment.getName();
        _deployment = deployment;
        
        addTransformers();
        addValidators();
        addComponents();
        addServices();
        addReferences();
        addProperties();

        // register event listener for metrics
        _deployment.getDomain().addEventObserver(this, ExchangeCompletionEvent.class);
    }

    void dispose() {
        // remove event listener for metrics
        _deployment.getDomain().removeObserver(this);
    }

    @Override
    public QName getName() {
        return _name;
    }

    @Override
    public List<Service> getServices() {
        if (_services == null) {
            return Collections.emptyList();
        }
        return new ArrayList<Service>(_services.values());
    }
    
    @Override
    public List<Reference> getReferences() {
        if (_references == null) {
            return Collections.emptyList();
        }
        return new ArrayList<Reference>(_references.values());
    }
    
    @Override
    public Service getService(QName serviceName) {
        if (_services == null) {
            return null;
        }
        return _services.get(serviceName);
    }
    
    @Override
    public Reference getReference(QName referenceName) {
        if (_references == null) {
            return null;
        }
        return _references.get(referenceName);
    }
    
    @Override
    public SwitchYardModel getConfig() {
        return _deployment.getConfig();
    }

    @Override
    public List<ComponentService> getComponentServices() {
        if (_componentServices == null) {
            return Collections.emptyList();
        }
        return new ArrayList<ComponentService>(_componentServices.values());
    }

    @Override
    public ComponentService getComponentService(QName componentServiceName) {
        if (_componentServices == null) {
            return null;
        }
        return _componentServices.get(componentServiceName);
    }

    @Override
    public Component getComponent(QName name) {
        return _components.get(name);
    }

    @Override
    public List<Component> getComponents() {
        return new ArrayList<Component>(_components.values());
    }

    @Override
    public List<Transformer> getTransformers() {
        return Collections.unmodifiableList(_transformers);
    }

    @Override
    public List<Validator> getValidators() {
        return Collections.unmodifiableList(_validators);
    }


    @Override
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(_properties);
    }

    @Override
    public void notify(EventObject event) {
        if (event instanceof ExchangeCompletionEvent) {
            exchangeCompleted((ExchangeCompletionEvent)event);
        }
    }
    
    /**
     * @return the deployment associated with this application.
     */
    public AbstractDeployment getDeployment() {
        return _deployment;
    }

    private void addServices() {
        _services = new LinkedHashMap<QName, Service>();
        if (getConfig().getComposite().getServices() == null) {
            return;
        }
        
        for (CompositeServiceModel service : getConfig().getComposite().getServices()) {
            _services.put(service.getQName(), new BaseService(service, this));
        }
    }
    
    private void addReferences() {
        _references = new LinkedHashMap<QName, Reference>();
        if (getConfig().getComposite().getReferences() == null) {
            return;
        }
        
        for (CompositeReferenceModel ref : getConfig().getComposite().getReferences()) {
            _references.put(ref.getQName(), new BaseReference(ref, this));
        }
    }

    private void addTransformers() {
        _transformers = new LinkedList<Transformer>();
        if (getConfig().getTransforms() == null) {
            return;
        }
        for (TransformModel transformModel : getConfig().getTransforms().getTransforms()) {
            _transformers.add(new BaseTransformer(transformModel));
        }
    }
    
    private void addValidators() {
        _validators = new LinkedList<Validator>();
        if (getConfig().getValidates() == null) {
            return;
        }
        for (ValidateModel validateModel : getConfig().getValidates().getValidates()) {
            _validators.add(new BaseValidator(validateModel));
        }
    }

    private void addComponents() {
        _components = new LinkedHashMap<QName, Component>();
        _componentServices = new LinkedHashMap<QName, ComponentService>();
        if (getConfig().getComposite().getComponents() == null) {
            return;
        }
        for (ComponentModel componentConfig : getConfig().getComposite().getComponents()) {
            // TODO: Should consider multiple services per component.
            final ComponentService service;
            if (componentConfig.getServices().size() > 0) {
                ComponentServiceModel serviceConfig = componentConfig.getServices().get(0);
                if (serviceConfig.getInterface() == null) {
                    service = new BaseNoopComponentService(serviceConfig, componentConfig, this);
                } else if (InterfaceModel.JAVA.equals(serviceConfig.getInterface().getType())) {
                    service = new BaseJavaComponentService(serviceConfig, componentConfig, this);
                } else if (InterfaceModel.WSDL.equals(serviceConfig.getInterface().getType())) {
                    service = new BaseWsdlComponentService(serviceConfig, componentConfig, this);
                } else {
                    // ESB or unknown
                    service = new BaseNoopComponentService(serviceConfig, componentConfig, this);
                }
                _componentServices.put(serviceConfig.getQName(), service);
            } else {
                service = null;
            }
            final Map<QName, ComponentReference> references = new LinkedHashMap<QName, ComponentReference>();
            if (service == null) {
                for (ComponentReferenceModel referenceModel : componentConfig.getReferences()) {
                    references.put(referenceModel.getQName(), new BaseComponentReference(referenceModel.getQName(), 
                            getInterfaceName(referenceModel.getInterface())));
                }
            } else {
                for (ComponentReference reference : service.getReferences()) {
                    references.put(reference.getName(), reference);
                }
            }
            final BaseComponent component = new BaseComponent(componentConfig.getQName(),
                    componentConfig.getImplementation() == null ? "null" : componentConfig.getImplementation()
                            .getType(), service, references, convertProperties(componentConfig.getProperties()));
            _components.put(component.getName(), component);
        }
    }

    private void addProperties() {
        if (getConfig().getComposite() == null) {
            _properties = convertProperties(null);
        } else {
            _properties = convertProperties(getConfig().getComposite().getProperties());
        }
    }

    private Map<String,String> convertProperties(final Map<String, PropertyModel> properties) {
        final Map<String,String> retVal = new LinkedHashMap<String, String>();
        if (properties == null) {
            return retVal;
        }
        for (PropertyModel property : properties.values()) {
            retVal.put(property.getName(), property.getValue());
        }
        return retVal;
    }

    private String getInterfaceName(InterfaceModel interfaceModel) {
        if (interfaceModel == null) {
            return null;
        }
        return interfaceModel.getInterface();
    }
    
    void exchangeCompleted(final ExchangeCompletionEvent event) {
        // Recording metrics at multiple levels at this point instead of
        // aggregating them.
        final Exchange exchange = event.getExchange();
        final QName qualifiedReferenceName = exchange.getConsumer().getName();
        final QName referenceName = ComponentNames.unqualify(qualifiedReferenceName);
        final QName componentName = ComponentNames.comopnentName(qualifiedReferenceName);
        if (componentName == null) {
            // service gateway initiated exchange
            final Service service = _services.get(referenceName);
            if (service != null) {
                /*
                 * service also records promoted component service metrics too
                 * (i.e. producer metrics)
                 */
                service.recordMetrics(exchange);
            }
        } else {
            // component reference initiated exchange
            // 1 - recored service metrics (producer)
            final QName serviceName = exchange.getProvider().getName();
            final ComponentService service = _componentServices.get(serviceName);
            if (service == null) {
                // must be routed to composite reference
                final Reference reference = _references.get(serviceName);
                if (reference != null) {
                    reference.recordMetrics(exchange);
                }
            } else {
                /*
                 * XXX: this could throw off metrics for composite services
                 * since they simply return the metrics for the component
                 * service they promote. That said, the metrics for the gateways
                 * will correlate with the global metrics.
                 */
                service.recordMetrics(exchange);
            }
            // 2 - record reference metrics (consumer)
            final Component component = _components.get(componentName);
            if (component != null) {
                final ComponentReference reference = component.getReference(referenceName);
                if (reference != null) {
                    reference.recordMetrics(exchange);
                }
                // else may have been an internal invocation, e.g. orders demo
            }
        }
    }
}
