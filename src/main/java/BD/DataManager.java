package BD;

import exceptions.InvalidYCoordinate;
import exceptions.NegativePrice;
import exceptions.NotUniqueFullName;
import exceptions.TooLargeFullName;
import org.postgresql.util.PSQLException;
import productdata.*;
import server.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class DataManager {
    private static final String SELECT_USER_BY_ID = "SELECT * FROM " + BD.DataHandler.USER_TABLE + " WHERE " +
            BD.DataHandler.USER_TABLE_ID_COLUMN + " = ?";
    // PRODUCTS_TABLE
    private final String SELECT_ALL_PRODUCTS = "SELECT * FROM " + BD.DataHandler.PRODUCTS_TABLE;
    private final String SELECT_ALL_PRODUCTS_BY_USER_ID = "SELECT * FROM " + BD.DataHandler.PRODUCTS_TABLE + " WHERE " +
            BD.DataHandler.PRODUCTS_TABLE_USER_ID_COLUMN + " = ?";
    private final String SELECT_PRODUCTS_BY_ID = SELECT_ALL_PRODUCTS + " WHERE " +
            BD.DataHandler.PRODUCTS_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_PRODUCTS_BY_ID_AND_USER_ID = SELECT_PRODUCTS_BY_ID + " AND " +
            BD.DataHandler.PRODUCTS_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_PRODUCTS = "INSERT INTO " +
            BD.DataHandler.PRODUCTS_TABLE + " (" +
            BD.DataHandler.PRODUCTS_TABLE_ID_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_KEY_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_NAME_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_CREATION_DATE_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_TYPE_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_X_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_Y_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_PRICE_COLUMN + ", " +
            BD.DataHandler.PRODUCTS_TABLE_USER_ID_COLUMN + ") VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String DELETE_PRODUCTS_BY_USER_ID = "DELETE FROM " + BD.DataHandler.PRODUCTS_TABLE +
            " WHERE " + BD.DataHandler.PRODUCTS_TABLE_USER_ID_COLUMN + " = ?";
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
            BD.DataHandler.COORDINATES_TABLE_ID_COLUMN + ", " +
            BD.DataHandler.COORDINATES_TABLE_ORGANISATION_ID_COLUMN + ", " +
            BD.DataHandler.COORDINATES_TABLE_STREET_COLUMN + ", " +
            BD.DataHandler.COORDINATES_TABLE_X_COLUMN + ", " +
            BD.DataHandler.COORDINATES_TABLE_Y_COLUMN + ", " +
            BD.DataHandler.COORDINATES_TABLE_Z_COLUMN + ") VALUES (DEFAULT, ?, ?, ?, ?, ?)";
    private final String UPDATE_COORDINATES_BY_ORGANISATION_ID = "UPDATE " + BD.DataHandler.LOCATION_TABLE + " SET " +
            BD.DataHandler.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            BD.DataHandler.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            BD.DataHandler.COORDINATES_TABLE_ORGANISATION_ID_COLUMN + " = ?";
    private final String DELETE_COORDINATES_BY_ORGANISATION_ID = "DELETE FROM " + BD.DataHandler.LOCATION_TABLE +
            " WHERE " + BD.DataHandler.COORDINATES_TABLE_ORGANISATION_ID_COLUMN + " = ?";

    // ORGANISATIONS_TABLE
    private final String SELECT_ALL_ORGANISATIONS = "SELECT * FROM " + BD.DataHandler.ORGANISATIONS_TABLE;
    private final String SELECT_ORGANISATIONS_BY_ID = SELECT_ALL_ORGANISATIONS +
            " WHERE " + BD.DataHandler.ORGANISATIONS_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_ORGANISATIONS_BY_PRODUCT_ID = SELECT_ALL_ORGANISATIONS +
            " WHERE " + BD.DataHandler.ORGANISATIONS_TABLE_PRODUCT_ID_COLUMN + " = ?";
    private final String INSERT_ORGANISATIONS = "INSERT INTO " +
            BD.DataHandler.ORGANISATIONS_TABLE + " (" +
            BD.DataHandler.ORGANISATIONS_TABLE_ID_COLUMN + ", " +
            BD.DataHandler.ORGANISATIONS_TABLE_NAME_COLUMN + ", " +
            BD.DataHandler.ORGANISATIONS_TABLE_FULLNAME_COLUMN + ", " +
            BD.DataHandler.ORGANISATIONS_TABLE_TYPE_COLUMN + ", " +
            BD.DataHandler.ORGANISATIONS_TABLE_PRODUCT_ID_COLUMN  + ") VALUES (DEFAULT, ?, ?, ?, ?)";
    private final String UPDATE_ORGANISATIONS_BY_PRODUCT_ID = "UPDATE " + BD.DataHandler.ORGANISATIONS_TABLE + " SET " +
            BD.DataHandler.ORGANISATIONS_TABLE_NAME_COLUMN + " = ?, " +
            BD.DataHandler.ORGANISATIONS_TABLE_FULLNAME_COLUMN + " = ?, " +
            BD.DataHandler.ORGANISATIONS_TABLE_TYPE_COLUMN + " = ?, " + " WHERE " +
            BD.DataHandler.PRODUCTS_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_ORGANISATIONS_BY_ID = "DELETE FROM " + BD.DataHandler.ORGANISATIONS_TABLE +
            " WHERE " + BD.DataHandler.ORGANISATIONS_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_ORGANISATIONS_BY_PRODUCT_ID = "DELETE FROM " + BD.DataHandler.ORGANISATIONS_TABLE +
            " WHERE " + BD.DataHandler.ORGANISATIONS_TABLE_PRODUCT_ID_COLUMN + " = ?";

    private DataHandler DataHandler;
    private DataUserManager dataUserManager;

    public DataManager(DataHandler DataHandler, DataUserManager userManager) {
        this.DataHandler = DataHandler;
        this.dataUserManager = userManager;
    }

    private Product createProduct(ResultSet resultSet, Map<Long, Organization> organizationMap) throws SQLException, NegativePrice, NotUniqueFullName, TooLargeFullName, InvalidYCoordinate {
        try {
            long id = resultSet.getLong("id");
            String name = resultSet.getString(BD.DataHandler.PRODUCTS_TABLE_NAME_COLUMN);
            LocalDateTime creationDate = resultSet.getTimestamp(BD.DataHandler.PRODUCTS_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
            UnitOfMeasure type = UnitOfMeasure.valueOf(resultSet.getString(BD.DataHandler.PRODUCTS_TABLE_TYPE_COLUMN));
            double px = resultSet.getDouble(BD.DataHandler.PRODUCTS_TABLE_X_COLUMN);
            int py = resultSet.getInt(BD.DataHandler.PRODUCTS_TABLE_Y_COLUMN);
            Float price = resultSet.getFloat(BD.DataHandler.PRODUCTS_TABLE_PRICE_COLUMN);
            User owner = dataUserManager.getUserById(resultSet.getLong(BD.DataHandler.PRODUCTS_TABLE_USER_ID_COLUMN));
            Product product = new Product(
                    id,
                    name,
                    new Coordinates(px, py),
                    price,
                    type,
                    organizationMap.get(id),
                    creationDate
            );
            product.setOwner(owner);
            return product;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private Organization createOrganisation(ResultSet resultSet, Map<Integer, Address> addressMap){
        try {
            int org_id = resultSet.getInt(BD.DataHandler.ORGANISATIONS_TABLE_ID_COLUMN);
            String org_name = resultSet.getString(BD.DataHandler.ORGANISATIONS_TABLE_NAME_COLUMN);
            String org_fname = resultSet.getString("fullname");
            OrganizationType org_type = OrganizationType.valueOf(resultSet.getString(BD.DataHandler.ORGANISATIONS_TABLE_TYPE_COLUMN));
            UniqueController.deleteRow(org_fname);
            Organization organisation = new Organization(
                    org_id,
                    org_name,
                    org_fname,
                    org_type,
                    addressMap.get(org_id)
                    );
            return organisation;
        }catch (SQLException | TooLargeFullName | NotUniqueFullName e){
            e.printStackTrace();
        }
        return null;
    }

    private Address createLocation(ResultSet resultSet) {
        try {
            String street = resultSet.getString(BD.DataHandler.COORDINATES_TABLE_STREET_COLUMN);
            long x = resultSet.getLong(BD.DataHandler.COORDINATES_TABLE_X_COLUMN);
            int y = resultSet.getInt(BD.DataHandler.COORDINATES_TABLE_Y_COLUMN);
            long z = resultSet.getLong(BD.DataHandler.COORDINATES_TABLE_Z_COLUMN);
            Location loc = new Location(x, y, z);
            Address adr = new Address(street, loc);
            return adr;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertProduct(Product product, String key, User user) throws SQLException {
        PreparedStatement insertProductStatement = null;
        PreparedStatement insertOrganisationStatement =null;
        PreparedStatement insertLocationStatement = null;

        try{
            DataHandler.setCommitMode();
            DataHandler.setSavepoint();

            LocalDateTime creationtime = LocalDateTime.now();

            insertProductStatement = DataHandler.getPreparedStatement(INSERT_PRODUCTS,false);
            insertOrganisationStatement = DataHandler.getPreparedStatement(INSERT_ORGANISATIONS, false);
            insertLocationStatement = DataHandler.getPreparedStatement(INSERT_COORDINATES,false);

            insertProductStatement.setString(1, key);
            insertProductStatement.setString(2, product.getName());
            insertProductStatement.setString(3, String.valueOf(Timestamp.valueOf(creationtime)));
            insertProductStatement.setString(4, product.getUnitOfMeasure().toString());
            insertProductStatement.setDouble(5, product.getCoordinates().getX());
            insertProductStatement.setInt(6, product.getCoordinates().getY());
            insertProductStatement.setFloat(7, product.getPrice());
            insertProductStatement.setLong(8, dataUserManager.getUserIdByUsername(user));
            if (insertProductStatement.executeUpdate() == 0) throw new SQLException();
            long productId = 1;
            Statement s = DataHandler.getConnection().createStatement();
            ResultSet rs = s.executeQuery("select count(*) from products");
            if(rs.next()){
            productId = rs.getInt(1);}

            insertOrganisationStatement.setString(1, product.getManufacturer().getName());
            insertOrganisationStatement.setString(2, product.getManufacturer().getFullName());
            insertOrganisationStatement.setString(3, product.getManufacturer().getType().toString());
            insertOrganisationStatement.setLong(4, productId);
            if (insertOrganisationStatement.executeUpdate() == 0) throw new SQLException();
            long orgId = 1;
            Statement st = DataHandler.getConnection().createStatement();
            ResultSet rst = st.executeQuery("select count(*) from organisations");
            if(rst.next()) {
                orgId = rst.getInt(1);
            }

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
        } finally {
            DataHandler.closePreparedStatement(insertLocationStatement);
            DataHandler.closePreparedStatement(insertOrganisationStatement);
            DataHandler.closePreparedStatement(insertProductStatement);
            DataHandler.setNormalMode();
        }
        return false;
    }

    public Hashtable<String,Product> getCollection(){
        Hashtable<String,Product> products = new Hashtable<>();
        Map<Integer, Address> addressMap = new HashMap<>();
        Map<Long, Organization> orgMap = new HashMap<>();
        PreparedStatement preparedSelectAllProducts = null;
        PreparedStatement preparedSelectAllOrganisations = null;
        PreparedStatement preparedSelectAllLocations = null;
        try {
            preparedSelectAllProducts = DataHandler.getPreparedStatement(SELECT_ALL_PRODUCTS, false);
            preparedSelectAllOrganisations = DataHandler.getPreparedStatement(SELECT_ALL_ORGANISATIONS,false);
            preparedSelectAllLocations = DataHandler.getPreparedStatement(SELECT_ALL_COORDINATES,false);
            ResultSet resultSet = preparedSelectAllLocations.executeQuery();
            while (resultSet.next()) {
                Address adr = createLocation(resultSet);
                assert adr != null;
                addressMap.put(resultSet.getInt("organisation_id"),adr);
            }
            DataHandler.closePreparedStatement(preparedSelectAllLocations);
            resultSet.close();

            ResultSet resultSet1 = preparedSelectAllOrganisations.executeQuery();
            while (resultSet1.next()) {
                Organization org = createOrganisation(resultSet1, addressMap);
                assert org != null;
                orgMap.put(resultSet1.getLong("product_id"),org);
            }
            DataHandler.closePreparedStatement(preparedSelectAllOrganisations);
            resultSet1.close();

            ResultSet resultSet2 = preparedSelectAllProducts.executeQuery();
            while (resultSet2.next()) {
                Product product = createProduct(resultSet2, orgMap);
                assert product != null;
                products.put(resultSet2.getString("key"), product);
            }
            DataHandler.closePreparedStatement(preparedSelectAllOrganisations);
            resultSet1.close();
        } catch (SQLException exception) {
            System.out.println("Something went wrong with BD");
        } catch (NotUniqueFullName notUniqueFullName) {
            notUniqueFullName.printStackTrace();
        } catch (InvalidYCoordinate notUniqueFullName) {
            notUniqueFullName.printStackTrace();
        } catch (TooLargeFullName notUniqueFullName) {
            notUniqueFullName.printStackTrace();
        } catch (NegativePrice notUniqueFullName) {
            notUniqueFullName.printStackTrace();
        } finally {
            DataHandler.closePreparedStatement(preparedSelectAllProducts);
            DataHandler.closePreparedStatement(preparedSelectAllLocations);
            DataHandler.closePreparedStatement(preparedSelectAllOrganisations);
        }
        return products;
    }

    public void deleteProductByUser(User user) {
        PreparedStatement preparedSelectProductByUser = null;
        PreparedStatement preparedDeleteProductByUser = null;
        PreparedStatement preparedDeleteOrganisationByProductId = null;
        PreparedStatement preparedSelectOrganisationsByProductId = null;
        PreparedStatement preparedDeleteLocationByOrganisationId = null;
        try {
            DataHandler.setCommitMode();
            DataHandler.setSavepoint();
            ArrayList<Long> ids = new ArrayList<>();
            preparedSelectProductByUser = DataHandler.getPreparedStatement(SELECT_ALL_PRODUCTS_BY_USER_ID, false);
            preparedSelectProductByUser.setLong(1, dataUserManager.getUserIdByUsername(user));
            ResultSet rs = preparedSelectProductByUser.executeQuery();
            while (rs.next()){
                ids.add(rs.getLong("id"));
            }
            preparedDeleteProductByUser = DataHandler.getPreparedStatement(DELETE_PRODUCTS_BY_USER_ID, false);
            preparedDeleteProductByUser.setLong(1, dataUserManager.getUserIdByUsername(user));
            if (preparedDeleteProductByUser.executeUpdate() == 0) throw new SQLException();

            int i = 0;
            while(i <= ids.size()) {
                ArrayList<Long> orgIds = new ArrayList<>();
                preparedSelectOrganisationsByProductId = DataHandler.getPreparedStatement(SELECT_ORGANISATIONS_BY_PRODUCT_ID, false);
                preparedSelectOrganisationsByProductId.setLong(1, ids.get(i));
                ResultSet resultSet = preparedSelectOrganisationsByProductId.executeQuery();
                while(resultSet.next()){
                    orgIds.add(resultSet.getLong("id"));
                }

                preparedDeleteOrganisationByProductId = DataHandler.getPreparedStatement(DELETE_ORGANISATIONS_BY_PRODUCT_ID, false);
                preparedDeleteOrganisationByProductId.setLong(1, ids.get(i));
                if (preparedDeleteOrganisationByProductId.executeUpdate() == 0) throw new SQLException();
                i++;
                DataHandler.closePreparedStatement(preparedDeleteOrganisationByProductId);

                int j = 0;
                while(j <= orgIds.size()) {
                    preparedDeleteLocationByOrganisationId = DataHandler.getPreparedStatement(DELETE_COORDINATES_BY_ORGANISATION_ID, false);
                    preparedDeleteLocationByOrganisationId.setLong(1, orgIds.get(j));
                    if (preparedDeleteLocationByOrganisationId.executeUpdate() == 0) throw new SQLException();
                    j++;
                    DataHandler.closePreparedStatement(preparedDeleteLocationByOrganisationId);
                }
            }
            DataHandler.commit();

            System.out.println("Выполнен запрос DELETE_PRODUCTS_BY_ID.");
        } catch (SQLException exception) {
            exception.printStackTrace();
            DataHandler.rollback();
            System.out.println("Произошла ошибка при выполнении запроса DELETE_PRODUCTS_BY_USER_ID!");
        } finally {
            DataHandler.setNormalMode();
            DataHandler.closePreparedStatement(preparedSelectProductByUser);
            DataHandler.closePreparedStatement(preparedDeleteProductByUser);
            DataHandler.closePreparedStatement(preparedSelectOrganisationsByProductId);
        }
    }

    public boolean checkForRoots(long productId, User user){
        PreparedStatement preparedCheckForRoots = null;
        try{
            preparedCheckForRoots = DataHandler.getPreparedStatement(SELECT_PRODUCTS_BY_ID_AND_USER_ID, false);
            preparedCheckForRoots.setLong(1, productId);
            preparedCheckForRoots.setLong(2, dataUserManager.getUserIdByUsername(user));
            ResultSet rs = preparedCheckForRoots.executeQuery();
            return rs.next();
        }catch (SQLException e){
            System.out.println("Problema v BD a ne vo mne");
        }finally {
            DataHandler.closePreparedStatement(preparedCheckForRoots);
        }
        return false;
    }

    /*public void updateProductById(long productId, Product newproduct) {
        PreparedStatement preparedUpdateProductNameByIdStatement = null;
        PreparedStatement preparedUpdateProductPriceByIdStatement = null;
        PreparedStatement preparedUpdateProductTypeByIdStatement = null;
        PreparedStatement preparedUpdateProductXByIdStatement = null;
        PreparedStatement preparedUpdateProductYByIdStatement = null;
        PreparedStatement preparedUpdateCoordinatesByProductIdStatement = null;
        PreparedStatement preparedUpdateOrganisationByProductIdStatement = null;
        try {
            DataHandler.setCommitMode();
            DataHandler.setSavepoint();

            Statement ps = DataHandler.getConnection().createStatement();
            ResultSet rs = ps.executeQuery("SELECT * FROM products WHERE id = " + productId);
            Product product = createProduct(rs);

            preparedUpdateProductNameByIdStatement = DataHandler.getPreparedStatement(UPDATE_PRODUCTS_NAME_BY_ID, false);
            preparedUpdateProductPriceByIdStatement = DataHandler.getPreparedStatement(UPDATE_PRODUCTS_PRICE_BY_ID, false);
            preparedUpdateProductTypeByIdStatement = DataHandler.getPreparedStatement(UPDATE_PRODUCTS_TYPE_BY_ID, false);
            preparedUpdateProductXByIdStatement = DataHandler.getPreparedStatement(UPDATE_PRODUCTS_X_BY_ID, false);
            preparedUpdateProductYByIdStatement = DataHandler.getPreparedStatement(UPDATE_PRODUCTS_Y_BY_ID, false);
            preparedUpdateCoordinatesByProductIdStatement = DataHandler.getPreparedStatement(UPDATE_COORDINATES_BY_ORGANISATION_ID, false);
            preparedUpdateOrganisationByProductIdStatement = DataHandler.getPreparedStatement(UPDATE_ORGANISATIONS_BY_PRODUCT_ID, false);


            preparedUpdateProductNameByIdStatement.setString(1, newproduct.getName());
            preparedUpdateProductNameByIdStatement.setLong(2, newproduct.getId());
            if (preparedUpdateProductNameByIdStatement.executeUpdate() == 0) throw new SQLException();
            System.out.println("Выполнен запрос UPDATE_PRODUCT_NAME_BY_ID.");

            preparedUpdateProductPriceByIdStatement.setFloat(1, newproduct.getPrice());
            preparedUpdateProductPriceByIdStatement.setLong(2, newproduct.getId());
            if (preparedUpdateProductPriceByIdStatement.executeUpdate() == 0) throw new SQLException();
            System.out.println("Выполнен запрос UPDATE_PRODUCT_NAME_BY_ID.");

            preparedUpdateProductTypeByIdStatement.setString(1,newproduct.getUnitOfMeasure().toString());
            preparedUpdateProductTypeByIdStatement.setLong(2, newproduct.getId());
            if (preparedUpdateProductTypeByIdStatement.executeUpdate() == 0) throw new SQLException();
            System.out.println("Выполнен запрос UPDATE_PRODUCT_NAME_BY_ID.");

            preparedUpdateProductXByIdStatement.setDouble(1, newproduct.getCoordinates().getX());
            preparedUpdateProductXByIdStatement.setLong(2, newproduct.getId());
            if (preparedUpdateProductXByIdStatement.executeUpdate() == 0) throw new SQLException();
            System.out.println("Выполнен запрос UPDATE_PRODUCT_NAME_BY_ID.");

            preparedUpdateProductYByIdStatement.setInt(1, newproduct.getCoordinates().getY());
            preparedUpdateProductYByIdStatement.setLong(2, newproduct.getId());
            if (preparedUpdateProductYByIdStatement.executeUpdate() == 0) throw new SQLException();
            System.out.println("Выполнен запрос UPDATE_PRODUCT_NAME_BY_ID.");

            preparedUpdateOrganisationByProductIdStatement.
            if (preparedUpdateMarineNameByIdStatement.executeUpdate() == 0) throw new SQLException();
            System.out.println("Выполнен запрос UPDATE_PRODUCT_NAME_BY_ID.");

            if (marineRaw.getCoordinates() != null) {
                preparedUpdateCoordinatesByMarineIdStatement.setDouble(1, marineRaw.getCoordinates().getX());
                preparedUpdateCoordinatesByMarineIdStatement.setFloat(2, marineRaw.getCoordinates().getY());
                preparedUpdateCoordinatesByMarineIdStatement.setLong(3, marineId);
                if (preparedUpdateCoordinatesByMarineIdStatement.executeUpdate() == 0) throw new SQLException();
                System.out.println("Выполнен запрос UPDATE_COORDINATES_BY_ORGANISATION_ID.");
            }
            if (marineRaw.getHealth() != -1) {
                preparedUpdateMarineHealthByIdStatement.setDouble(1, marineRaw.getHealth());
                preparedUpdateMarineHealthByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineHealthByIdStatement.executeUpdate() == 0) throw new SQLException();
                System.out.println("Выполнен запрос UPDATE_MARINE_HEALTH_BY_ID.");
            }
            if (marineRaw.getCategory() != null) {
                preparedUpdateMarineCategoryByIdStatement.setString(1, marineRaw.getCategory().toString());
                preparedUpdateMarineCategoryByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineCategoryByIdStatement.executeUpdate() == 0) throw new SQLException();
                System.out.println("Выполнен запрос UPDATE_MARINE_CATEGORY_BY_ID.");
            }
            if (marineRaw.getWeaponType() != null) {
                preparedUpdateMarineWeaponTypeByIdStatement.setString(1, marineRaw.getWeaponType().toString());
                preparedUpdateMarineWeaponTypeByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineWeaponTypeByIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_MARINE_WEAPON_TYPE_BY_ID.");
            }
            if (marineRaw.getMeleeWeapon() != null) {
                preparedUpdateMarineMeleeWeaponByIdStatement.setString(1, marineRaw.getMeleeWeapon().toString());
                preparedUpdateMarineMeleeWeaponByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineMeleeWeaponByIdStatement.executeUpdate() == 0) throw new SQLException();
                System.out.println("Выполнен запрос UPDATE_MARINE_MELEE_WEAPON_BY_ID.");
            }
            if (marineRaw.getChapter() != null) {
                preparedUpdateChapterByIdStatement.setString(1, marineRaw.getChapter().getName());
                preparedUpdateChapterByIdStatement.setLong(2, marineRaw.getChapter().getMarinesCount());
                preparedUpdateChapterByIdStatement.setLong(3, getChapterIdByMarineId(marineId));
                if (preparedUpdateChapterByIdStatement.executeUpdate() == 0) throw new SQLException();
                System.out.println("Выполнен запрос UPDATE_LOCATION_BY_ID.");
            }

            DataHandler.commit();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении группы запросов на обновление объекта!");
            DataHandler.rollback();
        } catch (NotUniqueFullName notUniqueFullName) {
            notUniqueFullName.printStackTrace();
        } catch (InvalidYCoordinate invalidYCoordinate) {
            invalidYCoordinate.printStackTrace();
        } catch (TooLargeFullName tooLargeFullName) {
            tooLargeFullName.printStackTrace();
        } catch (NegativePrice negativePrice) {
            negativePrice.printStackTrace();
        } finally {
            DataHandler.closePreparedStatement(preparedUpdateProductNameByIdStatement);
            DataHandler.closePreparedStatement(preparedUpdateProductPriceByIdStatement);
            DataHandler.closePreparedStatement(preparedUpdateProductTypeByIdStatement);
            DataHandler.closePreparedStatement(preparedUpdateProductXByIdStatement);
            DataHandler.closePreparedStatement(preparedUpdateProductYByIdStatement);
            DataHandler.closePreparedStatement(preparedUpdateCoordinatesByProductIdStatement);
            DataHandler.closePreparedStatement(preparedUpdateOrganisationByProductIdStatement);
            DataHandler.setNormalMode();
        }
    }*/

}