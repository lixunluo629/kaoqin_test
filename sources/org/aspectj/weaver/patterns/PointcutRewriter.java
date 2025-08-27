package org.aspectj.weaver.patterns;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/PointcutRewriter.class */
public class PointcutRewriter {
    private static final boolean WATCH_PROGRESS = false;

    public Pointcut rewrite(Pointcut pc, boolean forceRewrite) {
        Pointcut result = pc;
        if (forceRewrite || !isDNF(pc)) {
            result = pullUpDisjunctions(distributeNot(result));
        }
        return sortOrs(removeNothings(simplifyAnds(result)));
    }

    public Pointcut rewrite(Pointcut pc) {
        return rewrite(pc, false);
    }

    private boolean isDNF(Pointcut pc) {
        return isDNFHelper(pc, true);
    }

    private boolean isDNFHelper(Pointcut pc, boolean canStillHaveOrs) {
        if (isAnd(pc)) {
            AndPointcut ap = (AndPointcut) pc;
            return isDNFHelper(ap.getLeft(), false) && isDNFHelper(ap.getRight(), false);
        }
        if (isOr(pc)) {
            if (!canStillHaveOrs) {
                return false;
            }
            OrPointcut op = (OrPointcut) pc;
            return isDNFHelper(op.getLeft(), true) && isDNFHelper(op.getRight(), true);
        }
        if (isNot(pc)) {
            return isDNFHelper(((NotPointcut) pc).getNegatedPointcut(), canStillHaveOrs);
        }
        return true;
    }

    public static String format(Pointcut p) {
        String s = p.toString();
        return s;
    }

    private Pointcut distributeNot(Pointcut pc) {
        if (isNot(pc)) {
            NotPointcut npc = (NotPointcut) pc;
            Pointcut notBody = distributeNot(npc.getNegatedPointcut());
            if (isNot(notBody)) {
                return ((NotPointcut) notBody).getNegatedPointcut();
            }
            if (isAnd(notBody)) {
                AndPointcut apc = (AndPointcut) notBody;
                Pointcut newLeft = distributeNot(new NotPointcut(apc.getLeft(), npc.getStart()));
                Pointcut newRight = distributeNot(new NotPointcut(apc.getRight(), npc.getStart()));
                return new OrPointcut(newLeft, newRight);
            }
            if (isOr(notBody)) {
                OrPointcut opc = (OrPointcut) notBody;
                Pointcut newLeft2 = distributeNot(new NotPointcut(opc.getLeft(), npc.getStart()));
                Pointcut newRight2 = distributeNot(new NotPointcut(opc.getRight(), npc.getStart()));
                return new AndPointcut(newLeft2, newRight2);
            }
            return new NotPointcut(notBody, npc.getStart());
        }
        if (isAnd(pc)) {
            AndPointcut apc2 = (AndPointcut) pc;
            Pointcut left = distributeNot(apc2.getLeft());
            Pointcut right = distributeNot(apc2.getRight());
            return new AndPointcut(left, right);
        }
        if (isOr(pc)) {
            OrPointcut opc2 = (OrPointcut) pc;
            Pointcut left2 = distributeNot(opc2.getLeft());
            Pointcut right2 = distributeNot(opc2.getRight());
            return new OrPointcut(left2, right2);
        }
        return pc;
    }

    private Pointcut pullUpDisjunctions(Pointcut pc) {
        if (isNot(pc)) {
            NotPointcut npc = (NotPointcut) pc;
            return new NotPointcut(pullUpDisjunctions(npc.getNegatedPointcut()));
        }
        if (isAnd(pc)) {
            AndPointcut apc = (AndPointcut) pc;
            Pointcut left = pullUpDisjunctions(apc.getLeft());
            Pointcut right = pullUpDisjunctions(apc.getRight());
            if (isOr(left) && !isOr(right)) {
                Pointcut leftLeft = ((OrPointcut) left).getLeft();
                Pointcut leftRight = ((OrPointcut) left).getRight();
                return pullUpDisjunctions(new OrPointcut(new AndPointcut(leftLeft, right), new AndPointcut(leftRight, right)));
            }
            if (isOr(right) && !isOr(left)) {
                Pointcut rightLeft = ((OrPointcut) right).getLeft();
                Pointcut rightRight = ((OrPointcut) right).getRight();
                return pullUpDisjunctions(new OrPointcut(new AndPointcut(left, rightLeft), new AndPointcut(left, rightRight)));
            }
            if (isOr(right) && isOr(left)) {
                Pointcut A = pullUpDisjunctions(((OrPointcut) left).getLeft());
                Pointcut B = pullUpDisjunctions(((OrPointcut) left).getRight());
                Pointcut C = pullUpDisjunctions(((OrPointcut) right).getLeft());
                Pointcut D = pullUpDisjunctions(((OrPointcut) right).getRight());
                Pointcut newLeft = new OrPointcut(new AndPointcut(A, C), new AndPointcut(A, D));
                Pointcut newRight = new OrPointcut(new AndPointcut(B, C), new AndPointcut(B, D));
                return pullUpDisjunctions(new OrPointcut(newLeft, newRight));
            }
            return new AndPointcut(left, right);
        }
        if (isOr(pc)) {
            OrPointcut opc = (OrPointcut) pc;
            return new OrPointcut(pullUpDisjunctions(opc.getLeft()), pullUpDisjunctions(opc.getRight()));
        }
        return pc;
    }

    public Pointcut not(Pointcut p) {
        if (isNot(p)) {
            return ((NotPointcut) p).getNegatedPointcut();
        }
        return new NotPointcut(p);
    }

