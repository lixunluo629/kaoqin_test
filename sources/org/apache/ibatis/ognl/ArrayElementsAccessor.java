package org.apache.ibatis.ognl;

import java.lang.reflect.Array;
import java.util.Enumeration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ArrayElementsAccessor.class */
public class ArrayElementsAccessor implements ElementsAccessor {
    @Override // org.apache.ibatis.ognl.ElementsAccessor
    public Enumeration getElements(final Object target) {
        return new Enumeration() { // from class: org.apache.ibatis.ognl.ArrayElementsAccessor.1
            private int count;
            private int index = 0;

            {
                this.count = Array.getLength(target);
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.index < this.count;
            }

            @Override // java.util.Enumeration
            public Object nextElement() {
                Object obj = target;
                int i = this.index;
                this.index = i + 1;
                return Array.get(obj, i);
            }
        };
    }
}
