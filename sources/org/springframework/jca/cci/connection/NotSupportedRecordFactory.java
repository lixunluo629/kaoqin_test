package org.springframework.jca.cci.connection;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.RecordFactory;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/cci/connection/NotSupportedRecordFactory.class */
public class NotSupportedRecordFactory implements RecordFactory {
    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.resource.NotSupportedException */
    public MappedRecord createMappedRecord(String name) throws ResourceException, NotSupportedException {
        throw new NotSupportedException("The RecordFactory facility is not supported by the connector");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.resource.NotSupportedException */
    public IndexedRecord createIndexedRecord(String name) throws ResourceException, NotSupportedException {
        throw new NotSupportedException("The RecordFactory facility is not supported by the connector");
    }
}
