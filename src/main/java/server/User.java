package server;

public class User {
    private String username;
    private String password;

    public User(){

    }

    public User(String string, String string1) {
        username = string;
        password = string1;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}