package org.apache.xmlbeans.impl.common;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.hyperic.sigar.NetFlags;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XPath.class */
public class XPath {
    public static final String _NS_BOUNDARY = "$xmlbeans!ns_boundary";
    public static final String _DEFAULT_ELT_NS = "$xmlbeans!default_uri";
    private final Selector _selector;
    private final boolean _sawDeepDot;

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XPath$XPathCompileException.class */
    public static class XPathCompileException extends XmlException {
        XPathCompileException(XmlError err) {
            super(err.toString(), (Throwable) null, err);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XPath$ExecutionContext.class */
    public static class ExecutionContext {
        public static final int HIT = 1;
        public static final int DESCEND = 2;
        public static final int ATTRS = 4;
        private XPath _xpath;
        private ArrayList _stack = new ArrayList();
        private PathContext[] _paths;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !XPath.class.desiredAssertionStatus();
        }

        public final void init(XPath xpath) {
            if (this._xpath != xpath) {
                this._xpath = xpath;
                this._paths = new PathContext[xpath._selector._paths.length];
                for (int i = 0; i < this._paths.length; i++) {
                    this._paths[i] = new PathContext();
                }
            }
            this._stack.clear();
            for (int i2 = 0; i2 < this._paths.length; i2++) {
                this._paths[i2].init(xpath._selector._paths[i2]);
            }
        }

        public final int start() {
            int result = 0;
            for (int i = 0; i < this._paths.length; i++) {
                result |= this._paths[i].start();
            }
            return result;
        }

        public final int element(QName name) {
            if (!$assertionsDisabled && name == null) {
                throw new AssertionError();
            }
            this._stack.add(name);
            int result = 0;
            for (int i = 0; i < this._paths.length; i++) {
                result |= this._paths[i].element(name);
            }
            return result;
        }

        public final boolean attr(QName name) {
            boolean hit = false;
            for (int i = 0; i < this._paths.length; i++) {
                hit |= this._paths[i].attr(name);
            }
            return hit;
        }

        public final void end() {
            this._stack.remove(this._stack.size() - 1);
            for (int i = 0; i < this._paths.length; i++) {
                this._paths[i].end();
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XPath$ExecutionContext$PathContext.class */
        private final class PathContext {
            private Step _curr;
            private List _prev = new ArrayList();
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !XPath.class.desiredAssertionStatus();
            }

            PathContext() {
            }

            void init(Step steps) {
                this._curr = steps;
                this._prev.clear();
            }

            private QName top(int i) {
                return (QName) ExecutionContext.this._stack.get((ExecutionContext.this._stack.size() - 1) - i);
            }

            /* JADX WARN: Code restructure failed: missing block: B:29:0x0083, code lost:
            
                r4._curr = r4._curr._prev;
             */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            private void backtrack() {
                /*
                    r4 = this;
                    boolean r0 = org.apache.xmlbeans.impl.common.XPath.ExecutionContext.PathContext.$assertionsDisabled
                    if (r0 != 0) goto L15
                    r0 = r4
                    org.apache.xmlbeans.impl.common.XPath$Step r0 = r0._curr
                    if (r0 != 0) goto L15
                    java.lang.AssertionError r0 = new java.lang.AssertionError
                    r1 = r0
                    r1.<init>()
                    throw r0
                L15:
                    r0 = r4
                    org.apache.xmlbeans.impl.common.XPath$Step r0 = r0._curr
                    boolean r0 = r0._hasBacktrack
                    if (r0 == 0) goto L2b
                    r0 = r4
                    r1 = r4
                    org.apache.xmlbeans.impl.common.XPath$Step r1 = r1._curr
                    org.apache.xmlbeans.impl.common.XPath$Step r1 = r1._backtrack
                    r0._curr = r1
                    return
                L2b:
                    boolean r0 = org.apache.xmlbeans.impl.common.XPath.ExecutionContext.PathContext.$assertionsDisabled
                    if (r0 != 0) goto L43
                    r0 = r4
                    org.apache.xmlbeans.impl.common.XPath$Step r0 = r0._curr
                    boolean r0 = r0._deep
                    if (r0 == 0) goto L43
                    java.lang.AssertionError r0 = new java.lang.AssertionError
                    r1 = r0
                    r1.<init>()
                    throw r0
                L43:
                    r0 = r4
                    r1 = r4
                    org.apache.xmlbeans.impl.common.XPath$Step r1 = r1._curr
                    org.apache.xmlbeans.impl.common.XPath$Step r1 = r1._prev
                    r0._curr = r1
                L4e:
                    r0 = r4
                    org.apache.xmlbeans.impl.common.XPath$Step r0 = r0._curr
                    boolean r0 = r0._deep
                    if (r0 != 0) goto L91
                    r0 = 0
                    r5 = r0
                    r0 = r4
                    org.apache.xmlbeans.impl.common.XPath$Step r0 = r0._curr
                    r6 = r0
                L5f:
                    r0 = r6
                    boolean r0 = r0._deep
                    if (r0 != 0) goto L80
                    r0 = r6
                    r1 = r4
                    r2 = r5
                    int r5 = r5 + 1
                    javax.xml.namespace.QName r1 = r1.top(r2)
                    boolean r0 = r0.match(r1)
                    if (r0 != 0) goto L78
                    goto L83
                L78:
                    r0 = r6
                    org.apache.xmlbeans.impl.common.XPath$Step r0 = r0._prev
                    r6 = r0
                    goto L5f
                L80:
                    goto L91
                L83:
                    r0 = r4
                    r1 = r4
                    org.apache.xmlbeans.impl.common.XPath$Step r1 = r1._curr
                    org.apache.xmlbeans.impl.common.XPath$Step r1 = r1._prev
                    r0._curr = r1
                    goto L4e
                L91:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.common.XPath.ExecutionContext.PathContext.backtrack():void");
            }

            int start() {
                if (!$assertionsDisabled && this._curr == null) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && this._curr._prev != null) {
                    throw new AssertionError();
                }
                if (this._curr._name != null) {
                    return this._curr._flags;
                }
                this._curr = null;
                return 1;
            }

            int element(QName name) {
                this._prev.add(this._curr);
                if (this._curr == null) {
                    return 0;
                }
                if (!$assertionsDisabled && this._curr._name == null) {
                    throw new AssertionError();
                }
                if (!this._curr._attr && this._curr.match(name)) {
                    Step step = this._curr._next;
                    this._curr = step;
                    if (step._name != null) {
                        return this._curr._flags;
                    }
                    backtrack();
                    if (this._curr == null) {
                        return 1;
                    }
                    return 1 | this._curr._flags;
                }
                while (true) {
                    backtrack();
                    if (this._curr == null) {
                        return 0;
                    }
                    if (this._curr.match(name)) {
                        this._curr = this._curr._next;
                        break;
                    }
                    if (this._curr._deep) {
                        break;
                    }
                }
                return this._curr._flags;
            }

            boolean attr(QName name) {
                return this._curr != null && this._curr._attr && this._curr.match(name);
            }

            void end() {
                this._curr = (Step) this._prev.remove(this._prev.size() - 1);
            }
        }
    }

    public static XPath compileXPath(String xpath) throws XPathCompileException {
        return compileXPath(xpath, "$this", null);
    }

    public static XPath compileXPath(String xpath, String currentNodeVar) throws XPathCompileException {
        return compileXPath(xpath, currentNodeVar, null);
    }

    public static XPath compileXPath(String xpath, Map namespaces) throws XPathCompileException {
        return compileXPath(xpath, "$this", namespaces);
    }

    public static XPath compileXPath(String xpath, String currentNodeVar, Map namespaces) throws XPathCompileException {
        return new CompilationContext(namespaces, currentNodeVar).compile(xpath);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XPath$CompilationContext.class */
    private static class CompilationContext {
        private String _expr;
        private boolean _sawDeepDot;
        private boolean _lastDeepDot;
        private String _currentNodeVar;
        protected Map _namespaces;
        private Map _externalNamespaces;
        private int _offset;
        private int _line;
        private int _column;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !XPath.class.desiredAssertionStatus();
        }

        CompilationContext(Map namespaces, String currentNodeVar) {
            if (!$assertionsDisabled && this._currentNodeVar != null && !this._currentNodeVar.startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)) {
                throw new AssertionError();
            }
            if (currentNodeVar == null) {
                this._currentNodeVar = "$this";
            } else {
                this._currentNodeVar = currentNodeVar;
            }
            this._namespaces = new HashMap();
            this._externalNamespaces = namespaces == null ? new HashMap() : namespaces;
        }

        XPath compile(String expr) throws XPathCompileException {
            this._offset = 0;
            this._line = 1;
            this._column = 1;
            this._expr = expr;
            return tokenizeXPath();
        }

        int currChar() {
            return currChar(0);
        }

        int currChar(int offset) {
            if (this._offset + offset >= this._expr.length()) {
                return -1;
            }
            return this._expr.charAt(this._offset + offset);
        }

        void advance() {
            if (this._offset < this._expr.length()) {
                char ch2 = this._expr.charAt(this._offset);
                this._offset++;
                this._column++;
                if (ch2 == '\r' || ch2 == '\n') {
                    this._line++;
                    this._column = 1;
                    if (this._offset + 1 < this._expr.length()) {
                        char nextCh = this._expr.charAt(this._offset + 1);
                        if ((nextCh == '\r' || nextCh == '\n') && ch2 != nextCh) {
                            this._offset++;
                        }
                    }
                }
            }
        }

        void advance(int count) {
            if (!$assertionsDisabled && count < 0) {
                throw new AssertionError();
            }
            while (true) {
                int i = count;
                count--;
                if (i > 0) {
                    advance();
                } else {
                    return;
                }
            }
        }

        boolean isWhitespace() {
            return isWhitespace(0);
        }

        boolean isWhitespace(int offset) {
            int ch2 = currChar(offset);
            return ch2 == 32 || ch2 == 9 || ch2 == 10 || ch2 == 13;
        }

        boolean isNCNameStart() {
            if (currChar() == -1) {
                return false;
            }
            return XMLChar.isNCNameStart(currChar());
        }

        boolean isNCName() {
            if (currChar() == -1) {
                return false;
            }
            return XMLChar.isNCName(currChar());
        }

        boolean startsWith(String s) {
            return startsWith(s, 0);
        }

        boolean startsWith(String s, int offset) {
            if (this._offset + offset >= this._expr.length()) {
                return false;
            }
            return this._expr.startsWith(s, this._offset + offset);
        }

        private XPathCompileException newError(String msg) {
            XmlError err = XmlError.forLocation(msg, 0, null, this._line, this._column, this._offset);
            return new XPathCompileException(err);
        }

        String lookupPrefix(String prefix) throws XPathCompileException {
            if (this._namespaces.containsKey(prefix)) {
                return (String) this._namespaces.get(prefix);
            }
            if (this._externalNamespaces.containsKey(prefix)) {
                return (String) this._externalNamespaces.get(prefix);
            }
            if (prefix.equals("xml")) {
                return "http://www.w3.org/XML/1998/namespace";
            }
            if (prefix.equals("xs")) {
                return "http://www.w3.org/2001/XMLSchema";
            }
            if (prefix.equals("xsi")) {
                return "http://www.w3.org/2001/XMLSchema-instance";
            }
            if (prefix.equals("fn")) {
                return "http://www.w3.org/2002/11/xquery-functions";
            }
            if (prefix.equals("xdt")) {
                return "http://www.w3.org/2003/11/xpath-datatypes";
            }
            if (prefix.equals(BeanDefinitionParserDelegate.LOCAL_REF_ATTRIBUTE)) {
                return "http://www.w3.org/2003/11/xquery-local-functions";
            }
            throw newError("Undefined prefix: " + prefix);
        }

        private boolean parseWhitespace() throws XPathCompileException {
            boolean z = false;
            while (true) {
                boolean sawSpace = z;
                if (isWhitespace()) {
                    advance();
                    z = true;
                } else {
                    return sawSpace;
                }
            }
        }

        private boolean tokenize(String s) {
            if (!$assertionsDisabled && s.length() <= 0) {
                throw new AssertionError();
            }
            int offset = 0;
            while (isWhitespace(offset)) {
                offset++;
            }
            if (!startsWith(s, offset)) {
                return false;
            }
            advance(offset + s.length());
            return true;
        }

        private boolean tokenize(String s1, String s2) {
            if (!$assertionsDisabled && s1.length() <= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && s2.length() <= 0) {
                throw new AssertionError();
            }
            int offset = 0;
            while (isWhitespace(offset)) {
                offset++;
            }
            if (!startsWith(s1, offset)) {
                return false;
            }
            int offset2 = offset + s1.length();
            while (isWhitespace(offset2)) {
                offset2++;
            }
            if (!startsWith(s2, offset2)) {
                return false;
            }
            advance(offset2 + s2.length());
            return true;
        }

        private boolean tokenize(String s1, String s2, String s3) {
            if (!$assertionsDisabled && s1.length() <= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && s2.length() <= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && s3.length() <= 0) {
                throw new AssertionError();
            }
            int offset = 0;
            while (isWhitespace(offset)) {
                offset++;
            }
            if (!startsWith(s1, offset)) {
                return false;
            }
            int offset2 = offset + s1.length();
            while (isWhitespace(offset2)) {
                offset2++;
            }
            if (!startsWith(s2, offset2)) {
                return false;
            }
            int offset3 = offset2 + s2.length();
            while (isWhitespace(offset3)) {
                offset3++;
            }
            if (!startsWith(s3, offset3)) {
                return false;
            }
            int offset4 = offset3 + s3.length();
            while (isWhitespace(offset4)) {
                offset4++;
            }
            advance(offset4);
            return true;
        }

        private boolean tokenize(String s1, String s2, String s3, String s4) {
            if (!$assertionsDisabled && s1.length() <= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && s2.length() <= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && s3.length() <= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && s4.length() <= 0) {
                throw new AssertionError();
            }
            int offset = 0;
            while (isWhitespace(offset)) {
                offset++;
            }
            if (!startsWith(s1, offset)) {
                return false;
            }
            int offset2 = offset + s1.length();
            while (isWhitespace(offset2)) {
                offset2++;
            }
            if (!startsWith(s2, offset2)) {
                return false;
            }
            int offset3 = offset2 + s2.length();
            while (isWhitespace(offset3)) {
                offset3++;
            }
            if (!startsWith(s3, offset3)) {
                return false;
            }
            int offset4 = offset3 + s3.length();
            while (isWhitespace(offset4)) {
                offset4++;
            }
            if (!startsWith(s4, offset4)) {
                return false;
            }
            advance(offset4 + s4.length());
            return true;
        }

        private String tokenizeNCName() throws XPathCompileException {
            parseWhitespace();
            if (!isNCNameStart()) {
                throw newError("Expected non-colonized name");
            }
            StringBuffer sb = new StringBuffer();
            sb.append((char) currChar());
            advance();
            while (isNCName()) {
                sb.append((char) currChar());
                advance();
            }
            return sb.toString();
        }

        private QName getAnyQName() {
            return new QName("", "");
        }

        private QName tokenizeQName() throws XPathCompileException {
            if (tokenize("*")) {
                return getAnyQName();
            }
            String ncName = tokenizeNCName();
            if (!tokenize(":")) {
                return new QName(lookupPrefix(""), ncName);
            }
            return new QName(lookupPrefix(ncName), tokenize("*") ? "" : tokenizeNCName());
        }

        private String tokenizeQuotedUri() throws XPathCompileException {
            char quote;
            if (tokenize(SymbolConstants.QUOTES_SYMBOL)) {
                quote = '\"';
            } else if (tokenize("'")) {
                quote = '\'';
            } else {
                throw newError("Expected quote (\" or ')");
            }
            StringBuffer sb = new StringBuffer();
            while (currChar() != -1) {
                if (currChar() == quote) {
                    advance();
                    if (currChar() != quote) {
                        return sb.toString();
                    }
                }
                sb.append((char) currChar());
                advance();
            }
            throw newError("Path terminated in URI literal");
        }

        private Step addStep(boolean deep, boolean attr, QName name, Step steps) {
            Step step = new Step(deep, attr, name);
            if (steps == null) {
                return step;
            }
            while (steps._next != null) {
                steps = steps._next;
            }
            steps._next = step;
            step._prev = steps;
            return steps;
        }

        private Step tokenizeSteps() throws XPathCompileException {
            boolean deep;
            if (tokenize("/")) {
                throw newError("Absolute paths unsupported");
            }
            if (tokenize(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX, this._currentNodeVar, "//") || tokenize(".", "//")) {
                deep = true;
            } else if (tokenize(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX, this._currentNodeVar, "/") || tokenize(".", "/")) {
                deep = false;
            } else {
                if (tokenize(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX, this._currentNodeVar) || tokenize(".")) {
                    return addStep(false, false, null, null);
                }
                deep = false;
            }
            Step steps = null;
            boolean deepDot = false;
            while (!tokenize(BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT, NetFlags.ANY_ADDR_V6) && !tokenize("@")) {
                if (tokenize(".")) {
                    deepDot = deepDot || deep;
                } else {
                    tokenize("child", NetFlags.ANY_ADDR_V6);
                    QName name = tokenizeQName();
                    if (name != null) {
                        steps = addStep(deep, false, name, steps);
                        deep = false;
                    }
                }
                if (tokenize("//")) {
                    deep = true;
                    deepDot = false;
                } else {
                    if (!tokenize("/")) {
                        break;
                    }
                    if (deepDot) {
                        deep = true;
                    }
                }
            }
            steps = addStep(deep, true, tokenizeQName(), steps);
            boolean z = deepDot;
            this._lastDeepDot = z;
            if (z) {
                this._lastDeepDot = true;
                steps = addStep(true, false, getAnyQName(), steps);
            }
            return addStep(false, false, null, steps);
        }

        private void computeBacktrack(Step steps) throws XPathCompileException {
            Step t;
            Step u;
            Step step = steps;
            while (true) {
                Step s = step;
                if (s != null) {
                    Step step2 = s._next;
                    while (true) {
                        t = step2;
                        if (t == null || t._deep) {
                            break;
                        } else {
                            step2 = t._next;
                        }
                    }
                    if (!s._deep) {
                        Step step3 = s;
                        while (true) {
                            Step u2 = step3;
                            if (u2 != t) {
                                u2._hasBacktrack = true;
                                step3 = u2._next;
                            }
                        }
                    } else {
                        int n = 0;
                        Step step4 = s;
                        while (true) {
                            u = step4;
                            if (u == t || u._name == null || u.isWild() || u._attr) {
                                break;
                            }
                            n++;
                            step4 = u._next;
                        }
                        QName[] pattern = new QName[n + 1];
                        int[] kmp = new int[n + 1];
                        Step v = s;
                        for (int i = 0; i < n; i++) {
                            pattern[i] = v._name;
                            v = v._next;
                        }
                        pattern[n] = getAnyQName();
                        int i2 = 0;
                        kmp[0] = -1;
                        int j = -1;
                        while (i2 < n) {
                            while (j > -1 && !pattern[i2].equals(pattern[j])) {
                                j = kmp[j];
                            }
                            i2++;
                            j++;
                            if (pattern[i2].equals(pattern[j])) {
                                kmp[i2] = kmp[j];
                            } else {
                                kmp[i2] = j;
                            }
                        }
                        int i3 = 0;
                        Step step5 = s;
                        while (true) {
                            Step v2 = step5;
                            if (v2 == u) {
                                break;
                            }
                            v2._hasBacktrack = true;
                            v2._backtrack = s;
                            for (int j2 = kmp[i3]; j2 > 0; j2--) {
                                v2._backtrack = v2._backtrack._next;
                            }
                            i3++;
                            step5 = v2._next;
                        }
                        Step v3 = s;
                        if (n > 1) {
                            for (int j3 = kmp[n - 1]; j3 > 0; j3--) {
                                v3 = v3._next;
                            }
                        }
                        if (u != t && u._attr) {
                            u._hasBacktrack = true;
                            u._backtrack = v3;
                            u = u._next;
                        }
                        if (u != t && u._name == null) {
                            u._hasBacktrack = true;
                            u._backtrack = v3;
                        }
                        if (!$assertionsDisabled && !s._deep) {
                            throw new AssertionError();
                        }
                        s._hasBacktrack = true;
                        s._backtrack = s;
                    }
                    step = t;
                } else {
                    return;
                }
            }
        }

        private void tokenizePath(ArrayList paths) throws XPathCompileException {
            Step stepAddStep;
            this._lastDeepDot = false;
            Step steps = tokenizeSteps();
            computeBacktrack(steps);
            paths.add(steps);
            if (this._lastDeepDot) {
                this._sawDeepDot = true;
                Step s = null;
                Step step = steps;
                while (true) {
                    Step t = step;
                    if (t != null) {
                        if (t._next != null && t._next._next == null) {
                            stepAddStep = addStep(t._deep, true, t._name, s);
                        } else {
                            stepAddStep = addStep(t._deep, t._attr, t._name, s);
                        }
                        s = stepAddStep;
                        step = t._next;
                    } else {
                        computeBacktrack(s);
                        paths.add(s);
                        return;
                    }
                }
            }
        }

        private Selector tokenizeSelector() throws XPathCompileException {
            ArrayList paths = new ArrayList();
            tokenizePath(paths);
            while (tokenize("|")) {
                tokenizePath(paths);
            }
            return new Selector((Step[]) paths.toArray(new Step[0]));
        }

        private XPath tokenizeXPath() throws XPathCompileException {
            while (true) {
                if (tokenize(AsmRelationshipUtils.DEC_LABEL, "namespace")) {
                    if (!parseWhitespace()) {
                        throw newError("Expected prefix after 'declare namespace'");
                    }
                    String prefix = tokenizeNCName();
                    if (!tokenize(SymbolConstants.EQUAL_SYMBOL)) {
                        throw newError("Expected '='");
                    }
                    String uri = tokenizeQuotedUri();
                    if (this._namespaces.containsKey(prefix)) {
                        throw newError("Redefinition of namespace prefix: " + prefix);
                    }
                    this._namespaces.put(prefix, uri);
                    if (this._externalNamespaces.containsKey(prefix)) {
                        throw newError("Redefinition of namespace prefix: " + prefix);
                    }
                    this._externalNamespaces.put(prefix, uri);
                    if (!tokenize(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR)) {
                    }
                    this._externalNamespaces.put(XPath._NS_BOUNDARY, new Integer(this._offset));
                } else if (tokenize(AsmRelationshipUtils.DEC_LABEL, "default", "element", "namespace")) {
                    String uri2 = tokenizeQuotedUri();
                    if (this._namespaces.containsKey("")) {
                        throw newError("Redefinition of default element namespace");
                    }
                    this._namespaces.put("", uri2);
                    if (this._externalNamespaces.containsKey(XPath._DEFAULT_ELT_NS)) {
                        throw newError("Redefinition of default element namespace : ");
                    }
                    this._externalNamespaces.put(XPath._DEFAULT_ELT_NS, uri2);
                    if (!tokenize(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR)) {
                        throw newError("Default Namespace declaration must end with ;");
                    }
                    this._externalNamespaces.put(XPath._NS_BOUNDARY, new Integer(this._offset));
                } else {
                    if (!this._namespaces.containsKey("")) {
                        this._namespaces.put("", "");
                    }
                    Selector selector = tokenizeSelector();
                    parseWhitespace();
                    if (currChar() != -1) {
                        throw newError("Unexpected char '" + ((char) currChar()) + "'");
                    }
                    return new XPath(selector, this._sawDeepDot);
                }
            }
        }

        private void processNonXpathDecls() {
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XPath$Step.class */
    private static final class Step {
        final boolean _attr;
        final boolean _deep;
        int _flags;
        final QName _name;
        Step _next;
        Step _prev;
        boolean _hasBacktrack;
        Step _backtrack;

        Step(boolean deep, boolean attr, QName name) {
            this._name = name;
            this._deep = deep;
            this._attr = attr;
            int flags = 0;
            flags = (this._deep || !this._attr) ? 0 | 2 : flags;
            this._flags = this._attr ? flags | 4 : flags;
        }

        boolean isWild() {
            return this._name.getLocalPart().length() == 0;
        }

        boolean match(QName name) {
            String local = this._name.getLocalPart();
            String nameLocal = name.getLocalPart();
            int localLength = local.length();
            if (localLength == 0) {
                String uri = this._name.getNamespaceURI();
                int uriLength = uri.length();
                if (uriLength == 0) {
                    return true;
                }
                return uri.equals(name.getNamespaceURI());
            }
            if (localLength != nameLocal.length()) {
                return false;
            }
            String uri2 = this._name.getNamespaceURI();
            String nameUri = name.getNamespaceURI();
            return uri2.length() == nameUri.length() && local.equals(nameLocal) && uri2.equals(nameUri);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XPath$Selector.class */
    private static final class Selector {
        final Step[] _paths;

        Selector(Step[] paths) {
            this._paths = paths;
        }
    }

    private XPath(Selector selector, boolean sawDeepDot) {
        this._selector = selector;
        this._sawDeepDot = sawDeepDot;
    }

    public boolean sawDeepDot() {
        return this._sawDeepDot;
    }
}
