package sxf.term.androidadvance.singleton;

/**
 * @author by sunzhongda
 * @date 2019-06-28
 *
 * 1.利用jvm的本身的机制，保证了数据的线程安全。 static final  static保证只有一个，final保证不可被修改
 * 2.没有使用关键字syn。
 * 3.SingletonHolder是私有的。既有延迟加载，还有性能，线程安全
 */
public class StaticInnerSingleton {

    private StaticInnerSingleton() {
    }

    public static StaticInnerSingleton getInstance() {
        return SingletonHolder.mInstance;
    }

    private static class SingletonHolder {
        private static final StaticInnerSingleton mInstance = new StaticInnerSingleton();
    }
}
