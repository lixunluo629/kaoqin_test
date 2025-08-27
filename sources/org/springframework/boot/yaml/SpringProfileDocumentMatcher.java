package org.springframework.boot.yaml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.boot.bind.PropertySourcesPropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/yaml/SpringProfileDocumentMatcher.class */
public class SpringProfileDocumentMatcher implements YamlProcessor.DocumentMatcher {
    private String[] activeProfiles;

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/yaml/SpringProfileDocumentMatcher$ProfileType.class */
    enum ProfileType {
        POSITIVE,
        NEGATIVE
    }

    public SpringProfileDocumentMatcher() {
        this.activeProfiles = new String[0];
    }

    public SpringProfileDocumentMatcher(String... profiles) {
        this.activeProfiles = new String[0];
        addActiveProfiles(profiles);
    }

    public void addActiveProfiles(String... profiles) {
        LinkedHashSet<String> set = new LinkedHashSet<>(Arrays.asList(this.activeProfiles));
        Collections.addAll(set, profiles);
        this.activeProfiles = (String[]) set.toArray(new String[set.size()]);
    }

    @Override // org.springframework.beans.factory.config.YamlProcessor.DocumentMatcher
    public YamlProcessor.MatchStatus matches(Properties properties) {
        List<String> profiles = extractSpringProfiles(properties);
        ProfilesMatcher profilesMatcher = getProfilesMatcher();
        Set<String> negative = extractProfiles(profiles, ProfileType.NEGATIVE);
        Set<String> positive = extractProfiles(profiles, ProfileType.POSITIVE);
        if (!CollectionUtils.isEmpty(negative)) {
            if (profilesMatcher.matches(negative) == YamlProcessor.MatchStatus.FOUND) {
                return YamlProcessor.MatchStatus.NOT_FOUND;
            }
            if (CollectionUtils.isEmpty(positive)) {
                return YamlProcessor.MatchStatus.FOUND;
            }
        }
        return profilesMatcher.matches(positive);
    }

    private List<String> extractSpringProfiles(Properties properties) {
        SpringProperties springProperties = new SpringProperties();
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addFirst(new PropertiesPropertySource("profiles", properties));
        PropertyValues propertyValues = new PropertySourcesPropertyValues(propertySources);
        new RelaxedDataBinder(springProperties, "spring").bind(propertyValues);
        List<String> profiles = springProperties.getProfiles();
        return profiles;
    }

    private ProfilesMatcher getProfilesMatcher() {
        return this.activeProfiles.length != 0 ? new ActiveProfilesMatcher(new HashSet(Arrays.asList(this.activeProfiles))) : new EmptyProfilesMatcher();
    }

    private Set<String> extractProfiles(List<String> profiles, ProfileType type) {
        if (CollectionUtils.isEmpty(profiles)) {
            return null;
        }
        Set<String> extractedProfiles = new HashSet<>();
        for (String candidate : profiles) {
            ProfileType candidateType = ProfileType.POSITIVE;
            if (candidate.startsWith("!")) {
                candidateType = ProfileType.NEGATIVE;
            }
            if (candidateType == type) {
                extractedProfiles.add(type != ProfileType.POSITIVE ? candidate.substring(1) : candidate);
            }
        }
        return extractedProfiles;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/yaml/SpringProfileDocumentMatcher$ProfilesMatcher.class */
    private static abstract class ProfilesMatcher {
        protected abstract YamlProcessor.MatchStatus doMatches(Set<String> set);

        private ProfilesMatcher() {
        }

        public final YamlProcessor.MatchStatus matches(Set<String> profiles) {
            if (CollectionUtils.isEmpty(profiles)) {
                return YamlProcessor.MatchStatus.ABSTAIN;
            }
            return doMatches(profiles);
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/yaml/SpringProfileDocumentMatcher$ActiveProfilesMatcher.class */
    private static class ActiveProfilesMatcher extends ProfilesMatcher {
        private final Set<String> activeProfiles;

        ActiveProfilesMatcher(Set<String> activeProfiles) {
            super();
            this.activeProfiles = activeProfiles;
        }

        @Override // org.springframework.boot.yaml.SpringProfileDocumentMatcher.ProfilesMatcher
        protected YamlProcessor.MatchStatus doMatches(Set<String> profiles) {
            if (profiles.isEmpty()) {
                return YamlProcessor.MatchStatus.NOT_FOUND;
            }
            for (String activeProfile : this.activeProfiles) {
                if (profiles.contains(activeProfile)) {
                    return YamlProcessor.MatchStatus.FOUND;
                }
            }
            return YamlProcessor.MatchStatus.NOT_FOUND;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/yaml/SpringProfileDocumentMatcher$EmptyProfilesMatcher.class */
    private static class EmptyProfilesMatcher extends ProfilesMatcher {
        private EmptyProfilesMatcher() {
            super();
        }

        @Override // org.springframework.boot.yaml.SpringProfileDocumentMatcher.ProfilesMatcher
        public YamlProcessor.MatchStatus doMatches(Set<String> springProfiles) {
            if (springProfiles.isEmpty()) {
                return YamlProcessor.MatchStatus.FOUND;
            }
            for (String profile : springProfiles) {
                if (!StringUtils.hasText(profile)) {
                    return YamlProcessor.MatchStatus.FOUND;
                }
            }
            return YamlProcessor.MatchStatus.NOT_FOUND;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/yaml/SpringProfileDocumentMatcher$SpringProperties.class */
    static class SpringProperties {
        private List<String> profiles = new ArrayList();

        SpringProperties() {
        }

        public List<String> getProfiles() {
            return this.profiles;
        }

        public void setProfiles(List<String> profiles) {
            this.profiles = profiles;
        }
    }
}
