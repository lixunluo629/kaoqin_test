package org.springframework.boot.jta;

import javax.sql.DataSource;
import javax.sql.XADataSource;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/XADataSourceWrapper.class */
public interface XADataSourceWrapper {
    /* renamed from: wrapDataSource */
    DataSource mo7464wrapDataSource(XADataSource xADataSource) throws Exception;
}
