package ch.qos.logback.classic.gaffer;

import ch.qos.logback.core.joran.util.StringToObjectConverter;
import ch.qos.logback.core.joran.util.beans.BeanUtil;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.MetaProperty;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;

/* compiled from: PropertyUtil.groovy */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/gaffer/PropertyUtil.class */
public class PropertyUtil implements GroovyObject {
    private static /* synthetic */ ClassInfo $staticClassInfo;
    public static transient /* synthetic */ boolean __$stMC;
    private transient /* synthetic */ MetaClass metaClass;
    private static /* synthetic */ SoftReference $callSiteArray;

    public PropertyUtil() {
        $getCallSiteArray();
        this.metaClass = $getStaticMetaClass();
    }

    protected /* synthetic */ MetaClass $getStaticMetaClass() {
        if (getClass() != PropertyUtil.class) {
            return ScriptBytecodeAdapter.initMetaClass(this);
        }
        ClassInfo classInfo = $staticClassInfo;
        if (classInfo == null) {
            ClassInfo classInfo2 = ClassInfo.getClassInfo(getClass());
            classInfo = classInfo2;
            $staticClassInfo = classInfo2;
        }
        return classInfo.getMetaClass();
    }

    public /* synthetic */ MetaClass getMetaClass() {
        MetaClass metaClass = this.metaClass;
        if (metaClass != null) {
            return metaClass;
        }
        this.metaClass = $getStaticMetaClass();
        return this.metaClass;
    }

    public /* synthetic */ void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    public /* synthetic */ Object invokeMethod(String str, Object obj) {
        return getMetaClass().invokeMethod(this, str, obj);
    }

    public /* synthetic */ Object getProperty(String str) {
        return getMetaClass().getProperty(this, str);
    }

    public /* synthetic */ void setProperty(String str, Object obj) {
        getMetaClass().setProperty(this, str, obj);
    }

