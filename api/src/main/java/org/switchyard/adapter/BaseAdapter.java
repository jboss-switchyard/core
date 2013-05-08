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
package org.switchyard.adapter;

import javax.xml.namespace.QName;

/**
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public abstract class BaseAdapter implements Adapter {
    private QName _from;
    private QName _to;

    /**
     * Constructor.
     * @param from from
     * @param to to
     */
    public BaseAdapter(QName from, QName to) {
        super();
        this._from = from;
        this._to = to;
    }

    @Override
    public Adapter setFrom(QName fromType) {
        _from = fromType;
        return this;
    }

    @Override
    public Adapter setTo(QName toType) {
        _to = toType;
        return this;
    }

    @Override
    public QName getFrom() {
        return _from;
    }

    @Override
    public QName getTo() {
        return _to;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + ": from=\"" + getFrom() + "\", to=\"" + getTo() + "\"";
    }
}
