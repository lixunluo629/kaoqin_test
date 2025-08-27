package org.apache.ibatis.builder.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.Configuration;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/builder/annotation/ProviderSqlSource.class */
public class ProviderSqlSource implements SqlSource {
    private final Configuration configuration;
    private final SqlSourceBuilder sqlSourceParser;
    private final Class<?> providerType;
    private Method providerMethod;
    private String[] providerMethodArgumentNames;
    private Class<?>[] providerMethodParameterTypes;
    private ProviderContext providerContext;
    private Integer providerContextIndex;

    @Deprecated
    public ProviderSqlSource(Configuration configuration, Object provider) {
        this(configuration, provider, null, null);
    }

    public ProviderSqlSource(Configuration configuration, Object provider, Class<?> mapperType, Method mapperMethod) throws SecurityException {
        try {
            this.configuration = configuration;
            this.sqlSourceParser = new SqlSourceBuilder(configuration);
            this.providerType = (Class) provider.getClass().getMethod("type", new Class[0]).invoke(provider, new Object[0]);
            String providerMethodName = (String) provider.getClass().getMethod(JamXmlElements.METHOD, new Class[0]).invoke(provider, new Object[0]);
            for (Method m : this.providerType.getMethods()) {
                if (providerMethodName.equals(m.getName()) && CharSequence.class.isAssignableFrom(m.getReturnType())) {
                    if (this.providerMethod != null) {
                        throw new BuilderException("Error creating SqlSource for SqlProvider. Method '" + providerMethodName + "' is found multiple in SqlProvider '" + this.providerType.getName() + "'. Sql provider method can not overload.");
                    }
                    this.providerMethod = m;
                    this.providerMethodArgumentNames = new ParamNameResolver(configuration, m).getNames();
                    this.providerMethodParameterTypes = m.getParameterTypes();
                }
            }
            if (this.providerMethod == null) {
                throw new BuilderException("Error creating SqlSource for SqlProvider. Method '" + providerMethodName + "' not found in SqlProvider '" + this.providerType.getName() + "'.");
            }
            for (int i = 0; i < this.providerMethodParameterTypes.length; i++) {
                Class<?> parameterType = this.providerMethodParameterTypes[i];
                if (parameterType == ProviderContext.class) {
                    if (this.providerContext != null) {
                        throw new BuilderException("Error creating SqlSource for SqlProvider. ProviderContext found multiple in SqlProvider method (" + this.providerType.getName() + "." + this.providerMethod.getName() + "). ProviderContext can not define multiple in SqlProvider method argument.");
                    }
                    this.providerContext = new ProviderContext(mapperType, mapperMethod);
                    this.providerContextIndex = Integer.valueOf(i);
                }
            }
        } catch (BuilderException e) {
            throw e;
        } catch (Exception e2) {
            throw new BuilderException("Error creating SqlSource for SqlProvider.  Cause: " + e2, e2);
        }
    }

    @Override // org.apache.ibatis.mapping.SqlSource
    public BoundSql getBoundSql(Object parameterObject) {
        SqlSource sqlSource = createSqlSource(parameterObject);
        return sqlSource.getBoundSql(parameterObject);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x007a A[Catch: BuilderException -> 0x0107, Exception -> 0x010a, TryCatch #2 {BuilderException -> 0x0107, Exception -> 0x010a, blocks: (B:2:0x0000, B:6:0x0011, B:8:0x001b, B:38:0x00ef, B:37:0x00eb, B:11:0x002b, B:16:0x0047, B:18:0x0052, B:22:0x0062, B:24:0x006d, B:25:0x007a, B:27:0x0081, B:28:0x0099, B:32:0x00d3, B:33:0x00e1), top: B:46:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private org.apache.ibatis.mapping.SqlSource createSqlSource(java.lang.Object r7) {
        /*
            Method dump skipped, instructions count: 325
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.builder.annotation.ProviderSqlSource.createSqlSource(java.lang.Object):org.apache.ibatis.mapping.SqlSource");
    }

    private Object[] extractProviderMethodArguments(Object parameterObject) {
        if (this.providerContext != null) {
            Object[] args = new Object[2];
            args[this.providerContextIndex.intValue() == 0 ? (char) 1 : (char) 0] = parameterObject;
            args[this.providerContextIndex.intValue()] = this.providerContext;
            return args;
        }
        return new Object[]{parameterObject};
    }

    private Object[] extractProviderMethodArguments(Map<String, Object> params, String[] argumentNames) {
        Object[] args = new Object[argumentNames.length];
        for (int i = 0; i < args.length; i++) {
            if (this.providerContextIndex != null && this.providerContextIndex.intValue() == i) {
                args[i] = this.providerContext;
            } else {
                args[i] = params.get(argumentNames[i]);
            }
        }
        return args;
    }

    private String invokeProviderMethod(Object... args) throws Exception {
        Object targetObject = null;
        if (!Modifier.isStatic(this.providerMethod.getModifiers())) {
            targetObject = this.providerType.newInstance();
        }
        CharSequence sql = (CharSequence) this.providerMethod.invoke(targetObject, args);
        if (sql != null) {
            return sql.toString();
        }
        return null;
    }

    private String replacePlaceholder(String sql) {
        return PropertyParser.parse(sql, this.configuration.getVariables());
    }
}
