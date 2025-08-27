package com.alibaba.excel.annotation.write.style;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/annotation/write/style/ContentRowHeight.class */
public @interface ContentRowHeight {
    short value() default -1;
}
