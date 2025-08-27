package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import javax.annotation.Nullable;
import org.apache.poi.util.TempFile;

@Beta
@GwtIncompatible("java.lang.System#getProperty")
/* loaded from: guava-18.0.jar:com/google/common/base/StandardSystemProperty.class */
public enum StandardSystemProperty {
    JAVA_VERSION("java.version"),
    JAVA_VENDOR("java.vendor"),
    JAVA_VENDOR_URL("java.vendor.url"),
    JAVA_HOME("java.home"),
    JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"),
    JAVA_VM_SPECIFICATION_VENDOR("java.vm.specification.vendor"),
    JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"),
    JAVA_VM_VERSION("java.vm.version"),
    JAVA_VM_VENDOR("java.vm.vendor"),
    JAVA_VM_NAME("java.vm.name"),
    JAVA_SPECIFICATION_VERSION("java.specification.version"),
    JAVA_SPECIFICATION_VENDOR("java.specification.vendor"),
    JAVA_SPECIFICATION_NAME("java.specification.name"),
    JAVA_CLASS_VERSION("java.class.version"),
    JAVA_CLASS_PATH("java.class.path"),
    JAVA_LIBRARY_PATH("java.library.path"),
    JAVA_IO_TMPDIR(TempFile.JAVA_IO_TMPDIR),
    JAVA_COMPILER("java.compiler"),
    JAVA_EXT_DIRS("java.ext.dirs"),
    OS_NAME("os.name"),
    OS_ARCH("os.arch"),
    OS_VERSION("os.version"),
    FILE_SEPARATOR("file.separator"),
    PATH_SEPARATOR("path.separator"),
    LINE_SEPARATOR("line.separator"),
    USER_NAME("user.name"),
    USER_HOME("user.home"),
    USER_DIR("user.dir");

    private final String key;

    StandardSystemProperty(String key) {
        this.key = key;
    }

    public String key() {
        return this.key;
    }

    @Nullable
    public String value() {
        return System.getProperty(this.key);
    }

    @Override // java.lang.Enum
    public String toString() {
        String strValueOf = String.valueOf(String.valueOf(key()));
        String strValueOf2 = String.valueOf(String.valueOf(value()));
        return new StringBuilder(1 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(SymbolConstants.EQUAL_SYMBOL).append(strValueOf2).toString();
    }
}
