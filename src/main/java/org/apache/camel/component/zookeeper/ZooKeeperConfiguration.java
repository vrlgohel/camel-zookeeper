/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.RuntimeCamelException;

/**
 * <code>ZookeeperConfiguration</code> encapsulates the configuration that may
 * be applied to a {@link ZooKeeperComponent} and inherited by the
 * {@link ZooKeeperEndpoint}s it creates.
 *
 * @version $
 */
public class ZooKeeperConfiguration implements Cloneable {

    private int timeout = 5000;
    private int backoff = 5000;
    private List<String> servers;
    private boolean reuseConnection = true;
    private boolean changed;
    private int sessionId;
    private byte[] password;
    private String path;
    private boolean awaitCreation = true;
    private boolean repeat;
    private boolean listChildren;

    public void addZookeeperServer(String server) {
        if (servers == null) {
            servers = new ArrayList<String>();
        }
        servers.add(server);
        changed = true;
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public boolean shouldReuseConnection() {
        return reuseConnection;
    }

    public void setReuseConnection(boolean reuseConnection) {
        this.reuseConnection = reuseConnection;
        changed = true;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
        changed = true;
    }

    public boolean listChildren() {
        return listChildren;
    }

    public void setListChildren(boolean listChildren) {
        this.listChildren = listChildren;
    }

    public void clearChanged() {
        changed = false;
    }

    public boolean isChanged() {
        return changed;
    }

    public String getConnectString() {
        //return StringHelper.toStringCommaSeparated(servers);
        StringBuilder b = new StringBuilder();
        for(String server: servers) {
            b.append(server).append(",");
        }
        b.setLength(b.length() -1);
        return b.toString();

    }

    public byte[] getSessionPassword() {
        return password;
    }

    public int getSessionId(){
        return sessionId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean shouldRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat){
        this.repeat = repeat;
    }

    public ZooKeeperConfiguration copy() {
        try {
            return (ZooKeeperConfiguration)clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeCamelException(e);
        }
    }

    public boolean shouldAwaitExistence() {
        return awaitCreation;
    }

    public void setAwaitExistance(boolean awaitCreation)
    {
        this.awaitCreation = awaitCreation;
    }

    public long getBackoff() {
        return backoff;
    }

    public void setBackoff(int backoff) {
        this.backoff = backoff;
    }

}