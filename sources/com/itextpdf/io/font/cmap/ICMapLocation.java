package com.itextpdf.io.font.cmap;

import com.itextpdf.io.source.PdfTokenizer;
import java.io.IOException;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/cmap/ICMapLocation.class */
public interface ICMapLocation {
    PdfTokenizer getLocation(String str) throws IOException;
}
