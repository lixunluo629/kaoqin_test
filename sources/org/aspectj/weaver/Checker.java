package org.aspectj.weaver;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.patterns.DeclareErrorOrWarning;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Checker.class */
public class Checker extends ShadowMunger {
    private boolean isError;
    private String message;
    private volatile int hashCode;

    private Checker() {
        this.hashCode = -1;
    }

    public Checker(DeclareErrorOrWarning deow) {
        super(deow.getPointcut(), deow.getStart(), deow.getEnd(), deow.getSourceContext(), 2);
        this.hashCode = -1;
        this.message = deow.getMessage();
        this.isError = deow.isError();
    }

    private Checker(Pointcut pointcut, int start, int end, ISourceContext context, String message, boolean isError) {
        super(pointcut, start, end, context, 2);
        this.hashCode = -1;
        this.message = message;
        this.isError = isError;
    }

    public boolean isError() {
        return this.isError;
    }

    public String getMessage(Shadow shadow) {
        return format(this.message, shadow);
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public void specializeOn(Shadow shadow) {
        throw new IllegalStateException("Cannot call specializeOn(...) for a Checker");
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public boolean implementOn(Shadow shadow) {
        throw new IllegalStateException("Cannot call implementOn(...) for a Checker");
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public boolean match(Shadow shadow, World world) {
        if (super.match(shadow, world)) {
            world.reportCheckerMatch(this, shadow);
            return false;
        }
        return false;
    }

    @Override // org.aspectj.util.PartialOrder.PartialComparable
    public int compareTo(Object other) {
        return 0;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public boolean mustCheckExceptions() {
        return true;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public Collection<ResolvedType> getThrownExceptions() {
        return Collections.emptyList();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Checker)) {
            return false;
        }
        Checker o = (Checker) other;
        return o.isError == this.isError && (o.pointcut != null ? o.pointcut.equals(this.pointcut) : this.pointcut == null);
    }

    public int hashCode() {
        if (this.hashCode == -1) {
            int result = (37 * 17) + (this.isError ? 1 : 0);
            this.hashCode = (37 * result) + (this.pointcut == null ? 0 : this.pointcut.hashCode());
        }
        return this.hashCode;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public ShadowMunger parameterizeWith(ResolvedType declaringType, Map<String, UnresolvedType> typeVariableMap) {
        Checker ret = new Checker(this.pointcut.parameterizeWith(typeVariableMap, declaringType.getWorld()), this.start, this.end, this.sourceContext, this.message, this.isError);
        return ret;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public ShadowMunger concretize(ResolvedType theAspect, World world, PerClause clause) {
        this.pointcut = this.pointcut.concretize(theAspect, getDeclaringType(), 0, this);
        this.hashCode = -1;
        return this;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public ResolvedType getConcreteAspect() {
        return getDeclaringType();
    }

    private int nextCurly(String string, int pos) {
        do {
            int curlyIndex = string.indexOf(123, pos);
            if (curlyIndex == -1) {
                return -1;
            }
            if (curlyIndex == 0) {
                return 0;
            }
            if (string.charAt(curlyIndex - 1) != '\\') {
                return curlyIndex;
            }
            pos = curlyIndex + 1;
        } while (pos < string.length());
        return -1;
    }

    private String format(String msg, Shadow shadow) {
        int pos = 0;
        int curlyIndex = nextCurly(msg, 0);
        if (curlyIndex == -1) {
            if (msg.indexOf(123) != -1) {
                return msg.replace("\\{", "{");
            }
            return msg;
        }
        StringBuffer ret = new StringBuffer();
        while (curlyIndex >= 0) {
            if (curlyIndex > 0) {
                ret.append(msg.substring(pos, curlyIndex).replace("\\{", "{"));
            }
            int endCurly = msg.indexOf(125, curlyIndex);
            if (endCurly == -1) {
                ret.append('{');
                int i = curlyIndex + 1;
            } else {
                ret.append(getValue(msg.substring(curlyIndex + 1, endCurly), shadow));
            }
            pos = endCurly + 1;
            curlyIndex = nextCurly(msg, pos);
        }
        ret.append(msg.substring(pos, msg.length()));
        return ret.toString();
    }

    private String getValue(String key, Shadow shadow) {
        if (key.equalsIgnoreCase("joinpoint")) {
            return shadow.toString();
        }
        if (key.equalsIgnoreCase("joinpoint.kind")) {
            return shadow.getKind().getName();
        }
        if (key.equalsIgnoreCase("joinpoint.enclosingclass")) {
            return shadow.getEnclosingType().getName();
        }
        if (key.equalsIgnoreCase("joinpoint.enclosingmember.name")) {
            Member member = shadow.getEnclosingCodeSignature();
            if (member == null) {
                return "";
            }
            return member.getName();
        }
        if (key.equalsIgnoreCase("joinpoint.enclosingmember")) {
            Member member2 = shadow.getEnclosingCodeSignature();
            if (member2 == null) {
                return "";
            }
            return member2.toString();
        }
        if (key.equalsIgnoreCase("joinpoint.signature")) {
            return shadow.getSignature().toString();
        }
        if (key.equalsIgnoreCase("joinpoint.signature.declaringtype")) {
            return shadow.getSignature().getDeclaringType().toString();
        }
        if (key.equalsIgnoreCase("joinpoint.signature.name")) {
            return shadow.getSignature().getName();
        }
        if (key.equalsIgnoreCase("joinpoint.sourcelocation.sourcefile")) {
            ISourceLocation loc = shadow.getSourceLocation();
            if (loc != null && loc.getSourceFile() != null) {
                return loc.getSourceFile().toString();
            }
            return "UNKNOWN";
        }
        if (key.equalsIgnoreCase("joinpoint.sourcelocation.line")) {
            ISourceLocation loc2 = shadow.getSourceLocation();
            if (loc2 != null) {
                return Integer.toString(loc2.getLine());
            }
            return "-1";
        }
        if (key.equalsIgnoreCase("advice.aspecttype")) {
            return getDeclaringType().getName();
        }
        if (key.equalsIgnoreCase("advice.sourcelocation.line")) {
            ISourceLocation loc3 = getSourceLocation();
            if (loc3 != null && loc3.getSourceFile() != null) {
                return Integer.toString(loc3.getLine());
            }
            return "-1";
        }
        if (key.equalsIgnoreCase("advice.sourcelocation.sourcefile")) {
            ISourceLocation loc4 = getSourceLocation();
            if (loc4 != null && loc4.getSourceFile() != null) {
                return loc4.getSourceFile().toString();
            }
            return "UNKNOWN";
        }
        return "UNKNOWN_KEY{" + key + "}";
    }
}
