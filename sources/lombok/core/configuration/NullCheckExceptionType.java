package lombok.core.configuration;

import lombok.core.handlers.HandlerUtil;

@ExampleValueString("[NullPointerException | IllegalArgumentException]")
/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/NullCheckExceptionType.SCL.lombok */
public enum NullCheckExceptionType {
    ILLEGAL_ARGUMENT_EXCEPTION { // from class: lombok.core.configuration.NullCheckExceptionType.1
        @Override // lombok.core.configuration.NullCheckExceptionType
        public String toExceptionMessage(String fieldName) {
            return String.valueOf(fieldName) + " is null";
        }

        @Override // lombok.core.configuration.NullCheckExceptionType
        public String getExceptionType() {
            return "java.lang.IllegalArgumentException";
        }
    },
    NULL_POINTER_EXCEPTION { // from class: lombok.core.configuration.NullCheckExceptionType.2
        @Override // lombok.core.configuration.NullCheckExceptionType
        public String toExceptionMessage(String fieldName) {
            return fieldName;
        }

        @Override // lombok.core.configuration.NullCheckExceptionType
        public String getExceptionType() {
            return HandlerUtil.DEFAULT_EXCEPTION_FOR_NON_NULL;
        }
    };

    public abstract String toExceptionMessage(String str);

    public abstract String getExceptionType();

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static NullCheckExceptionType[] valuesCustom() {
        NullCheckExceptionType[] nullCheckExceptionTypeArrValuesCustom = values();
        int length = nullCheckExceptionTypeArrValuesCustom.length;
        NullCheckExceptionType[] nullCheckExceptionTypeArr = new NullCheckExceptionType[length];
        System.arraycopy(nullCheckExceptionTypeArrValuesCustom, 0, nullCheckExceptionTypeArr, 0, length);
        return nullCheckExceptionTypeArr;
    }

    /* synthetic */ NullCheckExceptionType(NullCheckExceptionType nullCheckExceptionType) {
        this();
    }
}
