package com.itextpdf.layout.hyphenation;

import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/hyphenation/IPatternConsumer.class */
public interface IPatternConsumer {
    void addClass(String str);

    void addException(String str, List list);

    void addPattern(String str, String str2);
}
