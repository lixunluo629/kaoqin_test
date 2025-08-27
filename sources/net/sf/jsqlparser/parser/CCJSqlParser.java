package net.sf.jsqlparser.parser;

import android.R;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.WindowElement;
import net.sf.jsqlparser.expression.WindowOffset;
import net.sf.jsqlparser.expression.WindowRange;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperatorType;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.expression.operators.relational.SupportsOldOracleJoinSyntax;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Database;
import net.sf.jsqlparser.schema.Server;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;
import net.sf.jsqlparser.statement.create.table.Index;
import net.sf.jsqlparser.statement.create.table.NamedConstraint;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.ExceptOp;
import net.sf.jsqlparser.statement.select.ExpressionListItem;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FunctionItem;
import net.sf.jsqlparser.statement.select.IntersectOp;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.MinusOp;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.Pivot;
import net.sf.jsqlparser.statement.select.PivotXml;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperation;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.Top;
import net.sf.jsqlparser.statement.select.UnionOp;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.poi.ddf.EscherProperties;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/parser/CCJSqlParser.class */
public class CCJSqlParser implements CCJSqlParserConstants {
    private boolean allowOraclePrior;
    public CCJSqlParserTokenManager token_source;
    SimpleCharStream jj_input_stream;
    public Token token;
    public Token jj_nt;
    private int jj_ntk;
    private Token jj_scanpos;
    private Token jj_lastpos;
    private int jj_la;
    private int jj_gen;
    private final int[] jj_la1;
    private static int[] jj_la1_0;
    private static int[] jj_la1_1;
    private static int[] jj_la1_2;
    private static int[] jj_la1_3;
    private static int[] jj_la1_4;
    private final JJCalls[] jj_2_rtns;
    private boolean jj_rescan;
    private int jj_gc;
    private final LookaheadSuccess jj_ls;
    private List<int[]> jj_expentries;
    private int[] jj_expentry;
    private int jj_kind;
    private int[] jj_lasttokens;
    private int jj_endpos;

