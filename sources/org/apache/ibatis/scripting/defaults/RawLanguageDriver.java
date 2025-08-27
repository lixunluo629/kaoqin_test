package org.apache.ibatis.scripting.defaults;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/defaults/RawLanguageDriver.class */
public class RawLanguageDriver extends XMLLanguageDriver {
    @Override // org.apache.ibatis.scripting.xmltags.XMLLanguageDriver, org.apache.ibatis.scripting.LanguageDriver
    public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
        SqlSource source = super.createSqlSource(configuration, script, parameterType);
        checkIsNotDynamic(source);
        return source;
    }

    @Override // org.apache.ibatis.scripting.xmltags.XMLLanguageDriver, org.apache.ibatis.scripting.LanguageDriver
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        SqlSource source = super.createSqlSource(configuration, script, parameterType);
        checkIsNotDynamic(source);
        return source;
    }

    private void checkIsNotDynamic(SqlSource source) {
        if (!RawSqlSource.class.equals(source.getClass())) {
            throw new BuilderException("Dynamic content is not allowed when using RAW language");
        }
    }
}
