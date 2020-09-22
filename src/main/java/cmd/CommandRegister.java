package cmd;

import java.io.IOException;
import java.util.Scanner;

public class CommandRegister implements Command, Preparable, Registerable {

    private String login;
    private String password;
    private static final long serialVersionUID = 1337000050L;

    @Override
    public String execute(String[] args) throws IOException {
        //Логика запроса к БД
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
