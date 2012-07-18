/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
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
package org.switchyard.transform;

import javax.xml.namespace.QName;

/**
 * Transformer data types.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class TransformerTypes {

    private QName _from;
    private QName _to;

    /**
     * Public constructor.
     *
     * @param from From type.
     * @param to   To type.
     */
    TransformerTypes(QName from, QName to) {
        this._from = from;
        this._to = to;
    }

    /**
     * Get from.
     *
     * @return from.
     */
    public QName getFrom() {
        return _from;
    }

    /**
     * Get to.
     *
     * @return to.
     */
    public QName getTo() {
        return _to;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%s [from=%s, to=%s]", getClass().getSimpleName(), getFrom(), getTo());
    }
}
