package com.example.cheng.enet;

import android.util.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cheng on 2017/6/5.
 */

public class Utils {
    /**
     * 计算字符串的md5
     *
     * @param str byte数组
     * @return md5值
     */
    public static String md5(byte str[]) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(str);
            byte[] digest = m.digest();
            BigInteger bigString = new BigInteger(1, digest);
            String hashtext = bigString.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decode(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}
