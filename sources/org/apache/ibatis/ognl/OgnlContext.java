package org.apache.ibatis.ognl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.ognl.enhance.LocalReference;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlContext.class */
public class OgnlContext implements Map {
    public static final String ROOT_CONTEXT_KEY = "root";
    public static final String THIS_CONTEXT_KEY = "this";
    public static final String TRACE_EVALUATIONS_CONTEXT_KEY = "_traceEvaluations";
    public static final String LAST_EVALUATION_CONTEXT_KEY = "_lastEvaluation";
    public static final String KEEP_LAST_EVALUATION_CONTEXT_KEY = "_keepLastEvaluation";
    public static final String TYPE_CONVERTER_CONTEXT_KEY = "_typeConverter";
    private static final String PROPERTY_KEY_PREFIX = "org.apache.ibatis.ognl";
    private static boolean DEFAULT_TRACE_EVALUATIONS;
    private static boolean DEFAULT_KEEP_LAST_EVALUATION;
    public static final ClassResolver DEFAULT_CLASS_RESOLVER = new DefaultClassResolver();
    public static final TypeConverter DEFAULT_TYPE_CONVERTER = new DefaultTypeConverter();
    public static final MemberAccess DEFAULT_MEMBER_ACCESS = new DefaultMemberAccess(false);
    private static Map RESERVED_KEYS = new HashMap(11);
    private Object _root;
    private Object _currentObject;
    private Node _currentNode;
    private boolean _traceEvaluations;
    private Evaluation _rootEvaluation;
    private Evaluation _currentEvaluation;
    private Evaluation _lastEvaluation;
    private boolean _keepLastEvaluation;
    private final Map _values;
    private ClassResolver _classResolver;
    private TypeConverter _typeConverter;
    private MemberAccess _memberAccess;
    private final List _typeStack;
    private final List _accessorStack;
    private int _localReferenceCounter;
    private Map _localReferenceMap;

    static {
        DEFAULT_TRACE_EVALUATIONS = false;
        DEFAULT_KEEP_LAST_EVALUATION = false;
        RESERVED_KEYS.put(ROOT_CONTEXT_KEY, null);
        RESERVED_KEYS.put(THIS_CONTEXT_KEY, null);
        RESERVED_KEYS.put(TRACE_EVALUATIONS_CONTEXT_KEY, null);
        RESERVED_KEYS.put(LAST_EVALUATION_CONTEXT_KEY, null);
        RESERVED_KEYS.put(KEEP_LAST_EVALUATION_CONTEXT_KEY, null);
        RESERVED_KEYS.put(TYPE_CONVERTER_CONTEXT_KEY, null);
        try {
            String s = System.getProperty("org.apache.ibatis.ognl.traceEvaluations");
            if (s != null) {
                DEFAULT_TRACE_EVALUATIONS = Boolean.valueOf(s.trim()).booleanValue();
            }
            String s2 = System.getProperty("org.apache.ibatis.ognl.keepLastEvaluation");
            if (s2 != null) {
                DEFAULT_KEEP_LAST_EVALUATION = Boolean.valueOf(s2.trim()).booleanValue();
            }
        } catch (SecurityException e) {
        }
    }

    public OgnlContext() {
        this(null, null, null);
    }

    public OgnlContext(ClassResolver classResolver, TypeConverter typeConverter, MemberAccess memberAccess) {
        this(classResolver, typeConverter, memberAccess, new HashMap(23));
    }

    public OgnlContext(Map values) {
        this(null, null, null, values);
    }

    public OgnlContext(ClassResolver classResolver, TypeConverter typeConverter, MemberAccess memberAccess, Map values) {
        this._traceEvaluations = DEFAULT_TRACE_EVALUATIONS;
        this._keepLastEvaluation = DEFAULT_KEEP_LAST_EVALUATION;
        this._classResolver = DEFAULT_CLASS_RESOLVER;
        this._typeConverter = DEFAULT_TYPE_CONVERTER;
        this._memberAccess = DEFAULT_MEMBER_ACCESS;
        this._typeStack = new ArrayList(3);
        this._accessorStack = new ArrayList(3);
        this._localReferenceCounter = 0;
        this._localReferenceMap = null;
        this._values = values;
        if (classResolver != null) {
            this._classResolver = classResolver;
        }
        if (typeConverter != null) {
            this._typeConverter = typeConverter;
        }
        if (memberAccess != null) {
            this._memberAccess = memberAccess;
        }
    }

    public void setValues(Map value) {
        for (Object k : value.keySet()) {
            this._values.put(k, value.get(k));
        }
    }

    public Map getValues() {
        return this._values;
    }

    public void setClassResolver(ClassResolver value) {
        if (value == null) {
            throw new IllegalArgumentException("cannot set ClassResolver to null");
        }
        this._classResolver = value;
    }

    public ClassResolver getClassResolver() {
        return this._classResolver;
    }

    public void setTypeConverter(TypeConverter value) {
        if (value == null) {
            throw new IllegalArgumentException("cannot set TypeConverter to null");
        }
        this._typeConverter = value;
    }

