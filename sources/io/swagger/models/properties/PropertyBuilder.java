package io.swagger.models.properties;

import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/PropertyBuilder.class */
public class PropertyBuilder {
    static Logger LOGGER = LoggerFactory.getLogger((Class<?>) PropertyBuilder.class);

    public static Property build(String type, String format, Map<PropertyId, Object> args) {
        Map<PropertyId, Object> fixedArgs;
        Processor processor = Processor.fromType(type, format);
        if (processor == null) {
            return null;
        }
        Map<PropertyId, Object> safeArgs = args == null ? Collections.emptyMap() : args;
        if (format != null) {
            fixedArgs = new EnumMap(PropertyId.class);
            fixedArgs.putAll(safeArgs);
            fixedArgs.put(PropertyId.FORMAT, format);
        } else {
            fixedArgs = safeArgs;
        }
        return processor.build(fixedArgs);
    }

    public static Property merge(Property property, Map<PropertyId, Object> args) {
        Processor processor;
        if (args != null && !args.isEmpty() && (processor = Processor.fromProperty(property)) != null) {
            processor.merge(property, args);
        }
        return property;
    }

    public static Model toModel(Property property) {
        Processor processor = Processor.fromProperty(property);
        if (processor != null) {
            return processor.toModel(property);
        }
        return null;
    }

    /* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/PropertyBuilder$PropertyId.class */
    public enum PropertyId {
        ENUM("enum"),
        TITLE("title"),
        DESCRIPTION("description"),
        DEFAULT("default"),
        PATTERN("pattern"),
        DESCRIMINATOR("discriminator"),
        MIN_ITEMS("minItems"),
        MAX_ITEMS("maxItems"),
        MIN_PROPERTIES("minProperties"),
        MAX_PROPERTIES("maxProperties"),
        MIN_LENGTH("minLength"),
        MAX_LENGTH("maxLength"),
        MINIMUM("minimum"),
        MAXIMUM("maximum"),
        EXCLUSIVE_MINIMUM("exclusiveMinimum"),
        EXCLUSIVE_MAXIMUM("exclusiveMaximum"),
        UNIQUE_ITEMS("uniqueItems"),
        EXAMPLE("example"),
        TYPE("type"),
        FORMAT("format"),
        READ_ONLY(DefaultTransactionDefinition.READ_ONLY_MARKER),
        VENDOR_EXTENSIONS("vendorExtensions");

        private String propertyName;

        PropertyId(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyName() {
            return this.propertyName;
        }

        public <T> T findValue(Map<PropertyId, Object> map) {
            return (T) map.get(this);
        }
    }

