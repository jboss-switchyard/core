/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.switchyard.deploy;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.switchyard.ExchangeHandler;
import org.switchyard.ServiceDomain;
import org.switchyard.bus.camel.CamelExchangeBus;
import org.switchyard.common.camel.CamelContextManager;
import org.switchyard.common.type.Classes;
import org.switchyard.config.model.domain.DomainModel;
import org.switchyard.config.model.domain.HandlerModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;
import org.switchyard.event.DomainShutdownEvent;
import org.switchyard.event.DomainStartupEvent;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.internal.DefaultServiceRegistry;
import org.switchyard.internal.DomainImpl;
import org.switchyard.internal.EventManager;
import org.switchyard.internal.transform.BaseTransformerRegistry;
import org.switchyard.internal.validate.BaseValidatorRegistry;
import org.switchyard.spi.ServiceRegistry;

/**
 * {@link org.switchyard.ServiceDomain} manager class.
 * <p/>
 * Currently supports a flat ServiceDomain model with a ServiceDomain per application/deployment,
 * all managed from this container level bean.  Deployments are supplied with a {@link DomainProxy}
 * instance which can first delegate service lookup to the application's own domain, but on lookup
 * failure, can then delegate to this class in order to continue the lookup across all application
 * ServiceDomain's managed by the container.
 * <p/>
 * This model does not yet support the notion of multiple isolated ServiceDomains.  This class will
 * change or go away.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class ServiceDomainManager {

    /**
     * Root domain property.
     */
    public static final QName ROOT_DOMAIN = new QName("org.switchyard.domains.root");
    /**
     * Exchange Bus provider class name key.
     */
    public static final String BUS_CLASS_NAME = "busProvider";
    /**
     * Registry class name property.
     */
    public static final String REGISTRY_CLASS_NAME = "registryProvider";

    private static Logger _log = Logger.getLogger(ServiceDomainManager.class);

    // Share the same service registry and bus across domains to give visibility 
    // to registered services across application domains
    private ServiceRegistry _registry = new DefaultServiceRegistry();
    private EventManager _eventManager = new EventManager();
    private CamelContextManager _camelManager = new CamelContextManager();
    private Map<QName, WeakReference<ServiceDomain>> domains = new HashMap<QName, WeakReference<ServiceDomain>>();

    /**
     * Create a ServiceDomain instance.
     * <p/>
     * Uses {@link #ROOT_DOMAIN} as the domain name.
     * @return The ServiceDomain instance.
     */
    public ServiceDomain createDomain() {
        return createDomain(ROOT_DOMAIN, null);
    }

    /**
     * Create a ServiceDomain instance.
     * @param domainName The domain name.
     * @param switchyardConfig The SwitchYard configuration.
     * @return The ServiceDomain instance.
     */
    public ServiceDomain createDomain(QName domainName, SwitchYardModel switchyardConfig) {
        BaseTransformerRegistry transformerRegistry = new BaseTransformerRegistry();
        BaseValidatorRegistry validatorRegistry = new BaseValidatorRegistry();

        // register camel manager as domain lifecycle tracker
        _eventManager.addObserver(_camelManager, DomainStartupEvent.class);
        _eventManager.addObserver(_camelManager, DomainShutdownEvent.class);

        ServiceDomain domain = null;
        if (domains.containsKey(domainName)) {
            WeakReference<ServiceDomain> reference = domains.get(domainName);
            if (reference.get() != null) {
                domain = domains.get(domainName).get();
            }
        }

        if (domain == null) {
            CamelExchangeBus bus = new CamelExchangeBus();
            domain = new DomainImpl(domainName, _registry, bus, transformerRegistry, validatorRegistry, _eventManager);
            domains.put(domainName, new WeakReference<ServiceDomain>(domain));
        }

        if (switchyardConfig != null) {
            domain.getHandlers().addAll(getDomainHandlers(switchyardConfig.getDomain()));
        }

        return domain;
    }

    /**
     * Return the shared EventManager used for all ServiceDomain instances.
     * @return EventManager instance
     */
    public EventManager getEventManager() {
        return _eventManager;
    }

    /**
     * Looks for handler definitions in the switchyard config and attempts to 
     * create and add them to the domain's global handler chain.
     *
     */
    private List<ExchangeHandler> getDomainHandlers(DomainModel domain) {
        LinkedList<ExchangeHandler> handlers = new LinkedList<ExchangeHandler>();
        if (domain != null && domain.getHandlers() != null) {
            for (HandlerModel handlerConfig : domain.getHandlers().getHandlers()) {
                Class<?> handlerClass = Classes.forName(handlerConfig.getClassName());
                if (handlerClass == null) {
                    throw new SwitchYardException("Handler class not found " + handlerConfig.getClassName());
                }
                if (!ExchangeHandler.class.isAssignableFrom(handlerClass)) {
                    throw new SwitchYardException("Handler " + handlerConfig.getName() 
                            + " is not an instance of " + ExchangeHandler.class.getName());
                }
                try {
                    _log.debug("Adding handler " + handlerConfig.getName() + " to domain.");
                    ExchangeHandler handler = (ExchangeHandler)handlerClass.newInstance();
                    handlers.addLast(handler);
                } catch (Exception ex) {
                    throw new SwitchYardException("Failed to initialize handler class " + handlerClass.getName(), ex);
                }
            }
        }
        return handlers;
    }

}
