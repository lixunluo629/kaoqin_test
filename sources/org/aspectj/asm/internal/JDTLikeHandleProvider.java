package org.aspectj.asm.internal;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.util.List;
import org.aspectj.asm.AsmManager;
import org.aspectj.asm.IElementHandleProvider;
import org.aspectj.asm.IProgramElement;
import org.aspectj.bridge.ISourceLocation;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/internal/JDTLikeHandleProvider.class */
public class JDTLikeHandleProvider implements IElementHandleProvider {
    private final AsmManager asm;
    private static final char[] empty = new char[0];
    private static final char[] countDelim = {HandleProviderDelimiter.COUNT.getDelimiter()};
    private static final String backslash = "\\";
    private static final String emptyString = "";

    public JDTLikeHandleProvider(AsmManager asm) {
        this.asm = asm;
    }

    @Override // org.aspectj.asm.IElementHandleProvider
    public void initialize() {
    }

    @Override // org.aspectj.asm.IElementHandleProvider
    public String createHandleIdentifier(IProgramElement ipe) {
        String configFile;
        if (ipe == null) {
            return "";
        }
        if (ipe.getKind().equals(IProgramElement.Kind.FILE_JAVA) && ipe.getName().equals("<root>")) {
            return "";
        }
        if (ipe.getHandleIdentifier(false) != null) {
            return ipe.getHandleIdentifier();
        }
        if (ipe.getKind().equals(IProgramElement.Kind.FILE_LST)) {
            String configFile2 = this.asm.getHierarchy().getConfigFile();
            int start = configFile2.lastIndexOf(File.separator);
            int end = configFile2.lastIndexOf(".lst");
            if (end != -1) {
                configFile = configFile2.substring(start + 1, end);
            } else {
                configFile = new StringBuffer(SymbolConstants.EQUAL_SYMBOL).append(configFile2.substring(start + 1)).toString();
            }
            ipe.setHandleIdentifier(configFile);
            return configFile;
        }
        if (ipe.getKind() == IProgramElement.Kind.SOURCE_FOLDER) {
            StringBuffer sb = new StringBuffer();
            sb.append(createHandleIdentifier(ipe.getParent())).append("/");
            String folder = ipe.getName();
            if (folder.endsWith("/")) {
                folder = folder.substring(0, folder.length() - 1);
            }
            if (folder.indexOf("/") != -1) {
                folder = folder.replace("/", "\\/");
            }
            sb.append(folder);
            String handle = sb.toString();
            ipe.setHandleIdentifier(handle);
            return handle;
        }
        IProgramElement parent = ipe.getParent();
        if (parent != null && parent.getKind().equals(IProgramElement.Kind.IMPORT_REFERENCE)) {
            parent = ipe.getParent().getParent();
        }
        StringBuffer handle2 = new StringBuffer();
        handle2.append(createHandleIdentifier(parent));
        handle2.append(HandleProviderDelimiter.getDelimiter(ipe));
        if (!ipe.getKind().equals(IProgramElement.Kind.INITIALIZER) && (ipe.getKind() != IProgramElement.Kind.CLASS || !ipe.getName().endsWith("{..}"))) {
            if (ipe.getKind() == IProgramElement.Kind.INTER_TYPE_CONSTRUCTOR) {
                handle2.append(ipe.getName()).append("_new").append(getParameters(ipe));
            } else if (ipe.getKind().isDeclareAnnotation()) {
                handle2.append("declare \\@").append(ipe.getName().substring(9)).append(getParameters(ipe));
            } else {
                if (ipe.getFullyQualifiedName() != null) {
                    handle2.append(ipe.getFullyQualifiedName());
                } else {
                    handle2.append(ipe.getName());
                }
                handle2.append(getParameters(ipe));
            }
        }
        handle2.append(getCount(ipe));
        ipe.setHandleIdentifier(handle2.toString());
        return handle2.toString();
    }

