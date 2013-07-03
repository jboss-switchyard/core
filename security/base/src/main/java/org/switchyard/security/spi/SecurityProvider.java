/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
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
package org.switchyard.security.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.apache.log4j.Logger;
import org.switchyard.ServiceDomain;
import org.switchyard.ServiceSecurity;
import org.switchyard.security.SecurityContext;

/**
 * SecurityProvider.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; &copy; 2013 Red Hat Inc.
 */
public interface SecurityProvider {

   /**
    * Authenticates the SecurityContext for the ServiceSecurity.
    * @param serviceSecurity the ServiceSecurity
    * @param securityContext the SecurityContext
    * @return whether authentication was successful
    */
   public abstract boolean authenticate(ServiceSecurity serviceSecurity, SecurityContext securityContext);

   /**
    * Propagates any existing container-specific (most likely thread-local) security information into the SecurityContext.
    * @param serviceSecurity the ServiceSecurity
    * @param securityContext the SecurityContext
    * @return whether propagation was successful
    */
   public abstract boolean propagate(ServiceSecurity serviceSecurity, SecurityContext securityContext);

   /**
     * Adds the ServiceSecurity's runAs Role to the SecurityContext's Subject.
     * @param serviceSecurity the ServiceSecurity
     * @param securityContext the SecurityContext
     * @return whether adding the runAs Role was successful
    */
   public abstract boolean addRunAs(ServiceSecurity serviceSecurity, SecurityContext securityContext);

   /**
    * Checks if the Subject associated in the SecurityContext has at least one of the allowed roles in the ServiceSecurity.
    * @param serviceSecurity the ServiceSecurity
    * @param securityContext the SecurityContext
    * @return whether the allowed roles check was successful
    */
   public abstract boolean checkRolesAllowed(ServiceSecurity serviceSecurity, SecurityContext securityContext);

   /**
    * Clears the SecurityContext and any underlying SecurityContextAssociation.
    * @param serviceSecurity the ServiceSecurity
    * @param securityContext the SecurityContext
    * @return whether the clear was successful
    */
   public abstract boolean clear(ServiceSecurity serviceSecurity, SecurityContext securityContext);

}
