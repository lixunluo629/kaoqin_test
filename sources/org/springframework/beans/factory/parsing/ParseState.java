package org.springframework.beans.factory.parsing;

import java.util.Stack;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/parsing/ParseState.class */
public final class ParseState {
    private static final char TAB = '\t';
    private final Stack<Entry> state;

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/parsing/ParseState$Entry.class */
    public interface Entry {
    }

    public ParseState() {
        this.state = new Stack<>();
    }

    private ParseState(ParseState other) {
        this.state = (Stack) other.state.clone();
    }

    public void push(Entry entry) {
        this.state.push(entry);
    }

    public void pop() {
        this.state.pop();
    }

    public Entry peek() {
        if (this.state.empty()) {
            return null;
        }
        return this.state.peek();
    }

    public ParseState snapshot() {
        return new ParseState(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < this.state.size(); x++) {
            if (x > 0) {
                sb.append('\n');
                for (int y = 0; y < x; y++) {
                    sb.append('\t');
                }
                sb.append("-> ");
            }
            sb.append(this.state.get(x));
        }
        return sb.toString();
    }
}
