package org.aspectj.weaver.patterns;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.itextpdf.kernel.xmp.PdfConst;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.swagger.models.properties.StringProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.aspectj.lang.JoinPoint;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.MemberKind;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.internal.tools.PointcutDesignatorHandlerBasedPointcut;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.aspectj.weaver.patterns.IfPointcut;
import org.aspectj.weaver.tools.ContextBasedMatcher;
import org.aspectj.weaver.tools.PointcutDesignatorHandler;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.validation.DataBinder;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/PatternParser.class */
public class PatternParser {
    private ITokenSource tokenSource;
    private ISourceContext sourceContext;
    private boolean allowHasTypePatterns;
    private Set<PointcutDesignatorHandler> pointcutDesignatorHandlers;
    private World world;
    private IToken pendingRightArrows;

    public PatternParser(ITokenSource tokenSource) {
        this.allowHasTypePatterns = false;
        this.pointcutDesignatorHandlers = Collections.emptySet();
        this.tokenSource = tokenSource;
        this.sourceContext = tokenSource.getSourceContext();
    }

    public void setPointcutDesignatorHandlers(Set<PointcutDesignatorHandler> handlers, World world) {
        this.pointcutDesignatorHandlers = handlers;
        this.world = world;
    }

    public PerClause maybeParsePerClause() {
        IToken tok = this.tokenSource.peek();
        if (tok != IToken.EOF && tok.isIdentifier()) {
            String name = tok.getString();
            if (name.equals("issingleton")) {
                return parsePerSingleton();
            }
            if (name.equals("perthis")) {
                return parsePerObject(true);
            }
            if (name.equals("pertarget")) {
                return parsePerObject(false);
            }
            if (name.equals("percflow")) {
                return parsePerCflow(false);
            }
            if (name.equals("percflowbelow")) {
                return parsePerCflow(true);
            }
            if (name.equals("pertypewithin")) {
                return parsePerTypeWithin();
            }
            return null;
        }
        return null;
    }

    private PerClause parsePerCflow(boolean isBelow) {
        parseIdentifier();
        eat("(");
        Pointcut entry = parsePointcut();
        eat(")");
        return new PerCflow(entry, isBelow);
    }

    private PerClause parsePerObject(boolean isThis) {
        parseIdentifier();
        eat("(");
        Pointcut entry = parsePointcut();
        eat(")");
        return new PerObject(entry, isThis);
    }

    private PerClause parsePerTypeWithin() {
        parseIdentifier();
        eat("(");
        TypePattern withinTypePattern = parseTypePattern();
        eat(")");
        return new PerTypeWithin(withinTypePattern);
    }

    private PerClause parsePerSingleton() {
        parseIdentifier();
        eat("(");
        eat(")");
        return new PerSingleton();
    }

    public Declare parseDeclare() {
        Declare ret;
        int startPos = this.tokenSource.peek().getStart();
        eatIdentifier(AsmRelationshipUtils.DEC_LABEL);
        String kind = parseIdentifier();
        if (kind.equals(AsmRelationshipUtils.DECLARE_ERROR)) {
            eat(":");
            ret = parseErrorOrWarning(true);
        } else if (kind.equals(AsmRelationshipUtils.DECLARE_WARNING)) {
            eat(":");
            ret = parseErrorOrWarning(false);
        } else if (kind.equals(AsmRelationshipUtils.DECLARE_PRECEDENCE)) {
            eat(":");
            ret = parseDominates();
        } else {
            if (kind.equals("dominates")) {
                throw new ParserException("name changed to declare precedence", this.tokenSource.peek(-2));
            }
            if (kind.equals(AsmRelationshipUtils.DECLARE_PARENTS)) {
                ret = parseParents();
            } else if (kind.equals(AsmRelationshipUtils.DECLARE_SOFT)) {
                eat(":");
                ret = parseSoft();
            } else {
                throw new ParserException("expected one of error, warning, parents, soft, precedence, @type, @method, @constructor, @field", this.tokenSource.peek(-1));
            }
        }
        int endPos = this.tokenSource.peek(-1).getEnd();
        ret.setLocation(this.sourceContext, startPos, endPos);
        return ret;
    }

