package cn.hollycloud.iplatform.common.utils;

import java.io.UnsupportedEncodingException;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2018-05-15
 * Time: 15:34
 */
public class HexUtils {
    public static String bytesToHexString(byte src) {
        String hex = "";
        int v = src & 0xFF;
        String hv = Integer.toHexString(v).toUpperCase();
        if (hv.length() < 2) {
            hex += "0";
        }
        hex += hv;
        return hex;
    }

    public static String bytesToHexString(byte[] src, boolean format) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            if (format) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    public static String bytesToHexString(byte[] src) {
        return bytesToHexString(src, false);
    }


    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }


    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static void writeLong(long num, byte[] buffer, int pos) {
        for (int ix = 0; ix < Long.BYTES; ++ix) {
            int offset = Long.SIZE - (ix + 1) * Byte.SIZE;
            buffer[ix + pos] = (byte) ((num >> offset) & 0xff);
        }
    }

    public static long bytes2Long(byte[] byteNum, int pos) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix + pos] & 0xff);
        }
        return num;
    }

    public static int twoBytes2Int(byte[] buf, int pos) {
        return ((buf[pos] << 8) & 0xFF00) | (buf[pos + 1] & 0xFF);
    }

    public static int twoBytes2IntLE(byte[] buf, int pos) {
        return ((buf[pos + 1] << 8) & 0xFF00) | (buf[pos] & 0xFF);
    }

    public static void writeTwoBytes(int val, byte[] buffer, int pos) {
        //数据长度    高
        buffer[pos] = (byte) ((val >> 8) & 0xFF);
        //数据长度    低
        buffer[pos + 1] = (byte) (val & 0xFF);
    }

    public static void writeTwoBytesLE(int val, byte[] buffer, int pos) {
        //数据长度    高
        buffer[pos + 1] = (byte) ((val >> 8) & 0xFF);
        //数据长度    低
        buffer[pos] = (byte) (val & 0xFF);
    }

    public static void writeIntLE(int val, byte[] buffer, int pos) {
        buffer[pos + 3] = (byte) ((val >> 24) & 0xFF);
        buffer[pos + 2] = (byte) ((val >> 16) & 0xFF);
        //数据长度    高
        buffer[pos + 1] = (byte) ((val >> 8) & 0xFF);
        //数据长度    低
        buffer[pos] = (byte) (val & 0xFF);
    }

    public static void writeOneByte(int val, byte[] buffer, int pos) {
        buffer[pos] = (byte) (val & 0xFF);
    }

    public static int oneByte2Int(byte[] buf, int pos) {
        return buf[pos] & 0xFF;
    }

    public static int byte2Int(byte data) {
        return data & 0xFF;
    }

    public static int towBytes2IntLE(byte byte1, byte byte2) {
        return ((byte2 << 8) & 0xFF00) | (byte1 & 0xFF);
    }

    public static long fourBytes2LongLE(byte byte1, byte byte2, byte byte3, byte byte4) {
        return ((byte4 << 24) & 0xFF000000) | ((byte3 << 16) & 0xFF0000) | ((byte2 << 8) & 0xFF00) | (byte1 & 0xFF);
    }

    public static long fourBytes2LongLE(byte[] buf, int pos) {
        return ((buf[pos + 3] << 24) & 0xFF000000) | ((buf[pos + 2] << 16) & 0xFF0000) | ((buf[pos + 1] << 8) & 0xFF00) | (buf[pos] & 0xFF);
    }

    public static long fourBytes2Long(byte[] buf, int pos) {
        return ((buf[pos] << 24) & 0xFF000000) | ((buf[pos + 1] << 16) & 0xFF0000) | ((buf[pos + 2] << 8) & 0xFF00) | (buf[pos + 3] & 0xFF);
    }

    public static byte[] stringToAsc(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ascToString(byte[] data) {
        String str = "";
        try {
            str = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

}
