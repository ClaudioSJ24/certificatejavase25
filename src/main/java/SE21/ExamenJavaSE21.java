package SE21;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

public class ExamenJavaSE21 {

    public static void main(String[] args) {

    }

    static void test1(){

        String a="Hello! Java";
        System.out.print(a.indexOf("Java"));
        a.replace("Hello!", "Wellcome!");
        System.out.print(a.indexOf("Java"));
        StringBuilder b=new StringBuilder(a);
        System.out.print(b.indexOf("Java"));
    }

    static void test2(){

        ReentrantReadWriteLock rw1=new ReentrantReadWriteLock();
        Lock r1=rw1.readLock();
        Lock w1=rw1.writeLock();
        String result=null;
        try {
            r1.lock();
            if(w1.tryLock()) {
                result="locked";
            }
        }catch(Exception ex) {
            result="broken";
        }finally {
            r1.unlock();
            if(rw1.isWriteLocked()) {
                w1.unlock();
                result="unlocked";
            }
        }
        System.out.print(result);
    }
    static void test3(){

        int a=0,b=1,c=2;
        if(Math.round(b/c)<b/c&b!=b/++a) {
            c+=a+++b;
        }else {
            c-=--b+a;
        }
        System.out.println(c);

    }
    static void test4(){

        System.out.println(
                Stream.of(1,2,3,4,5,6,7)
                        .filter(n->n%2!=0)
                        .peek(System.out::print)
                        .dropWhile(n->n<6)
                        .count()
        );
    }
    static void test5(){

    }
    static void test6(){

    }
    static void test7(){

    }
    static void test8(){

    }
    static void test9(){

    }static void test10(){

    }
    static void test11(){

    }static void test12(){

    }static void test13(){

    }static void test14(){

    }
    static void test15(){

    }

    static void test16(){

    }
    static void test17(){

    }
    static void test18(){

    }
    static void test19(){

    }
    static void test20(){

    }
    static void test21(){

    }
    static void test22(){

    }
    static void test23(){

    }static void test24(){

    }
    static void test25(){

    }static void test26(){

    }static void test27(){

    }static void test28(){

    }
    static void test29(){

    }

    static void test30(){

    }
    static void test31(){

    }
    static void test32(){

    }
    static void test33(){

    }
    static void test34(){

    }
    static void test35(){

    }
    static void test36(){

    }
    static void test37(){

    }static void test38(){

    }
    static void test39(){

    }static void test40(){

    }static void test41(){

    }static void test42(){

    }







}
