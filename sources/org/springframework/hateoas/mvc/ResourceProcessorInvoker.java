package org.springframework.hateoas.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ResourceProcessorInvoker.class */
public class ResourceProcessorInvoker {
    private final List<ProcessorWrapper> processors;

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ResourceProcessorInvoker$ProcessorWrapper.class */
    private interface ProcessorWrapper extends Ordered {
        boolean supports(ResolvableType resolvableType, Object obj);

        Object invokeProcessor(Object obj);
    }

    public ResourceProcessorInvoker(Collection<ResourceProcessor<?>> processors) {
        Assert.notNull(processors, "ResourceProcessors must not be null!");
        this.processors = new ArrayList();
        for (ResourceProcessor<?> processor : processors) {
            ResolvableType processorType = ResolvableType.forClass(ResourceProcessor.class, processor.getClass());
            Class<?> rawType = processorType.getGeneric(0).resolve();
            if (Resource.class.isAssignableFrom(rawType)) {
                this.processors.add(new ResourceProcessorWrapper(processor));
            } else if (Resources.class.isAssignableFrom(rawType)) {
                this.processors.add(new ResourcesProcessorWrapper(processor));
            } else {
                this.processors.add(new DefaultProcessorWrapper(processor));
            }
        }
        Collections.sort(this.processors, AnnotationAwareOrderComparator.INSTANCE);
    }

    public <T extends ResourceSupport> T invokeProcessorsFor(T t) {
        Assert.notNull(t, "Value must not be null!");
        return (T) invokeProcessorsFor((ResourceProcessorInvoker) t, ResolvableType.forClass(t.getClass()));
    }

    public <T extends ResourceSupport> T invokeProcessorsFor(T value, ResolvableType referenceType) throws IllegalAccessException, IllegalArgumentException {
        Assert.notNull(value, "Value must not be null!");
        Assert.notNull(referenceType, "Reference type must not be null!");
        if (ResourceProcessorHandlerMethodReturnValueHandler.RESOURCES_TYPE.isAssignableFrom(referenceType)) {
            Resources<?> resources = (Resources) value;
            ResolvableType elementTargetType = ResolvableType.forClass(Resources.class, referenceType.getRawClass()).getGeneric(0);
            ArrayList result = new ArrayList(resources.getContent().size());
            Iterator<?> it = resources.iterator();
            while (it.hasNext()) {
                Object element = it.next();
                ResolvableType elementType = ResolvableType.forClass(element.getClass());
                if (!getRawType(elementTargetType).equals(elementType.getRawClass())) {
                    elementTargetType = elementType;
                }
                result.add(invokeProcessorsFor(element, elementTargetType));
            }
            ReflectionUtils.setField(ResourceProcessorHandlerMethodReturnValueHandler.CONTENT_FIELD, resources, result);
        }
        return (T) invokeProcessorsFor((Object) value, referenceType);
    }

