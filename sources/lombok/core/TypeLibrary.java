package lombok.core;

import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: lombok-1.16.22.jar:lombok/core/TypeLibrary.SCL.lombok */
public class TypeLibrary {
    private final Map<String, String> unqualifiedToQualifiedMap;
    private final String unqualified;
    private final String qualified;
    private boolean locked;

    public TypeLibrary() {
        this.unqualifiedToQualifiedMap = new HashMap();
        this.unqualified = null;
        this.qualified = null;
    }

    public void lock() {
        this.locked = true;
    }

    private TypeLibrary(String fqnSingleton) {
        if (fqnSingleton.indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) != -1) {
            this.unqualifiedToQualifiedMap = new HashMap();
            this.unqualified = null;
            this.qualified = null;
            addType(fqnSingleton);
        } else {
            this.unqualifiedToQualifiedMap = null;
            this.qualified = fqnSingleton;
            int idx = fqnSingleton.lastIndexOf(46);
            if (idx == -1) {
                this.unqualified = fqnSingleton;
            } else {
                this.unqualified = fqnSingleton.substring(idx + 1);
            }
        }
        this.locked = true;
    }

    public static TypeLibrary createLibraryForSingleType(String fqnSingleton) {
        return new TypeLibrary(fqnSingleton);
    }

    public void addType(String fullyQualifiedTypeName) {
        String dotBased = fullyQualifiedTypeName.replace(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX, ".");
        if (this.locked) {
            throw new IllegalStateException(CellUtil.LOCKED);
        }
        int idx = fullyQualifiedTypeName.lastIndexOf(46);
        if (idx == -1) {
            throw new IllegalArgumentException("Only fully qualified types are allowed (and stuff in the default package is not palatable to us either!)");
        }
        String unqualified = fullyQualifiedTypeName.substring(idx + 1);
        if (this.unqualifiedToQualifiedMap == null) {
            throw new IllegalStateException("SingleType library");
        }
        this.unqualifiedToQualifiedMap.put(unqualified.replace(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX, "."), dotBased);
        this.unqualifiedToQualifiedMap.put(unqualified, dotBased);
        this.unqualifiedToQualifiedMap.put(fullyQualifiedTypeName, dotBased);
        this.unqualifiedToQualifiedMap.put(dotBased, dotBased);
        for (Map.Entry<String, String> e : LombokInternalAliasing.ALIASES.entrySet()) {
            if (fullyQualifiedTypeName.equals(e.getValue())) {
                this.unqualifiedToQualifiedMap.put(e.getKey(), dotBased);
            }
        }
        int iIndexOf = fullyQualifiedTypeName.indexOf(36, idx + 1);
        while (true) {
            int idx2 = iIndexOf;
            if (idx2 != -1) {
                String unq = fullyQualifiedTypeName.substring(idx2 + 1);
                this.unqualifiedToQualifiedMap.put(unq.replace(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX, "."), dotBased);
                this.unqualifiedToQualifiedMap.put(unq, dotBased);
                iIndexOf = fullyQualifiedTypeName.indexOf(36, idx2 + 1);
            } else {
                return;
            }
        }
    }

    public String toQualified(String typeReference) {
        if (this.unqualifiedToQualifiedMap == null) {
            if (typeReference.equals(this.unqualified) || typeReference.equals(this.qualified)) {
                return this.qualified;
            }
            for (Map.Entry<String, String> e : LombokInternalAliasing.ALIASES.entrySet()) {
                if (e.getKey().equals(typeReference)) {
                    return e.getValue();
                }
            }
            return null;
        }
        return this.unqualifiedToQualifiedMap.get(typeReference);
    }
}
