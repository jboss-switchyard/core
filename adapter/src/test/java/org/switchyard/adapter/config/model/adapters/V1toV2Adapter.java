/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
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
package org.switchyard.adapter.config.model.adapters;

import javax.xml.namespace.QName;

import org.switchyard.adapter.BaseAdapter;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;

/**
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class V1toV2Adapter extends BaseAdapter {	
	public static final QName FROM = QName.valueOf("{urn:org.switchyard.adapter.config.model}TestServiceV1");
	public static final QName TO = QName.valueOf("{urn:org.switchyard.adapter.config.model}TestServiceV2");

	public V1toV2Adapter() {
		super(FROM, TO);
	}

	@Override
	public ServiceOperation lookup(String consumerOperation, ServiceInterface targetInterface) {
		return null;
	}
}
