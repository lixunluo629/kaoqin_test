package org.apache.poi.util;

/* loaded from: poi-3.17.jar:org/apache/poi/util/Configurator.class */
public class Configurator {
    private static POILogger logger = POILogFactory.getLogger((Class<?>) Configurator.class);

    public static int getIntValue(String systemProperty, int defaultValue) throws NumberFormatException {
        int result = defaultValue;
        String property = System.getProperty(systemProperty);
        try {
            result = Integer.parseInt(property);
        } catch (Exception e) {
            logger.log(7, "System property -D" + systemProperty + " do not contains a valid integer " + property);
        }
        return result;
    }
}
