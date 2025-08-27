package org.springframework.data.repository.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.repository.query.EvaluationContextExtensionInformation;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.data.repository.query.spi.Function;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodExecutor;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ExtensionAwareEvaluationContextProvider.class */
public class ExtensionAwareEvaluationContextProvider implements EvaluationContextProvider, ApplicationContextAware {
    private final Map<Class<?>, EvaluationContextExtensionInformation> extensionInformationCache;
    private List<? extends EvaluationContextExtension> extensions;
    private ListableBeanFactory beanFactory;

    @Override // org.springframework.data.repository.query.EvaluationContextProvider
    public /* bridge */ /* synthetic */ EvaluationContext getEvaluationContext(Parameters parameters, Object[] objArr) {
        return getEvaluationContext((ExtensionAwareEvaluationContextProvider) parameters, objArr);
    }

    public ExtensionAwareEvaluationContextProvider() {
        this.extensionInformationCache = new HashMap();
        this.extensions = null;
    }

    public ExtensionAwareEvaluationContextProvider(List<? extends EvaluationContextExtension> extensions) {
        this.extensionInformationCache = new HashMap();
        Assert.notNull(extensions, "List of EvaluationContextExtensions must not be null!");
        this.extensions = extensions;
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanFactory = applicationContext;
    }

    @Override // org.springframework.data.repository.query.EvaluationContextProvider
    public <T extends Parameters<?, ?>> StandardEvaluationContext getEvaluationContext(T parameters, Object[] parameterValues) {
        StandardEvaluationContext ec = new StandardEvaluationContext();
        if (this.beanFactory != null) {
            ec.setBeanResolver(new BeanFactoryResolver(this.beanFactory));
        }
        ExtensionAwarePropertyAccessor accessor = new ExtensionAwarePropertyAccessor(getExtensions());
        ec.addPropertyAccessor(accessor);
        ec.addPropertyAccessor(new ReflectivePropertyAccessor());
        ec.addMethodResolver(accessor);
        ec.setRootObject(parameterValues);
        ec.setVariables(collectVariables(parameters, parameterValues));
        return ec;
    }

    private <T extends Parameters<?, ?>> Map<String, Object> collectVariables(T parameters, Object[] arguments) {
        Map<String, Object> variables = new HashMap<>();
        Iterator it = parameters.iterator();
        while (it.hasNext()) {
            Parameter param = (Parameter) it.next();
            if (param.isSpecialParameter()) {
                String key = StringUtils.uncapitalize(param.getType().getSimpleName());
                Object value = arguments[param.getIndex()];
                variables.put(key, value);
            }
        }
        Iterator it2 = parameters.iterator();
        while (it2.hasNext()) {
            Parameter param2 = (Parameter) it2.next();
            if (param2.isNamedParameter()) {
                variables.put(param2.getName(), arguments[param2.getIndex()]);
            }
        }
        return variables;
    }

    private List<? extends EvaluationContextExtension> getExtensions() {
        if (this.extensions != null) {
            return this.extensions;
        }
        if (this.beanFactory == null) {
            this.extensions = Collections.emptyList();
            return this.extensions;
        }
        this.extensions = new ArrayList(this.beanFactory.getBeansOfType(EvaluationContextExtension.class, true, false).values());
        return this.extensions;
    }

    private EvaluationContextExtensionInformation getOrCreateInformation(EvaluationContextExtension extension) {
        Class<?> cls = extension.getClass();
        EvaluationContextExtensionInformation information = this.extensionInformationCache.get(cls);
        if (information != null) {
            return information;
        }
        EvaluationContextExtensionInformation information2 = new EvaluationContextExtensionInformation(cls);
        this.extensionInformationCache.put(cls, information2);
        return information2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<EvaluationContextExtensionAdapter> toAdapters(Collection<? extends EvaluationContextExtension> extensions) {
        List<EvaluationContextExtension> extensionsToSet = new ArrayList<>(extensions);
        Collections.sort(extensionsToSet, AnnotationAwareOrderComparator.INSTANCE);
        List<EvaluationContextExtensionAdapter> adapters = new ArrayList<>(extensions.size());
        for (EvaluationContextExtension extension : extensionsToSet) {
            adapters.add(new EvaluationContextExtensionAdapter(extension, getOrCreateInformation(extension)));
        }
        return adapters;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ExtensionAwareEvaluationContextProvider$ExtensionAwarePropertyAccessor.class */
    private class ExtensionAwarePropertyAccessor implements PropertyAccessor, MethodResolver {
        private final List<EvaluationContextExtensionAdapter> adapters;
        private final Map<String, EvaluationContextExtensionAdapter> adapterMap;

        public ExtensionAwarePropertyAccessor(List<? extends EvaluationContextExtension> extensions) {
            Assert.notNull(extensions, "Extensions must not be null!");
            this.adapters = ExtensionAwareEvaluationContextProvider.this.toAdapters(extensions);
            this.adapterMap = new HashMap(extensions.size());
            for (EvaluationContextExtensionAdapter adapter : this.adapters) {
                this.adapterMap.put(adapter.getExtensionId(), adapter);
            }
            Collections.reverse(this.adapters);
        }

        @Override // org.springframework.expression.PropertyAccessor
        public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
            if ((target instanceof EvaluationContextExtension) || this.adapterMap.containsKey(name)) {
                return true;
            }
            for (EvaluationContextExtensionAdapter extension : this.adapters) {
                if (extension.getProperties().containsKey(name)) {
                    return true;
                }
            }
            return false;
        }

        @Override // org.springframework.expression.PropertyAccessor
        public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
            if (target instanceof EvaluationContextExtensionAdapter) {
                return lookupPropertyFrom((EvaluationContextExtensionAdapter) target, name);
            }
            if (this.adapterMap.containsKey(name)) {
                return new TypedValue(this.adapterMap.get(name));
            }
            for (EvaluationContextExtensionAdapter extension : this.adapters) {
                Map<String, Object> properties = extension.getProperties();
                if (properties.containsKey(name)) {
                    return lookupPropertyFrom(extension, name);
                }
            }
            return null;
        }

        @Override // org.springframework.expression.MethodResolver
        public MethodExecutor resolve(EvaluationContext context, Object target, String name, List<TypeDescriptor> argumentTypes) throws AccessException {
            if (target instanceof EvaluationContextExtensionAdapter) {
                return getMethodExecutor((EvaluationContextExtensionAdapter) target, name, argumentTypes);
            }
            for (EvaluationContextExtensionAdapter adapter : this.adapters) {
                MethodExecutor executor = getMethodExecutor(adapter, name, argumentTypes);
                if (executor != null) {
                    return executor;
                }
            }
            return null;
        }

        @Override // org.springframework.expression.PropertyAccessor
        public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
            return false;
        }

