package org.apache.xmlbeans.impl.schema;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlObject;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaGlobalElementImpl.class */
public class SchemaGlobalElementImpl extends SchemaLocalElementImpl implements SchemaGlobalElement {
    private static final QName[] _namearray = new QName[0];
    private boolean _finalExt;
    private boolean _finalRest;
    private SchemaContainer _container;
    private String _filename;
    private String _parseTNS;
    private boolean _chameleon;
    private SchemaGlobalElement.Ref _sg;
    private Set _sgMembers = new LinkedHashSet();
    private SchemaGlobalElement.Ref _selfref = new SchemaGlobalElement.Ref(this);

    public SchemaGlobalElementImpl(SchemaContainer container) {
        this._container = container;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public SchemaTypeSystem getTypeSystem() {
        return this._container.getTypeSystem();
    }

    SchemaContainer getContainer() {
        return this._container;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public String getSourceName() {
        return this._filename;
    }

    public void setFilename(String filename) {
        this._filename = filename;
    }

    void setFinal(boolean finalExt, boolean finalRest) {
        mutate();
        this._finalExt = finalExt;
        this._finalRest = finalRest;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public int getComponentType() {
        return 1;
    }

    @Override // org.apache.xmlbeans.SchemaGlobalElement
    public SchemaGlobalElement substitutionGroup() {
        if (this._sg == null) {
            return null;
        }
        return this._sg.get();
    }

    public void setSubstitutionGroup(SchemaGlobalElement.Ref sg) {
        this._sg = sg;
    }

    @Override // org.apache.xmlbeans.SchemaGlobalElement
    public QName[] substitutionGroupMembers() {
        return (QName[]) this._sgMembers.toArray(_namearray);
    }

    public void addSubstitutionGroupMember(QName name) {
        mutate();
        this._sgMembers.add(name);
    }

    @Override // org.apache.xmlbeans.SchemaGlobalElement
    public boolean finalExtension() {
        return this._finalExt;
    }

    @Override // org.apache.xmlbeans.SchemaGlobalElement
    public boolean finalRestriction() {
        return this._finalRest;
    }

    public void setParseContext(XmlObject parseObject, String targetNamespace, boolean chameleon) {
        this._parseObject = parseObject;
        this._parseTNS = targetNamespace;
        this._chameleon = chameleon;
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

    @Override // org.apache.xmlbeans.SchemaGlobalElement
    public SchemaGlobalElement.Ref getRef() {
        return this._selfref;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public SchemaComponent.Ref getComponentRef() {
        return getRef();
    }
}
