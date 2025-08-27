package org.springframework.boot;

import java.util.List;
import java.util.Set;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/ApplicationArguments.class */
public interface ApplicationArguments {
    String[] getSourceArgs();

    Set<String> getOptionNames();

    boolean containsOption(String str);

    List<String> getOptionValues(String str);

    List<String> getNonOptionArgs();
}
