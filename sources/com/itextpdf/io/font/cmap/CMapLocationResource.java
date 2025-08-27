package com.itextpdf.io.font.cmap;

import com.itextpdf.io.font.constants.FontResources;
import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.io.util.ResourceUtil;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/cmap/CMapLocationResource.class */
public class CMapLocationResource implements ICMapLocation {
    @Override // com.itextpdf.io.font.cmap.ICMapLocation
    public PdfTokenizer getLocation(String location) throws IOException {
        String fullName = FontResources.CMAPS + location;
        InputStream inp = ResourceUtil.getResourceStream(fullName);
        if (inp == null) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.Cmap1WasNotFound).setMessageParams(fullName);
        }
        return new PdfTokenizer(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(inp)));
    }
}