    public final Statement Statement() throws ParseException {
        Statement stm = SingleStatement();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 122:
                jj_consume_token(122);
                break;
            default:
                this.jj_la1[0] = this.jj_gen;
                break;
        }
        jj_consume_token(0);
        if ("" != 0) {
            return stm;
        }
        throw new Error("Missing return statement in function");
    }

    public final Statement SingleStatement() throws ParseException {
        Statement stm;
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 38:
            case 58:
            case 125:
                stm = Select();
                break;
            case 56:
                stm = Delete();
                break;
            case 62:
                stm = Insert();
                break;
            case 63:
                stm = Update();
                break;
            case 68:
                stm = Replace();
                break;
            case 92:
                stm = Alter();
                break;
            default:
                this.jj_la1[1] = this.jj_gen;
                if (jj_2_1(3)) {
                    stm = CreateIndex();
                    break;
                } else if (jj_2_2(2)) {
                    stm = CreateTable();
                    break;
                } else if (jj_2_3(2)) {
                    stm = CreateView();
                    break;
                } else {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 26:
                            stm = Drop();
                            break;
                        case 70:
                            stm = Truncate();
                            break;
                        case 109:
                        case 110:
                            stm = Execute();
                            break;
                        default:
                            this.jj_la1[2] = this.jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                }
        }
        if ("" != 0) {
            return stm;
        }
        throw new Error("Missing return statement in function");
    }

    public final Statements Statements() throws ParseException {
        Statements stmts = new Statements();
        List<Statement> list = new ArrayList<>();
        Statement stm = SingleStatement();
        list.add(stm);
        while (jj_2_4(2)) {
            jj_consume_token(122);
            Statement stm2 = SingleStatement();
            list.add(stm2);
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 122:
                jj_consume_token(122);
                break;
            default:
                this.jj_la1[3] = this.jj_gen;
                break;
        }
        jj_consume_token(0);
        stmts.setStatements(list);
        if ("" != 0) {
            return stmts;
        }
        throw new Error("Missing return statement in function");
    }

    public final Update Update() throws ParseException {
        Update update = new Update();
        List<Table> tables = new ArrayList<>();
        List<Expression> expList = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        FromItem fromItem = null;
        List<Join> joins = null;
        jj_consume_token(63);
        Table table = TableWithAlias();
        tables.add(table);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    Table table2 = TableWithAlias();
                    tables.add(table2);
                default:
                    this.jj_la1[4] = this.jj_gen;
                    jj_consume_token(17);
                    Column tableColumn = Column();
                    jj_consume_token(124);
                    Expression value = SimpleExpression();
                    columns.add(tableColumn);
                    expList.add(value);
                    while (true) {
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 123:
                                jj_consume_token(123);
                                Column tableColumn2 = Column();
                                jj_consume_token(124);
                                Expression value2 = SimpleExpression();
                                columns.add(tableColumn2);
                                expList.add(value2);
                            default:
                                this.jj_la1[5] = this.jj_gen;
                                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                    case 30:
                                        jj_consume_token(30);
                                        fromItem = FromItem();
                                        joins = JoinsList();
                                        break;
                                    default:
                                        this.jj_la1[6] = this.jj_gen;
                                        break;
                                }
                                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                    case 41:
                                        Expression where = WhereClause();
                                        update.setWhere(where);
                                        break;
                                    default:
                                        this.jj_la1[7] = this.jj_gen;
                                        break;
                                }
                                update.setColumns(columns);
                                update.setExpressions(expList);
                                update.setTables(tables);
                                update.setFromItem(fromItem);
                                update.setJoins(joins);
                                if ("" != 0) {
                                    return update;
                                }
                                throw new Error("Missing return statement in function");
                        }
                    }
            }
        }
    }

    public final Replace Replace() throws ParseException {
        ItemsList itemsList;
        Replace replace = new Replace();
        List<Column> columns = new ArrayList<>();
        List<Expression> expList = new ArrayList<>();
        jj_consume_token(68);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 23:
                jj_consume_token(23);
                break;
            default:
                this.jj_la1[8] = this.jj_gen;
                break;
        }
        Table table = Table();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 17:
                jj_consume_token(17);
                Column tableColumn = Column();
                jj_consume_token(124);
                Expression value = SimpleExpression();
                columns.add(tableColumn);
                expList.add(value);
                while (true) {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 123:
                            jj_consume_token(123);
                            Column tableColumn2 = Column();
                            jj_consume_token(124);
                            Expression value2 = SimpleExpression();
                            columns.add(tableColumn2);
                            expList.add(value2);
                        default:
                            this.jj_la1[9] = this.jj_gen;
                            replace.setExpressions(expList);
                            break;
                    }
                }
            case 55:
            case 58:
            case 64:
            case 125:
                if (jj_2_5(2)) {
                    jj_consume_token(125);
                    Column tableColumn3 = Column();
                    columns.add(tableColumn3);
                    while (true) {
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 123:
                                jj_consume_token(123);
                                Column tableColumn4 = Column();
                                columns.add(tableColumn4);
                            default:
                                this.jj_la1[10] = this.jj_gen;
                                jj_consume_token(126);
                                break;
                        }
                    }
                }
                if (jj_2_6(2)) {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 55:
                        case 64:
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 55:
                                    jj_consume_token(55);
                                    break;
                                case 64:
                                    jj_consume_token(64);
                                    break;
                                default:
                                    this.jj_la1[11] = this.jj_gen;
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                        default:
                            this.jj_la1[12] = this.jj_gen;
                            break;
                    }
                    jj_consume_token(125);
                    Expression exp = PrimaryExpression();
                    expList.add(exp);
                    while (true) {
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 123:
                                jj_consume_token(123);
                                Expression exp2 = PrimaryExpression();
                                expList.add(exp2);
                            default:
                                this.jj_la1[13] = this.jj_gen;
                                jj_consume_token(126);
                                itemsList = new ExpressionList(expList);
                                break;
                        }
                    }
                } else {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 58:
                        case 125:
                            replace.setUseValues(false);
                            itemsList = SubSelect();
                            ((SubSelect) itemsList).setUseBrackets(false);
                            break;
                        default:
                            this.jj_la1[14] = this.jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                }
                replace.setItemsList(itemsList);
                break;
            default:
                this.jj_la1[15] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        if (columns.size() > 0) {
            replace.setColumns(columns);
        }
        replace.setTable(table);
        if ("" != 0) {
            return replace;
        }
        throw new Error("Missing return statement in function");
    }

    public final List<SelectExpressionItem> ListExpressionItem() throws ParseException {
        List<SelectExpressionItem> retval = new ArrayList<>();
        SelectExpressionItem item = SelectExpressionItem();
        retval.add(item);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    SelectExpressionItem item2 = SelectExpressionItem();
                    retval.add(item2);
                default:
                    this.jj_la1[16] = this.jj_gen;
                    if ("" != 0) {
                        return retval;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:95)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final net.sf.jsqlparser.statement.insert.Insert Insert() throws net.sf.jsqlparser.parser.ParseException {
        /*
            Method dump skipped, instructions count: 1698
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sf.jsqlparser.parser.CCJSqlParser.Insert():net.sf.jsqlparser.statement.insert.Insert");
    }

    public final Delete Delete() throws ParseException {
        Delete delete = new Delete();
        jj_consume_token(56);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 30:
                jj_consume_token(30);
                break;
            default:
                this.jj_la1[28] = this.jj_gen;
                break;
        }
        Table table = TableWithAlias();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 41:
                Expression where = WhereClause();
                delete.setWhere(where);
                break;
            default:
                this.jj_la1[29] = this.jj_gen;
                break;
        }
        delete.setTable(table);
        if ("" != 0) {
            return delete;
        }
        throw new Error("Missing return statement in function");
    }

    public final Column Column() throws ParseException {
        String columnName;
        String databaseName = null;
        String schemaName = null;
        String tableName = null;
        if (jj_2_10(7)) {
            databaseName = RelObjectName();
            jj_consume_token(128);
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 7:
                case 44:
                case 55:
                case 68:
                case 70:
                case 73:
                case 77:
                case 78:
                case 80:
                case 91:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 102:
                case 104:
                case 116:
                case 120:
                    schemaName = RelObjectName();
                    break;
                default:
                    this.jj_la1[30] = this.jj_gen;
                    break;
            }
            jj_consume_token(128);
            tableName = RelObjectName();
            jj_consume_token(128);
            columnName = RelObjectName();
        } else if (jj_2_11(5)) {
            schemaName = RelObjectName();
            jj_consume_token(128);
            tableName = RelObjectName();
            jj_consume_token(128);
            columnName = RelObjectName();
        } else if (jj_2_12(3)) {
            tableName = RelObjectName();
            jj_consume_token(128);
            columnName = RelObjectName();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 7:
                case 44:
                case 55:
                case 68:
                case 70:
                case 73:
                case 77:
                case 78:
                case 80:
                case 91:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 102:
                case 104:
                case 116:
                case 120:
                    columnName = RelObjectName();
                    break;
                default:
                    this.jj_la1[31] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        Database database = new Database(databaseName);
        Table table = new Table(database, schemaName, tableName);
        if ("" != 0) {
            return new Column(table, columnName);
        }
        throw new Error("Missing return statement in function");
    }

    public final String RelObjectName() throws ParseException {
        Token tk2;
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 7:
                tk2 = jj_consume_token(7);
                break;
            case 44:
                tk2 = jj_consume_token(44);
                break;
            case 55:
                tk2 = jj_consume_token(55);
                break;
            case 68:
                tk2 = jj_consume_token(68);
                break;
            case 70:
                tk2 = jj_consume_token(70);
                break;
            case 73:
                tk2 = jj_consume_token(73);
                break;
            case 77:
                tk2 = jj_consume_token(77);
                break;
            case 78:
                tk2 = jj_consume_token(78);
                break;
            case 80:
                tk2 = jj_consume_token(80);
                break;
            case 91:
                tk2 = jj_consume_token(91);
                break;
            case 94:
                tk2 = jj_consume_token(94);
                break;
            case 95:
                tk2 = jj_consume_token(95);
                break;
            case 96:
                tk2 = jj_consume_token(96);
                break;
            case 97:
                tk2 = jj_consume_token(97);
                break;
            case 98:
                tk2 = jj_consume_token(98);
                break;
            case 99:
                tk2 = jj_consume_token(99);
                break;
            case 102:
                tk2 = jj_consume_token(102);
                break;
            case 104:
                tk2 = jj_consume_token(104);
                break;
            case 116:
                tk2 = jj_consume_token(116);
                break;
            case 120:
                tk2 = jj_consume_token(120);
                break;
            default:
                this.jj_la1[32] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        if ("" != 0) {
            return tk2.image;
        }
        throw new Error("Missing return statement in function");
    }

    public final Table Table() throws ParseException {
        String tableName;
        String serverName = null;
        String databaseName = null;
        String schemaName = null;
        if (jj_2_13(7)) {
            serverName = RelObjectName();
            jj_consume_token(128);
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 7:
                case 44:
                case 55:
                case 68:
                case 70:
                case 73:
                case 77:
                case 78:
                case 80:
                case 91:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 102:
                case 104:
                case 116:
                case 120:
                    databaseName = RelObjectName();
                    break;
                default:
                    this.jj_la1[33] = this.jj_gen;
                    break;
            }
            jj_consume_token(128);
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 7:
                case 44:
                case 55:
                case 68:
                case 70:
                case 73:
                case 77:
                case 78:
                case 80:
                case 91:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 102:
                case 104:
                case 116:
                case 120:
                    schemaName = RelObjectName();
                    break;
                default:
                    this.jj_la1[34] = this.jj_gen;
                    break;
            }
            jj_consume_token(128);
            tableName = RelObjectName();
        } else if (jj_2_14(5)) {
            databaseName = RelObjectName();
            jj_consume_token(128);
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 7:
                case 44:
                case 55:
                case 68:
                case 70:
                case 73:
                case 77:
                case 78:
                case 80:
                case 91:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 102:
                case 104:
                case 116:
                case 120:
                    schemaName = RelObjectName();
                    break;
                default:
                    this.jj_la1[35] = this.jj_gen;
                    break;
            }
            jj_consume_token(128);
            tableName = RelObjectName();
        } else if (jj_2_15(3)) {
            schemaName = RelObjectName();
            jj_consume_token(128);
            tableName = RelObjectName();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 7:
                case 44:
                case 55:
                case 68:
                case 70:
                case 73:
                case 77:
                case 78:
                case 80:
                case 91:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 102:
                case 104:
                case 116:
                case 120:
                    tableName = RelObjectName();
                    break;
                default:
                    this.jj_la1[36] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        Server server = new Server(serverName);
        Database database = new Database(server, databaseName);
        if ("" != 0) {
            return new Table(database, schemaName, tableName);
        }
        throw new Error("Missing return statement in function");
    }

    public final Table TableWithAlias() throws ParseException {
        Table table = Table();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 5:
            case 7:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 116:
            case 120:
                Alias alias = Alias();
                table.setAlias(alias);
                break;
            default:
                this.jj_la1[37] = this.jj_gen;
                break;
        }
        if ("" != 0) {
            return table;
        }
        throw new Error("Missing return statement in function");
    }

    public final Select Select() throws ParseException {
        Select select = new Select();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 38:
                List<WithItem> with = WithList();
                select.setWithItemsList(with);
                break;
            default:
                this.jj_la1[38] = this.jj_gen;
                break;
        }
        SelectBody selectBody = SelectBody();
        select.setSelectBody(selectBody);
        if ("" != 0) {
            return select;
        }
        throw new Error("Missing return statement in function");
    }

    public final SelectBody SelectBody() throws ParseException {
        SelectBody selectBody;
        if (jj_2_16(Integer.MAX_VALUE)) {
            selectBody = SetOperationList();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 58:
                    selectBody = PlainSelect();
                    break;
                default:
                    this.jj_la1[39] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        if ("" != 0) {
            return selectBody;
        }
        throw new Error("Missing return statement in function");
    }

    public final PlainSelect PlainSelect() throws ParseException {
        PlainSelect plainSelect = new PlainSelect();
        FromItem fromItem = null;
        List<Join> joins = null;
        jj_consume_token(58);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 12:
            case 71:
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 12:
                        jj_consume_token(12);
                        break;
                    case 71:
                        jj_consume_token(71);
                        Distinct distinct = new Distinct();
                        plainSelect.setDistinct(distinct);
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 11:
                                jj_consume_token(11);
                                jj_consume_token(125);
                                List<SelectItem> distinctOn = SelectItemsList();
                                plainSelect.getDistinct().setOnSelectItems(distinctOn);
                                jj_consume_token(126);
                                break;
                            default:
                                this.jj_la1[40] = this.jj_gen;
                                break;
                        }
                    default:
                        this.jj_la1[41] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            default:
                this.jj_la1[42] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 19:
                Top top = Top();
                plainSelect.setTop(top);
                break;
            default:
                this.jj_la1[43] = this.jj_gen;
                break;
        }
        List<SelectItem> selectItems = SelectItemsList();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 23:
                List<Table> intoTables = IntoClause();
                plainSelect.setIntoTables(intoTables);
                break;
            default:
                this.jj_la1[44] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 30:
                jj_consume_token(30);
                fromItem = FromItem();
                joins = JoinsList();
                break;
            default:
                this.jj_la1[45] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 41:
                Expression where = WhereClause();
                plainSelect.setWhere(where);
                break;
            default:
                this.jj_la1[46] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 87:
            case 88:
                OracleHierarchicalExpression oracleHierarchicalQueryClause = OracleHierarchicalQueryClause();
                plainSelect.setOracleHierarchical(oracleHierarchicalQueryClause);
                break;
            default:
                this.jj_la1[47] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 47:
                List<Expression> groupByColumnReferences = GroupByColumnReferences();
                plainSelect.setGroupByColumnReferences(groupByColumnReferences);
                break;
            default:
                this.jj_la1[48] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 61:
                Expression having = Having();
                plainSelect.setHaving(having);
                break;
            default:
                this.jj_la1[49] = this.jj_gen;
                break;
        }
        if (jj_2_17(Integer.MAX_VALUE)) {
            List<OrderByElement> orderByElements = OrderByElements();
            plainSelect.setOracleSiblings(true);
            plainSelect.setOrderByElements(orderByElements);
        }
        if (jj_2_18(Integer.MAX_VALUE)) {
            List<OrderByElement> orderByElements2 = OrderByElements();
            plainSelect.setOrderByElements(orderByElements2);
        }
        if (jj_2_19(2)) {
            Limit limit = Limit();
            plainSelect.setLimit(limit);
        }
        plainSelect.setSelectItems(selectItems);
        plainSelect.setFromItem(fromItem);
        if (joins != null && joins.size() > 0) {
            plainSelect.setJoins(joins);
        }
        if ("" != 0) {
            return plainSelect;
        }
        throw new Error("Missing return statement in function");
    }

    public final SetOperationList SetOperationList() throws ParseException {
        PlainSelect select;
        PlainSelect select2;
        SetOperationList list = new SetOperationList();
        List<PlainSelect> selects = new ArrayList<>();
        List<SetOperation> operations = new ArrayList<>();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 58:
                select = PlainSelect();
                break;
            case 125:
                jj_consume_token(125);
                select = PlainSelect();
                jj_consume_token(126);
                break;
            default:
                this.jj_la1[50] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        selects.add(select);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 46:
                    jj_consume_token(46);
                    UnionOp union = new UnionOp();
                    operations.add(union);
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 12:
                        case 71:
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 12:
                                    jj_consume_token(12);
                                    union.setAll(true);
                                    break;
                                case 71:
                                    jj_consume_token(71);
                                    union.setDistinct(true);
                                    break;
                                default:
                                    this.jj_la1[51] = this.jj_gen;
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                        default:
                            this.jj_la1[52] = this.jj_gen;
                            break;
                    }
                case 72:
                    jj_consume_token(72);
                    operations.add(new IntersectOp());
                    break;
                case 74:
                    jj_consume_token(74);
                    operations.add(new ExceptOp());
                    break;
                case 75:
                    jj_consume_token(75);
                    operations.add(new MinusOp());
                    break;
                default:
                    this.jj_la1[53] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 58:
                    select2 = PlainSelect();
                    break;
                case 125:
                    jj_consume_token(125);
                    select2 = PlainSelect();
                    jj_consume_token(126);
                    break;
                default:
                    this.jj_la1[54] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            selects.add(select2);
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 46:
                case 72:
                case 74:
                case 75:
                default:
                    this.jj_la1[55] = this.jj_gen;
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 53:
                            List<OrderByElement> orderByElements = OrderByElements();
                            list.setOrderByElements(orderByElements);
                            break;
                        default:
                            this.jj_la1[56] = this.jj_gen;
                            break;
                    }
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 51:
                        case 59:
                            Limit limit = Limit();
                            list.setLimit(limit);
                            break;
                        default:
                            this.jj_la1[57] = this.jj_gen;
                            break;
                    }
                    list.setOpsAndSelects(selects, operations);
                    if ("" != 0) {
                        return list;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final List<WithItem> WithList() throws ParseException {
        List<WithItem> withItemsList = new ArrayList<>();
        jj_consume_token(38);
        WithItem with = WithItem();
        withItemsList.add(with);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    WithItem with2 = WithItem();
                    withItemsList.add(with2);
                default:
                    this.jj_la1[58] = this.jj_gen;
                    if ("" != 0) {
                        return withItemsList;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final WithItem WithItem() throws ParseException {
        WithItem with = new WithItem();
        String name = RelObjectName();
        with.setName(name);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 125:
                jj_consume_token(125);
                List<SelectItem> selectItems = SelectItemsList();
                jj_consume_token(126);
                with.setWithItemList(selectItems);
                break;
            default:
                this.jj_la1[59] = this.jj_gen;
                break;
        }
        jj_consume_token(5);
        jj_consume_token(125);
        SelectBody selectBody = SelectBody();
        with.setSelectBody(selectBody);
        jj_consume_token(126);
        if ("" != 0) {
            return with;
        }
        throw new Error("Missing return statement in function");
    }

    public final List<SelectItem> SelectItemsList() throws ParseException {
        List<SelectItem> selectItemsList = new ArrayList<>();
        SelectItem selectItem = SelectItem();
        selectItemsList.add(selectItem);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    SelectItem selectItem2 = SelectItem();
                    selectItemsList.add(selectItem2);
                default:
                    this.jj_la1[60] = this.jj_gen;
                    if ("" != 0) {
                        return selectItemsList;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final SelectExpressionItem SelectExpressionItem() throws ParseException {
        Expression expression = SimpleExpression();
        SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
        selectExpressionItem.setExpression(expression);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 5:
            case 7:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 116:
            case 120:
                Alias alias = Alias();
                selectExpressionItem.setAlias(alias);
                break;
            default:
                this.jj_la1[61] = this.jj_gen;
                break;
        }
        if ("" != 0) {
            return selectExpressionItem;
        }
        throw new Error("Missing return statement in function");
    }

    public final SelectItem SelectItem() throws ParseException {
        SelectItem selectItem;
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 127:
                jj_consume_token(127);
                selectItem = new AllColumns();
                break;
            default:
                this.jj_la1[62] = this.jj_gen;
                if (jj_2_20(Integer.MAX_VALUE)) {
                    selectItem = AllTableColumns();
                    break;
                } else if (jj_2_21(Integer.MAX_VALUE)) {
                    selectItem = SelectExpressionItem();
                    break;
                } else {
                    jj_consume_token(-1);
                    throw new ParseException();
                }
        }
        if ("" != 0) {
            return selectItem;
        }
        throw new Error("Missing return statement in function");
    }

    public final AllTableColumns AllTableColumns() throws ParseException {
        Table table = Table();
        jj_consume_token(128);
        jj_consume_token(127);
        if ("" != 0) {
            return new AllTableColumns(table);
        }
        throw new Error("Missing return statement in function");
    }

    public final Alias Alias() throws ParseException {
        boolean useAs = false;
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 5:
                jj_consume_token(5);
                useAs = true;
                break;
            default:
                this.jj_la1[63] = this.jj_gen;
                break;
        }
        String name = RelObjectName();
        if ("" != 0) {
            return new Alias(name, useAs);
        }
        throw new Error("Missing return statement in function");
    }

    public final FunctionItem FunctionItem() throws ParseException {
        Function function = Function();
        FunctionItem functionItem = new FunctionItem();
        functionItem.setFunction(function);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 5:
            case 7:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 116:
            case 120:
                Alias alias = Alias();
                functionItem.setAlias(alias);
                break;
            default:
                this.jj_la1[64] = this.jj_gen;
                break;
        }
        if ("" != 0) {
            return functionItem;
        }
        throw new Error("Missing return statement in function");
    }

    public final List<Column> PivotForColumns() throws ParseException {
        List<Column> columns = new ArrayList<>();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 7:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 116:
            case 120:
                Column column = Column();
                columns.add(column);
                break;
            case 125:
                jj_consume_token(125);
                Column column2 = Column();
                columns.add(column2);
                while (true) {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 123:
                            jj_consume_token(123);
                            Column column3 = Column();
                            columns.add(column3);
                        default:
                            this.jj_la1[65] = this.jj_gen;
                            jj_consume_token(126);
                            break;
                    }
                }
            default:
                this.jj_la1[66] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        if ("" != 0) {
            return columns;
        }
        throw new Error("Missing return statement in function");
    }

    public final List<FunctionItem> PivotFunctionItems() throws ParseException {
        List<FunctionItem> functionItems = new ArrayList<>();
        FunctionItem item = FunctionItem();
        functionItems.add(item);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    FunctionItem item2 = FunctionItem();
                    functionItems.add(item2);
                default:
                    this.jj_la1[67] = this.jj_gen;
                    if ("" != 0) {
                        return functionItems;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final List<SelectExpressionItem> PivotSingleInItems() throws ParseException {
        List<SelectExpressionItem> retval = new ArrayList<>();
        SelectExpressionItem item = SelectExpressionItem();
        retval.add(item);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    SelectExpressionItem item2 = SelectExpressionItem();
                    retval.add(item2);
                default:
                    this.jj_la1[68] = this.jj_gen;
                    if ("" != 0) {
                        return retval;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final ExpressionListItem ExpressionListItem() throws ParseException {
        jj_consume_token(125);
        ExpressionList expressionList = SimpleExpressionList();
        ExpressionListItem expressionListItem = new ExpressionListItem();
        expressionListItem.setExpressionList(expressionList);
        jj_consume_token(126);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 5:
            case 7:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 116:
            case 120:
                Alias alias = Alias();
                expressionListItem.setAlias(alias);
                break;
            default:
                this.jj_la1[69] = this.jj_gen;
                break;
        }
        if ("" != 0) {
            return expressionListItem;
        }
        throw new Error("Missing return statement in function");
    }

    public final List<ExpressionListItem> PivotMultiInItems() throws ParseException {
        List<ExpressionListItem> retval = new ArrayList<>();
        ExpressionListItem item = ExpressionListItem();
        retval.add(item);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    ExpressionListItem item2 = ExpressionListItem();
                    retval.add(item2);
                default:
                    this.jj_la1[70] = this.jj_gen;
                    if ("" != 0) {
                        return retval;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final Pivot Pivot() throws ParseException {
        Pivot retval = new Pivot();
        List<SelectExpressionItem> singleInItems = null;
        List<ExpressionListItem> multiInItems = null;
        jj_consume_token(43);
        jj_consume_token(125);
        List<FunctionItem> functionItems = PivotFunctionItems();
        jj_consume_token(42);
        List<Column> forColumns = PivotForColumns();
        jj_consume_token(9);
        jj_consume_token(125);
        if (jj_2_22(3)) {
            singleInItems = PivotSingleInItems();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 125:
                    multiInItems = PivotMultiInItems();
                    break;
                default:
                    this.jj_la1[71] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        jj_consume_token(126);
        jj_consume_token(126);
        retval.setFunctionItems(functionItems);
        retval.setForColumns(forColumns);
        retval.setSingleInItems(singleInItems);
        retval.setMultiInItems(multiInItems);
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final PivotXml PivotXml() throws ParseException {
        PivotXml retval = new PivotXml();
        List<SelectExpressionItem> singleInItems = null;
        List<ExpressionListItem> multiInItems = null;
        SelectBody inSelect = null;
        jj_consume_token(43);
        jj_consume_token(44);
        jj_consume_token(125);
        List<FunctionItem> functionItems = PivotFunctionItems();
        jj_consume_token(42);
        List<Column> forColumns = PivotForColumns();
        jj_consume_token(9);
        jj_consume_token(125);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 14:
                jj_consume_token(14);
                retval.setInAny(true);
                break;
            case 58:
            case 125:
                inSelect = SelectBody();
                break;
            default:
                this.jj_la1[72] = this.jj_gen;
                if (jj_2_23(2)) {
                    singleInItems = PivotSingleInItems();
                    break;
                } else {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 125:
                            multiInItems = PivotMultiInItems();
                            break;
                        default:
                            this.jj_la1[73] = this.jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                }
        }
        jj_consume_token(126);
        jj_consume_token(126);
        retval.setFunctionItems(functionItems);
        retval.setForColumns(forColumns);
        retval.setSingleInItems(singleInItems);
        retval.setMultiInItems(multiInItems);
        retval.setInSelect(inSelect);
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final List<Table> IntoClause() throws ParseException {
        List<Table> tables = new ArrayList<>();
        jj_consume_token(23);
        Table table = Table();
        tables.add(table);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    Table table2 = Table();
                    tables.add(table2);
                default:
                    this.jj_la1[74] = this.jj_gen;
                    if ("" != 0) {
                        return tables;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final FromItem FromItem() throws ParseException {
        FromItem fromItem;
        Pivot pivot;
        if (jj_2_26(Integer.MAX_VALUE)) {
            fromItem = ValuesList();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 7:
                case 44:
                case 55:
                case 68:
                case 70:
                case 73:
                case 77:
                case 78:
                case 79:
                case 80:
                case 91:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 102:
                case 104:
                case 116:
                case 120:
                case 125:
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 7:
                        case 44:
                        case 55:
                        case 68:
                        case 70:
                        case 73:
                        case 77:
                        case 78:
                        case 80:
                        case 91:
                        case 94:
                        case 95:
                        case 96:
                        case 97:
                        case 98:
                        case 99:
                        case 102:
                        case 104:
                        case 116:
                        case 120:
                            fromItem = Table();
                            break;
                        case 79:
                            fromItem = LateralSubSelect();
                            break;
                        case 125:
                            jj_consume_token(125);
                            if (jj_2_24(Integer.MAX_VALUE)) {
                                fromItem = SubJoin();
                            } else {
                                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                    case 58:
                                    case 125:
                                        fromItem = SubSelect();
                                        break;
                                    default:
                                        this.jj_la1[75] = this.jj_gen;
                                        jj_consume_token(-1);
                                        throw new ParseException();
                                }
                            }
                            jj_consume_token(126);
                            break;
                        default:
                            this.jj_la1[76] = this.jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 43:
                            if (jj_2_25(2)) {
                                pivot = PivotXml();
                            } else {
                                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                    case 43:
                                        pivot = Pivot();
                                        break;
                                    default:
                                        this.jj_la1[77] = this.jj_gen;
                                        jj_consume_token(-1);
                                        throw new ParseException();
                                }
                            }
                            fromItem.setPivot(pivot);
                            break;
                        default:
                            this.jj_la1[78] = this.jj_gen;
                            break;
                    }
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 5:
                        case 7:
                        case 44:
                        case 55:
                        case 68:
                        case 70:
                        case 73:
                        case 77:
                        case 78:
                        case 80:
                        case 91:
                        case 94:
                        case 95:
                        case 96:
                        case 97:
                        case 98:
                        case 99:
                        case 102:
                        case 104:
                        case 116:
                        case 120:
                            Alias alias = Alias();
                            fromItem.setAlias(alias);
                            break;
                        default:
                            this.jj_la1[79] = this.jj_gen;
                            break;
                    }
                default:
                    this.jj_la1[80] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        if ("" != 0) {
            return fromItem;
        }
        throw new Error("Missing return statement in function");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:95)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final net.sf.jsqlparser.statement.select.FromItem ValuesList() throws net.sf.jsqlparser.parser.ParseException {
        /*
            Method dump skipped, instructions count: 1538
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sf.jsqlparser.parser.CCJSqlParser.ValuesList():net.sf.jsqlparser.statement.select.FromItem");
    }

    public final LateralSubSelect LateralSubSelect() throws ParseException {
        LateralSubSelect lateralSubSelect = new LateralSubSelect();
        jj_consume_token(79);
        jj_consume_token(125);
        SubSelect subSelect = SubSelect();
        jj_consume_token(126);
        lateralSubSelect.setSubSelect(subSelect);
        if ("" != 0) {
            return lateralSubSelect;
        }
        throw new Error("Missing return statement in function");
    }

    public final FromItem SubJoin() throws ParseException {
        SubJoin subJoin = new SubJoin();
        FromItem fromItem = FromItem();
        subJoin.setLeft(fromItem);
        Join join = JoinerExpression();
        subJoin.setJoin(join);
        if ("" != 0) {
            return subJoin;
        }
        throw new Error("Missing return statement in function");
    }

    public final List JoinsList() throws ParseException {
        List<Join> joinsList = new ArrayList<>();
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 27:
                case 28:
                case 29:
                case 37:
                case 50:
                case 54:
                case 67:
                case 123:
                    Join join = JoinerExpression();
                    joinsList.add(join);
                default:
                    this.jj_la1[89] = this.jj_gen;
                    if ("" != 0) {
                        return joinsList;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final Join JoinerExpression() throws ParseException {
        Join join = new Join();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 28:
            case 29:
            case 37:
            case 50:
            case 54:
            case 67:
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 28:
                    case 37:
                    case 54:
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 28:
                                jj_consume_token(28);
                                join.setLeft(true);
                                break;
                            case 37:
                                jj_consume_token(37);
                                join.setFull(true);
                                break;
                            case 54:
                                jj_consume_token(54);
                                join.setRight(true);
                                break;
                            default:
                                this.jj_la1[90] = this.jj_gen;
                                jj_consume_token(-1);
                                throw new ParseException();
                        }
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 52:
                                jj_consume_token(52);
                                join.setOuter(true);
                                break;
                            default:
                                this.jj_la1[91] = this.jj_gen;
                                break;
                        }
                    case 29:
                        jj_consume_token(29);
                        join.setCross(true);
                        break;
                    case 50:
                        jj_consume_token(50);
                        join.setInner(true);
                        break;
                    case 67:
                        jj_consume_token(67);
                        join.setNatural(true);
                        break;
                    default:
                        this.jj_la1[92] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            default:
                this.jj_la1[93] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 27:
                jj_consume_token(27);
                break;
            case 123:
                jj_consume_token(123);
                join.setSimple(true);
                break;
            default:
                this.jj_la1[94] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        FromItem right = FromItem();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 11:
            case 45:
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 11:
                        jj_consume_token(11);
                        Expression onExpression = Expression();
                        join.setOnExpression(onExpression);
                        break;
                    case 45:
                        jj_consume_token(45);
                        jj_consume_token(125);
                        Column tableColumn = Column();
                        List<Column> columns = new ArrayList<>();
                        columns.add(tableColumn);
                        while (true) {
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 123:
                                    jj_consume_token(123);
                                    Column tableColumn2 = Column();
                                    columns.add(tableColumn2);
                                default:
                                    this.jj_la1[95] = this.jj_gen;
                                    jj_consume_token(126);
                                    join.setUsingColumns(columns);
                                    break;
                            }
                        }
                    default:
                        this.jj_la1[96] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            default:
                this.jj_la1[97] = this.jj_gen;
                break;
        }
        join.setRightItem(right);
        if ("" != 0) {
            return join;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression WhereClause() throws ParseException {
        jj_consume_token(41);
        Expression retval = Expression();
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final OracleHierarchicalExpression OracleHierarchicalQueryClause() throws ParseException {
        OracleHierarchicalExpression result = new OracleHierarchicalExpression();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 87:
                jj_consume_token(87);
                jj_consume_token(38);
                Expression expr = AndExpression();
                result.setStartExpression(expr);
                break;
            default:
                this.jj_la1[98] = this.jj_gen;
                break;
        }
        jj_consume_token(88);
        jj_consume_token(6);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 90:
                jj_consume_token(90);
                result.setNoCycle(true);
                break;
            default:
                this.jj_la1[99] = this.jj_gen;
                break;
        }
        this.allowOraclePrior = true;
        Expression expr2 = AndExpression();
        result.setConnectExpression(expr2);
        this.allowOraclePrior = false;
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final List<Expression> GroupByColumnReferences() throws ParseException {
        List<Expression> columnReferences = new ArrayList<>();
        jj_consume_token(47);
        jj_consume_token(6);
        Expression columnReference = SimpleExpression();
        columnReferences.add(columnReference);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    Expression columnReference2 = SimpleExpression();
                    columnReferences.add(columnReference2);
                default:
                    this.jj_la1[100] = this.jj_gen;
                    if ("" != 0) {
                        return columnReferences;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final Expression Having() throws ParseException {
        jj_consume_token(61);
        Expression having = Expression();
        if ("" != 0) {
            return having;
        }
        throw new Error("Missing return statement in function");
    }

    public final List<OrderByElement> OrderByElements() throws ParseException {
        List<OrderByElement> orderByList = new ArrayList<>();
        jj_consume_token(53);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 91:
                jj_consume_token(91);
                break;
            default:
                this.jj_la1[101] = this.jj_gen;
                break;
        }
        jj_consume_token(6);
        OrderByElement orderByElement = OrderByElement();
        orderByList.add(orderByElement);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    OrderByElement orderByElement2 = OrderByElement();
                    orderByList.add(orderByElement2);
                default:
                    this.jj_la1[102] = this.jj_gen;
                    if ("" != 0) {
                        return orderByList;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final OrderByElement OrderByElement() throws ParseException {
        OrderByElement orderByElement = new OrderByElement();
        Expression columnReference = SimpleExpression();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 18:
            case 22:
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 18:
                        jj_consume_token(18);
                        break;
                    case 22:
                        jj_consume_token(22);
                        orderByElement.setAsc(false);
                        break;
                    default:
                        this.jj_la1[103] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                orderByElement.setAscDescPresent(true);
                break;
            default:
                this.jj_la1[104] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 95:
                jj_consume_token(95);
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 96:
                    case 97:
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 96:
                                jj_consume_token(96);
                                orderByElement.setNullOrdering(OrderByElement.NullOrdering.NULLS_FIRST);
                                break;
                            case 97:
                                jj_consume_token(97);
                                orderByElement.setNullOrdering(OrderByElement.NullOrdering.NULLS_LAST);
                                break;
                            default:
                                this.jj_la1[105] = this.jj_gen;
                                jj_consume_token(-1);
                                throw new ParseException();
                        }
                    default:
                        this.jj_la1[106] = this.jj_gen;
                        break;
                }
            default:
                this.jj_la1[107] = this.jj_gen;
                break;
        }
        orderByElement.setExpression(columnReference);
        if ("" != 0) {
            return orderByElement;
        }
        throw new Error("Missing return statement in function");
    }

    public final Limit Limit() throws ParseException {
        Limit limit = new Limit();
        limit.setRowCount(-1L);
        if (jj_2_29(3)) {
            jj_consume_token(51);
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 112:
                    Token token = jj_consume_token(112);
                    limit.setOffset(Long.parseLong(token.image));
                    break;
                case 129:
                    jj_consume_token(129);
                    limit.setOffsetJdbcParameter(true);
                    break;
                default:
                    this.jj_la1[108] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            jj_consume_token(123);
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 112:
                    Token token2 = jj_consume_token(112);
                    limit.setRowCount(Long.parseLong(token2.image));
                    break;
                case 129:
                    jj_consume_token(129);
                    limit.setRowCountJdbcParameter(true);
                    break;
                default:
                    this.jj_la1[109] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 51:
                    jj_consume_token(51);
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 12:
                            jj_consume_token(12);
                            limit.setLimitAll(true);
                            break;
                        case 24:
                            jj_consume_token(24);
                            limit.setLimitNull(true);
                            break;
                        case 112:
                            Token token3 = jj_consume_token(112);
                            limit.setRowCount(Long.parseLong(token3.image));
                            break;
                        case 129:
                            jj_consume_token(129);
                            limit.setRowCountJdbcParameter(true);
                            break;
                        default:
                            this.jj_la1[111] = this.jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                    if (jj_2_28(2)) {
                        jj_consume_token(59);
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 112:
                                Token token4 = jj_consume_token(112);
                                limit.setOffset(Long.parseLong(token4.image));
                                break;
                            case 129:
                                jj_consume_token(129);
                                limit.setOffsetJdbcParameter(true);
                                break;
                            default:
                                this.jj_la1[112] = this.jj_gen;
                                jj_consume_token(-1);
                                throw new ParseException();
                        }
                    }
                    break;
                case 59:
                    jj_consume_token(59);
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 112:
                            Token token5 = jj_consume_token(112);
                            limit.setOffset(Long.parseLong(token5.image));
                            break;
                        case 129:
                            jj_consume_token(129);
                            limit.setOffsetJdbcParameter(true);
                            break;
                        default:
                            this.jj_la1[110] = this.jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                default:
                    this.jj_la1[113] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        if ("" != 0) {
            return limit;
        }
        throw new Error("Missing return statement in function");
    }

    public final Top Top() throws ParseException {
        Top top = new Top();
        jj_consume_token(19);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 112:
                Token token = jj_consume_token(112);
                top.setRowCount(Long.parseLong(token.image));
                break;
            case 125:
                jj_consume_token(125);
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 112:
                        Token token2 = jj_consume_token(112);
                        top.setRowCount(Long.parseLong(token2.image));
                        break;
                    case 129:
                        jj_consume_token(129);
                        top.setRowCountJdbcParameter(true);
                        break;
                    default:
                        this.jj_la1[114] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                top.setParenthesis(true);
                jj_consume_token(126);
                break;
            case 129:
                jj_consume_token(129);
                top.setRowCountJdbcParameter(true);
                break;
            default:
                this.jj_la1[115] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 20:
                jj_consume_token(20);
                top.setPercentage(true);
                break;
            default:
                this.jj_la1[116] = this.jj_gen;
                break;
        }
        if ("" != 0) {
            return top;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression Expression() throws ParseException {
        Expression retval;
        if (jj_2_30(Integer.MAX_VALUE)) {
            retval = OrExpression();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 125:
                    jj_consume_token(125);
                    Expression retval2 = Expression();
                    jj_consume_token(126);
                    retval = new Parenthesis(retval2);
                    break;
                default:
                    this.jj_la1[117] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression OrExpression() throws ParseException {
        Expression left = AndExpression();
        Expression result = left;
        while (jj_2_31(Integer.MAX_VALUE)) {
            jj_consume_token(10);
            Expression right = AndExpression();
            result = new OrExpression(left, right);
            left = result;
        }
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression AndExpression() throws ParseException {
        Expression left;
        Expression right;
        boolean not = false;
        if (jj_2_32(Integer.MAX_VALUE)) {
            left = Condition();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 16:
                case 125:
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 16:
                            jj_consume_token(16);
                            not = true;
                            break;
                        default:
                            this.jj_la1[118] = this.jj_gen;
                            break;
                    }
                    jj_consume_token(125);
                    Expression left2 = OrExpression();
                    jj_consume_token(126);
                    left = new Parenthesis(left2);
                    if (not) {
                        ((Parenthesis) left).setNot();
                        not = false;
                        break;
                    }
                    break;
                default:
                    this.jj_la1[119] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        Expression result = left;
        while (jj_2_33(Integer.MAX_VALUE)) {
            jj_consume_token(13);
            if (jj_2_34(Integer.MAX_VALUE)) {
                right = Condition();
            } else {
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 16:
                    case 125:
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 16:
                                jj_consume_token(16);
                                not = true;
                                break;
                            default:
                                this.jj_la1[120] = this.jj_gen;
                                break;
                        }
                        jj_consume_token(125);
                        Expression right2 = OrExpression();
                        jj_consume_token(126);
                        right = new Parenthesis(right2);
                        if (!not) {
                            break;
                        } else {
                            ((Parenthesis) right).setNot();
                            not = false;
                            break;
                        }
                    default:
                        this.jj_la1[121] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
            result = new AndExpression(left, right);
            left = result;
        }
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression Condition() throws ParseException {
        Expression result;
        if (jj_2_35(Integer.MAX_VALUE)) {
            result = SQLCondition();
        } else if (jj_2_36(Integer.MAX_VALUE)) {
            result = RegularCondition();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 7:
                case 44:
                case 55:
                case 68:
                case 70:
                case 73:
                case 77:
                case 78:
                case 80:
                case 91:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 102:
                case 104:
                case 116:
                case 120:
                case 157:
                    result = Function();
                    break;
                default:
                    this.jj_la1[122] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression RegularCondition() throws ParseException {
        Expression result;
        boolean not = false;
        int oracleJoin = 0;
        int oraclePrior = 0;
        boolean binary = false;
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 89:
                jj_consume_token(89);
                oraclePrior = 1;
                break;
            default:
                this.jj_la1[123] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 16:
                jj_consume_token(16);
                not = true;
                break;
            default:
                this.jj_la1[124] = this.jj_gen;
                break;
        }
        Expression leftExpression = ComparisonItem();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 130:
                jj_consume_token(130);
                oracleJoin = 1;
                break;
            default:
                this.jj_la1[125] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 107:
                jj_consume_token(107);
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 106:
                        jj_consume_token(106);
                        binary = true;
                        break;
                    default:
                        this.jj_la1[126] = this.jj_gen;
                        break;
                }
                result = new RegExpMySQLOperator(binary ? RegExpMatchOperatorType.MATCH_CASESENSITIVE : RegExpMatchOperatorType.MATCH_CASEINSENSITIVE);
                break;
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            default:
                this.jj_la1[127] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            case 124:
                jj_consume_token(124);
                result = new EqualsTo();
                break;
            case 131:
                jj_consume_token(131);
                result = new GreaterThan();
                break;
            case 132:
                jj_consume_token(132);
                result = new MinorThan();
                break;
            case 133:
                jj_consume_token(133);
                result = new GreaterThanEquals();
                break;
            case 134:
                jj_consume_token(134);
                result = new MinorThanEquals();
                break;
            case 135:
                jj_consume_token(135);
                result = new NotEqualsTo();
                break;
            case 136:
                jj_consume_token(136);
                result = new NotEqualsTo("!=");
                break;
            case 137:
                jj_consume_token(137);
                result = new Matches();
                break;
            case 138:
                jj_consume_token(138);
                result = new RegExpMatchOperator(RegExpMatchOperatorType.MATCH_CASESENSITIVE);
                break;
            case 139:
                jj_consume_token(139);
                result = new RegExpMatchOperator(RegExpMatchOperatorType.MATCH_CASEINSENSITIVE);
                break;
            case 140:
                jj_consume_token(140);
                result = new RegExpMatchOperator(RegExpMatchOperatorType.NOT_MATCH_CASESENSITIVE);
                break;
            case 141:
                jj_consume_token(141);
                result = new RegExpMatchOperator(RegExpMatchOperatorType.NOT_MATCH_CASEINSENSITIVE);
                break;
        }
        Expression rightExpression = ComparisonItem();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 89:
                jj_consume_token(89);
                oraclePrior = 2;
                break;
            default:
                this.jj_la1[128] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 130:
                jj_consume_token(130);
                oracleJoin = 2;
                break;
            default:
                this.jj_la1[129] = this.jj_gen;
                break;
        }
        BinaryExpression regCond = (BinaryExpression) result;
        regCond.setLeftExpression(leftExpression);
        regCond.setRightExpression(rightExpression);
        if (not) {
            regCond.setNot();
        }
        if (oracleJoin > 0) {
            ((SupportsOldOracleJoinSyntax) result).setOldOracleJoinSyntax(oracleJoin);
        }
        if (oraclePrior != 0) {
            ((SupportsOldOracleJoinSyntax) result).setOraclePriorPosition(oraclePrior);
        }
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression SQLCondition() throws ParseException {
        Expression result;
        if (jj_2_37(Integer.MAX_VALUE)) {
            result = InExpression();
        } else if (jj_2_38(Integer.MAX_VALUE)) {
            result = Between();
        } else if (jj_2_39(Integer.MAX_VALUE)) {
            result = IsNullExpression();
        } else if (jj_2_40(Integer.MAX_VALUE)) {
            result = ExistsExpression();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 7:
                case 24:
                case 32:
                case 44:
                case 55:
                case 68:
                case 70:
                case 73:
                case 77:
                case 78:
                case 80:
                case 81:
                case 91:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 102:
                case 104:
                case 111:
                case 112:
                case 116:
                case 119:
                case 120:
                case 125:
                case 129:
                case 145:
                case 146:
                case 150:
                case 152:
                case 153:
                case 155:
                case 157:
                    result = LikeExpression();
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 69:
                case 71:
                case 72:
                case 74:
                case 75:
                case 76:
                case 79:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 92:
                case 93:
                case 100:
                case 101:
                case 103:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 113:
                case 114:
                case 115:
                case 117:
                case 118:
                case 121:
                case 122:
                case 123:
                case 124:
                case 126:
                case 127:
                case 128:
                case 130:
                case 131:
                case 132:
                case 133:
                case 134:
                case 135:
                case 136:
                case 137:
                case 138:
                case 139:
                case 140:
                case 141:
                case 142:
                case 143:
                case 144:
                case 147:
                case 148:
                case 149:
                case 151:
                case 154:
                case 156:
                default:
                    this.jj_la1[130] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression InExpression() throws ParseException {
        ItemsList rightItemsList;
        InExpression result = new InExpression();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 7:
            case 24:
            case 32:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 81:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 111:
            case 112:
            case 116:
            case 119:
            case 120:
            case 129:
            case 145:
            case 146:
            case 150:
            case 152:
            case 153:
            case 155:
            case 157:
                Expression leftExpression = SimpleExpression();
                result.setLeftExpression(leftExpression);
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 130:
                        jj_consume_token(130);
                        result.setOldOracleJoinSyntax(1);
                        break;
                    default:
                        this.jj_la1[133] = this.jj_gen;
                        break;
                }
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 69:
            case 71:
            case 72:
            case 74:
            case 75:
            case 76:
            case 79:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 92:
            case 93:
            case 100:
            case 101:
            case 103:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 121:
            case 122:
            case 123:
            case 124:
            case 126:
            case 127:
            case 128:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 147:
            case 148:
            case 149:
            case 151:
            case 154:
            case 156:
            default:
                this.jj_la1[134] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            case 125:
                jj_consume_token(125);
                if (jj_2_41(Integer.MAX_VALUE)) {
                    ItemsList leftItemsList = SimpleExpressionList();
                    result.setLeftItemsList(leftItemsList);
                } else {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 7:
                        case 24:
                        case 32:
                        case 44:
                        case 55:
                        case 68:
                        case 70:
                        case 73:
                        case 77:
                        case 78:
                        case 80:
                        case 81:
                        case 91:
                        case 94:
                        case 95:
                        case 96:
                        case 97:
                        case 98:
                        case 99:
                        case 102:
                        case 104:
                        case 111:
                        case 112:
                        case 116:
                        case 119:
                        case 120:
                        case 125:
                        case 129:
                        case 145:
                        case 146:
                        case 150:
                        case 152:
                        case 153:
                        case 155:
                        case 157:
                            SimpleExpression();
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 130:
                                    jj_consume_token(130);
                                    result.setOldOracleJoinSyntax(1);
                                    break;
                                default:
                                    this.jj_la1[131] = this.jj_gen;
                                    break;
                            }
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                        case 31:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                        case 45:
                        case 46:
                        case 47:
                        case 48:
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                        case 60:
                        case 61:
                        case 62:
                        case 63:
                        case 64:
                        case 65:
                        case 66:
                        case 67:
                        case 69:
                        case 71:
                        case 72:
                        case 74:
                        case 75:
                        case 76:
                        case 79:
                        case 82:
                        case 83:
                        case 84:
                        case 85:
                        case 86:
                        case 87:
                        case 88:
                        case 89:
                        case 90:
                        case 92:
                        case 93:
                        case 100:
                        case 101:
                        case 103:
                        case 105:
                        case 106:
                        case 107:
                        case 108:
                        case 109:
                        case 110:
                        case 113:
                        case 114:
                        case 115:
                        case 117:
                        case 118:
                        case 121:
                        case 122:
                        case 123:
                        case 124:
                        case 126:
                        case 127:
                        case 128:
                        case 130:
                        case 131:
                        case 132:
                        case 133:
                        case 134:
                        case 135:
                        case 136:
                        case 137:
                        case 138:
                        case 139:
                        case 140:
                        case 141:
                        case 142:
                        case 143:
                        case 144:
                        case 147:
                        case 148:
                        case 149:
                        case 151:
                        case 154:
                        case 156:
                        default:
                            this.jj_la1[132] = this.jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                }
                jj_consume_token(126);
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 16:
                jj_consume_token(16);
                result.setNot(true);
                break;
            default:
                this.jj_la1[135] = this.jj_gen;
                break;
        }
        jj_consume_token(9);
        jj_consume_token(125);
        if (jj_2_42(Integer.MAX_VALUE)) {
            rightItemsList = SubSelect();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 7:
                case 24:
                case 32:
                case 44:
                case 55:
                case 68:
                case 70:
                case 73:
                case 77:
                case 78:
                case 80:
                case 81:
                case 91:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 102:
                case 104:
                case 111:
                case 112:
                case 116:
                case 119:
                case 120:
                case 125:
                case 129:
                case 145:
                case 146:
                case 150:
                case 152:
                case 153:
                case 155:
                case 157:
                    rightItemsList = SimpleExpressionList();
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 69:
                case 71:
                case 72:
                case 74:
                case 75:
                case 76:
                case 79:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 92:
                case 93:
                case 100:
                case 101:
                case 103:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 113:
                case 114:
                case 115:
                case 117:
                case 118:
                case 121:
                case 122:
                case 123:
                case 124:
                case 126:
                case 127:
                case 128:
                case 130:
                case 131:
                case 132:
                case 133:
                case 134:
                case 135:
                case 136:
                case 137:
                case 138:
                case 139:
                case 140:
                case 141:
                case 142:
                case 143:
                case 144:
                case 147:
                case 148:
                case 149:
                case 151:
                case 154:
                case 156:
                default:
                    this.jj_la1[136] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        jj_consume_token(126);
        result.setRightItemsList(rightItemsList);
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression Between() throws ParseException {
        Between result = new Between();
        Expression leftExpression = SimpleExpression();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 16:
                jj_consume_token(16);
                result.setNot(true);
                break;
            default:
                this.jj_la1[137] = this.jj_gen;
                break;
        }
        jj_consume_token(69);
        Expression betweenExpressionStart = SimpleExpression();
        jj_consume_token(13);
        Expression betweenExpressionEnd = SimpleExpression();
        result.setLeftExpression(leftExpression);
        result.setBetweenExpressionStart(betweenExpressionStart);
        result.setBetweenExpressionEnd(betweenExpressionEnd);
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression LikeExpression() throws ParseException {
        LikeExpression result = new LikeExpression();
        Expression leftExpression = SimpleExpression();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 16:
                jj_consume_token(16);
                result.setNot(true);
                break;
            default:
                this.jj_la1[138] = this.jj_gen;
                break;
        }
        jj_consume_token(25);
        Expression rightExpression = SimpleExpression();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 65:
                jj_consume_token(65);
                this.token = jj_consume_token(119);
                result.setEscape(new StringValue(this.token.image).getValue());
                break;
            default:
                this.jj_la1[139] = this.jj_gen;
                break;
        }
        result.setLeftExpression(leftExpression);
        result.setRightExpression(rightExpression);
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression IsNullExpression() throws ParseException {
        Expression leftExpression;
        IsNullExpression result = new IsNullExpression();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 7:
            case 24:
            case 32:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 81:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 111:
            case 112:
            case 116:
            case 119:
            case 120:
            case 125:
            case 129:
            case 145:
            case 146:
            case 150:
            case 152:
            case 153:
            case 155:
            case 157:
                leftExpression = SimpleExpression();
                jj_consume_token(8);
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 16:
                        jj_consume_token(16);
                        result.setNot(true);
                        break;
                    default:
                        this.jj_la1[140] = this.jj_gen;
                        break;
                }
                jj_consume_token(24);
                break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 69:
            case 71:
            case 72:
            case 74:
            case 75:
            case 76:
            case 79:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 92:
            case 93:
            case 100:
            case 101:
            case 103:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 121:
            case 122:
            case 123:
            case 124:
            case 126:
            case 127:
            case 128:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 147:
            case 148:
            case 149:
            case 151:
            case 154:
            case 156:
            default:
                this.jj_la1[141] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            case 16:
                jj_consume_token(16);
                result.setNot(true);
                leftExpression = SimpleExpression();
                jj_consume_token(8);
                jj_consume_token(24);
                break;
        }
        result.setLeftExpression(leftExpression);
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression ExistsExpression() throws ParseException {
        ExistsExpression result = new ExistsExpression();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 16:
                jj_consume_token(16);
                result.setNot(true);
                break;
            default:
                this.jj_la1[142] = this.jj_gen;
                break;
        }
        jj_consume_token(60);
        Expression rightExpression = SimpleExpression();
        result.setRightExpression(rightExpression);
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final ExpressionList SQLExpressionList() throws ParseException {
        ExpressionList retval = new ExpressionList();
        List<Expression> expressions = new ArrayList<>();
        Expression expr = Expression();
        expressions.add(expr);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    Expression expr2 = Expression();
                    expressions.add(expr2);
                default:
                    this.jj_la1[143] = this.jj_gen;
                    retval.setExpressions(expressions);
                    if ("" != 0) {
                        return retval;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final ExpressionList SimpleExpressionList() throws ParseException {
        ExpressionList retval = new ExpressionList();
        List<Expression> expressions = new ArrayList<>();
        Expression expr = SimpleExpression();
        expressions.add(expr);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    Expression expr2 = SimpleExpression();
                    expressions.add(expr2);
                default:
                    this.jj_la1[144] = this.jj_gen;
                    retval.setExpressions(expressions);
                    if ("" != 0) {
                        return retval;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final Expression ComparisonItem() throws ParseException {
        Expression retval;
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 7:
            case 24:
            case 32:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 81:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 111:
            case 112:
            case 116:
            case 119:
            case 120:
            case 125:
            case 129:
            case 145:
            case 146:
            case 150:
            case 152:
            case 153:
            case 155:
            case 157:
                retval = SimpleExpression();
                break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 13:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 69:
            case 71:
            case 72:
            case 74:
            case 75:
            case 76:
            case 79:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 92:
            case 93:
            case 100:
            case 101:
            case 103:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 121:
            case 122:
            case 123:
            case 124:
            case 126:
            case 127:
            case 128:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 147:
            case 148:
            case 149:
            case 151:
            case 154:
            case 156:
            default:
                this.jj_la1[145] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            case 12:
                retval = AllComparisonExpression();
                break;
            case 14:
            case 36:
                retval = AnyComparisonExpression();
                break;
        }
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression AllComparisonExpression() throws ParseException {
        jj_consume_token(12);
        jj_consume_token(125);
        SubSelect subselect = SubSelect();
        jj_consume_token(126);
        AllComparisonExpression retval = new AllComparisonExpression(subselect);
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression AnyComparisonExpression() throws ParseException {
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 14:
                jj_consume_token(14);
                break;
            case 36:
                jj_consume_token(36);
                break;
            default:
                this.jj_la1[146] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        jj_consume_token(125);
        SubSelect subselect = SubSelect();
        jj_consume_token(126);
        AnyComparisonExpression retval = new AnyComparisonExpression(subselect);
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression SimpleExpression() throws ParseException {
        Expression retval;
        if (jj_2_43(Integer.MAX_VALUE)) {
            retval = BitwiseAndOr();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 125:
                    jj_consume_token(125);
                    Expression retval2 = BitwiseAndOr();
                    jj_consume_token(126);
                    retval = new Parenthesis(retval2);
                    break;
                default:
                    this.jj_la1[147] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression ConcatExpression() throws ParseException {
        Expression leftExpression = AdditiveExpression();
        Expression result = leftExpression;
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 142:
                    jj_consume_token(142);
                    Expression rightExpression = AdditiveExpression();
                    Concat binExp = new Concat();
                    binExp.setLeftExpression(leftExpression);
                    binExp.setRightExpression(rightExpression);
                    result = binExp;
                    leftExpression = result;
                default:
                    this.jj_la1[148] = this.jj_gen;
                    if ("" != 0) {
                        return result;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final Expression BitwiseAndOr() throws ParseException {
        Expression bitwiseAnd;
        Expression leftExpression = ConcatExpression();
        Expression result = leftExpression;
        while (jj_2_44(2)) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 143:
                    jj_consume_token(143);
                    bitwiseAnd = new BitwiseOr();
                    break;
                case 144:
                    jj_consume_token(144);
                    bitwiseAnd = new BitwiseAnd();
                    break;
                default:
                    this.jj_la1[149] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            result = bitwiseAnd;
            Expression rightExpression = ConcatExpression();
            BinaryExpression binExp = (BinaryExpression) result;
            binExp.setLeftExpression(leftExpression);
            binExp.setRightExpression(rightExpression);
            leftExpression = result;
        }
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression AdditiveExpression() throws ParseException {
        Expression subtraction;
        Expression leftExpression = MultiplicativeExpression();
        Expression result = leftExpression;
        while (jj_2_45(2)) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 145:
                    jj_consume_token(145);
                    subtraction = new Addition();
                    break;
                case 146:
                    jj_consume_token(146);
                    subtraction = new Subtraction();
                    break;
                default:
                    this.jj_la1[150] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            result = subtraction;
            Expression rightExpression = MultiplicativeExpression();
            BinaryExpression binExp = (BinaryExpression) result;
            binExp.setLeftExpression(leftExpression);
            binExp.setRightExpression(rightExpression);
            leftExpression = result;
        }
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression MultiplicativeExpression() throws ParseException {
        Expression leftExpression;
        Expression parenthesis;
        if (jj_2_46(Integer.MAX_VALUE)) {
            leftExpression = BitwiseXor();
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 125:
                    jj_consume_token(125);
                    Expression leftExpression2 = ConcatExpression();
                    jj_consume_token(126);
                    leftExpression = new Parenthesis(leftExpression2);
                    break;
                default:
                    this.jj_la1[151] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        Expression result = leftExpression;
        while (jj_2_47(2)) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 127:
                    jj_consume_token(127);
                    result = new Multiplication();
                    break;
                case 147:
                    jj_consume_token(147);
                    result = new Division();
                    break;
                case 148:
                    jj_consume_token(148);
                    result = new Modulo();
                    break;
                default:
                    this.jj_la1[152] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            if (jj_2_48(Integer.MAX_VALUE)) {
                parenthesis = BitwiseXor();
            } else {
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 125:
                        jj_consume_token(125);
                        Expression rightExpression = ConcatExpression();
                        jj_consume_token(126);
                        parenthesis = new Parenthesis(rightExpression);
                        break;
                    default:
                        this.jj_la1[153] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
            Expression rightExpression2 = parenthesis;
            BinaryExpression binExp = (BinaryExpression) result;
            binExp.setLeftExpression(leftExpression);
            binExp.setRightExpression(rightExpression2);
            leftExpression = result;
        }
        if ("" != 0) {
            return result;
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression BitwiseXor() throws ParseException {
        Expression leftExpression = PrimaryExpression();
        Expression result = leftExpression;
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 149:
                    jj_consume_token(149);
                    Expression rightExpression = PrimaryExpression();
                    BitwiseXor binExp = new BitwiseXor();
                    binExp.setLeftExpression(leftExpression);
                    binExp.setRightExpression(rightExpression);
                    result = binExp;
                    leftExpression = result;
                default:
                    this.jj_la1[154] = this.jj_gen;
                    if ("" != 0) {
                        return result;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final Expression PrimaryExpression() throws ParseException {
        Expression retval;
        Token sign = null;
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 24:
                jj_consume_token(24);
                retval = new NullValue();
                break;
            case 32:
                retval = CaseWhenExpression();
                break;
            case 129:
                jj_consume_token(129);
                retval = new JdbcParameter();
                break;
            case 155:
                retval = JdbcNamedParameter();
                break;
            default:
                this.jj_la1[171] = this.jj_gen;
                if (jj_2_49(Integer.MAX_VALUE)) {
                    retval = AnalyticExpression();
                    break;
                } else if (jj_2_50(Integer.MAX_VALUE)) {
                    retval = ExtractExpression();
                    break;
                } else if (jj_2_51(Integer.MAX_VALUE)) {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 145:
                        case 146:
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 145:
                                    sign = jj_consume_token(145);
                                    break;
                                case 146:
                                    sign = jj_consume_token(146);
                                    break;
                                default:
                                    this.jj_la1[155] = this.jj_gen;
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                        default:
                            this.jj_la1[156] = this.jj_gen;
                            break;
                    }
                    retval = JsonExpression();
                    break;
                } else if (jj_2_52(Integer.MAX_VALUE)) {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 145:
                        case 146:
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 145:
                                    sign = jj_consume_token(145);
                                    break;
                                case 146:
                                    sign = jj_consume_token(146);
                                    break;
                                default:
                                    this.jj_la1[157] = this.jj_gen;
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                        default:
                            this.jj_la1[158] = this.jj_gen;
                            break;
                    }
                    retval = Function();
                    break;
                } else if (jj_2_53(Integer.MAX_VALUE)) {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 145:
                        case 146:
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 145:
                                    sign = jj_consume_token(145);
                                    break;
                                case 146:
                                    sign = jj_consume_token(146);
                                    break;
                                default:
                                    this.jj_la1[159] = this.jj_gen;
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                        default:
                            this.jj_la1[160] = this.jj_gen;
                            break;
                    }
                    Token token = jj_consume_token(111);
                    retval = new DoubleValue(token.image);
                    break;
                } else if (jj_2_54(Integer.MAX_VALUE)) {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 145:
                        case 146:
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 145:
                                    sign = jj_consume_token(145);
                                    break;
                                case 146:
                                    sign = jj_consume_token(146);
                                    break;
                                default:
                                    this.jj_la1[161] = this.jj_gen;
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                        default:
                            this.jj_la1[162] = this.jj_gen;
                            break;
                    }
                    Token token2 = jj_consume_token(112);
                    retval = new LongValue(token2.image);
                    break;
                } else if (jj_2_55(Integer.MAX_VALUE)) {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 145:
                        case 146:
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 145:
                                    sign = jj_consume_token(145);
                                    break;
                                case 146:
                                    sign = jj_consume_token(146);
                                    break;
                                default:
                                    this.jj_la1[163] = this.jj_gen;
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                        default:
                            this.jj_la1[164] = this.jj_gen;
                            break;
                    }
                    retval = CastExpression();
                    break;
                } else if (jj_2_56(Integer.MAX_VALUE)) {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 145:
                        case 146:
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 145:
                                    sign = jj_consume_token(145);
                                    break;
                                case 146:
                                    sign = jj_consume_token(146);
                                    break;
                                default:
                                    this.jj_la1[165] = this.jj_gen;
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                        default:
                            this.jj_la1[166] = this.jj_gen;
                            break;
                    }
                    retval = Column();
                    break;
                } else if (jj_2_57(2)) {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 145:
                        case 146:
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 145:
                                    sign = jj_consume_token(145);
                                    break;
                                case 146:
                                    sign = jj_consume_token(146);
                                    break;
                                default:
                                    this.jj_la1[167] = this.jj_gen;
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                        default:
                            this.jj_la1[168] = this.jj_gen;
                            break;
                    }
                    jj_consume_token(125);
                    Expression retval2 = PrimaryExpression();
                    jj_consume_token(126);
                    retval = new Parenthesis(retval2);
                    break;
                } else {
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 81:
                            retval = IntervalExpression();
                            break;
                        case 119:
                            Token token3 = jj_consume_token(119);
                            retval = new StringValue(token3.image);
                            break;
                        case 125:
                        case 145:
                        case 146:
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 145:
                                case 146:
                                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                        case 145:
                                            sign = jj_consume_token(145);
                                            break;
                                        case 146:
                                            sign = jj_consume_token(146);
                                            break;
                                        default:
                                            this.jj_la1[169] = this.jj_gen;
                                            jj_consume_token(-1);
                                            throw new ParseException();
                                    }
                                default:
                                    this.jj_la1[170] = this.jj_gen;
                                    break;
                            }
                            jj_consume_token(125);
                            retval = SubSelect();
                            jj_consume_token(126);
                            break;
                        case 150:
                            jj_consume_token(150);
                            Token token4 = jj_consume_token(119);
                            jj_consume_token(151);
                            retval = new DateValue(token4.image);
                            break;
                        case 152:
                            jj_consume_token(152);
                            Token token5 = jj_consume_token(119);
                            jj_consume_token(151);
                            retval = new TimeValue(token5.image);
                            break;
                        case 153:
                            jj_consume_token(153);
                            Token token6 = jj_consume_token(119);
                            jj_consume_token(151);
                            retval = new TimestampValue(token6.image);
                            break;
                        default:
                            this.jj_la1[172] = this.jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                }
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 154:
                jj_consume_token(154);
                ColDataType type = ColDataType();
                CastExpression castExpr = new CastExpression();
                castExpr.setUseCastKeyword(false);
                castExpr.setLeftExpression(retval);
                castExpr.setType(type);
                retval = castExpr;
                break;
            default:
                this.jj_la1[173] = this.jj_gen;
                break;
        }
        if (sign != null) {
            retval = new SignedExpression(sign.image.charAt(0), retval);
        }
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final JdbcNamedParameter JdbcNamedParameter() throws ParseException {
        JdbcNamedParameter parameter = new JdbcNamedParameter();
        jj_consume_token(155);
        Token token = jj_consume_token(116);
        parameter.setName(token.image);
        if ("" != 0) {
            return parameter;
        }
        throw new Error("Missing return statement in function");
    }

    public final JsonExpression JsonExpression() throws ParseException {
        JsonExpression result = new JsonExpression();
        Column column = Column();
        while (true) {
            jj_consume_token(156);
            Token token = jj_consume_token(119);
            result.addIdent(token.image);
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 156:
                default:
                    this.jj_la1[174] = this.jj_gen;
                    result.setColumn(column);
                    if ("" != 0) {
                        return result;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final IntervalExpression IntervalExpression() throws ParseException {
        IntervalExpression interval = new IntervalExpression();
        jj_consume_token(81);
        Token token = jj_consume_token(119);
        interval.setParameter(token.image);
        if ("" != 0) {
            return interval;
        }
        throw new Error("Missing return statement in function");
    }

    public final AnalyticExpression AnalyticExpression() throws ParseException {
        AnalyticExpression retval = new AnalyticExpression();
        ExpressionList expressionList = null;
        List<OrderByElement> olist = null;
        Expression expr = null;
        Expression offset = null;
        Expression defaultValue = null;
        WindowElement windowElement = null;
        Token token = jj_consume_token(116);
        retval.setName(token.image);
        jj_consume_token(125);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 7:
            case 24:
            case 32:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 81:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 111:
            case 112:
            case 116:
            case 119:
            case 120:
            case 125:
            case 127:
            case 129:
            case 145:
            case 146:
            case 150:
            case 152:
            case 153:
            case 155:
            case 157:
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 7:
                    case 24:
                    case 32:
                    case 44:
                    case 55:
                    case 68:
                    case 70:
                    case 73:
                    case 77:
                    case 78:
                    case 80:
                    case 81:
                    case 91:
                    case 94:
                    case 95:
                    case 96:
                    case 97:
                    case 98:
                    case 99:
                    case 102:
                    case 104:
                    case 111:
                    case 112:
                    case 116:
                    case 119:
                    case 120:
                    case 125:
                    case 129:
                    case 145:
                    case 146:
                    case 150:
                    case 152:
                    case 153:
                    case 155:
                    case 157:
                        expr = SimpleExpression();
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 123:
                                jj_consume_token(123);
                                offset = SimpleExpression();
                                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                    case 123:
                                        jj_consume_token(123);
                                        defaultValue = SimpleExpression();
                                        break;
                                    default:
                                        this.jj_la1[175] = this.jj_gen;
                                        break;
                                }
                            default:
                                this.jj_la1[176] = this.jj_gen;
                                break;
                        }
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                    case 69:
                    case 71:
                    case 72:
                    case 74:
                    case 75:
                    case 76:
                    case 79:
                    case 82:
                    case 83:
                    case 84:
                    case 85:
                    case 86:
                    case 87:
                    case 88:
                    case 89:
                    case 90:
                    case 92:
                    case 93:
                    case 100:
                    case 101:
                    case 103:
                    case 105:
                    case 106:
                    case 107:
                    case 108:
                    case 109:
                    case 110:
                    case 113:
                    case 114:
                    case 115:
                    case 117:
                    case 118:
                    case 121:
                    case 122:
                    case 123:
                    case 124:
                    case 126:
                    case 128:
                    case 130:
                    case 131:
                    case 132:
                    case 133:
                    case 134:
                    case 135:
                    case 136:
                    case 137:
                    case 138:
                    case 139:
                    case 140:
                    case 141:
                    case 142:
                    case 143:
                    case 144:
                    case 147:
                    case 148:
                    case 149:
                    case 151:
                    case 154:
                    case 156:
                    default:
                        this.jj_la1[177] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                    case 127:
                        jj_consume_token(127);
                        retval.setAllColumns(true);
                        break;
                }
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 69:
            case 71:
            case 72:
            case 74:
            case 75:
            case 76:
            case 79:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 92:
            case 93:
            case 100:
            case 101:
            case 103:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 121:
            case 122:
            case 123:
            case 124:
            case 126:
            case 128:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 147:
            case 148:
            case 149:
            case 151:
            case 154:
            case 156:
            default:
                this.jj_la1[178] = this.jj_gen;
                break;
        }
        jj_consume_token(126);
        jj_consume_token(76);
        jj_consume_token(125);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 77:
                jj_consume_token(77);
                jj_consume_token(6);
                expressionList = SimpleExpressionList();
                break;
            default:
                this.jj_la1[179] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 53:
                olist = OrderByElements();
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 98:
                    case 99:
                        windowElement = WindowElement();
                        break;
                    default:
                        this.jj_la1[180] = this.jj_gen;
                        break;
                }
            default:
                this.jj_la1[181] = this.jj_gen;
                break;
        }
        retval.setExpression(expr);
        retval.setOffset(offset);
        retval.setDefaultValue(defaultValue);
        retval.setPartitionExpressionList(expressionList);
        retval.setOrderByElements(olist);
        retval.setWindowElement(windowElement);
        jj_consume_token(126);
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final WindowElement WindowElement() throws ParseException {
        WindowElement windowElement = new WindowElement();
        WindowRange range = new WindowRange();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 98:
                jj_consume_token(98);
                windowElement.setType(WindowElement.Type.ROWS);
                break;
            case 99:
                jj_consume_token(99);
                windowElement.setType(WindowElement.Type.RANGE);
                break;
            default:
                this.jj_la1[182] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 7:
            case 24:
            case 32:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 81:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 102:
            case 103:
            case 104:
            case 111:
            case 112:
            case 116:
            case 119:
            case 120:
            case 125:
            case 129:
            case 145:
            case 146:
            case 150:
            case 152:
            case 153:
            case 155:
            case 157:
                WindowOffset offset = WindowOffset();
                windowElement.setOffset(offset);
                break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 71:
            case 72:
            case 74:
            case 75:
            case 76:
            case 79:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 92:
            case 93:
            case 101:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 121:
            case 122:
            case 123:
            case 124:
            case 126:
            case 127:
            case 128:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 147:
            case 148:
            case 149:
            case 151:
            case 154:
            case 156:
            default:
                this.jj_la1[183] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            case 69:
                jj_consume_token(69);
                windowElement.setRange(range);
                WindowOffset offset2 = WindowOffset();
                range.setStart(offset2);
                jj_consume_token(13);
                WindowOffset offset3 = WindowOffset();
                range.setEnd(offset3);
                break;
        }
        if ("" != 0) {
            return windowElement;
        }
        throw new Error("Missing return statement in function");
    }

    public final WindowOffset WindowOffset() throws ParseException {
        WindowOffset offset = new WindowOffset();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 7:
            case 24:
            case 32:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 81:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 111:
            case 112:
            case 116:
            case 119:
            case 120:
            case 125:
            case 129:
            case 145:
            case 146:
            case 150:
            case 152:
            case 153:
            case 155:
            case 157:
                Expression expr = SimpleExpression();
                offset.setType(WindowOffset.Type.EXPR);
                offset.setExpression(expr);
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 101:
                        jj_consume_token(101);
                        offset.setType(WindowOffset.Type.PRECEDING);
                        break;
                    case 102:
                        jj_consume_token(102);
                        offset.setType(WindowOffset.Type.FOLLOWING);
                        break;
                    default:
                        this.jj_la1[185] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                if ("" != 0) {
                    return offset;
                }
                break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 69:
            case 71:
            case 72:
            case 74:
            case 75:
            case 76:
            case 79:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 92:
            case 93:
            case 101:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 121:
            case 122:
            case 123:
            case 124:
            case 126:
            case 127:
            case 128:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 147:
            case 148:
            case 149:
            case 151:
            case 154:
            case 156:
            default:
                this.jj_la1[186] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            case 100:
                jj_consume_token(100);
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 101:
                        jj_consume_token(101);
                        offset.setType(WindowOffset.Type.PRECEDING);
                        if ("" != 0) {
                            return offset;
                        }
                        break;
                    case 102:
                        jj_consume_token(102);
                        offset.setType(WindowOffset.Type.FOLLOWING);
                        if ("" != 0) {
                            return offset;
                        }
                        break;
                    default:
                        this.jj_la1[184] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            case 103:
                jj_consume_token(103);
                jj_consume_token(104);
                offset.setType(WindowOffset.Type.CURRENT);
                if ("" != 0) {
                    return offset;
                }
                break;
        }
        throw new Error("Missing return statement in function");
    }

    public final ExtractExpression ExtractExpression() throws ParseException {
        ExtractExpression retval = new ExtractExpression();
        jj_consume_token(78);
        jj_consume_token(125);
        Token token = jj_consume_token(116);
        retval.setName(token.image);
        jj_consume_token(30);
        Expression expr = SimpleExpression();
        retval.setExpression(expr);
        jj_consume_token(126);
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final CastExpression CastExpression() throws ParseException {
        CastExpression retval = new CastExpression();
        jj_consume_token(73);
        jj_consume_token(125);
        Expression expression = SimpleExpression();
        jj_consume_token(5);
        ColDataType type = ColDataType();
        jj_consume_token(126);
        retval.setUseCastKeyword(true);
        retval.setLeftExpression(expression);
        retval.setType(type);
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:41:0x03ee  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x03f0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final net.sf.jsqlparser.expression.Expression CaseWhenExpression() throws net.sf.jsqlparser.parser.ParseException {
        /*
            Method dump skipped, instructions count: 1018
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sf.jsqlparser.parser.CCJSqlParser.CaseWhenExpression():net.sf.jsqlparser.expression.Expression");
    }

    public final WhenClause WhenThenSearchCondition() throws ParseException {
        WhenClause whenThen = new WhenClause();
        jj_consume_token(33);
        Expression whenExp = Expression();
        jj_consume_token(34);
        Expression thenExp = SimpleExpression();
        whenThen.setWhenExpression(whenExp);
        whenThen.setThenExpression(thenExp);
        if ("" != 0) {
            return whenThen;
        }
        throw new Error("Missing return statement in function");
    }

    public final WhenClause WhenThenValue() throws ParseException {
        WhenClause whenThen = new WhenClause();
        jj_consume_token(33);
        Expression whenExp = PrimaryExpression();
        jj_consume_token(34);
        Expression thenExp = SimpleExpression();
        whenThen.setWhenExpression(whenExp);
        whenThen.setThenExpression(thenExp);
        if ("" != 0) {
            return whenThen;
        }
        throw new Error("Missing return statement in function");
    }

    public final Execute Execute() throws ParseException {
        ExpressionList expressionList = null;
        Execute execute = new Execute();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 109:
                jj_consume_token(109);
                break;
            case 110:
                jj_consume_token(110);
                break;
            default:
                this.jj_la1[192] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        String funcName = RelObjectName();
        execute.setName(funcName);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 7:
            case 24:
            case 32:
            case 44:
            case 55:
            case 68:
            case 70:
            case 73:
            case 77:
            case 78:
            case 80:
            case 81:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 111:
            case 112:
            case 116:
            case 119:
            case 120:
            case 125:
            case 129:
            case 145:
            case 146:
            case 150:
            case 152:
            case 153:
            case 155:
            case 157:
                expressionList = SimpleExpressionList();
                break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 69:
            case 71:
            case 72:
            case 74:
            case 75:
            case 76:
            case 79:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 92:
            case 93:
            case 100:
            case 101:
            case 103:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 121:
            case 122:
            case 123:
            case 124:
            case 126:
            case 127:
            case 128:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 147:
            case 148:
            case 149:
            case 151:
            case 154:
            case 156:
            default:
                this.jj_la1[193] = this.jj_gen;
                break;
        }
        execute.setExprList(expressionList);
        if ("" != 0) {
            return execute;
        }
        throw new Error("Missing return statement in function");
    }

    public final Function Function() throws ParseException {
        Function retval = new Function();
        ExpressionList expressionList = null;
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 157:
                jj_consume_token(157);
                retval.setEscaped(true);
                break;
            default:
                this.jj_la1[194] = this.jj_gen;
                break;
        }
        String funcName = RelObjectName();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 128:
                jj_consume_token(128);
                String tmp = RelObjectName();
                funcName = funcName + "." + tmp;
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 128:
                        jj_consume_token(128);
                        String tmp2 = RelObjectName();
                        funcName = funcName + "." + tmp2;
                        break;
                    default:
                        this.jj_la1[195] = this.jj_gen;
                        break;
                }
            default:
                this.jj_la1[196] = this.jj_gen;
                break;
        }
        jj_consume_token(125);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 7:
            case 12:
            case 24:
            case 32:
            case 44:
            case 55:
            case 68:
            case 70:
            case 71:
            case 73:
            case 77:
            case 78:
            case 80:
            case 81:
            case 91:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 102:
            case 104:
            case 111:
            case 112:
            case 116:
            case 119:
            case 120:
            case 125:
            case 127:
            case 129:
            case 145:
            case 146:
            case 150:
            case 152:
            case 153:
            case 155:
            case 157:
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 12:
                    case 71:
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 12:
                                jj_consume_token(12);
                                retval.setAllColumns(true);
                                break;
                            case 71:
                                jj_consume_token(71);
                                retval.setDistinct(true);
                                break;
                            default:
                                this.jj_la1[197] = this.jj_gen;
                                jj_consume_token(-1);
                                throw new ParseException();
                        }
                    default:
                        this.jj_la1[198] = this.jj_gen;
                        break;
                }
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 7:
                    case 24:
                    case 32:
                    case 44:
                    case 55:
                    case 68:
                    case 70:
                    case 73:
                    case 77:
                    case 78:
                    case 80:
                    case 81:
                    case 91:
                    case 94:
                    case 95:
                    case 96:
                    case 97:
                    case 98:
                    case 99:
                    case 102:
                    case 104:
                    case 111:
                    case 112:
                    case 116:
                    case 119:
                    case 120:
                    case 125:
                    case 129:
                    case 145:
                    case 146:
                    case 150:
                    case 152:
                    case 153:
                    case 155:
                    case 157:
                        expressionList = SimpleExpressionList();
                        break;
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                    case 69:
                    case 71:
                    case 72:
                    case 74:
                    case 75:
                    case 76:
                    case 79:
                    case 82:
                    case 83:
                    case 84:
                    case 85:
                    case 86:
                    case 87:
                    case 88:
                    case 89:
                    case 90:
                    case 92:
                    case 93:
                    case 100:
                    case 101:
                    case 103:
                    case 105:
                    case 106:
                    case 107:
                    case 108:
                    case 109:
                    case 110:
                    case 113:
                    case 114:
                    case 115:
                    case 117:
                    case 118:
                    case 121:
                    case 122:
                    case 123:
                    case 124:
                    case 126:
                    case 128:
                    case 130:
                    case 131:
                    case 132:
                    case 133:
                    case 134:
                    case 135:
                    case 136:
                    case 137:
                    case 138:
                    case 139:
                    case 140:
                    case 141:
                    case 142:
                    case 143:
                    case 144:
                    case 147:
                    case 148:
                    case 149:
                    case 151:
                    case 154:
                    case 156:
                    default:
                        this.jj_la1[199] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                    case 127:
                        jj_consume_token(127);
                        retval.setAllColumns(true);
                        break;
                }
            case 8:
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 69:
            case 72:
            case 74:
            case 75:
            case 76:
            case 79:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 92:
            case 93:
            case 100:
            case 101:
            case 103:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 121:
            case 122:
            case 123:
            case 124:
            case 126:
            case 128:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 147:
            case 148:
            case 149:
            case 151:
            case 154:
            case 156:
            default:
                this.jj_la1[200] = this.jj_gen;
                break;
        }
        jj_consume_token(126);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 151:
                jj_consume_token(151);
                break;
            default:
                this.jj_la1[201] = this.jj_gen;
                break;
        }
        retval.setParameters(expressionList);
        retval.setName(funcName);
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    public final SubSelect SubSelect() throws ParseException {
        SelectBody selectBody = SelectBody();
        SubSelect subSelect = new SubSelect();
        subSelect.setSelectBody(selectBody);
        if ("" != 0) {
            return subSelect;
        }
        throw new Error("Missing return statement in function");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final net.sf.jsqlparser.statement.create.index.CreateIndex CreateIndex() throws net.sf.jsqlparser.parser.ParseException {
        /*
            Method dump skipped, instructions count: 1324
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sf.jsqlparser.parser.CCJSqlParser.CreateIndex():net.sf.jsqlparser.statement.create.index.CreateIndex");
    }

    public final CreateTable CreateTable() throws ParseException {
        Token columnName;
        Token columnName2;
        CreateTable createTable = new CreateTable();
        List columnDefinitions = new ArrayList();
        List tableOptions = new ArrayList();
        List indexes = new ArrayList();
        jj_consume_token(57);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 108:
                jj_consume_token(108);
                createTable.setUnlogged(true);
                break;
            default:
                this.jj_la1[211] = this.jj_gen;
                break;
        }
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 15:
                case 16:
                case 24:
                case 66:
                case 111:
                case 112:
                case 116:
                case 119:
                case 124:
                case 125:
                    CreateParameter();
                default:
                    this.jj_la1[212] = this.jj_gen;
                    jj_consume_token(39);
                    Table table = Table();
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 5:
                        case 125:
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 5:
                                    jj_consume_token(5);
                                    Select select = Select();
                                    createTable.setSelect(select);
                                    break;
                                case 125:
                                    jj_consume_token(125);
                                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                        case 116:
                                            columnName = jj_consume_token(116);
                                            break;
                                        case 120:
                                            columnName = jj_consume_token(120);
                                            break;
                                        default:
                                            this.jj_la1[213] = this.jj_gen;
                                            jj_consume_token(-1);
                                            throw new ParseException();
                                    }
                                    ColDataType colDataType = ColDataType();
                                    List columnSpecs = new ArrayList();
                                    while (true) {
                                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                            case 15:
                                            case 16:
                                            case 24:
                                            case 66:
                                            case 111:
                                            case 112:
                                            case 116:
                                            case 119:
                                            case 124:
                                            case 125:
                                                String parameter = CreateParameter();
                                                columnSpecs.add(parameter);
                                            default:
                                                this.jj_la1[214] = this.jj_gen;
                                                ColumnDefinition coldef = new ColumnDefinition();
                                                coldef.setColumnName(columnName.image);
                                                coldef.setColDataType(colDataType);
                                                if (columnSpecs.size() > 0) {
                                                    coldef.setColumnSpecStrings(columnSpecs);
                                                }
                                                columnDefinitions.add(coldef);
                                                while (true) {
                                                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                                        case 123:
                                                            jj_consume_token(123);
                                                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                                                case 49:
                                                                    Token tk2 = jj_consume_token(49);
                                                                    Token tk3 = jj_consume_token(116);
                                                                    List colNames = ColumnsNamesList();
                                                                    Index index = new Index();
                                                                    index.setType(tk2.image);
                                                                    index.setName(tk3.image);
                                                                    index.setColumnsNames(colNames);
                                                                    indexes.add(index);
                                                                    break;
                                                                default:
                                                                    this.jj_la1[220] = this.jj_gen;
                                                                    if (jj_2_58(3)) {
                                                                        Index index2 = new NamedConstraint();
                                                                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                                                            case 83:
                                                                                jj_consume_token(83);
                                                                                Token tk32 = jj_consume_token(116);
                                                                                index2.setName(tk32.image);
                                                                                break;
                                                                            default:
                                                                                this.jj_la1[216] = this.jj_gen;
                                                                                break;
                                                                        }
                                                                        Token tk4 = jj_consume_token(66);
                                                                        Token tk22 = jj_consume_token(15);
                                                                        List colNames2 = ColumnsNamesList();
                                                                        index2.setType(tk4.image + SymbolConstants.SPACE_SYMBOL + tk22.image);
                                                                        index2.setColumnsNames(colNames2);
                                                                        indexes.add(index2);
                                                                        break;
                                                                    } else {
                                                                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                                                            case 15:
                                                                                Token tk5 = jj_consume_token(15);
                                                                                Token tk33 = jj_consume_token(116);
                                                                                List colNames3 = ColumnsNamesList();
                                                                                Index index3 = new Index();
                                                                                index3.setType(tk5.image);
                                                                                index3.setName(tk33.image);
                                                                                index3.setColumnsNames(colNames3);
                                                                                indexes.add(index3);
                                                                                break;
                                                                            case 82:
                                                                            case 83:
                                                                                ForeignKeyIndex fkIndex = new ForeignKeyIndex();
                                                                                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                                                                    case 83:
                                                                                        jj_consume_token(83);
                                                                                        Token tk34 = jj_consume_token(116);
                                                                                        fkIndex.setName(tk34.image);
                                                                                        break;
                                                                                    default:
                                                                                        this.jj_la1[217] = this.jj_gen;
                                                                                        break;
                                                                                }
                                                                                Token tk6 = jj_consume_token(82);
                                                                                Token tk23 = jj_consume_token(15);
                                                                                List colNames4 = ColumnsNamesList();
                                                                                fkIndex.setType(tk6.image + SymbolConstants.SPACE_SYMBOL + tk23.image);
                                                                                fkIndex.setColumnsNames(colNames4);
                                                                                jj_consume_token(84);
                                                                                Table fkTable = Table();
                                                                                List colNames5 = ColumnsNamesList();
                                                                                fkIndex.setTable(fkTable);
                                                                                fkIndex.setReferencedColumnNames(colNames5);
                                                                                indexes.add(fkIndex);
                                                                                break;
                                                                            case 116:
                                                                            case 120:
                                                                                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                                                                    case 116:
                                                                                        columnName2 = jj_consume_token(116);
                                                                                        break;
                                                                                    case 120:
                                                                                        columnName2 = jj_consume_token(120);
                                                                                        break;
                                                                                    default:
                                                                                        this.jj_la1[218] = this.jj_gen;
                                                                                        jj_consume_token(-1);
                                                                                        throw new ParseException();
                                                                                }
                                                                                ColDataType colDataType2 = ColDataType();
                                                                                List columnSpecs2 = new ArrayList();
                                                                                while (true) {
                                                                                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                                                                        case 15:
                                                                                        case 16:
                                                                                        case 24:
                                                                                        case 66:
                                                                                        case 111:
                                                                                        case 112:
                                                                                        case 116:
                                                                                        case 119:
                                                                                        case 124:
                                                                                        case 125:
                                                                                            String parameter2 = CreateParameter();
                                                                                            columnSpecs2.add(parameter2);
                                                                                        default:
                                                                                            this.jj_la1[219] = this.jj_gen;
                                                                                            ColumnDefinition coldef2 = new ColumnDefinition();
                                                                                            coldef2.setColumnName(columnName2.image);
                                                                                            coldef2.setColDataType(colDataType2);
                                                                                            if (columnSpecs2.size() > 0) {
                                                                                                coldef2.setColumnSpecStrings(columnSpecs2);
                                                                                            }
                                                                                            columnDefinitions.add(coldef2);
                                                                                            break;
                                                                                    }
                                                                                }
                                                                            default:
                                                                                this.jj_la1[221] = this.jj_gen;
                                                                                jj_consume_token(-1);
                                                                                throw new ParseException();
                                                                        }
                                                                    }
                                                            }
                                                        default:
                                                            this.jj_la1[215] = this.jj_gen;
                                                            jj_consume_token(126);
                                                            while (true) {
                                                                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                                                    case 15:
                                                                    case 16:
                                                                    case 24:
                                                                    case 66:
                                                                    case 111:
                                                                    case 112:
                                                                    case 116:
                                                                    case 119:
                                                                    case 124:
                                                                    case 125:
                                                                        String parameter3 = CreateParameter();
                                                                        tableOptions.add(parameter3);
                                                                    default:
                                                                        this.jj_la1[222] = this.jj_gen;
                                                                        break;
                                                                }
                                                            }
                                                    }
                                                }
                                        }
                                    }
                                default:
                                    this.jj_la1[223] = this.jj_gen;
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                        default:
                            this.jj_la1[224] = this.jj_gen;
                            break;
                    }
                    createTable.setTable(table);
                    if (indexes.size() > 0) {
                        createTable.setIndexes(indexes);
                    }
                    if (tableOptions.size() > 0) {
                        createTable.setTableOptionsStrings(tableOptions);
                    }
                    if (columnDefinitions.size() > 0) {
                        createTable.setColumnDefinitions(columnDefinitions);
                    }
                    if ("" != 0) {
                        return createTable;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final ColDataType ColDataType() throws ParseException {
        Token tk2;
        ColDataType colDataType = new ColDataType();
        ArrayList argumentsStringList = new ArrayList();
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 85:
                Token tk3 = jj_consume_token(85);
                Token tk22 = jj_consume_token(86);
                colDataType.setDataType(tk3.image + SymbolConstants.SPACE_SYMBOL + tk22.image);
                break;
            case 116:
                Token tk4 = jj_consume_token(116);
                colDataType.setDataType(tk4.image);
                break;
            default:
                this.jj_la1[225] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        if (jj_2_59(2)) {
            jj_consume_token(125);
            while (true) {
                switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                    case 112:
                    case 119:
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 112:
                                tk2 = jj_consume_token(112);
                                break;
                            case 119:
                                tk2 = jj_consume_token(119);
                                break;
                            default:
                                this.jj_la1[227] = this.jj_gen;
                                jj_consume_token(-1);
                                throw new ParseException();
                        }
                        argumentsStringList.add(tk2.image);
                        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                            case 123:
                                jj_consume_token(123);
                                break;
                            default:
                                this.jj_la1[228] = this.jj_gen;
                                break;
                        }
                    default:
                        this.jj_la1[226] = this.jj_gen;
                        jj_consume_token(126);
                        break;
                }
            }
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 85:
                jj_consume_token(85);
                jj_consume_token(17);
                Token tk5 = jj_consume_token(116);
                colDataType.setCharacterSet(tk5.image);
                break;
            default:
                this.jj_la1[229] = this.jj_gen;
                break;
        }
        if (argumentsStringList.size() > 0) {
            colDataType.setArgumentsStringList(argumentsStringList);
        }
        if ("" != 0) {
            return colDataType;
        }
        throw new Error("Missing return statement in function");
    }

    public final CreateView CreateView() throws ParseException {
        CreateView createView = new CreateView();
        jj_consume_token(57);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 10:
                jj_consume_token(10);
                jj_consume_token(68);
                createView.setOrReplace(true);
                break;
            default:
                this.jj_la1[230] = this.jj_gen;
                break;
        }
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 80:
                jj_consume_token(80);
                createView.setMaterialized(true);
                break;
            default:
                this.jj_la1[231] = this.jj_gen;
                break;
        }
        jj_consume_token(40);
        Table view = Table();
        createView.setView(view);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 125:
                List<String> columnNames = ColumnsNamesList();
                createView.setColumnNames(columnNames);
                break;
            default:
                this.jj_la1[232] = this.jj_gen;
                break;
        }
        jj_consume_token(5);
        if (jj_2_60(Integer.MAX_VALUE)) {
            SelectBody select = SelectBody();
            createView.setSelectBody(select);
        } else {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 125:
                    jj_consume_token(125);
                    SelectBody select2 = SelectBody();
                    jj_consume_token(126);
                    createView.setSelectBody(select2);
                    break;
                default:
                    this.jj_la1[233] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        if ("" != 0) {
            return createView;
        }
        throw new Error("Missing return statement in function");
    }

    public final String CreateParameter() throws ParseException {
        String retval;
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 15:
                Token tk2 = jj_consume_token(15);
                retval = tk2.image;
                break;
            case 16:
                Token tk3 = jj_consume_token(16);
                retval = tk3.image;
                break;
            case 24:
                Token tk4 = jj_consume_token(24);
                retval = tk4.image;
                break;
            case 66:
                Token tk5 = jj_consume_token(66);
                retval = tk5.image;
                break;
            case 111:
                Token tk6 = jj_consume_token(111);
                retval = tk6.image;
                break;
            case 112:
                Token tk7 = jj_consume_token(112);
                retval = tk7.image;
                break;
            case 116:
                Token tk8 = jj_consume_token(116);
                retval = tk8.image;
                break;
            case 119:
                Token tk9 = jj_consume_token(119);
                retval = tk9.image;
                break;
            case 124:
                jj_consume_token(124);
                retval = SymbolConstants.EQUAL_SYMBOL;
                break;
            case 125:
                retval = AList();
                break;
            default:
                this.jj_la1[234] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        if ("" != 0) {
            return retval;
        }
        throw new Error("Missing return statement in function");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final java.lang.String AList() throws net.sf.jsqlparser.parser.ParseException {
        /*
            Method dump skipped, instructions count: 356
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sf.jsqlparser.parser.CCJSqlParser.AList():java.lang.String");
    }

    public final List<String> ColumnsNamesList() throws ParseException {
        List<String> retval = new ArrayList<>();
        jj_consume_token(125);
        Token tk2 = jj_consume_token(116);
        retval.add(tk2.image);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 123:
                    jj_consume_token(123);
                    Token tk3 = jj_consume_token(116);
                    retval.add(tk3.image);
                default:
                    this.jj_la1[238] = this.jj_gen;
                    jj_consume_token(126);
                    if ("" != 0) {
                        return retval;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final Drop Drop() throws ParseException {
        Token tk2;
        Drop drop = new Drop();
        List<String> dropArgs = new ArrayList<>();
        jj_consume_token(26);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 39:
                tk2 = jj_consume_token(39);
                break;
            case 49:
                tk2 = jj_consume_token(49);
                break;
            case 116:
                tk2 = jj_consume_token(116);
                break;
            default:
                this.jj_la1[239] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        drop.setType(tk2.image);
        Token tk3 = jj_consume_token(116);
        drop.setName(tk3.image);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 116:
                    Token tk4 = jj_consume_token(116);
                    dropArgs.add(tk4.image);
                default:
                    this.jj_la1[240] = this.jj_gen;
                    if (dropArgs.size() > 0) {
                        drop.setParameters(dropArgs);
                    }
                    if ("" != 0) {
                        return drop;
                    }
                    throw new Error("Missing return statement in function");
            }
        }
    }

    public final Truncate Truncate() throws ParseException {
        Truncate truncate = new Truncate();
        jj_consume_token(70);
        jj_consume_token(39);
        Table table = Table();
        truncate.setTable(table);
        if ("" != 0) {
            return truncate;
        }
        throw new Error("Missing return statement in function");
    }

    public final Alter Alter() throws ParseException {
        Token tk2;
        Alter alter = new Alter();
        jj_consume_token(92);
        jj_consume_token(39);
        Table table = Table();
        jj_consume_token(93);
        jj_consume_token(94);
        switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
            case 116:
                tk2 = jj_consume_token(116);
                break;
            case 120:
                tk2 = jj_consume_token(120);
                break;
            default:
                this.jj_la1[241] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        ColDataType dataType = ColDataType();
        alter.setTable(table);
        alter.setColumnName(tk2.image);
        alter.setDataType(dataType);
        if ("" != 0) {
            return alter;
        }
        throw new Error("Missing return statement in function");
    }

    private boolean jj_2_1(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_1();
            jj_save(0, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(0, xla);
            return true;
        } catch (Throwable th) {
            jj_save(0, xla);
            throw th;
        }
    }

    private boolean jj_2_2(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_2();
            jj_save(1, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(1, xla);
            return true;
        } catch (Throwable th) {
            jj_save(1, xla);
            throw th;
        }
    }

    private boolean jj_2_3(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_3();
            jj_save(2, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(2, xla);
            return true;
        } catch (Throwable th) {
            jj_save(2, xla);
            throw th;
        }
    }

    private boolean jj_2_4(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_4();
            jj_save(3, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(3, xla);
            return true;
        } catch (Throwable th) {
            jj_save(3, xla);
            throw th;
        }
    }

    private boolean jj_2_5(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_5();
            jj_save(4, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(4, xla);
            return true;
        } catch (Throwable th) {
            jj_save(4, xla);
            throw th;
        }
    }

    private boolean jj_2_6(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_6();
            jj_save(5, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(5, xla);
            return true;
        } catch (Throwable th) {
            jj_save(5, xla);
            throw th;
        }
    }

    private boolean jj_2_7(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_7();
            jj_save(6, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(6, xla);
            return true;
        } catch (Throwable th) {
            jj_save(6, xla);
            throw th;
        }
    }

    private boolean jj_2_8(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_8();
            jj_save(7, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(7, xla);
            return true;
        } catch (Throwable th) {
            jj_save(7, xla);
            throw th;
        }
    }

    private boolean jj_2_9(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_9();
            jj_save(8, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(8, xla);
            return true;
        } catch (Throwable th) {
            jj_save(8, xla);
            throw th;
        }
    }

    private boolean jj_2_10(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_10();
            jj_save(9, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(9, xla);
            return true;
        } catch (Throwable th) {
            jj_save(9, xla);
            throw th;
        }
    }

    private boolean jj_2_11(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_11();
            jj_save(10, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(10, xla);
            return true;
        } catch (Throwable th) {
            jj_save(10, xla);
            throw th;
        }
    }

    private boolean jj_2_12(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_12();
            jj_save(11, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(11, xla);
            return true;
        } catch (Throwable th) {
            jj_save(11, xla);
            throw th;
        }
    }

    private boolean jj_2_13(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_13();
            jj_save(12, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(12, xla);
            return true;
        } catch (Throwable th) {
            jj_save(12, xla);
            throw th;
        }
    }

    private boolean jj_2_14(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_14();
            jj_save(13, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(13, xla);
            return true;
        } catch (Throwable th) {
            jj_save(13, xla);
            throw th;
        }
    }

    private boolean jj_2_15(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_15();
            jj_save(14, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(14, xla);
            return true;
        } catch (Throwable th) {
            jj_save(14, xla);
            throw th;
        }
    }

    private boolean jj_2_16(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_16();
            jj_save(15, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(15, xla);
            return true;
        } catch (Throwable th) {
            jj_save(15, xla);
            throw th;
        }
    }

    private boolean jj_2_17(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_17();
            jj_save(16, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(16, xla);
            return true;
        } catch (Throwable th) {
            jj_save(16, xla);
            throw th;
        }
    }

    private boolean jj_2_18(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_18();
            jj_save(17, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(17, xla);
            return true;
        } catch (Throwable th) {
            jj_save(17, xla);
            throw th;
        }
    }

    private boolean jj_2_19(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_19();
            jj_save(18, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(18, xla);
            return true;
        } catch (Throwable th) {
            jj_save(18, xla);
            throw th;
        }
    }

    private boolean jj_2_20(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_20();
            jj_save(19, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(19, xla);
            return true;
        } catch (Throwable th) {
            jj_save(19, xla);
            throw th;
        }
    }

    private boolean jj_2_21(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_21();
            jj_save(20, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(20, xla);
            return true;
        } catch (Throwable th) {
            jj_save(20, xla);
            throw th;
        }
    }

    private boolean jj_2_22(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_22();
            jj_save(21, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(21, xla);
            return true;
        } catch (Throwable th) {
            jj_save(21, xla);
            throw th;
        }
    }

    private boolean jj_2_23(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_23();
            jj_save(22, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(22, xla);
            return true;
        } catch (Throwable th) {
            jj_save(22, xla);
            throw th;
        }
    }

    private boolean jj_2_24(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_24();
            jj_save(23, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(23, xla);
            return true;
        } catch (Throwable th) {
            jj_save(23, xla);
            throw th;
        }
    }

    private boolean jj_2_25(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_25();
            jj_save(24, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(24, xla);
            return true;
        } catch (Throwable th) {
            jj_save(24, xla);
            throw th;
        }
    }

    private boolean jj_2_26(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_26();
            jj_save(25, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(25, xla);
            return true;
        } catch (Throwable th) {
            jj_save(25, xla);
            throw th;
        }
    }

    private boolean jj_2_27(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_27();
            jj_save(26, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(26, xla);
            return true;
        } catch (Throwable th) {
            jj_save(26, xla);
            throw th;
        }
    }

    private boolean jj_2_28(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_28();
            jj_save(27, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(27, xla);
            return true;
        } catch (Throwable th) {
            jj_save(27, xla);
            throw th;
        }
    }

    private boolean jj_2_29(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_29();
            jj_save(28, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(28, xla);
            return true;
        } catch (Throwable th) {
            jj_save(28, xla);
            throw th;
        }
    }

    private boolean jj_2_30(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_30();
            jj_save(29, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(29, xla);
            return true;
        } catch (Throwable th) {
            jj_save(29, xla);
            throw th;
        }
    }

    private boolean jj_2_31(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_31();
            jj_save(30, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(30, xla);
            return true;
        } catch (Throwable th) {
            jj_save(30, xla);
            throw th;
        }
    }

    private boolean jj_2_32(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_32();
            jj_save(31, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(31, xla);
            return true;
        } catch (Throwable th) {
            jj_save(31, xla);
            throw th;
        }
    }

    private boolean jj_2_33(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_33();
            jj_save(32, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(32, xla);
            return true;
        } catch (Throwable th) {
            jj_save(32, xla);
            throw th;
        }
    }

    private boolean jj_2_34(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_34();
            jj_save(33, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(33, xla);
            return true;
        } catch (Throwable th) {
            jj_save(33, xla);
            throw th;
        }
    }

    private boolean jj_2_35(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_35();
            jj_save(34, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(34, xla);
            return true;
        } catch (Throwable th) {
            jj_save(34, xla);
            throw th;
        }
    }

    private boolean jj_2_36(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_36();
            jj_save(35, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(35, xla);
            return true;
        } catch (Throwable th) {
            jj_save(35, xla);
            throw th;
        }
    }

    private boolean jj_2_37(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_37();
            jj_save(36, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(36, xla);
            return true;
        } catch (Throwable th) {
            jj_save(36, xla);
            throw th;
        }
    }

    private boolean jj_2_38(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_38();
            jj_save(37, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(37, xla);
            return true;
        } catch (Throwable th) {
            jj_save(37, xla);
            throw th;
        }
    }

    private boolean jj_2_39(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_39();
            jj_save(38, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(38, xla);
            return true;
        } catch (Throwable th) {
            jj_save(38, xla);
            throw th;
        }
    }

    private boolean jj_2_40(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_40();
            jj_save(39, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(39, xla);
            return true;
        } catch (Throwable th) {
            jj_save(39, xla);
            throw th;
        }
    }

    private boolean jj_2_41(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_41();
            jj_save(40, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(40, xla);
            return true;
        } catch (Throwable th) {
            jj_save(40, xla);
            throw th;
        }
    }

    private boolean jj_2_42(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_42();
            jj_save(41, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(41, xla);
            return true;
        } catch (Throwable th) {
            jj_save(41, xla);
            throw th;
        }
    }

    private boolean jj_2_43(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_43();
            jj_save(42, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(42, xla);
            return true;
        } catch (Throwable th) {
            jj_save(42, xla);
            throw th;
        }
    }

    private boolean jj_2_44(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_44();
            jj_save(43, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(43, xla);
            return true;
        } catch (Throwable th) {
            jj_save(43, xla);
            throw th;
        }
    }

    private boolean jj_2_45(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_45();
            jj_save(44, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(44, xla);
            return true;
        } catch (Throwable th) {
            jj_save(44, xla);
            throw th;
        }
    }

    private boolean jj_2_46(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_46();
            jj_save(45, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(45, xla);
            return true;
        } catch (Throwable th) {
            jj_save(45, xla);
            throw th;
        }
    }

    private boolean jj_2_47(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_47();
            jj_save(46, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(46, xla);
            return true;
        } catch (Throwable th) {
            jj_save(46, xla);
            throw th;
        }
    }

    private boolean jj_2_48(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_48();
            jj_save(47, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(47, xla);
            return true;
        } catch (Throwable th) {
            jj_save(47, xla);
            throw th;
        }
    }

    private boolean jj_2_49(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_49();
            jj_save(48, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(48, xla);
            return true;
        } catch (Throwable th) {
            jj_save(48, xla);
            throw th;
        }
    }

    private boolean jj_2_50(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_50();
            jj_save(49, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(49, xla);
            return true;
        } catch (Throwable th) {
            jj_save(49, xla);
            throw th;
        }
    }

    private boolean jj_2_51(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_51();
            jj_save(50, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(50, xla);
            return true;
        } catch (Throwable th) {
            jj_save(50, xla);
            throw th;
        }
    }

    private boolean jj_2_52(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_52();
            jj_save(51, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(51, xla);
            return true;
        } catch (Throwable th) {
            jj_save(51, xla);
            throw th;
        }
    }

    private boolean jj_2_53(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_53();
            jj_save(52, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(52, xla);
            return true;
        } catch (Throwable th) {
            jj_save(52, xla);
            throw th;
        }
    }

    private boolean jj_2_54(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_54();
            jj_save(53, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(53, xla);
            return true;
        } catch (Throwable th) {
            jj_save(53, xla);
            throw th;
        }
    }

    private boolean jj_2_55(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_55();
            jj_save(54, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(54, xla);
            return true;
        } catch (Throwable th) {
            jj_save(54, xla);
            throw th;
        }
    }

    private boolean jj_2_56(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_56();
            jj_save(55, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(55, xla);
            return true;
        } catch (Throwable th) {
            jj_save(55, xla);
            throw th;
        }
    }

    private boolean jj_2_57(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_57();
            jj_save(56, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(56, xla);
            return true;
        } catch (Throwable th) {
            jj_save(56, xla);
            throw th;
        }
    }

    private boolean jj_2_58(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_58();
            jj_save(57, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(57, xla);
            return true;
        } catch (Throwable th) {
            jj_save(57, xla);
            throw th;
        }
    }

    private boolean jj_2_59(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_59();
            jj_save(58, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(58, xla);
            return true;
        } catch (Throwable th) {
            jj_save(58, xla);
            throw th;
        }
    }

    private boolean jj_2_60(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_60();
            jj_save(59, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(59, xla);
            return true;
        } catch (Throwable th) {
            jj_save(59, xla);
            throw th;
        }
    }

    private boolean jj_3R_420() {
        Token xsp;
        if (jj_scan_token(125) || jj_3R_59()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_428());
        this.jj_scanpos = xsp;
        return jj_scan_token(126);
    }

    private boolean jj_3R_299() {
        return jj_scan_token(124);
    }

    private boolean jj_3R_427() {
        return jj_3R_258();
    }

    private boolean jj_3R_355() {
        return jj_scan_token(71);
    }

    private boolean jj_3R_342() {
        Token xsp;
        if (jj_scan_token(125)) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_379());
        this.jj_scanpos = xsp;
        return jj_scan_token(126);
    }

    private boolean jj_3R_298() {
        return jj_scan_token(111);
    }

    private boolean jj_3R_421() {
        return jj_3R_59();
    }

    private boolean jj_3R_297() {
        return jj_scan_token(112);
    }

    private boolean jj_3R_428() {
        return jj_scan_token(123) || jj_3R_59();
    }

    private boolean jj_3R_296() {
        return jj_scan_token(119);
    }

    private boolean jj_3R_295() {
        return jj_scan_token(15);
    }

    private boolean jj_3R_208() {
        return jj_scan_token(123) || jj_3R_63();
    }

    private boolean jj_3R_294() {
        return jj_scan_token(66);
    }

    private boolean jj_3R_63() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_154()) {
            this.jj_scanpos = xsp;
            return jj_3R_155();
        }
        return false;
    }

    private boolean jj_3R_293() {
        return jj_scan_token(16);
    }

    private boolean jj_3R_408() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_420()) {
            this.jj_scanpos = xsp;
            return jj_3R_421();
        }
        return false;
    }

    private boolean jj_3R_292() {
        return jj_scan_token(24);
    }

    private boolean jj_3R_291() {
        return jj_scan_token(116);
    }

    private boolean jj_3R_347() {
        return jj_scan_token(123) || jj_3R_346();
    }

    private boolean jj_3R_336() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(14)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(36)) {
                return true;
            }
        }
        return jj_scan_token(125) || jj_3R_91() || jj_scan_token(126);
    }

    private boolean jj_3R_418() {
        if (jj_3R_110()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_427()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_223() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_291()) {
            this.jj_scanpos = xsp;
            if (jj_3R_292()) {
                this.jj_scanpos = xsp;
                if (jj_3R_293()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_294()) {
                        this.jj_scanpos = xsp;
                        if (jj_3R_295()) {
                            this.jj_scanpos = xsp;
                            if (jj_3R_296()) {
                                this.jj_scanpos = xsp;
                                if (jj_3R_297()) {
                                    this.jj_scanpos = xsp;
                                    if (jj_3R_298()) {
                                        this.jj_scanpos = xsp;
                                        if (jj_3R_299()) {
                                            this.jj_scanpos = xsp;
                                            return jj_3R_300();
                                        }
                                        return false;
                                    }
                                    return false;
                                }
                                return false;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_316() {
        return jj_scan_token(5);
    }

    private boolean jj_3R_258() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_316()) {
            this.jj_scanpos = xsp;
        }
        return jj_3R_64();
    }

    private boolean jj_3R_354() {
        return jj_scan_token(12);
    }

    private boolean jj_3R_335() {
        return jj_scan_token(12) || jj_scan_token(125) || jj_3R_91() || jj_scan_token(126);
    }

    private boolean jj_3R_313() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_354()) {
            this.jj_scanpos = xsp;
            return jj_3R_355();
        }
        return false;
    }

    private boolean jj_3_60() {
        return jj_3R_120();
    }

    private boolean jj_3_21() {
        return jj_3R_72();
    }

    private boolean jj_3R_273() {
        return jj_3R_63();
    }

    private boolean jj_3R_71() {
        return jj_3R_163() || jj_scan_token(128) || jj_scan_token(127);
    }

    private boolean jj_3R_272() {
        return jj_3R_336();
    }

    private boolean jj_3_20() {
        return jj_3R_71();
    }

    private boolean jj_3R_164() {
        return jj_3R_258();
    }

    private boolean jj_3R_271() {
        return jj_3R_335();
    }

    private boolean jj_3R_125() {
        return jj_scan_token(80);
    }

    private boolean jj_3R_124() {
        return jj_scan_token(10);
    }

    private boolean jj_3R_182() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_271()) {
            this.jj_scanpos = xsp;
            if (jj_3R_272()) {
                this.jj_scanpos = xsp;
                return jj_3R_273();
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_119() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(112)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(119)) {
                return true;
            }
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_378()) {
            this.jj_scanpos = xsp2;
            return false;
        }
        return false;
    }

    private boolean jj_3R_57() {
        if (jj_scan_token(57)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_124()) {
            this.jj_scanpos = xsp;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_125()) {
            this.jj_scanpos = xsp2;
        }
        return jj_scan_token(40);
    }

    private boolean jj_3R_387() {
        return jj_3R_72();
    }

    private boolean jj_3R_386() {
        return jj_3R_71();
    }

    private boolean jj_3R_203() {
        return jj_3R_90();
    }

    private boolean jj_3R_278() {
        return jj_scan_token(16);
    }

    private boolean jj_3R_385() {
        return jj_scan_token(127);
    }

    private boolean jj_3R_90() {
        Token xsp;
        if (jj_3R_63()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_208());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_346() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_385()) {
            this.jj_scanpos = xsp;
            if (jj_3R_386()) {
                this.jj_scanpos = xsp;
                return jj_3R_387();
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_72() {
        if (jj_3R_63()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_164()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_249() {
        return jj_3R_243();
    }

    private boolean jj_3_59() {
        Token xsp;
        if (jj_scan_token(125)) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_119());
        this.jj_scanpos = xsp;
        return jj_scan_token(126);
    }

    private boolean jj_3R_289() {
        return jj_scan_token(116);
    }

    private boolean jj_3R_288() {
        return jj_scan_token(85) || jj_scan_token(86);
    }

    private boolean jj_3R_290() {
        return jj_scan_token(85) || jj_scan_token(17) || jj_scan_token(116);
    }

    private boolean jj_3R_220() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_288()) {
            this.jj_scanpos = xsp;
            if (jj_3R_289()) {
                return true;
            }
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3_59()) {
            this.jj_scanpos = xsp2;
        }
        Token xsp3 = this.jj_scanpos;
        if (jj_3R_290()) {
            this.jj_scanpos = xsp3;
            return false;
        }
        return false;
    }

    private boolean jj_3R_304() {
        Token xsp;
        if (jj_3R_346()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_347());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_157() {
        return jj_3R_243();
    }

    private boolean jj_3R_207() {
        return jj_scan_token(16);
    }

    private boolean jj_3R_89() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_207()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(60) || jj_3R_63();
    }

    private boolean jj_3R_248() {
        return jj_scan_token(125) || jj_3R_243() || jj_scan_token(126);
    }

    private boolean jj_3_42() {
        return jj_3R_91();
    }

    private boolean jj_3R_206() {
        if (jj_3R_63() || jj_scan_token(8)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_278()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(24);
    }

    private boolean jj_3R_247() {
        return jj_scan_token(74);
    }

    private boolean jj_3R_246() {
        return jj_scan_token(75);
    }

    private boolean jj_3R_205() {
        return jj_scan_token(16) || jj_3R_63() || jj_scan_token(8) || jj_scan_token(24);
    }

    private boolean jj_3R_245() {
        return jj_scan_token(72);
    }

    private boolean jj_3R_88() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_205()) {
            this.jj_scanpos = xsp;
            return jj_3R_206();
        }
        return false;
    }

    private boolean jj_3R_343() {
        return jj_scan_token(38);
    }

    private boolean jj_3R_244() {
        if (jj_scan_token(46)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_313()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_158() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_244()) {
            this.jj_scanpos = xsp;
            if (jj_3R_245()) {
                this.jj_scanpos = xsp;
                if (jj_3R_246()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_247()) {
                        return true;
                    }
                }
            }
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_248()) {
            this.jj_scanpos = xsp2;
            return jj_3R_249();
        }
        return false;
    }

    private boolean jj_3R_202() {
        return jj_3R_91();
    }

    private boolean jj_3R_160() {
        return jj_3R_70();
    }

    private boolean jj_3R_159() {
        return jj_3R_250();
    }

    private boolean jj_3R_156() {
        return jj_scan_token(125) || jj_3R_243() || jj_scan_token(126);
    }

    private boolean jj_3R_270() {
        if (jj_3R_63()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_333()) {
            this.jj_scanpos = xsp;
        }
        if (jj_scan_token(25) || jj_3R_63()) {
            return true;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_334()) {
            this.jj_scanpos = xsp2;
            return false;
        }
        return false;
    }

    private boolean jj_3R_334() {
        return jj_scan_token(65) || jj_scan_token(119);
    }

    private boolean jj_3R_333() {
        return jj_scan_token(16);
    }

    private boolean jj_3R_204() {
        return jj_scan_token(16);
    }

    private boolean jj_3R_69() {
        Token xsp;
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_156()) {
            this.jj_scanpos = xsp2;
            if (jj_3R_157()) {
                return true;
            }
        }
        if (jj_3R_158()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_158());
        this.jj_scanpos = xsp;
        Token xsp3 = this.jj_scanpos;
        if (jj_3R_159()) {
            this.jj_scanpos = xsp3;
        }
        Token xsp4 = this.jj_scanpos;
        if (jj_3R_160()) {
            this.jj_scanpos = xsp4;
            return false;
        }
        return false;
    }

    private boolean jj_3_41() {
        return jj_3R_90();
    }

    private boolean jj_3R_87() {
        if (jj_3R_63()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_204()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(69) || jj_3R_63() || jj_scan_token(13) || jj_3R_63();
    }

    private boolean jj_3R_276() {
        if (jj_3R_63()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_337()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3_18() {
        return jj_scan_token(53) || jj_scan_token(6);
    }

    private boolean jj_3_17() {
        return jj_scan_token(53) || jj_scan_token(91) || jj_scan_token(6);
    }

    private boolean jj_3R_275() {
        return jj_3R_90();
    }

    private boolean jj_3R_200() {
        if (jj_3R_63()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_277()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3_58() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_117()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(66) || jj_scan_token(15) || jj_3R_118();
    }

    private boolean jj_3R_380() {
        return jj_scan_token(11) || jj_scan_token(125) || jj_3R_304() || jj_scan_token(126);
    }

    private boolean jj_3R_337() {
        return jj_scan_token(130);
    }

    private boolean jj_3R_277() {
        return jj_scan_token(130);
    }

    private boolean jj_3R_117() {
        return jj_scan_token(83) || jj_scan_token(116);
    }

    private boolean jj_3R_312() {
        return jj_3R_250();
    }

    private boolean jj_3R_201() {
        return jj_scan_token(16);
    }

    private boolean jj_3R_311() {
        return jj_3R_250();
    }

    private boolean jj_3R_308() {
        return jj_3R_351();
    }

    private boolean jj_3_19() {
        return jj_3R_70();
    }

    private boolean jj_3R_310() {
        return jj_3R_353();
    }

    private boolean jj_3R_309() {
        return jj_3R_352();
    }

    private boolean jj_3R_199() {
        if (jj_scan_token(125)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_275()) {
            this.jj_scanpos = xsp;
            if (jj_3R_276()) {
                return true;
            }
        }
        return jj_scan_token(126);
    }

    private boolean jj_3R_307() {
        return jj_3R_350();
    }

    private boolean jj_3R_86() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_199()) {
            this.jj_scanpos = xsp;
            if (jj_3R_200()) {
                return true;
            }
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_201()) {
            this.jj_scanpos = xsp2;
        }
        if (jj_scan_token(9) || jj_scan_token(125)) {
            return true;
        }
        Token xsp3 = this.jj_scanpos;
        if (jj_3R_202()) {
            this.jj_scanpos = xsp3;
            if (jj_3R_203()) {
                return true;
            }
        }
        return jj_scan_token(126);
    }

    private boolean jj_3_40() {
        return jj_3R_89();
    }

    private boolean jj_3R_67() {
        return jj_3R_64();
    }

    private boolean jj_3_39() {
        return jj_3R_88();
    }

    private boolean jj_3R_306() {
        return jj_scan_token(30) || jj_3R_166() || jj_3R_349();
    }

    private boolean jj_3_38() {
        return jj_3R_87();
    }

    private boolean jj_3R_344() {
        if (jj_scan_token(71)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_380()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_305() {
        return jj_3R_348();
    }

    private boolean jj_3_37() {
        return jj_3R_86();
    }

    private boolean jj_3R_179() {
        return jj_3R_270();
    }

    private boolean jj_3R_303() {
        return jj_3R_345();
    }

    private boolean jj_3R_178() {
        return jj_3R_89();
    }

    private boolean jj_3R_177() {
        return jj_3R_88();
    }

    private boolean jj_3R_176() {
        return jj_3R_87();
    }

    private boolean jj_3R_302() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(12)) {
            this.jj_scanpos = xsp;
            return jj_3R_344();
        }
        return false;
    }

    private boolean jj_3R_175() {
        return jj_3R_86();
    }

    private boolean jj_3R_84() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_175()) {
            this.jj_scanpos = xsp;
            if (jj_3R_176()) {
                this.jj_scanpos = xsp;
                if (jj_3R_177()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_178()) {
                        this.jj_scanpos = xsp;
                        return jj_3R_179();
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_243() {
        if (jj_scan_token(58)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_302()) {
            this.jj_scanpos = xsp;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_303()) {
            this.jj_scanpos = xsp2;
        }
        if (jj_3R_304()) {
            return true;
        }
        Token xsp3 = this.jj_scanpos;
        if (jj_3R_305()) {
            this.jj_scanpos = xsp3;
        }
        Token xsp4 = this.jj_scanpos;
        if (jj_3R_306()) {
            this.jj_scanpos = xsp4;
        }
        Token xsp5 = this.jj_scanpos;
        if (jj_3R_307()) {
            this.jj_scanpos = xsp5;
        }
        Token xsp6 = this.jj_scanpos;
        if (jj_3R_308()) {
            this.jj_scanpos = xsp6;
        }
        Token xsp7 = this.jj_scanpos;
        if (jj_3R_309()) {
            this.jj_scanpos = xsp7;
        }
        Token xsp8 = this.jj_scanpos;
        if (jj_3R_310()) {
            this.jj_scanpos = xsp8;
        }
        Token xsp9 = this.jj_scanpos;
        if (jj_3R_311()) {
            this.jj_scanpos = xsp9;
        }
        Token xsp10 = this.jj_scanpos;
        if (jj_3R_312()) {
            this.jj_scanpos = xsp10;
        }
        Token xsp11 = this.jj_scanpos;
        if (jj_3_19()) {
            this.jj_scanpos = xsp11;
            return false;
        }
        return false;
    }

    private boolean jj_3R_287() {
        return jj_scan_token(127);
    }

    private boolean jj_3_16() {
        return jj_3R_69();
    }

    private boolean jj_3R_68() {
        return jj_3R_64();
    }

    private boolean jj_3R_274() {
        return jj_scan_token(106);
    }

    private boolean jj_3R_221() {
        return jj_3R_69();
    }

    private boolean jj_3R_222() {
        return jj_3R_243();
    }

    private boolean jj_3R_66() {
        return jj_3R_64();
    }

    private boolean jj_3R_198() {
        return jj_scan_token(130);
    }

    private boolean jj_3R_197() {
        return jj_scan_token(89);
    }

    private boolean jj_3R_120() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_221()) {
            this.jj_scanpos = xsp;
            return jj_3R_222();
        }
        return false;
    }

    private boolean jj_3R_196() {
        return jj_scan_token(141);
    }

    private boolean jj_3R_195() {
        return jj_scan_token(140);
    }

    private boolean jj_3R_194() {
        return jj_scan_token(139);
    }

    private boolean jj_3R_193() {
        if (jj_scan_token(107)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_274()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_192() {
        return jj_scan_token(138);
    }

    private boolean jj_3R_123() {
        return jj_3R_223();
    }

    private boolean jj_3R_191() {
        return jj_scan_token(137);
    }

    private boolean jj_3R_189() {
        return jj_scan_token(135);
    }

    private boolean jj_3R_188() {
        return jj_scan_token(134);
    }

    private boolean jj_3R_122() {
        return jj_scan_token(108);
    }

    private boolean jj_3R_301() {
        return jj_3R_343();
    }

    private boolean jj_3R_187() {
        return jj_scan_token(133);
    }

    private boolean jj_3R_56() {
        Token xsp;
        if (jj_scan_token(57)) {
            return true;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_122()) {
            this.jj_scanpos = xsp2;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_123());
        this.jj_scanpos = xsp;
        return jj_scan_token(39);
    }

    private boolean jj_3R_186() {
        return jj_scan_token(124);
    }

    private boolean jj_3R_190() {
        return jj_scan_token(136);
    }

    private boolean jj_3R_224() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_301()) {
            this.jj_scanpos = xsp;
        }
        return jj_3R_120();
    }

    private boolean jj_3R_185() {
        return jj_scan_token(132);
    }

    private boolean jj_3R_184() {
        return jj_scan_token(131);
    }

    private boolean jj_3R_183() {
        return jj_scan_token(130);
    }

    private boolean jj_3R_181() {
        return jj_scan_token(16);
    }

    private boolean jj_3R_180() {
        return jj_scan_token(89);
    }

    private boolean jj_3R_286() {
        return jj_3R_90();
    }

    private boolean jj_3R_65() {
        return jj_3R_64();
    }

    private boolean jj_3R_85() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_180()) {
            this.jj_scanpos = xsp;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_181()) {
            this.jj_scanpos = xsp2;
        }
        if (jj_3R_182()) {
            return true;
        }
        Token xsp3 = this.jj_scanpos;
        if (jj_3R_183()) {
            this.jj_scanpos = xsp3;
        }
        Token xsp4 = this.jj_scanpos;
        if (jj_3R_184()) {
            this.jj_scanpos = xsp4;
            if (jj_3R_185()) {
                this.jj_scanpos = xsp4;
                if (jj_3R_186()) {
                    this.jj_scanpos = xsp4;
                    if (jj_3R_187()) {
                        this.jj_scanpos = xsp4;
                        if (jj_3R_188()) {
                            this.jj_scanpos = xsp4;
                            if (jj_3R_189()) {
                                this.jj_scanpos = xsp4;
                                if (jj_3R_190()) {
                                    this.jj_scanpos = xsp4;
                                    if (jj_3R_191()) {
                                        this.jj_scanpos = xsp4;
                                        if (jj_3R_192()) {
                                            this.jj_scanpos = xsp4;
                                            if (jj_3R_193()) {
                                                this.jj_scanpos = xsp4;
                                                if (jj_3R_194()) {
                                                    this.jj_scanpos = xsp4;
                                                    if (jj_3R_195()) {
                                                        this.jj_scanpos = xsp4;
                                                        if (jj_3R_196()) {
                                                            return true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (jj_3R_182()) {
            return true;
        }
        Token xsp5 = this.jj_scanpos;
        if (jj_3R_197()) {
            this.jj_scanpos = xsp5;
        }
        Token xsp6 = this.jj_scanpos;
        if (jj_3R_198()) {
            this.jj_scanpos = xsp6;
            return false;
        }
        return false;
    }

    private boolean jj_3_36() {
        return jj_3R_85();
    }

    private boolean jj_3_35() {
        return jj_3R_84();
    }

    private boolean jj_3R_257() {
        return jj_3R_64();
    }

    private boolean jj_3_15() {
        return jj_3R_64() || jj_scan_token(128) || jj_3R_64();
    }

    private boolean jj_3R_174() {
        return jj_3R_110();
    }

    private boolean jj_3_14() {
        if (jj_3R_64() || jj_scan_token(128)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_68()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(128) || jj_3R_64();
    }

    private boolean jj_3R_173() {
        return jj_3R_85();
    }

    private boolean jj_3_13() {
        if (jj_3R_64() || jj_scan_token(128)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_66()) {
            this.jj_scanpos = xsp;
        }
        if (jj_scan_token(128)) {
            return true;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_67()) {
            this.jj_scanpos = xsp2;
        }
        return jj_scan_token(128) || jj_3R_64();
    }

    private boolean jj_3R_172() {
        return jj_3R_84();
    }

    private boolean jj_3R_83() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_172()) {
            this.jj_scanpos = xsp;
            if (jj_3R_173()) {
                this.jj_scanpos = xsp;
                return jj_3R_174();
            }
            return false;
        }
        return false;
    }

    private boolean jj_3_34() {
        return jj_3R_83();
    }

    private boolean jj_3R_370() {
        return jj_scan_token(16);
    }

    private boolean jj_3R_163() {
        Token xsp = this.jj_scanpos;
        if (jj_3_13()) {
            this.jj_scanpos = xsp;
            if (jj_3_14()) {
                this.jj_scanpos = xsp;
                if (jj_3_15()) {
                    this.jj_scanpos = xsp;
                    return jj_3R_257();
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_332() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_370()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(125) || jj_3R_82() || jj_scan_token(126);
    }

    private boolean jj_3_33() {
        return jj_scan_token(13);
    }

    private boolean jj_3R_79() {
        return jj_scan_token(129);
    }

    private boolean jj_3R_331() {
        return jj_3R_83();
    }

    private boolean jj_3R_64() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(116)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(120)) {
                this.jj_scanpos = xsp;
                if (jj_scan_token(73)) {
                    this.jj_scanpos = xsp;
                    if (jj_scan_token(7)) {
                        this.jj_scanpos = xsp;
                        if (jj_scan_token(78)) {
                            this.jj_scanpos = xsp;
                            if (jj_scan_token(96)) {
                                this.jj_scanpos = xsp;
                                if (jj_scan_token(102)) {
                                    this.jj_scanpos = xsp;
                                    if (jj_scan_token(97)) {
                                        this.jj_scanpos = xsp;
                                        if (jj_scan_token(80)) {
                                            this.jj_scanpos = xsp;
                                            if (jj_scan_token(95)) {
                                                this.jj_scanpos = xsp;
                                                if (jj_scan_token(77)) {
                                                    this.jj_scanpos = xsp;
                                                    if (jj_scan_token(99)) {
                                                        this.jj_scanpos = xsp;
                                                        if (jj_scan_token(104)) {
                                                            this.jj_scanpos = xsp;
                                                            if (jj_scan_token(98)) {
                                                                this.jj_scanpos = xsp;
                                                                if (jj_scan_token(91)) {
                                                                    this.jj_scanpos = xsp;
                                                                    if (jj_scan_token(55)) {
                                                                        this.jj_scanpos = xsp;
                                                                        if (jj_scan_token(44)) {
                                                                            this.jj_scanpos = xsp;
                                                                            if (jj_scan_token(94)) {
                                                                                this.jj_scanpos = xsp;
                                                                                if (jj_scan_token(68)) {
                                                                                    this.jj_scanpos = xsp;
                                                                                    return jj_scan_token(70);
                                                                                }
                                                                                return false;
                                                                            }
                                                                            return false;
                                                                        }
                                                                        return false;
                                                                    }
                                                                    return false;
                                                                }
                                                                return false;
                                                            }
                                                            return false;
                                                        }
                                                        return false;
                                                    }
                                                    return false;
                                                }
                                                return false;
                                            }
                                            return false;
                                        }
                                        return false;
                                    }
                                    return false;
                                }
                                return false;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_269() {
        if (jj_scan_token(13)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_331()) {
            this.jj_scanpos = xsp;
            return jj_3R_332();
        }
        return false;
    }

    private boolean jj_3R_341() {
        return jj_scan_token(12);
    }

    private boolean jj_3R_284() {
        return jj_scan_token(128) || jj_3R_64();
    }

    private boolean jj_3_32() {
        return jj_3R_83();
    }

    private boolean jj_3R_330() {
        return jj_scan_token(16);
    }

    private boolean jj_3R_135() {
        return jj_3R_64();
    }

    private boolean jj_3_12() {
        return jj_3R_64() || jj_scan_token(128) || jj_3R_64();
    }

    private boolean jj_3R_268() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_330()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(125) || jj_3R_82() || jj_scan_token(126);
    }

    private boolean jj_3_11() {
        return jj_3R_64() || jj_scan_token(128) || jj_3R_64() || jj_scan_token(128) || jj_3R_64();
    }

    private boolean jj_3_10() {
        if (jj_3R_64() || jj_scan_token(128)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_65()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(128) || jj_3R_64() || jj_scan_token(128) || jj_3R_64();
    }

    private boolean jj_3R_252() {
        return jj_scan_token(129);
    }

    private boolean jj_3R_267() {
        return jj_3R_83();
    }

    private boolean jj_3R_170() {
        Token xsp;
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_267()) {
            this.jj_scanpos = xsp2;
            if (jj_3R_268()) {
                return true;
            }
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_269());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_121() {
        return jj_3R_223();
    }

    private boolean jj_3R_59() {
        Token xsp = this.jj_scanpos;
        if (jj_3_10()) {
            this.jj_scanpos = xsp;
            if (jj_3_11()) {
                this.jj_scanpos = xsp;
                if (jj_3_12()) {
                    this.jj_scanpos = xsp;
                    return jj_3R_135();
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_359() {
        return jj_scan_token(129);
    }

    private boolean jj_3_31() {
        return jj_scan_token(10);
    }

    private boolean jj_3R_55() {
        Token xsp;
        if (jj_scan_token(57)) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_121());
        this.jj_scanpos = xsp;
        return jj_scan_token(49) || jj_3R_64();
    }

    private boolean jj_3R_171() {
        return jj_scan_token(10) || jj_3R_170();
    }

    private boolean jj_3R_227() {
        return jj_scan_token(56);
    }

    private boolean jj_3R_82() {
        Token xsp;
        if (jj_3R_170()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_171());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_397() {
        return jj_scan_token(125) || jj_3R_368() || jj_scan_token(126);
    }

    private boolean jj_3R_91() {
        return jj_3R_120();
    }

    private boolean jj_3_30() {
        return jj_3R_82();
    }

    private boolean jj_3R_340() {
        return jj_scan_token(71);
    }

    private boolean jj_3R_285() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_340()) {
            this.jj_scanpos = xsp;
            return jj_3R_341();
        }
        return false;
    }

    private boolean jj_3R_219() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_285()) {
            this.jj_scanpos = xsp;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_286()) {
            this.jj_scanpos = xsp2;
            return jj_3R_287();
        }
        return false;
    }

    private boolean jj_3_8() {
        return jj_scan_token(125);
    }

    private boolean jj_3R_396() {
        return jj_3R_82();
    }

    private boolean jj_3R_62() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(64)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(55);
        }
        return false;
    }

    private boolean jj_3R_368() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_396()) {
            this.jj_scanpos = xsp;
            return jj_3R_397();
        }
        return false;
    }

    private boolean jj_3R_218() {
        if (jj_scan_token(128) || jj_3R_64()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_284()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_78() {
        return jj_scan_token(112);
    }

    private boolean jj_3R_217() {
        return jj_scan_token(157);
    }

    private boolean jj_3R_110() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_217()) {
            this.jj_scanpos = xsp;
        }
        if (jj_3R_64()) {
            return true;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_218()) {
            this.jj_scanpos = xsp2;
        }
        if (jj_scan_token(125)) {
            return true;
        }
        Token xsp3 = this.jj_scanpos;
        if (jj_3R_219()) {
            this.jj_scanpos = xsp3;
        }
        if (jj_scan_token(126)) {
            return true;
        }
        Token xsp4 = this.jj_scanpos;
        if (jj_scan_token(151)) {
            this.jj_scanpos = xsp4;
            return false;
        }
        return false;
    }

    private boolean jj_3R_404() {
        return jj_scan_token(129);
    }

    private boolean jj_3R_384() {
        return jj_scan_token(20);
    }

    private boolean jj_3R_403() {
        return jj_scan_token(112);
    }

    private boolean jj_3R_256() {
        return jj_scan_token(24);
    }

    private boolean jj_3R_382() {
        return jj_scan_token(129);
    }

    private boolean jj_3R_255() {
        return jj_scan_token(12);
    }

    private boolean jj_3R_381() {
        return jj_scan_token(112);
    }

    private boolean jj_3_28() {
        if (jj_scan_token(59)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_78()) {
            this.jj_scanpos = xsp;
            return jj_3R_79();
        }
        return false;
    }

    private boolean jj_3R_383() {
        if (jj_scan_token(125)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_403()) {
            this.jj_scanpos = xsp;
            if (jj_3R_404()) {
                return true;
            }
        }
        return jj_scan_token(126);
    }

    private boolean jj_3_9() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_62()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(125) || jj_3R_63();
    }

    private boolean jj_3R_254() {
        return jj_scan_token(129);
    }

    private boolean jj_3R_253() {
        return jj_scan_token(112);
    }

    private boolean jj_3R_345() {
        if (jj_scan_token(19)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_381()) {
            this.jj_scanpos = xsp;
            if (jj_3R_382()) {
                this.jj_scanpos = xsp;
                if (jj_3R_383()) {
                    return true;
                }
            }
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_384()) {
            this.jj_scanpos = xsp2;
            return false;
        }
        return false;
    }

    private boolean jj_3R_81() {
        return jj_scan_token(129);
    }

    private boolean jj_3_7() {
        return jj_scan_token(125) || jj_3R_59();
    }

    private boolean jj_3R_80() {
        return jj_scan_token(112);
    }

    private boolean jj_3R_162() {
        if (jj_scan_token(51)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_253()) {
            this.jj_scanpos = xsp;
            if (jj_3R_254()) {
                this.jj_scanpos = xsp;
                if (jj_3R_255()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_256()) {
                        return true;
                    }
                }
            }
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3_28()) {
            this.jj_scanpos = xsp2;
            return false;
        }
        return false;
    }

    private boolean jj_3R_251() {
        return jj_scan_token(112);
    }

    private boolean jj_3R_232() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(109)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(110);
        }
        return false;
    }

    private boolean jj_3R_226() {
        return jj_scan_token(62);
    }

    private boolean jj_3R_358() {
        return jj_scan_token(112);
    }

    private boolean jj_3R_60() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(64)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(55);
        }
        return false;
    }

    private boolean jj_3R_161() {
        if (jj_scan_token(59)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_251()) {
            this.jj_scanpos = xsp;
            return jj_3R_252();
        }
        return false;
    }

    private boolean jj_3R_414() {
        return jj_scan_token(33) || jj_3R_61() || jj_scan_token(34) || jj_3R_63();
    }

    private boolean jj_3_6() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_60()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(125) || jj_3R_61();
    }

    private boolean jj_3R_413() {
        return jj_scan_token(33) || jj_3R_368() || jj_scan_token(34) || jj_3R_63();
    }

    private boolean jj_3_29() {
        if (jj_scan_token(51)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_80()) {
            this.jj_scanpos = xsp;
            if (jj_3R_81()) {
                return true;
            }
        }
        if (jj_scan_token(123)) {
            return true;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_358()) {
            this.jj_scanpos = xsp2;
            return jj_3R_359();
        }
        return false;
    }

    private boolean jj_3_5() {
        return jj_scan_token(125) || jj_3R_59();
    }

    private boolean jj_3R_372() {
        Token xsp;
        if (jj_3R_61()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_400());
        this.jj_scanpos = xsp;
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_401()) {
            this.jj_scanpos = xsp2;
            return false;
        }
        return false;
    }

    private boolean jj_3R_426() {
        return jj_scan_token(102);
    }

    private boolean jj_3R_401() {
        return jj_scan_token(35) || jj_3R_63();
    }

    private boolean jj_3R_400() {
        return jj_3R_414();
    }

    private boolean jj_3R_399() {
        return jj_scan_token(35) || jj_3R_63();
    }

    private boolean jj_3R_398() {
        return jj_3R_413();
    }

    private boolean jj_3R_70() {
        Token xsp = this.jj_scanpos;
        if (jj_3_29()) {
            this.jj_scanpos = xsp;
            if (jj_3R_161()) {
                this.jj_scanpos = xsp;
                return jj_3R_162();
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_371() {
        Token xsp;
        if (jj_3R_398()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_398());
        this.jj_scanpos = xsp;
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_399()) {
            this.jj_scanpos = xsp2;
            return false;
        }
        return false;
    }

    private boolean jj_3R_424() {
        return jj_scan_token(102);
    }

    private boolean jj_3R_233() {
        if (jj_scan_token(32)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_371()) {
            this.jj_scanpos = xsp;
            if (jj_3R_372()) {
                return true;
            }
        }
        return jj_scan_token(21);
    }

    private boolean jj_3R_393() {
        return jj_scan_token(22);
    }

    private boolean jj_3R_282() {
        return jj_scan_token(127);
    }

    private boolean jj_3R_406() {
        return jj_scan_token(97);
    }

    private boolean jj_3R_405() {
        return jj_scan_token(96);
    }

    private boolean jj_3R_394() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_405()) {
            this.jj_scanpos = xsp;
            return jj_3R_406();
        }
        return false;
    }

    private boolean jj_3R_314() {
        if (jj_3R_63()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_356()) {
            this.jj_scanpos = xsp;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_357()) {
            this.jj_scanpos = xsp2;
            return false;
        }
        return false;
    }

    private boolean jj_3R_357() {
        if (jj_scan_token(95)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_394()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_356() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(18)) {
            this.jj_scanpos = xsp;
            return jj_3R_393();
        }
        return false;
    }

    private boolean jj_3R_228() {
        return jj_scan_token(68);
    }

    private boolean jj_3R_114() {
        return jj_scan_token(73) || jj_scan_token(125) || jj_3R_63() || jj_scan_token(5) || jj_3R_220() || jj_scan_token(126);
    }

    private boolean jj_3R_375() {
        return jj_scan_token(99);
    }

    private boolean jj_3R_315() {
        return jj_scan_token(123) || jj_3R_314();
    }

    private boolean jj_3R_250() {
        Token xsp;
        if (jj_scan_token(53)) {
            return true;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_scan_token(91)) {
            this.jj_scanpos = xsp2;
        }
        if (jj_scan_token(6) || jj_3R_314()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_315());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_106() {
        return jj_scan_token(78) || jj_scan_token(125) || jj_scan_token(116) || jj_scan_token(30) || jj_3R_63() || jj_scan_token(126);
    }

    private boolean jj_3R_391() {
        return jj_scan_token(90);
    }

    private boolean jj_3R_353() {
        return jj_scan_token(61) || jj_3R_368();
    }

    private boolean jj_3R_373() {
        return jj_scan_token(123) || jj_3R_63();
    }

    private boolean jj_3R_225() {
        return jj_scan_token(63);
    }

    private boolean jj_3R_423() {
        return jj_scan_token(101);
    }

    private boolean jj_3R_425() {
        return jj_scan_token(101);
    }

    private boolean jj_3R_392() {
        return jj_scan_token(123) || jj_3R_63();
    }

    private boolean jj_3R_367() {
        return jj_scan_token(52);
    }

    private boolean jj_3R_352() {
        Token xsp;
        if (jj_scan_token(47) || jj_scan_token(6) || jj_3R_63()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_392());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_417() {
        if (jj_3R_63()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_425()) {
            this.jj_scanpos = xsp;
            return jj_3R_426();
        }
        return false;
    }

    private boolean jj_3R_390() {
        return jj_scan_token(87) || jj_scan_token(38) || jj_3R_170();
    }

    private boolean jj_3R_416() {
        return jj_scan_token(103) || jj_scan_token(104);
    }

    private boolean jj_3R_351() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_390()) {
            this.jj_scanpos = xsp;
        }
        if (jj_scan_token(88) || jj_scan_token(6)) {
            return true;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_391()) {
            this.jj_scanpos = xsp2;
        }
        return jj_3R_170();
    }

    private boolean jj_3R_369() {
        return jj_scan_token(123) || jj_3R_59();
    }

    private boolean jj_3R_415() {
        if (jj_scan_token(100)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_423()) {
            this.jj_scanpos = xsp;
            return jj_3R_424();
        }
        return false;
    }

    private boolean jj_3R_402() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_415()) {
            this.jj_scanpos = xsp;
            if (jj_3R_416()) {
                this.jj_scanpos = xsp;
                return jj_3R_417();
            }
            return false;
        }
        return false;
    }

    private boolean jj_3_4() {
        return jj_scan_token(122) || jj_3R_58();
    }

    private boolean jj_3R_338() {
        if (jj_scan_token(123) || jj_3R_63()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_373()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_350() {
        return jj_scan_token(41) || jj_3R_368();
    }

    private boolean jj_3R_262() {
        return jj_scan_token(123);
    }

    private boolean jj_3R_377() {
        return jj_3R_402();
    }

    private boolean jj_3R_327() {
        Token xsp;
        if (jj_scan_token(45) || jj_scan_token(125) || jj_3R_59()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_369());
        this.jj_scanpos = xsp;
        return jj_scan_token(126);
    }

    private boolean jj_3R_283() {
        return jj_3R_339();
    }

    private boolean jj_3R_134() {
        return jj_3R_232();
    }

    private boolean jj_3R_326() {
        return jj_scan_token(11) || jj_3R_368();
    }

    private boolean jj_3R_133() {
        return jj_3R_231();
    }

    private boolean jj_3R_263() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_326()) {
            this.jj_scanpos = xsp;
            return jj_3R_327();
        }
        return false;
    }

    private boolean jj_3_3() {
        return jj_3R_57();
    }

    private boolean jj_3R_376() {
        return jj_scan_token(69) || jj_3R_402() || jj_scan_token(13) || jj_3R_402();
    }

    private boolean jj_3R_325() {
        return jj_scan_token(29);
    }

    private boolean jj_3R_132() {
        return jj_3R_230();
    }

    private boolean jj_3R_374() {
        return jj_scan_token(98);
    }

    private boolean jj_3_2() {
        return jj_3R_56();
    }

    private boolean jj_3R_366() {
        return jj_scan_token(37);
    }

    private boolean jj_3R_339() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_374()) {
            this.jj_scanpos = xsp;
            if (jj_3R_375()) {
                return true;
            }
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_376()) {
            this.jj_scanpos = xsp2;
            return jj_3R_377();
        }
        return false;
    }

    private boolean jj_3R_365() {
        return jj_scan_token(54);
    }

    private boolean jj_3R_131() {
        return jj_3R_229();
    }

    private boolean jj_3R_324() {
        return jj_scan_token(67);
    }

    private boolean jj_3_1() {
        return jj_3R_55();
    }

    private boolean jj_3R_323() {
        return jj_scan_token(50);
    }

    private boolean jj_3R_364() {
        return jj_scan_token(28);
    }

    private boolean jj_3R_130() {
        return jj_3R_228();
    }

    private boolean jj_3R_322() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_364()) {
            this.jj_scanpos = xsp;
            if (jj_3R_365()) {
                this.jj_scanpos = xsp;
                if (jj_3R_366()) {
                    return true;
                }
            }
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_367()) {
            this.jj_scanpos = xsp2;
            return false;
        }
        return false;
    }

    private boolean jj_3R_261() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_322()) {
            this.jj_scanpos = xsp;
            if (jj_3R_323()) {
                this.jj_scanpos = xsp;
                if (jj_3R_324()) {
                    this.jj_scanpos = xsp;
                    return jj_3R_325();
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_213() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_281()) {
            this.jj_scanpos = xsp;
            return jj_3R_282();
        }
        return false;
    }

    private boolean jj_3R_281() {
        if (jj_3R_63()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_338()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_129() {
        return jj_3R_227();
    }

    private boolean jj_3R_167() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_261()) {
            this.jj_scanpos = xsp;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_scan_token(27)) {
            this.jj_scanpos = xsp2;
            if (jj_3R_262()) {
                return true;
            }
        }
        if (jj_3R_166()) {
            return true;
        }
        Token xsp3 = this.jj_scanpos;
        if (jj_3R_263()) {
            this.jj_scanpos = xsp3;
            return false;
        }
        return false;
    }

    private boolean jj_3R_128() {
        return jj_3R_226();
    }

    private boolean jj_3R_58() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_126()) {
            this.jj_scanpos = xsp;
            if (jj_3R_127()) {
                this.jj_scanpos = xsp;
                if (jj_3R_128()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_129()) {
                        this.jj_scanpos = xsp;
                        if (jj_3R_130()) {
                            this.jj_scanpos = xsp;
                            if (jj_3R_131()) {
                                this.jj_scanpos = xsp;
                                if (jj_3_1()) {
                                    this.jj_scanpos = xsp;
                                    if (jj_3_2()) {
                                        this.jj_scanpos = xsp;
                                        if (jj_3_3()) {
                                            this.jj_scanpos = xsp;
                                            if (jj_3R_132()) {
                                                this.jj_scanpos = xsp;
                                                if (jj_3R_133()) {
                                                    this.jj_scanpos = xsp;
                                                    return jj_3R_134();
                                                }
                                                return false;
                                            }
                                            return false;
                                        }
                                        return false;
                                    }
                                    return false;
                                }
                                return false;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_127() {
        return jj_3R_225();
    }

    private boolean jj_3R_126() {
        return jj_3R_224();
    }

    private boolean jj_3R_215() {
        if (jj_3R_250()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_283()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_214() {
        return jj_scan_token(77) || jj_scan_token(6) || jj_3R_90();
    }

    private boolean jj_3R_105() {
        if (jj_scan_token(116) || jj_scan_token(125)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_213()) {
            this.jj_scanpos = xsp;
        }
        if (jj_scan_token(126) || jj_scan_token(76) || jj_scan_token(125)) {
            return true;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_214()) {
            this.jj_scanpos = xsp2;
        }
        Token xsp3 = this.jj_scanpos;
        if (jj_3R_215()) {
            this.jj_scanpos = xsp3;
        }
        return jj_scan_token(126);
    }

    private boolean jj_3R_216() {
        return jj_scan_token(156) || jj_scan_token(119);
    }

    private boolean jj_3R_242() {
        return jj_scan_token(81) || jj_scan_token(119);
    }

    private boolean jj_3R_389() {
        return jj_3R_167();
    }

    private boolean jj_3R_349() {
        Token xsp;
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_389());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_108() {
        Token xsp;
        if (jj_3R_59() || jj_3R_216()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_216());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_241() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_148() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_241()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(125) || jj_3R_91() || jj_scan_token(126);
    }

    private boolean jj_3R_116() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_240() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_239() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_234() {
        return jj_scan_token(155) || jj_scan_token(116);
    }

    private boolean jj_3R_235() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_238() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_74() {
        return jj_3R_166() || jj_3R_167();
    }

    private boolean jj_3R_237() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_236() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_329() {
        return jj_scan_token(123) || jj_3R_64();
    }

    private boolean jj_3R_362() {
        return jj_scan_token(79) || jj_scan_token(125) || jj_3R_91() || jj_scan_token(126);
    }

    private boolean jj_3R_328() {
        return jj_scan_token(123) || jj_3R_63();
    }

    private boolean jj_3R_153() {
        return jj_3R_242();
    }

    private boolean jj_3R_152() {
        return jj_scan_token(153) || jj_scan_token(119) || jj_scan_token(151);
    }

    private boolean jj_3R_363() {
        return jj_3R_395();
    }

    private boolean jj_3R_266() {
        Token xsp;
        if (jj_scan_token(125) || jj_3R_64()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_329());
        this.jj_scanpos = xsp;
        return jj_scan_token(126);
    }

    private boolean jj_3R_115() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_151() {
        return jj_scan_token(152) || jj_scan_token(119) || jj_scan_token(151);
    }

    private boolean jj_3_56() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_115()) {
            this.jj_scanpos = xsp;
        }
        return jj_3R_59();
    }

    private boolean jj_3R_280() {
        return jj_scan_token(154) || jj_3R_220();
    }

    private boolean jj_3R_113() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_150() {
        return jj_scan_token(150) || jj_scan_token(119) || jj_scan_token(151);
    }

    private boolean jj_3_55() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_113()) {
            this.jj_scanpos = xsp;
        }
        return jj_3R_114();
    }

    private boolean jj_3R_112() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_264() {
        Token xsp;
        if (jj_scan_token(123) || jj_scan_token(125) || jj_3R_63()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_328());
        this.jj_scanpos = xsp;
        return jj_scan_token(126);
    }

    private boolean jj_3_54() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_112()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(112);
    }

    private boolean jj_3R_149() {
        return jj_scan_token(119);
    }

    private boolean jj_3R_111() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3R_77() {
        return jj_scan_token(123) || jj_3R_63();
    }

    private boolean jj_3R_265() {
        return jj_scan_token(123) || jj_3R_63();
    }

    private boolean jj_3_53() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_111()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(111);
    }

    private boolean jj_3R_109() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3_24() {
        return jj_3R_74();
    }

    private boolean jj_3_52() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_109()) {
            this.jj_scanpos = xsp;
        }
        return jj_3R_110();
    }

    private boolean jj_3_57() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_116()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(125) || jj_3R_61() || jj_scan_token(126);
    }

    private boolean jj_3R_169() {
        if (jj_3R_258()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_266()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_147() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_240()) {
            this.jj_scanpos = xsp;
        }
        return jj_3R_59();
    }

    private boolean jj_3_50() {
        return jj_3R_106();
    }

    private boolean jj_3R_146() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_239()) {
            this.jj_scanpos = xsp;
        }
        return jj_3R_114();
    }

    private boolean jj_3R_107() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(145)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(146);
        }
        return false;
    }

    private boolean jj_3_49() {
        return jj_3R_105();
    }

    private boolean jj_3_51() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_107()) {
            this.jj_scanpos = xsp;
        }
        return jj_3R_108();
    }

    private boolean jj_3R_145() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_238()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(112);
    }

    private boolean jj_3R_361() {
        return jj_3R_91();
    }

    private boolean jj_3R_168() {
        Token xsp;
        if (jj_3R_63()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_265());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_144() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_237()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(111);
    }

    private boolean jj_3R_360() {
        return jj_3R_74();
    }

    private boolean jj_3R_143() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_236()) {
            this.jj_scanpos = xsp;
        }
        return jj_3R_110();
    }

    private boolean jj_3R_141() {
        return jj_3R_106();
    }

    private boolean jj_3_27() {
        Token xsp;
        Token xsp2;
        if (jj_scan_token(125) || jj_3R_63()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_77());
        this.jj_scanpos = xsp;
        if (jj_scan_token(126)) {
            return true;
        }
        do {
            xsp2 = this.jj_scanpos;
        } while (!jj_3R_264());
        this.jj_scanpos = xsp2;
        return false;
    }

    private boolean jj_3R_319() {
        return jj_3R_362();
    }

    private boolean jj_3R_140() {
        return jj_3R_105();
    }

    private boolean jj_3R_142() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_235()) {
            this.jj_scanpos = xsp;
        }
        return jj_3R_108();
    }

    private boolean jj_3R_318() {
        return jj_3R_163();
    }

    private boolean jj_3R_76() {
        if (jj_scan_token(125) || jj_scan_token(64)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3_27()) {
            this.jj_scanpos = xsp;
            if (jj_3R_168()) {
                return true;
            }
        }
        if (jj_scan_token(126)) {
            return true;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_169()) {
            this.jj_scanpos = xsp2;
            return false;
        }
        return false;
    }

    private boolean jj_3R_321() {
        return jj_3R_258();
    }

    private boolean jj_3_25() {
        return jj_3R_75();
    }

    private boolean jj_3R_138() {
        return jj_scan_token(129);
    }

    private boolean jj_3R_320() {
        Token xsp = this.jj_scanpos;
        if (jj_3_25()) {
            this.jj_scanpos = xsp;
            return jj_3R_363();
        }
        return false;
    }

    private boolean jj_3R_137() {
        return jj_3R_233();
    }

    private boolean jj_3R_102() {
        return jj_scan_token(148);
    }

    private boolean jj_3R_139() {
        return jj_3R_234();
    }

    private boolean jj_3R_101() {
        return jj_scan_token(147);
    }

    private boolean jj_3R_136() {
        return jj_scan_token(24);
    }

    private boolean jj_3R_388() {
        return jj_scan_token(123) || jj_3R_163();
    }

    private boolean jj_3R_317() {
        if (jj_scan_token(125)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_360()) {
            this.jj_scanpos = xsp;
            if (jj_3R_361()) {
                return true;
            }
        }
        return jj_scan_token(126);
    }

    private boolean jj_3R_104() {
        return jj_scan_token(125) || jj_3R_95() || jj_scan_token(126);
    }

    private boolean jj_3R_61() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_136()) {
            this.jj_scanpos = xsp;
            if (jj_3R_137()) {
                this.jj_scanpos = xsp;
                if (jj_3R_138()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_139()) {
                        this.jj_scanpos = xsp;
                        if (jj_3R_140()) {
                            this.jj_scanpos = xsp;
                            if (jj_3R_141()) {
                                this.jj_scanpos = xsp;
                                if (jj_3R_142()) {
                                    this.jj_scanpos = xsp;
                                    if (jj_3R_143()) {
                                        this.jj_scanpos = xsp;
                                        if (jj_3R_144()) {
                                            this.jj_scanpos = xsp;
                                            if (jj_3R_145()) {
                                                this.jj_scanpos = xsp;
                                                if (jj_3R_146()) {
                                                    this.jj_scanpos = xsp;
                                                    if (jj_3R_147()) {
                                                        this.jj_scanpos = xsp;
                                                        if (jj_3_57()) {
                                                            this.jj_scanpos = xsp;
                                                            if (jj_3R_148()) {
                                                                this.jj_scanpos = xsp;
                                                                if (jj_3R_149()) {
                                                                    this.jj_scanpos = xsp;
                                                                    if (jj_3R_150()) {
                                                                        this.jj_scanpos = xsp;
                                                                        if (jj_3R_151()) {
                                                                            this.jj_scanpos = xsp;
                                                                            if (jj_3R_152()) {
                                                                                this.jj_scanpos = xsp;
                                                                                if (jj_3R_153()) {
                                                                                    return true;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_280()) {
            this.jj_scanpos = xsp2;
            return false;
        }
        return false;
    }

    private boolean jj_3_26() {
        return jj_3R_76();
    }

    private boolean jj_3_48() {
        return jj_3R_99();
    }

    private boolean jj_3R_260() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_317()) {
            this.jj_scanpos = xsp;
            if (jj_3R_318()) {
                this.jj_scanpos = xsp;
                if (jj_3R_319()) {
                    return true;
                }
            }
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_320()) {
            this.jj_scanpos = xsp2;
        }
        Token xsp3 = this.jj_scanpos;
        if (jj_3R_321()) {
            this.jj_scanpos = xsp3;
            return false;
        }
        return false;
    }

    private boolean jj_3R_259() {
        return jj_3R_76();
    }

    private boolean jj_3R_212() {
        return jj_scan_token(149) || jj_3R_61();
    }

    private boolean jj_3R_103() {
        return jj_3R_99();
    }

    private boolean jj_3R_97() {
        return jj_scan_token(146);
    }

    private boolean jj_3R_99() {
        Token xsp;
        if (jj_3R_61()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_212());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_166() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_259()) {
            this.jj_scanpos = xsp;
            return jj_3R_260();
        }
        return false;
    }

    private boolean jj_3R_100() {
        return jj_scan_token(127);
    }

    private boolean jj_3R_348() {
        Token xsp;
        if (jj_scan_token(23) || jj_3R_163()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_388());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3_46() {
        return jj_3R_99();
    }

    private boolean jj_3_47() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_100()) {
            this.jj_scanpos = xsp;
            if (jj_3R_101()) {
                this.jj_scanpos = xsp;
                if (jj_3R_102()) {
                    return true;
                }
            }
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_103()) {
            this.jj_scanpos = xsp2;
            return jj_3R_104();
        }
        return false;
    }

    private boolean jj_3R_211() {
        return jj_scan_token(125) || jj_3R_95() || jj_scan_token(126);
    }

    private boolean jj_3R_411() {
        return jj_3R_422();
    }

    private boolean jj_3_23() {
        return jj_3R_73();
    }

    private boolean jj_3R_410() {
        return jj_3R_120();
    }

    private boolean jj_3R_210() {
        return jj_3R_99();
    }

    private boolean jj_3R_409() {
        return jj_scan_token(14);
    }

    private boolean jj_3R_98() {
        Token xsp;
        Token xsp2 = this.jj_scanpos;
        if (jj_3R_210()) {
            this.jj_scanpos = xsp2;
            if (jj_3R_211()) {
                return true;
            }
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3_47());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_75() {
        if (jj_scan_token(43) || jj_scan_token(44) || jj_scan_token(125) || jj_3R_407() || jj_scan_token(42) || jj_3R_408() || jj_scan_token(9) || jj_scan_token(125)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_409()) {
            this.jj_scanpos = xsp;
            if (jj_3R_410()) {
                this.jj_scanpos = xsp;
                if (jj_3_23()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_411()) {
                        return true;
                    }
                }
            }
        }
        return jj_scan_token(126) || jj_scan_token(126);
    }

    private boolean jj_3R_96() {
        return jj_scan_token(145);
    }

    private boolean jj_3R_94() {
        return jj_scan_token(144);
    }

    private boolean jj_3R_93() {
        return jj_scan_token(143);
    }

    private boolean jj_3R_229() {
        return jj_scan_token(92);
    }

    private boolean jj_3_45() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_96()) {
            this.jj_scanpos = xsp;
            if (jj_3R_97()) {
                return true;
            }
        }
        return jj_3R_98();
    }

    private boolean jj_3R_412() {
        return jj_3R_422();
    }

    private boolean jj_3R_209() {
        Token xsp;
        if (jj_3R_98()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3_45());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3_22() {
        return jj_3R_73();
    }

    private boolean jj_3R_231() {
        return jj_scan_token(70);
    }

    private boolean jj_3R_395() {
        if (jj_scan_token(43) || jj_scan_token(125) || jj_3R_407() || jj_scan_token(42) || jj_3R_408() || jj_scan_token(9) || jj_scan_token(125)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3_22()) {
            this.jj_scanpos = xsp;
            if (jj_3R_412()) {
                return true;
            }
        }
        return jj_scan_token(126) || jj_scan_token(126);
    }

    private boolean jj_3_44() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_93()) {
            this.jj_scanpos = xsp;
            if (jj_3R_94()) {
                return true;
            }
        }
        return jj_3R_95();
    }

    private boolean jj_3R_430() {
        return jj_scan_token(123) || jj_3R_429();
    }

    private boolean jj_3R_422() {
        Token xsp;
        if (jj_3R_429()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_430());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_92() {
        Token xsp;
        if (jj_3R_95()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3_44());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_431() {
        return jj_3R_258();
    }

    private boolean jj_3R_230() {
        return jj_scan_token(26);
    }

    private boolean jj_3R_429() {
        if (jj_scan_token(125) || jj_3R_90() || jj_scan_token(126)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_431()) {
            this.jj_scanpos = xsp;
            return false;
        }
        return false;
    }

    private boolean jj_3R_378() {
        return jj_scan_token(123);
    }

    private boolean jj_3R_279() {
        return jj_scan_token(142) || jj_3R_209();
    }

    private boolean jj_3R_118() {
        return jj_scan_token(125);
    }

    private boolean jj_3R_165() {
        return jj_scan_token(123) || jj_3R_72();
    }

    private boolean jj_3_43() {
        return jj_3R_92();
    }

    private boolean jj_3R_73() {
        Token xsp;
        if (jj_3R_72()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_165());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_95() {
        Token xsp;
        if (jj_3R_209()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_279());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_155() {
        return jj_scan_token(125) || jj_3R_92() || jj_scan_token(126);
    }

    private boolean jj_3R_419() {
        return jj_scan_token(123) || jj_3R_418();
    }

    private boolean jj_3R_154() {
        return jj_3R_92();
    }

    private boolean jj_3R_407() {
        Token xsp;
        if (jj_3R_418()) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_419());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_379() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(112)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(111)) {
                this.jj_scanpos = xsp;
                if (jj_scan_token(119)) {
                    this.jj_scanpos = xsp;
                    return jj_scan_token(116);
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_300() {
        return jj_3R_342();
    }

    static {
        jj_la1_init_0();
        jj_la1_init_1();
        jj_la1_init_2();
        jj_la1_init_3();
        jj_la1_init_4();
    }

    private static void jj_la1_init_0() {
        jj_la1_0 = new int[]{0, 0, 67108864, 0, 0, 0, 1073741824, 0, 8388608, 0, 0, 0, 0, 0, 0, 131072, 0, 8388608, 0, 0, 0, 0, 0, 0, 0, 0, 16777344, 0, 1073741824, 0, 128, 128, 128, 128, 128, 128, 128, 160, 0, 0, 2048, 4096, 4096, 524288, 8388608, 1073741824, 0, 0, 0, 0, 0, 4096, 4096, 0, 0, 0, 0, 0, 0, 0, 0, 160, 0, 32, 160, 0, 128, 0, 0, 160, 0, 0, 16384, 0, 0, 0, 128, 0, 0, 160, 128, 0, 0, 0, 0, 16777344, 0, 0, 160, 939524096, 268435456, 0, 805306368, 805306368, 134217728, 0, 2048, 2048, 0, 0, 0, 0, 0, 4456448, 4456448, 0, 0, 0, 0, 0, 0, 16781312, 0, 0, 0, 0, 1048576, 0, 65536, 65536, 65536, 65536, 128, 0, 65536, 0, 0, 0, 0, 0, 16777344, 0, 16777344, 0, 16777344, 65536, 16777344, 65536, 65536, 0, 65536, R.attr.scrollViewStyle, 65536, 0, 0, 16797824, 16384, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16777216, 0, 0, 0, 0, 0, 16777344, 16777344, 0, 0, 0, 0, 16777344, 0, 0, 16777344, 0, 0, 0, 0, 16777344, 0, 16777344, 0, 0, 0, 4096, 4096, 16777344, 16781440, 0, 16875520, 0, 21331968, 21331968, 0, 0, 21331968, 21331968, 16875520, 0, 16875520, 0, 16875520, 0, 0, 0, 0, 16875520, 0, 32768, 16875520, 32, 32, 0, 0, 0, 0, 0, 1024, 0, 0, 0, 16875520, 0, 0, 0, 0, 0, 0, 0};
    }

    private static void jj_la1_init_1() {
        jj_la1_1 = new int[]{0, -989855680, 0, 0, 0, 0, 0, 512, 0, 0, 0, 8388608, 8388608, 0, 67108864, 75497472, 0, 0, 0, 8388608, 8388608, 0, 0, 0, 0, 67108928, 8392705, 0, 0, 512, 8392704, 8392704, 8392704, 8392704, 8392704, 8392704, 8392704, 8392704, 64, 67108864, 0, 0, 0, 0, 0, 0, 512, 0, 32768, 536870912, 67108864, 0, 0, 16384, 67108864, 16384, 2097152, 134742016, 0, 0, 0, 8392704, 0, 0, 8392704, 0, 8392704, 0, 0, 8392704, 0, 0, 67108864, 0, 0, 67108864, 8392704, 2048, 2048, 8392704, 8392704, 0, 0, 0, 0, 8392705, 0, 0, 8392704, 4456480, 4194336, 1048576, 4456480, 4456480, 0, 0, 8192, 8192, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 134742016, 0, 0, 0, 0, 0, 0, 0, 0, 8392704, 0, 0, 0, 0, 0, 0, 0, 8392705, 0, 8392705, 0, 8392705, 0, 8392705, 0, 0, 0, 0, 8392705, 0, 0, 0, 8392721, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 8392705, 8392705, 0, 0, 2097152, 0, 8392705, 0, 0, 8392705, 2, 8, 2, 8, 8392707, 0, 8392705, 0, 0, 0, 0, 0, 8392705, 8392705, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 131072, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 131200, 0, 0};
    }

    private static void jj_la1_init_2() {
        jj_la1_2 = new int[]{0, 268435472, 64, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, -939302320, 0, 0, 0, -939433392, -939433392, -939433392, -939433392, -939433392, -939433392, -939433392, -939433392, 0, 0, 0, 128, 128, 0, 0, 0, 0, 25165824, 0, 0, 0, 128, 128, 3328, 0, 3328, 0, 0, 0, 0, 0, -939433392, 0, 0, -939433392, 0, -939433392, 0, 0, -939433392, 0, 0, 0, 0, 0, 0, -939400624, 0, 0, -939433392, -939400624, 0, 0, 0, 0, -939302320, 0, 0, -939433392, 8, 0, 0, 8, 8, 0, 0, 0, 0, 8388608, 67108864, 0, 134217728, 0, 0, 0, 0, 0, Integer.MIN_VALUE, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -939433392, 33554432, 0, 0, 0, 0, 33554432, 0, -939302320, 0, -939302320, 0, -939302320, 0, -939302320, 0, 0, 2, 0, -939302320, 0, 0, 0, -939302320, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 131072, 0, 0, 0, 0, -939302320, -939302320, 8192, 0, 0, 0, -939302288, 0, 0, -939302320, 0, 0, 0, 0, -939302320, 0, -939302320, 0, 0, 0, 128, 128, -939302320, -939302192, 0, 4, 0, 4, 4, 0, 0, 4, 4, 4, 0, 4, 0, 4, 0, 524288, 524288, 0, 4, 0, 786432, 4, 0, 0, 2097152, 0, 0, 0, 2097152, 0, 65536, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0};
    }

    private static void jj_la1_init_3() {
        jj_la1_3 = new int[]{67108864, 536870912, CpioConstants.C_ISBLK, 67108864, 134217728, 134217728, 0, 0, 0, 134217728, 134217728, 0, 0, 134217728, 536870912, 536870912, 134217728, 0, 134217728, 0, 0, 134217728, 134217728, 134217728, 1073741824, 536870912, -1584299697, 512, 0, 0, 17826127, 17826127, 17826127, 17826127, 17826127, 17826127, 17826127, 17826127, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 536870912, 0, 0, 0, 536870912, 0, 0, 0, 134217728, 536870912, 134217728, 17826127, Integer.MIN_VALUE, 0, 17826127, 134217728, 554697039, 134217728, 134217728, 17826127, 134217728, 536870912, 536870912, 536870912, 134217728, 536870912, 554697039, 0, 0, 17826127, 554697039, 134217728, 134217728, 134217728, 134217728, 563183951, 134217728, 536870912, 17826127, 134217728, 0, 0, 0, 0, 134217728, 134217728, 0, 0, 0, 0, 134217728, 0, 134217728, 0, 0, 3, 3, 0, 65536, 65536, 65536, 65536, 65536, 0, 65536, 536936448, 0, 536870912, 0, 536870912, 0, 536870912, 17826127, 0, 0, 0, 1024, 268437504, 0, 0, 563183951, 0, 563183951, 0, 563183951, 0, 563183951, 0, 0, 0, 0, 563183951, 0, 134217728, 134217728, 563183951, 0, 536870912, 0, 0, 0, 536870912, Integer.MIN_VALUE, 536870912, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 545259520, 0, 0, 134217728, 134217728, -1584299697, -1584299697, 0, 12, 0, 12, 563184095, 96, 96, 563184095, 0, 0, 0, 0, 563183951, CpioConstants.C_ISBLK, 563183951, 0, 0, 0, 0, 0, -1584299697, -1584299697, 0, 814841856, R.raw.loaderror, 814841856, 814841856, 134217728, R.raw.loaderror, 814841856, 814841856, 814841856, 4096, 814841856, R.raw.loaderror, 814841856, 134217728, 0, 0, R.raw.loaderror, 814841856, 0, R.raw.loaderror, 814841856, 536870912, 536870912, 1048576, 8454144, 8454144, 134217728, 0, 0, 0, 536870912, 536870912, 814841856, 9535488, 9535488, 134217728, 134217728, 1048576, 1048576, R.raw.loaderror};
    }

    private static void jj_la1_init_4() {
        jj_la1_4 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 726007810, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 726007810, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 2, 2, 0, 0, 0, 0, 0, 0, 536870912, 0, 0, 4, 0, 16376, 0, 4, 726007810, 4, 726007810, 4, 726007810, 0, 726007810, 0, 0, 0, 0, 726007810, 0, 0, 0, 726007810, 0, 0, 16384, 98304, 393216, 0, 1572864, 0, 2097152, 393216, 393216, 393216, 393216, 393216, 393216, 393216, 393216, 393216, 393216, 393216, 393216, 393216, 393216, 393216, 393216, 134217730, 54919168, 67108864, 268435456, 0, 0, 726007810, 726007810, 0, 0, 0, 0, 726007810, 0, 0, 726007810, 0, 0, 0, 0, 726007810, 0, 726007810, 536870912, 1, 1, 0, 0, 726007810, 726007810, 8388608, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public CCJSqlParser(InputStream stream) {
        this(stream, null);
    }

    public CCJSqlParser(InputStream stream, String encoding) {
        this.allowOraclePrior = false;
        this.jj_la1 = new int[EscherProperties.GEOTEXT__ROTATECHARACTERS];
        this.jj_2_rtns = new JJCalls[60];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        try {
            this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
            this.token_source = new CCJSqlParserTokenManager(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            this.jj_gen = 0;
            for (int i = 0; i < 242; i++) {
                this.jj_la1[i] = -1;
            }
            for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
                this.jj_2_rtns[i2] = new JJCalls();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void ReInit(InputStream stream) {
        ReInit(stream, null);
    }

    public void ReInit(InputStream stream, String encoding) {
        try {
            this.jj_input_stream.ReInit(stream, encoding, 1, 1);
            this.token_source.ReInit(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            this.jj_gen = 0;
            for (int i = 0; i < 242; i++) {
                this.jj_la1[i] = -1;
            }
            for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
                this.jj_2_rtns[i2] = new JJCalls();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public CCJSqlParser(Reader stream) {
        this.allowOraclePrior = false;
        this.jj_la1 = new int[EscherProperties.GEOTEXT__ROTATECHARACTERS];
        this.jj_2_rtns = new JJCalls[60];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        this.token_source = new CCJSqlParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 242; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public void ReInit(Reader stream) {
        if (this.jj_input_stream == null) {
            this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        } else {
            this.jj_input_stream.ReInit(stream, 1, 1);
        }
        if (this.token_source == null) {
            this.token_source = new CCJSqlParserTokenManager(this.jj_input_stream);
        }
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 242; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public CCJSqlParser(CCJSqlParserTokenManager tm) {
        this.allowOraclePrior = false;
        this.jj_la1 = new int[EscherProperties.GEOTEXT__ROTATECHARACTERS];
        this.jj_2_rtns = new JJCalls[60];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 242; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public void ReInit(CCJSqlParserTokenManager tm) {
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 242; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    private Token jj_consume_token(int kind) throws ParseException {
        Token oldToken = this.token;
        if (oldToken.next != null) {
            this.token = this.token.next;
        } else {
            Token token = this.token;
            Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        if (this.token.kind == kind) {
            this.jj_gen++;
            int i = this.jj_gc + 1;
            this.jj_gc = i;
            if (i > 100) {
                this.jj_gc = 0;
                for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
                    JJCalls jJCalls = this.jj_2_rtns[i2];
                    while (true) {
                        JJCalls c = jJCalls;
                        if (c != null) {
                            if (c.gen < this.jj_gen) {
                                c.first = null;
                            }
                            jJCalls = c.next;
                        }
                    }
                }
            }
            return this.token;
        }
        this.token = oldToken;
        this.jj_kind = kind;
        throw generateParseException();
    }

    /* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/parser/CCJSqlParser$LookaheadSuccess.class */
    private static final class LookaheadSuccess extends Error {
        private LookaheadSuccess() {
        }
    }

    private boolean jj_scan_token(int kind) {
        Token tok;
        if (this.jj_scanpos == this.jj_lastpos) {
            this.jj_la--;
            if (this.jj_scanpos.next == null) {
                Token token = this.jj_scanpos;
                Token nextToken = this.token_source.getNextToken();
                token.next = nextToken;
                this.jj_scanpos = nextToken;
                this.jj_lastpos = nextToken;
            } else {
                Token token2 = this.jj_scanpos.next;
                this.jj_scanpos = token2;
                this.jj_lastpos = token2;
            }
        } else {
            this.jj_scanpos = this.jj_scanpos.next;
        }
        if (this.jj_rescan) {
            int i = 0;
            Token token3 = this.token;
            while (true) {
                tok = token3;
                if (tok == null || tok == this.jj_scanpos) {
                    break;
                }
                i++;
                token3 = tok.next;
            }
            if (tok != null) {
                jj_add_error_token(kind, i);
            }
        }
        if (this.jj_scanpos.kind != kind) {
            return true;
        }
        if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) {
            throw this.jj_ls;
        }
        return false;
    }

    public final Token getNextToken() {
        if (this.token.next != null) {
            this.token = this.token.next;
        } else {
            Token token = this.token;
            Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        this.jj_gen++;
        return this.token;
    }

    public final Token getToken(int index) {
        Token token;
        Token t = this.token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) {
                token = t.next;
            } else {
                Token nextToken = this.token_source.getNextToken();
                token = nextToken;
                t.next = nextToken;
            }
            t = token;
        }
        return t;
    }

    private int jj_ntk_f() {
        Token token = this.token.next;
        this.jj_nt = token;
        if (token == null) {
            Token token2 = this.token;
            Token nextToken = this.token_source.getNextToken();
            token2.next = nextToken;
            int i = nextToken.kind;
            this.jj_ntk = i;
            return i;
        }
        int i2 = this.jj_nt.kind;
        this.jj_ntk = i2;
        return i2;
    }

    private void jj_add_error_token(int kind, int pos) {
        if (pos >= 100) {
            return;
        }
        if (pos == this.jj_endpos + 1) {
            int[] iArr = this.jj_lasttokens;
            int i = this.jj_endpos;
            this.jj_endpos = i + 1;
            iArr[i] = kind;
            return;
        }
        if (this.jj_endpos != 0) {
            this.jj_expentry = new int[this.jj_endpos];
            for (int i2 = 0; i2 < this.jj_endpos; i2++) {
                this.jj_expentry[i2] = this.jj_lasttokens[i2];
            }
            Iterator i$ = this.jj_expentries.iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                int[] oldentry = i$.next();
                if (oldentry.length == this.jj_expentry.length) {
                    boolean isMatched = true;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= this.jj_expentry.length) {
                            break;
                        }
                        if (oldentry[i3] == this.jj_expentry[i3]) {
                            i3++;
                        } else {
                            isMatched = false;
                            break;
                        }
                    }
                    if (isMatched) {
                        this.jj_expentries.add(this.jj_expentry);
                        break;
                    }
                }
            }
            if (pos != 0) {
                int[] iArr2 = this.jj_lasttokens;
                this.jj_endpos = pos;
                iArr2[pos - 1] = kind;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v16, types: [int[], int[][]] */
    public ParseException generateParseException() {
        this.jj_expentries.clear();
        boolean[] la1tokens = new boolean[158];
        if (this.jj_kind >= 0) {
            la1tokens[this.jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int i = 0; i < 242; i++) {
            if (this.jj_la1[i] == this.jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                    if ((jj_la1_1[i] & (1 << j)) != 0) {
                        la1tokens[32 + j] = true;
                    }
                    if ((jj_la1_2[i] & (1 << j)) != 0) {
                        la1tokens[64 + j] = true;
                    }
                    if ((jj_la1_3[i] & (1 << j)) != 0) {
                        la1tokens[96 + j] = true;
                    }
                    if ((jj_la1_4[i] & (1 << j)) != 0) {
                        la1tokens[128 + j] = true;
                    }
                }
            }
        }
        for (int i2 = 0; i2 < 158; i2++) {
            if (la1tokens[i2]) {
                this.jj_expentry = new int[1];
                this.jj_expentry[0] = i2;
                this.jj_expentries.add(this.jj_expentry);
            }
        }
        this.jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
        ?? r0 = new int[this.jj_expentries.size()];
        for (int i3 = 0; i3 < this.jj_expentries.size(); i3++) {
            r0[i3] = this.jj_expentries.get(i3);
        }
        return new ParseException(this.token, r0, tokenImage);
    }

    public final void enable_tracing() {
    }

    public final void disable_tracing() {
    }

    private void jj_rescan_token() {
        this.jj_rescan = true;
        for (int i = 0; i < 60; i++) {
            try {
                JJCalls p = this.jj_2_rtns[i];
                do {
                    if (p.gen > this.jj_gen) {
                        this.jj_la = p.arg;
                        Token token = p.first;
                        this.jj_scanpos = token;
                        this.jj_lastpos = token;
                        switch (i) {
                            case 0:
                                jj_3_1();
                                break;
                            case 1:
                                jj_3_2();
                                break;
                            case 2:
                                jj_3_3();
                                break;
                            case 3:
                                jj_3_4();
                                break;
                            case 4:
                                jj_3_5();
                                break;
                            case 5:
                                jj_3_6();
                                break;
                            case 6:
                                jj_3_7();
                                break;
                            case 7:
                                jj_3_8();
                                break;
                            case 8:
                                jj_3_9();
                                break;
                            case 9:
                                jj_3_10();
                                break;
                            case 10:
                                jj_3_11();
                                break;
                            case 11:
                                jj_3_12();
                                break;
                            case 12:
                                jj_3_13();
                                break;
                            case 13:
                                jj_3_14();
                                break;
                            case 14:
                                jj_3_15();
                                break;
                            case 15:
                                jj_3_16();
                                break;
                            case 16:
                                jj_3_17();
                                break;
                            case 17:
                                jj_3_18();
                                break;
                            case 18:
                                jj_3_19();
                                break;
                            case 19:
                                jj_3_20();
                                break;
                            case 20:
                                jj_3_21();
                                break;
                            case 21:
                                jj_3_22();
                                break;
                            case 22:
                                jj_3_23();
                                break;
                            case 23:
                                jj_3_24();
                                break;
                            case 24:
                                jj_3_25();
                                break;
                            case 25:
                                jj_3_26();
                                break;
                            case 26:
                                jj_3_27();
                                break;
                            case 27:
                                jj_3_28();
                                break;
                            case 28:
                                jj_3_29();
                                break;
                            case 29:
                                jj_3_30();
                                break;
                            case 30:
                                jj_3_31();
                                break;
                            case 31:
                                jj_3_32();
                                break;
                            case 32:
                                jj_3_33();
                                break;
                            case 33:
                                jj_3_34();
                                break;
                            case 34:
                                jj_3_35();
                                break;
                            case 35:
                                jj_3_36();
                                break;
                            case 36:
                                jj_3_37();
                                break;
                            case 37:
                                jj_3_38();
                                break;
                            case 38:
                                jj_3_39();
                                break;
                            case 39:
                                jj_3_40();
                                break;
                            case 40:
                                jj_3_41();
                                break;
                            case 41:
                                jj_3_42();
                                break;
                            case 42:
                                jj_3_43();
                                break;
                            case 43:
                                jj_3_44();
                                break;
                            case 44:
                                jj_3_45();
                                break;
                            case 45:
                                jj_3_46();
                                break;
                            case 46:
                                jj_3_47();
                                break;
                            case 47:
                                jj_3_48();
                                break;
                            case 48:
                                jj_3_49();
                                break;
                            case 49:
                                jj_3_50();
                                break;
                            case 50:
                                jj_3_51();
                                break;
                            case 51:
                                jj_3_52();
                                break;
                            case 52:
                                jj_3_53();
                                break;
                            case 53:
                                jj_3_54();
                                break;
                            case 54:
                                jj_3_55();
                                break;
                            case 55:
                                jj_3_56();
                                break;
                            case 56:
                                jj_3_57();
                                break;
                            case 57:
                                jj_3_58();
                                break;
                            case 58:
                                jj_3_59();
                                break;
                            case 59:
                                jj_3_60();
                                break;
                        }
                    }
                    p = p.next;
                } while (p != null);
            } catch (LookaheadSuccess e) {
            }
        }
        this.jj_rescan = false;
    }

    private void jj_save(int index, int xla) {
        JJCalls p;
        JJCalls jJCalls = this.jj_2_rtns[index];
        while (true) {
            p = jJCalls;
            if (p.gen <= this.jj_gen) {
                break;
            }
            if (p.next == null) {
                JJCalls jJCalls2 = new JJCalls();
                p.next = jJCalls2;
                p = jJCalls2;
                break;
            }
            jJCalls = p.next;
        }
        p.gen = (this.jj_gen + xla) - this.jj_la;
        p.first = this.token;
        p.arg = xla;
    }

    /* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/parser/CCJSqlParser$JJCalls.class */
    static final class JJCalls {
        int gen;
        Token first;
        int arg;
        JJCalls next;

        JJCalls() {
        }
    }
}
