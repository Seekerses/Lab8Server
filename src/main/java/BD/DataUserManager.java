package BD;

import clientserverdata.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A manager of user BDase.
 */
public class DataUserManager {
    // USER_TABLE
    private final String SELECT_USER_BY_ID = "SELECT * FROM " + BD.DataHandler.USER_TABLE +
            " WHERE " + BD.DataHandler.USER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM " + BD.DataHandler.USER_TABLE +
            " WHERE " + BD.DataHandler.USER_TABLE_USERNAME_COLUMN + " = ?";
    private final String SELECT_USER_BY_PASSWORD = "SELECT * FROM " + BD.DataHandler.USER_TABLE +
            " WHERE " + BD.DataHandler.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = SELECT_USER_BY_USERNAME + " AND " +
            BD.DataHandler.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String INSERT_USER = "INSERT INTO " +
            BD.DataHandler.USER_TABLE + " VALUES (DEFAULT, ?, ?)";

    private DataHandler DataHandler;

    public DataUserManager(DataHandler DataHandler) {
        this.DataHandler = DataHandler;
    }

    public User getUserById(long userId) throws SQLException {
        User user = null;
        PreparedStatement preparedSelectUserByIdStatement = null;
        try {
            preparedSelectUserByIdStatement =
                    DataHandler.getPreparedStatement(SELECT_USER_BY_ID, false);
            preparedSelectUserByIdStatement.setLong(1, userId);
            ResultSet resultSet = preparedSelectUserByIdStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getString(BD.DataHandler.USER_TABLE_USERNAME_COLUMN),
                        resultSet.getString(BD.DataHandler.USER_TABLE_PASSWORD_COLUMN)
                );
            }         } catch (SQLException exception) {
            System.out.println(exception.getSQLState()+ " " + exception.getErrorCode());
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_ID!");
            throw new SQLException(exception);
        } finally {
            DataHandler.closePreparedStatement(preparedSelectUserByIdStatement);
        }
        return user;
    }

    public boolean checkUserbyUsername(User user){
        PreparedStatement preparedSelectUserByUsernameStatement = null;
        try {
            preparedSelectUserByUsernameStatement =
                    DataHandler.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedSelectUserByUsernameStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedSelectUserByUsernameStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME_AND_PASSWORD!");
        } finally {
            DataHandler.closePreparedStatement(preparedSelectUserByUsernameStatement);
        }
        return false;
    }

    public boolean checkUserbyPassword(User user){
        PreparedStatement preparedSelectUserByPasswordStatement = null;
        try {
            preparedSelectUserByPasswordStatement =
                    DataHandler.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedSelectUserByPasswordStatement.setString(1, PasswordHasher.hashPassword(user.getPassword()));
            ResultSet resultSet = preparedSelectUserByPasswordStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME_AND_PASSWORD!");
        } finally {
            DataHandler.closePreparedStatement(preparedSelectUserByPasswordStatement);
        }
        return false;
    }

    public boolean checkUserByUsernameAndPassword(User user) {
        if(user.getPassword() == null && user.getUsername() == null){
            return true;
        }
        PreparedStatement preparedSelectUserByUsernameAndPasswordStatement = null;
        try {
            preparedSelectUserByUsernameAndPasswordStatement =
                    DataHandler.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedSelectUserByUsernameAndPasswordStatement.setString(1, user.getUsername());
            preparedSelectUserByUsernameAndPasswordStatement.setString(2, PasswordHasher.hashPassword(user.getPassword()));
            ResultSet resultSet = preparedSelectUserByUsernameAndPasswordStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME_AND_PASSWORD!");
        } finally {
            DataHandler.closePreparedStatement(preparedSelectUserByUsernameAndPasswordStatement);
        }
        return false;
    }

    public long getUserIdByUsername(User user) {
        long userId;
        PreparedStatement preparedSelectUserByUsernameStatement = null;
        try {
            preparedSelectUserByUsernameStatement =
                    DataHandler.getPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedSelectUserByUsernameStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedSelectUserByUsernameStatement.executeQuery();
            System.out.println("Выполнен запрос SELECT_USER_BY_USERNAME.");
            if (resultSet.next()) {
                userId = resultSet.getLong(BD.DataHandler.USER_TABLE_ID_COLUMN);
            } else userId = -1;
            return userId;
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME!");
        } finally {
            DataHandler.closePreparedStatement(preparedSelectUserByUsernameStatement);
        }
        return -1;
    }

    public boolean insertUser(User user) {
        PreparedStatement preparedInsertUserStatement = null;
        try {
            preparedInsertUserStatement =
                    DataHandler.getPreparedStatement(INSERT_USER, false);
            preparedInsertUserStatement.setString(1, user.getUsername());
            preparedInsertUserStatement.setString(2, PasswordHasher.hashPassword(user.getPassword()));
            if (preparedInsertUserStatement.executeUpdate() == 0) throw new SQLException();
            System.out.println("Выполнен запрос INSERT_USER.");
            return true;
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса INSERT_USER!");
        } finally {
            DataHandler.closePreparedStatement(preparedInsertUserStatement);
        }
        return false;
    }
}