package SEJava21;

import SEJava21.class21.*;
import SEJava21.enums.Type;
import otherp.TestOther;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

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
        test23();
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
     * Crea un pool de 3 hilos y un contador atómico que empieza en 0.
     * Luego envía 4 tareas al pool (el ciclo va de i = 0 a i = 3 inclusive,
     * o sea 4 iteraciones, no 3). Cada tarea incrementa el contador y lo imprime.
     * Imprime Los números 1, 2, 3 y 4, cada uno exactamente una vez, pero el orden no está garantizado.
     */
    static void test2(){

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        AtomicInteger atomicInteger = new AtomicInteger();

        for (int i = 0; i <= 3; i++) {
            //incrementAndGet() es atómico: aunque varios hilos lo llamen a la vez, nunca se repite ni se pierde un valor.
            // Siempre se generan 1, 2, 3, 4.
            executorService.submit(() -> IO.println(atomicInteger.incrementAndGet()+ ""));
        }
        //executorService.shutdown(): No detiene las tareas ya enviadas: todas, incluso la que está en cola, se terminan de ejecutar.
        //Solo impide que se acepten tareas nuevas.
        executorService.shutdown();
    }

    /**
     * Error de compilacion, la variable local b se tiene que inicializar
     */
    static int varTest3;
    static void test3(){

        int b;
        while (varTest3 > 0 && varTest3-- < 1){
           // b--;
        }


    }

    /**
     *
     * @param type
     * @return
     * Dado que devuelve un resultado, debe existir la palabra return y el tipo de devolucion es String.
     * Todos los posibles valores de la Enumeracion deben estar contemplados
     * No se permite el uso de break
     *
     */
    static String getColor(Type type){

        return switch (type){
            case HARD, MEDIUM -> {yield "BLUE";}
            case LIGTH, SOFT -> "WHITE";

        };

//        return switch (type){
//            case HARD, MEDIUM -> "BLUE";
//            default -> "BLACK";
//        };
    }

    /**
     * Ambos son text blocks (bloques de texto con """).
     * La diferencia está en el \ al final de la línea de string2, que suprime el salto de línea.
     *
     * string1: el delimitador de cierre está en la línea siguiente,
     * así que el contenido incluye un \n al final → "mycad\n" (longitud 6).
     * string2: el \ al final de la línea escapa el salto de línea,
     * por lo que no se incluye → "mycad" (longitud 5).
     * trim() elimina espacios en blanco al inicio y al fina
     */
    static void test4(){

        String string1 = """
                mycad
                """;

        String string2 = """
                mycad\
                """;

        IO.println("VALUE > string1.equals(string2)"+string1.equals(string2));
        IO.println("VALUE > string1.length() > string2.length()"+(string1.length() > string2.length()));
        IO.println("VALUE > string1.trim().length() == string2.trim().length()"+(string1.trim().length() == string2.trim().length()));

    }

    /**
     *
     * @param string
     * @return
     * Resultado: 10.0     *
     * Siempre devuelve 10.0f, sin importar el valor de string.
     * Por qué
     * El return 10.0f dentro del finally sobrescribe cualquier valor que se hubiera preparado en el try o en el catch.
     *
     */
    static float parseFloat(String string){

        try {
            return  Float.valueOf(string).floatValue();
        } catch (NumberFormatException e) {
            return Float.NaN;
        }finally {
            return 10.0f;
        }

    }

    /**
     * Al eliminar path2
     * path1 se mueve a la localizacion de path2
     * se imprime DELETE, MOVED
     * Path3 existe al intentar copiar path2 en path3 se provoca una excepcion, imprime ERROR
     */
    static void test5(){

        Path path1 = Path.of("c:\\loco\\test1.txt");
        Path path2 = Path.of("c:\\loco\\test2.txt");
        Path path3 = Path.of("c:\\loco\\test3.txt");

        try {
            Files.delete(path2);
            IO.println("DELETED");
            Files.move(path1, path2);
            IO.println("MOVED");
            Files.copy(path2, path3);
            IO.println("COPIED");
        } catch (Exception e) {
            IO.println("ERROR");;
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

    /**
     * Traza paso a paso
     *
     * Iteración 1 (i = 0)
     *
     * Imprime vALUES I > 0.
     * 0 % 5 = 0 no coincide con ningún caso, así que entra en default -> i, y res = 0.
     * Imprime vALUES RES > 0.
     * El i++ del cuerpo hace i = 1 y el i++ del for hace i = 2.
     *
     * Iteración 2 (i = 2)
     *
     * Imprime vALUES I > 2.
     * 2 % 5 = 2 entra en case 2 -> i*=i. Esto asigna i = 4 y la expresión vale 4, así que res = 4.
     * Imprime vALUES RES > 4.
     * El i++ del cuerpo hace i = 5 y el i++ del for hace i = 6.
     *
     * Fin: 6 <= 5 es falso, así que el ciclo termina.
     */
    static void test7(){

        for (var i = 0; i <= 5; i++) {
            IO.println("vALUES I > "+i);
            var res = switch (i % 5){
                case 2 -> i*=i;
                case 3 -> i++;
                case 1, 4 -> i--;
                default -> i;
            };
            IO.println("vALUES RES > "+res);
            i++;

        }
    }

    /**
     * Traza paso a paso
     *
     * Fila 0 ({2, 3, 8})
     *
     * 2 < 0 es falso, así que s = 0 + 2 = 2.
     * 3 < 2 es falso, así que s = 2 + 3 = 5.
     * 8 < 5 es falso, así que s = 5 + 8 = 13.
     *
     * Fila 1 ({4, 1})
     *
     * 4 < 13 es verdadero, así que se ejecuta continue ex. Esto salta a la siguiente iteración del ciclo externo y el 1 nunca se procesa.
     *
     * Fin: i pasa a 2, 2 < 2 es falso y el ciclo termina. Se imprime s = 13.
     *
     * Punto clave
     *
     * continue ex aplica continue al ciclo externo etiquetado, no al interno. Abandona lo que quedaba de la fila actual y pasa directamente a la siguiente fila.
     * Un continue sin etiqueta solo habría saltado al siguiente k y el resultado habría sido distinto (en ese caso, 1 < 13 también se habría saltado, pero se habría seguido evaluando el resto de la fila).
     */
    static void test8(){
        int s=0;
        int[][] nums={{2,3,8},{4,1}};
        ex: for(int i=0;i<nums.length;i++){
            for(int k=0;k<nums[i].length;k++){
                if(nums[i][k]<s){
                    continue ex;
                }else {
                    s+=nums[i][k];
                }
            }
        }
        System.out.println(s);
    }

    /**
     * Traza paso a paso
     *
     * i = 0 (sw = true)
     *
     * El while incrementa n en cada vuelta y comprueba i > n || n > 5.
     * Con n = 1 a 5: 0 > n es falso y n > 5 es falso, así que sw sigue en true.
     * Con n = 6: n > 5 es verdadero, así que sw = false y el while termina.
     *
     * i = 1 e i = 2
     *
     * sw ya es false, así que el while no entra ni una sola vez y n no cambia.
     *
     * Fin: se imprime n = 6.
     *
     * Punto clave
     *
     * sw es una variable declarada fuera del for y nunca se vuelve a poner en true.
     * Una vez que pasa a false en la primera iteración del for, el while queda inutilizado para las siguientes.
     * Además, la condición i > n nunca llega a cumplirse, porque n siempre es mayor que i desde la primera vuelta.
     */
    static void test9(){
        var n = 0;
        var sw= true;
        for(var i = 0; i < 3; i++){
            while(sw){
                n++;
                if(i>n|| n>5) sw= false;
            }
        }
        System.out.println(n);
    }

    /**
     *Se produce un error de compilación en la tercera instrucción, ya que el método forEach requiere un Consumer, no una Function
     */
    static void test10(){
        List<Double> ml= Arrays.asList(5.0, 10.0);
        Function<Double,Double> f = a->a+1;
       // ml.stream().forEach(f);
        ml.stream().forEach(a->System.out.println(a));
    }

    /**
     * Para poder recorrer todos los cursos es necesario generar un stream con todos los nombres de cursos,
     * que es lo que hace flatMap() a partir del stream de cursos de cada alumno.
     * Como se deben eliminar los duplicados, se aplica el método distinct() al Stream de cadenas de caracteres generado por flatMap().
     */
    static void test11(){
        List<Student> students=List.of(new Student(new String[]{"Java","C++","Python"},"Fran"),
                new Student(new String[]{"JavaScript","Python"},"Helen"),
                new Student(new String[]{"Java","Angular"},"Bart"));
        students.stream().flatMap(s -> Arrays.stream(s.getCourses())).distinct().forEach(IO::println);
    }

    /**
     *Se aplicaría primero el accept de c2 y después el de c1,
     * por lo que se imprime el texto dos veces, la primera tal cual y la segunda todo en mayúsculas
     */

    static void test12(){

        Consumer<String> c1= s->System.out.println(s.toUpperCase());
        Consumer<String> c2=s->System.out.println(s);
        c2.andThen(c1).accept("hello");
    }
    static void test14(){

        TestOther testOther = new TestOther("Java");
        IO.println("VALUE TEST > "+testOther);
    }

    /**
     *Puntos clave
     * El for interno cuenta hacia atrás desde i/2 hasta 1, así que cada vuelta imprime una cuenta regresiva cada vez más corta.
     * i baja de 2 en 2 (10, 8, 6, 4, 2), por lo que hay 5 vueltas y el for interno arranca en 5, 4, 3, 2 y 1.
     * El do-while evalúa la condición al final, pero aquí no importa porque i empieza en 10 y el cuerpo se habría ejecutado igual con un while normal.
     */
    static void test15(){

        var i=10;
        do {
            for(var j=i/2;j>0;j--) {
                System.out.print(j+" ");
            }
            i-=2;
        }while(i>0);

    }

    /**
     *Traza paso a paso
     * m1 = new boolean[2] crea el arreglo con los valores por defecto: {false, false}.
     * m2 = {false, !m1[0]}: como m1[0] es false, !m1[0] es true, así que m2 = {false, true}.
     *
     * Comparaciones
     *
     * m1[0] == m2[1] → false == true → false.
     * m1[1] == m2[0] → false == false → true.
     */
    static void test16(){
        boolean[] m1 = new boolean[2];
        boolean[] m2 = {false , !m1[0]};
        System.out.println( "" + (m1[0] == m2[1]) + ", "+ (m1[1] == m2[0])  );
    }

    /**
     *Traza paso a paso
     * a++ == 0: compara con el valor anterior (0 == 0, es true) y luego a pasa a 1.
     * b++ == 2: compara con el valor anterior (1 == 2, es false) y luego b pasa a 2.
     * true & false es false, así que el if no se ejecuta y a no se reasigna a 12.
     *
     * Se imprime a = 1, b = 2.
     *
     * Punto clave
     *
     * El operador & (no &&) no hace cortocircuito: evalúa siempre ambos lados.
     * Por eso b++ se ejecuta aunque el resultado ya no dependa de él... en este caso el lado izquierdo es true,
     * así que con && también se habría evaluado el derecho y el resultado habría sido el mismo.
     * La diferencia solo se notaría si el lado izquierdo fuera false: con && el b++ no se ejecutaría, y con & sí.
     */
    static void test17(){
        int a = 0;
        int b = 1;
        if( (a++ == 0) & (b++ == 2) ){
            a = 12;
        }
        System.out.println(a+" "+b);
    }
    static void test18(){
        var nums=List.of(0,1,2,3,4,5,6,7,8,9);
        System.out.println(nums.stream().mapToInt(a->a).max().getAsInt());
        System.out.println(nums.stream().sorted((a,b)->b-a).findFirst().get());

    }

    /**
     * Al normalizar p1, text.txt queda directamente en c.
     * Por otro lado, data.txt y text.txt se encuentran en la misma carpeta,
     * por lo que para ir de uno a otro es necesario subir hasta el padre.
     * Finalmente, si la ruta que se para como parámetro a resolve() es absoluta,
     * se devuelve dicha ruta por lo que la tercera impresión muestra p2 tal cual
     * Resultado
     * c:\text.txt
     * ..\data.txt
     * c:\ser\..\data.txt
     */
    static void test19(){

        Path p1=Path.of("c:\\user\\.\\..\\text.txt");
        Path p2=Path.of("c:\\user\\..\\data.txt");
        System.out.println(p1.normalize().toAbsolutePath());
        System.out.println(p1.relativize(p2));
        System.out.println(p1.resolve(p2));

    }

    /**
     * El método print se encuentra sobrecargado en la clase Test.
     * Una versión recibe una lista y otra un Collection.
     * Por tanto, cuando se llama al método print con una lista se ejecuta la versión que recibe la lista,
     * y cuando se llama con un tipo Collection, aunque se le haya asignado una referencia a una lista,
     * se ejecutará la versión que recibe el Collection.
     */
    static void test20(){
        List<Integer> li=new ArrayList<>();
        Collection<Integer> co=li;
        Test1 test=new Test1();
        test.print(li);
        test.print(co);
    }


    /**
     *Traza paso a paso
     * Línea	Operador	¿Se evalúa method1(i++)?	                    i después
     * 1	    &	        Sí (no hace cortocircuito)	                    1
     * 2	    &&	        No: bool2 es false, el resultado ya es false	1
     * 3	    |	        Sí (no hace cortocircuito)	                    2
     * 4	    ||	        No: bool1 es true, el resultado ya es true	    2
     *
     * Se imprime i = 2.
     *
     * Punto clave
     *
     * Los operadores & y | evalúan siempre ambos lados, así que i++ se ejecuta aunque el resultado ya esté decidido.
     * Los operadores && y || hacen cortocircuito: si el lado izquierdo basta para decidir el resultado,
     * el derecho ni se evalúa y su efecto secundario (i++) no ocurre.
     *
     * Por eso solo las líneas 1 y 3 incrementan i.
     */
    static void test21(){

        int i = 0 ;
        boolean bool1 = true ;
        boolean bool2 = false;
        boolean bool  = false;
        bool = ( bool2 &  method1(i++) ); //line 1
        bool = ( bool2 && method1(i++) ); //line 2
        bool = ( bool1 |  method1(i++) ); //line 3
        bool = ( bool1 || method1(i++) ); //line 4
        System.out.println(i);

    }
    public static boolean method1(int i){
        return i>0 ? true : false;
    }

    /**
     * Traza paso a paso
     * col es un HashSet con {1}.
     * Línea 1: new ArrayList<>(col) crea una copia de los elementos en ese momento: [1].
     * Línea 2: List.copyOf(col) también crea una copia inmutable: [1].
     * Línea 3: col.add(2) modifica solo el HashSet. Las dos listas ya son independientes, así que no se enteran del cambio.
     * Se imprime [1], [1].
     * Punto clave
     *
     * Ni new ArrayList<>(col) ni List.copyOf(col) crean una vista de la colección original,
     * sino una copia superficial (shallow copy) tomada en el momento de la llamada. Los cambios posteriores en col no se reflejan en ellas
     */
    static void test22(){

        Collection<Integer> col = new HashSet<>();
        col.add(1);
        var list1 = new ArrayList<>(col); //line 1
        var list2 = List.copyOf(col); //line 2
        col.add(2); //line 3
        System.out.println(list1+", "+list2);

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
    static void test23(){
        String cad="hello";
        cad.toUpperCase();
        StringBuilder s1=new StringBuilder(cad);
        StringBuilder s2=s1;
        StringBuilder s3=new StringBuilder(s2.toString());
        System.out.println(cad.equals(s1));
        System.out.println(s1.equals(s2));
        System.out.println(s2.equals(s3));

    }
    static void test28(){

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

    }
    static void test38(){

    }

    static void test39(){

    }
    static void test40(){

    }










}