    private String getParameters(IProgramElement ipe) {
        if (ipe.getParameterSignatures() == null || ipe.getParameterSignatures().isEmpty()) {
            return "";
        }
        List<String> sourceRefs = ipe.getParameterSignaturesSourceRefs();
        List<char[]> parameterTypes = ipe.getParameterSignatures();
        StringBuffer sb = new StringBuffer();
        if (sourceRefs != null) {
            for (int i = 0; i < sourceRefs.size(); i++) {
                String sourceRef = sourceRefs.get(i);
                sb.append(HandleProviderDelimiter.getDelimiter(ipe));
                sb.append(sourceRef);
            }
        } else {
            for (char[] element : parameterTypes) {
                sb.append(HandleProviderDelimiter.getDelimiter(ipe));
                sb.append(NameConvertor.createShortName(element, false, false));
            }
        }
        return sb.toString();
    }

    private char[] getCount(IProgramElement ipe) {
        int idx;
        int idx2;
        char[] byteCodeName = ipe.getBytecodeName().toCharArray();
        if (ipe.getKind().isInterTypeMember()) {
            int count = 1;
            for (IProgramElement object : ipe.getParent().getChildren()) {
                if (object.equals(ipe)) {
                    break;
                }
                if (object.getKind().isInterTypeMember() && object.getName().equals(ipe.getName()) && getParameters(object).equals(getParameters(ipe))) {
                    String existingHandle = object.getHandleIdentifier();
                    int suffixPosition = existingHandle.indexOf(33);
                    if (suffixPosition != -1) {
                        count = new Integer(existingHandle.substring(suffixPosition + 1)).intValue() + 1;
                    } else if (count == 1) {
                        count = 2;
                    }
                }
            }
            if (count > 1) {
                return CharOperation.concat(countDelim, new Integer(count).toString().toCharArray());
            }
        } else if (ipe.getKind().isDeclare()) {
            int count2 = computeCountBasedOnPeers(ipe);
            if (count2 > 1) {
                return CharOperation.concat(countDelim, new Integer(count2).toString().toCharArray());
            }
        } else if (ipe.getKind().equals(IProgramElement.Kind.ADVICE)) {
            int count3 = 1;
            List<IProgramElement> kids = ipe.getParent().getChildren();
            String ipeSig = shortenIpeSig(ipe.getBytecodeSignature());
            for (IProgramElement object2 : kids) {
                if (object2.equals(ipe)) {
                    break;
                }
                if (object2.getKind() == ipe.getKind() && object2.getName().equals(ipe.getName())) {
                    String sig1 = object2.getBytecodeSignature();
                    if (sig1 != null && (idx2 = sig1.indexOf(")")) != -1) {
                        sig1 = sig1.substring(0, idx2);
                    }
                    if (sig1 != null && sig1.indexOf("Lorg/aspectj/lang") != -1) {
                        if (sig1.endsWith("Lorg/aspectj/lang/JoinPoint$StaticPart;")) {
                            sig1 = sig1.substring(0, sig1.lastIndexOf("Lorg/aspectj/lang/JoinPoint$StaticPart;"));
                        }
                        if (sig1.endsWith("Lorg/aspectj/lang/JoinPoint;")) {
                            sig1 = sig1.substring(0, sig1.lastIndexOf("Lorg/aspectj/lang/JoinPoint;"));
                        }
                        if (sig1.endsWith("Lorg/aspectj/lang/JoinPoint$StaticPart;")) {
                            sig1 = sig1.substring(0, sig1.lastIndexOf("Lorg/aspectj/lang/JoinPoint$StaticPart;"));
                        }
                    }
                    if ((sig1 == null && ipeSig == null) || (sig1 != null && sig1.equals(ipeSig))) {
                        String existingHandle2 = object2.getHandleIdentifier();
                        int suffixPosition2 = existingHandle2.indexOf(33);
                        if (suffixPosition2 != -1) {
                            count3 = new Integer(existingHandle2.substring(suffixPosition2 + 1)).intValue() + 1;
                        } else if (count3 == 1) {
                            count3 = 2;
                        }
                    }
                }
            }
            if (count3 > 1) {
                return CharOperation.concat(countDelim, new Integer(count3).toString().toCharArray());
            }
        } else {
            if (ipe.getKind().equals(IProgramElement.Kind.INITIALIZER)) {
                int count4 = 1;
                List<IProgramElement> kids2 = ipe.getParent().getChildren();
                String ipeSig2 = shortenIpeSig(ipe.getBytecodeSignature());
                for (IProgramElement object3 : kids2) {
                    if (object3.equals(ipe)) {
                        break;
                    }
                    if (object3.getKind() == ipe.getKind() && object3.getName().equals(ipe.getName())) {
                        String sig12 = object3.getBytecodeSignature();
                        if (sig12 != null && (idx = sig12.indexOf(")")) != -1) {
                            sig12 = sig12.substring(0, idx);
                        }
                        if (sig12 != null && sig12.indexOf("Lorg/aspectj/lang") != -1) {
                            if (sig12.endsWith("Lorg/aspectj/lang/JoinPoint$StaticPart;")) {
                                sig12 = sig12.substring(0, sig12.lastIndexOf("Lorg/aspectj/lang/JoinPoint$StaticPart;"));
                            }
                            if (sig12.endsWith("Lorg/aspectj/lang/JoinPoint;")) {
                                sig12 = sig12.substring(0, sig12.lastIndexOf("Lorg/aspectj/lang/JoinPoint;"));
                            }
                            if (sig12.endsWith("Lorg/aspectj/lang/JoinPoint$StaticPart;")) {
                                sig12 = sig12.substring(0, sig12.lastIndexOf("Lorg/aspectj/lang/JoinPoint$StaticPart;"));
                            }
                        }
                        if ((sig12 == null && ipeSig2 == null) || (sig12 != null && sig12.equals(ipeSig2))) {
                            String existingHandle3 = object3.getHandleIdentifier();
                            int suffixPosition3 = existingHandle3.indexOf(33);
                            if (suffixPosition3 != -1) {
                                count4 = new Integer(existingHandle3.substring(suffixPosition3 + 1)).intValue() + 1;
                            } else if (count4 == 1) {
                                count4 = 2;
                            }
                        }
                    }
                }
                return new Integer(count4).toString().toCharArray();
            }
            if (ipe.getKind().equals(IProgramElement.Kind.CODE)) {
                int index = CharOperation.lastIndexOf('!', byteCodeName);
                if (index != -1) {
                    return convertCount(CharOperation.subarray(byteCodeName, index + 1, byteCodeName.length));
                }
            } else if (ipe.getKind() == IProgramElement.Kind.CLASS) {
                int count5 = 1;
                List<IProgramElement> kids3 = ipe.getParent().getChildren();
                if (ipe.getName().endsWith("{..}")) {
                    for (IProgramElement object4 : kids3) {
                        if (object4.equals(ipe)) {
                            break;
                        }
                        if (object4.getKind() == ipe.getKind() && object4.getName().endsWith("{..}")) {
                            String existingHandle4 = object4.getHandleIdentifier();
                            int suffixPosition4 = existingHandle4.lastIndexOf(33);
                            int lastSquareBracket = existingHandle4.lastIndexOf(91);
                            if (suffixPosition4 != -1 && lastSquareBracket < suffixPosition4) {
                                count5 = new Integer(existingHandle4.substring(suffixPosition4 + 1)).intValue() + 1;
                            } else if (count5 == 1) {
                                count5 = 2;
                            }
                        }
                    }
                } else {
                    for (IProgramElement object5 : kids3) {
                        if (object5.equals(ipe)) {
                            break;
                        }
                        if (object5.getKind() == ipe.getKind() && object5.getName().equals(ipe.getName())) {
                            String existingHandle5 = object5.getHandleIdentifier();
                            int suffixPosition5 = existingHandle5.lastIndexOf(33);
                            int lastSquareBracket2 = existingHandle5.lastIndexOf(91);
                            if (suffixPosition5 != -1 && lastSquareBracket2 < suffixPosition5) {
                                count5 = new Integer(existingHandle5.substring(suffixPosition5 + 1)).intValue() + 1;
                            } else if (count5 == 1) {
                                count5 = 2;
                            }
                        }
                    }
                }
                if (count5 > 1) {
                    return CharOperation.concat(countDelim, new Integer(count5).toString().toCharArray());
                }
            }
        }
        return empty;
    }

