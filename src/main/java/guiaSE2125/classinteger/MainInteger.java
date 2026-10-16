package guiaSE2125.classinteger;

import java.text.NumberFormat;
import java.util.Locale;

public class MainInteger {
    public static void main(String[] args) {

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

}
