
import BD.DataHandler;
import BD.DataManager;
import BD.DataUserManager;
import consolehandler.*;
import server.ServerController;
import server.ServerScheduler;

import java.io.*;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) throws InterruptedException, SQLException {
        ServerController.connect();
        ServerController.setScheduler(new ServerScheduler(5,5));
        Thread initiateThread = new Thread(ServerController.getScheduler());
        initiateThread.start();
        try {
            initiateThread.join();
        } catch (InterruptedException e){
            System.out.println("Loading of scheduler was interrupted!");
        }
        new Thread(() -> {
            try {
                ServerController.start();
            }
            catch (IOException e){
                System.out.println("Oh, no. Server got a wrong data and fall...");
            }
        }).start();
        Thread.sleep(1000);
        TableManager prodTable = new TableManager("products");
        TableController.setCurrentTable(prodTable);
        try {
            if( new File("saved.csv").createNewFile()){
                System.out.println("Save file created.");
            }
        }
        catch (Exception e ){
            System.out.println("Could not create default save file, please specify it manually\n");
        }
        if(args.length != 0) {
            Initializer.init(prodTable, new File(args[0]));
        }
        else {
            try {
                Initializer.init(prodTable, new File("saved.csv").exists() ? new File("saved.csv") : null);
            } catch (NullPointerException e) {
                Initializer.init(prodTable, null);
            }
        }
        DataHandler handler = new DataHandler();
        handler.setPassword("unravel");
        handler.setUser("postgres");
        handler.setUrl("jdbc:postgresql://localhost:5432/postgres");
        handler.connectToDataBase();
        handler.createTableProducts();
        handler.createTableUsers();
        handler.createTableOrganisations();
        handler.createTableLocations();
        CommandController cmd = new CommandController();
        cmd.start(new CommandInterpreter());
        System.out.println("Enter Command or Help to display a list of commands:");
    }
}
