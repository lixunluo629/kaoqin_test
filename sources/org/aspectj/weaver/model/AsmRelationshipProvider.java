package org.aspectj.weaver.model;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.aspectj.asm.AsmManager;
import org.aspectj.asm.IHierarchy;
import org.aspectj.asm.IProgramElement;
import org.aspectj.asm.IRelationship;
import org.aspectj.asm.IRelationshipMap;
import org.aspectj.asm.internal.HandleProviderDelimiter;
import org.aspectj.asm.internal.ProgramElement;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.SourceLocation;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.Checker;
import org.aspectj.weaver.Lint;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NewParentTypeMunger;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.ResolvedTypeMunger;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelShadow;
import org.aspectj.weaver.bcel.BcelTypeMunger;
import org.aspectj.weaver.patterns.DeclareErrorOrWarning;
import org.aspectj.weaver.patterns.DeclareParents;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.TypePatternList;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/model/AsmRelationshipProvider.class */
public class AsmRelationshipProvider {
    public static final String ADVISES = "advises";
    public static final String ADVISED_BY = "advised by";
    public static final String DECLARES_ON = "declares on";
    public static final String DECLAREDY_BY = "declared by";
    public static final String SOFTENS = "softens";
    public static final String SOFTENED_BY = "softened by";
    public static final String MATCHED_BY = "matched by";
    public static final String MATCHES_DECLARE = "matches declare";
    public static final String INTER_TYPE_DECLARES = "declared on";
    public static final String INTER_TYPE_DECLARED_BY = "aspect declarations";
    public static final String ANNOTATES = "annotates";
    public static final String ANNOTATED_BY = "annotated by";
    private static final String NO_COMMENT = null;

    public static void addDeclareErrorOrWarningRelationship(AsmManager model, Shadow affectedShadow, Checker deow) {
        String targetHandle;
        IProgramElement sourceNode;
        String sourceHandle;
        if (model == null || affectedShadow.getSourceLocation() == null || deow.getSourceLocation() == null) {
            return;
        }
        if (World.createInjarHierarchy) {
            createHierarchyForBinaryAspect(model, deow);
        }
        IProgramElement targetNode = getNode(model, affectedShadow);
        if (targetNode == null || (targetHandle = targetNode.getHandleIdentifier()) == null || (sourceHandle = (sourceNode = model.getHierarchy().findElementForSourceLine(deow.getSourceLocation())).getHandleIdentifier()) == null) {
            return;
        }
        IRelationshipMap relmap = model.getRelationshipMap();
        IRelationship foreward = relmap.get(sourceHandle, IRelationship.Kind.DECLARE, MATCHED_BY, false, true);
        foreward.addTarget(targetHandle);
        IRelationship back = relmap.get(targetHandle, IRelationship.Kind.DECLARE, MATCHES_DECLARE, false, true);
        if (back != null && back.getTargets() != null) {
            back.addTarget(sourceHandle);
        }
        if (sourceNode.getSourceLocation() != null) {
            model.addAspectInEffectThisBuild(sourceNode.getSourceLocation().getSourceFile());
        }
    }

    private static boolean isMixinRelated(ResolvedTypeMunger typeTransformer) {
        ResolvedTypeMunger.Kind kind = typeTransformer.getKind();
        return kind == ResolvedTypeMunger.MethodDelegate2 || kind == ResolvedTypeMunger.FieldHost || (kind == ResolvedTypeMunger.Parent && ((NewParentTypeMunger) typeTransformer).isMixin());
    }