    public Pointcut createAndsFor(Pointcut[] ps) {
        if (ps.length == 1) {
            return ps[0];
        }
        if (ps.length == 2) {
            return new AndPointcut(ps[0], ps[1]);
        }
        Pointcut[] subset = new Pointcut[ps.length - 1];
        for (int i = 1; i < ps.length; i++) {
            subset[i - 1] = ps[i];
        }
        return new AndPointcut(ps[0], createAndsFor(subset));
    }

    private Pointcut simplifyAnds(Pointcut pc) {
        if (isNot(pc)) {
            NotPointcut npc = (NotPointcut) pc;
            Pointcut notBody = npc.getNegatedPointcut();
            if (isNot(notBody)) {
                return simplifyAnds(((NotPointcut) notBody).getNegatedPointcut());
            }
            return new NotPointcut(simplifyAnds(npc.getNegatedPointcut()));
        }
        if (isOr(pc)) {
            OrPointcut opc = (OrPointcut) pc;
            return new OrPointcut(simplifyAnds(opc.getLeft()), simplifyAnds(opc.getRight()));
        }
        if (isAnd(pc)) {
            return simplifyAnd((AndPointcut) pc);
        }
        return pc;
    }

    private Pointcut simplifyAnd(AndPointcut apc) {
        SortedSet<Pointcut> nodes = new TreeSet<>(new PointcutEvaluationExpenseComparator());
        collectAndNodes(apc, nodes);
        for (Pointcut element : nodes) {
            if (element instanceof NotPointcut) {
                Pointcut body = ((NotPointcut) element).getNegatedPointcut();
                if (nodes.contains(body)) {
                    return Pointcut.makeMatchesNothing(body.state);
                }
            }
            if ((element instanceof IfPointcut) && ((IfPointcut) element).alwaysFalse()) {
                return Pointcut.makeMatchesNothing(element.state);
            }
            if (element.couldMatchKinds() == Shadow.NO_SHADOW_KINDS_BITS) {
                return element;
            }
        }
        if (apc.couldMatchKinds() == Shadow.NO_SHADOW_KINDS_BITS) {
            return Pointcut.makeMatchesNothing(apc.state);
        }
        Iterator<Pointcut> iter = nodes.iterator();
        Pointcut next = iter.next();
        while (true) {
            Pointcut result = next;
            if (iter.hasNext()) {
                Pointcut right = iter.next();
                next = new AndPointcut(result, right);
            } else {
                return result;
            }
        }
    }

    private Pointcut sortOrs(Pointcut pc) {
        SortedSet<Pointcut> nodes = new TreeSet<>(new PointcutEvaluationExpenseComparator());
        collectOrNodes(pc, nodes);
        Iterator<Pointcut> iter = nodes.iterator();
        Pointcut next = iter.next();
        while (true) {
            Pointcut result = next;
            if (iter.hasNext()) {
                Pointcut right = iter.next();
                next = new OrPointcut(result, right);
            } else {
                return result;
            }
        }
    }

    private Pointcut removeNothings(Pointcut pc) {
        if (isAnd(pc)) {
            AndPointcut apc = (AndPointcut) pc;
            Pointcut right = removeNothings(apc.getRight());
            Pointcut left = removeNothings(apc.getLeft());
            if ((left instanceof Pointcut.MatchesNothingPointcut) || (right instanceof Pointcut.MatchesNothingPointcut)) {
                return new Pointcut.MatchesNothingPointcut();
            }
            return new AndPointcut(left, right);
        }
        if (isOr(pc)) {
            OrPointcut opc = (OrPointcut) pc;
            Pointcut right2 = removeNothings(opc.getRight());
            Pointcut left2 = removeNothings(opc.getLeft());
            if ((left2 instanceof Pointcut.MatchesNothingPointcut) && !(right2 instanceof Pointcut.MatchesNothingPointcut)) {
                return right2;
            }
            if ((right2 instanceof Pointcut.MatchesNothingPointcut) && !(left2 instanceof Pointcut.MatchesNothingPointcut)) {
                return left2;
            }
            if (!(left2 instanceof Pointcut.MatchesNothingPointcut) && !(right2 instanceof Pointcut.MatchesNothingPointcut)) {
                return new OrPointcut(left2, right2);
            }
            if ((left2 instanceof Pointcut.MatchesNothingPointcut) && (right2 instanceof Pointcut.MatchesNothingPointcut)) {
                return new Pointcut.MatchesNothingPointcut();
            }
        }
        return pc;
    }

    private void collectAndNodes(AndPointcut apc, Set<Pointcut> nodesSoFar) {
        Pointcut left = apc.getLeft();
        Pointcut right = apc.getRight();
        if (isAnd(left)) {
            collectAndNodes((AndPointcut) left, nodesSoFar);
        } else {
            nodesSoFar.add(left);
        }
        if (isAnd(right)) {
            collectAndNodes((AndPointcut) right, nodesSoFar);
        } else {
            nodesSoFar.add(right);
        }
    }

    private void collectOrNodes(Pointcut pc, Set<Pointcut> nodesSoFar) {
        if (isOr(pc)) {
            OrPointcut opc = (OrPointcut) pc;
            collectOrNodes(opc.getLeft(), nodesSoFar);
            collectOrNodes(opc.getRight(), nodesSoFar);
            return;
        }
        nodesSoFar.add(pc);
    }

    private boolean isNot(Pointcut pc) {
        return pc instanceof NotPointcut;
    }

    private boolean isAnd(Pointcut pc) {
        return pc instanceof AndPointcut;
    }

    private boolean isOr(Pointcut pc) {
        return pc instanceof OrPointcut;
    }
}
