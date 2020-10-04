package cmd;

import BD.DataHandler;
import BD.DataUserManager;
import server.User;

import java.io.IOException;
import java.util.Scanner;

public class CommandRegister implements Command, Preparable, Registerable {

    private String login;
    private String password;
    private static final long serialVersionUID = 1337000050L;

    @Override
    public String execute(String[] args){
        User user =  new User();
        try {
            DataUserManager manager = new DataUserManager(new DataHandler());
            user.setUsername(login);
            user.setPassword(password);
            if (manager.insertUser(user)) {
                System.out.println("User " +
                        user.getUsername() + " has been registered.");
                return "Approved";

            }else{
            return "User already registered. Try to log in";}
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Try again later...");
        }
        return "Can't register you";
    }

    @Override
    public void prepare(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter login:\n>");
        login = scanner.next().trim();
        System.out.println("Enter password:\n>");
        password = scanner.next().trim();
    }
}