    private Object invokeProcessorsFor(Object value, ResolvableType type) {
        Object currentValue = value;
        for (ProcessorWrapper wrapper : this.processors) {
            if (wrapper.supports(type, currentValue)) {
                currentValue = wrapper.invokeProcessor(currentValue);
            }
        }
        return currentValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isRawTypeAssignable(ResolvableType left, Class<?> right) {
        return getRawType(left).isAssignableFrom(right);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Class<?> getRawType(ResolvableType type) {
        Class<?> rawType = type.getRawClass();
        return rawType == null ? Object.class : rawType;
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ResourceProcessorInvoker$DefaultProcessorWrapper.class */
    private static class DefaultProcessorWrapper implements ProcessorWrapper {
        private final ResourceProcessor<?> processor;
        private final ResolvableType targetType;

        public DefaultProcessorWrapper(ResourceProcessor<?> processor) {
            Assert.notNull(processor);
            this.processor = processor;
            this.targetType = ResolvableType.forClass(ResourceProcessor.class, processor.getClass()).getGeneric(0);
        }

        @Override // org.springframework.hateoas.mvc.ResourceProcessorInvoker.ProcessorWrapper
        public boolean supports(ResolvableType type, Object value) {
            return ResourceProcessorInvoker.isRawTypeAssignable(this.targetType, ResourceProcessorInvoker.getRawType(type));
        }

        @Override // org.springframework.hateoas.mvc.ResourceProcessorInvoker.ProcessorWrapper
        public Object invokeProcessor(Object object) {
            return this.processor.process((ResourceSupport) object);
        }

        @Override // org.springframework.core.Ordered
        public int getOrder() {
            return CustomOrderAwareComparator.INSTANCE.getOrder(this.processor);
        }

        public ResolvableType getTargetType() {
            return this.targetType;
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ResourceProcessorInvoker$ResourceProcessorWrapper.class */
    private static class ResourceProcessorWrapper extends DefaultProcessorWrapper {
        public ResourceProcessorWrapper(ResourceProcessor<?> processor) {
            super(processor);
        }

        @Override // org.springframework.hateoas.mvc.ResourceProcessorInvoker.DefaultProcessorWrapper, org.springframework.hateoas.mvc.ResourceProcessorInvoker.ProcessorWrapper
        public boolean supports(ResolvableType type, Object value) {
            return ResourceProcessorHandlerMethodReturnValueHandler.RESOURCE_TYPE.isAssignableFrom(type) && super.supports(type, value) && isValueTypeMatch((Resource) value, getTargetType());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isValueTypeMatch(Resource<?> resource, ResolvableType target) {
            Object content;
            ResolvableType type;
            return (resource == null || !ResourceProcessorInvoker.isRawTypeAssignable(target, resource.getClass()) || (content = resource.getContent()) == null || (type = findGenericType(target, Resource.class)) == null || !type.getGeneric(0).isAssignableFrom(ResolvableType.forClass(content.getClass()))) ? false : true;
        }

        private static ResolvableType findGenericType(ResolvableType source, Class<?> type) {
            Class<?> rawType = ResourceProcessorInvoker.getRawType(source);
            if (Object.class.equals(rawType)) {
                return null;
            }
            if (rawType.equals(type)) {
                return source;
            }
            return findGenericType(source.getSuperType(), type);
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ResourceProcessorInvoker$ResourcesProcessorWrapper.class */
    public static class ResourcesProcessorWrapper extends DefaultProcessorWrapper {
        @Override // org.springframework.hateoas.mvc.ResourceProcessorInvoker.DefaultProcessorWrapper
        public /* bridge */ /* synthetic */ ResolvableType getTargetType() {
            return super.getTargetType();
        }

        @Override // org.springframework.hateoas.mvc.ResourceProcessorInvoker.DefaultProcessorWrapper, org.springframework.core.Ordered
        public /* bridge */ /* synthetic */ int getOrder() {
            return super.getOrder();
        }

        @Override // org.springframework.hateoas.mvc.ResourceProcessorInvoker.DefaultProcessorWrapper, org.springframework.hateoas.mvc.ResourceProcessorInvoker.ProcessorWrapper
        public /* bridge */ /* synthetic */ Object invokeProcessor(Object obj) {
            return super.invokeProcessor(obj);
        }

        public ResourcesProcessorWrapper(ResourceProcessor<?> processor) {
            super(processor);
        }

        @Override // org.springframework.hateoas.mvc.ResourceProcessorInvoker.DefaultProcessorWrapper, org.springframework.hateoas.mvc.ResourceProcessorInvoker.ProcessorWrapper
        public boolean supports(ResolvableType type, Object value) {
            return ResourceProcessorHandlerMethodReturnValueHandler.RESOURCES_TYPE.isAssignableFrom(type) && super.supports(type, value) && isValueTypeMatch((Resources) value, getTargetType());
        }

        static boolean isValueTypeMatch(Resources<?> resources, ResolvableType target) {
            if (resources == null) {
                return false;
            }
            Collection<?> content = resources.getContent();
            if (content.isEmpty()) {
                return false;
            }
            ResolvableType superType = null;
            for (Class<?> resourcesType : Arrays.asList(resources.getClass(), Resources.class)) {
                superType = getSuperType(target, resourcesType);
                if (superType != null) {
                    break;
                }
            }
            if (superType == null) {
                return false;
            }
            Object element = content.iterator().next();
            ResolvableType resourceType = superType.getGeneric(0);
            if (element instanceof Resource) {
                return ResourceProcessorWrapper.isValueTypeMatch((Resource) element, resourceType);
            }
            if (element instanceof EmbeddedWrapper) {
                return ResourceProcessorInvoker.isRawTypeAssignable(resourceType, ((EmbeddedWrapper) element).getRelTargetType());
            }
            return false;
        }

        private static ResolvableType getSuperType(ResolvableType source, Class<?> superType) {
            if (source.getRawClass().equals(superType)) {
                return source;
            }
            ResolvableType candidate = source.getSuperType();
            if (superType.isAssignableFrom(candidate.getRawClass())) {
                return candidate;
            }
            for (ResolvableType interfaces : source.getInterfaces()) {
                if (superType.isAssignableFrom(interfaces.getRawClass())) {
                    return interfaces;
                }
            }
            return ResolvableType.forClass(superType);
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ResourceProcessorInvoker$CustomOrderAwareComparator.class */
    private static class CustomOrderAwareComparator extends AnnotationAwareOrderComparator {
        public static CustomOrderAwareComparator INSTANCE = new CustomOrderAwareComparator();

        private CustomOrderAwareComparator() {
        }

        @Override // org.springframework.core.OrderComparator
        protected int getOrder(Object obj) {
            return super.getOrder(obj);
        }
    }
}
