package lombok.core.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lombok.core.configuration.ConfigurationSource;

/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/BubblingConfigurationResolver.SCL.lombok */
public class BubblingConfigurationResolver implements ConfigurationResolver {
    private final Iterable<ConfigurationSource> sources;

    public BubblingConfigurationResolver(Iterable<ConfigurationSource> sources) {
        this.sources = sources;
    }

    /* JADX WARN: Type inference failed for: r0v11, types: [T, java.util.ArrayList, java.util.List] */
    @Override // lombok.core.configuration.ConfigurationResolver
    public <T> T resolve(ConfigurationKey<T> configurationKey) {
        boolean zIsList = configurationKey.getType().isList();
        ArrayList<List> arrayList = null;
        Iterator<ConfigurationSource> it = this.sources.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ConfigurationSource.Result resultResolve = it.next().resolve(configurationKey);
            if (resultResolve != null) {
                if (zIsList) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add((List) resultResolve.getValue());
                }
                if (resultResolve.isAuthoritative()) {
                    if (!zIsList) {
                        return (T) resultResolve.getValue();
                    }
                }
            }
        }
        if (!zIsList) {
            return null;
        }
        if (arrayList == null) {
            return (T) Collections.emptyList();
        }
        ?? r0 = (T) new ArrayList();
        Collections.reverse(arrayList);
        for (List<ConfigurationSource.ListModification> list : arrayList) {
            if (list != null) {
                for (ConfigurationSource.ListModification listModification : list) {
                    r0.remove(listModification.getValue());
                    if (listModification.isAdded()) {
                        r0.add(listModification.getValue());
                    }
                }
            }
        }
        return r0;
    }
}
