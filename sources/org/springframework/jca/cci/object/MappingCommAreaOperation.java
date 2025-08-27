package org.springframework.jca.cci.object;

import java.io.IOException;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jca.cci.core.support.CommAreaRecord;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/cci/object/MappingCommAreaOperation.class */
public abstract class MappingCommAreaOperation extends MappingRecordOperation {
    protected abstract byte[] objectToBytes(Object obj) throws DataAccessException, IOException;

    protected abstract Object bytesToObject(byte[] bArr) throws DataAccessException, IOException;

    public MappingCommAreaOperation() {
    }

    public MappingCommAreaOperation(ConnectionFactory connectionFactory, InteractionSpec interactionSpec) {
        super(connectionFactory, interactionSpec);
    }

    @Override // org.springframework.jca.cci.object.MappingRecordOperation
    protected final Record createInputRecord(RecordFactory recordFactory, Object inObject) {
        try {
            return new CommAreaRecord(objectToBytes(inObject));
        } catch (IOException ex) {
            throw new DataRetrievalFailureException("I/O exception during bytes conversion", ex);
        }
    }

    @Override // org.springframework.jca.cci.object.MappingRecordOperation
    protected final Object extractOutputData(Record record) throws DataAccessException {
        CommAreaRecord commAreaRecord = (CommAreaRecord) record;
        try {
            return bytesToObject(commAreaRecord.toByteArray());
        } catch (IOException ex) {
            throw new DataRetrievalFailureException("I/O exception during bytes conversion", ex);
        }
    }
}
