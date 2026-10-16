package guiaSE2125.classsthread;

import guiaSE2125.SE21;

import java.io.IO;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MainThread {
    public static void main(String[] args) {

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
    static volatile int x=1;
    static AtomicInteger y=new AtomicInteger(1);

    static void question25(){

        SE21 se21 = new SE21();
        Runnable r1=()->{
            while(MainThread.x<3) {
                System.out.println("Thread 1: "+String.valueOf(MainThread.x));
                MainThread.x++;
            }
        };
        Runnable r2=()->{
            while(MainThread.y.get()<3) {
                System.out.println("Thread 2: "+String.valueOf(MainThread.y));
                MainThread.y.incrementAndGet();
            }
        };
        Thread t1=new Thread(r1);
        Thread t2=new Thread(r2);
        t1.setPriority(2);
        t2.setPriority(8);
        t1.start();
        t2.start();
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
}
