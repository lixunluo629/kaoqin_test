package org.terracotta.context;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.context.extractor.ObjectContextExtractor;
import org.terracotta.context.query.Query;
import org.terracotta.context.query.QueryBuilder;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/ContextManager.class */
public class ContextManager {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ContextManager.class);
    private static final WeakIdentityHashMap<Object, MutableTreeNode> CONTEXT_OBJECTS = new WeakIdentityHashMap<>();
    private static final Collection<ContextCreationListener> contextCreationListeners = new CopyOnWriteArrayList();
    private final RootNode root = new RootNode();

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/context/ContextManager$Association.class */
    public interface Association {
        Association withChild(Object obj);

        Association withParent(Object obj);
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/context/ContextManager$Dissociation.class */
    public interface Dissociation {
        Dissociation fromChild(Object obj);

        Dissociation fromParent(Object obj);
    }

    public static Association associate(final Object object) {
        return new Association() { // from class: org.terracotta.context.ContextManager.1
            @Override // org.terracotta.context.ContextManager.Association
            public Association withChild(Object child) {
                ContextManager.associate(child, object);
                return this;
            }

            @Override // org.terracotta.context.ContextManager.Association
            public Association withParent(Object parent) {
                ContextManager.associate(object, parent);
                return this;
            }
        };
    }

    public static Dissociation dissociate(final Object object) {
        return new Dissociation() { // from class: org.terracotta.context.ContextManager.2
            @Override // org.terracotta.context.ContextManager.Dissociation
            public Dissociation fromChild(Object child) {
                ContextManager.dissociate(child, object);
                return this;
            }

            @Override // org.terracotta.context.ContextManager.Dissociation
            public Dissociation fromParent(Object parent) {
                ContextManager.dissociate(object, parent);
                return this;
            }
        };
    }

    public static TreeNode nodeFor(Object object) {
        TreeNode node = getTreeNode(object);
        if (node == null) {
            return null;
        }
        return new ContextAwareTreeNode(node, object);
    }

    public static void registerContextCreationListener(ContextCreationListener listener) {
        contextCreationListeners.add(listener);
    }

    public static void deregisterContextCreationListener(ContextCreationListener listener) {
        contextCreationListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void associate(Object child, Object parent) {
        getOrCreateTreeNode(parent).addChild(getOrCreateTreeNode(child));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dissociate(Object child, Object parent) {
        getTreeNode(parent).removeChild(getTreeNode(child));
    }

    private static MutableTreeNode getTreeNode(Object object) {
        return CONTEXT_OBJECTS.get(object);
    }

    private static MutableTreeNode getOrCreateTreeNode(Object object) throws IllegalAccessException, IllegalArgumentException {
        MutableTreeNode node = CONTEXT_OBJECTS.get(object);
        if (node == null) {
            ContextElement context = ObjectContextExtractor.extract(object);
            MutableTreeNode node2 = new MutableTreeNode(context);
            MutableTreeNode racer = CONTEXT_OBJECTS.putIfAbsent(object, node2);
            if (racer != null) {
                return racer;
            }
            discoverAssociations(object);
            contextCreated(object);
            return node2;
        }
        return node;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00ac A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void discoverAssociations(java.lang.Object r5) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException {
        /*
            r0 = r5
            java.lang.Class r0 = r0.getClass()
            r6 = r0
        L5:
            r0 = r6
            if (r0 == 0) goto Lba
            r0 = r6
            java.lang.reflect.Field[] r0 = r0.getDeclaredFields()
            r7 = r0
            r0 = r7
            int r0 = r0.length
            r8 = r0
            r0 = 0
            r9 = r0
        L14:
            r0 = r9
            r1 = r8
            if (r0 >= r1) goto Lb2
            r0 = r7
            r1 = r9
            r0 = r0[r1]
            r10 = r0
            r0 = r10
            java.lang.Class<org.terracotta.context.annotations.ContextChild> r1 = org.terracotta.context.annotations.ContextChild.class
            boolean r0 = r0.isAnnotationPresent(r1)
            if (r0 == 0) goto L66
            r0 = r10
            r1 = 1
            r0.setAccessible(r1)
            r0 = r10
            r1 = r5
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.IllegalArgumentException -> L3c java.lang.IllegalAccessException -> L48
            r11 = r0
            goto L5b
        L3c:
            r12 = move-exception
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r1 = r0
            r2 = r12
            r1.<init>(r2)
            throw r0
        L48:
            r12 = move-exception
            org.slf4j.Logger r0 = org.terracotta.context.ContextManager.LOGGER
            java.lang.String r1 = "Failed to traverse {} due to: {}"
            r2 = r10
            r3 = r12
            r0.warn(r1, r2, r3)
            goto Lac
        L5b:
            r0 = r11
            if (r0 == 0) goto L66
            r0 = r11
            r1 = r5
            associate(r0, r1)
        L66:
            r0 = r10
            java.lang.Class<org.terracotta.context.annotations.ContextParent> r1 = org.terracotta.context.annotations.ContextParent.class
            boolean r0 = r0.isAnnotationPresent(r1)
            if (r0 == 0) goto Lac
            r0 = r10
            r1 = 1
            r0.setAccessible(r1)
            r0 = r10
            r1 = r5
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.IllegalArgumentException -> L82 java.lang.IllegalAccessException -> L8e
            r11 = r0
            goto La1
        L82:
            r12 = move-exception
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r1 = r0
            r2 = r12
            r1.<init>(r2)
            throw r0
        L8e:
            r12 = move-exception
            org.slf4j.Logger r0 = org.terracotta.context.ContextManager.LOGGER
            java.lang.String r1 = "Failed to traverse {} due to: {}"
            r2 = r10
            r3 = r12
            r0.warn(r1, r2, r3)
            goto Lac
        La1:
            r0 = r11
            if (r0 == 0) goto Lac
            r0 = r5
            r1 = r11
            associate(r0, r1)
        Lac:
            int r9 = r9 + 1
            goto L14
        Lb2:
            r0 = r6
            java.lang.Class r0 = r0.getSuperclass()
            r6 = r0
            goto L5
        Lba:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.terracotta.context.ContextManager.discoverAssociations(java.lang.Object):void");
    }

    private static void contextCreated(Object object) {
        for (ContextCreationListener listener : contextCreationListeners) {
            listener.contextCreated(object);
        }
    }

    public void root(Object object) {
        this.root.addChild(getOrCreateTreeNode(object));
    }

    public void uproot(Object object) {
        this.root.removeChild(getTreeNode(object));
    }

    public Set<TreeNode> query(Query query) {
        return query.execute(Collections.singleton(this.root));
    }

    public TreeNode queryForSingleton(Query query) throws IllegalStateException {
        return query(QueryBuilder.queryBuilder().chain(query).ensureUnique().build()).iterator().next();
    }

    public void registerContextListener(ContextListener listener) {
        this.root.addListener(listener);
    }

    public void deregisterContextListener(ContextListener listener) {
        this.root.removeListener(listener);
    }
}
