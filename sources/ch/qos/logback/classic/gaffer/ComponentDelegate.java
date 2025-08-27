package ch.qos.logback.classic.gaffer;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.joran.spi.NoAutoStartUtil;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.List;
import org.apache.catalina.Lifecycle;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.springframework.beans.PropertyAccessor;
import org.springframework.web.servlet.tags.form.InputTag;

/* compiled from: ComponentDelegate.groovy */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/gaffer/ComponentDelegate.class */
public class ComponentDelegate extends ContextAwareBase implements GroovyObject {
    private final Object component;
    private final List fieldsToCascade;
    private static /* synthetic */ ClassInfo $staticClassInfo;
    public static transient /* synthetic */ boolean __$stMC;
    private transient /* synthetic */ MetaClass metaClass;
    private static /* synthetic */ SoftReference $callSiteArray;

    protected /* synthetic */ MetaClass $getStaticMetaClass() {
        if (getClass() != ComponentDelegate.class) {
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

    public final Object getComponent() {
        return this.component;
    }

    public final List getFieldsToCascade() {
        return this.fieldsToCascade;
    }

    private static /* synthetic */ void $createCallSiteArray_1(String[] strArr) {
        strArr[0] = "plus";
        strArr[1] = "toUpperCase";
        strArr[2] = "getAt";
        strArr[3] = "getLabel";
        strArr[4] = "substring";
        strArr[5] = "getLabel";
        strArr[6] = "plus";
        strArr[7] = "toUpperCase";
        strArr[8] = "getAt";
        strArr[9] = "substring";
        strArr[10] = "nestingType";
        strArr[11] = "NA";
        strArr[12] = "addError";
        strArr[13] = "getLabelFistLetterInUpperCase";
        strArr[14] = "getComponentName";
        strArr[15] = "canonicalName";
        strArr[16] = "getClass";
        strArr[17] = "analyzeArgs";
        strArr[18] = "getAt";
        strArr[19] = "getAt";
        strArr[20] = "getAt";
        strArr[21] = "newInstance";
        strArr[22] = "hasProperty";
        strArr[23] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[24] = "<$constructor$>";
        strArr[25] = "cascadeFields";
        strArr[26] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[27] = "injectParent";
        strArr[28] = "DELEGATE_FIRST";
        strArr[29] = "call";
        strArr[30] = "notMarkedWithNoAutoStart";
        strArr[31] = Lifecycle.START_EVENT;
        strArr[32] = "attach";
        strArr[33] = "addError";
        strArr[34] = "getLabel";
        strArr[35] = "getComponentName";
        strArr[36] = "canonicalName";
        strArr[37] = "getClass";
        strArr[38] = "newInstance";
        strArr[39] = "hasProperty";
        strArr[40] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[41] = "<$constructor$>";
        strArr[42] = "cascadeFields";
        strArr[43] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[44] = "injectParent";
        strArr[45] = "DELEGATE_FIRST";
        strArr[46] = "call";
        strArr[47] = "notMarkedWithNoAutoStart";
        strArr[48] = Lifecycle.START_EVENT;
        strArr[49] = "attach";
        strArr[50] = "addError";
        strArr[51] = "canonicalName";
        strArr[52] = "getClass";
        strArr[53] = "iterator";
        strArr[54] = "metaClass";
        strArr[55] = "hasProperty";
        strArr[56] = "nestingType";
        strArr[57] = "NA";
        strArr[58] = "addError";
        strArr[59] = "getLabelFistLetterInUpperCase";
        strArr[60] = "getComponentName";
        strArr[61] = "canonicalName";
        strArr[62] = "getClass";
        strArr[63] = "attach";
        strArr[64] = InputTag.SIZE_ATTRIBUTE;
        strArr[65] = "addError";
        strArr[66] = "getAt";
        strArr[67] = "getAt";
        strArr[68] = "minus";
        strArr[69] = "getAt";
        strArr[70] = "minus";
        strArr[71] = InputTag.SIZE_ATTRIBUTE;
        strArr[72] = "parseClassArgument";
        strArr[73] = "getAt";
        strArr[74] = InputTag.SIZE_ATTRIBUTE;
        strArr[75] = "parseClassArgument";
        strArr[76] = InputTag.SIZE_ATTRIBUTE;
        strArr[77] = "parseNameArgument";
        strArr[78] = "getAt";
        strArr[79] = "parseClassArgument";
        strArr[80] = "getAt";
        strArr[81] = InputTag.SIZE_ATTRIBUTE;
        strArr[82] = "parseNameArgument";
        strArr[83] = "parseClassArgument";
        strArr[84] = "addError";
        strArr[85] = "canonicalName";
        strArr[86] = "getClass";
        strArr[87] = "addError";
        strArr[88] = "hasProperty";
        strArr[89] = "name";
    }

    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        String[] strArr = new String[90];
        $createCallSiteArray_1(strArr);
        return new CallSiteArray(ComponentDelegate.class, strArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static /* synthetic */ org.codehaus.groovy.runtime.callsite.CallSite[] $getCallSiteArray() {
        /*
            java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.ComponentDelegate.$callSiteArray
            if (r0 == 0) goto L14
            java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.ComponentDelegate.$callSiteArray
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
            ch.qos.logback.classic.gaffer.ComponentDelegate.$callSiteArray = r0
        L23:
            r0 = r4
            org.codehaus.groovy.runtime.callsite.CallSite[] r0 = r0.array
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ch.qos.logback.classic.gaffer.ComponentDelegate.$getCallSiteArray():org.codehaus.groovy.runtime.callsite.CallSite[]");
    }

    public ComponentDelegate(Object component) {
        $getCallSiteArray();
        this.fieldsToCascade = ScriptBytecodeAdapter.createList(new Object[0]);
        this.metaClass = $getStaticMetaClass();
        this.component = component;
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public String getLabel() {
        $getCallSiteArray();
        return "component";
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public String getLabelFistLetterInUpperCase() {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        return (__$stMC || BytecodeInterface8.disabledStandardMetaClass()) ? ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[0].call(callSiteArr$getCallSiteArray[1].call(callSiteArr$getCallSiteArray[2].call(callSiteArr$getCallSiteArray[3].callCurrent(this), 0)), callSiteArr$getCallSiteArray[4].call(callSiteArr$getCallSiteArray[5].callCurrent(this), 1))) : ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[6].call(callSiteArr$getCallSiteArray[7].call(callSiteArr$getCallSiteArray[8].call(getLabel(), 0)), callSiteArr$getCallSiteArray[9].call(getLabel(), 1)));
    }

    public void methodMissing(String name, Object args) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        NestingType nestingType = (NestingType) ShortTypeHandling.castToEnum(callSiteArr$getCallSiteArray[10].call(PropertyUtil.class, this.component, name, (Object) null), NestingType.class);
        if (ScriptBytecodeAdapter.compareEqual(nestingType, callSiteArr$getCallSiteArray[11].callGetProperty(NestingType.class))) {
            callSiteArr$getCallSiteArray[12].callCurrent(this, new GStringImpl(new Object[]{callSiteArr$getCallSiteArray[13].callCurrent(this), callSiteArr$getCallSiteArray[14].callCurrent(this), callSiteArr$getCallSiteArray[15].callGetProperty(callSiteArr$getCallSiteArray[16].call(this.component)), name}, new String[]{"", SymbolConstants.SPACE_SYMBOL, " of type [", "] has no appplicable [", "] property."}));
            return;
        }
        Object objCallCurrent = callSiteArr$getCallSiteArray[17].callCurrent(this, args);
        String subComponentName = ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[18].call(objCallCurrent, 0));
        Class clazz = ShortTypeHandling.castToClass(callSiteArr$getCallSiteArray[19].call(objCallCurrent, 1));
        Closure closure = (Closure) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[20].call(objCallCurrent, 2), Closure.class);
        if (!BytecodeInterface8.isOrigZ() || __$stMC || BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareNotEqual(clazz, (Object) null)) {
                Object subComponent = callSiteArr$getCallSiteArray[21].call(clazz);
                if (DefaultTypeTransformation.booleanUnbox(subComponentName) && DefaultTypeTransformation.booleanUnbox(callSiteArr$getCallSiteArray[22].call(subComponent, name))) {
                    ScriptBytecodeAdapter.setProperty(subComponentName, (Class) null, subComponent, "name");
                }
                if (subComponent instanceof ContextAware) {
                    ScriptBytecodeAdapter.setProperty(callSiteArr$getCallSiteArray[23].callGroovyObjectGetProperty(this), (Class) null, subComponent, CoreConstants.CONTEXT_SCOPE_VALUE);
                }
                if (DefaultTypeTransformation.booleanUnbox(closure)) {
                    ComponentDelegate subDelegate = (ComponentDelegate) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[24].callConstructor(ComponentDelegate.class, subComponent), ComponentDelegate.class);
                    callSiteArr$getCallSiteArray[25].callCurrent(this, subDelegate);
                    ScriptBytecodeAdapter.setGroovyObjectProperty(callSiteArr$getCallSiteArray[26].callGroovyObjectGetProperty(this), ComponentDelegate.class, subDelegate, CoreConstants.CONTEXT_SCOPE_VALUE);
                    callSiteArr$getCallSiteArray[27].callCurrent(this, subComponent);
                    ScriptBytecodeAdapter.setGroovyObjectProperty(subDelegate, ComponentDelegate.class, closure, "delegate");
                    ScriptBytecodeAdapter.setGroovyObjectProperty(callSiteArr$getCallSiteArray[28].callGetProperty(Closure.class), ComponentDelegate.class, closure, "resolveStrategy");
                    callSiteArr$getCallSiteArray[29].call(closure);
                }
                if ((subComponent instanceof LifeCycle) && DefaultTypeTransformation.booleanUnbox(callSiteArr$getCallSiteArray[30].call(NoAutoStartUtil.class, subComponent))) {
                    callSiteArr$getCallSiteArray[31].call(subComponent);
                }
                callSiteArr$getCallSiteArray[32].call(PropertyUtil.class, nestingType, this.component, subComponent, name);
                return;
            }
            callSiteArr$getCallSiteArray[33].callCurrent(this, new GStringImpl(new Object[]{name, callSiteArr$getCallSiteArray[34].callCurrent(this), callSiteArr$getCallSiteArray[35].callCurrent(this), callSiteArr$getCallSiteArray[36].callGetProperty(callSiteArr$getCallSiteArray[37].call(this.component))}, new String[]{"No 'class' argument specified for [", "] in ", SymbolConstants.SPACE_SYMBOL, " of type [", "]"}));
            return;
        }
        if (ScriptBytecodeAdapter.compareNotEqual(clazz, (Object) null)) {
            Object subComponent2 = callSiteArr$getCallSiteArray[38].call(clazz);
            if (DefaultTypeTransformation.booleanUnbox(subComponentName) && DefaultTypeTransformation.booleanUnbox(callSiteArr$getCallSiteArray[39].call(subComponent2, name))) {
                ScriptBytecodeAdapter.setProperty(subComponentName, (Class) null, subComponent2, "name");
            }
            if (subComponent2 instanceof ContextAware) {
                ScriptBytecodeAdapter.setProperty(callSiteArr$getCallSiteArray[40].callGroovyObjectGetProperty(this), (Class) null, subComponent2, CoreConstants.CONTEXT_SCOPE_VALUE);
            }
            if (DefaultTypeTransformation.booleanUnbox(closure)) {
                ComponentDelegate subDelegate2 = (ComponentDelegate) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[41].callConstructor(ComponentDelegate.class, subComponent2), ComponentDelegate.class);
                callSiteArr$getCallSiteArray[42].callCurrent(this, subDelegate2);
                ScriptBytecodeAdapter.setGroovyObjectProperty(callSiteArr$getCallSiteArray[43].callGroovyObjectGetProperty(this), ComponentDelegate.class, subDelegate2, CoreConstants.CONTEXT_SCOPE_VALUE);
                callSiteArr$getCallSiteArray[44].callCurrent(this, subComponent2);
                ScriptBytecodeAdapter.setGroovyObjectProperty(subDelegate2, ComponentDelegate.class, closure, "delegate");
                ScriptBytecodeAdapter.setGroovyObjectProperty(callSiteArr$getCallSiteArray[45].callGetProperty(Closure.class), ComponentDelegate.class, closure, "resolveStrategy");
                callSiteArr$getCallSiteArray[46].call(closure);
            }
            if ((subComponent2 instanceof LifeCycle) && DefaultTypeTransformation.booleanUnbox(callSiteArr$getCallSiteArray[47].call(NoAutoStartUtil.class, subComponent2))) {
                callSiteArr$getCallSiteArray[48].call(subComponent2);
            }
            callSiteArr$getCallSiteArray[49].call(PropertyUtil.class, nestingType, this.component, subComponent2, name);
            return;
        }
        callSiteArr$getCallSiteArray[50].callCurrent(this, new GStringImpl(new Object[]{name, getLabel(), getComponentName(), callSiteArr$getCallSiteArray[51].callGetProperty(callSiteArr$getCallSiteArray[52].call(this.component))}, new String[]{"No 'class' argument specified for [", "] in ", SymbolConstants.SPACE_SYMBOL, " of type [", "]"}));
    }

    public void cascadeFields(ComponentDelegate subDelegate) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        Iterator it = (Iterator) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[53].call(this.fieldsToCascade), Iterator.class);
        while (it.hasNext()) {
            String k = ShortTypeHandling.castToString(it.next());
            ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.getGroovyObjectProperty(ComponentDelegate.class, this, ShortTypeHandling.castToString(new GStringImpl(new Object[]{k}, new String[]{"", ""}))), (Class) null, callSiteArr$getCallSiteArray[54].callGroovyObjectGetProperty(subDelegate), ShortTypeHandling.castToString(new GStringImpl(new Object[]{k}, new String[]{"", ""})));
        }
    }

    public void injectParent(Object subComponent) {
        if (DefaultTypeTransformation.booleanUnbox($getCallSiteArray()[55].call(subComponent, "parent"))) {
            ScriptBytecodeAdapter.setProperty(this.component, (Class) null, subComponent, "parent");
        }
    }

    public void propertyMissing(String name, Object value) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        NestingType nestingType = (NestingType) ShortTypeHandling.castToEnum(callSiteArr$getCallSiteArray[56].call(PropertyUtil.class, this.component, name, value), NestingType.class);
        if (ScriptBytecodeAdapter.compareEqual(nestingType, callSiteArr$getCallSiteArray[57].callGetProperty(NestingType.class))) {
            callSiteArr$getCallSiteArray[58].callCurrent(this, new GStringImpl(new Object[]{callSiteArr$getCallSiteArray[59].callCurrent(this), callSiteArr$getCallSiteArray[60].callCurrent(this), callSiteArr$getCallSiteArray[61].callGetProperty(callSiteArr$getCallSiteArray[62].call(this.component)), name}, new String[]{"", SymbolConstants.SPACE_SYMBOL, " of type [", "] has no appplicable [", "] property "}));
        } else {
            callSiteArr$getCallSiteArray[63].call(PropertyUtil.class, nestingType, this.component, value, name);
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public Object analyzeArgs(Object... args) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        String name = null;
        Class clazz = null;
        Closure closure = null;
        if (ScriptBytecodeAdapter.compareGreaterThan(callSiteArr$getCallSiteArray[64].call(args), 3)) {
            callSiteArr$getCallSiteArray[65].callCurrent(this, new GStringImpl(new Object[]{args}, new String[]{"At most 3 arguments allowed but you passed ", ""}));
            return ScriptBytecodeAdapter.createList(new Object[]{null, null, null});
        }
        if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            Integer num = -1;
            if (BytecodeInterface8.objectArrayGet(args, num.intValue()) instanceof Closure) {
                Integer num2 = -1;
                closure = (Closure) ScriptBytecodeAdapter.castToType(BytecodeInterface8.objectArrayGet(args, num2.intValue()), Closure.class);
                Integer num3 = -1;
                args = (Object[]) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[70].call(args, BytecodeInterface8.objectArrayGet(args, num3.intValue())), Object[].class);
            }
        } else if (callSiteArr$getCallSiteArray[66].call(args, -1) instanceof Closure) {
            closure = (Closure) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[67].call(args, -1), Closure.class);
            args = (Object[]) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[68].call(args, callSiteArr$getCallSiteArray[69].call(args, -1)), Object[].class);
        }
        if (!BytecodeInterface8.isOrigInt() || !BytecodeInterface8.isOrigZ() || __$stMC || BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareEqual(callSiteArr$getCallSiteArray[71].call(args), 1)) {
                clazz = ShortTypeHandling.castToClass(callSiteArr$getCallSiteArray[72].callCurrent(this, callSiteArr$getCallSiteArray[73].call(args, 0)));
            }
        } else if (ScriptBytecodeAdapter.compareEqual(callSiteArr$getCallSiteArray[74].call(args), 1)) {
            clazz = ShortTypeHandling.castToClass(callSiteArr$getCallSiteArray[75].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 0)));
        }
        if (!BytecodeInterface8.isOrigInt() || !BytecodeInterface8.isOrigZ() || __$stMC || BytecodeInterface8.disabledStandardMetaClass()) {
            if (ScriptBytecodeAdapter.compareEqual(callSiteArr$getCallSiteArray[76].call(args), 2)) {
                name = ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[77].callCurrent(this, callSiteArr$getCallSiteArray[78].call(args, 0)));
                clazz = ShortTypeHandling.castToClass(callSiteArr$getCallSiteArray[79].callCurrent(this, callSiteArr$getCallSiteArray[80].call(args, 1)));
            }
        } else if (ScriptBytecodeAdapter.compareEqual(callSiteArr$getCallSiteArray[81].call(args), 2)) {
            name = ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[82].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 0)));
            clazz = ShortTypeHandling.castToClass(callSiteArr$getCallSiteArray[83].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 1)));
        }
        return ScriptBytecodeAdapter.createList(new Object[]{name, clazz, closure});
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public Class parseClassArgument(Object arg) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        if (arg instanceof Class) {
            return ShortTypeHandling.castToClass(arg);
        }
        if (arg instanceof String) {
            return Class.forName(ShortTypeHandling.castToString(arg));
        }
        callSiteArr$getCallSiteArray[84].callCurrent(this, new GStringImpl(new Object[]{callSiteArr$getCallSiteArray[85].callGetProperty(callSiteArr$getCallSiteArray[86].call(arg))}, new String[]{"Unexpected argument type ", ""}));
        return ShortTypeHandling.castToClass((Object) null);
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public String parseNameArgument(Object arg) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        if (arg instanceof String) {
            return ShortTypeHandling.castToString(arg);
        }
        callSiteArr$getCallSiteArray[87].callCurrent(this, "With 2 or 3 arguments, the first argument must be the component name, i.e of type string");
        return ShortTypeHandling.castToString((Object) null);
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public String getComponentName() {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        if (DefaultTypeTransformation.booleanUnbox(callSiteArr$getCallSiteArray[88].call(this.component, "name"))) {
            return ShortTypeHandling.castToString(new GStringImpl(new Object[]{callSiteArr$getCallSiteArray[89].callGetProperty(this.component)}, new String[]{PropertyAccessor.PROPERTY_KEY_PREFIX, "]"}));
        }
        return "";
    }
}
