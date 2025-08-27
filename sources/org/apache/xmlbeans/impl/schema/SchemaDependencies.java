package org.apache.xmlbeans.impl.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaDependencies.class */
public class SchemaDependencies {
    private Map _dependencies = new HashMap();
    private Map _contributions = new HashMap();

    void registerDependency(String source, String target) {
        Set depSet = (Set) this._dependencies.get(target);
        if (depSet == null) {
            depSet = new HashSet();
            this._dependencies.put(target, depSet);
        }
        depSet.add(source);
    }

    Set computeTransitiveClosure(List modifiedNamespaces) {
        List nsList = new ArrayList(modifiedNamespaces);
        Set result = new HashSet(modifiedNamespaces);
        for (int i = 0; i < nsList.size(); i++) {
            Set<String> deps = (Set) this._dependencies.get(nsList.get(i));
            if (deps != null) {
                for (String ns : deps) {
                    if (!result.contains(ns)) {
                        nsList.add(ns);
                        result.add(ns);
                    }
                }
            }
        }
        return result;
    }

    SchemaDependencies() {
    }

    SchemaDependencies(SchemaDependencies base, Set updatedNs) {
        for (String target : base._dependencies.keySet()) {
            if (!updatedNs.contains(target)) {
                HashSet hashSet = new HashSet();
                this._dependencies.put(target, hashSet);
                Set<String> baseDepSet = (Set) base._dependencies.get(target);
                for (String source : baseDepSet) {
                    if (!updatedNs.contains(source)) {
                        hashSet.add(source);
                    }
                }
            }
        }
        for (String ns : base._contributions.keySet()) {
            if (!updatedNs.contains(ns)) {
                ArrayList arrayList = new ArrayList();
                this._contributions.put(ns, arrayList);
                List baseFileList = (List) base._contributions.get(ns);
                Iterator it2 = baseFileList.iterator();
                while (it2.hasNext()) {
                    arrayList.add(it2.next());
                }
            }
        }
    }

    void registerContribution(String ns, String fileURL) {
        List fileList = (List) this._contributions.get(ns);
        if (fileList == null) {
            fileList = new ArrayList();
            this._contributions.put(ns, fileList);
        }
        fileList.add(fileURL);
    }

    boolean isFileRepresented(String fileURL) {
        for (List fileList : this._contributions.values()) {
            if (fileList.contains(fileURL)) {
                return true;
            }
        }
        return false;
    }

    List getFilesTouched(Set updatedNs) {
        List result = new ArrayList();
        Iterator it = updatedNs.iterator();
        while (it.hasNext()) {
            result.addAll((List) this._contributions.get(it.next()));
        }
        return result;
    }

    List getNamespacesTouched(Set modifiedFiles) {
        List result = new ArrayList();
        for (String ns : this._contributions.keySet()) {
            List files = (List) this._contributions.get(ns);
            for (int i = 0; i < files.size(); i++) {
                if (modifiedFiles.contains(files.get(i))) {
                    result.add(ns);
                }
            }
        }
        return result;
    }
}
