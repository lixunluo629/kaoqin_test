package org.springframework.boot.autoconfigure.data.couchbase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.couchbase.core.query.Consistency;

@ConfigurationProperties(prefix = "spring.data.couchbase")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/couchbase/CouchbaseDataProperties.class */
public class CouchbaseDataProperties {
    private boolean autoIndex;
    private Consistency consistency = Consistency.READ_YOUR_OWN_WRITES;

    public boolean isAutoIndex() {
        return this.autoIndex;
    }

    public void setAutoIndex(boolean autoIndex) {
        this.autoIndex = autoIndex;
    }

    public Consistency getConsistency() {
        return this.consistency;
    }

    public void setConsistency(Consistency consistency) {
        this.consistency = consistency;
    }
}
