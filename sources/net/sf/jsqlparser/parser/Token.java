package net.sf.jsqlparser.parser;

import java.io.Serializable;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/parser/Token.class */
public class Token implements Serializable {
    private static final long serialVersionUID = 1;
    public int kind;
    public int beginLine;
    public int beginColumn;
    public int endLine;
    public int endColumn;
    public String image;
    public Token next;
    public Token specialToken;

    public Object getValue() {
        return null;
    }

    public Token() {
    }

    public Token(int kind) {
        this(kind, null);
    }

    public Token(int kind, String image) {
        this.kind = kind;
        this.image = image;
    }

    public String toString() {
        return this.image;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0001. Please report as an issue. */
    public static Token newToken(int ofKind, String image) {
        switch (ofKind) {
        }
        return new Token(ofKind, image);
    }

    public static Token newToken(int ofKind) {
        return newToken(ofKind, null);
    }
}
