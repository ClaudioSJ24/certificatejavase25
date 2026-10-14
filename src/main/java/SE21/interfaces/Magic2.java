package SE21.interfaces;

/**
 * Resultado
 *
 * Compila correctamente. La interfaz es funcional válida.
 *
 * Por qué
 *
 * Ahora solo hay un método abstracto:
 *
 * MAX_POWER es una constante, no cuenta.
 * applyPower es default, no cuenta.
 * getRemainingPower(int) es private y tiene cuerpo, así que no es abstracto y no cuenta. Desde Java 9 las interfaces pueden tener métodos privados.
 * cast() es el único abstracto, así que cumple @FunctionalInterface.
 */
@FunctionalInterface
interface Magic2{
    int MAX_POWER=10;
    public default boolean  applyPower(int power) {
        return getRemainingPower(power)>=0;
    }
    private int getRemainingPower(int power) {
        return MAX_POWER-power;
    }
    int cast();
}
