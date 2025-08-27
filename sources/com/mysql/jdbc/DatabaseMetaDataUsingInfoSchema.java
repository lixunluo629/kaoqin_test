package com.mysql.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaDataUsingInfoSchema.class */
public class DatabaseMetaDataUsingInfoSchema extends DatabaseMetaData {
    private boolean hasReferentialConstraintsView;
    private final boolean hasParametersView;

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaDataUsingInfoSchema$JDBC4FunctionConstant.class */
    protected enum JDBC4FunctionConstant {
        FUNCTION_COLUMN_UNKNOWN,
        FUNCTION_COLUMN_IN,
        FUNCTION_COLUMN_INOUT,
        FUNCTION_COLUMN_OUT,
        FUNCTION_COLUMN_RETURN,
        FUNCTION_COLUMN_RESULT,
        FUNCTION_NO_NULLS,
        FUNCTION_NULLABLE,
        FUNCTION_NULLABLE_UNKNOWN
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    protected DatabaseMetaDataUsingInfoSchema(com.mysql.jdbc.MySQLConnection r7, java.lang.String r8) throws java.sql.SQLException {
        /*
            r6 = this;
            r0 = r6
            r1 = r7
            r2 = r8
            r0.<init>(r1, r2)
            r0 = r6
            r1 = r6
            com.mysql.jdbc.MySQLConnection r1 = r1.conn
            r2 = 5
            r3 = 1
            r4 = 10
            boolean r1 = r1.versionMeetsMinimum(r2, r3, r4)
            r0.hasReferentialConstraintsView = r1
            r0 = 0
            r9 = r0
            r0 = r6
            java.lang.String r1 = "INFORMATION_SCHEMA"
            r2 = 0
            java.lang.String r3 = "PARAMETERS"
            r4 = 0
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: java.lang.Throwable -> L37
            java.sql.ResultSet r0 = super.getTables(r1, r2, r3, r4)     // Catch: java.lang.Throwable -> L37
            r9 = r0
            r0 = r6
            r1 = r9
            boolean r1 = r1.next()     // Catch: java.lang.Throwable -> L37
            r0.hasParametersView = r1     // Catch: java.lang.Throwable -> L37
            r0 = jsr -> L3f
        L34:
            goto L4d
        L37:
            r10 = move-exception
            r0 = jsr -> L3f
        L3c:
            r1 = r10
            throw r1
        L3f:
            r11 = r0
            r0 = r9
            if (r0 == 0) goto L4b
            r0 = r9
            r0.close()
        L4b:
            ret r11
        L4d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.<init>(com.mysql.jdbc.MySQLConnection, java.lang.String):void");
    }

    protected ResultSet executeMetadataQuery(java.sql.PreparedStatement pStmt) throws SQLException {
        ResultSet rs = pStmt.executeQuery();
        ((ResultSetInternalMethods) rs).setOwningStatement(null);
        return rs;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getColumnPrivileges(java.lang.String r12, java.lang.String r13, java.lang.String r14, java.lang.String r15) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 310
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getColumns(java.lang.String r5, java.lang.String r6, java.lang.String r7, java.lang.String r8) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 564
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getCrossReference(java.lang.String r5, java.lang.String r6, java.lang.String r7, java.lang.String r8, java.lang.String r9, java.lang.String r10) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 333
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getCrossReference(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getExportedKeys(java.lang.String r5, java.lang.String r6, java.lang.String r7) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 272
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getExportedKeys(java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    private String generateOptionalRefContraintsJoin() {
        return this.hasReferentialConstraintsView ? "JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS R ON (R.CONSTRAINT_NAME = B.CONSTRAINT_NAME AND R.TABLE_NAME = B.TABLE_NAME AND R.CONSTRAINT_SCHEMA = B.TABLE_SCHEMA) " : "";
    }

    private String generateDeleteRuleClause() {
        return this.hasReferentialConstraintsView ? "CASE WHEN R.DELETE_RULE='CASCADE' THEN " + String.valueOf(0) + " WHEN R.DELETE_RULE='SET NULL' THEN " + String.valueOf(2) + " WHEN R.DELETE_RULE='SET DEFAULT' THEN " + String.valueOf(4) + " WHEN R.DELETE_RULE='RESTRICT' THEN " + String.valueOf(1) + " WHEN R.DELETE_RULE='NO ACTION' THEN " + String.valueOf(3) + " ELSE " + String.valueOf(3) + " END " : String.valueOf(1);
    }

    private String generateUpdateRuleClause() {
        return this.hasReferentialConstraintsView ? "CASE WHEN R.UPDATE_RULE='CASCADE' THEN " + String.valueOf(0) + " WHEN R.UPDATE_RULE='SET NULL' THEN " + String.valueOf(2) + " WHEN R.UPDATE_RULE='SET DEFAULT' THEN " + String.valueOf(4) + " WHEN R.UPDATE_RULE='RESTRICT' THEN " + String.valueOf(1) + " WHEN R.UPDATE_RULE='NO ACTION' THEN " + String.valueOf(3) + " ELSE " + String.valueOf(3) + " END " : String.valueOf(1);
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getImportedKeys(java.lang.String r5, java.lang.String r6, java.lang.String r7) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 282
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getImportedKeys(java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getIndexInfo(java.lang.String r5, java.lang.String r6, java.lang.String r7, boolean r8, boolean r9) throws java.sql.SQLException {
        /*
            r4 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            java.lang.String r2 = "SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, NON_UNIQUE,"
            r1.<init>(r2)
            r10 = r0
            r0 = r10
            java.lang.String r1 = "TABLE_SCHEMA AS INDEX_QUALIFIER, INDEX_NAME,3 AS TYPE, SEQ_IN_INDEX AS ORDINAL_POSITION, COLUMN_NAME,"
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r10
            java.lang.String r1 = "COLLATION AS ASC_OR_DESC, CARDINALITY, NULL AS PAGES, NULL AS FILTER_CONDITION FROM INFORMATION_SCHEMA.STATISTICS WHERE "
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r10
            java.lang.String r1 = "TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ?"
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r8
            if (r0 == 0) goto L30
            r0 = r10
            java.lang.String r1 = " AND NON_UNIQUE=0 "
            java.lang.StringBuilder r0 = r0.append(r1)
        L30:
            r0 = r10
            java.lang.String r1 = "ORDER BY NON_UNIQUE, INDEX_NAME, SEQ_IN_INDEX"
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = 0
            r11 = r0
            r0 = r5
            if (r0 != 0) goto L50
            r0 = r4
            com.mysql.jdbc.MySQLConnection r0 = r0.conn     // Catch: java.lang.Throwable -> L9e
            boolean r0 = r0.getNullCatalogMeansCurrent()     // Catch: java.lang.Throwable -> L9e
            if (r0 == 0) goto L50
            r0 = r4
            java.lang.String r0 = r0.database     // Catch: java.lang.Throwable -> L9e
            r5 = r0
        L50:
            r0 = r4
            r1 = r10
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L9e
            java.sql.PreparedStatement r0 = r0.prepareMetaDataSafeStatement(r1)     // Catch: java.lang.Throwable -> L9e
            r11 = r0
            r0 = r5
            if (r0 == 0) goto L6b
            r0 = r11
            r1 = 1
            r2 = r5
            r0.setString(r1, r2)     // Catch: java.lang.Throwable -> L9e
            goto L75
        L6b:
            r0 = r11
            r1 = 1
            java.lang.String r2 = "%"
            r0.setString(r1, r2)     // Catch: java.lang.Throwable -> L9e
        L75:
            r0 = r11
            r1 = 2
            r2 = r7
            r0.setString(r1, r2)     // Catch: java.lang.Throwable -> L9e
            r0 = r4
            r1 = r11
            java.sql.ResultSet r0 = r0.executeMetadataQuery(r1)     // Catch: java.lang.Throwable -> L9e
            r12 = r0
            r0 = r12
            com.mysql.jdbc.ResultSetInternalMethods r0 = (com.mysql.jdbc.ResultSetInternalMethods) r0     // Catch: java.lang.Throwable -> L9e
            r1 = r4
            com.mysql.jdbc.Field[] r1 = r1.createIndexInfoFields()     // Catch: java.lang.Throwable -> L9e
            r0.redefineFieldsForDBMD(r1)     // Catch: java.lang.Throwable -> L9e
            r0 = r12
            r13 = r0
            r0 = jsr -> La6
        L9b:
            r1 = r13
            return r1
        L9e:
            r14 = move-exception
            r0 = jsr -> La6
        La3:
            r1 = r14
            throw r1
        La6:
            r15 = r0
            r0 = r11
            if (r0 == 0) goto Lb4
            r0 = r11
            r0.close()
        Lb4:
            ret r15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean, boolean):java.sql.ResultSet");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getPrimaryKeys(java.lang.String r12, java.lang.String r13, java.lang.String r14) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 246
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getProcedures(java.lang.String r5, java.lang.String r6, java.lang.String r7) throws java.sql.SQLException {
        /*
            r4 = this;
            r0 = r7
            if (r0 == 0) goto Lb
            r0 = r7
            int r0 = r0.length()
            if (r0 != 0) goto L29
        Lb:
            r0 = r4
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            boolean r0 = r0.getNullNamePatternMatchesAll()
            if (r0 == 0) goto L1d
            java.lang.String r0 = "%"
            r7 = r0
            goto L29
        L1d:
            java.lang.String r0 = "Procedure name pattern can not be NULL or empty."
            java.lang.String r1 = "S1009"
            r2 = r4
            com.mysql.jdbc.ExceptionInterceptor r2 = r2.getExceptionInterceptor()
            java.sql.SQLException r0 = com.mysql.jdbc.SQLError.createSQLException(r0, r1, r2)
            throw r0
        L29:
            r0 = 0
            r8 = r0
            r0 = r5
            if (r0 != 0) goto L45
            r0 = r4
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            boolean r0 = r0.getNullCatalogMeansCurrent()
            if (r0 == 0) goto L48
            r0 = r4
            java.lang.String r0 = r0.database
            r8 = r0
            goto L48
        L45:
            r0 = r5
            r8 = r0
        L48:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "SELECT ROUTINE_SCHEMA AS PROCEDURE_CAT, NULL AS PROCEDURE_SCHEM, ROUTINE_NAME AS PROCEDURE_NAME, NULL AS RESERVED_1, NULL AS RESERVED_2, NULL AS RESERVED_3, ROUTINE_COMMENT AS REMARKS, CASE WHEN ROUTINE_TYPE = 'PROCEDURE' THEN 1 WHEN ROUTINE_TYPE='FUNCTION' THEN 2 ELSE 0 END AS PROCEDURE_TYPE, ROUTINE_NAME AS SPECIFIC_NAME FROM INFORMATION_SCHEMA.ROUTINES WHERE "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r4
            java.lang.String r1 = r1.getRoutineTypeConditionForGetProcedures()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = "ROUTINE_SCHEMA LIKE ? AND ROUTINE_NAME LIKE ? ORDER BY ROUTINE_SCHEMA, ROUTINE_NAME, ROUTINE_TYPE"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r9 = r0
            r0 = 0
            r10 = r0
            r0 = r4
            r1 = r9
            java.sql.PreparedStatement r0 = r0.prepareMetaDataSafeStatement(r1)     // Catch: java.lang.Throwable -> Lb5
            r10 = r0
            r0 = r8
            if (r0 == 0) goto L82
            r0 = r10
            r1 = 1
            r2 = r8
            r0.setString(r1, r2)     // Catch: java.lang.Throwable -> Lb5
            goto L8c
        L82:
            r0 = r10
            r1 = 1
            java.lang.String r2 = "%"
            r0.setString(r1, r2)     // Catch: java.lang.Throwable -> Lb5
        L8c:
            r0 = r10
            r1 = 2
            r2 = r7
            r0.setString(r1, r2)     // Catch: java.lang.Throwable -> Lb5
            r0 = r4
            r1 = r10
            java.sql.ResultSet r0 = r0.executeMetadataQuery(r1)     // Catch: java.lang.Throwable -> Lb5
            r11 = r0
            r0 = r11
            com.mysql.jdbc.ResultSetInternalMethods r0 = (com.mysql.jdbc.ResultSetInternalMethods) r0     // Catch: java.lang.Throwable -> Lb5
            r1 = r4
            com.mysql.jdbc.Field[] r1 = r1.createFieldMetadataForGetProcedures()     // Catch: java.lang.Throwable -> Lb5
            r0.redefineFieldsForDBMD(r1)     // Catch: java.lang.Throwable -> Lb5
            r0 = r11
            r12 = r0
            r0 = jsr -> Lbd
        Lb2:
            r1 = r12
            return r1
        Lb5:
            r13 = move-exception
            r0 = jsr -> Lbd
        Lba:
            r1 = r13
            throw r1
        Lbd:
            r14 = r0
            r0 = r10
            if (r0 == 0) goto Lcb
            r0 = r10
            r0.close()
        Lcb:
            ret r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getProcedures(java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    protected String getRoutineTypeConditionForGetProcedures() {
        return "";
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getProcedureColumns(java.lang.String r7, java.lang.String r8, java.lang.String r9, java.lang.String r10) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 333
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getProcedureColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    protected ResultSet getProcedureColumnsNoISParametersView(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        return super.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
    }

    protected String getRoutineTypeConditionForGetProcedureColumns() {
        return "";
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getTables(java.lang.String r6, java.lang.String r7, java.lang.String r8, java.lang.String[] r9) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 644
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[]):java.sql.ResultSet");
    }

    public boolean gethasParametersView() {
        return this.hasParametersView;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getVersionColumns(java.lang.String r12, java.lang.String r13, java.lang.String r14) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 364
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getVersionColumns(java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getFunctionColumns(java.lang.String r7, java.lang.String r8, java.lang.String r9, java.lang.String r10) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 459
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    protected int getJDBC4FunctionConstant(JDBC4FunctionConstant constant) {
        return 0;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public java.sql.ResultSet getFunctions(java.lang.String r12, java.lang.String r13, java.lang.String r14) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 315
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema.getFunctions(java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    @Override // com.mysql.jdbc.DatabaseMetaData
    protected int getJDBC4FunctionNoTableConstant() {
        return 0;
    }
}
