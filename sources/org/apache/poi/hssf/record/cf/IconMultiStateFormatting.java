package org.apache.poi.hssf.record.cf;

import org.apache.poi.ss.usermodel.IconMultiStateFormatting;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/cf/IconMultiStateFormatting.class */
public final class IconMultiStateFormatting implements Cloneable {
    private IconMultiStateFormatting.IconSet iconSet;
    private byte options;
    private Threshold[] thresholds;
    private static POILogger log = POILogFactory.getLogger((Class<?>) IconMultiStateFormatting.class);
    private static BitField iconOnly = BitFieldFactory.getInstance(1);
    private static BitField reversed = BitFieldFactory.getInstance(4);

    public IconMultiStateFormatting() {
        this.iconSet = IconMultiStateFormatting.IconSet.GYR_3_TRAFFIC_LIGHTS;
        this.options = (byte) 0;
        this.thresholds = new Threshold[this.iconSet.num];
    }

    public IconMultiStateFormatting(LittleEndianInput in) {
        in.readShort();
        in.readByte();
        int num = in.readByte();
        int set = in.readByte();
        this.iconSet = IconMultiStateFormatting.IconSet.byId(set);
        if (this.iconSet.num != num) {
            log.log(5, "Inconsistent Icon Set defintion, found " + this.iconSet + " but defined as " + num + " entries");
        }
        this.options = in.readByte();
        this.thresholds = new Threshold[this.iconSet.num];
        for (int i = 0; i < this.thresholds.length; i++) {
            this.thresholds[i] = new IconMultiStateThreshold(in);
        }
    }

    public IconMultiStateFormatting.IconSet getIconSet() {
        return this.iconSet;
    }

    public void setIconSet(IconMultiStateFormatting.IconSet set) {
        this.iconSet = set;
    }

    public Threshold[] getThresholds() {
        return this.thresholds;
    }

    public void setThresholds(Threshold[] thresholds) {
        this.thresholds = thresholds == null ? null : (Threshold[]) thresholds.clone();
    }

    public boolean isIconOnly() {
        return getOptionFlag(iconOnly);
    }

    public void setIconOnly(boolean only) {
        setOptionFlag(only, iconOnly);
    }

    public boolean isReversed() {
        return getOptionFlag(reversed);
    }

    public void setReversed(boolean rev) {
        setOptionFlag(rev, reversed);
    }

    private boolean getOptionFlag(BitField field) {
        int value = field.getValue(this.options);
        return value != 0;
    }

    private void setOptionFlag(boolean option, BitField field) {
        this.options = field.setByteBoolean(this.options, option);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("    [Icon Formatting]\n");
        buffer.append("          .icon_set = ").append(this.iconSet).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("          .icon_only= ").append(isIconOnly()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("          .reversed = ").append(isReversed()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        Threshold[] arr$ = this.thresholds;
        for (Threshold t : arr$) {
            buffer.append(t);
        }
        buffer.append("    [/Icon Formatting]\n");
        return buffer.toString();
    }

    public Object clone() {
        IconMultiStateFormatting rec = new IconMultiStateFormatting();
        rec.iconSet = this.iconSet;
        rec.options = this.options;
        rec.thresholds = new Threshold[this.thresholds.length];
        System.arraycopy(this.thresholds, 0, rec.thresholds, 0, this.thresholds.length);
        return rec;
    }

    public int getDataLength() {
        int len = 6;
        Threshold[] arr$ = this.thresholds;
        for (Threshold t : arr$) {
            len += t.getDataLength();
        }
        return len;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(0);
        out.writeByte(0);
        out.writeByte(this.iconSet.num);
        out.writeByte(this.iconSet.id);
        out.writeByte(this.options);
        Threshold[] arr$ = this.thresholds;
        for (Threshold t : arr$) {
            t.serialize(out);
        }
    }
}
