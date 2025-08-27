package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FileUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.BoundedReferenceType;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.IHasPosition;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeFactory;
import org.aspectj.weaver.TypeVariable;
import org.aspectj.weaver.TypeVariableReference;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.UnresolvedTypeVariableReferenceType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.patterns.TypePattern;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/WildTypePattern.class */
public class WildTypePattern extends TypePattern {
    private static final String GENERIC_WILDCARD_CHARACTER = "?";
    private static final String GENERIC_WILDCARD_SIGNATURE_CHARACTER = "*";
    private NamePattern[] namePatterns;
    private boolean failedResolution;
    int ellipsisCount;
    String[] importedPrefixes;
    String[] knownMatches;
    int dim;
    public static boolean boundscheckingoff = false;
    TypePattern upperBound;
    TypePattern[] additionalInterfaceBounds;
    TypePattern lowerBound;
    private boolean isGeneric;
    private static final byte VERSION = 1;

    WildTypePattern(NamePattern[] namePatterns, boolean includeSubtypes, int dim, boolean isVarArgs, TypePatternList typeParams) {
        super(includeSubtypes, isVarArgs, typeParams);
        this.failedResolution = false;
        this.isGeneric = true;
        this.namePatterns = namePatterns;
        this.dim = dim;
        this.ellipsisCount = 0;
        for (NamePattern namePattern : namePatterns) {
            if (namePattern == NamePattern.ELLIPSIS) {
                this.ellipsisCount++;
            }
        }
        setLocation(namePatterns[0].getSourceContext(), namePatterns[0].getStart(), namePatterns[namePatterns.length - 1].getEnd());
    }

    public WildTypePattern(List<NamePattern> names, boolean includeSubtypes, int dim) {
        this((NamePattern[]) names.toArray(new NamePattern[names.size()]), includeSubtypes, dim, false, TypePatternList.EMPTY);
    }

    public WildTypePattern(List<NamePattern> names, boolean includeSubtypes, int dim, int endPos) {
        this(names, includeSubtypes, dim);
        this.end = endPos;
    }

    public WildTypePattern(List<NamePattern> names, boolean includeSubtypes, int dim, int endPos, boolean isVarArg) {
        this(names, includeSubtypes, dim);
        this.end = endPos;
        this.isVarArgs = isVarArg;
    }

    public WildTypePattern(List<NamePattern> names, boolean includeSubtypes, int dim, int endPos, boolean isVarArg, TypePatternList typeParams, TypePattern upperBound, TypePattern[] additionalInterfaceBounds, TypePattern lowerBound) {
        this((NamePattern[]) names.toArray(new NamePattern[names.size()]), includeSubtypes, dim, isVarArg, typeParams);
        this.end = endPos;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.additionalInterfaceBounds = additionalInterfaceBounds;
    }

    public WildTypePattern(List<NamePattern> names, boolean includeSubtypes, int dim, int endPos, boolean isVarArg, TypePatternList typeParams) {
        this((NamePattern[]) names.toArray(new NamePattern[names.size()]), includeSubtypes, dim, isVarArg, typeParams);
        this.end = endPos;
    }

    public NamePattern[] getNamePatterns() {
        return this.namePatterns;
    }

    public TypePattern getUpperBound() {
        return this.upperBound;
    }

    public TypePattern getLowerBound() {
        return this.lowerBound;
    }

