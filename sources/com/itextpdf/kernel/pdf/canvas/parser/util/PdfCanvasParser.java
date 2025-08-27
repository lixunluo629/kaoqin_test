package com.itextpdf.kernel.pdf.canvas.parser.util;

import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfLiteral;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/util/PdfCanvasParser.class */
public class PdfCanvasParser {
    private PdfTokenizer tokeniser;
    private PdfResources currentResources;

    public PdfCanvasParser(PdfTokenizer tokeniser) {
        this.tokeniser = tokeniser;
    }

    public PdfCanvasParser(PdfTokenizer tokeniser, PdfResources currentResources) {
        this.tokeniser = tokeniser;
        this.currentResources = currentResources;
    }

    public List<PdfObject> parse(List<PdfObject> ls) throws IOException {
        if (ls == null) {
            ls = new ArrayList();
        } else {
            ls.clear();
        }
        while (true) {
            PdfObject ob = readObject();
            if (ob == null) {
                break;
            }
            ls.add(ob);
            if (this.tokeniser.getTokenType() == PdfTokenizer.TokenType.Other) {
                if ("BI".equals(ob.toString())) {
                    PdfStream inlineImageAsStream = InlineImageParsingUtils.parse(this, this.currentResources.getResource(PdfName.ColorSpace));
                    ls.clear();
                    ls.add(inlineImageAsStream);
                    ls.add(new PdfLiteral("EI"));
                }
            }
        }
        return ls;
    }

    public PdfTokenizer getTokeniser() {
        return this.tokeniser;
    }

    public void setTokeniser(PdfTokenizer tokeniser) {
        this.tokeniser = tokeniser;
    }

    public PdfDictionary readDictionary() throws IOException {
        PdfDictionary dic = new PdfDictionary();
        while (nextValidToken()) {
            if (this.tokeniser.getTokenType() != PdfTokenizer.TokenType.EndDic) {
                if (this.tokeniser.getTokenType() != PdfTokenizer.TokenType.Name) {
                    this.tokeniser.throwError(PdfException.DictionaryKey1IsNotAName, this.tokeniser.getStringValue());
                }
                PdfName name = new PdfName(this.tokeniser.getStringValue());
                PdfObject obj = readObject();
                dic.put(name, obj);
            } else {
                return dic;
            }
        }
        throw new PdfException(PdfException.UnexpectedEndOfFile);
    }

    public PdfArray readArray() throws IOException {
        PdfArray array = new PdfArray();
        while (true) {
            PdfObject obj = readObject();
            if (obj.isArray() || this.tokeniser.getTokenType() != PdfTokenizer.TokenType.EndArray) {
                if (this.tokeniser.getTokenType() == PdfTokenizer.TokenType.EndDic && obj.getType() != 3) {
                    this.tokeniser.throwError(PdfException.UnexpectedGtGt, new Object[0]);
                }
                array.add(obj);
            } else {
                return array;
            }
        }
    }

    public PdfObject readObject() throws IOException {
        if (!nextValidToken()) {
            return null;
        }
        PdfTokenizer.TokenType type = this.tokeniser.getTokenType();
        switch (type) {
            case StartDic:
                PdfDictionary dic = readDictionary();
                return dic;
            case StartArray:
                return readArray();
            case String:
                PdfString str = new PdfString(this.tokeniser.getDecodedStringContent()).setHexWriting(this.tokeniser.isHexString());
                return str;
            case Name:
                return new PdfName(this.tokeniser.getByteContent());
            case Number:
                return new PdfNumber(this.tokeniser.getByteContent());
            default:
                return new PdfLiteral(this.tokeniser.getByteContent());
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
}
