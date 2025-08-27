package net.sf.jsqlparser.schema;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.IntoTableVisitor;
import net.sf.jsqlparser.statement.select.Pivot;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/schema/Table.class */
public class Table implements FromItem, MultiPartName {
    private Database database;
    private String schemaName;
    private String name;
    private Alias alias;
    private Pivot pivot;

    public Table() {
    }

    public Table(String name) {
        this.name = name;
    }

    public Table(String schemaName, String name) {
        this.schemaName = schemaName;
        this.name = name;
    }

    public Table(Database database, String schemaName, String name) {
        this.database = database;
        this.schemaName = schemaName;
        this.name = name;
    }

    public Database getDatabase() {
        return this.database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public String getSchemaName() {
        return this.schemaName;
    }

    public void setSchemaName(String string) {
        this.schemaName = string;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String string) {
        this.name = string;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public Alias getAlias() {
        return this.alias;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    @Override // net.sf.jsqlparser.schema.MultiPartName
    public String getFullyQualifiedName() {
        String fqn = "";
        if (this.database != null) {
            fqn = fqn + this.database.getFullyQualifiedName();
        }
        if (!fqn.isEmpty()) {
            fqn = fqn + ".";
        }
        if (this.schemaName != null) {
            fqn = fqn + this.schemaName;
        }
        if (!fqn.isEmpty()) {
            fqn = fqn + ".";
        }
        if (this.name != null) {
            fqn = fqn + this.name;
        }
        return fqn;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void accept(FromItemVisitor fromItemVisitor) {
        fromItemVisitor.visit(this);
    }

    public void accept(IntoTableVisitor intoTableVisitor) {
        intoTableVisitor.visit(this);
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public Pivot getPivot() {
        return this.pivot;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    public String toString() {
        return getFullyQualifiedName() + (this.pivot != null ? SymbolConstants.SPACE_SYMBOL + this.pivot : "") + (this.alias != null ? this.alias.toString() : "");
    }
}
