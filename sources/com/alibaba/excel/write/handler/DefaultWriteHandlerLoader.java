package com.alibaba.excel.write.handler;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.IndexedColors;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/handler/DefaultWriteHandlerLoader.class */
public class DefaultWriteHandlerLoader {
    public static List<WriteHandler> loadDefaultHandler(Boolean useDefaultStyle) {
        List<WriteHandler> handlerList = new ArrayList<>();
        if (useDefaultStyle.booleanValue()) {
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            headWriteCellStyle.setFillForegroundColor(Short.valueOf(IndexedColors.GREY_25_PERCENT.getIndex()));
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontName("宋体");
            headWriteFont.setFontHeightInPoints((short) 14);
            headWriteFont.setBold(true);
            headWriteCellStyle.setWriteFont(headWriteFont);
            handlerList.add(new HorizontalCellStyleStrategy(headWriteCellStyle, new ArrayList()));
        }
        return handlerList;
    }
}
