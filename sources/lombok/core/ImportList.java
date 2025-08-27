package lombok.core;

import java.util.Collection;

/* loaded from: lombok-1.16.22.jar:lombok/core/ImportList.SCL.lombok */
public interface ImportList {
    String getFullyQualifiedNameForSimpleName(String str);

    boolean hasStarImport(String str);

    Collection<String> applyNameToStarImports(String str, String str2);

    String applyUnqualifiedNameToPackage(String str);
}
