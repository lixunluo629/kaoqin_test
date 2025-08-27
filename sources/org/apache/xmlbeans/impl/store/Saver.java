package org.apache.xmlbeans.impl.store;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import net.dongliu.apk.parser.struct.xml.XmlCData;
import org.apache.xmlbeans.SystemProperties;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlOptionCharEscapeMap;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.EncodingMap;
import org.apache.xmlbeans.impl.common.GenericXmlInputStream;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.Sax2Dom;
import org.apache.xmlbeans.impl.common.XmlEventBase;
import org.apache.xmlbeans.impl.common.XmlNameImpl;
import org.apache.xmlbeans.xml.stream.Attribute;
import org.apache.xmlbeans.xml.stream.AttributeIterator;
import org.apache.xmlbeans.xml.stream.ChangePrefixMapping;
import org.apache.xmlbeans.xml.stream.CharacterData;
import org.apache.xmlbeans.xml.stream.Comment;
import org.apache.xmlbeans.xml.stream.EndDocument;
import org.apache.xmlbeans.xml.stream.EndElement;
import org.apache.xmlbeans.xml.stream.EndPrefixMapping;
import org.apache.xmlbeans.xml.stream.Location;
import org.apache.xmlbeans.xml.stream.ProcessingInstruction;
import org.apache.xmlbeans.xml.stream.StartDocument;
import org.apache.xmlbeans.xml.stream.StartElement;
import org.apache.xmlbeans.xml.stream.StartPrefixMapping;
import org.apache.xmlbeans.xml.stream.XMLEvent;
import org.apache.xmlbeans.xml.stream.XMLName;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver.class */
abstract class Saver {
    static final int ROOT = 1;
    static final int ELEM = 2;
    static final int ATTR = 3;
    static final int COMMENT = 4;
    static final int PROCINST = 5;
    static final int TEXT = 0;
    private final Locale _locale;
    private final long _version;
    private SaveCur _cur;
    private List _ancestorNamespaces;
    private Map _suggestedPrefixes;
    protected XmlOptionCharEscapeMap _replaceChar;
    private boolean _useDefaultNamespace;
    private Map _preComputedNamespaces;
    private boolean _saveNamespacesFirst;
    private ArrayList _attrNames;
    private ArrayList _attrValues;
    private ArrayList _namespaceStack;
    private int _currentMapping;
    private HashMap _uriMap;
    private HashMap _prefixMap;
    private String _initialDefaultUri;
    static final String _newLine;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract boolean emitElement(SaveCur saveCur, ArrayList arrayList, ArrayList arrayList2);

    protected abstract void emitFinish(SaveCur saveCur);

    protected abstract void emitText(SaveCur saveCur);

    protected abstract void emitComment(SaveCur saveCur);

    protected abstract void emitProcinst(SaveCur saveCur);

    protected abstract void emitDocType(String str, String str2, String str3);

    protected abstract void emitStartDoc(SaveCur saveCur);

    protected abstract void emitEndDoc(SaveCur saveCur);

    static {
        $assertionsDisabled = !Saver.class.desiredAssertionStatus();
        _newLine = SystemProperties.getProperty("line.separator") == null ? ScriptUtils.FALLBACK_STATEMENT_SEPARATOR : SystemProperties.getProperty("line.separator");
    }

    protected void syntheticNamespace(String prefix, String uri, boolean considerDefault) {
    }

    Saver(Cur c, XmlOptions options) {
        if (!$assertionsDisabled && !c._locale.entered()) {
            throw new AssertionError();
        }
        XmlOptions options2 = XmlOptions.maskNull(options);
        this._cur = createSaveCur(c, options2);
        this._locale = c._locale;
        this._version = this._locale.version();
        this._namespaceStack = new ArrayList();
        this._uriMap = new HashMap();
        this._prefixMap = new HashMap();
        this._attrNames = new ArrayList();
        this._attrValues = new ArrayList();
        addMapping("xml", "http://www.w3.org/XML/1998/namespace");
        if (options2.hasOption(XmlOptions.SAVE_IMPLICIT_NAMESPACES)) {
            Map m = (Map) options2.get(XmlOptions.SAVE_IMPLICIT_NAMESPACES);
            for (String prefix : m.keySet()) {
                addMapping(prefix, (String) m.get(prefix));
            }
        }
        if (options2.hasOption(XmlOptions.SAVE_SUBSTITUTE_CHARACTERS)) {
            this._replaceChar = (XmlOptionCharEscapeMap) options2.get(XmlOptions.SAVE_SUBSTITUTE_CHARACTERS);
        }
        if (getNamespaceForPrefix("") == null) {
            this._initialDefaultUri = new String("");
            addMapping("", this._initialDefaultUri);
        }
        if (options2.hasOption(XmlOptions.SAVE_AGGRESSIVE_NAMESPACES) && !(this instanceof SynthNamespaceSaver)) {
            SynthNamespaceSaver saver = new SynthNamespaceSaver(c, options2);
            while (saver.process()) {
            }
            if (!saver._synthNamespaces.isEmpty()) {
                this._preComputedNamespaces = saver._synthNamespaces;
            }
        }
        this._useDefaultNamespace = options2.hasOption(XmlOptions.SAVE_USE_DEFAULT_NAMESPACE);
        this._saveNamespacesFirst = options2.hasOption(XmlOptions.SAVE_NAMESPACES_FIRST);
        if (options2.hasOption(XmlOptions.SAVE_SUGGESTED_PREFIXES)) {
            this._suggestedPrefixes = (Map) options2.get(XmlOptions.SAVE_SUGGESTED_PREFIXES);
        }
        this._ancestorNamespaces = this._cur.getAncestorNamespaces();
    }

    private static SaveCur createSaveCur(Cur c, XmlOptions options) {
        QName synthName = (QName) options.get(XmlOptions.SAVE_SYNTHETIC_DOCUMENT_ELEMENT);
        QName fragName = synthName;
        if (fragName == null) {
            fragName = options.hasOption(XmlOptions.SAVE_USE_OPEN_FRAGMENT) ? Locale._openuriFragment : Locale._xmlFragment;
        }
        boolean saveInner = options.hasOption(XmlOptions.SAVE_INNER) && !options.hasOption(XmlOptions.SAVE_OUTER);
        Cur start = c.tempCur();
        Cur end = c.tempCur();
        SaveCur cur = null;
        int k = c.kind();
        switch (k) {
            case 1:
                positionToInner(c, start, end);
                if (Locale.isFragment(start, end)) {
                    cur = new FragSaveCur(start, end, fragName);
                    break;
                } else if (synthName != null) {
                    cur = new FragSaveCur(start, end, synthName);
                    break;
                } else {
                    cur = new DocSaveCur(c);
                    break;
                }
            case 2:
                if (saveInner) {
                    positionToInner(c, start, end);
                    cur = new FragSaveCur(start, end, Locale.isFragment(start, end) ? fragName : synthName);
                    break;
                } else if (synthName != null) {
                    positionToInner(c, start, end);
                    cur = new FragSaveCur(start, end, synthName);
                    break;
                } else {
                    start.moveToCur(c);
                    end.moveToCur(c);
                    end.skip();
                    cur = new FragSaveCur(start, end, null);
                    break;
                }
        }
        if (cur == null) {
            if (!$assertionsDisabled && k >= 0 && k != 3 && k != 4 && k != 5 && k != 0) {
                throw new AssertionError();
            }
            if (k < 0) {
                start.moveToCur(c);
                end.moveToCur(c);
            } else if (k == 0) {
                start.moveToCur(c);
                end.moveToCur(c);
                end.next();
            } else if (saveInner) {
                start.moveToCur(c);
                start.next();
                end.moveToCur(c);
                end.toEnd();
            } else if (k == 3) {
                start.moveToCur(c);
                end.moveToCur(c);
            } else {
                if (!$assertionsDisabled && k != 4 && k != 5) {
                    throw new AssertionError();
                }
                start.moveToCur(c);
                end.moveToCur(c);
                end.skip();
            }
            cur = new FragSaveCur(start, end, fragName);
        }
        String filterPI = (String) options.get(XmlOptions.SAVE_FILTER_PROCINST);
        if (filterPI != null) {
            cur = new FilterPiSaveCur(cur, filterPI);
        }
        if (options.hasOption(XmlOptions.SAVE_PRETTY_PRINT)) {
            cur = new PrettySaveCur(cur, options);
        }
        start.release();
        end.release();
        return cur;
    }

    private static void positionToInner(Cur c, Cur start, Cur end) {
        if (!$assertionsDisabled && !c.isContainer()) {
            throw new AssertionError();
        }
        start.moveToCur(c);
        if (!start.toFirstAttr()) {
            start.next();
        }
        end.moveToCur(c);
        end.toEnd();
    }

    static boolean isBadChar(char ch2) {
        return (Character.isHighSurrogate(ch2) || Character.isLowSurrogate(ch2) || (ch2 >= ' ' && ch2 <= 55295) || ((ch2 >= 57344 && ch2 <= 65533) || ((ch2 >= 0 && ch2 <= 65535) || ch2 == '\t' || ch2 == '\n' || ch2 == '\r'))) ? false : true;
    }

    protected boolean saveNamespacesFirst() {
        return this._saveNamespacesFirst;
    }

    protected void enterLocale() {
        this._locale.enter();
    }

    protected void exitLocale() {
        this._locale.exit();
    }

    protected final boolean process() {
        if (!$assertionsDisabled && !this._locale.entered()) {
            throw new AssertionError();
        }
        if (this._cur == null) {
            return false;
        }
        if (this._version != this._locale.version()) {
            throw new ConcurrentModificationException("Document changed during save");
        }
        switch (this._cur.kind()) {
            case -2:
                processFinish();
                break;
            case -1:
                emitEndDoc(this._cur);
                this._cur.release();
                this._cur = null;
                return true;
            case 0:
                emitText(this._cur);
                break;
            case 1:
                processRoot();
                break;
            case 2:
                processElement();
                break;
            case 3:
            default:
                throw new RuntimeException("Unexpected kind");
            case 4:
                emitComment(this._cur);
                this._cur.toEnd();
                break;
            case 5:
                emitProcinst(this._cur);
                this._cur.toEnd();
                break;
        }
        this._cur.next();
        return true;
    }

    private final void processFinish() {
        emitFinish(this._cur);
        popMappings();
    }

    private final void processRoot() {
        if (!$assertionsDisabled && !this._cur.isRoot()) {
            throw new AssertionError();
        }
        XmlDocumentProperties props = this._cur.getDocProps();
        String systemId = null;
        String docTypeName = null;
        if (props != null) {
            systemId = props.getDoctypeSystemId();
            docTypeName = props.getDoctypeName();
        }
        if (systemId != null || docTypeName != null) {
            if (docTypeName == null) {
                this._cur.push();
                while (!this._cur.isElem() && this._cur.next()) {
                }
                if (this._cur.isElem()) {
                    docTypeName = this._cur.getName().getLocalPart();
                }
                this._cur.pop();
            }
            String publicId = props.getDoctypePublicId();
            if (docTypeName != null) {
                QName rootElemName = this._cur.getName();
                if (rootElemName == null) {
                    this._cur.push();
                    while (true) {
                        if (this._cur.isFinish()) {
                            break;
                        }
                        if (this._cur.isElem()) {
                            rootElemName = this._cur.getName();
                            break;
                        }
                        this._cur.next();
                    }
                    this._cur.pop();
                }
                if (rootElemName != null && docTypeName.equals(rootElemName.getLocalPart())) {
                    emitDocType(docTypeName, publicId, systemId);
                    return;
                }
            }
        }
        emitStartDoc(this._cur);
    }

