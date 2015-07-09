package com.novacloud.data.datacleaner.extra;

/**
 * Created by yong on 10/24/14.
 */
public final class Config {

    /**
     * phoenix table schema
     */
    public static String DEFAULT_PHOENIX_DATASTORE = "phoenix";
    public static String COLUMN_NAME_USER_ID = "userId";
    public static String COLUMN_NAME_TASK_ID = "taskId";
    public static String COLUMN_NAME_WEBSITE_ID = "websiteId";
    /**
     * hbase table schema
     */
    public static String DEFAULT_HBASE_DATASTORE = "hbase";
    public static String TABLE_NAME_DATA = "data";
    public static String COLUMNFAMILY_NAME_DATA = "data";

    /**
     * Used by {@link com.novacloud.data.datacleaner.extra.NovaBaseHeadElement} and {@code resources/analysis-result.js}
     */
    public static String StaticResourcesRootURI = "http://api.novadata.cn/public/assets";

    public static int TIMEOUT_SECONDS = 60;
    public static int FREE_MAXROWS = 10000;//free user max rows
    public static final int MAXROWS = 100000;//other user max rows, -1:not limit
    public static int StoredAnnotatedRowsThreshold = 50;
}
