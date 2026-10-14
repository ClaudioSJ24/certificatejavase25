package SE21.records;

import SE21.interfaces.I2;

import java.util.Collection;

public record Ra(Collection<Integer> col, String s) implements I2 {
}
