package org.springframework.core.env;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/env/SimpleCommandLineArgsParser.class */
class SimpleCommandLineArgsParser {
    SimpleCommandLineArgsParser() {
    }

    public CommandLineArgs parse(String... args) {
        String optionName;
        CommandLineArgs commandLineArgs = new CommandLineArgs();
        for (String arg : args) {
            if (arg.startsWith(ScriptUtils.DEFAULT_COMMENT_PREFIX)) {
                String optionText = arg.substring(2, arg.length());
                String optionValue = null;
                if (optionText.contains(SymbolConstants.EQUAL_SYMBOL)) {
                    optionName = optionText.substring(0, optionText.indexOf(61));
                    optionValue = optionText.substring(optionText.indexOf(61) + 1, optionText.length());
                } else {
                    optionName = optionText;
                }
                if (optionName.isEmpty() || (optionValue != null && optionValue.isEmpty())) {
                    throw new IllegalArgumentException("Invalid argument syntax: " + arg);
                }
                commandLineArgs.addOptionArg(optionName, optionValue);
            } else {
                commandLineArgs.addNonOptionArg(arg);
            }
        }
        return commandLineArgs;
    }
}
