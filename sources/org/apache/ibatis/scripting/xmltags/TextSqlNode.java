package org.apache.ibatis.scripting.xmltags;

import java.util.regex.Pattern;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.scripting.ScriptingException;
import org.apache.ibatis.type.SimpleTypeRegistry;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/TextSqlNode.class */
public class TextSqlNode implements SqlNode {
    private final String text;
    private final Pattern injectionFilter;

    public TextSqlNode(String text) {
        this(text, null);
    }

    public TextSqlNode(String text, Pattern injectionFilter) {
        this.text = text;
        this.injectionFilter = injectionFilter;
    }

    public boolean isDynamic() {
        DynamicCheckerTokenParser checker = new DynamicCheckerTokenParser();
        GenericTokenParser parser = createParser(checker);
        parser.parse(this.text);
        return checker.isDynamic();
    }

    @Override // org.apache.ibatis.scripting.xmltags.SqlNode
    public boolean apply(DynamicContext context) {
        GenericTokenParser parser = createParser(new BindingTokenParser(context, this.injectionFilter));
        context.appendSql(parser.parse(this.text));
        return true;
    }

    private GenericTokenParser createParser(TokenHandler handler) {
        return new GenericTokenParser("${", "}", handler);
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/TextSqlNode$BindingTokenParser.class */
    private static class BindingTokenParser implements TokenHandler {
        private DynamicContext context;
        private Pattern injectionFilter;

        public BindingTokenParser(DynamicContext context, Pattern injectionFilter) {
            this.context = context;
            this.injectionFilter = injectionFilter;
        }

        @Override // org.apache.ibatis.parsing.TokenHandler
        public String handleToken(String content) {
            Object parameter = this.context.getBindings().get(DynamicContext.PARAMETER_OBJECT_KEY);
            if (parameter == null) {
                this.context.getBindings().put("value", null);
            } else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) {
                this.context.getBindings().put("value", parameter);
            }
            Object value = OgnlCache.getValue(content, this.context.getBindings());
            String srtValue = value == null ? "" : String.valueOf(value);
            checkInjection(srtValue);
            return srtValue;
        }

        private void checkInjection(String value) {
            if (this.injectionFilter != null && !this.injectionFilter.matcher(value).matches()) {
                throw new ScriptingException("Invalid input. Please conform to regex" + this.injectionFilter.pattern());
            }
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/TextSqlNode$DynamicCheckerTokenParser.class */
    private static class DynamicCheckerTokenParser implements TokenHandler {
        private boolean isDynamic;

        public boolean isDynamic() {
            return this.isDynamic;
        }

        @Override // org.apache.ibatis.parsing.TokenHandler
        public String handleToken(String content) {
            this.isDynamic = true;
            return null;
        }
    }
}
