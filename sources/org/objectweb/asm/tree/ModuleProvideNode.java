package org.objectweb.asm.tree;

import java.util.List;
import org.objectweb.asm.ModuleVisitor;

/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/tree/ModuleProvideNode.SCL.lombok */
public class ModuleProvideNode {
    public String service;
    public List<String> providers;

    public ModuleProvideNode(String service, List<String> providers) {
        this.service = service;
        this.providers = providers;
    }

    public void accept(ModuleVisitor moduleVisitor) {
        moduleVisitor.visitProvide(this.service, (String[]) this.providers.toArray(new String[this.providers.size()]));
    }
}
