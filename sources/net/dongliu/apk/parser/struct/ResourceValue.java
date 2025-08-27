package net.dongliu.apk.parser.struct;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.dongliu.apk.parser.bean.Locales;
import net.dongliu.apk.parser.struct.resource.ResourceEntry;
import net.dongliu.apk.parser.struct.resource.ResourceTable;
import net.dongliu.apk.parser.struct.resource.Type;
import net.dongliu.apk.parser.struct.resource.TypeSpec;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResourceValue.class */
public abstract class ResourceValue {
    protected final int value;

    public abstract String toStringValue(ResourceTable resourceTable, Locale locale);

    protected ResourceValue(int value) {
        this.value = value;
    }

    public static ResourceValue decimal(int value) {
        return new DecimalResourceValue(value);
    }

    public static ResourceValue hexadecimal(int value) {
        return new HexadecimalResourceValue(value);
    }

    public static ResourceValue bool(int value) {
        return new BooleanResourceValue(value);
    }

    public static ResourceValue string(int value, StringPool stringPool) {
        return new StringResourceValue(value, stringPool);
    }

    public static ResourceValue reference(int value) {
        return new ReferenceResourceValue(value);
    }

    public static ResourceValue nullValue() {
        return NullResourceValue.instance;
    }

    public static ResourceValue rgb(int value, int len) {
        return new RGBResourceValue(value, len);
    }

    public static ResourceValue dimension(int value) {
        return new DimensionValue(value);
    }

    public static ResourceValue fraction(int value) {
        return new FractionValue(value);
    }

