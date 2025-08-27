package com.graphbuilder.math;

import com.graphbuilder.struc.Stack;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/ExpressionTree.class */
public class ExpressionTree {
    private ExpressionTree() {
    }

    public static Expression parse(String s) {
        if (s == null) {
            throw new ExpressionParseException("Expression string cannot be null.", -1);
        }
        return build(s, 0);
    }

    private static Expression build(String s, int indexErrorOffset) {
        char c;
        if (s.trim().length() == 0) {
            return null;
        }
        Stack s1 = new Stack();
        Stack s2 = new Stack();
        boolean term = true;
        boolean signed = false;
        boolean negate = false;
        int i = 0;
        while (i < s.length()) {
            char c2 = s.charAt(i);
            if (c2 != ' ' && c2 != '\t' && c2 != '\n') {
                if (term) {
                    if (c2 == '(') {
                        if (negate) {
                            throw new ExpressionParseException("Open bracket found after negate.", i);
                        }
                        s2.push("(");
                    } else if (!signed && (c2 == '+' || c2 == '-')) {
                        signed = true;
                        if (c2 == '-') {
                            negate = true;
                        }
                    } else if ((c2 >= '0' && c2 <= '9') || c2 == '.') {
                        int j = i + 1;
                        while (true) {
                            if (j >= s.length()) {
                                break;
                            }
                            char c3 = s.charAt(j);
                            if ((c3 >= '0' && c3 <= '9') || c3 == '.') {
                                j++;
                            } else if (c3 == 'e' || c3 == 'E') {
                                j++;
                                if (j < s.length()) {
                                    char c4 = s.charAt(j);
                                    if (c4 != '+' && c4 != '-' && (c4 < '0' || c4 > '9')) {
                                        throw new ExpressionParseException("Expected digit, plus sign or minus sign but found: " + String.valueOf(c4), j + indexErrorOffset);
                                    }
                                    j++;
                                }
                                while (j < s.length() && (c = s.charAt(j)) >= '0' && c <= '9') {
                                    j++;
                                }
                            }
                        }
                        String _d = s.substring(i, j);
                        try {
                            double d = Double.parseDouble(_d);
                            if (negate) {
                                d = -d;
                            }
                            s1.push(new ValNode(d));
                            i = j - 1;
                            negate = false;
                            term = false;
                            signed = false;
                        } catch (Throwable th) {
                            throw new ExpressionParseException("Improperly formatted value: " + _d, i + indexErrorOffset);
                        }
                    } else if (c2 != ',' && c2 != ')' && c2 != '^' && c2 != '*' && c2 != '/' && c2 != '+' && c2 != '-') {
                        int j2 = i + 1;
                        while (j2 < s.length()) {
                            c2 = s.charAt(j2);
                            if (c2 == ',' || c2 == ' ' || c2 == '\t' || c2 == '\n' || c2 == '(' || c2 == ')' || c2 == '^' || c2 == '*' || c2 == '/' || c2 == '+' || c2 == '-') {
                                break;
                            }
                            j2++;
                        }
                        if (j2 < s.length()) {
                            int k = j2;
                            while (true) {
                                if (c2 != ' ' && c2 != '\t' && c2 != '\n') {
                                    break;
                                }
                                k++;
                                if (k == s.length()) {
                                    break;
                                }
                                c2 = s.charAt(k);
                            }
                            if (c2 == '(') {
                                FuncNode fn = new FuncNode(s.substring(i, j2), negate);
                                int b = 1;
                                int kOld = k + 1;
                                while (b != 0) {
                                    k++;
                                    if (k >= s.length()) {
                                        throw new ExpressionParseException("Missing function close bracket.", i + indexErrorOffset);
                                    }
                                    char c5 = s.charAt(k);
                                    if (c5 == ')') {
                                        b--;
                                    } else if (c5 == '(') {
                                        b++;
                                    } else if (c5 == ',' && b == 1) {
                                        Expression o = build(s.substring(kOld, k), kOld);
                                        if (o == null) {
                                            throw new ExpressionParseException("Incomplete function.", kOld + indexErrorOffset);
                                        }
                                        fn.add(o);
                                        kOld = k + 1;
                                    }
                                }
                                Expression o2 = build(s.substring(kOld, k), kOld);
                                if (o2 == null) {
                                    if (fn.numChildren() > 0) {
                                        throw new ExpressionParseException("Incomplete function.", kOld + indexErrorOffset);
                                    }
                                } else {
                                    fn.add(o2);
                                }
                                s1.push(fn);
                                i = k;
                            } else {
                                s1.push(new VarNode(s.substring(i, j2), negate));
                                i = k - 1;
                            }
                        } else {
                            s1.push(new VarNode(s.substring(i, j2), negate));
                            i = j2 - 1;
                        }
                        negate = false;
                        term = false;
                        signed = false;
                    } else {
                        throw new ExpressionParseException("Unexpected character: " + String.valueOf(c2), i + indexErrorOffset);
                    }
                } else {
                    if (c2 == ')') {
                        Stack s3 = new Stack();
                        Stack s4 = new Stack();
                        while (!s2.isEmpty()) {
                            Object o3 = s2.pop();
                            if (!o3.equals("(")) {
                                s3.addToTail(s1.pop());
                                s4.addToTail(o3);
                            } else {
                                s3.addToTail(s1.pop());
                                s1.push(build(s3, s4));
                            }
                        }
                        throw new ExpressionParseException("Missing open bracket.", i + indexErrorOffset);
                    }
                    if (c2 == '^' || c2 == '*' || c2 == '/' || c2 == '+' || c2 == '-') {
                        term = true;
                        s2.push(String.valueOf(c2));
                    } else {
                        throw new ExpressionParseException("Expected operator or close bracket but found: " + String.valueOf(c2), i + indexErrorOffset);
                    }
                }
            }
            i++;
        }
        if (s1.size() != s2.size() + 1) {
            throw new ExpressionParseException("Incomplete expression.", indexErrorOffset + s.length());
        }
        return build(s1, s2);
    }

    private static Expression build(Stack s1, Stack s2) {
        Stack s3 = new Stack();
        Stack s4 = new Stack();
        while (!s2.isEmpty()) {
            Object o = s2.removeTail();
            Object o1 = s1.removeTail();
            Object o2 = s1.removeTail();
            if (o.equals("^")) {
                s1.addToTail(new PowNode((Expression) o1, (Expression) o2));
            } else {
                s1.addToTail(o2);
                s4.push(o);
                s3.push(o1);
            }
        }
        s3.push(s1.pop());
        while (!s4.isEmpty()) {
            Object o3 = s4.removeTail();
            Object o12 = s3.removeTail();
            Object o22 = s3.removeTail();
            if (o3.equals("*")) {
                s3.addToTail(new MultNode((Expression) o12, (Expression) o22));
            } else if (o3.equals("/")) {
                s3.addToTail(new DivNode((Expression) o12, (Expression) o22));
            } else {
                s3.addToTail(o22);
                s2.push(o3);
                s1.push(o12);
            }
        }
        s1.push(s3.pop());
        while (!s2.isEmpty()) {
            Object o4 = s2.removeTail();
            Object o13 = s1.removeTail();
            Object o23 = s1.removeTail();
            if (o4.equals("+")) {
                s1.addToTail(new AddNode((Expression) o13, (Expression) o23));
            } else if (o4.equals("-")) {
                s1.addToTail(new SubNode((Expression) o13, (Expression) o23));
            } else {
                throw new ExpressionParseException("Unknown operator: " + o4, -1);
            }
        }
        return (Expression) s1.pop();
    }
}
