package org.apache.ibatis.ognl;

import java.util.Enumeration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ObjectElementsAccessor.class */
public class ObjectElementsAccessor implements ElementsAccessor {
    @Override // org.apache.ibatis.ognl.ElementsAccessor
    public Enumeration getElements(final Object target) {
        return new Enumeration() { // from class: org.apache.ibatis.ognl.ObjectElementsAccessor.1
            private boolean seen = false;

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return !this.seen;
            }

            @Override // java.util.Enumeration
            public Object nextElement() {
                Object result = null;
                if (!this.seen) {
                    result = target;
                    this.seen = true;
                }
                return result;
            }
        };
    }
}
