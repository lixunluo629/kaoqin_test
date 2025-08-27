package org.springframework.boot.jta.bitronix;

import javax.sql.XADataSource;
import org.springframework.boot.jta.XADataSourceWrapper;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/bitronix/BitronixXADataSourceWrapper.class */
public class BitronixXADataSourceWrapper implements XADataSourceWrapper {
    @Override // org.springframework.boot.jta.XADataSourceWrapper
    /* renamed from: wrapDataSource, reason: merged with bridge method [inline-methods] */
    public PoolingDataSourceBean mo7464wrapDataSource(XADataSource dataSource) throws Exception {
        PoolingDataSourceBean pool = new PoolingDataSourceBean();
        pool.setDataSource(dataSource);
        return pool;
    }
}
