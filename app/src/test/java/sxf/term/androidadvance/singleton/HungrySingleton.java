package sxf.term.androidadvance.singleton;

/**
 * @author by sunzhongda
 * @date 2019-06-28
 */
public class HungrySingleton {

    private static final HungrySingleton mHungrySingleton = new HungrySingleton();

    private HungrySingleton() {
        System.out.println("HungrySingleton is Create!");
    }

    public static HungrySingleton getmHungrySingleton() {
        return mHungrySingleton;
    }
}
