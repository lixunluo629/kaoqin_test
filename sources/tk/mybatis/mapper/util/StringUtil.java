package tk.mybatis.mapper.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tk.mybatis.mapper.code.Style;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/util/StringUtil.class */
public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String convertByStyle(String str, Style style) {
        switch (style) {
            case camelhump:
                return camelhumpToUnderline(str);
            case uppercase:
                return str.toUpperCase();
            case lowercase:
                return str.toLowerCase();
            case normal:
            default:
                return str;
        }
    }

    public static String camelhumpToUnderline(String str) {
        char[] chars = str.toCharArray();
        int size = chars.length;
        StringBuilder sb = new StringBuilder(((size * 3) / 2) + 1);
        for (char c : chars) {
            if (isUppercaseAlpha(c)) {
                sb.append('_').append(c);
            } else {
                sb.append(toUpperAscii(c));
            }
        }
        return sb.charAt(0) == '_' ? sb.substring(1) : sb.toString();
    }

    public static String underlineToCamelhump(String str) {
        Matcher matcher = Pattern.compile("_[a-z]").matcher(str);
        StringBuilder builder = new StringBuilder(str);
        int i = 0;
        while (matcher.find()) {
            builder.replace(matcher.start() - i, matcher.end() - i, matcher.group().substring(1).toUpperCase());
            i++;
        }
        if (Character.isUpperCase(builder.charAt(0))) {
            builder.replace(0, 1, String.valueOf(Character.toLowerCase(builder.charAt(0))));
        }
        return builder.toString();
    }

    public static boolean isUppercaseAlpha(char c) {
        return c >= 'A' && c <= 'Z';
    }

    public static char toUpperAscii(char c) {
        if (isUppercaseAlpha(c)) {
            c = (char) (c - ' ');
        }
        return c;
    }
}
