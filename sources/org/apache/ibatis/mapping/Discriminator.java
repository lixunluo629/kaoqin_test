package org.apache.ibatis.mapping;

import java.util.Collections;
import java.util.Map;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/Discriminator.class */
public class Discriminator {
    private ResultMapping resultMapping;
    private Map<String, String> discriminatorMap;

    Discriminator() {
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/Discriminator$Builder.class */
    public static class Builder {
        private Discriminator discriminator = new Discriminator();
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Discriminator.class.desiredAssertionStatus();
        }

        public Builder(Configuration configuration, ResultMapping resultMapping, Map<String, String> discriminatorMap) {
            this.discriminator.resultMapping = resultMapping;
            this.discriminator.discriminatorMap = discriminatorMap;
        }

        public Discriminator build() {
            if (!$assertionsDisabled && this.discriminator.resultMapping == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.discriminator.discriminatorMap == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.discriminator.discriminatorMap.isEmpty()) {
                throw new AssertionError();
            }
            this.discriminator.discriminatorMap = Collections.unmodifiableMap(this.discriminator.discriminatorMap);
            return this.discriminator;
        }
    }

    public ResultMapping getResultMapping() {
        return this.resultMapping;
    }

    public Map<String, String> getDiscriminatorMap() {
        return this.discriminatorMap;
    }

    public String getMapIdFor(String s) {
        return this.discriminatorMap.get(s);
    }
}
