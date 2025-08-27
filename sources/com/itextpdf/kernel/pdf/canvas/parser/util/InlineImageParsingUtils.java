package com.itextpdf.kernel.pdf.canvas.parser.util;

import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.filters.DoNothingFilter;
import com.itextpdf.kernel.pdf.filters.FilterHandlers;
import com.itextpdf.kernel.pdf.filters.FlateDecodeStrictFilter;
import com.itextpdf.kernel.pdf.filters.IFilterHandler;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/util/InlineImageParsingUtils.class */
public final class InlineImageParsingUtils {
    private static final byte[] EI = {69, 73};
    private static final Map<PdfName, PdfName> inlineImageEntryAbbreviationMap = new HashMap();
    private static final Map<PdfName, PdfName> inlineImageColorSpaceAbbreviationMap;
    private static final Map<PdfName, PdfName> inlineImageFilterAbbreviationMap;

    static {
        inlineImageEntryAbbreviationMap.put(PdfName.BitsPerComponent, PdfName.BitsPerComponent);
        inlineImageEntryAbbreviationMap.put(PdfName.ColorSpace, PdfName.ColorSpace);
        inlineImageEntryAbbreviationMap.put(PdfName.Decode, PdfName.Decode);
        inlineImageEntryAbbreviationMap.put(PdfName.DecodeParms, PdfName.DecodeParms);
        inlineImageEntryAbbreviationMap.put(PdfName.Filter, PdfName.Filter);
        inlineImageEntryAbbreviationMap.put(PdfName.Height, PdfName.Height);
        inlineImageEntryAbbreviationMap.put(PdfName.ImageMask, PdfName.ImageMask);
        inlineImageEntryAbbreviationMap.put(PdfName.Intent, PdfName.Intent);
        inlineImageEntryAbbreviationMap.put(PdfName.Interpolate, PdfName.Interpolate);
        inlineImageEntryAbbreviationMap.put(PdfName.Width, PdfName.Width);
        inlineImageEntryAbbreviationMap.put(new PdfName("BPC"), PdfName.BitsPerComponent);
        inlineImageEntryAbbreviationMap.put(new PdfName("CS"), PdfName.ColorSpace);
        inlineImageEntryAbbreviationMap.put(new PdfName("D"), PdfName.Decode);
        inlineImageEntryAbbreviationMap.put(new PdfName("DP"), PdfName.DecodeParms);
        inlineImageEntryAbbreviationMap.put(new PdfName("F"), PdfName.Filter);
        inlineImageEntryAbbreviationMap.put(new PdfName(StandardRoles.H), PdfName.Height);
        inlineImageEntryAbbreviationMap.put(new PdfName("IM"), PdfName.ImageMask);
        inlineImageEntryAbbreviationMap.put(new PdfName("I"), PdfName.Interpolate);
        inlineImageEntryAbbreviationMap.put(new PdfName("W"), PdfName.Width);
        inlineImageColorSpaceAbbreviationMap = new HashMap();
        inlineImageColorSpaceAbbreviationMap.put(new PdfName("G"), PdfName.DeviceGray);
        inlineImageColorSpaceAbbreviationMap.put(new PdfName("RGB"), PdfName.DeviceRGB);
        inlineImageColorSpaceAbbreviationMap.put(new PdfName("CMYK"), PdfName.DeviceCMYK);
        inlineImageColorSpaceAbbreviationMap.put(new PdfName("I"), PdfName.Indexed);
        inlineImageFilterAbbreviationMap = new HashMap();
        inlineImageFilterAbbreviationMap.put(new PdfName("AHx"), PdfName.ASCIIHexDecode);
        inlineImageFilterAbbreviationMap.put(new PdfName("A85"), PdfName.ASCII85Decode);
        inlineImageFilterAbbreviationMap.put(new PdfName("LZW"), PdfName.LZWDecode);
        inlineImageFilterAbbreviationMap.put(new PdfName("Fl"), PdfName.FlateDecode);
        inlineImageFilterAbbreviationMap.put(new PdfName("RL"), PdfName.RunLengthDecode);
        inlineImageFilterAbbreviationMap.put(new PdfName("CCF"), PdfName.CCITTFaxDecode);
        inlineImageFilterAbbreviationMap.put(new PdfName("DCT"), PdfName.DCTDecode);
    }

