package sxf.term.androidadvance.lrucache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.LruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import sxf.term.androidadvance.BuildConfig;
import sxf.term.androidadvance.lrucache.disk.DiskLruCache;

/**
 * @author by sunzhongda
 * @date 2019/5/14
 */
public class ImageCache {

    private Context mContext;
    private LruCache<String, Bitmap> memoryCache;
    private DiskLruCache mDiskLruCache;
    BitmapFactory.Options mOptions = new BitmapFactory.Options();

    private static Set<WeakReference<Bitmap>> reuseablePool;
    private ReferenceQueue<Bitmap> mReferenceQueue;
    private Thread clearReferenceQueue;
    private boolean shutDown;

    private static final ImageCache ourInstance = new ImageCache();

    public static ImageCache getInstance() {
        return ourInstance;
    }


    public void init(Context context, String dir) {
        this.mContext = context.getApplicationContext();

        reuseablePool = Collections.synchronizedSet(new HashSet<WeakReference<Bitmap>>());

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        memoryCache = new LruCache<String, Bitmap>(am.getMemoryClass() / 8 * 1024 * 1024) {


            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    return value.getAllocationByteCount();
                }
                return value.getByteCount();
            }


            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                if (oldValue.isMutable()) {
                    reuseablePool.add(new WeakReference<Bitmap>(oldValue, mReferenceQueue));
                } else {
                    oldValue.recycle();
                }
            }
        };


        try {
            mDiskLruCache = DiskLruCache.open(new File(dir), BuildConfig.VERSION_CODE, 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getReferenceQueue();
    }

    private ReferenceQueue<Bitmap> getReferenceQueue() {
        if (mReferenceQueue == null) {
            mReferenceQueue = new ReferenceQueue<Bitmap>();

            clearReferenceQueue = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!shutDown) {
                        try {
                            Reference reference = mReferenceQueue.remove();
                            Bitmap bitmap = (Bitmap) reference.get();
                            if (bitmap != null && !bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            clearReferenceQueue.start();
        }
        return mReferenceQueue;
    }

    public void putBitmapToMemory(String key, Bitmap bitmap) {
        memoryCache.put(key, bitmap);
    }

    public Bitmap getBitmapFromMemory(String key) {
        return memoryCache.get(key);
    }

    public Bitmap getReusable(int w, int h, int inSampleSize) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return null;
        }

        Bitmap reusable = null;
        Iterator<WeakReference<Bitmap>> iterator = reuseablePool.iterator();
        while (iterator.hasNext()) {
            Bitmap bitmap = iterator.next().get();
            if (bitmap != null) {
                if (checkInBitmap(bitmap, w, h, inSampleSize)) {
                    reusable = bitmap;
                    iterator.remove();
                } else {
                    iterator.remove();
                }
            }
        }
        return reusable;
    }

    private boolean checkInBitmap(Bitmap bitmap, int w, int h, int inSampleSize) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return bitmap.getWidth() == w && bitmap.getHeight() == h && inSampleSize == 1;
        }

        if (inSampleSize > 1) {
            w /= inSampleSize;
            h /= inSampleSize;
        }

        int byteCount = w * h * getPixelsCount(bitmap.getConfig());
        return byteCount < bitmap.getAllocationByteCount();
    }

    private int getPixelsCount(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        }
        return 2;
    }

    public void putBitmapToDisk(String key, Bitmap bitmap) {
        DiskLruCache.Snapshot snapshot = null;
        OutputStream os = null;

        try {
            snapshot = mDiskLruCache.get(key);
            if (snapshot == null) {
                DiskLruCache.Editor edit = mDiskLruCache.edit(key);
                if (edit != null) {
                    os = edit.newOutputStream(0);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                    edit.commit();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap getBitmapFromDisk(String key, Bitmap reusable) {
        DiskLruCache.Snapshot snapshot = null;
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            snapshot = mDiskLruCache.get(key);
            if (snapshot == null) {
                return null;
            }
            inputStream = snapshot.getInputStream(0);
            mOptions.inMutable = true;
            mOptions.inBitmap = bitmap;
            bitmap = BitmapFactory.decodeStream(inputStream, null, mOptions);
            if (bitmap != null) {
                memoryCache.put(key, bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
