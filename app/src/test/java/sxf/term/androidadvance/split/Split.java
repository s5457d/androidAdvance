package sxf.term.androidadvance.split;

import org.junit.Test;

/**
 * @author by sunzhongda
 * @date 2019/5/7
 */
public class Split {

    @Test
    public void test(){
        String[] aa = "aaa||bbb||ccc".split("\\|\\|");
        for (String anAa : aa) {
            System.out.println("--" + anAa);
        }
    }
}
