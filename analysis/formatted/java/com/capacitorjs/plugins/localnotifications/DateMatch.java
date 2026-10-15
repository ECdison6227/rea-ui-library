package com.capacitorjs.plugins.localnotifications;

import androidx.webkit.ProxyConfig;
import java.util.Calendar;
import java.util.Date;

/* JADX INFO: loaded from: classes2.dex */
public class DateMatch {
    private static final String separator = " ";
    private Integer day;
    private Integer hour;
    private Integer minute;
    private Integer month;
    private Integer second;
    private Integer unit = -1;
    private Integer weekday;
    private Integer year;

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer num) {
        this.year = num;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(Integer num) {
        this.month = num;
    }

    public Integer getDay() {
        return this.day;
    }

    public void setDay(Integer num) {
        this.day = num;
    }

    public Integer getWeekday() {
        return this.weekday;
    }

    public void setWeekday(Integer num) {
        this.weekday = num;
    }

    public Integer getHour() {
        return this.hour;
    }

    public void setHour(Integer num) {
        this.hour = num;
    }

    public Integer getMinute() {
        return this.minute;
    }

    public void setMinute(Integer num) {
        this.minute = num;
    }

    public Integer getSecond() {
        return this.second;
    }

    public void setSecond(Integer num) {
        this.second = num;
    }

    private Calendar buildCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(14, 0);
        return calendar;
    }

    public long nextTrigger(Date date) {
        return postponeTriggerIfNeeded(buildCalendar(date), buildNextTriggerTime(date));
    }

    private long postponeTriggerIfNeeded(Calendar calendar, Calendar calendar2) {
        if (calendar2.getTimeInMillis() <= calendar.getTimeInMillis() && this.unit.intValue() != -1) {
            Integer num = -1;
            if (this.unit.intValue() == 1 || this.unit.intValue() == 2) {
                num = 1;
            } else if (this.unit.intValue() == 5) {
                num = 2;
            } else if (this.unit.intValue() == 7) {
                num = 4;
            } else if (this.unit.intValue() == 11) {
                num = 5;
            } else if (this.unit.intValue() == 12) {
                num = 11;
            } else if (this.unit.intValue() == 13) {
                num = 12;
            }
            if (num.intValue() != -1) {
                calendar2.set(num.intValue(), calendar2.get(num.intValue()) + 1);
            }
        }
        return calendar2.getTimeInMillis();
    }

    private Calendar buildNextTriggerTime(Date date) {
        Calendar calendarBuildCalendar = buildCalendar(date);
        Integer num = this.year;
        if (num != null) {
            calendarBuildCalendar.set(1, num.intValue());
            if (this.unit.intValue() == -1) {
                this.unit = 1;
            }
        }
        Integer num2 = this.month;
        if (num2 != null) {
            calendarBuildCalendar.set(2, num2.intValue());
            if (this.unit.intValue() == -1) {
                this.unit = 2;
            }
        }
        Integer num3 = this.day;
        if (num3 != null) {
            calendarBuildCalendar.set(5, num3.intValue());
            if (this.unit.intValue() == -1) {
                this.unit = 5;
            }
        }
        Integer num4 = this.weekday;
        if (num4 != null) {
            calendarBuildCalendar.set(7, num4.intValue());
            if (this.unit.intValue() == -1) {
                this.unit = 7;
            }
        }
        Integer num5 = this.hour;
        if (num5 != null) {
            calendarBuildCalendar.set(11, num5.intValue());
            if (this.unit.intValue() == -1) {
                this.unit = 11;
            }
        }
        Integer num6 = this.minute;
        if (num6 != null) {
            calendarBuildCalendar.set(12, num6.intValue());
            if (this.unit.intValue() == -1) {
                this.unit = 12;
            }
        }
        Integer num7 = this.second;
        if (num7 != null) {
            calendarBuildCalendar.set(13, num7.intValue());
            if (this.unit.intValue() == -1) {
                this.unit = 13;
            }
        }
        return calendarBuildCalendar;
    }

    public String toString() {
        return "DateMatch{year=" + this.year + ", month=" + this.month + ", day=" + this.day + ", weekday=" + this.weekday + ", hour=" + this.hour + ", minute=" + this.minute + ", second=" + this.second + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            DateMatch dateMatch = (DateMatch) obj;
            Integer num = this.year;
            if (num == null ? dateMatch.year != null : !num.equals(dateMatch.year)) {
                return false;
            }
            Integer num2 = this.month;
            if (num2 == null ? dateMatch.month != null : !num2.equals(dateMatch.month)) {
                return false;
            }
            Integer num3 = this.day;
            if (num3 == null ? dateMatch.day != null : !num3.equals(dateMatch.day)) {
                return false;
            }
            Integer num4 = this.weekday;
            if (num4 == null ? dateMatch.weekday != null : !num4.equals(dateMatch.weekday)) {
                return false;
            }
            Integer num5 = this.hour;
            if (num5 == null ? dateMatch.hour != null : !num5.equals(dateMatch.hour)) {
                return false;
            }
            Integer num6 = this.minute;
            if (num6 == null ? dateMatch.minute != null : !num6.equals(dateMatch.minute)) {
                return false;
            }
            Integer num7 = this.second;
            Integer num8 = dateMatch.second;
            if (num7 != null) {
                return num7.equals(num8);
            }
            if (num8 == null) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        Integer num = this.year;
        int iHashCode = (num != null ? num.hashCode() : 0) * 31;
        Integer num2 = this.month;
        int iHashCode2 = (iHashCode + (num2 != null ? num2.hashCode() : 0)) * 31;
        Integer num3 = this.day;
        int iHashCode3 = (iHashCode2 + (num3 != null ? num3.hashCode() : 0)) * 31;
        Integer num4 = this.weekday;
        int iHashCode4 = (iHashCode3 + (num4 != null ? num4.hashCode() : 0)) * 31;
        Integer num5 = this.hour;
        int iHashCode5 = (iHashCode4 + (num5 != null ? num5.hashCode() : 0)) * 31;
        Integer num6 = this.minute;
        int iHashCode6 = iHashCode5 + (num6 != null ? num6.hashCode() : 0) + 31;
        Integer num7 = this.second;
        return iHashCode6 + (num7 != null ? num7.hashCode() : 0);
    }

    public String toMatchString() {
        return (this.year + separator + this.month + separator + this.day + separator + this.weekday + separator + this.hour + separator + this.minute + separator + this.second + separator + this.unit).replace("null", ProxyConfig.MATCH_ALL_SCHEMES);
    }

    public static DateMatch fromMatchString(String str) {
        DateMatch dateMatch = new DateMatch();
        String[] strArrSplit = str.split(separator);
        if (strArrSplit != null && strArrSplit.length == 7) {
            dateMatch.setYear(getValueFromCronElement(strArrSplit[0]));
            dateMatch.setMonth(getValueFromCronElement(strArrSplit[1]));
            dateMatch.setDay(getValueFromCronElement(strArrSplit[2]));
            dateMatch.setWeekday(getValueFromCronElement(strArrSplit[3]));
            dateMatch.setHour(getValueFromCronElement(strArrSplit[4]));
            dateMatch.setMinute(getValueFromCronElement(strArrSplit[5]));
            dateMatch.setUnit(getValueFromCronElement(strArrSplit[6]));
        }
        if (strArrSplit != null && strArrSplit.length == 8) {
            dateMatch.setYear(getValueFromCronElement(strArrSplit[0]));
            dateMatch.setMonth(getValueFromCronElement(strArrSplit[1]));
            dateMatch.setDay(getValueFromCronElement(strArrSplit[2]));
            dateMatch.setWeekday(getValueFromCronElement(strArrSplit[3]));
            dateMatch.setHour(getValueFromCronElement(strArrSplit[4]));
            dateMatch.setMinute(getValueFromCronElement(strArrSplit[5]));
            dateMatch.setSecond(getValueFromCronElement(strArrSplit[6]));
            dateMatch.setUnit(getValueFromCronElement(strArrSplit[7]));
        }
        return dateMatch;
    }

    public static Integer getValueFromCronElement(String str) {
        try {
            return Integer.valueOf(Integer.parseInt(str));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    public Integer getUnit() {
        return this.unit;
    }

    public void setUnit(Integer num) {
        this.unit = num;
    }
}