package springfox.documentation.schema;

import com.google.common.base.Optional;
import springfox.documentation.service.AllowableValues;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/schema/ModelRef.class */
public class ModelRef {
    private final String type;
    private final boolean isMap;
    private final Optional<String> itemType;
    private final Optional<AllowableValues> allowableValues;

    public ModelRef(String type, String itemType) {
        this(type, itemType, false);
    }

    public ModelRef(String type, String itemType, AllowableValues allowableValues) {
        this(type, itemType, allowableValues, false);
    }

    public ModelRef(String type, AllowableValues allowableValues) {
        this(type, (String) null, allowableValues);
    }

    public ModelRef(String type, String itemType, boolean isMap) {
        this(type, itemType, null, isMap);
    }

    public ModelRef(String type, String itemType, AllowableValues allowableValues, boolean isMap) {
        this.type = type;
        this.isMap = isMap;
        this.allowableValues = Optional.fromNullable(allowableValues);
        this.itemType = Optional.fromNullable(itemType);
    }

    public ModelRef(String type) {
        this(type, (String) null, (AllowableValues) null);
    }

    public String getType() {
        return this.type;
    }

    public boolean isCollection() {
        return this.itemType.isPresent() && !this.isMap;
    }

    public boolean isMap() {
        return this.itemType.isPresent() && this.isMap;
    }

    public String getItemType() {
        return this.itemType.orNull();
    }

    public AllowableValues getAllowableValues() {
        return this.allowableValues.orNull();
    }
}