    public static ResourceValue raw(int value, short type) {
        return new RawValue(value, type);
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResourceValue$DecimalResourceValue.class */
    private static class DecimalResourceValue extends ResourceValue {
        private DecimalResourceValue(int value) {
            super(value);
        }

        @Override // net.dongliu.apk.parser.struct.ResourceValue
        public String toStringValue(ResourceTable resourceTable, Locale locale) {
            return String.valueOf(this.value);
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResourceValue$HexadecimalResourceValue.class */
    private static class HexadecimalResourceValue extends ResourceValue {
        private HexadecimalResourceValue(int value) {
            super(value);
        }

        @Override // net.dongliu.apk.parser.struct.ResourceValue
        public String toStringValue(ResourceTable resourceTable, Locale locale) {
            return "0x" + Integer.toHexString(this.value);
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResourceValue$BooleanResourceValue.class */
    private static class BooleanResourceValue extends ResourceValue {
        private BooleanResourceValue(int value) {
            super(value);
        }

        @Override // net.dongliu.apk.parser.struct.ResourceValue
        public String toStringValue(ResourceTable resourceTable, Locale locale) {
            return String.valueOf(this.value != 0);
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResourceValue$StringResourceValue.class */
    private static class StringResourceValue extends ResourceValue {
        private final StringPool stringPool;

        private StringResourceValue(int value, StringPool stringPool) {
            super(value);
            this.stringPool = stringPool;
        }

        @Override // net.dongliu.apk.parser.struct.ResourceValue
        public String toStringValue(ResourceTable resourceTable, Locale locale) {
            if (this.value >= 0) {
                return this.stringPool.get(this.value);
            }
            return null;
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResourceValue$ReferenceResourceValue.class */
    public static class ReferenceResourceValue extends ResourceValue {
        private ReferenceResourceValue(int value) {
            super(value);
        }

        @Override // net.dongliu.apk.parser.struct.ResourceValue
        public String toStringValue(ResourceTable resourceTable, Locale locale) {
            String result;
            long resourceId = getReferenceResourceId();
            if (resourceId > 16973824 && resourceId < 16977920) {
                return "@android:style/" + ResourceTable.sysStyle.get(Integer.valueOf((int) resourceId));
            }
            String raw = "resourceId:0x" + Long.toHexString(resourceId);
            if (resourceTable == null) {
                return raw;
            }
            List<ResourceTable.Resource> resources = resourceTable.getResourcesById(resourceId);
            ResourceEntry selected = null;
            TypeSpec typeSpec = null;
            int currentLevel = -1;
            Iterator<ResourceTable.Resource> it = resources.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ResourceTable.Resource resource = it.next();
                Type type = resource.getType();
                typeSpec = resource.getTypeSpec();
                ResourceEntry resourceEntry = resource.getResourceEntry();
                int level = Locales.match(locale, type.getLocale());
                if (level == 2) {
                    selected = resourceEntry;
                    break;
                }
                if (level > currentLevel) {
                    selected = resourceEntry;
                    currentLevel = level;
                }
            }
            if (selected == null) {
                result = raw;
            } else if (locale == null) {
                result = "@" + typeSpec.getName() + "/" + selected.getKey();
            } else {
                result = selected.toStringValue(resourceTable, locale);
            }
            return result;
        }

        public long getReferenceResourceId() {
            return this.value & 4294967295L;
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResourceValue$NullResourceValue.class */
    private static class NullResourceValue extends ResourceValue {
        private static final NullResourceValue instance = new NullResourceValue();

        private NullResourceValue() {
            super(-1);
        }

        @Override // net.dongliu.apk.parser.struct.ResourceValue
        public String toStringValue(ResourceTable resourceTable, Locale locale) {
            return "";
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResourceValue$RGBResourceValue.class */
    private static class RGBResourceValue extends ResourceValue {
        private final int len;

        private RGBResourceValue(int value, int len) {
            super(value);
            this.len = len;
        }

        @Override // net.dongliu.apk.parser.struct.ResourceValue
        public String toStringValue(ResourceTable resourceTable, Locale locale) {
            StringBuilder sb = new StringBuilder();
            for (int i = (this.len / 2) - 1; i >= 0; i--) {
                sb.append(Integer.toHexString((this.value >> (i * 8)) & 255));
            }
            return sb.toString();
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResourceValue$DimensionValue.class */
    private static class DimensionValue extends ResourceValue {
        private DimensionValue(int value) {
            super(value);
        }

        @Override // net.dongliu.apk.parser.struct.ResourceValue
        public String toStringValue(ResourceTable resourceTable, Locale locale) {
            String unitStr;
            short unit = (short) (this.value & 255);
            switch (unit) {
                case 0:
                    unitStr = "px";
                    break;
                case 1:
                    unitStr = "dp";
                    break;
                case 2:
                    unitStr = "sp";
                    break;
                case 3:
                    unitStr = "pt";
                    break;
                case 4:
                    unitStr = "in";
                    break;
                case 5:
                    unitStr = "mm";
                    break;
                default:
                    unitStr = "unknown unit:0x" + Integer.toHexString(unit);
                    break;
            }
            return (this.value >> 8) + unitStr;
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResourceValue$FractionValue.class */
    private static class FractionValue extends ResourceValue {
        private FractionValue(int value) {
            super(value);
        }

        @Override // net.dongliu.apk.parser.struct.ResourceValue
        public String toStringValue(ResourceTable resourceTable, Locale locale) {
            String pstr;
            short type = (short) (this.value & 15);
            switch (type) {
                case 0:
                    pstr = QuickTargetSourceCreator.PREFIX_THREAD_LOCAL;
                    break;
                case 1:
                    pstr = "%p";
                    break;
                default:
                    pstr = "unknown type:0x" + Integer.toHexString(type);
                    break;
            }
            float f = Float.intBitsToFloat(this.value >> 4);
            return f + pstr;
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ResourceValue$RawValue.class */
    private static class RawValue extends ResourceValue {
        private final short dataType;

        private RawValue(int value, short dataType) {
            super(value);
            this.dataType = dataType;
        }

        @Override // net.dongliu.apk.parser.struct.ResourceValue
        public String toStringValue(ResourceTable resourceTable, Locale locale) {
            return "{" + ((int) this.dataType) + ":" + (this.value & 4294967295L) + "}";
        }
    }
}
