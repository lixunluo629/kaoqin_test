package com.itextpdf.kernel.counter;

import com.itextpdf.kernel.counter.context.GenericContext;
import com.itextpdf.kernel.counter.context.IContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/ContextManager.class */
public class ContextManager {
    private static final ContextManager instance = new ContextManager();
    private static final long SECURITY_ERROR_LOGGING_INTERVAL = 60000;
    private volatile long securityErrorLastLogged = 0;
    private final Map<String, IContext> contextMappings = new ConcurrentHashMap();

    private ContextManager() {
        registerGenericContext(Arrays.asList(NamespaceConstant.CORE_IO, NamespaceConstant.CORE_KERNEL, NamespaceConstant.CORE_LAYOUT, NamespaceConstant.CORE_BARCODES, NamespaceConstant.CORE_PDFA, NamespaceConstant.CORE_SIGN, NamespaceConstant.CORE_FORMS, NamespaceConstant.CORE_SXP, NamespaceConstant.CORE_SVG), Collections.singletonList(NamespaceConstant.ITEXT));
        registerGenericContext(Collections.singletonList(NamespaceConstant.PDF_DEBUG), Collections.singletonList(NamespaceConstant.PDF_DEBUG));
        registerGenericContext(Collections.singletonList(NamespaceConstant.PDF_HTML), Collections.singletonList(NamespaceConstant.PDF_HTML));
        registerGenericContext(Collections.singletonList(NamespaceConstant.PDF_INVOICE), Collections.singletonList(NamespaceConstant.PDF_INVOICE));
        registerGenericContext(Collections.singletonList(NamespaceConstant.PDF_SWEEP), Collections.singletonList(NamespaceConstant.PDF_SWEEP));
    }

    public static ContextManager getInstance() {
        return instance;
    }

    public IContext getContext(Class<?> clazz) {
        if (clazz != null) {
            return getContext(clazz.getName());
        }
        return null;
    }

    public IContext getContext(String className) {
        return getNamespaceMapping(getRecognisedNamespace(className));
    }

    private String getRecognisedNamespace(String className) {
        if (className != null) {
            for (String namespace : this.contextMappings.keySet()) {
                if (className.toLowerCase().startsWith(namespace)) {
                    return namespace;
                }
            }
            return null;
        }
        return null;
    }

    private IContext getNamespaceMapping(String namespace) {
        if (namespace != null) {
            return this.contextMappings.get(namespace);
        }
        return null;
    }

    private void registerGenericContext(Collection<String> namespaces, Collection<String> eventIds) {
        GenericContext context = new GenericContext(eventIds);
        for (String namespace : namespaces) {
            registerContext(namespace.toLowerCase(), context);
        }
    }

    private void registerContext(String namespace, IContext context) {
        this.contextMappings.put(namespace, context);
    }
}
