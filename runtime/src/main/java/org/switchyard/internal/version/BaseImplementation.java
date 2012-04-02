/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved. 
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
package org.switchyard.internal.version;

import static org.switchyard.internal.version.BaseVersion.compare;

import java.net.MalformedURLException;
import java.net.URL;

import org.switchyard.version.Implementation;

/**
 * BaseImplementation.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2012 Red Hat Inc.
 */
public class BaseImplementation implements Implementation {

    private final String _title;
    private final URL _url;
    private final String _vendor;
    private final String _vendorId;
    private final String _version;

    /**
     * Constructs a new BaseImplementation.
     * @param title the title
     * @param url the url
     * @param vendor the vendor
     * @param vendorId the vendorId
     * @param version the version
     */
    public BaseImplementation(String title, URL url, String vendor, String vendorId, String version) {
        _title = title;
        _url = url;
        _vendor = vendor;
        _vendorId = vendorId;
        _version = version;
    }

    /**
     * Constructs a new BaseImplementation.
     * @param title the title
     * @param url the url
     * @param vendor the vendor
     * @param vendorId the vendorId
     * @param version the version
     */
    public BaseImplementation(String title, String url, String vendor, String vendorId, String version) {
        _title = title;
        URL u;
        try {
            u = new URL(url);
        } catch (MalformedURLException mue) {
            u = null;
        }
        _url = u;
        _vendor = vendor;
        _vendorId = vendorId;
        _version = version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return _title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getURL() {
        return _url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVendor() {
        return _vendor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVendorId() {
        return _vendorId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return _version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%s [title=%s, url=%s, vendor=%s, vendorId=%s, version=%s]", Implementation.class.getSimpleName(), getTitle(), getURL(), getVendor(), getVendorId(), getVersion());
    }

    /**
     * {@inheritDoc}
     */   
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BaseImplementation other = (BaseImplementation)obj;
        if (_title == null) {
            if (other._title != null) {
                return false;
            }
        } else if (!_title.equals(other._title)) {
            return false;
        }
        if (_url == null) {
            if (other._url != null) {
                return false;
            }
        } else if (!_url.equals(other._url)) {
            return false;
        }
        if (_vendor == null) {
            if (other._vendor != null) {
                return false;
            }
        } else if (!_vendor.equals(other._vendor)) {
            return false;
        }
        if (_vendorId == null) {
            if (other._vendorId != null) {
                return false;
            }
        } else if (!_vendorId.equals(other._vendorId)) {
            return false;
        }
        if (_version == null) {
            if (other._version != null) {
                return false;
            }
        } else if (!_version.equals(other._version)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */   
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (_title == null ? 0 : _title.hashCode());
        result = prime * result + (_url == null ? 0 : _url.hashCode());
        result = prime * result + (_vendor == null ? 0 : _vendor.hashCode());
        result = prime * result + (_vendorId == null ? 0 : _vendorId.hashCode());
        result = prime * result + (_version == null ? 0 : _version.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Implementation that) {
        int comparison = 0;
        if (this != that) {
            comparison = compare(_title, that.getTitle());
            if (comparison == 0) {
                comparison = compare(toString(_url), toString(that.getURL()));
                if (comparison == 0) {
                    comparison = compare(_vendor, that.getVendor());
                    if (comparison == 0) {
                        comparison = compare(_vendorId, that.getVendorId());
                        if (comparison == 0) {
                            comparison = compare(_version, that.getVersion());
                        }
                    }
                }
            }
        }
        return comparison;
    }

    private String toString(URL url) {
        return url != null ? url.toString() : null;
    }

}
