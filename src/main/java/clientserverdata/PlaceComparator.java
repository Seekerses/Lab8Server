package clientserverdata;

import productdata.Product;

import java.util.Comparator;

public class PlaceComparator implements Comparator<Product> {

    @Override
    public int compare(Product a, Product b) {
        int result = Integer.compare((int) a.getCoordinates().getX(), (int) b.getCoordinates().getX());
        if (result == 0 ) {
            result = Integer.compare(a.getCoordinates().getY(), b.getCoordinates().getY());
        }
        return result;
    }
}
