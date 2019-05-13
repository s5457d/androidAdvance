package sxf.term.androidadvance.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author by sunzhongda
 * @date 2019/5/9
 */
public class BitmapResize {

    /**
     * 调整原始图像大小
     *
     * @param context  application
     * @param id       资源id
     * @param maxW     期望最大宽度
     * @param maxH     期望最大高度
     * @param hasAlpha 是否支持透明度
     * @return bitmap
     */
    public static Bitmap resizeBitmap(Context context, int id, int maxW, int maxH, boolean hasAlpha, Bitmap reusable) {
        Resources resources = context.getResources();
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 拿到系统处理的信息，比如解码高度宽度
        options.inJustDecodeBounds = true;
        // 获取到原解码参数
        BitmapFactory.decodeResource(resources, id, options);

        int w = options.outWidth;
        int h = options.outHeight;

        // 设置缩放系数
        options.inSampleSize = calcuteSampleSize(w, h, maxW, maxH);

        if (!hasAlpha) {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }

        // 根据options设置获取到图片信息
        options.inJustDecodeBounds = false;

        // 设置成可以复用, 重复加载图片，不需要重复开辟内存
        options.inMutable = true;
        options.inBitmap = reusable;
        return BitmapFactory.decodeResource(resources, id, options);
    }

    //返回结果是原来解码的图片的大小  是我们需要的大小的   最接近2的几次方倍
    private static int calcuteSampleSize(int w, int h, int maxW, int maxH) {
        int inSampleSize = 1;
        while (w / inSampleSize > maxW && h / inSampleSize > maxH) {
            inSampleSize *= 2;
        }
        return inSampleSize;
    }
}
