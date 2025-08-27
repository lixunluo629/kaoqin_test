package org.apache.ibatis.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.reflection.Jdk;
import org.apache.ibatis.reflection.ParamNameUtil;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/ResultMap.class */
public class ResultMap {
    private Configuration configuration;
    private String id;
    private Class<?> type;
    private List<ResultMapping> resultMappings;
    private List<ResultMapping> idResultMappings;
    private List<ResultMapping> constructorResultMappings;
    private List<ResultMapping> propertyResultMappings;
    private Set<String> mappedColumns;
    private Set<String> mappedProperties;
    private Discriminator discriminator;
    private boolean hasNestedResultMaps;
    private boolean hasNestedQueries;
    private Boolean autoMapping;

    private ResultMap() {
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/ResultMap$Builder.class */
    public static class Builder {
        private static final Log log = LogFactory.getLog((Class<?>) Builder.class);
        private ResultMap resultMap;

        public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings) {
            this(configuration, id, type, resultMappings, null);
        }

        public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings, Boolean autoMapping) {
            this.resultMap = new ResultMap();
            this.resultMap.configuration = configuration;
            this.resultMap.id = id;
            this.resultMap.type = type;
            this.resultMap.resultMappings = resultMappings;
            this.resultMap.autoMapping = autoMapping;
        }

        public Builder discriminator(Discriminator discriminator) {
            this.resultMap.discriminator = discriminator;
            return this;
        }

        public Class<?> type() {
            return this.resultMap.type;
        }

        public ResultMap build() throws SecurityException {
            if (this.resultMap.id != null) {
                this.resultMap.mappedColumns = new HashSet();
                this.resultMap.mappedProperties = new HashSet();
                this.resultMap.idResultMappings = new ArrayList();
                this.resultMap.constructorResultMappings = new ArrayList();
                this.resultMap.propertyResultMappings = new ArrayList();
                List<String> constructorArgNames = new ArrayList<>();
                for (ResultMapping resultMapping : this.resultMap.resultMappings) {
                    this.resultMap.hasNestedQueries = this.resultMap.hasNestedQueries || resultMapping.getNestedQueryId() != null;
                    this.resultMap.hasNestedResultMaps = this.resultMap.hasNestedResultMaps || (resultMapping.getNestedResultMapId() != null && resultMapping.getResultSet() == null);
                    String column = resultMapping.getColumn();
                    if (column != null) {
                        this.resultMap.mappedColumns.add(column.toUpperCase(Locale.ENGLISH));
                    } else if (resultMapping.isCompositeResult()) {
                        for (ResultMapping compositeResultMapping : resultMapping.getComposites()) {
                            String compositeColumn = compositeResultMapping.getColumn();
                            if (compositeColumn != null) {
                                this.resultMap.mappedColumns.add(compositeColumn.toUpperCase(Locale.ENGLISH));
                            }
                        }
                    }
                    String property = resultMapping.getProperty();
                    if (property != null) {
                        this.resultMap.mappedProperties.add(property);
                    }
                    if (resultMapping.getFlags().contains(ResultFlag.CONSTRUCTOR)) {
                        this.resultMap.constructorResultMappings.add(resultMapping);
                        if (resultMapping.getProperty() != null) {
                            constructorArgNames.add(resultMapping.getProperty());
                        }
                    } else {
                        this.resultMap.propertyResultMappings.add(resultMapping);
                    }
                    if (resultMapping.getFlags().contains(ResultFlag.ID)) {
                        this.resultMap.idResultMappings.add(resultMapping);
                    }
                }
                if (this.resultMap.idResultMappings.isEmpty()) {
                    this.resultMap.idResultMappings.addAll(this.resultMap.resultMappings);
                }
                if (!constructorArgNames.isEmpty()) {
                    final List<String> actualArgNames = argNamesOfMatchingConstructor(constructorArgNames);
                    if (actualArgNames != null) {
                        Collections.sort(this.resultMap.constructorResultMappings, new Comparator<ResultMapping>() { // from class: org.apache.ibatis.mapping.ResultMap.Builder.1
                            @Override // java.util.Comparator
                            public int compare(ResultMapping o1, ResultMapping o2) {
                                int paramIdx1 = actualArgNames.indexOf(o1.getProperty());
                                int paramIdx2 = actualArgNames.indexOf(o2.getProperty());
                                return paramIdx1 - paramIdx2;
                            }
                        });
                    } else {
                        throw new BuilderException("Error in result map '" + this.resultMap.id + "'. Failed to find a constructor in '" + this.resultMap.getType().getName() + "' by arg names " + constructorArgNames + ". There might be more info in debug log.");
                    }
                }
                this.resultMap.resultMappings = Collections.unmodifiableList(this.resultMap.resultMappings);
                this.resultMap.idResultMappings = Collections.unmodifiableList(this.resultMap.idResultMappings);
                this.resultMap.constructorResultMappings = Collections.unmodifiableList(this.resultMap.constructorResultMappings);
                this.resultMap.propertyResultMappings = Collections.unmodifiableList(this.resultMap.propertyResultMappings);
                this.resultMap.mappedColumns = Collections.unmodifiableSet(this.resultMap.mappedColumns);
                return this.resultMap;
            }
            throw new IllegalArgumentException("ResultMaps must have an id");
        }

