package consolehandler;
import BD.DataHandler;
import BD.DataManager;
import BD.DataUserManager;
import productdata.Product;
import server.ServerController;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Class that works with one concrete Hashtable of Products
 */
public class TableManager {
    private DataManager databaseManager;
    private DataHandler dataHandler;
    private DataUserManager userManager;
    /**
     * Table which instance will work with
     */
    private volatile Hashtable<String, Product> table;
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
        ServerController.getScheduler().getCollectionLock().lock();
        try {
            product.setId((long)TableController.getCurrentTable().getSize()+1);
            table.put(index,product);
        }
        finally {
            ServerController.getScheduler().getCollectionLock().unlock();
        }
    }

    /**
     * Replaces the Product with the new one
     * @param key Key of Product
     * @param product New Product
     */
    public void replace(String key,Product product){
        ServerController.getScheduler().getCollectionLock().lock();
        try {
            table.replace(key,table.get(key),product);
        }
        finally {
            ServerController.getScheduler().getCollectionLock().unlock();
        }
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
        ServerController.getScheduler().getCollectionLock().lock();
        try {
            table.clear();
        }
        finally {
            ServerController.getScheduler().getCollectionLock().unlock();
        }
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
        ServerController.getScheduler().getCollectionLock().lock();
        try {
            table.remove(key);
        }
        finally {
            ServerController.getScheduler().getCollectionLock().unlock();
        }
    }

    /**
     * Returns the Set of Products
     * @return Set of Products
     */
    public Set<Map.Entry<String, Product>> getSet() {
        ServerController.getScheduler().getCollectionLock().lock();
        try {
            return table.entrySet();
        }
        finally {
            ServerController.getScheduler().getCollectionLock().unlock();
        }
    }

    /**
     * Returns the Collection of Products
     * @return Collection of Products
     */
    public Collection<Product> getProducts(){
        ServerController.getScheduler().getCollectionLock().lock();
        try {
            return table.values();
        }
        finally {
            ServerController.getScheduler().getCollectionLock().unlock();
        }
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
        ServerController.getScheduler().getCollectionLock().lock();
        try {
            Date = date;
        }
        finally {
            ServerController.getScheduler().getCollectionLock().unlock();
        }
    }

    /**
     * Returns Set of keys
     * @return Set of keys of Hashtable
     */
    public Set<String> getKey() {
        return table.keySet();
    }

    public void loadCollection() {
        try {
            dataHandler = new DataHandler();
            userManager = new DataUserManager(dataHandler);
            databaseManager = new DataManager(dataHandler, userManager);
            table = databaseManager.getCollection();
            System.out.println("Коллекция загружена.");
        } catch (Exception exception) {
            System.out.println("Коллекция не может быть загружена!");
        }
    }

}