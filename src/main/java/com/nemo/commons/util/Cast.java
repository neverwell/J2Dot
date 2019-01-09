package com.nemo.commons.util;

public class Cast {

    public static int toInteger(Object str) {
        if (str == null) {
            return 0;
        } else if (str instanceof Number) {
            return ((Number)str).intValue();
        } else {
            return str instanceof Integer ? ((Integer)str).intValue() : toInteger(str.toString());
        }
    }

    public static double toDouble(Object number) {
        if(number == null) {
            return 0.0D;
        } else if(number instanceof Number) {
            return ((Number)number).doubleValue();
        } else if(number instanceof String) {
            String str = (String)number;
            return isNumeric(str) > 0 ? Double.valueOf(str).doubleValue() : 0.0D;
        } else {
            return 0.0D;
        }
    }

    public static long toLong(Object number) {
        if(number == null) {
            return 0L;
        } else if(number instanceof Number) {
            return ((Number)number).longValue();
        } else {
            return number instanceof String ? Long.valueOf((String)number).longValue() : 0L;
        }
    }

    public static int toInteger(String str) {
        if(str == null) {
            return 0;
        } else {
            str = str.trim();
            if(str.length() == 0) {
                return 0;
            } else {
                int i = isNumeric(str);
                if(i == 1) {
                    return Integer.parseInt(str);
                } else {
                    return i == 2 ? Double.valueOf(str).intValue() : 0;
                }
            }
        }
    }

    //是否数值
    public static int isNumeric(String str) {
        if(str == null) {
            return 0;
        } else {
            boolean isdouble = false;
            int i = str.length();

            while (true) {
                char c;
                do {
                    --i; //从末尾位开始取 length-1
                    if(i < 0) {
                        if(isdouble) {
                            return 2;
                        }
                        return 1; //如果读完了都没有'.'也没有非数字字符
                    }
                    c = str.charAt(i);
                } while (i == 0 && c == '_');

                if(c == '.') {
                    if(isdouble) {
                        return 0;
                    }
                    isdouble = true;
                } else if(!Character.isDigit(str.charAt(i))) { //如果含有非数字字符
                    return 0;
                }
            }
        }
    }

    public static long combineInt2Long(int low, int high) {
        return (long)low & 4294967295L | (long)high << 32 & -4294967296L;
    }

    public static int[] stringToInts(String str, String regex) {
        String[] arr = str.split(regex);
        int length = arr.length;
        int[] ret = new int[length];

        for(int i = 0; i < length; i++) {
            ret[i] = toInteger(arr[i]);
        }
        return ret;
    }

    public static void main(String[] args) {
        int ret = toInteger("3.1415");
        System.out.println(ret);
        System.out.println(Long.toBinaryString(combineInt2Long(0b1111, 0b1001)));
        System.out.println(Long.toBinaryString(-4294967296L));
    }
}