    /* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/PropertyBuilder$Processor.class */
    private enum Processor {
        BOOLEAN(BooleanProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.1
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return BooleanProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public BooleanProperty create() {
                return new BooleanProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Property merge(Property property, Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof BooleanProperty) {
                    BooleanProperty resolved = (BooleanProperty) property;
                    if (args.containsKey(PropertyId.DEFAULT)) {
                        String value = (String) PropertyId.DEFAULT.findValue(args);
                        if (value != null) {
                            resolved.setDefault(value);
                        } else {
                            resolved.setDefault((Boolean) null);
                        }
                    }
                }
                return property;
            }
        },
        STRING(StringProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.2
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return StringProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public StringProperty create() {
                return new StringProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Property merge(Property property, Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof StringProperty) {
                    mergeString((StringProperty) property, args);
                }
                return property;
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Model toModel(Property property) {
                if (isType(property)) {
                    return createStringModel((StringProperty) property);
                }
                return null;
            }
        },
        BYTE_ARRAY(ByteArrayProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.3
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return ByteArrayProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public ByteArrayProperty create() {
                return new ByteArrayProperty();
            }
        },
        DATE(DateProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.4
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return DateProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public DateProperty create() {
                return new DateProperty();
            }
        },
        DATE_TIME(DateTimeProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.5
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return DateTimeProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public DateTimeProperty create() {
                return new DateTimeProperty();
            }
        },
        INT(IntegerProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.6
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return IntegerProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public IntegerProperty create() {
                return new IntegerProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Property merge(Property property, Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof IntegerProperty) {
                    IntegerProperty resolved = (IntegerProperty) property;
                    mergeNumeric(resolved, args);
                    if (args.containsKey(PropertyId.DEFAULT)) {
                        String value = (String) PropertyId.DEFAULT.findValue(args);
                        if (value != null) {
                            resolved.setDefault(value);
                        } else {
                            resolved.setDefault((Integer) null);
                        }
                    }
                }
                return property;
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Model toModel(Property property) {
                if (isType(property)) {
                    IntegerProperty resolved = (IntegerProperty) property;
                    ModelImpl model = createModel(resolved);
                    Integer defaultValue = resolved.getDefault();
                    if (defaultValue != null) {
                        model.setDefaultValue(defaultValue.toString());
                    }
                    return model;
                }
                return null;
            }
        },
        LONG(LongProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.7
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return LongProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public LongProperty create() {
                return new LongProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Property merge(Property property, Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof LongProperty) {
                    LongProperty resolved = (LongProperty) property;
                    mergeNumeric(resolved, args);
                    if (args.containsKey(PropertyId.DEFAULT)) {
                        String value = (String) PropertyId.DEFAULT.findValue(args);
                        if (value != null) {
                            resolved.setDefault(value);
                        } else {
                            resolved.setDefault((Long) null);
                        }
                    }
                }
                return property;
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Model toModel(Property property) {
                if (isType(property)) {
                    LongProperty resolved = (LongProperty) property;
                    ModelImpl model = createModel(resolved);
                    Long defaultValue = resolved.getDefault();
                    if (defaultValue != null) {
                        model.setDefaultValue(defaultValue.toString());
                    }
                    return model;
                }
                return null;
            }
        },
        FLOAT(FloatProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.8
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return FloatProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public FloatProperty create() {
                return new FloatProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Property merge(Property property, Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof FloatProperty) {
                    FloatProperty resolved = (FloatProperty) property;
                    mergeNumeric(resolved, args);
                    if (args.containsKey(PropertyId.DEFAULT)) {
                        String value = (String) PropertyId.DEFAULT.findValue(args);
                        if (value != null) {
                            resolved.setDefault(value);
                        } else {
                            resolved.setDefault((Float) null);
                        }
                    }
                }
                return property;
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Model toModel(Property property) {
                if (isType(property)) {
                    FloatProperty resolved = (FloatProperty) property;
                    ModelImpl model = createModel(resolved);
                    Float defaultValue = resolved.getDefault();
                    if (defaultValue != null) {
                        model.setDefaultValue(defaultValue.toString());
                    }
                    return model;
                }
                return null;
            }
        },
        DOUBLE(DoubleProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.9
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return DoubleProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public DoubleProperty create() {
                return new DoubleProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Property merge(Property property, Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof DoubleProperty) {
                    DoubleProperty resolved = (DoubleProperty) property;
                    mergeNumeric(resolved, args);
                    if (args.containsKey(PropertyId.DEFAULT)) {
                        String value = (String) PropertyId.DEFAULT.findValue(args);
                        if (value != null) {
                            resolved.setDefault(value);
                        } else {
                            resolved.setDefault((Double) null);
                        }
                    }
                }
                return property;
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Model toModel(Property property) {
                if (isType(property)) {
                    DoubleProperty resolved = (DoubleProperty) property;
                    ModelImpl model = createModel(resolved);
                    Double defaultValue = resolved.getDefault();
                    if (defaultValue != null) {
                        model.setDefaultValue(defaultValue.toString());
                    }
                    return model;
                }
                return null;
            }
        },
        INTEGER(BaseIntegerProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.10
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return BaseIntegerProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public BaseIntegerProperty create() {
                return new BaseIntegerProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Property merge(Property property, Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof BaseIntegerProperty) {
                    BaseIntegerProperty resolved = (BaseIntegerProperty) property;
                    mergeNumeric(resolved, args);
                }
                return property;
            }
        },
        DECIMAL(DecimalProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.11
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return DecimalProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public DecimalProperty create() {
                return new DecimalProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Property merge(Property property, Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof DecimalProperty) {
                    DecimalProperty resolved = (DecimalProperty) property;
                    mergeNumeric(resolved, args);
                }
                return property;
            }
        },
        FILE(FileProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.12
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return FileProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public FileProperty create() {
                return new FileProperty();
            }
        },
        REFERENCE(RefProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.13
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return RefProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public RefProperty create() {
                return new RefProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Model toModel(Property property) {
                if (property instanceof RefProperty) {
                    RefProperty resolved = (RefProperty) property;
                    RefModel model = new RefModel(resolved.get$ref());
                    model.setDescription(resolved.getDescription());
                    return model;
                }
                return null;
            }
        },
        E_MAIL(EmailProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.14
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return EmailProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public EmailProperty create() {
                return new EmailProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Property merge(Property property, Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof EmailProperty) {
                    mergeString((EmailProperty) property, args);
                }
                return property;
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Model toModel(Property property) {
                if (isType(property)) {
                    return createStringModel((StringProperty) property);
                }
                return null;
            }
        },
        UUID(UUIDProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.15
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return UUIDProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public UUIDProperty create() {
                return new UUIDProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Property merge(Property property, Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof UUIDProperty) {
                    UUIDProperty resolved = (UUIDProperty) property;
                    if (args.containsKey(PropertyId.DEFAULT)) {
                        String value = (String) PropertyId.DEFAULT.findValue(args);
                        property.setDefault(value);
                    }
                    if (args.containsKey(PropertyId.MIN_LENGTH)) {
                        Integer value2 = (Integer) PropertyId.MIN_LENGTH.findValue(args);
                        resolved.setMinLength(value2);
                    }
                    if (args.containsKey(PropertyId.MAX_LENGTH)) {
                        Integer value3 = (Integer) PropertyId.MAX_LENGTH.findValue(args);
                        resolved.setMaxLength(value3);
                    }
                    if (args.containsKey(PropertyId.PATTERN)) {
                        String value4 = (String) PropertyId.PATTERN.findValue(args);
                        resolved.setPattern(value4);
                    }
                }
                return property;
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Model toModel(Property property) {
                if (isType(property)) {
                    UUIDProperty resolved = (UUIDProperty) property;
                    ModelImpl model = createModel(resolved);
                    model.setDefaultValue(resolved.getDefault());
                    return model;
                }
                return null;
            }
        },
        OBJECT(ObjectProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.16
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                if (ObjectProperty.isType(type, format)) {
                    return true;
                }
                if ("object".equals(type) && format == null) {
                    PropertyBuilder.LOGGER.debug("no format specified for object type, falling back to object");
                    return true;
                }
                return false;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public ObjectProperty create() {
                return new ObjectProperty();
            }
        },
        ARRAY(ArrayProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.17
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return ArrayProperty.isType(type);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public ArrayProperty create() {
                return new ArrayProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Model toModel(Property property) {
                if (property instanceof ArrayProperty) {
                    ArrayProperty resolved = (ArrayProperty) property;
                    ArrayModel model = new ArrayModel().items(resolved.getItems()).description(resolved.getDescription());
                    return model;
                }
                return null;
            }
        },
        MAP(MapProperty.class) { // from class: io.swagger.models.properties.PropertyBuilder.Processor.18
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            protected boolean isType(String type, String format) {
                return MapProperty.isType(type, format);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public MapProperty create() {
                return new MapProperty();
            }

            @Override // io.swagger.models.properties.PropertyBuilder.Processor
            public Model toModel(Property property) {
                if (property instanceof MapProperty) {
                    MapProperty resolved = (MapProperty) property;
                    return createModel(property).additionalProperties(resolved.getAdditionalProperties());
                }
                return null;
            }
        };

        private final Class<? extends Property> type;

        protected abstract boolean isType(String str, String str2);

        protected abstract Property create();

        Processor(Class cls) {
            this.type = cls;
        }

        public static Processor fromType(String type, String format) {
            Processor[] arr$ = values();
            for (Processor item : arr$) {
                if (item.isType(type, format)) {
                    return item;
                }
            }
            PropertyBuilder.LOGGER.debug("no property for " + type + ", " + format);
            return null;
        }

        public static Processor fromProperty(Property property) {
            Processor[] arr$ = values();
            for (Processor item : arr$) {
                if (item.isType(property)) {
                    return item;
                }
            }
            PropertyBuilder.LOGGER.error("no property for " + (property == null ? "null" : property.getClass().getName()));
            return null;
        }

        protected boolean isType(Property property) {
            return this.type.isInstance(property);
        }

        protected <N extends AbstractNumericProperty> N mergeNumeric(N property, Map<PropertyId, Object> args) {
            if (args.containsKey(PropertyId.MINIMUM)) {
                Double value = (Double) PropertyId.MINIMUM.findValue(args);
                property.setMinimum(value);
            }
            if (args.containsKey(PropertyId.MAXIMUM)) {
                Double value2 = (Double) PropertyId.MAXIMUM.findValue(args);
                property.setMaximum(value2);
            }
            if (args.containsKey(PropertyId.EXCLUSIVE_MINIMUM)) {
                Boolean value3 = (Boolean) PropertyId.EXCLUSIVE_MINIMUM.findValue(args);
                property.setExclusiveMinimum(value3);
            }
            if (args.containsKey(PropertyId.EXCLUSIVE_MAXIMUM)) {
                Boolean value4 = (Boolean) PropertyId.EXCLUSIVE_MAXIMUM.findValue(args);
                property.setExclusiveMaximum(value4);
            }
            return property;
        }

        protected <N extends StringProperty> N mergeString(N property, Map<PropertyId, Object> args) {
            if (args.containsKey(PropertyId.DEFAULT)) {
                String value = (String) PropertyId.DEFAULT.findValue(args);
                property.setDefault(value);
            }
            if (args.containsKey(PropertyId.MIN_LENGTH)) {
                Integer value2 = (Integer) PropertyId.MIN_LENGTH.findValue(args);
                property.setMinLength(value2);
            }
            if (args.containsKey(PropertyId.MAX_LENGTH)) {
                Integer value3 = (Integer) PropertyId.MAX_LENGTH.findValue(args);
                property.setMaxLength(value3);
            }
            if (args.containsKey(PropertyId.PATTERN)) {
                String value4 = (String) PropertyId.PATTERN.findValue(args);
                property.setPattern(value4);
            }
            if (args.containsKey(PropertyId.ENUM)) {
                List<String> value5 = (List) PropertyId.ENUM.findValue(args);
                property.setEnum(value5);
            }
            return property;
        }

        protected ModelImpl createModel(Property property) {
            return new ModelImpl().type(property.getType()).format(property.getFormat()).description(property.getDescription());
        }

        protected ModelImpl createStringModel(StringProperty property) {
            ModelImpl model = createModel(property);
            model.setDefaultValue(property.getDefault());
            return model;
        }

        public Property build(Map<PropertyId, Object> args) {
            return merge(create(), args);
        }

        public Property merge(Property property, Map<PropertyId, Object> args) {
            if (args.containsKey(PropertyId.READ_ONLY)) {
                property.setReadOnly((Boolean) PropertyId.READ_ONLY.findValue(args));
            }
            if (property instanceof AbstractProperty) {
                AbstractProperty resolved = (AbstractProperty) property;
                if (resolved.getFormat() == null) {
                    resolved.setFormat((String) PropertyId.FORMAT.findValue(args));
                }
                if (args.containsKey(PropertyId.TITLE)) {
                    String value = (String) PropertyId.TITLE.findValue(args);
                    resolved.setTitle(value);
                }
                if (args.containsKey(PropertyId.DESCRIPTION)) {
                    String value2 = (String) PropertyId.DESCRIPTION.findValue(args);
                    resolved.setDescription(value2);
                }
                if (args.containsKey(PropertyId.EXAMPLE)) {
                    String value3 = (String) PropertyId.EXAMPLE.findValue(args);
                    resolved.setExample(value3);
                }
                if (args.containsKey(PropertyId.VENDOR_EXTENSIONS)) {
                    Map<String, Object> value4 = (Map) PropertyId.VENDOR_EXTENSIONS.findValue(args);
                    resolved.setVendorExtensionMap(value4);
                }
            }
            return property;
        }

        public Model toModel(Property property) {
            return createModel(property);
        }
    }
}
