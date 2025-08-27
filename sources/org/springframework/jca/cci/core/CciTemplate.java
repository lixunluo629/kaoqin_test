package org.springframework.jca.cci.core;

import java.sql.SQLException;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.Interaction;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;
import javax.resource.cci.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jca.cci.CannotCreateRecordException;
import org.springframework.jca.cci.CciOperationNotSupportedException;
import org.springframework.jca.cci.InvalidResultSetAccessException;
import org.springframework.jca.cci.RecordTypeNotSupportedException;
import org.springframework.jca.cci.connection.ConnectionFactoryUtils;
import org.springframework.jca.cci.connection.NotSupportedRecordFactory;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/cci/core/CciTemplate.class */
public class CciTemplate implements CciOperations {
    private final Log logger = LogFactory.getLog(getClass());
    private ConnectionFactory connectionFactory;
    private ConnectionSpec connectionSpec;
    private RecordCreator outputRecordCreator;

    public CciTemplate() {
    }

    public CciTemplate(ConnectionFactory connectionFactory) {
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

    public CciTemplate(ConnectionFactory connectionFactory, ConnectionSpec connectionSpec) {
        setConnectionFactory(connectionFactory);
        setConnectionSpec(connectionSpec);
        afterPropertiesSet();
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public ConnectionFactory getConnectionFactory() {
        return this.connectionFactory;
    }

    public void setConnectionSpec(ConnectionSpec connectionSpec) {
        this.connectionSpec = connectionSpec;
    }

    public ConnectionSpec getConnectionSpec() {
        return this.connectionSpec;
    }

    public void setOutputRecordCreator(RecordCreator creator) {
        this.outputRecordCreator = creator;
    }

    public RecordCreator getOutputRecordCreator() {
        return this.outputRecordCreator;
    }

    public void afterPropertiesSet() {
        if (getConnectionFactory() == null) {
            throw new IllegalArgumentException("Property 'connectionFactory' is required");
        }
    }

    public CciTemplate getDerivedTemplate(ConnectionSpec connectionSpec) {
        CciTemplate derived = new CciTemplate();
        derived.setConnectionFactory(getConnectionFactory());
        derived.setConnectionSpec(connectionSpec);
        derived.setOutputRecordCreator(getOutputRecordCreator());
        return derived;
    }

    @Override // org.springframework.jca.cci.core.CciOperations
    public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
        Assert.notNull(action, "Callback object must not be null");
        Connection con = ConnectionFactoryUtils.getConnection(getConnectionFactory(), getConnectionSpec());
        try {
            try {
                T tDoInConnection = action.doInConnection(con, getConnectionFactory());
                ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
                return tDoInConnection;
            } catch (NotSupportedException ex) {
                throw new CciOperationNotSupportedException("CCI operation not supported by connector", ex);
            } catch (ResourceException ex2) {
                throw new DataAccessResourceFailureException("CCI operation failed", ex2);
            } catch (SQLException ex3) {
                throw new InvalidResultSetAccessException("Parsing of CCI ResultSet failed", ex3);
            }
        } catch (Throwable th) {
            ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
            throw th;
        }
    }

    @Override // org.springframework.jca.cci.core.CciOperations
    public <T> T execute(final InteractionCallback<T> interactionCallback) throws DataAccessException {
        Assert.notNull(interactionCallback, "Callback object must not be null");
        return (T) execute(new ConnectionCallback<T>() { // from class: org.springframework.jca.cci.core.CciTemplate.1
            @Override // org.springframework.jca.cci.core.ConnectionCallback
            public T doInConnection(Connection connection, ConnectionFactory connectionFactory) throws SQLException, DataAccessException, ResourceException {
                Interaction interactionCreateInteraction = connection.createInteraction();
                try {
                    T t = (T) interactionCallback.doInInteraction(interactionCreateInteraction, connectionFactory);
                    CciTemplate.this.closeInteraction(interactionCreateInteraction);
                    return t;
                } catch (Throwable th) {
                    CciTemplate.this.closeInteraction(interactionCreateInteraction);
                    throw th;
                }
            }
        });
    }

    @Override // org.springframework.jca.cci.core.CciOperations
    public Record execute(InteractionSpec spec, Record inputRecord) throws DataAccessException {
        return (Record) doExecute(spec, inputRecord, null, new SimpleRecordExtractor());
    }

    @Override // org.springframework.jca.cci.core.CciOperations
    public void execute(InteractionSpec spec, Record inputRecord, Record outputRecord) throws DataAccessException {
        doExecute(spec, inputRecord, outputRecord, null);
    }

    @Override // org.springframework.jca.cci.core.CciOperations
    public Record execute(InteractionSpec spec, RecordCreator inputCreator) throws DataAccessException {
        return (Record) doExecute(spec, createRecord(inputCreator), null, new SimpleRecordExtractor());
    }

    @Override // org.springframework.jca.cci.core.CciOperations
    public <T> T execute(InteractionSpec interactionSpec, Record record, RecordExtractor<T> recordExtractor) throws DataAccessException {
        return (T) doExecute(interactionSpec, record, null, recordExtractor);
    }

    @Override // org.springframework.jca.cci.core.CciOperations
    public <T> T execute(InteractionSpec interactionSpec, RecordCreator recordCreator, RecordExtractor<T> recordExtractor) throws DataAccessException {
        return (T) doExecute(interactionSpec, createRecord(recordCreator), null, recordExtractor);
    }

    protected <T> T doExecute(final InteractionSpec interactionSpec, final Record record, final Record record2, final RecordExtractor<T> recordExtractor) throws DataAccessException {
        return (T) execute(new InteractionCallback<T>() { // from class: org.springframework.jca.cci.core.CciTemplate.2
            /* JADX WARN: Finally extract failed */
            @Override // org.springframework.jca.cci.core.InteractionCallback
            public T doInInteraction(Interaction interaction, ConnectionFactory connectionFactory) throws SQLException, DataAccessException, ResourceException {
                Record recordCreateRecord = record2;
                try {
                    if (record2 != null || CciTemplate.this.getOutputRecordCreator() != null) {
                        if (record2 == null) {
                            recordCreateRecord = CciTemplate.this.getOutputRecordCreator().createRecord(CciTemplate.this.getRecordFactory(connectionFactory));
                        }
                        interaction.execute(interactionSpec, record, recordCreateRecord);
                    } else {
                        recordCreateRecord = interaction.execute(interactionSpec, record);
                    }
                    T t = (T) (recordExtractor != null ? recordExtractor.extractData(recordCreateRecord) : null);
                    if (recordCreateRecord instanceof ResultSet) {
                        CciTemplate.this.closeResultSet((ResultSet) recordCreateRecord);
                    }
                    return t;
                } catch (Throwable th) {
                    if (recordCreateRecord instanceof ResultSet) {
                        CciTemplate.this.closeResultSet((ResultSet) recordCreateRecord);
                    }
                    throw th;
                }
            }
        });
    }

    public IndexedRecord createIndexedRecord(String name) throws DataAccessException {
        try {
            RecordFactory recordFactory = getRecordFactory(getConnectionFactory());
            return recordFactory.createIndexedRecord(name);
        } catch (NotSupportedException ex) {
            throw new RecordTypeNotSupportedException("Creation of indexed Record not supported by connector", ex);
        } catch (ResourceException ex2) {
            throw new CannotCreateRecordException("Creation of indexed Record failed", ex2);
        }
    }

    public MappedRecord createMappedRecord(String name) throws DataAccessException {
        try {
            RecordFactory recordFactory = getRecordFactory(getConnectionFactory());
            return recordFactory.createMappedRecord(name);
        } catch (NotSupportedException ex) {
            throw new RecordTypeNotSupportedException("Creation of mapped Record not supported by connector", ex);
        } catch (ResourceException ex2) {
            throw new CannotCreateRecordException("Creation of mapped Record failed", ex2);
        }
    }

    protected Record createRecord(RecordCreator recordCreator) throws DataAccessException {
        try {
            RecordFactory recordFactory = getRecordFactory(getConnectionFactory());
            return recordCreator.createRecord(recordFactory);
        } catch (NotSupportedException ex) {
            throw new RecordTypeNotSupportedException("Creation of the desired Record type not supported by connector", ex);
        } catch (ResourceException ex2) {
            throw new CannotCreateRecordException("Creation of the desired Record failed", ex2);
        }
    }

    protected RecordFactory getRecordFactory(ConnectionFactory connectionFactory) throws ResourceException {
        try {
            return connectionFactory.getRecordFactory();
        } catch (NotSupportedException e) {
            return new NotSupportedRecordFactory();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeInteraction(Interaction interaction) {
        if (interaction != null) {
            try {
                interaction.close();
            } catch (ResourceException e) {
                this.logger.trace("Could not close CCI Interaction", e);
            } catch (Throwable ex) {
                this.logger.trace("Unexpected exception on closing CCI Interaction", ex);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                this.logger.trace("Could not close CCI ResultSet", ex);
            } catch (Throwable ex2) {
                this.logger.trace("Unexpected exception on closing CCI ResultSet", ex2);
            }
        }
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/cci/core/CciTemplate$SimpleRecordExtractor.class */
    private static class SimpleRecordExtractor implements RecordExtractor<Record> {
        private SimpleRecordExtractor() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.jca.cci.core.RecordExtractor
        public Record extractData(Record record) {
            return record;
        }
    }
}