    private String shortenIpeSig(String ipeSig) {
        int idx;
        if (ipeSig != null && (idx = ipeSig.indexOf(")")) != -1) {
            ipeSig = ipeSig.substring(0, idx);
        }
        if (ipeSig != null && ipeSig.indexOf("Lorg/aspectj/lang") != -1) {
            if (ipeSig.endsWith("Lorg/aspectj/lang/JoinPoint$StaticPart;")) {
                ipeSig = ipeSig.substring(0, ipeSig.lastIndexOf("Lorg/aspectj/lang/JoinPoint$StaticPart;"));
            }
            if (ipeSig.endsWith("Lorg/aspectj/lang/JoinPoint;")) {
                ipeSig = ipeSig.substring(0, ipeSig.lastIndexOf("Lorg/aspectj/lang/JoinPoint;"));
            }
            if (ipeSig.endsWith("Lorg/aspectj/lang/JoinPoint$StaticPart;")) {
                ipeSig = ipeSig.substring(0, ipeSig.lastIndexOf("Lorg/aspectj/lang/JoinPoint$StaticPart;"));
            }
        }
        return ipeSig;
    }

    private int computeCountBasedOnPeers(IProgramElement ipe) {
        int count = 1;
        for (IProgramElement object : ipe.getParent().getChildren()) {
            if (object.equals(ipe)) {
                break;
            }
            if (object.getKind() == ipe.getKind() && object.getKind().toString().equals(ipe.getKind().toString())) {
                String existingHandle = object.getHandleIdentifier();
                int suffixPosition = existingHandle.indexOf(33);
                if (suffixPosition != -1) {
                    count = new Integer(existingHandle.substring(suffixPosition + 1)).intValue() + 1;
                } else if (count == 1) {
                    count = 2;
                }
            }
        }
        return count;
    }

