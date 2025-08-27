package org.apache.poi.xssf.binary;

import com.drew.metadata.iptc.IptcDirectory;
import com.itextpdf.kernel.pdf.canvas.wmf.MetaDo;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.util.Internal;

@Internal
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/binary/XSSFBRecordType.class */
public enum XSSFBRecordType {
    BrtCellBlank(1),
    BrtCellRk(2),
    BrtCellError(3),
    BrtCellBool(4),
    BrtCellReal(5),
    BrtCellSt(6),
    BrtCellIsst(7),
    BrtFmlaString(8),
    BrtFmlaNum(9),
    BrtFmlaBool(10),
    BrtFmlaError(11),
    BrtRowHdr(0),
    BrtCellRString(62),
    BrtBeginSheet(129),
    BrtWsProp(147),
    BrtWsDim(148),
    BrtColInfo(60),
    BrtBeginSheetData(145),
    BrtEndSheetData(146),
    BrtHLink(494),
    BrtBeginHeaderFooter(479),
    BrtBeginCommentAuthors(IptcDirectory.TAG_CONTACT),
    BrtEndCommentAuthors(631),
    BrtCommentAuthor(IptcDirectory.TAG_CAPTION),
    BrtBeginComment(635),
    BrtCommentText(IptcDirectory.TAG_RASTERIZED_CAPTION),
    BrtEndComment(636),
    BrtXf(47),
    BrtFmt(44),
    BrtBeginFmts(IptcDirectory.TAG_ORIGINAL_TRANSMISSION_REFERENCE),
    BrtEndFmts(616),
    BrtBeginCellXFs(IptcDirectory.TAG_HEADLINE),
    BrtEndCellXFs(618),
    BrtBeginCellStyleXFS(626),
    BrtEndCellStyleXFS(IptcDirectory.TAG_SOURCE),
    BrtSstItem(19),
    BrtBeginSst(159),
    BrtEndSst(160),
    BrtBundleSh(156),
    BrtAbsPath15(MetaDo.META_ARC),
    Unimplemented(-1);

    private static final Map<Integer, XSSFBRecordType> TYPE_MAP = new HashMap();
    private final int id;

    static {
        XSSFBRecordType[] arr$ = values();
        for (XSSFBRecordType type : arr$) {
            TYPE_MAP.put(Integer.valueOf(type.getId()), type);
        }
    }

    XSSFBRecordType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static XSSFBRecordType lookup(int id) {
        XSSFBRecordType type = TYPE_MAP.get(Integer.valueOf(id));
        if (type == null) {
            return Unimplemented;
        }
        return type;
    }
}
