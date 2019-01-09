package com.nemo.game.util;

import com.nemo.game.GameContext;
import com.nemo.game.constant.Symbol;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang3.StringUtils.isNumeric;

//id生成工具
public class IDUtil {
    private static Hashtable<String, String> AREA_CODE = null;

    //2017年1月1日0点整
    public static final long TIME_MILLS_2017_4_1_0_0_0_0 = 1483200000000L;
    //计时器
    private static int id = 0;
    //当前秒数
    private static long curSec = getTimeStampFrom20170101();

    private static final AtomicInteger INSTANCE_ID = new AtomicInteger(10000);
    private static final AtomicInteger BUFFER_ID = new AtomicInteger(1);
    private static final AtomicInteger DUPLICATE_ID = new AtomicInteger(100000);
    //锁对象
    private final static Object OBJ = new Object();

    /**
     * 获取一个唯一ID，ID 是一个64位long，19位服务器id + 29位时间秒数 + 16位自增ID.
     * 1、服务器id出掉符号位以后实际使用18位，如果用到19位，那么生产出来的id会为负数, id允许负数最大支持524287个服务器，否则减半
     * 2、29位秒数最多支持约20年
     * 生成规则：
     * 1、服务器id不变
     * 2、时间随着当前时间更新
     * 3、自增ID如果从1到65000后，自增ID复位，此时需要时间秒数加1防止重复
     * 4、自增后的时间小于了当前时间，那么就更新当前时间
     */
    public static long getId() {
        int nowId;
        long nowCurSec = getTimeStampFrom20170101();
        synchronized (OBJ) {
            //自增ID+1
            id += 1;
            //当前ID赋值id
            nowId = id;

            int max = 65535; //2的16次-1 0x0000到0xFFFF
            if(id > max) {
                //如果ID大于65535 id复位，如果时间不增1，那么会产生重复
                id = 0;
                //每过65535当前秒数加1
                curSec += 1L;
            }

            if(nowCurSec > curSec) {
                //自增后的时间<=当前时间，那么就更新自增时间为当前时间
                curSec = nowCurSec;
            } else {
                //自增时间>当前时间（id获取速度过快，1秒中获取了超过65535个id），那么就以自增时间为准
                nowCurSec = curSec;
            }
        }
        return (GameContext.getServerId() & 0x7_FFFFL) << 45 | (nowCurSec << 16) | (nowId & 0xFFFF);
    }

    //获取基于2017年1月1日0点整的时间秒数
    private static long getTimeStampFrom20170101() {
        return (System.currentTimeMillis() - TIME_MILLS_2017_4_1_0_0_0_0) / 1000;
    }

    public static AtomicInteger getInstanceId() {
        return INSTANCE_ID;
    }

    public static AtomicInteger getBufferId() {
        return BUFFER_ID;
    }

    public static AtomicInteger getDuplicateId() {
        return DUPLICATE_ID;
    }

    //功能：身份证的有效验证 有效：返回"" 无效：返回String信息
    public static String idCardValidate(String idNumber) throws ParseException {
        //18位身份证最后一位识别码
        String[] valCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};

        String[] wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};

        int number_15 = 15;
        int number_18 = 18;
        if (idNumber.length() != number_15 && idNumber.length() != number_18) {
            return "身份证号码长度应该为15位或18位。";
        }

        String ai;
        if (idNumber.length() == number_18) {
            ai = idNumber.substring(0, number_18 - 1);
        } else {
            ai = idNumber.substring(0, 6) + "19" + idNumber.substring(6, number_15); //老身份证 20世纪 19XX年
        }
        if (!isNumeric(ai)) {
            return "身份证15位号码都应为数字；18位号码除最后一位外，都应为数字。";
        }

        // 年份
        String strYear = ai.substring(6, 10);
        // 月份
        String strMonth = ai.substring(10, 12);
        // 日期
        String strDay = ai.substring(12, 14);
        if (!TimeUtil.checkDateFormat(strYear + Symbol.HENGXIAN + strMonth + Symbol.HENGXIAN + strDay)) {
            return "身份证生日无效。";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        //超过150岁非法 未来时间非法
        if ((calendar.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (calendar.getTime().getTime() - s.parse(strYear + Symbol.HENGXIAN + strMonth + Symbol.HENGXIAN + strDay).getTime()) < 0) {
            return "身份证生日不在有效范围。";
        }

        if (Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(strYear) < number_18) {
            return "您属于未成年人,无法通过防沉迷认证";
        }

        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            return "身份证月份无效";
        }
        //这里只简单验证了日期不能超过31天。。。
        int maxMonthDay = 31;
        if (Integer.parseInt(strDay) > maxMonthDay || Integer.parseInt(strDay) == 0) {
            return "身份证日期无效";
        }

        Hashtable<String, String> h = getAreaCode();
        //身份证前两位省代码
        if (h.get(ai.substring(0, 2)) == null) {
            return "身份证地区编码错误。";
        }

        //18位的最后一位识别码
        if (idNumber.length() == number_18) {
            int totalMulAiWi = 0;
            for (int i = 0; i < ai.length(); i++) {
                totalMulAiWi += Integer.parseInt(String.valueOf(ai.charAt(i))) * Integer.parseInt(wi[i]);
            }
            //前17位数值总和取模11
            int modValue = totalMulAiWi % 11;
            String strVerifyCode = valCodeArr[modValue];

            ai = ai + strVerifyCode;
            if (!ai.equals(idNumber)) {
                return "身份证无效，不是合法的身份证号码";
            }
        }
        return "";
    }

    //功能：设置地区编码
    private static Hashtable<String, String> getAreaCode() {
        if (AREA_CODE == null) {
            Hashtable<String, String> table = new Hashtable<>();
            table.put("11", "北京");
            table.put("12", "天津");
            table.put("13", "河北");
            table.put("14", "山西");
            table.put("15", "内蒙古");
            table.put("21", "辽宁");
            table.put("22", "吉林");
            table.put("23", "黑龙江");
            table.put("31", "上海");
            table.put("32", "江苏");
            table.put("33", "浙江");
            table.put("34", "安徽");
            table.put("35", "福建");
            table.put("36", "江西");
            table.put("37", "山东");
            table.put("41", "河南");
            table.put("42", "湖北");
            table.put("43", "湖南");
            table.put("44", "广东");
            table.put("45", "广西");
            table.put("46", "海南");
            table.put("50", "重庆");
            table.put("51", "四川");
            table.put("52", "贵州");
            table.put("53", "云南");
            table.put("54", "西藏");
            table.put("61", "陕西");
            table.put("62", "甘肃");
            table.put("63", "青海");
            table.put("64", "宁夏");
            table.put("65", "新疆");
            table.put("71", "台湾");
            table.put("81", "香港");
            table.put("82", "澳门");
            table.put("91", "国外");
            AREA_CODE = table;
        }
        return AREA_CODE;
    }

    public static void main(String[] args) {
        try {
            String result = idCardValidate("330381199001250012");
            System.out.println(result);
        } catch (Exception e) {

        }
    }
}