    public static void addRelationship(AsmManager model, ResolvedType onType, ResolvedTypeMunger typeTransformer, ResolvedType originatingAspect) {
        IProgramElement sourceNode;
        String sourceHandle;
        String targetHandle;
        if (model == null) {
            return;
        }
        if (World.createInjarHierarchy && isBinaryAspect(originatingAspect)) {
            createHierarchy(model, typeTransformer, originatingAspect);
        }
        if (originatingAspect.getSourceLocation() != null) {
            if (typeTransformer.getSourceLocation() != null && typeTransformer.getSourceLocation().getOffset() != -1 && !isMixinRelated(typeTransformer)) {
                sourceNode = model.getHierarchy().findElementForType(originatingAspect.getPackageName(), originatingAspect.getClassName());
                IProgramElement closer = model.getHierarchy().findCloserMatchForLineNumber(sourceNode, typeTransformer.getSourceLocation().getLine());
                if (closer != null) {
                    sourceNode = closer;
                }
                if (sourceNode == null && World.createInjarHierarchy) {
                    createHierarchy(model, typeTransformer, originatingAspect);
                    if (typeTransformer.getSourceLocation() != null && typeTransformer.getSourceLocation().getOffset() != -1 && !isMixinRelated(typeTransformer)) {
                        sourceNode = model.getHierarchy().findElementForType(originatingAspect.getPackageName(), originatingAspect.getClassName());
                        IProgramElement closer2 = model.getHierarchy().findCloserMatchForLineNumber(sourceNode, typeTransformer.getSourceLocation().getLine());
                        if (closer2 != null) {
                            sourceNode = closer2;
                        }
                    } else {
                        sourceNode = model.getHierarchy().findElementForType(originatingAspect.getPackageName(), originatingAspect.getClassName());
                    }
                }
                sourceHandle = sourceNode.getHandleIdentifier();
            } else {
                sourceNode = model.getHierarchy().findElementForType(originatingAspect.getPackageName(), originatingAspect.getClassName());
                sourceHandle = sourceNode.getHandleIdentifier();
            }
            if (sourceHandle == null || (targetHandle = findOrFakeUpNode(model, onType)) == null) {
                return;
            }
            IRelationshipMap mapper = model.getRelationshipMap();
            IRelationship foreward = mapper.get(sourceHandle, IRelationship.Kind.DECLARE_INTER_TYPE, INTER_TYPE_DECLARES, false, true);
            foreward.addTarget(targetHandle);
            IRelationship back = mapper.get(targetHandle, IRelationship.Kind.DECLARE_INTER_TYPE, INTER_TYPE_DECLARED_BY, false, true);
            back.addTarget(sourceHandle);
            if (sourceNode != null && sourceNode.getSourceLocation() != null) {
                model.addAspectInEffectThisBuild(sourceNode.getSourceLocation().getSourceFile());
            }
        }
    }

    private static String findOrFakeUpNode(AsmManager model, ResolvedType onType) {
        char ch2;
        IHierarchy hierarchy = model.getHierarchy();
        ISourceLocation sourceLocation = onType.getSourceLocation();
        String canonicalFilePath = model.getCanonicalFilePath(sourceLocation.getSourceFile());
        int lineNumber = sourceLocation.getLine();
        IProgramElement node = hierarchy.findNodeForSourceFile(hierarchy.getRoot(), canonicalFilePath);
        if (node == null) {
            String bpath = onType.getBinaryPath();
            if (bpath == null) {
                return model.getHandleProvider().createHandleIdentifier(createFileStructureNode(model, canonicalFilePath));
            }
            IProgramElement programElement = model.getHierarchy().getRoot();
            StringBuffer phantomHandle = new StringBuffer();
            phantomHandle.append(programElement.getHandleIdentifier());
            phantomHandle.append(HandleProviderDelimiter.PACKAGEFRAGMENTROOT.getDelimiter()).append(HandleProviderDelimiter.PHANTOM.getDelimiter());
            int pos = bpath.indexOf(33);
            if (pos != -1) {
                String jarPath = bpath.substring(0, pos);
                String element = model.getHandleElementForInpath(jarPath);
                if (element != null) {
                    phantomHandle.append(element);
                }
            }
            String packageName = onType.getPackageName();
            phantomHandle.append(HandleProviderDelimiter.PACKAGEFRAGMENT.getDelimiter()).append(packageName);
            int dotClassPosition = bpath.lastIndexOf(ClassUtils.CLASS_FILE_SUFFIX);
            if (dotClassPosition == -1) {
                phantomHandle.append(HandleProviderDelimiter.CLASSFILE.getDelimiter()).append("UNKNOWN.class");
            } else {
                int startPosition = dotClassPosition;
                while (startPosition > 0 && (ch2 = bpath.charAt(startPosition)) != '/' && ch2 != '\\' && ch2 != '!') {
                    startPosition--;
                }
                String classFile = bpath.substring(startPosition + 1, dotClassPosition + 6);
                phantomHandle.append(HandleProviderDelimiter.CLASSFILE.getDelimiter()).append(classFile);
            }
            phantomHandle.append(HandleProviderDelimiter.TYPE.getDelimiter()).append(onType.getClassName());
            return phantomHandle.toString();
        }
        IProgramElement closernode = hierarchy.findCloserMatchForLineNumber(node, lineNumber);
        if (closernode == null) {
            return node.getHandleIdentifier();
        }
        return closernode.getHandleIdentifier();
    }

    public static IProgramElement createFileStructureNode(AsmManager asm, String sourceFilePath) {
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
        IProgramElement fileNode = new ProgramElement(asm, fileName, IProgramElement.Kind.FILE_JAVA, new SourceLocation(new File(sourceFilePath), 1, 1), 0, null, null);
        fileNode.addChild(IHierarchy.NO_STRUCTURE);
        return fileNode;
    }

