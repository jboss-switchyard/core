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
package org.switchyard.version;

import static java.lang.System.out;
import static org.apache.log4j.Level.INFO;
import static org.switchyard.version.QueryType.PROJECT_ARTIFACT_ID;
import static org.switchyard.version.QueryType.PROJECT_GROUP_ID;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Versions.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2012 Red Hat Inc.
 */
public final class Versions {

    private static final Logger LOGGER = Logger.getLogger(Versions.class);

    private static final Query QUERY_SWITCHYARD = new Query(PROJECT_GROUP_ID, "org.switchyard");
    private static final Query QUERY_API = new Query(PROJECT_ARTIFACT_ID, "switchyard-api");
    private static final Query QUERY_RUNTIME = new Query(PROJECT_ARTIFACT_ID, "switchyard-runtime");

    private Versions() {}

    /**
     * Gets the SwitchYard API Version.
     * @return the SwitchYard API Version
     */
    public static Version getSwitchYardAPIVersion() {
        return VersionFactory.instance().getVersion(QUERY_SWITCHYARD, QUERY_API);
    }

    /**
     * Gets the SwitchYard Runtime Version.
     * @return the SwitchYard Runtime Version
     */
    public static Version getSwitchYardRuntimeVersion() {
        return VersionFactory.instance().getVersion(QUERY_SWITCHYARD, QUERY_RUNTIME);
    }

    /**
     * Gets the SwitchYard welcome (for example, "Welcome to SwitchYard api spec version 1.0.0.Final, runtime impl version 1.0.0.Final.").
     * @return the SwitchYard welcome
     */
    public static String getSwitchYardWelcome() {
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome to SwitchYard");
        Version api = getSwitchYardAPIVersion();
        Version runtime = getSwitchYardRuntimeVersion();
        if (api != null || runtime != null) {
            sb.append(' ');
            if (api != null) {
                sb.append("api spec version ");
                sb.append(api.getSpecification().getVersion());
            }
            if (runtime != null) {
                if (api != null) {
                    sb.append(", ");
                }
                sb.append("runtime impl version ");
                sb.append(runtime.getImplementation().getVersion());
            }
        }
        sb.append('.');
        return sb.toString();
    }

    /**
     * Logs the SwitchYard welcome returned by {@link #getSwitchYardWelcome()}.
     * @param logger the Logger to log to
     * @param level the Level to log at
     */
    public static void logSwitchYardWelcome(Logger logger, Level level) {
        if (logger.isEnabledFor(level)) {
            logger.log(level, getSwitchYardWelcome());
        }
    }

    /**
     * Logs the SwitchYard welcome returned by {@link #getSwitchYardWelcome()}.
     * @param logger the Logger to log to
     */
    public static void logSwitchYardWelcome(Logger logger) {
        logSwitchYardWelcome(logger, INFO);
    }

    /**
     * Logs the SwichYard welcome returned by {@link #getSwitchYardWelcome()}.
     */
    public static void logSwitchYardWelcome() {
        logSwitchYardWelcome(LOGGER);
    }

    /**
     * Prints the SwitchYard welcome returned by {@link #getSwitchYardWelcome()}.
     * @param w the Writer to print to
     */
    public static void printSwitchYardWelcome(Writer w) {
        PrintWriter pw = (w instanceof PrintWriter) ? (PrintWriter)w : new PrintWriter(w);
        pw.println(getSwitchYardWelcome());
        pw.flush();
    }

    /**
     * Prints the SwitchYard welcome returned by {@link #getSwitchYardWelcome()}.
     * @param os the OutputStream to print to
     */
    public static void printSwitchYardWelcome(OutputStream os) {
        printSwitchYardWelcome(new PrintWriter(os));
    }

    /**
     * Prints the SwitchYard welcome returned by {@link #getSwitchYardWelcome()} to STDOUT (System.out).
     */
    public static void printSwitchYardWelcome() {
        printSwitchYardWelcome(out);
    }

    /**
     * Gets the Set of all SwitchYard Versions (with titles that match "SwitchYard: .*").
     * @return the Set of all SwitchYard Versions
     */
    public static Set<Version> getSwitchYardVersions() {
        return VersionFactory.instance().getVersions(QUERY_SWITCHYARD);
    }

    /**
     * Logs each Version returned by {@link #getSwitchYardVersions()}.
     * @param logger the Logger to log to
     * @param level the Level to log at
     */
    public static void logSwitchYardVersions(Logger logger, Level level) {
        if (logger.isEnabledFor(level)) {
            for (Version version : getSwitchYardVersions()) {
                logger.log(level, version);
            }
        }
    }
    /**
     * Logs each Version returned by {@link #getSwitchYardVersions()}.
     * @param logger the Logger to log to
     */
    public static void logSwitchYardVersions(Logger logger) {
        logSwitchYardVersions(logger, INFO);
    }

    /**
     * Logs each Version returned by {@link #getSwitchYardVersions()}.
     */
    public static void logSwitchYardVersions() {
        logSwitchYardVersions(LOGGER);
    }

    /**
     * Prints each Version returned by {@link #getSwitchYardVersions()}.
     * @param w the Writer to print to
     */
    public static void printSwitchYardVersions(Writer w) {
        PrintWriter pw = (w instanceof PrintWriter) ? (PrintWriter)w : new PrintWriter(w);
        for (Version version : getSwitchYardVersions()) {
            pw.println(version);
        }
        pw.flush();
    }

    /**
     * Prints each Version returned by {@link #getSwitchYardVersions()}.
     * @param os the OutputStream to print to
     */
    public static void printSwitchYardVersions(OutputStream os) {
        printSwitchYardVersions(new PrintWriter(os));
    }

    /**
     * Prints each Version returned by {@link #getSwitchYardVersions()} to STDOUT (System.out).
     */
    public static void printSwitchYardVersions() {
        printSwitchYardVersions(out);
    }

    /**
     * Calls {@link #printSwitchYardWelcome()} and {@link #printSwitchYardVersions()}.
     * @param args unused
     */
    public static void main(String... args) {
        printSwitchYardWelcome();
        printSwitchYardVersions();
    }

}
