package org.apache.xmlbeans.impl.tool;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.tool.XSTCTester;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/XMLBeanXSTCHarness.class */
public class XMLBeanXSTCHarness implements XSTCTester.Harness {
    @Override // org.apache.xmlbeans.impl.tool.XSTCTester.Harness
    public void runTestCase(XSTCTester.TestCaseResult result) {
        XSTCTester.TestCase testCase = result.getTestCase();
        Collection errors = new ArrayList();
        boolean schemaValid = true;
        boolean instanceValid = true;
        if (testCase.getSchemaFile() == null) {
            return;
        }
        SchemaTypeLoader loader = null;
        try {
            XmlObject schema = XmlObject.Factory.parse(testCase.getSchemaFile(), new XmlOptions().setErrorListener(errors).setLoadLineNumbers());
            XmlObject schema2 = null;
            if (testCase.getResourceFile() != null) {
                schema2 = XmlObject.Factory.parse(testCase.getResourceFile(), new XmlOptions().setErrorListener(errors).setLoadLineNumbers());
            }
            XmlObject[] schemas = schema2 == null ? new XmlObject[]{schema} : new XmlObject[]{schema, schema2};
            SchemaTypeSystem system = XmlBeans.compileXsd(schemas, XmlBeans.getBuiltinTypeSystem(), new XmlOptions().setErrorListener(errors));
            loader = XmlBeans.typeLoaderUnion(new SchemaTypeLoader[]{system, XmlBeans.getBuiltinTypeSystem()});
        } catch (Exception e) {
            schemaValid = false;
            if (!(e instanceof XmlException) || errors.isEmpty()) {
                result.setCrash(true);
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                result.addSvMessages(Collections.singleton(sw.toString()));
            }
        }
        result.addSvMessages(errors);
        result.setSvActual(schemaValid);
        errors.clear();
        if (loader == null || testCase.getInstanceFile() == null) {
            return;
        }
        try {
            XmlObject instance = loader.parse(testCase.getInstanceFile(), (SchemaType) null, new XmlOptions().setErrorListener(errors).setLoadLineNumbers());
            if (!instance.validate(new XmlOptions().setErrorListener(errors))) {
                instanceValid = false;
            }
        } catch (Exception e2) {
            instanceValid = false;
            if (!(e2 instanceof XmlException) || errors.isEmpty()) {
                result.setCrash(true);
                StringWriter sw2 = new StringWriter();
                e2.printStackTrace(new PrintWriter(sw2));
                result.addIvMessages(Collections.singleton(sw2.toString()));
            }
        }
        result.addIvMessages(errors);
        result.setIvActual(instanceValid);
    }
}
