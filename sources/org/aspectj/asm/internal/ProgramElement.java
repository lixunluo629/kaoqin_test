package org.aspectj.asm.internal;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlErrorCodes;
import org.aspectj.asm.AsmManager;
import org.aspectj.asm.HierarchyWalker;
import org.aspectj.asm.IProgramElement;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.bouncycastle.i18n.ErrorBundle;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/internal/ProgramElement.class */
public class ProgramElement implements IProgramElement {
    public transient AsmManager asm;
    private static final long serialVersionUID = 171673495267384449L;
    public static boolean shortITDNames;
    private static final String UNDEFINED = "<undefined>";
    private static final int AccPublic = 1;
    private static final int AccPrivate = 2;
    private static final int AccProtected = 4;
    private static final int AccPrivileged = 6;
    private static final int AccStatic = 8;
    private static final int AccFinal = 16;
    private static final int AccSynchronized = 32;
    private static final int AccVolatile = 64;
    private static final int AccTransient = 128;
    private static final int AccNative = 256;
    private static final int AccAbstract = 1024;
    protected String name;
    private IProgramElement.Kind kind;
    protected IProgramElement parent;
    protected List<IProgramElement> children;
    public Map<String, Object> kvpairs;
    protected ISourceLocation sourceLocation;
    public int modifiers;
    private String handle;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ProgramElement.class.desiredAssertionStatus();
        shortITDNames = true;
    }

    @Override // org.aspectj.asm.IProgramElement
    public AsmManager getModel() {
        return this.asm;
    }

    public ProgramElement() {
        this.parent = null;
        this.children = Collections.emptyList();
        this.kvpairs = Collections.emptyMap();
        this.sourceLocation = null;
        this.handle = null;
    }

    public ProgramElement(AsmManager asm, String name, IProgramElement.Kind kind, List<IProgramElement> children) {
        this.parent = null;
        this.children = Collections.emptyList();
        this.kvpairs = Collections.emptyMap();
        this.sourceLocation = null;
        this.handle = null;
        this.asm = asm;
        if (asm == null && !name.equals("<build to view structure>")) {
            throw new RuntimeException();
        }
        this.name = name;
        this.kind = kind;
        if (children != null) {
            setChildren(children);
        }
    }

    public ProgramElement(AsmManager asm, String name, IProgramElement.Kind kind, ISourceLocation sourceLocation, int modifiers, String comment, List<IProgramElement> children) {
        this(asm, name, kind, children);
        this.sourceLocation = sourceLocation;
        setFormalComment(comment);
        this.modifiers = modifiers;
    }

    @Override // org.aspectj.asm.IProgramElement
    public int getRawModifiers() {
        return this.modifiers;
    }

    @Override // org.aspectj.asm.IProgramElement
    public List<IProgramElement.Modifiers> getModifiers() {
        return genModifiers(this.modifiers);
    }

    @Override // org.aspectj.asm.IProgramElement
    public IProgramElement.Accessibility getAccessibility() {
        return genAccessibility(this.modifiers);
    }

    public void setDeclaringType(String t) {
        if (t != null && t.length() > 0) {
            fixMap();
            this.kvpairs.put("declaringType", t);
        }
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getDeclaringType() {
        String dt = (String) this.kvpairs.get("declaringType");
        if (dt == null) {
            return "";
        }
        return dt;
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getPackageName() {
        if (this.kind == IProgramElement.Kind.PACKAGE) {
            return getName();
        }
        if (getParent() == null) {
            return "";
        }
        return getParent().getPackageName();
    }

    @Override // org.aspectj.asm.IProgramElement
    public IProgramElement.Kind getKind() {
        return this.kind;
    }

    public boolean isCode() {
        return this.kind.equals(IProgramElement.Kind.CODE);
    }

    @Override // org.aspectj.asm.IProgramElement
    public ISourceLocation getSourceLocation() {
        return this.sourceLocation;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setSourceLocation(ISourceLocation sourceLocation) {
    }

    @Override // org.aspectj.asm.IProgramElement
    public IMessage getMessage() {
        return (IMessage) this.kvpairs.get(ConstraintHelper.MESSAGE);
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setMessage(IMessage message) {
        fixMap();
        this.kvpairs.put(ConstraintHelper.MESSAGE, message);
    }

    @Override // org.aspectj.asm.IProgramElement
    public IProgramElement getParent() {
        return this.parent;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setParent(IProgramElement parent) {
        this.parent = parent;
    }

    public boolean isMemberKind() {
        return this.kind.isMember();
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setRunnable(boolean value) {
        fixMap();
        if (value) {
            this.kvpairs.put("isRunnable", "true");
        } else {
            this.kvpairs.remove("isRunnable");
        }
    }

    @Override // org.aspectj.asm.IProgramElement
    public boolean isRunnable() {
        return this.kvpairs.get("isRunnable") != null;
    }

    @Override // org.aspectj.asm.IProgramElement
    public boolean isImplementor() {
        return this.kvpairs.get("isImplementor") != null;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setImplementor(boolean value) {
        fixMap();
        if (value) {
            this.kvpairs.put("isImplementor", "true");
        } else {
            this.kvpairs.remove("isImplementor");
        }
    }

    @Override // org.aspectj.asm.IProgramElement
    public boolean isOverrider() {
        return this.kvpairs.get("isOverrider") != null;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setOverrider(boolean value) {
        fixMap();
        if (value) {
            this.kvpairs.put("isOverrider", "true");
        } else {
            this.kvpairs.remove("isOverrider");
        }
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getFormalComment() {
        return (String) this.kvpairs.get("formalComment");
    }

    @Override // org.aspectj.asm.IProgramElement
    public String toString() {
        return toLabelString();
    }

    private static List<IProgramElement.Modifiers> genModifiers(int modifiers) {
        List<IProgramElement.Modifiers> modifiersList = new ArrayList<>();
        if ((modifiers & 8) != 0) {
            modifiersList.add(IProgramElement.Modifiers.STATIC);
        }
        if ((modifiers & 16) != 0) {
            modifiersList.add(IProgramElement.Modifiers.FINAL);
        }
        if ((modifiers & 32) != 0) {
            modifiersList.add(IProgramElement.Modifiers.SYNCHRONIZED);
        }
        if ((modifiers & 64) != 0) {
            modifiersList.add(IProgramElement.Modifiers.VOLATILE);
        }
        if ((modifiers & 128) != 0) {
            modifiersList.add(IProgramElement.Modifiers.TRANSIENT);
        }
        if ((modifiers & 256) != 0) {
            modifiersList.add(IProgramElement.Modifiers.NATIVE);
        }
        if ((modifiers & 1024) != 0) {
            modifiersList.add(IProgramElement.Modifiers.ABSTRACT);
        }
        return modifiersList;
    }

    public static IProgramElement.Accessibility genAccessibility(int modifiers) {
        if ((modifiers & 1) != 0) {
            return IProgramElement.Accessibility.PUBLIC;
        }
        if ((modifiers & 2) != 0) {
            return IProgramElement.Accessibility.PRIVATE;
        }
        if ((modifiers & 4) != 0) {
            return IProgramElement.Accessibility.PROTECTED;
        }
        if ((modifiers & 6) != 0) {
            return IProgramElement.Accessibility.PRIVILEGED;
        }
        return IProgramElement.Accessibility.PACKAGE;
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getBytecodeName() {
        String s = (String) this.kvpairs.get("bytecodeName");
        if (s == null) {
            return UNDEFINED;
        }
        return s;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setBytecodeName(String s) {
        fixMap();
        this.kvpairs.put("bytecodeName", s);
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setBytecodeSignature(String s) {
        fixMap();
        this.kvpairs.put("bytecodeSignature", s);
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getBytecodeSignature() {
        String s = (String) this.kvpairs.get("bytecodeSignature");
        return s;
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getSourceSignature() {
        return (String) this.kvpairs.get("sourceSignature");
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setSourceSignature(String string) {
        fixMap();
        this.kvpairs.put("sourceSignature", string);
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setKind(IProgramElement.Kind kind) {
        this.kind = kind;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setCorrespondingType(String s) {
        fixMap();
        this.kvpairs.put("returnType", s);
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setParentTypes(List<String> ps) {
        fixMap();
        this.kvpairs.put("parentTypes", ps);
    }

    @Override // org.aspectj.asm.IProgramElement
    public List<String> getParentTypes() {
        return (List) (this.kvpairs == null ? null : this.kvpairs.get("parentTypes"));
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setAnnotationType(String fullyQualifiedAnnotationType) {
        fixMap();
        this.kvpairs.put("annotationType", fullyQualifiedAnnotationType);
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setAnnotationRemover(boolean isRemover) {
        fixMap();
        this.kvpairs.put("annotationRemover", Boolean.valueOf(isRemover));
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getAnnotationType() {
        if (isAnnotationRemover()) {
            return null;
        }
        return (String) (this.kvpairs == null ? null : this.kvpairs.get("annotationType"));
    }

    @Override // org.aspectj.asm.IProgramElement
    public boolean isAnnotationRemover() {
        Boolean b;
        if (this.kvpairs == null || (b = (Boolean) this.kvpairs.get("annotationRemover")) == null) {
            return false;
        }
        return b.booleanValue();
    }

    @Override // org.aspectj.asm.IProgramElement
    public String[] getRemovedAnnotationTypes() {
        if (!isAnnotationRemover()) {
            return null;
        }
        String annotype = (String) (this.kvpairs == null ? null : this.kvpairs.get("annotationType"));
        if (annotype == null) {
            return null;
        }
        return new String[]{annotype};
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getCorrespondingType() {
        return getCorrespondingType(false);
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getCorrespondingTypeSignature() {
        String typename = (String) this.kvpairs.get("returnType");
        if (typename == null) {
            return null;
        }
        return nameToSignature(typename);
    }

    public static String nameToSignature(String name) {
        int len = name.length();
        if (len < 8) {
            if (name.equals("byte")) {
                return "B";
            }
            if (name.equals("char")) {
                return "C";
            }
            if (name.equals(XmlErrorCodes.DOUBLE)) {
                return "D";
            }
            if (name.equals(XmlErrorCodes.FLOAT)) {
                return "F";
            }
            if (name.equals(XmlErrorCodes.INT)) {
                return "I";
            }
            if (name.equals(XmlErrorCodes.LONG)) {
                return "J";
            }
            if (name.equals("short")) {
                return "S";
            }
            if (name.equals("boolean")) {
                return "Z";
            }
            if (name.equals("void")) {
                return "V";
            }
            if (name.equals("?")) {
                return name;
            }
        }
        if (name.endsWith("[]")) {
            return PropertyAccessor.PROPERTY_KEY_PREFIX + nameToSignature(name.substring(0, name.length() - 2));
        }
        if (len != 0) {
            if (!$assertionsDisabled && name.charAt(0) == '[') {
                throw new AssertionError();
            }
            if (name.indexOf("<") == -1) {
                return StandardRoles.L + name.replace('.', '/') + ';';
            }
            StringBuffer nameBuff = new StringBuffer();
            int nestLevel = 0;
            nameBuff.append(StandardRoles.L);
            int i = 0;
            while (i < name.length()) {
                char c = name.charAt(i);
                switch (c) {
                    case ',':
                        throw new IllegalStateException("Should only happen inside <...>");
                    case '.':
                        nameBuff.append('/');
                        break;
                    case '<':
                        nameBuff.append("<");
                        nestLevel++;
                        StringBuffer innerBuff = new StringBuffer();
                        while (nestLevel > 0) {
                            i++;
                            char c2 = name.charAt(i);
                            if (c2 == '<') {
                                nestLevel++;
                            }
                            if (c2 == '>') {
                                nestLevel--;
                            }
                            if (c2 == ',' && nestLevel == 1) {
                                nameBuff.append(nameToSignature(innerBuff.toString()));
                                innerBuff = new StringBuffer();
                            } else if (nestLevel > 0) {
                                innerBuff.append(c2);
                            }
                        }
                        nameBuff.append(nameToSignature(innerBuff.toString()));
                        nameBuff.append('>');
                        break;
                    case '>':
                        throw new IllegalStateException("Should by matched by <");
                    default:
                        nameBuff.append(c);
                        break;
                }
                i++;
            }
            nameBuff.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
            return nameBuff.toString();
        }
        throw new IllegalArgumentException("Bad type name: " + name);
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getCorrespondingType(boolean getFullyQualifiedType) {
        String returnType = (String) this.kvpairs.get("returnType");
        if (returnType == null) {
            returnType = "";
        }
        if (getFullyQualifiedType) {
            return returnType;
        }
        return trim(returnType);
    }

    public static String trim(String fqname) {
        int i = fqname.indexOf("<");
        if (i == -1) {
            int lastdot = fqname.lastIndexOf(46);
            if (lastdot == -1) {
                return fqname;
            }
            return fqname.substring(lastdot + 1);
        }
        char[] charArray = fqname.toCharArray();
        StringBuilder candidate = new StringBuilder(charArray.length);
        StringBuilder complete = new StringBuilder(charArray.length);
        for (char c : charArray) {
            switch (c) {
                case ',':
                case '<':
                case '>':
                    complete.append((CharSequence) candidate).append(c);
                    candidate.setLength(0);
                    break;
                case '.':
                    candidate.setLength(0);
                    break;
                default:
                    candidate.append(c);
                    break;
            }
        }
        complete.append((CharSequence) candidate);
        return complete.toString();
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getName() {
        return this.name;
    }

    @Override // org.aspectj.asm.IProgramElement
    public List<IProgramElement> getChildren() {
        return this.children;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setChildren(List<IProgramElement> children) {
        this.children = children;
        if (children == null) {
            return;
        }
        Iterator<IProgramElement> it = children.iterator();
        while (it.hasNext()) {
            it.next().setParent(this);
        }
    }

    @Override // org.aspectj.asm.IProgramElement
    public void addChild(IProgramElement child) {
        if (this.children == null || this.children == Collections.EMPTY_LIST) {
            this.children = new ArrayList();
        }
        this.children.add(child);
        child.setParent(this);
    }

    public void addChild(int position, IProgramElement child) {
        if (this.children == null || this.children == Collections.EMPTY_LIST) {
            this.children = new ArrayList();
        }
        this.children.add(position, child);
        child.setParent(this);
    }

    @Override // org.aspectj.asm.IProgramElement
    public boolean removeChild(IProgramElement child) {
        child.setParent(null);
        return this.children.remove(child);
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setName(String string) {
        this.name = string;
    }

    @Override // org.aspectj.asm.IProgramElement
    public IProgramElement walk(HierarchyWalker walker) {
        if (this.children != null) {
            for (IProgramElement child : this.children) {
                walker.process(child);
            }
        }
        return this;
    }

    @Override // org.aspectj.asm.IProgramElement
    public String toLongString() {
        final StringBuffer buffer = new StringBuffer();
        HierarchyWalker walker = new HierarchyWalker() { // from class: org.aspectj.asm.internal.ProgramElement.1
            private int depth = 0;

            @Override // org.aspectj.asm.HierarchyWalker
            public void preProcess(IProgramElement node) {
                for (int i = 0; i < this.depth; i++) {
                    buffer.append(' ');
                }
                buffer.append(node.toString());
                buffer.append('\n');
                this.depth += 2;
            }

            @Override // org.aspectj.asm.HierarchyWalker
            public void postProcess(IProgramElement node) {
                this.depth -= 2;
            }
        };
        walker.process(this);
        return buffer.toString();
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setModifiers(int i) {
        this.modifiers = i;
    }

    public void addModifiers(IProgramElement.Modifiers newModifier) {
        this.modifiers |= newModifier.getBit();
    }

    @Override // org.aspectj.asm.IProgramElement
    public String toSignatureString() {
        return toSignatureString(true);
    }

    @Override // org.aspectj.asm.IProgramElement
    public String toSignatureString(boolean getFullyQualifiedArgTypes) {
        int index;
        StringBuffer sb = new StringBuffer();
        sb.append(this.name);
        List<char[]> ptypes = getParameterTypes();
        if ((ptypes != null && (!ptypes.isEmpty() || this.kind.equals(IProgramElement.Kind.METHOD))) || this.kind.equals(IProgramElement.Kind.CONSTRUCTOR) || this.kind.equals(IProgramElement.Kind.ADVICE) || this.kind.equals(IProgramElement.Kind.POINTCUT) || this.kind.equals(IProgramElement.Kind.INTER_TYPE_METHOD) || this.kind.equals(IProgramElement.Kind.INTER_TYPE_CONSTRUCTOR)) {
            sb.append('(');
            Iterator<char[]> it = ptypes.iterator();
            while (it.hasNext()) {
                char[] arg = it.next();
                if (!getFullyQualifiedArgTypes && (index = CharOperation.lastIndexOf('.', arg)) != -1) {
                    sb.append(CharOperation.subarray(arg, index + 1, arg.length));
                } else {
                    sb.append(arg);
                }
                if (it.hasNext()) {
                    sb.append(",");
                }
            }
            sb.append(')');
        }
        return sb.toString();
    }

    @Override // org.aspectj.asm.IProgramElement
    public String toLinkLabelString() {
        return toLinkLabelString(true);
    }

    @Override // org.aspectj.asm.IProgramElement
    public String toLinkLabelString(boolean getFullyQualifiedArgTypes) {
        String label;
        if (this.kind == IProgramElement.Kind.CODE || this.kind == IProgramElement.Kind.INITIALIZER) {
            label = this.parent.getParent().getName() + ": ";
        } else if (this.kind.isInterTypeMember()) {
            if (shortITDNames) {
                label = "";
            } else {
                int dotIndex = this.name.indexOf(46);
                if (dotIndex != -1) {
                    return this.parent.getName() + ": " + toLabelString().substring(dotIndex + 1);
                }
                label = this.parent.getName() + '.';
            }
        } else if (this.kind == IProgramElement.Kind.CLASS || this.kind == IProgramElement.Kind.ASPECT || this.kind == IProgramElement.Kind.INTERFACE || this.kind.equals(IProgramElement.Kind.DECLARE_PARENTS)) {
            label = "";
        } else if (this.parent != null) {
            label = this.parent.getName() + '.';
        } else {
            label = "injar aspect: ";
        }
        return label + toLabelString(getFullyQualifiedArgTypes);
    }

    @Override // org.aspectj.asm.IProgramElement
    public String toLabelString() {
        return toLabelString(true);
    }

    @Override // org.aspectj.asm.IProgramElement
    public String toLabelString(boolean getFullyQualifiedArgTypes) {
        String label = toSignatureString(getFullyQualifiedArgTypes);
        String details = getDetails();
        if (details != null) {
            label = label + ": " + details;
        }
        return label;
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getHandleIdentifier() {
        return getHandleIdentifier(true);
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getHandleIdentifier(boolean create) {
        String h = this.handle;
        if (null == this.handle && create) {
            if (this.asm == null && this.name.equals("<build to view structure>")) {
                h = "<build to view structure>";
            } else {
                try {
                    h = this.asm.getHandleProvider().createHandleIdentifier(this);
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    throw new RuntimeException("AIOOBE whilst building handle for " + this, aioobe);
                }
            }
        }
        setHandleIdentifier(h);
        return h;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setHandleIdentifier(String handle) {
        this.handle = handle;
    }

    @Override // org.aspectj.asm.IProgramElement
    public List<String> getParameterNames() {
        List<String> parameterNames = (List) this.kvpairs.get("parameterNames");
        return parameterNames;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setParameterNames(List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        fixMap();
        this.kvpairs.put("parameterNames", list);
    }

    @Override // org.aspectj.asm.IProgramElement
    public List<char[]> getParameterTypes() {
        List<char[]> l = getParameterSignatures();
        if (l == null || l.isEmpty()) {
            return Collections.emptyList();
        }
        List<char[]> params = new ArrayList<>();
        for (char[] param : l) {
            params.add(NameConvertor.convertFromSignature(param));
        }
        return params;
    }

    @Override // org.aspectj.asm.IProgramElement
    public List<char[]> getParameterSignatures() {
        List<char[]> parameters = (List) this.kvpairs.get("parameterSigs");
        return parameters;
    }

    @Override // org.aspectj.asm.IProgramElement
    public List<String> getParameterSignaturesSourceRefs() {
        List<String> parameters = (List) this.kvpairs.get("parameterSigsSourceRefs");
        return parameters;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setParameterSignatures(List<char[]> list, List<String> sourceRefs) {
        fixMap();
        if (list == null || list.size() == 0) {
            this.kvpairs.put("parameterSigs", Collections.EMPTY_LIST);
        } else {
            this.kvpairs.put("parameterSigs", list);
        }
        if (sourceRefs != null && sourceRefs.size() != 0) {
            this.kvpairs.put("parameterSigsSourceRefs", sourceRefs);
        }
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getDetails() {
        String details = (String) this.kvpairs.get(ErrorBundle.DETAIL_ENTRY);
        return details;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setDetails(String string) {
        fixMap();
        this.kvpairs.put(ErrorBundle.DETAIL_ENTRY, string);
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setFormalComment(String txt) {
        if (txt != null && txt.length() > 0) {
            fixMap();
            this.kvpairs.put("formalComment", txt);
        }
    }

    private void fixMap() {
        if (this.kvpairs == Collections.EMPTY_MAP) {
            this.kvpairs = new HashMap();
        }
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setExtraInfo(IProgramElement.ExtraInformation info) {
        fixMap();
        this.kvpairs.put("ExtraInformation", info);
    }

    @Override // org.aspectj.asm.IProgramElement
    public IProgramElement.ExtraInformation getExtraInfo() {
        return (IProgramElement.ExtraInformation) this.kvpairs.get("ExtraInformation");
    }

    @Override // org.aspectj.asm.IProgramElement
    public boolean isAnnotationStyleDeclaration() {
        return this.kvpairs.get("annotationStyleDeclaration") != null;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setAnnotationStyleDeclaration(boolean b) {
        if (b) {
            fixMap();
            this.kvpairs.put("annotationStyleDeclaration", "true");
        }
    }

    @Override // org.aspectj.asm.IProgramElement
    public Map<String, List<String>> getDeclareParentsMap() {
        Map<String, List<String>> s = (Map) this.kvpairs.get("declareparentsmap");
        return s;
    }

    @Override // org.aspectj.asm.IProgramElement
    public void setDeclareParentsMap(Map<String, List<String>> newmap) {
        fixMap();
        this.kvpairs.put("declareparentsmap", newmap);
    }

    @Override // org.aspectj.asm.IProgramElement
    public void addFullyQualifiedName(String fqname) {
        fixMap();
        this.kvpairs.put("itdfqname", fqname);
    }

    @Override // org.aspectj.asm.IProgramElement
    public String getFullyQualifiedName() {
        return (String) this.kvpairs.get("itdfqname");
    }
}
