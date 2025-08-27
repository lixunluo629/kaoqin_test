package org.apache.xmlbeans.impl.schema;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.QNameSetBuilder;
import org.apache.xmlbeans.SchemaAttributeModel;
import org.apache.xmlbeans.SchemaBookmark;
import org.apache.xmlbeans.SchemaGlobalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlNonNegativeInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlPositiveInteger;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.XMLChar;
import org.apache.xmlbeans.impl.common.XPath;
import org.apache.xmlbeans.impl.regex.RegularExpression;
import org.apache.xmlbeans.impl.schema.StscImporter;
import org.apache.xmlbeans.impl.values.NamespaceContext;
import org.apache.xmlbeans.impl.values.XmlNonNegativeIntegerImpl;
import org.apache.xmlbeans.impl.values.XmlPositiveIntegerImpl;
import org.apache.xmlbeans.impl.values.XmlValueOutOfRangeException;
import org.apache.xmlbeans.impl.xb.xsdschema.Annotated;
import org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.Element;
import org.apache.xmlbeans.impl.xb.xsdschema.FieldDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.FormChoice;
import org.apache.xmlbeans.impl.xb.xsdschema.Keybase;
import org.apache.xmlbeans.impl.xb.xsdschema.KeyrefDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalElement;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedAttributeGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelAttribute;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;
import org.apache.xmlbeans.soap.SOAPArrayType;
import org.springframework.validation.DefaultBindingErrorProcessor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscTranslator.class */
public class StscTranslator {
    private static final QName WSDL_ARRAYTYPE_NAME;
    private static final String FORM_QUALIFIED = "qualified";
    public static final RegularExpression XPATH_REGEXP;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StscTranslator.class.desiredAssertionStatus();
        WSDL_ARRAYTYPE_NAME = QNameHelper.forLNS(SoapEncSchemaTypeSystem.ARRAY_TYPE, "http://schemas.xmlsoap.org/wsdl/");
        XPATH_REGEXP = new RegularExpression("(\\.//)?((((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.)/)*((((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.)|((attribute::|@)((\\i\\c*:)?(\\i\\c*|\\*))))(\\|(\\.//)?((((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.)/)*((((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.)|((attribute::|@)((\\i\\c*:)?(\\i\\c*|\\*)))))*", "X");
    }

    public static void addAllDefinitions(StscImporter.SchemaToProcess[] schemasAndChameleons) {
        List redefinitions = new ArrayList();
        for (int i = 0; i < schemasAndChameleons.length; i++) {
            List redefines = schemasAndChameleons[i].getRedefines();
            if (redefines != null) {
                List redefineObjects = schemasAndChameleons[i].getRedefineObjects();
                Iterator it = redefines.iterator();
                Iterator ito = redefineObjects.iterator();
                while (it.hasNext()) {
                    if (!$assertionsDisabled && !ito.hasNext()) {
                        throw new AssertionError("The array of redefines and redefine objects have to have the same length");
                    }
                    redefinitions.add(new RedefinitionHolder((StscImporter.SchemaToProcess) it.next(), (RedefineDocument.Redefine) ito.next()));
                }
            }
        }
        RedefinitionMaster globalRedefinitions = new RedefinitionMaster((RedefinitionHolder[]) redefinitions.toArray(new RedefinitionHolder[redefinitions.size()]));
        StscState state = StscState.get();
        for (int j = 0; j < schemasAndChameleons.length; j++) {
            SchemaDocument.Schema schema = schemasAndChameleons[j].getSchema();
            String givenTargetNamespace = schemasAndChameleons[j].getChameleonNamespace();
            if (schema.sizeOfNotationArray() > 0) {
                state.warning("Schema <notation> is not yet supported for this release.", 51, schema.getNotationArray(0));
            }
            String targetNamespace = schema.getTargetNamespace();
            boolean chameleon = false;
            if (givenTargetNamespace != null && targetNamespace == null) {
                targetNamespace = givenTargetNamespace;
                chameleon = true;
            }
            if (targetNamespace == null) {
                targetNamespace = "";
            }
            if (targetNamespace.length() > 0 || !isEmptySchema(schema)) {
                state.registerContribution(targetNamespace, schema.documentProperties().getSourceName());
                state.addNewContainer(targetNamespace);
            }
            List redefChain = new ArrayList();
            TopLevelComplexType[] complexTypes = schema.getComplexTypeArray();
            for (int i2 = 0; i2 < complexTypes.length; i2++) {
                TopLevelComplexType type = complexTypes[i2];
                RedefinitionHolder[] rhArray = globalRedefinitions.getComplexTypeRedefinitions(type.getName(), schemasAndChameleons[j]);
                for (int k = 0; k < rhArray.length; k++) {
                    if (rhArray[k] != null) {
                        TopLevelComplexType redef = rhArray[k].redefineComplexType(type.getName());
                        if (!$assertionsDisabled && redef == null) {
                            throw new AssertionError();
                        }
                        redefChain.add(type);
                        type = redef;
                    }
                }
                SchemaTypeImpl t = translateGlobalComplexType(type, targetNamespace, chameleon, redefChain.size() > 0);
                state.addGlobalType(t, null);
                int k2 = redefChain.size() - 1;
                while (k2 >= 0) {
                    SchemaTypeImpl r = translateGlobalComplexType((TopLevelComplexType) redefChain.remove(k2), targetNamespace, chameleon, k2 > 0);
                    state.addGlobalType(r, t);
                    t = r;
                    k2--;
                }
            }
            TopLevelSimpleType[] simpleTypes = schema.getSimpleTypeArray();
            for (int i3 = 0; i3 < simpleTypes.length; i3++) {
                TopLevelSimpleType type2 = simpleTypes[i3];
                RedefinitionHolder[] rhArray2 = globalRedefinitions.getSimpleTypeRedefinitions(type2.getName(), schemasAndChameleons[j]);
                for (int k3 = 0; k3 < rhArray2.length; k3++) {
                    if (rhArray2[k3] != null) {
                        TopLevelSimpleType redef2 = rhArray2[k3].redefineSimpleType(type2.getName());
                        if (!$assertionsDisabled && redef2 == null) {
                            throw new AssertionError();
                        }
                        redefChain.add(type2);
                        type2 = redef2;
                    }
                }
                SchemaTypeImpl t2 = translateGlobalSimpleType(type2, targetNamespace, chameleon, redefChain.size() > 0);
                state.addGlobalType(t2, null);
                int k4 = redefChain.size() - 1;
                while (k4 >= 0) {
                    SchemaTypeImpl r2 = translateGlobalSimpleType((TopLevelSimpleType) redefChain.remove(k4), targetNamespace, chameleon, k4 > 0);
                    state.addGlobalType(r2, t2);
                    t2 = r2;
                    k4--;
                }
            }
            TopLevelElement[] elements = schema.getElementArray();
            for (TopLevelElement element : elements) {
                state.addDocumentType(translateDocumentType(element, targetNamespace, chameleon), QNameHelper.forLNS(element.getName(), targetNamespace));
            }
            TopLevelAttribute[] attributes = schema.getAttributeArray();
            for (TopLevelAttribute attribute : attributes) {
                state.addAttributeType(translateAttributeType(attribute, targetNamespace, chameleon), QNameHelper.forLNS(attribute.getName(), targetNamespace));
            }
            NamedGroup[] modelgroups = schema.getGroupArray();
            for (int i4 = 0; i4 < modelgroups.length; i4++) {
                NamedGroup group = modelgroups[i4];
                RedefinitionHolder[] rhArray3 = globalRedefinitions.getModelGroupRedefinitions(group.getName(), schemasAndChameleons[j]);
                for (int k5 = 0; k5 < rhArray3.length; k5++) {
                    if (rhArray3[k5] != null) {
                        NamedGroup redef3 = rhArray3[k5].redefineModelGroup(group.getName());
                        if (!$assertionsDisabled && redef3 == null) {
                            throw new AssertionError();
                        }
                        redefChain.add(group);
                        group = redef3;
                    }
                }
                SchemaModelGroupImpl g = translateModelGroup(group, targetNamespace, chameleon, redefChain.size() > 0);
                state.addModelGroup(g, null);
                int k6 = redefChain.size() - 1;
                while (k6 >= 0) {
                    SchemaModelGroupImpl r3 = translateModelGroup((NamedGroup) redefChain.remove(k6), targetNamespace, chameleon, k6 > 0);
                    state.addModelGroup(r3, g);
                    g = r3;
                    k6--;
                }
            }
            NamedAttributeGroup[] attrgroups = schema.getAttributeGroupArray();
            for (int i5 = 0; i5 < attrgroups.length; i5++) {
                NamedAttributeGroup group2 = attrgroups[i5];
                RedefinitionHolder[] rhArray4 = globalRedefinitions.getAttributeGroupRedefinitions(group2.getName(), schemasAndChameleons[j]);
                for (int k7 = 0; k7 < rhArray4.length; k7++) {
                    if (rhArray4[k7] != null) {
                        NamedAttributeGroup redef4 = rhArray4[k7].redefineAttributeGroup(group2.getName());
                        if (!$assertionsDisabled && redef4 == null) {
                            throw new AssertionError();
                        }
                        redefChain.add(group2);
                        group2 = redef4;
                    }
                }
                SchemaAttributeGroupImpl g2 = translateAttributeGroup(group2, targetNamespace, chameleon, redefChain.size() > 0);
                state.addAttributeGroup(g2, null);
                int k8 = redefChain.size() - 1;
                while (k8 >= 0) {
                    SchemaAttributeGroupImpl r4 = translateAttributeGroup((NamedAttributeGroup) redefChain.remove(k8), targetNamespace, chameleon, k8 > 0);
                    state.addAttributeGroup(r4, g2);
                    g2 = r4;
                    k8--;
                }
            }
            AnnotationDocument.Annotation[] annotations = schema.getAnnotationArray();
            for (AnnotationDocument.Annotation annotation : annotations) {
                state.addAnnotation(SchemaAnnotationImpl.getAnnotation(state.getContainer(targetNamespace), schema, annotation), targetNamespace);
            }
        }
        for (int i6 = 0; i6 < redefinitions.size(); i6++) {
            ((RedefinitionHolder) redefinitions.get(i6)).complainAboutMissingDefinitions();
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscTranslator$RedefinitionHolder.class */
    private static class RedefinitionHolder {
        private Map stRedefinitions;
        private Map ctRedefinitions;
        private Map agRedefinitions;
        private Map mgRedefinitions;
        private String schemaLocation;
        private StscImporter.SchemaToProcess schemaRedefined;

        RedefinitionHolder(StscImporter.SchemaToProcess schemaToProcess, RedefineDocument.Redefine redefine) {
            this.stRedefinitions = Collections.EMPTY_MAP;
            this.ctRedefinitions = Collections.EMPTY_MAP;
            this.agRedefinitions = Collections.EMPTY_MAP;
            this.mgRedefinitions = Collections.EMPTY_MAP;
            this.schemaLocation = "";
            this.schemaRedefined = schemaToProcess;
            if (redefine != null) {
                StscState state = StscState.get();
                this.stRedefinitions = new HashMap();
                this.ctRedefinitions = new HashMap();
                this.agRedefinitions = new HashMap();
                this.mgRedefinitions = new HashMap();
                if (redefine.getSchemaLocation() != null) {
                    this.schemaLocation = redefine.getSchemaLocation();
                }
                TopLevelComplexType[] complexTypes = redefine.getComplexTypeArray();
                for (int i = 0; i < complexTypes.length; i++) {
                    if (complexTypes[i].getName() != null) {
                        if (this.ctRedefinitions.containsKey(complexTypes[i].getName())) {
                            state.error("Duplicate type redefinition: " + complexTypes[i].getName(), 49, (XmlObject) null);
                        } else {
                            this.ctRedefinitions.put(complexTypes[i].getName(), complexTypes[i]);
                        }
                    }
                }
                TopLevelSimpleType[] simpleTypes = redefine.getSimpleTypeArray();
                for (int i2 = 0; i2 < simpleTypes.length; i2++) {
                    if (simpleTypes[i2].getName() != null) {
                        if (this.stRedefinitions.containsKey(simpleTypes[i2].getName())) {
                            state.error("Duplicate type redefinition: " + simpleTypes[i2].getName(), 49, (XmlObject) null);
                        } else {
                            this.stRedefinitions.put(simpleTypes[i2].getName(), simpleTypes[i2]);
                        }
                    }
                }
                NamedGroup[] modelgroups = redefine.getGroupArray();
                for (int i3 = 0; i3 < modelgroups.length; i3++) {
                    if (modelgroups[i3].getName() != null) {
                        if (this.mgRedefinitions.containsKey(modelgroups[i3].getName())) {
                            state.error("Duplicate type redefinition: " + modelgroups[i3].getName(), 49, (XmlObject) null);
                        } else {
                            this.mgRedefinitions.put(modelgroups[i3].getName(), modelgroups[i3]);
                        }
                    }
                }
                NamedAttributeGroup[] attrgroups = redefine.getAttributeGroupArray();
                for (int i4 = 0; i4 < attrgroups.length; i4++) {
                    if (attrgroups[i4].getName() != null) {
                        if (this.agRedefinitions.containsKey(attrgroups[i4].getName())) {
                            state.error("Duplicate type redefinition: " + attrgroups[i4].getName(), 49, (XmlObject) null);
                        } else {
                            this.agRedefinitions.put(attrgroups[i4].getName(), attrgroups[i4]);
                        }
                    }
                }
            }
        }

        public TopLevelSimpleType redefineSimpleType(String name) {
            if (name == null || !this.stRedefinitions.containsKey(name)) {
                return null;
            }
            return (TopLevelSimpleType) this.stRedefinitions.remove(name);
        }

        public TopLevelComplexType redefineComplexType(String name) {
            if (name == null || !this.ctRedefinitions.containsKey(name)) {
                return null;
            }
            return (TopLevelComplexType) this.ctRedefinitions.remove(name);
        }

        public NamedGroup redefineModelGroup(String name) {
            if (name == null || !this.mgRedefinitions.containsKey(name)) {
                return null;
            }
            return (NamedGroup) this.mgRedefinitions.remove(name);
        }

        public NamedAttributeGroup redefineAttributeGroup(String name) {
            if (name == null || !this.agRedefinitions.containsKey(name)) {
                return null;
            }
            return (NamedAttributeGroup) this.agRedefinitions.remove(name);
        }

        public void complainAboutMissingDefinitions() {
            if (this.stRedefinitions.isEmpty() && this.ctRedefinitions.isEmpty() && this.agRedefinitions.isEmpty() && this.mgRedefinitions.isEmpty()) {
                return;
            }
            StscState state = StscState.get();
            for (String name : this.stRedefinitions.keySet()) {
                state.error("Redefined simple type " + name + " not found in " + this.schemaLocation, 60, (XmlObject) this.stRedefinitions.get(name));
            }
            for (String name2 : this.ctRedefinitions.keySet()) {
                state.error("Redefined complex type " + name2 + " not found in " + this.schemaLocation, 60, (XmlObject) this.ctRedefinitions.get(name2));
            }
            for (String name3 : this.agRedefinitions.keySet()) {
                state.error("Redefined attribute group " + name3 + " not found in " + this.schemaLocation, 60, (XmlObject) this.agRedefinitions.get(name3));
            }
            for (String name4 : this.mgRedefinitions.keySet()) {
                state.error("Redefined model group " + name4 + " not found in " + this.schemaLocation, 60, (XmlObject) this.mgRedefinitions.get(name4));
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscTranslator$RedefinitionMaster.class */
    private static class RedefinitionMaster {
        private Map stRedefinitions;
        private Map ctRedefinitions;
        private Map agRedefinitions;
        private Map mgRedefinitions;
        private static final RedefinitionHolder[] EMPTY_REDEFINTION_HOLDER_ARRAY;
        private static final short SIMPLE_TYPE = 1;
        private static final short COMPLEX_TYPE = 2;
        private static final short MODEL_GROUP = 3;
        private static final short ATTRIBUTE_GROUP = 4;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !StscTranslator.class.desiredAssertionStatus();
            EMPTY_REDEFINTION_HOLDER_ARRAY = new RedefinitionHolder[0];
        }

        RedefinitionMaster(RedefinitionHolder[] redefHolders) {
            this.stRedefinitions = Collections.EMPTY_MAP;
            this.ctRedefinitions = Collections.EMPTY_MAP;
            this.agRedefinitions = Collections.EMPTY_MAP;
            this.mgRedefinitions = Collections.EMPTY_MAP;
            if (redefHolders.length > 0) {
                this.stRedefinitions = new HashMap();
                this.ctRedefinitions = new HashMap();
                this.agRedefinitions = new HashMap();
                this.mgRedefinitions = new HashMap();
                for (int i = 0; i < redefHolders.length; i++) {
                    RedefinitionHolder redefHolder = redefHolders[i];
                    for (Object key : redefHolder.stRedefinitions.keySet()) {
                        List redefinedIn = (List) this.stRedefinitions.get(key);
                        if (redefinedIn == null) {
                            redefinedIn = new ArrayList();
                            this.stRedefinitions.put(key, redefinedIn);
                        }
                        redefinedIn.add(redefHolders[i]);
                    }
                    for (Object key2 : redefHolder.ctRedefinitions.keySet()) {
                        List redefinedIn2 = (List) this.ctRedefinitions.get(key2);
                        if (redefinedIn2 == null) {
                            redefinedIn2 = new ArrayList();
                            this.ctRedefinitions.put(key2, redefinedIn2);
                        }
                        redefinedIn2.add(redefHolders[i]);
                    }
                    for (Object key3 : redefHolder.agRedefinitions.keySet()) {
                        List redefinedIn3 = (List) this.agRedefinitions.get(key3);
                        if (redefinedIn3 == null) {
                            redefinedIn3 = new ArrayList();
                            this.agRedefinitions.put(key3, redefinedIn3);
                        }
                        redefinedIn3.add(redefHolders[i]);
                    }
                    for (Object key4 : redefHolder.mgRedefinitions.keySet()) {
                        List redefinedIn4 = (List) this.mgRedefinitions.get(key4);
                        if (redefinedIn4 == null) {
                            redefinedIn4 = new ArrayList();
                            this.mgRedefinitions.put(key4, redefinedIn4);
                        }
                        redefinedIn4.add(redefHolders[i]);
                    }
                }
            }
        }

        RedefinitionHolder[] getSimpleTypeRedefinitions(String name, StscImporter.SchemaToProcess schema) {
            List redefines = (List) this.stRedefinitions.get(name);
            if (redefines == null) {
                return EMPTY_REDEFINTION_HOLDER_ARRAY;
            }
            return doTopologicalSort(redefines, schema, name, (short) 1);
        }

        RedefinitionHolder[] getComplexTypeRedefinitions(String name, StscImporter.SchemaToProcess schema) {
            List redefines = (List) this.ctRedefinitions.get(name);
            if (redefines == null) {
                return EMPTY_REDEFINTION_HOLDER_ARRAY;
            }
            return doTopologicalSort(redefines, schema, name, (short) 2);
        }

        RedefinitionHolder[] getAttributeGroupRedefinitions(String name, StscImporter.SchemaToProcess schema) {
            List redefines = (List) this.agRedefinitions.get(name);
            if (redefines == null) {
                return EMPTY_REDEFINTION_HOLDER_ARRAY;
            }
            return doTopologicalSort(redefines, schema, name, (short) 4);
        }

        RedefinitionHolder[] getModelGroupRedefinitions(String name, StscImporter.SchemaToProcess schema) {
            List redefines = (List) this.mgRedefinitions.get(name);
            if (redefines == null) {
                return EMPTY_REDEFINTION_HOLDER_ARRAY;
            }
            return doTopologicalSort(redefines, schema, name, (short) 3);
        }

        private RedefinitionHolder[] doTopologicalSort(List genericRedefines, StscImporter.SchemaToProcess schema, String name, short componentType) {
            RedefinitionHolder[] specificRedefines = new RedefinitionHolder[genericRedefines.size()];
            int n = 0;
            for (int i = 0; i < genericRedefines.size(); i++) {
                RedefinitionHolder h = (RedefinitionHolder) genericRedefines.get(i);
                if (h.schemaRedefined == schema || h.schemaRedefined.indirectIncludes(schema)) {
                    int i2 = n;
                    n++;
                    specificRedefines[i2] = h;
                }
            }
            RedefinitionHolder[] sortedRedefines = new RedefinitionHolder[n];
            int[] numberOfIncludes = new int[n];
            for (int i3 = 0; i3 < n - 1; i3++) {
                RedefinitionHolder current = specificRedefines[i3];
                for (int j = i3 + 1; j < n; j++) {
                    if (current.schemaRedefined.indirectIncludes(specificRedefines[j].schemaRedefined)) {
                        int i4 = i3;
                        numberOfIncludes[i4] = numberOfIncludes[i4] + 1;
                    }
                    if (specificRedefines[j].schemaRedefined.indirectIncludes(current.schemaRedefined)) {
                        int i5 = j;
                        numberOfIncludes[i5] = numberOfIncludes[i5] + 1;
                    }
                }
            }
            int position = 0;
            boolean errorReported = false;
            while (position < n) {
                int index = -1;
                for (int i6 = 0; i6 < numberOfIncludes.length; i6++) {
                    if (numberOfIncludes[i6] == 0 && index < 0) {
                        index = i6;
                    }
                }
                if (index < 0) {
                    if (!errorReported) {
                        StringBuffer fileNameList = new StringBuffer();
                        XmlObject location = null;
                        for (int i7 = 0; i7 < n; i7++) {
                            if (specificRedefines[i7] != null) {
                                fileNameList.append(specificRedefines[i7].schemaLocation).append(',').append(' ');
                                if (location == null) {
                                    location = locationFromRedefinitionAndCode(specificRedefines[i7], name, componentType);
                                }
                            }
                        }
                        StscState.get().error("Detected circular redefinition of " + componentNameFromCode(componentType) + " \"" + name + "\"; Files involved: " + fileNameList.toString(), 60, location);
                        errorReported = true;
                    }
                    int min = n;
                    for (int i8 = 0; i8 < n; i8++) {
                        if (numberOfIncludes[i8] > 0 && numberOfIncludes[i8] < min) {
                            min = numberOfIncludes[i8];
                            index = i8;
                        }
                    }
                    int i9 = index;
                    numberOfIncludes[i9] = numberOfIncludes[i9] - 1;
                } else {
                    if (!$assertionsDisabled && specificRedefines[index] == null) {
                        throw new AssertionError();
                    }
                    int i10 = position;
                    position++;
                    sortedRedefines[i10] = specificRedefines[index];
                    for (int i11 = 0; i11 < n; i11++) {
                        if (specificRedefines[i11] != null && specificRedefines[i11].schemaRedefined.indirectIncludes(specificRedefines[index].schemaRedefined)) {
                            int i12 = i11;
                            numberOfIncludes[i12] = numberOfIncludes[i12] - 1;
                        }
                    }
                    specificRedefines[index] = null;
                    int i13 = index;
                    numberOfIncludes[i13] = numberOfIncludes[i13] - 1;
                }
            }
            for (int i14 = 1; i14 < n; i14++) {
                int j2 = i14 - 1;
                while (j2 >= 0 && sortedRedefines[j2] == null) {
                    j2--;
                }
                if (!sortedRedefines[i14].schemaRedefined.indirectIncludes(sortedRedefines[j2].schemaRedefined)) {
                    StscState.get().error("Detected multiple redefinitions of " + componentNameFromCode(componentType) + " \"" + name + "\"; Files involved: " + sortedRedefines[j2].schemaRedefined.getSourceName() + ", " + sortedRedefines[i14].schemaRedefined.getSourceName(), 49, locationFromRedefinitionAndCode(sortedRedefines[i14], name, componentType));
                    switch (componentType) {
                        case 1:
                            sortedRedefines[i14].redefineSimpleType(name);
                            break;
                        case 2:
                            sortedRedefines[i14].redefineComplexType(name);
                            break;
                        case 3:
                            sortedRedefines[i14].redefineModelGroup(name);
                            break;
                        case 4:
                            sortedRedefines[i14].redefineAttributeGroup(name);
                            break;
                    }
                    sortedRedefines[i14] = null;
                }
            }
            return sortedRedefines;
        }

        private String componentNameFromCode(short code) {
            String componentName;
            switch (code) {
                case 1:
                    componentName = "simple type";
                    break;
                case 2:
                    componentName = "complex type";
                    break;
                case 3:
                    componentName = "model group";
                    break;
                case 4:
                    componentName = "attribute group";
                    break;
                default:
                    componentName = "";
                    break;
            }
            return componentName;
        }

        private XmlObject locationFromRedefinitionAndCode(RedefinitionHolder redefinition, String name, short code) {
            XmlObject location;
            switch (code) {
                case 1:
                    location = (XmlObject) redefinition.stRedefinitions.get(name);
                    break;
                case 2:
                    location = (XmlObject) redefinition.ctRedefinitions.get(name);
                    break;
                case 3:
                    location = (XmlObject) redefinition.mgRedefinitions.get(name);
                    break;
                case 4:
                    location = (XmlObject) redefinition.agRedefinitions.get(name);
                    break;
                default:
                    location = null;
                    break;
            }
            return location;
        }
    }

    private static String findFilename(XmlObject xobj) {
        return StscState.get().sourceNameForUri(xobj.documentProperties().getSourceName());
    }

    private static SchemaTypeImpl translateDocumentType(TopLevelElement xsdType, String targetNamespace, boolean chameleon) {
        SchemaTypeImpl sType = new SchemaTypeImpl(StscState.get().getContainer(targetNamespace));
        sType.setDocumentType(true);
        sType.setParseContext(xsdType, targetNamespace, chameleon, null, null, false);
        sType.setFilename(findFilename(xsdType));
        return sType;
    }

    private static SchemaTypeImpl translateAttributeType(TopLevelAttribute xsdType, String targetNamespace, boolean chameleon) {
        SchemaTypeImpl sType = new SchemaTypeImpl(StscState.get().getContainer(targetNamespace));
        sType.setAttributeType(true);
        sType.setParseContext(xsdType, targetNamespace, chameleon, null, null, false);
        sType.setFilename(findFilename(xsdType));
        return sType;
    }

    private static SchemaTypeImpl translateGlobalComplexType(TopLevelComplexType xsdType, String targetNamespace, boolean chameleon, boolean redefinition) {
        StscState state = StscState.get();
        String localname = xsdType.getName();
        if (localname == null) {
            state.error(XmlErrorCodes.MISSING_NAME, new Object[]{"global type"}, xsdType);
            return null;
        }
        if (!XMLChar.isValidNCName(localname)) {
            state.error(XmlErrorCodes.INVALID_VALUE, new Object[]{localname, "name"}, xsdType.xgetName());
        }
        QName name = QNameHelper.forLNS(localname, targetNamespace);
        if (isReservedTypeName(name)) {
            state.warning(XmlErrorCodes.RESERVED_TYPE_NAME, new Object[]{QNameHelper.pretty(name)}, xsdType);
            return null;
        }
        SchemaTypeImpl sType = new SchemaTypeImpl(state.getContainer(targetNamespace));
        sType.setParseContext(xsdType, targetNamespace, chameleon, null, null, redefinition);
        sType.setFilename(findFilename(xsdType));
        sType.setName(QNameHelper.forLNS(localname, targetNamespace));
        sType.setAnnotation(SchemaAnnotationImpl.getAnnotation(state.getContainer(targetNamespace), xsdType));
        sType.setUserData(getUserData(xsdType));
        return sType;
    }

    private static SchemaTypeImpl translateGlobalSimpleType(TopLevelSimpleType xsdType, String targetNamespace, boolean chameleon, boolean redefinition) {
        StscState state = StscState.get();
        String localname = xsdType.getName();
        if (localname == null) {
            state.error(XmlErrorCodes.MISSING_NAME, new Object[]{"global type"}, xsdType);
            return null;
        }
        if (!XMLChar.isValidNCName(localname)) {
            state.error(XmlErrorCodes.INVALID_VALUE, new Object[]{localname, "name"}, xsdType.xgetName());
        }
        QName name = QNameHelper.forLNS(localname, targetNamespace);
        if (isReservedTypeName(name)) {
            state.warning(XmlErrorCodes.RESERVED_TYPE_NAME, new Object[]{QNameHelper.pretty(name)}, xsdType);
            return null;
        }
        SchemaTypeImpl sType = new SchemaTypeImpl(state.getContainer(targetNamespace));
        sType.setSimpleType(true);
        sType.setParseContext(xsdType, targetNamespace, chameleon, null, null, redefinition);
        sType.setFilename(findFilename(xsdType));
        sType.setName(name);
        sType.setAnnotation(SchemaAnnotationImpl.getAnnotation(state.getContainer(targetNamespace), xsdType));
        sType.setUserData(getUserData(xsdType));
        return sType;
    }

    static SchemaTypeImpl translateAnonymousSimpleType(SimpleType typedef, String targetNamespace, boolean chameleon, String elemFormDefault, String attFormDefault, List anonymousTypes, SchemaType outerType) {
        StscState state = StscState.get();
        SchemaTypeImpl sType = new SchemaTypeImpl(state.getContainer(targetNamespace));
        sType.setSimpleType(true);
        sType.setParseContext(typedef, targetNamespace, chameleon, elemFormDefault, attFormDefault, false);
        sType.setOuterSchemaTypeRef(outerType.getRef());
        sType.setAnnotation(SchemaAnnotationImpl.getAnnotation(state.getContainer(targetNamespace), typedef));
        sType.setUserData(getUserData(typedef));
        anonymousTypes.add(sType);
        return sType;
    }

    static FormChoice findElementFormDefault(XmlObject obj) {
        XmlCursor cur = obj.newCursor();
        while (cur.getObject().schemaType() != SchemaDocument.Schema.type) {
            if (!cur.toParent()) {
                return null;
            }
        }
        return ((SchemaDocument.Schema) cur.getObject()).xgetElementFormDefault();
    }

    public static boolean uriMatch(String s1, String s2) {
        if (s1 == null) {
            return s2 == null || s2.equals("");
        }
        if (s2 == null) {
            return s1.equals("");
        }
        return s1.equals(s2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void copyGlobalElementToLocalElement(SchemaGlobalElement schemaGlobalElement, SchemaLocalElementImpl target) {
        target.setNameAndTypeRef(schemaGlobalElement.getName(), schemaGlobalElement.getType().getRef());
        target.setNillable(schemaGlobalElement.isNillable());
        target.setDefault(schemaGlobalElement.getDefaultText(), schemaGlobalElement.isFixed(), ((SchemaGlobalElementImpl) schemaGlobalElement).getParseObject());
        target.setIdentityConstraints(((SchemaLocalElementImpl) schemaGlobalElement).getIdentityConstraintRefs());
        target.setBlock(schemaGlobalElement.blockExtension(), schemaGlobalElement.blockRestriction(), schemaGlobalElement.blockSubstitution());
        target.setAbstract(schemaGlobalElement.isAbstract());
        target.setTransitionRules(((SchemaParticle) schemaGlobalElement).acceptedStartNames(), ((SchemaParticle) schemaGlobalElement).isSkippable());
        target.setAnnotation(schemaGlobalElement.getAnnotation());
    }

    public static void copyGlobalAttributeToLocalAttribute(SchemaGlobalAttributeImpl referenced, SchemaLocalAttributeImpl target) {
        target.init(referenced.getName(), referenced.getTypeRef(), referenced.getUse(), referenced.getDefaultText(), referenced.getParseObject(), referenced._defaultValue, referenced.isFixed(), referenced.getWSDLArrayType(), referenced.getAnnotation(), null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v183, types: [org.apache.xmlbeans.SchemaType] */
    public static SchemaLocalElementImpl translateElement(Element xsdElt, String targetNamespace, boolean chameleon, String elemFormDefault, String attFormDefault, List anonymousTypes, SchemaType outerType) {
        SchemaLocalElementImpl impl;
        QName qname;
        boolean qualified;
        StscState state = StscState.get();
        SchemaTypeImpl sgHead = null;
        if (xsdElt.isSetSubstitutionGroup()) {
            sgHead = state.findDocumentType(xsdElt.getSubstitutionGroup(), ((SchemaTypeImpl) outerType).getChameleonNamespace(), targetNamespace);
            if (sgHead != null) {
                StscResolver.resolveType(sgHead);
            }
        }
        String name = xsdElt.getName();
        QName ref = xsdElt.getRef();
        if (ref != null && name != null) {
            state.error(XmlErrorCodes.SCHEMA_ELEM$REF_OR_NAME_HAS_BOTH, new Object[]{name}, xsdElt.xgetRef());
            name = null;
        }
        if (ref == null && name == null) {
            state.error(XmlErrorCodes.SCHEMA_ELEM$REF_OR_NAME_HAS_NEITHER, (Object[]) null, xsdElt);
            return null;
        }
        if (name != null && !XMLChar.isValidNCName(name)) {
            state.error(XmlErrorCodes.INVALID_VALUE, new Object[]{name, "name"}, xsdElt.xgetName());
        }
        if (ref != null) {
            if (xsdElt.getType() != null) {
                state.error(XmlErrorCodes.SCHEMA_ELEM$REF_FEATURES, new Object[]{"type"}, xsdElt.xgetType());
            }
            if (xsdElt.getSimpleType() != null) {
                state.error(XmlErrorCodes.SCHEMA_ELEM$REF_FEATURES, new Object[]{"<simpleType>"}, xsdElt.getSimpleType());
            }
            if (xsdElt.getComplexType() != null) {
                state.error(XmlErrorCodes.SCHEMA_ELEM$REF_FEATURES, new Object[]{"<complexType>"}, xsdElt.getComplexType());
            }
            if (xsdElt.getForm() != null) {
                state.error(XmlErrorCodes.SCHEMA_ELEM$REF_FEATURES, new Object[]{"form"}, xsdElt.xgetForm());
            }
            if (xsdElt.sizeOfKeyArray() > 0) {
                state.warning(XmlErrorCodes.SCHEMA_ELEM$REF_FEATURES, new Object[]{"<key>"}, xsdElt);
            }
            if (xsdElt.sizeOfKeyrefArray() > 0) {
                state.warning(XmlErrorCodes.SCHEMA_ELEM$REF_FEATURES, new Object[]{"<keyref>"}, xsdElt);
            }
            if (xsdElt.sizeOfUniqueArray() > 0) {
                state.warning(XmlErrorCodes.SCHEMA_ELEM$REF_FEATURES, new Object[]{"<unique>"}, xsdElt);
            }
            if (xsdElt.isSetDefault()) {
                state.warning(XmlErrorCodes.SCHEMA_ELEM$REF_FEATURES, new Object[]{"default"}, xsdElt.xgetDefault());
            }
            if (xsdElt.isSetFixed()) {
                state.warning(XmlErrorCodes.SCHEMA_ELEM$REF_FEATURES, new Object[]{"fixed"}, xsdElt.xgetFixed());
            }
            if (xsdElt.isSetBlock()) {
                state.warning(XmlErrorCodes.SCHEMA_ELEM$REF_FEATURES, new Object[]{"block"}, xsdElt.xgetBlock());
            }
            if (xsdElt.isSetNillable()) {
                state.warning(XmlErrorCodes.SCHEMA_ELEM$REF_FEATURES, new Object[]{"nillable"}, xsdElt.xgetNillable());
            }
            if (!$assertionsDisabled && !(xsdElt instanceof LocalElement)) {
                throw new AssertionError();
            }
            SchemaGlobalElement referenced = state.findGlobalElement(ref, chameleon ? targetNamespace : null, targetNamespace);
            if (referenced == null) {
                state.notFoundError(ref, 1, xsdElt.xgetRef(), true);
                return null;
            }
            SchemaLocalElementImpl target = new SchemaLocalElementImpl();
            target.setParticleType(4);
            target.setUserData(getUserData(xsdElt));
            copyGlobalElementToLocalElement(referenced, target);
            return target;
        }
        SchemaTypeImpl type = null;
        if (xsdElt instanceof LocalElement) {
            impl = new SchemaLocalElementImpl();
            FormChoice form = xsdElt.xgetForm();
            if (form != null) {
                qualified = form.getStringValue().equals(FORM_QUALIFIED);
            } else if (elemFormDefault != null) {
                qualified = elemFormDefault.equals(FORM_QUALIFIED);
            } else {
                FormChoice form2 = findElementFormDefault(xsdElt);
                qualified = form2 != null && form2.getStringValue().equals(FORM_QUALIFIED);
            }
            qname = qualified ? QNameHelper.forLNS(name, targetNamespace) : QNameHelper.forLN(name);
        } else {
            SchemaGlobalElementImpl gelt = new SchemaGlobalElementImpl(state.getContainer(targetNamespace));
            impl = gelt;
            if (sgHead != null) {
                SchemaGlobalElementImpl head = state.findGlobalElement(xsdElt.getSubstitutionGroup(), chameleon ? targetNamespace : null, targetNamespace);
                if (head != null) {
                    gelt.setSubstitutionGroup(head.getRef());
                }
            }
            qname = QNameHelper.forLNS(name, targetNamespace);
            SchemaTypeImpl docType = (SchemaTypeImpl) outerType;
            QName[] sgMembers = docType.getSubstitutionGroupMembers();
            QNameSetBuilder transitionRules = new QNameSetBuilder();
            transitionRules.add(qname);
            for (int i = 0; i < sgMembers.length; i++) {
                gelt.addSubstitutionGroupMember(sgMembers[i]);
                transitionRules.add(sgMembers[i]);
            }
            impl.setTransitionRules(QNameSet.forSpecification(transitionRules), false);
            impl.setTransitionNotes(QNameSet.EMPTY, true);
            boolean finalExt = false;
            boolean finalRest = false;
            Object ds = xsdElt.getFinal();
            if (ds != null) {
                if ((ds instanceof String) && ds.equals("#all")) {
                    finalRest = true;
                    finalExt = true;
                } else if (ds instanceof List) {
                    if (((List) ds).contains("extension")) {
                        finalExt = true;
                    }
                    if (((List) ds).contains("restriction")) {
                        finalRest = true;
                    }
                }
            }
            gelt.setFinal(finalExt, finalRest);
            gelt.setAbstract(xsdElt.getAbstract());
            gelt.setFilename(findFilename(xsdElt));
            gelt.setParseContext(xsdElt, targetNamespace, chameleon);
        }
        SchemaAnnotationImpl ann = SchemaAnnotationImpl.getAnnotation(state.getContainer(targetNamespace), xsdElt);
        impl.setAnnotation(ann);
        impl.setUserData(getUserData(xsdElt));
        if (xsdElt.getType() != null) {
            type = state.findGlobalType(xsdElt.getType(), chameleon ? targetNamespace : null, targetNamespace);
            if (type == null) {
                state.notFoundError(xsdElt.getType(), 0, xsdElt.xgetType(), true);
            }
        }
        boolean simpleTypedef = false;
        Annotated typedef = xsdElt.getComplexType();
        if (typedef == null) {
            typedef = xsdElt.getSimpleType();
            simpleTypedef = true;
        }
        if (type != null && typedef != null) {
            state.error(XmlErrorCodes.SCHEMA_ELEM$TYPE_ATTR_OR_NESTED_TYPE, (Object[]) null, typedef);
            typedef = null;
        }
        if (typedef != null) {
            Object[] grps = state.getCurrentProcessing();
            QName[] context = new QName[grps.length];
            for (int i2 = 0; i2 < context.length; i2++) {
                if (grps[i2] instanceof SchemaModelGroupImpl) {
                    context[i2] = ((SchemaModelGroupImpl) grps[i2]).getName();
                }
            }
            ?? CheckRecursiveGroupReference = checkRecursiveGroupReference(context, qname, (SchemaTypeImpl) outerType);
            if (CheckRecursiveGroupReference != 0) {
                type = CheckRecursiveGroupReference;
            } else {
                SchemaTypeImpl sTypeImpl = new SchemaTypeImpl(state.getContainer(targetNamespace));
                type = sTypeImpl;
                sTypeImpl.setContainerField(impl);
                sTypeImpl.setOuterSchemaTypeRef(outerType == null ? null : outerType.getRef());
                sTypeImpl.setGroupReferenceContext(context);
                anonymousTypes.add(type);
                sTypeImpl.setSimpleType(simpleTypedef);
                sTypeImpl.setParseContext(typedef, targetNamespace, chameleon, elemFormDefault, attFormDefault, false);
                sTypeImpl.setAnnotation(SchemaAnnotationImpl.getAnnotation(state.getContainer(targetNamespace), typedef));
                sTypeImpl.setUserData(getUserData(typedef));
            }
        }
        if (type == null && sgHead != null) {
            SchemaGlobalElement head2 = state.findGlobalElement(xsdElt.getSubstitutionGroup(), chameleon ? targetNamespace : null, targetNamespace);
            if (head2 != null) {
                type = head2.getType();
            }
        }
        if (type == null) {
            type = BuiltinSchemaTypeSystem.ST_ANY_TYPE;
        }
        SOAPArrayType wat = null;
        XmlCursor c = xsdElt.newCursor();
        String arrayType = c.getAttributeText(WSDL_ARRAYTYPE_NAME);
        c.dispose();
        if (arrayType != null) {
            try {
                wat = new SOAPArrayType(arrayType, new NamespaceContext(xsdElt));
            } catch (XmlValueOutOfRangeException e) {
                state.error(XmlErrorCodes.SOAPARRAY, new Object[]{arrayType}, xsdElt);
            }
        }
        impl.setWsdlArrayType(wat);
        boolean isFixed = xsdElt.isSetFixed();
        if (xsdElt.isSetDefault() && isFixed) {
            state.error(XmlErrorCodes.SCHEMA_ELEM$DEFAULT_OR_FIXED, (Object[]) null, xsdElt.xgetFixed());
            isFixed = false;
        }
        impl.setParticleType(4);
        impl.setNameAndTypeRef(qname, type.getRef());
        impl.setNillable(xsdElt.getNillable());
        impl.setDefault(isFixed ? xsdElt.getFixed() : xsdElt.getDefault(), isFixed, xsdElt);
        Object block = xsdElt.getBlock();
        boolean blockExt = false;
        boolean blockRest = false;
        boolean blockSubst = false;
        if (block != null) {
            if ((block instanceof String) && block.equals("#all")) {
                blockSubst = true;
                blockRest = true;
                blockExt = true;
            } else if (block instanceof List) {
                if (((List) block).contains("extension")) {
                    blockExt = true;
                }
                if (((List) block).contains("restriction")) {
                    blockRest = true;
                }
                if (((List) block).contains("substitution")) {
                    blockSubst = true;
                }
            }
        }
        impl.setBlock(blockExt, blockRest, blockSubst);
        boolean constraintFailed = false;
        int length = xsdElt.sizeOfKeyArray() + xsdElt.sizeOfKeyrefArray() + xsdElt.sizeOfUniqueArray();
        SchemaIdentityConstraintImpl[] constraints = new SchemaIdentityConstraintImpl[length];
        int cur = 0;
        Keybase[] keys = xsdElt.getKeyArray();
        int i3 = 0;
        while (i3 < keys.length) {
            constraints[cur] = translateIdentityConstraint(keys[i3], targetNamespace, chameleon);
            if (constraints[cur] != null) {
                constraints[cur].setConstraintCategory(1);
            } else {
                constraintFailed = true;
            }
            i3++;
            cur++;
        }
        Keybase[] uc = xsdElt.getUniqueArray();
        int i4 = 0;
        while (i4 < uc.length) {
            constraints[cur] = translateIdentityConstraint(uc[i4], targetNamespace, chameleon);
            if (constraints[cur] != null) {
                constraints[cur].setConstraintCategory(3);
            } else {
                constraintFailed = true;
            }
            i4++;
            cur++;
        }
        KeyrefDocument.Keyref[] krs = xsdElt.getKeyrefArray();
        int i5 = 0;
        while (i5 < krs.length) {
            constraints[cur] = translateIdentityConstraint(krs[i5], targetNamespace, chameleon);
            if (constraints[cur] != null) {
                constraints[cur].setConstraintCategory(2);
            } else {
                constraintFailed = true;
            }
            i5++;
            cur++;
        }
        if (!constraintFailed) {
            SchemaIdentityConstraint.Ref[] refs = new SchemaIdentityConstraint.Ref[length];
            for (int i6 = 0; i6 < refs.length; i6++) {
                refs[i6] = constraints[i6].getRef();
            }
            impl.setIdentityConstraints(refs);
        }
        return impl;
    }

    private static SchemaType checkRecursiveGroupReference(QName[] context, QName containingElement, SchemaTypeImpl outerType) {
        QName[] outerContext;
        if (context.length < 1) {
            return null;
        }
        SchemaTypeImpl schemaTypeImpl = outerType;
        while (true) {
            SchemaTypeImpl type = schemaTypeImpl;
            if (type == null || type.getName() != null || type.isDocumentType()) {
                return null;
            }
            if (containingElement.equals(type.getContainerField().getName()) && (outerContext = type.getGroupReferenceContext()) != null && outerContext.length == context.length) {
                boolean equal = true;
                for (int i = 0; i < context.length; i++) {
                    if ((context[i] != null || outerContext[i] != null) && (context[i] == null || !context[i].equals(outerContext[i]))) {
                        equal = false;
                        break;
                    }
                }
                if (equal) {
                    return type;
                }
            }
            schemaTypeImpl = (SchemaTypeImpl) type.getOuterType();
        }
    }

    private static String removeWhitespace(String xpath) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < xpath.length(); i++) {
            char ch2 = xpath.charAt(i);
            if (!XMLChar.isSpace(ch2)) {
                sb.append(ch2);
            }
        }
        return sb.toString();
    }

    private static boolean checkXPathSyntax(String xpath) {
        boolean zMatches;
        if (xpath == null) {
            return false;
        }
        String xpath2 = removeWhitespace(xpath);
        synchronized (XPATH_REGEXP) {
            zMatches = XPATH_REGEXP.matches(xpath2);
        }
        return zMatches;
    }

    private static SchemaIdentityConstraintImpl translateIdentityConstraint(Keybase parseIC, String targetNamespace, boolean chameleon) {
        StscState state = StscState.get();
        String selector = parseIC.getSelector() == null ? null : parseIC.getSelector().getXpath();
        if (!checkXPathSyntax(selector)) {
            state.error(XmlErrorCodes.SELECTOR_XPATH, new Object[]{selector}, parseIC.getSelector().xgetXpath());
            return null;
        }
        FieldDocument.Field[] fieldElts = parseIC.getFieldArray();
        for (int j = 0; j < fieldElts.length; j++) {
            if (!checkXPathSyntax(fieldElts[j].getXpath())) {
                state.error(XmlErrorCodes.FIELDS_XPATH, new Object[]{fieldElts[j].getXpath()}, fieldElts[j].xgetXpath());
                return null;
            }
        }
        SchemaIdentityConstraintImpl ic = new SchemaIdentityConstraintImpl(state.getContainer(targetNamespace));
        ic.setName(QNameHelper.forLNS(parseIC.getName(), targetNamespace));
        ic.setSelector(parseIC.getSelector().getXpath());
        ic.setParseContext(parseIC, targetNamespace, chameleon);
        SchemaAnnotationImpl ann = SchemaAnnotationImpl.getAnnotation(state.getContainer(targetNamespace), parseIC);
        ic.setAnnotation(ann);
        ic.setUserData(getUserData(parseIC));
        XmlCursor c = parseIC.newCursor();
        Map nsMap = new HashMap();
        c.getAllNamespaces(nsMap);
        nsMap.remove("");
        ic.setNSMap(nsMap);
        c.dispose();
        String[] fields = new String[fieldElts.length];
        for (int j2 = 0; j2 < fields.length; j2++) {
            fields[j2] = fieldElts[j2].getXpath();
        }
        ic.setFields(fields);
        try {
            ic.buildPaths();
            state.addIdConstraint(ic);
            ic.setFilename(findFilename(parseIC));
            return state.findIdConstraint(ic.getName(), targetNamespace, null);
        } catch (XPath.XPathCompileException e) {
            state.error(XmlErrorCodes.INVALID_XPATH, new Object[]{e.getMessage()}, parseIC);
            return null;
        }
    }

    public static SchemaModelGroupImpl translateModelGroup(NamedGroup namedGroup, String targetNamespace, boolean chameleon, boolean redefinition) {
        String name = namedGroup.getName();
        if (name == null) {
            StscState.get().error(XmlErrorCodes.MISSING_NAME, new Object[]{"model group"}, namedGroup);
            return null;
        }
        SchemaContainer c = StscState.get().getContainer(targetNamespace);
        SchemaModelGroupImpl result = new SchemaModelGroupImpl(c);
        SchemaAnnotationImpl ann = SchemaAnnotationImpl.getAnnotation(c, namedGroup);
        FormChoice elemFormDefault = findElementFormDefault(namedGroup);
        FormChoice attFormDefault = findAttributeFormDefault(namedGroup);
        result.init(QNameHelper.forLNS(name, targetNamespace), targetNamespace, chameleon, elemFormDefault == null ? null : elemFormDefault.getStringValue(), attFormDefault == null ? null : attFormDefault.getStringValue(), redefinition, namedGroup, ann, getUserData(namedGroup));
        result.setFilename(findFilename(namedGroup));
        return result;
    }

    public static SchemaAttributeGroupImpl translateAttributeGroup(AttributeGroup attrGroup, String targetNamespace, boolean chameleon, boolean redefinition) {
        String name = attrGroup.getName();
        if (name == null) {
            StscState.get().error(XmlErrorCodes.MISSING_NAME, new Object[]{"attribute group"}, attrGroup);
            return null;
        }
        SchemaContainer c = StscState.get().getContainer(targetNamespace);
        SchemaAttributeGroupImpl result = new SchemaAttributeGroupImpl(c);
        SchemaAnnotationImpl ann = SchemaAnnotationImpl.getAnnotation(c, attrGroup);
        FormChoice formDefault = findAttributeFormDefault(attrGroup);
        result.init(QNameHelper.forLNS(name, targetNamespace), targetNamespace, chameleon, formDefault == null ? null : formDefault.getStringValue(), redefinition, attrGroup, ann, getUserData(attrGroup));
        result.setFilename(findFilename(attrGroup));
        return result;
    }

    static FormChoice findAttributeFormDefault(XmlObject obj) {
        XmlCursor cur = obj.newCursor();
        while (cur.getObject().schemaType() != SchemaDocument.Schema.type) {
            if (!cur.toParent()) {
                return null;
            }
        }
        return ((SchemaDocument.Schema) cur.getObject()).xgetAttributeFormDefault();
    }

    static SchemaLocalAttributeImpl translateAttribute(Attribute xsdAttr, String targetNamespace, String formDefault, boolean chameleon, List anonymousTypes, SchemaType outerType, SchemaAttributeModel baseModel, boolean local) {
        SchemaLocalAttributeImpl sAttr;
        QName qname;
        boolean qualified;
        StscState state = StscState.get();
        String name = xsdAttr.getName();
        QName ref = xsdAttr.getRef();
        if (ref != null && name != null) {
            if (name.equals(ref.getLocalPart()) && uriMatch(targetNamespace, ref.getNamespaceURI())) {
                state.warning(XmlErrorCodes.SCHEMA_ATTR$REF_OR_NAME_HAS_BOTH, new Object[]{name}, xsdAttr.xgetRef());
            } else {
                state.error(XmlErrorCodes.SCHEMA_ATTR$REF_OR_NAME_HAS_BOTH, new Object[]{name}, xsdAttr.xgetRef());
            }
            name = null;
        }
        if (ref == null && name == null) {
            state.error(XmlErrorCodes.SCHEMA_ATTR$REF_OR_NAME_HAS_NEITHER, (Object[]) null, xsdAttr);
            return null;
        }
        if (name != null && !XMLChar.isValidNCName(name)) {
            state.error(XmlErrorCodes.INVALID_VALUE, new Object[]{name, "name"}, xsdAttr.xgetName());
        }
        boolean isFixed = false;
        String deftext = null;
        String fmrfixedtext = null;
        SchemaType sType = null;
        int use = 2;
        if (local) {
            sAttr = new SchemaLocalAttributeImpl();
        } else {
            sAttr = new SchemaGlobalAttributeImpl(StscState.get().getContainer(targetNamespace));
            ((SchemaGlobalAttributeImpl) sAttr).setParseContext(xsdAttr, targetNamespace, chameleon);
        }
        if (ref != null) {
            if (xsdAttr.getType() != null) {
                state.error(XmlErrorCodes.SCHEMA_ATTR$REF_FEATURES, new Object[]{"type"}, xsdAttr.xgetType());
            }
            if (xsdAttr.getSimpleType() != null) {
                state.error(XmlErrorCodes.SCHEMA_ATTR$REF_FEATURES, new Object[]{"<simpleType>"}, xsdAttr.getSimpleType());
            }
            if (xsdAttr.getForm() != null) {
                state.error(XmlErrorCodes.SCHEMA_ATTR$REF_FEATURES, new Object[]{"form"}, xsdAttr.xgetForm());
            }
            SchemaGlobalAttribute referenced = state.findGlobalAttribute(ref, chameleon ? targetNamespace : null, targetNamespace);
            if (referenced == null) {
                state.notFoundError(ref, 3, xsdAttr.xgetRef(), true);
                return null;
            }
            qname = ref;
            use = referenced.getUse();
            sType = referenced.getType();
            deftext = referenced.getDefaultText();
            if (deftext != null) {
                isFixed = referenced.isFixed();
                if (isFixed) {
                    fmrfixedtext = deftext;
                }
            }
        } else {
            if (local) {
                FormChoice form = xsdAttr.xgetForm();
                if (form != null) {
                    qualified = form.getStringValue().equals(FORM_QUALIFIED);
                } else if (formDefault != null) {
                    qualified = formDefault.equals(FORM_QUALIFIED);
                } else {
                    FormChoice form2 = findAttributeFormDefault(xsdAttr);
                    qualified = form2 != null && form2.getStringValue().equals(FORM_QUALIFIED);
                }
                qname = qualified ? QNameHelper.forLNS(name, targetNamespace) : QNameHelper.forLN(name);
            } else {
                qname = QNameHelper.forLNS(name, targetNamespace);
            }
            if (xsdAttr.getType() != null) {
                sType = state.findGlobalType(xsdAttr.getType(), chameleon ? targetNamespace : null, targetNamespace);
                if (sType == null) {
                    state.notFoundError(xsdAttr.getType(), 0, xsdAttr.xgetType(), true);
                }
            }
            if (qname.getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema-instance")) {
                state.error(XmlErrorCodes.NO_XSI, new Object[]{"http://www.w3.org/2001/XMLSchema-instance"}, xsdAttr.xgetName());
            }
            if (qname.getNamespaceURI().length() == 0 && qname.getLocalPart().equals("xmlns")) {
                state.error(XmlErrorCodes.NO_XMLNS, (Object[]) null, xsdAttr.xgetName());
            }
            LocalSimpleType typedef = xsdAttr.getSimpleType();
            if (sType != null && typedef != null) {
                state.error(XmlErrorCodes.SCHEMA_ATTR$TYPE_ATTR_OR_NESTED_TYPE, (Object[]) null, typedef);
                typedef = null;
            }
            if (typedef != null) {
                SchemaTypeImpl sTypeImpl = new SchemaTypeImpl(state.getContainer(targetNamespace));
                sType = sTypeImpl;
                sTypeImpl.setContainerField(sAttr);
                sTypeImpl.setOuterSchemaTypeRef(outerType == null ? null : outerType.getRef());
                anonymousTypes.add(sType);
                sTypeImpl.setSimpleType(true);
                sTypeImpl.setParseContext(typedef, targetNamespace, chameleon, null, null, false);
                sTypeImpl.setAnnotation(SchemaAnnotationImpl.getAnnotation(state.getContainer(targetNamespace), typedef));
                sTypeImpl.setUserData(getUserData(typedef));
            }
            if (sType == null && baseModel != null && baseModel.getAttribute(qname) != null) {
                sType = baseModel.getAttribute(qname).getType();
            }
        }
        if (sType == null) {
            sType = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
        }
        if (!sType.isSimpleType()) {
            state.error("Attributes must have a simple type (not complex).", 46, xsdAttr);
            sType = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
        }
        if (xsdAttr.isSetUse()) {
            use = translateUseCode(xsdAttr.xgetUse());
            if (use != 2 && !isFixed) {
                deftext = null;
            }
        }
        if (xsdAttr.isSetDefault() || xsdAttr.isSetFixed()) {
            if (isFixed && !xsdAttr.isSetFixed()) {
                state.error("A use of a fixed attribute definition must also be fixed", 9, xsdAttr.xgetFixed());
            }
            isFixed = xsdAttr.isSetFixed();
            if (xsdAttr.isSetDefault() && isFixed) {
                state.error(XmlErrorCodes.SCHEMA_ATTR$DEFAULT_OR_FIXED, (Object[]) null, xsdAttr.xgetFixed());
                isFixed = false;
            }
            deftext = isFixed ? xsdAttr.getFixed() : xsdAttr.getDefault();
            if (fmrfixedtext != null && !fmrfixedtext.equals(deftext)) {
                state.error(XmlErrorCodes.SCHEMA_ATTR$FIXED_NOT_MATCH, (Object[]) null, xsdAttr.xgetFixed());
                deftext = fmrfixedtext;
            }
        }
        if (!local) {
            ((SchemaGlobalAttributeImpl) sAttr).setFilename(findFilename(xsdAttr));
        }
        SOAPArrayType wat = null;
        XmlCursor c = xsdAttr.newCursor();
        String arrayType = c.getAttributeText(WSDL_ARRAYTYPE_NAME);
        c.dispose();
        if (arrayType != null) {
            try {
                wat = new SOAPArrayType(arrayType, new NamespaceContext(xsdAttr));
            } catch (XmlValueOutOfRangeException e) {
                state.error(XmlErrorCodes.SOAPARRAY, new Object[]{arrayType}, xsdAttr);
            }
        }
        SchemaAnnotationImpl ann = SchemaAnnotationImpl.getAnnotation(state.getContainer(targetNamespace), xsdAttr);
        sAttr.init(qname, sType.getRef(), use, deftext, xsdAttr, null, isFixed, wat, ann, getUserData(xsdAttr));
        return sAttr;
    }

    static int translateUseCode(Attribute.Use attruse) {
        if (attruse == null) {
            return 2;
        }
        String val = attruse.getStringValue();
        if (val.equals("optional")) {
            return 2;
        }
        if (val.equals(DefaultBindingErrorProcessor.MISSING_FIELD_ERROR_CODE)) {
            return 3;
        }
        if (val.equals("prohibited")) {
            return 1;
        }
        return 2;
    }

    static BigInteger buildBigInt(XmlAnySimpleType value) {
        if (value == null) {
            return null;
        }
        String text = value.getStringValue();
        try {
            BigInteger bigInt = new BigInteger(text);
            if (bigInt.signum() < 0) {
                StscState.get().error(XmlErrorCodes.INVALID_VALUE, new Object[]{text, "nonNegativeInteger"}, value);
                return null;
            }
            return bigInt;
        } catch (NumberFormatException e) {
            StscState.get().error(XmlErrorCodes.INVALID_VALUE_DETAIL, new Object[]{text, "nonNegativeInteger", e.getMessage()}, value);
            return null;
        }
    }

    static XmlNonNegativeInteger buildNnInteger(XmlAnySimpleType value) {
        BigInteger bigInt = buildBigInt(value);
        try {
            XmlNonNegativeIntegerImpl i = new XmlNonNegativeIntegerImpl();
            i.set(bigInt);
            i.setImmutable();
            return i;
        } catch (XmlValueOutOfRangeException e) {
            StscState.get().error("Internal error processing number", 21, value);
            return null;
        }
    }

    static XmlPositiveInteger buildPosInteger(XmlAnySimpleType value) {
        BigInteger bigInt = buildBigInt(value);
        try {
            XmlPositiveIntegerImpl i = new XmlPositiveIntegerImpl();
            i.set(bigInt);
            i.setImmutable();
            return i;
        } catch (XmlValueOutOfRangeException e) {
            StscState.get().error("Internal error processing number", 21, value);
            return null;
        }
    }

    private static Object getUserData(XmlObject pos) {
        XmlCursor.XmlBookmark b = pos.newCursor().getBookmark(SchemaBookmark.class);
        if (b != null && (b instanceof SchemaBookmark)) {
            return ((SchemaBookmark) b).getValue();
        }
        return null;
    }

    private static boolean isEmptySchema(SchemaDocument.Schema schema) {
        XmlCursor cursor = schema.newCursor();
        boolean result = !cursor.toFirstChild();
        cursor.dispose();
        return result;
    }

    private static boolean isReservedTypeName(QName name) {
        return BuiltinSchemaTypeSystem.get().findType(name) != null;
    }
}
