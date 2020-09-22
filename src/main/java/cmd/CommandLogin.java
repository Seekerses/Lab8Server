package cmd;

import BD.DataHandler;
import BD.DataUserManager;
import server.User;

import javax.imageio.spi.RegisterableService;
import java.io.IOException;
import java.util.Scanner;

public class CommandLogin implements Command, Preparable, Registerable {

    private String login;
    private String password;
    private static final long serialVersionUID = 1337000023L;

    @Override
    public String execute(String[] args) throws IOException {
        DataHandler handler = new DataHandler();
        handler.setUser(login);
        handler.setPassword(password);
        DataUserManager userManager = new DataUserManager(handler);
        User user = new User();
        if (userManager.checkUserByUsernameAndPassword(user)) System.out.println("Пользователь " +
                user.getUsername() + " авторизован.");
        return "Approved";
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
