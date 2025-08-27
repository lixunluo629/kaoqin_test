package org.springframework.jdbc.support.nativejdbc;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/nativejdbc/SimpleNativeJdbcExtractor.class */
public class SimpleNativeJdbcExtractor extends NativeJdbcExtractorAdapter {
    private boolean nativeConnectionNecessaryForNativeStatements = false;
    private boolean nativeConnectionNecessaryForNativePreparedStatements = false;
    private boolean nativeConnectionNecessaryForNativeCallableStatements = false;

    public void setNativeConnectionNecessaryForNativeStatements(boolean nativeConnectionNecessaryForNativeStatements) {
        this.nativeConnectionNecessaryForNativeStatements = nativeConnectionNecessaryForNativeStatements;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public boolean isNativeConnectionNecessaryForNativeStatements() {
        return this.nativeConnectionNecessaryForNativeStatements;
    }

    public void setNativeConnectionNecessaryForNativePreparedStatements(boolean nativeConnectionNecessary) {
        this.nativeConnectionNecessaryForNativePreparedStatements = nativeConnectionNecessary;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public boolean isNativeConnectionNecessaryForNativePreparedStatements() {
        return this.nativeConnectionNecessaryForNativePreparedStatements;
    }

    public void setNativeConnectionNecessaryForNativeCallableStatements(boolean nativeConnectionNecessary) {
        this.nativeConnectionNecessaryForNativeCallableStatements = nativeConnectionNecessary;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public boolean isNativeConnectionNecessaryForNativeCallableStatements() {
        return this.nativeConnectionNecessaryForNativeCallableStatements;
    }
}
