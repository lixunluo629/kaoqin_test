package org.apache.ibatis.mapping;

import java.sql.ResultSet;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/ParameterMapping.class */
public class ParameterMapping {
    private Configuration configuration;
    private String property;
    private ParameterMode mode;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private Integer numericScale;
    private TypeHandler<?> typeHandler;
    private String resultMapId;
    private String jdbcTypeName;
    private String expression;

    private ParameterMapping() {
        this.javaType = Object.class;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/ParameterMapping$Builder.class */
    public static class Builder {
        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property, TypeHandler<?> typeHandler) {
            this.parameterMapping.configuration = configuration;
            this.parameterMapping.property = property;
            this.parameterMapping.typeHandler = typeHandler;
            this.parameterMapping.mode = ParameterMode.IN;
        }

        public Builder(Configuration configuration, String property, Class<?> javaType) {
            this.parameterMapping.configuration = configuration;
            this.parameterMapping.property = property;
            this.parameterMapping.javaType = javaType;
            this.parameterMapping.mode = ParameterMode.IN;
        }

        public Builder mode(ParameterMode mode) {
            this.parameterMapping.mode = mode;
            return this;
        }

        public Builder javaType(Class<?> javaType) {
            this.parameterMapping.javaType = javaType;
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType) {
            this.parameterMapping.jdbcType = jdbcType;
            return this;
        }

        public Builder numericScale(Integer numericScale) {
            this.parameterMapping.numericScale = numericScale;
            return this;
        }

        public Builder resultMapId(String resultMapId) {
            this.parameterMapping.resultMapId = resultMapId;
            return this;
        }

        public Builder typeHandler(TypeHandler<?> typeHandler) {
            this.parameterMapping.typeHandler = typeHandler;
            return this;
        }

        public Builder jdbcTypeName(String jdbcTypeName) {
            this.parameterMapping.jdbcTypeName = jdbcTypeName;
            return this;
        }

        public Builder expression(String expression) {
            this.parameterMapping.expression = expression;
            return this;
        }

        public ParameterMapping build() {
            resolveTypeHandler();
            validate();
            return this.parameterMapping;
        }

        private void validate() {
            if (ResultSet.class.equals(this.parameterMapping.javaType)) {
                if (this.parameterMapping.resultMapId == null) {
                    throw new IllegalStateException("Missing resultmap in property '" + this.parameterMapping.property + "'.  Parameters of type java.sql.ResultSet require a resultmap.");
                }
            } else if (this.parameterMapping.typeHandler == null) {
                throw new IllegalStateException("Type handler was null on parameter mapping for property '" + this.parameterMapping.property + "'. It was either not specified and/or could not be found for the javaType (" + this.parameterMapping.javaType.getName() + ") : jdbcType (" + this.parameterMapping.jdbcType + ") combination.");
            }
        }

        private void resolveTypeHandler() {
            if (this.parameterMapping.typeHandler == null && this.parameterMapping.javaType != null) {
                Configuration configuration = this.parameterMapping.configuration;
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                this.parameterMapping.typeHandler = typeHandlerRegistry.getTypeHandler(this.parameterMapping.javaType, this.parameterMapping.jdbcType);
            }
        }
    }

    public String getProperty() {
        return this.property;
    }

    public ParameterMode getMode() {
        return this.mode;
    }

    public Class<?> getJavaType() {
        return this.javaType;
    }

    public JdbcType getJdbcType() {
        return this.jdbcType;
    }

    public Integer getNumericScale() {
        return this.numericScale;
    }

    public TypeHandler<?> getTypeHandler() {
        return this.typeHandler;
    }

    public String getResultMapId() {
        return this.resultMapId;
    }

    public String getJdbcTypeName() {
        return this.jdbcTypeName;
    }

    public String getExpression() {
        return this.expression;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ParameterMapping{");
        sb.append("property='").append(this.property).append('\'');
        sb.append(", mode=").append(this.mode);
        sb.append(", javaType=").append(this.javaType);
        sb.append(", jdbcType=").append(this.jdbcType);
        sb.append(", numericScale=").append(this.numericScale);
        sb.append(", resultMapId='").append(this.resultMapId).append('\'');
        sb.append(", jdbcTypeName='").append(this.jdbcTypeName).append('\'');
        sb.append(", expression='").append(this.expression).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
