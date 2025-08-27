package org.apache.poi.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.springframework.beans.PropertyAccessor;

/* loaded from: poi-3.17.jar:org/apache/poi/util/SystemOutLogger.class */
public class SystemOutLogger extends POILogger {
    private String _cat;

    @Override // org.apache.poi.util.POILogger
    public void initialize(String cat) {
        this._cat = cat;
    }

    @Override // org.apache.poi.util.POILogger
    protected void _log(int level, Object obj1) {
        _log(level, obj1, null);
    }

    @Override // org.apache.poi.util.POILogger
    @SuppressForbidden("uses printStackTrace")
    protected void _log(int level, Object obj1, Throwable exception) {
        if (check(level)) {
            System.out.println(PropertyAccessor.PROPERTY_KEY_PREFIX + this._cat + "]" + LEVEL_STRINGS_SHORT[Math.min(LEVEL_STRINGS_SHORT.length - 1, level)] + SymbolConstants.SPACE_SYMBOL + obj1);
            if (exception != null) {
                exception.printStackTrace(System.out);
            }
        }
    }

    @Override // org.apache.poi.util.POILogger
    public boolean check(int level) throws NumberFormatException {
        int currentLevel;
        try {
            currentLevel = Integer.parseInt(System.getProperty("poi.log.level", "5"));
        } catch (SecurityException e) {
            currentLevel = 1;
        }
        return level >= currentLevel;
    }
}
