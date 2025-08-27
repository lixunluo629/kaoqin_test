package org.apache.poi.hssf.record.common;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/common/FeatProtection.class */
public final class FeatProtection implements SharedFeature {
    public static long NO_SELF_RELATIVE_SECURITY_FEATURE = 0;
    public static long HAS_SELF_RELATIVE_SECURITY_FEATURE = 1;
    private int fSD;
    private int passwordVerifier;
    private String title;
    private byte[] securityDescriptor;

    public FeatProtection() {
        this.securityDescriptor = new byte[0];
    }

    public FeatProtection(RecordInputStream in) {
        this.fSD = in.readInt();
        this.passwordVerifier = in.readInt();
        this.title = StringUtil.readUnicodeString(in);
        this.securityDescriptor = in.readRemainder();
    }

    @Override // org.apache.poi.hssf.record.common.SharedFeature
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" [FEATURE PROTECTION]\n");
        buffer.append("   Self Relative = " + this.fSD);
        buffer.append("   Password Verifier = " + this.passwordVerifier);
        buffer.append("   Title = " + this.title);
        buffer.append("   Security Descriptor Size = " + this.securityDescriptor.length);
        buffer.append(" [/FEATURE PROTECTION]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.common.SharedFeature
    public void serialize(LittleEndianOutput out) {
        out.writeInt(this.fSD);
        out.writeInt(this.passwordVerifier);
        StringUtil.writeUnicodeString(out, this.title);
        out.write(this.securityDescriptor);
    }

    @Override // org.apache.poi.hssf.record.common.SharedFeature
    public int getDataSize() {
        return 8 + StringUtil.getEncodedSize(this.title) + this.securityDescriptor.length;
    }

    public int getPasswordVerifier() {
        return this.passwordVerifier;
    }

    public void setPasswordVerifier(int passwordVerifier) {
        this.passwordVerifier = passwordVerifier;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFSD() {
        return this.fSD;
    }
}
