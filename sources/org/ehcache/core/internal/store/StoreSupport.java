package org.ehcache.core.internal.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.ehcache.config.ResourceType;
import org.ehcache.core.spi.store.Store;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceProvider;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/store/StoreSupport.class */
public final class StoreSupport {
    private StoreSupport() {
    }

    public static Store.Provider selectStoreProvider(ServiceProvider<Service> serviceProvider, Set<ResourceType<?>> resourceTypes, Collection<ServiceConfiguration<?>> serviceConfigs) {
        Collection<U> servicesOfType = serviceProvider.getServicesOfType(Store.Provider.class);
        int highRank = 0;
        List<Store.Provider> rankingProviders = new ArrayList<>();
        for (U provider : servicesOfType) {
            int rank = provider.rank(resourceTypes, serviceConfigs);
            if (rank > highRank) {
                highRank = rank;
                rankingProviders.clear();
                rankingProviders.add(provider);
            } else if (rank != 0 && rank == highRank) {
                rankingProviders.add(provider);
            }
        }
        if (rankingProviders.isEmpty()) {
            StringBuilder sb = new StringBuilder("No Store.Provider found to handle configured resource types ");
            sb.append(resourceTypes);
            sb.append(" from ");
            formatStoreProviders(servicesOfType, sb);
            throw new IllegalStateException(sb.toString());
        }
        if (rankingProviders.size() > 1) {
            StringBuilder sb2 = new StringBuilder("Multiple Store.Providers found to handle configured resource types ");
            sb2.append(resourceTypes);
            sb2.append(": ");
            formatStoreProviders(rankingProviders, sb2);
            throw new IllegalStateException(sb2.toString());
        }
        return rankingProviders.get(0);
    }

    private static StringBuilder formatStoreProviders(Collection<Store.Provider> storeProviders, StringBuilder sb) {
        sb.append('{');
        boolean prependSeparator = false;
        for (Store.Provider provider : storeProviders) {
            if (prependSeparator) {
                sb.append(", ");
            } else {
                prependSeparator = true;
            }
            sb.append(provider.getClass().getName());
        }
        sb.append('}');
        return sb;
    }
}
