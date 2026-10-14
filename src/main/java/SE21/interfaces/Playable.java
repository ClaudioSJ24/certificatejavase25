package SE21.interfaces;

public interface Playable {

    default String play() {
        return "none";
    }
}
