package tk.mybatis.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import tk.mybatis.mapper.code.Style;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/annotation/NameStyle.class */
public @interface NameStyle {
    Style value() default Style.normal;
}
