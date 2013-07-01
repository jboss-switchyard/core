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
package org.switchyard.config.model.domain;

import org.switchyard.config.model.Model;

/**
 * The "handler" configuration model.
 */
public interface HandlerModel extends Model {

    /** The "handler" name. */
    public static final String HANDLER = "handler";

    /** The "name" name. */
    public static final String NAME = "name";

    /** The "class" name. */
    public static final String CLASS = "class";

    /**
     * Gets the handler name attribute.
     * @return the name attribute
     */
    public String getName();

    /**
     * Sets the handler name attribute.
     * @param name the name attribute
     * @return this HandlerModel (useful for chaining)
     */
    public HandlerModel setName(String name);

    /**
     * Gets the class attribute.
     * @return the class attribute
     */
    public String getClassName();

    /**
     * Sets the class attribute.
     * @param className the class attribute
     * @return this HandlerModel (useful for chaining)
     */
    public HandlerModel setClassName(String className);

    /**
     * Gets the parent handlers model.
     * @return the parent handlers model.
     */
    public HandlersModel getHandlers();


}
