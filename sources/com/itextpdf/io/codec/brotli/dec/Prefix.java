package com.itextpdf.io.codec.brotli.dec;

import com.mysql.jdbc.MysqlErrorNumbers;
import org.apache.ibatis.javassist.compiler.TokenId;
import org.apache.poi.ddf.EscherProperties;
import org.aspectj.apache.bcel.Constants;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/brotli/dec/Prefix.class */
final class Prefix {
    static final int[] BLOCK_LENGTH_OFFSET = {1, 5, 9, 13, 17, 25, 33, 41, 49, 65, 81, 97, 113, 145, 177, Constants.PUTFIELD2_QUICK, EscherProperties.GEOTEXT__HASTEXTEFFECT, 305, TokenId.ANDAND, 497, 753, MysqlErrorNumbers.ER_WARN_DATA_TRUNCATED, 2289, 4337, 8433, 16625};
    static final int[] BLOCK_LENGTH_N_BITS = {2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 7, 8, 9, 10, 11, 12, 13, 24};
    static final int[] INSERT_LENGTH_OFFSET = {0, 1, 2, 3, 4, 5, 6, 8, 10, 14, 18, 26, 34, 50, 66, 98, 130, 194, 322, EscherProperties.PERSPECTIVE__OFFSETY, MysqlErrorNumbers.ER_CANT_REMOVE_ALL_FIELDS, 2114, 6210, 22594};
    static final int[] INSERT_LENGTH_N_BITS = {0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 7, 8, 9, 10, 12, 14, 24};
    static final int[] COPY_LENGTH_OFFSET = {2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 18, 22, 30, 38, 54, 70, 102, 134, 198, 326, 582, MysqlErrorNumbers.ER_NO_SUCH_THREAD, 2118};
    static final int[] COPY_LENGTH_N_BITS = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 7, 8, 9, 10, 24};
    static final int[] INSERT_RANGE_LUT = {0, 0, 8, 8, 0, 16, 8, 16, 16};
    static final int[] COPY_RANGE_LUT = {0, 8, 0, 8, 16, 0, 16, 8, 16};

    Prefix() {
    }
}
