package org.aspectj.weaver;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.bridge.SourceLocation;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.util.PartialOrder;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.TypePattern;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ShadowMunger.class */
public abstract class ShadowMunger implements PartialOrder.PartialComparable, IHasPosition {
    public static final ShadowMunger[] NONE = new ShadowMunger[0];
    private static int VERSION_1 = 1;
    protected static final int ShadowMungerAdvice = 1;
    protected static final int ShadowMungerDeow = 2;
    public String handle = null;
    private int shadowMungerKind;
    protected int start;
    protected int end;
    protected ISourceContext sourceContext;
    private ISourceLocation sourceLocation;
    private ISourceLocation binarySourceLocation;
    private File binaryFile;
    private ResolvedType declaringType;
    private boolean isBinary;
    private boolean checkedIsBinary;
    protected Pointcut pointcut;

    public abstract ResolvedType getConcreteAspect();

    public abstract ShadowMunger concretize(ResolvedType resolvedType, World world, PerClause perClause);

    public abstract void specializeOn(Shadow shadow);

    public abstract boolean implementOn(Shadow shadow);

    public abstract ShadowMunger parameterizeWith(ResolvedType resolvedType, Map<String, UnresolvedType> map);

    public abstract Collection<ResolvedType> getThrownExceptions();

    public abstract boolean mustCheckExceptions();

    protected ShadowMunger() {
    }

    public ShadowMunger(Pointcut pointcut, int start, int end, ISourceContext sourceContext, int shadowMungerKind) {
        this.shadowMungerKind = shadowMungerKind;
        this.pointcut = pointcut;
        this.start = start;
        this.end = end;
        this.sourceContext = sourceContext;
    }

    public boolean match(Shadow shadow, World world) throws AbortException {
        TypePattern scoped;
        if (world.isXmlConfigured() && world.isAspectIncluded(this.declaringType) && (scoped = world.getAspectScope(this.declaringType)) != null) {
            Set<ResolvedType> excludedTypes = world.getExclusionMap().get(this.declaringType);
            ResolvedType type = shadow.getEnclosingType().resolve(world);
            if (excludedTypes != null && excludedTypes.contains(type)) {
                return false;
            }
            boolean b = scoped.matches(type, TypePattern.STATIC).alwaysTrue();
            if (!b) {
                if (!world.getMessageHandler().isIgnoring(IMessage.INFO)) {
                    world.getMessageHandler().handleMessage(MessageUtil.info("Type '" + type.getName() + "' not woven by aspect '" + this.declaringType.getName() + "' due to scope exclusion in XML definition"));
                }
                if (excludedTypes == null) {
                    Set<ResolvedType> excludedTypes2 = new HashSet<>();
                    excludedTypes2.add(type);
                    world.getExclusionMap().put(this.declaringType, excludedTypes2);
                    return false;
                }
                excludedTypes.add(type);
                return false;
            }
        }
        if (world.areInfoMessagesEnabled() && world.isTimingEnabled()) {
            long starttime = System.nanoTime();
            FuzzyBoolean isMatch = this.pointcut.match(shadow);
            long endtime = System.nanoTime();
            world.record(this.pointcut, endtime - starttime);
            return isMatch.maybeTrue();
        }
        FuzzyBoolean isMatch2 = this.pointcut.match(shadow);
        return isMatch2.maybeTrue();
    }

    @Override // org.aspectj.util.PartialOrder.PartialComparable
    public int fallbackCompareTo(Object other) {
        return toString().compareTo(toString());
    }

    @Override // org.aspectj.weaver.IHasPosition
    public int getEnd() {
        return this.end;
    }

    @Override // org.aspectj.weaver.IHasPosition
    public int getStart() {
        return this.start;
    }

    public ISourceLocation getSourceLocation() {
        if (this.sourceLocation == null && this.sourceContext != null) {
            this.sourceLocation = this.sourceContext.makeSourceLocation(this);
        }
        if (isBinary()) {
            if (this.binarySourceLocation == null) {
                this.binarySourceLocation = getBinarySourceLocation(this.sourceLocation);
            }
            return this.binarySourceLocation;
        }
        return this.sourceLocation;
    }

    public Pointcut getPointcut() {
        return this.pointcut;
    }

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }

    public void setDeclaringType(ResolvedType aType) {
        this.declaringType = aType;
    }

    public ResolvedType getDeclaringType() {
        return this.declaringType;
    }

    public ISourceLocation getBinarySourceLocation(ISourceLocation sl) {
        if (sl == null) {
            return null;
        }
        String sourceFileName = null;
        if (getDeclaringType() instanceof ReferenceType) {
            String s = ((ReferenceType) getDeclaringType()).getDelegate().getSourcefilename();
            int i = s.lastIndexOf(47);
            if (i != -1) {
                sourceFileName = s.substring(i + 1);
            } else {
                sourceFileName = s;
            }
        }
        ISourceLocation sLoc = new SourceLocation(getBinaryFile(), sl.getLine(), sl.getEndLine(), sl.getColumn() == 0 ? ISourceLocation.NO_COLUMN : sl.getColumn(), sl.getContext(), sourceFileName);
        return sLoc;
    }

    private File getBinaryFile() {
        String path;
        if (this.binaryFile == null) {
            String binaryPath = getDeclaringType().getBinaryPath();
            if (binaryPath == null) {
                binaryPath = "classpath";
                getDeclaringType().setBinaryPath(binaryPath);
            }
            if (binaryPath.indexOf("!") == -1) {
                File f = getDeclaringType().getSourceLocation().getSourceFile();
                int i = f.getPath().lastIndexOf(46);
                if (i != -1) {
                    path = f.getPath().substring(0, i) + ClassUtils.CLASS_FILE_SUFFIX;
                } else {
                    path = f.getPath() + ClassUtils.CLASS_FILE_SUFFIX;
                }
                this.binaryFile = new File(binaryPath + "!" + path);
            } else {
                this.binaryFile = new File(binaryPath);
            }
        }
        return this.binaryFile;
    }

    public boolean isBinary() {
        if (!this.checkedIsBinary) {
            ResolvedType rt = getDeclaringType();
            if (rt != null) {
                this.isBinary = rt.getBinaryPath() != null;
            }
            this.checkedIsBinary = true;
        }
        return this.isBinary;
    }

    public void write(CompressingDataOutputStream stream) throws IOException {
        stream.writeInt(VERSION_1);
        stream.writeInt(this.shadowMungerKind);
        stream.writeInt(this.start);
        stream.writeInt(this.end);
        PersistenceSupport.write(stream, this.sourceContext);
        PersistenceSupport.write(stream, this.sourceLocation);
        PersistenceSupport.write(stream, this.binarySourceLocation);
        PersistenceSupport.write(stream, this.binaryFile);
        this.declaringType.write(stream);
        stream.writeBoolean(this.isBinary);
        stream.writeBoolean(this.checkedIsBinary);
        this.pointcut.write(stream);
    }
}
