package org.aspectj.asm;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.aspectj.asm.IProgramElement;
import org.aspectj.asm.IRelationship;
import org.aspectj.asm.internal.AspectJElementHierarchy;
import org.aspectj.asm.internal.HandleProviderDelimiter;
import org.aspectj.asm.internal.JDTLikeHandleProvider;
import org.aspectj.asm.internal.RelationshipMap;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.util.IStructureModel;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/AsmManager.class */
public class AsmManager implements IStructureModel {
    public static AsmManager lastActiveStructureModel;
    protected IHierarchy hierarchy;
    protected Map<File, String> inpathMap;
    private IRelationshipMap mapper;
    private IElementHandleProvider handleProvider;
    public static boolean recordingLastActiveStructureModel = true;
    public static boolean forceSingletonBehaviour = false;
    public static boolean attemptIncrementalModelRepairs = false;
    public static boolean dumpModelPostBuild = false;
    private static boolean dumpModel = false;
    private static boolean dumpRelationships = false;
    private static boolean dumpDeltaProcessing = false;
    private static IModelFilter modelFilter = null;
    private static String dumpFilename = "";
    private static boolean reporting = false;
    private static boolean completingTypeBindings = false;
    private final List<IHierarchyListener> structureListeners = new ArrayList();
    private final CanonicalFilePathMap canonicalFilePathMap = new CanonicalFilePathMap();
    private final Set<File> lastBuildChanges = new HashSet();
    final Set<File> aspectsWeavingInLastBuild = new HashSet();

    private AsmManager() {
    }

    public static AsmManager createNewStructureModel(Map<File, String> inpathMap) {
        if (forceSingletonBehaviour && lastActiveStructureModel != null) {
            return lastActiveStructureModel;
        }
        AsmManager asm = new AsmManager();
        asm.inpathMap = inpathMap;
        asm.hierarchy = new AspectJElementHierarchy(asm);
        asm.mapper = new RelationshipMap();
        asm.handleProvider = new JDTLikeHandleProvider(asm);
        asm.handleProvider.initialize();
        asm.resetDeltaProcessing();
        setLastActiveStructureModel(asm);
        return asm;
    }

    public IHierarchy getHierarchy() {
        return this.hierarchy;
    }

    public IRelationshipMap getRelationshipMap() {
        return this.mapper;
    }

    public void fireModelUpdated() throws IOException {
        notifyListeners();
        if (dumpModelPostBuild && this.hierarchy.getConfigFile() != null) {
            writeStructureModel(this.hierarchy.getConfigFile());
        }
    }

    public HashMap<Integer, List<IProgramElement>> getInlineAnnotations(String sourceFile, boolean showSubMember, boolean showMemberAndType) {
        if (!this.hierarchy.isValid()) {
            return null;
        }
        HashMap<Integer, List<IProgramElement>> annotations = new HashMap<>();
        IProgramElement node = this.hierarchy.findElementForSourceFile(sourceFile);
        if (node == IHierarchy.NO_STRUCTURE) {
            return null;
        }
        ArrayList<IProgramElement> peNodes = new ArrayList<>();
        getAllStructureChildren(node, peNodes, showSubMember, showMemberAndType);
        Iterator<IProgramElement> it = peNodes.iterator();
        while (it.hasNext()) {
            IProgramElement peNode = it.next();
            List<IProgramElement> entries = new ArrayList<>();
            entries.add(peNode);
            ISourceLocation sourceLoc = peNode.getSourceLocation();
            if (null != sourceLoc) {
                Integer hash = new Integer(sourceLoc.getLine());
                List<IProgramElement> existingEntry = annotations.get(hash);
                if (existingEntry != null) {
                    entries.addAll(existingEntry);
                }
                annotations.put(hash, entries);
            }
        }
        return annotations;
    }

