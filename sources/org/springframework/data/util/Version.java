package org.springframework.data.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/Version.class */
public class Version implements Comparable<Version> {
    private static final String VERSION_PARSE_ERROR = "Invalid version string! Could not parse segment %s within %s.";
    private final int major;
    private final int minor;
    private final int bugfix;
    private final int build;

    public Version(int... parts) {
        Assert.notNull(parts, "Parts must not be null!");
        Assert.isTrue(parts.length > 0 && parts.length < 5, String.format("Invalid parts length. 0 < %s < 5", Integer.valueOf(parts.length)));
        this.major = parts[0];
        this.minor = parts.length > 1 ? parts[1] : 0;
        this.bugfix = parts.length > 2 ? parts[2] : 0;
        this.build = parts.length > 3 ? parts[3] : 0;
        Assert.isTrue(this.major >= 0, "Major version must be greater or equal zero!");
        Assert.isTrue(this.minor >= 0, "Minor version must be greater or equal zero!");
        Assert.isTrue(this.bugfix >= 0, "Bugfix version must be greater or equal zero!");
        Assert.isTrue(this.build >= 0, "Build version must be greater or equal zero!");
    }

    public static Version parse(String version) {
        Assert.hasText(version, "Version must not be null o empty!");
        String[] parts = version.trim().split("\\.");
        int[] intParts = new int[parts.length];
        int i = 0;
        while (i < parts.length) {
            String input = i == parts.length - 1 ? parts[i].replaceAll("\\D.*", "") : parts[i];
            if (StringUtils.hasText(input)) {
                try {
                    intParts[i] = Integer.parseInt(input);
                } catch (IllegalArgumentException o_O) {
                    throw new IllegalArgumentException(String.format(VERSION_PARSE_ERROR, input, version), o_O);
                }
            }
            i++;
        }
        return new Version(intParts);
    }

    public boolean isGreaterThan(Version version) {
        return compareTo(version) > 0;
    }

    public boolean isGreaterThanOrEqualTo(Version version) {
        return compareTo(version) >= 0;
    }

    public boolean is(Version version) {
        return equals(version);
    }

    public boolean isLessThan(Version version) {
        return compareTo(version) < 0;
    }

    public boolean isLessThanOrEqualTo(Version version) {
        return compareTo(version) <= 0;
    }

    @Override // java.lang.Comparable
    public int compareTo(Version that) {
        if (that == null) {
            return 1;
        }
        if (this.major != that.major) {
            return this.major - that.major;
        }
        if (this.minor != that.minor) {
            return this.minor - that.minor;
        }
        if (this.bugfix != that.bugfix) {
            return this.bugfix - that.bugfix;
        }
        if (this.build != that.build) {
            return this.build - that.build;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Version)) {
            return false;
        }
        Version that = (Version) obj;
        return this.major == that.major && this.minor == that.minor && this.bugfix == that.bugfix && this.build == that.build;
    }

    public int hashCode() {
        int result = 17 + (31 * this.major);
        return result + (31 * this.minor) + (31 * this.bugfix) + (31 * this.build);
    }

    public String toString() {
        List<Integer> digits = new ArrayList<>();
        digits.add(Integer.valueOf(this.major));
        digits.add(Integer.valueOf(this.minor));
        if (this.build != 0 || this.bugfix != 0) {
            digits.add(Integer.valueOf(this.bugfix));
        }
        if (this.build != 0) {
            digits.add(Integer.valueOf(this.build));
        }
        return StringUtils.collectionToDelimitedString(digits, ".");
    }
}
