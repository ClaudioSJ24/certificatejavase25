package SE21.classse21;

public abstract  class Animal {
    public static enum Type{
        DOG,CAT,FISH;
    }
    private Type type;
    public Animal(Type type) {
        this.type=type;
    }
    public void setType(Type type) {
        this.type=type;
    }
    public abstract String name();
    public String toString() {
        return name()+" "+type;
    }
}
