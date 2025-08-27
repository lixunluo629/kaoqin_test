package org.springframework.boot.ansi;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/ansi/AnsiStyle.class */
public enum AnsiStyle implements AnsiElement {
    NORMAL("0"),
    BOLD("1"),
    FAINT("2"),
    ITALIC("3"),
    UNDERLINE("4");

    private final String code;

    AnsiStyle(String code) {
        this.code = code;
    }

    @Override // java.lang.Enum, org.springframework.boot.ansi.AnsiElement
    public String toString() {
        return this.code;
    }
}
