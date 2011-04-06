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
package org.switchyard.internal.io.graph;

import java.io.IOException;

/**
 * Wraps a Graph, and is itself a Graph. This is necessary for Protostuff, for example,
 * since the root object to create a runtime schema from must not be an interface or abstract class.
 * 
 * @param <T> the type of object at the root of this Graph node
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
@SuppressWarnings("serial")
public class GraphWrapper<T> implements Graph<T> {

    private Graph<T> _graph;

    /**
     * Constructs a new GraphWrapper.
     */
    public GraphWrapper() {}

    /**
     * Constructs a new GraphWrapper, pre-populated with the specified Graph.
     * @param graph the Graph to wrap
     */
    public GraphWrapper(Graph<T> graph) {
        setGraph(graph);
    }

    /**
     * Gets the wrapped Graph.
     * @return the wrapped Graph
     */
    public Graph<T> getGraph() {
        return _graph;
    }

    /**
     * Sets the Graph to wrap.
     * @param graph the graph to wrap
     */
    public void setGraph(Graph<T> graph) {
        _graph = graph;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void compose(T object) throws IOException {
        getGraph().compose(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T decompose() throws IOException {
        return getGraph().decompose();
    }

}