    private void getAllStructureChildren(IProgramElement node, List<IProgramElement> result, boolean showSubMember, boolean showMemberAndType) {
        List<IProgramElement> children = node.getChildren();
        if (node.getChildren() == null) {
            return;
        }
        for (IProgramElement next : children) {
            List<IRelationship> rels = this.mapper.get(next);
            if (next != null && (((next.getKind() == IProgramElement.Kind.CODE && showSubMember) || (next.getKind() != IProgramElement.Kind.CODE && showMemberAndType)) && rels != null && rels.size() > 0)) {
                result.add(next);
            }
            getAllStructureChildren(next, result, showSubMember, showMemberAndType);
        }
    }

    public void addListener(IHierarchyListener listener) {
        this.structureListeners.add(listener);
    }

    public void removeStructureListener(IHierarchyListener listener) {
        this.structureListeners.remove(listener);
    }

    public void removeAllListeners() {
        this.structureListeners.clear();
    }

    private void notifyListeners() {
        for (IHierarchyListener listener : this.structureListeners) {
            listener.elementsUpdated(this.hierarchy);
        }
    }

    public IElementHandleProvider getHandleProvider() {
        return this.handleProvider;
    }

    public void setHandleProvider(IElementHandleProvider handleProvider) {
        this.handleProvider = handleProvider;
    }

    public void writeStructureModel(String configFilePath) throws IOException {
        try {
            String filePath = genExternFilePath(configFilePath);
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream s = new ObjectOutputStream(fos);
            s.writeObject(this.hierarchy);
            s.writeObject(this.mapper);
            s.flush();
            fos.flush();
            fos.close();
            s.close();
        } catch (IOException e) {
        }
    }

    public void readStructureModel(String configFilePath) {
        boolean hierarchyReadOK = false;
        try {
            try {
                try {
                    if (configFilePath == null) {
                        this.hierarchy.setRoot(IHierarchy.NO_STRUCTURE);
                    } else {
                        String filePath = genExternFilePath(configFilePath);
                        FileInputStream in = new FileInputStream(filePath);
                        ObjectInputStream s = new ObjectInputStream(in);
                        this.hierarchy = (AspectJElementHierarchy) s.readObject();
                        ((AspectJElementHierarchy) this.hierarchy).setAsmManager(this);
                        hierarchyReadOK = true;
                        this.mapper = (RelationshipMap) s.readObject();
                        s.close();
                    }
                    notifyListeners();
                } catch (FileNotFoundException e) {
                    this.hierarchy.setRoot(IHierarchy.NO_STRUCTURE);
                    notifyListeners();
                } catch (Exception e2) {
                    this.hierarchy.setRoot(IHierarchy.NO_STRUCTURE);
                    notifyListeners();
                }
            } catch (EOFException eofe) {
                if (!hierarchyReadOK) {
                    System.err.println("AsmManager: Unable to read structure model: " + configFilePath + " because of:");
                    eofe.printStackTrace();
                    this.hierarchy.setRoot(IHierarchy.NO_STRUCTURE);
                }
                notifyListeners();
            }
        } catch (Throwable th) {
            notifyListeners();
            throw th;
        }
    }

    private String genExternFilePath(String configFilePath) {
        if (configFilePath.lastIndexOf(".lst") != -1) {
            configFilePath = configFilePath.substring(0, configFilePath.lastIndexOf(".lst"));
        }
        return configFilePath + ".ajsym";
    }

    public String getCanonicalFilePath(File f) {
        return this.canonicalFilePathMap.get(f);
    }

