package com.moredian.onpremise.core.model.info;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.moredian.onpremise.core.common.enums.AttendanceRecordResultEnum;
import com.moredian.onpremise.core.model.domain.AttendanceGroupTime;
import com.moredian.onpremise.core.utils.MyDateUtils;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/AttendanceTimeCheckInfo.class */
public class AttendanceTimeCheckInfo implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) AttendanceTimeCheckInfo.class);
    private static final long serialVersionUID = -807836458208434475L;
    private static final long MILLS_OF_MINUTE = 60000;
    private int workHours;
    private int earliestWorkHours;
    private int latestWorkHours;
    private int restHours;
    private int earliestRestHours;
    private int latestRestHours;
    private int attendanceHours;
    private int workMinutes;
    private int restMinutes;
    private int attendanceMinutes;
    private Long attendanceTime;
    private Date date;
    private Date workDate;
    private Date restDate;
    private Long workRuleTime;
    private Long restRuleTime;
    private Long earliestWorkRuleTime;
    private Long earliestRestRuleTime;
    private Long latestWorkRuleTime;
    private Long latestRestRuleTime;
    private boolean isWork;
    private boolean isRest;
    private boolean isAcrossDay;
    private Map<String, Long> ruleTime;
    private Long beginWorkStartTime;
    private Long beginWorkEndTime;
    private Long finishWorkStartTime;
    private Long finishWorkEndTime;

    public void setWorkHours(int workHours) {
        this.workHours = workHours;
    }

    public void setEarliestWorkHours(int earliestWorkHours) {
        this.earliestWorkHours = earliestWorkHours;
    }

    public void setLatestWorkHours(int latestWorkHours) {
        this.latestWorkHours = latestWorkHours;
    }

    public void setRestHours(int restHours) {
        this.restHours = restHours;
    }

    public void setEarliestRestHours(int earliestRestHours) {
        this.earliestRestHours = earliestRestHours;
    }

    public void setLatestRestHours(int latestRestHours) {
        this.latestRestHours = latestRestHours;
    }

    public void setAttendanceHours(int attendanceHours) {
        this.attendanceHours = attendanceHours;
    }

    public void setWorkMinutes(int workMinutes) {
        this.workMinutes = workMinutes;
    }

    public void setRestMinutes(int restMinutes) {
        this.restMinutes = restMinutes;
    }

    public void setAttendanceMinutes(int attendanceMinutes) {
        this.attendanceMinutes = attendanceMinutes;
    }

    public void setAttendanceTime(Long attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public void setRestDate(Date restDate) {
        this.restDate = restDate;
    }

    public void setWorkRuleTime(Long workRuleTime) {
        this.workRuleTime = workRuleTime;
    }

    public void setRestRuleTime(Long restRuleTime) {
        this.restRuleTime = restRuleTime;
    }

    public void setEarliestWorkRuleTime(Long earliestWorkRuleTime) {
        this.earliestWorkRuleTime = earliestWorkRuleTime;
    }

    public void setEarliestRestRuleTime(Long earliestRestRuleTime) {
        this.earliestRestRuleTime = earliestRestRuleTime;
    }

    public void setLatestWorkRuleTime(Long latestWorkRuleTime) {
        this.latestWorkRuleTime = latestWorkRuleTime;
    }

    public void setLatestRestRuleTime(Long latestRestRuleTime) {
        this.latestRestRuleTime = latestRestRuleTime;
    }

    public void setWork(boolean isWork) {
        this.isWork = isWork;
    }

    public void setRest(boolean isRest) {
        this.isRest = isRest;
    }

    public void setAcrossDay(boolean isAcrossDay) {
        this.isAcrossDay = isAcrossDay;
    }

    public void setRuleTime(Map<String, Long> ruleTime) {
        this.ruleTime = ruleTime;
    }

    public void setBeginWorkStartTime(Long beginWorkStartTime) {
        this.beginWorkStartTime = beginWorkStartTime;
    }

    public void setBeginWorkEndTime(Long beginWorkEndTime) {
        this.beginWorkEndTime = beginWorkEndTime;
    }

    public void setFinishWorkStartTime(Long finishWorkStartTime) {
        this.finishWorkStartTime = finishWorkStartTime;
    }

    public void setFinishWorkEndTime(Long finishWorkEndTime) {
        this.finishWorkEndTime = finishWorkEndTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceTimeCheckInfo)) {
            return false;
        }
        AttendanceTimeCheckInfo other = (AttendanceTimeCheckInfo) o;
        if (!other.canEqual(this) || getWorkHours() != other.getWorkHours() || getEarliestWorkHours() != other.getEarliestWorkHours() || getLatestWorkHours() != other.getLatestWorkHours() || getRestHours() != other.getRestHours() || getEarliestRestHours() != other.getEarliestRestHours() || getLatestRestHours() != other.getLatestRestHours() || getAttendanceHours() != other.getAttendanceHours() || getWorkMinutes() != other.getWorkMinutes() || getRestMinutes() != other.getRestMinutes() || getAttendanceMinutes() != other.getAttendanceMinutes()) {
            return false;
        }
        Object this$attendanceTime = getAttendanceTime();
        Object other$attendanceTime = other.getAttendanceTime();
        if (this$attendanceTime == null) {
            if (other$attendanceTime != null) {
                return false;
            }
        } else if (!this$attendanceTime.equals(other$attendanceTime)) {
            return false;
        }
        Object this$date = getDate();
        Object other$date = other.getDate();
        if (this$date == null) {
            if (other$date != null) {
                return false;
            }
        } else if (!this$date.equals(other$date)) {
            return false;
        }
        Object this$workDate = getWorkDate();
        Object other$workDate = other.getWorkDate();
        if (this$workDate == null) {
            if (other$workDate != null) {
                return false;
            }
        } else if (!this$workDate.equals(other$workDate)) {
            return false;
        }
        Object this$restDate = getRestDate();
        Object other$restDate = other.getRestDate();
        if (this$restDate == null) {
            if (other$restDate != null) {
                return false;
            }
        } else if (!this$restDate.equals(other$restDate)) {
            return false;
        }
        Object this$workRuleTime = getWorkRuleTime();
        Object other$workRuleTime = other.getWorkRuleTime();
        if (this$workRuleTime == null) {
            if (other$workRuleTime != null) {
                return false;
            }
        } else if (!this$workRuleTime.equals(other$workRuleTime)) {
            return false;
        }
        Object this$restRuleTime = getRestRuleTime();
        Object other$restRuleTime = other.getRestRuleTime();
        if (this$restRuleTime == null) {
            if (other$restRuleTime != null) {
                return false;
            }
        } else if (!this$restRuleTime.equals(other$restRuleTime)) {
            return false;
        }
        Object this$earliestWorkRuleTime = getEarliestWorkRuleTime();
        Object other$earliestWorkRuleTime = other.getEarliestWorkRuleTime();
        if (this$earliestWorkRuleTime == null) {
            if (other$earliestWorkRuleTime != null) {
                return false;
            }
        } else if (!this$earliestWorkRuleTime.equals(other$earliestWorkRuleTime)) {
            return false;
        }
        Object this$earliestRestRuleTime = getEarliestRestRuleTime();
        Object other$earliestRestRuleTime = other.getEarliestRestRuleTime();
        if (this$earliestRestRuleTime == null) {
            if (other$earliestRestRuleTime != null) {
                return false;
            }
        } else if (!this$earliestRestRuleTime.equals(other$earliestRestRuleTime)) {
            return false;
        }
        Object this$latestWorkRuleTime = getLatestWorkRuleTime();
        Object other$latestWorkRuleTime = other.getLatestWorkRuleTime();
        if (this$latestWorkRuleTime == null) {
            if (other$latestWorkRuleTime != null) {
                return false;
            }
        } else if (!this$latestWorkRuleTime.equals(other$latestWorkRuleTime)) {
            return false;
        }
        Object this$latestRestRuleTime = getLatestRestRuleTime();
        Object other$latestRestRuleTime = other.getLatestRestRuleTime();
        if (this$latestRestRuleTime == null) {
            if (other$latestRestRuleTime != null) {
                return false;
            }
        } else if (!this$latestRestRuleTime.equals(other$latestRestRuleTime)) {
            return false;
        }
        if (isWork() != other.isWork() || isRest() != other.isRest() || isAcrossDay() != other.isAcrossDay()) {
            return false;
        }
        Object this$ruleTime = getRuleTime();
        Object other$ruleTime = other.getRuleTime();
        if (this$ruleTime == null) {
            if (other$ruleTime != null) {
                return false;
            }
        } else if (!this$ruleTime.equals(other$ruleTime)) {
            return false;
        }
        Object this$beginWorkStartTime = getBeginWorkStartTime();
        Object other$beginWorkStartTime = other.getBeginWorkStartTime();
        if (this$beginWorkStartTime == null) {
            if (other$beginWorkStartTime != null) {
                return false;
            }
        } else if (!this$beginWorkStartTime.equals(other$beginWorkStartTime)) {
            return false;
        }
        Object this$beginWorkEndTime = getBeginWorkEndTime();
        Object other$beginWorkEndTime = other.getBeginWorkEndTime();
        if (this$beginWorkEndTime == null) {
            if (other$beginWorkEndTime != null) {
                return false;
            }
        } else if (!this$beginWorkEndTime.equals(other$beginWorkEndTime)) {
            return false;
        }
        Object this$finishWorkStartTime = getFinishWorkStartTime();
        Object other$finishWorkStartTime = other.getFinishWorkStartTime();
        if (this$finishWorkStartTime == null) {
            if (other$finishWorkStartTime != null) {
                return false;
            }
        } else if (!this$finishWorkStartTime.equals(other$finishWorkStartTime)) {
            return false;
        }
        Object this$finishWorkEndTime = getFinishWorkEndTime();
        Object other$finishWorkEndTime = other.getFinishWorkEndTime();
        return this$finishWorkEndTime == null ? other$finishWorkEndTime == null : this$finishWorkEndTime.equals(other$finishWorkEndTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceTimeCheckInfo;
    }

    public int hashCode() {
        int result = (1 * 59) + getWorkHours();
        int result2 = (((((((((((((((((result * 59) + getEarliestWorkHours()) * 59) + getLatestWorkHours()) * 59) + getRestHours()) * 59) + getEarliestRestHours()) * 59) + getLatestRestHours()) * 59) + getAttendanceHours()) * 59) + getWorkMinutes()) * 59) + getRestMinutes()) * 59) + getAttendanceMinutes();
        Object $attendanceTime = getAttendanceTime();
        int result3 = (result2 * 59) + ($attendanceTime == null ? 43 : $attendanceTime.hashCode());
        Object $date = getDate();
        int result4 = (result3 * 59) + ($date == null ? 43 : $date.hashCode());
        Object $workDate = getWorkDate();
        int result5 = (result4 * 59) + ($workDate == null ? 43 : $workDate.hashCode());
        Object $restDate = getRestDate();
        int result6 = (result5 * 59) + ($restDate == null ? 43 : $restDate.hashCode());
        Object $workRuleTime = getWorkRuleTime();
        int result7 = (result6 * 59) + ($workRuleTime == null ? 43 : $workRuleTime.hashCode());
        Object $restRuleTime = getRestRuleTime();
        int result8 = (result7 * 59) + ($restRuleTime == null ? 43 : $restRuleTime.hashCode());
        Object $earliestWorkRuleTime = getEarliestWorkRuleTime();
        int result9 = (result8 * 59) + ($earliestWorkRuleTime == null ? 43 : $earliestWorkRuleTime.hashCode());
        Object $earliestRestRuleTime = getEarliestRestRuleTime();
        int result10 = (result9 * 59) + ($earliestRestRuleTime == null ? 43 : $earliestRestRuleTime.hashCode());
        Object $latestWorkRuleTime = getLatestWorkRuleTime();
        int result11 = (result10 * 59) + ($latestWorkRuleTime == null ? 43 : $latestWorkRuleTime.hashCode());
        Object $latestRestRuleTime = getLatestRestRuleTime();
        int result12 = (((((((result11 * 59) + ($latestRestRuleTime == null ? 43 : $latestRestRuleTime.hashCode())) * 59) + (isWork() ? 79 : 97)) * 59) + (isRest() ? 79 : 97)) * 59) + (isAcrossDay() ? 79 : 97);
        Object $ruleTime = getRuleTime();
        int result13 = (result12 * 59) + ($ruleTime == null ? 43 : $ruleTime.hashCode());
        Object $beginWorkStartTime = getBeginWorkStartTime();
        int result14 = (result13 * 59) + ($beginWorkStartTime == null ? 43 : $beginWorkStartTime.hashCode());
        Object $beginWorkEndTime = getBeginWorkEndTime();
        int result15 = (result14 * 59) + ($beginWorkEndTime == null ? 43 : $beginWorkEndTime.hashCode());
        Object $finishWorkStartTime = getFinishWorkStartTime();
        int result16 = (result15 * 59) + ($finishWorkStartTime == null ? 43 : $finishWorkStartTime.hashCode());
        Object $finishWorkEndTime = getFinishWorkEndTime();
        return (result16 * 59) + ($finishWorkEndTime == null ? 43 : $finishWorkEndTime.hashCode());
    }

    public String toString() {
        return "AttendanceTimeCheckInfo(workHours=" + getWorkHours() + ", earliestWorkHours=" + getEarliestWorkHours() + ", latestWorkHours=" + getLatestWorkHours() + ", restHours=" + getRestHours() + ", earliestRestHours=" + getEarliestRestHours() + ", latestRestHours=" + getLatestRestHours() + ", attendanceHours=" + getAttendanceHours() + ", workMinutes=" + getWorkMinutes() + ", restMinutes=" + getRestMinutes() + ", attendanceMinutes=" + getAttendanceMinutes() + ", attendanceTime=" + getAttendanceTime() + ", date=" + getDate() + ", workDate=" + getWorkDate() + ", restDate=" + getRestDate() + ", workRuleTime=" + getWorkRuleTime() + ", restRuleTime=" + getRestRuleTime() + ", earliestWorkRuleTime=" + getEarliestWorkRuleTime() + ", earliestRestRuleTime=" + getEarliestRestRuleTime() + ", latestWorkRuleTime=" + getLatestWorkRuleTime() + ", latestRestRuleTime=" + getLatestRestRuleTime() + ", isWork=" + isWork() + ", isRest=" + isRest() + ", isAcrossDay=" + isAcrossDay() + ", ruleTime=" + getRuleTime() + ", beginWorkStartTime=" + getBeginWorkStartTime() + ", beginWorkEndTime=" + getBeginWorkEndTime() + ", finishWorkStartTime=" + getFinishWorkStartTime() + ", finishWorkEndTime=" + getFinishWorkEndTime() + ")";
    }

    public int getWorkHours() {
        return this.workHours;
    }

    public int getEarliestWorkHours() {
        return this.earliestWorkHours;
    }

    public int getLatestWorkHours() {
        return this.latestWorkHours;
    }

    public int getRestHours() {
        return this.restHours;
    }

    public int getEarliestRestHours() {
        return this.earliestRestHours;
    }

    public int getLatestRestHours() {
        return this.latestRestHours;
    }

    public int getAttendanceHours() {
        return this.attendanceHours;
    }

    public int getWorkMinutes() {
        return this.workMinutes;
    }

    public int getRestMinutes() {
        return this.restMinutes;
    }

    public int getAttendanceMinutes() {
        return this.attendanceMinutes;
    }

    public Long getAttendanceTime() {
        return this.attendanceTime;
    }

    public Date getDate() {
        return this.date;
    }

    public Date getWorkDate() {
        return this.workDate;
    }

    public Date getRestDate() {
        return this.restDate;
    }

    public Long getWorkRuleTime() {
        return this.workRuleTime;
    }

    public Long getRestRuleTime() {
        return this.restRuleTime;
    }

    public Long getEarliestWorkRuleTime() {
        return this.earliestWorkRuleTime;
    }

    public Long getEarliestRestRuleTime() {
        return this.earliestRestRuleTime;
    }

    public Long getLatestWorkRuleTime() {
        return this.latestWorkRuleTime;
    }

    public Long getLatestRestRuleTime() {
        return this.latestRestRuleTime;
    }

    public boolean isWork() {
        return this.isWork;
    }

    public boolean isRest() {
        return this.isRest;
    }

    public boolean isAcrossDay() {
        return this.isAcrossDay;
    }

    public Map<String, Long> getRuleTime() {
        return this.ruleTime;
    }

    public Long getBeginWorkStartTime() {
        return this.beginWorkStartTime;
    }

    public Long getBeginWorkEndTime() {
        return this.beginWorkEndTime;
    }

    public Long getFinishWorkStartTime() {
        return this.finishWorkStartTime;
    }

    public Long getFinishWorkEndTime() {
        return this.finishWorkEndTime;
    }

    public AttendanceTimeCheckInfo(AttendanceGroupTime groupTime, Long attendanceTime) {
        this.attendanceTime = attendanceTime;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(attendanceTime.longValue());
        this.workDate = MyDateUtils.getDate(attendanceTime.longValue());
        this.restDate = MyDateUtils.getDate(attendanceTime.longValue());
        this.workHours = MyDateUtils.getHours(groupTime.getAttendanceBeginTime());
        this.earliestWorkHours = MyDateUtils.getHours(MyDateUtils.formatDate(MyDateUtils.addMinutes(MyDateUtils.parseDate(groupTime.getAttendanceBeginTime(), "HH:mm"), -groupTime.getAttendanceBeginBefore().intValue()), "HH:mm"));
        this.latestWorkHours = MyDateUtils.getHours(MyDateUtils.formatDate(MyDateUtils.addMinutes(MyDateUtils.parseDate(groupTime.getAttendanceBeginTime(), "HH:mm"), groupTime.getAttendanceBeginAfter().intValue()), "HH:mm"));
        this.restHours = MyDateUtils.getHours(groupTime.getAttendanceEndTime());
        this.earliestRestHours = MyDateUtils.getHours(MyDateUtils.formatDate(MyDateUtils.addMinutes(MyDateUtils.parseDate(groupTime.getAttendanceEndTime(), "HH:mm"), -groupTime.getAttendanceEndBefore().intValue()), "HH:mm"));
        this.latestRestHours = MyDateUtils.getHours(MyDateUtils.formatDate(MyDateUtils.addMinutes(MyDateUtils.parseDate(groupTime.getAttendanceEndTime(), "HH:mm"), groupTime.getAttendanceEndAfter().intValue()), "HH:mm"));
        this.attendanceHours = calendar.get(11);
        this.workMinutes = MyDateUtils.getMinutes(groupTime.getAttendanceBeginTime());
        this.restMinutes = MyDateUtils.getMinutes(groupTime.getAttendanceEndTime());
        this.attendanceMinutes = calendar.get(12);
        this.beginWorkStartTime = Long.valueOf(MyDateUtils.addMinutes(MyDateUtils.parseDate(groupTime.getAttendanceBeginTime(), "HH:mm"), -groupTime.getAttendanceBeginBefore().intValue()).getTime() + 86400000);
        this.beginWorkEndTime = Long.valueOf(MyDateUtils.addMinutes(MyDateUtils.parseDate(groupTime.getAttendanceBeginTime(), "HH:mm"), groupTime.getAttendanceBeginAfter().intValue()).getTime() + 86400000);
        this.finishWorkStartTime = Long.valueOf(MyDateUtils.addMinutes(MyDateUtils.parseDate(groupTime.getAttendanceEndTime(), "HH:mm"), -groupTime.getAttendanceEndBefore().intValue()).getTime() + 86400000);
        this.finishWorkEndTime = Long.valueOf(MyDateUtils.addMinutes(MyDateUtils.parseDate(groupTime.getAttendanceEndTime(), "HH:mm"), groupTime.getAttendanceEndAfter().intValue()).getTime() + 86400000);
        this.isWork = calculateIsWork();
        this.isRest = calculateIsRest();
        this.isAcrossDay = calculateIsAcrossDay();
        logger.info("=====attendanceTime :{} , attendanceHours :{} , attendanceMinutes :{}", attendanceTime, Integer.valueOf(this.attendanceHours), Integer.valueOf(this.attendanceMinutes));
        logger.info("=====workHours :{} , earliestWorkHours :{}, latestWorkHours:{},workMinutes:{}", Integer.valueOf(this.workHours), Integer.valueOf(this.earliestWorkHours), Integer.valueOf(this.latestWorkHours), Integer.valueOf(this.workMinutes));
        logger.info("=====restHours :{} , earliestRestHours :{}, latestRestHours:{} ,restMinutes :{}", Integer.valueOf(this.restHours), Integer.valueOf(this.earliestRestHours), Integer.valueOf(this.latestRestHours), Integer.valueOf(this.restMinutes));
        logger.info("=====isWork :{} , isRest :{}", Boolean.valueOf(this.isWork), Boolean.valueOf(this.isRest));
        logger.info("=====BeginWork :{} ~ {} , FinishWork :{} ~ {}", this.beginWorkStartTime, this.beginWorkEndTime, this.finishWorkStartTime, this.finishWorkEndTime);
        if (this.isWork) {
            this.ruleTime = calculateWorkRuleTime();
        } else if (this.isRest) {
            this.ruleTime = calculateRestRuleTime();
        }
        if (this.ruleTime != null) {
            this.restRuleTime = this.ruleTime.get("restRuleTime");
            this.workRuleTime = this.ruleTime.get("workRuleTime");
            this.earliestRestRuleTime = Long.valueOf(this.restRuleTime.longValue() - (groupTime.getAttendanceEndBefore().intValue() * 60000));
            this.earliestWorkRuleTime = Long.valueOf(this.workRuleTime.longValue() - (groupTime.getAttendanceBeginBefore().intValue() * 60000));
            this.latestWorkRuleTime = Long.valueOf(this.workRuleTime.longValue() + (groupTime.getAttendanceBeginAfter().intValue() * 60000));
            this.latestRestRuleTime = Long.valueOf(this.restRuleTime.longValue() + (groupTime.getAttendanceEndAfter().intValue() * 60000));
        }
        logger.info("=====restRuleTime :{} , workRuleTime :{}, earliestRestRuleTime :{}, earliestWorkRuleTime :{}, latestWorkRuleTime :{}, latestRestRuleTime :{}", this.restRuleTime, this.workRuleTime, this.earliestRestRuleTime, this.earliestWorkRuleTime, this.latestWorkRuleTime, this.latestRestRuleTime);
    }

    public boolean calculateIsWork() {
        boolean result = false;
        Long attendanceTimeTemp = Long.valueOf(MyDateUtils.parseDate(this.attendanceHours + ":" + this.attendanceMinutes, "HH:mm").getTime() + 86400000);
        if ((this.beginWorkStartTime.longValue() <= 57600000 || this.beginWorkEndTime.longValue() <= 57600000) && attendanceTimeTemp.longValue() >= 57600000 && this.beginWorkStartTime.longValue() <= attendanceTimeTemp.longValue() - 86400000 && attendanceTimeTemp.longValue() - 86400000 <= this.beginWorkEndTime.longValue()) {
            result = true;
        }
        if (this.beginWorkStartTime.longValue() <= attendanceTimeTemp.longValue() && attendanceTimeTemp.longValue() <= this.beginWorkEndTime.longValue()) {
            result = true;
        }
        return result;
    }

    public boolean calculateIsRest() {
        boolean result = false;
        Long attendanceTimeTemp = Long.valueOf(MyDateUtils.parseDate(this.attendanceHours + ":" + this.attendanceMinutes, "HH:mm").getTime() + 86400000);
        if ((this.finishWorkStartTime.longValue() >= 144000000 || this.finishWorkEndTime.longValue() >= 144000000) && attendanceTimeTemp.longValue() >= 57600000 && this.finishWorkStartTime.longValue() <= attendanceTimeTemp.longValue() + 86400000 && attendanceTimeTemp.longValue() + 86400000 <= this.finishWorkEndTime.longValue()) {
            result = true;
        }
        if (this.finishWorkStartTime.longValue() <= attendanceTimeTemp.longValue() && attendanceTimeTemp.longValue() <= this.finishWorkEndTime.longValue()) {
            result = true;
        }
        return result;
    }

    public boolean calculateIsAcrossDay() {
        return this.earliestWorkHours < 0 || this.latestWorkHours > 24 || this.earliestRestHours < 0 || this.latestRestHours > 24;
    }

    private void packageAttendanceHours() {
        if (isWork() && this.earliestWorkHours < 0 && this.earliestWorkHours + 24 < this.attendanceHours) {
            this.attendanceHours -= 24;
        }
        if (isRest() && this.earliestRestHours < 0 && this.earliestRestHours + 24 < this.attendanceHours) {
            this.attendanceHours -= 24;
        }
    }

    private Map<String, Long> calculateWorkRuleTime() {
        Long workRuleTime = 0L;
        Long restRuleTime = 0L;
        if (this.earliestWorkHours < 0) {
            if ((0 > this.attendanceHours || this.attendanceHours >= this.latestWorkHours) && this.earliestWorkHours + 24 < this.attendanceHours && this.attendanceHours < 24) {
                this.workDate = MyDateUtils.addDays(this.workDate, 1);
                this.restDate = MyDateUtils.addDays(this.restDate, 1);
            }
        } else if (this.latestWorkHours > 24) {
            if (0 <= this.attendanceHours && this.attendanceHours < this.latestWorkHours - 24) {
                this.workDate = MyDateUtils.addDays(this.workDate, -1);
            } else if (this.earliestWorkHours < this.attendanceHours && this.attendanceHours < 24) {
                this.restDate = MyDateUtils.addDays(this.restDate, 1);
            }
        }
        try {
            workRuleTime = Long.valueOf(MyDateUtils.parseDate(MyDateUtils.formatDate(this.workDate, "yyyy-MM-dd") + SymbolConstants.SPACE_SYMBOL + this.workHours + ":" + this.workMinutes + ":" + TarConstants.VERSION_POSIX, "yyyy-MM-dd HH:mm:ss").getTime());
            restRuleTime = Long.valueOf(MyDateUtils.parseDate(MyDateUtils.formatDate(this.restDate, "yyyy-MM-dd") + SymbolConstants.SPACE_SYMBOL + this.restHours + ":" + this.restMinutes + ":59", "yyyy-MM-dd HH:mm:ss").getTime());
        } catch (Exception e) {
        }
        Map<String, Long> result = new HashMap<>(2);
        result.put("workRuleTime", workRuleTime);
        result.put("restRuleTime", restRuleTime);
        return result;
    }

    private Map<String, Long> calculateRestRuleTime() {
        Long workRuleTime = 0L;
        Long restRuleTime = 0L;
        if (this.earliestRestHours < 0) {
            if (0 <= this.attendanceHours && this.attendanceHours < this.latestRestHours) {
                this.workDate = MyDateUtils.addDays(this.workDate, -1);
            } else if (this.earliestRestHours + 24 < this.attendanceHours && this.attendanceHours < 24) {
                this.restDate = MyDateUtils.addDays(this.restDate, 1);
            }
        } else if (this.latestRestHours > 24) {
            if (0 <= this.attendanceHours && this.attendanceHours < this.latestRestHours - 24) {
                this.restDate = MyDateUtils.addDays(this.restDate, -1);
                this.workDate = MyDateUtils.addDays(this.workDate, -1);
            } else if (this.earliestRestHours >= this.attendanceHours || this.attendanceHours < 24) {
            }
        }
        try {
            workRuleTime = Long.valueOf(MyDateUtils.parseDate(MyDateUtils.formatDate(this.workDate, "yyyy-MM-dd") + SymbolConstants.SPACE_SYMBOL + this.workHours + ":" + this.workMinutes + ":" + TarConstants.VERSION_POSIX, "yyyy-MM-dd HH:mm:ss").getTime());
            restRuleTime = Long.valueOf(MyDateUtils.parseDate(MyDateUtils.formatDate(this.restDate, "yyyy-MM-dd") + SymbolConstants.SPACE_SYMBOL + this.restHours + ":" + this.restMinutes + ":59", "yyyy-MM-dd HH:mm:ss").getTime());
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
        }
        logger.info("===workRuleTime :{} , restRuleTime :{}", workRuleTime, restRuleTime);
        Map<String, Long> result = new HashMap<>(2);
        result.put("workRuleTime", workRuleTime);
        result.put("restRuleTime", restRuleTime);
        return result;
    }

    public int getAttendanceRestResult() {
        int attendanceResult = AttendanceRecordResultEnum.INVALID.getValue();
        if (this.attendanceHours > this.restHours && this.attendanceHours < this.latestRestHours) {
            attendanceResult = AttendanceRecordResultEnum.SUCCESS.getValue();
        } else if (this.attendanceHours == this.restHours && this.attendanceMinutes >= this.restMinutes) {
            attendanceResult = AttendanceRecordResultEnum.SUCCESS.getValue();
        } else if (this.attendanceHours == this.latestRestHours && this.attendanceMinutes <= this.restMinutes) {
            attendanceResult = AttendanceRecordResultEnum.SUCCESS.getValue();
        } else if (this.attendanceHours > this.earliestRestHours && this.attendanceHours < this.restHours) {
            attendanceResult = AttendanceRecordResultEnum.EARLY.getValue();
        } else if (this.attendanceHours == this.earliestRestHours && this.attendanceMinutes >= this.restMinutes) {
            attendanceResult = AttendanceRecordResultEnum.EARLY.getValue();
        } else if (this.attendanceHours == this.restHours && this.attendanceMinutes < this.restMinutes) {
            attendanceResult = AttendanceRecordResultEnum.EARLY.getValue();
        } else {
            logger.info(" invalid rest attendance time :{}:{}", Integer.valueOf(this.attendanceHours), Integer.valueOf(this.attendanceMinutes));
        }
        return attendanceResult;
    }

    public int getAttendanceWorkResult() {
        int attendanceResult = AttendanceRecordResultEnum.INVALID.getValue();
        if (this.workHours > this.attendanceHours && this.attendanceHours > this.earliestWorkHours) {
            attendanceResult = AttendanceRecordResultEnum.SUCCESS.getValue();
        } else if (this.workHours == this.attendanceHours && this.workMinutes >= this.attendanceMinutes) {
            attendanceResult = AttendanceRecordResultEnum.SUCCESS.getValue();
        } else if (this.attendanceHours == this.earliestWorkHours && this.workMinutes <= this.attendanceMinutes) {
            attendanceResult = AttendanceRecordResultEnum.SUCCESS.getValue();
        } else if (this.workHours < this.attendanceHours && this.attendanceHours < this.latestWorkHours) {
            attendanceResult = AttendanceRecordResultEnum.LATE.getValue();
        } else if (this.workHours == this.attendanceHours && this.workMinutes < this.attendanceMinutes) {
            attendanceResult = AttendanceRecordResultEnum.LATE.getValue();
        } else if (this.attendanceHours == this.latestWorkHours && this.workMinutes >= this.attendanceMinutes) {
            attendanceResult = AttendanceRecordResultEnum.LATE.getValue();
        } else {
            logger.info(" invalid work attendance time :{}:{}", Integer.valueOf(this.attendanceHours), Integer.valueOf(this.attendanceMinutes));
        }
        return attendanceResult;
    }
}
