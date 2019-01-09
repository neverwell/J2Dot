package com.nemo.game.util;

import com.nemo.game.GameContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {
    //1分钟的秒时间
    public static final long ONE_MINUTE_IN_SECONDS = 60L;
    //1小时的分钟时间
    public static final long ONE_HOUR_IN_MINUTES = 60L;
    //一分钟毫秒时长
    public static final long ONE_MINUTE_IN_MILLISECONDS = ONE_MINUTE_IN_SECONDS * 1000;
    //一小时的毫秒时长
    public static final long ONE_HOUR_IN_MILLISECONDS = 60L * ONE_MINUTE_IN_MILLISECONDS;
    //一天的毫秒时长
    public static final long ONE_DAY_IN_MILLISECONDS = 24L * ONE_HOUR_IN_MILLISECONDS;
    //一天的分钟时长
    public static final int ONE_DAY_IN_MIN = (int) (24 * ONE_MINUTE_IN_SECONDS);
    //一天的秒时长
    public static final long ONE_DAY_IN_SECONDS = ONE_DAY_IN_MIN * ONE_MINUTE_IN_SECONDS;
    //1秒的时长（毫秒）
    public static final long ONE_MILLS = 1000L;

    //格式
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //判断两个时间是否是同一天
    public static boolean isSameDay(long sourceTime, long targetTime) {
        Instant instant1 = Instant.ofEpochMilli(sourceTime); //2018-12-25T11:23:14.565Z
        LocalDateTime localDateTime1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault()); //2018-12-25T11:23:14.565
        long day1 = localDateTime1.getLong(ChronoField.EPOCH_DAY); //距离1970年间隔

        Instant instant2 = Instant.ofEpochMilli(targetTime);
        LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
        long day2 = localDateTime2.getLong(ChronoField.EPOCH_DAY);

        return day1 == day2;
    }

    //获取今天已经过去的分钟数
    public static int getTodayOfMinute() {
        int nowOfMinutes = getNowOfMinutes();
        int zeroMinuteFromNow = dayZeroMinuteFromNow();
        return nowOfMinutes - zeroMinuteFromNow;
    }

    //判断指定的时间是否是今天
    public static boolean isToday(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.toLocalDate().isEqual(LocalDate.now()); //LocalData.now()模板 2018-12-25
    }

    //获取今天零点的分钟数
    public static int dayZeroMinuteFromNow() {
        LocalDateTime localDataTime = LocalDate.now().atStartOfDay(); //2018-12-25T00:00
        return (int) (localDataTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000);
    }

    //获取今天零点的秒数
    public static int dayZeroSecondsFromNow() {
        return (int) (dayZeroMillsFromNow() / ONE_MILLS);
    }

    public static long dayZeroMillsFromNow() {
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    //获取time（毫秒）日零点毫秒时间
    public static long dayZeroMillsFromTime(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime dt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    //获取time（秒）日零点秒时间
    public static int dayZeroSecondsFromTime(long time) {
        return (int) (dayZeroMillsFromTime(time * 1000L) / 1000);
    }

    //获取指定日期的时间戳
    public static long getTimeInMillis(int year, int month, int day, int hour, int minute, int second, int milliSecond) {
        LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, minute, second, milliSecond * 1000_000);
        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getNowOfMills() {
        return System.currentTimeMillis();
    }

    public static int getNowOfSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static int getNowOfMinutes() {
        return (int) (System.currentTimeMillis() / 1000 / 60);
    }

    //获取现在距离time的间隔天数 同一天为0
    public static int getNatureDayFromTime(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        long day = ldt.getLong(ChronoField.EPOCH_DAY);

        long nowDay = LocalDate.now().getLong(ChronoField.EPOCH_DAY);
        return (int) (nowDay - day);
    }

    //获取指定时间的自然天数 from1970
    public static long getDay(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return ldt.getLong(ChronoField.EPOCH_DAY);
    }

    //获取开服天数，开服首日算作第一天
    public static int getOpenServerDay() {
        return (int) (LocalDateTime.now().getLong(ChronoField.EPOCH_DAY) - GameContext.getOpenTime().getLong(ChronoField.EPOCH_DAY) + 1);
    }

    //获取合服天数，合服首日算作第一天
    public static int getCombineServerDay() {
        if (!GameContext.isCombined()) {
            return 0;
        }
        return (int) (LocalDateTime.now().getLong(ChronoField.EPOCH_DAY) - GameContext.getCombineTime().getLong(ChronoField.EPOCH_DAY) + 1);
    }

    //格式化时间
    public static String timeFormat(long time, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        return ldt.format(dtf);
    }

    public static String timeFormat(LocalDateTime time, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        return time.format(dtf);
    }

    //解析时间 毫秒
    public static long timeParse(String text, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);

        LocalDateTime ldt = LocalDateTime.parse(text, dtf);
        return ldt.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
    }

    //将指定的毫秒转化成方便人识别的字符串
    public static String toHumanString(long time) {
        StringBuilder sb = new StringBuilder();
        //获取剩余天数
        int day = (int) (time / ONE_DAY_IN_MILLISECONDS);
        //1天及以上的显示剩余天
        if(day > 0) {
            sb.append(day).append("天");
            time -= (day * ONE_DAY_IN_MILLISECONDS);
        }
        int hour = (int) (time / ONE_HOUR_IN_MILLISECONDS);
        //1小时及以上或者前面显示了天数则后面需要小时
        if(hour > 0 || sb.length() > 0) {
            sb.append(hour).append("小时");
            time -= (hour * ONE_HOUR_IN_MILLISECONDS);
        }
        int minute = (int) (time / ONE_MINUTE_IN_MILLISECONDS);
        if(minute > 0 || sb.length() > 0) {
            sb.append(minute).append("分");
            time -= (minute * ONE_MINUTE_IN_MILLISECONDS);
        }
        sb.append(time / 1000).append("秒");
        return sb.toString();
    }

    //判断今天是否为同一个月
    public static boolean isThisMonth(int oldMonth) {
        return LocalDate.now().getMonthValue() == oldMonth;
    }

    //判断今天是否为某年的同一周
    public static boolean isSameWeek(long oldTime) {
        LocalDate now = LocalDate.now();
        Instant instant = Instant.ofEpochMilli(oldTime);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return now.getYear() == dateTime.getYear()
                && now.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == dateTime.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

    //验证日期字符串是否是YYYY-MM-dd格式或者YYYYMMdd
    public static boolean checkDateFormat(String str) {
        boolean flag = false;
        String regex = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regex);
        Matcher isNo = pattern1.matcher(str);
        if(isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    //获取当前时间的小时数
    public static int getNowHour() {
        return LocalDateTime.now().getHour();
    }

    //获取每天0点开始，每过point个小时的时间戳  间隔小时，例如:间隔2小时，就传2，单位：小时
    public static long getNowEvenTime(int point){
        if(point <= 0){
            return 0;
        }
        int hourTest = getNowHour();
        int mo = hourTest / point;
        int hour = (mo + 1) * point; //下一个间隔时间
        int dayAdd = 0;
        if(hour >= 24) { //跨天
            hour = 0;
            //TODO  可能增加几天
            dayAdd = 1;
        }

        LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth(), hour, 0, 0);
        if(dayAdd > 0) {
            localDateTime = localDateTime.plusDays(dayAdd);
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    public static LocalDateTime getDateTimeOfMillis(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    //通过时间秒毫秒数判断两个时间的间隔天数
    public static int betweenDay(long time1, long time2) {
        return (int) (Math.abs(time1 - time2) / ONE_DAY_IN_MILLISECONDS);
    }

    public static void main(String[] args) {

    }
}
