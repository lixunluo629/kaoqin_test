package org.apache.commons.codec.language;

import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import com.alibaba.excel.constant.ExcelXmlConstants;
import java.util.Locale;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.poi.ss.formula.functions.Complex;

/* loaded from: commons-codec-1.10.jar:org/apache/commons/codec/language/Caverphone2.class */
public class Caverphone2 extends AbstractCaverphone {
    private static final String TEN_1 = "1111111111";

    @Override // org.apache.commons.codec.StringEncoder
    public String encode(String source) {
        if (source == null || source.length() == 0) {
            return TEN_1;
        }
        String txt = source.toLowerCase(Locale.ENGLISH);
        return (txt.replaceAll("[^a-z]", "").replaceAll("e$", "").replaceAll("^cough", "cou2f").replaceAll("^rough", "rou2f").replaceAll("^tough", "tou2f").replaceAll("^enough", "enou2f").replaceAll("^trough", "trou2f").replaceAll("^gn", "2n").replaceAll("mb$", "m2").replaceAll("cq", "2q").replaceAll("ci", "si").replaceAll("ce", "se").replaceAll("cy", "sy").replaceAll("tch", "2ch").replaceAll(ExcelXmlConstants.CELL_TAG, "k").replaceAll("q", "k").replaceAll("x", "k").replaceAll(ExcelXmlConstants.CELL_VALUE_TAG, ExcelXmlConstants.CELL_FORMULA_TAG).replaceAll("dg", "2g").replaceAll("tio", "sio").replaceAll("tia", "sia").replaceAll(DateTokenConverter.CONVERTER_KEY, "t").replaceAll("ph", "fh").replaceAll("b", "p").replaceAll("sh", "s2").replaceAll(CompressorStreamFactory.Z, ExcelXmlConstants.CELL_DATA_FORMAT_TAG).replaceAll("^[aeiou]", "A").replaceAll("[aeiou]", "3").replaceAll(Complex.SUPPORTED_SUFFIX, "y").replaceAll("^y3", "Y3").replaceAll("^y", "A").replaceAll("y", "3").replaceAll("3gh3", "3kh3").replaceAll("gh", "22").replaceAll("g", "k").replaceAll("s+", "S").replaceAll("t+", "T").replaceAll("p+", "P").replaceAll("k+", "K").replaceAll("f+", "F").replaceAll("m+", "M").replaceAll("n+", "N").replaceAll("w3", "W3").replaceAll("wh3", "Wh3").replaceAll("w$", "3").replaceAll("w", "2").replaceAll("^h", "A").replaceAll("h", "2").replaceAll("r3", "R3").replaceAll("r$", "3").replaceAll(ExcelXmlConstants.POSITION, "2").replaceAll("l3", "L3").replaceAll("l$", "3").replaceAll("l", "2").replaceAll("2", "").replaceAll("3$", "A").replaceAll("3", "") + TEN_1).substring(0, TEN_1.length());
    }
}
