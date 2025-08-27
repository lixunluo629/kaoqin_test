package org.apache.commons.lang.p000enum;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.PropertyAccessor;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/enum/Enum.class */
public abstract class Enum implements Comparable, Serializable {
    private static final long serialVersionUID = -487045951170455942L;
    private static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap(0));
    private static Map cEnumClasses = new WeakHashMap();
    private final String iName;
    private final transient int iHashCode;
    protected transient String iToString = null;
    static Class class$org$apache$commons$lang$enum$Enum;
    static Class class$org$apache$commons$lang$enum$ValuedEnum;

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/enum/Enum$Entry.class */
    private static class Entry {
        final Map map = new HashMap();
        final Map unmodifiableMap = Collections.unmodifiableMap(this.map);
        final List list = new ArrayList(25);
        final List unmodifiableList = Collections.unmodifiableList(this.list);

        protected Entry() {
        }
    }

    protected Enum(String name) {
        init(name);
        this.iName = name;
        this.iHashCode = 7 + getEnumClass().hashCode() + (3 * name.hashCode());
    }

    private void init(String name) {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("The Enum name must not be empty or null");
        }
        Class enumClass = getEnumClass();
        if (enumClass == null) {
            throw new IllegalArgumentException("getEnumClass() must not be null");
        }
        Class cls = getClass();
        boolean ok = false;
        while (true) {
            if (cls == null) {
                break;
            }
            Class cls2 = cls;
            if (class$org$apache$commons$lang$enum$Enum == null) {
                clsClass$2 = class$("org.apache.commons.lang.enum.Enum");
                class$org$apache$commons$lang$enum$Enum = clsClass$2;
            } else {
                clsClass$2 = class$org$apache$commons$lang$enum$Enum;
            }
            if (cls2 == clsClass$2) {
                break;
            }
            Class cls3 = cls;
            if (class$org$apache$commons$lang$enum$ValuedEnum == null) {
                clsClass$3 = class$("org.apache.commons.lang.enum.ValuedEnum");
                class$org$apache$commons$lang$enum$ValuedEnum = clsClass$3;
            } else {
                clsClass$3 = class$org$apache$commons$lang$enum$ValuedEnum;
            }
            if (cls3 == clsClass$3) {
                break;
            }
            if (cls == enumClass) {
                ok = true;
                break;
            }
            cls = cls.getSuperclass();
        }
        if (!ok) {
            throw new IllegalArgumentException("getEnumClass() must return a superclass of this class");
        }
        if (class$org$apache$commons$lang$enum$Enum == null) {
            clsClass$ = class$("org.apache.commons.lang.enum.Enum");
            class$org$apache$commons$lang$enum$Enum = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$lang$enum$Enum;
        }
        Class cls4 = clsClass$;
        synchronized (clsClass$) {
            Entry entry = (Entry) cEnumClasses.get(enumClass);
            if (entry == null) {
                entry = createEntry(enumClass);
                Map myMap = new WeakHashMap();
                myMap.putAll(cEnumClasses);
                myMap.put(enumClass, entry);
                cEnumClasses = myMap;
            }
            if (entry.map.containsKey(name)) {
                throw new IllegalArgumentException(new StringBuffer().append("The Enum name must be unique, '").append(name).append("' has already been added").toString());
            }
            entry.map.put(name, this);
            entry.list.add(this);
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    protected Object readResolve() {
        Entry entry = (Entry) cEnumClasses.get(getEnumClass());
        if (entry == null) {
            return null;
        }
        return entry.map.get(getName());
    }

    protected static Enum getEnum(Class enumClass, String name) throws ClassNotFoundException {
        Entry entry = getEntry(enumClass);
        if (entry == null) {
            return null;
        }
        return (Enum) entry.map.get(name);
    }

    protected static Map getEnumMap(Class enumClass) throws ClassNotFoundException {
        Entry entry = getEntry(enumClass);
        if (entry == null) {
            return EMPTY_MAP;
        }
        return entry.unmodifiableMap;
    }

    protected static List getEnumList(Class enumClass) throws ClassNotFoundException {
        Entry entry = getEntry(enumClass);
        if (entry == null) {
            return Collections.EMPTY_LIST;
        }
        return entry.unmodifiableList;
    }

    protected static Iterator iterator(Class enumClass) {
        return getEnumList(enumClass).iterator();
    }

    private static Entry getEntry(Class enumClass) throws ClassNotFoundException {
        Class clsClass$;
        if (enumClass == null) {
            throw new IllegalArgumentException("The Enum Class must not be null");
        }
        if (class$org$apache$commons$lang$enum$Enum == null) {
            clsClass$ = class$("org.apache.commons.lang.enum.Enum");
            class$org$apache$commons$lang$enum$Enum = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$lang$enum$Enum;
        }
        if (!clsClass$.isAssignableFrom(enumClass)) {
            throw new IllegalArgumentException("The Class must be a subclass of Enum");
        }
        Entry entry = (Entry) cEnumClasses.get(enumClass);
        if (entry == null) {
            try {
                Class.forName(enumClass.getName(), true, enumClass.getClassLoader());
                entry = (Entry) cEnumClasses.get(enumClass);
            } catch (Exception e) {
            }
        }
        return entry;
    }

    private static Entry createEntry(Class enumClass) {
        Class clsClass$;
        Class clsClass$2;
        Entry entry = new Entry();
        Class superclass = enumClass.getSuperclass();
        while (true) {
            Class cls = superclass;
            if (cls == null) {
                break;
            }
            if (class$org$apache$commons$lang$enum$Enum == null) {
                clsClass$ = class$("org.apache.commons.lang.enum.Enum");
                class$org$apache$commons$lang$enum$Enum = clsClass$;
            } else {
                clsClass$ = class$org$apache$commons$lang$enum$Enum;
            }
            if (cls == clsClass$) {
                break;
            }
            if (class$org$apache$commons$lang$enum$ValuedEnum == null) {
                clsClass$2 = class$("org.apache.commons.lang.enum.ValuedEnum");
                class$org$apache$commons$lang$enum$ValuedEnum = clsClass$2;
            } else {
                clsClass$2 = class$org$apache$commons$lang$enum$ValuedEnum;
            }
            if (cls == clsClass$2) {
                break;
            }
            Entry loopEntry = (Entry) cEnumClasses.get(cls);
            if (loopEntry != null) {
                entry.list.addAll(loopEntry.list);
                entry.map.putAll(loopEntry.map);
                break;
            }
            superclass = cls.getSuperclass();
        }
        return entry;
    }

    public final String getName() {
        return this.iName;
    }

    public Class getEnumClass() {
        return getClass();
    }

    public final boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() == getClass()) {
            return this.iName.equals(((Enum) other).iName);
        }
        if (!other.getClass().getName().equals(getClass().getName())) {
            return false;
        }
        return this.iName.equals(getNameInOtherClassLoader(other));
    }

    public final int hashCode() {
        return this.iHashCode;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object other) {
        if (other == this) {
            return 0;
        }
        if (other.getClass() != getClass()) {
            if (other.getClass().getName().equals(getClass().getName())) {
                return this.iName.compareTo(getNameInOtherClassLoader(other));
            }
            throw new ClassCastException(new StringBuffer().append("Different enum class '").append(ClassUtils.getShortClassName(other.getClass())).append("'").toString());
        }
        return this.iName.compareTo(((Enum) other).iName);
    }

    private String getNameInOtherClassLoader(Object other) throws NoSuchMethodException, SecurityException {
        try {
            Method mth = other.getClass().getMethod("getName", null);
            String name = (String) mth.invoke(other, null);
            return name;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException("This should not happen");
        }
    }

    public String toString() {
        if (this.iToString == null) {
            String shortName = ClassUtils.getShortClassName(getEnumClass());
            this.iToString = new StringBuffer().append(shortName).append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(getName()).append("]").toString();
        }
        return this.iToString;
    }
}
