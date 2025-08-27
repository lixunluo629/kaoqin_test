package org.springframework.boot.jta.atomikos;

import javax.sql.XADataSource;
import org.springframework.boot.jta.XADataSourceWrapper;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/atomikos/AtomikosXADataSourceWrapper.class */
public class AtomikosXADataSourceWrapper implements XADataSourceWrapper {
    @Override // org.springframework.boot.jta.XADataSourceWrapper
    /* renamed from: wrapDataSource, reason: merged with bridge method [inline-methods] */
    public AtomikosDataSourceBean mo7464wrapDataSource(XADataSource dataSource) throws Exception {
        AtomikosDataSourceBean bean = new AtomikosDataSourceBean();
        bean.setXaDataSource(dataSource);
        return bean;
    }
}
