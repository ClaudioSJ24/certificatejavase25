package guiaSE2125;

import SEJava21.class21.*;
import otherp.TestOther;

import java.io.*;

public class JavaSE21 {



    public static void main(String[] args) throws IOException {

        //test1();//resultado 1 2 3 4
//        test2();//resultado 1 2 3 4 en cualquier orden
//        String color = getColor(Type.MEDIUM);
//        IO.println("VALUE ENUM -> "+color);

//        float v = parseFloat("00.0");
//        IO.println("RESULT > "+v);

//        test5();
//        test6();
//        test7();
//            test10();
//        test11();
//            test12();

//        test14();
//        test15();
//        test16();
//        test17();
//        test18();
//        test20();
//        test22();
//        test23();
    }
    static void test1() throws IOException {

        try {
            method1();//se ejecuta: imprime 1
            method2();//se ejecuta: imprime 1 y Como FileNotFoundException es subclase de IOException se lanza la FileNotFoundException

        } catch (IOException e) {

            IO.println(3);
            return;
        }finally {
            IO.println(4);
        }

        //El IO.println(5) nunca se ejecuta, porque el return dentro del catch termina el método (después del finally)
        IO.println(5);
    }

    private static void method1(){

        IO.println(1);

        if (false) {//nunca se cumple
            throw new IndexOutOfBoundsException();
        }

    }
    private static void method2() throws FileNotFoundException {

        IO.println(2);

        if (true) {//siempre se cumple
            throw new FileNotFoundException();
        }

    }





    /**
     * La variable classA contiene un ibjeto de tipo C
     * Y C es subclase de B se cumplen las condiciones de los dos primeros if
     */
    static void test6(){

        B classB = new C();
        A classA = classB;


        if (classA instanceof B b) {
            b.b();
        }
        if (classA instanceof C c) {
            c.c();
        }
        if (classA instanceof D d) {
            d.d();
        }

    }








    static void test14(){

        TestOther testOther = new TestOther("Java");
        IO.println("VALUE TEST > "+testOther);
    }















}
