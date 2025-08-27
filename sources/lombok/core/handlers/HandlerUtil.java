package lombok.core.handlers;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.ConfigurationKeys;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.JavaIdentifiers;
import lombok.core.LombokNode;
import lombok.core.configuration.AllowHelper;
import lombok.core.configuration.ConfigurationKey;
import lombok.core.configuration.FlagUsageType;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

/* loaded from: lombok-1.16.22.jar:lombok/core/handlers/HandlerUtil.SCL.lombok */
public class HandlerUtil {
    public static final List<String> INVALID_ON_BUILDERS = Collections.unmodifiableList(Arrays.asList(Getter.class.getName(), Setter.class.getName(), Wither.class.getName(), ToString.class.getName(), EqualsAndHashCode.class.getName(), RequiredArgsConstructor.class.getName(), AllArgsConstructor.class.getName(), NoArgsConstructor.class.getName(), Data.class.getName(), Value.class.getName(), "lombok.experimental.Value", FieldDefaults.class.getName()));
    public static final Pattern NON_NULL_PATTERN = Pattern.compile("^(?:nonnull)$", 2);
    public static final Pattern NULLABLE_PATTERN = Pattern.compile("^(?:nullable|checkfornull)$", 2);
    public static final String DEFAULT_EXCEPTION_FOR_NON_NULL = "java.lang.NullPointerException";

    /* loaded from: lombok-1.16.22.jar:lombok/core/handlers/HandlerUtil$FieldAccess.SCL.lombok */
    public enum FieldAccess {
        GETTER,
        PREFER_FIELD,
        ALWAYS_FIELD;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static FieldAccess[] valuesCustom() {
            FieldAccess[] fieldAccessArrValuesCustom = values();
            int length = fieldAccessArrValuesCustom.length;
            FieldAccess[] fieldAccessArr = new FieldAccess[length];
            System.arraycopy(fieldAccessArrValuesCustom, 0, fieldAccessArr, 0, length);
            return fieldAccessArr;
        }
    }

    private HandlerUtil() {
    }

    public static int primeForHashcode() {
        return 59;
    }

    public static int primeForTrue() {
        return 79;
    }

    public static int primeForFalse() {
        return 97;
    }

    public static int primeForNull() {
        return 43;
    }

    public static boolean checkName(String nameSpec, String identifier, LombokNode<?, ?, ?> errorNode) {
        if (identifier.isEmpty()) {
            errorNode.addError(String.valueOf(nameSpec) + " cannot be the empty string.");
            return false;
        }
        if (!JavaIdentifiers.isValidJavaIdentifier(identifier)) {
            errorNode.addError(String.valueOf(nameSpec) + " must be a valid java identifier.");
            return false;
        }
        return true;
    }

    public static String autoSingularize(String plural) {
        return Singulars.autoSingularize(plural);
    }

    public static void handleFlagUsage(LombokNode<?, ?, ?> node, ConfigurationKey<FlagUsageType> key, String featureName) {
        FlagUsageType fut = (FlagUsageType) node.getAst().readConfiguration(key);
        if (fut == null && AllowHelper.isAllowable(key)) {
            node.addError("Use of " + featureName + " is disabled by default. Please add '" + key.getKeyName() + " = " + FlagUsageType.ALLOW + "' to 'lombok.config' if you want to enable is.");
        }
        if (fut != null) {
            String msg = "Use of " + featureName + " is flagged according to lombok configuration.";
            if (fut != FlagUsageType.WARNING) {
                if (fut == FlagUsageType.ERROR) {
                    node.addError(msg);
                    return;
                }
                return;
            }
            node.addWarning(msg);
        }
    }

    public static boolean shouldAddGenerated(LombokNode<?, ?, ?> node) {
        Boolean add = (Boolean) node.getAst().readConfiguration(ConfigurationKeys.ADD_JAVAX_GENERATED_ANNOTATIONS);
        return add != null ? add.booleanValue() : Boolean.TRUE.equals(node.getAst().readConfiguration(ConfigurationKeys.ADD_GENERATED_ANNOTATIONS));
    }

    public static void handleExperimentalFlagUsage(LombokNode<?, ?, ?> node, ConfigurationKey<FlagUsageType> key, String featureName) {
        handleFlagUsage(node, key, featureName, ConfigurationKeys.EXPERIMENTAL_FLAG_USAGE, "any lombok.experimental feature");
    }

    public static void handleFlagUsage(LombokNode<?, ?, ?> node, ConfigurationKey<FlagUsageType> key1, String featureName1, ConfigurationKey<FlagUsageType> key2, String featureName2) {
        FlagUsageType fut;
        String featureName;
        FlagUsageType fut1 = (FlagUsageType) node.getAst().readConfiguration(key1);
        FlagUsageType fut2 = (FlagUsageType) node.getAst().readConfiguration(key2);
        if (fut1 == FlagUsageType.ERROR) {
            fut = fut1;
            featureName = featureName1;
        } else if (fut2 != FlagUsageType.ERROR && fut1 == FlagUsageType.WARNING) {
            fut = fut1;
            featureName = featureName1;
        } else {
            fut = fut2;
            featureName = featureName2;
        }
        if (fut != null) {
            String msg = "Use of " + featureName + " is flagged according to lombok configuration.";
            if (fut != FlagUsageType.WARNING) {
                node.addError(msg);
            } else {
                node.addWarning(msg);
            }
        }
    }

