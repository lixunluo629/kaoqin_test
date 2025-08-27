package org.apache.xmlbeans.impl.store;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlDate;
import org.apache.xmlbeans.XmlDecimal;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlLong;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.common.DefaultClassLoaderResourceLoader;
import org.apache.xmlbeans.impl.common.XPath;
import org.apache.xmlbeans.impl.store.DomImpl;
import org.apache.xmlbeans.impl.store.PathDelegate;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Path.class */
public abstract class Path {
    public static final String PATH_DELEGATE_INTERFACE = "PATH_DELEGATE_INTERFACE";
    private static final int USE_XBEAN = 1;
    private static final int USE_XQRL = 2;
    private static final int USE_DELEGATE = 4;
    private static final int USE_XQRL2002 = 8;
    private static final int USE_XDK = 16;
    private static Method _xdkCompilePath;
    private static Method _xqrlCompilePath;
    private static Method _xqrl2002CompilePath;
    private static final String _delIntfName;
    protected final String _pathKey;
    public static String _useDelegateForXpath = "use delegate for xpath";
    public static String _useXdkForXpath = "use xdk for xpath";
    public static String _useXqrlForXpath = "use xqrl for xpath";
    public static String _useXbeanForXpath = "use xbean for xpath";
    public static String _forceXqrl2002ForXpathXQuery = "use xqrl-2002 for xpath";
    private static Map _xbeanPathCache = new WeakHashMap();
    private static Map _xdkPathCache = new WeakHashMap();
    private static Map _xqrlPathCache = new WeakHashMap();
    private static Map _xqrl2002PathCache = new WeakHashMap();
    private static boolean _xdkAvailable = true;
    private static boolean _xqrlAvailable = true;
    private static boolean _xqrl2002Available = true;
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Path$PathEngine.class */
    interface PathEngine {
        void release();

        boolean next(Cur cur);
    }

    abstract PathEngine execute(Cur cur, XmlOptions xmlOptions);

