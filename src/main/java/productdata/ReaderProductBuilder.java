package productdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Class that controls a building of Product using console
 */
public class ReaderProductBuilder {
    /**
     * This method creates a Product through a console input
     * @param reader Reader that will interact with user
     * @return Builded Product
     */
    public static Product buildProduct(BufferedReader reader){
        String name = null;
        Float price = null;
        UnitOfMeasure unitOfMeasure = null;
        Organization manufacturer = null;
        System.out.println("Enter product name:");
        while (name == null) {
            try {
                name = reader.readLine();
            }
            catch (IOException e){
                System.out.println(" Invalid value.");
            }
            if ("".equals(name)) {
                System.out.println("Error: empty value entered. Enter product name:");
                name = null;
            }
        }
        System.out.println("Enter price ?\n yes \n any words to no:");
        try {
            if ("yes".equals(reader.readLine())) {
                while (price == null) {
                    try {
                        price = Float.parseFloat(reader.readLine());
                    } catch (Exception e) {
                        System.out.println("Invalid value");
                    }
                    if (price != null && price < 0) {
                        System.out.println("Error: price cannot be negative. Enter price: ");
                        price = null;
                    }
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        LocalDateTime creationDate = LocalDateTime.now();
        Coordinates coordinates = buildCoordinates(reader);
        System.out.println("Enter units of measure: \n" +
                "    KILOGRAMS,\n" +
                "    CENTIMETERS,\n" +
                "    PCS,\n" +
                "    LITERS,\n" +
                "    MILLILITERS;");

        while (unitOfMeasure == null) {
            try {
                unitOfMeasure = UnitOfMeasure.valueOf(reader.readLine().toUpperCase());
            }
            catch (Exception e ){
                System.out.println("Error: entered invalid value. Enter units of measure:");
            }
        }
        System.out.println("Enter manufacturer info ? \n yes \n any words to no");
        try {
            if(reader.readLine().equals("yes")) {
                while (true) {
                    System.out.println("Assign from existing ? \n yes \n any words to no");
                    try {
                        if (reader.readLine().equals("yes")) {
                            String key;
                            System.out.println("Choose from the list: ");
                            UniqueController.getOrgTable().forEach((k, v) -> System.out.println(k));
                            try {
                                key = reader.readLine();
                            } catch (Exception e) {
                                System.out.println("Error: invalid value.");
                                continue;
                            }
                            try {
                                manufacturer = UniqueController.get(key);
                                break;
                            }
                            catch (Exception e){
                                System.out.println("Such a company is not listed");
                            }
                        }
                        else {
                            manufacturer = buildOrganization(reader);
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error: invalid value.");
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("Error: invalid value.");
        }
        try {
            return new Product(null, name, coordinates, price, unitOfMeasure, manufacturer, creationDate);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static Coordinates buildCoordinates(BufferedReader reader){
        double x;
        Integer y = null;
        System.out.println("Enter product coordinates. Enter x coordinate:");
        while (true){
            try {
                x = Double.parseDouble(reader.readLine());
                break;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Error: invalid value.");
            }
        }
        System.out.println("Enter y coordinate:");
        while (y == null) {
            try {
                y = Integer.parseInt(reader.readLine());
            }
            catch (Exception e) {
                System.out.println("Error: invalid value.");
                continue;
            }
            if (y < -150){
                System.out.println("Error: the number is out of range. Enter the coordinate y> -150:");
                y = null;
            }
        }
        try {
            return new Coordinates(x, y);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static Organization buildOrganization(BufferedReader reader){
        String name = null;
        String fullName = null;
        OrganizationType type = null;
        Address postalAddress = null;
        try {
            System.out.println("Enter organization name:");
            while (name == null) {
                try {
                    name = reader.readLine();
                }
                catch (IOException e){
                    System.out.println("Error: invalid value. Enter organization name:");
                }
                if (name == null || name.equals("")) {
                    System.out.println("Error: empty value. Enter organization name:");
                }
            }
            System.out.println("Enter the name of the organization");
            while (fullName == null) {
                while(fullName == null) {
                    try {
                        fullName = reader.readLine();
                    } catch (IOException e) {
                        System.out.println("Error: invalid value. Enter the name of the organization");
                    }
                }
                if (fullName.length() > 1404) {
                    System.out.println("Error: The name is too long. Enter the name of the organization:");
                    fullName = null;
                }
                if (UniqueController.check(fullName)){
                    System.out.println("The full name of the organization must be unique.");
                    fullName = null;
                }
            }
            System.out.println("Enter type of organization ? \n yes \n any words to no");
            try {
                if(reader.readLine().equals("yes")) {
                    System.out.println("Enter type of organization:\n" +
                            " PUBLIC,\n" +
                            " TRUST,\n" +
                            " PRIVATE_LIMITED_COMPANY");
                    while(type == null) {
                        try {
                            type = OrganizationType.valueOf(reader.readLine().toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println(" Invalid value.");
                            type = null;
                        }
                    }
                }
            }
            catch (IOException e){
                System.out.println("Error: invalid value.");
            }
            System.out.println("Enter address? \n yes \n any words to no");
            try {
                if(reader.readLine().equals("yes")) {
                    postalAddress = buildAddress(reader);
                }
            }
            catch (IOException e){
                System.out.println("Error: invalid value.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            Organization org = new Organization(null,name,fullName,type,postalAddress);
            UniqueController.put(fullName,org);
            return org;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static Address buildAddress(BufferedReader reader){
        String street = null;
        Location town = null;
        System.out.println("Enter street: ");
        while (street == null) {
            try {
                street = reader.readLine();
            }
            catch (IOException e){
                System.out.println("Error: invalid value. Enter street:");
            }
            if (street == null) {
                System.out.println("Error: empty value. Enter street:");
            }
        }
        System.out.println("Enter a city ? \n yes \n any words to no");
        try {
            if(reader.readLine().equals("yes")) {
                town = buildLocation(reader);
            }
        }
        catch (IOException e){
            System.out.println("Error: invalid value.");
        }
        return new Address(street,town);
    }

    private static Location buildLocation(BufferedReader reader){
        Long x = null;
        int y = 0;
        Long z = null;
        try {
            System.out.println("Enter the coordinates of the organization. Enter the x coordinate:");
            while(x == null) {
                try {
                    x = Long.parseLong(reader.readLine());
                }
                catch (NumberFormatException e){
                    System.out.println("Error: invalid value. Enter the x coordinate:");
                    x = null;
                }
            }
            System.out.println("Enter y coordinate:");
            while (true){
                try {
                    y = Integer.parseInt(reader.readLine());
                    break;
                } catch (NumberFormatException | IOException e) {
                    System.out.println("Error: invalid value.");
                }
            }

            System.out.println("Enter z coordinate:");
            while(z == null) {
                try {
                    z = Long.parseLong(reader.readLine());
                }
                catch (NumberFormatException e){
                    System.out.println("Error: invalid value. Enter z coordinate:");
                    z = null;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new Location(x,y,z);
    }
}
