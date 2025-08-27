package org.springframework.boot.autoconfigure.mongo.embedded;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Feature;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.config.store.IDownloadPath;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.distribution.Platform;
import de.flapdoodle.embed.process.io.Processors;
import de.flapdoodle.embed.process.io.Slf4jLevel;
import de.flapdoodle.embed.process.io.progress.Slf4jProgressListener;
import de.flapdoodle.embed.process.runtime.Network;
import de.flapdoodle.embed.process.store.ArtifactStoreBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.catalina.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.mongo.MongoClientDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.util.Assert;

@EnableConfigurationProperties({MongoProperties.class, EmbeddedMongoProperties.class})
@AutoConfigureBefore({MongoAutoConfiguration.class})
@Configuration
@ConditionalOnClass({MongoClient.class, MongodStarter.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/embedded/EmbeddedMongoAutoConfiguration.class */
public class EmbeddedMongoAutoConfiguration {
    private static final byte[] IP4_LOOPBACK_ADDRESS = {Byte.MAX_VALUE, 0, 0, 1};
    private static final byte[] IP6_LOOPBACK_ADDRESS = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
    private final MongoProperties properties;
    private final EmbeddedMongoProperties embeddedProperties;
    private final ApplicationContext context;
    private final IRuntimeConfig runtimeConfig;

    public EmbeddedMongoAutoConfiguration(MongoProperties properties, EmbeddedMongoProperties embeddedProperties, ApplicationContext context, IRuntimeConfig runtimeConfig) {
        this.properties = properties;
        this.embeddedProperties = embeddedProperties;
        this.context = context;
        this.runtimeConfig = runtimeConfig;
    }

    @ConditionalOnMissingBean
    @Bean(initMethod = Lifecycle.START_EVENT, destroyMethod = Lifecycle.STOP_EVENT)
    public MongodExecutable embeddedMongoServer(IMongodConfig mongodConfig) throws IOException {
        Integer configuredPort = this.properties.getPort();
        if (configuredPort == null || configuredPort.intValue() == 0) {
            setEmbeddedPort(mongodConfig.net().getPort());
        }
        MongodStarter mongodStarter = getMongodStarter(this.runtimeConfig);
        return mongodStarter.prepare(mongodConfig);
    }

    private MongodStarter getMongodStarter(IRuntimeConfig runtimeConfig) {
        if (runtimeConfig == null) {
            return MongodStarter.getDefaultInstance();
        }
        return MongodStarter.getInstance(runtimeConfig);
    }

    @ConditionalOnMissingBean
    @Bean
    public IMongodConfig embeddedMongoConfiguration() throws IOException {
        IFeatureAwareVersion featureAwareVersion = new ToStringFriendlyFeatureAwareVersion(this.embeddedProperties.getVersion(), this.embeddedProperties.getFeatures());
        MongodConfigBuilder builder = new MongodConfigBuilder().version(featureAwareVersion);
        EmbeddedMongoProperties.Storage storage = this.embeddedProperties.getStorage();
        if (storage != null) {
            String databaseDir = storage.getDatabaseDir();
            String replSetName = storage.getReplSetName();
            int oplogSize = storage.getOplogSize() != null ? storage.getOplogSize().intValue() : 0;
            builder.replication(new Storage(databaseDir, replSetName, oplogSize));
        }
        Integer configuredPort = this.properties.getPort();
        if (configuredPort != null && configuredPort.intValue() > 0) {
            builder.net(new Net(getHost().getHostAddress(), configuredPort.intValue(), Network.localhostIsIPv6()));
        } else {
            builder.net(new Net(getHost().getHostAddress(), Network.getFreeServerPort(getHost()), Network.localhostIsIPv6()));
        }
        return builder.build();
    }

    private InetAddress getHost() throws UnknownHostException {
        if (this.properties.getHost() == null) {
            return InetAddress.getByAddress(Network.localhostIsIPv6() ? IP6_LOOPBACK_ADDRESS : IP4_LOOPBACK_ADDRESS);
        }
        return InetAddress.getByName(this.properties.getHost());
    }

    private void setEmbeddedPort(int port) {
        setPortProperty(this.context, port);
    }

    private void setPortProperty(ApplicationContext currentContext, int port) {
        if (currentContext instanceof ConfigurableApplicationContext) {
            MutablePropertySources sources = ((ConfigurableApplicationContext) currentContext).getEnvironment().getPropertySources();
            getMongoPorts(sources).put("local.mongo.port", Integer.valueOf(port));
        }
        if (currentContext.getParent() != null) {
            setPortProperty(currentContext.getParent(), port);
        }
    }

    private Map<String, Object> getMongoPorts(MutablePropertySources sources) {
        PropertySource<?> propertySource = sources.get("mongo.ports");
        if (propertySource == null) {
            propertySource = new MapPropertySource("mongo.ports", new HashMap());
            sources.addFirst(propertySource);
        }
        return (Map) propertySource.getSource();
    }

    @ConditionalOnMissingBean({IRuntimeConfig.class})
    @Configuration
    @ConditionalOnClass({Logger.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/embedded/EmbeddedMongoAutoConfiguration$RuntimeConfigConfiguration.class */
    static class RuntimeConfigConfiguration {
        RuntimeConfigConfiguration() {
        }

        @Bean
        public IRuntimeConfig embeddedMongoRuntimeConfig() {
            Logger logger = LoggerFactory.getLogger(getClass().getPackage().getName() + ".EmbeddedMongo");
            ProcessOutput processOutput = new ProcessOutput(Processors.logTo(logger, Slf4jLevel.INFO), Processors.logTo(logger, Slf4jLevel.ERROR), Processors.named("[console>]", Processors.logTo(logger, Slf4jLevel.DEBUG)));
            return new RuntimeConfigBuilder().defaultsWithLogger(Command.MongoD, logger).processOutput(processOutput).artifactStore(getArtifactStore(logger)).build();
        }

        private ArtifactStoreBuilder getArtifactStore(Logger logger) {
            return new ExtractedArtifactStoreBuilder().defaults(Command.MongoD).download(new HttpsDownloadConfigBuilder().defaultsForCommand(Command.MongoD).progressListener(new Slf4jProgressListener(logger)).build());
        }
    }

    @Configuration
    @ConditionalOnClass({MongoClient.class, MongoClientFactoryBean.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/embedded/EmbeddedMongoAutoConfiguration$EmbeddedMongoDependencyConfiguration.class */
    protected static class EmbeddedMongoDependencyConfiguration extends MongoClientDependsOnBeanFactoryPostProcessor {
        public EmbeddedMongoDependencyConfiguration() {
            super("embeddedMongoServer");
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/embedded/EmbeddedMongoAutoConfiguration$ToStringFriendlyFeatureAwareVersion.class */
    private static final class ToStringFriendlyFeatureAwareVersion implements IFeatureAwareVersion {
        private final String version;
        private final Set<Feature> features;

        private ToStringFriendlyFeatureAwareVersion(String version, Set<Feature> features) {
            Assert.notNull(version, "version must not be null");
            this.version = version;
            this.features = features != null ? features : Collections.emptySet();
        }

        public String asInDownloadPath() {
            return this.version;
        }

        public boolean enabled(Feature feature) {
            return this.features.contains(feature);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                ToStringFriendlyFeatureAwareVersion other = (ToStringFriendlyFeatureAwareVersion) obj;
                boolean equals = 1 != 0 && this.features.equals(other.features);
                boolean equals2 = equals && this.version.equals(other.version);
                return equals2;
            }
            return super.equals(obj);
        }

        public int hashCode() {
            int result = (31 * 1) + this.features.hashCode();
            return (31 * result) + this.version.hashCode();
        }

        public String toString() {
            return this.version;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/embedded/EmbeddedMongoAutoConfiguration$HttpsDownloadConfigBuilder.class */
    private static class HttpsDownloadConfigBuilder extends DownloadConfigBuilder {
        private HttpsDownloadConfigBuilder() {
        }

        public DownloadConfigBuilder defaultsForCommand(Command command) {
            super.defaultsForCommand(command);
            downloadPath().setDefault(new PlatformDependentHttpsDownloadPath());
            return this;
        }

        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/embedded/EmbeddedMongoAutoConfiguration$HttpsDownloadConfigBuilder$PlatformDependentHttpsDownloadPath.class */
        private static class PlatformDependentHttpsDownloadPath implements IDownloadPath {
            private PlatformDependentHttpsDownloadPath() {
            }

            public String getPath(Distribution distribution) {
                if (distribution.getPlatform() == Platform.Windows) {
                    return "https://downloads.mongodb.org/";
                }
                return "https://fastdl.mongodb.org/";
            }
        }
    }
}
