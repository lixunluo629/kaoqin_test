package org.springframework.boot.autoconfigure.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "spring.info")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/info/ProjectInfoProperties.class */
public class ProjectInfoProperties {
    private final Build build = new Build();
    private final Git git = new Git();

    public Build getBuild() {
        return this.build;
    }

    public Git getGit() {
        return this.git;
    }

    @Autowired
    void setDefaultGitLocation(@Value("${spring.git.properties:classpath:git.properties}") Resource defaultGitLocation) {
        getGit().setLocation(defaultGitLocation);
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/info/ProjectInfoProperties$Build.class */
    public static class Build {
        private Resource location = new ClassPathResource("META-INF/build-info.properties");

        public Resource getLocation() {
            return this.location;
        }

        public void setLocation(Resource location) {
            this.location = location;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/info/ProjectInfoProperties$Git.class */
    public static class Git {
        private Resource location;

        public Resource getLocation() {
            return this.location;
        }

        public void setLocation(Resource location) {
            this.location = location;
        }
    }
}
