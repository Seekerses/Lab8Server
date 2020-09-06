package productdata;
import exceptions.NegativePrice;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Product implements Serializable {

    private static long idCounter;
    private static final long serialVersionUID = 1337000022L;
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Float price; //Поле может быть null, Значение поля должно быть больше 0
    private UnitOfMeasure unitOfMeasure; //Поле не может быть null
    private Organization manufacturer; //Поле может быть null

    /**
     * Standart Product constructor
     * @param id It uses for setting id of Product when reading a table from files
     * @param name Name of Product
     * @param coordinates Coordinates of Product
     * @param price Price of Product
     * @param unitOfMeasure Units of measure of Product
     * @param organization Organization that produces a Product
     * @param data It uses for setting creation date of Product when reading a table from files
     * @throws NullPointerException If you are trying to create a Product with null name, coordinates or units of measure arguments
     * @throws NegativePrice If you are trying to create a Product with negative price
     */
    public Product(Long id,String name, Coordinates coordinates, Float price, UnitOfMeasure unitOfMeasure, Organization organization,LocalDateTime data) throws NullPointerException,NegativePrice {
        if (id == null ) {
            idCounter++;
            this.id = idCounter;
        }
        else
        {
            this.id = id;
        }
        if (name == null || coordinates == null || unitOfMeasure == null) throw new NullPointerException();
        if (price != null && price < 0) throw new NegativePrice();
        this.name = name;
        this.price = price;
        this.coordinates = coordinates;
        if (data == null) {
            creationDate = LocalDateTime.now();
        }
        else {
            creationDate = data;
        }
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer = organization;
    }

    /**
     * Returns a fields value in format for saving
     * @return String in format for saving
     */
    public String out(){
        return id.toString() + ";" + name + ";" + coordinates.output() + ";" +
                (manufacturer == null ? ";;;;;;;":manufacturer.getId() + ";" +
                        (manufacturer.getPostalAddress() == null ? ";;;" : manufacturer.getPostalAddress().toString())
                        + ";" + manufacturer.getName() + ";" + manufacturer.getFullName() + ";"
                        + (manufacturer.getType() == null?"":manufacturer.getType().toString())) + ";" +
                unitOfMeasure.toString() + ";" + creationDate.toString() + ";" + (price == null ? "":price.toString());
    }

    /**
     * Name getter
     * @return Name
     */
    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ("ID : " + id + " \nProduct name: " + name + " \nCoordinates: "
                + coordinates.toString() + " \nCreation date : " + creationDate.toString()
                + " \nPrice : " + (price == null ? "not indicated":price.toString())+ " \nUnits of measure : "
                + unitOfMeasure.toString() + " \nManufactured by : " + (manufacturer == null ? "not indicated" : manufacturer.toString())+"\n\n");
    }

    /**
     * Id getter
     * @return Id
     */
    public Long getId() {
        return id;
    }

    /**
     * Organization getter
     * @return Organization
     */
    public Organization getManufacturer() {
        return manufacturer;
    }

    /**
     * Price getter
     * @return Price
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Coordinates getter
     * @return Coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Creation Date getter
     * @return Creation Date
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * UnitsOfMeasure getter
     * @return Units of Measure
     */
    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public int compareTo(Product product) {
        return id.intValue() - product.getId().intValue();
    }

    public static  void setIdCounter(int value){
        idCounter = value;
    }
}