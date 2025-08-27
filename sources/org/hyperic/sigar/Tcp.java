package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/Tcp.class */
public class Tcp implements Serializable {
    private static final long serialVersionUID = 14992;
    long activeOpens = 0;
    long passiveOpens = 0;
    long attemptFails = 0;
    long estabResets = 0;
    long currEstab = 0;
    long inSegs = 0;
    long outSegs = 0;
    long retransSegs = 0;
    long inErrs = 0;
    long outRsts = 0;

    public native void gather(Sigar sigar) throws SigarException;

    static Tcp fetch(Sigar sigar) throws SigarException {
        Tcp tcp = new Tcp();
        tcp.gather(sigar);
        return tcp;
    }

    public long getActiveOpens() {
        return this.activeOpens;
    }

    public long getPassiveOpens() {
        return this.passiveOpens;
    }

    public long getAttemptFails() {
        return this.attemptFails;
    }

    public long getEstabResets() {
        return this.estabResets;
    }

    public long getCurrEstab() {
        return this.currEstab;
    }

    public long getInSegs() {
        return this.inSegs;
    }

    public long getOutSegs() {
        return this.outSegs;
    }

    public long getRetransSegs() {
        return this.retransSegs;
    }

    public long getInErrs() {
        return this.inErrs;
    }

    public long getOutRsts() {
        return this.outRsts;
    }

    void copyTo(Tcp copy) {
        copy.activeOpens = this.activeOpens;
        copy.passiveOpens = this.passiveOpens;
        copy.attemptFails = this.attemptFails;
        copy.estabResets = this.estabResets;
        copy.currEstab = this.currEstab;
        copy.inSegs = this.inSegs;
        copy.outSegs = this.outSegs;
        copy.retransSegs = this.retransSegs;
        copy.inErrs = this.inErrs;
        copy.outRsts = this.outRsts;
    }

    public Map toMap() {
        Map map = new HashMap();
        String stractiveOpens = String.valueOf(this.activeOpens);
        if (!"-1".equals(stractiveOpens)) {
            map.put("ActiveOpens", stractiveOpens);
        }
        String strpassiveOpens = String.valueOf(this.passiveOpens);
        if (!"-1".equals(strpassiveOpens)) {
            map.put("PassiveOpens", strpassiveOpens);
        }
        String strattemptFails = String.valueOf(this.attemptFails);
        if (!"-1".equals(strattemptFails)) {
            map.put("AttemptFails", strattemptFails);
        }
        String strestabResets = String.valueOf(this.estabResets);
        if (!"-1".equals(strestabResets)) {
            map.put("EstabResets", strestabResets);
        }
        String strcurrEstab = String.valueOf(this.currEstab);
        if (!"-1".equals(strcurrEstab)) {
            map.put("CurrEstab", strcurrEstab);
        }
        String strinSegs = String.valueOf(this.inSegs);
        if (!"-1".equals(strinSegs)) {
            map.put("InSegs", strinSegs);
        }
        String stroutSegs = String.valueOf(this.outSegs);
        if (!"-1".equals(stroutSegs)) {
            map.put("OutSegs", stroutSegs);
        }
        String strretransSegs = String.valueOf(this.retransSegs);
        if (!"-1".equals(strretransSegs)) {
            map.put("RetransSegs", strretransSegs);
        }
        String strinErrs = String.valueOf(this.inErrs);
        if (!"-1".equals(strinErrs)) {
            map.put("InErrs", strinErrs);
        }
        String stroutRsts = String.valueOf(this.outRsts);
        if (!"-1".equals(stroutRsts)) {
            map.put("OutRsts", stroutRsts);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