    private static boolean isBinaryAspect(ResolvedType aspect) {
        return aspect.getBinaryPath() != null;
    }

    private static ISourceLocation getBinarySourceLocation(ResolvedType aspect, ISourceLocation sl) {
        if (sl == null) {
            return null;
        }
        String sourceFileName = null;
        if (aspect instanceof ReferenceType) {
            String s = ((ReferenceType) aspect).getDelegate().getSourcefilename();
            int i = s.lastIndexOf(47);
            if (i != -1) {
                sourceFileName = s.substring(i + 1);
            } else {
                sourceFileName = s;
            }
        }
        ISourceLocation sLoc = new SourceLocation(getBinaryFile(aspect), sl.getLine(), sl.getEndLine(), sl.getColumn() == 0 ? ISourceLocation.NO_COLUMN : sl.getColumn(), sl.getContext(), sourceFileName);
        return sLoc;
    }

    private static ISourceLocation createSourceLocation(String sourcefilename, ResolvedType aspect, ISourceLocation sl) {
        ISourceLocation sLoc = new SourceLocation(getBinaryFile(aspect), sl.getLine(), sl.getEndLine(), sl.getColumn() == 0 ? ISourceLocation.NO_COLUMN : sl.getColumn(), sl.getContext(), sourcefilename);
        return sLoc;
    }

    private static String getSourceFileName(ResolvedType aspect) {
        String sourceFileName = null;
        if (aspect instanceof ReferenceType) {
            String s = ((ReferenceType) aspect).getDelegate().getSourcefilename();
            int i = s.lastIndexOf(47);
            if (i != -1) {
                sourceFileName = s.substring(i + 1);
            } else {
                sourceFileName = s;
            }
        }
        return sourceFileName;
    }

    private static File getBinaryFile(ResolvedType aspect) {
        String path;
        String s = aspect.getBinaryPath();
        File f = aspect.getSourceLocation().getSourceFile();
        int i = f.getPath().lastIndexOf(46);
        if (i != -1) {
            path = f.getPath().substring(0, i) + ClassUtils.CLASS_FILE_SUFFIX;
        } else {
            path = f.getPath() + ClassUtils.CLASS_FILE_SUFFIX;
        }
        return new File(s + "!" + path);
    }

    private static void createHierarchy(AsmManager model, ResolvedTypeMunger typeTransformer, ResolvedType aspect) {
        IProgramElement filenode = model.getHierarchy().findElementForSourceLine(typeTransformer.getSourceLocation());
        if ((filenode == null && (typeTransformer.getKind() == ResolvedTypeMunger.MethodDelegate2 || typeTransformer.getKind() == ResolvedTypeMunger.FieldHost)) || !filenode.getKind().equals(IProgramElement.Kind.FILE_JAVA)) {
            return;
        }
        ISourceLocation binLocation = getBinarySourceLocation(aspect, aspect.getSourceLocation());
        String f = getBinaryFile(aspect).getName();
        IProgramElement classFileNode = new ProgramElement(model, f, IProgramElement.Kind.FILE, binLocation, 0, null, null);
        IProgramElement root = model.getHierarchy().getRoot();
        IProgramElement binaries = model.getHierarchy().findElementForLabel(root, IProgramElement.Kind.SOURCE_FOLDER, "binaries");
        if (binaries == null) {
            binaries = new ProgramElement(model, "binaries", IProgramElement.Kind.SOURCE_FOLDER, new ArrayList());
            root.addChild(binaries);
        }
        String packagename = aspect.getPackageName() == null ? "" : aspect.getPackageName();
        IProgramElement pkgNode = model.getHierarchy().findElementForLabel(binaries, IProgramElement.Kind.PACKAGE, packagename);
        if (pkgNode == null) {
            IProgramElement pkgNode2 = new ProgramElement(model, packagename, IProgramElement.Kind.PACKAGE, new ArrayList());
            binaries.addChild(pkgNode2);
            pkgNode2.addChild(classFileNode);
        } else {
            pkgNode.addChild(classFileNode);
            for (IProgramElement element : pkgNode.getChildren()) {
                if (!element.equals(classFileNode) && element.getHandleIdentifier().equals(classFileNode.getHandleIdentifier())) {
                    pkgNode.removeChild(classFileNode);
                    return;
                }
            }
        }
        IProgramElement aspectNode = new ProgramElement(model, aspect.getSimpleName(), IProgramElement.Kind.ASPECT, getBinarySourceLocation(aspect, aspect.getSourceLocation()), aspect.getModifiers(), null, null);
        classFileNode.addChild(aspectNode);
        addChildNodes(model, aspect, aspectNode, aspect.getDeclaredPointcuts());
        addChildNodes(model, aspect, aspectNode, aspect.getDeclaredAdvice());
        addChildNodes(model, aspect, aspectNode, aspect.getDeclares());
        addChildNodes(model, aspect, aspectNode, aspect.getTypeMungers());
    }

