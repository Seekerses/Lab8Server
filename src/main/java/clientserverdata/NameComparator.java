package clientserverdata;

import productdata.Product;

import java.io.Serializable;
import java.util.Comparator;

public class NameComparator implements Comparator<Product> {

    @Override
    public int compare(Product a, Product b) {
        return a.getName().compareTo(b.getName());
    }
}
