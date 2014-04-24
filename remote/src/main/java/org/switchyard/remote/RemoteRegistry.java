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
package org.switchyard.remote;

import java.util.List;

import javax.xml.namespace.QName;

/**
 * Contract for a distributed registry provider.
 */
public interface RemoteRegistry {

    /**
     * Add an endpoint to the registry.  If the endpoint already exists then nothing is added.
     * @param endpoint the endpoint to add
     */
    public void addEndpoint(RemoteEndpoint endpoint);

    /**
     * Removes an endpoint from the registry if it exists.
     * @param endpoint the endpoint to remove.
     */
    public void removeEndpoint(RemoteEndpoint endpoint);

    /**
     * Returns a list of all registered endpoints for a given service.
     * @param serviceName name of the service
     * @return list of registered endpoints
     */
    public List<RemoteEndpoint> getEndpoints(QName serviceName);
    
    
    /**
     * Returns the local endpoints for a given service if existing.
     * @param serviceName name of the service
     * @return endpoint
     */
    public RemoteEndpoint getLocalEndpoint(QName serviceName);

    /**
     * Returns true if there is local endpoints for a given service.
     * @param serviceName name of the service
     * @return true in case of existing local endpoint, false otherwise
     */
    public boolean hasLocalEndpoint(QName serviceName);
}
