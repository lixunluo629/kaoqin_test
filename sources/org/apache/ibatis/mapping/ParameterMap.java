package org.apache.ibatis.mapping;

import java.util.Collections;
import java.util.List;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/ParameterMap.class */
public class ParameterMap {
    private String id;
    private Class<?> type;
    private List<ParameterMapping> parameterMappings;

    private ParameterMap() {
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/ParameterMap$Builder.class */
    public static class Builder {
        private ParameterMap parameterMap = new ParameterMap();

        public Builder(Configuration configuration, String id, Class<?> type, List<ParameterMapping> parameterMappings) {
            this.parameterMap.id = id;
            this.parameterMap.type = type;
            this.parameterMap.parameterMappings = parameterMappings;
        }

        public Class<?> type() {
            return this.parameterMap.type;
        }

        public ParameterMap build() {
            this.parameterMap.parameterMappings = Collections.unmodifiableList(this.parameterMap.parameterMappings);
            return this.parameterMap;
        }
    }

    public String getId() {
        return this.id;
    }

    public Class<?> getType() {
        return this.type;
    }

    public List<ParameterMapping> getParameterMappings() {
        return this.parameterMappings;
    }
}
