package guiaSE2125.classdate;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class MainDate {

    public static void main(String[] args) {

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


}
