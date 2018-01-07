package com.smx.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by wangh on 2017/10/26.
 */

public class RandomStringUtil {

    public static String getRandomJianHan(int len)
    {
        String ret="";
        for(int i=0;i<len;i++){
            String str = null;
            int hightPos, lowPos;
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39)));
            lowPos = (161 + Math.abs(random.nextInt(93)));
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try
            {
                str = new String(b, "GBk");
            }
            catch (UnsupportedEncodingException ex)
            {
                ex.printStackTrace();
            }
            ret+=str;
        }
        return ret;
    }
}
