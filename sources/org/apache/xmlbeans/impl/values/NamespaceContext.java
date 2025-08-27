package org.apache.xmlbeans.impl.values;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Map;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.PrefixResolver;
import org.apache.xmlbeans.xml.stream.StartElement;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/NamespaceContext.class */
public class NamespaceContext implements PrefixResolver {
    private static final int TYPE_STORE = 1;
    private static final int XML_OBJECT = 2;
    private static final int MAP = 3;
    private static final int START_ELEMENT = 4;
    private static final int RESOLVER = 5;
    private Object _obj;
    private int _code = 3;
    private static ThreadLocal tl_namespaceContextStack;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NamespaceContext.class.desiredAssertionStatus();
        tl_namespaceContextStack = new ThreadLocal();
    }

    public NamespaceContext(Map prefixToUriMap) {
        this._obj = prefixToUriMap;
    }

    public NamespaceContext(TypeStore typeStore) {
        this._obj = typeStore;
    }

    public NamespaceContext(XmlObject xmlObject) {
        this._obj = xmlObject;
    }

    public NamespaceContext(StartElement start) {
        this._obj = start;
    }

    public NamespaceContext(PrefixResolver resolver) {
        this._obj = resolver;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/NamespaceContext$NamespaceContextStack.class */
    private static final class NamespaceContextStack {
        NamespaceContext current;
        ArrayList stack;

        private NamespaceContextStack() {
            this.stack = new ArrayList();
        }

        final void push(NamespaceContext next) {
            this.stack.add(this.current);
            this.current = next;
        }

        final void pop() {
            this.current = (NamespaceContext) this.stack.get(this.stack.size() - 1);
            this.stack.remove(this.stack.size() - 1);
        }
    }

    public static void clearThreadLocals() {
        tl_namespaceContextStack.remove();
    }

    private static NamespaceContextStack getNamespaceContextStack() {
        NamespaceContextStack namespaceContextStack = (NamespaceContextStack) tl_namespaceContextStack.get();
        if (namespaceContextStack == null) {
            namespaceContextStack = new NamespaceContextStack();
            tl_namespaceContextStack.set(namespaceContextStack);
        }
        return namespaceContextStack;
    }

    public static void push(NamespaceContext next) {
        getNamespaceContextStack().push(next);
    }

    public static void pop() {
        NamespaceContextStack nsContextStack = getNamespaceContextStack();
        nsContextStack.pop();
        if (nsContextStack.stack.size() == 0) {
            tl_namespaceContextStack.set(null);
        }
    }

    public static PrefixResolver getCurrent() {
        return getNamespaceContextStack().current;
    }

    @Override // org.apache.xmlbeans.impl.common.PrefixResolver
    public String getNamespaceForPrefix(String prefix) throws IllegalArgumentException {
        if (prefix != null && prefix.equals("xml")) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        switch (this._code) {
            case 1:
                return ((TypeStore) this._obj).getNamespaceForPrefix(prefix);
            case 2:
                Object obj = this._obj;
                if (Proxy.isProxyClass(obj.getClass())) {
                    obj = Proxy.getInvocationHandler(obj);
                }
                if (obj instanceof TypeStoreUser) {
                    return ((TypeStoreUser) obj).get_store().getNamespaceForPrefix(prefix);
                }
                XmlCursor cur = ((XmlObject) this._obj).newCursor();
                if (cur != null) {
                    if (cur.currentTokenType() == XmlCursor.TokenType.ATTR) {
                        cur.toParent();
                    }
                    try {
                        String strNamespaceForPrefix = cur.namespaceForPrefix(prefix);
                        cur.dispose();
                        return strNamespaceForPrefix;
                    } catch (Throwable th) {
                        cur.dispose();
                        throw th;
                    }
                }
                break;
            case 3:
                break;
            case 4:
                return ((StartElement) this._obj).getNamespaceUri(prefix);
            case 5:
                return ((PrefixResolver) this._obj).getNamespaceForPrefix(prefix);
            default:
                if ($assertionsDisabled) {
                    return null;
                }
                throw new AssertionError("Improperly initialized NamespaceContext.");
        }
        return (String) ((Map) this._obj).get(prefix);
    }
}
