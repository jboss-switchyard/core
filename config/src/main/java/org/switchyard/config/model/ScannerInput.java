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
package org.switchyard.config.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The input to a {@link Scanner}.
 *
 * @param <M> the Model type being scanned for
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public class ScannerInput<M extends Model> {

    private List<URL> _urls;
    private String _name;

    /**
     * Constructs a new ScannerInput.
     */
    public ScannerInput() {
        _urls = new ArrayList<URL>();
    }

    /**
     * Gets the URLs to scan.
     * @return the URLs
     */
    public synchronized List<URL> getURLs() {
        return Collections.unmodifiableList(_urls);
    }

    /**
     * Sets the URLs to scan.
     * @param urls the URLs
     * @return this ScannerInput (useful for chaining)
     */
    public synchronized ScannerInput<M> setURLs(List<URL> urls) {
        _urls.clear();
        if (urls != null) {
            for (URL url : urls) {
                if (url != null) {
                    _urls.add(url);
                }
            }
        }
        return this;
    }

    /**
     * Gets a contextual name for the scan.
     * @return the contextual name
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets a contextual name for the scan.
     * @param name the contextual name
     * @return this ScannerInput (useful for chaining)
     */
    public ScannerInput<M> setName(String name) {
        _name = name;
        return this;
    }

}
