/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.metamodel.hbase;

import org.apache.metamodel.schema.ColumnType;
import org.apache.metamodel.util.BaseObject;
import org.apache.metamodel.util.SimpleTableDef;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the configuration of MetaModel's HBase adaptor.
 */
public class HBaseConfiguration extends BaseObject implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_SCHEMA_NAME = "HBase";
    public static final String DEFAULT_ZOOKEEPER_HOSTNAME = "127.0.0.1";
    public static final int DEFAULT_ZOOKEEPER_PORT = 2181;

    private final String _schemaName;
    private final int _zookeeperPort;
    private final String _zookeeperHostname;
    private final SimpleTableDef[] _tableDefinitions;
    private final ColumnType _defaultRowKeyType;

    /**
     * Creates a {@link org.apache.metamodel.hbase.HBaseConfiguration} using default values.
     */
    public HBaseConfiguration() {
        this(DEFAULT_ZOOKEEPER_HOSTNAME, DEFAULT_ZOOKEEPER_PORT);
    }

    public HBaseConfiguration(String zookeeperHostname, int zookeeperPort) {
        this(DEFAULT_SCHEMA_NAME, zookeeperHostname, zookeeperPort, null, ColumnType.BINARY);
    }
    
    public HBaseConfiguration(String zookeeperHostname, int zookeeperPort, ColumnType defaultRowKeyType) {
        this(DEFAULT_SCHEMA_NAME, zookeeperHostname, zookeeperPort, null, defaultRowKeyType);
    }

    /**
     * Creates a {@link org.apache.metamodel.hbase.HBaseConfiguration} using detailed configuration
     * properties.
     * 
     * @param schemaName
     * @param zookeeperHostname
     * @param zookeeperPort
     * @param tableDefinitions
     * @param defaultRowKeyType
     */
    public HBaseConfiguration(String schemaName, String zookeeperHostname, int zookeeperPort,
            SimpleTableDef[] tableDefinitions, ColumnType defaultRowKeyType) {
        _schemaName = schemaName;
        _zookeeperHostname = zookeeperHostname;
        _zookeeperPort = zookeeperPort;
        _tableDefinitions = tableDefinitions;
        _defaultRowKeyType = defaultRowKeyType;
    }

    public String getSchemaName() {
        return _schemaName;
    }

    public String getZookeeperHostname() {
        return _zookeeperHostname;
    }

    public int getZookeeperPort() {
        return _zookeeperPort;
    }

    public SimpleTableDef[] getTableDefinitions() {
        return _tableDefinitions;
    }
    
    public ColumnType getDefaultRowKeyType() {
        return _defaultRowKeyType;
    }

    @Override
    protected void decorateIdentity(List<Object> list) {
        list.add(_schemaName);
        list.add(_zookeeperHostname);
        list.add(_zookeeperPort);
        list.add(_tableDefinitions);
        list.add(_defaultRowKeyType);
    }
}
