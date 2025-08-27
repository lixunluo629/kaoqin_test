package org.aspectj.weaver.loadtime;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.ognl.OgnlContext;
import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.ClassElementValue;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.aspectj.apache.bcel.classfile.annotation.SimpleElementValue;
import org.aspectj.apache.bcel.generic.FieldGen;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.LocalVariableTag;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.GeneratedReferenceTypeDelegate;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelAnnotation;
import org.aspectj.weaver.bcel.BcelPerClauseAspectAdder;
import org.aspectj.weaver.bcel.BcelWorld;
import org.aspectj.weaver.bcel.LazyClassGen;
import org.aspectj.weaver.bcel.LazyMethodGen;
import org.aspectj.weaver.loadtime.definition.Definition;
import org.aspectj.weaver.patterns.BasicTokenSource;
import org.aspectj.weaver.patterns.DeclareAnnotation;
import org.aspectj.weaver.patterns.ISignaturePattern;
import org.aspectj.weaver.patterns.ITokenSource;
import org.aspectj.weaver.patterns.PatternParser;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.TypePattern;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/ConcreteAspectCodeGen.class */
public class ConcreteAspectCodeGen {
    private static final String[] EMPTY_STRINGS = new String[0];
    private static final Type[] EMPTY_TYPES = new Type[0];
    private final Definition.ConcreteAspect concreteAspect;
    private final World world;
    private boolean isValid = false;
    private ResolvedType parent;
    private PerClause perclause;
    private byte[] bytes;

    ConcreteAspectCodeGen(Definition.ConcreteAspect concreteAspect, World world) {
        this.concreteAspect = concreteAspect;
        this.world = world;
    }

