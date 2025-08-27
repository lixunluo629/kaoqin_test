package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/TerminalShowDataRequest.class */
public class TerminalShowDataRequest implements Serializable {
    private static final long serialVersionUID = -8285096134957361216L;
    public static final IOTModelType MODEL_TYPE = IOTModelType.SHOW_DATA_REQUEST;
    private String tipsText;
    private String tipsSpeech;
    private Integer time;

    public void setTipsText(String tipsText) {
        this.tipsText = tipsText;
    }

    public void setTipsSpeech(String tipsSpeech) {
        this.tipsSpeech = tipsSpeech;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalShowDataRequest)) {
            return false;
        }
        TerminalShowDataRequest other = (TerminalShowDataRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$tipsText = getTipsText();
        Object other$tipsText = other.getTipsText();
        if (this$tipsText == null) {
            if (other$tipsText != null) {
                return false;
            }
        } else if (!this$tipsText.equals(other$tipsText)) {
            return false;
        }
        Object this$tipsSpeech = getTipsSpeech();
        Object other$tipsSpeech = other.getTipsSpeech();
        if (this$tipsSpeech == null) {
            if (other$tipsSpeech != null) {
                return false;
            }
        } else if (!this$tipsSpeech.equals(other$tipsSpeech)) {
            return false;
        }
        Object this$time = getTime();
        Object other$time = other.getTime();
        return this$time == null ? other$time == null : this$time.equals(other$time);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalShowDataRequest;
    }

    public int hashCode() {
        Object $tipsText = getTipsText();
        int result = (1 * 59) + ($tipsText == null ? 43 : $tipsText.hashCode());
        Object $tipsSpeech = getTipsSpeech();
        int result2 = (result * 59) + ($tipsSpeech == null ? 43 : $tipsSpeech.hashCode());
        Object $time = getTime();
        return (result2 * 59) + ($time == null ? 43 : $time.hashCode());
    }

    public String toString() {
        return "TerminalShowDataRequest(tipsText=" + getTipsText() + ", tipsSpeech=" + getTipsSpeech() + ", time=" + getTime() + ")";
    }

    public String getTipsText() {
        return this.tipsText;
    }

    public String getTipsSpeech() {
        return this.tipsSpeech;
    }

    public Integer getTime() {
        return this.time;
    }

    public TerminalShowDataRequest(String tipsText, String tipsSpeech, Integer time) {
        this.tipsText = tipsText;
        this.tipsSpeech = tipsSpeech;
        this.time = time;
    }
}
