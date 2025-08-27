package lombok.core.runtimeDependencies;

import java.util.List;

/* loaded from: lombok-1.16.22.jar:lombok/core/runtimeDependencies/RuntimeDependencyInfo.SCL.lombok */
public interface RuntimeDependencyInfo {
    List<String> getRuntimeDependentsDescriptions();

    List<String> getRuntimeDependencies();
}
