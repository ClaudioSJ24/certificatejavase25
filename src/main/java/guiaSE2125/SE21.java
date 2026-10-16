package guiaSE2125;

import SE21.classse21.*;

import java.io.*;
import java.util.concurrent.ExecutionException;

public class SE21 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        question1();
//        question2();
//        question3();
//        question4();
//        question5();
//        question6();
//        question7();
//        question8();
//        question9();
//        question10();
//        question11();
//        question13();
//        question14();
//        question15();
//        question24();
//        question26();
//        question28();
//        question17();

    }







    /**
     *
     */
    static void question4(){
        TestSE21.IClass inner = new TestSE21().new IClass();
    }



    /**
     * Traza paso a paso
     * Se crea p con description = "Cookie" y price = 3.99f.
     * Serialización: writeObject(p) guarda description.
     * El campo price es transient, así que no se guarda.
     * averagePrice es static, así que tampoco forma parte del estado del objeto.
     * Deserialización: readObject(ObjectInputStream) en Product está declarado como public,
     * pero el mecanismo de serialización solo invoca un readObject que sea private.
     * Al ser public, se ignora y se usa la deserialización por defecto.
     * Con la deserialización por defecto, description se restaura como "Cookie" y price,
     * al ser transient, queda en el valor por defecto de float: 0.0. La línea price = averagePrice nunca se ejecuta.
     * toString() imprime description + " " + price + " " + averagePrice, es decir, Cookie 0.0 2.99.
     */
    static void question6(){

        Product p=new Product("Cookie", 3.99f);
        try {
            try (ObjectOutputStream out=new ObjectOutputStream(
                    new FileOutputStream("p.obj"))){
                out.writeObject(p);
            }
            try (ObjectInputStream in=new ObjectInputStream(
                    new FileInputStream("p.obj"))){
                p=(Product)in.readObject();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println(p);
    }
























    static void question24(){
        Animal a=new Pet("Scooby",Animal.Type.DOG);
        System.out.println(a);
    }





    /**
     * Salida del código
     * method two
     * Por qué
     *
     * La resolución de sobrecarga en Java se hace en tres fases, y se queda con la primera que encuentra un método aplicable:
     *
     * Fase 1: sin boxing ni varargs (solo identidad y ensanchamiento primitivo).
     * Fase 2: permitiendo boxing/unboxing.
     * Fase 3: permitiendo varargs.
     *
     * Los argumentos son 3 y 6, dos int.
     *
     * Fase 1
     *
     * m(Number, int): pasar un int a Number exige boxing a Integer, así que no es aplicable en esta fase.
     * m(long, long): int se ensancha a long sin boxing, así que sí es aplicable.
     * m(Integer, Integer): exige boxing, no aplicable.
     * m(int...): es varargs, no se considera en esta fase.
     *
     * Solo m(long, long) es aplicable, así que se elige y se imprime method two. Las fases 2 y 3 ni se alcanzan.
     *
     * Puntos clave
     * El ensanchamiento primitivo gana al boxing: int → long se prefiere sobre int → Integer.
     * Varargs es siempre el último recurso, así que method four solo se elegiría si ningún otro método fuera aplicable.
     * Si se eliminara m(long, long), la fase 2 tendría dos candidatos: m(Number, int) y m(Integer, Integer).
     * En ese caso la llamada sería ambigua y no compilaría, porque ninguno es más específico que el otro.
     */
    static void question29(){

        P43 p43=new P43();
        p43.m(3,6);
    }




   


}
