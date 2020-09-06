package consolehandler;
import productdata.Product;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

//receiver

/**
 * Class that works with one concrete Hashtable of Products
 */
public class TableManager {
    /**
     * Table which instance will work with
     */
    private Hashtable<String, Product> table;
    /**
     * Creation date of this Table Manager
     */
    private java.time.LocalDateTime Date;

    /**
     * Standard constructor
     * @param name Name of this Table Manager in the TableController`s table
     */
    public TableManager(String name){
        table = new Hashtable<>();
        TableController.put(name,this);
    }

    /**
     * Returns size of HashTable
     * @return Size
     */
    public int getSize(){
        return table.size();
    }

    /**
     * Put Product into Hashtable
     * @param index Key
     * @param product Product
     */
    public void put(String index, Product product){
        product.setId((long)TableController.getCurrentTable().getSize()+1);
        table.put(index,product);
    }

    /**
     * Replaces the Product with the new one
     * @param key Key of Product
     * @param product New Product
     */
    public void replace(String key,Product product){
        table.replace(key,table.get(key),product);
    }

    /**
     * Returns the Product from HashTable
     * @param index Key
     * @return Product
     */
    public Product get(String index){
        return table.get(index);
    }

    /**
     * Cleans the Hashtable
     */
    public void clear(){
        table.clear();
    }

    /**
     * Saves the table to the file
     * @param file Save to this file
     * @throws FileNotFoundException IF something went wrong
     */
    public void save(File file) throws FileNotFoundException {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            StringBuilder text = new StringBuilder();
            text.append(Date.toString()).append("\n");
            Map<Long, String> map = new HashMap<>();
            for (Map.Entry<String, Product> entry : table.entrySet()) {
                map.put(entry.getValue().getId(), entry.getKey());
            }
            Map<Long, String> sortedmap = new TreeMap<>(map);
            for (Map.Entry<Long, String> entry : sortedmap.entrySet()) {
                text.append(entry.getValue()).append(";").append(table.get(entry.getValue()).out()).append("\n");
            }
            try {
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                byte[] buffer = text.toString().getBytes();
                bos.write(buffer, 0, buffer.length);
                bos.flush();
                bos.close();
                System.out.println("Save complete...");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }catch (NullPointerException e){
            System.out.println("The directory has an error.");
        }
    }

    /**
     * Prints the table to the console
     */
    public String show(){
        StringBuilder stringBuilder = new StringBuilder("");
        table.forEach((k,v) -> stringBuilder.append(v.toString()));
        return stringBuilder.toString();
    }

    /**
     * Returns type of collection
     * @return name of type of collection
     */
    public String getType(){
        return table.getClass().getTypeName();
    }

    /**
     * Removes the Product from Hashtable
     * @param key Key of Product
     */
    public void remove(String key){
        table.remove(key);
    }

    /**
     * Returns the Set of Products
     * @return Set of Products
     */
    public Set<Map.Entry<String, Product>> getSet() {
        return table.entrySet();
    }

    /**
     * Returns the Collection of Products
     * @return Collection of Products
     */
    public Collection<Product> getProducts(){
        return table.values();
    }

    /**
     * Returns creation date of Table Manager
     * @return LocalDateTime
     */
    public LocalDateTime getCreationDate() {
        return Date;
    }

    /**
     * Sets creation date of Table Manager
     * @param date new creation date
     */
    void setCreationDate(LocalDateTime date){
            Date = date;
    }

    /**
     * Returns Set of keys
     * @return Set of keys of Hashtable
     */
    public Set<String> getKey() {
        return table.keySet();
    }

    public void setTable(Hashtable<String, Product> table) {
        this.table = table;
    }
}