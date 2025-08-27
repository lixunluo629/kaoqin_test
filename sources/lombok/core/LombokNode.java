package lombok.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.core.AST;
import lombok.core.LombokNode;

/* loaded from: lombok-1.16.22.jar:lombok/core/LombokNode.SCL.lombok */
public abstract class LombokNode<A extends AST<A, L, N>, L extends LombokNode<A, L, N>, N> implements DiagnosticsReceiver {
    protected final A ast;
    protected final AST.Kind kind;
    protected final N node;
    protected LombokImmutableList<L> children;
    protected L parent;
    protected boolean isStructurallySignificant;

    protected abstract boolean calculateIsStructurallySignificant(N n);

    public abstract String getName();

    protected abstract boolean fieldContainsAnnotation(N n, N n2);

    public abstract boolean hasAnnotation(Class<? extends Annotation> cls);

    public abstract <Z extends Annotation> AnnotationValues<Z> findAnnotation(Class<Z> cls);

    public abstract boolean isStatic();

    public abstract boolean isTransient();

    public abstract boolean isEnumMember();

    public abstract int countMethodParameters();

    public abstract int getStartPos();

    protected LombokNode(A ast, N node, List<L> children, AST.Kind kind) {
        this.ast = ast;
        this.kind = kind;
        this.node = node;
        this.children = children != null ? LombokImmutableList.copyOf((Collection) children) : LombokImmutableList.of();
        Iterator<L> it = this.children.iterator();
        while (it.hasNext()) {
            L child = it.next();
            child.parent = this;
            if (!child.isStructurallySignificant) {
                child.isStructurallySignificant = calculateIsStructurallySignificant(node);
            }
        }
        this.isStructurallySignificant = calculateIsStructurallySignificant(null);
    }

    public A getAst() {
        return this.ast;
    }

    public String toString() {
        Object[] objArr = new Object[3];
        objArr[0] = this.kind;
        objArr[1] = this.node == null ? "(NULL)" : this.node.getClass();
        objArr[2] = this.node == null ? "" : this.node;
        return String.format("NODE %s (%s) %s", objArr);
    }

    public String getPackageDeclaration() {
        return this.ast.getPackageDeclaration();
    }

    public ImportList getImportList() {
        return this.ast.getImportList();
    }

    public L getNodeFor(N n) {
        return (L) this.ast.get(n);
    }

    public N get() {
        return this.node;
    }

    public AST.Kind getKind() {
        return this.kind;
    }

    public L up() {
        L result;
        L l = this.parent;
        while (true) {
            result = l;
            if (result == null || result.isStructurallySignificant) {
                break;
            }
            l = result.parent;
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Collection<L> upFromAnnotationToFields() {
        if (getKind() != AST.Kind.ANNOTATION) {
            return Collections.emptyList();
        }
        LombokNode lombokNodeUp = up();
        if (lombokNodeUp == null || lombokNodeUp.getKind() != AST.Kind.FIELD) {
            return Collections.emptyList();
        }
        LombokNode lombokNodeUp2 = lombokNodeUp.up();
        if (lombokNodeUp2 == null || lombokNodeUp2.getKind() != AST.Kind.TYPE) {
            return Collections.emptyList();
        }
        List<L> fields = new ArrayList<>();
        Iterator<L> it = lombokNodeUp2.down().iterator();
        while (it.hasNext()) {
            L potentialField = it.next();
            if (potentialField.getKind() == AST.Kind.FIELD && fieldContainsAnnotation(potentialField.get(), get())) {
                fields.add(potentialField);
            }
        }
        return fields;
    }

    public L directUp() {
        return this.parent;
    }

    public LombokImmutableList<L> down() {
        return this.children;
    }

    public int getLatestJavaSpecSupported() {
        return this.ast.getLatestJavaSpecSupported();
    }

    public int getSourceVersion() {
        return this.ast.getSourceVersion();
    }

    public L top() {
        return (L) this.ast.top();
    }

    public String getFileName() {
        return this.ast.getFileName();
    }

    public L add(N n, AST.Kind kind) {
        this.ast.setChanged();
        L l = (L) this.ast.buildTree(n, kind);
        if (l == null) {
            return null;
        }
        l.parent = this;
        this.children = this.children.append(l);
        return l;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void rebuild() {
        IdentityHashMap identityHashMap = new IdentityHashMap();
        gatherAndRemoveChildren(identityHashMap);
        LombokNode lombokNodeBuildTree = this.ast.buildTree(get(), this.kind);
        this.ast.setChanged();
        this.ast.replaceNewWithExistingOld(identityHashMap, lombokNodeBuildTree);
    }

    private void gatherAndRemoveChildren(Map<N, L> map) {
        Iterator<L> it = this.children.iterator();
        while (it.hasNext()) {
            LombokNode child = it.next();
            child.gatherAndRemoveChildren(map);
        }
        this.ast.identityDetector.remove(get());
        map.put(get(), this);
        this.children = LombokImmutableList.of();
        this.ast.getNodeMap().remove(get());
    }

    public void removeChild(L child) {
        this.ast.setChanged();
        this.children = this.children.removeElement(child);
    }

    public boolean isStructurallySignificant() {
        return this.isStructurallySignificant;
    }
}
