package org.apache.xmlbeans.impl.schema;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaModelGroup;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlObject;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaModelGroupImpl.class */
public class SchemaModelGroupImpl implements SchemaModelGroup {
    private SchemaContainer _container;
    private QName _name;
    private XmlObject _parseObject;
    private Object _userData;
    private String _parseTNS;
    private boolean _chameleon;
    private String _elemFormDefault;
    private String _attFormDefault;
    private boolean _redefinition;
    private SchemaAnnotation _annotation;
    private String _filename;
    private SchemaModelGroup.Ref _selfref = new SchemaModelGroup.Ref(this);
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SchemaModelGroupImpl.class.desiredAssertionStatus();
    }

    public SchemaModelGroupImpl(SchemaContainer container) {
        this._container = container;
    }

    public SchemaModelGroupImpl(SchemaContainer container, QName name) {
        this._container = container;
        this._name = name;
    }

    public void init(QName name, String targetNamespace, boolean chameleon, String elemFormDefault, String attFormDefault, boolean redefinition, XmlObject x, SchemaAnnotation a, Object userData) {
        if (!$assertionsDisabled && this._name != null && !name.equals(this._name)) {
            throw new AssertionError();
        }
        this._name = name;
        this._parseTNS = targetNamespace;
        this._chameleon = chameleon;
        this._elemFormDefault = elemFormDefault;
        this._attFormDefault = attFormDefault;
        this._redefinition = redefinition;
        this._parseObject = x;
        this._annotation = a;
        this._userData = userData;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public SchemaTypeSystem getTypeSystem() {
        return this._container.getTypeSystem();
    }

    SchemaContainer getContainer() {
        return this._container;
    }

    @Override // org.apache.xmlbeans.SchemaModelGroup, org.apache.xmlbeans.SchemaComponent
    public int getComponentType() {
        return 6;
    }

    public void setFilename(String filename) {
        this._filename = filename;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public String getSourceName() {
        return this._filename;
    }

    @Override // org.apache.xmlbeans.SchemaModelGroup, org.apache.xmlbeans.SchemaComponent
    public QName getName() {
        return this._name;
    }

    public XmlObject getParseObject() {
        return this._parseObject;
    }

    public String getTargetNamespace() {
        return this._parseTNS;
    }

    public String getChameleonNamespace() {
        if (this._chameleon) {
            return this._parseTNS;
        }
        return null;
    }

    public String getElemFormDefault() {
        return this._elemFormDefault;
    }

    public String getAttFormDefault() {
        return this._attFormDefault;
    }

    public boolean isRedefinition() {
        return this._redefinition;
    }

    @Override // org.apache.xmlbeans.SchemaAnnotated
    public SchemaAnnotation getAnnotation() {
        return this._annotation;
    }

    public SchemaModelGroup.Ref getRef() {
        return this._selfref;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public SchemaComponent.Ref getComponentRef() {
        return getRef();
    }

    @Override // org.apache.xmlbeans.SchemaModelGroup
    public Object getUserData() {
        return this._userData;
    }
}
