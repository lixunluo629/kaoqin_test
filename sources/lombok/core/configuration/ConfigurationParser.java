package lombok.core.configuration;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/ConfigurationParser.SCL.lombok */
public class ConfigurationParser {
    private static final Pattern LINE = Pattern.compile("(?:clear\\s+([^=]+))|(?:(\\S*?)\\s*([-+]?=)\\s*(.*?))");
    private static final Pattern NEWLINE_FINDER = Pattern.compile("^[\t ]*(.*?)[\t\r ]*$", 8);
    private ConfigurationProblemReporter reporter;

    /* loaded from: lombok-1.16.22.jar:lombok/core/configuration/ConfigurationParser$Collector.SCL.lombok */
    public interface Collector {
        void clear(ConfigurationKey<?> configurationKey, String str, int i);

        void set(ConfigurationKey<?> configurationKey, Object obj, String str, int i);

        void add(ConfigurationKey<?> configurationKey, Object obj, String str, int i);

        void remove(ConfigurationKey<?> configurationKey, Object obj, String str, int i);
    }

    public ConfigurationParser(ConfigurationProblemReporter reporter) {
        if (reporter == null) {
            throw new NullPointerException("reporter");
        }
        this.reporter = reporter;
    }

    public void parse(CharSequence content, String contentDescription, Collector collector) {
        String keyName;
        String operator;
        String stringValue;
        Map<String, ConfigurationKey<?>> registeredKeys = ConfigurationKey.registeredKeys();
        int lineNumber = 0;
        Matcher lineMatcher = NEWLINE_FINDER.matcher(content);
        while (lineMatcher.find()) {
            CharSequence line = content.subSequence(lineMatcher.start(1), lineMatcher.end(1));
            lineNumber++;
            if (line.length() != 0 && line.charAt(0) != '#') {
                Matcher matcher = LINE.matcher(line);
                if (!matcher.matches()) {
                    this.reporter.report(contentDescription, "Invalid line", lineNumber, line);
                } else {
                    if (matcher.group(1) == null) {
                        keyName = matcher.group(2);
                        operator = matcher.group(3);
                        stringValue = matcher.group(4);
                    } else {
                        keyName = matcher.group(1);
                        operator = "clear";
                        stringValue = null;
                    }
                    ConfigurationKey<?> key = registeredKeys.get(keyName);
                    if (key == null) {
                        this.reporter.report(contentDescription, "Unknown key '" + keyName + "'", lineNumber, line);
                    } else {
                        ConfigurationDataType type = key.getType();
                        boolean listOperator = operator.equals("+=") || operator.equals("-=");
                        if (listOperator && !type.isList()) {
                            this.reporter.report(contentDescription, "'" + keyName + "' is not a list and doesn't support " + operator + " (only = and clear)", lineNumber, line);
                        } else if (operator.equals(SymbolConstants.EQUAL_SYMBOL) && type.isList()) {
                            this.reporter.report(contentDescription, "'" + keyName + "' is a list and cannot be assigned to (use +=, -= and clear instead)", lineNumber, line);
                        } else {
                            Object value = null;
                            if (stringValue != null) {
                                try {
                                    value = type.getParser().parse(stringValue);
                                } catch (Exception unused) {
                                    this.reporter.report(contentDescription, "Error while parsing the value for '" + keyName + "' value '" + stringValue + "' (should be " + type.getParser().exampleValue() + ")", lineNumber, line);
                                }
                            }
                            if (operator.equals("clear")) {
                                collector.clear(key, contentDescription, lineNumber);
                            } else if (operator.equals(SymbolConstants.EQUAL_SYMBOL)) {
                                collector.set(key, value, contentDescription, lineNumber);
                            } else if (operator.equals("+=")) {
                                collector.add(key, value, contentDescription, lineNumber);
                            } else {
                                collector.remove(key, value, contentDescription, lineNumber);
                            }
                        }
                    }
                }
            }
        }
    }
}
