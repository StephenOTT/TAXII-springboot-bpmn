package io.digitalstate.taxii.mongo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TenantDbContext {

    //@TODO Add logging

    @Value("${taxi.tenant.db_name_prefix:taxii_}")
    private String DB_NAME_PREFIX;

    @Value("${taxi.tenant.default_db_name:default}")
    private String DEFAULT_DB_NAME;

    /**
     * Sets the DB name for the current thread.
     * Name is set in a ThreadLocal.
     * Appends the Db Name Prefix to the dbName
     *
     * If Database name is null, the db connection will fallback to the default DB.
     * @param dbName
     */
    public void setDatabaseNameForCurrentThread(final String dbName){
        MultiTenantMongoDbFactory.setDatabaseNameForCurrentThread(DB_NAME_PREFIX + dbName);
    }

    public void setDatabaseNameToDefaultForCurrentThread(){
        MultiTenantMongoDbFactory.setDatabaseNameForCurrentThread(DB_NAME_PREFIX + DEFAULT_DB_NAME);
    }

    public void getDatabaseNameForCurrentThread(){
        MultiTenantMongoDbFactory.getDatabaseNameForCurrentThread();
    }
}
