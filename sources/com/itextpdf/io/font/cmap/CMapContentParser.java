package com.itextpdf.io.font.cmap;

import com.fasterxml.jackson.core.base.GeneratorBase;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.source.ByteBuffer;
import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.kernel.PdfException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/cmap/CMapContentParser.class */
public class CMapContentParser {
    public static final int COMMAND_TYPE = 200;
    private PdfTokenizer tokeniser;

    public CMapContentParser(PdfTokenizer tokeniser) {
        this.tokeniser = tokeniser;
    }

    public void parse(List<CMapObject> ls) throws IOException {
        CMapObject ob;
        ls.clear();
        do {
            ob = readObject();
            if (ob != null) {
                ls.add(ob);
            } else {
                return;
            }
        } while (!ob.isLiteral());
    }

    public CMapObject readDictionary() throws IOException {
        Map<String, CMapObject> dic = new HashMap<>();
        while (nextValidToken()) {
            if (this.tokeniser.getTokenType() != PdfTokenizer.TokenType.EndDic) {
                if (this.tokeniser.getTokenType() != PdfTokenizer.TokenType.Other || !"def".equals(this.tokeniser.getStringValue())) {
                    if (this.tokeniser.getTokenType() != PdfTokenizer.TokenType.Name) {
                        throw new com.itextpdf.io.IOException(PdfException.DictionaryKey1IsNotAName).setMessageParams(this.tokeniser.getStringValue());
                    }
                    String name = this.tokeniser.getStringValue();
                    CMapObject obj = readObject();
                    if (obj.isToken()) {
                        if (obj.toString().equals(">>")) {
                            this.tokeniser.throwError(com.itextpdf.io.IOException.UnexpectedGtGt, new Object[0]);
                        }
                        if (obj.toString().equals("]")) {
                            this.tokeniser.throwError("Unexpected close bracket.", new Object[0]);
                        }
                    }
                    dic.put(name, obj);
                }
            } else {
                return new CMapObject(7, dic);
            }
        }
        throw new com.itextpdf.io.IOException(PdfException.UnexpectedEndOfFile);
    }

    public CMapObject readArray() throws IOException {
        List<CMapObject> array = new ArrayList<>();
        while (true) {
            CMapObject obj = readObject();
            if (obj.isToken()) {
                if (!obj.toString().equals("]")) {
                    if (obj.toString().equals(">>")) {
                        this.tokeniser.throwError(com.itextpdf.io.IOException.UnexpectedGtGt, new Object[0]);
                    }
                } else {
                    return new CMapObject(6, array);
                }
            }
            array.add(obj);
        }
    }

    public CMapObject readObject() throws IOException {
        CMapObject obj;
        if (!nextValidToken()) {
            return null;
        }
        PdfTokenizer.TokenType type = this.tokeniser.getTokenType();
        switch (type) {
            case StartDic:
                return readDictionary();
            case StartArray:
                return readArray();
            case String:
                if (this.tokeniser.isHexString()) {
                    obj = new CMapObject(2, PdfTokenizer.decodeStringContent(this.tokeniser.getByteContent(), true));
                } else {
                    obj = new CMapObject(1, PdfTokenizer.decodeStringContent(this.tokeniser.getByteContent(), false));
                }
                return obj;
            case Name:
                return new CMapObject(3, decodeName(this.tokeniser.getByteContent()));
            case Number:
                CMapObject numObject = new CMapObject(4, null);
                try {
                    numObject.setValue(Integer.valueOf((int) Double.parseDouble(this.tokeniser.getStringValue())));
                } catch (NumberFormatException e) {
                    numObject.setValue(Integer.MIN_VALUE);
                }
                return numObject;
            case Other:
                return new CMapObject(5, this.tokeniser.getStringValue());
            case EndArray:
                return new CMapObject(8, "]");
            case EndDic:
                return new CMapObject(8, ">>");
            default:
                return new CMapObject(0, "");
        }
    }

    public boolean nextValidToken() throws IOException {
        while (this.tokeniser.nextToken()) {
            if (this.tokeniser.getTokenType() != PdfTokenizer.TokenType.Comment) {
                return true;
            }
        }
        return false;
    }

    protected static String decodeName(byte[] content) {
        StringBuilder buf = new StringBuilder();
        int k = 0;
        while (k < content.length) {
            try {
                char c = (char) content[k];
                if (c == '#') {
                    byte c1 = content[k + 1];
                    byte c2 = content[k + 2];
                    c = (char) ((ByteBuffer.getHex(c1) << 4) + ByteBuffer.getHex(c2));
                    k += 2;
                }
                buf.append(c);
                k++;
            } catch (IndexOutOfBoundsException e) {
            }
        }
        return buf.toString();
    }

    private static String toHex4(int n) {
        String s = "0000" + Integer.toHexString(n);
        return s.substring(s.length() - 4);
    }

    public static String toHex(int n) {
        if (n < 65536) {
            return "<" + toHex4(n) + ">";
        }
        int n2 = n - 65536;
        int high = (n2 / 1024) + GeneratorBase.SURR1_FIRST;
        int low = (n2 % 1024) + GeneratorBase.SURR2_FIRST;
        return "[<" + toHex4(high) + toHex4(low) + ">]";
    }

    public static String decodeCMapObject(CMapObject cMapObject) {
        if (cMapObject.isHexString()) {
            return PdfEncodings.convertToString(((String) cMapObject.getValue()).getBytes(), PdfEncodings.UNICODE_BIG_UNMARKED);
        }
        return (String) cMapObject.getValue();
    }
}
