package cmd;

import BD.DataHandler;
import BD.DataUserManager;
import server.User;

import java.io.IOException;
import java.util.Scanner;

public class CommandRegister implements Command, Preparable, Registerable {

    private String login = "";
    private String password = "";
    private static final long serialVersionUID = 1337000050L;

    @Override
    public String execute(String[] args) throws IOException {
        if(login.equals("") || password.equals("")){
            prepare(args);
            execute(args);
        }
        DataHandler handler = new DataHandler();
        DataUserManager userManager = new DataUserManager(handler);
        User user =  new User();
        try {
            if (args.length == 0) {
                System.out.println("Enter username + password: ");
            } else {
                user.setUsername(login);
                user.setPassword(password);
                if (userManager.insertUser(user)) {
                    System.out.println("User " +
                            user.getUsername() + " has been registered.");
                    return "User has been registered.";
                }
            }
            return null;
        }catch (Exception e){
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
