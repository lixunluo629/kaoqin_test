package com.itextpdf.io.font.woff2;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/TableTags.class */
class TableTags {
    public static final int kGlyfTableTag = 1735162214;
    public static final int kHeadTableTag = 1751474532;
    public static final int kLocaTableTag = 1819239265;
    public static final int kDsigTableTag = 1146308935;
    public static final int kCffTableTag = 1128678944;
    public static final int kHmtxTableTag = 1752003704;
    public static final int kHheaTableTag = 1751672161;
    public static final int kMaxpTableTag = 1835104368;
    public static int[] kKnownTags = {tag('c', 'm', 'a', 'p'), tag('h', 'e', 'a', 'd'), tag('h', 'h', 'e', 'a'), tag('h', 'm', 't', 'x'), tag('m', 'a', 'x', 'p'), tag('n', 'a', 'm', 'e'), tag('O', 'S', '/', '2'), tag('p', 'o', 's', 't'), tag('c', 'v', 't', ' '), tag('f', 'p', 'g', 'm'), tag('g', 'l', 'y', 'f'), tag('l', 'o', 'c', 'a'), tag('p', 'r', 'e', 'p'), tag('C', 'F', 'F', ' '), tag('V', 'O', 'R', 'G'), tag('E', 'B', 'D', 'T'), tag('E', 'B', 'L', 'C'), tag('g', 'a', 's', 'p'), tag('h', 'd', 'm', 'x'), tag('k', 'e', 'r', 'n'), tag('L', 'T', 'S', 'H'), tag('P', 'C', 'L', 'T'), tag('V', 'D', 'M', 'X'), tag('v', 'h', 'e', 'a'), tag('v', 'm', 't', 'x'), tag('B', 'A', 'S', 'E'), tag('G', 'D', 'E', 'F'), tag('G', 'P', 'O', 'S'), tag('G', 'S', 'U', 'B'), tag('E', 'B', 'S', 'C'), tag('J', 'S', 'T', 'F'), tag('M', 'A', 'T', 'H'), tag('C', 'B', 'D', 'T'), tag('C', 'B', 'L', 'C'), tag('C', 'O', 'L', 'R'), tag('C', 'P', 'A', 'L'), tag('S', 'V', 'G', ' '), tag('s', 'b', 'i', 'x'), tag('a', 'c', 'n', 't'), tag('a', 'v', 'a', 'r'), tag('b', 'd', 'a', 't'), tag('b', 'l', 'o', 'c'), tag('b', 's', 'l', 'n'), tag('c', 'v', 'a', 'r'), tag('f', 'd', 's', 'c'), tag('f', 'e', 'a', 't'), tag('f', 'm', 't', 'x'), tag('f', 'v', 'a', 'r'), tag('g', 'v', 'a', 'r'), tag('h', 's', 't', 'y'), tag('j', 'u', 's', 't'), tag('l', 'c', 'a', 'r'), tag('m', 'o', 'r', 't'), tag('m', 'o', 'r', 'x'), tag('o', 'p', 'b', 'd'), tag('p', 'r', 'o', 'p'), tag('t', 'r', 'a', 'k'), tag('Z', 'a', 'p', 'f'), tag('S', 'i', 'l', 'f'), tag('G', 'l', 'a', 't'), tag('G', 'l', 'o', 'c'), tag('F', 'e', 'a', 't'), tag('S', 'i', 'l', 'l')};

    TableTags() {
    }

    private static int tag(char a, char b, char c, char d) {
        return (a << 24) | (b << 16) | (c << '\b') | d;
    }
}
