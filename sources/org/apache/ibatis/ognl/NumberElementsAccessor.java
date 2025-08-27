package org.apache.ibatis.ognl;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/NumberElementsAccessor.class */
public class NumberElementsAccessor implements ElementsAccessor, NumericTypes {
    @Override // org.apache.ibatis.ognl.ElementsAccessor
    public Enumeration getElements(final Object target) {
        return new Enumeration() { // from class: org.apache.ibatis.ognl.NumberElementsAccessor.1
            private int type;
            private long next = 0;
            private long finish;

            {
                this.type = OgnlOps.getNumericType(target);
                this.finish = OgnlOps.longValue(target);
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.next < this.finish;
            }

            @Override // java.util.Enumeration
            public Object nextElement() {
                if (this.next >= this.finish) {
                    throw new NoSuchElementException();
                }
                int i = this.type;
                long j = this.next;
                this.next = j + 1;
                return OgnlOps.newInteger(i, j);
            }
        };
    }
}
