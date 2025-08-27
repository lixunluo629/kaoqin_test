package springfox.documentation.service;

import com.google.common.base.Objects;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/Tag.class */
public class Tag {
    private final String name;
    private final String description;

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return Objects.equal(this.name, tag.name) && Objects.equal(this.description, tag.description);
    }

    public int hashCode() {
        return Objects.hashCode(this.name, this.description);
    }
}
