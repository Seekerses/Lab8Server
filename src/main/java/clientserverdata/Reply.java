package clientserverdata;

import productdata.Product;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentSkipListSet;

public class Reply implements Serializable {

    private ConcurrentSkipListSet<Product> products;
    private String answer;
    private InetSocketAddress address;
    private static final long serialVersionUID = 1338L;

    public Reply(ConcurrentSkipListSet<Product> collection, String message){
        this.products = collection;
        this.answer = message;
    }

    public ConcurrentSkipListSet<Product> getProducts(){
        return products;
    }

    public String getAnswer(){
        return answer;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }
}