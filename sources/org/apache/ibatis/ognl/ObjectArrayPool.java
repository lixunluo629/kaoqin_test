package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ObjectArrayPool.class */
public final class ObjectArrayPool {
    public Object[] create(int arraySize) {
        return new Object[arraySize];
    }

    public Object[] create(Object singleton) {
        Object[] result = create(1);
        result[0] = singleton;
        return result;
    }

    public Object[] create(Object object1, Object object2) {
        Object[] result = create(2);
        result[0] = object1;
        result[1] = object2;
        return result;
    }

    public Object[] create(Object object1, Object object2, Object object3) {
        Object[] result = create(3);
        result[0] = object1;
        result[1] = object2;
        result[2] = object3;
        return result;
    }

    public Object[] create(Object object1, Object object2, Object object3, Object object4) {
        Object[] result = create(4);
        result[0] = object1;
        result[1] = object2;
        result[2] = object3;
        result[3] = object4;
        return result;
    }

    public Object[] create(Object object1, Object object2, Object object3, Object object4, Object object5) {
        Object[] result = create(5);
        result[0] = object1;
        result[1] = object2;
        result[2] = object3;
        result[3] = object4;
        result[4] = object5;
        return result;
    }

    public void recycle(Object[] value) {
    }
}
