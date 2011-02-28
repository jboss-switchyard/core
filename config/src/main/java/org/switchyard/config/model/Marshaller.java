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
package org.switchyard.config.model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.switchyard.config.Configuration;

/**
 * Used to read or write Models.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public interface Marshaller {

    /**
     * Gets the Descriptor used by this Marshaller.
     * @return the Descriptor
     */
    public Descriptor getDescriptor();

    /**
     * Reads in (constructs) a Model based on the specified Configuration.
     * @param config the config
     * @return the new Model
     */
    public Model read(Configuration config);

    /**
     * Writes the specified Model to the specified OutputStream.
     * @param model the model
     * @param out the OutputStream
     * @throws IOException if a problem occurred
     */
    public void write(Model model, OutputStream out) throws IOException;

    /**
     * Writes the specified Model to the specified Writer.
     * @param model the model
     * @param writer the Writer
     * @throws IOException if a problem occurred
     */
    public void write(Model model, Writer writer) throws IOException;

}