    public TypePattern[] getAdditionalIntefaceBounds() {
        return this.additionalInterfaceBounds;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public void setIsVarArgs(boolean isVarArgs) {
        this.isVarArgs = isVarArgs;
        if (isVarArgs) {
            this.dim++;
        }
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean couldEverMatchSameTypesAs(TypePattern other) {
        if (super.couldEverMatchSameTypesAs(other)) {
            return true;
        }
        UnresolvedType otherType = other.getExactType();
        if (!ResolvedType.isMissing(otherType) && this.namePatterns.length > 0 && !this.namePatterns[0].matches(otherType.getName())) {
            return false;
        }
        if (other instanceof WildTypePattern) {
            WildTypePattern owtp = (WildTypePattern) other;
            String mySimpleName = this.namePatterns[0].maybeGetSimpleName();
            String yourSimpleName = owtp.namePatterns[0].maybeGetSimpleName();
            return mySimpleName == null || yourSimpleName == null || mySimpleName.startsWith(yourSimpleName) || yourSimpleName.startsWith(mySimpleName);
        }
        return true;
    }

    public static char[][] splitNames(String s, boolean convertDollar) {
        List<char[]> ret = new ArrayList<>();
        int i = 0;
        while (true) {
            int startIndex = i;
            int breakIndex = s.indexOf(46, startIndex);
            if (convertDollar && breakIndex == -1) {
                breakIndex = s.indexOf(36, startIndex);
            }
            if (breakIndex != -1) {
                char[] name = s.substring(startIndex, breakIndex).toCharArray();
                ret.add(name);
                i = breakIndex + 1;
            } else {
                ret.add(s.substring(startIndex).toCharArray());
                return (char[][]) ret.toArray((Object[]) new char[ret.size()]);
            }
        }
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type) {
        return matchesExactly(type, type);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type, ResolvedType annotatedType) {
        String targetTypeName = type.getName();
        this.annotationPattern.resolve(type.getWorld());
        return matchesExactlyByName(targetTypeName, type.isAnonymous(), type.isNested()) && matchesParameters(type, STATIC) && matchesBounds(type, STATIC) && this.annotationPattern.matches(annotatedType, type.temporaryAnnotationTypes).alwaysTrue();
    }

    private boolean matchesParameters(ResolvedType aType, TypePattern.MatchKind staticOrDynamic) {
        if (!this.isGeneric && this.typeParameters.size() > 0) {
            if (!aType.isParameterizedType()) {
                return false;
            }
            return this.typeParameters.matches(aType.getResolvedTypeParameters(), staticOrDynamic).alwaysTrue();
        }
        return true;
    }

    private boolean matchesBounds(ResolvedType aType, TypePattern.MatchKind staticOrDynamic) {
        if (!(aType instanceof BoundedReferenceType)) {
            return true;
        }
        BoundedReferenceType boundedRT = (BoundedReferenceType) aType;
        if (this.upperBound == null && boundedRT.getUpperBound() != null && !boundedRT.getUpperBound().getName().equals(UnresolvedType.OBJECT.getName())) {
            return false;
        }
        if (this.lowerBound == null && boundedRT.getLowerBound() != null) {
            return false;
        }
        if (this.upperBound != null) {
            if ((aType.isGenericWildcard() && boundedRT.isSuper()) || boundedRT.getUpperBound() == null) {
                return false;
            }
            return this.upperBound.matches((ResolvedType) boundedRT.getUpperBound(), staticOrDynamic).alwaysTrue();
        }
        if (this.lowerBound != null) {
            if (!boundedRT.isGenericWildcard() || !boundedRT.isSuper()) {
                return false;
            }
            return this.lowerBound.matches((ResolvedType) boundedRT.getLowerBound(), staticOrDynamic).alwaysTrue();
        }
        return true;
    }

    public int getDimensions() {
        return this.dim;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean isArray() {
        return this.dim > 1;
    }

    private boolean matchesExactlyByName(String targetTypeName, boolean isAnonymous, boolean isNested) {
        if (targetTypeName.indexOf(60) != -1) {
            targetTypeName = targetTypeName.substring(0, targetTypeName.indexOf(60));
        }
        if (targetTypeName.startsWith("?")) {
            targetTypeName = "?";
        }
        if (this.knownMatches == null && this.importedPrefixes == null) {
            return innerMatchesExactly(targetTypeName, isAnonymous, isNested);
        }
        if (isNamePatternStar()) {
            int numDimensionsInTargetType = 0;
            if (this.dim > 0) {
                while (true) {
                    int index = targetTypeName.indexOf(91);
                    if (index == -1) {
                        break;
                    }
                    numDimensionsInTargetType++;
                    targetTypeName = targetTypeName.substring(index + 1);
                }
                if (numDimensionsInTargetType == this.dim) {
                    return true;
                }
                return false;
            }
        }
        if (this.namePatterns.length == 1) {
            if (isAnonymous) {
                return false;
            }
            int len = this.knownMatches.length;
            for (int i = 0; i < len; i++) {
                if (this.knownMatches[i].equals(targetTypeName)) {
                    return true;
                }
            }
        } else {
            int len2 = this.knownMatches.length;
            for (int i2 = 0; i2 < len2; i2++) {
                String knownMatch = this.knownMatches[i2];
                if (targetTypeName.startsWith(knownMatch) && targetTypeName.length() > knownMatch.length() && targetTypeName.charAt(knownMatch.length()) == '$') {
                    int pos = lastIndexOfDotOrDollar(knownMatch);
                    if (innerMatchesExactly(targetTypeName.substring(pos + 1), isAnonymous, isNested)) {
                        return true;
                    }
                }
            }
        }
        int len3 = this.importedPrefixes.length;
        for (int i3 = 0; i3 < len3; i3++) {
            String prefix = this.importedPrefixes[i3];
            if (targetTypeName.startsWith(prefix) && innerMatchesExactly(targetTypeName.substring(prefix.length()), isAnonymous, isNested)) {
                return true;
            }
        }
        return innerMatchesExactly(targetTypeName, isAnonymous, isNested);
    }

    private int lastIndexOfDotOrDollar(String string) {
        for (int pos = string.length() - 1; pos > -1; pos--) {
            char ch2 = string.charAt(pos);
            if (ch2 == '.' || ch2 == '$') {
                return pos;
            }
        }
        return -1;
    }

    private boolean innerMatchesExactly(String s, boolean isAnonymous, boolean convertDollar) {
        int startIndex;
        List<char[]> ret = new ArrayList<>();
        int i = 0;
        while (true) {
            startIndex = i;
            int breakIndex = s.indexOf(46, startIndex);
            if (convertDollar && breakIndex == -1) {
                breakIndex = s.indexOf(36, startIndex);
            }
            if (breakIndex == -1) {
                break;
            }
            char[] name = s.substring(startIndex, breakIndex).toCharArray();
            ret.add(name);
            i = breakIndex + 1;
        }
        ret.add(s.substring(startIndex).toCharArray());
        int namesLength = ret.size();
        int patternsLength = this.namePatterns.length;
        int namesIndex = 0;
        int patternsIndex = 0;
        if (!this.namePatterns[patternsLength - 1].isAny() && isAnonymous) {
            return false;
        }
        if (this.ellipsisCount == 0) {
            if (namesLength != patternsLength) {
                return false;
            }
            while (patternsIndex < patternsLength) {
                int i2 = patternsIndex;
                patternsIndex++;
                int i3 = namesIndex;
                namesIndex++;
                if (!this.namePatterns[i2].matches(ret.get(i3))) {
                    return false;
                }
            }
            return true;
        }
        if (this.ellipsisCount == 1) {
            if (namesLength < patternsLength - 1) {
                return false;
            }
            while (patternsIndex < patternsLength) {
                int i4 = patternsIndex;
                patternsIndex++;
                NamePattern p = this.namePatterns[i4];
                if (p == NamePattern.ELLIPSIS) {
                    namesIndex = namesLength - (patternsLength - patternsIndex);
                } else {
                    int i5 = namesIndex;
                    namesIndex++;
                    if (!p.matches(ret.get(i5))) {
                        return false;
                    }
                }
            }
            return true;
        }
        boolean b = outOfStar(this.namePatterns, (char[][]) ret.toArray((Object[]) new char[ret.size()]), 0, 0, patternsLength - this.ellipsisCount, namesLength, this.ellipsisCount);
        return b;
    }

    private static boolean outOfStar(NamePattern[] pattern, char[][] target, int pi, int ti, int pLeft, int tLeft, int starsLeft) {
        if (pLeft > tLeft) {
            return false;
        }
        while (tLeft != 0) {
            if (pLeft == 0) {
                return starsLeft > 0;
            }
            if (pattern[pi] == NamePattern.ELLIPSIS) {
                return inStar(pattern, target, pi + 1, ti, pLeft, tLeft, starsLeft - 1);
            }
            if (!pattern[pi].matches(target[ti])) {
                return false;
            }
            pi++;
            ti++;
            pLeft--;
            tLeft--;
        }
        return true;
    }

    private static boolean inStar(NamePattern[] pattern, char[][] target, int pi, int ti, int pLeft, int tLeft, int starsLeft) {
        NamePattern patternChar;
        NamePattern namePattern = pattern[pi];
        while (true) {
            patternChar = namePattern;
            if (patternChar != NamePattern.ELLIPSIS) {
                break;
            }
            starsLeft--;
            pi++;
            namePattern = pattern[pi];
        }
        while (pLeft <= tLeft) {
            if (patternChar.matches(target[ti]) && outOfStar(pattern, target, pi + 1, ti + 1, pLeft - 1, tLeft - 1, starsLeft)) {
                return true;
            }
            ti++;
            tLeft--;
        }
        return false;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public FuzzyBoolean matchesInstanceof(ResolvedType type) throws AbortException {
        if (maybeGetSimpleName() != null) {
            return FuzzyBoolean.NO;
        }
        type.getWorld().getMessageHandler().handleMessage(new Message("can't do instanceof matching on patterns with wildcards", IMessage.ERROR, (Throwable) null, getSourceLocation()));
        return FuzzyBoolean.NO;
    }

    public NamePattern extractName() {
        if (isIncludeSubtypes() || isVarArgs() || isArray() || this.typeParameters.size() > 0) {
            return null;
        }
        int len = this.namePatterns.length;
        if (len == 1 && !this.annotationPattern.isAny()) {
            return null;
        }
        NamePattern ret = this.namePatterns[len - 1];
        NamePattern[] newNames = new NamePattern[len - 1];
        System.arraycopy(this.namePatterns, 0, newNames, 0, len - 1);
        this.namePatterns = newNames;
        return ret;
    }

    public boolean maybeExtractName(String string) {
        int len = this.namePatterns.length;
        NamePattern ret = this.namePatterns[len - 1];
        String simple = ret.maybeGetSimpleName();
        if (simple != null && simple.equals(string)) {
            extractName();
            return true;
        }
        return false;
    }

    public String maybeGetSimpleName() {
        if (this.namePatterns.length == 1) {
            return this.namePatterns[0].maybeGetSimpleName();
        }
        return null;
    }

    public String maybeGetCleanName() {
        if (this.namePatterns.length == 0) {
            throw new RuntimeException("bad name: " + this.namePatterns);
        }
        StringBuffer buf = new StringBuffer();
        int len = this.namePatterns.length;
        for (int i = 0; i < len; i++) {
            NamePattern p = this.namePatterns[i];
            String simpleName = p.maybeGetSimpleName();
            if (simpleName == null) {
                return null;
            }
            if (i > 0) {
                buf.append(".");
            }
            buf.append(simpleName);
        }
        return buf.toString();
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        String simpleName;
        NamePattern[] newNamePatterns = new NamePattern[this.namePatterns.length];
        for (int i = 0; i < this.namePatterns.length; i++) {
            newNamePatterns[i] = this.namePatterns[i];
        }
        if (newNamePatterns.length == 1 && (simpleName = newNamePatterns[0].maybeGetSimpleName()) != null && typeVariableMap.containsKey(simpleName)) {
            String newName = ((ReferenceType) typeVariableMap.get(simpleName)).getName().replace('$', '.');
            StringTokenizer strTok = new StringTokenizer(newName, ".");
            newNamePatterns = new NamePattern[strTok.countTokens()];
            int index = 0;
            while (strTok.hasMoreTokens()) {
                int i2 = index;
                index++;
                newNamePatterns[i2] = new NamePattern(strTok.nextToken());
            }
        }
        WildTypePattern ret = new WildTypePattern(newNamePatterns, this.includeSubtypes, this.dim, this.isVarArgs, this.typeParameters.parameterizeWith(typeVariableMap, w));
        ret.annotationPattern = this.annotationPattern.parameterizeWith(typeVariableMap, w);
        if (this.additionalInterfaceBounds == null) {
            ret.additionalInterfaceBounds = null;
        } else {
            ret.additionalInterfaceBounds = new TypePattern[this.additionalInterfaceBounds.length];
            for (int i3 = 0; i3 < this.additionalInterfaceBounds.length; i3++) {
                ret.additionalInterfaceBounds[i3] = this.additionalInterfaceBounds[i3].parameterizeWith(typeVariableMap, w);
            }
        }
        ret.upperBound = this.upperBound != null ? this.upperBound.parameterizeWith(typeVariableMap, w) : null;
        ret.lowerBound = this.lowerBound != null ? this.lowerBound.parameterizeWith(typeVariableMap, w) : null;
        ret.isGeneric = this.isGeneric;
        ret.knownMatches = this.knownMatches;
        ret.importedPrefixes = this.importedPrefixes;
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) throws AbortException {
        TypePattern anyPattern;
        if (isNamePatternStar() && (anyPattern = maybeResolveToAnyPattern(scope, bindings, allowBinding, requireExactType)) != null) {
            if (requireExactType) {
                scope.getWorld().getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.WILDCARD_NOT_ALLOWED), getSourceLocation()));
                return NO;
            }
            return anyPattern;
        }
        TypePattern bindingTypePattern = maybeResolveToBindingTypePattern(scope, bindings, allowBinding, requireExactType);
        if (bindingTypePattern != null) {
            return bindingTypePattern;
        }
        this.annotationPattern = this.annotationPattern.resolveBindings(scope, bindings, allowBinding);
        if (this.typeParameters != null && this.typeParameters.size() > 0) {
            this.typeParameters.resolveBindings(scope, bindings, allowBinding, requireExactType);
            this.isGeneric = false;
        }
        if (this.upperBound != null) {
            this.upperBound = this.upperBound.resolveBindings(scope, bindings, allowBinding, requireExactType);
        }
        if (this.lowerBound != null) {
            this.lowerBound = this.lowerBound.resolveBindings(scope, bindings, allowBinding, requireExactType);
        }
        String fullyQualifiedName = maybeGetCleanName();
        if (fullyQualifiedName != null) {
            return resolveBindingsFromFullyQualifiedTypeName(fullyQualifiedName, scope, bindings, allowBinding, requireExactType);
        }
        if (requireExactType) {
            scope.getWorld().getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.WILDCARD_NOT_ALLOWED), getSourceLocation()));
            return NO;
        }
        this.importedPrefixes = scope.getImportedPrefixes();
        this.knownMatches = preMatch(scope.getImportedNames());
        return this;
    }

    private TypePattern maybeResolveToAnyPattern(IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) {
        if (this.annotationPattern == AnnotationTypePattern.ANY) {
            if (this.dim != 0 || this.isVarArgs || this.upperBound != null || this.lowerBound != null) {
                return null;
            }
            if (this.additionalInterfaceBounds == null || this.additionalInterfaceBounds.length == 0) {
                return TypePattern.ANY;
            }
            return null;
        }
        if (!this.isVarArgs) {
            this.annotationPattern = this.annotationPattern.resolveBindings(scope, bindings, allowBinding);
            AnyWithAnnotationTypePattern ret = new AnyWithAnnotationTypePattern(this.annotationPattern);
            ret.setLocation(this.sourceContext, this.start, this.end);
            return ret;
        }
        return null;
    }

    private TypePattern maybeResolveToBindingTypePattern(IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) {
        FormalBinding formalBinding;
        String simpleName = maybeGetSimpleName();
        if (simpleName != null && (formalBinding = scope.lookupFormal(simpleName)) != null) {
            if (bindings == null) {
                scope.message(IMessage.ERROR, this, "negation doesn't allow binding");
                return this;
            }
            if (!allowBinding) {
                scope.message(IMessage.ERROR, this, "name binding only allowed in target, this, and args pcds");
                return this;
            }
            BindingTypePattern binding = new BindingTypePattern(formalBinding, this.isVarArgs);
            binding.copyLocationFrom(this);
            bindings.register(binding, scope);
            return binding;
        }
        return null;
    }

    private TypePattern resolveBindingsFromFullyQualifiedTypeName(String fullyQualifiedName, IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) {
        UnresolvedType type;
        ResolvedType resolvedTypeInTheWorld = lookupTypeInWorldIncludingPrefixes(scope.getWorld(), fullyQualifiedName, scope.getImportedPrefixes());
        if (resolvedTypeInTheWorld.isGenericWildcard()) {
            type = resolvedTypeInTheWorld;
        } else {
            type = lookupTypeInScope(scope, fullyQualifiedName, this);
        }
        if ((type instanceof ResolvedType) && ((ResolvedType) type).isMissing()) {
            return resolveBindingsForMissingType(resolvedTypeInTheWorld, fullyQualifiedName, scope, bindings, allowBinding, requireExactType);
        }
        return resolveBindingsForExactType(scope, type, fullyQualifiedName, requireExactType);
    }

    private UnresolvedType lookupTypeInScope(IScope scope, String typeName, IHasPosition location) {
        UnresolvedType type;
        int lastDot;
        while (true) {
            type = scope.lookupType(typeName, location);
            if (!ResolvedType.isMissing(type) || (lastDot = typeName.lastIndexOf(46)) == -1) {
                break;
            }
            typeName = typeName.substring(0, lastDot) + '$' + typeName.substring(lastDot + 1);
        }
        return type;
    }

    private ResolvedType lookupTypeInWorldIncludingPrefixes(World world, String typeName, String[] prefixes) {
        ResolvedType ret = lookupTypeInWorld(world, typeName);
        if (!ret.isMissing()) {
            return ret;
        }
        ResolvedType retWithPrefix = ret;
        for (int counter = 0; retWithPrefix.isMissing() && counter < prefixes.length; counter++) {
            retWithPrefix = lookupTypeInWorld(world, prefixes[counter] + typeName);
        }
        if (!retWithPrefix.isMissing()) {
            return retWithPrefix;
        }
        return ret;
    }

    private ResolvedType lookupTypeInWorld(World world, String typeName) {
        ResolvedType ret;
        int lastDot;
        UnresolvedType ut = UnresolvedType.forName(typeName);
        ResolvedType resolvedTypeResolve = world.resolve(ut, true);
        while (true) {
            ret = resolvedTypeResolve;
            if (!ret.isMissing() || (lastDot = typeName.lastIndexOf(46)) == -1) {
                break;
            }
            typeName = typeName.substring(0, lastDot) + '$' + typeName.substring(lastDot + 1);
            resolvedTypeResolve = world.resolve(UnresolvedType.forName(typeName), true);
        }
        return ret;
    }

    private TypePattern resolveBindingsForExactType(IScope scope, UnresolvedType aType, String fullyQualifiedName, boolean requireExactType) {
        TypePattern ret;
        if (aType.isTypeVariableReference()) {
            ret = resolveBindingsForTypeVariable(scope, (UnresolvedTypeVariableReferenceType) aType);
        } else if (this.typeParameters.size() > 0) {
            ret = resolveParameterizedType(scope, aType, requireExactType);
        } else if (this.upperBound != null || this.lowerBound != null) {
            ret = resolveGenericWildcard(scope, aType);
        } else {
            if (this.dim != 0) {
                aType = UnresolvedType.makeArray(aType, this.dim);
            }
            ret = new ExactTypePattern(aType, this.includeSubtypes, this.isVarArgs);
        }
        ret.setAnnotationTypePattern(this.annotationPattern);
        ret.copyLocationFrom(this);
        return ret;
    }

    private TypePattern resolveGenericWildcard(IScope scope, UnresolvedType aType) {
        if (!aType.getSignature().equals("*")) {
            throw new IllegalStateException("Can only have bounds for a generic wildcard");
        }
        boolean canBeExact = true;
        if (this.upperBound != null && ResolvedType.isMissing(this.upperBound.getExactType())) {
            canBeExact = false;
        }
        if (this.lowerBound != null && ResolvedType.isMissing(this.lowerBound.getExactType())) {
            canBeExact = false;
        }
        if (canBeExact) {
            ResolvedType type = null;
            if (this.upperBound != null) {
                if (this.upperBound.isIncludeSubtypes()) {
                    canBeExact = false;
                } else {
                    ReferenceType upper = (ReferenceType) this.upperBound.getExactType().resolve(scope.getWorld());
                    type = new BoundedReferenceType(upper, true, scope.getWorld());
                }
            } else if (this.lowerBound.isIncludeSubtypes()) {
                canBeExact = false;
            } else {
                ReferenceType lower = (ReferenceType) this.lowerBound.getExactType().resolve(scope.getWorld());
                type = new BoundedReferenceType(lower, false, scope.getWorld());
            }
            if (canBeExact) {
                return new ExactTypePattern(type, this.includeSubtypes, this.isVarArgs);
            }
        }
        this.importedPrefixes = scope.getImportedPrefixes();
        this.knownMatches = preMatch(scope.getImportedNames());
        return this;
    }

    private TypePattern resolveParameterizedType(IScope scope, UnresolvedType aType, boolean requireExactType) {
        ResolvedType rt = aType.resolve(scope.getWorld());
        if (!verifyTypeParameters(rt, scope, requireExactType)) {
            return TypePattern.NO;
        }
        if (this.typeParameters.areAllExactWithNoSubtypesAllowed()) {
            TypePattern[] typePats = this.typeParameters.getTypePatterns();
            UnresolvedType[] typeParameterTypes = new UnresolvedType[typePats.length];
            for (int i = 0; i < typeParameterTypes.length; i++) {
                typeParameterTypes[i] = ((ExactTypePattern) typePats[i]).getExactType();
            }
            if (rt.isParameterizedType()) {
                rt = rt.getGenericType();
            }
            ResolvedType type = TypeFactory.createParameterizedType(rt, typeParameterTypes, scope.getWorld());
            if (this.isGeneric) {
                type = type.getGenericType();
            }
            if (this.dim != 0) {
                type = ResolvedType.makeArray(type, this.dim);
            }
            return new ExactTypePattern(type, this.includeSubtypes, this.isVarArgs);
        }
        this.importedPrefixes = scope.getImportedPrefixes();
        this.knownMatches = preMatch(scope.getImportedNames());
        return this;
    }

    private TypePattern resolveBindingsForMissingType(ResolvedType typeFoundInWholeWorldSearch, String nameWeLookedFor, IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) throws AbortException {
        if (requireExactType) {
            if (!allowBinding) {
                scope.getWorld().getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.CANT_BIND_TYPE, nameWeLookedFor), getSourceLocation()));
            } else if (scope.getWorld().getLint().invalidAbsoluteTypeName.isEnabled()) {
                scope.getWorld().getLint().invalidAbsoluteTypeName.signal(nameWeLookedFor, getSourceLocation());
            }
            return NO;
        }
        if (scope.getWorld().getLint().invalidAbsoluteTypeName.isEnabled() && typeFoundInWholeWorldSearch.isMissing()) {
            scope.getWorld().getLint().invalidAbsoluteTypeName.signal(nameWeLookedFor, getSourceLocation());
            this.failedResolution = true;
        }
        this.importedPrefixes = scope.getImportedPrefixes();
        this.knownMatches = preMatch(scope.getImportedNames());
        return this;
    }

    private TypePattern resolveBindingsForTypeVariable(IScope scope, UnresolvedTypeVariableReferenceType tvrType) {
        Bindings emptyBindings = new Bindings(0);
        if (this.upperBound != null) {
            this.upperBound = this.upperBound.resolveBindings(scope, emptyBindings, false, false);
        }
        if (this.lowerBound != null) {
            this.lowerBound = this.lowerBound.resolveBindings(scope, emptyBindings, false, false);
        }
        if (this.additionalInterfaceBounds != null) {
            TypePattern[] resolvedIfBounds = new TypePattern[this.additionalInterfaceBounds.length];
            for (int i = 0; i < resolvedIfBounds.length; i++) {
                resolvedIfBounds[i] = this.additionalInterfaceBounds[i].resolveBindings(scope, emptyBindings, false, false);
            }
            this.additionalInterfaceBounds = resolvedIfBounds;
        }
        if (this.upperBound == null && this.lowerBound == null && this.additionalInterfaceBounds == null) {
            ResolvedType rType = tvrType.resolve(scope.getWorld());
            if (this.dim != 0) {
                rType = ResolvedType.makeArray(rType, this.dim);
            }
            return new ExactTypePattern(rType, this.includeSubtypes, this.isVarArgs);
        }
        boolean canCreateExactTypePattern = true;
        if (this.upperBound != null && ResolvedType.isMissing(this.upperBound.getExactType())) {
            canCreateExactTypePattern = false;
        }
        if (this.lowerBound != null && ResolvedType.isMissing(this.lowerBound.getExactType())) {
            canCreateExactTypePattern = false;
        }
        if (this.additionalInterfaceBounds != null) {
            for (int i2 = 0; i2 < this.additionalInterfaceBounds.length; i2++) {
                if (ResolvedType.isMissing(this.additionalInterfaceBounds[i2].getExactType())) {
                    canCreateExactTypePattern = false;
                }
            }
        }
        if (canCreateExactTypePattern) {
            TypeVariable tv = tvrType.getTypeVariable();
            if (this.upperBound != null) {
                tv.setSuperclass(this.upperBound.getExactType());
            }
            if (this.additionalInterfaceBounds != null) {
                UnresolvedType[] ifBounds = new UnresolvedType[this.additionalInterfaceBounds.length];
                for (int i3 = 0; i3 < ifBounds.length; i3++) {
                    ifBounds[i3] = this.additionalInterfaceBounds[i3].getExactType();
                }
                tv.setAdditionalInterfaceBounds(ifBounds);
            }
            ResolvedType rType2 = tvrType.resolve(scope.getWorld());
            if (this.dim != 0) {
                rType2 = ResolvedType.makeArray(rType2, this.dim);
            }
            return new ExactTypePattern(rType2, this.includeSubtypes, this.isVarArgs);
        }
        return this;
    }

    private boolean verifyTypeParameters(ResolvedType baseType, IScope scope, boolean requireExactType) {
        ResolvedType genericType = baseType.getGenericType();
        if (genericType == null) {
            scope.message(MessageUtil.warn(WeaverMessages.format(WeaverMessages.NOT_A_GENERIC_TYPE, baseType.getName()), getSourceLocation()));
            return false;
        }
        int minRequiredTypeParameters = this.typeParameters.size();
        boolean foundEllipsis = false;
        TypePattern[] typeParamPatterns = this.typeParameters.getTypePatterns();
        for (int i = 0; i < typeParamPatterns.length; i++) {
            if (typeParamPatterns[i] instanceof WildTypePattern) {
                WildTypePattern wtp = (WildTypePattern) typeParamPatterns[i];
                if (wtp.ellipsisCount > 0) {
                    foundEllipsis = true;
                    minRequiredTypeParameters--;
                }
            }
        }
        TypeVariable[] tvs = genericType.getTypeVariables();
        if (tvs.length < minRequiredTypeParameters || (!foundEllipsis && minRequiredTypeParameters != tvs.length)) {
            String msg = WeaverMessages.format(WeaverMessages.INCORRECT_NUMBER_OF_TYPE_ARGUMENTS, genericType.getName(), new Integer(tvs.length));
            if (requireExactType) {
                scope.message(MessageUtil.error(msg, getSourceLocation()));
                return false;
            }
            scope.message(MessageUtil.warn(msg, getSourceLocation()));
            return false;
        }
        if (!boundscheckingoff) {
            VerifyBoundsForTypePattern verification = new VerifyBoundsForTypePattern(scope, genericType, requireExactType, this.typeParameters, getSourceLocation());
            scope.getWorld().getCrosscuttingMembersSet().recordNecessaryCheck(verification);
            return true;
        }
        return true;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/WildTypePattern$VerifyBoundsForTypePattern.class */
    static class VerifyBoundsForTypePattern implements IVerificationRequired {
        private final IScope scope;
        private final ResolvedType genericType;
        private final boolean requireExactType;
        private TypePatternList typeParameters;
        private final ISourceLocation sLoc;

        public VerifyBoundsForTypePattern(IScope scope, ResolvedType genericType, boolean requireExactType, TypePatternList typeParameters, ISourceLocation sLoc) {
            this.typeParameters = TypePatternList.EMPTY;
            this.scope = scope;
            this.genericType = genericType;
            this.requireExactType = requireExactType;
            this.typeParameters = typeParameters;
            this.sLoc = sLoc;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.aspectj.weaver.patterns.IVerificationRequired
        public void verify() {
            TypeVariable[] tvs = this.genericType.getTypeVariables();
            TypePattern[] typeParamPatterns = this.typeParameters.getTypePatterns();
            if (this.typeParameters.areAllExactWithNoSubtypesAllowed()) {
                for (int i = 0; i < tvs.length; i++) {
                    UnresolvedType exactType = typeParamPatterns[i].getExactType();
                    boolean continueCheck = true;
                    if (exactType.isTypeVariableReference()) {
                        continueCheck = false;
                    }
                    if (continueCheck && !tvs[i].canBeBoundTo(exactType.resolve(this.scope.getWorld()))) {
                        String parameterName = exactType.getName();
                        if (exactType.isTypeVariableReference()) {
                            parameterName = ((TypeVariableReference) exactType).getTypeVariable().getDisplayName();
                        }
                        String msg = WeaverMessages.format(WeaverMessages.VIOLATES_TYPE_VARIABLE_BOUNDS, parameterName, new Integer(i + 1), tvs[i].getDisplayName(), this.genericType.getName());
                        if (this.requireExactType) {
                            this.scope.message(MessageUtil.error(msg, this.sLoc));
                        } else {
                            this.scope.message(MessageUtil.warn(msg, this.sLoc));
                        }
                    }
                }
            }
        }
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean isStar() {
        boolean annPatternStar = this.annotationPattern == AnnotationTypePattern.ANY;
        return isNamePatternStar() && annPatternStar && this.dim == 0;
    }

    private boolean isNamePatternStar() {
        return this.namePatterns.length == 1 && this.namePatterns[0].isAny();
    }

    private String[] preMatch(String[] possibleMatches) {
        List<String> ret = new ArrayList<>();
        int len = possibleMatches.length;
        for (int i = 0; i < len; i++) {
            char[][] names = splitNames(possibleMatches[i], true);
            if (this.namePatterns[0].matches(names[names.length - 1])) {
                ret.add(possibleMatches[i]);
            } else if (possibleMatches[i].indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) != -1) {
                char[][] names2 = splitNames(possibleMatches[i], false);
                if (this.namePatterns[0].matches(names2[names2.length - 1])) {
                    ret.add(possibleMatches[i]);
                }
            }
        }
        return (String[]) ret.toArray(new String[ret.size()]);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (this.annotationPattern != AnnotationTypePattern.ANY) {
            buf.append('(');
            buf.append(this.annotationPattern.toString());
            buf.append(' ');
        }
        int len = this.namePatterns.length;
        for (int i = 0; i < len; i++) {
            NamePattern name = this.namePatterns[i];
            if (name == null) {
                buf.append(".");
            } else {
                if (i > 0) {
                    buf.append(".");
                }
                buf.append(name.toString());
            }
        }
        if (this.upperBound != null) {
            buf.append(" extends ");
            buf.append(this.upperBound.toString());
        }
        if (this.lowerBound != null) {
            buf.append(" super ");
            buf.append(this.lowerBound.toString());
        }
        if (this.typeParameters != null && this.typeParameters.size() != 0) {
            buf.append("<");
            buf.append(this.typeParameters.toString());
            buf.append(">");
        }
        if (this.includeSubtypes) {
            buf.append('+');
        }
        if (this.isVarArgs) {
            buf.append("...");
        }
        if (this.annotationPattern != AnnotationTypePattern.ANY) {
            buf.append(')');
        }
        return buf.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof WildTypePattern)) {
            return false;
        }
        WildTypePattern o = (WildTypePattern) other;
        int len = o.namePatterns.length;
        if (len != this.namePatterns.length || this.includeSubtypes != o.includeSubtypes || this.dim != o.dim || this.isVarArgs != o.isVarArgs) {
            return false;
        }
        if (this.upperBound != null) {
            if (o.upperBound == null || !this.upperBound.equals(o.upperBound)) {
                return false;
            }
        } else if (o.upperBound != null) {
            return false;
        }
        if (this.lowerBound != null) {
            if (o.lowerBound == null || !this.lowerBound.equals(o.lowerBound)) {
                return false;
            }
        } else if (o.lowerBound != null) {
            return false;
        }
        if (!this.typeParameters.equals(o.typeParameters)) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (!o.namePatterns[i].equals(this.namePatterns[i])) {
                return false;
            }
        }
        return o.annotationPattern.equals(this.annotationPattern);
    }

    public int hashCode() {
        int result = 17;
        int len = this.namePatterns.length;
        for (int i = 0; i < len; i++) {
            result = (37 * result) + this.namePatterns[i].hashCode();
        }
        int result2 = (37 * result) + this.annotationPattern.hashCode();
        if (this.upperBound != null) {
            result2 = (37 * result2) + this.upperBound.hashCode();
        }
        if (this.lowerBound != null) {
            result2 = (37 * result2) + this.lowerBound.hashCode();
        }
        return result2;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(1);
        s.writeByte(1);
        s.writeShort(this.namePatterns.length);
        for (int i = 0; i < this.namePatterns.length; i++) {
            this.namePatterns[i].write(s);
        }
        s.writeBoolean(this.includeSubtypes);
        s.writeInt(this.dim);
        s.writeBoolean(this.isVarArgs);
        this.typeParameters.write(s);
        FileUtil.writeStringArray(this.knownMatches, s);
        FileUtil.writeStringArray(this.importedPrefixes, s);
        writeLocation(s);
        this.annotationPattern.write(s);
        s.writeBoolean(this.isGeneric);
        s.writeBoolean(this.upperBound != null);
        if (this.upperBound != null) {
            this.upperBound.write(s);
        }
        s.writeBoolean(this.lowerBound != null);
        if (this.lowerBound != null) {
            this.lowerBound.write(s);
        }
        s.writeInt(this.additionalInterfaceBounds == null ? 0 : this.additionalInterfaceBounds.length);
        if (this.additionalInterfaceBounds != null) {
            for (int i2 = 0; i2 < this.additionalInterfaceBounds.length; i2++) {
                this.additionalInterfaceBounds[i2].write(s);
            }
        }
    }

    public static TypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        if (s.getMajorVersion() >= 2) {
            return readTypePattern150(s, context);
        }
        return readTypePatternOldStyle(s, context);
    }

    public static TypePattern readTypePattern150(VersionedDataInputStream s, ISourceContext context) throws IOException {
        byte version = s.readByte();
        if (version > 1) {
            throw new BCException("WildTypePattern was written by a more recent version of AspectJ, cannot read");
        }
        int len = s.readShort();
        NamePattern[] namePatterns = new NamePattern[len];
        for (int i = 0; i < len; i++) {
            namePatterns[i] = NamePattern.read(s);
        }
        boolean includeSubtypes = s.readBoolean();
        int dim = s.readInt();
        boolean varArg = s.readBoolean();
        TypePatternList typeParams = TypePatternList.read(s, context);
        WildTypePattern ret = new WildTypePattern(namePatterns, includeSubtypes, dim, varArg, typeParams);
        ret.knownMatches = FileUtil.readStringArray(s);
        ret.importedPrefixes = FileUtil.readStringArray(s);
        ret.readLocation(context, s);
        ret.setAnnotationTypePattern(AnnotationTypePattern.read(s, context));
        ret.isGeneric = s.readBoolean();
        if (s.readBoolean()) {
            ret.upperBound = TypePattern.read(s, context);
        }
        if (s.readBoolean()) {
            ret.lowerBound = TypePattern.read(s, context);
        }
        int numIfBounds = s.readInt();
        if (numIfBounds > 0) {
            ret.additionalInterfaceBounds = new TypePattern[numIfBounds];
            for (int i2 = 0; i2 < numIfBounds; i2++) {
                ret.additionalInterfaceBounds[i2] = TypePattern.read(s, context);
            }
        }
        return ret;
    }

    public static TypePattern readTypePatternOldStyle(VersionedDataInputStream s, ISourceContext context) throws IOException {
        int len = s.readShort();
        NamePattern[] namePatterns = new NamePattern[len];
        for (int i = 0; i < len; i++) {
            namePatterns[i] = NamePattern.read(s);
        }
        boolean includeSubtypes = s.readBoolean();
        int dim = s.readInt();
        WildTypePattern ret = new WildTypePattern(namePatterns, includeSubtypes, dim, false, (TypePatternList) null);
        ret.knownMatches = FileUtil.readStringArray(s);
        ret.importedPrefixes = FileUtil.readStringArray(s);
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean hasFailedResolution() {
        return this.failedResolution;
    }
}
