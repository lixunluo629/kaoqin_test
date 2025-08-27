package org.apache.ibatis.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/SqlSessionFactoryBuilder.class */
public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(Reader reader) {
        return build(reader, (String) null, (Properties) null);
    }

    public SqlSessionFactory build(Reader reader, String environment) {
        return build(reader, environment, (Properties) null);
    }

    public SqlSessionFactory build(Reader reader, Properties properties) {
        return build(reader, (String) null, properties);
    }

    public SqlSessionFactory build(Reader reader, String environment, Properties properties) throws IOException {
        try {
            try {
                XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
                SqlSessionFactory sqlSessionFactoryBuild = build(parser.parse());
                ErrorContext.instance().reset();
                try {
                    reader.close();
                } catch (IOException e) {
                }
                return sqlSessionFactoryBuild;
            } catch (Exception e2) {
                throw ExceptionFactory.wrapException("Error building SqlSession.", e2);
            }
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            try {
                reader.close();
            } catch (IOException e3) {
            }
            throw th;
        }
    }

    public SqlSessionFactory build(InputStream inputStream) {
        return build(inputStream, (String) null, (Properties) null);
    }

    public SqlSessionFactory build(InputStream inputStream, String environment) {
        return build(inputStream, environment, (Properties) null);
    }

    public SqlSessionFactory build(InputStream inputStream, Properties properties) {
        return build(inputStream, (String) null, properties);
    }

    public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) throws IOException {
        try {
            try {
                XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
                SqlSessionFactory sqlSessionFactoryBuild = build(parser.parse());
                ErrorContext.instance().reset();
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
                return sqlSessionFactoryBuild;
            } catch (Exception e2) {
                throw ExceptionFactory.wrapException("Error building SqlSession.", e2);
            }
        } catch (Throwable th) {
            ErrorContext.instance().reset();
            try {
                inputStream.close();
            } catch (IOException e3) {
            }
            throw th;
        }
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }
}
