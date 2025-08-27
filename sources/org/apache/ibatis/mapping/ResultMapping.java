package org.apache.ibatis.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/ResultMapping.class */
public class ResultMapping {
    private Configuration configuration;
    private String property;
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private TypeHandler<?> typeHandler;
    private String nestedResultMapId;
    private String nestedQueryId;
    private Set<String> notNullColumns;
    private String columnPrefix;
    private List<ResultFlag> flags;
    private List<ResultMapping> composites;
    private String resultSet;
    private String foreignColumn;
    private boolean lazy;

    ResultMapping() {
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/ResultMapping$Builder.class */
    public static class Builder {
        private ResultMapping resultMapping;

        public Builder(Configuration configuration, String property, String column, TypeHandler<?> typeHandler) {
            this(configuration, property);
            this.resultMapping.column = column;
            this.resultMapping.typeHandler = typeHandler;
        }

        public Builder(Configuration configuration, String property, String column, Class<?> javaType) {
            this(configuration, property);
            this.resultMapping.column = column;
            this.resultMapping.javaType = javaType;
        }

        public Builder(Configuration configuration, String property) {
            this.resultMapping = new ResultMapping();
            this.resultMapping.configuration = configuration;
            this.resultMapping.property = property;
            this.resultMapping.flags = new ArrayList();
            this.resultMapping.composites = new ArrayList();
            this.resultMapping.lazy = configuration.isLazyLoadingEnabled();
        }

        public Builder javaType(Class<?> javaType) {
            this.resultMapping.javaType = javaType;
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType) {
            this.resultMapping.jdbcType = jdbcType;
            return this;
        }

        public Builder nestedResultMapId(String nestedResultMapId) {
            this.resultMapping.nestedResultMapId = nestedResultMapId;
            return this;
        }

        public Builder nestedQueryId(String nestedQueryId) {
            this.resultMapping.nestedQueryId = nestedQueryId;
            return this;
        }

        public Builder resultSet(String resultSet) {
            this.resultMapping.resultSet = resultSet;
            return this;
        }

        public Builder foreignColumn(String foreignColumn) {
            this.resultMapping.foreignColumn = foreignColumn;
            return this;
        }

        public Builder notNullColumns(Set<String> notNullColumns) {
            this.resultMapping.notNullColumns = notNullColumns;
            return this;
        }

        public Builder columnPrefix(String columnPrefix) {
            this.resultMapping.columnPrefix = columnPrefix;
            return this;
        }

        public Builder flags(List<ResultFlag> flags) {
            this.resultMapping.flags = flags;
            return this;
        }

        public Builder typeHandler(TypeHandler<?> typeHandler) {
            this.resultMapping.typeHandler = typeHandler;
            return this;
        }

        public Builder composites(List<ResultMapping> composites) {
            this.resultMapping.composites = composites;
            return this;
        }

        public Builder lazy(boolean lazy) {
            this.resultMapping.lazy = lazy;
            return this;
        }

        public ResultMapping build() {
            this.resultMapping.flags = Collections.unmodifiableList(this.resultMapping.flags);
            this.resultMapping.composites = Collections.unmodifiableList(this.resultMapping.composites);
            resolveTypeHandler();
            validate();
            return this.resultMapping;
        }

        private void validate() {
            if (this.resultMapping.nestedQueryId == null || this.resultMapping.nestedResultMapId == null) {
                if (this.resultMapping.nestedQueryId != null || this.resultMapping.nestedResultMapId != null || this.resultMapping.typeHandler != null) {
                    if (this.resultMapping.nestedResultMapId == null && this.resultMapping.column == null && this.resultMapping.composites.isEmpty()) {
                        throw new IllegalStateException("Mapping is missing column attribute for property " + this.resultMapping.property);
                    }
                    if (this.resultMapping.getResultSet() != null) {
                        int numColumns = 0;
                        if (this.resultMapping.column != null) {
                            numColumns = this.resultMapping.column.split(",").length;
                        }
                        int numForeignColumns = 0;
                        if (this.resultMapping.foreignColumn != null) {
                            numForeignColumns = this.resultMapping.foreignColumn.split(",").length;
                        }
                        if (numColumns != numForeignColumns) {
                            throw new IllegalStateException("There should be the same number of columns and foreignColumns in property " + this.resultMapping.property);
                        }
                        return;
                    }
                    return;
                }
                throw new IllegalStateException("No typehandler found for property " + this.resultMapping.property);
            }
            throw new IllegalStateException("Cannot define both nestedQueryId and nestedResultMapId in property " + this.resultMapping.property);
        }

        private void resolveTypeHandler() {
            if (this.resultMapping.typeHandler == null && this.resultMapping.javaType != null) {
                Configuration configuration = this.resultMapping.configuration;
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                this.resultMapping.typeHandler = typeHandlerRegistry.getTypeHandler(this.resultMapping.javaType, this.resultMapping.jdbcType);
            }
        }

        public Builder column(String column) {
            this.resultMapping.column = column;
            return this;
        }
    }

    public String getProperty() {
        return this.property;
    }

    public String getColumn() {
        return this.column;
    }

    public Class<?> getJavaType() {
        return this.javaType;
    }

    public JdbcType getJdbcType() {
        return this.jdbcType;
    }

    public TypeHandler<?> getTypeHandler() {
        return this.typeHandler;
    }

    public String getNestedResultMapId() {
        return this.nestedResultMapId;
    }

    public String getNestedQueryId() {
        return this.nestedQueryId;
    }

    public Set<String> getNotNullColumns() {
        return this.notNullColumns;
    }

    public String getColumnPrefix() {
        return this.columnPrefix;
    }

    public List<ResultFlag> getFlags() {
        return this.flags;
    }

    public List<ResultMapping> getComposites() {
        return this.composites;
    }

    public boolean isCompositeResult() {
        return (this.composites == null || this.composites.isEmpty()) ? false : true;
    }

    public String getResultSet() {
        return this.resultSet;
    }

    public String getForeignColumn() {
        return this.foreignColumn;
    }

    public void setForeignColumn(String foreignColumn) {
        this.foreignColumn = foreignColumn;
    }

    public boolean isLazy() {
        return this.lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResultMapping that = (ResultMapping) o;
        if (this.property == null || !this.property.equals(that.property)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.property != null) {
            return this.property.hashCode();
        }
        if (this.column != null) {
            return this.column.hashCode();
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ResultMapping{");
        sb.append("property='").append(this.property).append('\'');
        sb.append(", column='").append(this.column).append('\'');
        sb.append(", javaType=").append(this.javaType);
        sb.append(", jdbcType=").append(this.jdbcType);
        sb.append(", nestedResultMapId='").append(this.nestedResultMapId).append('\'');
        sb.append(", nestedQueryId='").append(this.nestedQueryId).append('\'');
        sb.append(", notNullColumns=").append(this.notNullColumns);
        sb.append(", columnPrefix='").append(this.columnPrefix).append('\'');
        sb.append(", flags=").append(this.flags);
        sb.append(", composites=").append(this.composites);
        sb.append(", resultSet='").append(this.resultSet).append('\'');
        sb.append(", foreignColumn='").append(this.foreignColumn).append('\'');
        sb.append(", lazy=").append(this.lazy);
        sb.append('}');
        return sb.toString();
    }
}
