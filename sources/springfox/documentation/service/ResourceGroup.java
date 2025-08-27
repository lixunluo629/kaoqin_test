package springfox.documentation.service;

import com.google.common.base.Objects;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/ResourceGroup.class */
public class ResourceGroup {
    private final String groupName;
    private final Class<?> controllerClazz;
    private final Integer position;

    public ResourceGroup(String groupName, Class<?> controllerClazz) {
        this(groupName, controllerClazz, 0);
    }

    public ResourceGroup(String groupName, Class<?> controllerClazz, Integer position) {
        this.groupName = groupName;
        this.controllerClazz = controllerClazz;
        this.position = position;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Integer getPosition() {
        return this.position;
    }

    public Class<?> getControllerClass() {
        return this.controllerClazz;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ResourceGroup rhs = (ResourceGroup) obj;
        return Objects.equal(this.groupName, rhs.groupName) && Objects.equal(this.position, rhs.position) && Objects.equal(this.controllerClazz, rhs.controllerClazz);
    }

    public int hashCode() {
        return Objects.hashCode(this.groupName, this.controllerClazz, this.position);
    }

    public String toString() {
        return String.format("ResourceGroup{groupName='%s', position=%d, controller=%s}", this.groupName, this.position, this.controllerClazz.getName());
    }
}
