package com.zaxxer.hikari.pool;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.Wrapper;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/HikariProxyDatabaseMetaData.class */
public class HikariProxyDatabaseMetaData extends ProxyDatabaseMetaData implements Wrapper, DatabaseMetaData {
    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class cls) throws SQLException {
        try {
            return this.delegate.isWrapperFor(cls);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean allProceduresAreCallable() throws SQLException {
        try {
            return this.delegate.allProceduresAreCallable();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean allTablesAreSelectable() throws SQLException {
        try {
            return this.delegate.allTablesAreSelectable();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getURL() throws SQLException {
        try {
            return this.delegate.getURL();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getUserName() throws SQLException {
        try {
            return this.delegate.getUserName();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean isReadOnly() throws SQLException {
        try {
            return this.delegate.isReadOnly();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedHigh() throws SQLException {
        try {
            return this.delegate.nullsAreSortedHigh();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedLow() throws SQLException {
        try {
            return this.delegate.nullsAreSortedLow();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedAtStart() throws SQLException {
        try {
            return this.delegate.nullsAreSortedAtStart();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedAtEnd() throws SQLException {
        try {
            return this.delegate.nullsAreSortedAtEnd();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getDatabaseProductName() throws SQLException {
        try {
            return this.delegate.getDatabaseProductName();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getDatabaseProductVersion() throws SQLException {
        try {
            return this.delegate.getDatabaseProductVersion();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getDriverName() throws SQLException {
        try {
            return this.delegate.getDriverName();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getDriverVersion() throws SQLException {
        try {
            return this.delegate.getDriverVersion();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getDriverMajorVersion() {
        return this.delegate.getDriverMajorVersion();
    }

    @Override // java.sql.DatabaseMetaData
    public int getDriverMinorVersion() {
        return this.delegate.getDriverMinorVersion();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean usesLocalFiles() throws SQLException {
        try {
            return this.delegate.usesLocalFiles();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean usesLocalFilePerTable() throws SQLException {
        try {
            return this.delegate.usesLocalFilePerTable();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        try {
            return this.delegate.supportsMixedCaseIdentifiers();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        try {
            return this.delegate.storesUpperCaseIdentifiers();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        try {
            return this.delegate.storesLowerCaseIdentifiers();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        try {
            return this.delegate.storesMixedCaseIdentifiers();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        try {
            return this.delegate.supportsMixedCaseQuotedIdentifiers();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        try {
            return this.delegate.storesUpperCaseQuotedIdentifiers();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        try {
            return this.delegate.storesLowerCaseQuotedIdentifiers();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        try {
            return this.delegate.storesMixedCaseQuotedIdentifiers();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getIdentifierQuoteString() throws SQLException {
        try {
            return this.delegate.getIdentifierQuoteString();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getSQLKeywords() throws SQLException {
        try {
            return this.delegate.getSQLKeywords();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getNumericFunctions() throws SQLException {
        try {
            return this.delegate.getNumericFunctions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getStringFunctions() throws SQLException {
        try {
            return this.delegate.getStringFunctions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getSystemFunctions() throws SQLException {
        try {
            return this.delegate.getSystemFunctions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getTimeDateFunctions() throws SQLException {
        try {
            return this.delegate.getTimeDateFunctions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getSearchStringEscape() throws SQLException {
        try {
            return this.delegate.getSearchStringEscape();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getExtraNameCharacters() throws SQLException {
        try {
            return this.delegate.getExtraNameCharacters();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        try {
            return this.delegate.supportsAlterTableWithAddColumn();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        try {
            return this.delegate.supportsAlterTableWithDropColumn();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsColumnAliasing() throws SQLException {
        try {
            return this.delegate.supportsColumnAliasing();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullPlusNonNullIsNull() throws SQLException {
        try {
            return this.delegate.nullPlusNonNullIsNull();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsConvert() throws SQLException {
        try {
            return this.delegate.supportsConvert();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsConvert(int i, int i2) throws SQLException {
        try {
            return this.delegate.supportsConvert(i, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsTableCorrelationNames() throws SQLException {
        try {
            return this.delegate.supportsTableCorrelationNames();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        try {
            return this.delegate.supportsDifferentTableCorrelationNames();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        try {
            return this.delegate.supportsExpressionsInOrderBy();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOrderByUnrelated() throws SQLException {
        try {
            return this.delegate.supportsOrderByUnrelated();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGroupBy() throws SQLException {
        try {
            return this.delegate.supportsGroupBy();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGroupByUnrelated() throws SQLException {
        try {
            return this.delegate.supportsGroupByUnrelated();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        try {
            return this.delegate.supportsGroupByBeyondSelect();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsLikeEscapeClause() throws SQLException {
        try {
            return this.delegate.supportsLikeEscapeClause();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMultipleResultSets() throws SQLException {
        try {
            return this.delegate.supportsMultipleResultSets();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMultipleTransactions() throws SQLException {
        try {
            return this.delegate.supportsMultipleTransactions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsNonNullableColumns() throws SQLException {
        try {
            return this.delegate.supportsNonNullableColumns();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        try {
            return this.delegate.supportsMinimumSQLGrammar();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCoreSQLGrammar() throws SQLException {
        try {
            return this.delegate.supportsCoreSQLGrammar();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        try {
            return this.delegate.supportsExtendedSQLGrammar();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        try {
            return this.delegate.supportsANSI92EntryLevelSQL();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        try {
            return this.delegate.supportsANSI92IntermediateSQL();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsANSI92FullSQL() throws SQLException {
        try {
            return this.delegate.supportsANSI92FullSQL();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        try {
            return this.delegate.supportsIntegrityEnhancementFacility();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOuterJoins() throws SQLException {
        try {
            return this.delegate.supportsOuterJoins();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsFullOuterJoins() throws SQLException {
        try {
            return this.delegate.supportsFullOuterJoins();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsLimitedOuterJoins() throws SQLException {
        try {
            return this.delegate.supportsLimitedOuterJoins();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getSchemaTerm() throws SQLException {
        try {
            return this.delegate.getSchemaTerm();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getProcedureTerm() throws SQLException {
        try {
            return this.delegate.getProcedureTerm();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getCatalogTerm() throws SQLException {
        try {
            return this.delegate.getCatalogTerm();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean isCatalogAtStart() throws SQLException {
        try {
            return this.delegate.isCatalogAtStart();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public String getCatalogSeparator() throws SQLException {
        try {
            return this.delegate.getCatalogSeparator();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        try {
            return this.delegate.supportsSchemasInDataManipulation();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        try {
            return this.delegate.supportsSchemasInProcedureCalls();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        try {
            return this.delegate.supportsSchemasInTableDefinitions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        try {
            return this.delegate.supportsSchemasInIndexDefinitions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        try {
            return this.delegate.supportsSchemasInPrivilegeDefinitions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        try {
            return this.delegate.supportsCatalogsInDataManipulation();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        try {
            return this.delegate.supportsCatalogsInProcedureCalls();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        try {
            return this.delegate.supportsCatalogsInTableDefinitions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        try {
            return this.delegate.supportsCatalogsInIndexDefinitions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        try {
            return this.delegate.supportsCatalogsInPrivilegeDefinitions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsPositionedDelete() throws SQLException {
        try {
            return this.delegate.supportsPositionedDelete();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsPositionedUpdate() throws SQLException {
        try {
            return this.delegate.supportsPositionedUpdate();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSelectForUpdate() throws SQLException {
        try {
            return this.delegate.supportsSelectForUpdate();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsStoredProcedures() throws SQLException {
        try {
            return this.delegate.supportsStoredProcedures();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        try {
            return this.delegate.supportsSubqueriesInComparisons();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInExists() throws SQLException {
        try {
            return this.delegate.supportsSubqueriesInExists();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInIns() throws SQLException {
        try {
            return this.delegate.supportsSubqueriesInIns();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        try {
            return this.delegate.supportsSubqueriesInQuantifieds();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        try {
            return this.delegate.supportsCorrelatedSubqueries();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsUnion() throws SQLException {
        try {
            return this.delegate.supportsUnion();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsUnionAll() throws SQLException {
        try {
            return this.delegate.supportsUnionAll();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        try {
            return this.delegate.supportsOpenCursorsAcrossCommit();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        try {
            return this.delegate.supportsOpenCursorsAcrossRollback();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        try {
            return this.delegate.supportsOpenStatementsAcrossCommit();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        try {
            return this.delegate.supportsOpenStatementsAcrossRollback();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxBinaryLiteralLength() throws SQLException {
        try {
            return this.delegate.getMaxBinaryLiteralLength();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxCharLiteralLength() throws SQLException {
        try {
            return this.delegate.getMaxCharLiteralLength();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnNameLength() throws SQLException {
        try {
            return this.delegate.getMaxColumnNameLength();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInGroupBy() throws SQLException {
        try {
            return this.delegate.getMaxColumnsInGroupBy();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInIndex() throws SQLException {
        try {
            return this.delegate.getMaxColumnsInIndex();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInOrderBy() throws SQLException {
        try {
            return this.delegate.getMaxColumnsInOrderBy();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInSelect() throws SQLException {
        try {
            return this.delegate.getMaxColumnsInSelect();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInTable() throws SQLException {
        try {
            return this.delegate.getMaxColumnsInTable();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxConnections() throws SQLException {
        try {
            return this.delegate.getMaxConnections();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxCursorNameLength() throws SQLException {
        try {
            return this.delegate.getMaxCursorNameLength();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxIndexLength() throws SQLException {
        try {
            return this.delegate.getMaxIndexLength();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxSchemaNameLength() throws SQLException {
        try {
            return this.delegate.getMaxSchemaNameLength();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxProcedureNameLength() throws SQLException {
        try {
            return this.delegate.getMaxProcedureNameLength();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxCatalogNameLength() throws SQLException {
        try {
            return this.delegate.getMaxCatalogNameLength();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxRowSize() throws SQLException {
        try {
            return this.delegate.getMaxRowSize();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        try {
            return this.delegate.doesMaxRowSizeIncludeBlobs();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxStatementLength() throws SQLException {
        try {
            return this.delegate.getMaxStatementLength();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxStatements() throws SQLException {
        try {
            return this.delegate.getMaxStatements();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxTableNameLength() throws SQLException {
        try {
            return this.delegate.getMaxTableNameLength();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxTablesInSelect() throws SQLException {
        try {
            return this.delegate.getMaxTablesInSelect();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxUserNameLength() throws SQLException {
        try {
            return this.delegate.getMaxUserNameLength();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getDefaultTransactionIsolation() throws SQLException {
        try {
            return this.delegate.getDefaultTransactionIsolation();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsTransactions() throws SQLException {
        try {
            return this.delegate.supportsTransactions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsTransactionIsolationLevel(int i) throws SQLException {
        try {
            return this.delegate.supportsTransactionIsolationLevel(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        try {
            return this.delegate.supportsDataDefinitionAndDataManipulationTransactions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        try {
            return this.delegate.supportsDataManipulationTransactionsOnly();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        try {
            return this.delegate.dataDefinitionCausesTransactionCommit();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        try {
            return this.delegate.dataDefinitionIgnoredInTransactions();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getProcedures(String str, String str2, String str3) throws SQLException {
        try {
            return super.getProcedures(str, str2, str3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getProcedureColumns(String str, String str2, String str3, String str4) throws SQLException {
        try {
            return super.getProcedureColumns(str, str2, str3, str4);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getTables(String str, String str2, String str3, String[] strArr) throws SQLException {
        try {
            return super.getTables(str, str2, str3, strArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getSchemas() throws SQLException {
        try {
            return super.getSchemas();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getCatalogs() throws SQLException {
        try {
            return super.getCatalogs();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getTableTypes() throws SQLException {
        try {
            return super.getTableTypes();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getColumns(String str, String str2, String str3, String str4) throws SQLException {
        try {
            return super.getColumns(str, str2, str3, str4);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getColumnPrivileges(String str, String str2, String str3, String str4) throws SQLException {
        try {
            return super.getColumnPrivileges(str, str2, str3, str4);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getTablePrivileges(String str, String str2, String str3) throws SQLException {
        try {
            return super.getTablePrivileges(str, str2, str3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getBestRowIdentifier(String str, String str2, String str3, int i, boolean z) throws SQLException {
        try {
            return super.getBestRowIdentifier(str, str2, str3, i, z);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getVersionColumns(String str, String str2, String str3) throws SQLException {
        try {
            return super.getVersionColumns(str, str2, str3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getPrimaryKeys(String str, String str2, String str3) throws SQLException {
        try {
            return super.getPrimaryKeys(str, str2, str3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getImportedKeys(String str, String str2, String str3) throws SQLException {
        try {
            return super.getImportedKeys(str, str2, str3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getExportedKeys(String str, String str2, String str3) throws SQLException {
        try {
            return super.getExportedKeys(str, str2, str3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getCrossReference(String str, String str2, String str3, String str4, String str5, String str6) throws SQLException {
        try {
            return super.getCrossReference(str, str2, str3, str4, str5, str6);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getTypeInfo() throws SQLException {
        try {
            return super.getTypeInfo();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getIndexInfo(String str, String str2, String str3, boolean z, boolean z2) throws SQLException {
        try {
            return super.getIndexInfo(str, str2, str3, z, z2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsResultSetType(int i) throws SQLException {
        try {
            return this.delegate.supportsResultSetType(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsResultSetConcurrency(int i, int i2) throws SQLException {
        try {
            return this.delegate.supportsResultSetConcurrency(i, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean ownUpdatesAreVisible(int i) throws SQLException {
        try {
            return this.delegate.ownUpdatesAreVisible(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean ownDeletesAreVisible(int i) throws SQLException {
        try {
            return this.delegate.ownDeletesAreVisible(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean ownInsertsAreVisible(int i) throws SQLException {
        try {
            return this.delegate.ownInsertsAreVisible(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean othersUpdatesAreVisible(int i) throws SQLException {
        try {
            return this.delegate.othersUpdatesAreVisible(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean othersDeletesAreVisible(int i) throws SQLException {
        try {
            return this.delegate.othersDeletesAreVisible(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean othersInsertsAreVisible(int i) throws SQLException {
        try {
            return this.delegate.othersInsertsAreVisible(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean updatesAreDetected(int i) throws SQLException {
        try {
            return this.delegate.updatesAreDetected(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean deletesAreDetected(int i) throws SQLException {
        try {
            return this.delegate.deletesAreDetected(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean insertsAreDetected(int i) throws SQLException {
        try {
            return this.delegate.insertsAreDetected(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsBatchUpdates() throws SQLException {
        try {
            return this.delegate.supportsBatchUpdates();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getUDTs(String str, String str2, String str3, int[] iArr) throws SQLException {
        try {
            return super.getUDTs(str, str2, str3, iArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSavepoints() throws SQLException {
        try {
            return this.delegate.supportsSavepoints();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsNamedParameters() throws SQLException {
        try {
            return this.delegate.supportsNamedParameters();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMultipleOpenResults() throws SQLException {
        try {
            return this.delegate.supportsMultipleOpenResults();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGetGeneratedKeys() throws SQLException {
        try {
            return this.delegate.supportsGetGeneratedKeys();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getSuperTypes(String str, String str2, String str3) throws SQLException {
        try {
            return super.getSuperTypes(str, str2, str3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getSuperTables(String str, String str2, String str3) throws SQLException {
        try {
            return super.getSuperTables(str, str2, str3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getAttributes(String str, String str2, String str3, String str4) throws SQLException {
        try {
            return super.getAttributes(str, str2, str3, str4);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsResultSetHoldability(int i) throws SQLException {
        try {
            return this.delegate.supportsResultSetHoldability(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getResultSetHoldability() throws SQLException {
        try {
            return this.delegate.getResultSetHoldability();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getDatabaseMajorVersion() throws SQLException {
        try {
            return this.delegate.getDatabaseMajorVersion();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getDatabaseMinorVersion() throws SQLException {
        try {
            return this.delegate.getDatabaseMinorVersion();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getJDBCMajorVersion() throws SQLException {
        try {
            return this.delegate.getJDBCMajorVersion();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getJDBCMinorVersion() throws SQLException {
        try {
            return this.delegate.getJDBCMinorVersion();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getSQLStateType() throws SQLException {
        try {
            return this.delegate.getSQLStateType();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean locatorsUpdateCopy() throws SQLException {
        try {
            return this.delegate.locatorsUpdateCopy();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsStatementPooling() throws SQLException {
        try {
            return this.delegate.supportsStatementPooling();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        try {
            return this.delegate.getRowIdLifetime();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getSchemas(String str, String str2) throws SQLException {
        try {
            return super.getSchemas(str, str2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        try {
            return this.delegate.supportsStoredFunctionsUsingCallSyntax();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        try {
            return this.delegate.autoCommitFailureClosesAllResultSets();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getClientInfoProperties() throws SQLException {
        try {
            return super.getClientInfoProperties();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getFunctions(String str, String str2, String str3) throws SQLException {
        try {
            return super.getFunctions(str, str2, str3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getFunctionColumns(String str, String str2, String str3, String str4) throws SQLException {
        try {
            return super.getFunctionColumns(str, str2, str3, str4);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyDatabaseMetaData
    public ResultSet getPseudoColumns(String str, String str2, String str3, String str4) throws SQLException {
        try {
            return super.getPseudoColumns(str, str2, str3, str4);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public boolean generatedKeyAlwaysReturned() throws SQLException {
        try {
            return this.delegate.generatedKeyAlwaysReturned();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public long getMaxLogicalLobSize() throws SQLException {
        try {
            return this.delegate.getMaxLogicalLobSize();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public boolean supportsRefCursors() throws SQLException {
        try {
            return this.delegate.supportsRefCursors();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    HikariProxyDatabaseMetaData(ProxyConnection proxyConnection, DatabaseMetaData databaseMetaData) {
        super(proxyConnection, databaseMetaData);
    }
}
