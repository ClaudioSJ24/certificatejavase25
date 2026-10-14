package SE21.enums;

public enum Desig {

    CEO('A'),CMO('B'),CTO('C'),CFO('D');
    public char c;
    private Desig(char c) {
        this.c=c;
    }
}