    private static /* synthetic */ void $createCallSiteArray_1(String[] strArr) {
        strArr[0] = "upperCaseFirstLetter";
        strArr[1] = "respondsTo";
        strArr[2] = "metaClass";
        strArr[3] = "toLowerCamelCase";
        strArr[4] = "hasProperty";
        strArr[5] = "followsTheValueOfConvention";
        strArr[6] = "getType";
        strArr[7] = "SINGLE_WITH_VALUE_OF_CONVENTION";
        strArr[8] = "SINGLE";
        strArr[9] = "hasAdderMethod";
        strArr[10] = "AS_COLLECTION";
        strArr[11] = "NA";
        strArr[12] = "toLowerCamelCase";
        strArr[13] = "hasProperty";
        strArr[14] = "getValueOfMethod";
        strArr[15] = "getType";
        strArr[16] = "invoke";
        strArr[17] = "SINGLE_WITH_VALUE_OF_CONVENTION";
        strArr[18] = "toLowerCamelCase";
        strArr[19] = "convertByValueMethod";
        strArr[20] = "SINGLE";
        strArr[21] = "toLowerCamelCase";
        strArr[22] = "AS_COLLECTION";
        strArr[23] = "upperCaseFirstLetter";
        strArr[24] = "length";
        strArr[25] = "length";
        strArr[26] = "<$constructor$>";
        strArr[27] = "getAt";
        strArr[28] = "call";
        strArr[29] = "length";
        strArr[30] = "plus";
        strArr[31] = "substring";
        strArr[32] = "transformFirstLetter";
    }

    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        String[] strArr = new String[33];
        $createCallSiteArray_1(strArr);
        return new CallSiteArray(PropertyUtil.class, strArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static /* synthetic */ org.codehaus.groovy.runtime.callsite.CallSite[] $getCallSiteArray() {
        /*
            java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.PropertyUtil.$callSiteArray
            if (r0 == 0) goto L14
            java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.PropertyUtil.$callSiteArray
            java.lang.Object r0 = r0.get()
            org.codehaus.groovy.runtime.callsite.CallSiteArray r0 = (org.codehaus.groovy.runtime.callsite.CallSiteArray) r0
            r1 = r0
            r4 = r1
            if (r0 != 0) goto L23
        L14:
            org.codehaus.groovy.runtime.callsite.CallSiteArray r0 = $createCallSiteArray()
            r4 = r0
            java.lang.ref.SoftReference r0 = new java.lang.ref.SoftReference
            r1 = r0
            r2 = r4
            r1.<init>(r2)
            ch.qos.logback.classic.gaffer.PropertyUtil.$callSiteArray = r0
        L23:
            r0 = r4
            org.codehaus.groovy.runtime.callsite.CallSite[] r0 = r0.array
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ch.qos.logback.classic.gaffer.PropertyUtil.$getCallSiteArray():org.codehaus.groovy.runtime.callsite.CallSite[]");
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    public static boolean hasAdderMethod(Object obj, String name) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        String addMethod = (__$stMC || BytecodeInterface8.disabledStandardMetaClass()) ? ShortTypeHandling.castToString(new GStringImpl(new Object[]{callSiteArr$getCallSiteArray[0].callStatic(PropertyUtil.class, name)}, new String[]{BeanUtil.PREFIX_ADDER, ""})) : ShortTypeHandling.castToString(new GStringImpl(new Object[]{upperCaseFirstLetter(name)}, new String[]{BeanUtil.PREFIX_ADDER, ""}));
        return DefaultTypeTransformation.booleanUnbox(callSiteArr$getCallSiteArray[1].call(callSiteArr$getCallSiteArray[2].callGetProperty(obj), obj, addMethod));
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public static NestingType nestingType(Object obj, String name, Object value) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        Object decapitalizedName = callSiteArr$getCallSiteArray[3].call(BeanUtil.class, name);
        MetaProperty metaProperty = (MetaProperty) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[4].call(obj, decapitalizedName), MetaProperty.class);
        if (ScriptBytecodeAdapter.compareNotEqual(metaProperty, (Object) null)) {
            boolean VALUE_IS_A_STRING = value instanceof String;
            if (VALUE_IS_A_STRING && DefaultTypeTransformation.booleanUnbox(callSiteArr$getCallSiteArray[5].call(StringToObjectConverter.class, callSiteArr$getCallSiteArray[6].call(metaProperty)))) {
                return (NestingType) ShortTypeHandling.castToEnum(callSiteArr$getCallSiteArray[7].callGetProperty(NestingType.class), NestingType.class);
            }
            return (NestingType) ShortTypeHandling.castToEnum(callSiteArr$getCallSiteArray[8].callGetProperty(NestingType.class), NestingType.class);
        }
        if (DefaultTypeTransformation.booleanUnbox(callSiteArr$getCallSiteArray[9].callStatic(PropertyUtil.class, obj, name))) {
            return (NestingType) ShortTypeHandling.castToEnum(callSiteArr$getCallSiteArray[10].callGetProperty(NestingType.class), NestingType.class);
        }
        return (NestingType) ShortTypeHandling.castToEnum(callSiteArr$getCallSiteArray[11].callGetProperty(NestingType.class), NestingType.class);
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public static Object convertByValueMethod(Object component, String name, String value) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        Object decapitalizedName = callSiteArr$getCallSiteArray[12].call(BeanUtil.class, name);
        MetaProperty metaProperty = (MetaProperty) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[13].call(component, decapitalizedName), MetaProperty.class);
        Method valueOfMethod = (Method) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[14].call(StringToObjectConverter.class, callSiteArr$getCallSiteArray[15].call(metaProperty)), Method.class);
        return callSiteArr$getCallSiteArray[16].call(valueOfMethod, (Object) null, value);
    }

    public static void attach(NestingType nestingType, Object component, Object subComponent, String name) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        if (ScriptBytecodeAdapter.isCase(nestingType, callSiteArr$getCallSiteArray[17].callGetProperty(NestingType.class))) {
            String name2 = ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[18].call(BeanUtil.class, name));
            Object value = callSiteArr$getCallSiteArray[19].callStatic(PropertyUtil.class, component, name2, subComponent);
            ScriptBytecodeAdapter.setProperty(value, (Class) null, component, ShortTypeHandling.castToString(new GStringImpl(new Object[]{name2}, new String[]{"", ""})));
        } else if (ScriptBytecodeAdapter.isCase(nestingType, callSiteArr$getCallSiteArray[20].callGetProperty(NestingType.class))) {
            ScriptBytecodeAdapter.setProperty(subComponent, (Class) null, component, ShortTypeHandling.castToString(new GStringImpl(new Object[]{ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[21].call(BeanUtil.class, name))}, new String[]{"", ""})));
        } else if (ScriptBytecodeAdapter.isCase(nestingType, callSiteArr$getCallSiteArray[22].callGetProperty(NestingType.class))) {
            String firstUpperName = ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[23].call(PropertyUtil.class, name));
            ScriptBytecodeAdapter.invokeMethodN(PropertyUtil.class, component, ShortTypeHandling.castToString(new GStringImpl(new Object[]{firstUpperName}, new String[]{BeanUtil.PREFIX_ADDER, ""})), new Object[]{subComponent});
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public static String transformFirstLetter(String s, Closure closure) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        if (!BytecodeInterface8.isOrigInt() || !BytecodeInterface8.isOrigZ() || __$stMC || BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareEqual(s, (Object) null) || ScriptBytecodeAdapter.compareEqual(callSiteArr$getCallSiteArray[24].call(s), 0)) {
                return s;
            }
        } else {
            if (ScriptBytecodeAdapter.compareEqual(s, (Object) null) || ScriptBytecodeAdapter.compareEqual(callSiteArr$getCallSiteArray[25].call(s), 0)) {
                return s;
            }
        }
        String firstLetter = ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[26].callConstructor(String.class, callSiteArr$getCallSiteArray[27].call(s, 0)));
        String modifiedFistLetter = ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[28].call(closure, firstLetter));
        if (ScriptBytecodeAdapter.compareEqual(callSiteArr$getCallSiteArray[29].call(s), 1)) {
            return modifiedFistLetter;
        }
        return ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[30].call(modifiedFistLetter, callSiteArr$getCallSiteArray[31].call(s, 1)));
    }

    /* compiled from: PropertyUtil.groovy */
    /* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/gaffer/PropertyUtil$_upperCaseFirstLetter_closure1.class */
    class _upperCaseFirstLetter_closure1 extends Closure implements GeneratedClosure {
        private static /* synthetic */ ClassInfo $staticClassInfo;
        public static transient /* synthetic */ boolean __$stMC;
        private static /* synthetic */ SoftReference $callSiteArray;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public _upperCaseFirstLetter_closure1(Object _outerInstance, Object _thisObject) {
            super(_outerInstance, _thisObject);
            $getCallSiteArray();
        }

        /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
        public Object call(String it) {
            return (__$stMC || BytecodeInterface8.disabledStandardMetaClass()) ? $getCallSiteArray()[1].callCurrent(this, it) : doCall(it);
        }

        protected /* synthetic */ MetaClass $getStaticMetaClass() {
            if (getClass() != _upperCaseFirstLetter_closure1.class) {
                return ScriptBytecodeAdapter.initMetaClass(this);
            }
            ClassInfo classInfo = $staticClassInfo;
            if (classInfo == null) {
                ClassInfo classInfo2 = ClassInfo.getClassInfo(getClass());
                classInfo = classInfo2;
                $staticClassInfo = classInfo2;
            }
            return classInfo.getMetaClass();
        }

        private static /* synthetic */ void $createCallSiteArray_1(String[] strArr) {
            strArr[0] = "toUpperCase";
            strArr[1] = "doCall";
        }

        private static /* synthetic */ CallSiteArray $createCallSiteArray() {
            String[] strArr = new String[2];
            $createCallSiteArray_1(strArr);
            return new CallSiteArray(_upperCaseFirstLetter_closure1.class, strArr);
        }

        /* JADX WARN: Removed duplicated region for block: B:6:0x0014  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private static /* synthetic */ org.codehaus.groovy.runtime.callsite.CallSite[] $getCallSiteArray() {
            /*
                java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.PropertyUtil._upperCaseFirstLetter_closure1.$callSiteArray
                if (r0 == 0) goto L14
                java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.PropertyUtil._upperCaseFirstLetter_closure1.$callSiteArray
                java.lang.Object r0 = r0.get()
                org.codehaus.groovy.runtime.callsite.CallSiteArray r0 = (org.codehaus.groovy.runtime.callsite.CallSiteArray) r0
                r1 = r0
                r4 = r1
                if (r0 != 0) goto L23
            L14:
                org.codehaus.groovy.runtime.callsite.CallSiteArray r0 = $createCallSiteArray()
                r4 = r0
                java.lang.ref.SoftReference r0 = new java.lang.ref.SoftReference
                r1 = r0
                r2 = r4
                r1.<init>(r2)
                ch.qos.logback.classic.gaffer.PropertyUtil._upperCaseFirstLetter_closure1.$callSiteArray = r0
            L23:
                r0 = r4
                org.codehaus.groovy.runtime.callsite.CallSite[] r0 = r0.array
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: ch.qos.logback.classic.gaffer.PropertyUtil._upperCaseFirstLetter_closure1.$getCallSiteArray():org.codehaus.groovy.runtime.callsite.CallSite[]");
        }

        /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
        public Object doCall(String it) {
            return $getCallSiteArray()[0].call(it);
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public static String upperCaseFirstLetter(String s) {
        return ShortTypeHandling.castToString($getCallSiteArray()[32].callStatic(PropertyUtil.class, s, new _upperCaseFirstLetter_closure1(PropertyUtil.class, PropertyUtil.class)));
    }
}