    public TypeConverter getTypeConverter() {
        return this._typeConverter;
    }

    public void setMemberAccess(MemberAccess value) {
        if (value == null) {
            throw new IllegalArgumentException("cannot set MemberAccess to null");
        }
        this._memberAccess = value;
    }

    public MemberAccess getMemberAccess() {
        return this._memberAccess;
    }

    public void setRoot(Object value) {
        this._root = value;
        this._accessorStack.clear();
        this._typeStack.clear();
        this._currentObject = value;
        if (this._currentObject != null) {
            setCurrentType(this._currentObject.getClass());
        }
    }

    public Object getRoot() {
        return this._root;
    }

    public boolean getTraceEvaluations() {
        return this._traceEvaluations;
    }

    public void setTraceEvaluations(boolean value) {
        this._traceEvaluations = value;
    }

    public Evaluation getLastEvaluation() {
        return this._lastEvaluation;
    }

    public void setLastEvaluation(Evaluation value) {
        this._lastEvaluation = value;
    }

    public void recycleLastEvaluation() {
        OgnlRuntime.getEvaluationPool().recycleAll(this._lastEvaluation);
        this._lastEvaluation = null;
    }

    public boolean getKeepLastEvaluation() {
        return this._keepLastEvaluation;
    }

    public void setKeepLastEvaluation(boolean value) {
        this._keepLastEvaluation = value;
    }

    public void setCurrentObject(Object value) {
        this._currentObject = value;
    }

    public Object getCurrentObject() {
        return this._currentObject;
    }

    public void setCurrentAccessor(Class type) {
        this._accessorStack.add(type);
    }

    public Class getCurrentAccessor() {
        if (this._accessorStack.isEmpty()) {
            return null;
        }
        return (Class) this._accessorStack.get(this._accessorStack.size() - 1);
    }

    public Class getPreviousAccessor() {
        if (!this._accessorStack.isEmpty() && this._accessorStack.size() > 1) {
            return (Class) this._accessorStack.get(this._accessorStack.size() - 2);
        }
        return null;
    }

    public Class getFirstAccessor() {
        if (this._accessorStack.isEmpty()) {
            return null;
        }
        return (Class) this._accessorStack.get(0);
    }

    public Class getCurrentType() {
        if (this._typeStack.isEmpty()) {
            return null;
        }
        return (Class) this._typeStack.get(this._typeStack.size() - 1);
    }

    public void setCurrentType(Class type) {
        this._typeStack.add(type);
    }

    public Class getPreviousType() {
        if (!this._typeStack.isEmpty() && this._typeStack.size() > 1) {
            return (Class) this._typeStack.get(this._typeStack.size() - 2);
        }
        return null;
    }

    public void setPreviousType(Class type) {
        if (this._typeStack.isEmpty() || this._typeStack.size() < 2) {
            return;
        }
        this._typeStack.set(this._typeStack.size() - 2, type);
    }

    public Class getFirstType() {
        if (this._typeStack.isEmpty()) {
            return null;
        }
        return (Class) this._typeStack.get(0);
    }

    public void setCurrentNode(Node value) {
        this._currentNode = value;
    }

    public Node getCurrentNode() {
        return this._currentNode;
    }

    public Evaluation getCurrentEvaluation() {
        return this._currentEvaluation;
    }

    public void setCurrentEvaluation(Evaluation value) {
        this._currentEvaluation = value;
    }

    public Evaluation getRootEvaluation() {
        return this._rootEvaluation;
    }

    public void setRootEvaluation(Evaluation value) {
        this._rootEvaluation = value;
    }

    public Evaluation getEvaluation(int relativeIndex) {
        Evaluation result = null;
        if (relativeIndex <= 0) {
            Evaluation parent = this._currentEvaluation;
            while (true) {
                result = parent;
                relativeIndex++;
                if (relativeIndex >= 0 || result == null) {
                    break;
                }
                parent = result.getParent();
            }
        }
        return result;
    }

    public void pushEvaluation(Evaluation value) {
        if (this._currentEvaluation != null) {
            this._currentEvaluation.addChild(value);
        } else {
            setRootEvaluation(value);
        }
        setCurrentEvaluation(value);
    }

    public Evaluation popEvaluation() {
        Evaluation result = this._currentEvaluation;
        setCurrentEvaluation(result.getParent());
        if (this._currentEvaluation == null) {
            setLastEvaluation(getKeepLastEvaluation() ? result : null);
            setRootEvaluation(null);
            setCurrentNode(null);
        }
        return result;
    }

    public int incrementLocalReferenceCounter() {
        int i = this._localReferenceCounter + 1;
        this._localReferenceCounter = i;
        return i;
    }

    public void addLocalReference(String key, LocalReference reference) {
        if (this._localReferenceMap == null) {
            this._localReferenceMap = new LinkedHashMap();
        }
        this._localReferenceMap.put(key, reference);
    }

    public Map getLocalReferences() {
        return this._localReferenceMap;
    }

    @Override // java.util.Map
    public int size() {
        return this._values.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this._values.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this._values.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this._values.containsValue(value);
    }

