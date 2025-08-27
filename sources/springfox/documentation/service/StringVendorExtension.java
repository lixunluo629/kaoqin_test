package springfox.documentation.service;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/StringVendorExtension.class */
public class StringVendorExtension implements VendorExtension<String> {
    private String name;
    private String value;

    public StringVendorExtension(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override // springfox.documentation.service.VendorExtension
    public String getName() {
        return this.name;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // springfox.documentation.service.VendorExtension
    public String getValue() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StringVendorExtension that = (StringVendorExtension) o;
        return Objects.equal(this.name, that.name) && Objects.equal(this.value, that.value);
    }

    public int hashCode() {
        return Objects.hashCode(this.name, this.value);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", this.name).add("value", this.value).toString();
    }
}
