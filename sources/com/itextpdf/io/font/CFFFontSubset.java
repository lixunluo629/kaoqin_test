package com.itextpdf.io.font;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.drew.metadata.adobe.AdobeJpegReader;
import com.itextpdf.io.font.CFFFont;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.util.GenericArray;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.mysql.jdbc.MysqlErrorNumbers;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.boot.context.config.RandomValuePropertySource;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CFFFontSubset.class */
public class CFFFontSubset extends CFFFont {
    static final String[] SubrsFunctions;
    static final String[] SubrsEscapeFuncs;
    static final byte ENDCHAR_OP = 14;
    static final byte RETURN_OP = 11;
    Set<Integer> GlyphsUsed;
    List<Integer> glyphsInList;
    Set<Integer> FDArrayUsed;
    GenericArray<Set<Integer>> hSubrsUsed;
    GenericArray<List<Integer>> lSubrsUsed;
    Set<Integer> hGSubrsUsed;
    List<Integer> lGSubrsUsed;
    Set<Integer> hSubrsUsedNonCID;
    List<Integer> lSubrsUsedNonCID;
    byte[][] NewLSubrsIndex;
    byte[] NewSubrsIndexNonCID;
    byte[] NewGSubrsIndex;
    byte[] NewCharStringsIndex;
    int GBias;
    LinkedList<CFFFont.Item> OutputList;
    int NumOfHints;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CFFFontSubset.class.desiredAssertionStatus();
        SubrsFunctions = new String[]{"RESERVED_0", "hstem", "RESERVED_2", "vstem", "vmoveto", "rlineto", "hlineto", "vlineto", "rrcurveto", "RESERVED_9", "callsubr", "return", "escape", "RESERVED_13", "endchar", "RESERVED_15", "RESERVED_16", "RESERVED_17", "hstemhm", "hintmask", "cntrmask", "rmoveto", "hmoveto", "vstemhm", "rcurveline", "rlinecurve", "vvcurveto", "hhcurveto", "shortint", "callgsubr", "vhcurveto", "hvcurveto"};
        SubrsEscapeFuncs = new String[]{"RESERVED_0", "RESERVED_1", "RESERVED_2", "and", "or", "not", "RESERVED_6", "RESERVED_7", "RESERVED_8", "abs", BeanUtil.PREFIX_ADDER, Claims.SUBJECT, "div", "RESERVED_13", "neg", "eq", "RESERVED_16", "RESERVED_17", "drop", "RESERVED_19", "put", BeanUtil.PREFIX_GETTER_GET, "ifelse", RandomValuePropertySource.RANDOM_PROPERTY_SOURCE_NAME, "mul", "RESERVED_25", "sqrt", "dup", "exch", BeanDefinitionParserDelegate.INDEX_ATTRIBUTE, "roll", "RESERVED_31", "RESERVED_32", "RESERVED_33", "hflex", "flex", "hflex1", "flex1", "RESERVED_REST"};
    }

    public CFFFontSubset(byte[] cff, Set<Integer> GlyphsUsed) {
        super(cff);
        this.FDArrayUsed = new HashSet();
        this.hGSubrsUsed = new HashSet();
        this.lGSubrsUsed = new ArrayList();
        this.hSubrsUsedNonCID = new HashSet();
        this.lSubrsUsedNonCID = new ArrayList();
        this.GBias = 0;
        this.NumOfHints = 0;
        this.GlyphsUsed = GlyphsUsed;
        this.glyphsInList = new ArrayList(GlyphsUsed);
        for (int i = 0; i < this.fonts.length; i++) {
            seek(this.fonts[i].charstringsOffset);
            this.fonts[i].nglyphs = getCard16();
            seek(this.stringIndexOffset);
            this.fonts[i].nstrings = getCard16() + standardStrings.length;
            this.fonts[i].charstringsOffsets = getIndex(this.fonts[i].charstringsOffset);
            if (this.fonts[i].fdselectOffset >= 0) {
                readFDSelect(i);
                BuildFDArrayUsed(i);
            }
            if (this.fonts[i].isCID) {
                ReadFDArray(i);
            }
            this.fonts[i].CharsetLength = CountCharset(this.fonts[i].charsetOffset, this.fonts[i].nglyphs);
        }
    }

    int CountCharset(int Offset, int NumofGlyphs) {
        int Length = 0;
        seek(Offset);
        int format = getCard8();
        switch (format) {
            case 0:
                Length = 1 + (2 * NumofGlyphs);
                break;
            case 1:
                Length = 1 + (3 * CountRange(NumofGlyphs, 1));
                break;
            case 2:
                Length = 1 + (4 * CountRange(NumofGlyphs, 2));
                break;
        }
        return Length;
    }

    int CountRange(int NumofGlyphs, int Type) {
        int card16;
        int num = 0;
        int i = 1;
        while (true) {
            int i2 = i;
            if (i2 < NumofGlyphs) {
                num++;
                getCard16();
                if (Type == 1) {
                    card16 = getCard8();
                } else {
                    card16 = getCard16();
                }
                int nLeft = card16;
                i = i2 + nLeft + 1;
            } else {
                return num;
            }
        }
    }

    protected void readFDSelect(int Font) {
        int NumOfGlyphs = this.fonts[Font].nglyphs;
        int[] FDSelect = new int[NumOfGlyphs];
        seek(this.fonts[Font].fdselectOffset);
        this.fonts[Font].FDSelectFormat = getCard8();
        switch (this.fonts[Font].FDSelectFormat) {
            case 0:
                for (int i = 0; i < NumOfGlyphs; i++) {
                    FDSelect[i] = getCard8();
                }
                this.fonts[Font].FDSelectLength = this.fonts[Font].nglyphs + 1;
                break;
            case 3:
                int nRanges = getCard16();
                int l = 0;
                int first = getCard16();
                for (int i2 = 0; i2 < nRanges; i2++) {
                    int fd = getCard8();
                    int last = getCard16();
                    int steps = last - first;
                    for (int k = 0; k < steps; k++) {
                        FDSelect[l] = fd;
                        l++;
                    }
                    first = last;
                }
                this.fonts[Font].FDSelectLength = 3 + (nRanges * 3) + 2;
                break;
        }
        this.fonts[Font].FDSelect = FDSelect;
    }

    protected void BuildFDArrayUsed(int Font) {
        int[] FDSelect = this.fonts[Font].FDSelect;
        for (Integer glyphsInList1 : this.glyphsInList) {
            int glyph = glyphsInList1.intValue();
            int FD = FDSelect[glyph];
            this.FDArrayUsed.add(Integer.valueOf(FD));
        }
    }

    protected void ReadFDArray(int Font) {
        seek(this.fonts[Font].fdarrayOffset);
        this.fonts[Font].FDArrayCount = getCard16();
        this.fonts[Font].FDArrayOffsize = getCard8();
        if (this.fonts[Font].FDArrayOffsize < 4) {
            this.fonts[Font].FDArrayOffsize++;
        }
        this.fonts[Font].FDArrayOffsets = getIndex(this.fonts[Font].fdarrayOffset);
    }

    public byte[] Process(String fontName) {
        int j = 0;
        while (j < this.fonts.length && !fontName.equals(this.fonts[j].name)) {
            try {
                try {
                    j++;
                } catch (IOException e) {
                    throw new com.itextpdf.io.IOException("I/O exception.", (Throwable) e);
                }
            } finally {
                try {
                    this.buf.close();
                } catch (Exception e2) {
                }
            }
        }
        if (j == this.fonts.length) {
            return null;
        }
        if (this.gsubrIndexOffset >= 0) {
            this.GBias = CalcBias(this.gsubrIndexOffset, j);
        }
        BuildNewCharString(j);
        BuildNewLGSubrs(j);
        byte[] bArrBuildNewFile = BuildNewFile(j);
        try {
            this.buf.close();
        } catch (Exception e3) {
        }
        return bArrBuildNewFile;
    }

    public byte[] Process() {
        return Process(getNames()[0]);
    }

    protected int CalcBias(int Offset, int Font) {
        seek(Offset);
        int nSubrs = getCard16();
        if (this.fonts[Font].CharstringType == 1) {
            return 0;
        }
        if (nSubrs < 1240) {
            return 107;
        }
        if (nSubrs < 33900) {
            return MysqlErrorNumbers.ER_PASSWORD_ANONYMOUS_USER;
        }
        return 32768;
    }

    protected void BuildNewCharString(int FontIndex) throws IOException {
        this.NewCharStringsIndex = BuildNewIndex(this.fonts[FontIndex].charstringsOffsets, this.GlyphsUsed, (byte) 14);
    }

    /* JADX WARN: Type inference failed for: r1v19, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r1v33, types: [int[], int[][]] */
    protected void BuildNewLGSubrs(int Font) throws IOException {
        if (this.fonts[Font].isCID) {
            this.hSubrsUsed = new GenericArray<>(this.fonts[Font].fdprivateOffsets.length);
            this.lSubrsUsed = new GenericArray<>(this.fonts[Font].fdprivateOffsets.length);
            this.NewLSubrsIndex = new byte[this.fonts[Font].fdprivateOffsets.length];
            this.fonts[Font].PrivateSubrsOffset = new int[this.fonts[Font].fdprivateOffsets.length];
            this.fonts[Font].PrivateSubrsOffsetsArray = new int[this.fonts[Font].fdprivateOffsets.length];
            List<Integer> FDInList = new ArrayList<>(this.FDArrayUsed);
            for (int j = 0; j < FDInList.size(); j++) {
                int FD = FDInList.get(j).intValue();
                this.hSubrsUsed.set(FD, new HashSet());
                this.lSubrsUsed.set(FD, new ArrayList());
                BuildFDSubrsOffsets(Font, FD);
                if (this.fonts[Font].PrivateSubrsOffset[FD] >= 0) {
                    BuildSubrUsed(Font, FD, this.fonts[Font].PrivateSubrsOffset[FD], this.fonts[Font].PrivateSubrsOffsetsArray[FD], this.hSubrsUsed.get(FD), this.lSubrsUsed.get(FD));
                    this.NewLSubrsIndex[FD] = BuildNewIndex(this.fonts[Font].PrivateSubrsOffsetsArray[FD], this.hSubrsUsed.get(FD), (byte) 11);
                }
            }
        } else if (this.fonts[Font].privateSubrs >= 0) {
            this.fonts[Font].SubrsOffsets = getIndex(this.fonts[Font].privateSubrs);
            BuildSubrUsed(Font, -1, this.fonts[Font].privateSubrs, this.fonts[Font].SubrsOffsets, this.hSubrsUsedNonCID, this.lSubrsUsedNonCID);
        }
        BuildGSubrsUsed(Font);
        if (this.fonts[Font].privateSubrs >= 0) {
            this.NewSubrsIndexNonCID = BuildNewIndex(this.fonts[Font].SubrsOffsets, this.hSubrsUsedNonCID, (byte) 11);
        }
        this.NewGSubrsIndex = BuildNewIndexAndCopyAllGSubrs(this.gsubrOffsets, (byte) 11);
    }

    protected void BuildFDSubrsOffsets(int Font, int FD) {
        this.fonts[Font].PrivateSubrsOffset[FD] = -1;
        seek(this.fonts[Font].fdprivateOffsets[FD]);
        while (getPosition() < this.fonts[Font].fdprivateOffsets[FD] + this.fonts[Font].fdprivateLengths[FD]) {
            getDictItem();
            if ("Subrs".equals(this.key)) {
                this.fonts[Font].PrivateSubrsOffset[FD] = ((Integer) this.args[0]).intValue() + this.fonts[Font].fdprivateOffsets[FD];
            }
        }
        if (this.fonts[Font].PrivateSubrsOffset[FD] >= 0) {
            this.fonts[Font].PrivateSubrsOffsetsArray[FD] = getIndex(this.fonts[Font].PrivateSubrsOffset[FD]);
        }
    }

    protected void BuildSubrUsed(int Font, int FD, int SubrOffset, int[] SubrsOffsets, Set<Integer> hSubr, List<Integer> lSubr) {
        int LBias = CalcBias(SubrOffset, Font);
        for (int i = 0; i < this.glyphsInList.size(); i++) {
            int glyph = this.glyphsInList.get(i).intValue();
            int Start = this.fonts[Font].charstringsOffsets[glyph];
            int End = this.fonts[Font].charstringsOffsets[glyph + 1];
            if (FD >= 0) {
                EmptyStack();
                this.NumOfHints = 0;
                int GlyphFD = this.fonts[Font].FDSelect[glyph];
                if (GlyphFD == FD) {
                    ReadASubr(Start, End, this.GBias, LBias, hSubr, lSubr, SubrsOffsets);
                }
            } else {
                ReadASubr(Start, End, this.GBias, LBias, hSubr, lSubr, SubrsOffsets);
            }
        }
        for (int i2 = 0; i2 < lSubr.size(); i2++) {
            int Subr = lSubr.get(i2).intValue();
            if (Subr < SubrsOffsets.length - 1 && Subr >= 0) {
                ReadASubr(SubrsOffsets[Subr], SubrsOffsets[Subr + 1], this.GBias, LBias, hSubr, lSubr, SubrsOffsets);
            }
        }
    }

    protected void BuildGSubrsUsed(int Font) {
        int LBias = 0;
        int SizeOfNonCIDSubrsUsed = 0;
        if (this.fonts[Font].privateSubrs >= 0) {
            LBias = CalcBias(this.fonts[Font].privateSubrs, Font);
            SizeOfNonCIDSubrsUsed = this.lSubrsUsedNonCID.size();
        }
        for (int i = 0; i < this.lGSubrsUsed.size(); i++) {
            int Subr = this.lGSubrsUsed.get(i).intValue();
            if (Subr < this.gsubrOffsets.length - 1 && Subr >= 0) {
                int Start = this.gsubrOffsets[Subr];
                int End = this.gsubrOffsets[Subr + 1];
                if (this.fonts[Font].isCID) {
                    ReadASubr(Start, End, this.GBias, 0, this.hGSubrsUsed, this.lGSubrsUsed, null);
                } else {
                    ReadASubr(Start, End, this.GBias, LBias, this.hSubrsUsedNonCID, this.lSubrsUsedNonCID, this.fonts[Font].SubrsOffsets);
                    if (SizeOfNonCIDSubrsUsed < this.lSubrsUsedNonCID.size()) {
                        for (int j = SizeOfNonCIDSubrsUsed; j < this.lSubrsUsedNonCID.size(); j++) {
                            int LSubr = this.lSubrsUsedNonCID.get(j).intValue();
                            if (LSubr < this.fonts[Font].SubrsOffsets.length - 1 && LSubr >= 0) {
                                int LStart = this.fonts[Font].SubrsOffsets[LSubr];
                                int LEnd = this.fonts[Font].SubrsOffsets[LSubr + 1];
                                ReadASubr(LStart, LEnd, this.GBias, LBias, this.hSubrsUsedNonCID, this.lSubrsUsedNonCID, this.fonts[Font].SubrsOffsets);
                            }
                        }
                        SizeOfNonCIDSubrsUsed = this.lSubrsUsedNonCID.size();
                    }
                }
            }
        }
    }

    protected void ReadASubr(int begin, int end, int GBias, int LBias, Set<Integer> hSubr, List<Integer> lSubr, int[] LSubrsOffsets) {
        EmptyStack();
        this.NumOfHints = 0;
        seek(begin);
        while (getPosition() < end) {
            ReadCommand();
            int pos = getPosition();
            Object TopElement = null;
            if (this.arg_count > 0) {
                TopElement = this.args[this.arg_count - 1];
            }
            int NumOfArgs = this.arg_count;
            HandelStack();
            if (null != this.key) {
                switch (this.key) {
                    case "callsubr":
                        if (NumOfArgs > 0) {
                            int Subr = ((Integer) TopElement).intValue() + LBias;
                            if (!hSubr.contains(Integer.valueOf(Subr))) {
                                hSubr.add(Integer.valueOf(Subr));
                                lSubr.add(Integer.valueOf(Subr));
                            }
                            CalcHints(LSubrsOffsets[Subr], LSubrsOffsets[Subr + 1], LBias, GBias, LSubrsOffsets);
                            seek(pos);
                            break;
                        } else {
                            break;
                        }
                    case "callgsubr":
                        if (NumOfArgs > 0) {
                            int Subr2 = ((Integer) TopElement).intValue() + GBias;
                            if (!this.hGSubrsUsed.contains(Integer.valueOf(Subr2))) {
                                this.hGSubrsUsed.add(Integer.valueOf(Subr2));
                                this.lGSubrsUsed.add(Integer.valueOf(Subr2));
                            }
                            CalcHints(this.gsubrOffsets[Subr2], this.gsubrOffsets[Subr2 + 1], LBias, GBias, LSubrsOffsets);
                            seek(pos);
                            break;
                        } else {
                            break;
                        }
                    case "hstem":
                    case "vstem":
                    case "hstemhm":
                    case "vstemhm":
                        this.NumOfHints += NumOfArgs / 2;
                        break;
                    case "hintmask":
                    case "cntrmask":
                        this.NumOfHints += NumOfArgs / 2;
                        int SizeOfMask = this.NumOfHints / 8;
                        if (this.NumOfHints % 8 != 0 || SizeOfMask == 0) {
                            SizeOfMask++;
                        }
                        for (int i = 0; i < SizeOfMask; i++) {
                            getCard8();
                        }
                        break;
                }
            }
        }
    }

    protected void HandelStack() {
        int StackHandel = StackOpp();
        if (StackHandel < 2) {
            if (StackHandel == 1) {
                PushStack();
                return;
            }
            int StackHandel2 = StackHandel * (-1);
            for (int i = 0; i < StackHandel2; i++) {
                PopStack();
            }
            return;
        }
        EmptyStack();
    }

    protected int StackOpp() {
        switch (this.key) {
            case "ifelse":
                return -3;
            case "roll":
            case "put":
                return -2;
            case "callsubr":
            case "callgsubr":
            case "add":
            case "sub":
            case "div":
            case "mul":
            case "drop":
            case "and":
            case "or":
            case "eq":
                return -1;
            case "abs":
            case "neg":
            case "sqrt":
            case "exch":
            case "index":
            case "get":
            case "not":
            case "return":
                return 0;
            case "random":
            case "dup":
                return 1;
            default:
                return 2;
        }
    }

    protected void EmptyStack() {
        for (int i = 0; i < this.arg_count; i++) {
            this.args[i] = null;
        }
        this.arg_count = 0;
    }

    protected void PopStack() {
        if (this.arg_count > 0) {
            this.args[this.arg_count - 1] = null;
            this.arg_count--;
        }
    }

    protected void PushStack() {
        this.arg_count++;
    }

    protected void ReadCommand() {
        this.key = null;
        boolean gotKey = false;
        while (!gotKey) {
            char b0 = getCard8();
            if (b0 == 28) {
                int first = getCard8();
                int second = getCard8();
                this.args[this.arg_count] = Integer.valueOf((first << 8) | second);
                this.arg_count++;
            } else if (b0 >= ' ' && b0 <= 246) {
                this.args[this.arg_count] = Integer.valueOf(b0 - 139);
                this.arg_count++;
            } else if (b0 >= 247 && b0 <= 250) {
                int w = getCard8();
                this.args[this.arg_count] = Integer.valueOf(((b0 - 247) * 256) + w + 108);
                this.arg_count++;
            } else if (b0 >= 251 && b0 <= 254) {
                int w2 = getCard8();
                this.args[this.arg_count] = Integer.valueOf((((-(b0 - 251)) * 256) - w2) - 108);
                this.arg_count++;
            } else if (b0 == 255) {
                int first2 = getCard8();
                int second2 = getCard8();
                int third = getCard8();
                int fourth = getCard8();
                this.args[this.arg_count] = Integer.valueOf((first2 << 24) | (second2 << 16) | (third << 8) | fourth);
                this.arg_count++;
            } else if (b0 <= 31 && b0 != 28) {
                gotKey = true;
                if (b0 == '\f') {
                    int b1 = getCard8();
                    if (b1 > SubrsEscapeFuncs.length - 1) {
                        b1 = SubrsEscapeFuncs.length - 1;
                    }
                    this.key = SubrsEscapeFuncs[b1];
                } else {
                    this.key = SubrsFunctions[b0];
                }
            }
        }
    }

    protected int CalcHints(int begin, int end, int LBias, int GBias, int[] LSubrsOffsets) {
        int pos;
        Object TopElement;
        int NumOfArgs;
        seek(begin);
        while (getPosition() < end) {
            ReadCommand();
            pos = getPosition();
            TopElement = null;
            if (this.arg_count > 0) {
                TopElement = this.args[this.arg_count - 1];
            }
            NumOfArgs = this.arg_count;
            HandelStack();
            switch (this.key) {
                case "callsubr":
                    if (NumOfArgs > 0) {
                        if (!$assertionsDisabled && !(TopElement instanceof Integer)) {
                            throw new AssertionError();
                        }
                        int Subr = ((Integer) TopElement).intValue() + LBias;
                        CalcHints(LSubrsOffsets[Subr], LSubrsOffsets[Subr + 1], LBias, GBias, LSubrsOffsets);
                        seek(pos);
                        break;
                    } else {
                        continue;
                    }
                    break;
                case "callgsubr":
                    if (NumOfArgs > 0) {
                        if (!$assertionsDisabled && !(TopElement instanceof Integer)) {
                            throw new AssertionError();
                        }
                        int Subr2 = ((Integer) TopElement).intValue() + GBias;
                        CalcHints(this.gsubrOffsets[Subr2], this.gsubrOffsets[Subr2 + 1], LBias, GBias, LSubrsOffsets);
                        seek(pos);
                        break;
                    } else {
                        continue;
                    }
                    break;
                case "hstem":
                case "vstem":
                case "hstemhm":
                case "vstemhm":
                    this.NumOfHints += NumOfArgs / 2;
                    break;
                case "hintmask":
                case "cntrmask":
                    int SizeOfMask = this.NumOfHints / 8;
                    if (this.NumOfHints % 8 != 0 || SizeOfMask == 0) {
                        SizeOfMask++;
                    }
                    for (int i = 0; i < SizeOfMask; i++) {
                        getCard8();
                    }
                    break;
            }
        }
        return this.NumOfHints;
    }

    protected byte[] BuildNewIndex(int[] Offsets, Set<Integer> Used, byte OperatorForUnusedEntries) throws IOException {
        int unusedCount = 0;
        int Offset = 0;
        int[] NewOffsets = new int[Offsets.length];
        for (int i = 0; i < Offsets.length; i++) {
            NewOffsets[i] = Offset;
            if (Used.contains(Integer.valueOf(i))) {
                Offset += Offsets[i + 1] - Offsets[i];
            } else {
                unusedCount++;
            }
        }
        byte[] NewObjects = new byte[Offset + unusedCount];
        int unusedOffset = 0;
        for (int i2 = 0; i2 < Offsets.length - 1; i2++) {
            int start = NewOffsets[i2];
            int end = NewOffsets[i2 + 1];
            NewOffsets[i2] = start + unusedOffset;
            if (start != end) {
                this.buf.seek(Offsets[i2]);
                this.buf.readFully(NewObjects, start + unusedOffset, end - start);
            } else {
                NewObjects[start + unusedOffset] = OperatorForUnusedEntries;
                unusedOffset++;
            }
        }
        int length = Offsets.length - 1;
        NewOffsets[length] = NewOffsets[length] + unusedOffset;
        return AssembleIndex(NewOffsets, NewObjects);
    }

    protected byte[] BuildNewIndexAndCopyAllGSubrs(int[] Offsets, byte OperatorForUnusedEntries) throws IOException {
        int Offset = 0;
        int[] NewOffsets = new int[Offsets.length];
        for (int i = 0; i < Offsets.length - 1; i++) {
            NewOffsets[i] = Offset;
            Offset += Offsets[i + 1] - Offsets[i];
        }
        NewOffsets[Offsets.length - 1] = Offset;
        int unusedCount = 0 + 1;
        byte[] NewObjects = new byte[Offset + unusedCount];
        int unusedOffset = 0;
        for (int i2 = 0; i2 < Offsets.length - 1; i2++) {
            int start = NewOffsets[i2];
            int end = NewOffsets[i2 + 1];
            NewOffsets[i2] = start + unusedOffset;
            if (start != end) {
                this.buf.seek(Offsets[i2]);
                this.buf.readFully(NewObjects, start + unusedOffset, end - start);
            } else {
                NewObjects[start + unusedOffset] = OperatorForUnusedEntries;
                unusedOffset++;
            }
        }
        int length = Offsets.length - 1;
        NewOffsets[length] = NewOffsets[length] + unusedOffset;
        return AssembleIndex(NewOffsets, NewObjects);
    }

    protected byte[] AssembleIndex(int[] NewOffsets, byte[] NewObjects) {
        byte Offsize;
        char Count = (char) (NewOffsets.length - 1);
        int Size = NewOffsets[NewOffsets.length - 1];
        if (Size < 255) {
            Offsize = 1;
        } else if (Size < 65535) {
            Offsize = 2;
        } else if (Size < 16777215) {
            Offsize = 3;
        } else {
            Offsize = 4;
        }
        byte[] NewIndex = new byte[3 + (Offsize * (Count + 1)) + NewObjects.length];
        int Place = 0 + 1;
        NewIndex[0] = (byte) ((Count >> '\b') & 255);
        int Place2 = Place + 1;
        NewIndex[Place] = (byte) (Count & 255);
        int Place3 = Place2 + 1;
        NewIndex[Place2] = Offsize;
        for (int newOffset : NewOffsets) {
            int Num = (newOffset - NewOffsets[0]) + 1;
            for (int i = Offsize; i > 0; i--) {
                int i2 = Place3;
                Place3++;
                NewIndex[i2] = (byte) ((Num >>> ((i - 1) << 3)) & 255);
            }
        }
        for (byte newObject : NewObjects) {
            int i3 = Place3;
            Place3++;
            NewIndex[i3] = newObject;
        }
        return NewIndex;
    }

    protected byte[] BuildNewFile(int Font) {
        this.OutputList = new LinkedList<>();
        CopyHeader();
        BuildIndexHeader(1, 1, 1);
        this.OutputList.addLast(new CFFFont.UInt8Item((char) (1 + this.fonts[Font].name.length())));
        this.OutputList.addLast(new CFFFont.StringItem(this.fonts[Font].name));
        BuildIndexHeader(1, 2, 1);
        CFFFont.OffsetItem topdictIndex1Ref = new CFFFont.IndexOffsetItem(2);
        this.OutputList.addLast(topdictIndex1Ref);
        CFFFont.IndexBaseItem topdictBase = new CFFFont.IndexBaseItem();
        this.OutputList.addLast(topdictBase);
        CFFFont.OffsetItem charsetRef = new CFFFont.DictOffsetItem();
        CFFFont.OffsetItem charstringsRef = new CFFFont.DictOffsetItem();
        CFFFont.OffsetItem fdarrayRef = new CFFFont.DictOffsetItem();
        CFFFont.OffsetItem fdselectRef = new CFFFont.DictOffsetItem();
        CFFFont.OffsetItem privateRef = new CFFFont.DictOffsetItem();
        if (!this.fonts[Font].isCID) {
            this.OutputList.addLast(new CFFFont.DictNumberItem(this.fonts[Font].nstrings));
            this.OutputList.addLast(new CFFFont.DictNumberItem(this.fonts[Font].nstrings + 1));
            this.OutputList.addLast(new CFFFont.DictNumberItem(0));
            this.OutputList.addLast(new CFFFont.UInt8Item('\f'));
            this.OutputList.addLast(new CFFFont.UInt8Item((char) 30));
            this.OutputList.addLast(new CFFFont.DictNumberItem(this.fonts[Font].nglyphs));
            this.OutputList.addLast(new CFFFont.UInt8Item('\f'));
            this.OutputList.addLast(new CFFFont.UInt8Item('\"'));
        }
        seek(this.topdictOffsets[Font]);
        while (getPosition() < this.topdictOffsets[Font + 1]) {
            int p1 = getPosition();
            getDictItem();
            int p2 = getPosition();
            if (!"Encoding".equals(this.key) && !StandardRoles.PRIVATE.equals(this.key) && !"FDSelect".equals(this.key) && !"FDArray".equals(this.key) && !"charset".equals(this.key) && !"CharStrings".equals(this.key)) {
                this.OutputList.addLast(new CFFFont.RangeItem(this.buf, p1, p2 - p1));
            }
        }
        CreateKeys(fdarrayRef, fdselectRef, charsetRef, charstringsRef);
        this.OutputList.addLast(new CFFFont.IndexMarkerItem(topdictIndex1Ref, topdictBase));
        if (this.fonts[Font].isCID) {
            this.OutputList.addLast(getEntireIndexRange(this.stringIndexOffset));
        } else {
            CreateNewStringIndex(Font);
        }
        this.OutputList.addLast(new CFFFont.RangeItem(new RandomAccessFileOrArray(this.rasFactory.createSource(this.NewGSubrsIndex)), 0, this.NewGSubrsIndex.length));
        if (this.fonts[Font].isCID) {
            this.OutputList.addLast(new CFFFont.MarkerItem(fdselectRef));
            if (this.fonts[Font].fdselectOffset >= 0) {
                this.OutputList.addLast(new CFFFont.RangeItem(this.buf, this.fonts[Font].fdselectOffset, this.fonts[Font].FDSelectLength));
            } else {
                CreateFDSelect(fdselectRef, this.fonts[Font].nglyphs);
            }
            this.OutputList.addLast(new CFFFont.MarkerItem(charsetRef));
            this.OutputList.addLast(new CFFFont.RangeItem(this.buf, this.fonts[Font].charsetOffset, this.fonts[Font].CharsetLength));
            if (this.fonts[Font].fdarrayOffset >= 0) {
                this.OutputList.addLast(new CFFFont.MarkerItem(fdarrayRef));
                Reconstruct(Font);
            } else {
                CreateFDArray(fdarrayRef, privateRef, Font);
            }
        } else {
            CreateFDSelect(fdselectRef, this.fonts[Font].nglyphs);
            CreateCharset(charsetRef, this.fonts[Font].nglyphs);
            CreateFDArray(fdarrayRef, privateRef, Font);
        }
        if (this.fonts[Font].privateOffset >= 0) {
            CFFFont.IndexBaseItem PrivateBase = new CFFFont.IndexBaseItem();
            this.OutputList.addLast(PrivateBase);
            this.OutputList.addLast(new CFFFont.MarkerItem(privateRef));
            CFFFont.OffsetItem Subr = new CFFFont.DictOffsetItem();
            CreateNonCIDPrivate(Font, Subr);
            CreateNonCIDSubrs(Font, PrivateBase, Subr);
        }
        this.OutputList.addLast(new CFFFont.MarkerItem(charstringsRef));
        this.OutputList.addLast(new CFFFont.RangeItem(new RandomAccessFileOrArray(this.rasFactory.createSource(this.NewCharStringsIndex)), 0, this.NewCharStringsIndex.length));
        int[] currentOffset = {0};
        Iterator<CFFFont.Item> it = this.OutputList.iterator();
        while (it.hasNext()) {
            CFFFont.Item item = it.next();
            item.increment(currentOffset);
        }
        Iterator<CFFFont.Item> it2 = this.OutputList.iterator();
        while (it2.hasNext()) {
            CFFFont.Item item2 = it2.next();
            item2.xref();
        }
        int size = currentOffset[0];
        byte[] b = new byte[size];
        Iterator<CFFFont.Item> it3 = this.OutputList.iterator();
        while (it3.hasNext()) {
            CFFFont.Item item3 = it3.next();
            item3.emit(b);
        }
        return b;
    }

    protected void CopyHeader() {
        seek(0);
        getCard8();
        getCard8();
        int hdrSize = getCard8();
        getCard8();
        this.nextIndexOffset = hdrSize;
        this.OutputList.addLast(new CFFFont.RangeItem(this.buf, 0, hdrSize));
    }

    protected void BuildIndexHeader(int Count, int Offsize, int First) {
        this.OutputList.addLast(new CFFFont.UInt16Item((char) Count));
        this.OutputList.addLast(new CFFFont.UInt8Item((char) Offsize));
        switch (Offsize) {
            case 1:
                this.OutputList.addLast(new CFFFont.UInt8Item((char) First));
                break;
            case 2:
                this.OutputList.addLast(new CFFFont.UInt16Item((char) First));
                break;
            case 3:
                this.OutputList.addLast(new CFFFont.UInt24Item((char) First));
                break;
            case 4:
                this.OutputList.addLast(new CFFFont.UInt32Item((char) First));
                break;
        }
    }

    protected void CreateKeys(CFFFont.OffsetItem fdarrayRef, CFFFont.OffsetItem fdselectRef, CFFFont.OffsetItem charsetRef, CFFFont.OffsetItem charstringsRef) {
        this.OutputList.addLast(fdarrayRef);
        this.OutputList.addLast(new CFFFont.UInt8Item('\f'));
        this.OutputList.addLast(new CFFFont.UInt8Item('$'));
        this.OutputList.addLast(fdselectRef);
        this.OutputList.addLast(new CFFFont.UInt8Item('\f'));
        this.OutputList.addLast(new CFFFont.UInt8Item('%'));
        this.OutputList.addLast(charsetRef);
        this.OutputList.addLast(new CFFFont.UInt8Item((char) 15));
        this.OutputList.addLast(charstringsRef);
        this.OutputList.addLast(new CFFFont.UInt8Item((char) 17));
    }

    protected void CreateNewStringIndex(int Font) {
        byte stringsIndexOffSize;
        String fdFontName = this.fonts[Font].name + "-OneRange";
        if (fdFontName.length() > 127) {
            fdFontName = fdFontName.substring(0, 127);
        }
        String extraStrings = "AdobeIdentity" + fdFontName;
        int origStringsLen = this.stringOffsets[this.stringOffsets.length - 1] - this.stringOffsets[0];
        int stringsBaseOffset = this.stringOffsets[0] - 1;
        if (origStringsLen + extraStrings.length() <= 255) {
            stringsIndexOffSize = 1;
        } else if (origStringsLen + extraStrings.length() <= 65535) {
            stringsIndexOffSize = 2;
        } else {
            stringsIndexOffSize = origStringsLen + extraStrings.length() <= 16777215 ? (byte) 3 : (byte) 4;
        }
        this.OutputList.addLast(new CFFFont.UInt16Item((char) ((this.stringOffsets.length - 1) + 3)));
        this.OutputList.addLast(new CFFFont.UInt8Item((char) stringsIndexOffSize));
        for (int stringOffset : this.stringOffsets) {
            this.OutputList.addLast(new CFFFont.IndexOffsetItem(stringsIndexOffSize, stringOffset - stringsBaseOffset));
        }
        int currentStringsOffset = (this.stringOffsets[this.stringOffsets.length - 1] - stringsBaseOffset) + AdobeJpegReader.PREAMBLE.length();
        this.OutputList.addLast(new CFFFont.IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
        int currentStringsOffset2 = currentStringsOffset + "Identity".length();
        this.OutputList.addLast(new CFFFont.IndexOffsetItem(stringsIndexOffSize, currentStringsOffset2));
        this.OutputList.addLast(new CFFFont.IndexOffsetItem(stringsIndexOffSize, currentStringsOffset2 + fdFontName.length()));
        this.OutputList.addLast(new CFFFont.RangeItem(this.buf, this.stringOffsets[0], origStringsLen));
        this.OutputList.addLast(new CFFFont.StringItem(extraStrings));
    }

    protected void CreateFDSelect(CFFFont.OffsetItem fdselectRef, int nglyphs) {
        this.OutputList.addLast(new CFFFont.MarkerItem(fdselectRef));
        this.OutputList.addLast(new CFFFont.UInt8Item((char) 3));
        this.OutputList.addLast(new CFFFont.UInt16Item((char) 1));
        this.OutputList.addLast(new CFFFont.UInt16Item((char) 0));
        this.OutputList.addLast(new CFFFont.UInt8Item((char) 0));
        this.OutputList.addLast(new CFFFont.UInt16Item((char) nglyphs));
    }

    protected void CreateCharset(CFFFont.OffsetItem charsetRef, int nglyphs) {
        this.OutputList.addLast(new CFFFont.MarkerItem(charsetRef));
        this.OutputList.addLast(new CFFFont.UInt8Item((char) 2));
        this.OutputList.addLast(new CFFFont.UInt16Item((char) 1));
        this.OutputList.addLast(new CFFFont.UInt16Item((char) (nglyphs - 1)));
    }

    protected void CreateFDArray(CFFFont.OffsetItem fdarrayRef, CFFFont.OffsetItem privateRef, int Font) {
        this.OutputList.addLast(new CFFFont.MarkerItem(fdarrayRef));
        BuildIndexHeader(1, 1, 1);
        CFFFont.OffsetItem privateIndex1Ref = new CFFFont.IndexOffsetItem(1);
        this.OutputList.addLast(privateIndex1Ref);
        CFFFont.IndexBaseItem privateBase = new CFFFont.IndexBaseItem();
        this.OutputList.addLast(privateBase);
        int NewSize = this.fonts[Font].privateLength;
        int OrgSubrsOffsetSize = CalcSubrOffsetSize(this.fonts[Font].privateOffset, this.fonts[Font].privateLength);
        if (OrgSubrsOffsetSize != 0) {
            NewSize += 5 - OrgSubrsOffsetSize;
        }
        this.OutputList.addLast(new CFFFont.DictNumberItem(NewSize));
        this.OutputList.addLast(privateRef);
        this.OutputList.addLast(new CFFFont.UInt8Item((char) 18));
        this.OutputList.addLast(new CFFFont.IndexMarkerItem(privateIndex1Ref, privateBase));
    }

    void Reconstruct(int Font) {
        CFFFont.OffsetItem[] fdPrivate = new CFFFont.DictOffsetItem[this.fonts[Font].FDArrayOffsets.length - 1];
        CFFFont.IndexBaseItem[] fdPrivateBase = new CFFFont.IndexBaseItem[this.fonts[Font].fdprivateOffsets.length];
        CFFFont.OffsetItem[] fdSubrs = new CFFFont.DictOffsetItem[this.fonts[Font].fdprivateOffsets.length];
        ReconstructFDArray(Font, fdPrivate);
        ReconstructPrivateDict(Font, fdPrivate, fdPrivateBase, fdSubrs);
        ReconstructPrivateSubrs(Font, fdPrivateBase, fdSubrs);
    }

    void ReconstructFDArray(int Font, CFFFont.OffsetItem[] fdPrivate) {
        BuildIndexHeader(this.fonts[Font].FDArrayCount, this.fonts[Font].FDArrayOffsize, 1);
        CFFFont.OffsetItem[] fdOffsets = new CFFFont.IndexOffsetItem[this.fonts[Font].FDArrayOffsets.length - 1];
        for (int i = 0; i < this.fonts[Font].FDArrayOffsets.length - 1; i++) {
            fdOffsets[i] = new CFFFont.IndexOffsetItem(this.fonts[Font].FDArrayOffsize);
            this.OutputList.addLast(fdOffsets[i]);
        }
        CFFFont.IndexBaseItem fdArrayBase = new CFFFont.IndexBaseItem();
        this.OutputList.addLast(fdArrayBase);
        for (int k = 0; k < this.fonts[Font].FDArrayOffsets.length - 1; k++) {
            seek(this.fonts[Font].FDArrayOffsets[k]);
            while (getPosition() < this.fonts[Font].FDArrayOffsets[k + 1]) {
                int p1 = getPosition();
                getDictItem();
                int p2 = getPosition();
                if (StandardRoles.PRIVATE.equals(this.key)) {
                    int NewSize = ((Integer) this.args[0]).intValue();
                    int OrgSubrsOffsetSize = CalcSubrOffsetSize(this.fonts[Font].fdprivateOffsets[k], this.fonts[Font].fdprivateLengths[k]);
                    if (OrgSubrsOffsetSize != 0) {
                        NewSize += 5 - OrgSubrsOffsetSize;
                    }
                    this.OutputList.addLast(new CFFFont.DictNumberItem(NewSize));
                    fdPrivate[k] = new CFFFont.DictOffsetItem();
                    this.OutputList.addLast(fdPrivate[k]);
                    this.OutputList.addLast(new CFFFont.UInt8Item((char) 18));
                    seek(p2);
                } else {
                    this.OutputList.addLast(new CFFFont.RangeItem(this.buf, p1, p2 - p1));
                }
            }
            this.OutputList.addLast(new CFFFont.IndexMarkerItem(fdOffsets[k], fdArrayBase));
        }
    }

    void ReconstructPrivateDict(int Font, CFFFont.OffsetItem[] fdPrivate, CFFFont.IndexBaseItem[] fdPrivateBase, CFFFont.OffsetItem[] fdSubrs) {
        for (int i = 0; i < this.fonts[Font].fdprivateOffsets.length; i++) {
            this.OutputList.addLast(new CFFFont.MarkerItem(fdPrivate[i]));
            fdPrivateBase[i] = new CFFFont.IndexBaseItem();
            this.OutputList.addLast(fdPrivateBase[i]);
            seek(this.fonts[Font].fdprivateOffsets[i]);
            while (getPosition() < this.fonts[Font].fdprivateOffsets[i] + this.fonts[Font].fdprivateLengths[i]) {
                int p1 = getPosition();
                getDictItem();
                int p2 = getPosition();
                if ("Subrs".equals(this.key)) {
                    fdSubrs[i] = new CFFFont.DictOffsetItem();
                    this.OutputList.addLast(fdSubrs[i]);
                    this.OutputList.addLast(new CFFFont.UInt8Item((char) 19));
                } else {
                    this.OutputList.addLast(new CFFFont.RangeItem(this.buf, p1, p2 - p1));
                }
            }
        }
    }

    void ReconstructPrivateSubrs(int Font, CFFFont.IndexBaseItem[] fdPrivateBase, CFFFont.OffsetItem[] fdSubrs) {
        for (int i = 0; i < this.fonts[Font].fdprivateLengths.length; i++) {
            if (fdSubrs[i] != null && this.fonts[Font].PrivateSubrsOffset[i] >= 0) {
                this.OutputList.addLast(new CFFFont.SubrMarkerItem(fdSubrs[i], fdPrivateBase[i]));
                if (this.NewLSubrsIndex[i] != null) {
                    this.OutputList.addLast(new CFFFont.RangeItem(new RandomAccessFileOrArray(this.rasFactory.createSource(this.NewLSubrsIndex[i])), 0, this.NewLSubrsIndex[i].length));
                }
            }
        }
    }

    int CalcSubrOffsetSize(int Offset, int Size) {
        int OffsetSize = 0;
        seek(Offset);
        while (getPosition() < Offset + Size) {
            int p1 = getPosition();
            getDictItem();
            int p2 = getPosition();
            if ("Subrs".equals(this.key)) {
                OffsetSize = (p2 - p1) - 1;
            }
        }
        return OffsetSize;
    }

    protected int countEntireIndexRange(int indexOffset) {
        seek(indexOffset);
        int count = getCard16();
        if (count == 0) {
            return 2;
        }
        int indexOffSize = getCard8();
        seek(indexOffset + 2 + 1 + (count * indexOffSize));
        int size = getOffset(indexOffSize) - 1;
        return 3 + ((count + 1) * indexOffSize) + size;
    }

    void CreateNonCIDPrivate(int Font, CFFFont.OffsetItem Subr) {
        seek(this.fonts[Font].privateOffset);
        while (getPosition() < this.fonts[Font].privateOffset + this.fonts[Font].privateLength) {
            int p1 = getPosition();
            getDictItem();
            int p2 = getPosition();
            if ("Subrs".equals(this.key)) {
                this.OutputList.addLast(Subr);
                this.OutputList.addLast(new CFFFont.UInt8Item((char) 19));
            } else {
                this.OutputList.addLast(new CFFFont.RangeItem(this.buf, p1, p2 - p1));
            }
        }
    }

    void CreateNonCIDSubrs(int Font, CFFFont.IndexBaseItem PrivateBase, CFFFont.OffsetItem Subrs) {
        this.OutputList.addLast(new CFFFont.SubrMarkerItem(Subrs, PrivateBase));
        if (this.NewSubrsIndexNonCID != null) {
            this.OutputList.addLast(new CFFFont.RangeItem(new RandomAccessFileOrArray(this.rasFactory.createSource(this.NewSubrsIndexNonCID)), 0, this.NewSubrsIndexNonCID.length));
        }
    }
}
