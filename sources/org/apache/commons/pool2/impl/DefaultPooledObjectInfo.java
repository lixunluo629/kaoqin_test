package org.apache.commons.pool2.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import org.apache.commons.pool2.PooledObject;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/DefaultPooledObjectInfo.class */
public class DefaultPooledObjectInfo implements DefaultPooledObjectInfoMBean {
    private final PooledObject<?> pooledObject;

    public DefaultPooledObjectInfo(PooledObject<?> pooledObject) {
        this.pooledObject = pooledObject;
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObjectInfoMBean
    public long getCreateTime() {
        return this.pooledObject.getCreateTime();
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObjectInfoMBean
    public String getCreateTimeFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return sdf.format(Long.valueOf(this.pooledObject.getCreateTime()));
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObjectInfoMBean
    public long getLastBorrowTime() {
        return this.pooledObject.getLastBorrowTime();
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObjectInfoMBean
    public String getLastBorrowTimeFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return sdf.format(Long.valueOf(this.pooledObject.getLastBorrowTime()));
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObjectInfoMBean
    public String getLastBorrowTrace() {
        StringWriter sw = new StringWriter();
        this.pooledObject.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObjectInfoMBean
    public long getLastReturnTime() {
        return this.pooledObject.getLastReturnTime();
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObjectInfoMBean
    public String getLastReturnTimeFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return sdf.format(Long.valueOf(this.pooledObject.getLastReturnTime()));
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObjectInfoMBean
    public String getPooledObjectType() {
        return this.pooledObject.getObject().getClass().getName();
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObjectInfoMBean
    public String getPooledObjectToString() {
        return this.pooledObject.getObject().toString();
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObjectInfoMBean
    public long getBorrowedCount() {
        if (this.pooledObject instanceof DefaultPooledObject) {
            return ((DefaultPooledObject) this.pooledObject).getBorrowedCount();
        }
        return -1L;
    }

    public String toString() {
        return "DefaultPooledObjectInfo [pooledObject=" + this.pooledObject + "]";
    }
}
