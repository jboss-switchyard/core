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

import org.switchyard.version.Project;

/**
 * BaseProject.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2012 Red Hat Inc.
 */
public class BaseProject implements Project {

    private final String _groupId;
    private final String _artifactId;
    private final String _version;

    /**
     * Constructs a new BaseSpecification.
     * @param groupId the groupId
     * @param artifactId the artifactId
     * @param version the version
     */
    public BaseProject(String groupId, String artifactId, String version) {
        _groupId = groupId;
        _artifactId = artifactId;
        _version = version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroupId() {
        return _groupId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getArtifactId() {
        return _artifactId;
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
        return String.format("%s [groupId=%s, artifactId=%s, version=%s]", Project.class.getSimpleName(), getGroupId(), getArtifactId(), getVersion());
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
        BaseProject other = (BaseProject)obj;
        if (_groupId == null) {
            if (other._groupId != null) {
                return false;
            }
        } else if (!_groupId.equals(other._groupId)) {
            return false;
        }
        if (_artifactId == null) {
            if (other._artifactId != null) {
                return false;
            }
        } else if (!_artifactId.equals(other._artifactId)) {
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
        result = prime * result + (_groupId == null ? 0 : _groupId.hashCode());
        result = prime * result + (_artifactId == null ? 0 : _artifactId.hashCode());
        result = prime * result + (_version == null ? 0 : _version.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Project that) {
        int comparison = 0;
        if (this != that) {
            comparison = compare(_groupId, that.getGroupId());
            if (comparison == 0) {
                comparison = compare(_artifactId, that.getArtifactId());
                if (comparison == 0) {
                    comparison = compare(_version, that.getVersion());
                }
            }
        }
        return comparison;
    }

}
