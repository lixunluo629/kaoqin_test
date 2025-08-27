package com.moredian.onpremise.core.model.convert;

import java.text.SimpleDateFormat;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/convert/TimeConvert.class */
public class TimeConvert extends DefaultConvert {
    @Override // com.moredian.onpremise.core.model.convert.DefaultConvert, com.moredian.onpremise.core.model.convert.ModelConvert
    public Object convert(Object val) {
        if (val == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = sdf.format(val);
        return s;
    }
}
