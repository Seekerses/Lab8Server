package cmd;

import productdata.Product;
import java.util.concurrent.ConcurrentSkipListSet;

public interface extractable {
    ConcurrentSkipListSet<Product> execute(String[] args);
}
