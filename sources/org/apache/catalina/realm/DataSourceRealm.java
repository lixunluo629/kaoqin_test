package org.apache.catalina.realm;

import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.realm.RealmBase;
import org.apache.naming.ContextBindings;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/realm/DataSourceRealm.class */
public class DataSourceRealm extends RealmBase {

    @Deprecated
    protected static final String name = "DataSourceRealm";
    private String preparedRoles = null;
    private String preparedCredentials = null;
    protected String dataSourceName = null;
    protected boolean localDataSource = false;
    protected String roleNameCol = null;
    protected String userCredCol = null;
    protected String userNameCol = null;
    protected String userRoleTable = null;
    protected String userTable = null;
    private volatile boolean connectionSuccess = true;

    public String getDataSourceName() {
        return this.dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public boolean getLocalDataSource() {
        return this.localDataSource;
    }

    public void setLocalDataSource(boolean localDataSource) {
        this.localDataSource = localDataSource;
    }

    public String getRoleNameCol() {
        return this.roleNameCol;
    }

    public void setRoleNameCol(String roleNameCol) {
        this.roleNameCol = roleNameCol;
    }

    public String getUserCredCol() {
        return this.userCredCol;
    }

    public void setUserCredCol(String userCredCol) {
        this.userCredCol = userCredCol;
    }

    public String getUserNameCol() {
        return this.userNameCol;
    }

    public void setUserNameCol(String userNameCol) {
        this.userNameCol = userNameCol;
    }

    public String getUserRoleTable() {
        return this.userRoleTable;
    }

    public void setUserRoleTable(String userRoleTable) {
        this.userRoleTable = userRoleTable;
    }

    public String getUserTable() {
        return this.userTable;
    }

    public void setUserTable(String userTable) {
        this.userTable = userTable;
    }

    @Override // org.apache.catalina.realm.RealmBase, org.apache.catalina.Realm
    public Principal authenticate(String username, String credentials) throws SQLException {
        Connection dbConnection;
        if (username == null || credentials == null || (dbConnection = open()) == null) {
            return null;
        }
        try {
            Principal principalAuthenticate = authenticate(dbConnection, username, credentials);
            close(dbConnection);
            return principalAuthenticate;
        } catch (Throwable th) {
            close(dbConnection);
            throw th;
        }
    }

    @Override // org.apache.catalina.realm.RealmBase, org.apache.catalina.Realm
    public boolean isAvailable() {
        return this.connectionSuccess;
    }

    protected Principal authenticate(Connection dbConnection, String username, String credentials) throws SQLException {
        if (username == null || credentials == null) {
            if (this.containerLog.isTraceEnabled()) {
                this.containerLog.trace(sm.getString("dataSourceRealm.authenticateFailure", username));
                return null;
            }
            return null;
        }
        String dbCredentials = getPassword(dbConnection, username);
        if (dbCredentials == null) {
            getCredentialHandler().mutate(credentials);
            if (this.containerLog.isTraceEnabled()) {
                this.containerLog.trace(sm.getString("dataSourceRealm.authenticateFailure", username));
                return null;
            }
            return null;
        }
        boolean validated = getCredentialHandler().matches(credentials, dbCredentials);
        if (validated) {
            if (this.containerLog.isTraceEnabled()) {
                this.containerLog.trace(sm.getString("dataSourceRealm.authenticateSuccess", username));
            }
            ArrayList<String> list = getRoles(dbConnection, username);
            return new GenericPrincipal(username, credentials, list);
        }
        if (this.containerLog.isTraceEnabled()) {
            this.containerLog.trace(sm.getString("dataSourceRealm.authenticateFailure", username));
            return null;
        }
        return null;
    }

    protected void close(Connection dbConnection) throws SQLException {
        if (dbConnection == null) {
            return;
        }
        try {
            if (!dbConnection.getAutoCommit()) {
                dbConnection.commit();
            }
        } catch (SQLException e) {
            this.containerLog.error("Exception committing connection before closing:", e);
        }
        try {
            dbConnection.close();
        } catch (SQLException e2) {
            this.containerLog.error(sm.getString("dataSourceRealm.close"), e2);
        }
    }

    protected Connection open() throws SQLException, NamingException {
        Context context;
        try {
            if (this.localDataSource) {
                Context context2 = ContextBindings.getClassLoader();
                context = (Context) context2.lookup("comp/env");
            } else {
                context = getServer().getGlobalNamingContext();
            }
            DataSource dataSource = (DataSource) context.lookup(this.dataSourceName);
            Connection connection = dataSource.getConnection();
            this.connectionSuccess = true;
            return connection;
        } catch (Exception e) {
            this.connectionSuccess = false;
            this.containerLog.error(sm.getString("dataSourceRealm.exception"), e);
            return null;
        }
    }

    @Override // org.apache.catalina.realm.RealmBase
    @Deprecated
    protected String getName() {
        return name;
    }

    @Override // org.apache.catalina.realm.RealmBase
    protected String getPassword(String username) throws SQLException, NamingException {
        Connection dbConnection = open();
        if (dbConnection == null) {
            return null;
        }
        try {
            String password = getPassword(dbConnection, username);
            close(dbConnection);
            return password;
        } catch (Throwable th) {
            close(dbConnection);
            throw th;
        }
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r12v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x00d8: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('stmt' java.sql.PreparedStatement)]) A[TRY_LEAVE], block:B:48:0x00d8 */
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x00dd: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:50:0x00dd */
    /* JADX WARN: Type inference failed for: r12v1, names: [stmt], types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.lang.Throwable] */
    protected String getPassword(Connection dbConnection, String username) throws SQLException {
        String dbCredentials = null;
        try {
            try {
                PreparedStatement preparedStatementPrepareStatement = dbConnection.prepareStatement(this.preparedCredentials);
                Throwable th = null;
                preparedStatementPrepareStatement.setString(1, username);
                ResultSet resultSetExecuteQuery = preparedStatementPrepareStatement.executeQuery();
                Throwable th2 = null;
                try {
                    if (resultSetExecuteQuery.next()) {
                        dbCredentials = resultSetExecuteQuery.getString(1);
                    }
                    String strTrim = dbCredentials != null ? dbCredentials.trim() : null;
                    if (preparedStatementPrepareStatement != null) {
                        if (0 != 0) {
                            try {
                                preparedStatementPrepareStatement.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                        } else {
                            preparedStatementPrepareStatement.close();
                        }
                    }
                    return strTrim;
                } finally {
                    if (resultSetExecuteQuery != null) {
                        if (0 != 0) {
                            try {
                                resultSetExecuteQuery.close();
                            } catch (Throwable th4) {
                                th2.addSuppressed(th4);
                            }
                        } else {
                            resultSetExecuteQuery.close();
                        }
                    }
                }
            } finally {
            }
        } catch (SQLException e) {
            this.containerLog.error(sm.getString("dataSourceRealm.getPassword.exception", username), e);
            return null;
        }
    }

    @Override // org.apache.catalina.realm.RealmBase
    protected Principal getPrincipal(String username) throws SQLException, NamingException {
        Connection dbConnection = open();
        if (dbConnection == null) {
            return new GenericPrincipal(username, null, null);
        }
        try {
            GenericPrincipal genericPrincipal = new GenericPrincipal(username, getPassword(dbConnection, username), getRoles(dbConnection, username));
            close(dbConnection);
            return genericPrincipal;
        } catch (Throwable th) {
            close(dbConnection);
            throw th;
        }
    }

    protected ArrayList<String> getRoles(String username) throws SQLException, NamingException {
        Connection dbConnection = open();
        if (dbConnection == null) {
            return null;
        }
        try {
            ArrayList<String> roles = getRoles(dbConnection, username);
            close(dbConnection);
            return roles;
        } catch (Throwable th) {
            close(dbConnection);
            throw th;
        }
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r12v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x00fb: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('stmt' java.sql.PreparedStatement)]) A[TRY_LEAVE], block:B:54:0x00fb */
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x0100: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:56:0x0100 */
    /* JADX WARN: Type inference failed for: r12v1, names: [stmt], types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.lang.Throwable] */
    protected ArrayList<String> getRoles(Connection dbConnection, String username) throws SQLException {
        if (this.allRolesMode != RealmBase.AllRolesMode.STRICT_MODE && !isRoleStoreDefined()) {
            return null;
        }
        try {
            try {
                PreparedStatement preparedStatementPrepareStatement = dbConnection.prepareStatement(this.preparedRoles);
                Throwable th = null;
                preparedStatementPrepareStatement.setString(1, username);
                ResultSet resultSetExecuteQuery = preparedStatementPrepareStatement.executeQuery();
                Throwable th2 = null;
                try {
                    try {
                        ArrayList<String> arrayList = new ArrayList<>();
                        while (resultSetExecuteQuery.next()) {
                            String string = resultSetExecuteQuery.getString(1);
                            if (string != null) {
                                arrayList.add(string.trim());
                            }
                        }
                        if (resultSetExecuteQuery != null) {
                            if (0 != 0) {
                                try {
                                    resultSetExecuteQuery.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                            } else {
                                resultSetExecuteQuery.close();
                            }
                        }
                        if (preparedStatementPrepareStatement != null) {
                            if (0 != 0) {
                                try {
                                    preparedStatementPrepareStatement.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                preparedStatementPrepareStatement.close();
                            }
                        }
                        return arrayList;
                    } catch (Throwable th5) {
                        if (resultSetExecuteQuery != null) {
                            if (th2 != null) {
                                try {
                                    resultSetExecuteQuery.close();
                                } catch (Throwable th6) {
                                    th2.addSuppressed(th6);
                                }
                            } else {
                                resultSetExecuteQuery.close();
                            }
                        }
                        throw th5;
                    }
                } finally {
                }
            } finally {
            }
        } catch (SQLException e) {
            this.containerLog.error(sm.getString("dataSourceRealm.getRoles.exception", username), e);
            return null;
        }
    }

    private boolean isRoleStoreDefined() {
        return (this.userRoleTable == null && this.roleNameCol == null) ? false : true;
    }

    @Override // org.apache.catalina.realm.RealmBase, org.apache.catalina.util.LifecycleBase
    protected void startInternal() throws LifecycleException {
        this.preparedRoles = "SELECT " + this.roleNameCol + " FROM " + this.userRoleTable + " WHERE " + this.userNameCol + " = ?";
        this.preparedCredentials = "SELECT " + this.userCredCol + " FROM " + this.userTable + " WHERE " + this.userNameCol + " = ?";
        super.startInternal();
    }
}
