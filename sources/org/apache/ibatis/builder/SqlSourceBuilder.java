package org.apache.ibatis.builder;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.context.expression.StandardBeanExpressionResolver;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/builder/SqlSourceBuilder.class */
public class SqlSourceBuilder extends BaseBuilder {
    private static final String parameterProperties = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    public SqlSource parse(String originalSql, Class<?> parameterType, Map<String, Object> additionalParameters) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(this.configuration, parameterType, additionalParameters);
        GenericTokenParser parser = new GenericTokenParser(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX, "}", handler);
        String sql = parser.parse(originalSql);
        return new StaticSqlSource(this.configuration, sql, handler.getParameterMappings());
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/builder/SqlSourceBuilder$ParameterMappingTokenHandler.class */
    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {
        private List<ParameterMapping> parameterMappings;
        private Class<?> parameterType;
        private MetaObject metaParameters;

        public ParameterMappingTokenHandler(Configuration configuration, Class<?> parameterType, Map<String, Object> additionalParameters) {
            super(configuration);
            this.parameterMappings = new ArrayList();
            this.parameterType = parameterType;
            this.metaParameters = configuration.newMetaObject(additionalParameters);
        }

        public List<ParameterMapping> getParameterMappings() {
            return this.parameterMappings;
        }

        @Override // org.apache.ibatis.parsing.TokenHandler
        public String handleToken(String content) {
            this.parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

        private ParameterMapping buildParameterMapping(String content) throws NoSuchFieldException {
            Class<?> propertyType;
            Map<String, String> propertiesMap = parseParameterMapping(content);
            String property = propertiesMap.get(BeanDefinitionParserDelegate.PROPERTY_ELEMENT);
            if (this.metaParameters.hasGetter(property)) {
                propertyType = this.metaParameters.getGetterType(property);
            } else if (this.typeHandlerRegistry.hasTypeHandler(this.parameterType)) {
                propertyType = this.parameterType;
            } else if (JdbcType.CURSOR.name().equals(propertiesMap.get("jdbcType"))) {
                propertyType = ResultSet.class;
            } else if (property == null || Map.class.isAssignableFrom(this.parameterType)) {
                propertyType = Object.class;
            } else {
                MetaClass metaClass = MetaClass.forClass(this.parameterType, this.configuration.getReflectorFactory());
                if (metaClass.hasGetter(property)) {
                    propertyType = metaClass.getGetterType(property);
                } else {
                    propertyType = Object.class;
                }
            }
            ParameterMapping.Builder builder = new ParameterMapping.Builder(this.configuration, property, propertyType);
            Class<?> javaType = propertyType;
            String typeHandlerAlias = null;
            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                if ("javaType".equals(name)) {
                    javaType = resolveClass(value);
                    builder.javaType(javaType);
                } else if ("jdbcType".equals(name)) {
                    builder.jdbcType(resolveJdbcType(value));
                } else if ("mode".equals(name)) {
                    builder.mode(resolveParameterMode(value));
                } else if ("numericScale".equals(name)) {
                    builder.numericScale(Integer.valueOf(value));
                } else if ("resultMap".equals(name)) {
                    builder.resultMapId(value);
                } else if ("typeHandler".equals(name)) {
                    typeHandlerAlias = value;
                } else if ("jdbcTypeName".equals(name)) {
                    builder.jdbcTypeName(value);
                } else if (!BeanDefinitionParserDelegate.PROPERTY_ELEMENT.equals(name)) {
                    if ("expression".equals(name)) {
                        throw new BuilderException("Expression based parameters are not supported yet");
                    }
                    throw new BuilderException("An invalid property '" + name + "' was found in mapping #{" + content + "}.  Valid properties are " + SqlSourceBuilder.parameterProperties);
                }
            }
            if (typeHandlerAlias != null) {
                builder.typeHandler(resolveTypeHandler(javaType, typeHandlerAlias));
            }
            return builder.build();
        }

        private Map<String, String> parseParameterMapping(String content) {
            try {
                return new ParameterExpression(content);
            } catch (BuilderException ex) {
                throw ex;
            } catch (Exception ex2) {
                throw new BuilderException("Parsing error was found in mapping #{" + content + "}.  Check syntax #{property|(expression), var1=value1, var2=value2, ...} ", ex2);
            }
        }
    }
}
