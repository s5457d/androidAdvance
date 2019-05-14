package sxf.term.androidadvance.classloader;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author by sunzhongda
 * @date 2019/4/23
 */
public class main {

    @Test
    public void main() {
        DiskClassLoader diskClassLoader = new DiskClassLoader("/Users/sunzhongda/Desktop");
        try {
            Class<?> c = diskClassLoader.loadClass("sxf.term.androidadvance.classloader.Jobs");
            if (c != null) {
                Object obj = c.newInstance();
                System.out.println(obj.getClass().getClassLoader());
                Method method = c.getDeclaredMethod("say", null);
                method.invoke(obj, null);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
