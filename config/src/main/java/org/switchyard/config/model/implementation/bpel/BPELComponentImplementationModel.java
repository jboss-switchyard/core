/*
 * 2012 Red Hat Inc. and/or its affiliates and other contributors.
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
package org.switchyard.config.model.implementation.bpel;

import javax.xml.namespace.QName;

import org.switchyard.config.model.composite.ComponentImplementationModel;

/**
 * A "bpel" ComponentImplementationModel.
 *
 */
public interface BPELComponentImplementationModel extends ComponentImplementationModel {

    /**
     * The "bpel" namespace.
     */
    public static final String DEFAULT_NAMESPACE = "http://docs.oasis-open.org/ns/opencsa/sca/200912";

    /**
     * The "bpel" implementation type.
     */
    public static final String BPEL = "bpel";
    
    /**
     * Gets the "process" attribute.
     *
     * @return the "process" attribute
     */
    public String getProcess();

    /**
     * Gets the "process" attribute as a QName.
     *
     * @return the "process" attribute
     */
    public QName getProcessQName();

    /**
     * Sets the "process" attribute.
     *
     * @param process the "process" attribute
     * @return this instance (useful for chaining)
     */
    public BPELComponentImplementationModel setProcess(String process);

}
