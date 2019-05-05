package sxf.term.androidadvance.nonameinnerclass;

import org.junit.Test;

/**
 * @author by sunzhongda
 * @date 2019/5/5
 */
public class OutClass {


    @Test
    public void test() {

        try {
            Class<?> aClass = Class.forName("sxf.term.androidadvance.nonameinnerclass.OutClass$1");
            // class sxf.term.androidadvance.nonameinnerclass.OutClass$1 所以匿名内部类是有名字的
            System.out.println(aClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new Foo() {
            @Override
            public int bar() {
                return 0;
            }
        }.bar();

    }

    interface Foo {

        int bar();
    }
}





