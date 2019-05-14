package sxf.term.androidadvance.methoddispatch;

/**
 * @author by sunzhongda
 * @date 2019/5/5
 */
class Dispatch {

    public String getName(){
        return "Super";
    }
}

class SubClass extends Dispatch{

    public String getName(){
        return "SubClass";
    }
}
