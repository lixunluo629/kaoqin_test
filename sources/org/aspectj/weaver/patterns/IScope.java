package org.aspectj.weaver.patterns;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.weaver.IHasPosition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/IScope.class */
public interface IScope {
    UnresolvedType lookupType(String str, IHasPosition iHasPosition);

    World getWorld();

    ResolvedType getEnclosingType();

    IMessageHandler getMessageHandler();

    FormalBinding lookupFormal(String str);

    FormalBinding getFormal(int i);

    int getFormalCount();

    String[] getImportedPrefixes();

    String[] getImportedNames();

    void message(IMessage.Kind kind, IHasPosition iHasPosition, String str);

    void message(IMessage.Kind kind, IHasPosition iHasPosition, IHasPosition iHasPosition2, String str);

    void message(IMessage iMessage);
}
