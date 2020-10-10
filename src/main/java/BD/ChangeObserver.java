package BD;

import consolehandler.TableController;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class ChangeObserver implements Runnable {

    private Connection connection;
    private PGConnection pgConnection;

    public ChangeObserver(Connection connection) throws SQLException {
        this.connection = connection;
        this.pgConnection = (PGConnection) (this.connection);
        Statement listenSubscribe = this.connection.createStatement();
        listenSubscribe.execute("LISTEN listener");
        listenSubscribe.close();
        createListenerTrigger();
    }

    @Override
    public void run() {
        try {
        while (true){
                PGNotification[] notifications = pgConnection.getNotifications(4);

                if (notifications != null && notifications.length != 0) {
                    TableController.getCurrentTable().loadCollection();
                    notifications = null;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                }

    } catch (SQLException ex){
            ex.printStackTrace();
        System.out.println("Daemon listener is down.");
    }
}

    private void createListenerTrigger() throws SQLException {

        Statement trigger = connection.createStatement();
        trigger.execute(
        "CREATE OR REPLACE FUNCTION notificator()\n" +
                "RETURNS trigger AS\n" +
                "$$\n" +
                "begin\n" +
                "PERFORM pg_notify('listener','change');\n" +
                "RETURN NEW;\n" +
                "end\n" +
                "$$ LANGUAGE plpgsql;\n" +
                "\n" +
                "DROP TRIGGER IF EXISTS add_notify on products;\n" +
                "\n" +
                "CREATE TRIGGER add_notify\n" +
                "AFTER INSERT OR DELETE OR UPDATE\n" +
                "ON products\n" +
                "FOR EACH ROW \n" +
                "EXECUTE PROCEDURE notificator();");
        trigger.close();
    }
}
