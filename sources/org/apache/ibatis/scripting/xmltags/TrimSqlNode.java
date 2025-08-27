package org.apache.ibatis.scripting.xmltags;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/TrimSqlNode.class */
public class TrimSqlNode implements SqlNode {
    private final SqlNode contents;
    private final String prefix;
    private final String suffix;
    private final List<String> prefixesToOverride;
    private final List<String> suffixesToOverride;
    private final Configuration configuration;

    public TrimSqlNode(Configuration configuration, SqlNode contents, String prefix, String prefixesToOverride, String suffix, String suffixesToOverride) {
        this(configuration, contents, prefix, parseOverrides(prefixesToOverride), suffix, parseOverrides(suffixesToOverride));
    }

    protected TrimSqlNode(Configuration configuration, SqlNode contents, String prefix, List<String> prefixesToOverride, String suffix, List<String> suffixesToOverride) {
        this.contents = contents;
        this.prefix = prefix;
        this.prefixesToOverride = prefixesToOverride;
        this.suffix = suffix;
        this.suffixesToOverride = suffixesToOverride;
        this.configuration = configuration;
    }

    @Override // org.apache.ibatis.scripting.xmltags.SqlNode
    public boolean apply(DynamicContext context) {
        FilteredDynamicContext filteredDynamicContext = new FilteredDynamicContext(context);
        boolean result = this.contents.apply(filteredDynamicContext);
        filteredDynamicContext.applyAll();
        return result;
    }

    private static List<String> parseOverrides(String overrides) {
        if (overrides != null) {
            StringTokenizer parser = new StringTokenizer(overrides, "|", false);
            List<String> list = new ArrayList<>(parser.countTokens());
            while (parser.hasMoreTokens()) {
                list.add(parser.nextToken().toUpperCase(Locale.ENGLISH));
            }
            return list;
        }
        return Collections.emptyList();
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/TrimSqlNode$FilteredDynamicContext.class */
    private class FilteredDynamicContext extends DynamicContext {
        private DynamicContext delegate;
        private boolean prefixApplied;
        private boolean suffixApplied;
        private StringBuilder sqlBuffer;

        public FilteredDynamicContext(DynamicContext delegate) {
            super(TrimSqlNode.this.configuration, null);
            this.delegate = delegate;
            this.prefixApplied = false;
            this.suffixApplied = false;
            this.sqlBuffer = new StringBuilder();
        }

        public void applyAll() {
            this.sqlBuffer = new StringBuilder(this.sqlBuffer.toString().trim());
            String trimmedUppercaseSql = this.sqlBuffer.toString().toUpperCase(Locale.ENGLISH);
            if (trimmedUppercaseSql.length() > 0) {
                applyPrefix(this.sqlBuffer, trimmedUppercaseSql);
                applySuffix(this.sqlBuffer, trimmedUppercaseSql);
            }
            this.delegate.appendSql(this.sqlBuffer.toString());
        }

        @Override // org.apache.ibatis.scripting.xmltags.DynamicContext
        public Map<String, Object> getBindings() {
            return this.delegate.getBindings();
        }

        @Override // org.apache.ibatis.scripting.xmltags.DynamicContext
        public void bind(String name, Object value) {
            this.delegate.bind(name, value);
        }

        @Override // org.apache.ibatis.scripting.xmltags.DynamicContext
        public int getUniqueNumber() {
            return this.delegate.getUniqueNumber();
        }

        @Override // org.apache.ibatis.scripting.xmltags.DynamicContext
        public void appendSql(String sql) {
            this.sqlBuffer.append(sql);
        }

        @Override // org.apache.ibatis.scripting.xmltags.DynamicContext
        public String getSql() {
            return this.delegate.getSql();
        }

        private void applyPrefix(StringBuilder sql, String trimmedUppercaseSql) {
            if (!this.prefixApplied) {
                this.prefixApplied = true;
                if (TrimSqlNode.this.prefixesToOverride != null) {
                    Iterator it = TrimSqlNode.this.prefixesToOverride.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        String toRemove = (String) it.next();
                        if (trimmedUppercaseSql.startsWith(toRemove)) {
                            sql.delete(0, toRemove.trim().length());
                            break;
                        }
                    }
                }
                if (TrimSqlNode.this.prefix != null) {
                    sql.insert(0, SymbolConstants.SPACE_SYMBOL);
                    sql.insert(0, TrimSqlNode.this.prefix);
                }
            }
        }

        private void applySuffix(StringBuilder sql, String trimmedUppercaseSql) {
            if (!this.suffixApplied) {
                this.suffixApplied = true;
                if (TrimSqlNode.this.suffixesToOverride != null) {
                    for (String toRemove : TrimSqlNode.this.suffixesToOverride) {
                        if (trimmedUppercaseSql.endsWith(toRemove) || trimmedUppercaseSql.endsWith(toRemove.trim())) {
                            int start = sql.length() - toRemove.trim().length();
                            int end = sql.length();
                            sql.delete(start, end);
                            break;
                        }
                    }
                }
                if (TrimSqlNode.this.suffix != null) {
                    sql.append(SymbolConstants.SPACE_SYMBOL);
                    sql.append(TrimSqlNode.this.suffix);
                }
            }
        }
    }
}