        private List<String> argNamesOfMatchingConstructor(List<String> constructorArgNames) throws SecurityException {
            Constructor<?>[] constructors = this.resultMap.type.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                if (constructorArgNames.size() == paramTypes.length) {
                    List<String> paramNames = getArgNames(constructor);
                    if (constructorArgNames.containsAll(paramNames) && argTypesMatch(constructorArgNames, paramTypes, paramNames)) {
                        return paramNames;
                    }
                }
            }
            return null;
        }

        private boolean argTypesMatch(List<String> constructorArgNames, Class<?>[] paramTypes, List<String> paramNames) {
            for (int i = 0; i < constructorArgNames.size(); i++) {
                Class<?> actualType = paramTypes[paramNames.indexOf(constructorArgNames.get(i))];
                Class<?> specifiedType = ((ResultMapping) this.resultMap.constructorResultMappings.get(i)).getJavaType();
                if (!actualType.equals(specifiedType)) {
                    if (log.isDebugEnabled()) {
                        log.debug("While building result map '" + this.resultMap.id + "', found a constructor with arg names " + constructorArgNames + ", but the type of '" + constructorArgNames.get(i) + "' did not match. Specified: [" + specifiedType.getName() + "] Declared: [" + actualType.getName() + "]");
                        return false;
                    }
                    return false;
                }
            }
            return true;
        }

        private List<String> getArgNames(Constructor<?> constructor) {
            List<String> paramNames = new ArrayList<>();
            List<String> actualParamNames = null;
            Annotation[][] paramAnnotations = constructor.getParameterAnnotations();
            int paramCount = paramAnnotations.length;
            for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
                String name = null;
                Annotation[] annotationArr = paramAnnotations[paramIndex];
                int length = annotationArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    Annotation annotation = annotationArr[i];
                    if (!(annotation instanceof Param)) {
                        i++;
                    } else {
                        name = ((Param) annotation).value();
                        break;
                    }
                }
                if (name == null && this.resultMap.configuration.isUseActualParamName() && Jdk.parameterExists) {
                    if (actualParamNames == null) {
                        actualParamNames = ParamNameUtil.getParamNames(constructor);
                    }
                    if (actualParamNames.size() > paramIndex) {
                        name = actualParamNames.get(paramIndex);
                    }
                }
                paramNames.add(name != null ? name : "arg" + paramIndex);
            }
            return paramNames;
        }
    }

    public String getId() {
        return this.id;
    }

    public boolean hasNestedResultMaps() {
        return this.hasNestedResultMaps;
    }

    public boolean hasNestedQueries() {
        return this.hasNestedQueries;
    }

    public Class<?> getType() {
        return this.type;
    }

    public List<ResultMapping> getResultMappings() {
        return this.resultMappings;
    }

    public List<ResultMapping> getConstructorResultMappings() {
        return this.constructorResultMappings;
    }

    public List<ResultMapping> getPropertyResultMappings() {
        return this.propertyResultMappings;
    }

    public List<ResultMapping> getIdResultMappings() {
        return this.idResultMappings;
    }

    public Set<String> getMappedColumns() {
        return this.mappedColumns;
    }

    public Set<String> getMappedProperties() {
        return this.mappedProperties;
    }

    public Discriminator getDiscriminator() {
        return this.discriminator;
    }

    public void forceNestedResultMaps() {
        this.hasNestedResultMaps = true;
    }

    public Boolean getAutoMapping() {
        return this.autoMapping;
    }
}
