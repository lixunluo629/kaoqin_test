package org.aspectj.runtime.reflect;

import java.lang.ref.SoftReference;
import java.util.StringTokenizer;
import org.aspectj.lang.Signature;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/reflect/SignatureImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/reflect/SignatureImpl.class */
abstract class SignatureImpl implements Signature {
    int modifiers;
    String name;
    String declaringTypeName;
    Class declaringType;
    Cache stringCache;
    private String stringRep;
    ClassLoader lookupClassLoader;
    static final char SEP = '-';
    static final String INNER_SEP = ":";
    private static boolean useCache = true;
    static String[] EMPTY_STRING_ARRAY = new String[0];
    static Class[] EMPTY_CLASS_ARRAY = new Class[0];

    /* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/reflect/SignatureImpl$Cache.class
 */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/reflect/SignatureImpl$Cache.class */
    private interface Cache {
        String get(int i);

        void set(int i, String str);
    }

    protected abstract String createToString(StringMaker stringMaker);

    SignatureImpl(int modifiers, String name, Class declaringType) {
        this.modifiers = -1;
        this.lookupClassLoader = null;
        this.modifiers = modifiers;
        this.name = name;
        this.declaringType = declaringType;
    }

    String toString(StringMaker sm) {
        String result = null;
        if (useCache) {
            if (this.stringCache == null) {
                try {
                    this.stringCache = new CacheImpl();
                } catch (Throwable th) {
                    useCache = false;
                }
            } else {
                result = this.stringCache.get(sm.cacheOffset);
            }
        }
        if (result == null) {
            result = createToString(sm);
        }
        if (useCache) {
            this.stringCache.set(sm.cacheOffset, result);
        }
        return result;
    }

    @Override // org.aspectj.lang.Signature
    public final String toString() {
        return toString(StringMaker.middleStringMaker);
    }

    @Override // org.aspectj.lang.Signature
    public final String toShortString() {
        return toString(StringMaker.shortStringMaker);
    }

    @Override // org.aspectj.lang.Signature
    public final String toLongString() {
        return toString(StringMaker.longStringMaker);
    }

    @Override // org.aspectj.lang.Signature
    public int getModifiers() {
        if (this.modifiers == -1) {
            this.modifiers = extractInt(0);
        }
        return this.modifiers;
    }

    @Override // org.aspectj.lang.Signature
    public String getName() {
        if (this.name == null) {
            this.name = extractString(1);
        }
        return this.name;
    }

    @Override // org.aspectj.lang.Signature
    public Class getDeclaringType() {
        if (this.declaringType == null) {
            this.declaringType = extractType(2);
        }
        return this.declaringType;
    }

    @Override // org.aspectj.lang.Signature
    public String getDeclaringTypeName() {
        if (this.declaringTypeName == null) {
            this.declaringTypeName = getDeclaringType().getName();
        }
        return this.declaringTypeName;
    }

    String fullTypeName(Class type) {
        return type == null ? "ANONYMOUS" : type.isArray() ? new StringBuffer().append(fullTypeName(type.getComponentType())).append("[]").toString() : type.getName().replace('$', '.');
    }

    String stripPackageName(String name) {
        int dot = name.lastIndexOf(46);
        return dot == -1 ? name : name.substring(dot + 1);
    }

    String shortTypeName(Class type) {
        return type == null ? "ANONYMOUS" : type.isArray() ? new StringBuffer().append(shortTypeName(type.getComponentType())).append("[]").toString() : stripPackageName(type.getName()).replace('$', '.');
    }

    void addFullTypeNames(StringBuffer buf, Class[] types) {
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(fullTypeName(types[i]));
        }
    }

    void addShortTypeNames(StringBuffer buf, Class[] types) {
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(shortTypeName(types[i]));
        }
    }

    void addTypeArray(StringBuffer buf, Class[] types) {
        addFullTypeNames(buf, types);
    }

    public void setLookupClassLoader(ClassLoader loader) {
        this.lookupClassLoader = loader;
    }

    private ClassLoader getLookupClassLoader() {
        if (this.lookupClassLoader == null) {
            this.lookupClassLoader = getClass().getClassLoader();
        }
        return this.lookupClassLoader;
    }

    public SignatureImpl(String stringRep) {
        this.modifiers = -1;
        this.lookupClassLoader = null;
        this.stringRep = stringRep;
    }

    String extractString(int n) {
        int endIndex;
        int startIndex = 0;
        int iIndexOf = this.stringRep.indexOf(45);
        while (true) {
            endIndex = iIndexOf;
            int i = n;
            n--;
            if (i <= 0) {
                break;
            }
            startIndex = endIndex + 1;
            iIndexOf = this.stringRep.indexOf(45, startIndex);
        }
        if (endIndex == -1) {
            endIndex = this.stringRep.length();
        }
        return this.stringRep.substring(startIndex, endIndex);
    }

    int extractInt(int n) {
        String s = extractString(n);
        return Integer.parseInt(s, 16);
    }

    Class extractType(int n) {
        String s = extractString(n);
        return Factory.makeClass(s, getLookupClassLoader());
    }

    String[] extractStrings(int n) {
        String s = extractString(n);
        StringTokenizer st = new StringTokenizer(s, ":");
        int N = st.countTokens();
        String[] ret = new String[N];
        for (int i = 0; i < N; i++) {
            ret[i] = st.nextToken();
        }
        return ret;
    }

    Class[] extractTypes(int n) {
        String s = extractString(n);
        StringTokenizer st = new StringTokenizer(s, ":");
        int N = st.countTokens();
        Class[] ret = new Class[N];
        for (int i = 0; i < N; i++) {
            ret[i] = Factory.makeClass(st.nextToken(), getLookupClassLoader());
        }
        return ret;
    }

    static void setUseCache(boolean b) {
        useCache = b;
    }

    static boolean getUseCache() {
        return useCache;
    }

    /* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/reflect/SignatureImpl$CacheImpl.class
 */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/reflect/SignatureImpl$CacheImpl.class */
    private static final class CacheImpl implements Cache {
        private SoftReference toStringCacheRef;

        public CacheImpl() {
            makeCache();
        }

        @Override // org.aspectj.runtime.reflect.SignatureImpl.Cache
        public String get(int cacheOffset) {
            String[] cachedArray = array();
            if (cachedArray == null) {
                return null;
            }
            return cachedArray[cacheOffset];
        }

        @Override // org.aspectj.runtime.reflect.SignatureImpl.Cache
        public void set(int cacheOffset, String result) {
            String[] cachedArray = array();
            if (cachedArray == null) {
                cachedArray = makeCache();
            }
            cachedArray[cacheOffset] = result;
        }

        private String[] array() {
            return (String[]) this.toStringCacheRef.get();
        }

        private String[] makeCache() {
            String[] array = new String[3];
            this.toStringCacheRef = new SoftReference(array);
            return array;
        }
    }
}
