package com.graphbuilder.math;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/TermNode.class */
public abstract class TermNode extends Expression {
    protected String name = null;
    protected boolean negate = false;

    public TermNode(String name, boolean negate) {
        setName(name);
        setNegate(negate);
    }

    public boolean getNegate() {
        return this.negate;
    }

    public void setNegate(boolean b) {
        this.negate = b;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String s) {
        if (s == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (!isValidName(s)) {
            throw new IllegalArgumentException("invalid name: " + s);
        }
        this.name = s;
    }

    private static boolean isValidName(String s) {
        if (s.length() == 0) {
            return false;
        }
        char c = s.charAt(0);
        if ((c >= '0' && c <= '9') || c == '.' || c == ',' || c == '(' || c == ')' || c == '^' || c == '*' || c == '/' || c == '+' || c == '-' || c == ' ' || c == '\t' || c == '\n') {
            return false;
        }
        for (int i = 1; i < s.length(); i++) {
            char c2 = s.charAt(i);
            if (c2 == ',' || c2 == '(' || c2 == ')' || c2 == '^' || c2 == '*' || c2 == '/' || c2 == '+' || c2 == '-' || c2 == ' ' || c2 == '\t' || c2 == '\n') {
                return false;
            }
        }
        return true;
    }
}
