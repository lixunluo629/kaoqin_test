package org.apache.xmlbeans.impl.store;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDate;
import org.apache.xmlbeans.XmlDecimal;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlLong;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlRuntimeException;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.common.DefaultClassLoaderResourceLoader;
import org.apache.xmlbeans.impl.common.XPath;
import org.apache.xmlbeans.impl.store.Cur;
import org.apache.xmlbeans.impl.store.Locale;
import org.apache.xmlbeans.impl.store.QueryDelegate;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Query.class */
public abstract class Query {
    public static final String QUERY_DELEGATE_INTERFACE = "QUERY_DELEGATE_INTERFACE";
    public static String _useDelegateForXQuery;
    public static String _useXdkForXQuery;
    private static String _delIntfName;
    private static HashMap _xdkQueryCache;
    private static Method _xdkCompileQuery;
    private static boolean _xdkAvailable;
    private static HashMap _xqrlQueryCache;
    private static Method _xqrlCompileQuery;
    private static boolean _xqrlAvailable;
    private static HashMap _xqrl2002QueryCache;
    private static Method _xqrl2002CompileQuery;
    private static boolean _xqrl2002Available;
    static final /* synthetic */ boolean $assertionsDisabled;

    abstract XmlObject[] objectExecute(Cur cur, XmlOptions xmlOptions);

    abstract XmlCursor cursorExecute(Cur cur, XmlOptions xmlOptions);

