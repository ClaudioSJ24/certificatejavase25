package guiaSE2125.classlambdas;

import SE21.classse21.ElectricProduct;
import SE21.classse21.Product1;
import SE21.classse21.Product2;
import SE21.enums.Desig;
import SE21.interfaces.I1;
import SE21.interfaces.I2;
import SE21.records.Ra;
import SE21.records.Rb;
import SEJava21.class21.Student;
import SEJava21.enums.Type;

import java.io.IO;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainLambdas {
    public static void main(String[] args) {

    }

    /**
     * Traza paso a paso
     * f1.apply(10) → 10 * 2 = 20, así que result = 20.
     * p.test(20) → 20 % 2 == 0 es true, así que se entra al if.
     * f2.apply(20) → 20 + 5 = 25, y result = 25.
     * Se imprime 25.
     * Puntos clave
     * T se infiere como Integer: los tres argumentos funcionales (Function<Integer,Integer>, UnaryOperator<Integer>, Predicate<Integer>) coinciden con los parámetros del método genérico.
     * UnaryOperator<T> es un Function<T,T>, por eso se puede pasar donde se espera una función del mismo tipo de entrada y salida, y aquí se usa con apply.
     * Si el resultado del primer paso fuera impar, el if no se ejecutaría y se devolvería el valor de f1 sin sumar 5.
     */
    static void question14(){
        Function<Integer, Integer> f = x -> x * 2;
        UnaryOperator<Integer> u = x -> x + 5;
        Predicate<Integer> p = x -> x % 2 == 0;

        System.out.println(applyFunction(10, f, u, p));
    }

    static <T> T applyFunction(T value, Function<T, T> f1, UnaryOperator<T> f2, Predicate<T> p) {
        T result = f1.apply(value);
        if (p.test(result)) {
            result = f2.apply(result);
        }
        return result;
    }

    /**
     * Traza
     * l3.stream() produce dos elementos: l1 y l2.
     * flatMap(x -> x.stream()) aplana ambas listas en un solo stream: 1, 3, 5, 7, 9, 2, 4, 6, 8.
     * filter(y -> y > 5) deja 7, 9, 6, 8.
     * collect(Collectors.toList()) los recoge en una lista, y println imprime [7, 9, 6, 8].
     * Puntos clave
     * El orden es 7, 9, 6, 8 porque primero salen los elementos de l1 y luego los de l2.
     * x -> x.stream() y List::stream son equivalentes en este caso.
     * Collectors.toList() devuelve una lista modificable (en la práctica un ArrayList, aunque la especificación no lo garantiza),
     * a diferencia de .toList(), que devuelve una lista inmutable.
     * El comentario //line 1 no afecta nada: es solo un comentario y el código compila sin cambios.
     */
    static void question18(){


        List<Integer> l1=List.of(1,3,5,7,9);
        List<Integer> l2=List.of(2,4,6,8);
        List<List<Integer>> l3=List.of(l1,l2);
//line 1
        List<Integer> l4=l3.stream()

                .flatMap(x->x.stream())

                .filter(y->y>5)

                .collect(Collectors.toList());
        System.out.println(l4);


    }

    /**
     * Traza paso a paso
     * Arrays.asList(a1) → [1, 2, 0, 5] y Arrays.asList(a2) → [3, 6, 11, 1, 2].
     * Stream.of(lista1, lista2) produce un Stream<List<Integer>> con dos elementos.
     * flatMap(l -> l.stream()) los aplana: 1, 2, 0, 5, 3, 6, 11, 1, 2.
     * distinct() elimina repetidos (1 y 2 aparecen dos veces): 1, 2, 0, 5, 3, 6, 11.
     * filter(n -> n % 2 == 0) deja los pares: 2, 0, 6.
     * summingInt(n -> n) suma: 2 + 0 + 6 = 8.
     * Puntos clave
     * distinct() se aplica antes del filtro, así que el segundo 2 no se cuenta dos veces. Sin distinct() el resultado sería 2 + 0 + 6 + 2 = 10.
     * Stream.of(Arrays.asList(a1), Arrays.asList(a2)) produce un stream de listas, no de enteros, por eso es necesario flatMap.
     * Este ejercicio es la respuesta a la pregunta anterior: una sentencia en //line 1 que imprima 8 es justamente este tipo de pipeline sobre a1 y a2.
     */
    static void question20(){

        Integer[] a1= {1,2,0,5};
        Integer[] a2= {3,6,11,1,2};
//line 1
        System.out.println(
                Stream.of(Arrays.asList(a1), Arrays.asList(a2))
                        .flatMap(l->l.stream())
                        .distinct()
                        .filter(n->n%2==0)
                        .collect(Collectors.summingInt(n->n))
        );

    }

    /**
     * Traza
     * Los precios son 2.0, 0.5, 1.0, 3.0, que suman 6.5.
     * Se usa la versión de tres argumentos: reduce(identity, accumulator, combiner).
     * En un stream paralelo, cada partición arranca con la identidad 10.0. Cada partición produce 10.0 + (suma de sus elementos).
     * El combinador (x, y) -> x + y - 10.0 une dos parciales y resta el 10.0 sobrante, compensando que la identidad se sumó una vez de más.
     * Por qué siempre da 16.5
     *
     * Con n particiones, la suma de parciales es 6.5 + 10.0 * n. Se hacen n - 1 combinaciones, y cada una resta 10.0:
     *
     * 6.5 + 10.0 * n - 10.0 * (n - 1) = 6.5 + 10.0 = 16.5
     *
     * Da igual cuántas particiones haya: el resultado es 16.5. En secuencial también: 10.0 + 6.5 = 16.5.
     */
    static void question21(){

        List<Product1> prods=List.of(new Product1("p1",2.0),
                new Product1("p2",0.5),
                new Product1("p3",1.0),
                new Product1("p4",3.0));
        System.out.println(prods.parallelStream()
                .map(p->p.getPrice())
                .reduce(10.0, (a,b)->a+b, (x,y)->x+y-10.0)
        );
    }

    /**
     * Traza
     * Los cuatro objetos son ElectricProduct, que hereda de Product, así que ambos filtros (instanceof ElectricProduct e instanceof Product) dejan pasar los cuatro elementos.
     * summarizingDouble(a -> a.price) calcula estadísticas sobre 100, 90, 200, 300. getMax() devuelve 300.0; se imprime con .0 porque es double.
     * Collectors.mapping(p2 -> p2.name, Collectors.joining(",")) extrae los nombres y los une con comas, respetando el orden de la lista: CellPhone,ToyCar,Motor,Fan.
     * Puntos clave
     * Los literales 100, 90, 200, 300 son int, pero se convierten automáticamente a double al pasarse al constructor.
     * Compila: price y name tienen acceso de paquete y todo está en el mismo paquete. En la lambda a -> a.price, a es de tipo Product, así que el acceso es válido.
     * p2 como nombre de parámetro de la lambda no choca con la variable local p, así que no hay conflicto.
     * Los instanceof son redundantes aquí: siempre dan true para estos elementos. Con Java 21 y la lista tipada como List<Product>, a instanceof Product no genera error de compilación.
     */
    static void question28(){

        List<Product2> p=List.of(new ElectricProduct("CellPhone",100),
                new ElectricProduct("ToyCar",90),
                new ElectricProduct("Motor",200),
                new ElectricProduct("Fan",300)
        );
        DoubleSummaryStatistics sts=p.stream()
                .filter(a->a instanceof ElectricProduct)
                .collect(Collectors.summarizingDouble(a->a.getPrice()));
        String s1=p.stream().filter(a->a instanceof Product2)
                .collect(Collectors.mapping(p2->p2.getName(), Collectors.joining(",")));
        System.out.println(sts.getMax());
        System.out.println(s1);
    }

    /**
     * Primer println: máximo con mapToInt
     * mapToInt(a -> a) convierte el Stream<Integer> en un IntStream (hace unboxing de Integer a int).
     * max() devuelve un OptionalInt, porque el stream podría estar vacío.
     * getAsInt() extrae el valor. Si el OptionalInt estuviera vacío lanzaría NoSuchElementException.
     *
     * Segundo println: máximo con orden descendente
     *
     * java
     * nums.stream().sorted((a,b) -> b - a).findFirst().get()
     * sorted((a,b) -> b - a) ordena de mayor a menor: 9, 8, 7, ..., 0.
     * findFirst() toma el primer elemento (9) y lo devuelve en un Optional<Integer>.
     * get() extrae el valor.
     */
    static void test18(){
        var nums=List.of(0,1,2,3,4,5,6,7,8,9);
        System.out.println(nums.stream().mapToInt(a->a).max().getAsInt());
        System.out.println(nums.stream().sorted((a,b)->b-a).findFirst().get());

    }

    /**
     * Pero el orden no está garantizado. Lo normal es ver ABCDE, aunque el resultado puede variar entre ejecuciones.
     *
     * Traza paso a paso
     * Stream.concat(s1, s2) produce A, B, C, B, A, D, E.
     * distinct() elimina los duplicados y deja A, B, C, D, E. En un stream ordenado conserva la primera aparición de cada elemento.
     * sorted() los ordena de forma natural: A, B, C, D, E.
     * forEach(System.out::print) imprime cada elemento.
     * Punto clave
     *
     * sorted() ordena el stream, pero en un stream paralelo forEach no respeta el orden de encuentro:
     * los elementos pueden procesarse desde distintos hilos y salir en cualquier orden, por ejemplo CDEAB.
     * Con solo cinco elementos es muy probable que salga ABCDE, pero el comportamiento no está garantizado.
     */
    static void question13(){
        Stream<String> s1=Stream.of("A","B","C","B");
        Stream<String> s2=Stream.of("A","D","E");
        Stream.concat(s1, s2).parallel().distinct()
                .forEach(System.out::print);
    }

    /**
     * Traza paso a paso
     * Stream.of(1,...,7) produce los números del 1 al 7.
     * filter(n -> n % 2 != 0) deja solo los impares: 1, 3, 5, 7.
     * peek(System.out::print) imprime cada elemento que pasa por ahí: 1357, sin saltos de línea.
     * dropWhile(n -> n < 6) descarta los elementos iniciales mientras sean menores que 6: elimina 1, 3, 5 y se detiene en 7,
     * que se conserva (a partir de ahí ya no evalúa el predicado).
     * count() cuenta lo que queda: solo 7, así que devuelve 1.
     * println imprime ese 1 justo después de lo que ya había escrito peek, en la misma línea: 1357 + 1 = 13571.
     * Punto clave
     *
     * Desde Java 9, count() puede saltarse el recorrido del stream (y por tanto no ejecutar peek) si puede calcular el tamaño directamente de la fuente.
     * Aquí no ocurre, porque filter y dropWhile eliminan esa información de tamaño, así que el pipeline sí se ejecuta y peek imprime.
     *
     * Por cierto, el código que pegaste termina con un st suelto al final. Lo ignoré, pero si era el inicio de otro fragmento, mándalo completo.
     */
    static void question7(){

        System.out.println(
                Stream.of(1,2,3,4,5,6,7)
                        .filter(n->n%2!=0)
                        .peek(System.out::print)
                        .dropWhile(n->n<6)
                        .count()
        );
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
     *Traza paso a paso
     * i es un Ra con col = [3,7,4,2] y s = "List ".
     * El switch evalúa los case en orden y el primero que coincide es Ra(var x, String y).
     * var x es Collection<Integer> y String y es "List ".
     * filter(z -> (Integer) z % 2 == 0) deja los pares: 4 y 2.
     * summingInt(a -> a) los suma y da 6.
     * El resultado es y + 6, es decir, "List 6".
     * println imprime List 6.
     * Puntos clave
     * Los patrones de registro con subpatrones totales coinciden con cualquier Ra no nulo.
     * var x y String y aceptan cualquier valor de sus componentes, incluso null. Por eso el primer case captura todos los Ra.
     * l1 (case Ra r) no es un error de compilación.
     * Un patrón de registro no domina a un patrón de tipo posterior, así que el compilador lo acepta.
     * En ejecución es código inalcanzable, porque ningún Ra llega hasta ahí.
     * l2 (case I2 i2) tampoco da error. Va después de Ra r, que es más específico, y el orden de específico a general es el correcto.
     * Si case I2 i2 estuviera antes de case Ra r, sí habría un error de dominancia.
     * El default es obligatorio aquí. I1 no es una interfaz sealed, así que el switch no sería exhaustivo sin él.
     * (Integer) z % 2 se interpreta como ((Integer) z) % 2, porque el cast tiene mayor precedencia que %.
     */
    static void question3(){

        I1 i=new Ra(List.of(3,7,4,2),"List ");
        System.out.println(
                switch(i) {
                    case Ra(var x, String y)->y+x.stream()
                            .filter(z->(Integer)z%2==0)
                            .collect(Collectors.summingInt(a->a));
                    case Rb(Iterable<Integer> it, var y)->{
                        int s=0;
                        Iterator<Integer> ir=it.iterator();
                        while(ir.hasNext()) {
                            s+=ir.next();
                        }
                        yield y+s;
                    }
                    case Ra r->r.s(); //l1
                    case I2 i2->"Interface I2 "; //l2
                    default->"None ";
                }
        );
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
     * Traza paso a paso
     * El orden de las constantes es CEO('A'), CMO('B'), CTO('C'), CFO('D').
     * dropWhile(s -> s.equals(Desig.CMO)) evalúa primero CEO. Como CEO no es CMO,
     * el predicado es false y dropWhile deja de descartar de inmediato, sin quitar nada.
     * findFirst() devuelve CEO, así que d = Optional[CEO].
     * d.orElse(null) devuelve CEO, y el switch entra en case CEO.
     * Se imprime Desig.CEO.c, que es el campo c de esa constante: 'A'.
     * Puntos clave
     * dropWhile no filtra todos los CMO: solo descarta un prefijo.
     * Aquí el prefijo es vacío porque el primer elemento ya no cumple la condición, así que CMO (que está en segundo lugar) se queda en el stream,
     * pero no es el primero.
     * El switch compila: cubre las cuatro constantes y case null, así que es exhaustivo sin necesidad de default.
     * case null no se ejecuta, porque d contiene un valor.
     */
    static void question17(){

        Optional<Desig> d=Arrays.stream(Desig.values()).dropWhile(s->s.equals(Desig.CMO)).findFirst();
        switch(d.orElse(null)) {
            case CEO ->System.out.println(Desig.CEO.c);
            case CMO ->System.out.println(Desig.CMO+"x");
            case CTO ->System.out.println(Desig.CTO+"xy");
            case CFO ->System.out.println(Desig.CFO+"xyz");
            case null->System.out.println("null");
        }
    }



}
