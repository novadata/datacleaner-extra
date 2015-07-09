package com.novacloud.data.datacleaner.extra;

import org.apache.metamodel.jdbc.JdbcDataContext;
import org.apache.metamodel.schema.TableType;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Created by yong on 10/26/14.
 */
public class PhoenixJdbcDataContext extends JdbcDataContext {
    /**
     * Creates the strategy based on a {@link javax.sql.DataSource}
     *
     * @param dataSource the data source
     */
    public PhoenixJdbcDataContext(DataSource dataSource) {
        super(dataSource);
    }

    public PhoenixJdbcDataContext(Connection connection, TableType[] tableTypes, String catalogName) {
        super(connection,tableTypes,catalogName);
    }
    @Override
    protected String[] getSchemaNamesInternal() {
       return  new String[]{null};

    }

}