    static {
        $assertionsDisabled = !Query.class.desiredAssertionStatus();
        _useDelegateForXQuery = "use delegate for xquery";
        _useXdkForXQuery = "use xdk for xquery";
        _xdkQueryCache = new HashMap();
        _xdkAvailable = true;
        _xqrlQueryCache = new HashMap();
        _xqrlAvailable = true;
        _xqrl2002QueryCache = new HashMap();
        _xqrl2002Available = true;
        InputStream in = new DefaultClassLoaderResourceLoader().getResourceAsStream("META-INF/services/org.apache.xmlbeans.impl.store.QueryDelegate.QueryInterface");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            _delIntfName = br.readLine().trim();
            br.close();
        } catch (Exception e) {
            _delIntfName = null;
        }
    }

    static XmlObject[] objectExecQuery(Cur c, String queryExpr, XmlOptions options) {
        return getCompiledQuery(queryExpr, options).objectExecute(c, options);
    }

    static XmlCursor cursorExecQuery(Cur c, String queryExpr, XmlOptions options) {
        return getCompiledQuery(queryExpr, options).cursorExecute(c, options);
    }

    public static synchronized Query getCompiledQuery(String queryExpr, XmlOptions options) {
        return getCompiledQuery(queryExpr, Path.getCurrentNodeVar(options), options);
    }

    static synchronized Query getCompiledQuery(String queryExpr, String currentVar, XmlOptions options) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        int boundaryVal;
        if (!$assertionsDisabled && queryExpr == null) {
            throw new AssertionError();
        }
        XmlOptions options2 = XmlOptions.maskNull(options);
        if (options2.hasOption(Path._forceXqrl2002ForXpathXQuery)) {
            Query query = (Query) _xqrl2002QueryCache.get(queryExpr);
            if (query != null) {
                return query;
            }
            Query query2 = getXqrl2002CompiledQuery(queryExpr, currentVar);
            if (query2 != null) {
                _xqrl2002QueryCache.put(queryExpr, query2);
                return query2;
            }
            throw new RuntimeException("No 2002 query engine found.");
        }
        Map boundary = new HashMap();
        try {
            XPath.compileXPath(queryExpr, currentVar, boundary);
            boundaryVal = boundary.get(XPath._NS_BOUNDARY) == null ? 0 : ((Integer) boundary.get(XPath._NS_BOUNDARY)).intValue();
        } catch (XPath.XPathCompileException e) {
            boundaryVal = boundary.get(XPath._NS_BOUNDARY) == null ? 0 : ((Integer) boundary.get(XPath._NS_BOUNDARY)).intValue();
        } catch (Throwable th) {
            int iIntValue = boundary.get(XPath._NS_BOUNDARY) == null ? 0 : ((Integer) boundary.get(XPath._NS_BOUNDARY)).intValue();
            throw th;
        }
        if (options2.hasOption(_useXdkForXQuery)) {
            Query query3 = (Query) _xdkQueryCache.get(queryExpr);
            if (query3 != null) {
                return query3;
            }
            Query query4 = createXdkCompiledQuery(queryExpr, currentVar);
            if (query4 != null) {
                _xdkQueryCache.put(queryExpr, query4);
                return query4;
            }
        }
        if (!options2.hasOption(_useDelegateForXQuery)) {
            Query query5 = (Query) _xqrlQueryCache.get(queryExpr);
            if (query5 != null) {
                return query5;
            }
            Query query6 = createXqrlCompiledQuery(queryExpr, currentVar);
            if (query6 != null) {
                _xqrlQueryCache.put(queryExpr, query6);
                return query6;
            }
        }
        String delIntfName = options2.hasOption(QUERY_DELEGATE_INTERFACE) ? (String) options2.get(QUERY_DELEGATE_INTERFACE) : _delIntfName;
        Query query7 = DelegateQueryImpl.createDelegateCompiledQuery(delIntfName, queryExpr, currentVar, boundaryVal, options2);
        if (query7 != null) {
            return query7;
        }
        throw new RuntimeException("No query engine found");
    }

    public static synchronized String compileQuery(String queryExpr, XmlOptions options) {
        getCompiledQuery(queryExpr, options);
        return queryExpr;
    }

    private static Query createXdkCompiledQuery(String queryExpr, String currentVar) throws ClassNotFoundException {
        if (!_xdkAvailable) {
            return null;
        }
        if (_xdkCompileQuery == null) {
            try {
                Class xdkImpl = Class.forName("org.apache.xmlbeans.impl.store.OXQXBXqrlImpl");
                _xdkCompileQuery = xdkImpl.getDeclaredMethod("compileQuery", String.class, String.class, Boolean.class);
            } catch (ClassNotFoundException e) {
                _xdkAvailable = false;
                return null;
            } catch (Exception e2) {
                _xdkAvailable = false;
                throw new RuntimeException(e2.getMessage(), e2);
            }
        }
        Object[] args = {queryExpr, currentVar, new Boolean(true)};
        try {
            return (Query) _xdkCompileQuery.invoke(null, args);
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(e3.getMessage(), e3);
        } catch (InvocationTargetException e4) {
            Throwable t = e4.getCause();
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    private static Query createXqrlCompiledQuery(String queryExpr, String currentVar) throws ClassNotFoundException {
        if (!_xqrlAvailable) {
            return null;
        }
        if (_xqrlCompileQuery == null) {
            try {
                Class xqrlImpl = Class.forName("org.apache.xmlbeans.impl.store.XqrlImpl");
                _xqrlCompileQuery = xqrlImpl.getDeclaredMethod("compileQuery", String.class, String.class, Boolean.class);
            } catch (ClassNotFoundException e) {
                _xqrlAvailable = false;
                return null;
            } catch (Exception e2) {
                _xqrlAvailable = false;
                throw new RuntimeException(e2.getMessage(), e2);
            }
        }
        Object[] args = {queryExpr, currentVar, new Boolean(true)};
        try {
            return (Query) _xqrlCompileQuery.invoke(null, args);
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(e3.getMessage(), e3);
        } catch (InvocationTargetException e4) {
            Throwable t = e4.getCause();
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    private static Query getXqrl2002CompiledQuery(String queryExpr, String currentVar) throws ClassNotFoundException {
        if (_xqrl2002Available && _xqrl2002CompileQuery == null) {
            try {
                Class xqrlImpl = Class.forName("org.apache.xmlbeans.impl.store.Xqrl2002Impl");
                _xqrl2002CompileQuery = xqrlImpl.getDeclaredMethod("compileQuery", String.class, String.class, Boolean.class);
            } catch (ClassNotFoundException e) {
                _xqrl2002Available = false;
                return null;
            } catch (Exception e2) {
                _xqrl2002Available = false;
                throw new RuntimeException(e2.getMessage(), e2);
            }
        }
        Object[] args = {queryExpr, currentVar, new Boolean(true)};
        try {
            return (Query) _xqrl2002CompileQuery.invoke(null, args);
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(e3.getMessage(), e3);
        } catch (InvocationTargetException e4) {
            Throwable t = e4.getCause();
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Query$DelegateQueryImpl.class */
    private static final class DelegateQueryImpl extends Query {
        private QueryDelegate.QueryInterface _xqueryImpl;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Query.class.desiredAssertionStatus();
        }

        private DelegateQueryImpl(QueryDelegate.QueryInterface xqueryImpl) {
            this._xqueryImpl = xqueryImpl;
        }

        static Query createDelegateCompiledQuery(String delIntfName, String queryExpr, String currentVar, int boundary, XmlOptions xmlOptions) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
            if (!$assertionsDisabled && (currentVar.startsWith(".") || currentVar.startsWith(".."))) {
                throw new AssertionError();
            }
            QueryDelegate.QueryInterface impl = QueryDelegate.createInstance(delIntfName, queryExpr, currentVar, boundary, xmlOptions);
            if (impl == null) {
                return null;
            }
            return new DelegateQueryImpl(impl);
        }

        @Override // org.apache.xmlbeans.impl.store.Query
        XmlObject[] objectExecute(Cur c, XmlOptions options) {
            return new DelegateQueryEngine(this._xqueryImpl, c, options).objectExecute();
        }

        @Override // org.apache.xmlbeans.impl.store.Query
        XmlCursor cursorExecute(Cur c, XmlOptions options) {
            return new DelegateQueryEngine(this._xqueryImpl, c, options).cursorExecute();
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Query$DelegateQueryImpl$DelegateQueryEngine.class */
        private static class DelegateQueryEngine {
            private Cur _cur;
            private QueryDelegate.QueryInterface _engine;
            private long _version;
            private XmlOptions _options;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !Query.class.desiredAssertionStatus();
            }

            public DelegateQueryEngine(QueryDelegate.QueryInterface xqImpl, Cur c, XmlOptions opt) {
                this._engine = xqImpl;
                this._version = c._locale.version();
                this._cur = c.weakCur(this);
                this._options = opt;
            }

            public XmlObject[] objectExecute() {
                Cur res;
                if (this._cur == null || this._version != this._cur._locale.version()) {
                }
                Map bindings = (Map) XmlOptions.maskNull(this._options).get(XmlOptions.XQUERY_VARIABLE_MAP);
                List resultsList = this._engine.execQuery(this._cur.getDom(), bindings);
                if (!$assertionsDisabled && resultsList.size() <= -1) {
                    throw new AssertionError();
                }
                XmlObject[] result = new XmlObject[resultsList.size()];
                for (int i = 0; i < resultsList.size(); i++) {
                    Locale l = Locale.getLocale(this._cur._locale._schemaTypeLoader, this._options);
                    l.enter();
                    Object node = resultsList.get(i);
                    try {
                        try {
                            if (!(node instanceof Node)) {
                                res = l.load("<xml-fragment/>").tempCur();
                                res.setValue(node.toString());
                                SchemaType type = getType(node);
                                Locale.autoTypeDocument(res, type, null);
                                result[i] = res.getObject();
                            } else {
                                res = loadNode(l, (Node) node);
                            }
                            result[i] = res.getObject();
                            l.exit();
                            res.release();
                        } catch (XmlException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (Throwable th) {
                        l.exit();
                        throw th;
                    }
                }
                release();
                this._engine = null;
                return result;
            }

            private SchemaType getType(Object node) {
                SchemaType type;
                if (node instanceof Integer) {
                    type = XmlInteger.type;
                } else if (node instanceof Double) {
                    type = XmlDouble.type;
                } else if (node instanceof Long) {
                    type = XmlLong.type;
                } else if (node instanceof Float) {
                    type = XmlFloat.type;
                } else if (node instanceof BigDecimal) {
                    type = XmlDecimal.type;
                } else if (node instanceof Boolean) {
                    type = XmlBoolean.type;
                } else if (node instanceof String) {
                    type = XmlString.type;
                } else if (node instanceof Date) {
                    type = XmlDate.type;
                } else {
                    type = XmlAnySimpleType.type;
                }
                return type;
            }

            public XmlCursor cursorExecute() {
                if (this._cur == null || this._version != this._cur._locale.version()) {
                }
                Map bindings = (Map) XmlOptions.maskNull(this._options).get(XmlOptions.XQUERY_VARIABLE_MAP);
                List resultsList = this._engine.execQuery(this._cur.getDom(), bindings);
                if (!$assertionsDisabled && resultsList.size() <= -1) {
                    throw new AssertionError();
                }
                this._engine = null;
                Locale locale = Locale.getLocale(this._cur._locale._schemaTypeLoader, this._options);
                locale.enter();
                Locale.LoadContext _context = new Cur.CurLoadContext(locale, this._options);
                Cursor resultCur = null;
                for (int i = 0; i < resultsList.size(); i++) {
                    try {
                        loadNodeHelper(locale, (Node) resultsList.get(i), _context);
                    } catch (Exception e) {
                        locale.exit();
                    } catch (Throwable th) {
                        locale.exit();
                        throw th;
                    }
                }
                Cur c = _context.finish();
                Locale.associateSourceName(c, this._options);
                Locale.autoTypeDocument(c, null, this._options);
                resultCur = new Cursor(c);
                locale.exit();
                release();
                return resultCur;
            }

            public void release() {
                if (this._cur != null) {
                    this._cur.release();
                    this._cur = null;
                }
            }

            private Cur loadNode(Locale locale, Node node) {
                Locale.LoadContext context = new Cur.CurLoadContext(locale, this._options);
                try {
                    loadNodeHelper(locale, node, context);
                    Cur c = context.finish();
                    Locale.associateSourceName(c, this._options);
                    Locale.autoTypeDocument(c, null, this._options);
                    return c;
                } catch (Exception e) {
                    throw new XmlRuntimeException(e.getMessage(), e);
                }
            }

            private void loadNodeHelper(Locale locale, Node node, Locale.LoadContext context) throws DOMException {
                if (node.getNodeType() == 2) {
                    QName attName = new QName(node.getNamespaceURI(), node.getLocalName(), node.getPrefix());
                    context.attr(attName, node.getNodeValue());
                } else {
                    locale.loadNode(node, context);
                }
            }
        }
    }
}
