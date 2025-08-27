package org.aspectj.asm;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/IProgramElement.class */
public interface IProgramElement extends Serializable {
    List<IProgramElement> getChildren();

    void setChildren(List<IProgramElement> list);

    void addChild(IProgramElement iProgramElement);

    boolean removeChild(IProgramElement iProgramElement);

    void setExtraInfo(ExtraInformation extraInformation);

    ExtraInformation getExtraInfo();

    IProgramElement getParent();

    void setParent(IProgramElement iProgramElement);

    void setParentTypes(List<String> list);

    List<String> getParentTypes();

    String getName();

    void setName(String str);

    String getDetails();

    void setDetails(String str);

    Kind getKind();

    void setKind(Kind kind);

    List<Modifiers> getModifiers();

    void setModifiers(int i);

    Accessibility getAccessibility();

    String getDeclaringType();

    String getPackageName();

    void setCorrespondingType(String str);

    String getCorrespondingType();

    String getCorrespondingType(boolean z);

    String toSignatureString();

    String toSignatureString(boolean z);

    void setRunnable(boolean z);

    boolean isRunnable();

    boolean isImplementor();

    void setImplementor(boolean z);

    boolean isOverrider();

    void setOverrider(boolean z);

    IMessage getMessage();

    void setMessage(IMessage iMessage);

    ISourceLocation getSourceLocation();

    void setSourceLocation(ISourceLocation iSourceLocation);

    String toString();

    String getFormalComment();

    void setFormalComment(String str);

    String toLinkLabelString();

    String toLinkLabelString(boolean z);

    String toLabelString();

    String toLabelString(boolean z);

    List<String> getParameterNames();

    void setParameterNames(List<String> list);

    List<char[]> getParameterSignatures();

    List<String> getParameterSignaturesSourceRefs();

    void setParameterSignatures(List<char[]> list, List<String> list2);

    List<char[]> getParameterTypes();

    String getHandleIdentifier();

    String getHandleIdentifier(boolean z);

    void setHandleIdentifier(String str);

    String toLongString();

    String getBytecodeName();

    String getBytecodeSignature();

    void setBytecodeName(String str);

    void setBytecodeSignature(String str);

    String getSourceSignature();

    void setSourceSignature(String str);

    IProgramElement walk(HierarchyWalker hierarchyWalker);

    AsmManager getModel();

    int getRawModifiers();

    void setAnnotationStyleDeclaration(boolean z);

    boolean isAnnotationStyleDeclaration();

    void setAnnotationType(String str);

    String getAnnotationType();

    String[] getRemovedAnnotationTypes();

    Map<String, List<String>> getDeclareParentsMap();

    void setDeclareParentsMap(Map<String, List<String>> map);

    void addFullyQualifiedName(String str);

    String getFullyQualifiedName();

    void setAnnotationRemover(boolean z);

    boolean isAnnotationRemover();

    String getCorrespondingTypeSignature();

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/IProgramElement$ExtraInformation.class */
    public static class ExtraInformation implements Serializable {
        private static final long serialVersionUID = -3880735494840820638L;
        private String extraInfo = "";

        public void setExtraAdviceInformation(String string) {
            this.extraInfo = string;
        }

        public String getExtraAdviceInformation() {
            return this.extraInfo;
        }

