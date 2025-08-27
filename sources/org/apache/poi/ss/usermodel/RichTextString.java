package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/RichTextString.class */
public interface RichTextString {
    void applyFont(int i, int i2, short s);

    void applyFont(int i, int i2, Font font);

    void applyFont(Font font);

    void clearFormatting();

    String getString();

    int length();

    int numFormattingRuns();

    int getIndexOfFormattingRun(int i);

    void applyFont(short s);
}
