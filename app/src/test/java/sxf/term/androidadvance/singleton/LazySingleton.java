package sxf.term.androidadvance.singleton;

/**
 * @author by sunzhongda
 * @date 2019-06-28
 */
public class LazySingleton {

    private static LazySingleton mLazySingleton;

    private LazySingleton() {
    }

    public static LazySingleton getInstace() {
        if (mLazySingleton == null) {
            mLazySingleton = new LazySingleton();
        }
        return mLazySingleton;
    }
}
