package org.springframework.boot.autoconfigure.data.neo4j;

import java.lang.annotation.Annotation;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.config.Neo4jRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/neo4j/Neo4jRepositoriesAutoConfigureRegistrar.class */
class Neo4jRepositoriesAutoConfigureRegistrar extends AbstractRepositoryConfigurationSourceSupport {
    Neo4jRepositoriesAutoConfigureRegistrar() {
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<? extends Annotation> getAnnotation() {
        return EnableNeo4jRepositories.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<?> getConfiguration() {
        return EnableNeo4jRepositoriesConfiguration.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new Neo4jRepositoryConfigurationExtension();
    }

    @EnableNeo4jRepositories
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/neo4j/Neo4jRepositoriesAutoConfigureRegistrar$EnableNeo4jRepositoriesConfiguration.class */
    private static class EnableNeo4jRepositoriesConfiguration {
        private EnableNeo4jRepositoriesConfiguration() {
        }
    }
}
