package org.apache.xmlbeans.impl.xb.ltgfmt.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.ltgfmt.TestCase;
import org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/impl/TestsDocumentImpl.class */
public class TestsDocumentImpl extends XmlComplexContentImpl implements TestsDocument {
    private static final long serialVersionUID = 1;
    private static final QName TESTS$0 = new QName("http://www.bea.com/2003/05/xmlbean/ltgfmt", "tests");

    public TestsDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument
    public TestsDocument.Tests getTests() {
        synchronized (monitor()) {
            check_orphaned();
            TestsDocument.Tests target = (TestsDocument.Tests) get_store().find_element_user(TESTS$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument
    public void setTests(TestsDocument.Tests tests) {
        generatedSetterHelperImpl(tests, TESTS$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument
    public TestsDocument.Tests addNewTests() {
        TestsDocument.Tests target;
        synchronized (monitor()) {
            check_orphaned();
            target = (TestsDocument.Tests) get_store().add_element_user(TESTS$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/impl/TestsDocumentImpl$TestsImpl.class */
    public static class TestsImpl extends XmlComplexContentImpl implements TestsDocument.Tests {
        private static final long serialVersionUID = 1;
        private static final QName TEST$0 = new QName("http://www.bea.com/2003/05/xmlbean/ltgfmt", "test");

        public TestsImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument.Tests
        public TestCase[] getTestArray() {
            TestCase[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(TEST$0, targetList);
                result = new TestCase[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument.Tests
        public TestCase getTestArray(int i) {
            TestCase target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TestCase) get_store().find_element_user(TEST$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument.Tests
        public int sizeOfTestArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(TEST$0);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument.Tests
        public void setTestArray(TestCase[] testArray) {
            check_orphaned();
            arraySetterHelper(testArray, TEST$0);
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument.Tests
        public void setTestArray(int i, TestCase test) {
            generatedSetterHelperImpl(test, TEST$0, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument.Tests
        public TestCase insertNewTest(int i) {
            TestCase target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TestCase) get_store().insert_element_user(TEST$0, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument.Tests
        public TestCase addNewTest() {
            TestCase target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TestCase) get_store().add_element_user(TEST$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument.Tests
        public void removeTest(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(TEST$0, i);
            }
        }
    }
}
