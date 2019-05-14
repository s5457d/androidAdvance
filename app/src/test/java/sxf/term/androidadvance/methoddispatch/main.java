package sxf.term.androidadvance.methoddispatch;

import org.junit.Test;

/**
 * @author by sunzhongda
 * @date 2019/5/5
 */
public class main {

    @Test
    public void test() {
        Dispatch dispatch = new SubClass();
        // 调用哪一个，取决于编译时 调用dispatch 方法重载
        // 运行的结果，取决于运行时的实际类型
        pringHello(dispatch);
    }

    private void pringHello(Dispatch dispatch) {
        System.out.println("Hello " + dispatch.getName());
    }

    private void pringHello(SubClass subClass) {
        System.out.println("Hello " + subClass.getName());
    }
}