    private InlineImageParsingUtils() {
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/util/InlineImageParsingUtils$InlineImageParseException.class */
    public static class InlineImageParseException extends PdfException implements Serializable {
        private static final long serialVersionUID = 233760879000268548L;

        public InlineImageParseException(String message) {
            super(message);
        }
    }

    public static PdfStream parse(PdfCanvasParser ps, PdfDictionary colorSpaceDic) throws IOException {
        PdfDictionary inlineImageDict = parseDictionary(ps);
        byte[] samples = parseSamples(inlineImageDict, colorSpaceDic, ps);
        PdfStream inlineImageAsStreamObject = new PdfStream(samples);
        inlineImageAsStreamObject.putAll(inlineImageDict);
        return inlineImageAsStreamObject;
    }

    private static PdfDictionary parseDictionary(PdfCanvasParser ps) throws IOException {
        PdfDictionary dict = new PdfDictionary();
        PdfObject object = ps.readObject();
        while (true) {
            PdfObject key = object;
            if (key == null || "ID".equals(key.toString())) {
                break;
            }
            PdfObject value = ps.readObject();
            PdfName resolvedKey = inlineImageEntryAbbreviationMap.get((PdfName) key);
            if (resolvedKey == null) {
                resolvedKey = (PdfName) key;
            }
            dict.put(resolvedKey, getAlternateValue(resolvedKey, value));
            object = ps.readObject();
        }
        int ch2 = ps.getTokeniser().read();
        if (PdfTokenizer.isWhitespace(ch2)) {
            return dict;
        }
        throw new InlineImageParseException(PdfException.UnexpectedCharacter1FoundAfterIDInInlineImage).setMessageParams(Integer.valueOf(ch2));
    }

    private static PdfObject getAlternateValue(PdfName key, PdfObject value) {
        PdfName altValue;
        if (key == PdfName.Filter) {
            if (value instanceof PdfName) {
                PdfName altValue2 = inlineImageFilterAbbreviationMap.get((PdfName) value);
                if (altValue2 != null) {
                    return altValue2;
                }
            } else if (value instanceof PdfArray) {
                PdfArray array = (PdfArray) value;
                PdfArray altArray = new PdfArray();
                int count = array.size();
                for (int i = 0; i < count; i++) {
                    altArray.add(getAlternateValue(key, array.get(i)));
                }
                return altArray;
            }
        } else if (key == PdfName.ColorSpace && (value instanceof PdfName) && (altValue = inlineImageColorSpaceAbbreviationMap.get((PdfName) value)) != null) {
            return altValue;
        }
        return value;
    }

    private static int getComponentsPerPixel(PdfName colorSpaceName, PdfDictionary colorSpaceDic) {
        if (colorSpaceName == null || colorSpaceName.equals(PdfName.DeviceGray)) {
            return 1;
        }
        if (colorSpaceName.equals(PdfName.DeviceRGB)) {
            return 3;
        }
        if (colorSpaceName.equals(PdfName.DeviceCMYK)) {
            return 4;
        }
        if (colorSpaceDic != null) {
            PdfArray colorSpace = colorSpaceDic.getAsArray(colorSpaceName);
            if (colorSpace != null) {
                if (PdfName.Indexed.equals(colorSpace.getAsName(0))) {
                    return 1;
                }
            } else {
                PdfName tempName = colorSpaceDic.getAsName(colorSpaceName);
                if (tempName != null) {
                    return getComponentsPerPixel(tempName, colorSpaceDic);
                }
            }
        }
        throw new InlineImageParseException(PdfException.UnexpectedColorSpace1).setMessageParams(colorSpaceName);
    }

    private static int computeBytesPerRow(PdfDictionary imageDictionary, PdfDictionary colorSpaceDic) {
        PdfNumber wObj = imageDictionary.getAsNumber(PdfName.Width);
        PdfNumber bpcObj = imageDictionary.getAsNumber(PdfName.BitsPerComponent);
        int cpp = getComponentsPerPixel(imageDictionary.getAsName(PdfName.ColorSpace), colorSpaceDic);
        int w = wObj.intValue();
        int bpc = bpcObj != null ? bpcObj.intValue() : 1;
        return (((w * bpc) * cpp) + 7) / 8;
    }

    private static byte[] parseUnfilteredSamples(PdfDictionary imageDictionary, PdfDictionary colorSpaceDic, PdfCanvasParser ps) throws IOException {
        if (imageDictionary.containsKey(PdfName.Filter)) {
            throw new IllegalArgumentException("Dictionary contains filters");
        }
        PdfNumber h = imageDictionary.getAsNumber(PdfName.Height);
        int bytesToRead = computeBytesPerRow(imageDictionary, colorSpaceDic) * h.intValue();
        byte[] bytes = new byte[bytesToRead];
        PdfTokenizer tokeniser = ps.getTokeniser();
        int shouldBeWhiteSpace = tokeniser.read();
        int startIndex = 0;
        if (!PdfTokenizer.isWhitespace(shouldBeWhiteSpace) || shouldBeWhiteSpace == 0) {
            bytes[0] = (byte) shouldBeWhiteSpace;
            startIndex = 0 + 1;
        }
        for (int i = startIndex; i < bytesToRead; i++) {
            int ch2 = tokeniser.read();
            if (ch2 == -1) {
                throw new InlineImageParseException(PdfException.EndOfContentStreamReachedBeforeEndOfImageData);
            }
            bytes[i] = (byte) ch2;
        }
        PdfObject ei = ps.readObject();
        if (!"EI".equals(ei.toString())) {
            PdfObject ei2 = ps.readObject();
            if (!"EI".equals(ei2.toString())) {
                throw new InlineImageParseException(PdfException.OperatorEINotFoundAfterEndOfImageData);
            }
        }
        return bytes;
    }

    private static byte[] parseSamples(PdfDictionary imageDictionary, PdfDictionary colorSpaceDic, PdfCanvasParser ps) throws IOException {
        if (!imageDictionary.containsKey(PdfName.Filter) && imageColorSpaceIsKnown(imageDictionary, colorSpaceDic)) {
            return parseUnfilteredSamples(imageDictionary, colorSpaceDic, ps);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int found = 0;
        PdfTokenizer tokeniser = ps.getTokeniser();
        while (true) {
            int ch2 = tokeniser.read();
            if (ch2 != -1) {
                if (ch2 == 69) {
                    baos.write(EI, 0, found);
                    found = 1;
                } else if (found == 1 && ch2 == 73) {
                    found = 2;
                } else {
                    if (found == 2 && PdfTokenizer.isWhitespace(ch2)) {
                        byte[] tmp = baos.toByteArray();
                        if (inlineImageStreamBytesAreComplete(tmp, imageDictionary)) {
                            return tmp;
                        }
                    }
                    baos.write(EI, 0, found);
                    baos.write(ch2);
                    found = 0;
                }
            } else {
                throw new InlineImageParseException(PdfException.CannotFindImageDataOrEI);
            }
        }
    }

    private static boolean imageColorSpaceIsKnown(PdfDictionary imageDictionary, PdfDictionary colorSpaceDic) {
        PdfName cs = imageDictionary.getAsName(PdfName.ColorSpace);
        if (cs == null || cs.equals(PdfName.DeviceGray) || cs.equals(PdfName.DeviceRGB) || cs.equals(PdfName.DeviceCMYK)) {
            return true;
        }
        return colorSpaceDic != null && colorSpaceDic.containsKey(cs);
    }

    private static boolean inlineImageStreamBytesAreComplete(byte[] samples, PdfDictionary imageDictionary) {
        try {
            Map<PdfName, IFilterHandler> filters = new HashMap<>(FilterHandlers.getDefaultFilterHandlers());
            DoNothingFilter stubfilter = new DoNothingFilter();
            filters.put(PdfName.DCTDecode, stubfilter);
            filters.put(PdfName.JBIG2Decode, stubfilter);
            filters.put(PdfName.JPXDecode, stubfilter);
            filters.put(PdfName.FlateDecode, new FlateDecodeStrictFilter());
            PdfReader.decodeBytes(samples, imageDictionary, filters);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