    public boolean validate() throws AbortException {
        if (!(this.world instanceof BcelWorld)) {
            reportError("Internal error: world must be of type BcelWorld");
            return false;
        }
        ResolvedType current = this.world.lookupBySignature(UnresolvedType.forName(this.concreteAspect.name).getSignature());
        if (current != null && !current.isMissing()) {
            reportError("Attempt to concretize but chosen aspect name already defined: " + stringify());
            return false;
        }
        if (this.concreteAspect.pointcutsAndAdvice.size() != 0) {
            this.isValid = true;
            return true;
        }
        if (this.concreteAspect.declareAnnotations.size() != 0) {
            this.isValid = true;
            return true;
        }
        if (this.concreteAspect.extend == null && this.concreteAspect.precedence != null) {
            if (this.concreteAspect.pointcuts.isEmpty()) {
                this.isValid = true;
                this.parent = null;
                return true;
            }
            reportError("Attempt to use nested pointcuts without extends clause: " + stringify());
            return false;
        }
        String parentAspectName = this.concreteAspect.extend;
        if (parentAspectName.indexOf("<") != -1) {
            this.parent = this.world.resolve(UnresolvedType.forName(parentAspectName), true);
            if (this.parent.isMissing()) {
                reportError("Unable to resolve type reference: " + stringify());
                return false;
            }
            if (this.parent.isParameterizedType()) {
                UnresolvedType[] typeParameters = this.parent.getTypeParameters();
                for (UnresolvedType typeParameter : typeParameters) {
                    if ((typeParameter instanceof ResolvedType) && ((ResolvedType) typeParameter).isMissing()) {
                        reportError("Unablet to resolve type parameter '" + typeParameter.getName() + "' from " + stringify());
                        return false;
                    }
                }
            }
        } else {
            this.parent = this.world.resolve(this.concreteAspect.extend, true);
        }
        if (this.parent.isMissing()) {
            String fixedName = this.concreteAspect.extend;
            int hasDot = fixedName.lastIndexOf(46);
            while (hasDot > 0) {
                char[] fixedNameChars = fixedName.toCharArray();
                fixedNameChars[hasDot] = '$';
                fixedName = new String(fixedNameChars);
                hasDot = fixedName.lastIndexOf(46);
                this.parent = this.world.resolve(UnresolvedType.forName(fixedName), true);
                if (!this.parent.isMissing()) {
                    break;
                }
            }
        }
        if (this.parent.isMissing()) {
            reportError("Cannot find parent aspect for: " + stringify());
            return false;
        }
        if (!this.parent.isAbstract() && !this.parent.equals(ResolvedType.OBJECT)) {
            reportError("Attempt to concretize a non-abstract aspect: " + stringify());
            return false;
        }
        if (!this.parent.isAspect() && !this.parent.equals(ResolvedType.OBJECT)) {
            reportError("Attempt to concretize a non aspect: " + stringify());
            return false;
        }
        List<String> elligibleAbstractions = new ArrayList<>();
        Collection<ResolvedMember> abstractMethods = getOutstandingAbstractMethods(this.parent);
        for (ResolvedMember method : abstractMethods) {
            if ("()V".equals(method.getSignature())) {
                String n = method.getName();
                if (n.startsWith("ajc$pointcut")) {
                    String n2 = n.substring(14);
                    elligibleAbstractions.add(n2.substring(0, n2.indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)));
                } else if (hasPointcutAnnotation(method)) {
                    elligibleAbstractions.add(method.getName());
                } else {
                    reportError("Abstract method '" + method.toString() + "' cannot be concretized in XML: " + stringify());
                    return false;
                }
            } else {
                if (method.getName().startsWith("ajc$pointcut") || hasPointcutAnnotation(method)) {
                    reportError("Abstract method '" + method.toString() + "' cannot be concretized as a pointcut (illegal signature, must have no arguments, must return void): " + stringify());
                    return false;
                }
                reportError("Abstract method '" + method.toString() + "' cannot be concretized in XML: " + stringify());
                return false;
            }
        }
        List<String> pointcutNames = new ArrayList<>();
        for (Definition.Pointcut abstractPc : this.concreteAspect.pointcuts) {
            pointcutNames.add(abstractPc.name);
        }
        for (String elligiblePc : elligibleAbstractions) {
            if (!pointcutNames.contains(elligiblePc)) {
                reportError("Abstract pointcut '" + elligiblePc + "' not configured: " + stringify());
                return false;
            }
        }
        if (this.concreteAspect.perclause != null) {
            String perclauseString = this.concreteAspect.perclause;
            if (!perclauseString.startsWith("persingleton") && !perclauseString.startsWith("percflow") && !perclauseString.startsWith("pertypewithin") && !perclauseString.startsWith("perthis") && !perclauseString.startsWith("pertarget") && !perclauseString.startsWith("percflowbelow")) {
                reportError("Unrecognized per clause specified " + stringify());
                return false;
            }
        }
        this.isValid = true;
        return this.isValid;
    }

    private Collection<ResolvedMember> getOutstandingAbstractMethods(ResolvedType type) {
        Map<String, ResolvedMember> collector = new HashMap<>();
        getOutstandingAbstractMethodsHelper(type, collector);
        return collector.values();
    }

    private void getOutstandingAbstractMethodsHelper(ResolvedType type, Map<String, ResolvedMember> collector) {
        if (type == null) {
            return;
        }
        if (!type.equals(ResolvedType.OBJECT) && type.getSuperclass() != null) {
            getOutstandingAbstractMethodsHelper(type.getSuperclass(), collector);
        }
        ResolvedMember[] rms = type.getDeclaredMethods();
        if (rms != null) {
            for (ResolvedMember member : rms) {
                String key = member.getName() + member.getSignature();
                if (member.isAbstract()) {
                    collector.put(key, member);
                } else {
                    collector.remove(key);
                }
            }
        }
    }

    private String stringify() {
        StringBuffer sb = new StringBuffer("<concrete-aspect name='");
        sb.append(this.concreteAspect.name);
        sb.append("' extends='");
        sb.append(this.concreteAspect.extend);
        sb.append("' perclause='");
        sb.append(this.concreteAspect.perclause);
        sb.append("'/> in aop.xml");
        return sb.toString();
    }

    private boolean hasPointcutAnnotation(ResolvedMember member) {
        AnnotationAJ[] as = member.getAnnotations();
        if (as == null || as.length == 0) {
            return false;
        }
        for (AnnotationAJ annotationAJ : as) {
            if (annotationAJ.getTypeSignature().equals("Lorg/aspectj/lang/annotation/Pointcut;")) {
                return true;
            }
        }
        return false;
    }

    public String getClassName() {
        return this.concreteAspect.name;
    }

    public byte[] getBytes() {
        if (!this.isValid) {
            throw new RuntimeException("Must validate first");
        }
        if (this.bytes != null) {
            return this.bytes;
        }
        PerClause.Kind perclauseKind = PerClause.SINGLETON;
        PerClause parentPerClause = this.parent != null ? this.parent.getPerClause() : null;
        if (parentPerClause != null) {
            perclauseKind = parentPerClause.getKind();
        }
        String perclauseString = null;
        if (this.concreteAspect.perclause != null) {
            perclauseString = this.concreteAspect.perclause;
            if (perclauseString.startsWith("persingleton")) {
                perclauseKind = PerClause.SINGLETON;
            } else if (perclauseString.startsWith("percflow")) {
                perclauseKind = PerClause.PERCFLOW;
            } else if (perclauseString.startsWith("pertypewithin")) {
                perclauseKind = PerClause.PERTYPEWITHIN;
            } else if (perclauseString.startsWith("perthis") || perclauseString.startsWith("pertarget")) {
                perclauseKind = PerClause.PEROBJECT;
            } else if (perclauseString.startsWith("percflowbelow")) {
                perclauseKind = PerClause.PERCFLOW;
            }
        }
        String parentName = "java/lang/Object";
        if (this.parent != null) {
            if (this.parent.isParameterizedType()) {
                parentName = this.parent.getGenericType().getName().replace('.', '/');
            } else {
                parentName = this.parent.getName().replace('.', '/');
            }
        }
        LazyClassGen cg = new LazyClassGen(this.concreteAspect.name.replace('.', '/'), parentName, null, 33, EMPTY_STRINGS, this.world);
        if (this.parent != null && this.parent.isParameterizedType()) {
            cg.setSuperClass(this.parent);
        }
        if (perclauseString == null) {
            AnnotationGen ag = new AnnotationGen(new ObjectType("org/aspectj/lang/annotation/Aspect"), Collections.emptyList(), true, cg.getConstantPool());
            cg.addAnnotation(ag);
        } else {
            List<NameValuePair> elems = new ArrayList<>();
            elems.add(new NameValuePair("value", new SimpleElementValue(115, cg.getConstantPool(), perclauseString), cg.getConstantPool()));
            AnnotationGen ag2 = new AnnotationGen(new ObjectType("org/aspectj/lang/annotation/Aspect"), elems, true, cg.getConstantPool());
            cg.addAnnotation(ag2);
        }
        if (this.concreteAspect.precedence != null) {
            SimpleElementValue svg = new SimpleElementValue(115, cg.getConstantPool(), this.concreteAspect.precedence);
            List<NameValuePair> elems2 = new ArrayList<>();
            elems2.add(new NameValuePair("value", svg, cg.getConstantPool()));
            AnnotationGen agprec = new AnnotationGen(new ObjectType("org/aspectj/lang/annotation/DeclarePrecedence"), elems2, true, cg.getConstantPool());
            cg.addAnnotation(agprec);
        }
        LazyMethodGen init = new LazyMethodGen(1, Type.VOID, "<init>", EMPTY_TYPES, EMPTY_STRINGS, cg);
        InstructionList cbody = init.getBody();
        cbody.append(InstructionConstants.ALOAD_0);
        cbody.append(cg.getFactory().createInvoke(parentName, "<init>", Type.VOID, EMPTY_TYPES, (short) 183));
        cbody.append(InstructionConstants.RETURN);
        cg.addMethodGen(init);
        for (Definition.Pointcut abstractPc : this.concreteAspect.pointcuts) {
            LazyMethodGen mg = new LazyMethodGen(1, Type.VOID, abstractPc.name, EMPTY_TYPES, EMPTY_STRINGS, cg);
            SimpleElementValue svg2 = new SimpleElementValue(115, cg.getConstantPool(), abstractPc.expression);
            List<NameValuePair> elems3 = new ArrayList<>();
            elems3.add(new NameValuePair("value", svg2, cg.getConstantPool()));
            AnnotationGen mag = new AnnotationGen(new ObjectType("org/aspectj/lang/annotation/Pointcut"), elems3, true, cg.getConstantPool());
            AnnotationAJ max = new BcelAnnotation(mag, this.world);
            mg.addAnnotation(max);
            InstructionList body = mg.getBody();
            body.append(InstructionConstants.RETURN);
            cg.addMethodGen(mg);
        }
        if (this.concreteAspect.deows.size() > 0) {
            int counter = 1;
            for (Definition.DeclareErrorOrWarning deow : this.concreteAspect.deows) {
                int i = counter;
                counter++;
                FieldGen field = new FieldGen(16, ObjectType.STRING, "rule" + i, cg.getConstantPool());
                SimpleElementValue svg3 = new SimpleElementValue(115, cg.getConstantPool(), deow.pointcut);
                List<NameValuePair> elems4 = new ArrayList<>();
                elems4.add(new NameValuePair("value", svg3, cg.getConstantPool()));
                AnnotationGen mag2 = new AnnotationGen(new ObjectType("org/aspectj/lang/annotation/Declare" + (deow.isError ? "Error" : "Warning")), elems4, true, cg.getConstantPool());
                field.addAnnotation(mag2);
                field.setValue(deow.message);
                cg.addField(field, null);
            }
        }
        if (this.concreteAspect.pointcutsAndAdvice.size() > 0) {
            int adviceCounter = 1;
            for (Definition.PointcutAndAdvice paa : this.concreteAspect.pointcutsAndAdvice) {
                generateAdviceMethod(paa, adviceCounter, cg);
                adviceCounter++;
            }
        }
        if (this.concreteAspect.declareAnnotations.size() > 0) {
            int decCounter = 1;
            for (Definition.DeclareAnnotation da : this.concreteAspect.declareAnnotations) {
                int i2 = decCounter;
                decCounter++;
                generateDeclareAnnotation(da, i2, cg);
            }
        }
        ReferenceType rt = new ReferenceType(ResolvedType.forName(this.concreteAspect.name).getSignature(), this.world);
        GeneratedReferenceTypeDelegate grtd = new GeneratedReferenceTypeDelegate(rt);
        grtd.setSuperclass(this.parent);
        rt.setDelegate(grtd);
        BcelPerClauseAspectAdder perClauseMunger = new BcelPerClauseAspectAdder(rt, perclauseKind);
        perClauseMunger.forceMunge(cg, false);
        JavaClass jc = cg.getJavaClass((BcelWorld) this.world);
        ((BcelWorld) this.world).addSourceObjectType(jc, true);
        this.bytes = jc.getBytes();
        return this.bytes;
    }

    private void generateDeclareAnnotation(Definition.DeclareAnnotation da, int decCounter, LazyClassGen cg) throws AbortException, NumberFormatException {
        AnnotationAJ constructedAnnotation = buildDeclareAnnotation_actualAnnotation(cg, da);
        if (constructedAnnotation == null) {
            return;
        }
        String nameComponent = da.declareAnnotationKind.name().toLowerCase();
        String declareName = "ajc$declare_at_" + nameComponent + "_" + decCounter;
        LazyMethodGen declareMethod = new LazyMethodGen(1, Type.VOID, declareName, Type.NO_ARGS, EMPTY_STRINGS, cg);
        InstructionList declareMethodBody = declareMethod.getBody();
        declareMethodBody.append(InstructionFactory.RETURN);
        declareMethod.addAnnotation(constructedAnnotation);
        DeclareAnnotation deca = null;
        ITokenSource tokenSource = BasicTokenSource.makeTokenSource(da.pattern, null);
        PatternParser pp = new PatternParser(tokenSource);
        if (da.declareAnnotationKind == Definition.DeclareAnnotationKind.Method || da.declareAnnotationKind == Definition.DeclareAnnotationKind.Field) {
            ISignaturePattern isp = da.declareAnnotationKind == Definition.DeclareAnnotationKind.Method ? pp.parseCompoundMethodOrConstructorSignaturePattern(true) : pp.parseCompoundFieldSignaturePattern();
            deca = new DeclareAnnotation(da.declareAnnotationKind == Definition.DeclareAnnotationKind.Method ? DeclareAnnotation.AT_METHOD : DeclareAnnotation.AT_FIELD, isp);
        } else if (da.declareAnnotationKind == Definition.DeclareAnnotationKind.Type) {
            TypePattern tp = pp.parseTypePattern();
            deca = new DeclareAnnotation(DeclareAnnotation.AT_TYPE, tp);
        }
        deca.setAnnotationMethod(declareName);
        deca.setAnnotationString(da.annotation);
        AjAttribute attribute = new AjAttribute.DeclareAttribute(deca);
        cg.addAttribute(attribute);
        cg.addMethodGen(declareMethod);
    }

    private AnnotationAJ buildDeclareAnnotation_actualAnnotation(LazyClassGen cg, Definition.DeclareAnnotation da) throws AbortException, NumberFormatException {
        AnnotationGen anno = buildAnnotationFromString(cg.getConstantPool(), cg.getWorld(), da.annotation);
        if (anno == null) {
            return null;
        }
        AnnotationAJ bcelAnnotation = new BcelAnnotation(anno, this.world);
        return bcelAnnotation;
    }

    private AnnotationGen buildAnnotationFromString(ConstantPool cp, World w, String annotationString) throws AbortException, NumberFormatException {
        char ch2;
        int paren = annotationString.indexOf(40);
        if (paren == -1) {
            return buildBaseAnnotationType(cp, this.world, annotationString);
        }
        String name = annotationString.substring(0, paren);
        List<String> values = new ArrayList<>();
        int pos = paren + 1;
        int depth = 0;
        int len = annotationString.length();
        int start = pos;
        while (pos < len && ((ch2 = annotationString.charAt(pos)) != ')' || depth != 0)) {
            if (ch2 == '(' || ch2 == '[') {
                depth++;
            } else if (ch2 == ')' || ch2 == ']') {
                depth--;
            }
            if (ch2 == ',' && depth == 0) {
                values.add(annotationString.substring(start, pos).trim());
                start = pos + 1;
            }
            pos++;
        }
        if (start != pos) {
            values.add(annotationString.substring(start, pos).trim());
        }
        AnnotationGen aaj = buildBaseAnnotationType(cp, this.world, name);
        if (aaj == null) {
            return null;
        }
        String typename = aaj.getTypeName();
        ResolvedType type = UnresolvedType.forName(typename).resolve(this.world);
        ResolvedMember[] rms = type.getDeclaredMethods();
        for (String value : values) {
            int equalsIndex = value.indexOf(SymbolConstants.EQUAL_SYMBOL);
            String key = "value";
            if (value.charAt(0) != '\"' && equalsIndex != -1) {
                key = value.substring(0, equalsIndex).trim();
                value = value.substring(equalsIndex + 1).trim();
            }
            boolean keyIsOk = false;
            for (int m = 0; m < rms.length; m++) {
                NameValuePair nvp = null;
                if (rms[m].getName().equals(key)) {
                    keyIsOk = true;
                    UnresolvedType rt = rms[m].getReturnType();
                    if (rt.isPrimitiveType()) {
                        switch (rt.getSignature().charAt(0)) {
                            case 'B':
                                try {
                                    byte byteValue = Byte.parseByte(value);
                                    nvp = new NameValuePair(key, new SimpleElementValue(66, cp, byteValue), cp);
                                    break;
                                } catch (NumberFormatException e) {
                                    reportError("unable to interpret annotation value '" + value + "' as a byte");
                                    return null;
                                }
                            case 'C':
                                if (value.length() < 2) {
                                    reportError("unable to interpret annotation value '" + value + "' as a char");
                                    return null;
                                }
                                nvp = new NameValuePair(key, new SimpleElementValue(67, cp, value.charAt(1)), cp);
                                break;
                            case 'D':
                                try {
                                    double doubleValue = Double.parseDouble(value);
                                    nvp = new NameValuePair(key, new SimpleElementValue(68, cp, doubleValue), cp);
                                    break;
                                } catch (NumberFormatException e2) {
                                    reportError("unable to interpret annotation value '" + value + "' as a double");
                                    return null;
                                }
                            case 'E':
                            case 'G':
                            case 'H':
                            case 'K':
                            case 'L':
                            case 'M':
                            case 'N':
                            case 'O':
                            case 'P':
                            case 'Q':
                            case 'R':
                            case 'T':
                            case 'U':
                            case 'V':
                            case 'W':
                            case 'X':
                            case 'Y':
                            default:
                                reportError("not yet supporting XML setting of annotation values of type " + rt.getName());
                                return null;
                            case 'F':
                                try {
                                    float floatValue = Float.parseFloat(value);
                                    nvp = new NameValuePair(key, new SimpleElementValue(70, cp, floatValue), cp);
                                    break;
                                } catch (NumberFormatException e3) {
                                    reportError("unable to interpret annotation value '" + value + "' as a float");
                                    return null;
                                }
                            case 'I':
                                try {
                                    int intValue = Integer.parseInt(value);
                                    nvp = new NameValuePair(key, new SimpleElementValue(73, cp, intValue), cp);
                                    break;
                                } catch (NumberFormatException e4) {
                                    reportError("unable to interpret annotation value '" + value + "' as an integer");
                                    return null;
                                }
                            case 'J':
                                try {
                                    long longValue = Long.parseLong(value);
                                    nvp = new NameValuePair(key, new SimpleElementValue(74, cp, longValue), cp);
                                    break;
                                } catch (NumberFormatException e5) {
                                    reportError("unable to interpret annotation value '" + value + "' as a long");
                                    return null;
                                }
                            case 'S':
                                try {
                                    short shortValue = Short.parseShort(value);
                                    nvp = new NameValuePair(key, new SimpleElementValue(83, cp, shortValue), cp);
                                    break;
                                } catch (NumberFormatException e6) {
                                    reportError("unable to interpret annotation value '" + value + "' as a short");
                                    return null;
                                }
                            case 'Z':
                                try {
                                    boolean booleanValue = Boolean.parseBoolean(value);
                                    nvp = new NameValuePair(key, new SimpleElementValue(90, cp, booleanValue), cp);
                                    break;
                                } catch (NumberFormatException e7) {
                                    reportError("unable to interpret annotation value '" + value + "' as a boolean");
                                    return null;
                                }
                        }
                    } else if (UnresolvedType.JL_STRING.equals(rt)) {
                        if (value.length() < 2) {
                            reportError("Invalid string value specified in annotation string: " + annotationString);
                            return null;
                        }
                        value = value.substring(1, value.length() - 1);
                        nvp = new NameValuePair(key, new SimpleElementValue(115, cp, value), cp);
                    } else if (UnresolvedType.JL_CLASS.equals(rt)) {
                        if (value.length() < 6) {
                            reportError("Not a well formed class value for an annotation '" + value + "'");
                            return null;
                        }
                        String clazz = value.substring(0, value.length() - 6);
                        boolean qualified = clazz.indexOf(".") != -1;
                        if (!qualified) {
                            clazz = "java.lang." + clazz;
                        }
                        nvp = new NameValuePair(key, new ClassElementValue(new ObjectType(clazz), cp), cp);
                    }
                }
                if (nvp != null) {
                    aaj.addElementNameValuePair(nvp);
                }
            }
            if (!keyIsOk) {
                reportError("annotation @" + typename + " does not have a value named " + key);
                return null;
            }
        }
        return aaj;
    }

    private AnnotationGen buildBaseAnnotationType(ConstantPool cp, World world, String typename) throws AbortException {
        String annoname = typename;
        if (annoname.startsWith("@")) {
            annoname = annoname.substring(1);
        }
        ResolvedType annotationType = UnresolvedType.forName(annoname).resolve(world);
        if (!annotationType.isAnnotation()) {
            reportError("declare is not specifying an annotation type :" + typename);
            return null;
        }
        if (!annotationType.isAnnotationWithRuntimeRetention()) {
            reportError("declare is using an annotation type that does not have runtime retention: " + typename);
            return null;
        }
        List<NameValuePair> elems = new ArrayList<>();
        return new AnnotationGen(new ObjectType(annoname), elems, true, cp);
    }

    private void generateAdviceMethod(Definition.PointcutAndAdvice paa, int adviceCounter, LazyClassGen cg) throws AbortException, ClassFormatException {
        ResolvedType resolvedParamType;
        ResolvedType delegateClass = this.world.resolve(UnresolvedType.forName(paa.adviceClass));
        if (delegateClass.isMissing()) {
            reportError("Class to invoke cannot be found: '" + paa.adviceClass + "'");
            return;
        }
        String adviceName = "generated$" + paa.adviceKind.toString().toLowerCase() + "$advice$" + adviceCounter;
        AnnotationAJ aaj = buildAdviceAnnotation(cg, paa);
        String method = paa.adviceMethod;
        int paren = method.indexOf("(");
        String methodName = method.substring(0, paren);
        String signature = method.substring(paren);
        if (signature.charAt(0) != '(' || !signature.endsWith(")")) {
            reportError("Badly formatted parameter signature: '" + method + "'");
            return;
        }
        List<Type> paramTypes = new ArrayList<>();
        List<String> paramNames = new ArrayList<>();
        if (signature.charAt(1) != ')') {
            StringBuilder convertedSignature = new StringBuilder("(");
            boolean paramsBroken = false;
            int i = 1;
            while (true) {
                int pos = i;
                if (pos >= signature.length() || signature.charAt(pos) == ')' || paramsBroken) {
                    break;
                }
                int nextChunkEndPos = signature.indexOf(44, pos);
                if (nextChunkEndPos == -1) {
                    nextChunkEndPos = signature.indexOf(41, pos);
                }
                String nextChunk = signature.substring(pos, nextChunkEndPos).trim();
                int space = nextChunk.indexOf(SymbolConstants.SPACE_SYMBOL);
                if (space == -1) {
                    if (nextChunk.equals("JoinPoint")) {
                        nextChunk = "org.aspectj.lang.JoinPoint";
                    } else if (nextChunk.equals("JoinPoint.StaticPart")) {
                        nextChunk = "org.aspectj.lang.JoinPoint$StaticPart";
                    } else if (nextChunk.equals("ProceedingJoinPoint")) {
                        nextChunk = "org.aspectj.lang.ProceedingJoinPoint";
                    }
                    UnresolvedType unresolvedParamType = UnresolvedType.forName(nextChunk);
                    resolvedParamType = this.world.resolve(unresolvedParamType);
                } else {
                    String typename = nextChunk.substring(0, space);
                    if (typename.equals("JoinPoint")) {
                        typename = "org.aspectj.lang.JoinPoint";
                    } else if (typename.equals("JoinPoint.StaticPart")) {
                        typename = "org.aspectj.lang.JoinPoint$StaticPart";
                    } else if (typename.equals("ProceedingJoinPoint")) {
                        typename = "org.aspectj.lang.ProceedingJoinPoint";
                    }
                    UnresolvedType unresolvedParamType2 = UnresolvedType.forName(typename);
                    resolvedParamType = this.world.resolve(unresolvedParamType2);
                    String paramname = nextChunk.substring(space).trim();
                    paramNames.add(paramname);
                }
                if (resolvedParamType.isMissing()) {
                    reportError("Cannot find type specified as parameter: '" + nextChunk + "' from signature '" + signature + "'");
                    paramsBroken = true;
                }
                paramTypes.add(Type.getType(resolvedParamType.getSignature()));
                convertedSignature.append(resolvedParamType.getSignature());
                i = nextChunkEndPos + 1;
            }
            convertedSignature.append(")");
            signature = convertedSignature.toString();
            if (paramsBroken) {
                return;
            }
        }
        Type returnType = Type.VOID;
        if (paa.adviceKind == Definition.AdviceKind.Around) {
            ResolvedMember[] methods = delegateClass.getDeclaredMethods();
            ResolvedMember found = null;
            int length = methods.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                ResolvedMember candidate = methods[i2];
                if (candidate.getName().equals(methodName)) {
                    UnresolvedType[] cparms = candidate.getParameterTypes();
                    if (cparms.length == paramTypes.size()) {
                        boolean paramsMatch = true;
                        int i3 = 0;
                        while (true) {
                            if (i3 >= cparms.length) {
                                break;
                            }
                            if (cparms[i3].getSignature().equals(paramTypes.get(i3).getSignature())) {
                                i3++;
                            } else {
                                paramsMatch = false;
                                break;
                            }
                        }
                        if (paramsMatch) {
                            found = candidate;
                            break;
                        }
                    } else {
                        continue;
                    }
                }
                i2++;
            }
            if (found != null) {
                returnType = Type.getType(found.getReturnType().getSignature());
            } else {
                reportError("Unable to find method to invoke.  In class: " + delegateClass.getName() + " cant find " + paa.adviceMethod);
                return;
            }
        }
        LazyMethodGen advice = new LazyMethodGen(1, returnType, adviceName, (Type[]) paramTypes.toArray(new Type[paramTypes.size()]), EMPTY_STRINGS, cg);
        InstructionList adviceBody = advice.getBody();
        int pos2 = 1;
        for (int i4 = 0; i4 < paramTypes.size(); i4++) {
            adviceBody.append(InstructionFactory.createLoad(paramTypes.get(i4), pos2));
            pos2 += paramTypes.get(i4).getSize();
        }
        adviceBody.append(cg.getFactory().createInvoke(paa.adviceClass, methodName, signature + returnType.getSignature(), (short) 184));
        if (returnType == Type.VOID) {
            adviceBody.append(InstructionConstants.RETURN);
        } else if (returnType.getSignature().length() < 2) {
            String sig = returnType.getSignature();
            if (sig.equals("F")) {
                adviceBody.append(InstructionConstants.FRETURN);
            } else if (sig.equals("D")) {
                adviceBody.append(InstructionConstants.DRETURN);
            } else if (sig.equals("J")) {
                adviceBody.append(InstructionConstants.LRETURN);
            } else {
                adviceBody.append(InstructionConstants.IRETURN);
            }
        } else {
            adviceBody.append(InstructionConstants.ARETURN);
        }
        advice.addAnnotation(aaj);
        InstructionHandle start = adviceBody.getStart();
        start.addTargeter(new LocalVariableTag(StandardRoles.L + this.concreteAspect.name.replace('.', '/') + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, OgnlContext.THIS_CONTEXT_KEY, 0, start.getPosition()));
        if (paramNames.size() > 0) {
            for (int i5 = 0; i5 < paramNames.size(); i5++) {
                start.addTargeter(new LocalVariableTag(paramTypes.get(i5).getSignature(), paramNames.get(i5), i5 + 1, start.getPosition()));
            }
        }
        cg.addMethodGen(advice);
    }

    private AnnotationAJ buildAdviceAnnotation(LazyClassGen cg, Definition.PointcutAndAdvice paa) {
        SimpleElementValue svg = new SimpleElementValue(115, cg.getConstantPool(), paa.pointcut);
        List<NameValuePair> elems = new ArrayList<>();
        elems.add(new NameValuePair("value", svg, cg.getConstantPool()));
        AnnotationGen mag = new AnnotationGen(new ObjectType("org/aspectj/lang/annotation/" + paa.adviceKind.toString()), elems, true, cg.getConstantPool());
        AnnotationAJ aaj = new BcelAnnotation(mag, this.world);
        return aaj;
    }

    private void reportError(String message) throws AbortException {
        this.world.getMessageHandler().handleMessage(new Message(message, IMessage.ERROR, (Throwable) null, (ISourceLocation) null));
    }
}
