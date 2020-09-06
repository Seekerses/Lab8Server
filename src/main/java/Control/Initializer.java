package Control;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import Exceptions.NotUniqueFullName;
import productdata.*;

/**
 * This class upload a table of Products from files(files must be correctly filled)
 */
public class Initializer {
    /**
     * This method fills the table with Product that creates using the csv file
     * @param table Table, which will filled
     * @param file csv File
     */
    public static void init(TableManager table, File file) {
        try {
            if(file != null && !file.canRead()) throw new IllegalAccessException();
            if( file == null || file.length() == 0){
                table.setCreationDate(LocalDateTime.now());
                System.out.println("Initializing complete...");
                return;
            }
            FileReader fileReader = new FileReader(file);
            String line;
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int maxIDpr = 1;
            int maxIDorg = 1;
            while ((line = bufferedReader.readLine()) != null) {
                if(line.split(";").length >1){
                    if(Integer.parseInt(line.split(";")[1]) > maxIDpr){
                        maxIDpr =Integer.parseInt(line.split(";")[1]);
                    }
                    if (!line.split(";")[5].equals("") && Integer.parseInt(line.split(";")[5]) > maxIDorg){
                        maxIDorg = Integer.parseInt(line.split(";")[5]);
                    }
                }
            }
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            Product.setIdCounter(maxIDpr);
            Organization.setOrgId(maxIDorg);
            line = bufferedReader.readLine();
            if (line.contains(";")){
                table.setCreationDate(LocalDateTime.now());
                Product product = build(line.split(";"));
                if (product != null ) {
                    try {
                        table.put(line.split(";")[0], product);
                    }
                    catch (NullPointerException e){
                        System.out.println("Key is null, please try again with valid key...");
                    }
                }
            }
            else {
                table.setCreationDate("".equals(line) ? LocalDateTime.now() : LocalDateTime.parse(line));
            }

            while ((line = bufferedReader.readLine()) != null) {
                Product product = build(line.split(";"));
                if (product != null ) {
                    try {
                        table.put(line.split(";")[0], product);
                    }
                    catch (NullPointerException e){
                        System.out.println("Key is null, please try again with valid key...");
                    }
                }
            }
            bufferedReader.close();
            System.out.println("Initializing complete...\n");

        }
        catch (IllegalAccessException a) {
            System.out.println("The file cannot be accessed, enter the available file address, or type \"null\" to create new empty table:");
            changeAddress(table);
        }
        catch (Exception e) {
            System.out.println("The file contains an error or it have wrong format, enter the correct file address:");
            changeAddress(table);
        }
    }

    /**
     * Method that response for changing file address if source file contents problems
     * @param table Table that connected to file
     */
    private static void changeAddress(TableManager table) {
        try {
            System.out.println("(Enter \"exit\" to exit)");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();
            if("null".equals(line)) init(table , null);
            if(!"exit".equals(line)) init(table , new File(line));
            else System.exit(0);
        }
        catch (IOException ex){
            System.out.println("Invalid character sequence.");
            System.out.println("Enter valid address (Enter \"exit\" to exit):");
            changeAddress(table);
        }
    }

    /**
     * Creates the new Product using the Strings array where all arguments are located in correct positions(as in the saved file)
     * @param str Strings array of arguments for building Product
     * @return Built Product
     */
    public static Product build(String[] str){
        if (str.length < 15){
            try {
                String[] temp = Arrays.copyOfRange(str, 0, 1);
                String[] temp2 = Arrays.copyOfRange(str, 1, 4);
                String[] temp3 = Arrays.copyOfRange(str, 4, 14);
                ArrayList<String> un = new ArrayList<>(Arrays.asList(temp));
                un.add("");
                un.addAll(Arrays.asList(temp2));
                un.add("");
                un.addAll(Arrays.asList(temp3));
                str = un.toArray(new String[0]);
            }
            catch (Exception e){
                System.out.println("Wrong arguments. Argument should be in format \"key productName xCoordinate yCoordinate " +
                        "orgStreet xOrgCoordinate yOrgCoordinate zOrgCoordinate organizationName organizationFullName orgType UnitsOfMeasure creationDate price\"" +
                        "to create null value use ;. In files separator is \";\" instead of \" \" and null value is \"\". Unformatted product will be ignored.");
                return null;
            }
        }
        Location location;
        try {
            if (!"".equals(str[7] + str[9])) {
                location = new Location(Long.parseLong(str[7]), "".equals(str[8]) ? 0 : Integer.parseInt(str[8]), Long.parseLong(str[9]));
            } else {
                location = null;
            }
            Address adr;
            if (!"".equals(str[6])) {
                adr = new Address(str[6], location);
            } else {
                adr = null;
            }
            Product product = null;
            try {
                Coordinates coord = new Coordinates("".equals(str[3]) ? 0 : Double.parseDouble(str[3]), Integer.parseInt(str[4]));
                Organization org;
                if (!"".equals(str[11])) {
                    try {
                        org = new Organization("".equals(str[5]) ? null : Integer.parseInt(str[5]), str[10], str[11], "".equals(str[12]) ? null : OrganizationType.valueOf(str[12]), adr);
                    } catch (NotUniqueFullName e) {
                        Organization comp = UniqueController.getOrgTable().get(str[11]);
                        if (comp.getPostalAddress().equals(adr) && comp.getName().equals(str[10])
                                && comp.getType().equals("".equals(str[12]) ? null : OrganizationType.valueOf(str[12])))
                            org = comp;
                        else throw new NotUniqueFullName();
                    }
                } else {
                    org = null;
                }
                product = new Product("".equals(str[1]) ? null : Long.parseLong(str[1]), str[2], coord, str.length < 16 ? null : Float.parseFloat(str[15]), UnitOfMeasure.valueOf(str[13]), org, "".equals(str[14]) ? null : LocalDateTime.parse(str[14]));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return product;
        }
        catch (Exception e){
            System.out.println("Wrong arguments. Argument should be in format \"key productName xCoordinate yCoordinate " +
                    "orgStreet xOrgCoordinate yOrgCoordinate zOrgCoordinate organizationName organizationFullName orgType UnitsOfMeasure creationDate price\"" +
                    "to create null value use ;. In files separator is \";\" instead of \" \" and null value is \"\". Unformatted product will be ignored.");
            return null;
        }
    }
}