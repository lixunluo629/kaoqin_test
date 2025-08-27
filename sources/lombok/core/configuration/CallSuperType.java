package lombok.core.configuration;

/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/CallSuperType.SCL.lombok */
public enum CallSuperType {
    CALL,
    SKIP,
    WARN;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static CallSuperType[] valuesCustom() {
        CallSuperType[] callSuperTypeArrValuesCustom = values();
        int length = callSuperTypeArrValuesCustom.length;
        CallSuperType[] callSuperTypeArr = new CallSuperType[length];
        System.arraycopy(callSuperTypeArrValuesCustom, 0, callSuperTypeArr, 0, length);
        return callSuperTypeArr;
    }
}
