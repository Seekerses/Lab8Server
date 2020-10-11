package BD;

import java.sql.*;

public class DataHandler {
    // Table names
    public static final String USER_TABLE = "users";
    public static final String PRODUCTS_TABLE = "products";
    public static final String ORGANISATIONS_TABLE = "organisations";
    public static final String LOCATION_TABLE = "locations";
    // PRODUCTS_TABLE column names
    public static final String PRODUCTS_TABLE_ID_COLUMN = "id";
    public static final String PRODUCTS_TABLE_KEY_COLUMN = "key";
    public static final String PRODUCTS_TABLE_NAME_COLUMN = "name";
    public static final String PRODUCTS_TABLE_CREATION_DATE_COLUMN = "creation_time";
    public static final String PRODUCTS_TABLE_TYPE_COLUMN = "product_unitofmeasure";
    public static final String PRODUCTS_TABLE_PRICE_COLUMN = "price";
    public static final String PRODUCTS_TABLE_X_COLUMN = "x_coordinate";
    public static final String PRODUCTS_TABLE_Y_COLUMN = "y_coordinate";
    public static final String PRODUCTS_TABLE_USER_ID_COLUMN = "user_id";
    // USER_TABLE column names
    public static final String USER_TABLE_ID_COLUMN = "id";
    public static final String USER_TABLE_USERNAME_COLUMN = "username";
    public static final String USER_TABLE_PASSWORD_COLUMN = "password";
    // LOCATION_TABLE column names
    public static final String COORDINATES_TABLE_ID_COLUMN = "id";
    public static final String COORDINATES_TABLE_ORGANISATION_ID_COLUMN = "organisation_id";
    public static final String COORDINATES_TABLE_STREET_COLUMN = "street";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";
    public static final String COORDINATES_TABLE_Z_COLUMN = "z";
    // ORGANISATIONS_TABLE column names
    public static final String ORGANISATIONS_TABLE_ID_COLUMN = "id";
    public static final String ORGANISATIONS_TABLE_NAME_COLUMN = "name";
    public static final String ORGANISATIONS_TABLE_FULLNAME_COLUMN = "fullname";
    public static final String ORGANISATIONS_TABLE_TYPE_COLUMN = "type";
    public static final String ORGANISATIONS_TABLE_PRODUCT_ID_COLUMN = "product_id";

    private final String JDBC_DRIVER = "org.postgresql.Driver";

    private String url = "jdbc:postgresql://pg:5432/studs";
    private String user = "s285582";
    private String password = "sbq939";
    private Connection connection;
    private static volatile DataHandler instance;

    private DataHandler(){
        connectToDataBase();
    }

    public static DataHandler getInstance() {
        DataHandler localInstance = instance;
        if (localInstance == null) {
            synchronized (DataHandler.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DataHandler();
                }
            }
        }
        return localInstance;
    }

    public void connectToDataBase() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, user, password);
            createTableUsers();
            createTableProducts();
            createTableOrganisations();
            createTableLocations();
            new Thread(new ChangeObserver(connection)).start();
        } catch (SQLException | ClassNotFoundException exception) {
            System.out.println(exception);
        }
    }

    public void createTableProducts() {
        try {
            Statement st = connection.createStatement();
            boolean rs = st.execute("CREATE TABLE IF NOT EXISTS products (\n" +
                    "  id serial,\n" +
                    "  key varchar(50) NOT NULL,\n" +
                    "  name varchar(50) NOT NULL,\n" +
                    "  creation_time varchar(50) NOT NULL,\n" +
                    "  product_unitofmeasure varchar(50) NOT NULL,\n" +
                    "  price NUMERIC ,\n" +
                    "  x_coordinate NUMERIC NOT NULL,\n" +
                    "  y_coordinate NUMERIC NOT NULL,\n" +
                    "  user_id NUMERIC NOT NULL,\n" +
                    "  PRIMARY KEY (id)\n," +
                    "  UNIQUE(key)" +
                    ")");
            st.close();
        }catch (SQLException e){
            System.out.println("nepoladki");
        }
    }

    public void createTableUsers() throws SQLException {
        try {
            Statement stat = connection.createStatement();
            boolean rsat = stat.execute("CREATE TABLE IF NOT EXISTS users (\n" +
                    "  id serial,\n" +
                    "  username varchar(50) NOT NULL,\n" +
                    "  password varchar(150) NOT NULL,\n" +
                    "  PRIMARY KEY (id)\n," +
                    "  UNIQUE(username)" +
                    ")");
            stat.close();
        } catch (SQLException e) {
            System.out.println("nepoladki");
        }
    }

    public void createTableLocations() throws SQLException {
        try {
            Statement sta = connection.createStatement();
            boolean rsa = sta.execute("CREATE TABLE IF NOT EXISTS locations (\n" +
                    "  id serial,\n" +
                    "  organisation_id NUMERIC,\n" +
                    "  street varchar(50),\n" +
                    "  x NUMERIC NOT NULL ,\n" +
                    "  y NUMERIC NOT NULL ,\n" +
                    "  z NUMERIC NOT NULL ,\n" +
                    "  PRIMARY KEY (id)\n" +
                    ")");
            sta.close();
        }catch (SQLException e){
            System.out.println("nepoladki");
        }
    }

    public void createTableOrganisations() throws SQLException {
        try {
            Statement state = connection.createStatement();
            boolean rsate = state.execute("CREATE TABLE IF NOT EXISTS organisations (\n" +
                    "  id serial,\n" +
                    "  name varchar(50),\n" +
                    "  fullname varchar(50),\n" +
                    "  type varchar(50),\n" +
                    "  product_id NUMERIC,\n" +
                    "  PRIMARY KEY (id)\n," +
                    "  UNIQUE (fullname)\n" +
                    ")");
            state.close();
        }catch (SQLException e){
            System.out.println("nepoladki");
        }
    }

    public void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
            System.out.println("Соединение разорвано");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException {
        PreparedStatement preparedStatement;
        try {
            if (connection == null) throw new SQLException();
            int autoGeneratedKeys = generateKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            preparedStatement = connection.prepareStatement(sqlStatement, autoGeneratedKeys);
            return preparedStatement;
        } catch (SQLException exception) {
            if (connection == null) System.out.println("Соединение с базой данных не установлено!");
            throw new SQLException(exception);
        }
    }

    public void closePreparedStatement(PreparedStatement sqlStatement) {
        if (sqlStatement == null) return;
        try {
            sqlStatement.close();
        } catch (SQLException exception) {
            System.out.println("Error while closing statement");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setCommitMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(false);
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при установлении режима транзакции базы данных!");
        }
    }

    public void setNormalMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при установлении нормального режима базы данных!");
        }
    }

    public void commit() {
        try {
            if (connection == null) throw new SQLException();
            connection.commit();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при подтверждении нового состояния базы данных!");
        }
    }

    public void rollback() {
        try {
            if (connection == null) throw new SQLException();
            connection.rollback();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при возврате исходного состояния базы данных!");
        }
    }

    public void setSavepoint() {
        try {
            if (connection == null) throw new SQLException();
            connection.setSavepoint();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при сохранении состояния базы данных!");
        }
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}