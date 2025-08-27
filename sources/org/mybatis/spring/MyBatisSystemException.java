package org.mybatis.spring;

import org.springframework.dao.UncategorizedDataAccessException;

/* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/MyBatisSystemException.class */
public class MyBatisSystemException extends UncategorizedDataAccessException {
    private static final long serialVersionUID = -5284728621670758939L;

    public MyBatisSystemException(Throwable cause) {
        super(null, cause);
    }
}
