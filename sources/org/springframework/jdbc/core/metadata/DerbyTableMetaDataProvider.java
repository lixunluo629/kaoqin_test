package org.springframework.jdbc.core.metadata;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/DerbyTableMetaDataProvider.class */
public class DerbyTableMetaDataProvider extends GenericTableMetaDataProvider {
    private boolean supportsGeneratedKeysOverride;

    public DerbyTableMetaDataProvider(DatabaseMetaData databaseMetaData) throws SQLException {
        super(databaseMetaData);
        this.supportsGeneratedKeysOverride = false;
    }

    @Override // org.springframework.jdbc.core.metadata.GenericTableMetaDataProvider, org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public void initializeWithMetaData(DatabaseMetaData databaseMetaData) throws SQLException {
        super.initializeWithMetaData(databaseMetaData);
        if (!databaseMetaData.supportsGetGeneratedKeys()) {
            if (logger.isWarnEnabled()) {
                logger.warn("Overriding supportsGetGeneratedKeys from DatabaseMetaData to 'true'; it was reported as 'false' by " + databaseMetaData.getDriverName() + SymbolConstants.SPACE_SYMBOL + databaseMetaData.getDriverVersion());
            }
            this.supportsGeneratedKeysOverride = true;
        }
    }

    @Override // org.springframework.jdbc.core.metadata.GenericTableMetaDataProvider, org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public boolean isGetGeneratedKeysSupported() {
        return super.isGetGeneratedKeysSupported() || this.supportsGeneratedKeysOverride;
    }
}