    static {
        InputStream in = new DefaultClassLoaderResourceLoader().getResourceAsStream("META-INF/services/org.apache.xmlbeans.impl.store.PathDelegate.SelectPathInterface");
        String name = null;
        if (in != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                name = br.readLine().trim();
                br.close();
            } catch (Exception e) {
            }
        }
        _delIntfName = name;
    }

    Path(String key) {
        this._pathKey = key;
    }

    static String getCurrentNodeVar(XmlOptions options) {
        String currentNodeVar = OgnlContext.THIS_CONTEXT_KEY;
        XmlOptions options2 = XmlOptions.maskNull(options);
        if (options2.hasOption(XmlOptions.XQUERY_CURRENT_NODE_VAR)) {
            currentNodeVar = (String) options2.get(XmlOptions.XQUERY_CURRENT_NODE_VAR);
            if (currentNodeVar.startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)) {
                throw new IllegalArgumentException("Omit the '$' prefix for the current node variable");
            }
        }
        return currentNodeVar;
    }

    public static Path getCompiledPath(String pathExpr, XmlOptions options) {
        XmlOptions options2 = XmlOptions.maskNull(options);
        int force = options2.hasOption(_useDelegateForXpath) ? 4 : options2.hasOption(_useXqrlForXpath) ? 2 : options2.hasOption(_useXdkForXpath) ? 16 : options2.hasOption(_useXbeanForXpath) ? 1 : options2.hasOption(_forceXqrl2002ForXpathXQuery) ? 8 : 23;
        String delIntfName = options2.hasOption(PATH_DELEGATE_INTERFACE) ? (String) options2.get(PATH_DELEGATE_INTERFACE) : _delIntfName;
        return getCompiledPath(pathExpr, force, getCurrentNodeVar(options2), delIntfName);
    }

    static Path getCompiledPath(String pathExpr, int force, String currentVar, String delIntfName) {
        Path path = null;
        WeakReference pathWeakRef = null;
        Map namespaces = (force & 4) != 0 ? new HashMap() : null;
        lock.readLock().lock();
        try {
            if ((force & 1) != 0) {
                pathWeakRef = (WeakReference) _xbeanPathCache.get(pathExpr);
            }
            if (pathWeakRef == null && (force & 2) != 0) {
                pathWeakRef = (WeakReference) _xqrlPathCache.get(pathExpr);
            }
            if (pathWeakRef == null && (force & 16) != 0) {
                pathWeakRef = (WeakReference) _xdkPathCache.get(pathExpr);
            }
            if (pathWeakRef == null && (force & 8) != 0) {
                pathWeakRef = (WeakReference) _xqrl2002PathCache.get(pathExpr);
            }
            if (pathWeakRef != null) {
                path = (Path) pathWeakRef.get();
            }
            if (path != null) {
                Path path2 = path;
                lock.readLock().unlock();
                return path2;
            }
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                if ((force & 1) != 0) {
                    WeakReference pathWeakRef2 = (WeakReference) _xbeanPathCache.get(pathExpr);
                    if (pathWeakRef2 != null) {
                        path = (Path) pathWeakRef2.get();
                    }
                    if (path == null) {
                        path = getCompiledPathXbean(pathExpr, currentVar, namespaces);
                    }
                }
                if (path == null && (force & 2) != 0) {
                    WeakReference pathWeakRef3 = (WeakReference) _xqrlPathCache.get(pathExpr);
                    if (pathWeakRef3 != null) {
                        path = (Path) pathWeakRef3.get();
                    }
                    if (path == null) {
                        path = getCompiledPathXqrl(pathExpr, currentVar);
                    }
                }
                if (path == null && (force & 16) != 0) {
                    WeakReference pathWeakRef4 = (WeakReference) _xdkPathCache.get(pathExpr);
                    if (pathWeakRef4 != null) {
                        path = (Path) pathWeakRef4.get();
                    }
                    if (path == null) {
                        path = getCompiledPathXdk(pathExpr, currentVar);
                    }
                }
                if (path == null && (force & 4) != 0) {
                    path = getCompiledPathDelegate(pathExpr, currentVar, namespaces, delIntfName);
                }
                if (path == null && (force & 8) != 0) {
                    WeakReference pathWeakRef5 = (WeakReference) _xqrl2002PathCache.get(pathExpr);
                    if (pathWeakRef5 != null) {
                        path = (Path) pathWeakRef5.get();
                    }
                    if (path == null) {
                        path = getCompiledPathXqrl2002(pathExpr, currentVar);
                    }
                }
                if (path == null) {
                    StringBuffer errMessage = new StringBuffer();
                    if ((force & 1) != 0) {
                        errMessage.append(" Trying XBeans path engine...");
                    }
                    if ((force & 2) != 0) {
                        errMessage.append(" Trying XQRL...");
                    }
                    if ((force & 16) != 0) {
                        errMessage.append(" Trying XDK...");
                    }
                    if ((force & 4) != 0) {
                        errMessage.append(" Trying delegated path engine...");
                    }
                    if ((force & 8) != 0) {
                        errMessage.append(" Trying XQRL2002...");
                    }
                    throw new RuntimeException(errMessage.toString() + " FAILED on " + pathExpr);
                }
                lock.writeLock().unlock();
                return path;
            } finally {
                lock.writeLock().unlock();
            }
        } catch (Throwable th) {
            lock.readLock().unlock();
            throw th;
        }
    }

    private static Path getCompiledPathXdk(String pathExpr, String currentVar) throws ClassNotFoundException {
        Path path = createXdkCompiledPath(pathExpr, currentVar);
        if (path != null) {
            _xdkPathCache.put(path._pathKey, new WeakReference(path));
        }
        return path;
    }

    private static Path getCompiledPathXqrl(String pathExpr, String currentVar) throws ClassNotFoundException {
        Path path = createXqrlCompiledPath(pathExpr, currentVar);
        if (path != null) {
            _xqrlPathCache.put(path._pathKey, new WeakReference(path));
        }
        return path;
    }

    private static Path getCompiledPathXqrl2002(String pathExpr, String currentVar) throws ClassNotFoundException {
        Path path = createXqrl2002CompiledPath(pathExpr, currentVar);
        if (path != null) {
            _xqrl2002PathCache.put(path._pathKey, new WeakReference(path));
        }
        return path;
    }

    private static Path getCompiledPathXbean(String pathExpr, String currentVar, Map namespaces) {
        Path path = XbeanPath.create(pathExpr, currentVar, namespaces);
        if (path != null) {
            _xbeanPathCache.put(path._pathKey, new WeakReference(path));
        }
        return path;
    }

    private static Path getCompiledPathDelegate(String pathExpr, String currentVar, Map namespaces, String delIntfName) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        if (namespaces == null) {
            namespaces = new HashMap();
        }
        try {
            XPath.compileXPath(pathExpr, currentVar, namespaces);
        } catch (XPath.XPathCompileException e) {
        }
        int offset = namespaces.get(XPath._NS_BOUNDARY) == null ? 0 : ((Integer) namespaces.get(XPath._NS_BOUNDARY)).intValue();
        namespaces.remove(XPath._NS_BOUNDARY);
        Path path = DelegatePathImpl.create(delIntfName, pathExpr.substring(offset), currentVar, namespaces);
        return path;
    }

    public static String compilePath(String pathExpr, XmlOptions options) {
        return getCompiledPath(pathExpr, options)._pathKey;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Path$XbeanPath.class */
    private static final class XbeanPath extends Path {
        private final String _currentVar;
        private final XPath _compiledPath;
        public Map namespaces;

        static Path create(String pathExpr, String currentVar, Map namespaces) {
            try {
                return new XbeanPath(pathExpr, currentVar, XPath.compileXPath(pathExpr, currentVar, namespaces));
            } catch (XPath.XPathCompileException e) {
                return null;
            }
        }

        private XbeanPath(String pathExpr, String currentVar, XPath xpath) {
            super(pathExpr);
            this._currentVar = currentVar;
            this._compiledPath = xpath;
        }

        @Override // org.apache.xmlbeans.impl.store.Path
        PathEngine execute(Cur c, XmlOptions options) {
            XmlOptions options2 = XmlOptions.maskNull(options);
            String delIntfName = options2.hasOption(Path.PATH_DELEGATE_INTERFACE) ? (String) options2.get(Path.PATH_DELEGATE_INTERFACE) : Path._delIntfName;
            if (!c.isContainer() || this._compiledPath.sawDeepDot()) {
                return getCompiledPath(this._pathKey, 22, this._currentVar, delIntfName).execute(c, options2);
            }
            return new XbeanPathEngine(this._compiledPath, c);
        }
    }

    private static Path createXdkCompiledPath(String pathExpr, String currentVar) throws ClassNotFoundException {
        if (!_xdkAvailable) {
            return null;
        }
        if (_xdkCompilePath == null) {
            try {
                Class xdkImpl = Class.forName("org.apache.xmlbeans.impl.store.OXQXBXqrlImpl");
                _xdkCompilePath = xdkImpl.getDeclaredMethod("compilePath", String.class, String.class, Boolean.class);
            } catch (ClassNotFoundException e) {
                _xdkAvailable = false;
                return null;
            } catch (Exception e2) {
                _xdkAvailable = false;
                throw new RuntimeException(e2.getMessage(), e2);
            }
        }
        Object[] args = {pathExpr, currentVar, new Boolean(true)};
        try {
            return (Path) _xdkCompilePath.invoke(null, args);
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(e3.getMessage(), e3);
        } catch (InvocationTargetException e4) {
            Throwable t = e4.getCause();
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    private static Path createXqrlCompiledPath(String pathExpr, String currentVar) throws ClassNotFoundException {
        if (!_xqrlAvailable) {
            return null;
        }
        if (_xqrlCompilePath == null) {
            try {
                Class xqrlImpl = Class.forName("org.apache.xmlbeans.impl.store.XqrlImpl");
                _xqrlCompilePath = xqrlImpl.getDeclaredMethod("compilePath", String.class, String.class, Boolean.class);
            } catch (ClassNotFoundException e) {
                _xqrlAvailable = false;
                return null;
            } catch (Exception e2) {
                _xqrlAvailable = false;
                throw new RuntimeException(e2.getMessage(), e2);
            }
        }
        Object[] args = {pathExpr, currentVar, new Boolean(true)};
        try {
            return (Path) _xqrlCompilePath.invoke(null, args);
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(e3.getMessage(), e3);
        } catch (InvocationTargetException e4) {
            Throwable t = e4.getCause();
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    private static Path createXqrl2002CompiledPath(String pathExpr, String currentVar) throws ClassNotFoundException {
        if (!_xqrl2002Available) {
            return null;
        }
        if (_xqrl2002CompilePath == null) {
            try {
                Class xqrlImpl = Class.forName("org.apache.xmlbeans.impl.store.Xqrl2002Impl");
                _xqrl2002CompilePath = xqrlImpl.getDeclaredMethod("compilePath", String.class, String.class, Boolean.class);
            } catch (ClassNotFoundException e) {
                _xqrl2002Available = false;
                return null;
            } catch (Exception e2) {
                _xqrl2002Available = false;
                throw new RuntimeException(e2.getMessage(), e2);
            }
        }
        Object[] args = {pathExpr, currentVar, new Boolean(true)};
        try {
            return (Path) _xqrl2002CompilePath.invoke(null, args);
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(e3.getMessage(), e3);
        } catch (InvocationTargetException e4) {
            Throwable t = e4.getCause();
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Path$XbeanPathEngine.class */
    private static final class XbeanPathEngine extends XPath.ExecutionContext implements PathEngine {
        private final long _version;
        private Cur _cur;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Path.class.desiredAssertionStatus();
        }

        XbeanPathEngine(XPath xpath, Cur c) {
            if (!$assertionsDisabled && !c.isContainer()) {
                throw new AssertionError();
            }
            this._version = c._locale.version();
            this._cur = c.weakCur(this);
            this._cur.push();
            init(xpath);
            int ret = start();
            if ((ret & 1) != 0) {
                c.addToSelection();
            }
            doAttrs(ret, c);
            if ((ret & 2) == 0 || !Locale.toFirstChildElement(this._cur)) {
                release();
            }
        }

        private void advance(Cur c) {
            if (!$assertionsDisabled && this._cur == null) {
                throw new AssertionError();
            }
            if (this._cur.isFinish()) {
                if (this._cur.isAtEndOfLastPush()) {
                    release();
                    return;
                } else {
                    end();
                    this._cur.next();
                    return;
                }
            }
            if (this._cur.isElem()) {
                int ret = element(this._cur.getName());
                if ((ret & 1) != 0) {
                    c.addToSelection(this._cur);
                }
                doAttrs(ret, c);
                if ((ret & 2) == 0 || !Locale.toFirstChildElement(this._cur)) {
                    end();
                    this._cur.skip();
                    return;
                }
                return;
            }
            do {
                this._cur.next();
            } while (!this._cur.isContainerOrFinish());
        }

        private void doAttrs(int ret, Cur c) {
            if (!$assertionsDisabled && !this._cur.isContainer()) {
                throw new AssertionError();
            }
            if ((ret & 4) != 0 && this._cur.toFirstAttr()) {
                do {
                    if (attr(this._cur.getName())) {
                        c.addToSelection(this._cur);
                    }
                } while (this._cur.toNextAttr());
                this._cur.toParent();
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Path.PathEngine
        public boolean next(Cur c) {
            if (this._cur != null && this._version != this._cur._locale.version()) {
                throw new ConcurrentModificationException("Document changed during select");
            }
            int startCount = c.selectionCount();
            while (this._cur != null) {
                advance(c);
                if (startCount != c.selectionCount()) {
                    return true;
                }
            }
            return false;
        }

        @Override // org.apache.xmlbeans.impl.store.Path.PathEngine
        public void release() {
            if (this._cur != null) {
                this._cur.release();
                this._cur = null;
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Path$DelegatePathImpl.class */
    private static final class DelegatePathImpl extends Path {
        private PathDelegate.SelectPathInterface _xpathImpl;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Path.class.desiredAssertionStatus();
        }

        static Path create(String implClassName, String pathExpr, String currentNodeVar, Map namespaceMap) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
            if (!$assertionsDisabled && currentNodeVar.startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)) {
                throw new AssertionError();
            }
            PathDelegate.SelectPathInterface impl = PathDelegate.createInstance(implClassName, pathExpr, currentNodeVar, namespaceMap);
            if (impl == null) {
                return null;
            }
            return new DelegatePathImpl(impl, pathExpr);
        }

        private DelegatePathImpl(PathDelegate.SelectPathInterface xpathImpl, String pathExpr) {
            super(pathExpr);
            this._xpathImpl = xpathImpl;
        }

        @Override // org.apache.xmlbeans.impl.store.Path
        protected PathEngine execute(Cur c, XmlOptions options) {
            return new DelegatePathEngine(this._xpathImpl, c);
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Path$DelegatePathImpl$DelegatePathEngine.class */
        private static class DelegatePathEngine extends XPath.ExecutionContext implements PathEngine {
            private Cur _cur;
            private PathDelegate.SelectPathInterface _engine;
            private long _version;
            static final /* synthetic */ boolean $assertionsDisabled;
            private final DateFormat xmlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            private boolean _firstCall = true;

            static {
                $assertionsDisabled = !Path.class.desiredAssertionStatus();
            }

            DelegatePathEngine(PathDelegate.SelectPathInterface xpathImpl, Cur c) {
                this._engine = xpathImpl;
                this._version = c._locale.version();
                this._cur = c.weakCur(this);
            }

            @Override // org.apache.xmlbeans.impl.store.Path.PathEngine
            public boolean next(Cur c) {
                Cur pos;
                String value;
                if (!this._firstCall) {
                    return false;
                }
                this._firstCall = false;
                if (this._cur != null && this._version != this._cur._locale.version()) {
                    throw new ConcurrentModificationException("Document changed during select");
                }
                Object context_node = this._cur.getDom();
                List resultsList = this._engine.selectPath(context_node);
                for (int i = 0; i < resultsList.size(); i++) {
                    Object node = resultsList.get(i);
                    if (!(node instanceof Node)) {
                        Object obj = resultsList.get(i);
                        if (obj instanceof Date) {
                            value = this.xmlDateFormat.format((Date) obj);
                        } else if (obj instanceof BigDecimal) {
                            value = ((BigDecimal) obj).toPlainString();
                        } else {
                            value = obj.toString();
                        }
                        Locale l = c._locale;
                        try {
                            pos = l.load("<xml-fragment/>").tempCur();
                            pos.setValue(value);
                            SchemaType type = getType(node);
                            Locale.autoTypeDocument(pos, type, null);
                            pos.next();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        if (!$assertionsDisabled && !(node instanceof DomImpl.Dom)) {
                            throw new AssertionError("New object created in XPATH!");
                        }
                        pos = ((DomImpl.Dom) node).tempCur();
                    }
                    c.addToSelection(pos);
                    pos.release();
                }
                release();
                this._engine = null;
                return true;
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

            @Override // org.apache.xmlbeans.impl.store.Path.PathEngine
            public void release() {
                if (this._cur != null) {
                    this._cur.release();
                    this._cur = null;
                }
            }
        }
    }
}
