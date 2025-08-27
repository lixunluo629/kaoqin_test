package org.springframework.core.env;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/env/CommandLineArgs.class */
class CommandLineArgs {
    private final Map<String, List<String>> optionArgs = new HashMap();
    private final List<String> nonOptionArgs = new ArrayList();

    CommandLineArgs() {
    }

    public void addOptionArg(String optionName, String optionValue) {
        if (!this.optionArgs.containsKey(optionName)) {
            this.optionArgs.put(optionName, new ArrayList());
        }
        if (optionValue != null) {
            this.optionArgs.get(optionName).add(optionValue);
        }
    }

    public Set<String> getOptionNames() {
        return Collections.unmodifiableSet(this.optionArgs.keySet());
    }

    public boolean containsOption(String optionName) {
        return this.optionArgs.containsKey(optionName);
    }

    public List<String> getOptionValues(String optionName) {
        return this.optionArgs.get(optionName);
    }

    public void addNonOptionArg(String value) {
        this.nonOptionArgs.add(value);
    }

    public List<String> getNonOptionArgs() {
        return Collections.unmodifiableList(this.nonOptionArgs);
    }
}
