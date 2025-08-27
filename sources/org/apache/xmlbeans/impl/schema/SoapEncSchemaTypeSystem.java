package org.apache.xmlbeans.impl.schema;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.Filer;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaAttributeGroup;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaModelGroup;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SoapEncSchemaTypeSystem.class */
public class SoapEncSchemaTypeSystem extends SchemaTypeLoaderBase implements SchemaTypeSystem {
    public static final String SOAPENC = "http://schemas.xmlsoap.org/soap/encoding/";
    public static final String SOAP_ARRAY = "Array";
    public static final String ARRAY_TYPE = "arrayType";
    private static final String ATTR_ID = "id";
    private static final String ATTR_HREF = "href";
    private static final String ATTR_OFFSET = "offset";
    private static final SchemaType[] EMPTY_SCHEMATYPE_ARRAY = new SchemaType[0];
    private static final SchemaGlobalElement[] EMPTY_SCHEMAELEMENT_ARRAY = new SchemaGlobalElement[0];
    private static final SchemaModelGroup[] EMPTY_SCHEMAMODELGROUP_ARRAY = new SchemaModelGroup[0];
    private static final SchemaAttributeGroup[] EMPTY_SCHEMAATTRIBUTEGROUP_ARRAY = new SchemaAttributeGroup[0];
    private static final SchemaAnnotation[] EMPTY_SCHEMAANNOTATION_ARRAY = new SchemaAnnotation[0];
    private static SoapEncSchemaTypeSystem _global = new SoapEncSchemaTypeSystem();
    private SchemaTypeImpl soapArray;
    private SchemaGlobalAttributeImpl arrayType;
    private String soapArrayHandle;
    private Map _handlesToObjects = new HashMap();
    private SchemaContainer _container = new SchemaContainer("http://schemas.xmlsoap.org/soap/encoding/");

    public static SchemaTypeSystem get() {
        return _global;
    }