    public static boolean shouldReturnThis0(AnnotationValues<Accessors> accessors, AST<?, ?, ?> ast) {
        Boolean fluentConfig;
        Boolean chainConfig;
        boolean chainForced = accessors.isExplicit("chain");
        boolean fluentForced = accessors.isExplicit("fluent");
        Accessors instance = (Accessors) accessors.getInstance();
        boolean chain = instance.chain();
        boolean fluent = instance.fluent();
        if (chainForced) {
            return chain;
        }
        if (!chainForced && (chainConfig = (Boolean) ast.readConfiguration(ConfigurationKeys.ACCESSORS_CHAIN)) != null) {
            return chainConfig.booleanValue();
        }
        if (!fluentForced && (fluentConfig = (Boolean) ast.readConfiguration(ConfigurationKeys.ACCESSORS_FLUENT)) != null) {
            fluent = fluentConfig.booleanValue();
        }
        return chain || fluent;
    }

    public static CharSequence removePrefix(CharSequence fieldName, List<String> prefixes) {
        if (prefixes == null || prefixes.isEmpty()) {
            return fieldName;
        }
        CharSequence fieldName2 = fieldName.toString();
        for (String prefix : prefixes) {
            if (prefix.length() == 0) {
                return fieldName2;
            }
            if (fieldName2.length() > prefix.length()) {
                int i = 0;
                while (true) {
                    if (i < prefix.length()) {
                        if (fieldName2.charAt(i) != prefix.charAt(i)) {
                            break;
                        }
                        i++;
                    } else {
                        char followupChar = fieldName2.charAt(prefix.length());
                        if (!Character.isLetter(prefix.charAt(prefix.length() - 1)) || !Character.isLowerCase(followupChar)) {
                            return new StringBuilder().append(Character.toLowerCase(followupChar)).append((Object) fieldName2.subSequence(prefix.length() + 1, fieldName2.length())).toString();
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String toGetterName(AST<?, ?, ?> ast, AnnotationValues<Accessors> accessors, CharSequence fieldName, boolean isBoolean) {
        return toAccessorName(ast, accessors, fieldName, isBoolean, BeanUtil.PREFIX_GETTER_IS, BeanUtil.PREFIX_GETTER_GET, true);
    }

    public static String toSetterName(AST<?, ?, ?> ast, AnnotationValues<Accessors> accessors, CharSequence fieldName, boolean isBoolean) {
        return toAccessorName(ast, accessors, fieldName, isBoolean, "set", "set", true);
    }

    public static String toWitherName(AST<?, ?, ?> ast, AnnotationValues<Accessors> accessors, CharSequence fieldName, boolean isBoolean) {
        return toAccessorName(ast, accessors, fieldName, isBoolean, JsonPOJOBuilder.DEFAULT_WITH_PREFIX, JsonPOJOBuilder.DEFAULT_WITH_PREFIX, false);
    }

    private static String toAccessorName(AST<?, ?, ?> ast, AnnotationValues<Accessors> accessors, CharSequence fieldName, boolean isBoolean, String booleanPrefix, String normalPrefix, boolean adhereToFluent) {
        CharSequence fieldName2 = fieldName.toString();
        if (fieldName2.length() == 0) {
            return null;
        }
        if (Boolean.TRUE.equals(ast.readConfiguration(ConfigurationKeys.GETTER_CONSEQUENT_BOOLEAN))) {
            isBoolean = false;
        }
        boolean explicitPrefix = accessors != null && accessors.isExplicit("prefix");
        boolean explicitFluent = accessors != null && accessors.isExplicit("fluent");
        Accessors ac = (explicitPrefix || explicitFluent) ? (Accessors) accessors.getInstance() : null;
        List<String> prefix = explicitPrefix ? Arrays.asList(ac.prefix()) : (List) ast.readConfiguration(ConfigurationKeys.ACCESSORS_PREFIX);
        boolean fluent = explicitFluent ? ac.fluent() : Boolean.TRUE.equals(ast.readConfiguration(ConfigurationKeys.ACCESSORS_FLUENT));
        CharSequence fieldName3 = removePrefix(fieldName2, prefix);
        if (fieldName3 == null) {
            return null;
        }
        String fName = fieldName3.toString();
        if (adhereToFluent && fluent) {
            return fName;
        }
        if (!isBoolean || !fName.startsWith(BeanUtil.PREFIX_GETTER_IS) || fieldName3.length() <= 2 || Character.isLowerCase(fieldName3.charAt(2))) {
            return buildAccessorName(isBoolean ? booleanPrefix : normalPrefix, fName);
        }
        return String.valueOf(booleanPrefix) + fName.substring(2);
    }

    public static List<String> toAllGetterNames(AST<?, ?, ?> ast, AnnotationValues<Accessors> accessors, CharSequence fieldName, boolean isBoolean) {
        return toAllAccessorNames(ast, accessors, fieldName, isBoolean, BeanUtil.PREFIX_GETTER_IS, BeanUtil.PREFIX_GETTER_GET, true);
    }

    public static List<String> toAllSetterNames(AST<?, ?, ?> ast, AnnotationValues<Accessors> accessors, CharSequence fieldName, boolean isBoolean) {
        return toAllAccessorNames(ast, accessors, fieldName, isBoolean, "set", "set", true);
    }

    public static List<String> toAllWitherNames(AST<?, ?, ?> ast, AnnotationValues<Accessors> accessors, CharSequence fieldName, boolean isBoolean) {
        return toAllAccessorNames(ast, accessors, fieldName, isBoolean, JsonPOJOBuilder.DEFAULT_WITH_PREFIX, JsonPOJOBuilder.DEFAULT_WITH_PREFIX, false);
    }

    private static List<String> toAllAccessorNames(AST<?, ?, ?> ast, AnnotationValues<Accessors> accessors, CharSequence fieldName, boolean isBoolean, String booleanPrefix, String normalPrefix, boolean adhereToFluent) {
        if (Boolean.TRUE.equals(ast.readConfiguration(ConfigurationKeys.GETTER_CONSEQUENT_BOOLEAN))) {
            isBoolean = false;
        }
        if (!isBoolean) {
            String accessorName = toAccessorName(ast, accessors, fieldName, false, booleanPrefix, normalPrefix, adhereToFluent);
            return accessorName == null ? Collections.emptyList() : Collections.singletonList(accessorName);
        }
        boolean explicitPrefix = accessors != null && accessors.isExplicit("prefix");
        boolean explicitFluent = accessors != null && accessors.isExplicit("fluent");
        Accessors ac = (explicitPrefix || explicitFluent) ? (Accessors) accessors.getInstance() : null;
        List<String> prefix = explicitPrefix ? Arrays.asList(ac.prefix()) : (List) ast.readConfiguration(ConfigurationKeys.ACCESSORS_PREFIX);
        boolean fluent = explicitFluent ? ac.fluent() : Boolean.TRUE.equals(ast.readConfiguration(ConfigurationKeys.ACCESSORS_FLUENT));
        CharSequence fieldName2 = removePrefix(fieldName, prefix);
        if (fieldName2 == null) {
            return Collections.emptyList();
        }
        List<String> baseNames = toBaseNames(fieldName2, isBoolean, fluent);
        Set<String> names = new HashSet<>();
        for (String baseName : baseNames) {
            if (adhereToFluent && fluent) {
                names.add(baseName);
            } else {
                names.add(buildAccessorName(normalPrefix, baseName));
                if (!normalPrefix.equals(booleanPrefix)) {
                    names.add(buildAccessorName(booleanPrefix, baseName));
                }
            }
        }
        return new ArrayList(names);
    }

    private static List<String> toBaseNames(CharSequence fieldName, boolean isBoolean, boolean fluent) {
        List<String> baseNames = new ArrayList<>();
        baseNames.add(fieldName.toString());
        String fName = fieldName.toString();
        if (fName.startsWith(BeanUtil.PREFIX_GETTER_IS) && fName.length() > 2 && !Character.isLowerCase(fName.charAt(2))) {
            String baseName = fName.substring(2);
            if (fluent) {
                baseNames.add(Character.toLowerCase(baseName.charAt(0)) + baseName.substring(1));
            } else {
                baseNames.add(baseName);
            }
        }
        return baseNames;
    }

    public static String buildAccessorName(String prefix, String suffix) {
        if (suffix.length() == 0) {
            return prefix;
        }
        if (prefix.length() == 0) {
            return suffix;
        }
        char first = suffix.charAt(0);
        if (Character.isLowerCase(first)) {
            boolean useUpperCase = suffix.length() > 2 && (Character.isTitleCase(suffix.charAt(1)) || Character.isUpperCase(suffix.charAt(1)));
            Object[] objArr = new Object[2];
            objArr[0] = Character.valueOf(useUpperCase ? Character.toUpperCase(first) : Character.toTitleCase(first));
            objArr[1] = suffix.subSequence(1, suffix.length());
            suffix = String.format("%s%s", objArr);
        }
        return String.format("%s%s", prefix, suffix);
    }

    public static String camelCaseToConstant(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return "";
        }
        StringBuilder b = new StringBuilder();
        b.append(Character.toUpperCase(fieldName.charAt(0)));
        for (int i = 1; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c)) {
                b.append('_');
            }
            b.append(Character.toUpperCase(c));
        }
        return b.toString();
    }
}
