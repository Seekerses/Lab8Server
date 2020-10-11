package clientserverdata;

import productdata.Product;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentSkipListSet;

public class Reply implements Serializable {

    private Hashtable<String,Product> products;
    private String answer;
    private InetSocketAddress address;
    private static final long serialVersionUID = 1338L;

    public Reply(Hashtable<String,Product> collection, String message){
        this.products = collection;
        this.answer = message;
    }

    public Hashtable<String,Product> getProducts(){
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