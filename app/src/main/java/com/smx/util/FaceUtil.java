package com.smx.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.smx.R;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vivo on 2018/5/14.
 */

public class FaceUtil {

    static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

    static Map<String, Integer> map;

    static {
        map = new LinkedHashMap<>();
        map.put("[呲牙]", R.mipmap.f000);
        map.put("[调皮]", R.mipmap.f001);
        map.put("[流汗]", R.mipmap.f002);
        map.put("[偷笑]", R.mipmap.f003);
        map.put("[再见]", R.mipmap.f004);
        map.put("[敲打]", R.mipmap.f005);
        map.put("[擦汗]", R.mipmap.f006);
        map.put("[猪头]", R.mipmap.f007);
        map.put("[玫瑰]", R.mipmap.f008);
        map.put("[流泪]", R.mipmap.f009);
        map.put("[大哭]", R.mipmap.f010);
        map.put("[嘘]", R.mipmap.f011);
        map.put("[酷]", R.mipmap.f012);
        map.put("[抓狂]", R.mipmap.f013);
        map.put("[委屈]", R.mipmap.f014);
        map.put("[便便]", R.mipmap.f015);
        map.put("[炸弹]", R.mipmap.f016);
        map.put("[菜刀]", R.mipmap.f017);
        map.put("[可爱]", R.mipmap.f018);
        map.put("[色]", R.mipmap.f019);
        map.put("[害羞]", R.mipmap.f020);
        map.put("[得意]", R.mipmap.f021);
        map.put("[吐]", R.mipmap.f022);
        map.put("[微笑]", R.mipmap.f023);
        map.put("[发怒]", R.mipmap.f024);
        map.put("[尴尬]", R.mipmap.f025);
        map.put("[惊恐]", R.mipmap.f026);
        map.put("[冷汗]", R.mipmap.f027);
        map.put("[爱心]", R.mipmap.f028);
        map.put("[示爱]", R.mipmap.f029);
        map.put("[白眼]", R.mipmap.f030);
        map.put("[傲慢]", R.mipmap.f031);
        map.put("[难过]", R.mipmap.f032);
        map.put("[惊讶]", R.mipmap.f033);
        map.put("[疑问]", R.mipmap.f034);
        map.put("[睡]", R.mipmap.f035);
        map.put("[亲亲]", R.mipmap.f036);
        map.put("[憨笑]", R.mipmap.f037);
        map.put("[爱情]", R.mipmap.f038);
        map.put("[衰]", R.mipmap.f039);
    }

    public static Map<String, Integer> getFaceMap() {
        return map;
    }



    //解析表情
    public static CharSequence convertNormalStringToSpannableString(Context context, int width, int height, String message) {
        // TODO Auto-generated method stub
        String hackTxt;
        if (message.startsWith("[") && message.endsWith("]")) {
            hackTxt = message + " ";
        } else {
            hackTxt = message;
        }
        SpannableString value = SpannableString.valueOf(hackTxt);

        Matcher localMatcher = EMOTION_URL.matcher(value);
        while (localMatcher.find()) {
            String str2 = localMatcher.group(0);
            int k = localMatcher.start();
            int m = localMatcher.end();
            // k = str2.lastIndexOf("[");
            // Log.i("way", "str2.length = "+str2.length()+", k = " + k);
            // str2 = str2.substring(k, m);
            if (m - k < 8) {
                if (map.containsKey(str2)) {//map中包含str2这个key
                    int face = map.get(str2);
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), face);
                    if (bitmap != null) {
                        Bitmap newBitmap = BitmapUtils.getScaleBitmap(bitmap, width, height);
                        ImageSpan localImageSpan = new ImageSpan(context, newBitmap, ImageSpan.ALIGN_BASELINE);
                        value.setSpan(localImageSpan, k, m, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        return value;
    }
}