        public String toString() {
            return "ExtraInformation: [" + this.extraInfo + "]";
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/IProgramElement$Modifiers.class */
    public static class Modifiers implements Serializable {
        private static final long serialVersionUID = -8279300899976607927L;
        private final String name;
        private final int bit;
        private final int ordinal;
        public static final Modifiers STATIC = new Modifiers("static", 8);
        public static final Modifiers FINAL = new Modifiers("final", 16);
        public static final Modifiers ABSTRACT = new Modifiers(BeanDefinitionParserDelegate.ABSTRACT_ATTRIBUTE, 1024);
        public static final Modifiers SYNCHRONIZED = new Modifiers("synchronized", 32);
        public static final Modifiers VOLATILE = new Modifiers("volatile", 64);
        public static final Modifiers STRICTFP = new Modifiers("strictfp", 2048);
        public static final Modifiers TRANSIENT = new Modifiers("transient", 128);
        public static final Modifiers NATIVE = new Modifiers("native", 256);
        public static final Modifiers[] ALL = {STATIC, FINAL, ABSTRACT, SYNCHRONIZED, VOLATILE, STRICTFP, TRANSIENT, NATIVE};
        private static int nextOrdinal = 0;

        private Modifiers(String name, int bit) {
            int i = nextOrdinal;
            nextOrdinal = i + 1;
            this.ordinal = i;
            this.name = name;
            this.bit = bit;
        }

        public String toString() {
            return this.name;
        }

        public int getBit() {
            return this.bit;
        }

        private Object readResolve() throws ObjectStreamException {
            return ALL[this.ordinal];
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/IProgramElement$Accessibility.class */
    public static class Accessibility implements Serializable {
        private static final long serialVersionUID = 5371838588180918519L;
        private final String name;
        private final int ordinal;
        public static final Accessibility PUBLIC = new Accessibility("public");
        public static final Accessibility PACKAGE = new Accessibility("package");
        public static final Accessibility PROTECTED = new Accessibility("protected");
        public static final Accessibility PRIVATE = new Accessibility("private");
        public static final Accessibility PRIVILEGED = new Accessibility("privileged");
        public static final Accessibility[] ALL = {PUBLIC, PACKAGE, PROTECTED, PRIVATE, PRIVILEGED};
        private static int nextOrdinal = 0;

        private Accessibility(String name) {
            int i = nextOrdinal;
            nextOrdinal = i + 1;
            this.ordinal = i;
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        private Object readResolve() throws ObjectStreamException {
            return ALL[this.ordinal];
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/IProgramElement$Kind.class */
    public static class Kind implements Serializable {
        private static final long serialVersionUID = -1963553877479266124L;
        private final String name;
        private final int ordinal;
        public static final Kind PROJECT = new Kind("project");
        public static final Kind PACKAGE = new Kind("package");
        public static final Kind FILE = new Kind("file");
        public static final Kind FILE_JAVA = new Kind("java source file");
        public static final Kind FILE_ASPECTJ = new Kind("aspect source file");
        public static final Kind FILE_LST = new Kind("build configuration file");
        public static final Kind IMPORT_REFERENCE = new Kind("import reference");
        public static final Kind CLASS = new Kind("class");
        public static final Kind INTERFACE = new Kind(JamXmlElements.INTERFACE);
        public static final Kind ASPECT = new Kind("aspect");
        public static final Kind ENUM = new Kind("enum");
        public static final Kind ENUM_VALUE = new Kind("enumvalue");
        public static final Kind ANNOTATION = new Kind(JamXmlElements.ANNOTATION);
        public static final Kind INITIALIZER = new Kind("initializer");
        public static final Kind INTER_TYPE_FIELD = new Kind("inter-type field");
        public static final Kind INTER_TYPE_METHOD = new Kind("inter-type method");
        public static final Kind INTER_TYPE_CONSTRUCTOR = new Kind("inter-type constructor");
        public static final Kind INTER_TYPE_PARENT = new Kind("inter-type parent");
        public static final Kind CONSTRUCTOR = new Kind("constructor");
        public static final Kind METHOD = new Kind(JamXmlElements.METHOD);
        public static final Kind FIELD = new Kind(JamXmlElements.FIELD);
        public static final Kind POINTCUT = new Kind("pointcut");
        public static final Kind ADVICE = new Kind("advice");
        public static final Kind DECLARE_PARENTS = new Kind("declare parents");
        public static final Kind DECLARE_WARNING = new Kind("declare warning");
        public static final Kind DECLARE_ERROR = new Kind("declare error");
        public static final Kind DECLARE_SOFT = new Kind("declare soft");
        public static final Kind DECLARE_PRECEDENCE = new Kind("declare precedence");
        public static final Kind CODE = new Kind("code");
        public static final Kind ERROR = new Kind(AsmRelationshipUtils.DECLARE_ERROR);
        public static final Kind DECLARE_ANNOTATION_AT_CONSTRUCTOR = new Kind("declare @constructor");
        public static final Kind DECLARE_ANNOTATION_AT_FIELD = new Kind("declare @field");
        public static final Kind DECLARE_ANNOTATION_AT_METHOD = new Kind("declare @method");
        public static final Kind DECLARE_ANNOTATION_AT_TYPE = new Kind("declare @type");
        public static final Kind SOURCE_FOLDER = new Kind("source folder");
        public static final Kind PACKAGE_DECLARATION = new Kind("package declaration");
        public static final Kind[] ALL = {PROJECT, PACKAGE, FILE, FILE_JAVA, FILE_ASPECTJ, FILE_LST, IMPORT_REFERENCE, CLASS, INTERFACE, ASPECT, ENUM, ENUM_VALUE, ANNOTATION, INITIALIZER, INTER_TYPE_FIELD, INTER_TYPE_METHOD, INTER_TYPE_CONSTRUCTOR, INTER_TYPE_PARENT, CONSTRUCTOR, METHOD, FIELD, POINTCUT, ADVICE, DECLARE_PARENTS, DECLARE_WARNING, DECLARE_ERROR, DECLARE_SOFT, DECLARE_PRECEDENCE, CODE, ERROR, DECLARE_ANNOTATION_AT_CONSTRUCTOR, DECLARE_ANNOTATION_AT_FIELD, DECLARE_ANNOTATION_AT_METHOD, DECLARE_ANNOTATION_AT_TYPE, SOURCE_FOLDER, PACKAGE_DECLARATION};
        private static int nextOrdinal = 0;

        public static Kind getKindForString(String kindString) {
            for (int i = 0; i < ALL.length; i++) {
                if (ALL[i].toString().equals(kindString)) {
                    return ALL[i];
                }
            }
            return ERROR;
        }

        private Kind(String name) {
            int i = nextOrdinal;
            nextOrdinal = i + 1;
            this.ordinal = i;
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public static List<Kind> getNonAJMemberKinds() {
            List<Kind> list = new ArrayList<>();
            list.add(METHOD);
            list.add(ENUM_VALUE);
            list.add(FIELD);
            list.add(CONSTRUCTOR);
            return list;
        }

        public boolean isMember() {
            return this == FIELD || this == METHOD || this == CONSTRUCTOR || this == POINTCUT || this == ADVICE || this == ENUM_VALUE;
        }

        public boolean isInterTypeMember() {
            return this == INTER_TYPE_CONSTRUCTOR || this == INTER_TYPE_FIELD || this == INTER_TYPE_METHOD;
        }

        public boolean isType() {
            return this == CLASS || this == INTERFACE || this == ASPECT || this == ANNOTATION || this == ENUM;
        }

        public boolean isSourceFile() {
            return this == FILE_ASPECTJ || this == FILE_JAVA;
        }

        public boolean isFile() {
            return this == FILE;
        }

        public boolean isDeclare() {
            return this.name.startsWith(AsmRelationshipUtils.DEC_LABEL);
        }

        public boolean isDeclareAnnotation() {
            return this.name.startsWith("declare @");
        }

        public boolean isDeclareParents() {
            return this.name.startsWith("declare parents");
        }

        public boolean isDeclareSoft() {
            return this.name.startsWith("declare soft");
        }

        public boolean isDeclareWarning() {
            return this.name.startsWith("declare warning");
        }

        public boolean isDeclareError() {
            return this.name.startsWith("declare error");
        }

        public boolean isDeclarePrecedence() {
            return this.name.startsWith("declare precedence");
        }

        private Object readResolve() throws ObjectStreamException {
            return ALL[this.ordinal];
        }

        public boolean isPackageDeclaration() {
            return this == PACKAGE_DECLARATION;
        }
    }
}
