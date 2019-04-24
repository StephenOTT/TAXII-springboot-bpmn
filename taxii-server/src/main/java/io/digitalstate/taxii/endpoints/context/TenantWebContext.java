package io.digitalstate.taxii.endpoints.context;

import io.digitalstate.taxii.mongo.TenantDbContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;

/**
 * Holds Tenant ID and Tenant Slug during the lifecycle of a Web Request / Web Context thread.
 * Use {@link TenantDbContext} to control which DB the Repositories and DB Templates are connected to.
 */
@Component
@RequestScope
public class TenantWebContext {

    @Autowired
    private TenantDbContext tenantDbContext;

    private String tenantSlug;
    private String tenantId;

    public String getTenantSlug() {
        return tenantSlug;
    }

    public void setTenantSlug(final String tenantSlug) {
        this.tenantSlug = tenantSlug;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public void setTenantId(final String tenantId, final boolean setDatabaseNameForCurrentThread) {
        setTenantId(tenantId);
        if (setDatabaseNameForCurrentThread){
            tenantDbContext.setDatabaseNameForCurrentThread(tenantId);
        }
    }

    public void setDatabaseToTenantIdForCurrentThread(){
        if (tenantId.isEmpty()) {
            throw new IllegalStateException("Unable to set Database to TenantId because TenantId is blank");
        } else {
            tenantDbContext.setDatabaseNameForCurrentThread(tenantId);
        }
    }

    public void setDatabaseToDefaultTenantForCurrentThread(){
        tenantDbContext.setDatabaseNameToDefaultForCurrentThread();
    }

}
