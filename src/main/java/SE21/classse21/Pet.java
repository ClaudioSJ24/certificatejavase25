package SE21.classse21;

import SE21.interfaces.Playable;

public class Pet extends Animal implements Playable {
    private String name;
    public Pet(String name, Type type) {
        super(type);
        this.name=name;
    }
    public String name() {
        return play()+" "+name;
    }

    public String play() {
        return "Fetch";
    }
}