        @Override // org.springframework.expression.PropertyAccessor
        public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
        }

        @Override // org.springframework.expression.PropertyAccessor
        public Class<?>[] getSpecificTargetClasses() {
            return null;
        }

        private MethodExecutor getMethodExecutor(EvaluationContextExtensionAdapter adapter, String name, List<TypeDescriptor> argumentTypes) {
            Map<String, Function> functions = adapter.getFunctions();
            if (!functions.containsKey(name)) {
                return null;
            }
            Function function = functions.get(name);
            if (!function.supports(argumentTypes)) {
                return null;
            }
            return new FunctionMethodExecutor(function);
        }

        private TypedValue lookupPropertyFrom(EvaluationContextExtensionAdapter extension, String name) {
            Object value = extension.getProperties().get(name);
            if (!(value instanceof Function)) {
                return new TypedValue(value);
            }
            Function function = (Function) value;
            try {
                return new TypedValue(function.invoke(new Object[0]));
            } catch (Exception e) {
                throw new SpelEvaluationException(e, SpelMessage.FUNCTION_REFERENCE_CANNOT_BE_INVOKED, name, function.getDeclaringClass());
            }
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ExtensionAwareEvaluationContextProvider$FunctionMethodExecutor.class */
    private static class FunctionMethodExecutor implements MethodExecutor {
        private final Function function;

        public FunctionMethodExecutor(Function function) {
            this.function = function;
        }

        @Override // org.springframework.expression.MethodExecutor
        public TypedValue execute(EvaluationContext context, Object target, Object... arguments) throws AccessException {
            try {
                return new TypedValue(this.function.invoke(arguments));
            } catch (Exception e) {
                throw new SpelEvaluationException(e, SpelMessage.FUNCTION_REFERENCE_CANNOT_BE_INVOKED, this.function.getName(), this.function.getDeclaringClass());
            }
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ExtensionAwareEvaluationContextProvider$EvaluationContextExtensionAdapter.class */
    private static class EvaluationContextExtensionAdapter {
        private final EvaluationContextExtension extension;
        private final Map<String, Function> functions;
        private final Map<String, Object> properties;

        public EvaluationContextExtensionAdapter(EvaluationContextExtension extension, EvaluationContextExtensionInformation information) {
            Assert.notNull(extension, "Extenstion must not be null!");
            Assert.notNull(information, "Extension information must not be null!");
            Object target = extension.getRootObject();
            EvaluationContextExtensionInformation.ExtensionTypeInformation extensionTypeInformation = information.getExtensionTypeInformation();
            EvaluationContextExtensionInformation.RootObjectInformation rootObjectInformation = information.getRootObjectInformation(target);
            this.functions = new HashMap();
            this.functions.putAll(extensionTypeInformation.getFunctions());
            this.functions.putAll(rootObjectInformation.getFunctions(target));
            this.functions.putAll(extension.getFunctions());
            this.properties = new HashMap();
            this.properties.putAll(extensionTypeInformation.getProperties());
            this.properties.putAll(rootObjectInformation.getProperties(target));
            this.properties.putAll(extension.getProperties());
            this.extension = extension;
        }

        public String getExtensionId() {
            return this.extension.getExtensionId();
        }

        public Map<String, Function> getFunctions() {
            return this.functions;
        }

        public Map<String, Object> getProperties() {
            return this.properties;
        }
    }
}
