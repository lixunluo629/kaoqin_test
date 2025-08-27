package org.apache.xmlbeans.impl.schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaAttributeGroup;
import org.apache.xmlbeans.SchemaField;
import org.apache.xmlbeans.SchemaGlobalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaModelGroup;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlFactoryHook;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlSaxHandler;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.store.Locale;
import org.apache.xmlbeans.impl.validator.ValidatingXMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaTypeLoaderBase.class */
public abstract class SchemaTypeLoaderBase implements SchemaTypeLoader {
    private static final String USER_AGENT;
    private static final Method _pathCompiler;
    private static final Method _queryCompiler;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SchemaTypeLoaderBase.class.desiredAssertionStatus();
        USER_AGENT = "XMLBeans/" + XmlBeans.getVersion() + " (" + XmlBeans.getTitle() + ")";
        _pathCompiler = getMethod("org.apache.xmlbeans.impl.store.Path", "compilePath", new Class[]{String.class, XmlOptions.class});
        _queryCompiler = getMethod("org.apache.xmlbeans.impl.store.Query", "compileQuery", new Class[]{String.class, XmlOptions.class});
    }

    private static Method getMethod(String className, String methodName, Class[] args) {
        try {
            return Class.forName(className).getDeclaredMethod(methodName, args);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot find " + className + "." + methodName + ".  verify that xmlstore (from xbean.jar) is on classpath");
        }
    }

    private static Object invokeMethod(Method method, Object[] args) {
        try {
            return method.invoke(method, args);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            IllegalStateException ise = new IllegalStateException(t.getMessage());
            ise.initCause(t);
            throw ise;
        } catch (Exception e2) {
            IllegalStateException ise2 = new IllegalStateException(e2.getMessage());
            ise2.initCause(e2);
            throw ise2;
        }
    }

    private static String doCompilePath(String pathExpr, XmlOptions options) {
        return (String) invokeMethod(_pathCompiler, new Object[]{pathExpr, options});
    }

    private static String doCompileQuery(String queryExpr, XmlOptions options) {
        return (String) invokeMethod(_queryCompiler, new Object[]{queryExpr, options});
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType findType(QName name) {
        SchemaType.Ref ref = findTypeRef(name);
        if (ref == null) {
            return null;
        }
        SchemaType result = ref.get();
        if ($assertionsDisabled || result != null) {
            return result;
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType findDocumentType(QName name) {
        SchemaType.Ref ref = findDocumentTypeRef(name);
        if (ref == null) {
            return null;
        }
        SchemaType result = ref.get();
        if ($assertionsDisabled || result != null) {
            return result;
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType findAttributeType(QName name) {
        SchemaType.Ref ref = findAttributeTypeRef(name);
        if (ref == null) {
            return null;
        }
        SchemaType result = ref.get();
        if ($assertionsDisabled || result != null) {
            return result;
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaModelGroup findModelGroup(QName name) {
        SchemaModelGroup.Ref ref = findModelGroupRef(name);
        if (ref == null) {
            return null;
        }
        SchemaModelGroup result = ref.get();
        if ($assertionsDisabled || result != null) {
            return result;
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaAttributeGroup findAttributeGroup(QName name) {
        SchemaAttributeGroup.Ref ref = findAttributeGroupRef(name);
        if (ref == null) {
            return null;
        }
        SchemaAttributeGroup result = ref.get();
        if ($assertionsDisabled || result != null) {
            return result;
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalElement findElement(QName name) {
        SchemaGlobalElement.Ref ref = findElementRef(name);
        if (ref == null) {
            return null;
        }
        SchemaGlobalElement result = ref.get();
        if ($assertionsDisabled || result != null) {
            return result;
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalAttribute findAttribute(QName name) {
        SchemaGlobalAttribute.Ref ref = findAttributeRef(name);
        if (ref == null) {
            return null;
        }
        SchemaGlobalAttribute result = ref.get();
        if ($assertionsDisabled || result != null) {
            return result;
        }
        throw new AssertionError();
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public XmlObject newInstance(SchemaType type, XmlOptions options) {
        XmlFactoryHook hook = XmlFactoryHook.ThreadContext.getHook();
        if (hook != null) {
            return hook.newInstance(this, type, options);
        }
        return Locale.newInstance(this, type, options);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public XmlObject parse(String xmlText, SchemaType type, XmlOptions options) throws XmlException {
        XmlFactoryHook hook = XmlFactoryHook.ThreadContext.getHook();
        if (hook != null) {
            return hook.parse(this, xmlText, type, options);
        }
        return Locale.parseToXmlObject(this, xmlText, type, options);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public XmlObject parse(XMLInputStream xis, SchemaType type, XmlOptions options) throws XMLStreamException, XmlException {
        XmlFactoryHook hook = XmlFactoryHook.ThreadContext.getHook();
        if (hook != null) {
            return hook.parse(this, xis, type, options);
        }
        return Locale.parseToXmlObject(this, xis, type, options);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public XmlObject parse(XMLStreamReader xsr, SchemaType type, XmlOptions options) throws XmlException {
        XmlFactoryHook hook = XmlFactoryHook.ThreadContext.getHook();
        if (hook != null) {
            return hook.parse(this, xsr, type, options);
        }
        return Locale.parseToXmlObject(this, xsr, type, options);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public XmlObject parse(File file, SchemaType type, XmlOptions options) throws XmlException, IOException {
        if (options == null) {
            options = new XmlOptions();
            options.put(XmlOptions.DOCUMENT_SOURCE_NAME, file.toURI().normalize().toString());
        } else if (!options.hasOption(XmlOptions.DOCUMENT_SOURCE_NAME)) {
            options = new XmlOptions(options);
            options.put(XmlOptions.DOCUMENT_SOURCE_NAME, file.toURI().normalize().toString());
        }
        InputStream fis = new FileInputStream(file);
        try {
            XmlObject xmlObject = parse(fis, type, options);
            fis.close();
            return xmlObject;
        } catch (Throwable th) {
            fis.close();
            throw th;
        }
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public XmlObject parse(URL url, SchemaType type, XmlOptions options) throws XmlException, IOException {
        URLConnection conn;
        if (options == null) {
            options = new XmlOptions();
            options.put(XmlOptions.DOCUMENT_SOURCE_NAME, url.toString());
        } else if (!options.hasOption(XmlOptions.DOCUMENT_SOURCE_NAME)) {
            options = new XmlOptions(options);
            options.put(XmlOptions.DOCUMENT_SOURCE_NAME, url.toString());
        }
        InputStream stream = null;
        boolean redirected = false;
        int count = 0;
        do {
            try {
                conn = url.openConnection();
                conn.addRequestProperty("User-Agent", USER_AGENT);
                conn.addRequestProperty("Accept", "application/xml, text/xml, */*");
                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection httpcon = (HttpURLConnection) conn;
                    int code = httpcon.getResponseCode();
                    redirected = code == 301 || code == 302;
                    if (redirected && count > 5) {
                        redirected = false;
                    }
                    if (redirected) {
                        String newLocation = httpcon.getHeaderField("Location");
                        if (newLocation == null) {
                            redirected = false;
                        } else {
                            url = new URL(newLocation);
                            count++;
                        }
                    }
                }
            } catch (Throwable th) {
                if (stream != null) {
                    stream.close();
                }
                throw th;
            }
        } while (redirected);
        stream = conn.getInputStream();
        XmlObject xmlObject = parse(stream, type, options);
        if (stream != null) {
            stream.close();
        }
        return xmlObject;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public XmlObject parse(InputStream jiois, SchemaType type, XmlOptions options) throws XmlException, NoSuchAlgorithmException, IOException {
        XmlFactoryHook hook = XmlFactoryHook.ThreadContext.getHook();
        DigestInputStream digestStream = null;
        if (options != null && options.hasOption(XmlOptions.LOAD_MESSAGE_DIGEST)) {
            try {
                MessageDigest sha = MessageDigest.getInstance("SHA");
                digestStream = new DigestInputStream(jiois, sha);
                jiois = digestStream;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        if (hook != null) {
            return hook.parse(this, jiois, type, options);
        }
        XmlObject result = Locale.parseToXmlObject(this, jiois, type, options);
        if (digestStream != null) {
            result.documentProperties().setMessageDigest(digestStream.getMessageDigest().digest());
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public XmlObject parse(Reader jior, SchemaType type, XmlOptions options) throws XmlException, IOException {
        XmlFactoryHook hook = XmlFactoryHook.ThreadContext.getHook();
        if (hook != null) {
            return hook.parse(this, jior, type, options);
        }
        return Locale.parseToXmlObject(this, jior, type, options);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public XmlObject parse(Node node, SchemaType type, XmlOptions options) throws XmlException {
        XmlFactoryHook hook = XmlFactoryHook.ThreadContext.getHook();
        if (hook != null) {
            return hook.parse(this, node, type, options);
        }
        return Locale.parseToXmlObject(this, node, type, options);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public XmlSaxHandler newXmlSaxHandler(SchemaType type, XmlOptions options) {
        XmlFactoryHook hook = XmlFactoryHook.ThreadContext.getHook();
        if (hook != null) {
            return hook.newXmlSaxHandler(this, type, options);
        }
        return Locale.newSaxHandler(this, type, options);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public DOMImplementation newDomImplementation(XmlOptions options) {
        return Locale.newDomImplementation(this, options);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, SchemaType type, XmlOptions options) throws XMLStreamException, XmlException {
        return new ValidatingXMLInputStream(xis, this, type, options);
    }

    public String compilePath(String pathExpr) {
        return compilePath(pathExpr, null);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public String compilePath(String pathExpr, XmlOptions options) {
        return doCompilePath(pathExpr, options);
    }

    public String compileQuery(String queryExpr) {
        return compileQuery(queryExpr, null);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public String compileQuery(String queryExpr, XmlOptions options) {
        return doCompileQuery(queryExpr, options);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType typeForSignature(String signature) throws NumberFormatException {
        String uri;
        int end = signature.indexOf(64);
        if (end < 0) {
            uri = "";
            end = signature.length();
        } else {
            uri = signature.substring(end + 1);
        }
        List parts = new ArrayList();
        int i = 0;
        while (true) {
            int index = i;
            if (index >= end) {
                break;
            }
            int nextc = signature.indexOf(58, index);
            int nextd = signature.indexOf(124, index);
            int i2 = nextc < 0 ? nextd : (nextd >= 0 && nextc >= nextd) ? nextd : nextc;
            int next = i2;
            if (next < 0 || next > end) {
                next = end;
            }
            parts.add(signature.substring(index, next));
            i = next + 1;
        }
        SchemaType curType = null;
        for (int i3 = parts.size() - 1; i3 >= 0; i3--) {
            String part = (String) parts.get(i3);
            if (part.length() < 1) {
                throw new IllegalArgumentException();
            }
            int offset = (part.length() < 2 || part.charAt(1) != '=') ? 1 : 2;
            switch (part.charAt(0)) {
                case 'A':
                case 'Q':
                    if (curType != null) {
                        if (curType.isSimpleType()) {
                            return null;
                        }
                        SchemaType[] subTypes = curType.getAnonymousTypes();
                        String localName = part.substring(offset);
                        for (int j = 0; j < subTypes.length; j++) {
                            SchemaField field = subTypes[j].getContainerField();
                            if (field != null && field.isAttribute() && field.getName().getLocalPart().equals(localName)) {
                                curType = subTypes[j];
                                break;
                            }
                        }
                        return null;
                    }
                    SchemaGlobalAttribute attr = findAttribute(QNameHelper.forLNS(part.substring(offset), uri));
                    if (attr == null) {
                        return null;
                    }
                    curType = attr.getType();
                    break;
                case 'B':
                    if (curType == null) {
                        throw new IllegalArgumentException();
                    }
                    if (curType.getSimpleVariety() != 1) {
                        return null;
                    }
                    SchemaType[] subTypes2 = curType.getAnonymousTypes();
                    if (subTypes2.length != 1) {
                        return null;
                    }
                    curType = subTypes2[0];
                    break;
                case 'C':
                case 'R':
                    if (curType != null) {
                        throw new IllegalArgumentException();
                    }
                    curType = findAttributeType(QNameHelper.forLNS(part.substring(offset), uri));
                    if (curType == null) {
                        return null;
                    }
                    break;
                case 'D':
                    if (curType != null) {
                        throw new IllegalArgumentException();
                    }
                    curType = findDocumentType(QNameHelper.forLNS(part.substring(offset), uri));
                    if (curType == null) {
                        return null;
                    }
                    break;
                case 'E':
                case 'U':
                    if (curType != null) {
                        if (curType.getContentType() < 3) {
                            return null;
                        }
                        SchemaType[] subTypes3 = curType.getAnonymousTypes();
                        String localName2 = part.substring(offset);
                        for (int j2 = 0; j2 < subTypes3.length; j2++) {
                            SchemaField field2 = subTypes3[j2].getContainerField();
                            if (field2 != null && !field2.isAttribute() && field2.getName().getLocalPart().equals(localName2)) {
                                curType = subTypes3[j2];
                                break;
                            }
                        }
                        return null;
                    }
                    SchemaGlobalElement elt = findElement(QNameHelper.forLNS(part.substring(offset), uri));
                    if (elt == null) {
                        return null;
                    }
                    curType = elt.getType();
                    break;
                case 'F':
                case 'G':
                case 'H':
                case 'J':
                case 'K':
                case 'L':
                case 'N':
                case 'O':
                case 'P':
                case 'S':
                default:
                    throw new IllegalArgumentException();
                case 'I':
                    if (curType == null) {
                        throw new IllegalArgumentException();
                    }
                    if (curType.getSimpleVariety() != 3) {
                        return null;
                    }
                    SchemaType[] subTypes4 = curType.getAnonymousTypes();
                    if (subTypes4.length != 1) {
                        return null;
                    }
                    curType = subTypes4[0];
                    break;
                case 'M':
                    if (curType == null) {
                        throw new IllegalArgumentException();
                    }
                    try {
                        int index2 = Integer.parseInt(part.substring(offset));
                        if (curType.getSimpleVariety() != 2) {
                            return null;
                        }
                        SchemaType[] subTypes5 = curType.getAnonymousTypes();
                        if (subTypes5.length <= index2) {
                            return null;
                        }
                        curType = subTypes5[index2];
                        break;
                    } catch (Exception e) {
                        throw new IllegalArgumentException();
                    }
                case 'T':
                    if (curType != null) {
                        throw new IllegalArgumentException();
                    }
                    curType = findType(QNameHelper.forLNS(part.substring(offset), uri));
                    if (curType == null) {
                        return null;
                    }
                    break;
            }
        }
        return curType;
    }
}
