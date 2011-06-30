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
package org.switchyard.test.mixins;

import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.agent.impl.DoNothingSystemEventListener;
import org.jbpm.task.Status;
import org.jbpm.task.User;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.TaskServer;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.TaskServiceSession;
import org.jbpm.task.service.mina.MinaTaskClientConnector;
import org.jbpm.task.service.mina.MinaTaskClientHandler;
import org.jbpm.task.service.mina.MinaTaskServer;
import org.jbpm.task.service.responsehandlers.BlockingTaskOperationResponseHandler;
import org.jbpm.task.service.responsehandlers.BlockingTaskSummaryResponseHandler;

/**
 * BPM Test Mix In.
 * Helps with <a href="http://docs.jboss.org/jbpm/v5.0/userguide/ch.Human_Tasks.html">jBPM 5 Human Tasks</a>.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public class BPMMixIn extends AbstractTestMixIn {

    /** The default host. */
    public static final String DEFAULT_HOST = "127.0.0.1";
    /** The default port. */
    public static final int DEFAULT_PORT = 9123;

    /** The Administrator user id. */
    public static final String ADMINISTRATOR = "Administrator";

    private TaskServer _server = null;
    private TaskClient _client = null;
    private boolean _clientConnected = false;

    /**
     * Starts the task server with the default host and port, and the specified user ids to pre-populate the database with.
     * @param userIds the user ids to pre-populate the database with
     * @return whether the task server started successfully
     */
    public boolean startTaskServer(String... userIds) {
        return startTaskServer(DEFAULT_HOST, DEFAULT_PORT, userIds);
    }

    /**
     * Starts the task server with the specified host and port, and the specified user ids to pre-populate the database with.
     * @param host the task server host
     * @param port the task server port
     * @param userIds the user ids to pre-populate the database with
     * @return whether the task server started successfully
     */
    public boolean startTaskServer(String host, int port, String... userIds) {
        try {
            boolean ready = false;
            while (!ready) {
                Socket socket = null;
                try {
                    socket = new Socket(host, port);
                    if (socket.isConnected()) {
                        Thread.sleep(1000);
                    } else {
                        ready = true;
                    }
                } catch (SocketException se) {
                    ready = true;
                } finally {
                    if (socket != null) {
                        socket.close();
                        socket = null;
                    }
                }
            }
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.task");
            TaskService taskService = new TaskService(emf, new DoNothingSystemEventListener());
            TaskServiceSession taskServiceSession = taskService.createSession();
            taskServiceSession.addUser(new User(ADMINISTRATOR));
            for (String userId : userIds) {
                if (!ADMINISTRATOR.equals(userId)) {
                    taskServiceSession.addUser(new User(userId));
                }
            }
            taskServiceSession.dispose();
            _server = new MinaTaskServer(taskService, port, host);
            new Thread(_server).start();
            //_server.start();
            return true;
        } catch (Throwable t) {
            System.err.println("*** Could not start task server: " + t.getMessage() + " ***");
            return false;
        }
    }

    /**
     * Stops the task server.
     */
    public void stopTaskServer() {
        if (_server != null) {
            try {
                _server.stop();
            } catch (Throwable t) {
                // just to keep checkstyle happy ("Must have at least one statement.")
                t.getMessage();
            }
            _server = null;
        }
    }

    /**
     * Starts the task client with the default host and port of the task server to connect to.
     * @throws Exception if a problem occurred
     */
    public void startTaskClient() throws Exception {
        startTaskClient(DEFAULT_HOST, DEFAULT_PORT);
    }

    /**
     * Starts the task client with the specified host and port of the task server to connect to.
     * @param host the task server host
     * @param port the task server port
     * @throws Exception if a problem occurred
     */
    public void startTaskClient(String host, int port) throws Exception {
        if (_client == null) {
            _client = new TaskClient(new MinaTaskClientConnector(BPMMixIn.class.getSimpleName(), new MinaTaskClientHandler(new DoNothingSystemEventListener())));
        }
        if (!_clientConnected) {
            _client.connect(host, port);
            _clientConnected = true;
        }
    }

    /**
     * Stops the task client.
     */
    public void stopTaskClient() {
        if (_client != null) {
            if (_clientConnected) {
                try {
                    _client.disconnect();
                } catch (Throwable t) {
                    // just to keep checkstyle happy ("Must have at least one statement.")
                    t.getMessage();
                }
                _clientConnected = false;
            }
            _client = null;
        }
    }

    /**
     * Completes all tasks for the specified user ids, auto-connecting to the task server with the default host and port.
     * @param userIds the user ids to complete the tasks for
     * @return if there might be more user tasks to complete
     * @throws Exception if a problem occurred
     */
    public boolean completeTasksForUsers(String... userIds) throws Exception {
        return completeTasksForUsers(DEFAULT_HOST, DEFAULT_PORT, userIds);
    }

    /**
     * Completes all tasks for the specified user ids, connecting to the task server with the specified host and port.
     * @param host the task server host
     * @param port the task server port
     * @param userIds the user ids to complete the tasks for
     * @return if there might be more user tasks to complete
     * @throws Exception if a problem occurred
     */
    public boolean completeTasksForUsers(String host, int port, String... userIds) throws Exception {
        boolean keepWorking = false;
        startTaskClient(host, port);
        for (String userId : userIds) {
            BlockingTaskSummaryResponseHandler btsrh =  new BlockingTaskSummaryResponseHandler();
            _client.getTasksOwned(userId, Locale.getDefault().toString().replaceAll("_", "-"), btsrh);
            List<TaskSummary> tasks = btsrh.getResults();
            for (TaskSummary task : tasks) {
                if (Status.Completed.equals(task.getStatus())) {
                    continue;
                }
                User user = task.getActualOwner();
                BlockingTaskOperationResponseHandler btorh = new BlockingTaskOperationResponseHandler();
                _client.start(task.getId(), user.getId(), btorh);
                btorh.waitTillDone(10000);
                btorh = new BlockingTaskOperationResponseHandler();
                _client.complete(task.getId(), user.getId(), null, btorh);
                btorh.waitTillDone(10000);
                keepWorking = true;
            }
        }
        stopTaskClient();
        Thread.sleep(1000);
        return keepWorking;
    }

}
