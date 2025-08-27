package org.aspectj.weaver.patterns;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.weaver.IHasPosition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeVariable;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.UnresolvedTypeVariableReferenceType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ScopeWithTypeVariables.class */
public class ScopeWithTypeVariables implements IScope {
    private IScope delegateScope;
    private String[] typeVariableNames;
    private UnresolvedTypeVariableReferenceType[] typeVarTypeXs;

    public ScopeWithTypeVariables(String[] typeVarNames, IScope delegate) {
        this.delegateScope = delegate;
        this.typeVariableNames = typeVarNames;
        this.typeVarTypeXs = new UnresolvedTypeVariableReferenceType[typeVarNames.length];
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public UnresolvedType lookupType(String name, IHasPosition location) {
        for (int i = 0; i < this.typeVariableNames.length; i++) {
            if (this.typeVariableNames[i].equals(name)) {
                if (this.typeVarTypeXs[i] == null) {
                    this.typeVarTypeXs[i] = new UnresolvedTypeVariableReferenceType(new TypeVariable(name));
                }
                return this.typeVarTypeXs[i];
            }
        }
        return this.delegateScope.lookupType(name, location);
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public World getWorld() {
        return this.delegateScope.getWorld();
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public ResolvedType getEnclosingType() {
        return this.delegateScope.getEnclosingType();
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public IMessageHandler getMessageHandler() {
        return this.delegateScope.getMessageHandler();
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public FormalBinding lookupFormal(String name) {
        return this.delegateScope.lookupFormal(name);
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public FormalBinding getFormal(int i) {
        return this.delegateScope.getFormal(i);
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public int getFormalCount() {
        return this.delegateScope.getFormalCount();
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public String[] getImportedPrefixes() {
        return this.delegateScope.getImportedPrefixes();
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public String[] getImportedNames() {
        return this.delegateScope.getImportedNames();
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public void message(IMessage.Kind kind, IHasPosition location, String message) {
        this.delegateScope.message(kind, location, message);
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public void message(IMessage.Kind kind, IHasPosition location1, IHasPosition location2, String message) {
        this.delegateScope.message(kind, location1, location2, message);
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public void message(IMessage aMessage) {
        this.delegateScope.message(aMessage);
    }
}