    public CanonicalFilePathMap getCanonicalFilePathMap() {
        return this.canonicalFilePathMap;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/AsmManager$CanonicalFilePathMap.class */
    private static class CanonicalFilePathMap {
        private static final int MAX_SIZE = 4000;
        private final Map<String, String> pathMap;

        private CanonicalFilePathMap() {
            this.pathMap = new HashMap(20);
        }

        public String get(File f) throws IOException {
            String ret = this.pathMap.get(f.getPath());
            if (ret == null) {
                try {
                    ret = f.getCanonicalPath();
                } catch (IOException e) {
                    ret = f.getPath();
                }
                this.pathMap.put(f.getPath(), ret);
                if (this.pathMap.size() > MAX_SIZE) {
                    this.pathMap.clear();
                }
            }
            return ret;
        }
    }

    public static void setReporting(String filename, boolean dModel, boolean dRels, boolean dDeltaProcessing, boolean deletefile) {
        reporting = true;
        dumpModel = dModel;
        dumpRelationships = dRels;
        dumpDeltaProcessing = dDeltaProcessing;
        if (deletefile) {
            new File(filename).delete();
        }
        dumpFilename = filename;
    }

    public static void setReporting(String filename, boolean dModel, boolean dRels, boolean dDeltaProcessing, boolean deletefile, IModelFilter aFilter) {
        setReporting(filename, dModel, dRels, dDeltaProcessing, deletefile);
        modelFilter = aFilter;
    }

    public static boolean isReporting() {
        return reporting;
    }

    public static void setDontReport() {
        reporting = false;
        dumpDeltaProcessing = false;
        dumpModel = false;
        dumpRelationships = false;
    }

    public void reportModelInfo(String reasonForReport) throws IOException {
        if (!dumpModel && !dumpRelationships) {
            return;
        }
        try {
            FileWriter fw = new FileWriter(dumpFilename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            if (dumpModel) {
                bw.write("=== MODEL STATUS REPORT ========= " + reasonForReport + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                dumptree(bw, this.hierarchy.getRoot(), 0);
                bw.write("=== END OF MODEL REPORT =========\n");
            }
            if (dumpRelationships) {
                bw.write("=== RELATIONSHIPS REPORT ========= " + reasonForReport + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                dumprels(bw);
                bw.write("=== END OF RELATIONSHIPS REPORT ==\n");
            }
            Properties p = summarizeModel().getProperties();
            Enumeration<Object> pkeyenum = p.keys();
            bw.write("=== Properties of the model and relationships map =====\n");
            while (pkeyenum.hasMoreElements()) {
                String pkey = (String) pkeyenum.nextElement();
                bw.write(pkey + SymbolConstants.EQUAL_SYMBOL + p.getProperty(pkey) + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
            bw.flush();
            fw.close();
        } catch (IOException e) {
            System.err.println("InternalError: Unable to report model information:");
            e.printStackTrace();
        }
    }

    public static void dumptree(Writer w, IProgramElement node, int indent) throws IOException {
        for (int i = 0; i < indent; i++) {
            w.write(SymbolConstants.SPACE_SYMBOL);
        }
        String loc = "";
        if (node != null && node.getSourceLocation() != null) {
            loc = node.getSourceLocation().toString();
            if (modelFilter != null) {
                loc = modelFilter.processFilelocation(loc);
            }
        }
        w.write(node + "  [" + (node == null ? "null" : node.getKind().toString()) + "] " + loc + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        if (node != null) {
            for (IProgramElement child : node.getChildren()) {
                dumptree(w, child, indent + 2);
            }
        }
    }

    public static void dumptree(IProgramElement node, int indent) throws IOException {
        for (int i = 0; i < indent; i++) {
            System.out.print(SymbolConstants.SPACE_SYMBOL);
        }
        String loc = "";
        if (node != null && node.getSourceLocation() != null) {
            loc = node.getSourceLocation().toString();
        }
        System.out.println(node + "  [" + (node == null ? "null" : node.getKind().toString()) + "] " + loc);
        if (node != null) {
            for (IProgramElement child : node.getChildren()) {
                dumptree(child, indent + 2);
            }
        }
    }

    public void dumprels(Writer w) throws IOException {
        int ctr = 1;
        Set<String> entries = this.mapper.getEntries();
        for (String hid : entries) {
            List<IRelationship> rels = this.mapper.get(hid);
            for (IRelationship ir : rels) {
                List<String> targets = ir.getTargets();
                for (String thid : targets) {
                    StringBuffer sb = new StringBuffer();
                    if (modelFilter == null || modelFilter.wantsHandleIds()) {
                        int i = ctr;
                        ctr++;
                        sb.append("Hid:" + i + ":");
                    }
                    sb.append("(targets=" + targets.size() + ") " + hid + " (" + ir.getName() + ") " + thid + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                    w.write(sb.toString());
                }
            }
        }
    }

    private void dumprelsStderr(String key) {
        System.err.println("Relationships dump follows: " + key);
        int ctr = 1;
        Set<String> entries = this.mapper.getEntries();
        for (String hid : entries) {
            for (IRelationship ir : this.mapper.get(hid)) {
                List<String> targets = ir.getTargets();
                for (String thid : targets) {
                    int i = ctr;
                    ctr++;
                    System.err.println("Hid:" + i + ":(targets=" + targets.size() + ") " + hid + " (" + ir.getName() + ") " + thid);
                }
            }
        }
        System.err.println("End of relationships dump for: " + key);
    }

    public boolean removeStructureModelForFiles(Writer fw, Collection<File> files) throws IOException {
        boolean modelModified = false;
        Set<String> deletedNodes = new HashSet<>();
        for (File fileForCompilation : files) {
            String correctedPath = getCanonicalFilePath(fileForCompilation);
            IProgramElement progElem = (IProgramElement) this.hierarchy.findInFileMap(correctedPath);
            if (progElem != null) {
                if (dumpDeltaProcessing) {
                    fw.write("Deleting " + progElem + " node for file " + fileForCompilation + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                }
                removeNode(progElem);
                this.lastBuildChanges.add(fileForCompilation);
                deletedNodes.add(getCanonicalFilePath(progElem.getSourceLocation().getSourceFile()));
                if (!this.hierarchy.removeFromFileMap(correctedPath)) {
                    throw new RuntimeException("Whilst repairing model, couldn't remove entry for file: " + correctedPath + " from the filemap");
                }
                modelModified = true;
            }
        }
        if (modelModified) {
            this.hierarchy.updateHandleMap(deletedNodes);
        }
        return modelModified;
    }

    public void processDelta(Collection<File> files_tobecompiled, Set<File> files_added, Set<File> files_deleted) throws IOException {
        try {
            Writer fw = null;
            if (dumpDeltaProcessing) {
                FileWriter filew = new FileWriter(dumpFilename, true);
                fw = new BufferedWriter(filew);
                fw.write("=== Processing delta changes for the model ===\n");
                fw.write("Files for compilation:#" + files_tobecompiled.size() + ":" + files_tobecompiled + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                fw.write("Files added          :#" + files_added.size() + ":" + files_added + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                fw.write("Files deleted        :#" + files_deleted.size() + ":" + files_deleted + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
            long stime = System.currentTimeMillis();
            removeStructureModelForFiles(fw, files_deleted);
            long etime1 = System.currentTimeMillis();
            repairRelationships(fw);
            long etime2 = System.currentTimeMillis();
            removeStructureModelForFiles(fw, files_tobecompiled);
            if (dumpDeltaProcessing) {
                fw.write("===== Delta Processing timing ==========\n");
                fw.write("Hierarchy=" + (etime1 - stime) + "ms   Relationshipmap=" + (etime2 - etime1) + "ms\n");
                fw.write("===== Traversal ========================\n");
                fw.write("========================================\n");
                fw.flush();
                fw.close();
            }
            reportModelInfo("After delta processing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTypeNameFromHandle(String handle, Map<String, String> cache) {
        try {
            String typename = cache.get(handle);
            if (typename != null) {
                return typename;
            }
            int hasPackage = handle.indexOf(HandleProviderDelimiter.PACKAGEFRAGMENT.getDelimiter());
            int typeLocation = handle.indexOf(HandleProviderDelimiter.TYPE.getDelimiter());
            if (typeLocation == -1) {
                typeLocation = handle.indexOf(HandleProviderDelimiter.ASPECT_TYPE.getDelimiter());
            }
            if (typeLocation == -1) {
                return "";
            }
            StringBuffer qualifiedTypeNameFromHandle = new StringBuffer();
            if (hasPackage != -1) {
                int classfileLoc = handle.indexOf(HandleProviderDelimiter.CLASSFILE.getDelimiter(), hasPackage);
                qualifiedTypeNameFromHandle.append(handle.substring(hasPackage + 1, classfileLoc));
                qualifiedTypeNameFromHandle.append('.');
            }
            qualifiedTypeNameFromHandle.append(handle.substring(typeLocation + 1));
            String typename2 = qualifiedTypeNameFromHandle.toString();
            cache.put(handle, typename2);
            return typename2;
        } catch (StringIndexOutOfBoundsException sioobe) {
            System.err.println("Handle processing problem, the handle is: " + handle);
            sioobe.printStackTrace(System.err);
            return "";
        }
    }

    public void removeRelationshipsTargettingThisType(String typename) {
        if (0 != 0) {
            System.err.println(">>removeRelationshipsTargettingThisType " + typename);
        }
        String pkg = null;
        String type = typename;
        int lastSep = typename.lastIndexOf(46);
        if (lastSep != -1) {
            pkg = typename.substring(0, lastSep);
            type = typename.substring(lastSep + 1);
        }
        boolean didsomething = false;
        IProgramElement typeNode = this.hierarchy.findElementForType(pkg, type);
        if (typeNode == null) {
            return;
        }
        Set<String> sourcesToRemove = new HashSet<>();
        Map<String, String> handleToTypenameCache = new HashMap<>();
        Set<String> sourcehandlesSet = this.mapper.getEntries();
        List<IRelationship> relationshipsToRemove = new ArrayList<>();
        for (String hid : sourcehandlesSet) {
            if (!isPhantomHandle(hid) || getTypeNameFromHandle(hid, handleToTypenameCache).equals(typename)) {
                IProgramElement sourceElement = this.hierarchy.getElement(hid);
                if (sourceElement == null || sameType(hid, sourceElement, typeNode)) {
                    relationshipsToRemove.clear();
                    List<IRelationship> relationships = this.mapper.get(hid);
                    for (IRelationship relationship : relationships) {
                        if (relationship.getKind() != IRelationship.Kind.USES_POINTCUT && !relationship.isAffects()) {
                            relationshipsToRemove.add(relationship);
                        }
                    }
                    if (relationshipsToRemove.size() > 0) {
                        didsomething = true;
                        if (relationshipsToRemove.size() == relationships.size()) {
                            sourcesToRemove.add(hid);
                        } else {
                            for (int i = 0; i < relationshipsToRemove.size(); i++) {
                                relationships.remove(relationshipsToRemove.get(i));
                            }
                        }
                    }
                }
            }
        }
        for (String hid2 : sourcesToRemove) {
            this.mapper.removeAll(hid2);
            IProgramElement ipe = this.hierarchy.getElement(hid2);
            if (ipe != null && ipe.getKind().equals(IProgramElement.Kind.CODE)) {
                if (0 != 0) {
                    System.err.println("  source handle: it was code node, removing that as well... code=" + ipe + " parent=" + ipe.getParent());
                }
                removeSingleNode(ipe);
            }
        }
        if (0 != 0) {
            dumprelsStderr("after processing 'affectedby'");
        }
        if (didsomething) {
            sourcesToRemove.clear();
            if (0 != 0) {
                dumprelsStderr("before processing 'affects'");
            }
            Set<String> sourcehandlesSet2 = this.mapper.getEntries();
            for (String hid3 : sourcehandlesSet2) {
                relationshipsToRemove.clear();
                List<IRelationship> relationships2 = this.mapper.get(hid3);
                for (IRelationship rel : relationships2) {
                    if (rel.getKind() != IRelationship.Kind.USES_POINTCUT && rel.isAffects()) {
                        List<String> targets = rel.getTargets();
                        List<String> targetsToRemove = new ArrayList<>();
                        for (String targethid : targets) {
                            if (!isPhantomHandle(hid3) || getTypeNameFromHandle(hid3, handleToTypenameCache).equals(typename)) {
                                IProgramElement existingTarget = this.hierarchy.getElement(targethid);
                                if (existingTarget == null || sameType(targethid, existingTarget, typeNode)) {
                                    targetsToRemove.add(targethid);
                                }
                            }
                        }
                        if (targetsToRemove.size() != 0) {
                            if (targetsToRemove.size() == targets.size()) {
                                relationshipsToRemove.add(rel);
                            } else {
                                for (String togo : targetsToRemove) {
                                    targets.remove(togo);
                                }
                            }
                        }
                    }
                }
                if (relationshipsToRemove.size() > 0) {
                    if (relationshipsToRemove.size() == relationships2.size()) {
                        sourcesToRemove.add(hid3);
                    } else {
                        for (int i2 = 0; i2 < relationshipsToRemove.size(); i2++) {
                            relationships2.remove(relationshipsToRemove.get(i2));
                        }
                    }
                }
            }
            for (String hid4 : sourcesToRemove) {
                this.mapper.removeAll(hid4);
                IProgramElement ipe2 = this.hierarchy.getElement(hid4);
                if (ipe2 != null && ipe2.getKind().equals(IProgramElement.Kind.CODE)) {
                    if (0 != 0) {
                        System.err.println("  source handle: it was code node, removing that as well... code=" + ipe2 + " parent=" + ipe2.getParent());
                    }
                    removeSingleNode(ipe2);
                }
            }
            if (0 != 0) {
                dumprelsStderr("after processing 'affects'");
            }
        }
        if (0 != 0) {
            System.err.println("<<removeRelationshipsTargettingThisFile");
        }
    }

    private boolean sameType(String hid, IProgramElement target, IProgramElement type) {
        IProgramElement containingType = target;
        if (target == null) {
            throw new RuntimeException("target can't be null!");
        }
        if (type == null) {
            throw new RuntimeException("type can't be null!");
        }
        if (target.getKind().isSourceFile() || target.getKind().isFile()) {
            if (target.getSourceLocation() == null || type.getSourceLocation() == null || target.getSourceLocation().getSourceFile() == null || type.getSourceLocation().getSourceFile() == null) {
                return false;
            }
            return target.getSourceLocation().getSourceFile().equals(type.getSourceLocation().getSourceFile());
        }
        while (!containingType.getKind().isType()) {
            try {
                containingType = containingType.getParent();
            } catch (Throwable t) {
                throw new RuntimeException("Exception whilst walking up from target " + target.toLabelString() + " kind=(" + target.getKind() + ") hid=(" + target.getHandleIdentifier() + ")", t);
            }
        }
        return type.equals(containingType);
    }

    private boolean isPhantomHandle(String handle) {
        int phantomMarker = handle.indexOf(HandleProviderDelimiter.PHANTOM.getDelimiter());
        return phantomMarker != -1 && handle.charAt(phantomMarker - 1) == HandleProviderDelimiter.PACKAGEFRAGMENTROOT.getDelimiter();
    }

    private void repairRelationships(Writer fw) throws IOException {
        try {
            if (dumpDeltaProcessing) {
                fw.write("Repairing relationships map:\n");
            }
            Set<String> sourcesToRemove = new HashSet<>();
            Set<String> nonExistingHandles = new HashSet<>();
            Set<String> keyset = this.mapper.getEntries();
            for (String hid : keyset) {
                if (nonExistingHandles.contains(hid)) {
                    sourcesToRemove.add(hid);
                } else if (!isPhantomHandle(hid)) {
                    IProgramElement existingElement = this.hierarchy.getElement(hid);
                    if (dumpDeltaProcessing) {
                        fw.write("Looking for handle [" + hid + "] in model, found: " + existingElement + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                    }
                    if (existingElement == null) {
                        sourcesToRemove.add(hid);
                        nonExistingHandles.add(hid);
                    } else {
                        List<IRelationship> relationships = this.mapper.get(hid);
                        List<IRelationship> relationshipsToRemove = new ArrayList<>();
                        for (IRelationship rel : relationships) {
                            List<String> targets = rel.getTargets();
                            List<String> targetsToRemove = new ArrayList<>();
                            for (String targethid : targets) {
                                if (nonExistingHandles.contains(targethid)) {
                                    if (dumpDeltaProcessing) {
                                        fw.write("Target handle [" + targethid + "] for srchid[" + hid + "]rel[" + rel.getName() + "] does not exist\n");
                                    }
                                    targetsToRemove.add(targethid);
                                } else if (!isPhantomHandle(targethid)) {
                                    IProgramElement existingTarget = this.hierarchy.getElement(targethid);
                                    if (existingTarget == null) {
                                        if (dumpDeltaProcessing) {
                                            fw.write("Target handle [" + targethid + "] for srchid[" + hid + "]rel[" + rel.getName() + "] does not exist\n");
                                        }
                                        targetsToRemove.add(targethid);
                                        nonExistingHandles.add(targethid);
                                    }
                                }
                            }
                            if (targetsToRemove.size() != 0) {
                                if (targetsToRemove.size() == targets.size()) {
                                    if (dumpDeltaProcessing) {
                                        fw.write("No targets remain for srchid[" + hid + "] rel[" + rel.getName() + "]: removing it\n");
                                    }
                                    relationshipsToRemove.add(rel);
                                } else {
                                    for (String togo : targetsToRemove) {
                                        targets.remove(togo);
                                    }
                                    if (targets.size() == 0) {
                                        if (dumpDeltaProcessing) {
                                            fw.write("No targets remain for srchid[" + hid + "] rel[" + rel.getName() + "]: removing it\n");
                                        }
                                        relationshipsToRemove.add(rel);
                                    }
                                }
                            }
                        }
                        if (relationshipsToRemove.size() > 0) {
                            if (relationshipsToRemove.size() == relationships.size()) {
                                sourcesToRemove.add(hid);
                            } else {
                                for (int i = 0; i < relationshipsToRemove.size(); i++) {
                                    IRelationship irel = relationshipsToRemove.get(i);
                                    verifyAssumption(this.mapper.remove(hid, irel), "Failed to remove relationship " + irel.getName() + " for shid " + hid);
                                }
                                List<IRelationship> rels = this.mapper.get(hid);
                                if (rels == null || rels.size() == 0) {
                                    sourcesToRemove.add(hid);
                                }
                            }
                        }
                    }
                }
            }
            for (String hid2 : sourcesToRemove) {
                this.mapper.removeAll(hid2);
                IProgramElement ipe = this.hierarchy.getElement(hid2);
                if (ipe != null && ipe.getKind().equals(IProgramElement.Kind.CODE)) {
                    removeSingleNode(ipe);
                }
            }
        } catch (IOException ioe) {
            System.err.println("Failed to repair relationships:");
            ioe.printStackTrace();
        }
    }

    private void removeSingleNode(IProgramElement progElem) {
        if (progElem == null) {
            throw new IllegalStateException("AsmManager.removeNode(): programElement unexpectedly null");
        }
        boolean deleteOK = false;
        IProgramElement parent = progElem.getParent();
        List<IProgramElement> kids = parent.getChildren();
        int i = 0;
        int max = kids.size();
        while (true) {
            if (i >= max) {
                break;
            }
            if (!kids.get(i).equals(progElem)) {
                i++;
            } else {
                kids.remove(i);
                deleteOK = true;
                break;
            }
        }
        if (!deleteOK) {
            System.err.println("unexpectedly failed to delete node from model.  hid=" + progElem.getHandleIdentifier());
        }
    }

    private void removeNode(IProgramElement progElem) {
        try {
            if (progElem == null) {
                throw new IllegalStateException("AsmManager.removeNode(): programElement unexpectedly null");
            }
            IProgramElement parent = progElem.getParent();
            List<IProgramElement> kids = parent.getChildren();
            int i = 0;
            while (true) {
                if (i >= kids.size()) {
                    break;
                }
                if (!kids.get(i).equals(progElem)) {
                    i++;
                } else {
                    kids.remove(i);
                    break;
                }
            }
            if (parent.getChildren().size() == 0 && parent.getParent() != null && (parent.getKind().equals(IProgramElement.Kind.CODE) || parent.getKind().equals(IProgramElement.Kind.PACKAGE))) {
                removeNode(parent);
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    public static void verifyAssumption(boolean b, String info) {
        if (!b) {
            System.err.println("=========== ASSERTION IS NOT TRUE =========v");
            System.err.println(info);
            Thread.dumpStack();
            System.err.println("=========== ASSERTION IS NOT TRUE =========^");
            throw new RuntimeException("Assertion is false");
        }
    }

    public static void verifyAssumption(boolean b) {
        if (!b) {
            Thread.dumpStack();
            throw new RuntimeException("Assertion is false");
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/AsmManager$ModelInfo.class */
    public static class ModelInfo {
        private final Hashtable<String, Integer> nodeTypeCount;
        private final Properties extraProperties;

        private ModelInfo(IHierarchy hierarchy, IRelationshipMap relationshipMap) {
            this.nodeTypeCount = new Hashtable<>();
            this.extraProperties = new Properties();
            IProgramElement ipe = hierarchy.getRoot();
            walkModel(ipe);
            recordStat("FileMapSize", new Integer(hierarchy.getFileMapEntrySet().size()).toString());
            recordStat("RelationshipMapSize", new Integer(relationshipMap.getEntries().size()).toString());
        }

        private void walkModel(IProgramElement ipe) {
            countNode(ipe);
            for (IProgramElement child : ipe.getChildren()) {
                walkModel(child);
            }
        }

        private void countNode(IProgramElement ipe) {
            String node = ipe.getKind().toString();
            Integer ctr = this.nodeTypeCount.get(node);
            if (ctr == null) {
                this.nodeTypeCount.put(node, new Integer(1));
            } else {
                this.nodeTypeCount.put(node, new Integer(ctr.intValue() + 1));
            }
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("Model node summary:\n");
            Enumeration<String> nodeKeys = this.nodeTypeCount.keys();
            while (nodeKeys.hasMoreElements()) {
                String key = nodeKeys.nextElement();
                Integer ct = this.nodeTypeCount.get(key);
                sb.append(key + SymbolConstants.EQUAL_SYMBOL + ct + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
            sb.append("Model stats:\n");
            Enumeration<Object> ks = this.extraProperties.keys();
            while (ks.hasMoreElements()) {
                String k = (String) ks.nextElement();
                String v = this.extraProperties.getProperty(k);
                sb.append(k + SymbolConstants.EQUAL_SYMBOL + v + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
            return sb.toString();
        }

        public Properties getProperties() {
            Properties p = new Properties();
            for (Map.Entry<String, Integer> entry : this.nodeTypeCount.entrySet()) {
                p.setProperty(entry.getKey(), entry.getValue().toString());
            }
            p.putAll(this.extraProperties);
            return p;
        }

        public void recordStat(String string, String string2) {
            this.extraProperties.setProperty(string, string2);
        }
    }

    public ModelInfo summarizeModel() {
        return new ModelInfo(getHierarchy(), getRelationshipMap());
    }

    public static void setCompletingTypeBindings(boolean b) {
        completingTypeBindings = b;
    }

    public static boolean isCompletingTypeBindings() {
        return completingTypeBindings;
    }

    public void resetDeltaProcessing() {
        this.lastBuildChanges.clear();
        this.aspectsWeavingInLastBuild.clear();
    }

    public Set<File> getModelChangesOnLastBuild() {
        return this.lastBuildChanges;
    }

    public Set<File> getAspectsWeavingFilesOnLastBuild() {
        return this.aspectsWeavingInLastBuild;
    }

    public void addAspectInEffectThisBuild(File f) {
        this.aspectsWeavingInLastBuild.add(f);
    }

    public static void setLastActiveStructureModel(AsmManager structureModel) {
        if (recordingLastActiveStructureModel) {
            lastActiveStructureModel = structureModel;
        }
    }

    public String getHandleElementForInpath(String binaryPath) {
        return this.inpathMap.get(new File(binaryPath));
    }
}
