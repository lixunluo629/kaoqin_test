package org.apache.xmlbeans.impl.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaAttributeGroup;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaModelGroup;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaContainer.class */
class SchemaContainer {
    private String _namespace;
    private SchemaTypeSystem _typeSystem;
    boolean _immutable;
    private List _globalElements = new ArrayList();
    private List _globalAttributes = new ArrayList();
    private List _modelGroups = new ArrayList();
    private List _redefinedModelGroups = new ArrayList();
    private List _attributeGroups = new ArrayList();
    private List _redefinedAttributeGroups = new ArrayList();
    private List _globalTypes = new ArrayList();
    private List _redefinedGlobalTypes = new ArrayList();
    private List _documentTypes = new ArrayList();
    private List _attributeTypes = new ArrayList();
    private List _identityConstraints = new ArrayList();
    private List _annotations = new ArrayList();

    SchemaContainer(String namespace) {
        this._namespace = namespace;
    }

    String getNamespace() {
        return this._namespace;
    }

    synchronized SchemaTypeSystem getTypeSystem() {
        return this._typeSystem;
    }

    synchronized void setTypeSystem(SchemaTypeSystem typeSystem) {
        this._typeSystem = typeSystem;
    }

    synchronized void setImmutable() {
        this._immutable = true;
    }

    synchronized void unsetImmutable() {
        this._immutable = false;
    }

    private void check_immutable() {
        if (this._immutable) {
            throw new IllegalStateException("Cannot add components to immutable SchemaContainer");
        }
    }

    void addGlobalElement(SchemaGlobalElement.Ref e) {
        check_immutable();
        this._globalElements.add(e);
    }

    List globalElements() {
        return getComponentList(this._globalElements);
    }

    void addGlobalAttribute(SchemaGlobalAttribute.Ref a) {
        check_immutable();
        this._globalAttributes.add(a);
    }

    List globalAttributes() {
        return getComponentList(this._globalAttributes);
    }

    void addModelGroup(SchemaModelGroup.Ref g) {
        check_immutable();
        this._modelGroups.add(g);
    }

    List modelGroups() {
        return getComponentList(this._modelGroups);
    }

    void addRedefinedModelGroup(SchemaModelGroup.Ref g) {
        check_immutable();
        this._redefinedModelGroups.add(g);
    }

    List redefinedModelGroups() {
        return getComponentList(this._redefinedModelGroups);
    }

    void addAttributeGroup(SchemaAttributeGroup.Ref g) {
        check_immutable();
        this._attributeGroups.add(g);
    }

    List attributeGroups() {
        return getComponentList(this._attributeGroups);
    }

    void addRedefinedAttributeGroup(SchemaAttributeGroup.Ref g) {
        check_immutable();
        this._redefinedAttributeGroups.add(g);
    }

    List redefinedAttributeGroups() {
        return getComponentList(this._redefinedAttributeGroups);
    }

    void addGlobalType(SchemaType.Ref t) {
        check_immutable();
        this._globalTypes.add(t);
    }

    List globalTypes() {
        return getComponentList(this._globalTypes);
    }

    void addRedefinedType(SchemaType.Ref t) {
        check_immutable();
        this._redefinedGlobalTypes.add(t);
    }

    List redefinedGlobalTypes() {
        return getComponentList(this._redefinedGlobalTypes);
    }

    void addDocumentType(SchemaType.Ref t) {
        check_immutable();
        this._documentTypes.add(t);
    }

    List documentTypes() {
        return getComponentList(this._documentTypes);
    }

    void addAttributeType(SchemaType.Ref t) {
        check_immutable();
        this._attributeTypes.add(t);
    }

    List attributeTypes() {
        return getComponentList(this._attributeTypes);
    }

    void addIdentityConstraint(SchemaIdentityConstraint.Ref c) {
        check_immutable();
        this._identityConstraints.add(c);
    }

    List identityConstraints() {
        return getComponentList(this._identityConstraints);
    }

    void addAnnotation(SchemaAnnotation a) {
        check_immutable();
        this._annotations.add(a);
    }

    List annotations() {
        return Collections.unmodifiableList(this._annotations);
    }

    private List getComponentList(List referenceList) {
        List result = new ArrayList();
        for (int i = 0; i < referenceList.size(); i++) {
            result.add(((SchemaComponent.Ref) referenceList.get(i)).getComponent());
        }
        return Collections.unmodifiableList(result);
    }
}
