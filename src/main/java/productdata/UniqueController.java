package productdata;
import java.util.Hashtable;

/**
 * This class controls a table of all created Organization, which helps to keep uniqueness of their Full Names
 */
public class UniqueController {
    /**
     * Table of all existing organizations
     */
    private static Hashtable<String, Organization> orgTable = new Hashtable<>();

    /**
     * Puts an Organization in the table
     * @param index Key of Org in the table(this must be full name of Org)
     * @param org Organization
     */
    public static void put(String index, Organization org){
        orgTable.put(index, org);
    }

    /**
     * Check if Organization with index full name already exists
     * @param index Full name of Org
     * @return true if Org already exists, false if not
     */
    public static boolean check(String index){
        return orgTable.containsKey(index);
    }

    /**
     * Returns a Org from table
     * @param index Full name of Org
     * @return Organization
     */
    public static Organization get(String index){
        return orgTable.get(index);
    }

    /**
     * Returns a table of Organizations
     * @return Hashtable of Organizations
     */
    public static Hashtable<String, Organization> getOrgTable(){
        return orgTable;
    }
}