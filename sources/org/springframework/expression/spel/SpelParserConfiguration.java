package org.springframework.expression.spel;

import org.springframework.core.SpringProperties;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/SpelParserConfiguration.class */
public class SpelParserConfiguration {
    private static final SpelCompilerMode defaultCompilerMode;
    private final SpelCompilerMode compilerMode;
    private final ClassLoader compilerClassLoader;
    private final boolean autoGrowNullReferences;
    private final boolean autoGrowCollections;
    private final int maximumAutoGrowSize;

    static {
        String compilerMode = SpringProperties.getProperty("spring.expression.compiler.mode");
        defaultCompilerMode = compilerMode != null ? SpelCompilerMode.valueOf(compilerMode.toUpperCase()) : SpelCompilerMode.OFF;
    }

    public SpelParserConfiguration() {
        this(null, null, false, false, Integer.MAX_VALUE);
    }

    public SpelParserConfiguration(SpelCompilerMode compilerMode, ClassLoader compilerClassLoader) {
        this(compilerMode, compilerClassLoader, false, false, Integer.MAX_VALUE);
    }

    public SpelParserConfiguration(boolean autoGrowNullReferences, boolean autoGrowCollections) {
        this(null, null, autoGrowNullReferences, autoGrowCollections, Integer.MAX_VALUE);
    }

    public SpelParserConfiguration(boolean autoGrowNullReferences, boolean autoGrowCollections, int maximumAutoGrowSize) {
        this(null, null, autoGrowNullReferences, autoGrowCollections, maximumAutoGrowSize);
    }

    public SpelParserConfiguration(SpelCompilerMode compilerMode, ClassLoader compilerClassLoader, boolean autoGrowNullReferences, boolean autoGrowCollections, int maximumAutoGrowSize) {
        this.compilerMode = compilerMode != null ? compilerMode : defaultCompilerMode;
        this.compilerClassLoader = compilerClassLoader;
        this.autoGrowNullReferences = autoGrowNullReferences;
        this.autoGrowCollections = autoGrowCollections;
        this.maximumAutoGrowSize = maximumAutoGrowSize;
    }

    public SpelCompilerMode getCompilerMode() {
        return this.compilerMode;
    }

    public ClassLoader getCompilerClassLoader() {
        return this.compilerClassLoader;
    }

    public boolean isAutoGrowNullReferences() {
        return this.autoGrowNullReferences;
    }

    public boolean isAutoGrowCollections() {
        return this.autoGrowCollections;
    }

    public int getMaximumAutoGrowSize() {
        return this.maximumAutoGrowSize;
    }
}
