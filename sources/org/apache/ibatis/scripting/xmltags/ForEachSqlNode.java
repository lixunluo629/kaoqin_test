package org.apache.ibatis.scripting.xmltags;

import java.util.Map;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.session.Configuration;
import org.springframework.context.expression.StandardBeanExpressionResolver;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/ForEachSqlNode.class */
public class ForEachSqlNode implements SqlNode {
    public static final String ITEM_PREFIX = "__frch_";
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    private final String collectionExpression;
    private final SqlNode contents;
    private final String open;
    private final String close;
    private final String separator;
    private final String item;
    private final String index;
    private final Configuration configuration;

    public ForEachSqlNode(Configuration configuration, SqlNode contents, String collectionExpression, String index, String item, String open, String close, String separator) {
        this.collectionExpression = collectionExpression;
        this.contents = contents;
        this.open = open;
        this.close = close;
        this.separator = separator;
        this.index = index;
        this.item = item;
        this.configuration = configuration;
    }

    @Override // org.apache.ibatis.scripting.xmltags.SqlNode
    public boolean apply(DynamicContext context) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        DynamicContext context2;
        Map<String, Object> bindings = context.getBindings();
        Iterable<?> iterable = this.evaluator.evaluateIterable(this.collectionExpression, bindings);
        if (!iterable.iterator().hasNext()) {
            return true;
        }
        boolean first = true;
        applyOpen(context);
        int i = 0;
        for (Object o : iterable) {
            DynamicContext oldContext = context;
            if (first || this.separator == null) {
                context2 = new PrefixedContext(context, "");
            } else {
                context2 = new PrefixedContext(context, this.separator);
            }
            int uniqueNumber = context2.getUniqueNumber();
            if (o instanceof Map.Entry) {
                Map.Entry<Object, Object> mapEntry = (Map.Entry) o;
                applyIndex(context2, mapEntry.getKey(), uniqueNumber);
                applyItem(context2, mapEntry.getValue(), uniqueNumber);
            } else {
                applyIndex(context2, Integer.valueOf(i), uniqueNumber);
                applyItem(context2, o, uniqueNumber);
            }
            this.contents.apply(new FilteredDynamicContext(this.configuration, context2, this.index, this.item, uniqueNumber));
            if (first) {
                first = !((PrefixedContext) context2).isPrefixApplied();
            }
            context = oldContext;
            i++;
        }
        applyClose(context);
        context.getBindings().remove(this.item);
        context.getBindings().remove(this.index);
        return true;
    }

    private void applyIndex(DynamicContext context, Object o, int i) {
        if (this.index != null) {
            context.bind(this.index, o);
            context.bind(itemizeItem(this.index, i), o);
        }
    }

    private void applyItem(DynamicContext context, Object o, int i) {
        if (this.item != null) {
            context.bind(this.item, o);
            context.bind(itemizeItem(this.item, i), o);
        }
    }

    private void applyOpen(DynamicContext context) {
        if (this.open != null) {
            context.appendSql(this.open);
        }
    }

    private void applyClose(DynamicContext context) {
        if (this.close != null) {
            context.appendSql(this.close);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String itemizeItem(String item, int i) {
        return ITEM_PREFIX + item + "_" + i;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/ForEachSqlNode$FilteredDynamicContext.class */
    private static class FilteredDynamicContext extends DynamicContext {
        private final DynamicContext delegate;
        private final int index;
        private final String itemIndex;
        private final String item;

        public FilteredDynamicContext(Configuration configuration, DynamicContext delegate, String itemIndex, String item, int i) {
            super(configuration, null);
            this.delegate = delegate;
            this.index = i;
            this.itemIndex = itemIndex;
            this.item = item;
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
        public String getSql() {
            return this.delegate.getSql();
        }

        @Override // org.apache.ibatis.scripting.xmltags.DynamicContext
        public void appendSql(String sql) {
            GenericTokenParser parser = new GenericTokenParser(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX, "}", new TokenHandler() { // from class: org.apache.ibatis.scripting.xmltags.ForEachSqlNode.FilteredDynamicContext.1
                @Override // org.apache.ibatis.parsing.TokenHandler
                public String handleToken(String content) {
                    String newContent = content.replaceFirst("^\\s*" + FilteredDynamicContext.this.item + "(?![^.,:\\s])", ForEachSqlNode.itemizeItem(FilteredDynamicContext.this.item, FilteredDynamicContext.this.index));
                    if (FilteredDynamicContext.this.itemIndex != null && newContent.equals(content)) {
                        newContent = content.replaceFirst("^\\s*" + FilteredDynamicContext.this.itemIndex + "(?![^.,:\\s])", ForEachSqlNode.itemizeItem(FilteredDynamicContext.this.itemIndex, FilteredDynamicContext.this.index));
                    }
                    return StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX + newContent + "}";
                }
            });
            this.delegate.appendSql(parser.parse(sql));
        }

        @Override // org.apache.ibatis.scripting.xmltags.DynamicContext
        public int getUniqueNumber() {
            return this.delegate.getUniqueNumber();
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/ForEachSqlNode$PrefixedContext.class */
    private class PrefixedContext extends DynamicContext {
        private final DynamicContext delegate;
        private final String prefix;
        private boolean prefixApplied;

        public PrefixedContext(DynamicContext delegate, String prefix) {
            super(ForEachSqlNode.this.configuration, null);
            this.delegate = delegate;
            this.prefix = prefix;
            this.prefixApplied = false;
        }

        public boolean isPrefixApplied() {
            return this.prefixApplied;
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
        public void appendSql(String sql) {
            if (!this.prefixApplied && sql != null && sql.trim().length() > 0) {
                this.delegate.appendSql(this.prefix);
                this.prefixApplied = true;
            }
            this.delegate.appendSql(sql);
        }

        @Override // org.apache.ibatis.scripting.xmltags.DynamicContext
        public String getSql() {
            return this.delegate.getSql();
        }

        @Override // org.apache.ibatis.scripting.xmltags.DynamicContext
        public int getUniqueNumber() {
            return this.delegate.getUniqueNumber();
        }
    }
}
