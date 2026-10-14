package SE21;

import SE21.classse21.*;
import SE21.enums.Desig;
import SE21.interfaces.I1;
import SE21.interfaces.I2;
import SE21.records.Ra;
import SE21.records.Rb;

import java.io.*;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        question28();

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

        String a="Hello! Java";
        System.out.print(a.indexOf("Java"));
        a.replace("Hello!", "Wellcome!");
        System.out.print(a.indexOf("Java"));
        StringBuilder b=new StringBuilder(a);
        System.out.print(b.indexOf("Java"));
    }

    /**
     *Traza paso a paso
     * r1.lock() adquiere el bloqueo de lectura en el hilo actual.
     * w1.tryLock() intenta adquirir el bloqueo de escritura sin esperar. Devuelve false,
     * porque ReentrantReadWriteLock no permite “subir de categoría” (lock upgrade): un hilo que tiene el bloqueo de lectura no puede obtener el de escritura mientras no lo libere.
     * Como tryLock() devolvió false, no se entra al if y result sigue siendo null. No se lanza ninguna excepción, así que el catch no se ejecuta.
     * finally: r1.unlock() libera el bloqueo de lectura sin problema.
     * Después, rw1.isWriteLocked() es false, porque el bloqueo de escritura nunca se adquirió, así que no se ejecuta w1.unlock() ni se asigna "unlocked".
     * Se imprime el valor de result, que sigue siendo null.
     * Puntos clave
     * tryLock() no bloquea el hilo. Si hubiera sido w1.lock(), el hilo se quedaría esperando para siempre (deadlock contra sí mismo), porque nunca se liberaría el bloqueo de lectura.
     * Lo contrario sí está permitido: un hilo con el bloqueo de escritura puede adquirir el de lectura (lock downgrade).
     * Si tryLock() hubiera devuelto true, result sería "locked", pero luego el finally lo cambiaría a "unlocked" al liberar el de escritura.
     */

    static void question2(){
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
     *
     */
    static void question4(){
        TestSE21.IClass inner = new TestSE21().new IClass();
    }

    /**
     * Traza paso a paso
     *
     * Valores iniciales: a = 0, b = 1, c = 2.
     *
     * Condición: Math.round(b/c) < b/c & b != b/++a
     *
     * Por precedencia, se agrupa así: (Math.round(b/c) < b/c) & (b != b/++a).
     *
     * Lado izquierdo: b/c es división entera, 1/2 = 0. Math.round(0) devuelve 0,
     * porque el int se ajusta a la versión round(float). Entonces 0 < 0 es false.
     * Lado derecho: como & no hace cortocircuito, se evalúa igual. ++a hace a = 1, y b/++a es 1/1 = 1. Entonces b != 1 es 1 != 1, que es false.
     * false & false es false, así que se ejecuta el else.
     *
     * Else: c -= --b + a
     *
     * --b deja b = 0 y devuelve 0.
     * 0 + a es 0 + 1 = 1.
     * c = c - 1 = 2 - 1 = 1.
     */
    static void question5(){

        int a=0,b=1,c=2;
        if(Math.round(b/c)<b/c&b!=b/++a) {
            c+=a+++b;
        }else {
            c-=--b+a;
        }
        System.out.println(c);

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
     *Traza
     * La suma de 1..9 es 45.
     * En reduce(identity, accumulator), la identidad se usa como valor inicial en cada partición, no una sola vez.
     * En un stream secuencial habría una sola partición: 5 + 45 = 50.
     * En uno paralelo, el stream se divide en varios fragmentos (según el número de núcleos), cada uno arranca con 5, y luego se combinan los parciales.
     * El resultado es 45 + 5 * (número de particiones).
     */
    static void question8(){

        List<Integer> nums=List.of(1,2,3,4,5,6,7,8,9);
        System.out.println(nums.parallelStream()

                .reduce(5, Integer::sum));
    }

    /**
     * Traza paso a paso
     * lst1 contiene ["1", "2"].
     * List.copyOf(lst1) crea una copia inmutable con ["1", "2"]. No es una vista de lst1.
     * lst1.add("3") modifica solo lst1, que pasa a ["1", "2", "3"]. lst2 no cambia.
     * System.out.println(lst2) imprime [1, 2].
     * lst2.add("4") (l1) lanza UnsupportedOperationException, porque la lista devuelta por List.copyOf es inmutable.
     * El programa termina por la excepción, así que System.out.println(lst1) no se ejecuta.
     * Puntos clave
     * List.copyOf es una copia, no una vista: los cambios posteriores en la original no se reflejan, como en test22.
     * Inmutable significa que cualquier método modificador lanza la excepción (add, remove, set, clear...),
     * incluso si la operación no cambiaría nada.
     * Si necesitas una copia modificable, usa new ArrayList<>(lst1).
     */
    static void question9(){

        List<String> lst1=new ArrayList<>();
        lst1.add("1");
        lst1.add("2");
        List<String> lst2=List.copyOf(lst1);
        lst1.add("3");
        System.out.println(lst2);
        lst2.add("4"); //l1
        System.out.println(lst1);
    }

    /**
     *
     * @throws ExecutionException
     * @throws InterruptedException
     *
     * Traza paso a paso
     * El pool tiene 2 hilos, así que las dos tareas arrancan casi a la vez.
     * f1 termina a los ~100 ms con "A" y f2 a los ~500 ms con "B".
     * La expresión f2.get() + f1.get() se evalúa de izquierda a derecha: primero f2.get(),
     * que bloquea al hilo principal hasta que f2 termina (~500 ms) y devuelve "B".
     * Luego f1.get() devuelve "A" al instante, porque ya había terminado.
     * La concatenación da "BA", que se imprime.
     * executor.shutdown() se ejecuta después, lo que permite que el programa termine.
     * Puntos clave
     * El orden de la salida lo decide el orden de las llamadas a get(), no cuál tarea terminó primero. Aunque f1 acabó antes, se pide después.
     * get() es bloqueante: espera hasta que la tarea termina.
     * throws ExecutionException, InterruptedException es necesario porque get() los declara,
     * y Thread.sleep dentro del Callable está permitido sin try/catch porque Callable.call() puede lanzar Exception.
     * Si shutdown() no se llamara, el programa no terminaría, porque los hilos del pool no son daemon.
     */
    static void question10() throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<String> f1 = executor.submit(() -> {
            Thread.sleep(100);
            return "A";
        });
        Future<String> f2 = executor.submit(() -> {
            Thread.sleep(500);
            return "B";
        });
        System.out.println(f2.get() + f1.get());
        executor.shutdown();
    }

    /**
     * Traza paso a paso
     *
     * LinkedHashSet implementa SequencedSet desde Java 21, así que addFirst está disponible a través de SequencedCollection.
     *
     * Llamada	Contenido del conjunto
     * addFirst("A")	A
     * addFirst("B")	B A
     * addFirst("C")	C B A
     * addFirst("D")	D C B A
     * addFirst("B")	B D C A
     *
     * En la última llamada, "B" ya existía.
     * Un Set no admite duplicados, pero addFirst en un LinkedHashSet mueve el elemento existente al principio en lugar de ignorarlo.
     * Por eso B sale de la tercera posición y pasa a la primera.
     *
     * forEach(System.out::print) imprime los elementos en orden de encuentro, sin separadores: BDCA.
     *
     * Punto clave
     *
     * Con el add normal, un elemento repetido no cambia nada (conserva su posición original).
     * addFirst y addLast sí reubican el elemento si ya está presente. Si se hubiera usado add("B"),
     * la salida habría sido ABCD, con A primero y los demás en orden de inserción.
     */
    static void question11(){

        SequencedCollection<String> letters=new LinkedHashSet<>();
        letters.addFirst("A");
        letters.addFirst("B");
        letters.addFirst("C");
        letters.addFirst("D");
        letters.addFirst("B");
        letters.forEach(System.out::print);

    }

    /**
     * Traza paso a paso
     *
     * p1.relativize(p2)
     *
     * Ambas rutas son absolutas y contienen . o .., así que en Java 11+ relativize las normaliza primero:
     * p1 → /f1/temp/dir.txt
     * p2 → /f1/cmp.txt
     * El prefijo común es /f1. De p1 quedan dos nombres (temp, dir.txt), que se convierten en ../.., y luego se añade lo que queda de p2, cmp.txt.
     * Resultado: ../../cmp.txt.
     *
     * p1.resolve(p2)
     *
     * Como p2 es absoluta, resolve la devuelve tal cual, sin normalizar: /f1/./ext/../cmp.txt.
     *
     * Puntos clave
     * relativize normaliza internamente (Java 11+), pero resolve nunca normaliza.
     * resolve con una ruta absoluta devuelve esa ruta absoluta y descarta la original.
     *
     * Salida del código (en Linux/macOS)
     * ../../cmp.txt
     * /f1/./ext/../cmp.txt
     */
    static void question12(){

        Path p1=Path.of("/f1/data/../temp/dir.txt");
        Path p2=Path.of("/f1/./ext/../cmp.txt");
        System.out.println(p1.relativize(p2));
        System.out.println(p1.resolve(p2));

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
     * Traza paso a paso
     * ldt1 = 2025-03-17 10:30.
     * ldt1.withDayOfMonth(18) da 2025-03-18 10:30, y plusHours(-15) lo deja en ldt2 = 2025-03-17 19:30.
     * zdt1 = 2025-03-17 10:30 en GMT+00, que equivale a 10:30 UTC del día 17.
     * zdt2 = 2025-03-17 19:30 en GMT-09. Para pasarlo a UTC se suman 9 horas, así que equivale a 04:30 UTC del día 18.
     * Duration.between(zdt1, zdt2) va de 17 a las 10:30 UTC hasta 18 a las 04:30 UTC: 18 horas.
     * Puntos clave
     * Duration.between con ZonedDateTime compara instantes reales (convierte ambos a UTC), no las horas locales.
     * Comparando solo las horas locales, la diferencia sería 9 horas (10:30 → 19:30), lo cual es incorrecto.
     * Una zona con offset negativo (GMT-09) está retrasada respecto a UTC, por eso su hora local de 19:30 corresponde a un instante posterior.
     * Los identificadores GMT+00 y GMT-09 son válidos para ZoneId.of.
     */
    static void question15(){

        LocalDateTime ldt1=LocalDateTime.of(2025, 3, 17,10,30);
        LocalDateTime ldt2=ldt1.withDayOfMonth(18).plusHours(-15);
        ZonedDateTime zdt1=ZonedDateTime.of(ldt1, ZoneId.of("GMT+00"));
        ZonedDateTime zdt2=ZonedDateTime.of(ldt2, ZoneId.of("GMT-09"));
        System.out.println(Duration.between(zdt1, zdt2).toHours());
    }

    /**
     * Traza paso a paso
     *
     * in.txt tiene 10 caracteres: 0123456789.
     *
     * Lectura	count   Búfer después de leer	Se escribe
     * 1	    8	    01234567	            01234567
     * 2	    2	    89234567	            89234567
     * 3	    -1	    (fin del archivo)	    el ciclo termina
     * En la segunda lectura solo se sobrescriben las dos primeras posiciones del búfer (8 y 9). Las otras seis conservan 234567 de la lectura anterior.
     * Como out.write(buffer) escribe los 8 caracteres completos, se añaden 89234567 en lugar de solo 89.
     * Resultado final: 01234567 + 89234567 = 0123456789234567.
     */
    static void question16(){

        char[] buffer=new char[8];
        int count=0;
        try(FileReader fr=new FileReader("in.txt");
            FileWriter out=new FileWriter("out.txt")){
            while((count=fr.read(buffer))!=-1) {
                out.write(buffer);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
     * f1.format(10000) con Locale.US produce "10,000" (con coma como separador de miles), que se guarda en res.
     * Primer println: imprime 10,000.
     * res.replaceAll(",", "") devuelve un nuevo String sin comas, pero el resultado se descarta. String es inmutable, así que res sigue siendo "10,000".
     * Integer.parseInt("10,000") lanza NumberFormatException, porque parseInt no acepta separadores de miles. El segundo println nunca se ejecuta.
     * Punto clave
     *
     * Si el resultado de replaceAll se hubiera asignado (res = res.replaceAll(",", "")), res sería "10000", parseInt daría 10000 y f2.format(10000)
     * (formato compacto corto, Locale.US) imprimiría 10K. La salida habría sido:
     * 10,000
     * 10K
     */
    static void question19(){

        int number=10000;
        NumberFormat f1=NumberFormat.getInstance(Locale.US);
        NumberFormat f2=NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        String res=f1.format(number);
        System.out.println(res);
        res.replaceAll(",", "");
        System.out.println(f2.format(Integer.parseInt(res)));

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
    static void question22(){
        String[][] groups= {{"a","u","c","d"},
                {"c","x","n"},
                {"k","j","b"},
                {"n","x","c"},
                {"c","a","u","d"}};
        Map<Integer,List<String>> cars=Arrays.stream(groups)
                .peek(n->Arrays.sort(n))
                .map(a->Arrays.stream(a).collect(Collectors.joining("")))
                .distinct()
                .collect(Collectors.groupingBy(String::length));
        new TreeMap(cars).keySet().forEach(k->System.out.print(cars.get(k).size()));

    }

    /**
     * Traza por hilo
     *
     * Hilo 1 (x, volatile int)
     *
     * x = 1: 1 < 3, imprime Thread 1: 1 y hace x++ (x = 2).
     * x = 2: 2 < 3, imprime Thread 1: 2 y hace x++ (x = 3).
     * x = 3: 3 < 3 es falso y termina.
     *
     * Hilo 2 (y, AtomicInteger)
     *
     * y = 1: imprime Thread 2: 1 (String.valueOf(y) llama a toString(), que devuelve el valor) y incrementAndGet() lo deja en 2.
     * y = 2: imprime Thread 2: 2 y lo deja en 3.
     * y = 3: termina.
     */
    volatile int x=1;
    AtomicInteger y=new AtomicInteger(1);

    static void question25(){

        SE21 se21 = new SE21();
        Runnable r1=()->{
            while(se21.x<3) {
                System.out.println("Thread 1: "+String.valueOf(se21.x));
                se21.x++;
            }
        };
        Runnable r2=()->{
            while(se21.y.get()<3) {
                System.out.println("Thread 2: "+String.valueOf(se21.y));
                se21.y.incrementAndGet();
            }
        };
        Thread t1=new Thread(r1);
        Thread t2=new Thread(r2);
        t1.setPriority(2);
        t2.setPriority(8);
        t1.start();
        t2.start();
    }

    static void question24(){
        Animal a=new Pet("Scooby",Animal.Type.DOG);
        System.out.println(a);
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
    static void question26(){

        String myStr="Hello Java";
        String myText1="""
		Hello Java""";
        String myText2="""
		Hello Java
		""";
        System.out.print(myStr.equals(myText1)+":");
        System.out.print(myStr.equals(myText2)+":");
        System.out.print(myText1.equals(myText2)+":");
        System.out.println(myText1.intern()==myText2.intern());
    }

    /**
     * Traza paso a paso
     *
     * Valores iniciales: x = 0, a = 3. Cada vuelta del for interno hace b++ y a-- en su actualización.
     *
     * Vuelta interna	a	b	¿Qué ocurre?	                            x	Tras la actualización
     *      1	        3	0	b == 2 no, a < b (3 < 0) no, x += 3 + 0	    3	b = 1, a = 2
     *      2	        2	1	b == 2 no, a < b (2 < 1) no, x += 2 + 1	    6	b = 2, a = 1
     *      3	        1	2	b == 2 es verdadero, continue (no suma)	    6	b = 3, a = 0
     * Con b = 3, b < 3 es falso y termina el for interno.
     * La actualización del for externo hace a--, así que a = -1. Como -1 > 0 es falso, el ciclo externo termina.
     * El break ext nunca se ejecuta, porque a < b nunca fue verdadero.
     *
     * Se imprime x = 6.
     *
     * Puntos clave
     * El a-- del for interno modifica la misma variable a del for externo, por eso a baja de 3 a 0 en una sola pasada interna.
     * continue en un for ejecuta igualmente la actualización (b++, a--) antes de volver a evaluar la condición.
     */

    static void question27(){

        int x=0;
        ext: for(int a=3;a>0;a--) {
            for(int b=0;b<3;b++,a--) {
                if(b==2) {
                    continue;
                }
                else if(a<b){
                    break ext;
                }
                x+=a+b;
            }
        }
        System.out.println(x);
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

    /**
     * Traza paso a paso
     * ld1 = 2025-02-28.
     * ld1.withMonth(4) devuelve un objeto nuevo y se descarta, así que ld1 sigue siendo 2025-02-28.
     * ld1.plus(Period.ofDays(2)) = 2025-03-02, y plusMonths(-2) da ld2 = 2025-01-02.
     * ldt3 = 2025-01-02 21:30.
     *
     * Condición: ld1.getYear() > ld2.getYear() ^ !ld1.getMonth().equals(ldt3.getMonth())
     *
     * > tiene mayor precedencia que ^, así que queda (2025 > 2025) ^ (!(FEBRUARY.equals(JANUARY))).
     * Izquierda: false.
     * Derecha: FEBRUARY.equals(JANUARY) es false, y con ! queda true.
     * false ^ true es true, así que se entra al if.
     * ldt3.plus(Duration.ofMinutes(15)) da 2025-01-02 21:45.
     * El formato
     *
     * El patrón "yy/mm/dd" usa mm, que es minutos (no meses, que sería MM):
     *
     * yy → 25
     * mm → minutos: 45
     * dd → 02
     *
     * Resultado: 25/45/02.
     *
     * Puntos clave
     * withMonth no modifica el objeto, igual que el resto de métodos de java.time, que son inmutables.
     * ^ entre booleanos es XOR: verdadero solo si ambos lados difieren.
     * mm frente a MM es una trampa clásica: con MM la salida habría sido 25/01/02.
     */
    static void question30(){

        LocalDate ld1=LocalDate.of(2025, Month.FEBRUARY, 28);
        ld1.withMonth(4);
        LocalDate ld2=ld1.plus(Period.ofDays(2)).plusMonths(-2);
        LocalDateTime ldt3=ld2.atTime(LocalTime.of(21, 30));
        if(ld1.getYear()>ld2.getYear()^!ld1.getMonth().equals(ldt3.getMonth())) {
            ldt3=ldt3.plus(Duration.ofMinutes(15));
        }
        System.out.println(ldt3.format(DateTimeFormatter.ofPattern("yy/mm/dd")));
    }

    /**
     * Traza
     * Longitudes: Chocolate = 9, Milk = 4, Honey = 5.
     * El comparador f1.length() - f2.length() ordena de menor a mayor longitud: Milk (4), Honey (5), Chocolate (9).
     * forEach imprime cada elemento seguido de un espacio.
     * Puntos clave
     * Arrays.asList envuelto en new ArrayList<>(...) produce una lista modificable.
     * Con Arrays.asList directamente también funcionaría sort, porque solo reordena sin cambiar el tamaño; con List.of lanzaría UnsupportedOperationException.
     * Restar longitudes como comparador es seguro aquí, pero con valores muy grandes o negativos puede desbordar. Lo más robusto es Comparator.comparingInt(String::length).
     * List.sort es estable: si dos elementos tuvieran la misma longitud, conservarían su orden relativo original.
     */
    static void question31(){

        List<String> foods=new ArrayList<>(

                Arrays.asList("Chocolate","Milk","Honey"));

        foods.sort((f1,f2)->f1.length()-f2.length());

        foods.forEach(f->System.out.print(f+" "));

    }
    static void question32(){

    }
   


}
