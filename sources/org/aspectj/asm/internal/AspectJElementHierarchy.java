package org.aspectj.asm.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aspectj.asm.AsmManager;
import org.aspectj.asm.IHierarchy;
import org.aspectj.asm.IProgramElement;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.SourceLocation;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/internal/AspectJElementHierarchy.class */
public class AspectJElementHierarchy implements IHierarchy {
    private static final long serialVersionUID = 6462734311117048620L;
    private transient AsmManager asm;
    protected IProgramElement root = null;
    protected String configFile = null;
    private Map<String, IProgramElement> fileMap = null;
    private Map<String, IProgramElement> handleMap = new HashMap();
    private Map<String, IProgramElement> typeMap = null;

    public AspectJElementHierarchy(AsmManager asm) {
        this.asm = asm;
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement getElement(String handle) {
        return findElementForHandleOrCreate(handle, false);
    }

    public void setAsmManager(AsmManager asm) {
        this.asm = asm;
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement getRoot() {
        return this.root;
    }

    public String toSummaryString() {
        StringBuilder s = new StringBuilder();
        s.append("FileMap has " + this.fileMap.size() + " entries\n");
        s.append("HandleMap has " + this.handleMap.size() + " entries\n");
        s.append("TypeMap has " + this.handleMap.size() + " entries\n");
        s.append("FileMap:\n");
        for (Map.Entry<String, IProgramElement> fileMapEntry : this.fileMap.entrySet()) {
            s.append(fileMapEntry).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        s.append("TypeMap:\n");
        for (Map.Entry<String, IProgramElement> typeMapEntry : this.typeMap.entrySet()) {
            s.append(typeMapEntry).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        s.append("HandleMap:\n");
        for (Map.Entry<String, IProgramElement> handleMapEntry : this.handleMap.entrySet()) {
            s.append(handleMapEntry).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        return s.toString();
    }

    @Override // org.aspectj.asm.IHierarchy
    public void setRoot(IProgramElement root) {
        this.root = root;
        this.handleMap = new HashMap();
        this.typeMap = new HashMap();
    }

    @Override // org.aspectj.asm.IHierarchy
    public void addToFileMap(String key, IProgramElement value) {
        this.fileMap.put(key, value);
    }

    @Override // org.aspectj.asm.IHierarchy
    public boolean removeFromFileMap(String canonicalFilePath) {
        return this.fileMap.remove(canonicalFilePath) != null;
    }

    @Override // org.aspectj.asm.IHierarchy
    public void setFileMap(HashMap<String, IProgramElement> fileMap) {
        this.fileMap = fileMap;
    }

    @Override // org.aspectj.asm.IHierarchy
    public Object findInFileMap(Object key) {
        return this.fileMap.get(key);
    }

    @Override // org.aspectj.asm.IHierarchy
    public Set<Map.Entry<String, IProgramElement>> getFileMapEntrySet() {
        return this.fileMap.entrySet();
    }

    @Override // org.aspectj.asm.IHierarchy
    public boolean isValid() {
        return (this.root == null || this.fileMap == null) ? false : true;
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement findElementForSignature(IProgramElement parent, IProgramElement.Kind kind, String signature) {
        for (IProgramElement node : parent.getChildren()) {
            if (node.getKind() == kind && signature.equals(node.toSignatureString())) {
                return node;
            }
            IProgramElement childSearch = findElementForSignature(node, kind, signature);
            if (childSearch != null) {
                return childSearch;
            }
        }
        return null;
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement findElementForLabel(IProgramElement parent, IProgramElement.Kind kind, String label) {
        for (IProgramElement node : parent.getChildren()) {
            if (node.getKind() == kind && label.equals(node.toLabelString())) {
                return node;
            }
            IProgramElement childSearch = findElementForLabel(node, kind, label);
            if (childSearch != null) {
                return childSearch;
            }
        }
        return null;
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement findElementForType(String packageName, String typeName) {
        synchronized (this) {
            StringBuilder keyb = packageName == null ? new StringBuilder() : new StringBuilder(packageName);
            keyb.append(".").append(typeName);
            String key = keyb.toString();
            IProgramElement cachedValue = this.typeMap.get(key);
            if (cachedValue != null) {
                return cachedValue;
            }
            List<IProgramElement> packageNodes = findMatchingPackages(packageName);
            for (IProgramElement pkg : packageNodes) {
                for (IProgramElement fileNode : pkg.getChildren()) {
                    IProgramElement cNode = findClassInNodes(fileNode.getChildren(), typeName, typeName);
                    if (cNode != null) {
                        this.typeMap.put(key, cNode);
                        return cNode;
                    }
                }
            }
            return null;
        }
    }

    public List<IProgramElement> findMatchingPackages(String packagename) {
        List<IProgramElement> children = this.root.getChildren();
        if (children.size() == 0) {
            return Collections.emptyList();
        }
        if (children.get(0).getKind() == IProgramElement.Kind.SOURCE_FOLDER) {
            String searchPackageName = packagename == null ? "" : packagename;
            List<IProgramElement> matchingPackageNodes = new ArrayList<>();
            for (IProgramElement sourceFolder : children) {
                List<IProgramElement> possiblePackageNodes = sourceFolder.getChildren();
                for (IProgramElement possiblePackageNode : possiblePackageNodes) {
                    if (possiblePackageNode.getKind() == IProgramElement.Kind.PACKAGE && possiblePackageNode.getName().equals(searchPackageName)) {
                        matchingPackageNodes.add(possiblePackageNode);
                    }
                }
            }
            return matchingPackageNodes;
        }
        if (packagename == null) {
            List<IProgramElement> result = new ArrayList<>();
            result.add(this.root);
            return result;
        }
        List<IProgramElement> result2 = new ArrayList<>();
        for (IProgramElement possiblePackage : children) {
            if (possiblePackage.getKind() == IProgramElement.Kind.PACKAGE && possiblePackage.getName().equals(packagename)) {
                result2.add(possiblePackage);
            }
            if (possiblePackage.getKind() == IProgramElement.Kind.SOURCE_FOLDER && possiblePackage.getName().equals("binaries")) {
                Iterator<IProgramElement> it = possiblePackage.getChildren().iterator();
                while (true) {
                    if (it.hasNext()) {
                        IProgramElement possiblePackage2 = it.next();
                        if (possiblePackage2.getKind() == IProgramElement.Kind.PACKAGE && possiblePackage2.getName().equals(packagename)) {
                            result2.add(possiblePackage2);
                            break;
                        }
                    }
                }
            }
        }
        if (result2.isEmpty()) {
            return Collections.emptyList();
        }
        return result2;
    }

    private IProgramElement findClassInNodes(Collection<IProgramElement> nodes, String name, String typeName) {
        String baseName;
        String innerName;
        IProgramElement node;
        IProgramElement node2;
        int dollar = name.indexOf(36);
        if (dollar == -1) {
            baseName = name;
            innerName = null;
        } else {
            baseName = name.substring(0, dollar);
            innerName = name.substring(dollar + 1);
        }
        for (IProgramElement classNode : nodes) {
            if (!classNode.getKind().isType()) {
                List<IProgramElement> kids = classNode.getChildren();
                if (kids != null && !kids.isEmpty() && (node = findClassInNodes(kids, name, typeName)) != null) {
                    return node;
                }
            } else {
                if (baseName.equals(classNode.getName())) {
                    if (innerName == null) {
                        return classNode;
                    }
                    return findClassInNodes(classNode.getChildren(), innerName, typeName);
                }
                if (name.equals(classNode.getName())) {
                    return classNode;
                }
                if (typeName.equals(classNode.getBytecodeSignature())) {
                    return classNode;
                }
                if (classNode.getChildren() != null && !classNode.getChildren().isEmpty() && (node2 = findClassInNodes(classNode.getChildren(), name, typeName)) != null) {
                    return node2;
                }
            }
        }
        return null;
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement findElementForSourceFile(String sourceFile) {
        try {
            if (!isValid() || sourceFile == null) {
                return IHierarchy.NO_STRUCTURE;
            }
            String correctedPath = this.asm.getCanonicalFilePath(new File(sourceFile));
            IProgramElement node = (IProgramElement) findInFileMap(correctedPath);
            if (node != null) {
                return node;
            }
            return createFileStructureNode(correctedPath);
        } catch (Exception e) {
            return IHierarchy.NO_STRUCTURE;
        }
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement findElementForSourceLine(ISourceLocation location) {
        try {
            return findElementForSourceLine(this.asm.getCanonicalFilePath(location.getSourceFile()), location.getLine());
        } catch (Exception e) {
            return null;
        }
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement findElementForSourceLine(String sourceFilePath, int lineNumber) {
        String canonicalSFP = this.asm.getCanonicalFilePath(new File(sourceFilePath));
        IProgramElement node = findNodeForSourceFile(this.root, canonicalSFP);
        if (node == null) {
            return createFileStructureNode(sourceFilePath);
        }
        IProgramElement closernode = findCloserMatchForLineNumber(node, lineNumber);
        if (closernode == null) {
            return node;
        }
        return closernode;
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement findNodeForSourceFile(IProgramElement node, String sourcefilePath) {
        if ((node.getKind().isSourceFile() && !node.getName().equals("<root>")) || node.getKind().isFile()) {
            ISourceLocation nodeLoc = node.getSourceLocation();
            if (nodeLoc != null && this.asm.getCanonicalFilePath(nodeLoc.getSourceFile()).equals(sourcefilePath)) {
                return node;
            }
            return null;
        }
        for (IProgramElement child : node.getChildren()) {
            IProgramElement foundit = findNodeForSourceFile(child, sourcefilePath);
            if (foundit != null) {
                return foundit;
            }
        }
        return null;
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement findElementForOffSet(String sourceFilePath, int lineNumber, int offSet) {
        String canonicalSFP = this.asm.getCanonicalFilePath(new File(sourceFilePath));
        IProgramElement node = findNodeForSourceLineHelper(this.root, canonicalSFP, lineNumber, offSet);
        if (node != null) {
            return node;
        }
        return createFileStructureNode(sourceFilePath);
    }

    private IProgramElement createFileStructureNode(String sourceFilePath) {
        int lastSlash = sourceFilePath.lastIndexOf(92);
        if (lastSlash == -1) {
            lastSlash = sourceFilePath.lastIndexOf(47);
        }
        int i = sourceFilePath.lastIndexOf(33);
        int j = sourceFilePath.indexOf(ClassUtils.CLASS_FILE_SUFFIX);
        if (i > lastSlash && i != -1 && j != -1) {
            lastSlash = i;
        }
        String fileName = sourceFilePath.substring(lastSlash + 1);
        IProgramElement fileNode = new ProgramElement(this.asm, fileName, IProgramElement.Kind.FILE_JAVA, new SourceLocation(new File(sourceFilePath), 1, 1), 0, null, null);
        fileNode.addChild(NO_STRUCTURE);
        return fileNode;
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement findCloserMatchForLineNumber(IProgramElement node, int lineno) {
        IProgramElement evenCloserMatch;
        if (node == null || node.getChildren() == null) {
            return null;
        }
        for (IProgramElement child : node.getChildren()) {
            ISourceLocation childLoc = child.getSourceLocation();
            if (childLoc != null) {
                if (childLoc.getLine() <= lineno && childLoc.getEndLine() >= lineno) {
                    IProgramElement evenCloserMatch2 = findCloserMatchForLineNumber(child, lineno);
                    if (evenCloserMatch2 == null) {
                        return child;
                    }
                    return evenCloserMatch2;
                }
                if (child.getKind().isType() && (evenCloserMatch = findCloserMatchForLineNumber(child, lineno)) != null) {
                    return evenCloserMatch;
                }
            }
        }
        return null;
    }

    private IProgramElement findNodeForSourceLineHelper(IProgramElement node, String sourceFilePath, int lineno, int offset) {
        if (matches(node, sourceFilePath, lineno, offset) && !hasMoreSpecificChild(node, sourceFilePath, lineno, offset)) {
            return node;
        }
        if (node != null) {
            for (IProgramElement child : node.getChildren()) {
                IProgramElement foundNode = findNodeForSourceLineHelper(child, sourceFilePath, lineno, offset);
                if (foundNode != null) {
                    return foundNode;
                }
            }
            return null;
        }
        return null;
    }

    private boolean matches(IProgramElement node, String sourceFilePath, int lineNumber, int offSet) {
        ISourceLocation nodeSourceLocation = node != null ? node.getSourceLocation() : null;
        return node != null && nodeSourceLocation != null && nodeSourceLocation.getSourceFile().getAbsolutePath().equals(sourceFilePath) && ((offSet != -1 && nodeSourceLocation.getOffset() == offSet) || offSet == -1) && ((nodeSourceLocation.getLine() <= lineNumber && nodeSourceLocation.getEndLine() >= lineNumber) || (lineNumber <= 1 && node.getKind().isSourceFile()));
    }

    private boolean hasMoreSpecificChild(IProgramElement node, String sourceFilePath, int lineNumber, int offSet) {
        for (IProgramElement child : node.getChildren()) {
            if (matches(child, sourceFilePath, lineNumber, offSet)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.asm.IHierarchy
    public String getConfigFile() {
        return this.configFile;
    }

    @Override // org.aspectj.asm.IHierarchy
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement findElementForHandle(String handle) {
        return findElementForHandleOrCreate(handle, true);
    }

    @Override // org.aspectj.asm.IHierarchy
    public IProgramElement findElementForHandleOrCreate(String handle, boolean create) {
        synchronized (this) {
            IProgramElement ipe = this.handleMap.get(handle);
            if (ipe != null) {
                return ipe;
            }
            IProgramElement ipe2 = findElementForHandle(this.root, handle);
            if (ipe2 == null && create) {
                ipe2 = createFileStructureNode(getFilename(handle));
            }
            if (ipe2 != null) {
                cache(handle, ipe2);
            }
            return ipe2;
        }
    }

    private IProgramElement findElementForHandle(IProgramElement parent, String handle) {
        IProgramElement childSearch;
        for (IProgramElement node : parent.getChildren()) {
            String nodeHid = node.getHandleIdentifier();
            if (handle.equals(nodeHid)) {
                return node;
            }
            if (handle.startsWith(nodeHid) && (childSearch = findElementForHandle(node, handle)) != null) {
                return childSearch;
            }
        }
        return null;
    }

    protected void cache(String handle, IProgramElement pe) {
        if (!AsmManager.isCompletingTypeBindings()) {
            this.handleMap.put(handle, pe);
        }
    }

    @Override // org.aspectj.asm.IHierarchy
    public void flushTypeMap() {
        this.typeMap.clear();
    }

    @Override // org.aspectj.asm.IHierarchy
    public void flushHandleMap() {
        this.handleMap.clear();
    }

    public void flushFileMap() {
        this.fileMap.clear();
    }

    public void forget(IProgramElement compilationUnitNode, IProgramElement typeNode) {
        String k = null;
        synchronized (this) {
            Iterator<Map.Entry<String, IProgramElement>> it = this.typeMap.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<String, IProgramElement> typeMapEntry = it.next();
                if (typeMapEntry.getValue() == typeNode) {
                    k = typeMapEntry.getKey();
                    break;
                }
            }
            if (k != null) {
                this.typeMap.remove(k);
            }
        }
        if (compilationUnitNode != null) {
            String k2 = null;
            Iterator<Map.Entry<String, IProgramElement>> it2 = this.fileMap.entrySet().iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                Map.Entry<String, IProgramElement> entry = it2.next();
                if (entry.getValue() == compilationUnitNode) {
                    k2 = entry.getKey();
                    break;
                }
            }
            if (k2 != null) {
                this.fileMap.remove(k2);
            }
        }
    }

    @Override // org.aspectj.asm.IHierarchy
    public void updateHandleMap(Set<String> deletedFiles) {
        List<String> forRemoval = new ArrayList<>();
        synchronized (this) {
            Set<String> k = this.handleMap.keySet();
            for (String handle : k) {
                IProgramElement ipe = this.handleMap.get(handle);
                if (ipe == null) {
                    System.err.println("handleMap expectation not met, where is the IPE for " + handle);
                }
                if (ipe == null || deletedFiles.contains(getCanonicalFilePath(ipe))) {
                    forRemoval.add(handle);
                }
            }
            Iterator<String> it = forRemoval.iterator();
            while (it.hasNext()) {
                this.handleMap.remove(it.next());
            }
            forRemoval.clear();
            Set<String> k2 = this.typeMap.keySet();
            for (String typeName : k2) {
                if (deletedFiles.contains(getCanonicalFilePath(this.typeMap.get(typeName)))) {
                    forRemoval.add(typeName);
                }
            }
            Iterator<String> it2 = forRemoval.iterator();
            while (it2.hasNext()) {
                this.typeMap.remove(it2.next());
            }
            forRemoval.clear();
        }
        for (Map.Entry<String, IProgramElement> entry : this.fileMap.entrySet()) {
            String filePath = entry.getKey();
            if (deletedFiles.contains(getCanonicalFilePath(entry.getValue()))) {
                forRemoval.add(filePath);
            }
        }
        for (String filePath2 : forRemoval) {
            this.fileMap.remove(filePath2);
        }
    }

    private String getFilename(String hid) {
        return this.asm.getHandleProvider().getFileForHandle(hid);
    }

    private String getCanonicalFilePath(IProgramElement ipe) {
        if (ipe.getSourceLocation() != null) {
            return this.asm.getCanonicalFilePath(ipe.getSourceLocation().getSourceFile());
        }
        return "";
    }
}