    public static void addDeclareAnnotationRelationship(AsmManager model, ISourceLocation declareAnnotationLocation, ISourceLocation annotatedLocation, boolean isRemove) {
        IProgramElement sourceNode;
        String sourceHandle;
        if (model == null || (sourceHandle = (sourceNode = model.getHierarchy().findElementForSourceLine(declareAnnotationLocation)).getHandleIdentifier()) == null) {
            return;
        }
        IProgramElement targetNode = model.getHierarchy().findElementForSourceLine(annotatedLocation);
        String targetHandle = targetNode.getHandleIdentifier();
        if (targetHandle == null) {
            return;
        }
        IRelationshipMap mapper = model.getRelationshipMap();
        IRelationship foreward = mapper.get(sourceHandle, IRelationship.Kind.DECLARE_INTER_TYPE, ANNOTATES, false, true);
        foreward.addTarget(targetHandle);
        IRelationship back = mapper.get(targetHandle, IRelationship.Kind.DECLARE_INTER_TYPE, ANNOTATED_BY, false, true);
        back.addTarget(sourceHandle);
        if (sourceNode.getSourceLocation() != null) {
            model.addAspectInEffectThisBuild(sourceNode.getSourceLocation().getSourceFile());
        }
    }

    public static void createHierarchyForBinaryAspect(AsmManager asm, ShadowMunger munger) {
        if (!munger.isBinary()) {
            return;
        }
        IProgramElement sourceFileNode = asm.getHierarchy().findElementForSourceLine(munger.getSourceLocation());
        if (!sourceFileNode.getKind().equals(IProgramElement.Kind.FILE_JAVA)) {
            return;
        }
        ResolvedType aspect = munger.getDeclaringType();
        IProgramElement classFileNode = new ProgramElement(asm, sourceFileNode.getName(), IProgramElement.Kind.FILE, munger.getBinarySourceLocation(aspect.getSourceLocation()), 0, null, null);
        IProgramElement root = asm.getHierarchy().getRoot();
        IProgramElement binaries = asm.getHierarchy().findElementForLabel(root, IProgramElement.Kind.SOURCE_FOLDER, "binaries");
        if (binaries == null) {
            binaries = new ProgramElement(asm, "binaries", IProgramElement.Kind.SOURCE_FOLDER, new ArrayList());
            root.addChild(binaries);
        }
        String packagename = aspect.getPackageName() == null ? "" : aspect.getPackageName();
        IProgramElement pkgNode = asm.getHierarchy().findElementForLabel(binaries, IProgramElement.Kind.PACKAGE, packagename);
        if (pkgNode == null) {
            IProgramElement pkgNode2 = new ProgramElement(asm, packagename, IProgramElement.Kind.PACKAGE, new ArrayList());
            binaries.addChild(pkgNode2);
            pkgNode2.addChild(classFileNode);
        } else {
            pkgNode.addChild(classFileNode);
            for (IProgramElement element : pkgNode.getChildren()) {
                if (!element.equals(classFileNode) && element.getHandleIdentifier().equals(classFileNode.getHandleIdentifier())) {
                    pkgNode.removeChild(classFileNode);
                    return;
                }
            }
        }
        IProgramElement aspectNode = new ProgramElement(asm, aspect.getSimpleName(), IProgramElement.Kind.ASPECT, munger.getBinarySourceLocation(aspect.getSourceLocation()), aspect.getModifiers(), null, null);
        classFileNode.addChild(aspectNode);
        String sourcefilename = getSourceFileName(aspect);
        addPointcuts(asm, sourcefilename, aspect, aspectNode, aspect.getDeclaredPointcuts());
        addChildNodes(asm, aspect, aspectNode, aspect.getDeclaredAdvice());
        addChildNodes(asm, aspect, aspectNode, aspect.getDeclares());
        addChildNodes(asm, aspect, aspectNode, aspect.getTypeMungers());
    }

