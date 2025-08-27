package org.apache.xmlbeans.impl.schema;

import java.util.ArrayList;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.xb.xsdschema.Annotated;
import org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaAnnotationImpl.class */
public class SchemaAnnotationImpl implements SchemaAnnotation {
    private SchemaContainer _container;
    private String[] _appInfoAsXml;
    private AppinfoDocument.Appinfo[] _appInfo;
    private String[] _documentationAsXml;
    private DocumentationDocument.Documentation[] _documentation;
    private SchemaAnnotation.Attribute[] _attributes;
    private String _filename;

    public void setFilename(String filename) {
        this._filename = filename;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public String getSourceName() {
        return this._filename;
    }

    @Override // org.apache.xmlbeans.SchemaAnnotation
    public XmlObject[] getApplicationInformation() {
        if (this._appInfo == null) {
            int n = this._appInfoAsXml.length;
            this._appInfo = new AppinfoDocument.Appinfo[n];
            for (int i = 0; i < n; i++) {
                String appInfo = this._appInfoAsXml[i];
                try {
                    this._appInfo[i] = AppinfoDocument.Factory.parse(appInfo).getAppinfo();
                } catch (XmlException e) {
                    this._appInfo[i] = AppinfoDocument.Factory.newInstance().getAppinfo();
                }
            }
        }
        return this._appInfo;
    }

    @Override // org.apache.xmlbeans.SchemaAnnotation
    public XmlObject[] getUserInformation() {
        if (this._documentation == null) {
            int n = this._documentationAsXml.length;
            this._documentation = new DocumentationDocument.Documentation[n];
            for (int i = 0; i < n; i++) {
                String doc = this._documentationAsXml[i];
                try {
                    this._documentation[i] = DocumentationDocument.Factory.parse(doc).getDocumentation();
                } catch (XmlException e) {
                    this._documentation[i] = DocumentationDocument.Factory.newInstance().getDocumentation();
                }
            }
        }
        return this._documentation;
    }

    @Override // org.apache.xmlbeans.SchemaAnnotation
    public SchemaAnnotation.Attribute[] getAttributes() {
        return this._attributes;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public int getComponentType() {
        return 8;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public SchemaTypeSystem getTypeSystem() {
        if (this._container != null) {
            return this._container.getTypeSystem();
        }
        return null;
    }

    SchemaContainer getContainer() {
        return this._container;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public QName getName() {
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaComponent
    public SchemaComponent.Ref getComponentRef() {
        return null;
    }

    public static SchemaAnnotationImpl getAnnotation(SchemaContainer c, Annotated elem) {
        AnnotationDocument.Annotation ann = elem.getAnnotation();
        return getAnnotation(c, elem, ann);
    }

    public static SchemaAnnotationImpl getAnnotation(SchemaContainer c, XmlObject elem, AnnotationDocument.Annotation ann) {
        if (StscState.get().noAnn()) {
            return null;
        }
        SchemaAnnotationImpl result = new SchemaAnnotationImpl(c);
        ArrayList attrArray = new ArrayList(2);
        addNoSchemaAttributes(elem, attrArray);
        if (ann == null) {
            if (attrArray.size() == 0) {
                return null;
            }
            result._appInfo = new AppinfoDocument.Appinfo[0];
            result._documentation = new DocumentationDocument.Documentation[0];
        } else {
            result._appInfo = ann.getAppinfoArray();
            result._documentation = ann.getDocumentationArray();
            addNoSchemaAttributes(ann, attrArray);
        }
        result._attributes = (AttributeImpl[]) attrArray.toArray(new AttributeImpl[attrArray.size()]);
        return result;
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x000f */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void addNoSchemaAttributes(org.apache.xmlbeans.XmlObject r7, java.util.List r8) {
        /*
            r0 = r7
            org.apache.xmlbeans.XmlCursor r0 = r0.newCursor()
            r9 = r0
            r0 = r9
            boolean r0 = r0.toFirstAttribute()
            r10 = r0
        Le:
            r0 = r10
            if (r0 == 0) goto L9c
            r0 = r9
            javax.xml.namespace.QName r0 = r0.getName()
            r11 = r0
            r0 = r11
            java.lang.String r0 = r0.getNamespaceURI()
            r12 = r0
            java.lang.String r0 = ""
            r1 = r12
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L92
            java.lang.String r0 = "http://www.w3.org/2001/XMLSchema"
            r1 = r12
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L38
            goto L92
        L38:
            r0 = r9
            java.lang.String r0 = r0.getTextValue()
            r13 = r0
            r0 = r13
            r1 = 58
            int r0 = r0.indexOf(r1)
            if (r0 <= 0) goto L5c
            r0 = r13
            r1 = 0
            r2 = r13
            r3 = 58
            int r2 = r2.indexOf(r3)
            java.lang.String r0 = r0.substring(r1, r2)
            r15 = r0
            goto L60
        L5c:
            java.lang.String r0 = ""
            r15 = r0
        L60:
            r0 = r9
            r0.push()
            r0 = r9
            boolean r0 = r0.toParent()
            r0 = r9
            r1 = r15
            java.lang.String r0 = r0.namespaceForPrefix(r1)
            r14 = r0
            r0 = r9
            boolean r0 = r0.pop()
            r0 = r8
            org.apache.xmlbeans.impl.schema.SchemaAnnotationImpl$AttributeImpl r1 = new org.apache.xmlbeans.impl.schema.SchemaAnnotationImpl$AttributeImpl
            r2 = r1
            r3 = r11
            r4 = r13
            r5 = r14
            r2.<init>(r3, r4, r5)
            boolean r0 = r0.add(r1)
        L92:
            r0 = r9
            boolean r0 = r0.toNextAttribute()
            r10 = r0
            goto Le
        L9c:
            r0 = r9
            r0.dispose()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.schema.SchemaAnnotationImpl.addNoSchemaAttributes(org.apache.xmlbeans.XmlObject, java.util.List):void");
    }

    private SchemaAnnotationImpl(SchemaContainer c) {
        this._container = c;
    }

    SchemaAnnotationImpl(SchemaContainer c, String[] aapStrings, String[] adocStrings, SchemaAnnotation.Attribute[] aat) {
        this._container = c;
        this._appInfoAsXml = aapStrings;
        this._documentationAsXml = adocStrings;
        this._attributes = aat;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaAnnotationImpl$AttributeImpl.class */
    static class AttributeImpl implements SchemaAnnotation.Attribute {
        private QName _name;
        private String _value;
        private String _valueUri;

        AttributeImpl(QName name, String value, String valueUri) {
            this._name = name;
            this._value = value;
            this._valueUri = valueUri;
        }

        @Override // org.apache.xmlbeans.SchemaAnnotation.Attribute
        public QName getName() {
            return this._name;
        }

        @Override // org.apache.xmlbeans.SchemaAnnotation.Attribute
        public String getValue() {
            return this._value;
        }

        @Override // org.apache.xmlbeans.SchemaAnnotation.Attribute
        public String getValueUri() {
            return this._valueUri;
        }
    }
}
