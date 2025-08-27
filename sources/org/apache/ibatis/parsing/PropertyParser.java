package org.apache.ibatis.parsing;

import java.util.Properties;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/parsing/PropertyParser.class */
public class PropertyParser {
    private static final String KEY_PREFIX = "org.apache.ibatis.parsing.PropertyParser.";
    public static final String KEY_ENABLE_DEFAULT_VALUE = "org.apache.ibatis.parsing.PropertyParser.enable-default-value";
    public static final String KEY_DEFAULT_VALUE_SEPARATOR = "org.apache.ibatis.parsing.PropertyParser.default-value-separator";
    private static final String ENABLE_DEFAULT_VALUE = "false";
    private static final String DEFAULT_VALUE_SEPARATOR = ":";

    private PropertyParser() {
    }

    public static String parse(String string, Properties variables) {
        VariableTokenHandler handler = new VariableTokenHandler(variables);
        GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
        return parser.parse(string);
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/parsing/PropertyParser$VariableTokenHandler.class */
    private static class VariableTokenHandler implements TokenHandler {
        private final Properties variables;
        private final boolean enableDefaultValue;
        private final String defaultValueSeparator;

        private VariableTokenHandler(Properties variables) {
            this.variables = variables;
            this.enableDefaultValue = Boolean.parseBoolean(getPropertyValue(PropertyParser.KEY_ENABLE_DEFAULT_VALUE, "false"));
            this.defaultValueSeparator = getPropertyValue(PropertyParser.KEY_DEFAULT_VALUE_SEPARATOR, ":");
        }

        private String getPropertyValue(String key, String defaultValue) {
            return this.variables == null ? defaultValue : this.variables.getProperty(key, defaultValue);
        }

        @Override // org.apache.ibatis.parsing.TokenHandler
        public String handleToken(String content) {
            if (this.variables != null) {
                String key = content;
                if (this.enableDefaultValue) {
                    int separatorIndex = content.indexOf(this.defaultValueSeparator);
                    String defaultValue = null;
                    if (separatorIndex >= 0) {
                        key = content.substring(0, separatorIndex);
                        defaultValue = content.substring(separatorIndex + this.defaultValueSeparator.length());
                    }
                    if (defaultValue != null) {
                        return this.variables.getProperty(key, defaultValue);
                    }
                }
                if (this.variables.containsKey(key)) {
                    return this.variables.getProperty(key);
                }
            }
            return "${" + content + "}";
        }
    }
}
