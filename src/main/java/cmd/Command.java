package cmd;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public interface Command extends Serializable {

    String execute(String[] args) throws IOException;

}
