package org.ehcache.spi.service;

@PluralService
/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/service/MaintainableService.class */
public interface MaintainableService extends Service {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/service/MaintainableService$MaintenanceScope.class */
    public enum MaintenanceScope {
        CACHE_MANAGER,
        CACHE
    }

    void startForMaintenance(ServiceProvider<? super MaintainableService> serviceProvider, MaintenanceScope maintenanceScope);
}
