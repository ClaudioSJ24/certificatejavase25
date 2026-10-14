package SE21.classse21;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Product implements Serializable {

    private static float averagePrice=2.99f;
    private String description;
    private transient float price;
    public Product(String description, float price) {
        this.description=description;
        this.price=price;
    }
    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        price=averagePrice;
    }
    public String toString() {
        return description+" "+price+" "+averagePrice;
    }
}
