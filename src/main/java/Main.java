
import BD.DataHandler;
import consolehandler.*;
import server.ServerController;
import server.ServerScheduler;

import java.io.*;
import org.postgresql.Driver;
import java.sql.SQLException;
import java.util.jar.JarFile;

public class Main {

    public static void main(String[] args) throws InterruptedException, SQLException{
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

        DataHandler handler = new DataHandler();
        handler.connectToDataBase();
        prodTable.loadCollection();
        CommandController cmd = new CommandController();
        cmd.start(new CommandInterpreter());
        System.out.println("Enter Command or Help to display a list of commands:");
    }
}
