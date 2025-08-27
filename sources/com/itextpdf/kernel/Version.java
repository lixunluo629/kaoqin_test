package com.itextpdf.kernel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/Version.class */
public final class Version {
    private static final String AGPL = " (AGPL-version)";
    private static final String iTextProductName = "iText®";
    private static final String release = "7.1.10";
    private static final String producerLine = "iText® 7.1.10 ©2000-2020 iText Group NV";
    private final VersionInfo info;
    private boolean expired;
    private static final Object staticLock = new Object();
    private static volatile Version version = null;

    @Deprecated
    public Version() {
        this.info = new VersionInfo(iTextProductName, release, producerLine, null);
    }

    private Version(VersionInfo info, boolean expired) {
        this.info = info;
        this.expired = expired;
    }

    public static Version getInstance() {
        Version localVersion;
        String key;
        synchronized (staticLock) {
            if (version != null) {
                try {
                    licenseScheduledCheck();
                } catch (Exception e) {
                    atomicSetVersion(initAGPLVersion(e, null));
                }
                return version;
            }
            try {
                String[] info = getLicenseeInfoFromLicenseKey(release);
                if (info == null) {
                    localVersion = initAGPLVersion(null, null);
                } else {
                    if (info[3] != null && info[3].trim().length() > 0) {
                        key = info[3];
                    } else {
                        key = info[5] == null ? "Trial version unauthorised" : "Trial version " + info[5];
                    }
                    if (info.length > 6 && info[6] != null && info[6].trim().length() > 0) {
                        checkLicenseVersion(release, info[6]);
                    }
                    if (info[4] != null && info[4].trim().length() > 0) {
                        localVersion = initVersion(info[4], key, false);
                    } else if (info[2] != null && info[2].trim().length() > 0) {
                        localVersion = initDefaultLicensedVersion(info[2], key);
                    } else if (info[0] != null && info[0].trim().length() > 0) {
                        localVersion = initDefaultLicensedVersion(info[0], key);
                    } else {
                        localVersion = initAGPLVersion(null, key);
                    }
                }
            } catch (LicenseVersionException lve) {
                throw lve;
            } catch (ClassNotFoundException e2) {
                localVersion = initAGPLVersion(null, null);
            } catch (Exception e3) {
                if (e3.getCause() == null || !e3.getCause().getMessage().equals(LicenseVersionException.LICENSE_FILE_NOT_LOADED) || !isiText5licenseLoaded()) {
                    localVersion = initAGPLVersion(e3.getCause(), null);
                } else {
                    throw new LicenseVersionException(LicenseVersionException.NO_I_TEXT7_LICENSE_IS_LOADED_BUT_AN_I_TEXT5_LICENSE_IS_LOADED);
                }
            }
            return atomicSetVersion(localVersion);
        }
    }

    public static boolean isAGPLVersion() {
        return getInstance().isAGPL();
    }

    public static boolean isExpired() {
        return getInstance().expired;
    }

    public String getProduct() {
        return this.info.getProduct();
    }

    public String getRelease() {
        return this.info.getRelease();
    }

    public String getVersion() {
        return this.info.getVersion();
    }

    public String getKey() {
        return this.info.getKey();
    }

    public VersionInfo getInfo() {
        return this.info;
    }

    private boolean isAGPL() {
        return getVersion().indexOf(AGPL) > 0;
    }

    private static Version initDefaultLicensedVersion(String ownerName, String key) {
        String producer;
        String producer2 = "iText® 7.1.10 ©2000-2020 iText Group NV (" + ownerName;
        if (!key.toLowerCase().startsWith("trial")) {
            producer = producer2 + "; licensed version)";
        } else {
            producer = producer2 + "; " + key + ")";
        }
        return initVersion(producer, key, false);
    }

    private static Version initAGPLVersion(Throwable cause, String key) {
        boolean expired = (cause == null || cause.getMessage() == null || !cause.getMessage().contains("expired")) ? false : true;
        return initVersion("iText® 7.1.10 ©2000-2020 iText Group NV (AGPL-version)", key, expired);
    }

    private static Version initVersion(String producer, String key, boolean expired) {
        return new Version(new VersionInfo(iTextProductName, release, producer, key), expired);
    }