    private char[] convertCount(char[] c) {
        if ((c.length == 1 && c[0] != ' ' && c[0] != '1') || c.length > 1) {
            return CharOperation.concat(countDelim, c);
        }
        return empty;
    }

    @Override // org.aspectj.asm.IElementHandleProvider
    public String getFileForHandle(String handle) {
        IProgramElement node = this.asm.getHierarchy().getElement(handle);
        if (node != null) {
            return this.asm.getCanonicalFilePath(node.getSourceLocation().getSourceFile());
        }
        if (handle.charAt(0) == HandleProviderDelimiter.ASPECT_CU.getDelimiter() || handle.charAt(0) == HandleProviderDelimiter.COMPILATIONUNIT.getDelimiter()) {
            return backslash + handle.substring(1);
        }
        return "";
    }

    @Override // org.aspectj.asm.IElementHandleProvider
    public int getLineNumberForHandle(String handle) {
        IProgramElement node = this.asm.getHierarchy().getElement(handle);
        if (node != null) {
            return node.getSourceLocation().getLine();
        }
        if (handle.charAt(0) == HandleProviderDelimiter.ASPECT_CU.getDelimiter() || handle.charAt(0) == HandleProviderDelimiter.COMPILATIONUNIT.getDelimiter()) {
            return 1;
        }
        return -1;
    }

    @Override // org.aspectj.asm.IElementHandleProvider
    public int getOffSetForHandle(String handle) {
        IProgramElement node = this.asm.getHierarchy().getElement(handle);
        if (node != null) {
            return node.getSourceLocation().getOffset();
        }
        if (handle.charAt(0) == HandleProviderDelimiter.ASPECT_CU.getDelimiter() || handle.charAt(0) == HandleProviderDelimiter.COMPILATIONUNIT.getDelimiter()) {
            return 0;
        }
        return -1;
    }

    @Override // org.aspectj.asm.IElementHandleProvider
    public String createHandleIdentifier(ISourceLocation location) {
        IProgramElement node = this.asm.getHierarchy().findElementForSourceLine(location);
        if (node != null) {
            return createHandleIdentifier(node);
        }
        return null;
    }

    @Override // org.aspectj.asm.IElementHandleProvider
    public String createHandleIdentifier(File sourceFile, int line, int column, int offset) {
        IProgramElement node = this.asm.getHierarchy().findElementForOffSet(sourceFile.getAbsolutePath(), line, offset);
        if (node != null) {
            return createHandleIdentifier(node);
        }
        return null;
    }

    public boolean dependsOnLocation() {
        return false;
    }
}
