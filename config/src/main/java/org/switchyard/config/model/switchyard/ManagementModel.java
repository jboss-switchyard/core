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
package org.switchyard.config.model.switchyard;

import org.switchyard.config.model.Model;

/**
 * Additional service management configuration model
 */
public interface ManagementModel extends Model {

    /** The "management" name. */
    public static final String MANAGEMENT                    = "management";

    /** The "autoStartupBindings" attribute. */
    public static final String AUTO_STARTUP_BINDINGS         = "autoStartupBindings";

    /**
     * Should service bindings startup automatically after deployment
     *
     * @return true if should, false in other case
     */
    public boolean isAutoStartupBindings();

    /**
     * Sets the autoStartupBindings attribute
     *
     * @param autoStartupBindings the autoStartupBindings attribute
     * @return this ManagementModel (useful for chaining)
     */
    public ManagementModel setAutoStartupBindings(boolean autoStartupBindings);

}
