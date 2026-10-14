package SE21.interfaces;

/**
 * Resultado
 *
 * Compila correctamente. La interfaz es funcional válida.
 *
 * Por qué
 *
 * Sigue habiendo un solo método abstracto:
 *
 * MAX_POWER es una constante, no cuenta.
 * applyPower es default, no cuenta.
 * getRemainingPower(int) ahora es static y tiene cuerpo, así que no es abstracto y no cuenta.
 * cast() es el único abstracto, así que cumple @FunctionalInterface.
 *
 * Un default puede llamar a un método static de la misma interfaz sin calificarlo, así que getRemainingPower(power) dentro de applyPower es válido.
 */
@FunctionalInterface
interface Magic1 {
    int MAX_POWER=10;
    public default boolean  applyPower(int power) {
        return getRemainingPower(power)>=0;
    }
    static int getRemainingPower(int power) {
        return MAX_POWER-power;
    }
    int cast();
}
