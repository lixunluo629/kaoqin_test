package com.alibaba.excel.util;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.read.metadata.ReadSheet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/util/SheetUtils.class */
public class SheetUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) SheetUtils.class);

    private SheetUtils() {
    }

    public static ReadSheet match(ReadSheet readSheet, List<ReadSheet> parameterReadSheetList, Boolean readAll, GlobalConfiguration globalConfiguration) {
        if (readAll.booleanValue()) {
            return readSheet;
        }
        for (ReadSheet parameterReadSheet : parameterReadSheetList) {
            if (parameterReadSheet != null) {
                if (parameterReadSheet.getSheetNo() == null && parameterReadSheet.getSheetName() == null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("The first is read by default.");
                    }
                    parameterReadSheet.setSheetNo(0);
                }
                boolean match = parameterReadSheet.getSheetNo() != null && parameterReadSheet.getSheetNo().equals(readSheet.getSheetNo());
                if (!match) {
                    String parameterSheetName = parameterReadSheet.getSheetName();
                    if (!StringUtils.isEmpty(parameterSheetName)) {
                        boolean autoTrim = (parameterReadSheet.getAutoTrim() != null && parameterReadSheet.getAutoTrim().booleanValue()) || (parameterReadSheet.getAutoTrim() == null && globalConfiguration.getAutoTrim().booleanValue());
                        if (autoTrim) {
                            parameterSheetName = parameterSheetName.trim();
                        }
                        match = parameterSheetName.equals(readSheet.getSheetName());
                    }
                }
                if (match) {
                    readSheet.copyBasicParameter(parameterReadSheet);
                    return readSheet;
                }
            }
        }
        return null;
    }
}
