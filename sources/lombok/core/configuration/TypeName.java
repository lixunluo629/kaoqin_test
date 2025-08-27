package lombok.core.configuration;

/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/TypeName.SCL.lombok */
public final class TypeName {
    private final String name;

    private TypeName(String name) {
        this.name = name;
    }

    public static TypeName valueOf(String name) {
        return new TypeName(name);
    }

    public boolean equals(Object obj) {
        if (obj instanceof TypeName) {
            return this.name.equals(((TypeName) obj).name);
        }
        return false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return this.name;
    }
}
