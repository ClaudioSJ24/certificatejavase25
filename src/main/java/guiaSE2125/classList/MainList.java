package guiaSE2125.classList;

import SEJava21.class21.Test1;

import java.util.*;
import java.util.function.Function;

public class MainList {

    public static void main(String[] args) {

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


}
