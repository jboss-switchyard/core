/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.switchyard;

/**
 * Empty implementation of the ExchangeHandler interface that can be used to
 * selectively override fault or message handling logic.
 */
public class BaseHandler implements ExchangeHandler {

    /**
     * @param exchange the exchange
     * @see ExchangeHandler#handleFault(Exchange)
     */
    public void handleFault(final Exchange exchange) {
        // Default implementation does nothing for now
    }

    /**
     * @param exchange the exchange
     * @throws HandlerException handler exception
     * @see ExchangeHandler#handleMessage(Exchange)
     */
    public void handleMessage(final Exchange exchange) throws HandlerException {
        // Default implementation does nothing for now
    }
}
