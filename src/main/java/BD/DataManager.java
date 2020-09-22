package BD;

import exceptions.InvalidYCoordinate;
import exceptions.NegativePrice;
import exceptions.NotUniqueFullName;
import exceptions.TooLargeFullName;
import productdata.*;
import server.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Hashtable;

public class DataManager {
    // PRODUCTS_TABLE
    private final String SELECT_ALL_PRODUCTS = "SELECT * FROM " + BD.DataHandler.PRODUCTS_TABLE;
    private final String SELECT_PRODUCTS_BY_ID = SELECT_ALL_PRODUCTS + " WHERE " +
            BD.DataHandler.PRODUCTS_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_PRODUCTS_BY_ID_AND_USER_ID = SELECT_PRODUCTS_BY_ID + " AND " +
            BD.DataHandler.PRODUCTS_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_PRODUCTS = "INSERT INTO " +
            BD.DataHandler.PRODUCTS_TABLE + " (" +
            BD.DataHandler.PRODUCTS_TABLE_KEY_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_NAME_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_CREATION_DATE_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_TYPE_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_X_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_Y_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_PRICE_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String DELETE_PRODUCTS_BY_ID = "DELETE FROM " + BD.DataHandler.PRODUCTS_TABLE +
            " WHERE " + BD.DataHandler.PRODUCTS_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PRODUCTS_NAME_BY_ID = "UPDATE " + BD.DataHandler.PRODUCTS_TABLE + " SET " +
            BD.DataHandler.PRODUCTS_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            BD.DataHandler.PRODUCTS_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PRODUCTS_TYPE_BY_ID = "UPDATE " + BD.DataHandler.PRODUCTS_TABLE + " SET " +
            BD.DataHandler.PRODUCTS_TABLE_TYPE_COLUMN + " = ?" + " WHERE " +
            BD.DataHandler.PRODUCTS_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PRODUCTS_X_BY_ID = "UPDATE " + BD.DataHandler.PRODUCTS_TABLE + " SET " +
            BD.DataHandler.PRODUCTS_TABLE_X_COLUMN + " = ?" + " WHERE " +
            BD.DataHandler.PRODUCTS_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PRODUCTS_Y_BY_ID = "UPDATE " + BD.DataHandler.PRODUCTS_TABLE + " SET " +
            BD.DataHandler.PRODUCTS_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            BD.DataHandler.PRODUCTS_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PRODUCTS_PRICE_BY_ID = "UPDATE " + BD.DataHandler.PRODUCTS_TABLE + " SET " +
            BD.DataHandler.PRODUCTS_TABLE_PRICE_COLUMN + " = ?" + " WHERE " +
            BD.DataHandler.PRODUCTS_TABLE_ID_COLUMN + " = ?";
    // COORDINATES_TABLE
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + BD.DataHandler.LOCATION_TABLE;
    private final String SELECT_COORDINATES_BY_ORGANISATION_ID = SELECT_ALL_COORDINATES +
            " WHERE " + BD.DataHandler.COORDINATES_TABLE_ORGANISATION_ID_COLUMN + " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            BD.DataHandler.LOCATION_TABLE + " (" +
            BD.DataHandler.COORDINATES_TABLE_ORGANISATION_ID_COLUMN + ", " +
            BD.DataHandler.COORDINATES_TABLE_STREET_COLUMN + ", " +
            BD.DataHandler.COORDINATES_TABLE_X_COLUMN + ", " +
            BD.DataHandler.COORDINATES_TABLE_Y_COLUMN + ", " +
            BD.DataHandler.COORDINATES_TABLE_Z_COLUMN + ") VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_COORDINATES_BY_ORGANISATION_ID = "UPDATE " + BD.DataHandler.LOCATION_TABLE + " SET " +
            BD.DataHandler.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            BD.DataHandler.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            BD.DataHandler.COORDINATES_TABLE_ORGANISATION_ID_COLUMN + " = ?";
    // ORGANISATIONS_TABLE
    private final String SELECT_ALL_ORGANISATIONS = "SELECT * FROM " + BD.DataHandler.ORGANISATIONS_TABLE;
    private final String SELECT_ORGANISATIONS_BY_ID = SELECT_ALL_ORGANISATIONS +
            " WHERE " + BD.DataHandler.ORGANISATIONS_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_ORGANISATIONS = "INSERT INTO " +
            BD.DataHandler.ORGANISATIONS_TABLE + " (" +
            BD.DataHandler.ORGANISATIONS_TABLE_NAME_COLUMN + ", " +
            BD.DataHandler.ORGANISATIONS_TABLE_FULLNAME_COLUMN + ", " +
            BD.DataHandler.ORGANISATIONS_TABLE_TYPE_COLUMN + ", " +
            BD.DataHandler.ORGANISATIONS_TABLE_PRODUCT_ID_COLUMN + ", " + ") VALUES (?, ?, ?, ?)";
    private final String UPDATE_ORGANISATIONS_BY_ID = "UPDATE " + BD.DataHandler.ORGANISATIONS_TABLE + " SET " +
            BD.DataHandler.ORGANISATIONS_TABLE_NAME_COLUMN + " = ?, " +
            BD.DataHandler.ORGANISATIONS_TABLE_FULLNAME_COLUMN + " = ?, " +
            BD.DataHandler.ORGANISATIONS_TABLE_TYPE_COLUMN + " = ?, " + " WHERE " +
            BD.DataHandler.ORGANISATIONS_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_ORGANISATIONS_BY_ID = "DELETE FROM " + BD.DataHandler.ORGANISATIONS_TABLE +
            " WHERE " + BD.DataHandler.ORGANISATIONS_TABLE_ID_COLUMN + " = ?";
    private DataHandler DataHandler;
    private DataUserManager dataUserManager;

    public DataManager(DataHandler DataHandler, DataUserManager userManager) {
        this.DataHandler = DataHandler;
        this.dataUserManager = userManager;
    }

    private Product createProduct(ResultSet resultSet) throws SQLException, NegativePrice, NotUniqueFullName, TooLargeFullName, InvalidYCoordinate {
        long id = resultSet.getLong(BD.DataHandler.COORDINATES_TABLE_ID_COLUMN);
        String name = resultSet.getString(BD.DataHandler.PRODUCTS_TABLE_NAME_COLUMN);
        LocalDateTime creationDate = resultSet.getTimestamp(BD.DataHandler.PRODUCTS_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        UnitOfMeasure type = UnitOfMeasure.valueOf(resultSet.getString(BD.DataHandler.PRODUCTS_TABLE_TYPE_COLUMN));
        double px = resultSet.getDouble(BD.DataHandler.PRODUCTS_TABLE_X_COLUMN);
        int py = resultSet.getInt(BD.DataHandler.PRODUCTS_TABLE_Y_COLUMN);
        Float price = resultSet.getFloat(BD.DataHandler.PRODUCTS_TABLE_PRICE_COLUMN);
        int org_id = resultSet.getInt(BD.DataHandler.COORDINATES_TABLE_ID_COLUMN);
        String org_name = resultSet.getString(BD.DataHandler.ORGANISATIONS_TABLE_NAME_COLUMN);
        String org_fullname = resultSet.getString(BD.DataHandler.ORGANISATIONS_TABLE_FULLNAME_COLUMN);
        OrganizationType org_type = OrganizationType.valueOf(resultSet.getString(BD.DataHandler.ORGANISATIONS_TABLE_TYPE_COLUMN));
        String street = resultSet.getString(BD.DataHandler.COORDINATES_TABLE_STREET_COLUMN);
        long x = resultSet.getLong(BD.DataHandler.COORDINATES_TABLE_X_COLUMN);
        int y = resultSet.getInt(BD.DataHandler.COORDINATES_TABLE_Y_COLUMN);
        long z = resultSet.getLong(BD.DataHandler.COORDINATES_TABLE_Z_COLUMN);
        Location loc = new Location(x,y,z);
        Address adr = new Address(street, loc);
        Organization org = new Organization(org_id, org_name, org_fullname, org_type, adr);
        User owner = dataUserManager.getUserById(resultSet.getLong(BD.DataHandler.PRODUCTS_TABLE_USER_ID_COLUMN));
        return new Product(
                id,
                name,
                new Coordinates(px, py),
                price,
                type,
                org,
                creationDate
        );
    }

    public boolean insertProduct(Product product, String key, User user) throws SQLException {
        PreparedStatement insertProductStatement = null;
        PreparedStatement insertOrganisationStatement =null;
        PreparedStatement insertLocationStatement = null;

        try{
            DataHandler.setCommitMode();
            DataHandler.setSavepoint();

            LocalDateTime creationtime = LocalDateTime.now();

            insertProductStatement = DataHandler.getPreparedStatement(INSERT_PRODUCTS,true);
            insertOrganisationStatement = DataHandler.getPreparedStatement(INSERT_ORGANISATIONS, true);
            insertLocationStatement = DataHandler.getPreparedStatement(INSERT_COORDINATES,true);

            insertProductStatement.setString(1, key);
            insertProductStatement.setString(2,product.getName());
            insertProductStatement.setTimestamp(3, Timestamp.valueOf(creationtime));
            insertProductStatement.setString(4, product.getUnitOfMeasure().toString());
            insertProductStatement.setDouble(5, product.getCoordinates().getX());
            insertProductStatement.setInt(6, product.getCoordinates().getY());
            insertProductStatement.setFloat(7, product.getPrice());
            insertProductStatement.setLong(8, dataUserManager.getUserIdByUsername(user));
            if (insertProductStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedChapterKeys = insertProductStatement.getGeneratedKeys();
            long productId;
            if (generatedChapterKeys.next()) {
                productId = generatedChapterKeys.getLong(1);
            } else throw new SQLException();

            insertOrganisationStatement.setString(1, product.getManufacturer().getName());
            insertOrganisationStatement.setString(2, product.getManufacturer().getFullName());
            insertOrganisationStatement.setString(3, product.getManufacturer().getType().toString());
            insertOrganisationStatement.setLong(4, productId);
            if (insertOrganisationStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedMarineKeys = insertOrganisationStatement.getGeneratedKeys();
            long orgId;
            if (generatedMarineKeys.next()) {
                orgId = generatedMarineKeys.getLong(1);
            } else throw new SQLException();

            insertLocationStatement.setLong(1, orgId);
            insertLocationStatement.setString(2, product.getManufacturer().getPostalAddress().getStreet());
            insertLocationStatement.setDouble(3, product.getManufacturer().getPostalAddress().getTown().getX());
            insertLocationStatement.setInt(4, product.getManufacturer().getPostalAddress().getTown().getY());
            insertLocationStatement.setLong(5, product.getManufacturer().getPostalAddress().getTown().getZ());
            if (insertLocationStatement.executeUpdate() == 0) throw new SQLException();
            DataHandler.commit();
            return true;
        } catch (SQLException e) {
            DataHandler.rollback();
            e.printStackTrace();
        }finally {
            DataHandler.closePreparedStatement(insertLocationStatement);
            DataHandler.closePreparedStatement(insertOrganisationStatement);
            DataHandler.closePreparedStatement(insertProductStatement);
            DataHandler.setNormalMode();
        }
        return false;
    }

    public Hashtable<String,Product> getCollection(){
        Hashtable<String,Product> products = new Hashtable<>();
        PreparedStatement preparedSelectAllStatement = null;
        try {
            preparedSelectAllStatement = DataHandler.getPreparedStatement(SELECT_ALL_PRODUCTS, false);
            ResultSet resultSet = preparedSelectAllStatement.executeQuery();
            while (resultSet.next()) {
                products.put(resultSet.getString(BD.DataHandler.PRODUCTS_TABLE_KEY_COLUMN),createProduct(resultSet));
            }
        } catch (SQLException exception) {
            System.out.println("Something went wrong with BD");
        } catch (NotUniqueFullName | InvalidYCoordinate | TooLargeFullName | NegativePrice notUniqueFullName) {
            notUniqueFullName.printStackTrace();
        } finally {
            DataHandler.closePreparedStatement(preparedSelectAllStatement);
        }
        return products;
    }

}