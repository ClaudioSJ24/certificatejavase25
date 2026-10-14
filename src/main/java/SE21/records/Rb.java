package SE21.records;

import SE21.interfaces.I1;
import SE21.interfaces.I2;

import java.util.Collection;

public record Rb(Iterable<Integer> it, String s) implements I1 {
}
