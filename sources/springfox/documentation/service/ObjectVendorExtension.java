package springfox.documentation.service;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/ObjectVendorExtension.class */
public class ObjectVendorExtension implements VendorExtension<List<VendorExtension>> {
    private final List<VendorExtension> properties = Lists.newArrayList();
    private final String name;

    public ObjectVendorExtension(String name) {
        this.name = name;
    }

    @Override // springfox.documentation.service.VendorExtension
    public String getName() {
        return this.name;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // springfox.documentation.service.VendorExtension
    public List<VendorExtension> getValue() {
        return ImmutableList.copyOf((Collection) this.properties);
    }

    public void addProperty(VendorExtension property) {
        this.properties.add(property);
    }

    public void replaceProperty(VendorExtension property) {
        Optional<VendorExtension> vendorProperty = Iterables.tryFind(this.properties, withName(property.getName()));
        if (vendorProperty.isPresent()) {
            this.properties.remove(vendorProperty.get());
        }
        this.properties.add(property);
    }

    private Predicate<VendorExtension> withName(final String name) {
        return new Predicate<VendorExtension>() { // from class: springfox.documentation.service.ObjectVendorExtension.1
            @Override // com.google.common.base.Predicate
            public boolean apply(VendorExtension input) {
                return input.getName().equals(name);
            }
        };
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ObjectVendorExtension that = (ObjectVendorExtension) o;
        return Objects.equal(this.properties, that.properties);
    }

    public int hashCode() {
        return Objects.hashCode(this.properties);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("properties", this.properties).add("name", this.name).toString();
    }
}
