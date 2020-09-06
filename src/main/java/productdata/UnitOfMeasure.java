package productdata;

import java.io.Serializable;

/**
 * Class that represents all Units of Measure
 */
public enum UnitOfMeasure implements Serializable {
    KILOGRAMS,
    CENTIMETERS,
    PCS,
    LITERS,
    MILLILITERS;

    private static final long serialVersionUID = 1337000023L;
}