package sxf.term.androidadvance.singleton;

/**
 * @author by sunzhongda
 * @date 2019-06-28
 */
public class DclSingleton {

//    private static DclSingleton mInstance = null;
    // volatile 禁止指令重排序 在工作线程没有副本，mInstance只在主内存中
    private static volatile DclSingleton mInstance = null;

    private DclSingleton() {
    }

    public static DclSingleton getInstance() {
        //避免其他线程进入不必要的同步代码块
        if (mInstance == null) {
            synchronized (DclSingleton.class) {
                if (mInstance == null) {
                    mInstance = new DclSingleton();//并非是一个原子操作  1 分配内存  2 执行构造方法   3 mInstance指向内存
                }
            }
        }
        return mInstance;
    }
}
