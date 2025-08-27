package ch.qos.logback.classic.gaffer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.jmx.JMXConfigurator;
import ch.qos.logback.classic.jmx.MBeanUtil;
import ch.qos.logback.classic.joran.ReconfigureOnChangeTask;
import ch.qos.logback.classic.net.ReceiverBase;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.joran.util.beans.BeanUtil;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.status.StatusListener;
import ch.qos.logback.core.util.CachingDateFormatter;
import ch.qos.logback.core.util.Duration;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.management.ManagementFactory;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.catalina.Lifecycle;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.slf4j.Logger;

/* compiled from: ConfigurationDelegate.groovy */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/gaffer/ConfigurationDelegate.class */
public class ConfigurationDelegate extends ContextAwareBase implements GroovyObject {
    private List<Appender> appenderList;
    private static /* synthetic */ ClassInfo $staticClassInfo;
    public static transient /* synthetic */ boolean __$stMC;
    private transient /* synthetic */ MetaClass metaClass;
    private static /* synthetic */ SoftReference $callSiteArray;

    public void scan() {
        $getCallSiteArray();
        if (__$stMC || BytecodeInterface8.disabledStandardMetaClass()) {
            scan(null);
        } else {
            scan(null);
        }
    }

    public void logger(String name, Level level, List<String> list) {
        $getCallSiteArray();
        logger(name, level, list, null);
    }

    public void appender(String name, Class clazz) {
        $getCallSiteArray();
        appender(name, clazz, null);
    }

    public void receiver(String name, Class aClass) {
        $getCallSiteArray();
        receiver(name, aClass, null);
    }

