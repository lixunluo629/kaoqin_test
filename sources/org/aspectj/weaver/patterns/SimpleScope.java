package org.aspectj.weaver.patterns;

import org.apache.xmlbeans.XmlErrorCodes;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.SourceLocation;
import org.aspectj.weaver.IHasPosition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/SimpleScope.class */
public class SimpleScope implements IScope {
    private static final String[] NoStrings = new String[0];
    private static final String[] javaLangPrefixArray = {"java.lang."};
    private String[] importedPrefixes = javaLangPrefixArray;
    private String[] importedNames = NoStrings;
    private World world;
    private ResolvedType enclosingType;
    protected FormalBinding[] bindings;

    public SimpleScope(World world, FormalBinding[] bindings) {
        this.world = world;
        this.bindings = bindings;
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public UnresolvedType lookupType(String name, IHasPosition location) {
        for (int i = 0; i < this.importedNames.length; i++) {
            String importedName = this.importedNames[i];
            if (importedName.endsWith(name)) {
                return this.world.resolve(importedName);
            }
        }
        if (name.length() < 8 && Character.isLowerCase(name.charAt(0))) {
            int len = name.length();
            if (len == 3) {
                if (name.equals(XmlErrorCodes.INT)) {
                    return UnresolvedType.INT;
                }
            } else if (len == 4) {
                if (name.equals("void")) {
                    return UnresolvedType.VOID;
                }
                if (name.equals("byte")) {
                    return UnresolvedType.BYTE;
                }
                if (name.equals("char")) {
                    return UnresolvedType.CHAR;
                }
                if (name.equals(XmlErrorCodes.LONG)) {
                    return UnresolvedType.LONG;
                }
            } else if (len == 5) {
                if (name.equals(XmlErrorCodes.FLOAT)) {
                    return UnresolvedType.FLOAT;
                }
                if (name.equals("short")) {
                    return UnresolvedType.SHORT;
                }
            } else if (len == 6) {
                if (name.equals(XmlErrorCodes.DOUBLE)) {
                    return UnresolvedType.DOUBLE;
                }
            } else if (len == 7 && name.equals("boolean")) {
                return UnresolvedType.BOOLEAN;
            }
        }
        if (name.indexOf(46) != -1) {
            return this.world.resolve(UnresolvedType.forName(name), true);
        }
        for (String importedPrefix : this.importedPrefixes) {
            ResolvedType tryType = this.world.resolve(UnresolvedType.forName(importedPrefix + name), true);
            if (!tryType.isMissing()) {
                return tryType;
            }
        }
        return this.world.resolve(UnresolvedType.forName(name), true);
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public IMessageHandler getMessageHandler() {
        return this.world.getMessageHandler();
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public FormalBinding lookupFormal(String name) {
        int len = this.bindings.length;
        for (int i = 0; i < len; i++) {
            if (this.bindings[i].getName().equals(name)) {
                return this.bindings[i];
            }
        }
        return null;
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public FormalBinding getFormal(int i) {
        return this.bindings[i];
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public int getFormalCount() {
        return this.bindings.length;
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public String[] getImportedNames() {
        return this.importedNames;
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public String[] getImportedPrefixes() {
        return this.importedPrefixes;
    }

    public void setImportedNames(String[] importedNames) {
        this.importedNames = importedNames;
    }

    public void setImportedPrefixes(String[] importedPrefixes) {
        this.importedPrefixes = importedPrefixes;
    }

    public static FormalBinding[] makeFormalBindings(UnresolvedType[] types, String[] names) {
        int len = types.length;
        FormalBinding[] bindings = new FormalBinding[len];
        for (int i = 0; i < len; i++) {
            bindings[i] = new FormalBinding(types[i], names[i], i);
        }
        return bindings;
    }

    public ISourceLocation makeSourceLocation(IHasPosition location) {
        return new SourceLocation(ISourceLocation.NO_FILE, 0);
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public void message(IMessage.Kind kind, IHasPosition location1, IHasPosition location2, String message) throws AbortException {
        message(kind, location1, message);
        message(kind, location2, message);
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public void message(IMessage.Kind kind, IHasPosition location, String message) throws AbortException {
        getMessageHandler().handleMessage(new Message(message, kind, (Throwable) null, makeSourceLocation(location)));
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public void message(IMessage aMessage) throws AbortException {
        getMessageHandler().handleMessage(aMessage);
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public World getWorld() {
        return this.world;
    }

    @Override // org.aspectj.weaver.patterns.IScope
    public ResolvedType getEnclosingType() {
        return this.enclosingType;
    }
}
