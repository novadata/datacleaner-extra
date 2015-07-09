package com.novacloud.data.datacleaner.extra;

import org.apache.metamodel.UpdateableDataContext;
import org.eobjects.analyzer.connection.DataSourceDatastoreConnection;
import org.eobjects.analyzer.connection.JdbcDatastore;
import org.eobjects.analyzer.connection.UpdateableDatastoreConnectionImpl;
import org.eobjects.analyzer.connection.UsageAwareDatastoreConnection;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Created by yong on 10/26/14.
 */
public class PhoenixJdbcDatastore extends JdbcDatastore {

    public  PhoenixJdbcDatastore(JdbcDatastore jdbcDatastore){
        super(jdbcDatastore.getName(),jdbcDatastore.getJdbcUrl(),jdbcDatastore.getDriverClass(),null,null,false);
    }
    public PhoenixJdbcDatastore(String name, String jdbcUrl, String driverClass) {
        super(name, jdbcUrl, driverClass,null,null,false);
    }
    @Override
    protected UsageAwareDatastoreConnection<UpdateableDataContext> createDatastoreConnection() {
            if (isMultipleConnections()) {
                final DataSource dataSource = createDataSource();
                return new DataSourceDatastoreConnection(dataSource, getTableTypes(), null, this);
            } else {
                final Connection connection = createConnection();
                final UpdateableDataContext dataContext = new PhoenixJdbcDataContext(connection, getTableTypes(), null);
                return new UpdateableDatastoreConnectionImpl<UpdateableDataContext>(dataContext, this);
            }

    }

}