    public void turboFilter(Class clazz) {
        $getCallSiteArray();
        turboFilter(clazz, null);
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public String timestamp(String datePattern) {
        $getCallSiteArray();
        return (__$stMC || BytecodeInterface8.disabledStandardMetaClass()) ? timestamp(datePattern, DefaultTypeTransformation.longUnbox(-1)) : timestamp(datePattern, DefaultTypeTransformation.longUnbox(-1));
    }

    public void jmxConfigurator() {
        $getCallSiteArray();
        if (__$stMC || BytecodeInterface8.disabledStandardMetaClass()) {
            jmxConfigurator(null);
        } else {
            jmxConfigurator(null);
        }
    }

    protected /* synthetic */ MetaClass $getStaticMetaClass() {
        if (getClass() != ConfigurationDelegate.class) {
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

    public List<Appender> getAppenderList() {
        return this.appenderList;
    }

    public void setAppenderList(List<Appender> list) {
        this.appenderList = list;
    }

    public /* synthetic */ Object super$2$getDeclaredOrigin() {
        return super.getDeclaredOrigin();
    }

    private static /* synthetic */ void $createCallSiteArray_1(String[] strArr) {
        strArr[0] = "<$constructor$>";
        strArr[1] = "setContext";
        strArr[2] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[3] = "putObject";
        strArr[4] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[5] = CoreConstants.RECONFIGURE_ON_CHANGE_TASK;
        strArr[6] = CoreConstants.VALUE_OF;
        strArr[7] = "getScheduledExecutorService";
        strArr[8] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[9] = "scheduleAtFixedRate";
        strArr[10] = "getMilliseconds";
        strArr[11] = "getMilliseconds";
        strArr[12] = "MILLISECONDS";
        strArr[13] = "addScheduledFuture";
        strArr[14] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[15] = "addInfo";
        strArr[16] = "plus";
        strArr[17] = "addError";
        strArr[18] = "plus";
        strArr[19] = "plus";
        strArr[20] = "newInstance";
        strArr[21] = BeanUtil.PREFIX_ADDER;
        strArr[22] = "statusManager";
        strArr[23] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[24] = "setContext";
        strArr[25] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[26] = Lifecycle.START_EVENT;
        strArr[27] = "addInfo";
        strArr[28] = "canonicalName";
        strArr[29] = "getName";
        strArr[30] = "getObject";
        strArr[31] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[32] = CoreConstants.PATTERN_RULE_REGISTRY;
        strArr[33] = "<$constructor$>";
        strArr[34] = "putObject";
        strArr[35] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[36] = CoreConstants.PATTERN_RULE_REGISTRY;
        strArr[37] = "addInfo";
        strArr[38] = "plus";
        strArr[39] = "plus";
        strArr[40] = "plus";
        strArr[41] = "plus";
        strArr[42] = "put";
        strArr[43] = "addError";
        strArr[44] = "logger";
        strArr[45] = "ROOT_LOGGER_NAME";
        strArr[46] = "getLogger";
        strArr[47] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[48] = "addInfo";
        strArr[49] = "plus";
        strArr[50] = "iterator";
        strArr[51] = "find";
        strArr[52] = "addInfo";
        strArr[53] = "plus";
        strArr[54] = "addAppender";
        strArr[55] = "addError";
        strArr[56] = "addInfo";
        strArr[57] = "addInfo";
        strArr[58] = "plus";
        strArr[59] = "plus";
        strArr[60] = "name";
        strArr[61] = "newInstance";
        strArr[62] = "addInfo";
        strArr[63] = "plus";
        strArr[64] = "plus";
        strArr[65] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[66] = BeanUtil.PREFIX_ADDER;
        strArr[67] = "<$constructor$>";
        strArr[68] = "copyContributions";
        strArr[69] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[70] = "DELEGATE_FIRST";
        strArr[71] = "call";
        strArr[72] = Lifecycle.START_EVENT;
        strArr[73] = "addError";
        strArr[74] = "plus";
        strArr[75] = "plus";
        strArr[76] = "addInfo";
        strArr[77] = "plus";
        strArr[78] = "plus";
        strArr[79] = "name";
        strArr[80] = "clazz";
        strArr[81] = "newInstance";
        strArr[82] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[83] = "<$constructor$>";
        strArr[84] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[85] = "DELEGATE_FIRST";
        strArr[86] = "call";
        strArr[87] = Lifecycle.START_EVENT;
        strArr[88] = "addError";
        strArr[89] = "plus";
        strArr[90] = "plus";
        strArr[91] = "getName";
        strArr[92] = "each";
        strArr[93] = "getMappings";
        strArr[94] = "addInfo";
        strArr[95] = "plus";
        strArr[96] = "plus";
        strArr[97] = "name";
        strArr[98] = "newInstance";
        strArr[99] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[100] = "<$constructor$>";
        strArr[101] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[102] = "DELEGATE_FIRST";
        strArr[103] = "call";
        strArr[104] = Lifecycle.START_EVENT;
        strArr[105] = "addInfo";
        strArr[106] = "addTurboFilter";
        strArr[107] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[108] = "addInfo";
        strArr[109] = "currentTimeMillis";
        strArr[110] = "addInfo";
        strArr[111] = "plus";
        strArr[112] = "plus";
        strArr[113] = "<$constructor$>";
        strArr[114] = "format";
        strArr[115] = "name";
        strArr[116] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[117] = "<$constructor$>";
        strArr[118] = "getObjectNameFor";
        strArr[119] = "string2ObjectName";
        strArr[120] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[121] = "addError";
        strArr[122] = "platformMBeanServer";
        strArr[123] = "isRegistered";
        strArr[124] = "<$constructor$>";
        strArr[125] = CoreConstants.CONTEXT_SCOPE_VALUE;
        strArr[126] = "registerMBean";
        strArr[127] = "addError";
    }

    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        String[] strArr = new String[128];
        $createCallSiteArray_1(strArr);
        return new CallSiteArray(ConfigurationDelegate.class, strArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static /* synthetic */ org.codehaus.groovy.runtime.callsite.CallSite[] $getCallSiteArray() {
        /*
            java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.ConfigurationDelegate.$callSiteArray
            if (r0 == 0) goto L14
            java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.ConfigurationDelegate.$callSiteArray
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
            ch.qos.logback.classic.gaffer.ConfigurationDelegate.$callSiteArray = r0
        L23:
            r0 = r4
            org.codehaus.groovy.runtime.callsite.CallSite[] r0 = r0.array
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ch.qos.logback.classic.gaffer.ConfigurationDelegate.$getCallSiteArray():org.codehaus.groovy.runtime.callsite.CallSite[]");
    }

    public ConfigurationDelegate() {
        $getCallSiteArray();
        this.appenderList = ScriptBytecodeAdapter.createList(new Object[0]);
        this.metaClass = $getStaticMetaClass();
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    @Override // ch.qos.logback.core.spi.ContextAwareBase
    public Object getDeclaredOrigin() {
        $getCallSiteArray();
        return this;
    }

    public void scan(String scanPeriodStr) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        if (DefaultTypeTransformation.booleanUnbox(scanPeriodStr)) {
            ReconfigureOnChangeTask rocTask = (ReconfigureOnChangeTask) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[0].callConstructor(ReconfigureOnChangeTask.class), ReconfigureOnChangeTask.class);
            callSiteArr$getCallSiteArray[1].call(rocTask, callSiteArr$getCallSiteArray[2].callGroovyObjectGetProperty(this));
            callSiteArr$getCallSiteArray[3].call(callSiteArr$getCallSiteArray[4].callGroovyObjectGetProperty(this), callSiteArr$getCallSiteArray[5].callGetProperty(CoreConstants.class), rocTask);
            try {
                Duration duration = (Duration) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[6].call(Duration.class, scanPeriodStr), Duration.class);
                ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[7].call(callSiteArr$getCallSiteArray[8].callGroovyObjectGetProperty(this)), ScheduledExecutorService.class);
                ScheduledFuture scheduledFuture = (ScheduledFuture) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[9].call(scheduledExecutorService, rocTask, callSiteArr$getCallSiteArray[10].call(duration), callSiteArr$getCallSiteArray[11].call(duration), callSiteArr$getCallSiteArray[12].callGetProperty(TimeUnit.class)), ScheduledFuture.class);
                callSiteArr$getCallSiteArray[13].call(callSiteArr$getCallSiteArray[14].callGroovyObjectGetProperty(this), scheduledFuture);
                callSiteArr$getCallSiteArray[15].callCurrent(this, callSiteArr$getCallSiteArray[16].call("Setting ReconfigureOnChangeTask scanning period to ", duration));
            } catch (NumberFormatException nfe) {
                callSiteArr$getCallSiteArray[17].callCurrent(this, callSiteArr$getCallSiteArray[18].call(callSiteArr$getCallSiteArray[19].call("Error while converting [", scanPeriodStr), "] to long"), nfe);
            }
        }
    }

    public void statusListener(Class listenerClass) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        StatusListener statusListener = (StatusListener) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[20].call(listenerClass), StatusListener.class);
        callSiteArr$getCallSiteArray[21].call(callSiteArr$getCallSiteArray[22].callGetProperty(callSiteArr$getCallSiteArray[23].callGroovyObjectGetProperty(this)), statusListener);
        if (statusListener instanceof ContextAware) {
            callSiteArr$getCallSiteArray[24].call((ContextAware) ScriptBytecodeAdapter.castToType(statusListener, ContextAware.class), callSiteArr$getCallSiteArray[25].callGroovyObjectGetProperty(this));
        }
        if (statusListener instanceof LifeCycle) {
            callSiteArr$getCallSiteArray[26].call((LifeCycle) ScriptBytecodeAdapter.castToType(statusListener, LifeCycle.class));
        }
        callSiteArr$getCallSiteArray[27].callCurrent(this, new GStringImpl(new Object[]{callSiteArr$getCallSiteArray[28].callGetProperty(listenerClass)}, new String[]{"Added status listener of type [", "]"}));
    }

    public void conversionRule(String conversionWord, Class converterClass) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        String converterClassName = ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[29].call(converterClass));
        Map ruleRegistry = (Map) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[30].call(callSiteArr$getCallSiteArray[31].callGroovyObjectGetProperty(this), callSiteArr$getCallSiteArray[32].callGetProperty(CoreConstants.class)), Map.class);
        if (ScriptBytecodeAdapter.compareEqual(ruleRegistry, (Object) null)) {
            ruleRegistry = (Map) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[33].callConstructor(HashMap.class), Map.class);
            callSiteArr$getCallSiteArray[34].call(callSiteArr$getCallSiteArray[35].callGroovyObjectGetProperty(this), callSiteArr$getCallSiteArray[36].callGetProperty(CoreConstants.class), ruleRegistry);
        }
        callSiteArr$getCallSiteArray[37].callCurrent(this, callSiteArr$getCallSiteArray[38].call(callSiteArr$getCallSiteArray[39].call(callSiteArr$getCallSiteArray[40].call(callSiteArr$getCallSiteArray[41].call("registering conversion word ", conversionWord), " with class ["), converterClassName), "]"));
        callSiteArr$getCallSiteArray[42].call(ruleRegistry, conversionWord, converterClassName);
    }

    public void root(Level level) {
        $getCallSiteArray();
        root(level, ScriptBytecodeAdapter.createList(new Object[0]));
    }

    public void root(Level level, List<String> list) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        if (ScriptBytecodeAdapter.compareEqual(level, (Object) null)) {
            callSiteArr$getCallSiteArray[43].callCurrent(this, "Root logger cannot be set to level null");
        } else {
            callSiteArr$getCallSiteArray[44].callCurrent(this, callSiteArr$getCallSiteArray[45].callGetProperty(Logger.class), level, list);
        }
    }

    public void logger(String name, Level level) {
        $getCallSiteArray();
        logger(name, level, ScriptBytecodeAdapter.createList(new Object[0]), null);
    }

    public void logger(String name, Level level, List<String> list, Boolean additivity) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        if (DefaultTypeTransformation.booleanUnbox(name)) {
            ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[46].call((LoggerContext) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[47].callGroovyObjectGetProperty(this), LoggerContext.class), name), ch.qos.logback.classic.Logger.class);
            callSiteArr$getCallSiteArray[48].callCurrent(this, callSiteArr$getCallSiteArray[49].call(new GStringImpl(new Object[]{name}, new String[]{"Setting level of logger [", "] to "}), level));
            ScriptBytecodeAdapter.setProperty(level, (Class) null, logger, "level");
            Reference aName = new Reference((Object) null);
            Iterator it = (Iterator) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[50].call(list), Iterator.class);
            while (it.hasNext()) {
                aName.set(it.next());
                Appender appender = (Appender) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[51].call(this.appenderList, new _logger_closure1(this, this, aName)), Appender.class);
                if (ScriptBytecodeAdapter.compareNotEqual(appender, (Object) null)) {
                    callSiteArr$getCallSiteArray[52].callCurrent(this, callSiteArr$getCallSiteArray[53].call(new GStringImpl(new Object[]{aName.get()}, new String[]{"Attaching appender named [", "] to "}), logger));
                    callSiteArr$getCallSiteArray[54].call(logger, appender);
                } else {
                    callSiteArr$getCallSiteArray[55].callCurrent(this, new GStringImpl(new Object[]{aName.get()}, new String[]{"Failed to find appender named [", "]"}));
                }
            }
            if (ScriptBytecodeAdapter.compareNotEqual(additivity, (Object) null)) {
                ScriptBytecodeAdapter.setProperty(additivity, (Class) null, logger, "additive");
                return;
            }
            return;
        }
        callSiteArr$getCallSiteArray[56].callCurrent(this, "No name attribute for logger");
    }

    /* compiled from: ConfigurationDelegate.groovy */
    /* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/gaffer/ConfigurationDelegate$_logger_closure1.class */
    class _logger_closure1 extends Closure implements GeneratedClosure {
        private /* synthetic */ Reference aName;
        private static /* synthetic */ ClassInfo $staticClassInfo;
        public static transient /* synthetic */ boolean __$stMC;
        private static /* synthetic */ SoftReference $callSiteArray;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public _logger_closure1(Object _outerInstance, Object _thisObject, Reference aName) {
            super(_outerInstance, _thisObject);
            $getCallSiteArray();
            this.aName = aName;
        }

        /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
        public Object getaName() {
            $getCallSiteArray();
            return this.aName.get();
        }

        protected /* synthetic */ MetaClass $getStaticMetaClass() {
            if (getClass() != _logger_closure1.class) {
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
            strArr[0] = "name";
        }

        private static /* synthetic */ CallSiteArray $createCallSiteArray() {
            String[] strArr = new String[1];
            $createCallSiteArray_1(strArr);
            return new CallSiteArray(_logger_closure1.class, strArr);
        }

        /* JADX WARN: Removed duplicated region for block: B:6:0x0014  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private static /* synthetic */ org.codehaus.groovy.runtime.callsite.CallSite[] $getCallSiteArray() {
            /*
                java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.ConfigurationDelegate._logger_closure1.$callSiteArray
                if (r0 == 0) goto L14
                java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.ConfigurationDelegate._logger_closure1.$callSiteArray
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
                ch.qos.logback.classic.gaffer.ConfigurationDelegate._logger_closure1.$callSiteArray = r0
            L23:
                r0 = r4
                org.codehaus.groovy.runtime.callsite.CallSite[] r0 = r0.array
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: ch.qos.logback.classic.gaffer.ConfigurationDelegate._logger_closure1.$getCallSiteArray():org.codehaus.groovy.runtime.callsite.CallSite[]");
        }

        /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
        public Object doCall(Object it) {
            return Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($getCallSiteArray()[0].callGetProperty(it), this.aName.get()));
        }
    }

    public void appender(String name, Class clazz, Closure closure) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        callSiteArr$getCallSiteArray[57].callCurrent(this, callSiteArr$getCallSiteArray[58].call(callSiteArr$getCallSiteArray[59].call("About to instantiate appender of type [", callSiteArr$getCallSiteArray[60].callGetProperty(clazz)), "]"));
        Appender appender = (Appender) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[61].call(clazz), Appender.class);
        callSiteArr$getCallSiteArray[62].callCurrent(this, callSiteArr$getCallSiteArray[63].call(callSiteArr$getCallSiteArray[64].call("Naming appender as [", name), "]"));
        ScriptBytecodeAdapter.setProperty(name, (Class) null, appender, "name");
        ScriptBytecodeAdapter.setProperty(callSiteArr$getCallSiteArray[65].callGroovyObjectGetProperty(this), (Class) null, appender, CoreConstants.CONTEXT_SCOPE_VALUE);
        callSiteArr$getCallSiteArray[66].call(this.appenderList, appender);
        if (ScriptBytecodeAdapter.compareNotEqual(closure, (Object) null)) {
            AppenderDelegate ad = (AppenderDelegate) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[67].callConstructor(AppenderDelegate.class, appender, this.appenderList), AppenderDelegate.class);
            callSiteArr$getCallSiteArray[68].callCurrent(this, ad, appender);
            ScriptBytecodeAdapter.setGroovyObjectProperty(callSiteArr$getCallSiteArray[69].callGroovyObjectGetProperty(this), ConfigurationDelegate.class, ad, CoreConstants.CONTEXT_SCOPE_VALUE);
            ScriptBytecodeAdapter.setGroovyObjectProperty(ad, ConfigurationDelegate.class, closure, "delegate");
            ScriptBytecodeAdapter.setGroovyObjectProperty(callSiteArr$getCallSiteArray[70].callGetProperty(Closure.class), ConfigurationDelegate.class, closure, "resolveStrategy");
            callSiteArr$getCallSiteArray[71].call(closure);
        }
        try {
            callSiteArr$getCallSiteArray[72].call(appender);
        } catch (RuntimeException e) {
            callSiteArr$getCallSiteArray[73].callCurrent(this, callSiteArr$getCallSiteArray[74].call(callSiteArr$getCallSiteArray[75].call("Failed to start apppender named [", name), "]"), e);
        }
    }

    public void receiver(String name, Class aClass, Closure closure) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        callSiteArr$getCallSiteArray[76].callCurrent(this, callSiteArr$getCallSiteArray[77].call(callSiteArr$getCallSiteArray[78].call("About to instantiate receiver of type [", callSiteArr$getCallSiteArray[79].callGetProperty(callSiteArr$getCallSiteArray[80].callGroovyObjectGetProperty(this))), "]"));
        ReceiverBase receiver = (ReceiverBase) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[81].call(aClass), ReceiverBase.class);
        ScriptBytecodeAdapter.setProperty(callSiteArr$getCallSiteArray[82].callGroovyObjectGetProperty(this), (Class) null, receiver, CoreConstants.CONTEXT_SCOPE_VALUE);
        if (ScriptBytecodeAdapter.compareNotEqual(closure, (Object) null)) {
            ComponentDelegate componentDelegate = (ComponentDelegate) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[83].callConstructor(ComponentDelegate.class, receiver), ComponentDelegate.class);
            ScriptBytecodeAdapter.setGroovyObjectProperty(callSiteArr$getCallSiteArray[84].callGroovyObjectGetProperty(this), ConfigurationDelegate.class, componentDelegate, CoreConstants.CONTEXT_SCOPE_VALUE);
            ScriptBytecodeAdapter.setGroovyObjectProperty(componentDelegate, ConfigurationDelegate.class, closure, "delegate");
            ScriptBytecodeAdapter.setGroovyObjectProperty(callSiteArr$getCallSiteArray[85].callGetProperty(Closure.class), ConfigurationDelegate.class, closure, "resolveStrategy");
            callSiteArr$getCallSiteArray[86].call(closure);
        }
        try {
            callSiteArr$getCallSiteArray[87].call(receiver);
        } catch (RuntimeException e) {
            callSiteArr$getCallSiteArray[88].callCurrent(this, callSiteArr$getCallSiteArray[89].call(callSiteArr$getCallSiteArray[90].call("Failed to start receiver of type [", callSiteArr$getCallSiteArray[91].call(aClass)), "]"), e);
        }
    }

    private void copyContributions(AppenderDelegate appenderDelegate, Appender appender) {
        Reference appenderDelegate2 = new Reference(appenderDelegate);
        Reference appender2 = new Reference(appender);
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        if (((Appender) appender2.get()) instanceof ConfigurationContributor) {
            ConfigurationContributor cc = (ConfigurationContributor) ScriptBytecodeAdapter.castToType((Appender) appender2.get(), ConfigurationContributor.class);
            callSiteArr$getCallSiteArray[92].call(callSiteArr$getCallSiteArray[93].call(cc), new _copyContributions_closure2(this, this, appenderDelegate2, appender2));
        }
    }

    /* compiled from: ConfigurationDelegate.groovy */
    /* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/gaffer/ConfigurationDelegate$_copyContributions_closure2.class */
    class _copyContributions_closure2 extends Closure implements GeneratedClosure {
        private /* synthetic */ Reference appenderDelegate;
        private /* synthetic */ Reference appender;
        private static /* synthetic */ ClassInfo $staticClassInfo;
        public static transient /* synthetic */ boolean __$stMC;
        private static /* synthetic */ SoftReference $callSiteArray;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public _copyContributions_closure2(Object _outerInstance, Object _thisObject, Reference appenderDelegate, Reference appender) {
            super(_outerInstance, _thisObject);
            $getCallSiteArray();
            this.appenderDelegate = appenderDelegate;
            this.appender = appender;
        }

        /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
        public Object call(Object oldName, Object newName) {
            return $getCallSiteArray()[1].callCurrent(this, oldName, newName);
        }

        /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
        public AppenderDelegate getAppenderDelegate() {
            $getCallSiteArray();
            return (AppenderDelegate) ScriptBytecodeAdapter.castToType(this.appenderDelegate.get(), AppenderDelegate.class);
        }

        /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
        public Appender getAppender() {
            $getCallSiteArray();
            return (Appender) ScriptBytecodeAdapter.castToType(this.appender.get(), Appender.class);
        }

        protected /* synthetic */ MetaClass $getStaticMetaClass() {
            if (getClass() != _copyContributions_closure2.class) {
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
            strArr[0] = "metaClass";
            strArr[1] = "doCall";
        }

        private static /* synthetic */ CallSiteArray $createCallSiteArray() {
            String[] strArr = new String[2];
            $createCallSiteArray_1(strArr);
            return new CallSiteArray(_copyContributions_closure2.class, strArr);
        }

        /* JADX WARN: Removed duplicated region for block: B:6:0x0014  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private static /* synthetic */ org.codehaus.groovy.runtime.callsite.CallSite[] $getCallSiteArray() {
            /*
                java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.ConfigurationDelegate._copyContributions_closure2.$callSiteArray
                if (r0 == 0) goto L14
                java.lang.ref.SoftReference r0 = ch.qos.logback.classic.gaffer.ConfigurationDelegate._copyContributions_closure2.$callSiteArray
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
                ch.qos.logback.classic.gaffer.ConfigurationDelegate._copyContributions_closure2.$callSiteArray = r0
            L23:
                r0 = r4
                org.codehaus.groovy.runtime.callsite.CallSite[] r0 = r0.array
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: ch.qos.logback.classic.gaffer.ConfigurationDelegate._copyContributions_closure2.$getCallSiteArray():org.codehaus.groovy.runtime.callsite.CallSite[]");
        }

        /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
        public Object doCall(Object oldName, Object newName) {
            CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
            Closure methodPointer = ScriptBytecodeAdapter.getMethodPointer(this.appender.get(), ShortTypeHandling.castToString(new GStringImpl(new Object[]{oldName}, new String[]{"", ""})));
            ScriptBytecodeAdapter.setProperty(methodPointer, (Class) null, callSiteArr$getCallSiteArray[0].callGroovyObjectGetProperty(this.appenderDelegate.get()), ShortTypeHandling.castToString(new GStringImpl(new Object[]{newName}, new String[]{"", ""})));
            return methodPointer;
        }
    }

    public void turboFilter(Class clazz, Closure closure) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        callSiteArr$getCallSiteArray[94].callCurrent(this, callSiteArr$getCallSiteArray[95].call(callSiteArr$getCallSiteArray[96].call("About to instantiate turboFilter of type [", callSiteArr$getCallSiteArray[97].callGetProperty(clazz)), "]"));
        TurboFilter turboFilter = (TurboFilter) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[98].call(clazz), TurboFilter.class);
        ScriptBytecodeAdapter.setProperty(callSiteArr$getCallSiteArray[99].callGroovyObjectGetProperty(this), (Class) null, turboFilter, CoreConstants.CONTEXT_SCOPE_VALUE);
        if (ScriptBytecodeAdapter.compareNotEqual(closure, (Object) null)) {
            ComponentDelegate componentDelegate = (ComponentDelegate) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[100].callConstructor(ComponentDelegate.class, turboFilter), ComponentDelegate.class);
            ScriptBytecodeAdapter.setGroovyObjectProperty(callSiteArr$getCallSiteArray[101].callGroovyObjectGetProperty(this), ConfigurationDelegate.class, componentDelegate, CoreConstants.CONTEXT_SCOPE_VALUE);
            ScriptBytecodeAdapter.setGroovyObjectProperty(componentDelegate, ConfigurationDelegate.class, closure, "delegate");
            ScriptBytecodeAdapter.setGroovyObjectProperty(callSiteArr$getCallSiteArray[102].callGetProperty(Closure.class), ConfigurationDelegate.class, closure, "resolveStrategy");
            callSiteArr$getCallSiteArray[103].call(closure);
        }
        callSiteArr$getCallSiteArray[104].call(turboFilter);
        callSiteArr$getCallSiteArray[105].callCurrent(this, "Adding aforementioned turbo filter to context");
        callSiteArr$getCallSiteArray[106].call(callSiteArr$getCallSiteArray[107].callGroovyObjectGetProperty(this), turboFilter);
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public String timestamp(String datePattern, long timeReference) {
        long now;
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        DefaultTypeTransformation.longUnbox(-1);
        if (ScriptBytecodeAdapter.compareEqual(Long.valueOf(timeReference), -1)) {
            callSiteArr$getCallSiteArray[108].callCurrent(this, "Using current interpretation time, i.e. now, as time reference.");
            now = DefaultTypeTransformation.longUnbox(callSiteArr$getCallSiteArray[109].call(System.class));
        } else {
            now = timeReference;
            callSiteArr$getCallSiteArray[110].callCurrent(this, callSiteArr$getCallSiteArray[111].call(callSiteArr$getCallSiteArray[112].call("Using ", Long.valueOf(now)), " as time reference."));
        }
        CachingDateFormatter sdf = (CachingDateFormatter) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[113].callConstructor(CachingDateFormatter.class, datePattern), CachingDateFormatter.class);
        return ShortTypeHandling.castToString(callSiteArr$getCallSiteArray[114].call(sdf, Long.valueOf(now)));
    }

    public void jmxConfigurator(String name) {
        CallSite[] callSiteArr$getCallSiteArray = $getCallSiteArray();
        Object objectName = null;
        Object contextName = callSiteArr$getCallSiteArray[115].callGetProperty(callSiteArr$getCallSiteArray[116].callGroovyObjectGetProperty(this));
        if (ScriptBytecodeAdapter.compareNotEqual(name, (Object) null)) {
            try {
                objectName = callSiteArr$getCallSiteArray[117].callConstructor(ObjectName.class, name);
            } catch (MalformedObjectNameException e) {
                contextName = name;
            }
        }
        if (ScriptBytecodeAdapter.compareEqual(objectName, (Object) null)) {
            Object objectNameAsStr = callSiteArr$getCallSiteArray[118].call(MBeanUtil.class, contextName, JMXConfigurator.class);
            objectName = callSiteArr$getCallSiteArray[119].call(MBeanUtil.class, callSiteArr$getCallSiteArray[120].callGroovyObjectGetProperty(this), this, objectNameAsStr);
            if (ScriptBytecodeAdapter.compareEqual(objectName, (Object) null)) {
                callSiteArr$getCallSiteArray[121].callCurrent(this, new GStringImpl(new Object[]{objectNameAsStr}, new String[]{"Failed to construct ObjectName for [", "]"}));
                return;
            }
        }
        Object platformMBeanServer = callSiteArr$getCallSiteArray[122].callGetProperty(ManagementFactory.class);
        if (!DefaultTypeTransformation.booleanUnbox(callSiteArr$getCallSiteArray[123].call(MBeanUtil.class, platformMBeanServer, objectName))) {
            JMXConfigurator jmxConfigurator = (JMXConfigurator) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[124].callConstructor(JMXConfigurator.class, ScriptBytecodeAdapter.createPojoWrapper((LoggerContext) ScriptBytecodeAdapter.castToType(callSiteArr$getCallSiteArray[125].callGroovyObjectGetProperty(this), LoggerContext.class), LoggerContext.class), platformMBeanServer, objectName), JMXConfigurator.class);
            try {
                callSiteArr$getCallSiteArray[126].call(platformMBeanServer, jmxConfigurator, objectName);
            } catch (Exception all) {
                callSiteArr$getCallSiteArray[127].callCurrent(this, "Failed to create mbean", all);
            }
        }
    }
}