    private SoapEncSchemaTypeSystem() {
        this._container.setTypeSystem(this);
        this.soapArray = new SchemaTypeImpl(this._container, true);
        this._container.addGlobalType(this.soapArray.getRef());
        this.soapArray.setName(new QName("http://schemas.xmlsoap.org/soap/encoding/", SOAP_ARRAY));
        this.soapArrayHandle = SOAP_ARRAY.toLowerCase() + "type";
        this.soapArray.setComplexTypeVariety(3);
        this.soapArray.setBaseTypeRef(BuiltinSchemaTypeSystem.ST_ANY_TYPE.getRef());
        this.soapArray.setBaseDepth(1);
        this.soapArray.setDerivationType(2);
        this.soapArray.setSimpleTypeVariety(0);
        SchemaParticleImpl contentModel = new SchemaParticleImpl();
        contentModel.setParticleType(3);
        contentModel.setMinOccurs(BigInteger.ZERO);
        contentModel.setMaxOccurs(BigInteger.ONE);
        contentModel.setTransitionRules(QNameSet.ALL, true);
        SchemaParticleImpl[] children = {contentModel2};
        contentModel.setParticleChildren(children);
        SchemaParticleImpl contentModel2 = new SchemaParticleImpl();
        contentModel2.setParticleType(5);
        contentModel2.setWildcardSet(QNameSet.ALL);
        contentModel2.setWildcardProcess(2);
        contentModel2.setMinOccurs(BigInteger.ZERO);
        contentModel2.setMaxOccurs(null);
        contentModel2.setTransitionRules(QNameSet.ALL, true);
        SchemaAttributeModelImpl attrModel = new SchemaAttributeModelImpl();
        attrModel.setWildcardProcess(2);
        HashSet excludedURI = new HashSet();
        excludedURI.add("http://schemas.xmlsoap.org/soap/encoding/");
        attrModel.setWildcardSet(QNameSet.forSets(excludedURI, null, Collections.EMPTY_SET, Collections.EMPTY_SET));
        SchemaLocalAttributeImpl attr = new SchemaLocalAttributeImpl();
        attr.init(new QName("", "id"), BuiltinSchemaTypeSystem.ST_ID.getRef(), 2, null, null, null, false, null, null, null);
        attrModel.addAttribute(attr);
        SchemaLocalAttributeImpl attr2 = new SchemaLocalAttributeImpl();
        attr2.init(new QName("", ATTR_HREF), BuiltinSchemaTypeSystem.ST_ANY_URI.getRef(), 2, null, null, null, false, null, null, null);
        attrModel.addAttribute(attr2);
        SchemaLocalAttributeImpl attr3 = new SchemaLocalAttributeImpl();
        attr3.init(new QName("http://schemas.xmlsoap.org/soap/encoding/", ARRAY_TYPE), BuiltinSchemaTypeSystem.ST_STRING.getRef(), 2, null, null, null, false, null, null, null);
        attrModel.addAttribute(attr3);
        SchemaLocalAttributeImpl attr4 = new SchemaLocalAttributeImpl();
        attr4.init(new QName("http://schemas.xmlsoap.org/soap/encoding/", ATTR_OFFSET), BuiltinSchemaTypeSystem.ST_STRING.getRef(), 2, null, null, null, false, null, null, null);
        attrModel.addAttribute(attr4);
        this.soapArray.setContentModel(contentModel, attrModel, Collections.EMPTY_MAP, Collections.EMPTY_MAP, false);
        this.arrayType = new SchemaGlobalAttributeImpl(this._container);
        this._container.addGlobalAttribute(this.arrayType.getRef());
        this.arrayType.init(new QName("http://schemas.xmlsoap.org/soap/encoding/", ARRAY_TYPE), BuiltinSchemaTypeSystem.ST_STRING.getRef(), 2, null, null, null, false, null, null, null);
        this._handlesToObjects.put(this.soapArrayHandle, this.soapArray);
        this._handlesToObjects.put(ARRAY_TYPE.toLowerCase() + BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT, this.arrayType);
        this._container.setImmutable();
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public String getName() {
        return "schema.typesystem.soapenc.builtin";
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType findType(QName qName) {
        if ("http://schemas.xmlsoap.org/soap/encoding/".equals(qName.getNamespaceURI()) && SOAP_ARRAY.equals(qName.getLocalPart())) {
            return this.soapArray;
        }
        return null;
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType findDocumentType(QName qName) {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType findAttributeType(QName qName) {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalElement findElement(QName qName) {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalAttribute findAttribute(QName qName) {
        if ("http://schemas.xmlsoap.org/soap/encoding/".equals(qName.getNamespaceURI()) && ARRAY_TYPE.equals(qName.getLocalPart())) {
            return this.arrayType;
        }
        return null;
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaModelGroup findModelGroup(QName qName) {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.schema.SchemaTypeLoaderBase, org.apache.xmlbeans.SchemaTypeLoader
    public SchemaAttributeGroup findAttributeGroup(QName qName) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public boolean isNamespaceDefined(String string) {
        return "http://schemas.xmlsoap.org/soap/encoding/".equals(string);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findTypeRef(QName qName) {
        SchemaType type = findType(qName);
        if (type == null) {
            return null;
        }
        return type.getRef();
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findDocumentTypeRef(QName qName) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findAttributeTypeRef(QName qName) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalElement.Ref findElementRef(QName qName) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalAttribute.Ref findAttributeRef(QName qName) {
        SchemaGlobalAttribute attr = findAttribute(qName);
        if (attr == null) {
            return null;
        }
        return attr.getRef();
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaModelGroup.Ref findModelGroupRef(QName qName) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaAttributeGroup.Ref findAttributeGroupRef(QName qName) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaIdentityConstraint.Ref findIdentityConstraintRef(QName qName) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType typeForClassname(String string) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public InputStream getSourceAsStream(String string) {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public ClassLoader getClassLoader() {
        return SoapEncSchemaTypeSystem.class.getClassLoader();
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public void resolve() {
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType[] globalTypes() {
        return new SchemaType[]{this.soapArray};
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType[] documentTypes() {
        return EMPTY_SCHEMATYPE_ARRAY;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType[] attributeTypes() {
        return EMPTY_SCHEMATYPE_ARRAY;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaGlobalElement[] globalElements() {
        return EMPTY_SCHEMAELEMENT_ARRAY;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaGlobalAttribute[] globalAttributes() {
        return new SchemaGlobalAttribute[]{this.arrayType};
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaModelGroup[] modelGroups() {
        return EMPTY_SCHEMAMODELGROUP_ARRAY;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaAttributeGroup[] attributeGroups() {
        return EMPTY_SCHEMAATTRIBUTEGROUP_ARRAY;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaAnnotation[] annotations() {
        return EMPTY_SCHEMAANNOTATION_ARRAY;
    }

    public String handleForType(SchemaType type) {
        if (this.soapArray.equals(type)) {
            return this.soapArrayHandle;
        }
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaComponent resolveHandle(String string) {
        return (SchemaComponent) this._handlesToObjects.get(string);
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType typeForHandle(String string) {
        return (SchemaType) this._handlesToObjects.get(string);
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public void saveToDirectory(File file) {
        throw new UnsupportedOperationException("The builtin soap encoding schema type system cannot be saved.");
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public void save(Filer filer) {
        throw new UnsupportedOperationException("The builtin soap encoding schema type system cannot be saved.");
    }
}
