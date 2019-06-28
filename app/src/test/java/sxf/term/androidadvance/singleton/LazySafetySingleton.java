package sxf.term.androidadvance.singleton;

/**
 * @author by sunzhongda
 * @date 2019-06-28
 */
public class LazySafetySingleton {

    private static LazySafetySingleton mInstance;

    private LazySafetySingleton() {
    }

    public static synchronized LazySafetySingleton getInstance() {
        if (mInstance == null) {
            mInstance = new LazySafetySingleton();
        }
        return mInstance;
    }

    public static LazySafetySingleton obtain() {
        synchronized (LazySafetySingleton.class) {
            if (mInstance == null) {
                mInstance = new LazySafetySingleton();
            }
            return mInstance;
        }
    }
}
