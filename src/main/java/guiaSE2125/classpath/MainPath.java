package guiaSE2125.classpath;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainPath {
    public static void main(String[] args) {

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
}