    private static Class<?> getLicenseKeyClass() throws ClassNotFoundException {
        return getClassFromLicenseKey("com.itextpdf.licensekey.LicenseKey");
    }

    private static Class<?> getClassFromLicenseKey(String classFullName) throws ClassNotFoundException {
        return Class.forName(classFullName);
    }

    private static void checkLicenseVersion(String coreVersionString, String licenseVersionString) throws NumberFormatException {
        String[] coreVersions = parseVersionString(coreVersionString);
        String[] licenseVersions = parseVersionString(licenseVersionString);
        int coreMajor = Integer.parseInt(coreVersions[0]);
        int coreMinor = Integer.parseInt(coreVersions[1]);
        int licenseMajor = Integer.parseInt(licenseVersions[0]);
        int licenseMinor = Integer.parseInt(licenseVersions[1]);
        if (licenseMajor < coreMajor) {
            throw new LicenseVersionException(LicenseVersionException.THE_MAJOR_VERSION_OF_THE_LICENSE_0_IS_LOWER_THAN_THE_MAJOR_VERSION_1_OF_THE_CORE_LIBRARY).setMessageParams(Integer.valueOf(licenseMajor), Integer.valueOf(coreMajor));
        }
        if (licenseMajor > coreMajor) {
            throw new LicenseVersionException(LicenseVersionException.THE_MAJOR_VERSION_OF_THE_LICENSE_0_IS_HIGHER_THAN_THE_MAJOR_VERSION_1_OF_THE_CORE_LIBRARY).setMessageParams(Integer.valueOf(licenseMajor), Integer.valueOf(coreMajor));
        }
        if (licenseMinor < coreMinor) {
            throw new LicenseVersionException(LicenseVersionException.THE_MINOR_VERSION_OF_THE_LICENSE_0_IS_LOWER_THAN_THE_MINOR_VERSION_1_OF_THE_CORE_LIBRARY).setMessageParams(Integer.valueOf(licenseMinor), Integer.valueOf(coreMinor));
        }
    }

    private static String[] parseVersionString(String version2) {
        String[] split = version2.split("\\.");
        if (split.length == 0) {
            throw new LicenseVersionException(LicenseVersionException.VERSION_STRING_IS_EMPTY_AND_CANNOT_BE_PARSED);
        }
        String major = split[0];
        String minor = "0";
        if (split.length > 1) {
            minor = split[1].substring(0);
        }
        if (!isVersionNumeric(major)) {
            throw new LicenseVersionException(LicenseVersionException.MAJOR_VERSION_IS_NOT_NUMERIC);
        }
        if (isVersionNumeric(minor)) {
            return new String[]{major, minor};
        }
        throw new LicenseVersionException(LicenseVersionException.MINOR_VERSION_IS_NOT_NUMERIC);
    }

    private static String[] getLicenseeInfoFromLicenseKey(String validatorKey) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, SecurityException, InvocationTargetException {
        Class<?> klass = getLicenseKeyClass();
        if (klass != null) {
            Class[] cArg = {String.class};
            Method m = klass.getMethod("getLicenseeInfoForVersion", cArg);
            Object[] args = {validatorKey};
            String[] info = (String[]) m.invoke(klass.newInstance(), args);
            return info;
        }
        return null;
    }

    private static boolean isiText5licenseLoaded() {
        boolean result = false;
        try {
            getLicenseeInfoFromLicenseKey("5");
            result = true;
        } catch (Exception e) {
        }
        return result;
    }

    private static boolean isVersionNumeric(String version2) throws NumberFormatException {
        try {
            Double.parseDouble(version2);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static Version atomicSetVersion(Version newVersion) {
        Version version2;
        synchronized (staticLock) {
            version = newVersion;
            version2 = version;
        }
        return version2;
    }

    private static void licenseScheduledCheck() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (version.isAGPL()) {
            return;
        }
        try {
            Class<?> licenseKeyClass = getLicenseKeyClass();
            Class licenseKeyProductClass = getClassFromLicenseKey("com.itextpdf.licensekey.LicenseKeyProduct");
            Class[] cArg = {licenseKeyProductClass};
            Method method = licenseKeyClass.getMethod("scheduledCheck", cArg);
            method.invoke(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
