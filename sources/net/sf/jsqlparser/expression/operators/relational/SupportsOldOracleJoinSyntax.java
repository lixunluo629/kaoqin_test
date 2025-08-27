package net.sf.jsqlparser.expression.operators.relational;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/SupportsOldOracleJoinSyntax.class */
public interface SupportsOldOracleJoinSyntax {
    public static final int NO_ORACLE_JOIN = 0;
    public static final int ORACLE_JOIN_RIGHT = 1;
    public static final int ORACLE_JOIN_LEFT = 2;
    public static final int NO_ORACLE_PRIOR = 0;
    public static final int ORACLE_PRIOR_START = 1;
    public static final int ORACLE_PRIOR_END = 2;

    int getOldOracleJoinSyntax();

    void setOldOracleJoinSyntax(int i);

    int getOraclePriorPosition();

    void setOraclePriorPosition(int i);
}
