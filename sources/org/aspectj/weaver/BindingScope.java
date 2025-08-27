package org.aspectj.weaver;

import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.patterns.FormalBinding;
import org.aspectj.weaver.patterns.SimpleScope;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/BindingScope.class */
public class BindingScope extends SimpleScope {
    private final ResolvedType enclosingType;
    private final ISourceContext sourceContext;
    private boolean importsUpdated;

    public BindingScope(ResolvedType type, ISourceContext sourceContext, FormalBinding[] bindings) {
        super(type.getWorld(), bindings);
        this.importsUpdated = false;
        this.enclosingType = type;
        this.sourceContext = sourceContext;
    }

    @Override // org.aspectj.weaver.patterns.SimpleScope, org.aspectj.weaver.patterns.IScope
    public ResolvedType getEnclosingType() {
        return this.enclosingType;
    }

    @Override // org.aspectj.weaver.patterns.SimpleScope
    public ISourceLocation makeSourceLocation(IHasPosition location) {
        return this.sourceContext.makeSourceLocation(location);
    }

    @Override // org.aspectj.weaver.patterns.SimpleScope, org.aspectj.weaver.patterns.IScope
    public UnresolvedType lookupType(String name, IHasPosition location) {
        if (this.enclosingType != null && !this.importsUpdated) {
            String pkgName = this.enclosingType.getPackageName();
            if (pkgName != null && !pkgName.equals("")) {
                String[] existingImports = getImportedPrefixes();
                String pkgNameWithDot = pkgName.concat(".");
                boolean found = false;
                int length = existingImports.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    String existingImport = existingImports[i];
                    if (!existingImport.equals(pkgNameWithDot)) {
                        i++;
                    } else {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    String[] newImports = new String[existingImports.length + 1];
                    System.arraycopy(existingImports, 0, newImports, 0, existingImports.length);
                    newImports[existingImports.length] = pkgNameWithDot;
                    setImportedPrefixes(newImports);
                }
            }
            this.importsUpdated = true;
        }
        return super.lookupType(name, location);
    }
}
