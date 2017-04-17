package com.xcommon.utils;

import java.security.MessageDigest;

/**
 * Created by Photostsrs on 2016/9/14.
 */
public class StringUtil {
    public static String md5Str(String name) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(name.getBytes());
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