    /* JADX WARN: Incorrect condition in loop: B:20:0x0078 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final void processElement() {
        /*
            Method dump skipped, instructions count: 389
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Saver.processElement():void");
    }

    boolean hasMappings() {
        int i = this._namespaceStack.size();
        return i > 0 && this._namespaceStack.get(i - 1) != null;
    }

    void iterateMappings() {
        this._currentMapping = this._namespaceStack.size();
        while (this._currentMapping > 0 && this._namespaceStack.get(this._currentMapping - 1) != null) {
            this._currentMapping -= 8;
        }
    }

    boolean hasMapping() {
        return this._currentMapping < this._namespaceStack.size();
    }

    void nextMapping() {
        this._currentMapping += 8;
    }

    String mappingPrefix() {
        if ($assertionsDisabled || hasMapping()) {
            return (String) this._namespaceStack.get(this._currentMapping + 6);
        }
        throw new AssertionError();
    }

    String mappingUri() {
        if ($assertionsDisabled || hasMapping()) {
            return (String) this._namespaceStack.get(this._currentMapping + 7);
        }
        throw new AssertionError();
    }

    /* JADX WARN: Incorrect condition in loop: B:10:0x0028 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final void pushMappings(org.apache.xmlbeans.impl.store.Saver.SaveCur r6, boolean r7) {
        /*
            r5 = this;
            boolean r0 = org.apache.xmlbeans.impl.store.Saver.$assertionsDisabled
            if (r0 != 0) goto L15
            r0 = r6
            boolean r0 = r0.isContainer()
            if (r0 != 0) goto L15
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r1 = r0
            r1.<init>()
            throw r0
        L15:
            r0 = r5
            java.util.ArrayList r0 = r0._namespaceStack
            r1 = 0
            boolean r0 = r0.add(r1)
            r0 = r6
            r0.push()
            r0 = r6
            boolean r0 = r0.toFirstAttr()
            r8 = r0
        L27:
            r0 = r8
            if (r0 == 0) goto L47
            r0 = r6
            boolean r0 = r0.isXmlns()
            if (r0 == 0) goto L3f
            r0 = r5
            r1 = r6
            java.lang.String r1 = r1.getXmlnsPrefix()
            r2 = r6
            java.lang.String r2 = r2.getXmlnsUri()
            r3 = r7
            r0.addNewFrameMapping(r1, r2, r3)
        L3f:
            r0 = r6
            boolean r0 = r0.toNextAttr()
            r8 = r0
            goto L27
        L47:
            r0 = r6
            r0.pop()
            r0 = r5
            java.util.List r0 = r0._ancestorNamespaces
            if (r0 == 0) goto L95
            r0 = 0
            r8 = r0
        L54:
            r0 = r8
            r1 = r5
            java.util.List r1 = r1._ancestorNamespaces
            int r1 = r1.size()
            if (r0 >= r1) goto L90
            r0 = r5
            java.util.List r0 = r0._ancestorNamespaces
            r1 = r8
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            r9 = r0
            r0 = r5
            java.util.List r0 = r0._ancestorNamespaces
            r1 = r8
            r2 = 1
            int r1 = r1 + r2
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            r10 = r0
            r0 = r5
            r1 = r9
            r2 = r10
            r3 = r7
            r0.addNewFrameMapping(r1, r2, r3)
            int r8 = r8 + 2
            goto L54
        L90:
            r0 = r5
            r1 = 0
            r0._ancestorNamespaces = r1
        L95:
            r0 = r7
            if (r0 == 0) goto Lc7
            r0 = r5
            java.util.HashMap r0 = r0._prefixMap
            java.lang.String r1 = ""
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            r8 = r0
            boolean r0 = org.apache.xmlbeans.impl.store.Saver.$assertionsDisabled
            if (r0 != 0) goto Lb8
            r0 = r8
            if (r0 != 0) goto Lb8
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r1 = r0
            r1.<init>()
            throw r0
        Lb8:
            r0 = r8
            int r0 = r0.length()
            if (r0 <= 0) goto Lc7
            r0 = r5
            java.lang.String r1 = ""
            java.lang.String r2 = ""
            r0.addMapping(r1, r2)
        Lc7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Saver.pushMappings(org.apache.xmlbeans.impl.store.Saver$SaveCur, boolean):void");
    }

    private final void addNewFrameMapping(String prefix, String uri, boolean ensureDefaultEmpty) {
        if (prefix.length() == 0 || uri.length() > 0) {
            if (!ensureDefaultEmpty || prefix.length() > 0 || uri.length() == 0) {
                iterateMappings();
                while (hasMapping()) {
                    if (!mappingPrefix().equals(prefix)) {
                        nextMapping();
                    } else {
                        return;
                    }
                }
                if (uri.equals(getNamespaceForPrefix(prefix))) {
                    return;
                }
                addMapping(prefix, uri);
            }
        }
    }

    private final void addMapping(String prefix, String uri) {
        if (!$assertionsDisabled && uri == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && prefix == null) {
            throw new AssertionError();
        }
        String renameUri = (String) this._prefixMap.get(prefix);
        String renamePrefix = null;
        if (renameUri != null) {
            if (renameUri.equals(uri)) {
                renameUri = null;
            } else {
                int i = this._namespaceStack.size();
                while (i > 0) {
                    if (this._namespaceStack.get(i - 1) == null) {
                        i--;
                    } else {
                        if (this._namespaceStack.get(i - 7).equals(renameUri)) {
                            renamePrefix = (String) this._namespaceStack.get(i - 8);
                            if (renamePrefix == null || !renamePrefix.equals(prefix)) {
                                break;
                            }
                        }
                        i -= 8;
                    }
                }
                if (!$assertionsDisabled && i <= 0) {
                    throw new AssertionError();
                }
            }
        }
        this._namespaceStack.add(this._uriMap.get(uri));
        this._namespaceStack.add(uri);
        if (renameUri != null) {
            this._namespaceStack.add(this._uriMap.get(renameUri));
            this._namespaceStack.add(renameUri);
        } else {
            this._namespaceStack.add(null);
            this._namespaceStack.add(null);
        }
        this._namespaceStack.add(prefix);
        this._namespaceStack.add(this._prefixMap.get(prefix));
        this._namespaceStack.add(prefix);
        this._namespaceStack.add(uri);
        this._uriMap.put(uri, prefix);
        this._prefixMap.put(prefix, uri);
        if (renameUri != null) {
            this._uriMap.put(renameUri, renamePrefix);
        }
    }

    private final void popMappings() {
        while (true) {
            int i = this._namespaceStack.size();
            if (i != 0) {
                if (this._namespaceStack.get(i - 1) == null) {
                    this._namespaceStack.remove(i - 1);
                    return;
                }
                Object oldUri = this._namespaceStack.get(i - 7);
                Object oldPrefix = this._namespaceStack.get(i - 8);
                if (oldPrefix == null) {
                    this._uriMap.remove(oldUri);
                } else {
                    this._uriMap.put(oldUri, oldPrefix);
                }
                Object oldPrefix2 = this._namespaceStack.get(i - 4);
                Object oldUri2 = this._namespaceStack.get(i - 3);
                if (oldUri2 == null) {
                    this._prefixMap.remove(oldPrefix2);
                } else {
                    this._prefixMap.put(oldPrefix2, oldUri2);
                }
                String uri = (String) this._namespaceStack.get(i - 5);
                if (uri != null) {
                    this._uriMap.put(uri, this._namespaceStack.get(i - 6));
                }
                this._namespaceStack.remove(i - 1);
                this._namespaceStack.remove(i - 2);
                this._namespaceStack.remove(i - 3);
                this._namespaceStack.remove(i - 4);
                this._namespaceStack.remove(i - 5);
                this._namespaceStack.remove(i - 6);
                this._namespaceStack.remove(i - 7);
                this._namespaceStack.remove(i - 8);
            } else {
                return;
            }
        }
    }

    private final void dumpMappings() {
        int i = this._namespaceStack.size();
        while (i > 0) {
            if (this._namespaceStack.get(i - 1) == null) {
                System.out.println("----------------");
                i--;
            } else {
                System.out.print("Mapping: ");
                System.out.print(this._namespaceStack.get(i - 2));
                System.out.print(" -> ");
                System.out.print(this._namespaceStack.get(i - 1));
                System.out.println();
                System.out.print("Prefix Undo: ");
                System.out.print(this._namespaceStack.get(i - 4));
                System.out.print(" -> ");
                System.out.print(this._namespaceStack.get(i - 3));
                System.out.println();
                System.out.print("Uri Rename: ");
                System.out.print(this._namespaceStack.get(i - 5));
                System.out.print(" -> ");
                System.out.print(this._namespaceStack.get(i - 6));
                System.out.println();
                System.out.print("UriUndo: ");
                System.out.print(this._namespaceStack.get(i - 7));
                System.out.print(" -> ");
                System.out.print(this._namespaceStack.get(i - 8));
                System.out.println();
                System.out.println();
                i -= 8;
            }
        }
    }

    private final String ensureMapping(String uri, String candidatePrefix, boolean considerCreatingDefault, boolean mustHavePrefix) {
        if (!$assertionsDisabled && uri == null) {
            throw new AssertionError();
        }
        if (uri.length() == 0) {
            return null;
        }
        String prefix = (String) this._uriMap.get(uri);
        if (prefix != null && (prefix.length() > 0 || !mustHavePrefix)) {
            return prefix;
        }
        if (candidatePrefix != null && candidatePrefix.length() == 0) {
            candidatePrefix = null;
        }
        if (candidatePrefix == null || !tryPrefix(candidatePrefix)) {
            if (this._suggestedPrefixes != null && this._suggestedPrefixes.containsKey(uri) && tryPrefix((String) this._suggestedPrefixes.get(uri))) {
                candidatePrefix = (String) this._suggestedPrefixes.get(uri);
            } else if (considerCreatingDefault && this._useDefaultNamespace && tryPrefix("")) {
                candidatePrefix = "";
            } else {
                String basePrefix = QNameHelper.suggestPrefix(uri);
                candidatePrefix = basePrefix;
                int i = 1;
                while (!tryPrefix(candidatePrefix)) {
                    candidatePrefix = basePrefix + i;
                    i++;
                }
            }
        }
        if (!$assertionsDisabled && candidatePrefix == null) {
            throw new AssertionError();
        }
        syntheticNamespace(candidatePrefix, uri, considerCreatingDefault);
        addMapping(candidatePrefix, uri);
        return candidatePrefix;
    }

    protected final String getUriMapping(String uri) {
        if ($assertionsDisabled || this._uriMap.get(uri) != null) {
            return (String) this._uriMap.get(uri);
        }
        throw new AssertionError();
    }

    String getNonDefaultUriMapping(String uri) {
        String prefix = (String) this._uriMap.get(uri);
        if (prefix != null && prefix.length() > 0) {
            return prefix;
        }
        for (String prefix2 : this._prefixMap.keySet()) {
            if (prefix2.length() > 0 && this._prefixMap.get(prefix2).equals(uri)) {
                return prefix2;
            }
        }
        if ($assertionsDisabled) {
            return null;
        }
        throw new AssertionError("Could not find non-default mapping");
    }

    private final boolean tryPrefix(String prefix) {
        if (prefix == null || Locale.beginsWithXml(prefix)) {
            return false;
        }
        String existingUri = (String) this._prefixMap.get(prefix);
        if (existingUri == null) {
            return true;
        }
        if (prefix.length() > 0 || existingUri != this._initialDefaultUri) {
            return false;
        }
        return true;
    }

    public final String getNamespaceForPrefix(String prefix) {
        if ($assertionsDisabled || !prefix.equals("xml") || this._prefixMap.get(prefix).equals("http://www.w3.org/XML/1998/namespace")) {
            return (String) this._prefixMap.get(prefix);
        }
        throw new AssertionError();
    }

    protected Map getPrefixMap() {
        return this._prefixMap;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$SynthNamespaceSaver.class */
    static final class SynthNamespaceSaver extends Saver {
        LinkedHashMap _synthNamespaces;

