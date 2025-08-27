package org.apache.xmlbeans.impl.config;

import org.apache.xmlbeans.PrePostExtension;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JMethod;
import org.apache.xmlbeans.impl.jam.JamClassLoader;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/config/PrePostExtensionImpl.class */
public class PrePostExtensionImpl implements PrePostExtension {
    private static JClass[] PARAMTYPES_PREPOST;
    private static final String[] PARAMTYPES_STRING;
    private static final String SIGNATURE;
    private NameSet _xbeanSet;
    private JClass _delegateToClass;
    private String _delegateToClassName;
    private JMethod _preSet;
    private JMethod _postSet;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PrePostExtensionImpl.class.desiredAssertionStatus();
        PARAMTYPES_PREPOST = null;
        PARAMTYPES_STRING = new String[]{XmlErrorCodes.INT, "org.apache.xmlbeans.XmlObject", "javax.xml.namespace.QName", "boolean", XmlErrorCodes.INT};
        String sig = "(";
        for (int i = 0; i < PARAMTYPES_STRING.length; i++) {
            String t = PARAMTYPES_STRING[i];
            if (i != 0) {
                sig = sig + ", ";
            }
            sig = sig + t;
        }
        SIGNATURE = sig + ")";
    }

    static PrePostExtensionImpl newInstance(JamClassLoader jamLoader, NameSet xbeanSet, Extensionconfig.PrePostSet prePostXO) {
        if (prePostXO == null) {
            return null;
        }
        PrePostExtensionImpl result = new PrePostExtensionImpl();
        result._xbeanSet = xbeanSet;
        result._delegateToClassName = prePostXO.getStaticHandler();
        result._delegateToClass = InterfaceExtensionImpl.validateClass(jamLoader, result._delegateToClassName, prePostXO);
        if (result._delegateToClass == null) {
            BindingConfigImpl.warning("Handler class '" + prePostXO.getStaticHandler() + "' not found on classpath, skip validation.", prePostXO);
            return result;
        }
        if (!result.lookAfterPreAndPost(jamLoader, prePostXO)) {
            return null;
        }
        return result;
    }

    private boolean lookAfterPreAndPost(JamClassLoader jamLoader, XmlObject loc) {
        if (!$assertionsDisabled && this._delegateToClass == null) {
            throw new AssertionError("Delegate to class handler expected.");
        }
        boolean valid = true;
        initParamPrePost(jamLoader);
        this._preSet = InterfaceExtensionImpl.getMethod(this._delegateToClass, "preSet", PARAMTYPES_PREPOST);
        if (this._preSet == null) {
        }
        if (this._preSet != null && !this._preSet.getReturnType().equals(jamLoader.loadClass("boolean"))) {
            BindingConfigImpl.warning("Method '" + this._delegateToClass.getSimpleName() + ".preSet" + SIGNATURE + "' should return boolean to be considered for a preSet handler.", loc);
            this._preSet = null;
        }
        this._postSet = InterfaceExtensionImpl.getMethod(this._delegateToClass, "postSet", PARAMTYPES_PREPOST);
        if (this._postSet == null) {
        }
        if (this._preSet == null && this._postSet == null) {
            BindingConfigImpl.error("prePostSet handler specified '" + this._delegateToClass.getSimpleName() + "' but no preSet" + SIGNATURE + " or postSet" + SIGNATURE + " methods found.", loc);
            valid = false;
        }
        return valid;
    }

    private void initParamPrePost(JamClassLoader jamLoader) {
        if (PARAMTYPES_PREPOST == null) {
            PARAMTYPES_PREPOST = new JClass[PARAMTYPES_STRING.length];
            for (int i = 0; i < PARAMTYPES_PREPOST.length; i++) {
                PARAMTYPES_PREPOST[i] = jamLoader.loadClass(PARAMTYPES_STRING[i]);
                if (PARAMTYPES_PREPOST[i] == null) {
                    throw new IllegalStateException("JAM should have access to the following types " + SIGNATURE);
                }
            }
        }
    }

    public NameSet getNameSet() {
        return this._xbeanSet;
    }

    public boolean contains(String fullJavaName) {
        return this._xbeanSet.contains(fullJavaName);
    }

    @Override // org.apache.xmlbeans.PrePostExtension
    public boolean hasPreCall() {
        return this._preSet != null;
    }

    @Override // org.apache.xmlbeans.PrePostExtension
    public boolean hasPostCall() {
        return this._postSet != null;
    }

    @Override // org.apache.xmlbeans.PrePostExtension
    public String getStaticHandler() {
        return this._delegateToClassName;
    }

    public String getHandlerNameForJavaSource() {
        if (this._delegateToClass == null) {
            return null;
        }
        return InterfaceExtensionImpl.emitType(this._delegateToClass);
    }

    boolean hasNameSetIntersection(PrePostExtensionImpl ext) {
        return !NameSet.EMPTY.equals(this._xbeanSet.intersect(ext._xbeanSet));
    }
}