    private static void addPointcuts(AsmManager model, String sourcefilename, ResolvedType aspect, IProgramElement containingAspect, ResolvedMember[] pointcuts) {
        for (ResolvedMember pointcut : pointcuts) {
            if (pointcut instanceof ResolvedPointcutDefinition) {
                ResolvedPointcutDefinition rpcd = (ResolvedPointcutDefinition) pointcut;
                Pointcut p = rpcd.getPointcut();
                ISourceLocation sLoc = p == null ? null : p.getSourceLocation();
                if (sLoc == null) {
                    sLoc = rpcd.getSourceLocation();
                }
                ISourceLocation pointcutLocation = sLoc == null ? null : createSourceLocation(sourcefilename, aspect, sLoc);
                ProgramElement pointcutElement = new ProgramElement(model, pointcut.getName(), IProgramElement.Kind.POINTCUT, pointcutLocation, pointcut.getModifiers(), NO_COMMENT, Collections.emptyList());
                containingAspect.addChild(pointcutElement);
            }
        }
    }

    private static void addChildNodes(AsmManager asm, ResolvedType aspect, IProgramElement parent, ResolvedMember[] children) {
        for (ResolvedMember pcd : children) {
            if (pcd instanceof ResolvedPointcutDefinition) {
                ResolvedPointcutDefinition rpcd = (ResolvedPointcutDefinition) pcd;
                Pointcut p = rpcd.getPointcut();
                ISourceLocation sLoc = p == null ? null : p.getSourceLocation();
                if (sLoc == null) {
                    sLoc = rpcd.getSourceLocation();
                }
                parent.addChild(new ProgramElement(asm, pcd.getName(), IProgramElement.Kind.POINTCUT, getBinarySourceLocation(aspect, sLoc), pcd.getModifiers(), null, Collections.emptyList()));
            }
        }
    }

    private static void addChildNodes(AsmManager asm, ResolvedType aspect, IProgramElement parent, Collection<?> children) {
        int i;
        IProgramElement newChild;
        int deCtr = 1;
        int dwCtr = 1;
        for (Object element : children) {
            if (element instanceof DeclareErrorOrWarning) {
                DeclareErrorOrWarning decl = (DeclareErrorOrWarning) element;
                if (decl.isError()) {
                    i = deCtr;
                    deCtr++;
                } else {
                    i = dwCtr;
                    dwCtr++;
                }
                int counter = i;
                parent.addChild(createDeclareErrorOrWarningChild(asm, aspect, decl, counter));
            } else if (element instanceof Advice) {
                Advice advice = (Advice) element;
                parent.addChild(createAdviceChild(asm, advice));
            } else if (element instanceof DeclareParents) {
                parent.addChild(createDeclareParentsChild(asm, (DeclareParents) element));
            } else if ((element instanceof BcelTypeMunger) && (newChild = createIntertypeDeclaredChild(asm, aspect, (BcelTypeMunger) element)) != null) {
                parent.addChild(newChild);
            }
        }
    }

    private static IProgramElement createDeclareErrorOrWarningChild(AsmManager model, ResolvedType aspect, DeclareErrorOrWarning decl, int count) {
        IProgramElement deowNode = new ProgramElement(model, decl.getName(), decl.isError() ? IProgramElement.Kind.DECLARE_ERROR : IProgramElement.Kind.DECLARE_WARNING, getBinarySourceLocation(aspect, decl.getSourceLocation()), decl.getDeclaringType().getModifiers(), null, null);
        deowNode.setDetails(SymbolConstants.QUOTES_SYMBOL + AsmRelationshipUtils.genDeclareMessage(decl.getMessage()) + SymbolConstants.QUOTES_SYMBOL);
        if (count != -1) {
            deowNode.setBytecodeName(decl.getName() + "_" + count);
        }
        return deowNode;
    }

    private static IProgramElement createAdviceChild(AsmManager model, Advice advice) {
        IProgramElement adviceNode = new ProgramElement(model, advice.getKind().getName(), IProgramElement.Kind.ADVICE, advice.getBinarySourceLocation(advice.getSourceLocation()), advice.getSignature().getModifiers(), null, Collections.emptyList());
        adviceNode.setDetails(AsmRelationshipUtils.genPointcutDetails(advice.getPointcut()));
        adviceNode.setBytecodeName(advice.getSignature().getName());
        return adviceNode;
    }

