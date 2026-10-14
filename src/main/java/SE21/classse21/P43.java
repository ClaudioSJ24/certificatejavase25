package SE21.classse21;

public class P43 {
    public void m(Number n1, int n2) {
        System.out.println("method one");
    }
    public void m(long n1, long n2) {
        System.out.println("method two");
    }
    public void m(Integer n1, Integer n2) {
        System.out.println("method three");
    }
    public void m(int... nums) {
        System.out.println("method four");
    }
}
