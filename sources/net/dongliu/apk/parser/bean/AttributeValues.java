package net.dongliu.apk.parser.bean;

import java.util.ArrayList;
import java.util.List;
import net.dongliu.apk.parser.utils.Strings;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/AttributeValues.class */
public class AttributeValues {
    public static String getScreenOrientation(int value) {
        switch (value) {
            case -1:
                return "unspecified";
            case 0:
                return "landscape";
            case 1:
                return "portrait";
            case 2:
                return "user";
            case 3:
                return "behind";
            case 4:
                return "sensor";
            case 5:
                return "nosensor";
            case 6:
                return "sensorLandscape";
            case 7:
                return "sensorPortrait";
            case 8:
                return "reverseLandscape";
            case 9:
                return "reversePortrait";
            case 10:
                return "fullSensor";
            case 11:
                return "userLandscape";
            case 12:
                return "userPortrait";
            case 13:
                return "fullUser";
            case 14:
                return CellUtil.LOCKED;
            default:
                return "ScreenOrientation:" + Integer.toHexString(value);
        }
    }

    public static String getLaunchMode(int value) {
        switch (value) {
            case 0:
                return "standard";
            case 1:
                return "singleTop";
            case 2:
                return "singleTask";
            case 3:
                return "singleInstance";
            default:
                return "LaunchMode:" + Integer.toHexString(value);
        }
    }

    public static String getConfigChanges(int value) {
        List<String> list = new ArrayList<>();
        if ((value & 4096) != 0) {
            list.add("density");
        } else if ((value & 1073741824) != 0) {
            list.add("fontScale");
        } else if ((value & 16) != 0) {
            list.add("keyboard");
        } else if ((value & 32) != 0) {
            list.add("keyboardHidden");
        } else if ((value & 8192) != 0) {
            list.add("direction");
        } else if ((value & 4) != 0) {
            list.add(LocaleChangeInterceptor.DEFAULT_PARAM_NAME);
        } else if ((value & 1) != 0) {
            list.add("mcc");
        } else if ((value & 2) != 0) {
            list.add("mnc");
        } else if ((value & 64) != 0) {
            list.add("navigation");
        } else if ((value & 128) != 0) {
            list.add("orientation");
        } else if ((value & 256) != 0) {
            list.add("screenLayout");
        } else if ((value & 1024) != 0) {
            list.add("screenSize");
        } else if ((value & 2048) != 0) {
            list.add("smallestScreenSize");
        } else if ((value & 8) != 0) {
            list.add("touchscreen");
        } else if ((value & 512) != 0) {
            list.add("uiMode");
        }
        return Strings.join(list, "|");
    }

    public static String getWindowSoftInputMode(int value) {
        int adjust = value & 240;
        int state = value & 15;
        List<String> list = new ArrayList<>(2);
        switch (adjust) {
            case 0:
                break;
            case 16:
                list.add("adjustResize");
                break;
            case 32:
                list.add("adjustPan");
                break;
            case 48:
                list.add("adjustNothing");
                break;
            default:
                list.add("WindowInputModeAdjust:" + Integer.toHexString(adjust));
                break;
        }
        switch (state) {
            case 0:
                break;
            case 1:
                list.add("stateUnchanged");
                break;
            case 2:
                list.add("stateHidden");
                break;
            case 3:
                list.add("stateAlwaysHidden");
                break;
            case 4:
                list.add("stateVisible");
                break;
            case 5:
                list.add("stateAlwaysVisible");
                break;
            default:
                list.add("WindowInputModeState:" + Integer.toHexString(state));
                break;
        }
        return Strings.join(list, "|");
    }

    public static String getProtectionLevel(int value) {
        List<String> levels = new ArrayList<>(3);
        if ((value & 16) != 0) {
            value ^= 16;
            levels.add("system");
        }
        if ((value & 32) != 0) {
            value ^= 32;
            levels.add("development");
        }
        switch (value) {
            case 0:
                levels.add("normal");
                break;
            case 1:
                levels.add("dangerous");
                break;
            case 2:
                levels.add("signature");
                break;
            case 3:
                levels.add("signatureOrSystem");
                break;
            default:
                levels.add("ProtectionLevel:" + Integer.toHexString(value));
                break;
        }
        return Strings.join(levels, "|");
    }

    public static String getInstallLocation(int value) {
        switch (value) {
            case 0:
                return "auto";
            case 1:
                return "internalOnly";
            case 2:
                return "preferExternal";
            default:
                return "installLocation:" + Integer.toHexString(value);
        }
    }
}