    public Declare parseDeclareAnnotation() {
        Declare ret;
        int startPos = this.tokenSource.peek().getStart();
        eatIdentifier(AsmRelationshipUtils.DEC_LABEL);
        eat("@");
        String kind = parseIdentifier();
        eat(":");
        if (kind.equals("type")) {
            ret = parseDeclareAtType();
        } else if (kind.equals(JamXmlElements.METHOD)) {
            ret = parseDeclareAtMethod(true);
        } else if (kind.equals(JamXmlElements.FIELD)) {
            ret = parseDeclareAtField();
        } else if (kind.equals("constructor")) {
            ret = parseDeclareAtMethod(false);
        } else {
            throw new ParserException("one of type, method, field, constructor", this.tokenSource.peek(-1));
        }
        eat(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        int endPos = this.tokenSource.peek(-1).getEnd();
        ret.setLocation(this.sourceContext, startPos, endPos);
        return ret;
    }

    public DeclareAnnotation parseDeclareAtType() {
        this.allowHasTypePatterns = true;
        TypePattern p = parseTypePattern();
        this.allowHasTypePatterns = false;
        return new DeclareAnnotation(DeclareAnnotation.AT_TYPE, p);
    }

    public DeclareAnnotation parseDeclareAtMethod(boolean isMethod) {
        ISignaturePattern sp = parseCompoundMethodOrConstructorSignaturePattern(isMethod);
        if (!isMethod) {
            return new DeclareAnnotation(DeclareAnnotation.AT_CONSTRUCTOR, sp);
        }
        return new DeclareAnnotation(DeclareAnnotation.AT_METHOD, sp);
    }

    public DeclareAnnotation parseDeclareAtField() {
        ISignaturePattern compoundFieldSignaturePattern = parseCompoundFieldSignaturePattern();
        DeclareAnnotation da = new DeclareAnnotation(DeclareAnnotation.AT_FIELD, compoundFieldSignaturePattern);
        return da;
    }

    public ISignaturePattern parseCompoundFieldSignaturePattern() {
        int index = this.tokenSource.getIndex();
        try {
            ISignaturePattern atomicFieldSignaturePattern = parseMaybeParenthesizedFieldSignaturePattern();
            while (isEitherAndOrOr()) {
                if (maybeEat("&&")) {
                    atomicFieldSignaturePattern = new AndSignaturePattern(atomicFieldSignaturePattern, parseMaybeParenthesizedFieldSignaturePattern());
                }
                if (maybeEat("||")) {
                    atomicFieldSignaturePattern = new OrSignaturePattern(atomicFieldSignaturePattern, parseMaybeParenthesizedFieldSignaturePattern());
                }
            }
            return atomicFieldSignaturePattern;
        } catch (ParserException e) {
            int nowAt = this.tokenSource.getIndex();
            this.tokenSource.setIndex(index);
            try {
                ISignaturePattern fsp = parseFieldSignaturePattern();
                return fsp;
            } catch (Exception e2) {
                this.tokenSource.setIndex(nowAt);
                throw e;
            }
        }
    }

    private boolean isEitherAndOrOr() {
        String tokenstring = this.tokenSource.peek().getString();
        return tokenstring.equals("&&") || tokenstring.equals("||");
    }

    public ISignaturePattern parseCompoundMethodOrConstructorSignaturePattern(boolean isMethod) {
        ISignaturePattern atomicMethodCtorSignaturePattern = parseMaybeParenthesizedMethodOrConstructorSignaturePattern(isMethod);
        while (isEitherAndOrOr()) {
            if (maybeEat("&&")) {
                atomicMethodCtorSignaturePattern = new AndSignaturePattern(atomicMethodCtorSignaturePattern, parseMaybeParenthesizedMethodOrConstructorSignaturePattern(isMethod));
            }
            if (maybeEat("||")) {
                atomicMethodCtorSignaturePattern = new OrSignaturePattern(atomicMethodCtorSignaturePattern, parseMaybeParenthesizedMethodOrConstructorSignaturePattern(isMethod));
            }
        }
        return atomicMethodCtorSignaturePattern;
    }

    public DeclarePrecedence parseDominates() {
        List<TypePattern> l = new ArrayList<>();
        do {
            l.add(parseTypePattern());
        } while (maybeEat(","));
        return new DeclarePrecedence(l);
    }

    private Declare parseParents() {
        eat(":");
        this.allowHasTypePatterns = true;
        TypePattern p = parseTypePattern(false, false);
        this.allowHasTypePatterns = false;
        IToken t = this.tokenSource.next();
        if (!t.getString().equals("extends") && !t.getString().equals("implements")) {
            throw new ParserException("extends or implements", t);
        }
        boolean isExtends = t.getString().equals("extends");
        List<TypePattern> l = new ArrayList<>();
        do {
            l.add(parseTypePattern());
        } while (maybeEat(","));
        DeclareParents decp = new DeclareParents(p, l, isExtends);
        return decp;
    }

    private Declare parseSoft() {
        TypePattern p = parseTypePattern();
        eat(":");
        Pointcut pointcut = parsePointcut();
        return new DeclareSoft(p, pointcut);
    }

    private Declare parseErrorOrWarning(boolean isError) {
        int index = this.tokenSource.getIndex();
        try {
            Pointcut pointcut = parsePointcut();
            eat(":");
            String message = parsePossibleStringSequence(true);
            return new DeclareErrorOrWarning(isError, pointcut, message);
        } catch (ParserException pe) {
            try {
                this.tokenSource.setIndex(index);
                boolean oldValue = this.allowHasTypePatterns;
                try {
                    this.allowHasTypePatterns = true;
                    TypePattern typePattern = parseTypePattern();
                    this.allowHasTypePatterns = oldValue;
                    eat(":");
                    String message2 = parsePossibleStringSequence(true);
                    return new DeclareTypeErrorOrWarning(isError, typePattern, message2);
                } catch (Throwable th) {
                    this.allowHasTypePatterns = oldValue;
                    throw th;
                }
            } catch (ParserException e) {
                throw pe;
            }
        }
    }

    public Pointcut parsePointcut() {
        Pointcut p = parseAtomicPointcut();
        if (maybeEat("&&")) {
            p = new AndPointcut(p, parseNotOrPointcut());
        }
        if (maybeEat("||")) {
            p = new OrPointcut(p, parsePointcut());
        }
        return p;
    }

    private Pointcut parseNotOrPointcut() {
        Pointcut p = parseAtomicPointcut();
        if (maybeEat("&&")) {
            p = new AndPointcut(p, parseNotOrPointcut());
        }
        return p;
    }

    private Pointcut parseAtomicPointcut() {
        if (maybeEat("!")) {
            int startPos = this.tokenSource.peek(-1).getStart();
            return new NotPointcut(parseAtomicPointcut(), startPos);
        }
        if (maybeEat("(")) {
            Pointcut p = parsePointcut();
            eat(")");
            return p;
        }
        if (maybeEat("@")) {
            int startPos2 = this.tokenSource.peek().getStart();
            Pointcut p2 = parseAnnotationPointcut();
            int endPos = this.tokenSource.peek(-1).getEnd();
            p2.setLocation(this.sourceContext, startPos2, endPos);
            return p2;
        }
        int startPos3 = this.tokenSource.peek().getStart();
        Pointcut p3 = parseSinglePointcut();
        int endPos2 = this.tokenSource.peek(-1).getEnd();
        p3.setLocation(this.sourceContext, startPos3, endPos2);
        return p3;
    }

    public Pointcut parseSinglePointcut() {
        int start = this.tokenSource.getIndex();
        IToken t = this.tokenSource.peek();
        Pointcut p = t.maybeGetParsedPointcut();
        if (p != null) {
            this.tokenSource.next();
            return p;
        }
        String kind = parseIdentifier();
        if (kind.equals("execution") || kind.equals("call") || kind.equals(BeanUtil.PREFIX_GETTER_GET) || kind.equals("set")) {
            p = parseKindedPointcut(kind);
        } else if (kind.equals("args")) {
            p = parseArgsPointcut();
        } else if (kind.equals(OgnlContext.THIS_CONTEXT_KEY) || kind.equals(DataBinder.DEFAULT_OBJECT_NAME)) {
            p = parseThisOrTargetPointcut(kind);
        } else if (kind.equals("within")) {
            p = parseWithinPointcut();
        } else if (kind.equals("withincode")) {
            p = parseWithinCodePointcut();
        } else if (kind.equals("cflow")) {
            p = parseCflowPointcut(false);
        } else if (kind.equals("cflowbelow")) {
            p = parseCflowPointcut(true);
        } else if (kind.equals(JoinPoint.ADVICE_EXECUTION)) {
            eat("(");
            eat(")");
            p = new KindedPointcut(Shadow.AdviceExecution, new SignaturePattern(Member.ADVICE, ModifiersPattern.ANY, TypePattern.ANY, TypePattern.ANY, NamePattern.ANY, TypePatternList.ANY, ThrowsPattern.ANY, AnnotationTypePattern.ANY));
        } else if (kind.equals("handler")) {
            eat("(");
            TypePattern typePat = parseTypePattern(false, false);
            eat(")");
            p = new HandlerPointcut(typePat);
        } else if (kind.equals(JoinPoint.SYNCHRONIZATION_LOCK) || kind.equals(JoinPoint.SYNCHRONIZATION_UNLOCK)) {
            p = parseMonitorPointcut(kind);
        } else if (kind.equals(JoinPoint.INITIALIZATION)) {
            eat("(");
            SignaturePattern sig = parseConstructorSignaturePattern();
            eat(")");
            p = new KindedPointcut(Shadow.Initialization, sig);
        } else if (kind.equals(JoinPoint.STATICINITIALIZATION)) {
            eat("(");
            TypePattern typePat2 = parseTypePattern(false, false);
            eat(")");
            p = new KindedPointcut(Shadow.StaticInitialization, new SignaturePattern(Member.STATIC_INITIALIZATION, ModifiersPattern.ANY, TypePattern.ANY, typePat2, NamePattern.ANY, TypePatternList.EMPTY, ThrowsPattern.ANY, AnnotationTypePattern.ANY));
        } else if (kind.equals(JoinPoint.PREINITIALIZATION)) {
            eat("(");
            SignaturePattern sig2 = parseConstructorSignaturePattern();
            eat(")");
            p = new KindedPointcut(Shadow.PreInitialization, sig2);
        } else if (kind.equals("if")) {
            eat("(");
            if (maybeEatIdentifier("true")) {
                eat(")");
                p = new IfPointcut.IfTruePointcut();
            } else if (maybeEatIdentifier("false")) {
                eat(")");
                p = new IfPointcut.IfFalsePointcut();
            } else {
                if (!maybeEat(")")) {
                    throw new ParserException("in annotation style, if(...) pointcuts cannot contain code. Use if() and put the code in the annotated method", t);
                }
                p = new IfPointcut("");
            }
        } else {
            boolean matchedByExtensionDesignator = false;
            for (PointcutDesignatorHandler pcd : this.pointcutDesignatorHandlers) {
                if (pcd.getDesignatorName().equals(kind)) {
                    p = parseDesignatorPointcut(pcd);
                    matchedByExtensionDesignator = true;
                }
            }
            if (!matchedByExtensionDesignator) {
                this.tokenSource.setIndex(start);
                p = parseReferencePointcut();
            }
        }
        return p;
    }

    private void assertNoTypeVariables(String[] tvs, String errorMessage, IToken token) {
        if (tvs != null) {
            throw new ParserException(errorMessage, token);
        }
    }

    public Pointcut parseAnnotationPointcut() {
        int start = this.tokenSource.getIndex();
        IToken t = this.tokenSource.peek();
        String kind = parseIdentifier();
        IToken possibleTypeVariableToken = this.tokenSource.peek();
        String[] typeVariables = maybeParseSimpleTypeVariableList();
        if (typeVariables != null) {
            assertNoTypeVariables(typeVariables, "(", possibleTypeVariableToken);
        }
        this.tokenSource.setIndex(start);
        if (kind.equals(JamXmlElements.ANNOTATION)) {
            return parseAtAnnotationPointcut();
        }
        if (kind.equals("args")) {
            return parseArgsAnnotationPointcut();
        }
        if (kind.equals(OgnlContext.THIS_CONTEXT_KEY) || kind.equals(DataBinder.DEFAULT_OBJECT_NAME)) {
            return parseThisOrTargetAnnotationPointcut();
        }
        if (kind.equals("within")) {
            return parseWithinAnnotationPointcut();
        }
        if (kind.equals("withincode")) {
            return parseWithinCodeAnnotationPointcut();
        }
        throw new ParserException("pointcut name", t);
    }

    private Pointcut parseAtAnnotationPointcut() {
        parseIdentifier();
        eat("(");
        if (maybeEat(")")) {
            throw new ParserException("@AnnotationName or parameter", this.tokenSource.peek());
        }
        ExactAnnotationTypePattern type = parseAnnotationNameOrVarTypePattern();
        eat(")");
        return new AnnotationPointcut(type);
    }

    private SignaturePattern parseConstructorSignaturePattern() {
        SignaturePattern ret = parseMethodOrConstructorSignaturePattern();
        if (ret.getKind() == Member.CONSTRUCTOR) {
            return ret;
        }
        throw new ParserException("constructor pattern required, found method pattern", ret);
    }

    private Pointcut parseWithinCodePointcut() {
        eat("(");
        SignaturePattern sig = parseMethodOrConstructorSignaturePattern();
        eat(")");
        return new WithincodePointcut(sig);
    }

    private Pointcut parseCflowPointcut(boolean isBelow) {
        eat("(");
        Pointcut entry = parsePointcut();
        eat(")");
        return new CflowPointcut(entry, isBelow, null);
    }

    private Pointcut parseWithinPointcut() {
        eat("(");
        TypePattern type = parseTypePattern();
        eat(")");
        return new WithinPointcut(type);
    }

    private Pointcut parseThisOrTargetPointcut(String kind) {
        eat("(");
        TypePattern type = parseTypePattern();
        eat(")");
        return new ThisOrTargetPointcut(kind.equals(OgnlContext.THIS_CONTEXT_KEY), type);
    }

    private Pointcut parseThisOrTargetAnnotationPointcut() {
        String kind = parseIdentifier();
        eat("(");
        if (maybeEat(")")) {
            throw new ParserException("expecting @AnnotationName or parameter, but found ')'", this.tokenSource.peek());
        }
        ExactAnnotationTypePattern type = parseAnnotationNameOrVarTypePattern();
        eat(")");
        return new ThisOrTargetAnnotationPointcut(kind.equals(OgnlContext.THIS_CONTEXT_KEY), type);
    }

    private Pointcut parseWithinAnnotationPointcut() {
        parseIdentifier();
        eat("(");
        if (maybeEat(")")) {
            throw new ParserException("expecting @AnnotationName or parameter, but found ')'", this.tokenSource.peek());
        }
        AnnotationTypePattern type = parseAnnotationNameOrVarTypePattern();
        eat(")");
        return new WithinAnnotationPointcut(type);
    }

    private Pointcut parseWithinCodeAnnotationPointcut() {
        parseIdentifier();
        eat("(");
        if (maybeEat(")")) {
            throw new ParserException("expecting @AnnotationName or parameter, but found ')'", this.tokenSource.peek());
        }
        ExactAnnotationTypePattern type = parseAnnotationNameOrVarTypePattern();
        eat(")");
        return new WithinCodeAnnotationPointcut(type);
    }

    private Pointcut parseArgsPointcut() {
        TypePatternList arguments = parseArgumentsPattern(false);
        return new ArgsPointcut(arguments);
    }

    private Pointcut parseArgsAnnotationPointcut() {
        parseIdentifier();
        AnnotationPatternList arguments = parseArgumentsAnnotationPattern();
        return new ArgsAnnotationPointcut(arguments);
    }

    private Pointcut parseReferencePointcut() {
        NamePattern name;
        TypePattern onType = parseTypePattern();
        if (onType.typeParameters.size() > 0) {
            eat(".");
            name = parseNamePattern();
        } else {
            name = tryToExtractName(onType);
        }
        if (name == null) {
            throw new ParserException("name pattern", this.tokenSource.peek());
        }
        if (onType.toString().equals("")) {
            onType = null;
        }
        String simpleName = name.maybeGetSimpleName();
        if (simpleName == null) {
            throw new ParserException("(", this.tokenSource.peek(-1));
        }
        TypePatternList arguments = parseArgumentsPattern(false);
        return new ReferencePointcut(onType, simpleName, arguments);
    }

    private Pointcut parseDesignatorPointcut(PointcutDesignatorHandler pcdHandler) {
        eat("(");
        int parenCount = 1;
        StringBuffer pointcutBody = new StringBuffer();
        while (parenCount > 0) {
            if (maybeEat("(")) {
                parenCount++;
                pointcutBody.append("(");
            } else if (maybeEat(")")) {
                parenCount--;
                if (parenCount > 0) {
                    pointcutBody.append(")");
                }
            } else {
                pointcutBody.append(nextToken().getString());
            }
        }
        ContextBasedMatcher pcExpr = pcdHandler.parse(pointcutBody.toString());
        return new PointcutDesignatorHandlerBasedPointcut(pcExpr, this.world);
    }

    public List<String> parseDottedIdentifier() {
        List<String> ret = new ArrayList<>();
        ret.add(parseIdentifier());
        while (maybeEat(".")) {
            ret.add(parseIdentifier());
        }
        return ret;
    }

    private KindedPointcut parseKindedPointcut(String kind) {
        SignaturePattern sig;
        eat("(");
        Shadow.Kind shadowKind = null;
        if (kind.equals("execution")) {
            sig = parseMethodOrConstructorSignaturePattern();
            if (sig.getKind() == Member.METHOD) {
                shadowKind = Shadow.MethodExecution;
            } else if (sig.getKind() == Member.CONSTRUCTOR) {
                shadowKind = Shadow.ConstructorExecution;
            }
        } else if (kind.equals("call")) {
            sig = parseMethodOrConstructorSignaturePattern();
            if (sig.getKind() == Member.METHOD) {
                shadowKind = Shadow.MethodCall;
            } else if (sig.getKind() == Member.CONSTRUCTOR) {
                shadowKind = Shadow.ConstructorCall;
            }
        } else if (kind.equals(BeanUtil.PREFIX_GETTER_GET)) {
            sig = parseFieldSignaturePattern();
            shadowKind = Shadow.FieldGet;
        } else if (kind.equals("set")) {
            sig = parseFieldSignaturePattern();
            shadowKind = Shadow.FieldSet;
        } else {
            throw new ParserException("bad kind: " + kind, this.tokenSource.peek());
        }
        eat(")");
        return new KindedPointcut(shadowKind, sig);
    }

    private KindedPointcut parseMonitorPointcut(String kind) {
        eat("(");
        eat(")");
        if (kind.equals(JoinPoint.SYNCHRONIZATION_LOCK)) {
            return new KindedPointcut(Shadow.SynchronizationLock, new SignaturePattern(Member.MONITORENTER, ModifiersPattern.ANY, TypePattern.ANY, TypePattern.ANY, NamePattern.ANY, TypePatternList.ANY, ThrowsPattern.ANY, AnnotationTypePattern.ANY));
        }
        return new KindedPointcut(Shadow.SynchronizationUnlock, new SignaturePattern(Member.MONITORENTER, ModifiersPattern.ANY, TypePattern.ANY, TypePattern.ANY, NamePattern.ANY, TypePatternList.ANY, ThrowsPattern.ANY, AnnotationTypePattern.ANY));
    }

    public TypePattern parseTypePattern() {
        return parseTypePattern(false, false);
    }

    public TypePattern parseTypePattern(boolean insideTypeParameters, boolean parameterAnnotationsPossible) {
        TypePattern p = parseAtomicTypePattern(insideTypeParameters, parameterAnnotationsPossible);
        if (maybeEat("&&")) {
            p = new AndTypePattern(p, parseNotOrTypePattern(insideTypeParameters, parameterAnnotationsPossible));
        }
        if (maybeEat("||")) {
            p = new OrTypePattern(p, parseTypePattern(insideTypeParameters, parameterAnnotationsPossible));
        }
        return p;
    }

    private TypePattern parseNotOrTypePattern(boolean insideTypeParameters, boolean parameterAnnotationsPossible) {
        TypePattern p = parseAtomicTypePattern(insideTypeParameters, parameterAnnotationsPossible);
        if (maybeEat("&&")) {
            p = new AndTypePattern(p, parseTypePattern(insideTypeParameters, parameterAnnotationsPossible));
        }
        return p;
    }

    private TypePattern parseAtomicTypePattern(boolean insideTypeParameters, boolean parameterAnnotationsPossible) {
        TypePattern p;
        TypePattern p2;
        AnnotationTypePattern ap = maybeParseAnnotationPattern();
        if (maybeEat("!")) {
            TypePattern tp = parseAtomicTypePattern(insideTypeParameters, parameterAnnotationsPossible);
            if (!(ap instanceof AnyAnnotationTypePattern)) {
                TypePattern p3 = new NotTypePattern(tp);
                p2 = new AndTypePattern(setAnnotationPatternForTypePattern(TypePattern.ANY, ap, false), p3);
            } else {
                p2 = new NotTypePattern(tp);
            }
            return p2;
        }
        if (maybeEat("(")) {
            int openParenPos = this.tokenSource.peek(-1).getStart();
            TypePattern p4 = parseTypePattern(insideTypeParameters, false);
            if ((p4 instanceof NotTypePattern) && !(ap instanceof AnyAnnotationTypePattern)) {
                p = new AndTypePattern(setAnnotationPatternForTypePattern(TypePattern.ANY, ap, parameterAnnotationsPossible), p4);
            } else {
                p = setAnnotationPatternForTypePattern(p4, ap, parameterAnnotationsPossible);
            }
            eat(")");
            int closeParenPos = this.tokenSource.peek(-1).getStart();
            boolean isVarArgs = maybeEat("...");
            if (isVarArgs) {
                p.setIsVarArgs(isVarArgs);
            }
            boolean isIncludeSubtypes = maybeEat("+");
            if (isIncludeSubtypes) {
                p.includeSubtypes = true;
            }
            p.start = openParenPos;
            p.end = closeParenPos;
            return p;
        }
        int startPos = this.tokenSource.peek().getStart();
        if (ap.start != -1) {
            startPos = ap.start;
        }
        TypePattern p5 = parseSingleTypePattern(insideTypeParameters);
        int endPos = this.tokenSource.peek(-1).getEnd();
        TypePattern p6 = setAnnotationPatternForTypePattern(p5, ap, false);
        p6.setLocation(this.sourceContext, startPos, endPos);
        return p6;
    }

    private TypePattern setAnnotationPatternForTypePattern(TypePattern t, AnnotationTypePattern ap, boolean parameterAnnotationsPattern) {
        if (parameterAnnotationsPattern) {
            ap.setForParameterAnnotationMatch();
        }
        if (ap != AnnotationTypePattern.ANY) {
            if (t == TypePattern.ANY) {
                if (t.annotationPattern == AnnotationTypePattern.ANY) {
                    return new AnyWithAnnotationTypePattern(ap);
                }
                return new AnyWithAnnotationTypePattern(new AndAnnotationTypePattern(ap, t.annotationPattern));
            }
            if (t.annotationPattern == AnnotationTypePattern.ANY) {
                t.setAnnotationTypePattern(ap);
            } else {
                t.setAnnotationTypePattern(new AndAnnotationTypePattern(ap, t.annotationPattern));
            }
        }
        return t;
    }

    public AnnotationTypePattern maybeParseAnnotationPattern() {
        AnnotationTypePattern ret = AnnotationTypePattern.ANY;
        while (true) {
            AnnotationTypePattern nextPattern = maybeParseSingleAnnotationPattern();
            if (nextPattern != null) {
                if (ret == AnnotationTypePattern.ANY) {
                    ret = nextPattern;
                } else {
                    ret = new AndAnnotationTypePattern(ret, nextPattern);
                }
            } else {
                return ret;
            }
        }
    }

    public AnnotationTypePattern maybeParseSingleAnnotationPattern() {
        AnnotationTypePattern ret;
        AnnotationTypePattern ret2;
        int startIndex = this.tokenSource.getIndex();
        if (maybeEat("!")) {
            if (maybeEat("@")) {
                if (maybeEat("(")) {
                    AnnotationTypePattern ret3 = new NotAnnotationTypePattern(new WildAnnotationTypePattern(parseTypePattern()));
                    eat(")");
                    return ret3;
                }
                TypePattern p = parseSingleTypePattern();
                if (maybeEatAdjacent("(")) {
                    Map<String, String> values = parseAnnotationValues();
                    eat(")");
                    ret2 = new NotAnnotationTypePattern(new WildAnnotationTypePattern(p, values));
                } else {
                    ret2 = new NotAnnotationTypePattern(new WildAnnotationTypePattern(p));
                }
                return ret2;
            }
            this.tokenSource.setIndex(startIndex);
            return null;
        }
        if (maybeEat("@")) {
            if (maybeEat("(")) {
                AnnotationTypePattern ret4 = new WildAnnotationTypePattern(parseTypePattern());
                eat(")");
                return ret4;
            }
            int atPos = this.tokenSource.peek(-1).getStart();
            TypePattern p2 = parseSingleTypePattern();
            if (maybeEatAdjacent("(")) {
                Map<String, String> values2 = parseAnnotationValues();
                eat(")");
                ret = new WildAnnotationTypePattern(p2, values2);
            } else {
                ret = new WildAnnotationTypePattern(p2);
            }
            ret.start = atPos;
            return ret;
        }
        this.tokenSource.setIndex(startIndex);
        return null;
    }

    public Map<String, String> parseAnnotationValues() {
        Map<String, String> values = new HashMap<>();
        boolean seenDefaultValue = false;
        do {
            String possibleKeyString = parseAnnotationNameValuePattern();
            if (possibleKeyString == null) {
                throw new ParserException("expecting simple literal ", this.tokenSource.peek(-1));
            }
            if (maybeEat(SymbolConstants.EQUAL_SYMBOL)) {
                String valueString = parseAnnotationNameValuePattern();
                if (valueString == null) {
                    throw new ParserException("expecting simple literal ", this.tokenSource.peek(-1));
                }
                values.put(possibleKeyString, valueString);
            } else if (maybeEat("!=")) {
                String valueString2 = parseAnnotationNameValuePattern();
                if (valueString2 == null) {
                    throw new ParserException("expecting simple literal ", this.tokenSource.peek(-1));
                }
                values.put(possibleKeyString + "!", valueString2);
            } else {
                if (seenDefaultValue) {
                    throw new ParserException("cannot specify two default values", this.tokenSource.peek(-1));
                }
                seenDefaultValue = true;
                values.put("value", possibleKeyString);
            }
        } while (maybeEat(","));
        return values;
    }

    public TypePattern parseSingleTypePattern() {
        return parseSingleTypePattern(false);
    }

    public TypePattern parseSingleTypePattern(boolean insideTypeParameters) {
        if (insideTypeParameters && maybeEat("?")) {
            return parseGenericsWildcardTypePattern();
        }
        if (this.allowHasTypePatterns) {
            if (maybeEatIdentifier("hasmethod")) {
                return parseHasMethodTypePattern();
            }
            if (maybeEatIdentifier("hasfield")) {
                return parseHasFieldTypePattern();
            }
        }
        if (maybeEatIdentifier(BeanUtil.PREFIX_GETTER_IS)) {
            int pos = this.tokenSource.getIndex() - 1;
            TypePattern typeIsPattern = parseIsTypePattern();
            if (typeIsPattern != null) {
                return typeIsPattern;
            }
            this.tokenSource.setIndex(pos);
        }
        List<NamePattern> names = parseDottedNamePattern();
        int dim = 0;
        while (maybeEat(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
            eat("]");
            dim++;
        }
        TypePatternList typeParameters = maybeParseTypeParameterList();
        int endPos = this.tokenSource.peek(-1).getEnd();
        boolean includeSubtypes = maybeEat("+");
        while (maybeEat(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
            eat("]");
            dim++;
        }
        boolean isVarArgs = maybeEat("...");
        if (names.size() == 1 && names.get(0).isAny() && dim == 0 && !isVarArgs && typeParameters == null) {
            return TypePattern.ANY;
        }
        return new WildTypePattern(names, includeSubtypes, dim + (isVarArgs ? 1 : 0), endPos, isVarArgs, typeParameters);
    }

    public TypePattern parseHasMethodTypePattern() {
        int startPos = this.tokenSource.peek(-1).getStart();
        eat("(");
        SignaturePattern sp = parseMethodOrConstructorSignaturePattern();
        eat(")");
        int endPos = this.tokenSource.peek(-1).getEnd();
        HasMemberTypePattern ret = new HasMemberTypePattern(sp);
        ret.setLocation(this.sourceContext, startPos, endPos);
        return ret;
    }

    public TypePattern parseIsTypePattern() {
        int startPos = this.tokenSource.peek(-1).getStart();
        if (!maybeEatAdjacent("(")) {
            return null;
        }
        IToken token = this.tokenSource.next();
        TypeCategoryTypePattern typeIsPattern = null;
        if (token.isIdentifier()) {
            String category = token.getString();
            if (category.equals("ClassType")) {
                typeIsPattern = new TypeCategoryTypePattern(1);
            } else if (category.equals("AspectType")) {
                typeIsPattern = new TypeCategoryTypePattern(3);
            } else if (category.equals("InterfaceType")) {
                typeIsPattern = new TypeCategoryTypePattern(2);
            } else if (category.equals("InnerType")) {
                typeIsPattern = new TypeCategoryTypePattern(4);
            } else if (category.equals("AnonymousType")) {
                typeIsPattern = new TypeCategoryTypePattern(5);
            } else if (category.equals("EnumType")) {
                typeIsPattern = new TypeCategoryTypePattern(6);
            } else if (category.equals("AnnotationType")) {
                typeIsPattern = new TypeCategoryTypePattern(7);
            } else if (category.equals("FinalType")) {
                typeIsPattern = new TypeCategoryTypePattern(8);
            }
        }
        if (typeIsPattern == null) {
            return null;
        }
        if (!maybeEat(")")) {
            throw new ParserException(")", this.tokenSource.peek());
        }
        int endPos = this.tokenSource.peek(-1).getEnd();
        typeIsPattern.setLocation(this.tokenSource.getSourceContext(), startPos, endPos);
        return typeIsPattern;
    }

    public TypePattern parseHasFieldTypePattern() {
        int startPos = this.tokenSource.peek(-1).getStart();
        eat("(");
        SignaturePattern sp = parseFieldSignaturePattern();
        eat(")");
        int endPos = this.tokenSource.peek(-1).getEnd();
        HasMemberTypePattern ret = new HasMemberTypePattern(sp);
        ret.setLocation(this.sourceContext, startPos, endPos);
        return ret;
    }

    public TypePattern parseGenericsWildcardTypePattern() {
        List<NamePattern> names = new ArrayList<>();
        names.add(new NamePattern("?"));
        TypePattern upperBound = null;
        TypePattern[] additionalInterfaceBounds = new TypePattern[0];
        TypePattern lowerBound = null;
        if (maybeEatIdentifier("extends")) {
            upperBound = parseTypePattern(false, false);
            additionalInterfaceBounds = maybeParseAdditionalInterfaceBounds();
        }
        if (maybeEatIdentifier("super")) {
            lowerBound = parseTypePattern(false, false);
        }
        int endPos = this.tokenSource.peek(-1).getEnd();
        return new WildTypePattern(names, false, 0, endPos, false, null, upperBound, additionalInterfaceBounds, lowerBound);
    }

    protected ExactAnnotationTypePattern parseAnnotationNameOrVarTypePattern() {
        int startPos = this.tokenSource.peek().getStart();
        if (maybeEat("@")) {
            throw new ParserException("@Foo form was deprecated in AspectJ 5 M2: annotation name or var ", this.tokenSource.peek(-1));
        }
        ExactAnnotationTypePattern p = parseSimpleAnnotationName();
        int endPos = this.tokenSource.peek(-1).getEnd();
        p.setLocation(this.sourceContext, startPos, endPos);
        if (maybeEat("(")) {
            String formalName = parseIdentifier();
            p = new ExactAnnotationFieldTypePattern(p, formalName);
            eat(")");
        }
        return p;
    }

    private ExactAnnotationTypePattern parseSimpleAnnotationName() {
        StringBuffer annotationName = new StringBuffer();
        annotationName.append(parseIdentifier());
        while (maybeEat(".")) {
            annotationName.append('.');
            annotationName.append(parseIdentifier());
        }
        UnresolvedType type = UnresolvedType.forName(annotationName.toString());
        ExactAnnotationTypePattern p = new ExactAnnotationTypePattern(type, null);
        return p;
    }

    public List<NamePattern> parseDottedNamePattern() {
        IToken tok;
        boolean onADot;
        List<NamePattern> names = new ArrayList<>();
        StringBuffer buf = new StringBuffer();
        IToken previous = null;
        boolean justProcessedEllipsis = false;
        boolean justProcessedDot = false;
        while (true) {
            int startPos = this.tokenSource.peek().getStart();
            String afterDot = null;
            while (true) {
                if (previous != null && previous.getString().equals(".")) {
                    justProcessedDot = true;
                }
                tok = this.tokenSource.peek();
                onADot = tok.getString().equals(".");
                if (previous != null && !isAdjacent(previous, tok)) {
                    break;
                }
                if (tok.getString() == "*" || (tok.isIdentifier() && tok.getString() != "...")) {
                    buf.append(tok.getString());
                } else {
                    if (tok.getString() == "..." || tok.getLiteralKind() == null) {
                        break;
                    }
                    String s = tok.getString();
                    int dot = s.indexOf(46);
                    if (dot != -1) {
                        buf.append(s.substring(0, dot));
                        afterDot = s.substring(dot + 1);
                        previous = this.tokenSource.next();
                        break;
                    }
                    buf.append(s);
                }
                previous = this.tokenSource.next();
            }
            int endPos = this.tokenSource.peek(-1).getEnd();
            if (buf.length() == 0 && names.isEmpty()) {
                throw new ParserException("name pattern", tok);
            }
            if (buf.length() == 0 && justProcessedEllipsis) {
                throw new ParserException("name pattern cannot finish with ..", tok);
            }
            if (buf.length() == 0 && justProcessedDot && !onADot) {
                throw new ParserException("name pattern cannot finish with .", tok);
            }
            if (buf.length() == 0) {
                names.add(NamePattern.ELLIPSIS);
                justProcessedEllipsis = true;
            } else {
                checkLegalName(buf.toString(), previous);
                NamePattern ret = new NamePattern(buf.toString());
                ret.setLocation(this.sourceContext, startPos, endPos);
                names.add(ret);
                justProcessedEllipsis = false;
            }
            if (afterDot == null) {
                buf.setLength(0);
                if (maybeEat(".")) {
                    previous = this.tokenSource.peek(-1);
                } else {
                    return names;
                }
            } else {
                buf.setLength(0);
                buf.append(afterDot);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x0102, code lost:
    
        return r0.toString();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String parseAnnotationNameValuePattern() {
        /*
            r5 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r1 = r0
            r1.<init>()
            r6 = r0
            r0 = r5
            org.aspectj.weaver.patterns.ITokenSource r0 = r0.tokenSource
            org.aspectj.weaver.patterns.IToken r0 = r0.peek()
            int r0 = r0.getStart()
            r0 = 0
            r8 = r0
            r0 = 0
            r9 = r0
        L1c:
            r0 = r5
            org.aspectj.weaver.patterns.ITokenSource r0 = r0.tokenSource
            org.aspectj.weaver.patterns.IToken r0 = r0.peek()
            r7 = r0
            r0 = r7
            java.lang.String r0 = r0.getString()
            java.lang.String r1 = ")"
            if (r0 != r1) goto L39
            r0 = r9
            if (r0 != 0) goto L39
            goto Lfe
        L39:
            r0 = r7
            java.lang.String r0 = r0.getString()
            java.lang.String r1 = "!="
            if (r0 != r1) goto L4d
            r0 = r9
            if (r0 != 0) goto L4d
            goto Lfe
        L4d:
            r0 = r7
            java.lang.String r0 = r0.getString()
            java.lang.String r1 = "="
            if (r0 != r1) goto L61
            r0 = r9
            if (r0 != 0) goto L61
            goto Lfe
        L61:
            r0 = r7
            java.lang.String r0 = r0.getString()
            java.lang.String r1 = ","
            if (r0 != r1) goto L74
            r0 = r9
            if (r0 != 0) goto L74
            goto Lfe
        L74:
            r0 = r7
            org.aspectj.weaver.patterns.IToken r1 = org.aspectj.weaver.patterns.IToken.EOF
            if (r0 != r1) goto L8f
            org.aspectj.weaver.patterns.ParserException r0 = new org.aspectj.weaver.patterns.ParserException
            r1 = r0
            java.lang.String r2 = "eof"
            r3 = r5
            org.aspectj.weaver.patterns.ITokenSource r3 = r3.tokenSource
            org.aspectj.weaver.patterns.IToken r3 = r3.peek()
            r1.<init>(r2, r3)
            throw r0
        L8f:
            r0 = r7
            java.lang.String r0 = r0.getString()
            java.lang.String r1 = "("
            if (r0 != r1) goto L9d
            int r9 = r9 + 1
        L9d:
            r0 = r7
            java.lang.String r0 = r0.getString()
            java.lang.String r1 = ")"
            if (r0 != r1) goto Lab
            int r9 = r9 + (-1)
        Lab:
            r0 = r7
            java.lang.String r0 = r0.getString()
            java.lang.String r1 = "{"
            if (r0 != r1) goto Lba
            int r9 = r9 + 1
        Lba:
            r0 = r7
            java.lang.String r0 = r0.getString()
            java.lang.String r1 = "}"
            if (r0 != r1) goto Lc9
            int r9 = r9 + (-1)
        Lc9:
            r0 = r7
            java.lang.String r0 = r0.getString()
            java.lang.String r1 = "."
            if (r0 != r1) goto Le4
            r0 = r8
            if (r0 != 0) goto Le4
            org.aspectj.weaver.patterns.ParserException r0 = new org.aspectj.weaver.patterns.ParserException
            r1 = r0
            java.lang.String r2 = "dot not expected"
            r3 = r7
            r1.<init>(r2, r3)
            throw r0
        Le4:
            r0 = r6
            r1 = r7
            java.lang.String r1 = r1.getString()
            java.lang.StringBuffer r0 = r0.append(r1)
            r0 = r5
            org.aspectj.weaver.patterns.ITokenSource r0 = r0.tokenSource
            org.aspectj.weaver.patterns.IToken r0 = r0.next()
            r0 = 1
            r8 = r0
            goto L1c
        Lfe:
            r0 = r6
            java.lang.String r0 = r0.toString()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.weaver.patterns.PatternParser.parseAnnotationNameValuePattern():java.lang.String");
    }

    public NamePattern parseNamePattern() {
        IToken tok;
        StringBuffer buf = new StringBuffer();
        IToken previous = null;
        int startPos = this.tokenSource.peek().getStart();
        while (true) {
            tok = this.tokenSource.peek();
            if (previous != null && !isAdjacent(previous, tok)) {
                break;
            }
            if (tok.getString() == "*" || tok.isIdentifier()) {
                buf.append(tok.getString());
            } else {
                if (tok.getLiteralKind() == null) {
                    break;
                }
                String s = tok.getString();
                if (s.indexOf(46) != -1) {
                    break;
                }
                buf.append(s);
            }
            previous = this.tokenSource.next();
        }
        int endPos = this.tokenSource.peek(-1).getEnd();
        if (buf.length() == 0) {
            throw new ParserException("name pattern", tok);
        }
        checkLegalName(buf.toString(), previous);
        NamePattern ret = new NamePattern(buf.toString());
        ret.setLocation(this.sourceContext, startPos, endPos);
        return ret;
    }

    private void checkLegalName(String s, IToken tok) {
        char ch2 = s.charAt(0);
        if (ch2 != '*' && !Character.isJavaIdentifierStart(ch2)) {
            throw new ParserException("illegal identifier start (" + ch2 + ")", tok);
        }
        int len = s.length();
        for (int i = 1; i < len; i++) {
            char ch3 = s.charAt(i);
            if (ch3 != '*' && !Character.isJavaIdentifierPart(ch3)) {
                throw new ParserException("illegal identifier character (" + ch3 + ")", tok);
            }
        }
    }

    private boolean isAdjacent(IToken first, IToken second) {
        return first.getEnd() == second.getStart() - 1;
    }

    public ModifiersPattern parseModifiersPattern() {
        int start;
        int requiredFlags = 0;
        int forbiddenFlags = 0;
        while (true) {
            start = this.tokenSource.getIndex();
            boolean isForbidden = maybeEat("!");
            IToken t = this.tokenSource.next();
            int flag = ModifiersPattern.getModifierFlag(t.getString());
            if (flag == -1) {
                break;
            }
            if (isForbidden) {
                forbiddenFlags |= flag;
            } else {
                requiredFlags |= flag;
            }
        }
        this.tokenSource.setIndex(start);
        if (requiredFlags == 0 && forbiddenFlags == 0) {
            return ModifiersPattern.ANY;
        }
        return new ModifiersPattern(requiredFlags, forbiddenFlags);
    }

    public TypePatternList parseArgumentsPattern(boolean parameterAnnotationsPossible) {
        List<TypePattern> patterns = new ArrayList<>();
        eat("(");
        if (maybeEat(")")) {
            return new TypePatternList();
        }
        do {
            if (maybeEat(".")) {
                eat(".");
                patterns.add(TypePattern.ELLIPSIS);
            } else {
                patterns.add(parseTypePattern(false, parameterAnnotationsPossible));
            }
        } while (maybeEat(","));
        eat(")");
        return new TypePatternList(patterns);
    }

    public AnnotationPatternList parseArgumentsAnnotationPattern() {
        List<AnnotationTypePattern> patterns = new ArrayList<>();
        eat("(");
        if (maybeEat(")")) {
            return new AnnotationPatternList();
        }
        do {
            if (maybeEat(".")) {
                eat(".");
                patterns.add(AnnotationTypePattern.ELLIPSIS);
            } else if (maybeEat("*")) {
                patterns.add(AnnotationTypePattern.ANY);
            } else {
                patterns.add(parseAnnotationNameOrVarTypePattern());
            }
        } while (maybeEat(","));
        eat(")");
        return new AnnotationPatternList(patterns);
    }

    public ThrowsPattern parseOptionalThrowsPattern() {
        IToken t = this.tokenSource.peek();
        if (t.isIdentifier() && t.getString().equals("throws")) {
            this.tokenSource.next();
            List<TypePattern> required = new ArrayList<>();
            List<TypePattern> forbidden = new ArrayList<>();
            do {
                boolean isForbidden = maybeEat("!");
                TypePattern p = parseTypePattern();
                if (isForbidden) {
                    forbidden.add(p);
                } else {
                    required.add(p);
                }
            } while (maybeEat(","));
            return new ThrowsPattern(new TypePatternList(required), new TypePatternList(forbidden));
        }
        return ThrowsPattern.ANY;
    }

    public SignaturePattern parseMethodOrConstructorSignaturePattern() {
        MemberKind kind;
        TypePattern declaringType;
        NamePattern name;
        int startPos = this.tokenSource.peek().getStart();
        AnnotationTypePattern annotationPattern = maybeParseAnnotationPattern();
        ModifiersPattern modifiers = parseModifiersPattern();
        TypePattern returnType = parseTypePattern(false, false);
        if (maybeEatNew(returnType)) {
            kind = Member.CONSTRUCTOR;
            if (returnType.toString().length() == 0) {
                declaringType = TypePattern.ANY;
            } else {
                declaringType = returnType;
            }
            returnType = TypePattern.ANY;
            name = NamePattern.ANY;
        } else {
            kind = Member.METHOD;
            IToken nameToken = this.tokenSource.peek();
            declaringType = parseTypePattern(false, false);
            if (maybeEat(".")) {
                nameToken = this.tokenSource.peek();
                name = parseNamePattern();
            } else {
                name = tryToExtractName(declaringType);
                if (declaringType.toString().equals("")) {
                    declaringType = TypePattern.ANY;
                }
            }
            if (name == null) {
                throw new ParserException("name pattern", this.tokenSource.peek());
            }
            String simpleName = name.maybeGetSimpleName();
            if (simpleName != null && simpleName.equals("new")) {
                throw new ParserException("method name (not constructor)", nameToken);
            }
        }
        TypePatternList parameterTypes = parseArgumentsPattern(true);
        ThrowsPattern throwsPattern = parseOptionalThrowsPattern();
        SignaturePattern ret = new SignaturePattern(kind, modifiers, returnType, declaringType, name, parameterTypes, throwsPattern, annotationPattern);
        int endPos = this.tokenSource.peek(-1).getEnd();
        ret.setLocation(this.sourceContext, startPos, endPos);
        return ret;
    }

    private boolean maybeEatNew(TypePattern returnType) {
        if (returnType instanceof WildTypePattern) {
            WildTypePattern p = (WildTypePattern) returnType;
            if (p.maybeExtractName("new")) {
                return true;
            }
        }
        int start = this.tokenSource.getIndex();
        if (maybeEat(".")) {
            String id = maybeEatIdentifier();
            if (id != null && id.equals("new")) {
                return true;
            }
            this.tokenSource.setIndex(start);
            return false;
        }
        return false;
    }

    public ISignaturePattern parseMaybeParenthesizedFieldSignaturePattern() {
        ISignaturePattern result;
        boolean negated = this.tokenSource.peek().getString().equals("!") && this.tokenSource.peek(1).getString().equals("(");
        if (negated) {
            eat("!");
        }
        if (maybeEat("(")) {
            result = parseCompoundFieldSignaturePattern();
            eat(")", "missing ')' - unbalanced parentheses around field signature pattern in declare @field");
            if (negated) {
                result = new NotSignaturePattern(result);
            }
        } else {
            result = parseFieldSignaturePattern();
        }
        return result;
    }

    public ISignaturePattern parseMaybeParenthesizedMethodOrConstructorSignaturePattern(boolean isMethod) {
        ISignaturePattern result;
        boolean negated = this.tokenSource.peek().getString().equals("!") && this.tokenSource.peek(1).getString().equals("(");
        if (negated) {
            eat("!");
        }
        if (maybeEat("(")) {
            result = parseCompoundMethodOrConstructorSignaturePattern(isMethod);
            eat(")", "missing ')' - unbalanced parentheses around method/ctor signature pattern in declare annotation");
            if (negated) {
                result = new NotSignaturePattern(result);
            }
        } else {
            SignaturePattern sp = parseMethodOrConstructorSignaturePattern();
            boolean isConstructorPattern = sp.getKind() == Member.CONSTRUCTOR;
            if (isMethod && isConstructorPattern) {
                throw new ParserException("method signature pattern", this.tokenSource.peek(-1));
            }
            if (!isMethod && !isConstructorPattern) {
                throw new ParserException("constructor signature pattern", this.tokenSource.peek(-1));
            }
            result = sp;
        }
        return result;
    }

    public SignaturePattern parseFieldSignaturePattern() {
        NamePattern name;
        int startPos = this.tokenSource.peek().getStart();
        AnnotationTypePattern annotationPattern = maybeParseAnnotationPattern();
        ModifiersPattern modifiers = parseModifiersPattern();
        TypePattern returnType = parseTypePattern();
        TypePattern declaringType = parseTypePattern();
        if (maybeEat(".")) {
            name = parseNamePattern();
        } else {
            name = tryToExtractName(declaringType);
            if (name == null) {
                throw new ParserException("name pattern", this.tokenSource.peek());
            }
            if (declaringType.toString().equals("")) {
                declaringType = TypePattern.ANY;
            }
        }
        SignaturePattern ret = new SignaturePattern(Member.FIELD, modifiers, returnType, declaringType, name, TypePatternList.ANY, ThrowsPattern.ANY, annotationPattern);
        int endPos = this.tokenSource.peek(-1).getEnd();
        ret.setLocation(this.sourceContext, startPos, endPos);
        return ret;
    }

    private NamePattern tryToExtractName(TypePattern nextType) {
        if (nextType == TypePattern.ANY) {
            return NamePattern.ANY;
        }
        if (nextType instanceof WildTypePattern) {
            WildTypePattern p = (WildTypePattern) nextType;
            return p.extractName();
        }
        return null;
    }

    public TypeVariablePatternList maybeParseTypeVariableList() {
        if (!maybeEat("<")) {
            return null;
        }
        List<TypeVariablePattern> typeVars = new ArrayList<>();
        TypeVariablePattern t = parseTypeVariable();
        typeVars.add(t);
        while (maybeEat(",")) {
            TypeVariablePattern nextT = parseTypeVariable();
            typeVars.add(nextT);
        }
        eat(">");
        TypeVariablePattern[] tvs = new TypeVariablePattern[typeVars.size()];
        typeVars.toArray(tvs);
        return new TypeVariablePatternList(tvs);
    }

    public String[] maybeParseSimpleTypeVariableList() {
        if (!maybeEat("<")) {
            return null;
        }
        List<String> typeVarNames = new ArrayList<>();
        do {
            typeVarNames.add(parseIdentifier());
        } while (maybeEat(","));
        eat(">", "',' or '>'");
        String[] tvs = new String[typeVarNames.size()];
        typeVarNames.toArray(tvs);
        return tvs;
    }

    public TypePatternList maybeParseTypeParameterList() {
        if (!maybeEat("<")) {
            return null;
        }
        List<TypePattern> typePats = new ArrayList<>();
        do {
            TypePattern tp = parseTypePattern(true, false);
            typePats.add(tp);
        } while (maybeEat(","));
        eat(">");
        TypePattern[] tps = new TypePattern[typePats.size()];
        typePats.toArray(tps);
        return new TypePatternList(tps);
    }

    public TypeVariablePattern parseTypeVariable() {
        TypePattern upperBound = null;
        TypePattern[] additionalInterfaceBounds = null;
        TypePattern lowerBound = null;
        String typeVariableName = parseIdentifier();
        if (maybeEatIdentifier("extends")) {
            upperBound = parseTypePattern();
            additionalInterfaceBounds = maybeParseAdditionalInterfaceBounds();
        } else if (maybeEatIdentifier("super")) {
            lowerBound = parseTypePattern();
        }
        return new TypeVariablePattern(typeVariableName, upperBound, additionalInterfaceBounds, lowerBound);
    }

    private TypePattern[] maybeParseAdditionalInterfaceBounds() {
        List<TypePattern> boundsList = new ArrayList<>();
        while (maybeEat("&")) {
            TypePattern tp = parseTypePattern();
            boundsList.add(tp);
        }
        if (boundsList.size() == 0) {
            return null;
        }
        TypePattern[] ret = new TypePattern[boundsList.size()];
        boundsList.toArray(ret);
        return ret;
    }

    public String parsePossibleStringSequence(boolean shouldEnd) {
        StringBuffer result = new StringBuffer();
        IToken token = this.tokenSource.next();
        if (token.getLiteralKind() == null) {
            throw new ParserException(StringProperty.TYPE, token);
        }
        while (token.getLiteralKind().equals(StringProperty.TYPE)) {
            result.append(token.getString());
            boolean plus = maybeEat("+");
            if (!plus) {
                break;
            }
            token = this.tokenSource.next();
            if (token.getLiteralKind() == null) {
                throw new ParserException(StringProperty.TYPE, token);
            }
        }
        eatIdentifier(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        IToken t = this.tokenSource.next();
        if (shouldEnd && t != IToken.EOF) {
            throw new ParserException("<string>;", token);
        }
        int currentIndex = this.tokenSource.getIndex();
        this.tokenSource.setIndex(currentIndex - 1);
        return result.toString();
    }

    public String parseStringLiteral() {
        IToken token = this.tokenSource.next();
        String literalKind = token.getLiteralKind();
        if (literalKind == StringProperty.TYPE) {
            return token.getString();
        }
        throw new ParserException(StringProperty.TYPE, token);
    }

    public String parseIdentifier() {
        IToken token = this.tokenSource.next();
        if (token.isIdentifier()) {
            return token.getString();
        }
        throw new ParserException(PdfConst.Identifier, token);
    }

    public void eatIdentifier(String expectedValue) {
        IToken next = this.tokenSource.next();
        if (!next.getString().equals(expectedValue)) {
            throw new ParserException(expectedValue, next);
        }
    }

    public boolean maybeEatIdentifier(String expectedValue) {
        IToken next = this.tokenSource.peek();
        if (next.getString().equals(expectedValue)) {
            this.tokenSource.next();
            return true;
        }
        return false;
    }

    public void eat(String expectedValue) {
        eat(expectedValue, expectedValue);
    }

    private void eat(String expectedValue, String expectedMessage) {
        IToken next = nextToken();
        if (next.getString() != expectedValue) {
            if (expectedValue.equals(">") && next.getString().startsWith(">")) {
                this.pendingRightArrows = BasicToken.makeLiteral(next.getString().substring(1).intern(), StringProperty.TYPE, next.getStart() + 1, next.getEnd());
                return;
            }
            throw new ParserException(expectedMessage, next);
        }
    }

    private IToken nextToken() {
        if (this.pendingRightArrows != null) {
            IToken ret = this.pendingRightArrows;
            this.pendingRightArrows = null;
            return ret;
        }
        return this.tokenSource.next();
    }

    public boolean maybeEatAdjacent(String token) {
        IToken next = this.tokenSource.peek();
        if (next.getString() == token && isAdjacent(this.tokenSource.peek(-1), next)) {
            this.tokenSource.next();
            return true;
        }
        return false;
    }

    public boolean maybeEat(String token) {
        IToken next = this.tokenSource.peek();
        if (next.getString() == token) {
            this.tokenSource.next();
            return true;
        }
        return false;
    }

    public String maybeEatIdentifier() {
        IToken next = this.tokenSource.peek();
        if (next.isIdentifier()) {
            this.tokenSource.next();
            return next.getString();
        }
        return null;
    }

    public boolean peek(String token) {
        IToken next = this.tokenSource.peek();
        return next.getString() == token;
    }

    public void checkEof() {
        IToken last = this.tokenSource.next();
        if (last != IToken.EOF) {
            throw new ParserException("unexpected pointcut element: " + last.toString(), last);
        }
    }

    public PatternParser(String data) {
        this(BasicTokenSource.makeTokenSource(data, null));
    }

    public PatternParser(String data, ISourceContext context) {
        this(BasicTokenSource.makeTokenSource(data, context));
    }
}
