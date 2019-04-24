package io.digitalstate.taxii.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

public class MultiTenantMongoDbFactory extends SimpleMongoDbFactory{

    private final String DEFAULT_DB_NAME;

    private static ThreadLocal<String> tlDbName = new InheritableThreadLocal<>();

    public MultiTenantMongoDbFactory(final MongoClient mongoClient,
                                     final String defaultDatabaseName,
                                     final String databaseNamePrefix) {

        super(mongoClient, databaseNamePrefix + defaultDatabaseName);
        DEFAULT_DB_NAME = databaseNamePrefix + defaultDatabaseName;
    }

    /**
     * Sets the Database Name for the current Thread.
     * This is used by the Database connection and Repositories to understand which database/tenant to connect to.
     * DatabaseName is prefixed with the DB Name Prefix (by default is "taxii_")
     * @param databaseName
     */
    public static void setDatabaseNameForCurrentThread(final String databaseName) {
        tlDbName.set(databaseName);
    }

    /**
     * Gets the Database Name for the Current Thread.
     * @return
     */
    public static String getDatabaseNameForCurrentThread() {
        return tlDbName.get();
    }

    @Override
    public MongoDatabase getDb() throws DataAccessException {
        final String tlName = tlDbName.get();
        // If the db is not set for the thread, then the default DB is used:
        final String dbToUse = tlName != null ? tlName : DEFAULT_DB_NAME;
        return super.getDb(dbToUse);
    }
}