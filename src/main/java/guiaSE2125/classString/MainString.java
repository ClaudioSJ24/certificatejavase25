package guiaSE2125.classString;

import java.io.IO;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MainString {
    public static void main(String[] args) {

//        question1();
//        question11();
//        question2();
//        question3();
//        question4();
        question5();
        question15();
        question16();
//        question6();
    }

    /**
     * Traza paso a paso
     * a = "Hello! Java". Los índices son: H=0, e=1, l=2, l=3, o=4, !=5, espacio=6, J=7. Así que a.indexOf("Java") devuelve 7.
     * a.replace("Hello!", "Wellcome!") devuelve un nuevo String, pero el resultado se descarta. String es inmutable, así que a sigue siendo "Hello! Java".
     * El segundo a.indexOf("Java") vuelve a dar 7.
     * b = new StringBuilder(a) contiene "Hello! Java", y StringBuilder también tiene indexOf(String), que devuelve 7.
     *
     * Como se usa print y no println, los tres valores salen pegados en una sola línea: 777.
     *
     * Punto clave
     *
     * Si el resultado de replace se hubiera asignado (a = a.replace(...)), a sería "Wellcome! Java" y el segundo valor sería 9, así que la salida habría sido 799.
     */
    static void question1(){
        IO.println("-----EXECUTE QUESTIO 1-----");
        String a="Hello! Java";
        showString(a);
        System.out.print(a.indexOf("Java"));
        IO.println("   LENGTH A > "+a.length());
        a.replace("Hello!", "Wellcome!");
        System.out.print(a.indexOf("Java"));
        IO.println("   LENGTH A > "+a.length());
        StringBuilder b=new StringBuilder(a);
        System.out.print(b.indexOf("Java"));
        IO.println("   LENGTH A > "+a.length());
    }
    static void question11(){
        IO.println("-----EXECUTE QUESTIO 11-----");

        String a="Hello! Java";
        showString(a);
        System.out.print(a.indexOf("Java"));
        IO.println("   LENGTH A > "+a.length());
        a = a.replace("Hello!", "Wellcome!");
        showString(a);
        System.out.print(a.indexOf("Java"));
        IO.println("   LENGTH A > "+a.length());
        StringBuilder b=new StringBuilder(a);
        System.out.print(b.indexOf("Java"));
        IO.println("   LENGTH A > "+a.length());
    }

    static void showString(String s){

        for (int i = 0; i < s.length(); i++) {

            IO.println("INDEX["+i+"] --> "+s.charAt(i));
        }


    }
    /**
     * Traza paso a paso
     * myStr = "Hello Java".
     * myText1: el delimitador de cierre """ está en la misma línea que el texto, así que no hay salto de línea final. Vale "Hello Java".
     * myText2: el delimitador de cierre está en una línea aparte, así que el bloque termina con un salto de línea. Vale "Hello Java\n".
     *
     * Comparaciones
     *
     * myStr.equals(myText1) → "Hello Java" frente a "Hello Java": true.
     * myStr.equals(myText2) → falta el \n: false.
     * myText1.equals(myText2) → también difieren por el \n: false.
     * myText1.intern() == myText2.intern() → son contenidos distintos, así que sus versiones internadas son objetos distintos del pool: false.
     * Puntos clave
     * La posición del """ de cierre decide si hay salto de línea final. Es la diferencia clave entre myText1 y myText2.
     * intern() devuelve la referencia del pool para ese contenido. Dos cadenas con igual contenido darían == verdadero tras intern(),
     * pero aquí el contenido no es igual.
     * Con print y println mezclados, todo sale en una sola línea, con : como separador.
     */

    static void question2(){

        String myStr="Hello Java";
        IO.println("------VALUE MYSTRING------");
        showString(myStr);
        String myText1="""
		Hello Java""";
        IO.println("------VALUE MYTEST1------");
        showString(myText1);
        String myText2="""
		Hello Java
		""";
        IO.println("------VALUE MYTEST2------");
        showString(myText2);
        System.out.print(myStr.equals(myText1)+":");
        System.out.print(myStr.equals(myText2)+":");
        System.out.print(myText1.equals(myText2)+":");
        System.out.println(myText1.intern()==myText2.intern());
    }

    /**
     * Ambos son text blocks (bloques de texto con """).
     * La diferencia está en el \ al final de la línea de string2, que suprime el salto de línea.
     *
     * string1: el delimitador de cierre está en la línea siguiente,
     * así que el contenido incluye un \n al final → "mycad\n" (longitud 6).
     * string2: el \ al final de la línea escapa el salto de línea,
     * por lo que no se incluye → "mycad" (longitud 5).
     * trim() elimina espacios en blanco al inicio y al final
     */
    static void question3(){

        String string1 = """
                mycad
                """;
        IO.println("------VALUE STRING1------");
        showString(string1);
        String string2 = """
                mycad\
                """;
        IO.println("------VALUE STRING2------");
        showString(string2);

        IO.println("VALUE > string1.equals(string2)"+string1.equals(string2));
        IO.println("VALUE > string1.length() > string2.length()"+(string1.length() > string2.length()));
        IO.println("VALUE > string1.trim().length() == string2.trim().length()"+(string1.trim().length() == string2.trim().length()));

    }

    /**
     *Se aplicaría primero el accept de c2 y después el de c1,
     * por lo que se imprime el texto dos veces, la primera tal cual y la segunda todo en mayúsculas
     */

    static void question4(){

        Consumer<String> c1= s->System.out.println(s.toUpperCase());
        Consumer<String> c2=s->System.out.println(s);
        c2.andThen(c1).accept("hello");
    }

    /**
     * Traza paso a paso
     * cad.toUpperCase() devuelve un nuevo String, pero el resultado se descarta. String es inmutable, así que cad sigue siendo "hello".
     * s1 es un StringBuilder con "hello".
     * s2 = s1 no crea un objeto nuevo: ambas variables apuntan al mismo objeto.
     * s3 es un StringBuilder distinto, con el mismo contenido "hello".
     *
     * Comparaciones
     *
     * cad.equals(s1) → String.equals solo devuelve true si el argumento también es un String. Aquí es un StringBuilder, así que es false, aunque el texto sea igual.
     * s1.equals(s2) → StringBuilder no sobrescribe equals, así que usa el de Object (igualdad de referencia). Como s1 y s2 son el mismo objeto, es true.
     * s2.equals(s3) → son objetos distintos, así que es false, aunque tengan el mismo contenido.
     * Punto clave
     *
     * StringBuilder no compara por contenido con equals. Para comparar el texto puedes usar s2.compareTo(s3) == 0 o s2.toString().equals(s3.toString()).
     */
    static void question5(){
        String cad="hello";
        cad.toUpperCase();
        StringBuilder s1=new StringBuilder(cad);
        StringBuilder s2=s1;
        StringBuilder s3=new StringBuilder(s2.toString());
        System.out.println(cad.equals(s1));
        System.out.println(s1.equals(s2));
        System.out.println(s2.equals(s3));

    }

    static void question15(){
        String cad="hello";
        cad = cad.toUpperCase();
        StringBuilder s1=new StringBuilder(cad);
        StringBuilder s2=s1;
        StringBuilder s3=new StringBuilder(s2.toString());
        System.out.println(cad.compareTo(String.valueOf(s1)) == 0);
        System.out.println(s1.compareTo(s2) == 0);
        System.out.println(s2.compareTo(s3) == 0);

    }

    static void question16(){
        String cad="hello";
        cad.toUpperCase();
        StringBuilder s1=new StringBuilder(cad);
        StringBuilder s2=s1;
        StringBuilder s3=new StringBuilder(s2.toString());
        System.out.println(cad.toString().equals(s1.toString()));
        System.out.println(s1.toString().equals(s2.toString()));
        System.out.println(s2.toString().equals(s3.toString()));

    }
    /**
     * Traza paso a paso
     * peek(n -> Arrays.sort(n)) ordena cada subarreglo in situ:
     * {a,u,c,d} → {a,c,d,u}
     * {c,x,n} → {c,n,x}
     * {k,j,b} → {b,j,k}
     * {n,x,c} → {c,n,x}
     * {c,a,u,d} → {a,c,d,u}
     * map(...) une cada uno en un String: "acdu", "cnx", "bjk", "cnx", "acdu".
     * distinct() elimina repetidos y deja "acdu", "cnx", "bjk".
     * groupingBy(String::length) agrupa por longitud:
     * 4 → ["acdu"]
     * 3 → ["cnx", "bjk"]
     * new TreeMap(cars).keySet() recorre las claves en orden ascendente: 3, luego 4.
     * Clave 3 → size() = 2
     * Clave 4 → size() = 1
     *
     * Por eso se imprime 2 y luego 1: 21.
     */
    static void question6(){
        String[][] groups= {{"a","u","c","d"},
                {"c","x","n"},
                {"k","j","b"},
                {"n","x","c"},
                {"c","a","u","d"}};
        Map<Integer, List<String>> cars= Arrays.stream(groups)
                .peek(n->Arrays.sort(n))
                .map(a->Arrays.stream(a).collect(Collectors.joining("")))
                .distinct()
                .collect(Collectors.groupingBy(String::length));
        new TreeMap(cars).keySet().forEach(k->System.out.print(cars.get(k).size()));

    }
}