    private static IProgramElement createIntertypeDeclaredChild(AsmManager model, ResolvedType aspect, BcelTypeMunger itd) {
        ResolvedTypeMunger rtMunger = itd.getMunger();
        ResolvedMember sig = rtMunger.getSignature();
        ResolvedTypeMunger.Kind kind = rtMunger.getKind();
        if (kind == ResolvedTypeMunger.Field) {
            String name = sig.getDeclaringType().getClassName() + "." + sig.getName();
            if (name.indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) != -1) {
                name = name.substring(name.indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) + 1);
            }
            IProgramElement pe = new ProgramElement(model, name, IProgramElement.Kind.INTER_TYPE_FIELD, getBinarySourceLocation(aspect, itd.getSourceLocation()), rtMunger.getSignature().getModifiers(), null, Collections.emptyList());
            pe.setCorrespondingType(sig.getReturnType().getName());
            return pe;
        }
        if (kind == ResolvedTypeMunger.Method) {
            String name2 = sig.getDeclaringType().getClassName() + "." + sig.getName();
            if (name2.indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) != -1) {
                name2 = name2.substring(name2.indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) + 1);
            }
            IProgramElement pe2 = new ProgramElement(model, name2, IProgramElement.Kind.INTER_TYPE_METHOD, getBinarySourceLocation(aspect, itd.getSourceLocation()), rtMunger.getSignature().getModifiers(), null, Collections.emptyList());
            setParams(pe2, sig);
            return pe2;
        }
        if (kind == ResolvedTypeMunger.Constructor) {
            String name3 = sig.getDeclaringType().getClassName() + "." + sig.getDeclaringType().getClassName();
            if (name3.indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) != -1) {
                name3 = name3.substring(name3.indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) + 1);
            }
            IProgramElement pe3 = new ProgramElement(model, name3, IProgramElement.Kind.INTER_TYPE_CONSTRUCTOR, getBinarySourceLocation(aspect, itd.getSourceLocation()), rtMunger.getSignature().getModifiers(), null, Collections.emptyList());
            setParams(pe3, sig);
            return pe3;
        }
        return null;
    }

    private static void setParams(IProgramElement pe, ResolvedMember sig) {
        UnresolvedType[] ts = sig.getParameterTypes();
        pe.setParameterNames(Collections.emptyList());
        if (ts == null) {
            pe.setParameterSignatures(Collections.emptyList(), Collections.emptyList());
        } else {
            List<char[]> paramSigs = new ArrayList<>();
            for (UnresolvedType unresolvedType : ts) {
                paramSigs.add(unresolvedType.getSignature().toCharArray());
            }
            pe.setParameterSignatures(paramSigs, Collections.emptyList());
        }
        pe.setCorrespondingType(sig.getReturnType().getName());
    }

    private static IProgramElement createDeclareParentsChild(AsmManager model, DeclareParents decp) {
        IProgramElement decpElement = new ProgramElement(model, "declare parents", IProgramElement.Kind.DECLARE_PARENTS, getBinarySourceLocation(decp.getDeclaringType(), decp.getSourceLocation()), 1, null, Collections.emptyList());
        setParentTypesOnDeclareParentsNode(decp, decpElement);
        return decpElement;
    }

    private static void setParentTypesOnDeclareParentsNode(DeclareParents decp, IProgramElement decpElement) {
        TypePatternList tpl = decp.getParents();
        List<String> parents = new ArrayList<>();
        for (int i = 0; i < tpl.size(); i++) {
            parents.add(tpl.get(i).getExactType().getName().replaceAll("\\$", "."));
        }
        decpElement.setParentTypes(parents);
    }

    public static String getHandle(AsmManager asm, Advice advice) {
        ISourceLocation sl;
        if (null == advice.handle && (sl = advice.getSourceLocation()) != null) {
            IProgramElement ipe = asm.getHierarchy().findElementForSourceLine(sl);
            advice.handle = ipe.getHandleIdentifier();
        }
        return advice.handle;
    }

    public static void addAdvisedRelationship(AsmManager model, Shadow matchedShadow, ShadowMunger munger) {
        if (model != null && (munger instanceof Advice)) {
            Advice advice = (Advice) munger;
            if (advice.getKind().isPerEntry() || advice.getKind().isCflow()) {
                return;
            }
            if (World.createInjarHierarchy) {
                createHierarchyForBinaryAspect(model, advice);
            }
            IRelationshipMap mapper = model.getRelationshipMap();
            IProgramElement targetNode = getNode(model, matchedShadow);
            if (targetNode == null) {
                return;
            }
            boolean runtimeTest = advice.hasDynamicTests();
            IProgramElement.ExtraInformation extra = new IProgramElement.ExtraInformation();
            String adviceHandle = getHandle(model, advice);
            if (adviceHandle == null) {
                return;
            }
            extra.setExtraAdviceInformation(advice.getKind().getName());
            IProgramElement adviceElement = model.getHierarchy().findElementForHandle(adviceHandle);
            if (adviceElement != null) {
                adviceElement.setExtraInfo(extra);
            }
            String targetHandle = targetNode.getHandleIdentifier();
            if (advice.getKind().equals(AdviceKind.Softener)) {
                IRelationship foreward = mapper.get(adviceHandle, IRelationship.Kind.DECLARE_SOFT, SOFTENS, runtimeTest, true);
                if (foreward != null) {
                    foreward.addTarget(targetHandle);
                }
                IRelationship back = mapper.get(targetHandle, IRelationship.Kind.DECLARE, SOFTENED_BY, runtimeTest, true);
                if (back != null) {
                    back.addTarget(adviceHandle);
                }
            } else {
                IRelationship foreward2 = mapper.get(adviceHandle, IRelationship.Kind.ADVICE, ADVISES, runtimeTest, true);
                if (foreward2 != null) {
                    foreward2.addTarget(targetHandle);
                }
                IRelationship back2 = mapper.get(targetHandle, IRelationship.Kind.ADVICE, ADVISED_BY, runtimeTest, true);
                if (back2 != null) {
                    back2.addTarget(adviceHandle);
                }
            }
            if (adviceElement.getSourceLocation() != null) {
                model.addAspectInEffectThisBuild(adviceElement.getSourceLocation().getSourceFile());
            }
        }
    }

    protected static IProgramElement getNode(AsmManager model, Shadow shadow) {
        IProgramElement enclosingNode;
        Member actualEnclosingMember;
        Member enclosingMember = shadow.getEnclosingCodeSignature();
        if (!(shadow instanceof BcelShadow) || (actualEnclosingMember = ((BcelShadow) shadow).getRealEnclosingCodeSignature()) == null) {
            enclosingNode = lookupMember(model.getHierarchy(), shadow.getEnclosingType(), enclosingMember);
        } else {
            UnresolvedType type = enclosingMember.getDeclaringType();
            UnresolvedType actualType = actualEnclosingMember.getDeclaringType();
            if (type.equals(actualType)) {
                enclosingNode = lookupMember(model.getHierarchy(), shadow.getEnclosingType(), enclosingMember);
            } else {
                enclosingNode = lookupMember(model.getHierarchy(), shadow.getEnclosingType(), actualEnclosingMember);
            }
        }
        if (enclosingNode == null) {
            Lint.Kind err = shadow.getIWorld().getLint().shadowNotInStructure;
            if (err.isEnabled()) {
                err.signal(shadow.toString(), shadow.getSourceLocation());
                return null;
            }
            return null;
        }
        Member shadowSig = shadow.getSignature();
        if (shadow.getKind() == Shadow.MethodCall || shadow.getKind() == Shadow.ConstructorCall || !shadowSig.equals(enclosingMember)) {
            IProgramElement bodyNode = findOrCreateCodeNode(model, enclosingNode, shadowSig, shadow);
            return bodyNode;
        }
        return enclosingNode;
    }

    private static boolean sourceLinesMatch(ISourceLocation location1, ISourceLocation location2) {
        return location1.getLine() == location2.getLine();
    }

    private static IProgramElement findOrCreateCodeNode(AsmManager asm, IProgramElement enclosingNode, Member shadowSig, Shadow shadow) {
        for (IProgramElement node : enclosingNode.getChildren()) {
            int excl = node.getBytecodeName().lastIndexOf(33);
            if ((excl != -1 && shadowSig.getName().equals(node.getBytecodeName().substring(0, excl))) || shadowSig.getName().equals(node.getBytecodeName())) {
                if (shadowSig.getSignature().equals(node.getBytecodeSignature()) && sourceLinesMatch(node.getSourceLocation(), shadow.getSourceLocation())) {
                    return node;
                }
            }
        }
        ISourceLocation sl = shadow.getSourceLocation();
        SourceLocation peLoc = new SourceLocation(enclosingNode.getSourceLocation().getSourceFile(), sl.getLine());
        peLoc.setOffset(sl.getOffset());
        IProgramElement peNode = new ProgramElement(asm, shadow.toString(), IProgramElement.Kind.CODE, peLoc, 0, null, null);
        int numberOfChildrenWithThisName = 0;
        for (IProgramElement child : enclosingNode.getChildren()) {
            if (child.getName().equals(shadow.toString())) {
                numberOfChildrenWithThisName++;
            }
        }
        peNode.setBytecodeName(shadowSig.getName() + "!" + String.valueOf(numberOfChildrenWithThisName + 1));
        peNode.setBytecodeSignature(shadowSig.getSignature());
        enclosingNode.addChild(peNode);
        return peNode;
    }

    private static IProgramElement lookupMember(IHierarchy model, UnresolvedType declaringType, Member member) {
        IProgramElement typeElement = model.findElementForType(declaringType.getPackageName(), declaringType.getClassName());
        if (typeElement == null) {
            return null;
        }
        for (IProgramElement element : typeElement.getChildren()) {
            if (member.getName().equals(element.getBytecodeName()) && member.getSignature().equals(element.getBytecodeSignature())) {
                return element;
            }
        }
        return typeElement;
    }

    public static void addDeclareAnnotationMethodRelationship(ISourceLocation sourceLocation, String affectedTypeName, ResolvedMember affectedMethod, AsmManager model) {
        IProgramElement methodElem;
        if (model == null) {
            return;
        }
        String pkg = null;
        String type = affectedTypeName;
        int packageSeparator = affectedTypeName.lastIndexOf(".");
        if (packageSeparator != -1) {
            pkg = affectedTypeName.substring(0, packageSeparator);
            type = affectedTypeName.substring(packageSeparator + 1);
        }
        IHierarchy hierarchy = model.getHierarchy();
        IProgramElement typeElem = hierarchy.findElementForType(pkg, type);
        if (typeElem == null) {
            return;
        }
        if (!typeElem.getKind().isType()) {
            throw new IllegalStateException("Did not find a type element, found a " + typeElem.getKind() + " element");
        }
        StringBuilder parmString = new StringBuilder("(");
        UnresolvedType[] args = affectedMethod.getParameterTypes();
        for (int i = 0; i < args.length; i++) {
            parmString.append(args[i].getName());
            if (i + 1 < args.length) {
                parmString.append(",");
            }
        }
        parmString.append(")");
        if (affectedMethod.getName().startsWith("<init>")) {
            methodElem = hierarchy.findElementForSignature(typeElem, IProgramElement.Kind.CONSTRUCTOR, type + ((Object) parmString));
            if (methodElem == null && args.length == 0) {
                methodElem = typeElem;
            }
        } else {
            methodElem = hierarchy.findElementForSignature(typeElem, IProgramElement.Kind.METHOD, affectedMethod.getName() + ((Object) parmString));
        }
        if (methodElem == null) {
            return;
        }
        try {
            String targetHandle = methodElem.getHandleIdentifier();
            if (targetHandle == null) {
                return;
            }
            IProgramElement sourceNode = hierarchy.findElementForSourceLine(sourceLocation);
            String sourceHandle = sourceNode.getHandleIdentifier();
            if (sourceHandle == null) {
                return;
            }
            IRelationshipMap mapper = model.getRelationshipMap();
            IRelationship foreward = mapper.get(sourceHandle, IRelationship.Kind.DECLARE_INTER_TYPE, ANNOTATES, false, true);
            foreward.addTarget(targetHandle);
            IRelationship back = mapper.get(targetHandle, IRelationship.Kind.DECLARE_INTER_TYPE, ANNOTATED_BY, false, true);
            back.addTarget(sourceHandle);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void addDeclareAnnotationFieldRelationship(AsmManager model, ISourceLocation declareLocation, String affectedTypeName, ResolvedMember affectedFieldName, boolean isRemove) {
        IProgramElement fieldElem;
        String targetHandle;
        if (model == null) {
            return;
        }
        String pkg = null;
        String type = affectedTypeName;
        int packageSeparator = affectedTypeName.lastIndexOf(".");
        if (packageSeparator != -1) {
            pkg = affectedTypeName.substring(0, packageSeparator);
            type = affectedTypeName.substring(packageSeparator + 1);
        }
        IHierarchy hierarchy = model.getHierarchy();
        IProgramElement typeElem = hierarchy.findElementForType(pkg, type);
        if (typeElem == null || (fieldElem = hierarchy.findElementForSignature(typeElem, IProgramElement.Kind.FIELD, affectedFieldName.getName())) == null || (targetHandle = fieldElem.getHandleIdentifier()) == null) {
            return;
        }
        IProgramElement sourceNode = hierarchy.findElementForSourceLine(declareLocation);
        String sourceHandle = sourceNode.getHandleIdentifier();
        if (sourceHandle == null) {
            return;
        }
        IRelationshipMap relmap = model.getRelationshipMap();
        IRelationship foreward = relmap.get(sourceHandle, IRelationship.Kind.DECLARE_INTER_TYPE, ANNOTATES, false, true);
        foreward.addTarget(targetHandle);
        IRelationship back = relmap.get(targetHandle, IRelationship.Kind.DECLARE_INTER_TYPE, ANNOTATED_BY, false, true);
        back.addTarget(sourceHandle);
    }
}