        SynthNamespaceSaver(Cur c, XmlOptions options) {
            super(c, options);
            this._synthNamespaces = new LinkedHashMap();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void syntheticNamespace(String prefix, String uri, boolean considerCreatingDefault) {
            this._synthNamespaces.put(uri, considerCreatingDefault ? "" : prefix);
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected boolean emitElement(SaveCur c, ArrayList attrNames, ArrayList attrValues) {
            return false;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitFinish(SaveCur c) {
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitText(SaveCur c) {
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitComment(SaveCur c) {
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitProcinst(SaveCur c) {
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitDocType(String docTypeName, String publicId, String systemId) {
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitStartDoc(SaveCur c) {
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitEndDoc(SaveCur c) {
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$TextSaver.class */
    static final class TextSaver extends Saver {
        private static final int _initialBufSize = 4096;
        private int _cdataLengthThreshold;
        private int _cdataEntityCountThreshold;
        private boolean _useCDataBookmarks;
        private boolean _isPrettyPrint;
        private int _lastEmitIn;
        private int _lastEmitCch;
        private int _free;
        private int _in;
        private int _out;
        private char[] _buf;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Saver.class.desiredAssertionStatus();
        }

        TextSaver(Cur c, XmlOptions options, String encoding) {
            super(c, options);
            this._cdataLengthThreshold = 32;
            this._cdataEntityCountThreshold = 5;
            this._useCDataBookmarks = false;
            this._isPrettyPrint = false;
            boolean noSaveDecl = options != null && options.hasOption(XmlOptions.SAVE_NO_XML_DECL);
            if (options != null && options.hasOption(XmlOptions.SAVE_CDATA_LENGTH_THRESHOLD)) {
                this._cdataLengthThreshold = ((Integer) options.get(XmlOptions.SAVE_CDATA_LENGTH_THRESHOLD)).intValue();
            }
            if (options != null && options.hasOption(XmlOptions.SAVE_CDATA_ENTITY_COUNT_THRESHOLD)) {
                this._cdataEntityCountThreshold = ((Integer) options.get(XmlOptions.SAVE_CDATA_ENTITY_COUNT_THRESHOLD)).intValue();
            }
            if (options != null && options.hasOption(XmlOptions.LOAD_SAVE_CDATA_BOOKMARKS)) {
                this._useCDataBookmarks = true;
            }
            if (options != null && options.hasOption(XmlOptions.SAVE_PRETTY_PRINT)) {
                this._isPrettyPrint = true;
            }
            this._out = 0;
            this._in = 0;
            this._free = 0;
            if (!$assertionsDisabled && this._buf != null && ((this._out >= this._in || this._free != this._buf.length - (this._in - this._out)) && ((this._out <= this._in || this._free != this._out - this._in) && ((this._out != this._in || this._free != this._buf.length) && (this._out != this._in || this._free != 0))))) {
                throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
            }
            if (encoding != null && !noSaveDecl) {
                XmlDocumentProperties props = Locale.getDocProps(c, false);
                String version = props == null ? null : props.getVersion();
                version = version == null ? "1.0" : version;
                emit("<?xml version=\"");
                emit(version);
                emit("\" encoding=\"" + encoding + "\"?>" + _newLine);
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected boolean emitElement(SaveCur c, ArrayList attrNames, ArrayList attrValues) {
            if (!$assertionsDisabled && !c.isElem()) {
                throw new AssertionError();
            }
            emit('<');
            emitName(c.getName(), false);
            if (saveNamespacesFirst()) {
                emitNamespacesHelper();
            }
            for (int i = 0; i < attrNames.size(); i++) {
                emitAttrHelper((QName) attrNames.get(i), (String) attrValues.get(i));
            }
            if (!saveNamespacesFirst()) {
                emitNamespacesHelper();
            }
            if (!c.hasChildren() && !c.hasText()) {
                emit('/', '>');
                return true;
            }
            emit('>');
            return false;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitFinish(SaveCur c) {
            emit('<', '/');
            emitName(c.getName(), false);
            emit('>');
        }

        protected void emitXmlns(String prefix, String uri) {
            if (!$assertionsDisabled && prefix == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && uri == null) {
                throw new AssertionError();
            }
            emit("xmlns");
            if (prefix.length() > 0) {
                emit(':');
                emit(prefix);
            }
            emit('=', '\"');
            emit(uri);
            entitizeAttrValue(false);
            emit('\"');
        }

        private void emitNamespacesHelper() {
            iterateMappings();
            while (hasMapping()) {
                emit(' ');
                emitXmlns(mappingPrefix(), mappingUri());
                nextMapping();
            }
        }

        private void emitAttrHelper(QName attrName, String attrValue) {
            emit(' ');
            emitName(attrName, true);
            emit('=', '\"');
            emit(attrValue);
            entitizeAttrValue(true);
            emit('\"');
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitText(SaveCur c) {
            if (!$assertionsDisabled && !c.isText()) {
                throw new AssertionError();
            }
            boolean forceCData = this._useCDataBookmarks && c.isTextCData();
            emit(c);
            entitizeContent(forceCData);
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitComment(SaveCur c) {
            if (!$assertionsDisabled && !c.isComment()) {
                throw new AssertionError();
            }
            emit("<!--");
            c.push();
            c.next();
            emit(c);
            c.pop();
            entitizeComment();
            emit("-->");
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitProcinst(SaveCur c) {
            if (!$assertionsDisabled && !c.isProcinst()) {
                throw new AssertionError();
            }
            emit("<?");
            emit(c.getName().getLocalPart());
            c.push();
            c.next();
            if (c.isText()) {
                emit(SymbolConstants.SPACE_SYMBOL);
                emit(c);
                entitizeProcinst();
            }
            c.pop();
            emit("?>");
        }

        private void emitLiteral(String literal) {
            if (literal.indexOf(SymbolConstants.QUOTES_SYMBOL) < 0) {
                emit('\"');
                emit(literal);
                emit('\"');
            } else {
                emit('\'');
                emit(literal);
                emit('\'');
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitDocType(String docTypeName, String publicId, String systemId) {
            if (!$assertionsDisabled && docTypeName == null) {
                throw new AssertionError();
            }
            emit("<!DOCTYPE ");
            emit(docTypeName);
            if (publicId == null && systemId != null) {
                emit(" SYSTEM ");
                emitLiteral(systemId);
            } else if (publicId != null) {
                emit(" PUBLIC ");
                emitLiteral(publicId);
                emit(SymbolConstants.SPACE_SYMBOL);
                emitLiteral(systemId);
            }
            emit(">");
            emit(_newLine);
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitStartDoc(SaveCur c) {
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitEndDoc(SaveCur c) {
        }

        private void emitName(QName name, boolean needsPrefix) {
            if (!$assertionsDisabled && name == null) {
                throw new AssertionError();
            }
            String uri = name.getNamespaceURI();
            if (!$assertionsDisabled && uri == null) {
                throw new AssertionError();
            }
            if (uri.length() != 0) {
                String prefix = name.getPrefix();
                String mappedUri = getNamespaceForPrefix(prefix);
                if (mappedUri == null || !mappedUri.equals(uri)) {
                    prefix = getUriMapping(uri);
                }
                if (needsPrefix && prefix.length() == 0) {
                    prefix = getNonDefaultUriMapping(uri);
                }
                if (prefix.length() > 0) {
                    emit(prefix);
                    emit(':');
                }
            }
            if (!$assertionsDisabled && name.getLocalPart().length() <= 0) {
                throw new AssertionError();
            }
            emit(name.getLocalPart());
        }

        private void emit(char ch2) {
            if (!$assertionsDisabled && this._buf != null && ((this._out >= this._in || this._free != this._buf.length - (this._in - this._out)) && ((this._out <= this._in || this._free != this._out - this._in) && ((this._out != this._in || this._free != this._buf.length) && (this._out != this._in || this._free != 0))))) {
                throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
            }
            preEmit(1);
            this._buf[this._in] = ch2;
            this._in = (this._in + 1) % this._buf.length;
            if ($assertionsDisabled || this._buf == null) {
                return;
            }
            if (this._out >= this._in || this._free != this._buf.length - (this._in - this._out)) {
                if (this._out <= this._in || this._free != this._out - this._in) {
                    if (this._out == this._in && this._free == this._buf.length) {
                        return;
                    }
                    if (this._out != this._in || this._free != 0) {
                        throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
                    }
                }
            }
        }

        private void emit(char ch1, char ch2) {
            if (preEmit(2)) {
                return;
            }
            this._buf[this._in] = ch1;
            this._in = (this._in + 1) % this._buf.length;
            this._buf[this._in] = ch2;
            this._in = (this._in + 1) % this._buf.length;
            if ($assertionsDisabled || this._buf == null) {
                return;
            }
            if (this._out >= this._in || this._free != this._buf.length - (this._in - this._out)) {
                if (this._out <= this._in || this._free != this._out - this._in) {
                    if (this._out == this._in && this._free == this._buf.length) {
                        return;
                    }
                    if (this._out != this._in || this._free != 0) {
                        throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
                    }
                }
            }
        }

        private void emit(String s) {
            int chunk;
            if (!$assertionsDisabled && this._buf != null && ((this._out >= this._in || this._free != this._buf.length - (this._in - this._out)) && ((this._out <= this._in || this._free != this._out - this._in) && ((this._out != this._in || this._free != this._buf.length) && (this._out != this._in || this._free != 0))))) {
                throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
            }
            int cch = s == null ? 0 : s.length();
            if (preEmit(cch)) {
                return;
            }
            if (this._in <= this._out || cch < (chunk = this._buf.length - this._in)) {
                s.getChars(0, cch, this._buf, this._in);
                this._in += cch;
            } else {
                s.getChars(0, chunk, this._buf, this._in);
                s.getChars(chunk, cch, this._buf, 0);
                this._in = (this._in + cch) % this._buf.length;
            }
            if ($assertionsDisabled || this._buf == null) {
                return;
            }
            if (this._out >= this._in || this._free != this._buf.length - (this._in - this._out)) {
                if (this._out <= this._in || this._free != this._out - this._in) {
                    if (this._out == this._in && this._free == this._buf.length) {
                        return;
                    }
                    if (this._out != this._in || this._free != 0) {
                        throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
                    }
                }
            }
        }

        private void emit(SaveCur c) {
            int chunk;
            if (c.isText()) {
                Object src = c.getChars();
                int cch = c._cchSrc;
                if (preEmit(cch)) {
                    return;
                }
                if (this._in <= this._out || cch < (chunk = this._buf.length - this._in)) {
                    CharUtil.getChars(this._buf, this._in, src, c._offSrc, cch);
                    this._in += cch;
                    return;
                } else {
                    CharUtil.getChars(this._buf, this._in, src, c._offSrc, chunk);
                    CharUtil.getChars(this._buf, 0, src, c._offSrc + chunk, cch - chunk);
                    this._in = (this._in + cch) % this._buf.length;
                    return;
                }
            }
            preEmit(0);
        }

        private boolean preEmit(int cch) {
            if (!$assertionsDisabled && cch < 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._buf != null && ((this._out >= this._in || this._free != this._buf.length - (this._in - this._out)) && ((this._out <= this._in || this._free != this._out - this._in) && ((this._out != this._in || this._free != this._buf.length) && (this._out != this._in || this._free != 0))))) {
                throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
            }
            this._lastEmitCch = cch;
            if (cch == 0) {
                return true;
            }
            if (this._free <= cch) {
                resize(cch, -1);
            }
            if (!$assertionsDisabled && cch > this._free) {
                throw new AssertionError();
            }
            int used = getAvailable();
            if (used == 0) {
                if (!$assertionsDisabled && this._in != this._out) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && this._free != this._buf.length) {
                    throw new AssertionError();
                }
                this._out = 0;
                this._in = 0;
            }
            this._lastEmitIn = this._in;
            this._free -= cch;
            if (!$assertionsDisabled && this._free < 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._buf != null) {
                if (this._free != (this._in >= this._out ? this._buf.length - (this._in - this._out) : this._out - this._in) - cch) {
                    throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
                }
            }
            if ($assertionsDisabled || this._buf == null) {
                return false;
            }
            if (this._out < this._in && this._free == (this._buf.length - (this._in - this._out)) - cch) {
                return false;
            }
            if (this._out > this._in && this._free == (this._out - this._in) - cch) {
                return false;
            }
            if (this._out == this._in && this._free == this._buf.length - cch) {
                return false;
            }
            if (this._out == this._in && this._free == 0) {
                return false;
            }
            throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
        }

        private void entitizeContent(boolean forceCData) {
            if (!$assertionsDisabled && this._free < 0) {
                throw new AssertionError();
            }
            if (this._lastEmitCch == 0) {
                return;
            }
            int i = this._lastEmitIn;
            int n = this._buf.length;
            boolean hasCharToBeReplaced = false;
            int count = 0;
            char prevChar = 0;
            char prevPrevChar = 0;
            for (int cch = this._lastEmitCch; cch > 0; cch--) {
                char ch2 = this._buf[i];
                if (ch2 == '<' || ch2 == '&') {
                    count++;
                } else if ((prevPrevChar == ']' && prevChar == ']' && ch2 == '>') || isBadChar(ch2) || isEscapedChar(ch2) || (!this._isPrettyPrint && ch2 == '\r')) {
                    hasCharToBeReplaced = true;
                }
                i++;
                if (i == n) {
                    i = 0;
                }
                prevPrevChar = prevChar;
                prevChar = ch2;
            }
            if (!forceCData && count == 0 && !hasCharToBeReplaced && count < this._cdataEntityCountThreshold) {
                return;
            }
            int i2 = this._lastEmitIn;
            if (forceCData || (this._lastEmitCch > this._cdataLengthThreshold && count > this._cdataEntityCountThreshold)) {
                boolean lastWasBracket = this._buf[i2] == ']';
                int i3 = replace(i2, XmlCData.CDATA_START + this._buf[i2]);
                boolean secondToLastWasBracket = lastWasBracket;
                boolean lastWasBracket2 = this._buf[i3] == ']';
                int i4 = i3 + 1;
                if (i4 == this._buf.length) {
                    i4 = 0;
                }
                for (int cch2 = this._lastEmitCch - 2; cch2 > 0; cch2--) {
                    char ch3 = this._buf[i4];
                    if (ch3 == '>' && secondToLastWasBracket && lastWasBracket2) {
                        i4 = replace(i4, "]]>><![CDATA[");
                    } else if (isBadChar(ch3)) {
                        i4 = replace(i4, "?");
                    } else {
                        i4++;
                    }
                    secondToLastWasBracket = lastWasBracket2;
                    lastWasBracket2 = ch3 == ']';
                    if (i4 == this._buf.length) {
                        i4 = 0;
                    }
                }
                emit(XmlCData.CDATA_END);
                return;
            }
            char ch4 = 0;
            char ch_1 = 0;
            for (int cch3 = this._lastEmitCch; cch3 > 0; cch3--) {
                char ch_2 = ch_1;
                ch_1 = ch4;
                ch4 = this._buf[i2];
                if (ch4 == '<') {
                    i2 = replace(i2, "&lt;");
                } else if (ch4 == '&') {
                    i2 = replace(i2, "&amp;");
                } else if (ch4 == '>' && ch_1 == ']' && ch_2 == ']') {
                    i2 = replace(i2, "&gt;");
                } else if (isBadChar(ch4)) {
                    i2 = replace(i2, "?");
                } else if (!this._isPrettyPrint && ch4 == '\r') {
                    i2 = replace(i2, "&#13;");
                } else if (isEscapedChar(ch4)) {
                    i2 = replace(i2, this._replaceChar.getEscapedString(ch4));
                } else {
                    i2++;
                }
                if (i2 == this._buf.length) {
                    i2 = 0;
                }
            }
        }

        private void entitizeAttrValue(boolean replaceEscapedChar) {
            if (this._lastEmitCch == 0) {
                return;
            }
            int i = this._lastEmitIn;
            for (int cch = this._lastEmitCch; cch > 0; cch--) {
                char ch2 = this._buf[i];
                if (ch2 == '<') {
                    i = replace(i, "&lt;");
                } else if (ch2 == '&') {
                    i = replace(i, "&amp;");
                } else if (ch2 == '\"') {
                    i = replace(i, "&quot;");
                } else if (isEscapedChar(ch2)) {
                    if (replaceEscapedChar) {
                        i = replace(i, this._replaceChar.getEscapedString(ch2));
                    }
                } else {
                    i++;
                }
                if (i == this._buf.length) {
                    i = 0;
                }
            }
        }

        private void entitizeComment() {
            if (this._lastEmitCch == 0) {
                return;
            }
            int i = this._lastEmitIn;
            boolean lastWasDash = false;
            for (int cch = this._lastEmitCch; cch > 0; cch--) {
                char ch2 = this._buf[i];
                if (isBadChar(ch2)) {
                    i = replace(i, "?");
                } else if (ch2 == '-') {
                    if (lastWasDash) {
                        i = replace(i, SymbolConstants.SPACE_SYMBOL);
                        lastWasDash = false;
                    } else {
                        lastWasDash = true;
                        i++;
                    }
                } else {
                    lastWasDash = false;
                    i++;
                }
                if (i == this._buf.length) {
                    i = 0;
                }
            }
            int offset = ((this._lastEmitIn + this._lastEmitCch) - 1) % this._buf.length;
            if (this._buf[offset] == '-') {
                replace(offset, SymbolConstants.SPACE_SYMBOL);
            }
        }

        private void entitizeProcinst() {
            if (this._lastEmitCch == 0) {
                return;
            }
            int i = this._lastEmitIn;
            boolean lastWasQuestion = false;
            for (int cch = this._lastEmitCch; cch > 0; cch--) {
                char ch2 = this._buf[i];
                if (isBadChar(ch2)) {
                    i = replace(i, "?");
                }
                if (ch2 == '>') {
                    if (lastWasQuestion) {
                        i = replace(i, SymbolConstants.SPACE_SYMBOL);
                    } else {
                        i++;
                    }
                    lastWasQuestion = false;
                } else {
                    lastWasQuestion = ch2 == '?';
                    i++;
                }
                if (i == this._buf.length) {
                    i = 0;
                }
            }
        }

        private boolean isEscapedChar(char ch2) {
            return null != this._replaceChar && this._replaceChar.containsChar(ch2);
        }

        private int replace(int i, String replacement) {
            if (!$assertionsDisabled && replacement.length() <= 0) {
                throw new AssertionError();
            }
            int dCch = replacement.length() - 1;
            if (dCch == 0) {
                this._buf[i] = replacement.charAt(0);
                return i + 1;
            }
            if (!$assertionsDisabled && this._free < 0) {
                throw new AssertionError();
            }
            if (dCch > this._free) {
                i = resize(dCch, i);
            }
            if (!$assertionsDisabled && this._free < 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._free < dCch) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && getAvailable() <= 0) {
                throw new AssertionError();
            }
            int charsToCopy = dCch + 1;
            if (this._out > this._in && i >= this._out) {
                System.arraycopy(this._buf, this._out, this._buf, this._out - dCch, i - this._out);
                this._out -= dCch;
                i -= dCch;
            } else {
                if (!$assertionsDisabled && i >= this._in) {
                    throw new AssertionError();
                }
                int availableEndChunk = this._buf.length - this._in;
                if (dCch <= availableEndChunk) {
                    System.arraycopy(this._buf, i, this._buf, i + dCch, this._in - i);
                    this._in = (this._in + dCch) % this._buf.length;
                } else if (dCch <= ((availableEndChunk + this._in) - i) - 1) {
                    int numToCopyToStart = dCch - availableEndChunk;
                    System.arraycopy(this._buf, this._in - numToCopyToStart, this._buf, 0, numToCopyToStart);
                    System.arraycopy(this._buf, i + 1, this._buf, i + 1 + dCch, ((this._in - i) - 1) - numToCopyToStart);
                    this._in = numToCopyToStart;
                } else {
                    int numToCopyToStart2 = (this._in - i) - 1;
                    charsToCopy = (availableEndChunk + this._in) - i;
                    System.arraycopy(this._buf, this._in - numToCopyToStart2, this._buf, (dCch - charsToCopy) + 1, numToCopyToStart2);
                    replacement.getChars(charsToCopy, dCch + 1, this._buf, 0);
                    this._in = ((numToCopyToStart2 + dCch) - charsToCopy) + 1;
                }
            }
            replacement.getChars(0, charsToCopy, this._buf, i);
            this._free -= dCch;
            if (!$assertionsDisabled && this._free < 0) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || this._buf == null || ((this._out < this._in && this._free == this._buf.length - (this._in - this._out)) || ((this._out > this._in && this._free == this._out - this._in) || ((this._out == this._in && this._free == this._buf.length) || (this._out == this._in && this._free == 0))))) {
                return ((i + dCch) + 1) % this._buf.length;
            }
            throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
        }

        private int ensure(int cch) {
            int available;
            if (cch <= 0) {
                cch = 1;
            }
            int available2 = getAvailable();
            while (true) {
                available = available2;
                if (available >= cch || !process()) {
                    break;
                }
                available2 = getAvailable();
            }
            if ($assertionsDisabled || available == getAvailable()) {
                return available;
            }
            throw new AssertionError();
        }

        int getAvailable() {
            if (this._buf == null) {
                return 0;
            }
            return this._buf.length - this._free;
        }

        private int resize(int cch, int i) {
            if (!$assertionsDisabled && this._free < 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && cch <= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && cch < this._free) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._buf != null && ((this._out >= this._in || this._free != this._buf.length - (this._in - this._out)) && ((this._out <= this._in || this._free != this._out - this._in) && ((this._out != this._in || this._free != this._buf.length) && (this._out != this._in || this._free != 0))))) {
                throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
            }
            int newLen = this._buf == null ? 4096 : this._buf.length * 2;
            int used = getAvailable();
            while (newLen - used < cch) {
                newLen *= 2;
            }
            char[] newBuf = new char[newLen];
            if (used > 0) {
                if (this._in > this._out) {
                    if (!$assertionsDisabled && i != -1 && (i < this._out || i >= this._in)) {
                        throw new AssertionError();
                    }
                    System.arraycopy(this._buf, this._out, newBuf, 0, used);
                    i -= this._out;
                } else {
                    if (!$assertionsDisabled && i != -1 && i < this._out && i >= this._in) {
                        throw new AssertionError();
                    }
                    System.arraycopy(this._buf, this._out, newBuf, 0, used - this._in);
                    System.arraycopy(this._buf, 0, newBuf, used - this._in, this._in);
                    i = i >= this._out ? i - this._out : i + this._out;
                }
                this._out = 0;
                this._in = used;
                this._free += newBuf.length - this._buf.length;
            } else {
                this._free = newBuf.length;
                if (!$assertionsDisabled && (this._in != 0 || this._out != 0)) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && i != -1) {
                    throw new AssertionError();
                }
            }
            this._buf = newBuf;
            if (!$assertionsDisabled && this._free < 0) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || this._buf == null || ((this._out < this._in && this._free == this._buf.length - (this._in - this._out)) || ((this._out > this._in && this._free == this._out - this._in) || ((this._out == this._in && this._free == this._buf.length) || (this._out == this._in && this._free == 0))))) {
                return i;
            }
            throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
        }

        public int read() {
            if (ensure(1) == 0) {
                return -1;
            }
            if (!$assertionsDisabled && getAvailable() <= 0) {
                throw new AssertionError();
            }
            char c = this._buf[this._out];
            this._out = (this._out + 1) % this._buf.length;
            this._free++;
            if ($assertionsDisabled || this._buf == null || ((this._out < this._in && this._free == this._buf.length - (this._in - this._out)) || ((this._out > this._in && this._free == this._out - this._in) || ((this._out == this._in && this._free == this._buf.length) || (this._out == this._in && this._free == 0))))) {
                return c;
            }
            throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
        }

        public int read(char[] cbuf, int off, int len) {
            int chunk;
            int n = ensure(len);
            if (n == 0) {
                return -1;
            }
            if (cbuf == null || len <= 0) {
                return 0;
            }
            if (n < len) {
                len = n;
            }
            if (this._out < this._in || (chunk = this._buf.length - this._out) >= len) {
                System.arraycopy(this._buf, this._out, cbuf, off, len);
            } else {
                System.arraycopy(this._buf, this._out, cbuf, off, chunk);
                System.arraycopy(this._buf, 0, cbuf, off + chunk, len - chunk);
            }
            this._out = (this._out + len) % this._buf.length;
            this._free += len;
            if (!$assertionsDisabled && this._buf != null && ((this._out >= this._in || this._free != this._buf.length - (this._in - this._out)) && ((this._out <= this._in || this._free != this._out - this._in) && ((this._out != this._in || this._free != this._buf.length) && (this._out != this._in || this._free != 0))))) {
                throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
            }
            if ($assertionsDisabled || this._free >= 0) {
                return len;
            }
            throw new AssertionError();
        }

        public int write(Writer writer, int cchMin) throws IOException {
            while (getAvailable() < cchMin && process()) {
            }
            int charsAvailable = getAvailable();
            if (charsAvailable > 0) {
                if (!$assertionsDisabled && this._out != 0) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && this._in < this._out) {
                    throw new AssertionError("_in:" + this._in + " < _out:" + this._out);
                }
                if (!$assertionsDisabled && this._free != this._buf.length - this._in) {
                    throw new AssertionError();
                }
                try {
                    writer.write(this._buf, 0, charsAvailable);
                    writer.flush();
                    this._free += charsAvailable;
                    if (!$assertionsDisabled && this._free < 0) {
                        throw new AssertionError();
                    }
                    this._in = 0;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if ($assertionsDisabled || this._buf == null || ((this._out < this._in && this._free == this._buf.length - (this._in - this._out)) || ((this._out > this._in && this._free == this._out - this._in) || ((this._out == this._in && this._free == this._buf.length) || (this._out == this._in && this._free == 0))))) {
                return charsAvailable;
            }
            throw new AssertionError("_buf.length:" + this._buf.length + " _in:" + this._in + " _out:" + this._out + " _free:" + this._free);
        }

        public String saveToString() {
            while (process()) {
            }
            if (!$assertionsDisabled && this._out != 0) {
                throw new AssertionError();
            }
            int available = getAvailable();
            return available == 0 ? "" : new String(this._buf, this._out, available);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$OptimizedForSpeedSaver.class */
    static final class OptimizedForSpeedSaver extends Saver {
        Writer _w;
        private char[] _buf;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Saver.class.desiredAssertionStatus();
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$OptimizedForSpeedSaver$SaverIOException.class */
        private static class SaverIOException extends RuntimeException {
            SaverIOException(IOException e) {
                super(e);
            }
        }

        OptimizedForSpeedSaver(Cur cur, Writer writer) {
            super(cur, XmlOptions.maskNull(null));
            this._buf = new char[1024];
            this._w = writer;
        }

        static void save(Cur cur, Writer writer) throws IOException {
            try {
                Saver saver = new OptimizedForSpeedSaver(cur, writer);
                do {
                } while (saver.process());
            } catch (SaverIOException e) {
                throw ((IOException) e.getCause());
            }
        }

        private void emit(String s) throws IOException {
            try {
                this._w.write(s);
            } catch (IOException e) {
                throw new SaverIOException(e);
            }
        }

        private void emit(char c) throws IOException {
            try {
                this._buf[0] = c;
                this._w.write(this._buf, 0, 1);
            } catch (IOException e) {
                throw new SaverIOException(e);
            }
        }

        private void emit(char c1, char c2) throws IOException {
            try {
                this._buf[0] = c1;
                this._buf[1] = c2;
                this._w.write(this._buf, 0, 2);
            } catch (IOException e) {
                throw new SaverIOException(e);
            }
        }

        private void emit(char[] buf, int start, int len) throws IOException {
            try {
                this._w.write(buf, start, len);
            } catch (IOException e) {
                throw new SaverIOException(e);
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected boolean emitElement(SaveCur c, ArrayList attrNames, ArrayList attrValues) throws IOException {
            if (!$assertionsDisabled && !c.isElem()) {
                throw new AssertionError();
            }
            emit('<');
            emitName(c.getName(), false);
            for (int i = 0; i < attrNames.size(); i++) {
                emitAttrHelper((QName) attrNames.get(i), (String) attrValues.get(i));
            }
            if (!saveNamespacesFirst()) {
                emitNamespacesHelper();
            }
            if (!c.hasChildren() && !c.hasText()) {
                emit('/', '>');
                return true;
            }
            emit('>');
            return false;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitFinish(SaveCur c) throws IOException {
            emit('<', '/');
            emitName(c.getName(), false);
            emit('>');
        }

        protected void emitXmlns(String prefix, String uri) throws IOException {
            if (!$assertionsDisabled && prefix == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && uri == null) {
                throw new AssertionError();
            }
            emit("xmlns");
            if (prefix.length() > 0) {
                emit(':');
                emit(prefix);
            }
            emit('=', '\"');
            emitAttrValue(uri);
            emit('\"');
        }

        private void emitNamespacesHelper() throws IOException {
            iterateMappings();
            while (hasMapping()) {
                emit(' ');
                emitXmlns(mappingPrefix(), mappingUri());
                nextMapping();
            }
        }

        private void emitAttrHelper(QName attrName, String attrValue) throws IOException {
            emit(' ');
            emitName(attrName, true);
            emit('=', '\"');
            emitAttrValue(attrValue);
            emit('\"');
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitComment(SaveCur c) throws IOException {
            if (!$assertionsDisabled && !c.isComment()) {
                throw new AssertionError();
            }
            emit("<!--");
            c.push();
            c.next();
            emitCommentText(c);
            c.pop();
            emit("-->");
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitProcinst(SaveCur c) throws IOException {
            if (!$assertionsDisabled && !c.isProcinst()) {
                throw new AssertionError();
            }
            emit("<?");
            emit(c.getName().getLocalPart());
            c.push();
            c.next();
            if (c.isText()) {
                emit(' ');
                emitPiText(c);
            }
            c.pop();
            emit("?>");
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitDocType(String docTypeName, String publicId, String systemId) throws IOException {
            if (!$assertionsDisabled && docTypeName == null) {
                throw new AssertionError();
            }
            emit("<!DOCTYPE ");
            emit(docTypeName);
            if (publicId == null && systemId != null) {
                emit(" SYSTEM ");
                emitLiteral(systemId);
            } else if (publicId != null) {
                emit(" PUBLIC ");
                emitLiteral(publicId);
                emit(' ');
                emitLiteral(systemId);
            }
            emit('>');
            emit(_newLine);
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitStartDoc(SaveCur c) {
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitEndDoc(SaveCur c) {
        }

        private void emitName(QName name, boolean needsPrefix) throws IOException {
            if (!$assertionsDisabled && name == null) {
                throw new AssertionError();
            }
            String uri = name.getNamespaceURI();
            if (!$assertionsDisabled && uri == null) {
                throw new AssertionError();
            }
            if (uri.length() != 0) {
                String prefix = name.getPrefix();
                String mappedUri = getNamespaceForPrefix(prefix);
                if (mappedUri == null || !mappedUri.equals(uri)) {
                    prefix = getUriMapping(uri);
                }
                if (needsPrefix && prefix.length() == 0) {
                    prefix = getNonDefaultUriMapping(uri);
                }
                if (prefix.length() > 0) {
                    emit(prefix);
                    emit(':');
                }
            }
            if (!$assertionsDisabled && name.getLocalPart().length() <= 0) {
                throw new AssertionError();
            }
            emit(name.getLocalPart());
        }

        private void emitAttrValue(CharSequence attVal) throws IOException {
            int len = attVal.length();
            for (int i = 0; i < len; i++) {
                char ch2 = attVal.charAt(i);
                if (ch2 == '<') {
                    emit("&lt;");
                } else if (ch2 == '&') {
                    emit("&amp;");
                } else if (ch2 == '\"') {
                    emit("&quot;");
                } else {
                    emit(ch2);
                }
            }
        }

        private void emitLiteral(String literal) throws IOException {
            if (literal.indexOf(SymbolConstants.QUOTES_SYMBOL) < 0) {
                emit('\"');
                emit(literal);
                emit('\"');
            } else {
                emit('\'');
                emit(literal);
                emit('\'');
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitText(SaveCur c) throws IOException {
            if (!$assertionsDisabled && !c.isText()) {
                throw new AssertionError();
            }
            Object src = c.getChars();
            int cch = c._cchSrc;
            int off = c._offSrc;
            int index = 0;
            while (index < cch) {
                int indexLimit = index + 512 > cch ? cch : index + 512;
                CharUtil.getChars(this._buf, 0, src, off + index, indexLimit - index);
                entitizeAndWriteText(indexLimit - index);
                index = indexLimit;
            }
        }

        protected void emitPiText(SaveCur c) throws IOException {
            if (!$assertionsDisabled && !c.isText()) {
                throw new AssertionError();
            }
            Object src = c.getChars();
            int cch = c._cchSrc;
            int off = c._offSrc;
            int index = 0;
            while (index < cch) {
                int indexLimit = index + 512 > cch ? cch : 512;
                CharUtil.getChars(this._buf, 0, src, off + index, indexLimit);
                entitizeAndWritePIText(indexLimit - index);
                index = indexLimit;
            }
        }

        protected void emitCommentText(SaveCur c) throws IOException {
            if (!$assertionsDisabled && !c.isText()) {
                throw new AssertionError();
            }
            Object src = c.getChars();
            int cch = c._cchSrc;
            int off = c._offSrc;
            int index = 0;
            while (index < cch) {
                int indexLimit = index + 512 > cch ? cch : 512;
                CharUtil.getChars(this._buf, 0, src, off + index, indexLimit);
                entitizeAndWriteCommentText(indexLimit - index);
                index = indexLimit;
            }
        }

        private void entitizeAndWriteText(int bufLimit) throws IOException {
            int index = 0;
            for (int i = 0; i < bufLimit; i++) {
                char c = this._buf[i];
                switch (c) {
                    case '&':
                        emit(this._buf, index, i - index);
                        emit("&amp;");
                        index = i + 1;
                        break;
                    case '<':
                        emit(this._buf, index, i - index);
                        emit("&lt;");
                        index = i + 1;
                        break;
                }
            }
            emit(this._buf, index, bufLimit - index);
        }

        private void entitizeAndWriteCommentText(int bufLimit) throws IOException {
            boolean lastWasDash = false;
            int i = 0;
            while (i < bufLimit) {
                char ch2 = this._buf[i];
                if (isBadChar(ch2)) {
                    this._buf[i] = '?';
                } else if (ch2 == '-') {
                    if (lastWasDash) {
                        this._buf[i] = ' ';
                        lastWasDash = false;
                    } else {
                        lastWasDash = true;
                    }
                } else {
                    lastWasDash = false;
                }
                if (i == this._buf.length) {
                    i = 0;
                }
                i++;
            }
            if (this._buf[bufLimit - 1] == '-') {
                this._buf[bufLimit - 1] = ' ';
            }
            emit(this._buf, 0, bufLimit);
        }

        private void entitizeAndWritePIText(int bufLimit) throws IOException {
            boolean z;
            boolean lastWasQuestion = false;
            for (int i = 0; i < bufLimit; i++) {
                char ch2 = this._buf[i];
                if (isBadChar(ch2)) {
                    this._buf[i] = '?';
                    ch2 = '?';
                }
                if (ch2 == '>') {
                    if (lastWasQuestion) {
                        this._buf[i] = ' ';
                    }
                    z = false;
                } else {
                    z = ch2 == '?';
                }
                lastWasQuestion = z;
            }
            emit(this._buf, 0, bufLimit);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$TextReader.class */
    static final class TextReader extends Reader {
        private Locale _locale;
        private TextSaver _textSaver;
        private boolean _closed = false;

        TextReader(Cur c, XmlOptions options) {
            this._textSaver = new TextSaver(c, options, null);
            this._locale = c._locale;
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this._closed = true;
        }

        @Override // java.io.Reader
        public boolean ready() throws IOException {
            return !this._closed;
        }

        @Override // java.io.Reader
        public int read() throws IOException {
            int i;
            checkClosed();
            if (this._locale.noSync()) {
                this._locale.enter();
                try {
                    int i2 = this._textSaver.read();
                    this._locale.exit();
                    return i2;
                } finally {
                }
            }
            synchronized (this._locale) {
                this._locale.enter();
                try {
                    i = this._textSaver.read();
                    this._locale.exit();
                } finally {
                }
            }
            return i;
        }

        @Override // java.io.Reader
        public int read(char[] cbuf) throws IOException {
            int i;
            checkClosed();
            if (this._locale.noSync()) {
                this._locale.enter();
                try {
                    int i2 = this._textSaver.read(cbuf, 0, cbuf == null ? 0 : cbuf.length);
                    this._locale.exit();
                    return i2;
                } finally {
                }
            }
            synchronized (this._locale) {
                this._locale.enter();
                try {
                    i = this._textSaver.read(cbuf, 0, cbuf == null ? 0 : cbuf.length);
                    this._locale.exit();
                } finally {
                }
            }
            return i;
        }

        @Override // java.io.Reader
        public int read(char[] cbuf, int off, int len) throws IOException {
            int i;
            checkClosed();
            if (this._locale.noSync()) {
                this._locale.enter();
                try {
                    int i2 = this._textSaver.read(cbuf, off, len);
                    this._locale.exit();
                    return i2;
                } finally {
                }
            }
            synchronized (this._locale) {
                this._locale.enter();
                try {
                    i = this._textSaver.read(cbuf, off, len);
                    this._locale.exit();
                } finally {
                }
            }
            return i;
        }

        private void checkClosed() throws IOException {
            if (this._closed) {
                throw new IOException("Reader has been closed");
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$InputStreamSaver.class */
    static final class InputStreamSaver extends InputStream {
        private Locale _locale;
        private boolean _closed = false;
        private OutputStreamImpl _outStreamImpl;
        private TextSaver _textSaver;
        private OutputStreamWriter _converter;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Saver.class.desiredAssertionStatus();
        }

        InputStreamSaver(Cur c, XmlOptions options) {
            String ianaEncoding;
            this._locale = c._locale;
            if (!$assertionsDisabled && !this._locale.entered()) {
                throw new AssertionError();
            }
            XmlOptions options2 = XmlOptions.maskNull(options);
            this._outStreamImpl = new OutputStreamImpl();
            String encoding = null;
            XmlDocumentProperties props = Locale.getDocProps(c, false);
            if (props != null && props.getEncoding() != null) {
                encoding = EncodingMap.getIANA2JavaMapping(props.getEncoding());
            }
            encoding = options2.hasOption(XmlOptions.CHARACTER_ENCODING) ? (String) options2.get(XmlOptions.CHARACTER_ENCODING) : encoding;
            if (encoding != null && (ianaEncoding = EncodingMap.getJava2IANAMapping(encoding)) != null) {
                encoding = ianaEncoding;
            }
            encoding = encoding == null ? EncodingMap.getJava2IANAMapping("UTF8") : encoding;
            String javaEncoding = EncodingMap.getIANA2JavaMapping(encoding);
            if (javaEncoding == null) {
                throw new IllegalStateException("Unknown encoding: " + encoding);
            }
            try {
                this._converter = new OutputStreamWriter(this._outStreamImpl, javaEncoding);
                this._textSaver = new TextSaver(c, options2, encoding);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this._closed = true;
        }

        private void checkClosed() throws IOException {
            if (this._closed) {
                throw new IOException("Stream closed");
            }
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            int i;
            checkClosed();
            if (this._locale.noSync()) {
                this._locale.enter();
                try {
                    int i2 = this._outStreamImpl.read();
                    this._locale.exit();
                    return i2;
                } finally {
                }
            }
            synchronized (this._locale) {
                this._locale.enter();
                try {
                    i = this._outStreamImpl.read();
                    this._locale.exit();
                } finally {
                }
            }
            return i;
        }

        @Override // java.io.InputStream
        public int read(byte[] bbuf, int off, int len) throws IOException {
            int i;
            checkClosed();
            if (bbuf == null) {
                throw new NullPointerException("buf to read into is null");
            }
            if (off < 0 || off > bbuf.length) {
                throw new IndexOutOfBoundsException("Offset is not within buf");
            }
            if (this._locale.noSync()) {
                this._locale.enter();
                try {
                    int i2 = this._outStreamImpl.read(bbuf, off, len);
                    this._locale.exit();
                    return i2;
                } finally {
                }
            }
            synchronized (this._locale) {
                this._locale.enter();
                try {
                    i = this._outStreamImpl.read(bbuf, off, len);
                    this._locale.exit();
                } finally {
                }
            }
            return i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Incorrect condition in loop: B:7:0x0010 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int ensure(int r5) {
            /*
                r4 = this;
                r0 = r5
                if (r0 > 0) goto L6
                r0 = 1
                r5 = r0
            L6:
                r0 = r4
                org.apache.xmlbeans.impl.store.Saver$InputStreamSaver$OutputStreamImpl r0 = r0._outStreamImpl
                int r0 = r0.getAvailable()
                r6 = r0
            Le:
                r0 = r6
                r1 = r5
                if (r0 >= r1) goto L35
                r0 = r4
                org.apache.xmlbeans.impl.store.Saver$TextSaver r0 = r0._textSaver
                r1 = r4
                java.io.OutputStreamWriter r1 = r1._converter
                r2 = 2048(0x800, float:2.87E-42)
                int r0 = r0.write(r1, r2)
                r1 = 2048(0x800, float:2.87E-42)
                if (r0 >= r1) goto L2a
                goto L35
            L2a:
                r0 = r4
                org.apache.xmlbeans.impl.store.Saver$InputStreamSaver$OutputStreamImpl r0 = r0._outStreamImpl
                int r0 = r0.getAvailable()
                r6 = r0
                goto Le
            L35:
                r0 = r4
                org.apache.xmlbeans.impl.store.Saver$InputStreamSaver$OutputStreamImpl r0 = r0._outStreamImpl
                int r0 = r0.getAvailable()
                r6 = r0
                r0 = r6
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.Saver.InputStreamSaver.ensure(int):int");
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            int iEnsure;
            if (this._locale.noSync()) {
                this._locale.enter();
                try {
                    int iEnsure2 = ensure(1024);
                    this._locale.exit();
                    return iEnsure2;
                } finally {
                }
            }
            synchronized (this._locale) {
                this._locale.enter();
                try {
                    iEnsure = ensure(1024);
                    this._locale.exit();
                } finally {
                }
            }
            return iEnsure;
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$InputStreamSaver$OutputStreamImpl.class */
        private final class OutputStreamImpl extends OutputStream {
            private static final int _initialBufSize = 4096;
            private int _free;
            private int _in;
            private int _out;
            private byte[] _buf;
            static final /* synthetic */ boolean $assertionsDisabled;

            private OutputStreamImpl() {
            }

            static {
                $assertionsDisabled = !Saver.class.desiredAssertionStatus();
            }

            int read() {
                if (InputStreamSaver.this.ensure(1) == 0) {
                    return -1;
                }
                if (!$assertionsDisabled && getAvailable() <= 0) {
                    throw new AssertionError();
                }
                byte b = this._buf[this._out];
                this._out = (this._out + 1) % this._buf.length;
                this._free++;
                return b;
            }

            int read(byte[] bbuf, int off, int len) {
                int chunk;
                int n = InputStreamSaver.this.ensure(len);
                if (n == 0) {
                    return -1;
                }
                if (bbuf == null || len <= 0) {
                    return 0;
                }
                if (n < len) {
                    len = n;
                }
                if (this._out < this._in || (chunk = this._buf.length - this._out) >= len) {
                    System.arraycopy(this._buf, this._out, bbuf, off, len);
                } else {
                    System.arraycopy(this._buf, this._out, bbuf, off, chunk);
                    System.arraycopy(this._buf, 0, bbuf, off + chunk, len - chunk);
                }
                this._out = (this._out + len) % this._buf.length;
                this._free += len;
                return len;
            }

            int getAvailable() {
                if (this._buf == null) {
                    return 0;
                }
                return this._buf.length - this._free;
            }

            @Override // java.io.OutputStream
            public void write(int bite) {
                if (this._free == 0) {
                    resize(1);
                }
                if (!$assertionsDisabled && this._free <= 0) {
                    throw new AssertionError();
                }
                this._buf[this._in] = (byte) bite;
                this._in = (this._in + 1) % this._buf.length;
                this._free--;
            }

            @Override // java.io.OutputStream
            public void write(byte[] buf, int off, int cbyte) {
                if (!$assertionsDisabled && cbyte < 0) {
                    throw new AssertionError();
                }
                if (cbyte == 0) {
                    return;
                }
                if (this._free < cbyte) {
                    resize(cbyte);
                }
                if (this._in == this._out) {
                    if (!$assertionsDisabled && getAvailable() != 0) {
                        throw new AssertionError();
                    }
                    if (!$assertionsDisabled && this._free != this._buf.length - getAvailable()) {
                        throw new AssertionError();
                    }
                    this._out = 0;
                    this._in = 0;
                }
                int chunk = this._buf.length - this._in;
                if (this._in <= this._out || cbyte < chunk) {
                    System.arraycopy(buf, off, this._buf, this._in, cbyte);
                    this._in += cbyte;
                } else {
                    System.arraycopy(buf, off, this._buf, this._in, chunk);
                    System.arraycopy(buf, off + chunk, this._buf, 0, cbyte - chunk);
                    this._in = (this._in + cbyte) % this._buf.length;
                }
                this._free -= cbyte;
            }

            void resize(int cbyte) {
                if (!$assertionsDisabled && cbyte <= this._free) {
                    throw new AssertionError(cbyte + " !> " + this._free);
                }
                int newLen = this._buf == null ? 4096 : this._buf.length * 2;
                int used = getAvailable();
                while (newLen - used < cbyte) {
                    newLen *= 2;
                }
                byte[] newBuf = new byte[newLen];
                if (used > 0) {
                    if (this._in > this._out) {
                        System.arraycopy(this._buf, this._out, newBuf, 0, used);
                    } else {
                        System.arraycopy(this._buf, this._out, newBuf, 0, used - this._in);
                        System.arraycopy(this._buf, 0, newBuf, used - this._in, this._in);
                    }
                    this._out = 0;
                    this._in = used;
                    this._free += newBuf.length - this._buf.length;
                } else {
                    this._free = newBuf.length;
                    if (!$assertionsDisabled && this._in != this._out) {
                        throw new AssertionError();
                    }
                }
                this._buf = newBuf;
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver.class */
    static final class XmlInputStreamSaver extends Saver {
        private XmlEventImpl _in;
        private XmlEventImpl _out;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Saver.class.desiredAssertionStatus();
        }

        XmlInputStreamSaver(Cur c, XmlOptions options) {
            super(c, options);
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected boolean emitElement(SaveCur c, ArrayList attrNames, ArrayList attrValues) {
            if (!$assertionsDisabled && !c.isElem()) {
                throw new AssertionError();
            }
            iterateMappings();
            while (hasMapping()) {
                enqueue(new StartPrefixMappingImpl(mappingPrefix(), mappingUri()));
                nextMapping();
            }
            StartElementImpl.AttributeImpl lastAttr = null;
            StartElementImpl.AttributeImpl attributes = null;
            StartElementImpl.AttributeImpl namespaces = null;
            for (int i = 0; i < attrNames.size(); i++) {
                XMLName attXMLName = computeName((QName) attrNames.get(i), this, true);
                StartElementImpl.AttributeImpl attr = new StartElementImpl.NormalAttributeImpl(attXMLName, (String) attrValues.get(i));
                if (attributes == null) {
                    attributes = attr;
                } else {
                    lastAttr._next = attr;
                }
                lastAttr = attr;
            }
            StartElementImpl.AttributeImpl lastAttr2 = null;
            iterateMappings();
            while (hasMapping()) {
                String prefix = mappingPrefix();
                String uri = mappingUri();
                StartElementImpl.AttributeImpl attr2 = new StartElementImpl.XmlnsAttributeImpl(prefix, uri);
                if (namespaces == null) {
                    namespaces = attr2;
                } else {
                    lastAttr2._next = attr2;
                }
                lastAttr2 = attr2;
                nextMapping();
            }
            QName name = c.getName();
            enqueue(new StartElementImpl(computeName(name, this, false), attributes, namespaces, getPrefixMap()));
            return false;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitFinish(SaveCur c) {
            if (c.isRoot()) {
                enqueue(new EndDocumentImpl());
            } else {
                XMLName xmlName = computeName(c.getName(), this, false);
                enqueue(new EndElementImpl(xmlName));
            }
            emitEndPrefixMappings();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitText(SaveCur c) {
            if (!$assertionsDisabled && !c.isText()) {
                throw new AssertionError();
            }
            Object src = c.getChars();
            int cch = c._cchSrc;
            int off = c._offSrc;
            enqueue(new CharacterDataImpl(src, cch, off));
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitComment(SaveCur c) {
            enqueue(new CommentImpl(c.getChars(), c._cchSrc, c._offSrc));
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitProcinst(SaveCur c) {
            String target = null;
            QName name = c.getName();
            if (name != null) {
                target = name.getLocalPart();
            }
            enqueue(new ProcessingInstructionImpl(target, c.getChars(), c._cchSrc, c._offSrc));
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitDocType(String doctypeName, String publicID, String systemID) {
            enqueue(new StartDocumentImpl(systemID, null, true, null));
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitStartDoc(SaveCur c) {
            emitDocType(null, null, null);
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitEndDoc(SaveCur c) {
            enqueue(new EndDocumentImpl());
        }

        XMLEvent dequeue() {
            if (this._out == null) {
                enterLocale();
                try {
                    if (!process()) {
                        return null;
                    }
                    exitLocale();
                } finally {
                    exitLocale();
                }
            }
            if (this._out == null) {
                return null;
            }
            XmlEventImpl e = this._out;
            XmlEventImpl xmlEventImpl = this._out._next;
            this._out = xmlEventImpl;
            if (xmlEventImpl == null) {
                this._in = null;
            }
            return e;
        }

        private void enqueue(XmlEventImpl e) {
            if (!$assertionsDisabled && e._next != null) {
                throw new AssertionError();
            }
            if (this._in == null) {
                if (!$assertionsDisabled && this._out != null) {
                    throw new AssertionError();
                }
                this._in = e;
                this._out = e;
                return;
            }
            this._in._next = e;
            this._in = e;
        }

        protected void emitEndPrefixMappings() {
            iterateMappings();
            while (hasMapping()) {
                String prefix = mappingPrefix();
                String uri = mappingUri();
                if (0 == 0) {
                    enqueue(new EndPrefixMappingImpl(prefix));
                } else {
                    enqueue(new ChangePrefixMappingImpl(prefix, uri, null));
                }
                nextMapping();
            }
        }

        private static XMLName computeName(QName name, Saver saver, boolean needsPrefix) {
            String uri = name.getNamespaceURI();
            String local = name.getLocalPart();
            if (!$assertionsDisabled && uri == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && local.length() <= 0) {
                throw new AssertionError();
            }
            String prefix = null;
            if (uri != null && uri.length() != 0) {
                prefix = name.getPrefix();
                String mappedUri = saver.getNamespaceForPrefix(prefix);
                if (mappedUri == null || !mappedUri.equals(uri)) {
                    prefix = saver.getUriMapping(uri);
                }
                if (needsPrefix && prefix.length() == 0) {
                    prefix = saver.getNonDefaultUriMapping(uri);
                }
            }
            return new XmlNameImpl(uri, local, prefix);
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$XmlEventImpl.class */
        private static abstract class XmlEventImpl extends XmlEventBase {
            XmlEventImpl _next;

            XmlEventImpl(int type) {
                super(type);
            }

            @Override // org.apache.xmlbeans.xml.stream.XMLEvent
            public XMLName getName() {
                return null;
            }

            @Override // org.apache.xmlbeans.xml.stream.XMLEvent
            public XMLName getSchemaType() {
                throw new RuntimeException("NYI");
            }

            @Override // org.apache.xmlbeans.xml.stream.XMLEvent
            public boolean hasName() {
                return false;
            }

            @Override // org.apache.xmlbeans.xml.stream.XMLEvent
            public final Location getLocation() {
                return null;
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$StartDocumentImpl.class */
        private static class StartDocumentImpl extends XmlEventImpl implements StartDocument {
            String _systemID;
            String _encoding;
            boolean _standAlone;
            String _version;

            StartDocumentImpl(String systemID, String encoding, boolean isStandAlone, String version) {
                super(256);
                this._systemID = systemID;
                this._encoding = encoding;
                this._standAlone = isStandAlone;
                this._version = version;
            }

            @Override // org.apache.xmlbeans.xml.stream.StartDocument
            public String getSystemId() {
                return this._systemID;
            }

            @Override // org.apache.xmlbeans.xml.stream.StartDocument
            public String getCharacterEncodingScheme() {
                return this._encoding;
            }

            @Override // org.apache.xmlbeans.xml.stream.StartDocument
            public boolean isStandalone() {
                return this._standAlone;
            }

            @Override // org.apache.xmlbeans.xml.stream.StartDocument
            public String getVersion() {
                return this._version;
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$StartElementImpl.class */
        private static class StartElementImpl extends XmlEventImpl implements StartElement {
            private XMLName _name;
            private Map _prefixMap;
            private AttributeImpl _attributes;
            private AttributeImpl _namespaces;

            StartElementImpl(XMLName name, AttributeImpl attributes, AttributeImpl namespaces, Map prefixMap) {
                super(2);
                this._name = name;
                this._attributes = attributes;
                this._namespaces = namespaces;
                this._prefixMap = prefixMap;
            }

            @Override // org.apache.xmlbeans.impl.store.Saver.XmlInputStreamSaver.XmlEventImpl, org.apache.xmlbeans.xml.stream.XMLEvent
            public boolean hasName() {
                return true;
            }

            @Override // org.apache.xmlbeans.impl.store.Saver.XmlInputStreamSaver.XmlEventImpl, org.apache.xmlbeans.xml.stream.XMLEvent
            public XMLName getName() {
                return this._name;
            }

            @Override // org.apache.xmlbeans.xml.stream.StartElement
            public AttributeIterator getAttributes() {
                return new AttributeIteratorImpl(this._attributes, null);
            }

            @Override // org.apache.xmlbeans.xml.stream.StartElement
            public AttributeIterator getNamespaces() {
                return new AttributeIteratorImpl(null, this._namespaces);
            }

            @Override // org.apache.xmlbeans.xml.stream.StartElement
            public AttributeIterator getAttributesAndNamespaces() {
                return new AttributeIteratorImpl(this._attributes, this._namespaces);
            }

            @Override // org.apache.xmlbeans.xml.stream.StartElement
            public Attribute getAttributeByName(XMLName xmlName) {
                AttributeImpl attributeImpl = this._attributes;
                while (true) {
                    AttributeImpl a = attributeImpl;
                    if (a != null) {
                        if (!xmlName.equals(a.getName())) {
                            attributeImpl = a._next;
                        } else {
                            return a;
                        }
                    } else {
                        return null;
                    }
                }
            }

            @Override // org.apache.xmlbeans.xml.stream.StartElement
            public String getNamespaceUri(String prefix) {
                return (String) this._prefixMap.get(prefix == null ? "" : prefix);
            }

            @Override // org.apache.xmlbeans.xml.stream.StartElement
            public Map getNamespaceMap() {
                return this._prefixMap;
            }

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$StartElementImpl$AttributeIteratorImpl.class */
            private static class AttributeIteratorImpl implements AttributeIterator {
                private AttributeImpl _attributes;
                private AttributeImpl _namespaces;

                AttributeIteratorImpl(AttributeImpl attributes, AttributeImpl namespaces) {
                    this._attributes = attributes;
                    this._namespaces = namespaces;
                }

                public Object monitor() {
                    return this;
                }

                @Override // org.apache.xmlbeans.xml.stream.AttributeIterator
                public Attribute next() {
                    AttributeImpl attributeImpl;
                    synchronized (monitor()) {
                        checkVersion();
                        AttributeImpl attr = null;
                        if (this._attributes != null) {
                            attr = this._attributes;
                            this._attributes = attr._next;
                        } else if (this._namespaces != null) {
                            attr = this._namespaces;
                            this._namespaces = attr._next;
                        }
                        attributeImpl = attr;
                    }
                    return attributeImpl;
                }

                @Override // org.apache.xmlbeans.xml.stream.AttributeIterator
                public boolean hasNext() {
                    boolean z;
                    synchronized (monitor()) {
                        checkVersion();
                        z = (this._attributes == null && this._namespaces == null) ? false : true;
                    }
                    return z;
                }

                @Override // org.apache.xmlbeans.xml.stream.AttributeIterator
                public Attribute peek() {
                    synchronized (monitor()) {
                        checkVersion();
                        if (this._attributes != null) {
                            return this._attributes;
                        }
                        if (this._namespaces != null) {
                            return this._namespaces;
                        }
                        return null;
                    }
                }

                @Override // org.apache.xmlbeans.xml.stream.AttributeIterator
                public void skip() {
                    synchronized (monitor()) {
                        checkVersion();
                        if (this._attributes != null) {
                            this._attributes = this._attributes._next;
                        } else if (this._namespaces != null) {
                            this._namespaces = this._namespaces._next;
                        }
                    }
                }

                private final void checkVersion() {
                }
            }

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$StartElementImpl$AttributeImpl.class */
            private static abstract class AttributeImpl implements Attribute {
                AttributeImpl _next;
                protected XMLName _name;

                AttributeImpl() {
                }

                @Override // org.apache.xmlbeans.xml.stream.Attribute
                public XMLName getName() {
                    return this._name;
                }

                @Override // org.apache.xmlbeans.xml.stream.Attribute
                public String getType() {
                    return "CDATA";
                }

                @Override // org.apache.xmlbeans.xml.stream.Attribute
                public XMLName getSchemaType() {
                    return null;
                }
            }

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$StartElementImpl$XmlnsAttributeImpl.class */
            private static class XmlnsAttributeImpl extends AttributeImpl {
                private String _uri;

                XmlnsAttributeImpl(String prefix, String uri) {
                    String local;
                    String prefix2;
                    this._uri = uri;
                    if (prefix.length() == 0) {
                        prefix2 = null;
                        local = "xmlns";
                    } else {
                        local = prefix;
                        prefix2 = "xmlns";
                    }
                    this._name = new XmlNameImpl(null, local, prefix2);
                }

                @Override // org.apache.xmlbeans.xml.stream.Attribute
                public String getValue() {
                    return this._uri;
                }
            }

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$StartElementImpl$NormalAttributeImpl.class */
            private static class NormalAttributeImpl extends AttributeImpl {
                private String _value;

                NormalAttributeImpl(XMLName name, String value) {
                    this._name = name;
                    this._value = value;
                }

                @Override // org.apache.xmlbeans.xml.stream.Attribute
                public String getValue() {
                    return this._value;
                }
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$StartPrefixMappingImpl.class */
        private static class StartPrefixMappingImpl extends XmlEventImpl implements StartPrefixMapping {
            private String _prefix;
            private String _uri;

            StartPrefixMappingImpl(String prefix, String uri) {
                super(1024);
                this._prefix = prefix;
                this._uri = uri;
            }

            @Override // org.apache.xmlbeans.xml.stream.StartPrefixMapping
            public String getNamespaceUri() {
                return this._uri;
            }

            @Override // org.apache.xmlbeans.xml.stream.StartPrefixMapping
            public String getPrefix() {
                return this._prefix;
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$ChangePrefixMappingImpl.class */
        private static class ChangePrefixMappingImpl extends XmlEventImpl implements ChangePrefixMapping {
            private String _oldUri;
            private String _newUri;
            private String _prefix;

            ChangePrefixMappingImpl(String prefix, String oldUri, String newUri) {
                super(4096);
                this._oldUri = oldUri;
                this._newUri = newUri;
                this._prefix = prefix;
            }

            @Override // org.apache.xmlbeans.xml.stream.ChangePrefixMapping
            public String getOldNamespaceUri() {
                return this._oldUri;
            }

            @Override // org.apache.xmlbeans.xml.stream.ChangePrefixMapping
            public String getNewNamespaceUri() {
                return this._newUri;
            }

            @Override // org.apache.xmlbeans.xml.stream.ChangePrefixMapping
            public String getPrefix() {
                return this._prefix;
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$EndPrefixMappingImpl.class */
        private static class EndPrefixMappingImpl extends XmlEventImpl implements EndPrefixMapping {
            private String _prefix;

            EndPrefixMappingImpl(String prefix) {
                super(2048);
                this._prefix = prefix;
            }

            @Override // org.apache.xmlbeans.xml.stream.EndPrefixMapping
            public String getPrefix() {
                return this._prefix;
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$EndElementImpl.class */
        private static class EndElementImpl extends XmlEventImpl implements EndElement {
            private XMLName _name;

            EndElementImpl(XMLName name) {
                super(4);
                this._name = name;
            }

            @Override // org.apache.xmlbeans.impl.store.Saver.XmlInputStreamSaver.XmlEventImpl, org.apache.xmlbeans.xml.stream.XMLEvent
            public boolean hasName() {
                return true;
            }

            @Override // org.apache.xmlbeans.impl.store.Saver.XmlInputStreamSaver.XmlEventImpl, org.apache.xmlbeans.xml.stream.XMLEvent
            public XMLName getName() {
                return this._name;
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$EndDocumentImpl.class */
        private static class EndDocumentImpl extends XmlEventImpl implements EndDocument {
            EndDocumentImpl() {
                super(512);
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$TripletEventImpl.class */
        private static class TripletEventImpl extends XmlEventImpl implements CharacterData {
            private Object _obj;
            private int _cch;
            private int _off;

            TripletEventImpl(int eventType, Object obj, int cch, int off) {
                super(eventType);
                this._obj = obj;
                this._cch = cch;
                this._off = off;
            }

            @Override // org.apache.xmlbeans.xml.stream.CharacterData
            public String getContent() {
                return CharUtil.getString(this._obj, this._off, this._cch);
            }

            @Override // org.apache.xmlbeans.xml.stream.CharacterData
            public boolean hasContent() {
                return this._cch > 0;
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$CharacterDataImpl.class */
        private static class CharacterDataImpl extends TripletEventImpl implements CharacterData {
            CharacterDataImpl(Object obj, int cch, int off) {
                super(16, obj, cch, off);
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$CommentImpl.class */
        private static class CommentImpl extends TripletEventImpl implements Comment {
            CommentImpl(Object obj, int cch, int off) {
                super(32, obj, cch, off);
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamSaver$ProcessingInstructionImpl.class */
        private static class ProcessingInstructionImpl extends TripletEventImpl implements ProcessingInstruction {
            private String _target;

            ProcessingInstructionImpl(String target, Object obj, int cch, int off) {
                super(8, obj, cch, off);
                this._target = target;
            }

            @Override // org.apache.xmlbeans.xml.stream.ProcessingInstruction
            public String getTarget() {
                return this._target;
            }

            @Override // org.apache.xmlbeans.xml.stream.ProcessingInstruction
            public String getData() {
                return getContent();
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$XmlInputStreamImpl.class */
    static final class XmlInputStreamImpl extends GenericXmlInputStream {
        private XmlInputStreamSaver _xmlInputStreamSaver;

        XmlInputStreamImpl(Cur cur, XmlOptions options) {
            this._xmlInputStreamSaver = new XmlInputStreamSaver(cur, options);
            this._xmlInputStreamSaver.process();
        }

        @Override // org.apache.xmlbeans.impl.common.GenericXmlInputStream
        protected XMLEvent nextEvent() throws XMLStreamException {
            return this._xmlInputStreamSaver.dequeue();
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$SaxSaver.class */
    static final class SaxSaver extends Saver {
        private ContentHandler _contentHandler;
        private LexicalHandler _lexicalHandler;
        private AttributesImpl _attributes;
        private char[] _buf;
        private boolean _nsAsAttrs;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Saver.class.desiredAssertionStatus();
        }

        SaxSaver(Cur c, XmlOptions options, ContentHandler ch2, LexicalHandler lh) throws SAXException {
            super(c, options);
            this._contentHandler = ch2;
            this._lexicalHandler = lh;
            this._attributes = new AttributesImpl();
            this._nsAsAttrs = !options.hasOption(XmlOptions.SAVE_SAX_NO_NSDECLS_IN_ATTRIBUTES);
            this._contentHandler.startDocument();
            do {
                try {
                } catch (SaverSAXException e) {
                    throw e._saxException;
                }
            } while (process());
            this._contentHandler.endDocument();
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$SaxSaver$SaverSAXException.class */
        private class SaverSAXException extends RuntimeException {
            SAXException _saxException;

            SaverSAXException(SAXException e) {
                this._saxException = e;
            }
        }

        private String getPrefixedName(QName name) {
            String uri = name.getNamespaceURI();
            String local = name.getLocalPart();
            if (uri.length() == 0) {
                return local;
            }
            String prefix = getUriMapping(uri);
            if (prefix.length() == 0) {
                return local;
            }
            return prefix + ":" + local;
        }

        private void emitNamespacesHelper() throws SAXException {
            iterateMappings();
            while (hasMapping()) {
                String prefix = mappingPrefix();
                String uri = mappingUri();
                try {
                    this._contentHandler.startPrefixMapping(prefix, uri);
                    if (this._nsAsAttrs) {
                        if (prefix == null || prefix.length() == 0) {
                            this._attributes.addAttribute("http://www.w3.org/2000/xmlns/", "xmlns", "xmlns", "CDATA", uri);
                        } else {
                            this._attributes.addAttribute("http://www.w3.org/2000/xmlns/", prefix, Sax2Dom.XMLNS_STRING + prefix, "CDATA", uri);
                        }
                    }
                    nextMapping();
                } catch (SAXException e) {
                    throw new SaverSAXException(e);
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected boolean emitElement(SaveCur c, ArrayList attrNames, ArrayList attrValues) throws SAXException {
            this._attributes.clear();
            if (saveNamespacesFirst()) {
                emitNamespacesHelper();
            }
            for (int i = 0; i < attrNames.size(); i++) {
                QName name = (QName) attrNames.get(i);
                this._attributes.addAttribute(name.getNamespaceURI(), name.getLocalPart(), getPrefixedName(name), "CDATA", (String) attrValues.get(i));
            }
            if (!saveNamespacesFirst()) {
                emitNamespacesHelper();
            }
            QName elemName = c.getName();
            try {
                this._contentHandler.startElement(elemName.getNamespaceURI(), elemName.getLocalPart(), getPrefixedName(elemName), this._attributes);
                return false;
            } catch (SAXException e) {
                throw new SaverSAXException(e);
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitFinish(SaveCur c) throws SAXException {
            QName name = c.getName();
            try {
                this._contentHandler.endElement(name.getNamespaceURI(), name.getLocalPart(), getPrefixedName(name));
                iterateMappings();
                while (hasMapping()) {
                    this._contentHandler.endPrefixMapping(mappingPrefix());
                    nextMapping();
                }
            } catch (SAXException e) {
                throw new SaverSAXException(e);
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitText(SaveCur c) throws SAXException {
            if (!$assertionsDisabled && !c.isText()) {
                throw new AssertionError();
            }
            Object src = c.getChars();
            try {
                if (src instanceof char[]) {
                    this._contentHandler.characters((char[]) src, c._offSrc, c._cchSrc);
                } else {
                    if (this._buf == null) {
                        this._buf = new char[1024];
                    }
                    while (c._cchSrc > 0) {
                        int cch = Math.min(this._buf.length, c._cchSrc);
                        CharUtil.getChars(this._buf, 0, src, c._offSrc, cch);
                        this._contentHandler.characters(this._buf, 0, cch);
                        c._offSrc += cch;
                        c._cchSrc -= cch;
                    }
                }
            } catch (SAXException e) {
                throw new SaverSAXException(e);
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitComment(SaveCur c) throws SAXException {
            if (this._lexicalHandler != null) {
                c.push();
                c.next();
                try {
                    if (!c.isText()) {
                        this._lexicalHandler.comment(null, 0, 0);
                    } else {
                        Object src = c.getChars();
                        if (src instanceof char[]) {
                            this._lexicalHandler.comment((char[]) src, c._offSrc, c._cchSrc);
                        } else {
                            if (this._buf == null || this._buf.length < c._cchSrc) {
                                this._buf = new char[Math.max(1024, c._cchSrc)];
                            }
                            CharUtil.getChars(this._buf, 0, src, c._offSrc, c._cchSrc);
                            this._lexicalHandler.comment(this._buf, 0, c._cchSrc);
                        }
                    }
                    c.pop();
                } catch (SAXException e) {
                    throw new SaverSAXException(e);
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitProcinst(SaveCur c) throws SAXException {
            c.getName().getLocalPart();
            c.push();
            c.next();
            String value = CharUtil.getString(c.getChars(), c._offSrc, c._cchSrc);
            c.pop();
            try {
                this._contentHandler.processingInstruction(c.getName().getLocalPart(), value);
            } catch (SAXException e) {
                throw new SaverSAXException(e);
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitDocType(String docTypeName, String publicId, String systemId) throws SAXException {
            if (this._lexicalHandler != null) {
                try {
                    this._lexicalHandler.startDTD(docTypeName, publicId, systemId);
                    this._lexicalHandler.endDTD();
                } catch (SAXException e) {
                    throw new SaverSAXException(e);
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitStartDoc(SaveCur c) {
        }

        @Override // org.apache.xmlbeans.impl.store.Saver
        protected void emitEndDoc(SaveCur c) {
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$SaveCur.class */
    static abstract class SaveCur {
        int _offSrc;
        int _cchSrc;

        abstract void release();

        abstract int kind();

        abstract QName getName();

        abstract String getXmlnsPrefix();

        abstract String getXmlnsUri();

        abstract boolean isXmlns();

        abstract boolean hasChildren();

        abstract boolean hasText();

        abstract boolean isTextCData();

        abstract boolean toFirstAttr();

        abstract boolean toNextAttr();

        abstract String getAttrValue();

        abstract boolean next();

        abstract void toEnd();

        abstract void push();

        abstract void pop();

        abstract Object getChars();

        abstract List getAncestorNamespaces();

        abstract XmlDocumentProperties getDocProps();

        SaveCur() {
        }

        final boolean isRoot() {
            return kind() == 1;
        }

        final boolean isElem() {
            return kind() == 2;
        }

        final boolean isAttr() {
            return kind() == 3;
        }

        final boolean isText() {
            return kind() == 0;
        }

        final boolean isComment() {
            return kind() == 4;
        }

        final boolean isProcinst() {
            return kind() == 5;
        }

        final boolean isFinish() {
            return Cur.kindIsFinish(kind());
        }

        final boolean isContainer() {
            return Cur.kindIsContainer(kind());
        }

        final boolean isNormalAttr() {
            return kind() == 3 && !isXmlns();
        }

        final boolean skip() {
            toEnd();
            return next();
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$DocSaveCur.class */
    private static final class DocSaveCur extends SaveCur {
        private Cur _cur;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Saver.class.desiredAssertionStatus();
        }

        DocSaveCur(Cur c) {
            if (!$assertionsDisabled && !c.isRoot()) {
                throw new AssertionError();
            }
            this._cur = c.weakCur(this);
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void release() {
            this._cur.release();
            this._cur = null;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        int kind() {
            return this._cur.kind();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        QName getName() {
            return this._cur.getName();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getXmlnsPrefix() {
            return this._cur.getXmlnsPrefix();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getXmlnsUri() {
            return this._cur.getXmlnsUri();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean isXmlns() {
            return this._cur.isXmlns();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean hasChildren() {
            return this._cur.hasChildren();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean hasText() {
            return this._cur.hasText();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean isTextCData() {
            return this._cur.isTextCData();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean toFirstAttr() {
            return this._cur.toFirstAttr();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean toNextAttr() {
            return this._cur.toNextAttr();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getAttrValue() {
            if ($assertionsDisabled || this._cur.isAttr()) {
                return this._cur.getValueAsString();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void toEnd() {
            this._cur.toEnd();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean next() {
            return this._cur.next();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void push() {
            this._cur.push();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void pop() {
            this._cur.pop();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        List getAncestorNamespaces() {
            return null;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        Object getChars() {
            Object o = this._cur.getChars(-1);
            this._offSrc = this._cur._offSrc;
            this._cchSrc = this._cur._cchSrc;
            return o;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        XmlDocumentProperties getDocProps() {
            return Locale.getDocProps(this._cur, false);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$FilterSaveCur.class */
    private static abstract class FilterSaveCur extends SaveCur {
        private SaveCur _cur;
        static final /* synthetic */ boolean $assertionsDisabled;

        protected abstract boolean filter();

        static {
            $assertionsDisabled = !Saver.class.desiredAssertionStatus();
        }

        FilterSaveCur(SaveCur c) {
            if (!$assertionsDisabled && !c.isRoot()) {
                throw new AssertionError();
            }
            this._cur = c;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void release() {
            this._cur.release();
            this._cur = null;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        int kind() {
            return this._cur.kind();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        QName getName() {
            return this._cur.getName();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getXmlnsPrefix() {
            return this._cur.getXmlnsPrefix();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getXmlnsUri() {
            return this._cur.getXmlnsUri();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean isXmlns() {
            return this._cur.isXmlns();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean hasChildren() {
            return this._cur.hasChildren();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean hasText() {
            return this._cur.hasText();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean isTextCData() {
            return this._cur.isTextCData();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean toFirstAttr() {
            return this._cur.toFirstAttr();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean toNextAttr() {
            return this._cur.toNextAttr();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getAttrValue() {
            return this._cur.getAttrValue();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void toEnd() {
            this._cur.toEnd();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean next() {
            if (!this._cur.next()) {
                return false;
            }
            if (!filter()) {
                return true;
            }
            if (!$assertionsDisabled && (isRoot() || isText() || isAttr())) {
                throw new AssertionError();
            }
            toEnd();
            return next();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void push() {
            this._cur.push();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void pop() {
            this._cur.pop();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        List getAncestorNamespaces() {
            return this._cur.getAncestorNamespaces();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        Object getChars() {
            Object o = this._cur.getChars();
            this._offSrc = this._cur._offSrc;
            this._cchSrc = this._cur._cchSrc;
            return o;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        XmlDocumentProperties getDocProps() {
            return this._cur.getDocProps();
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$FilterPiSaveCur.class */
    private static final class FilterPiSaveCur extends FilterSaveCur {
        private String _piTarget;

        FilterPiSaveCur(SaveCur c, String target) {
            super(c);
            this._piTarget = target;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.FilterSaveCur
        protected boolean filter() {
            return kind() == 5 && getName().getLocalPart().equals(this._piTarget);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$FragSaveCur.class */
    private static final class FragSaveCur extends SaveCur {
        private Cur _cur;
        private Cur _end;
        private ArrayList _ancestorNamespaces;
        private QName _elem;
        private boolean _saveAttr;
        private static final int ROOT_START = 1;
        private static final int ELEM_START = 2;
        private static final int ROOT_END = 3;
        private static final int ELEM_END = 4;
        private static final int CUR = 5;
        private int _state;
        private int[] _stateStack;
        private int _stateStackSize;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Saver.class.desiredAssertionStatus();
        }

        FragSaveCur(Cur start, Cur end, QName synthElem) {
            this._saveAttr = start.isAttr() && start.isSamePos(end);
            this._cur = start.weakCur(this);
            this._end = end.weakCur(this);
            this._elem = synthElem;
            this._state = 1;
            this._stateStack = new int[8];
            start.push();
            computeAncestorNamespaces(start);
            start.pop();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        List getAncestorNamespaces() {
            return this._ancestorNamespaces;
        }

        private void computeAncestorNamespaces(Cur c) {
            this._ancestorNamespaces = new ArrayList();
            while (c.toParentRaw()) {
                if (c.toFirstAttr()) {
                    do {
                        if (c.isXmlns()) {
                            String prefix = c.getXmlnsPrefix();
                            String uri = c.getXmlnsUri();
                            if (uri.length() > 0 || prefix.length() == 0) {
                                this._ancestorNamespaces.add(c.getXmlnsPrefix());
                                this._ancestorNamespaces.add(c.getXmlnsUri());
                            }
                        }
                    } while (c.toNextAttr());
                    c.toParent();
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void release() {
            this._cur.release();
            this._cur = null;
            this._end.release();
            this._end = null;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        int kind() {
            switch (this._state) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return -1;
                case 4:
                    return -2;
                default:
                    if ($assertionsDisabled || this._state == 5) {
                        return this._cur.kind();
                    }
                    throw new AssertionError();
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        QName getName() {
            switch (this._state) {
                case 1:
                case 3:
                    return null;
                case 2:
                case 4:
                    return this._elem;
                default:
                    if ($assertionsDisabled || this._state == 5) {
                        return this._cur.getName();
                    }
                    throw new AssertionError();
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getXmlnsPrefix() {
            if ($assertionsDisabled || (this._state == 5 && this._cur.isAttr())) {
                return this._cur.getXmlnsPrefix();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getXmlnsUri() {
            if ($assertionsDisabled || (this._state == 5 && this._cur.isAttr())) {
                return this._cur.getXmlnsUri();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean isXmlns() {
            if ($assertionsDisabled || (this._state == 5 && this._cur.isAttr())) {
                return this._cur.isXmlns();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean hasChildren() {
            boolean hasChildren = false;
            if (isContainer()) {
                push();
                next();
                if (!isText() && !isFinish()) {
                    hasChildren = true;
                }
                pop();
            }
            return hasChildren;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean hasText() {
            boolean hasText = false;
            if (isContainer()) {
                push();
                next();
                if (isText()) {
                    hasText = true;
                }
                pop();
            }
            return hasText;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean isTextCData() {
            return this._cur.isTextCData();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        Object getChars() {
            if (!$assertionsDisabled && (this._state != 5 || !this._cur.isText())) {
                throw new AssertionError();
            }
            Object src = this._cur.getChars(-1);
            this._offSrc = this._cur._offSrc;
            this._cchSrc = this._cur._cchSrc;
            return src;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean next() {
            switch (this._state) {
                case 1:
                    this._state = this._elem == null ? 5 : 2;
                    return true;
                case 2:
                    if (this._saveAttr) {
                        this._state = 4;
                        return true;
                    }
                    if (this._cur.isAttr()) {
                        this._cur.toParent();
                        this._cur.next();
                    }
                    if (this._cur.isSamePos(this._end)) {
                        this._state = 4;
                        return true;
                    }
                    this._state = 5;
                    return true;
                case 3:
                    return false;
                case 4:
                    this._state = 3;
                    return true;
                case 5:
                    if (!$assertionsDisabled && this._cur.isAttr()) {
                        throw new AssertionError();
                    }
                    this._cur.next();
                    if (this._cur.isSamePos(this._end)) {
                        this._state = this._elem == null ? 3 : 4;
                        return true;
                    }
                    return true;
                default:
                    return true;
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void toEnd() {
            switch (this._state) {
                case 1:
                    this._state = 3;
                    return;
                case 2:
                    this._state = 4;
                    return;
                case 3:
                case 4:
                    return;
                default:
                    if (!$assertionsDisabled && (this._state != 5 || this._cur.isAttr() || this._cur.isText())) {
                        throw new AssertionError();
                    }
                    this._cur.toEnd();
                    return;
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean toFirstAttr() {
            switch (this._state) {
                case 1:
                case 3:
                case 4:
                    return false;
                case 2:
                default:
                    if (!$assertionsDisabled && this._state != 2) {
                        throw new AssertionError();
                    }
                    if (!this._cur.isAttr()) {
                        return false;
                    }
                    this._state = 5;
                    return true;
                case 5:
                    return this._cur.toFirstAttr();
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean toNextAttr() {
            if ($assertionsDisabled || this._state == 5) {
                return !this._saveAttr && this._cur.toNextAttr();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getAttrValue() {
            if ($assertionsDisabled || (this._state == 5 && this._cur.isAttr())) {
                return this._cur.getValueAsString();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void push() {
            if (this._stateStackSize == this._stateStack.length) {
                int[] newStateStack = new int[this._stateStackSize * 2];
                System.arraycopy(this._stateStack, 0, newStateStack, 0, this._stateStackSize);
                this._stateStack = newStateStack;
            }
            int[] iArr = this._stateStack;
            int i = this._stateStackSize;
            this._stateStackSize = i + 1;
            iArr[i] = this._state;
            this._cur.push();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void pop() {
            this._cur.pop();
            int[] iArr = this._stateStack;
            int i = this._stateStackSize - 1;
            this._stateStackSize = i;
            this._state = iArr[i];
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        XmlDocumentProperties getDocProps() {
            return Locale.getDocProps(this._cur, false);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/Saver$PrettySaveCur.class */
    private static final class PrettySaveCur extends SaveCur {
        private SaveCur _cur;
        private int _prettyIndent;
        private int _prettyOffset;
        private String _txt;
        private int _depth;
        private boolean _useCDataBookmarks;
        static final /* synthetic */ boolean $assertionsDisabled;
        private boolean _isTextCData = false;
        private StringBuffer _sb = new StringBuffer();
        private ArrayList _stack = new ArrayList();

        static {
            $assertionsDisabled = !Saver.class.desiredAssertionStatus();
        }

        PrettySaveCur(SaveCur c, XmlOptions options) {
            this._useCDataBookmarks = false;
            this._cur = c;
            if (!$assertionsDisabled && options == null) {
                throw new AssertionError();
            }
            this._prettyIndent = 2;
            if (options.hasOption(XmlOptions.SAVE_PRETTY_PRINT_INDENT)) {
                this._prettyIndent = ((Integer) options.get(XmlOptions.SAVE_PRETTY_PRINT_INDENT)).intValue();
            }
            if (options.hasOption(XmlOptions.SAVE_PRETTY_PRINT_OFFSET)) {
                this._prettyOffset = ((Integer) options.get(XmlOptions.SAVE_PRETTY_PRINT_OFFSET)).intValue();
            }
            if (options.hasOption(XmlOptions.LOAD_SAVE_CDATA_BOOKMARKS)) {
                this._useCDataBookmarks = true;
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        List getAncestorNamespaces() {
            return this._cur.getAncestorNamespaces();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void release() {
            this._cur.release();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        int kind() {
            if (this._txt == null) {
                return this._cur.kind();
            }
            return 0;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        QName getName() {
            if ($assertionsDisabled || this._txt == null) {
                return this._cur.getName();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getXmlnsPrefix() {
            if ($assertionsDisabled || this._txt == null) {
                return this._cur.getXmlnsPrefix();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getXmlnsUri() {
            if ($assertionsDisabled || this._txt == null) {
                return this._cur.getXmlnsUri();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean isXmlns() {
            if (this._txt == null) {
                return this._cur.isXmlns();
            }
            return false;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean hasChildren() {
            if (this._txt == null) {
                return this._cur.hasChildren();
            }
            return false;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean hasText() {
            if (this._txt == null) {
                return this._cur.hasText();
            }
            return false;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean isTextCData() {
            return this._txt == null ? this._useCDataBookmarks && this._cur.isTextCData() : this._isTextCData;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean toFirstAttr() {
            if ($assertionsDisabled || this._txt == null) {
                return this._cur.toFirstAttr();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean toNextAttr() {
            if ($assertionsDisabled || this._txt == null) {
                return this._cur.toNextAttr();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        String getAttrValue() {
            if ($assertionsDisabled || this._txt == null) {
                return this._cur.getAttrValue();
            }
            throw new AssertionError();
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void toEnd() {
            if (!$assertionsDisabled && this._txt != null) {
                throw new AssertionError();
            }
            this._cur.toEnd();
            if (this._cur.kind() == -2) {
                this._depth--;
            }
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        boolean next() {
            int k;
            if (this._txt != null) {
                if (!$assertionsDisabled && this._txt.length() <= 0) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && this._cur.isText()) {
                    throw new AssertionError();
                }
                this._txt = null;
                this._isTextCData = false;
                k = this._cur.kind();
            } else {
                int prevKind = this._cur.kind();
                if (!this._cur.next()) {
                    return false;
                }
                this._sb.delete(0, this._sb.length());
                if (!$assertionsDisabled && this._txt != null) {
                    throw new AssertionError();
                }
                if (this._cur.isText()) {
                    this._isTextCData = this._useCDataBookmarks && this._cur.isTextCData();
                    CharUtil.getString(this._sb, this._cur.getChars(), this._cur._offSrc, this._cur._cchSrc);
                    this._cur.next();
                    trim(this._sb);
                }
                k = this._cur.kind();
                if (this._prettyIndent >= 0 && prevKind != 4 && prevKind != 5 && (prevKind != 2 || k != -2)) {
                    if (this._sb.length() > 0) {
                        this._sb.insert(0, Saver._newLine);
                        spaces(this._sb, Saver._newLine.length(), this._prettyOffset + (this._prettyIndent * this._depth));
                    }
                    if (k != -1) {
                        if (prevKind != 1) {
                            this._sb.append(Saver._newLine);
                        }
                        int d = k < 0 ? this._depth - 1 : this._depth;
                        spaces(this._sb, this._sb.length(), this._prettyOffset + (this._prettyIndent * d));
                    }
                }
                if (this._sb.length() > 0) {
                    this._txt = this._sb.toString();
                    k = 0;
                }
            }
            if (k == 2) {
                this._depth++;
                return true;
            }
            if (k == -2) {
                this._depth--;
                return true;
            }
            return true;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void push() {
            this._cur.push();
            this._stack.add(this._txt);
            this._stack.add(new Integer(this._depth));
            this._isTextCData = false;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        void pop() {
            this._cur.pop();
            this._depth = ((Integer) this._stack.remove(this._stack.size() - 1)).intValue();
            this._txt = (String) this._stack.remove(this._stack.size() - 1);
            this._isTextCData = false;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        Object getChars() {
            if (this._txt != null) {
                this._offSrc = 0;
                this._cchSrc = this._txt.length();
                return this._txt;
            }
            Object o = this._cur.getChars();
            this._offSrc = this._cur._offSrc;
            this._cchSrc = this._cur._cchSrc;
            return o;
        }

        @Override // org.apache.xmlbeans.impl.store.Saver.SaveCur
        XmlDocumentProperties getDocProps() {
            return this._cur.getDocProps();
        }

        static void spaces(StringBuffer sb, int offset, int count) {
            while (true) {
                int i = count;
                count--;
                if (i > 0) {
                    sb.insert(offset, ' ');
                } else {
                    return;
                }
            }
        }

        static void trim(StringBuffer sb) {
            int i = 0;
            while (i < sb.length() && CharUtil.isWhiteSpace(sb.charAt(i))) {
                i++;
            }
            sb.delete(0, i);
            int i2 = sb.length();
            while (i2 > 0 && CharUtil.isWhiteSpace(sb.charAt(i2 - 1))) {
                i2--;
            }
            sb.delete(i2, sb.length());
        }
    }
}
