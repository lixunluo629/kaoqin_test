package org.terracotta.statistics;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import org.apache.ibatis.ognl.OgnlContext;
import org.terracotta.context.ContextCreationListener;
import org.terracotta.context.ContextElement;
import org.terracotta.context.ContextManager;
import org.terracotta.context.TreeNode;
import org.terracotta.statistics.observer.OperationObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/StatisticsManager.class */
public class StatisticsManager extends ContextManager {
    static {
        ContextManager.registerContextCreationListener(new ContextCreationListener() { // from class: org.terracotta.statistics.StatisticsManager.1
            @Override // org.terracotta.context.ContextCreationListener
            public void contextCreated(Object object) throws SecurityException {
                StatisticsManager.parseStatisticAnnotations(object);
            }
        });
    }

    public static <T extends Enum<T>> OperationObserver<T> createOperationStatistic(Object context, String name, Set<String> tags, Class<T> eventTypes) {
        return createOperationStatistic(context, name, tags, Collections.emptyMap(), eventTypes);
    }

    public static <T extends Enum<T>> OperationObserver<T> createOperationStatistic(Object context, String name, Set<String> tags, Map<String, ? extends Object> properties, Class<T> resultType) {
        OperationStatistic<T> stat = createOperationStatistic(name, tags, properties, resultType);
        associate(context).withChild(stat);
        return stat;
    }

    private static <T extends Enum<T>> OperationStatistic<T> createOperationStatistic(String name, Set<String> tags, Map<String, ? extends Object> properties, Class<T> resultType) {
        return new GeneralOperationStatistic(name, tags, properties, resultType);
    }

    public static <T extends Enum<T>> OperationStatistic<T> getOperationStatisticFor(OperationObserver<T> observer) {
        TreeNode node = ContextManager.nodeFor(observer);
        if (node == null) {
            return null;
        }
        ContextElement context = node.getContext();
        if (OperationStatistic.class.isAssignableFrom(context.identifier())) {
            return (OperationStatistic) context.attributes().get(OgnlContext.THIS_CONTEXT_KEY);
        }
        throw new AssertionError();
    }

    public static <T extends Number> void createPassThroughStatistic(Object context, String name, Set<String> tags, Callable<T> source) {
        createPassThroughStatistic(context, name, tags, Collections.emptyMap(), source);
    }

    public static <T extends Number> void createPassThroughStatistic(Object context, String name, Set<String> tags, Map<String, ? extends Object> properties, Callable<T> source) {
        PassThroughStatistic<T> stat = new PassThroughStatistic<>(context, name, tags, properties, source);
        associate(context).withChild(stat);
    }

    public static void removePassThroughStatistics(Object context) {
        PassThroughStatistic.removeStatistics(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void parseStatisticAnnotations(Object object) throws SecurityException {
        Method[] arr$ = object.getClass().getMethods();
        for (Method m : arr$) {
            Statistic anno = (Statistic) m.getAnnotation(Statistic.class);
            if (anno != null) {
                Class<?> returnType = m.getReturnType();
                if (m.getParameterTypes().length != 0) {
                    throw new IllegalArgumentException("Statistic methods must be no-arg: " + m);
                }
                if (!Number.class.isAssignableFrom(returnType) && (!m.getReturnType().isPrimitive() || m.getReturnType().equals(Boolean.TYPE))) {
                    throw new IllegalArgumentException("Statistic methods must return a Number: " + m);
                }
                if (Modifier.isStatic(m.getModifiers())) {
                    throw new IllegalArgumentException("Statistic methods must be non-static: " + m);
                }
                createPassThroughStatistic(object, anno.name(), new HashSet(Arrays.asList(anno.tags())), new MethodCallable(object, m));
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/StatisticsManager$MethodCallable.class */
    static class MethodCallable<T> implements Callable<T> {
        private final WeakReference<Object> targetRef;
        private final Method method;

        MethodCallable(Object target, Method method) {
            this.targetRef = new WeakReference<>(target);
            this.method = method;
        }

        @Override // java.util.concurrent.Callable
        public T call() throws Exception {
            return (T) this.method.invoke(this.targetRef.get(), new Object[0]);
        }
    }
}