    @Override // java.util.Map
    public Object get(Object key) {
        Object result;
        if (RESERVED_KEYS.containsKey(key)) {
            if (key.equals(THIS_CONTEXT_KEY)) {
                result = getCurrentObject();
            } else if (key.equals(ROOT_CONTEXT_KEY)) {
                result = getRoot();
            } else if (key.equals(TRACE_EVALUATIONS_CONTEXT_KEY)) {
                result = getTraceEvaluations() ? Boolean.TRUE : Boolean.FALSE;
            } else if (key.equals(LAST_EVALUATION_CONTEXT_KEY)) {
                result = getLastEvaluation();
            } else if (key.equals(KEEP_LAST_EVALUATION_CONTEXT_KEY)) {
                result = getKeepLastEvaluation() ? Boolean.TRUE : Boolean.FALSE;
            } else if (key.equals(TYPE_CONVERTER_CONTEXT_KEY)) {
                result = getTypeConverter();
            } else {
                throw new IllegalArgumentException("unknown reserved key '" + key + "'");
            }
        } else {
            result = this._values.get(key);
        }
        return result;
    }

    @Override // java.util.Map
    public Object put(Object key, Object value) {
        Object result;
        if (RESERVED_KEYS.containsKey(key)) {
            if (key.equals(THIS_CONTEXT_KEY)) {
                result = getCurrentObject();
                setCurrentObject(value);
            } else if (key.equals(ROOT_CONTEXT_KEY)) {
                result = getRoot();
                setRoot(value);
            } else if (key.equals(TRACE_EVALUATIONS_CONTEXT_KEY)) {
                result = getTraceEvaluations() ? Boolean.TRUE : Boolean.FALSE;
                setTraceEvaluations(OgnlOps.booleanValue(value));
            } else if (key.equals(LAST_EVALUATION_CONTEXT_KEY)) {
                result = getLastEvaluation();
                this._lastEvaluation = (Evaluation) value;
            } else if (key.equals(KEEP_LAST_EVALUATION_CONTEXT_KEY)) {
                result = getKeepLastEvaluation() ? Boolean.TRUE : Boolean.FALSE;
                setKeepLastEvaluation(OgnlOps.booleanValue(value));
            } else if (key.equals(TYPE_CONVERTER_CONTEXT_KEY)) {
                result = getTypeConverter();
                setTypeConverter((TypeConverter) value);
            } else {
                throw new IllegalArgumentException("unknown reserved key '" + key + "'");
            }
        } else {
            result = this._values.put(key, value);
        }
        return result;
    }

    @Override // java.util.Map
    public Object remove(Object key) {
        Object result;
        if (RESERVED_KEYS.containsKey(key)) {
            if (key.equals(THIS_CONTEXT_KEY)) {
                result = getCurrentObject();
                setCurrentObject(null);
            } else if (key.equals(ROOT_CONTEXT_KEY)) {
                result = getRoot();
                setRoot(null);
            } else {
                if (key.equals(TRACE_EVALUATIONS_CONTEXT_KEY)) {
                    throw new IllegalArgumentException("can't remove _traceEvaluations from context");
                }
                if (key.equals(LAST_EVALUATION_CONTEXT_KEY)) {
                    result = this._lastEvaluation;
                    setLastEvaluation(null);
                } else {
                    if (key.equals(KEEP_LAST_EVALUATION_CONTEXT_KEY)) {
                        throw new IllegalArgumentException("can't remove _keepLastEvaluation from context");
                    }
                    if (key.equals(TYPE_CONVERTER_CONTEXT_KEY)) {
                        result = getTypeConverter();
                        setTypeConverter(null);
                    } else {
                        throw new IllegalArgumentException("unknown reserved key '" + key + "'");
                    }
                }
            }
        } else {
            result = this._values.remove(key);
        }
        return result;
    }

    @Override // java.util.Map
    public void putAll(Map t) {
        for (Object k : t.keySet()) {
            put(k, t.get(k));
        }
    }

    @Override // java.util.Map
    public void clear() {
        this._values.clear();
        this._typeStack.clear();
        this._accessorStack.clear();
        this._localReferenceCounter = 0;
        if (this._localReferenceMap != null) {
            this._localReferenceMap.clear();
        }
        setRoot(null);
        setCurrentObject(null);
        setRootEvaluation(null);
        setCurrentEvaluation(null);
        setLastEvaluation(null);
        setCurrentNode(null);
        setClassResolver(DEFAULT_CLASS_RESOLVER);
        setTypeConverter(DEFAULT_TYPE_CONVERTER);
        setMemberAccess(DEFAULT_MEMBER_ACCESS);
    }

    @Override // java.util.Map
    public Set keySet() {
        return this._values.keySet();
    }

    @Override // java.util.Map
    public Collection values() {
        return this._values.values();
    }

    @Override // java.util.Map
    public Set entrySet() {
        return this._values.entrySet();
    }

    @Override // java.util.Map
    public boolean equals(Object o) {
        return this._values.equals(o);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this._values.hashCode();
    }
}
